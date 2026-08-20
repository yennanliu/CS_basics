#!/usr/bin/env python3
"""
extract_must_lc.py

Scan the repo README.md and extract every LeetCode problem whose row is
flagged with `MUST` (the high-priority marker used in the status/tag columns).

Output: a markdown doc grouped by category, each row showing
    LC number | LC name | difficulty | status | category

Usage:
    python3 script/extract_must_lc.py                       # writes doc/must_lc_list.md
    python3 script/extract_must_lc.py --readme README.md --out doc/must_lc_list.md
    python3 script/extract_must_lc.py --stdout              # print to stdout instead

Notes:
- A "category" is any `## <Name>` header that appears at/after the first LC
  table section (default: the `## Array` section).
- The `MUST` marker is looked for in two places:
    * the trailing status/AGAIN column -> matched case-insensitively
      (`MUST`, `must`, `Must`, `(MUST!)`, `DP MUST`, ...)
    * the tags column -> only a standalone, all-caps `MUST` token counts, so
      that prose such as "window must be non-decreasing" is not picked up.
"""
import argparse
import os
import re
from collections import OrderedDict

DIFFS = {"easy", "medium", "hard"}
# status column: any casing of "must" is a marker -> MUST / must / Must / MUST!
MUST_ANY_CASE = re.compile(r"must", re.IGNORECASE)
# tags column: only a standalone ALL-CAPS MUST token is a marker, otherwise
# ordinary prose ("gcd ... must be >= 2") would be treated as a flag.
MUST_TAG_TOKEN = re.compile(r"(?<![A-Za-z])MUST(?![A-Za-z])")
# Category sections only start once the LC tables begin. Everything above
# (## Cmd, ## Resource, ## Database, ...) is intro/non-LC content.
FIRST_LC_SECTION = "Array"


def parse(readme_path):
    with open(readme_path, encoding="utf-8") as f:
        lines = f.readlines()

    current_cat = None
    seen_first_section = False
    results = []  # (num, name, diff, status, category)

    for raw in lines:
        line = raw.rstrip("\n")

        # category header
        m = re.match(r"^##\s+(.*)", line)
        if m:
            cat = m.group(1).strip()
            if cat == FIRST_LC_SECTION:
                seen_first_section = True
            if seen_first_section:
                current_cat = cat
            continue

        if not seen_first_section:
            continue
        if "|" not in line or not MUST_ANY_CASE.search(line):
            continue

        # rows are pipe-delimited and may or may not carry an outer `|`
        cols = [c.strip() for c in line.strip().strip("|").split("|")]
        num = cols[0]
        if not re.match(r"^\d+$", num):
            continue  # skip non-data rows (separators, prose, etc.)

        # last non-empty column = the trailing AGAIN/MUST status marker
        status = next((c for c in reversed(cols) if c), "")
        tags = cols[-2] if len(cols) >= 2 else ""
        if not (MUST_ANY_CASE.search(status) or MUST_TAG_TOKEN.search(tags)):
            continue

        # problem name: first markdown link text in col 2
        name_match = re.search(r"\[([^\]]+)\]", cols[1]) if len(cols) > 1 else None
        name = (name_match.group(1).strip() if name_match else cols[1]).strip()

        # difficulty: the column whose value is Easy/Medium/Hard
        diff = next((c for c in cols if c.lower() in DIFFS), "")

        results.append((int(num), name, diff, status, current_cat))

    return results


def render(results):
    groups = OrderedDict()
    for num, name, diff, status, cat in results:
        groups.setdefault(cat, []).append((num, name, diff, status))

    out = []
    out.append("# LeetCode `MUST` Problems")
    out.append("")
    out.append(
        f"Auto-generated from `README.md` by `script/extract_must_lc.py`. "
        f"Total: **{len(results)}** problems across **{len(groups)}** categories."
    )
    out.append("")
    # table of contents
    out.append("## Categories")
    out.append("")
    for cat, items in groups.items():
        anchor = re.sub(r"[^a-z0-9]+", "-", cat.lower()).strip("-")
        out.append(f"- [{cat}](#{anchor}) ({len(items)})")
    out.append("")

    for cat, items in groups.items():
        out.append(f"## {cat}")
        out.append("")
        out.append("| LC | Name | Difficulty | Status | Category |")
        out.append("|----|------|------------|--------|----------|")
        for num, name, diff, status in items:
            # escape pipes inside the status text so the table stays valid
            safe_status = status.replace("|", "\\|")
            out.append(f"| {num} | {name} | {diff} | {safe_status} | {cat} |")
        out.append("")

    return "\n".join(out)


def main():
    repo_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--readme", default=os.path.join(repo_root, "README.md"))
    ap.add_argument("--out", default=os.path.join(repo_root, "doc", "must_lc_list.md"))
    ap.add_argument("--stdout", action="store_true", help="print to stdout instead of writing a file")
    args = ap.parse_args()

    results = parse(args.readme)
    doc = render(results)

    if args.stdout:
        print(doc)
    else:
        os.makedirs(os.path.dirname(args.out), exist_ok=True)
        with open(args.out, "w", encoding="utf-8") as f:
            f.write(doc + "\n")
        print(f"Wrote {len(results)} MUST problems to {args.out}")


if __name__ == "__main__":
    main()
