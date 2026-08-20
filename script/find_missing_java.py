#!/usr/bin/env python3
"""Find LeetCode problems that have a Python solution but no Java one.

Why this exists
---------------
The naive way to answer "which problems still need Java?" is to compare
`leetcode_python/<Cat>/<slug>.py` filenames against the
`https://leetcode.com/problems/<slug>` comment in each Java file. That
over-reports, because a single problem can be spelled three different ways:

  * LeetCode renamed the slug         - LC 211 is `add-and-search-word-data-structure-design`
                                        in the Python filename but
                                        `design-add-and-search-words-data-structure` in the
                                        Java url comment.
  * the Python file is underscored    - `coin_change_2.py` vs `coin-change-2`
  * the Java file has no url comment  - ~60 files, mostly non-LC helpers

So this script joins on three signals instead of one, in order of strength:

  1. LC problem number, parsed from the `123. Title` line that both the Python
     docstring header and the Java problem javadoc carry.
  2. Any `leetcode.com/problems/<slug>` url found in the file body.
  3. The problem title, normalised to a Java-style class name
     (`3 Sum Smaller` -> `threesumsmaller`), matched against the Java filename.

A problem counts as covered if ANY signal finds a Java file.

Usage
-----
    python3 script/find_missing_java.py                  # summary + per-category counts
    python3 script/find_missing_java.py --list           # also print every missing problem
    python3 script/find_missing_java.py --category Array # restrict to one Python category
    python3 script/find_missing_java.py --json out.json  # machine-readable, for batch work
    python3 script/find_missing_java.py --false-gaps     # show slug-only mismatches
    python3 script/find_missing_java.py --unidentified    # files where no LC id could be parsed

Run from the repository root.
"""

import argparse
import json
import os
import re
import sys
from collections import defaultdict

PY_ROOT = os.path.join("leetcode_python")
JAVA_ROOT = os.path.join("leetcode_java", "src", "main", "java")

# `211. Design Add and Search Words Data Structure`, optionally behind javadoc/
# docstring/comment decoration (` * `, `# `, `// `, `"""`).
LC_ID_RE = re.compile(
    r'^[\s*#/"\']*(\d{1,4})\s*\.\s+([A-Za-z0-9][^\n]*?)\s*$',
    re.MULTILINE,
)
# `// LC 211` / `// LC 2656`, the marker used inside multi-problem files
# (LCWeekly/*.java holds one contest's worth of problems). Two digits minimum:
# a bare `LC 1` is nearly always prose, and LC 1-9 all carry url comments anyway.
LC_TAG_RE = re.compile(r'\bLC\s*#?\s*(\d{2,4})\b')
# `leetcode.com/problems/<slug>`, the `leetcode.ca/all/<slug>` mirror, and the
# contest form `leetcode.com/contest/weekly-contest-468/problems/<slug>` that
# LCWeekly/*.java uses - a contest link is still a solved problem.
SLUG_RE = re.compile(r'leetcode(?:\.com|\.ca)?/(?:contest/[a-z0-9\-]+/)?(?:problems|all)/([a-z0-9\-]+)')
# Where a file stops being a header and starts being code. `1. Populate the graph
# map` in a mid-function comment is not an LC id, so the id scan stops here.
CODE_STARTS_RE = re.compile(r'(?m)^\s*(?:public\s+|final\s+|abstract\s+)*(?:class|interface|enum)\s|^\s*(?:def|class)\s')
HEADER_CHARS = 3000
# A problem title is a short noun phrase. Code fragments, prose and sentence
# tails are not: they carry operators, quotes, brackets or a trailing colon.
TITLE_REJECT_RE = re.compile(r'[=()\[\]{}"\'_;<>|&%$#@\\]|\.\.\.|:\s*$')


def norm_title(text):
    """`3 Sum Smaller` / `3sum-smaller` -> `3sumsmaller` (a class-name-ish key)."""
    return re.sub(r"[^a-z0-9]", "", text.lower())


def read(path):
    with open(path, errors="ignore") as fh:
        return fh.read()


def looks_like_title(text):
    """Reject prose and code fragments that happen to follow `<digits>. `."""
    if not (3 <= len(text) <= 80):
        return False
    if TITLE_REJECT_RE.search(text):
        return False
    words = text.split()
    if not (1 <= len(words) <= 12):
        return False
    # Titles are Title Case or at least start capitalised; `prefix[i` and
    # `init i = j` do not survive this plus the reject pattern above.
    return text[0].isupper() or text[0].isdigit()


def header_of(text):
    """The leading comment/docstring block, before the first type declaration."""
    code = CODE_STARTS_RE.search(text)
    end = min(code.start() if code else HEADER_CHARS, HEADER_CHARS)
    return text[:end]


def lc_id(text):
    """Return (number, title) parsed from a file header, or (None, None)."""
    header = header_of(text)
    for num, title in LC_ID_RE.findall(header):
        n = int(num)
        title = title.strip()
        # `0. Concept` style section headers are below the real problem range.
        if 1 <= n <= 4000 and looks_like_title(title):
            return n, title
    tag = LC_TAG_RE.search(header)
    if tag:
        return int(tag.group(1)), None
    return None, None


def lc_tags(text):
    """LC numbers a multi-problem file genuinely solves.

    `LCWeekly/*.java` holds a whole contest, each problem introduced by a
    standalone `// LC 2656` line followed by its url. Only that shape counts:
    an inline mention like `check with LC 542` is a cross-reference to another
    problem, and counting it would fake coverage for a problem nobody solved.
    """
    lines = text.split("\n")
    found = set()
    for i, line in enumerate(lines):
        tag = re.match(r'^\s*(?:\*|//|#)?\s*LC\s*#?\s*(\d{2,4})\s*$', line)
        if not tag:
            continue
        if any("leetcode.com/problems/" in nxt for nxt in lines[i + 1:i + 7]):
            found.add(int(tag.group(1)))
    return found


def walk(root, ext):
    for dirpath, _dirnames, filenames in os.walk(root):
        for name in sorted(filenames):
            if name.endswith(ext):
                yield os.path.join(dirpath, name)


def index_java():
    """Build number / slug / title indexes over every Java file."""
    by_num = defaultdict(list)
    by_slug = defaultdict(list)
    by_title = defaultdict(list)
    count = 0
    for path in walk(JAVA_ROOT, ".java"):
        count += 1
        text = read(path)
        num, title = lc_id(text)
        if num is not None:
            by_num[num].append(path)
        # A contest file solves several problems; each `// LC n` marker counts.
        for tag in lc_tags(text):
            if path not in by_num[tag]:
                by_num[tag].append(path)
        for slug in set(SLUG_RE.findall(text)):
            by_slug[slug].append(path)
        if title:
            by_title[norm_title(title)].append(path)
        # The filename itself is a title signal, and the only one for files
        # that carry no header at all.
        by_title[norm_title(os.path.basename(path)[:-5])].append(path)
    return {"num": by_num, "slug": by_slug, "title": by_title, "count": count}


def index_python():
    """One record per Python solution file."""
    problems = []
    for path in walk(PY_ROOT, ".py"):
        name = os.path.basename(path)[:-3]
        text = read(path)
        num, title = lc_id(text)
        slugs = set(SLUG_RE.findall(text))
        slugs.add(name.replace("_", "-"))
        problems.append(
            {
                "path": path,
                "category": os.path.relpath(os.path.dirname(path), PY_ROOT),
                "file": name,
                "lc": num,
                "title": title,
                "slugs": sorted(slugs),
            }
        )
    return problems


def best(candidates, problem):
    """Several Java files can claim one LC number; prefer the one whose filename
    echoes the problem title, so the reported path is the real solution."""
    if len(candidates) == 1:
        return candidates[0]
    keys = [norm_title(problem["file"])]
    if problem["title"]:
        keys.append(norm_title(problem["title"]))
    for path in candidates:
        name = norm_title(os.path.basename(path)[:-5])
        if any(name == k or k in name or name in k for k in keys):
            return path
    return candidates[0]


def match(problem, java):
    """Return (matched_java_path, signal) or (None, None)."""
    if problem["lc"] is not None and problem["lc"] in java["num"]:
        return best(java["num"][problem["lc"]], problem), "lc-number"
    for slug in problem["slugs"]:
        if slug in java["slug"]:
            return java["slug"][slug][0], "slug"
    keys = [norm_title(problem["file"])]
    if problem["title"]:
        keys.append(norm_title(problem["title"]))
    for key in keys:
        if key in java["title"]:
            return java["title"][key][0], "title"
    return None, None


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--list", action="store_true", help="print every missing problem")
    ap.add_argument("--category", help="restrict to one leetcode_python/ subdirectory")
    ap.add_argument("--json", metavar="PATH", help="write the missing list as JSON")
    ap.add_argument("--false-gaps", action="store_true",
                    help="problems a slug-only comparison would wrongly call missing")
    ap.add_argument("--unidentified", action="store_true",
                    help="Python files with no parseable LC number")
    args = ap.parse_args()

    if not os.path.isdir(PY_ROOT) or not os.path.isdir(JAVA_ROOT):
        sys.exit("run this from the repository root (leetcode_python/ not found)")

    java = index_java()
    problems = index_python()
    if args.category:
        problems = [p for p in problems if p["category"] == args.category]

    missing, covered, false_gaps, unidentified = [], defaultdict(int), [], []
    for problem in problems:
        path, signal = match(problem, java)
        if path is None:
            missing.append(problem)
            if problem["lc"] is None:
                unidentified.append(problem)
        else:
            covered[signal] += 1
            if signal != "slug":
                # A slug-only scan would have missed this one.
                false_gaps.append((problem, path, signal))

    by_cat = defaultdict(int)
    for problem in missing:
        by_cat[problem["category"]] += 1

    print("Java files indexed      : %d" % java["count"])
    print("Python solutions scanned: %d" % len(problems))
    print("  covered by Java       : %d  (lc-number %d, slug %d, title %d)"
          % (sum(covered.values()), covered["lc-number"], covered["slug"], covered["title"]))
    print("  MISSING Java          : %d" % len(missing))
    print("  of the missing, no LC id parsed: %d" % len(unidentified))
    print()
    print("Missing by category:")
    for cat, n in sorted(by_cat.items(), key=lambda kv: -kv[1]):
        print("  %5d  %s" % (n, cat))

    if args.false_gaps:
        print("\nCovered only via a non-slug signal (%d) - a slug-only scan calls these missing:"
              % len(false_gaps))
        for problem, path, signal in false_gaps:
            print("  [%s] %s -> %s" % (signal, problem["path"], path))

    if args.unidentified:
        print("\nMissing AND no LC number parseable (%d) - need a manual look:" % len(unidentified))
        for problem in unidentified:
            print("  %s" % problem["path"])

    if args.list:
        print("\nMissing problems:")
        for problem in sorted(missing, key=lambda p: (p["category"], p["file"])):
            print("  %-22s %-6s %s" % (problem["category"], problem["lc"] or "?", problem["file"]))

    if args.json:
        with open(args.json, "w") as fh:
            json.dump(
                {
                    "missing": missing,
                    "by_category": dict(by_cat),
                    "covered": dict(covered),
                },
                fh,
                indent=1,
            )
        print("\nwrote %s" % args.json)


if __name__ == "__main__":
    main()
