#!/usr/bin/env python3
"""
Gate for the agent skills under .claude/skills/.

    python3 script/check_skills.py             # structure + links
    python3 script/check_skills.py --install   # ...and exercise both install paths
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

Three groups, matching the three things that can rot independently:

  structure   the frontmatter a host parses, and the body it loads
  wiring      every .claude/skills path named by the site page, CLAUDE.md or an
              INSTALL.md still resolves — this is what catches a renamed
              reference file silently breaking the published page's links
  install     the documented installs (a `cp -r`, and the zip upload) actually
              produce a tree that passes the structure checks again, which is
              the only way to prove a skill is self-contained
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

# Anthropic's limit on the field a host matches a request against. A description
# over it is truncated, and a truncated description matches badly.
MAX_DESCRIPTION = 1024

# Files that name a skill path and go stale when a skill is renamed.
WIRING_SOURCES = ["CLAUDE.md", "site/pages/skills.html"]

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

    This is the check that earns the file. site/pages/skills.html links each
    reference file by name; renaming one leaves the skill perfectly valid and
    the published page pointing at four GitHub 404s, and nothing else in the
    build can see it — e2e-check.js only resolves links that are local files,
    and these are absolute github.com URLs by necessity.
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

    rep.section("self-contained")
    for skill in skills:
        check_self_contained(rep, skill)

    rep.section("wiring")
    check_wiring(rep, skills)

    if args.install:
        rep.section("install")
        for skill in skills:
            check_install(rep, skill)

    print(f"\n{rep.passed} passed, {rep.failed} failed\n")
    return 1 if rep.failed else 0


if __name__ == "__main__":
    sys.exit(main())
