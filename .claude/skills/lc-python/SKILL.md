---
name: lc-python
description: File a LeetCode **Python** solution into this repo the way the existing ones are filed — find the problem's real slug, write the Python file in the house layout (problem docstring, V0 with an IDEA block and a time/space line; the standard, readable approach with explanatory comments and no clever syntactic sugar; every other good approach filed as V1, V2, …), smoke-test it against the examples, and insert the README row. Use when asked to "add LC <number>" in Python, to file a problem just solved in a weekly contest, to turn a pasted draft into a committed solution, or to wire an existing solution file into README. For the Java counterpart use lc-java instead. Triggers - "add LC 4038 to Hash_table", "/lc-python 239 slide_window", "add this python solution and update the README", "file yesterday's contest Q1".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add a LeetCode Python solution

Turn an LC number (plus, usually, a draft the user already wrote) into a committed-quality
Python solution file **and** its README row, in the shape the other ~826 files under
`leetcode_python/` already use.

**Invocation**: `/lc-python <LC number> <pattern dir>` — e.g. `/lc-python 4038 Hash_table`.
A reference or draft solution pasted under the command is used as `V0`; with no draft, the
draft is usually already in the contest scratch file (step 1).

Missing arguments are inferred, not asked about: the pattern dir from the technique the
solution actually uses. Ask only if the number itself is missing or the dir is genuinely
ambiguous.

**Wrong skill?** This one owns `leetcode_python/` only. A Java solution goes to
[`/lc-java`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-java/SKILL.md) — it files into
`leetcode_java/src/main/java/LeetCodeJava/` and *updates* the README row this skill created
rather than adding a second one. If the user pasted Java, say so and switch rather than
translating it to Python.

## Prime directives

1. **The problem page is the authority on the title; the class name is not.** LC 4038's
   method is `countSpecialIntegers` and its problem is *Count Integers Appearing in a
   Single Block*. A slug guessed from a method name lands on the wrong file name and a
   dead README link.
2. **Copy a neighbour, never invent the layout.** Every file in a pattern dir has the same
   shape. Read one from the target dir before writing.
3. **The user's approach is the solution.** When a draft is pasted, fix its bugs and keep
   its idea as `V0`. Replacing it with a cleverer one teaches nothing and hides the bug
   they wanted found. Keeping the *idea* does not mean keeping the *spelling*: a draft
   written as a dense one-liner is rewritten to the readable form of directive 7, with the
   same algorithm underneath.
4. **Untested is unfinished.** Run the docstring's own examples before reporting done.
5. **Say what was assumed.** Difficulty inferred from contest position, examples written
   from the rule rather than copied from LC — state it, so it can be corrected.
6. **README has TWO sets of topic tables; only the main one takes a new row.** The `##`
   headings near the top are the real index. Everything under `## Newly Added (kamyu104
   gap)` is an *imported* index with its own `###` sub-tables whose every row carries the
   status `imported` — and it is the bigger of the two (1971 rows against 1310),
   duplicating **23 topic names** (`### Stack`, `### Math`, `### Array`, …). Every one of
   the 17 most recent contest rows (LC 3964-4054) lives in a main `##` table and none in
   the imported one. A row filed there renders fine and is ~2500 lines from where the user
   looks; `script/check_readme.py` and the site build both fail on it now (a row under
   that heading without the `imported` cell), but only after the fact.
7. **Readable beats clever.** These files are read back months later, under interview
   pressure, by someone who has to *say* the solution out loud. Every block is written the
   way a strong candidate would write it on a whiteboard and a reviewer would approve it in
   production: the **standard, well-known approach** for the problem, explicit loops and
   named intermediate variables, a comment on every step that is not obvious, and no
   syntactic sugar that has to be decoded before the idea can be seen (step 3 lists what
   that means). When two spellings run in the same complexity, the plainer one wins.
8. **Every good approach is worth filing.** If the problem has more than one solution that
   passes LC's constraints and is worth knowing in an interview — BFS *and* DFS, a heap
   *and* quickselect, top-down memo *and* bottom-up DP — file each as its own version
   (`V0`, `V1`, `V2`, …), not only the one the user happened to write (step 4).

## The steps

### 1. Find the problem's source of truth

For a contest problem it is `leetcode_python/lc_weekly/weekly_<n>/ws.txt` — it records the
LC number, the problem URL, the user's idea notes and their draft (often with the bug they
hit, then a `(fixed)` version below it):

```bash
grep -rn "LC <number>" leetcode_python/lc_weekly/*/ws.txt
sed -n '/# LC <number>/,/^# LC /p' leetcode_python/lc_weekly/weekly_<n>/ws.txt
```

Two things to read out of it:

- **The URL slug is the file name** — `count-integers-appearing-in-a-single-block`.
- **Position implies difficulty.** The LC numbers appear in contest order, so the first is
  Q1 (Easy), then Medium, Medium/Hard, Hard. Mark it as an assumption in the report.

If the user pasted a URL or a full problem statement instead, that is the source of truth
and this step is already done.

### 2. Read a neighbour before writing

```bash
ls leetcode_python/<Pattern_Dir>/
git log --oneline -12 --name-only        # the last few files added, in any dir
```

Open one recent file from the **target dir** and match it. The layout below is the current
house shape, but the dir wins if it has drifted.

### 3. Write `leetcode_python/<Pattern_Dir>/<slug>.py`

```python
"""

<number>. <Exact Problem Title>
<Difficulty>

<the statement, as prose — wrapped, not one long line>

Example 1:

Input: nums = [1,1,2,2,3]

Output: 3

Explanation:

<why>

Constraints:

1 <= nums.length <= 10^5

"""

# V0
# IDEA : <ONE LINE — THE TECHNIQUE, IN CAPS LIKE ITS NEIGHBOURS>
#
#   <2-6 indented lines saying WHY the trick is correct — the invariant, not a
#   restatement of the loop. A worked mini-example earns its place here:>
#
#   e.g. nums = [1,2,1] -> idx of 1 = [0,2] -> 2 - 0 + 1 = 3 != 2 -> NOT special
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def <lcMethodName>(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge case: nothing to count
        if not nums:
            return 0

        # value -> every index it appears at
        positions = defaultdict(list)
        for idx, val in enumerate(nums):
            positions[val].append(idx)

        count = 0
        for val, idx_list in positions.items():
            first = idx_list[0]
            last = idx_list[-1]
            # NOTE !!! the occurrences form ONE block exactly when the span
            #          they cover is as long as the number of occurrences
            if last - first + 1 == len(idx_list):
                count += 1

        return count
```

Rules that are not negotiable:

- The docstring block is **first**, before any import.
- `# V0` is the canonical solution. `# IDEA : ...` header, indented *why*, then
  `# time = O(...), space = O(...)` as the last comment line before the code.
- LeetCode's `:type: / :rtype:` docstring stays on the method — these files are pasted
  back into LC.
- `# NOTE !!!` marks the one line a reader would get wrong. Use it once, not five times.
- Python 2-style `class Solution(object)` — it is what the whole tree uses.

**Readability rules** (directive 7) — they apply to every version, `V0` included:

- **The standard approach first.** `V0` is the solution a reader would find in a textbook or
  an editorial for this pattern — two pointers, BFS, a monotonic stack, 1D DP — not a
  problem-specific trick that only works once you have seen it. A trick that genuinely
  beats the standard bound is filed too, as a later version (step 4).
- **No syntactic sugar that hides the logic.** Write it out instead of:
  - nested or side-effecting comprehensions, and `sum(1 for ...)` / `any(...)` / `all(...)`
    wrapped around non-trivial logic — a plain loop with a counter or an early `return`;
  - `lambda` / `map` / `filter` / `reduce` chains — a loop, or a small named helper.
    A short `key=lambda x: x[0]` on a sort is fine; a lambda that holds the algorithm is not;
  - the walrus operator `:=`, chained or nested ternaries, `a and b or c`;
  - `zip(*grid)`, `[::-1]` slices or `*` unpacking used as a trick rather than to say what
    they say, unless a comment names what they do;
  - bit tricks (`x & -x`, `~i`, `n & (n - 1)`) with no comment saying what they compute;
  - `@lru_cache` / `@cache` when the memo is the point of the solution — an explicit
    `memo = {}` shows the state being cached. (Fine in a later version, with a comment.)
- **Names say what they hold.** `left` / `right`, `window_sum`, `in_degree`, `visited` —
  not `l`, `r`, `s`, `d`, `tmp`, `res2`. Loop indices `i` / `j` and grid coordinates
  `r` / `c` are the exceptions.
- **Comments explain why, not what.** One short comment above each logical step — the
  invariant a loop keeps, why a pointer moves, what a DP cell *means*, why a `+ 1` is there.
  Do not narrate syntax (`# increment i`). A DP solution states the recurrence and the base
  case in comments; a graph solution states what a node and an edge are.
- **Production-ready.** Handle the edges the constraints allow (empty, single element,
  all-same, negatives) explicitly and early; do not mutate an input argument unless the
  problem allows it and a comment says so; no `print`, no dead code, no commented-out
  attempts; a helper is a named method with a one-line docstring, not a nested lambda; a
  magic number becomes a named constant (`MOD = 10**9 + 7`).

### 4. File every good approach as its own version

When the problem has **several solutions that all pass LC's constraints and are each worth
knowing**, file all of them (directive 8). Before writing, list the approaches the problem
is known for — the editorial's, the standard ones for its pattern — and keep every one that
is a genuinely different idea:

| Marker | What it holds |
|---|---|
| `# V0` | The canonical one — the user's own approach when a draft was given, otherwise the most standard, easiest-to-explain solution. |
| `# V0-1`, `# V0-2` | A variation on `V0`'s idea — same technique, a better bound or a cleaner state (e.g. rolling 1D DP after a 2D table). |
| `# V1`, `# V2`, … | A **different approach** that also solves the problem — BFS after DFS, heap after sort, bottom-up DP after top-down memo, union-find after DFS. Ordered from most standard to least. |

- Each version has its own `IDEA` header, its own *why* lines, and its own
  `time = O(...), space = O(...)` line, so the trade-off between versions is readable from
  the comments alone. Say in the `IDEA` when a version is the one to use in an interview,
  and when it is not (`# NOTE : O(n) but hard to derive live — learn V0 first`).
- The readability rules of step 3 apply to every version, not only `V0`.
- A brute force that **times out** on the constraints is not a version. Mention it in one
  comment line inside `V0`'s *why* block if it is the natural stepping stone.
- Do **not** pad: two spellings of the same loop, or the same algorithm with a different
  container, are one approach. Most problems end up with one to three versions.
- The class is named `Solution`, then `Solution2`, `Solution3`, … in file order, so the
  file still imports and step 5 can call every version.

### 5. Smoke-test before reporting done

The file name has dashes, so it is not importable — load it by path and run the docstring's
examples plus the edges (empty, single element, all-same):

```bash
python3 -c "
import importlib.util
spec = importlib.util.spec_from_file_location('m', 'leetcode_python/<Dir>/<slug>.py')
m = importlib.util.module_from_spec(spec); spec.loader.exec_module(m)
s = m.Solution()
print([s.<method>(x) for x in ([1,1,2,2,3], [1,2,1], [5], [])])"
```

Every version gets the same call, and they must agree. If an example from the problem
statement disagrees, the solution is wrong — say so rather than adjusting the example.

### 6. Add the README row

Find the pattern's table and insert in **ascending LC-number order** (a 4-digit contest
problem goes at the end of that table).

**Anchor on the heading, never on the solution path.** Grepping the path matches both table
sets, and `tail` then hands back whichever one sits *later* in the file — which is always
the imported `### ` one (directive 6). Locate the heading first:

```bash
grep -n "^## \|^### " README.md | grep -i "stack"   # -> two hits: "## Stack" AND "### Stack"
```

Take the `## ` line. Its table runs to the next heading; read the tail of *that* range and
insert after the last row:

```bash
# <start>/<end> = the "## " heading's line, and the next heading's line
awk 'NR>=536 && NR<=585 && /^\| [0-9]/{print NR": "substr($0,1,60)}' README.md | tail -3
```

If the topic somehow has no `## ` heading, the row still does **not** go in the imported
table — file it under the closest main table and say so in the report.

```text
| <num> | [<Title>](<leetcode url>) | [Python](./leetcode_python/<Dir>/<slug>.py) | _O(t)_ | _O(s)_ | <Difficulty> | **<pattern>**, <tags>, LC weekly | AGAIN(1) |
```

- Match the spacing of the rows already there. `git show <sha> -- README.md` on a previous
  `update <NNN> py` commit shows the exact column shape.
- Add a second `Java` link into the same cell **only if that file already exists under
  `leetcode_java/`** — check, do not assume the pair. Do not write the Java file to make the
  link true. That is [`/lc-java`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-java/SKILL.md)'s
  job, and it edits this same row.
- Tags: the pattern in bold first, then the trick worth grepping for later
  (`hashmap`, `prefix sum`, `span == cnt trick`), then `LC weekly` for a contest problem,
  then company tags in backticks if known.
- Status column: `AGAIN(1)` for a first pass. Never downgrade a status the user has set.
- **Verify the row landed in the main table** before reporting done — print the heading it
  now sits under, which must start with `## ` and not `### `:

```bash
awk -v L=583 'NR<=L && /^#{1,3} /{h=$0} NR==L{print h}' README.md   # -> ## Stack
```

### 7. Report what was assumed

Close with the file, the README line number, the test results, and every inference:
difficulty from contest position, examples written from the rule because `ws.txt` kept only
the statement, a Java link deliberately omitted.

## Do not

- ❌ invent the file layout — copy a neighbour (step 2)
- ❌ guess the slug from the method name (step 1)
- ❌ hand back untested code (step 5)
- ❌ rewrite the user's approach into your own — but do rewrite its *spelling* when it is
  dense (directive 3)
- ❌ syntactic sugar that hides the algorithm — one-liners, nested comprehensions, lambda
  chains, walrus, uncommented bit tricks (step 3)
- ❌ single-letter names for anything but an index, or code with no comment on a non-obvious
  step
- ❌ file only one approach when the problem has several good ones, or pad with a second
  spelling of the same loop (step 4)
- ❌ touch `data/progress.txt` — the practice log is the user's own record and gets its own
  commit
- ❌ file the row under `## Newly Added (kamyu104 gap)` / any `### ` table — main `## ` only
  (directive 6), and prove it with the `awk` check in step 6
- ❌ commit or push unless asked

## Worked example

`/lc-python 4038 Hash_table`, with the user's draft pasted:

| Step | What it produced |
|---|---|
| 1 | `weekly_517/ws.txt` → slug `count-integers-appearing-in-a-single-block`, first of 4038/4039/4040/4041 → Easy |
| 2 | read `count-special-triplets.py` from the same dir |
| 3 | wrote the file; `IDEA` explains why `last - first + 1 == count` **is** the contiguity test |
| 4 | added `V0-1` (keep only `first/last/cnt`) — justified: `O(distinct)` space, not `O(n)` cells |
| 5 | `[1,1,2,2,3]→3`, `[1,2,1]→1`, `[5]→1`, `[1,2,1,3,3,2]→1`, `[]→0`, both variants agreeing |
| 6 | `grep -n "^## \|^### " README.md | grep -i hash` → two hits; took **`## Hash Table`**, not `### Hash Table`; row inserted after LC 4007, its last row; `awk` re-printed `## Hash Table` |
| 7 | flagged: difficulty inferred from contest position, examples written from the rule |
