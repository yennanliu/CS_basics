#!/usr/bin/env python3
"""Audit leetcode_python/ for the '>=3 solution versions' task and write batch work-lists.

Counts, per file, how many `# V<n>` blocks hold a REAL implementation (a placeholder
block whose body is only `pass` / a bare `return` does not count). Files with fewer
than 3 are written out as batches of 6, ordered so the highest-traffic directories
land first.

  python3 script/py_multi_version/audit.py            # report + rewrite batches/
  python3 script/py_multi_version/audit.py --report   # report only, leave batches/ alone
"""
import os, sys, ast, json
from collections import Counter

HERE = os.path.dirname(os.path.abspath(__file__))
REPO = os.path.dirname(os.path.dirname(HERE))
LC = os.path.join(os.environ.get("PMV_WORKTREE", REPO), "leetcode_python")
BATCH_DIR = os.path.join(HERE, "batches")
BATCH_SIZE = 6

# Directory order == priority order: highest-traffic interview topics get fixed first.
DIR_ORDER = [
    "Array", "Dynamic_Programming", "String", "Hash_table", "Tree", "Two_Pointers",
    "Binary_Search", "Breadth-First-Search", "Depth-First-Search", "Graph", "Heap",
    "Stack", "Linked_list", "Greedy", "Math", "Sort", "Design", "Backtracking",
    "Bit_Manipulation", "Recursion", "Binary_Search_Tree", "slide_window", "Queue",
    "Geometry", "prefix_sum",
]

sys.path.insert(0, HERE)
from verify import MARK, CODEISH, PLACEHOLDER, strip_str  # reuse ONE definition of "impl"
import re


def count_impls(path):
    lines = open(path, encoding="utf-8", errors="replace").read().split("\n")
    idx = [i for i, l in enumerate(lines) if MARK.match(l)]
    impls = 0
    for n, i in enumerate(idx):
        end = idx[n + 1] if n + 1 < len(idx) else len(lines)
        seg = strip_str("\n".join(lines[i + 1:end])).split("\n")
        if not any(CODEISH.match(l) for l in seg):
            continue
        body = [re.sub(r"\s+", " ", l.strip()) for l in seg
                if l.strip() and not l.strip().startswith("#")]
        meat = [b for b in body if not re.match(r"^(class|def)\b", b)]
        if not meat or all(m in PLACEHOLDER for m in meat):
            continue
        impls += 1
    return impls


def main():
    report_only = "--report" in sys.argv
    rows = []
    for dp, dn, fns in os.walk(LC):
        dn[:] = [d for d in dn if d != "__pycache__"]
        for fn in sorted(fns):
            if not fn.endswith(".py"):
                continue
            p = os.path.join(dp, fn)
            rows.append((os.path.relpath(p, LC), count_impls(p)))

    dist = Counter(n for _, n in rows)
    print(f"files scanned          : {len(rows)}")
    print(f"real implementations   : {sum(n for _, n in rows)}")
    print("impl-count distribution:")
    for k in sorted(dist):
        print(f"  {k:>3} impls : {dist[k]} files")

    weight = {d: i for i, d in enumerate(DIR_ORDER)}
    todo = sorted((r for r in rows if r[1] < 3),
                  key=lambda r: (r[1], weight.get(r[0].split("/")[0], 99), r[0]))
    print(f"\nshort of 3 : {len(todo)} files -> {-(-len(todo) // BATCH_SIZE)} batches")
    by_dir = Counter(r[0].split("/")[0] for r in todo)
    for d in sorted(by_dir, key=lambda k: -by_dir[k]):
        print(f"  {d:<24}{by_dir[d]:>5}")

    if report_only:
        return
    os.makedirs(BATCH_DIR, exist_ok=True)
    for f in os.listdir(BATCH_DIR):
        if f.startswith("batch_") and f.endswith(".txt"):
            os.remove(os.path.join(BATCH_DIR, f))
    for bi in range(0, len(todo), BATCH_SIZE):
        chunk = todo[bi:bi + BATCH_SIZE]
        with open(os.path.join(BATCH_DIR, f"batch_{bi // BATCH_SIZE + 1:03d}.txt"), "w") as fh:
            for rel, n in chunk:
                fh.write(f"{rel}\t(has {n} impl, need {3 - n} more)\n")
    print(f"\nwrote {-(-len(todo) // BATCH_SIZE)} batch files to {BATCH_DIR}")


if __name__ == "__main__":
    main()
