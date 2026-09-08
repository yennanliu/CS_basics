---
name: add-lc
description: File a LeetCode solution into this repo the way the existing ones are filed — find the problem's real slug, write the Python file in the house layout (problem docstring, V0 with an IDEA block and a time/space line), smoke-test it against the examples, and insert the README row. Use when asked to "add LC <number>", to file a problem just solved in a weekly contest, to turn a pasted draft into a committed solution, or to wire an existing solution file into README. Triggers - "add LC 4038 to Hash_table", "/add-lc 239 Sliding_Window", "add this solution and update the README", "file yesterday's contest Q1".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add a LeetCode solution

Turn an LC number (plus, usually, a draft the user already wrote) into a committed-quality
solution file **and** its README row, in the shape the other ~826 Python files already use.

**Invocation**: `/add-lc <LC number> <pattern dir>` — e.g. `/add-lc 4038 Hash_table`.
A reference or draft solution pasted under the command is used as `V0`; with no draft, the
draft is usually already in the contest scratch file (step 1).

Missing arguments are inferred, not asked about: the pattern dir from the technique the
solution actually uses, the language from where the draft came from (default Python).
Ask only if the number itself is missing or the dir is genuinely ambiguous.

## Prime directives

1. **The problem page is the authority on the title; the class name is not.** LC 4038's
   method is `countSpecialIntegers` and its problem is *Count Integers Appearing in a
   Single Block*. A slug guessed from a method name lands on the wrong file name and a
   dead README link.
2. **Copy a neighbour, never invent the layout.** Every file in a pattern dir has the same
   shape. Read one from the target dir before writing.
3. **The user's approach is the solution.** When a draft is pasted, fix its bugs and keep
   its idea as `V0`. Replacing it with a cleverer one teaches nothing and hides the bug
   they wanted found.
4. **Untested is unfinished.** Run the docstring's own examples before reporting done.
5. **Say what was assumed.** Difficulty inferred from contest position, examples written
   from the rule rather than copied from LC — state it, so it can be corrected.

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
        # edge
        if not nums:
            return 0
        ...
```

Rules that are not negotiable:

- The docstring block is **first**, before any import.
- `# V0` is the canonical solution. `# IDEA : ...` header, indented *why*, then
  `# time = O(...), space = O(...)` as the last comment line before the code.
- LeetCode's `:type: / :rtype:` docstring stays on the method — these files are pasted
  back into LC.
- `# NOTE !!!` marks the one line a reader would get wrong. Use it once, not five times.
- Python 2-style `class Solution(object)` — it is what the whole tree uses.

### 4. A second variant needs a stated reason

One canonical solution per problem. A second one is justified only by a **different
complexity, a distinct trick, or a different language idiom** — not a different spelling of
the same loop. When justified it is `# V0-1` (a variation on V0) or `# V1` (a genuinely
different approach), each with its own `IDEA` and `time =` line, and the class is named
`Solution2` / `Solution3` so the file still imports.

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

Every variant gets the same call, and they must agree. If an example from the problem
statement disagrees, the solution is wrong — say so rather than adjusting the example.

### 6. Add the README row

Find the pattern's table and insert in **ascending LC-number order** (a 4-digit contest
problem goes at the end of that table):

```bash
grep -n "leetcode_python/<Pattern_Dir>" README.md | tail -5
```

```text
| <num> | [<Title>](<leetcode url>) | [Python](./leetcode_python/<Dir>/<slug>.py) | _O(t)_ | _O(s)_ | <Difficulty> | **<pattern>**, <tags>, LC weekly | AGAIN(1) |
```

- Match the spacing of the rows already there. `git show <sha> -- README.md` on a previous
  `update <NNN> py` commit shows the exact column shape.
- Add a second `Java` link into the same cell **only if that file exists under
  `leetcode_java/`** — check, do not assume the pair.
- Tags: the pattern in bold first, then the trick worth grepping for later
  (`hashmap`, `prefix sum`, `span == cnt trick`), then `LC weekly` for a contest problem,
  then company tags in backticks if known.
- Status column: `AGAIN(1)` for a first pass. Never downgrade a status the user has set.

### 7. Report what was assumed

Close with the file, the README line number, the test results, and every inference:
difficulty from contest position, examples written from the rule because `ws.txt` kept only
the statement, a Java link deliberately omitted.

## Do not

- ❌ invent the file layout — copy a neighbour (step 2)
- ❌ guess the slug from the method name (step 1)
- ❌ hand back untested code (step 5)
- ❌ rewrite the user's approach into your own
- ❌ touch `data/progress.txt` — the practice log is the user's own record and gets its own
  commit
- ❌ commit or push unless asked

## Worked example

`add-lc 4038 Hash_table`, with the user's draft pasted:

| Step | What it produced |
|---|---|
| 1 | `weekly_517/ws.txt` → slug `count-integers-appearing-in-a-single-block`, first of 4038/4039/4040/4041 → Easy |
| 2 | read `count-special-triplets.py` from the same dir |
| 3 | wrote the file; `IDEA` explains why `last - first + 1 == count` **is** the contiguity test |
| 4 | added `V0-1` (keep only `first/last/cnt`) — justified: `O(distinct)` space, not `O(n)` cells |
| 5 | `[1,1,2,2,3]→3`, `[1,2,1]→1`, `[5]→1`, `[1,2,1,3,3,2]→1`, `[]→0`, both variants agreeing |
| 6 | README row inserted after LC 4007, the last row of the hash-table table |
| 7 | flagged: difficulty inferred from contest position, examples written from the rule |
