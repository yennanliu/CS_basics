#!/usr/bin/env python3
"""Add a README table row for every Java LC solution that has none.

The README's per-pattern tables are the repo's index, but they only ever grew by
hand: 223 Java solution files were absent from them, so the index under-reported
what the repo actually contains. This appends the missing rows, one per Java
file, to the table of the section that matches the file's package.

Everything in a row is read out of the Java file itself - LC number, title and
difficulty from the problem javadoc, complexity from the `time =` / `space =`
javadoc on the V0 solution - so the rows say what the code says. The Python link
is filled in when a Python solution for the same problem exists, using the same
three-signal matcher as find_missing_java.py.

Usage
-----
    python3 script/sync_readme_java.py --dry-run     # report what would be added
    python3 script/sync_readme_java.py               # rewrite README.md in place

Run from the repository root. Existing rows are never touched or reordered.
"""

import argparse
import importlib.util
import os
import re
import sys

HERE = os.path.dirname(os.path.abspath(__file__))

# README section heading -> Java package directory. Most are the same word with
# spaces removed; the exceptions are the ones worth writing down.
SECTION_TO_PKG = {
    "Array": "Array",
    "Set": "Set",
    "Slide Window": "SlideWindow",
    "Hash Table": "HashTable",
    "Linked list": "LinkedList",
    "Stack": "Stack",
    "Tree": "Tree",
    "Heap": "Heap",
    "Bit Manipulation": "BitManipulation",
    "String": "String",
    "Queue": "Queue",
    "Math": "Math",
    "Scan Line": "ScanLine",
    "Sort": "Sort",
    "Two Pointers": "TwoPointer",
    "Recursion": "Recursion",
    "Binary Search": "BinarySearch",
    "Binary Search Tree": "BinarySearchTree",
    "Breadth-First Search": "BFS",
    "Depth-First Search": "DFS",
    "Backtracking": "BackTrack",
    "Dynamic Programming": "DynamicProgramming",
    "Greedy": "Greedy",
    "Graph": "Graph",
    "Design": "Design",
    "Prefix Sum": "PrefixSum",
    "Trie": "Trie",
}
# Packages with no section of their own land here.
PKG_FALLBACK_SECTION = {
    "Trie": "Tree",
    "DataStructure": None,   # helper types, not problems
}

DIFFICULTY_RE = re.compile(r'(?m)^\s*\*?\s*(Easy|Medium|Hard)\s*$')
# Capture to end of line, NOT to the first `*`: complexities multiply, so
# `time = O(31 * N)` would otherwise truncate to `O(31`. The javadoc's own
# leading ` * ` is already excluded by stopping at the newline.
TIME_RE = re.compile(r'time\s*=\s*([^\n]+)')
SPACE_RE = re.compile(r'space\s*=\s*([^\n]+)')
IDEA_RE = re.compile(r'//\s*IDEA\s*:?\s*([^\n]+)')


def load_matcher():
    spec = importlib.util.spec_from_file_location("fmj", os.path.join(HERE, "find_missing_java.py"))
    mod = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(mod)
    return mod


def clean_complexity(raw):
    """`O(N^2)   // ignoring the sort` -> `_O(N^2)_`.

    Pipes must be escaped: a complexity like `O(SUM(|dictionary[i]|))` would
    otherwise open two extra table columns and break the row.
    """
    if not raw:
        return "_?_"
    text = raw.split("//")[0].strip()
    # Drop trailing prose like `O(n) for the constructor,` -> `O(n)`.
    text = re.sub(r'\s+(for|per|amortiz\w*|excluding|including|where)\b.*$', '', text)
    text = re.sub(r'\s+', ' ', text).strip().rstrip(".,;").strip()
    if not text:
        return "_?_"
    return "_%s_" % text.replace("|", "\\|")


def row_for(path, mod, py_by_key):
    """Build one README row, or None if the file is not a problem solution."""
    text = mod.read(path)
    num, title = mod.lc_id(text)
    slug_match = mod.SLUG_RE.search(text)
    if num is None or not title or not slug_match:
        return None
    slug = slug_match.group(1)

    difficulty = DIFFICULTY_RE.search(text)
    difficulty = difficulty.group(1) if difficulty else ""

    time_m = TIME_RE.search(text)
    space_m = SPACE_RE.search(text)

    links = []
    py = py_by_key.get(num) or py_by_key.get(mod.norm_title(title))
    if py:
        links.append("[Python](./%s)" % py)
    links.append("[Java](./%s)" % path)

    idea = IDEA_RE.search(text)
    pkg = path.split("/LeetCodeJava/")[-1].split("/")[0] if "/LeetCodeJava/" in path else ""
    note = "**%s**" % pkg.lower() if pkg else ""
    if idea:
        hint = idea.group(1).strip().rstrip(".").lower()
        # An IDEA line often wraps; drop one that ends on a dangling connector
        # rather than paste half a clause into the table.
        if len(hint) <= 60 and not re.search(r'[+\-,]$|\b(and|or|the|a|of|with|for|to)$', hint):
            note = "%s, %s" % (note, hint) if note else hint

    return {
        "num": num,
        "pkg": pkg,
        "text": "| %d | [%s](https://leetcode.com/problems/%s/) | %s | %s | %s | %s | %s |  |" % (
            num,
            title.replace("|", "\\|"),
            slug,
            ", ".join(links),
            clean_complexity(time_m.group(1) if time_m else None),
            clean_complexity(space_m.group(1) if space_m else None),
            difficulty,
            note.replace("|", "\\|"),
        ),
    }


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--dry-run", action="store_true", help="report only, do not write README.md")
    args = ap.parse_args()

    if not os.path.isfile("README.md"):
        sys.exit("run this from the repository root")

    mod = load_matcher()
    readme = open("README.md").read()
    linked_java = set(re.findall(r'\./leetcode_java/([^\)\s,]+\.java)', readme))

    # LC number / title -> python path, so a new row can link both languages.
    py_by_key = {}
    for problem in mod.index_python():
        rel = problem["path"]
        if problem["lc"] is not None:
            py_by_key.setdefault(problem["lc"], rel)
        if problem["title"]:
            py_by_key.setdefault(mod.norm_title(problem["title"]), rel)
        py_by_key.setdefault(mod.norm_title(problem["file"]), rel)

    # Collect rows for every unlinked Java solution.
    pending = {}
    skipped = []
    for dirpath, _dirs, files in os.walk(os.path.join("leetcode_java", "src", "main", "java")):
        for name in sorted(files):
            if not name.endswith(".java"):
                continue
            path = os.path.join(dirpath, name)
            if path[len("leetcode_java/"):] in linked_java:
                continue
            if "/LeetCodeJava/" not in path:
                continue          # AlgorithmJava, dev, ws: not problem solutions
            row = row_for(path, mod, py_by_key)
            if row is None:
                skipped.append(path)
                continue
            pending.setdefault(row["pkg"], []).append(row)

    # Which section does each package's rows belong to?
    pkg_to_section = {}
    for section, pkg in SECTION_TO_PKG.items():
        pkg_to_section.setdefault(pkg, section)
    for pkg, section in PKG_FALLBACK_SECTION.items():
        if section:
            pkg_to_section[pkg] = section

    lines = readme.split("\n")
    # Where each section's table ends (last consecutive `|` row).
    inserts = {}
    for i, line in enumerate(lines):
        if not line.startswith("## "):
            continue
        section = line[3:].strip()
        pkg = SECTION_TO_PKG.get(section)
        if pkg is None:
            continue
        end = i
        for j in range(i + 1, len(lines)):
            if lines[j].startswith("## "):
                break
            if lines[j].strip().startswith("|"):
                end = j
        inserts[pkg] = end

    added = 0
    unplaced = []
    # Insert bottom-up so earlier line numbers stay valid.
    for pkg in sorted(pending, key=lambda p: -inserts.get(p, 0)):
        rows = sorted(pending[pkg], key=lambda r: r["num"])
        target = inserts.get(pkg)
        if target is None:
            section = pkg_to_section.get(pkg)
            target = inserts.get(SECTION_TO_PKG.get(section)) if section else None
        if target is None:
            unplaced.extend(rows)
            continue
        if not args.dry_run:
            lines[target + 1:target + 1] = [r["text"] for r in rows]
        added += len(rows)
        print("%-22s +%d rows" % (pkg, len(rows)))

    print()
    print("rows to add   : %d" % added)
    print("unplaced      : %d (no README section for the package)" % len(unplaced))
    print("skipped       : %d (no LC number/title/url in the file - not a problem solution)"
          % len(skipped))
    for path in skipped[:10]:
        print("    %s" % path)

    if not args.dry_run and added:
        with open("README.md", "w") as fh:
            fh.write("\n".join(lines))
        print("\nREADME.md updated")


if __name__ == "__main__":
    main()
