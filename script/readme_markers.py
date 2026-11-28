#!/usr/bin/env python3
"""
List README's problem rows that carry a marker, and save them to data/.

    python3 script/readme_markers.py again     # -> data/again_problems.txt
    python3 script/readme_markers.py must      # -> data/must_problems.txt
    python3 script/readme_markers.py again --stdout   # print only, write nothing

`get_again_problems.sh` and `get_must_problems.sh` are thin wrappers over this,
kept because the /lc-again skill and its page document them by name.

Both used to be `grep -i <MARKER> README.md`, which matched prose, the
data-structure table above `## Array`, and notes that merely contain the word
("must be non-decreasing", "try again"). The rows are now chosen the way every
other script reads README:

- only problem rows of the main tables (`check_readme.parse_rows`), never the
  imported kamyu104 index;
- `again` — the status cell's word is AGAIN, in any casing (`suggest_review.status_word`);
- `must` — `suggest_review.is_must`, the rule the planner and the roadmap use.

The output keeps the old shape — a `#` header, then each README row verbatim —
so a diff of the file after a README edit still reads row by row.
"""

import argparse
import os
import sys
import time

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, HERE)

import check_readme as cr  # noqa: E402  — the row walker
import suggest_review as sr  # noqa: E402  — the status word and the MUST rule


def is_again(row):
    return sr.status_word(row["status"]) == "AGAIN"


def is_must(row):
    return sr.is_must(row["note"], row["status"])


MARKERS = {"again": is_again, "must": is_must}


def marked_rows(readme_text, marker):
    """-> [(line_no, raw README line)] for main-table rows carrying `marker`."""
    lines = readme_text.split("\n")
    keep = MARKERS[marker]
    return [(r["line"], lines[r["line"] - 1])
            for r in cr.parse_rows(readme_text)
            if not r["imported"] and keep(r)]


def render(rows, marker):
    head = [
        "# LeetCode Problems with %s marker" % marker.upper(),
        "# Generated on: %s by script/readme_markers.py %s" % (time.strftime("%Y-%m-%d"), marker),
        "# Total count: %d" % len(rows),
        "",
    ]
    return "\n".join(head + [line for _, line in rows]) + "\n"


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    ap.add_argument("marker", choices=sorted(MARKERS))
    ap.add_argument("--readme", default=os.path.join(ROOT, "README.md"))
    ap.add_argument("--stdout", action="store_true", help="print the rows; write no file")
    a = ap.parse_args(argv)

    with open(a.readme, encoding="utf-8") as f:
        rows = marked_rows(f.read(), a.marker)
    text = render(rows, a.marker)
    if a.stdout:
        sys.stdout.write(text)
        return 0

    out = os.path.join(ROOT, "data", "%s_problems.txt" % a.marker)
    with open(out, "w", encoding="utf-8") as f:
        f.write(text)
    print("%d README rows marked %s -> %s" % (len(rows), a.marker.upper(), os.path.relpath(out, ROOT)))
    return 0


if __name__ == "__main__":
    sys.exit(main())
