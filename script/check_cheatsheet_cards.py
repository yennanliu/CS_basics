#!/usr/bin/env python3
"""Fail when a new or rewritten cheatsheet section is not card-shaped.

doc/cheatsheet/00_template.md defines two shapes for what goes inside a section:

  pattern card   ### Template N: <Pattern name> — LC <n>
                 **Recognise** -> **Key point** -> ```python (then java) -> **Complexity**
                 -> [**Dry run**] -> **Traps** -> [**Not this if**] -> **Drill**
  problem card   ### <Problem Name> — LC <n>
                 > **Key point** — ...   (the first line, before any code)
                 **Reduces to**: [card](#...)   (before the first fence)

and three sheet roles (core X.md, X_examples.md, X_advanced.md). An agent told the
rules in prose still copies the old section next to the one it is writing, so this
checker is what makes them hold.

It judges the **diff**, not the tree. Most of the 135 sheets predate the cards
(the Nov 2026 review), so checking every section would fail on day one, and a
ratchet baseline of hundreds of entries would excuse exactly the wrong things.
Instead:

  * a section whose heading line is added or changed is new or rewritten work, and
    is held to the full card shape;
  * every other added line is held to the line rules (no ``NOTE !!!``);
  * a new sheet is also held to the sheet rules (no ``1-1)`` numbering).

Usage:
  python3 script/check_cheatsheet_cards.py                 # vs origin/master
  python3 script/check_cheatsheet_cards.py --base HEAD~1
  python3 script/check_cheatsheet_cards.py --all doc/cheatsheet/heap.md   # audit one file whole

Exit 0 when clean, 1 with one line per problem otherwise.
"""
import argparse
import os
import re
import subprocess
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SHEET_DIR = "doc/cheatsheet/"
SKIP = {"doc/cheatsheet/00_template.md"}

HEADING = re.compile(r"^(#{1,6})\s+(.*?)\s*$")
FENCE = re.compile(r"^\s*(```+|~~~+)\s*([\w+-]*)")
TEMPLATE_HEADING = re.compile(r"^Template\s+\d+[a-z]?\s*:")
GOOD_TEMPLATE_HEADING = re.compile(r"^Template\s+\d+[a-z]?\s*:\s*\S.*?\s+—\s+LC\s+\d+\b")
LC_IN_HEADING = re.compile(r"\bLC\s*\d+\b")
NUMBERED = re.compile(r"^\d+(-\d+)+\)")
# Pictographs and dingbats. The star run the site renders is allowed.
EMOJI = re.compile("[\U0001F300-\U0001FAFF☀-➿⬆⬇⬛⬜⭕✅❌]")

# Pattern-card slots, in order. (label, required)
PATTERN_SLOTS = [
    ("Recognise", True),
    ("Key point", True),
    ("@python", True),          # the template: a python fence
    ("Complexity", True),
    ("Dry run", False),
    ("Traps", True),
    ("Not this if", False),
    ("Drill", True),
]


def fence_open(line):
    """(run, lang) when the line opens a fence, else None."""
    m = FENCE.match(line)
    return (m.group(1), (m.group(2) or "").lower()) if m else None


def fence_closes(line, run):
    """A fence closes on a bare run of the same character, at least as long as the opener."""
    t = line.strip()
    return bool(re.fullmatch(re.escape(run[0]) + "{%d,}" % len(run), t))


def scan(lines):
    """Headings outside fences: list of (index, level, text)."""
    out, run = [], None
    for i, line in enumerate(lines):
        if run:
            if fence_closes(line, run):
                run = None
            continue
        opened = fence_open(line)
        if opened:
            run = opened[0]
            continue
        h = HEADING.match(line)
        if h:
            out.append((i, len(h.group(1)), h.group(2)))
    return out


def section_body(lines, headings, k):
    """Lines under heading k, up to the next heading of the same or a higher level."""
    start, level, _ = headings[k]
    end = len(lines)
    for j in range(k + 1, len(headings)):
        if headings[j][1] <= level:
            end = headings[j][0]
            break
    return start + 1, end


def strip_stars(text):
    return re.sub(r"\s*⭐+\s*$", "", text).strip()


def card_kind(text):
    """'pattern', 'problem' or None for a heading's text."""
    t = strip_stars(text)
    if TEMPLATE_HEADING.match(t):
        return "pattern"
    if LC_IN_HEADING.search(t):
        return "problem"
    return None


def slot_events(body_lines):
    """Ordered slot labels found in a section body, outside fences; fences as '@lang'."""
    events, run = [], None
    for line in body_lines:
        if run:
            if fence_closes(line, run):
                run = None
            continue
        opened = fence_open(line)
        if opened:
            run = opened[0]
            events.append("@" + opened[1])
            continue
        for label, _ in PATTERN_SLOTS:
            if not label.startswith("@") and re.match(r"^\s*(>\s*)?\*\*" + re.escape(label) + r"\*\*", line):
                events.append(label)
    return events


def check_pattern_card(path, lineno, text, body):
    problems = []
    if not GOOD_TEMPLATE_HEADING.match(strip_stars(text)):
        problems.append(f"{path}:{lineno}: pattern card heading must be "
                        f"'Template N: <Pattern name> — LC <n>', got '{text}'")
    events = slot_events(body)
    # First occurrence of each slot, in the order they appear.
    first = []
    for e in events:
        if e not in first:
            first.append(e)
    for label, required in PATTERN_SLOTS:
        if required and label not in first:
            shown = "a ```python template" if label == "@python" else f"**{label}**"
            problems.append(f"{path}:{lineno}: pattern card '{strip_stars(text)}' has no {shown}")
    order = [label for label, _ in PATTERN_SLOTS]
    seen = [e for e in first if e in order]
    if seen != sorted(seen, key=order.index):
        problems.append(f"{path}:{lineno}: pattern card slots out of order: {' -> '.join(seen)} "
                        f"(want {' -> '.join(order)})")
    if "@java" in first and "@python" in first and first.index("@java") < first.index("@python"):
        problems.append(f"{path}:{lineno}: Java comes before Python; Python first")
    problems += check_code(path, lineno, body)
    return problems


def check_problem_card(path, lineno, text, body):
    problems = []
    content = [l for l in body if l.strip()]
    if not content or not re.match(r"^>\s*\*\*Key point\*\*", content[0]):
        problems.append(f"{path}:{lineno}: problem card '{strip_stars(text)}' must open with "
                        f"'> **Key point** —', before any code")
    fence_at = next((i for i, l in enumerate(body) if fence_open(l)), len(body))
    if not any(re.match(r"^\s*\*\*Reduces to\*\*", l) for l in body[:fence_at]):
        problems.append(f"{path}:{lineno}: problem card '{strip_stars(text)}' needs "
                        f"'**Reduces to**:' before its first code block")
    problems += check_code(path, lineno, body)
    return problems


def check_code(path, lineno, body):
    """Python before Java within the section; IDEA then time in each python/java block."""
    problems, run, lang, block = [], None, "", []
    for line in body:
        if not run:
            opened = fence_open(line)
            if opened:
                run, lang, block = opened[0], opened[1], []
            continue
        if fence_closes(line, run):
            run = None
            if lang in ("python", "java") and len(block) >= 4:
                text = "\n".join(block)
                if "IDEA" not in text:
                    problems.append(f"{path}:{lineno}: a {lang} block has no IDEA comment")
                if not re.search(r"time\s*[=:]\s*O\(", text):
                    problems.append(f"{path}:{lineno}: a {lang} block has no 'time = O(...)' line")
            continue
        block.append(line)
    return problems


def check_heading(path, lineno, text, new_sheet):
    problems = []
    if EMOJI.search(strip_stars(text)):
        problems.append(f"{path}:{lineno}: emoji in a heading (only a trailing ⭐ run): '{text}'")
    if new_sheet and NUMBERED.match(text):
        problems.append(f"{path}:{lineno}: a new sheet does not use '1-1)' numbering: '{text}'")
    return problems


def check_file(path, lines, changed, new_sheet, whole=False):
    """changed: set of 0-based line indexes added or modified. whole: judge every section."""
    problems = []
    headings = scan(lines)
    is_examples = path.endswith("_examples.md")
    for k, (idx, level, text) in enumerate(headings):
        if not whole and idx not in changed:
            continue
        lineno = idx + 1
        problems += check_heading(path, lineno, text, new_sheet)
        start, end = section_body(lines, headings, k)
        body = lines[start:end]
        kind = card_kind(text)
        if kind == "pattern":
            if is_examples:
                problems.append(f"{path}:{lineno}: an _examples sheet holds no templates; "
                                f"put the pattern card in the core sheet")
            else:
                problems += check_pattern_card(path, lineno, text, body)
        elif kind == "problem" and level >= 3:
            problems += check_problem_card(path, lineno, text, body)
        if is_examples and re.match(r"^Other Examples\b", strip_stars(text)):
            problems.append(f"{path}:{lineno}: no '## Other Examples' in an _examples sheet; "
                            f"a problem that fits no template needs a core pattern card first")
    for i in sorted(changed):
        if i < len(lines) and "NOTE !!!" in lines[i]:
            problems.append(f"{path}:{i + 1}: 'NOTE !!!' — write it as a **Traps** bullet")
    return problems


def git(*args):
    return subprocess.run(["git", *args], cwd=ROOT, capture_output=True, text=True)


def changed_lines(base, path):
    """0-based indexes of lines added in the working tree vs base, from a -U0 diff."""
    out = git("diff", "-U0", base, "--", path).stdout
    changed = set()
    for m in re.finditer(r"^@@ -\d+(?:,\d+)? \+(\d+)(?:,(\d+))? @@", out, re.M):
        start, count = int(m.group(1)), int(m.group(2) or "1")
        changed.update(range(start - 1, start - 1 + count))
    return changed


DEFAULT_BASES = ("origin/master", "master")


def resolve_base(explicit, candidates=DEFAULT_BASES):
    """(merge-base sha, ref) for an explicit --base, or the first default that resolves.

    Returns (None, ref) when an explicit ref does not resolve, and (None, None) when no
    default does — a shallow clone with neither origin/master nor master, such as the
    skills gate's checkout, which runs this command only to prove it still works.
    """
    for ref in ([explicit] if explicit else list(candidates)):
        mb = git("merge-base", ref, "HEAD")
        if mb.returncode == 0:
            return mb.stdout.strip(), ref
        if explicit:
            return None, ref
    return None, None


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    ap.add_argument("--base", default=None,
                    help="git ref to diff against (default: origin/master, else master)")
    ap.add_argument("--all", nargs="+", metavar="FILE",
                    help="judge every section of these files, ignoring the diff")
    args = ap.parse_args(argv)

    problems = []
    if args.all:
        for path in args.all:
            rel = os.path.relpath(os.path.abspath(path), ROOT)
            with open(os.path.join(ROOT, rel), encoding="utf-8") as f:
                lines = f.read().split("\n")
            problems += check_file(rel, lines, set(), new_sheet=True, whole=True)
    else:
        base, ref = resolve_base(args.base)
        if base is None and args.base:
            print(f"check_cheatsheet_cards: cannot resolve base '{ref}'", file=sys.stderr)
            return 2
        if base is None:
            print("cheatsheet cards: no origin/master or master in this checkout, so there is "
                  "no diff to judge; pass --base <ref> to check one (CI's card job does)")
            return 0
        names = git("diff", "--name-only", "--diff-filter=AMR", base, "--", SHEET_DIR).stdout.split()
        untracked = git("ls-files", "--others", "--exclude-standard", "--", SHEET_DIR).stdout.split()
        for rel in sorted(set(names) | set(untracked)):
            if not rel.endswith(".md") or rel in SKIP or "/" in rel[len(SHEET_DIR):]:
                continue
            full = os.path.join(ROOT, rel)
            if not os.path.exists(full):
                continue
            with open(full, encoding="utf-8") as f:
                lines = f.read().split("\n")
            new_sheet = rel in untracked or git("cat-file", "-e", f"{base}:{rel}").returncode != 0
            changed = set(range(len(lines))) if new_sheet else changed_lines(base, rel)
            problems += check_file(rel, lines, changed, new_sheet)

    for p in problems:
        print(p)
    if problems:
        print(f"\n{len(problems)} problem(s). The shapes are in doc/cheatsheet/00_template.md "
              f"(pattern card, problem card, Skeletons A-D).", file=sys.stderr)
        return 1
    print("cheatsheet cards: every new or rewritten section is card-shaped")
    return 0


if __name__ == "__main__":
    sys.exit(main())
