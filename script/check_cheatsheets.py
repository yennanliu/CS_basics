#!/usr/bin/env python3
"""Ratchet the cheatsheet anti-patterns the Nov 2026 review counted.

doc/cheatsheet-review-2026-11.md (§ P7) measured how far the 135 sheets are from
interview material: `NOTE !!!` walls, IPython transcripts, worked examples that
are a heading over code, Java comparators that overflow, and sheets with no
recognition table or drill path. P2–P6 bring those numbers down a cluster at a
time; this gate is what stops them going back up in between.

    python3 script/check_cheatsheets.py                    # fail on a regression
    python3 script/check_cheatsheets.py --verbose          # also list what improved
    python3 script/check_cheatsheets.py --strict           # ignore the baseline
    python3 script/check_cheatsheets.py --update-baseline  # after a fix: lower the floor
    python3 script/check_cheatsheets.py --update-baseline --allow-increase   # a rule changed
    python3 script/check_cheatsheets.py --report           # the counts, rule by rule

The baseline, data/cheatsheet_lint_baseline.json, holds **a count per rule per
file**, never a line number: line numbers move with every edit above them, and
check_readme.py's one-entry-per-finding shape does not fit a rule like "this
heading is followed by code", whose findings have no stable identity. A count
can only go down. A file whose count rises fails, a file not in the baseline is
held to zero, and a count that fell is reported so the next commit can lower the
floor with --update-baseline.

It complements script/check_cheatsheet_cards.py rather than overlapping it:
that one judges the *diff* (is the section you just wrote a card?), this one the
*tree* (did the corpus as a whole get worse?).

Rules that need judgement (one home per problem, V0 is the canonical answer)
stay review items, not lint.
"""
import argparse
import glob
import json
import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SHEET_DIR = os.path.join(ROOT, "doc", "cheatsheet")
META = os.path.join(ROOT, "data", "cheatsheet_meta.json")
BASELINE = os.path.join(ROOT, "data", "cheatsheet_lint_baseline.json")
SKIP = {"00_template.md", "README.md"}

HEADING = re.compile(r"^(#{1,6})\s+(.*?)\s*$")
FENCE = re.compile(r"^\s*(```+|~~~+)\s*([\w+-]*)")
LC_IN_HEADING = re.compile(r"\bLC\s*\d+\b")
# Pictographs and dingbats; the trailing ⭐ run the site renders (U+2B50) is not in it.
EMOJI = re.compile("[\U0001F300-\U0001FAFF☀-➿⬅-⬇⬛⬜⭕]")
STAR_RUN = re.compile(r"\s*⭐+\s*$")
BLOG = re.compile(r"csdn\.net|cnblogs\.com|bilibili\.com|itdaan\.com", re.I)
GPT = re.compile(r"\bgpt\b", re.I)
IPYTHON = re.compile(r"^\s*(In|Out)\s*\[\d+\]:")
# A Java lambda comparator whose body subtracts: (a, b) -> a[0] - b[0]
# A Java comparator, in the three shapes the sheets use: an expression lambda
# `(a, b) -> a[0] - b[0]`, a block lambda `(a, b) -> { return a - b; }` (on one
# line or several), and an anonymous class's `int compare(T o1, T o2) {…}`.
# Parameters may be typed: `(int[] a, int[] b) ->`.
_PARAM = r"(?:[\w<>\[\],.?]+\s+)?(\w+)"
JAVA_LAMBDA = re.compile(r"\(\s*" + _PARAM + r"\s*,\s*" + _PARAM + r"\s*\)\s*->\s*(.*)")
JAVA_COMPARE = re.compile(r"\bint\s+compare\s*\(\s*" + _PARAM + r"\s*,\s*" + _PARAM + r"\s*\)")
JAVA_RETURN = re.compile(r"\breturn\b([^;]*);")
# How many lines a block comparator's `return` may sit below its signature.
COMPARATOR_SPAN = 6
# Python midpoint by true division, used as an index: (l + r) / 2
PY_FLOAT_MID = re.compile(r"\(\s*\w+\s*\+\s*\w+\s*\)\s*/(?!/)\s*2\b(?!\.)")
RECOGNITION = re.compile(r"recogni(?:s|z|tion)|when to use|how to (identify|spot|recogni)|signal", re.I)
TEMPLATE_HEADING = re.compile(r"^Template\s+\d+[a-z]?\s*:", re.I)

# rule -> one-line description; the order is the report's order.
RULES = {
    "note-wall": "`NOTE !!!` line (write a **Traps** bullet instead)",
    "ipython": "IPython `In [n]:` / `Out[n]:` transcript line",
    "gpt": "`gpt` mention",
    "blog-link": "csdn / cnblogs / bilibili / itdaan link",
    "heading-emoji": "emoji in a heading, other than the trailing ⭐ run",
    "code-first-example": "`### … LC n` heading followed directly by a code fence",
    "java-sub-comparator": "Java comparator that subtracts; use `Integer.compare`",
    "py-float-mid": "Python `(l + r) / 2` (true division); use `//`",
    "examples-after-summary": "`## … Examples` section after `## Summary`",
    "no-recognition": "tier ≥ 4 core sheet with no recognition section",
    "no-drill-path": "tier ≥ 4 core sheet with no `## Drill Path`",
    "examples-no-index": "`_examples` sheet with no `## Index`",
    "examples-template": "`Template N` heading in an `_examples` sheet",
    "advanced-no-when-not": "`_advanced` sheet with no `## When Not to Use`",
}


def role(name):
    stem = name[:-3]
    if stem.endswith("_examples"):
        return "examples"
    if stem.endswith("_advanced"):
        return "advanced"
    return "core"


def scan(lines):
    """Yield (index, line, fence_lang) — fence_lang is None outside code, else the tag."""
    fence = None
    for i, line in enumerate(lines):
        m = FENCE.match(line)
        if fence is None:
            if m:
                fence = (len(m.group(1)), m.group(1)[0], m.group(2).lower())
                yield i, line, "!open"
                continue
            yield i, line, None
        else:
            run, ch, lang = fence
            stripped = line.strip()
            if stripped and set(stripped) == {ch} and len(stripped) >= run:
                fence = None
                yield i, line, "!close"
                continue
            yield i, line, lang


def java_subtracts(body, a, b):
    """True when a lambda body compares by subtraction: a[0] - b[0], b - a, x.v - y.v."""
    for term in re.split(r"[?:]", body):
        # a binary minus, spaced or not; never the `->` arrow or a `--`
        m = re.search(r"([\w.\[\]()/% ]+?)\s*(?<![-<>=])-(?![->=-])\s*([\w.\[\]()/% ]+)", term)
        if not m:
            continue
        left, right = m.group(1), m.group(2)
        if re.search(r"\b(%s|%s)\b" % (a, b), left) and re.search(r"\b(%s|%s)\b" % (a, b), right):
            return True
    return False


def count_file(name, text, tier):
    """-> {rule: count} for one sheet."""
    counts = {r: 0 for r in RULES}
    lines = text.split("\n")
    h2 = []
    prev_lc_heading = None    # index of an `### … LC n` heading awaiting its first content line
    pending = None            # a block comparator whose `return` is still to come
    for i, line, lang in scan(lines):
        # line rules, prose, headings and code alike
        if lang not in ("!open", "!close"):
            if "NOTE !!!" in line:
                counts["note-wall"] += 1
            if IPYTHON.match(line):
                counts["ipython"] += 1
            if GPT.search(line):
                counts["gpt"] += 1
            if BLOG.search(line):
                counts["blog-link"] += 1
        if lang is None:
            hm = HEADING.match(line)
            if hm:
                level, title = len(hm.group(1)), hm.group(2)
                prev_lc_heading = i if level >= 3 and LC_IN_HEADING.search(title) else None
                if EMOJI.search(STAR_RUN.sub("", title)):
                    counts["heading-emoji"] += 1
                if level == 2:
                    h2.append(title)
                if role(name) == "examples" and TEMPLATE_HEADING.match(title):
                    counts["examples-template"] += 1
                continue
            if line.strip() and prev_lc_heading is not None:
                prev_lc_heading = None            # prose came first: fine
        elif lang == "!open":
            if prev_lc_heading is not None:
                counts["code-first-example"] += 1
                prev_lc_heading = None
        if lang == "java":
            code = line.split("//")[0]
            found = False
            m = JAVA_LAMBDA.search(code)
            c = JAVA_COMPARE.search(code)
            if m:
                a, b, body = m.group(1), m.group(2), m.group(3).strip()
                if body.startswith("{") or not body:
                    # block lambda, or an expression continued on the next line
                    pending = (a, b, COMPARATOR_SPAN, not body)
                    r = JAVA_RETURN.search(body)
                    found = bool(r and java_subtracts(r.group(1), a, b))
                else:
                    found = java_subtracts(body.split(";")[0], a, b)
                    pending = None
            elif c:
                pending = (c.group(1), c.group(2), COMPARATOR_SPAN, False)
            elif pending:
                a, b, left, expression = pending
                r = JAVA_RETURN.search(code)
                if expression:
                    found = java_subtracts(code.split(";")[0], a, b)
                    pending = None
                elif r:
                    found = java_subtracts(r.group(1), a, b)
                    pending = None if found else (a, b, left - 1, False)
                else:
                    pending = (a, b, left - 1, False) if left > 1 else None
            if found:
                counts["java-sub-comparator"] += 1
                pending = None
        else:
            pending = None
        if lang in ("python", "py") and not line.lstrip().startswith("#"):
            if PY_FLOAT_MID.search(line.split("#")[0]):
                counts["py-float-mid"] += 1

    summary_at = next((k for k, t in enumerate(h2) if re.match(r"(\d+\)\s*)?summary\b", STAR_RUN.sub("", t), re.I)), None)
    if summary_at is not None:
        counts["examples-after-summary"] = sum(
            1 for t in h2[summary_at + 1:] if re.search(r"\bexamples?\b", t, re.I))

    headings = [HEADING.match(l).group(2) for _, l, lang in scan(lines)
                if lang is None and HEADING.match(l)]
    r = role(name)
    if r == "core" and tier >= 4:
        counts["no-recognition"] = 0 if any(RECOGNITION.search(h) for h in headings) else 1
        counts["no-drill-path"] = 0 if any(re.match(r"(\d+\)\s*)?drill path\b", STAR_RUN.sub("", h), re.I)
                                           for h in h2) else 1    # a level-two section, not a subsection
    if r == "examples":
        counts["examples-no-index"] = 0 if any(re.match(r"index\b", h, re.I) for h in h2) else 1
    if r == "advanced":
        counts["advanced-no-when-not"] = 0 if any(re.search(r"when not to use", h, re.I) for h in h2) else 1
    return {k: v for k, v in counts.items() if v}


def survey(sheet_dir=SHEET_DIR, meta_path=META):
    """-> {rule: {file: count}} over every sheet, zero counts left out."""
    with open(meta_path, encoding="utf-8") as fh:
        meta = json.load(fh)
    out = {r: {} for r in RULES}
    for path in sorted(glob.glob(os.path.join(sheet_dir, "*.md"))):
        name = os.path.basename(path)
        if name in SKIP:
            continue
        entry = meta.get("sheets", meta).get(name[:-3]) or {}
        tier = entry.get("tier", 0) if isinstance(entry, dict) else 0
        if isinstance(entry, dict) and entry.get("kind") in ("stub", "reference"):
            tier = 0
        with open(path, encoding="utf-8") as fh:
            for rule, n in count_file(name, fh.read(), tier).items():
                out[rule][name] = n
    return out


def load_baseline(path):
    if not os.path.exists(path):
        return {r: {} for r in RULES}
    with open(path, encoding="utf-8") as fh:
        data = json.load(fh)
    return {r: data.get(r, {}) for r in RULES}


def compare(current, baseline):
    """-> (regressions, improvements): lists of (rule, file, was, now)."""
    worse, better = [], []
    for rule in RULES:
        cur, base = current.get(rule, {}), baseline.get(rule, {})
        for f in sorted(set(cur) | set(base)):
            now, was = cur.get(f, 0), base.get(f, 0)
            if now > was:
                worse.append((rule, f, was, now))
            elif now < was:
                better.append((rule, f, was, now))
    return worse, better


def write_baseline(current, path):
    data = {"_comment": (
        "Per-rule, per-file counts that script/check_cheatsheets.py tolerates. "
        "A count can only go down: a rise fails, a file not listed is held to 0. "
        "Lower it with `python3 script/check_cheatsheets.py --update-baseline` "
        "after a fix; see the script's docstring.")}
    for rule in RULES:
        data[rule] = dict(sorted(current[rule].items()))
    with open(path, "w", encoding="utf-8") as fh:
        json.dump(data, fh, indent=2, ensure_ascii=False)
        fh.write("\n")


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    ap.add_argument("--verbose", "-v", action="store_true", help="also list counts that fell")
    ap.add_argument("--strict", action="store_true", help="ignore the baseline")
    ap.add_argument("--update-baseline", action="store_true", help="write the current counts as the baseline")
    ap.add_argument("--allow-increase", action="store_true",
                    help="with --update-baseline: also record counts that rose (only when a rule itself changed)")
    ap.add_argument("--report", action="store_true", help="print the totals per rule and exit 0")
    ap.add_argument("--baseline", default=BASELINE, help=argparse.SUPPRESS)
    ap.add_argument("--sheets", default=SHEET_DIR, help=argparse.SUPPRESS)
    ap.add_argument("--meta", default=META, help=argparse.SUPPRESS)
    args = ap.parse_args(argv)

    current = survey(args.sheets, args.meta)

    if args.report:
        for rule, desc in RULES.items():
            files = current[rule]
            print("%5d in %3d file(s)  %-24s %s" % (sum(files.values()), len(files), rule, desc))
        return 0

    if args.update_baseline:
        # Lowering the floor is the point; raising it is not. Refuse to record a count that
        # rose, or one fix elsewhere would let a regression through. A first baseline is fine.
        if os.path.exists(args.baseline) and not args.allow_increase:
            worse, _ = compare(current, load_baseline(args.baseline))
            if worse:
                for rule, f, was, now in worse:
                    print("doc/cheatsheet/%s: %s %d -> %d — %s" % (f, rule, was, now, RULES[rule]))
                print("cheatsheet lint: not updating the baseline while %d count(s) rose; fix them first"
                      % len(worse))
                return 1
        write_baseline(current, args.baseline)
        total = sum(sum(v.values()) for v in current.values())
        print("wrote %s: %d finding(s) across %d rule(s)" % (
            os.path.relpath(args.baseline, ROOT), total, sum(1 for v in current.values() if v)))
        return 0

    baseline = {r: {} for r in RULES} if args.strict else load_baseline(args.baseline)
    worse, better = compare(current, baseline)
    for rule, f, was, now in worse:
        print("doc/cheatsheet/%s: %s %d -> %d — %s" % (f, rule, was, now, RULES[rule]))
    if better:
        msg = "%d count(s) fell below the baseline; lower it with --update-baseline" % len(better)
        print(("note: " + msg) if not worse else msg)
        if args.verbose:
            for rule, f, was, now in better:
                print("  doc/cheatsheet/%s: %s %d -> %d" % (f, rule, was, now))
    if worse:
        print("cheatsheet lint: %d regression(s) against %s" % (
            len(worse), os.path.relpath(args.baseline, ROOT)))
        return 1
    print("cheatsheet lint: no rule got worse")
    return 0


if __name__ == "__main__":
    sys.exit(main())
