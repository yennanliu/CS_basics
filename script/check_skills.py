#!/usr/bin/env python3
"""
Gate for the agent skills under .claude/skills/.

    python3 script/check_skills.py             # structure + format + links
    python3 script/check_skills.py --install   # ...and exercise both install paths
    python3 script/check_skills.py --run       # ...and RUN the commands they document
    python3 script/check_skills.py --verbose   # list every path that was resolved

A skill is markdown, so nothing type-checks it and nothing runs it — a skill with
a malformed frontmatter fence still *looks* fine in a diff and in the rendered
file, and only misbehaves at load time, where the failure is silent: the host
falls back to treating the whole file as body and the skill is never matched.
That is not hypothetical here. `add-time-space/SKILL.md` shipped without its
`---` fences, so its advertised description was the literal text
"name: add-time-space" until this file was written.

The checks are here rather than inside the workflow for the reason given in
CLAUDE.md about e2e-check.js: a rule that only exists in YAML cannot be run
before pushing, and a gate nobody can run locally is a gate people route around.

Five groups, matching the five things that can rot independently:

  structure   the frontmatter a host parses, and the body it loads
  format      the house shape the recipe skills share — the steps numbered and
              contiguous, an Invocation line naming the right command, a Do not
              list, a worked example, every fence tagged. A recipe that drops
              its steps section still parses perfectly and is still useless
  wiring      every .claude/skills path named by the site page, CLAUDE.md or an
              INSTALL.md still resolves — this is what catches a renamed
              reference file silently breaking the published page's links
  install     the documented installs (a `cp -r`, and the zip upload) actually
              produce a tree that passes the structure checks again, which is
              the only way to prove a skill is self-contained
  run         the commands a skill tells an agent to type are executed here, for
              real, against this repo. Everything above proves the *file* is
              well-formed; only this proves the *recipe* still works. A renamed
              script or a changed flag leaves a skill that validates green and
              then fails on the first line the agent runs

`--run` executes only what it can show is safe: a command carrying a
<placeholder> is a template and is skipped, and anything matching MUTATING is
skipped by name because it writes a tracked file, reaches the network or never
returns. Both kinds are reported with the reason, so the count of what actually
ran is visible rather than assumed.

It is not quite read-only: `node site/build-review-plan.js` regenerates
`_site/data/progress.json`, creating `_site/` if it is absent. That tree is
build output and is gitignored, so it is left runnable on purpose — it is the
gate the practice-log recipe actually depends on. Nothing else a run touches is
tracked.
"""

import argparse
import os
import re
import shutil
import subprocess
import sys
import tempfile
import zipfile
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SKILLS_DIR = ROOT / ".claude" / "skills"
# The skills document their commands in ```bash fences, so --run honours that
# rather than whatever /bin/sh happens to be.
BASH = shutil.which("bash") or "/bin/bash"

# Anthropic's limit on the field a host matches a request against. A description
# over it is truncated, and a truncated description matches badly.
MAX_DESCRIPTION = 1024

# Files that name a skill path and go stale when a skill is renamed.
WIRING_SOURCES = ["CLAUDE.md", "site/pages/skills.html", "site/pages/lc-python.html",
                  "site/pages/lc-java.html", "site/pages/lc-cheatsheet.html",
                  "site/pages/lc-log.html", "site/pages/lc-again.html",
                  "site/pages/lc-zh-translate.html", "site/pages/lc-algo-demo.html",
                  "site/pages/lc-site-data.html", "site/pages/lc-faq-add.html"]

NAME_RE = re.compile(r"^[a-z0-9]+(?:-[a-z0-9]+)*$")
FRONTMATTER_RE = re.compile(r"\A---\n(.*?)\n---\n", re.S)
# A `.claude/skills/...` path as it appears in prose, a shell command or an href.
SKILL_PATH_RE = re.compile(r"\.claude/skills/[A-Za-z0-9_./-]*")
# `references/foo.md` in backticks, and [text](relative/path.md) links. Both
# require a `/`: a bare `SKILL.md` or `GEMINI.md` in prose is the *name* of a
# file, not a pointer to one — INSTALL.md names GEMINI.md and AGENTS.md as files
# the reader will create, and neither is meant to exist here.
BACKTICK_PATH_RE = re.compile(r"`([A-Za-z0-9_][A-Za-z0-9_.-]*(?:/[A-Za-z0-9_.-]+)+\.md)`")
MD_LINK_RE = re.compile(r"\]\(([^)#][^)]*)\)")
FENCE_RE = re.compile(r"^```.*?^```", re.S | re.M)


class Report:
    """PASS/FAIL lines and a tally, in the shape site/e2e-check.js prints."""

    def __init__(self, verbose=False):
        self.passed = 0
        self.failed = 0
        self.verbose = verbose

    def section(self, title):
        print(f"\n== {title} ==")

    def check(self, name, cond, detail=""):
        if cond:
            self.passed += 1
        else:
            self.failed += 1
        suffix = f"  — {detail}" if detail else ""
        print(f"  {'PASS' if cond else 'FAIL'}  {name}{suffix}")
        return cond

    def info(self, text):
        if self.verbose:
            print(f"  INFO  {text}")


def parse_frontmatter(text):
    """Return (fields, error). Deliberately not a YAML parser.

    A skill's frontmatter is a flat block of `key: value` lines, and the only
    values in play are plain scalars. Hand-rolling it keeps this script on the
    standard library, which is what lets it run on a clean checkout with no
    install step — the same reason site/build.sh never touches the network.
    """
    match = FRONTMATTER_RE.match(text)
    if not match:
        return None, "no --- fenced frontmatter at the top of the file"

    fields = {}
    for line in match.group(1).split("\n"):
        if not line.strip() or line.lstrip().startswith("#"):
            continue
        if line[0].isspace():
            return None, f"indented frontmatter line (block scalars are not read by hosts): {line.strip()[:40]}"
        if ":" not in line:
            return None, f"frontmatter line is not `key: value`: {line[:40]}"
        key, value = line.split(":", 1)
        fields[key.strip()] = value.strip()
    return fields, None


def body_of(text):
    match = FRONTMATTER_RE.match(text)
    return text[match.end():] if match else text


def check_structure(rep, skill_dir, label=None):
    """The frontmatter a host parses and the body it loads. Returns ok."""
    label = label or skill_dir.name
    md = skill_dir / "SKILL.md"

    if not rep.check(f"{label}: SKILL.md exists", md.is_file()):
        return False

    text = md.read_text(encoding="utf-8")
    fields, error = parse_frontmatter(text)
    if not rep.check(f"{label}: frontmatter parses", fields is not None, error or ""):
        return False

    name = fields.get("name", "")
    description = fields.get("description", "")

    ok = True
    ok &= rep.check(f"{label}: has a name", bool(name))
    ok &= rep.check(f"{label}: name is kebab-case", bool(NAME_RE.match(name)), name)
    # A host addresses the skill by its `name`, but a user and every pointer in
    # the repo address it by its directory. If the two disagree, one of them is
    # wrong and nothing says which.
    ok &= rep.check(f"{label}: name matches the directory", name == skill_dir.name,
                    f"{name!r} vs {skill_dir.name!r}")
    ok &= rep.check(f"{label}: has a description", bool(description))
    ok &= rep.check(f"{label}: description fits in {MAX_DESCRIPTION} chars",
                    len(description) <= MAX_DESCRIPTION, f"{len(description)} chars")
    # The description is the only thing a host reads when deciding whether the
    # skill is relevant, so it has to say when to use it, not just what it is.
    ok &= rep.check(f"{label}: description is a sentence, not a name echo",
                    description.lower() != name.replace("-", " ").lower()
                    and not description.startswith("name:"),
                    description[:48])

    tools = fields.get("allowed-tools")
    if tools is not None:
        ok &= rep.check(f"{label}: allowed-tools is a comma-separated list",
                        all(part.strip() for part in tools.split(",")), tools)

    body = body_of(text)
    ok &= rep.check(f"{label}: body is non-empty", len(body.strip()) > 200,
                    f"{len(body.strip())} chars")
    ok &= rep.check(f"{label}: code fences are balanced",
                    body.count("\n```") % 2 == 0)
    return bool(ok)


def check_self_contained(rep, skill_dir, label=None):
    """Every path the skill names is inside the skill, and resolves."""
    label = label or skill_dir.name
    ok = True

    files = [p for p in skill_dir.rglob("*") if p.is_file()]
    ok &= rep.check(f"{label}: no symlinks", not any(p.is_symlink() for p in skill_dir.rglob("*")))

    # An absolute path bakes in one machine's layout, so the skill works for the
    # author and silently misfires for everyone who installs it.
    absolute = []
    referenced = set()
    for path in files:
        if path.suffix != ".md":
            continue
        text = path.read_text(encoding="utf-8")
        for m in re.finditer(r"(?<![\w.~])/(?:Users|home)/[A-Za-z0-9._-]+", text):
            absolute.append(f"{path.relative_to(skill_dir)} → {m.group(0)}")

        # Relative targets, from both spellings the skill files use. Fenced
        # blocks are stripped first: a skill that teaches markdown is full of
        # `[Title](URL)` placeholders, and they are examples, not links.
        prose = FENCE_RE.sub("", text)
        targets = {m.group(1) for m in BACKTICK_PATH_RE.finditer(prose)}
        targets |= {m.group(1) for m in MD_LINK_RE.finditer(prose)
                    if "/" in m.group(1) and not re.match(r"^[a-z][a-z0-9+.-]*:", m.group(1))}
        for target in sorted(targets):
            # Two bases are legitimate: a sibling inside the skill, and a repo
            # path — a skill may point at system_design/00_template.md, which is
            # only meaningful from the repo root.
            local = (path.parent / target).resolve()
            resolved = local if local.exists() else (ROOT / target).resolve()
            if local.exists():
                referenced.add(local)
            ok &= rep.check(f"{label}: {path.name} → {target} resolves", resolved.exists())
            rep.info(f"{path.relative_to(skill_dir)} → {target}")

    ok &= rep.check(f"{label}: no absolute home paths", not absolute, "; ".join(absolute[:3]))

    # A reference file nothing points at is a file the host will never load —
    # dead weight in the repo that reads like shipped content.
    orphans = [p.relative_to(skill_dir) for p in files
               if p.suffix == ".md" and p.name not in ("SKILL.md", "INSTALL.md")
               and p.resolve() not in referenced]
    ok &= rep.check(f"{label}: every reference file is referenced", not orphans,
                    ", ".join(str(o) for o in orphans))
    return bool(ok)


def check_wiring(rep, skills):
    """Every .claude/skills path named outside the skill still resolves.

    This is the check that earns the file. Each skill's page under site/pages/
    links its files by name; renaming one leaves the skill perfectly valid and
    the published page pointing at GitHub 404s, and nothing else in the build
    can see it — e2e-check.js only resolves links that are local files, and
    these are absolute github.com URLs by necessity.
    """
    sources = list(WIRING_SOURCES)
    sources += [str((d / "INSTALL.md").relative_to(ROOT)) for d in skills
                if (d / "INSTALL.md").is_file()]

    ok = True
    for source in sources:
        path = ROOT / source
        if not rep.check(f"{source} exists", path.is_file()):
            ok = False
            continue
        text = path.read_text(encoding="utf-8")
        broken = []
        seen = 0
        for m in SKILL_PATH_RE.finditer(text):
            target = m.group(0).rstrip("./,;:)\"'`")
            # ~/.claude/skills is where a skill is installed TO, not a path in
            # this repo; the regex cannot see the ~ so filter on it here. Only
            # the ~ — a leading "/" is how the path appears inside the GitHub
            # blob URLs on the site page, which are the whole point of this
            # check, and skipping those made it pass on a rename it should have
            # caught.
            if text[max(0, m.start() - 1):m.start()] == "~":
                continue
            seen += 1
            if not (ROOT / target).exists():
                broken.append(target)
            else:
                rep.info(f"{source} → {target}")
        ok &= rep.check(f"{source}: every skill path resolves", not broken,
                        f"{seen} checked" + (f", broken: {', '.join(broken[:3])}" if broken else ""))
    return bool(ok)


# ── Format ───────────────────────────────────────────────────────────────────
#
# The recipe skills share a shape, and it is load-bearing rather than cosmetic:
# an agent reads the Invocation line to know how it is called, works down the
# numbered steps in order, and stops at the Do not list. A recipe that drops its
# steps section parses perfectly, loads perfectly, and is useless.
#
# `lc-coach` is deliberately exempt. It is a coach persona with modes, not a
# filing recipe with steps, so it has no Invocation line and no numbered run.
# The generic skills (code-refactor-master, markdown-doc-writer, …) predate the
# house shape and are left alone; they are not `lc-` skills and do not claim to
# be.
RECIPE_EXEMPT = {"lc-coach"}
REQUIRED_SECTIONS = ["## The steps", "## Do not", "## Worked example"]
STEP_RE = re.compile(r"^### (\d+)\.\s", re.M)
INVOCATION_RE = re.compile(r"^\*\*Invocation\*\*:\s*`(/[a-z0-9-]+)", re.M)
BARE_FENCE_RE = re.compile(r"^```\s*$", re.M)


def is_recipe(skill_dir):
    return skill_dir.name.startswith("lc-") and skill_dir.name not in RECIPE_EXEMPT


def check_format(rep, skill_dir, label=None):
    """The house shape the recipe skills share. Returns ok."""
    label = label or skill_dir.name
    md = skill_dir / "SKILL.md"
    if not md.is_file():
        return False
    text = md.read_text(encoding="utf-8")
    body = body_of(text)
    # Headings and fences inside a fenced block are examples, not structure:
    # lc-faq-add shows a whole FAQ file skeleton, `## 1) <First section>` and
    # all, inside a ```markdown block.
    prose = FENCE_RE.sub("", body)

    ok = True
    ok &= rep.check(f"{label}: has an H1", bool(re.search(r"^# \S", prose, re.M)))

    # An opening fence with no language renders unhighlighted, and it is the
    # same mistake the cheatsheet style guide bans in the docs. Fence lines
    # toggle, so every other one is an opener; a *closing* fence carrying a
    # language is itself a symptom, since CommonMark does not allow one.
    openers = [m for m in re.finditer(r"^```(.*)$", body, re.M)][::2]
    untagged = [m for m in openers if not m.group(1).strip()]
    detail = f"{len(untagged)} untagged of {len(openers)}"
    if is_recipe(skill_dir):
        ok &= rep.check(f"{label}: every code fence is tagged", not untagged, detail)
    elif untagged:
        # markdown-doc-writer and system-architecture predate the house shape
        # and are not `lc-` skills. Reported, not enforced — turning this into a
        # failure would make an unrelated change to those two files the price of
        # running this gate.
        rep.info(f"{label}: {detail} (pre-dates the house shape, not enforced)")

    if not is_recipe(skill_dir):
        return bool(ok)

    for section in REQUIRED_SECTIONS:
        ok &= rep.check(f"{label}: has a '{section}' section", section in prose)

    # The invocation line is how a reader learns the command, and the command is
    # the directory name — so a renamed directory that leaves this line behind
    # documents a command that does not exist.
    inv = INVOCATION_RE.search(prose)
    ok &= rep.check(f"{label}: has an Invocation line", inv is not None)
    if inv:
        ok &= rep.check(f"{label}: Invocation names its own command",
                        inv.group(1) == f"/{skill_dir.name}",
                        f"{inv.group(1)} vs /{skill_dir.name}")

    # Steps are worked in order, so a gap or a repeat is an instruction to skip
    # one. Numbering from 1 and counting up is the whole contract.
    steps = [int(n) for n in STEP_RE.findall(prose)]
    ok &= rep.check(f"{label}: has numbered steps", bool(steps), f"{len(steps)} steps")
    if steps:
        ok &= rep.check(f"{label}: steps run 1..n with no gap or repeat",
                        steps == list(range(1, len(steps) + 1)),
                        ", ".join(str(n) for n in steps))

    # The Do not list is the part an agent scans when it is about to do
    # something irreversible, so it is a list, not a paragraph.
    do_not = prose.split("## Do not", 1)[1].split("\n## ", 1)[0] if "## Do not" in prose else ""
    ok &= rep.check(f"{label}: Do not is a list of ❌ items",
                    do_not.count("- ❌") >= 3, f"{do_not.count('- ❌')} items")
    return bool(ok)


# ── Referenced repo paths ────────────────────────────────────────────────────
#
# A recipe is a list of commands over this repo's files. When one of those files
# is renamed, nothing points at the skill — the skill points at the file — so
# every check above stays green and the recipe breaks on the line that names it.
#
# Only paths that are complete are checked: a segment containing <placeholder>
# is a template the agent fills in, and `leetcode_python/<Dir>/<slug>.py` cannot
# be resolved here by design.
REPO_PATH_RE = re.compile(
    r"(?<![\w./-])((?:script|site|data|doc|algo_demo|i18n)/[A-Za-z0-9_./<>-]*"
    r"[A-Za-z0-9_](?:\.[A-Za-z0-9]+)?)")


def repo_paths_in(text):
    """Complete repo paths named anywhere in the skill's fenced commands."""
    found = set()
    for block in re.finditer(r"^```(?:bash|text)?\n(.*?)^```", text, re.S | re.M):
        for match in REPO_PATH_RE.finditer(block.group(1)):
            path = match.group(1).rstrip(".,:;)")
            if "<" in path or ">" in path or "*" in path or "{" in path:
                continue
            if path.endswith("/"):
                continue
            found.add(path)
    return found


def check_referenced_paths(rep, skill_dir, label=None):
    """Every complete repo path a skill's commands name still exists."""
    label = label or skill_dir.name
    md = skill_dir / "SKILL.md"
    if not md.is_file():
        return False
    paths = sorted(repo_paths_in(md.read_text(encoding="utf-8")))
    missing = []
    for path in paths:
        target = ROOT / path
        if target.exists():
            rep.info(f"{label} → {path}")
        else:
            missing.append(path)
    return rep.check(f"{label}: every repo path its commands name exists",
                     not missing,
                     f"{len(paths)} checked"
                     + (f", missing: {', '.join(missing[:3])}" if missing else ""))


# ── Run ──────────────────────────────────────────────────────────────────────
#
# Commands that write, reach the network, or never return. They are skipped by
# name rather than by guessing, and the reason is printed, because a silent skip
# is indistinguishable from a pass.
MUTATING = [
    ("--write", "writes the generated progress docs"),
    ("zh.js sync", "parks and rewrites the translation store"),
    ("get_again_problems.sh", "rewrites data/again_problems.txt"),
    ("fetch_problem_lists.py", "hits the network and rewrites the vendored lists"),
    ("http.server", "serves until interrupted"),
    ("build.sh", "rebuilds the whole _site tree; the site workflow already runs it"),
    ("npm test", "the site workflow already runs it"),
    ("e2e-check.js", "the site workflow already runs it"),
    ("git ", "mutates the working tree or reads history this gate does not own"),
    ("mvn ", "needs a maven install this gate does not assume"),
    ("cp -r", "an install path; --install already exercises it"),
    ("zip -r", "an install path; --install already exercises it"),
    ("curl", "reaches the network"),
    ("gemini", "invokes another agent"),
    ("cd ", "changes the directory the rest of the block assumes"),
    ("javac", "compiles a template path filled in per problem"),
    ("cat >", "writes a file"),
]

# A line that is part of a multi-line construct rather than a command of its
# own: a heredoc body, a continued node/python -e string, a markdown artefact.
CONTINUATION = ("import ", "const ", "public ", "System.", "print(", "spec =",
                "m = ", "s = ", "} }", "EOF", "PY", "<", "*", "|", "&")


def balanced(text):
    """Quotes closed, so the shell would see a complete command."""
    return text.count('"') % 2 == 0 and text.count("'") % 2 == 0


def runnable_commands(text):
    """-> ([command, ...], [(command, reason), ...]) from the bash fences.

    A `node -e "..."` or `python3 -c "..."` spans several lines, so lines are
    joined until the quotes balance. Splitting them per line hands /bin/sh half
    a string, which fails for a reason that has nothing to do with the recipe.
    """
    run, skip = [], []
    for block in re.finditer(r"^```bash\n(.*?)^```", text, re.S | re.M):
        pending = ""
        for raw in block.group(1).splitlines():
            line = raw.strip()
            if pending:
                pending += "\n" + raw
                if not balanced(pending):
                    continue
                line, pending = pending, ""
            elif not balanced(line):
                pending = raw
                continue
            if not line or line.strip().startswith("#"):
                continue
            if line.startswith(CONTINUATION) or line.endswith(("\\", "<<'EOF'", "<<'PY'")):
                continue
            # A trailing `# comment` is documentation, not part of the command.
            line = re.sub(r"\s+#\s.*$", "", line).strip()
            if not line:
                continue
            if "<" in line and ">" in line:
                skip.append((line, "template: fill in the <placeholder>"))
                continue
            reason = next((why for token, why in MUTATING if token in line), None)
            if reason:
                skip.append((line, reason))
                continue
            run.append(line)
        if pending:
            skip.append((pending.strip(), "unterminated quote: a heredoc, not a command"))
    return run, skip


def check_run(rep, skill_dir, timeout=180):
    """Execute the commands the skill documents that are safe to run. Returns ok."""
    md = skill_dir / "SKILL.md"
    if not md.is_file():
        return False
    label = skill_dir.name
    run, skip = runnable_commands(md.read_text(encoding="utf-8"))

    for command, reason in skip:
        rep.info(f"{label}: skipped `{command[:56]}` — {reason}")

    if not run:
        # Not a failure: lc-cheatsheet's and lc-faq-add's runnable lines are all
        # templates or writes. The point is that this is visible.
        rep.check(f"{label}: has commands to run", True,
                  f"0 runnable, {len(skip)} skipped")
        return True

    ok = True
    for command in run:
        try:
            # bash, not the default /bin/sh. The fences say ```bash and the
            # commands use bash features — `grep doc/cheatsheet/{a,b}.md` is
            # brace expansion. /bin/sh is bash-in-posix-mode on macOS and dash
            # on Ubuntu, so leaving this to the default passed locally and
            # failed in CI on exactly that line.
            proc = subprocess.run(command, shell=True, executable=BASH, cwd=ROOT,
                                  timeout=timeout, capture_output=True, text=True)
            rc = proc.returncode
            detail = f"exit {rc}"
            if rc != 0:
                tail = (proc.stderr or proc.stdout).strip().splitlines()
                detail += f": {tail[-1][:80]}" if tail else ""
        except subprocess.TimeoutExpired:
            rc, detail = -1, f"timed out after {timeout}s"
        ok &= rep.check(f"{label}: `{command[:56]}`", rc == 0, detail)
    rep.info(f"{label}: {len(run)} ran, {len(skip)} skipped")
    return bool(ok)


def check_install(rep, skill_dir):
    """Run the two documented installs and re-check what they produced.

    INSTALL.md offers a `cp -r` into ~/.claude/skills and a zip for the Claude
    app's upload. Both are only real if the tree that comes out the far end is
    still a valid skill, so each one is followed by the same structure checks
    against the copy — which is also what proves the skill is self-contained,
    since a copy has none of this repo around it.
    """
    ok = True
    with tempfile.TemporaryDirectory() as tmp:
        tmp = Path(tmp)

        # 1. The `cp -r ... ~/.claude/skills/` install.
        home = tmp / "home"
        dest = home / ".claude" / "skills"
        dest.mkdir(parents=True)
        shutil.copytree(skill_dir, dest / skill_dir.name)
        installed = dest / skill_dir.name
        rep.check(f"{skill_dir.name}: cp install lands the tree",
                  (installed / "SKILL.md").is_file(), str(installed.relative_to(tmp)))
        ok &= check_structure(rep, installed, label=f"{skill_dir.name} [copied]")
        ok &= check_self_contained(rep, installed, label=f"{skill_dir.name} [copied]")

        # 2. The zip the Claude app uploads. Built the way INSTALL.md says, from
        #    inside .claude/skills, so the archive carries the skill directory
        #    as its top-level entry — an archive of loose files unpacks into
        #    whatever directory it lands in and stops being a skill.
        archive = tmp / f"{skill_dir.name}.zip"
        with zipfile.ZipFile(archive, "w", zipfile.ZIP_DEFLATED) as zf:
            for path in sorted(skill_dir.rglob("*")):
                if path.is_file():
                    zf.write(path, Path(skill_dir.name) / path.relative_to(skill_dir))
        with zipfile.ZipFile(archive) as zf:
            names = zf.namelist()
            roots = {Path(n).parts[0] for n in names}
            ok &= rep.check(f"{skill_dir.name}: zip has a single top-level directory",
                            roots == {skill_dir.name}, ", ".join(sorted(roots)))
            unpacked = tmp / "unpacked"
            zf.extractall(unpacked)
        ok &= check_structure(rep, unpacked / skill_dir.name, label=f"{skill_dir.name} [zipped]")
        ok &= check_self_contained(rep, unpacked / skill_dir.name, label=f"{skill_dir.name} [zipped]")

        size_kb = archive.stat().st_size / 1024
        rep.info(f"{skill_dir.name}.zip is {size_kb:.1f} KB over {len(names)} files")
    return bool(ok)


def main():
    parser = argparse.ArgumentParser(description=__doc__,
                                     formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--install", action="store_true",
                        help="also exercise the copy and zip installs")
    parser.add_argument("--run", action="store_true",
                        help="also run the commands each skill documents (skips templates and writes)")
    parser.add_argument("--verbose", action="store_true",
                        help="list every path that was resolved")
    args = parser.parse_args()

    rep = Report(verbose=args.verbose)

    if not SKILLS_DIR.is_dir():
        print(f"no skills directory at {SKILLS_DIR}", file=sys.stderr)
        return 1
    skills = sorted(d for d in SKILLS_DIR.iterdir() if d.is_dir())

    rep.section("skills found")
    rep.check("at least one skill", bool(skills), ", ".join(d.name for d in skills))

    rep.section("structure")
    for skill in skills:
        check_structure(rep, skill)

    rep.section("format")
    for skill in skills:
        check_format(rep, skill)

    rep.section("referenced paths")
    for skill in skills:
        check_referenced_paths(rep, skill)

    rep.section("self-contained")
    for skill in skills:
        check_self_contained(rep, skill)

    rep.section("wiring")
    check_wiring(rep, skills)

    if args.install:
        rep.section("install")
        for skill in skills:
            check_install(rep, skill)

    if args.run:
        rep.section("run")
        for skill in skills:
            check_run(rep, skill)

    print(f"\n{rep.passed} passed, {rep.failed} failed\n")
    return 1 if rep.failed else 0


if __name__ == "__main__":
    sys.exit(main())
