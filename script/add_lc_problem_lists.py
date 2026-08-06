#!/usr/bin/env python3
"""Insert a "LeetCode Problem Lists" section into every cheatsheet under doc/cheatsheet/.

Each doc gets links to the LeetCode problem lists (https://leetcode.com/problem-list/<tag>/)
for the LC topic tags that match the doc's pattern, e.g.

    ## LeetCode Problem Lists
    - [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
    - [Stack](https://leetcode.com/problem-list/stack/)

Placement: as the first `##` section of the doc (right before the existing first `##`
heading), so the links show up at the top of the page and in the generated site TOC.

The script is idempotent: re-running it rewrites the block in place instead of
appending a second copy.

Usage:
    python3 script/add_lc_problem_lists.py           # apply
    python3 script/add_lc_problem_lists.py --dry-run # report only
    python3 script/add_lc_problem_lists.py --verify  # check slugs/names against LeetCode

Note on --verify: leetcode.com serves 403 to non-browser clients for HTML pages, so
slugs are validated through the public GraphQL endpoint (`topicTag(slug:)`) instead,
which also gives the canonical display name for each tag.
"""

import argparse
import json
import os
import re
import sys
import urllib.error
import urllib.request

SECTION_TITLE = "LeetCode Problem Lists"

# LC topic tag slug -> display name (as LeetCode labels them)
TAG_NAMES = {
    "array": "Array",
    "backtracking": "Backtracking",
    "binary-indexed-tree": "Binary Indexed Tree",
    "binary-search": "Binary Search",
    "binary-search-tree": "Binary Search Tree",
    "binary-tree": "Binary Tree",
    "bit-manipulation": "Bit Manipulation",
    "bitmask": "Bitmask",
    "breadth-first-search": "Breadth-First Search",
    "bucket-sort": "Bucket Sort",
    "combinatorics": "Combinatorics",
    "concurrency": "Concurrency",
    "counting": "Counting",
    "counting-sort": "Counting Sort",
    "data-stream": "Data Stream",
    "depth-first-search": "Depth-First Search",
    "design": "Design",
    "divide-and-conquer": "Divide and Conquer",
    "doubly-linked-list": "Doubly-Linked List",
    "dynamic-programming": "Dynamic Programming",
    "graph": "Graph Theory",
    "greedy": "Greedy",
    "hash-function": "Hash Function",
    "hash-table": "Hash Table",
    "heap-priority-queue": "Heap (Priority Queue)",
    "iterator": "Iterator",
    "linked-list": "Linked List",
    "math": "Math",
    "matrix": "Matrix",
    "memoization": "Memoization",
    "merge-sort": "Merge Sort",
    "monotonic-queue": "Monotonic Queue",
    "monotonic-stack": "Monotonic Stack",
    "number-theory": "Number Theory",
    "ordered-set": "Ordered Set",
    "prefix-sum": "Prefix Sum",
    "queue": "Queue",
    "quickselect": "Quickselect",
    "radix-sort": "Radix Sort",
    "recursion": "Recursion",
    "reservoir-sampling": "Reservoir Sampling",
    "rolling-hash": "Rolling Hash",
    "segment-tree": "Segment Tree",
    "shortest-path": "Shortest Path",
    "simulation": "Simulation",
    "sliding-window": "Sliding Window",
    "sorting": "Sorting",
    "stack": "Stack",
    "string": "String",
    "string-matching": "String Matching",
    "suffix-array": "Suffix Array",
    "sweep-line": "Sweep Line",
    "topological-sort": "Topological Sort",
    "tree": "Tree",
    "trie": "Trie",
    "two-pointers": "Two Pointers",
    "union-find": "Union-Find",
}

# cheatsheet file -> LC topic tags (most relevant first)
DOC_TAGS = {
    "2_pointers.md": ["two-pointers", "sliding-window", "array"],
    "2_pointers_linkedlist.md": ["two-pointers", "linked-list"],
    "add_x_sum.md": ["math", "string", "bit-manipulation"],
    "advanced_divide_and_conquer.md": ["divide-and-conquer", "merge-sort", "quickselect"],
    "advanced_simulation.md": ["simulation", "design"],
    "advanced_string_algorithms.md": ["string", "string-matching", "rolling-hash", "suffix-array"],
    "array.md": ["array"],
    "array_overlap_explaination.md": ["design", "ordered-set", "segment-tree"],
    "backtrack.md": ["backtracking", "recursion"],
    "Bellman-Ford.md": ["shortest-path", "graph", "dynamic-programming"],
    "bfs.md": ["breadth-first-search", "graph"],
    "binary_indexed_tree.md": ["binary-indexed-tree", "segment-tree"],
    "binary_search.md": ["binary-search"],
    "binary_tree.md": ["binary-tree", "tree"],
    "bit_manipulation.md": ["bit-manipulation", "bitmask"],
    "bst.md": ["binary-search-tree", "binary-tree"],
    "Collection.md": ["array", "hash-table", "linked-list", "heap-priority-queue"],
    "combinatorics_math_patterns.md": ["combinatorics", "math", "number-theory"],
    "concurrency_patterns.md": ["concurrency"],
    "design.md": ["design", "data-stream"],
    "dfs.md": ["depth-first-search", "graph"],
    "diff_toposort_quickunion.md": ["topological-sort", "union-find"],
    "difference_array.md": ["prefix-sum", "array"],
    "Dijkstra.md": ["shortest-path", "graph", "heap-priority-queue"],
    "dp.md": ["dynamic-programming", "memoization"],
    "dp_pattern.md": ["dynamic-programming"],
    "Floyd-Warshall.md": ["shortest-path", "graph", "dynamic-programming"],
    "graph.md": ["graph"],
    "greedy.md": ["greedy"],
    "hash_map.md": ["hash-table"],
    "hashing.md": ["hash-table", "counting", "hash-function"],
    "heap.md": ["heap-priority-queue"],
    "intervals.md": ["sorting", "greedy", "sweep-line"],
    "iterator.md": ["iterator", "design"],
    "kadane_algorithm.md": ["dynamic-programming", "array"],
    "linked_list.md": ["linked-list", "doubly-linked-list"],
    "math.md": ["math", "number-theory"],
    "matrix.md": ["matrix"],
    "monotonic_queue.md": ["monotonic-queue", "sliding-window", "queue"],
    "monotonic_stack.md": ["monotonic-stack", "stack"],
    "n_sum.md": ["two-pointers", "hash-table", "sorting"],
    "ood_design.md": ["design"],
    "palindrome.md": ["string", "two-pointers", "dynamic-programming"],
    "prefix_sum.md": ["prefix-sum"],
    "priority_queue.md": ["heap-priority-queue"],
    "python_gotchas.md": ["concurrency"],
    "queue.md": ["queue"],
    "recursion.md": ["recursion"],
    "recursion_to_dp.md": ["recursion", "memoization", "dynamic-programming"],
    "scanning_line.md": ["sweep-line", "sorting", "prefix-sum"],
    "segment_tree.md": ["segment-tree", "binary-indexed-tree"],
    "set.md": ["hash-table", "ordered-set"],
    "shortest_path_comparison.md": ["shortest-path", "graph"],
    "sliding_window.md": ["sliding-window", "two-pointers"],
    "sort.md": ["sorting", "merge-sort", "counting-sort", "bucket-sort", "radix-sort", "quickselect"],
    "stack.md": ["stack", "monotonic-stack"],
    "stock_trading.md": ["dynamic-programming", "array", "greedy"],
    "streaming_algorithms.md": ["data-stream", "heap-priority-queue", "reservoir-sampling"],
    "string.md": ["string"],
    "string_matching_kmp_rolling_hash.md": ["string-matching", "rolling-hash", "hash-function"],
    "topology_sorting.md": ["topological-sort", "graph"],
    "tree.md": ["tree", "binary-tree", "depth-first-search"],
    "tree2.md": ["tree", "binary-tree", "depth-first-search"],
    "tree_backtrack.md": ["tree", "backtracking", "depth-first-search"],
    "trie.md": ["trie", "string"],
    "union_find.md": ["union-find", "graph"],
}

# Docs with no meaningful LC topic tag (language tricks, complexity theory,
# pattern indexes). Listed explicitly so nothing is silently missed.
SKIP = {
    "00_template.md",
    "code_interview_general_cheatsheet.md",
    "complexity_cheatsheet.md",
    "complexity_drills.md",
    "java_trick.md",
    "lc_category.md",
    "lc_pattern.md",
    "python_trick.md",
    "time_space_complexity.md",
}

def build_block(tags):
    lines = ["## " + SECTION_TITLE, ""]
    for tag in tags:
        lines.append(f"- [{TAG_NAMES[tag]}](https://leetcode.com/problem-list/{tag}/)")
    lines.append("")
    return "\n".join(lines)


FENCE_RE = re.compile(r"^\s*(`{3,}|~{3,})")
SUBHEADING_RE = re.compile(r"^#{2,6} ")
HEADING_RE = re.compile(r"^#{1,6} ")
SECTION_RE = re.compile(r"^## " + re.escape(SECTION_TITLE) + r"\s*$")


def code_line_flags(lines):
    """Flag every line that belongs to a fenced code block (fence lines included).

    A fence closes only on the same character with a marker at least as long as
    the one that opened it, so a ``` example nested inside a ```` block doesn't
    end the outer block early.
    """
    flags = []
    fence = None  # (char, length) of the currently open fence
    for line in lines:
        match = FENCE_RE.match(line)
        if match:
            marker = match.group(1)
            if fence is None:
                fence = (marker[0], len(marker))
            elif marker[0] == fence[0] and len(marker) >= fence[1]:
                fence = None
            flags.append(True)
        else:
            flags.append(fence is not None)
    return flags


def strip_block(lines):
    """Drop a previously inserted block: its heading plus the lines under it.

    Stops at the next heading of ANY level (stopping only at `## ` would swallow
    a following `### ` section on re-run). Only headings outside fenced code
    count, so a markdown example of the section inside a fence stays intact.
    """
    in_code = code_line_flags(lines)
    kept = []
    dropping = False
    for i, line in enumerate(lines):
        if dropping:
            if in_code[i] or not HEADING_RE.match(line):
                continue
            dropping = False
        if not in_code[i] and SECTION_RE.match(line):
            dropping = True
            continue
        kept.append(line)
    return kept


def insert_block(text, block):
    """Put block right after the doc's intro, before its first sub-heading.

    Anything above the first `##`/`###`/... heading is the doc's intro (subtitle,
    image, ref bullets), so the block becomes the doc's first real section.
    Headings inside fenced code blocks don't count.
    """
    lines = strip_block(text.split("\n"))
    in_code = code_line_flags(lines)

    insert_at = None
    for i, line in enumerate(lines):
        if not in_code[i] and SUBHEADING_RE.match(line):
            insert_at = i
            break

    if insert_at is None:
        # No sub-heading anywhere: place after the H1 plus any immediately
        # following subtitle / image lines.
        insert_at = 0
        for i, line in enumerate(lines):
            if line.startswith("# "):
                insert_at = i + 1
                break
        while insert_at < len(lines):
            stripped = lines[insert_at].strip()
            if stripped and not stripped.startswith((">", "<p", "<img")):
                break
            insert_at += 1

    head = "\n".join(lines[:insert_at]).rstrip("\n")
    tail = "\n".join(lines[insert_at:]).lstrip("\n")
    return f"{head}\n\n{block}\n{tail}"


GRAPHQL_URL = "https://leetcode.com/graphql/"
BROWSER_UA = (
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
)


def fetch_tag(slug):
    """Return LeetCode's display name for a topic tag slug, or None if it doesn't exist."""
    payload = json.dumps(
        {
            "query": "query t($slug:String!){topicTag(slug:$slug){name slug}}",
            "variables": {"slug": slug},
        }
    ).encode()
    req = urllib.request.Request(
        GRAPHQL_URL,
        data=payload,
        headers={
            "Content-Type": "application/json",
            "User-Agent": BROWSER_UA,
            "Origin": "https://leetcode.com",
            "Referer": "https://leetcode.com/problemset/",
        },
    )
    try:
        with urllib.request.urlopen(req, timeout=20) as resp:
            tag = json.load(resp)["data"]["topicTag"]
    except (urllib.error.URLError, KeyError, TypeError, ValueError) as exc:
        print(f"  {slug}: request failed ({exc})", file=sys.stderr)
        return None
    return tag["name"] if tag else None


def verify():
    used = sorted({t for tags in DOC_TAGS.values() for t in tags})
    problems = []
    for slug in used:
        official = fetch_tag(slug)
        if official is None:
            problems.append(f"{slug}: not a LeetCode topic tag")
        elif official != TAG_NAMES[slug]:
            problems.append(f"{slug}: label {TAG_NAMES[slug]!r} != LeetCode {official!r}")
    for line in problems:
        print(f"FAIL {line}")
    print(f"\n{len(used) - len(problems)}/{len(used)} tag(s) verified")
    return 1 if problems else 0


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--dry-run", action="store_true")
    parser.add_argument("--verify", action="store_true", help="validate slugs against LeetCode, don't edit files")
    parser.add_argument(
        "--dir",
        default=os.path.join(os.path.dirname(os.path.dirname(os.path.abspath(__file__))), "doc", "cheatsheet"),
    )
    args = parser.parse_args()

    if args.verify:
        return verify()

    present = {f for f in os.listdir(args.dir) if f.endswith(".md")}
    unmapped = present - set(DOC_TAGS) - SKIP
    missing = (set(DOC_TAGS) | SKIP) - present
    if unmapped:
        print(f"warning: no tag mapping for: {sorted(unmapped)}", file=sys.stderr)
    if missing:
        print(f"warning: mapped file not found: {sorted(missing)}", file=sys.stderr)

    changed = 0
    for name in sorted(DOC_TAGS):
        path = os.path.join(args.dir, name)
        if not os.path.exists(path):
            continue
        with open(path, encoding="utf-8") as fh:
            original = fh.read()
        updated = insert_block(original, build_block(DOC_TAGS[name]))
        if updated == original:
            continue
        changed += 1
        if args.dry_run:
            print(f"would update {name}: {', '.join(DOC_TAGS[name])}")
        else:
            with open(path, "w", encoding="utf-8") as fh:
                fh.write(updated)
            print(f"updated {name}: {', '.join(DOC_TAGS[name])}")

    print(f"\n{changed} file(s) {'to change' if args.dry_run else 'changed'}; {len(SKIP)} skipped (no LC topic tag)")


if __name__ == "__main__":
    sys.exit(main() or 0)
