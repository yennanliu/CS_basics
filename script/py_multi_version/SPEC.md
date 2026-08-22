# TASK SPEC — add missing solution versions to LeetCode Python files

## Where to work

**ALL edits go in the git worktree named in your prompt**, under its
`leetcode_python/` directory. Paths in your batch list are relative to that.
NEVER edit `/Users/jliu/CS_basics/` — that is the user's main checkout.

Do not `git add`, `git commit`, `git checkout`, or `git stash`. Just edit files.
Do not create any new file inside the repo. Scratch/test files go in
`/private/tmp/claude-501/-Users-jliu-CS-basics/f482e3d0-7b21-4ad0-b061-bd154a13dca7/scratchpad/work/`.

## Goal

Each assigned file must end up with **at least 3 implementation blocks**, each one a
genuinely different algorithmic approach, each correct and each annotated with complexity.

## File anatomy (existing house convention — follow it exactly)

```python
"""

<LC number>. <Problem Title>
<Difficulty>

<problem statement, examples, constraints — pasted from LeetCode>

"""

# V0
# IDEA : <APPROACH NAME IN CAPS>
# time = O(n)
# space = O(1)
class Solution(object):
    def someMethod(self, nums):
        ...


# V0-1
# IDEA : <A DIFFERENT APPROACH IN CAPS>
# time = O(n log n)
# space = O(n)
class Solution(object):
    def someMethod(self, nums):
        ...
```

Note: multiple `class Solution` definitions in ONE file is intentional in this repo.
Do not rename them, do not merge them, do not add `Solution2`.

## What you must do per file

1. Read the whole file. Identify the existing implementation block(s) and what approach
   each already uses.
2. **Leave every existing block byte-for-byte unchanged.** Do not reformat, re-indent,
   reword comments, or "improve" existing code. Your job is purely additive.
   The one exception: if an existing block is missing `# time =` / `# space =`, add those
   two comment lines to its header.
3. Append the new block(s) needed to reach 3 implementations, placed AFTER the last
   existing implementation block but BEFORE any trailing placeholder markers that contain
   only a URL or commented-out C++/Java (leave those where they are).
4. Marker naming: continue the existing scheme without colliding.
   - If the file already has `V0`, add `V0-1`, then `V0-2`.
   - If `V0-1` is taken, use the next free suffix (`V0-2`, `V0-3`, ...).
   - Never reuse a marker string that already appears in the file.
5. Every new block header is exactly these 4 comment lines, in this order:
   ```
   # V0-1
   # IDEA : <APPROACH NAME IN CAPS>
   # time = O(...)
   # space = O(...)
   ```
   You may add extra `#` explanation lines after the IDEA line if the trick is subtle.
   Complexity must be the honest complexity of THAT block, not copied from V0.
6. Separate blocks by exactly two blank lines.

## What counts as a genuinely different approach

Good axes of variation (pick ones that actually apply to the problem):

- brute force O(n^2) vs optimal O(n)
- hash map vs sorting vs two pointers
- iterative vs recursive
- DP top-down memoization vs bottom-up tabulation vs O(1)-space rolling variables
- prefix sum vs sliding window
- BFS vs DFS vs union-find
- heap vs bucket sort vs quickselect
- bit manipulation vs arithmetic
- one-pass vs two-pass
- built-in/Pythonic one-liner (`collections.Counter`, `itertools`, slicing) vs explicit loop
  — this counts ONLY when the underlying mechanism differs, not as a cosmetic respelling

**NOT acceptable** (this is the exact anti-pattern the repo is trying to remove):
- the same algorithm with renamed variables
- `for i in range(len(x))` vs `for v in x` over identical logic
- a copy of an existing block with the comments changed
- a wrapper that just calls the other approach

## Correctness is mandatory

Every new solution MUST actually work. For each new block:

1. Extract the examples from the file's docstring.
2. Write a throwaway harness under
   `/private/tmp/claude-501/-Users-jliu-CS-basics/f482e3d0-7b21-4ad0-b061-bd154a13dca7/scratchpad/work/`
   that imports/execs your new class and runs every docstring example.
3. Run it with `python3`. Fix until all examples pass.
4. Delete nothing from the repo; the harness stays in scratchpad.

For problems where a standalone harness is impractical (interactive/design problems with
a judge-provided API, e.g. `Street`, `isBadVersion`, `Celebrity`, iterator/design classes),
write a small mock of that API in your harness and test against it. Do not skip testing.

Python 3 only: no `xrange`, no `print x`, no `.iteritems()`. If the file's EXISTING code
uses those, leave it alone (a separate task covers that) — just don't introduce them.

## Formatting rules for the code you add

- 4-space indent, spaces only, never tabs
- no trailing whitespace on any line you write
- file ends with exactly one newline
- keep lines under ~88 chars where practical
- assume LeetCode-provided names (`List`, `TreeNode`, `ListNode`, `Optional`) are available
  without import, matching the rest of the repo; DO import stdlib you use
  (`collections`, `heapq`, `bisect`, `functools`) inside or above the block, consistent
  with how neighbouring files in the same directory do it

## When 3 real approaches genuinely do not exist

A few problems admit exactly one sensible method (interactive walk problems, trivial
one-line arithmetic, problems whose only alternative is strictly worse in a way that
teaches nothing). In that case:

- add whatever genuinely distinct approaches DO exist (so 2 blocks instead of 3), and
- report that file in your final output under `UNDER_3:` with a one-line reason.

Do NOT pad a file with a fake variant to hit the number. Under-delivering with a stated
reason is correct; padding is not.

## Final output format

Return ONLY this, no prose:

```
DONE: <path> | <n_before> -> <n_after> | <approach names added>
DONE: ...
UNDER_3: <path> | <n_after> | <reason>
FAILED: <path> | <what went wrong>
```
