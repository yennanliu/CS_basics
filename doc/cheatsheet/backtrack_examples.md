# Backtracking — Worked LC Examples

> **Scope** — The backtracking problems that are not a template's anchor (LC 17, 784, 996, 93, 140, 980, 22), each as a problem card filed under the [backtrack.md](./backtrack.md) template it reduces to; the anchors themselves are worked on their cards there, and this sheet teaches no templates of its own.
> **See also**: [backtrack.md](./backtrack.md) — the templates every card here reduces to, and the decision table for picking one; [backtrack_advanced.md](./backtrack_advanced.md) — the hard-tier templates (LC 212, 282, 301); [tree_backtrack.md](./tree_backtrack.md) — root→leaf path problems on a tree; [dfs.md](./dfs.md) — traversal without undo.

## LeetCode Problem Lists

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Recursion](https://leetcode.com/problem-list/recursion/)

## Overview

This is the problem set for [backtrack.md](./backtrack.md): it holds the templates, this sheet
holds the problems. Each card names the template it reduces to and the one step it adds. The
sections follow the core sheet's template order; a template whose problems are all worked on its
own card has no section here.

If a card felt unfamiliar, go back to the part of the core sheet it leans on:

| If this felt unfamiliar | Go back to |
|---|---|
| why `path.pop()`, and when it is not needed | [Template 14](./backtrack.md#template-14-when-to-undo--mutable-vs-immutable-state--lc-113-) |
| `i` vs `i + 1` in the recursive call | [Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1--lc-39-) |
| skipping duplicate values | [Duplicate skipping](./backtrack.md#duplicate-skipping--the-same-level-skip-rule-) |
| `break` vs `continue` when pruning | [Sort, dedup, prune — when](./backtrack.md#sort-dedup-prune--when) |
| picking the right shape from the statement | [Decision Table](./backtrack.md#decision-table--which-backtrack-shape-) |

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 17 | [Letter Combinations of a Phone Number](#letter-combinations-of-a-phone-number--lc-17) | [Template 1](./backtrack.md#template-1-choose--explore--un-choose--lc-216-) choose → explore → un-choose | Medium |
| 784 | [Letter Case Permutation](#letter-case-permutation--lc-784) | [Template 3](./backtrack.md#template-3-subsets--lc-78-) subsets, include / exclude form | Medium |
| 996 | [Number of Squareful Arrays](#number-of-squareful-arrays--lc-996) | [Template 5](./backtrack.md#template-5-permutations--lc-46-) permutations + the LC 47 skip | Hard |
| 93 | [Restore IP Addresses](#restore-ip-addresses--lc-93) | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) choose where to cut | Medium |
| 140 | [Word Break II](#word-break-ii--lc-140) | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) choose where to cut, memoised | Hard |
| 980 | [Unique Paths III](#unique-paths-iii--lc-980) | [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) mark, recurse, restore | Hard |
| 22 | [Generate Parentheses](#generate-parentheses--lc-22) | [Template 12](./backtrack.md#template-12-pruning-at-the-choice--lc-526-) test before recursing | Medium |

### Worked elsewhere

These are worked once, on the card that owns them. They are listed so a search for the number lands somewhere.

| LC | Problem | Home |
|---|---|---|
| 216 | Combination Sum III | [backtrack.md § Template 1](./backtrack.md#template-1-choose--explore--un-choose--lc-216-) (anchor) |
| 39 / 40 | Combination Sum / II | [backtrack.md § Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1--lc-39-) and [§ Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-) (anchors) |
| 78 | Subsets | [backtrack.md § Template 3](./backtrack.md#template-3-subsets--lc-78-) (anchor) |
| 90 | Subsets II | [backtrack.md § Template 4](./backtrack.md#template-4-subsets-ii-skip-same-level-duplicates--lc-90-) (anchor) |
| 46 / 47 | Permutations / II | [backtrack.md § Template 5](./backtrack.md#template-5-permutations--lc-46-) (anchor, LC 47 under it) |
| 77 | Combinations | [backtrack.md § Template 6](./backtrack.md#template-6-combinations--lc-77) (anchor) |
| 79 | Word Search | [backtrack.md § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) (anchor) |
| 526 | Beautiful Arrangement | [backtrack.md § Template 12](./backtrack.md#template-12-pruning-at-the-choice--lc-526-) (anchor) |
| 1219 | Path with Maximum Gold | [dfs.md § Template 9](./dfs.md#template-9-grid-dfs-with-restore-backtracking--lc-1219) (anchor); its mark-lifetime trace is under [backtrack.md § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) |
| 139 | Word Break | [dp.md § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-) (anchor): only *whether* a split exists, so DP, not backtracking |
| 207 | Course Schedule | [topology_sorting_examples.md](./topology_sorting_examples.md): the `visiting` set is cycle detection, not a search for answers |

## Choose → Explore → Un-choose Examples

### Letter Combinations of a Phone Number — LC 17

> **Key point** — the depth is the index into `digits`, and each depth has its **own** choice
> list (the letters of `digits[i]`). Two depths never draw from the same pool, so there is no
> `start_idx` and no `used[]`: the path is complete exactly when it has one letter per digit.

**Reduces to**: [backtrack.md § Template 1](./backtrack.md#template-1-choose--explore--un-choose--lc-216-) — with the choice list read from the current depth instead of `start..n`.

```python
# python
# LC 17 - Letter Combinations of a Phone Number
# IDEA: one depth per digit; the choice list at depth i is the letters of digits[i]
# time = O(4^n * n), space = O(n)   n = len(digits); up to 4 letters per digit, O(n) to join each
def letterCombinations(digits):
    if not digits:
        return []                        # no digits: no combinations, not [""]
    letters = {"2": "abc", "3": "def", "4": "ghi", "5": "jkl",
               "6": "mno", "7": "pqrs", "8": "tuv", "9": "wxyz"}
    res = []
    path = []

    def backtrack(i):
        if i == len(digits):             # one letter chosen for every digit
            res.append("".join(path))
            return
        for ch in letters[digits[i]]:
            path.append(ch)
            backtrack(i + 1)
            path.pop()                   # undo the LAST letter

    backtrack(0)
    return res
```

```java
// java
// LC 17 - Letter Combinations of a Phone Number
// IDEA: one depth per digit; the choice list at depth i is the letters of digits[i]
// time = O(4^n * n), space = O(n)
private static final String[] LETTERS = {
    "", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"
};

public List<String> letterCombinations(String digits) {
    List<String> res = new ArrayList<>();
    if (digits.isEmpty()) {
        return res;                                   // no digits: no combinations, not [""]
    }
    backtrack(digits, 0, new StringBuilder(), res);
    return res;
}

private void backtrack(String digits, int i, StringBuilder path, List<String> res) {
    if (i == digits.length()) {
        res.add(path.toString());
        return;
    }
    String choices = LETTERS[digits.charAt(i) - '0'];
    for (char ch : choices.toCharArray()) {
        path.append(ch);
        backtrack(digits, i + 1, path, res);
        path.deleteCharAt(path.length() - 1);         // undo the LAST char, not the first
    }
}
```

A second solution, kept because it is a different algorithm rather than a different spelling:
build the answer **level by level** with no recursion. It holds every partial combination at once,
so it uses O(4^n) memory where the backtrack holds one path.

```python
# python
# IDEA: iterative product: extend every partial combination by each letter of the next digit
# time = O(4^n * n), space = O(4^n * n)   every partial combination is kept
def letterCombinations(digits):
    if not digits:
        return []
    letters = {"2": "abc", "3": "def", "4": "ghi", "5": "jkl",
               "6": "mno", "7": "pqrs", "8": "tuv", "9": "wxyz"}
    combos = [""]
    for digit in digits:
        extended = []
        for prefix in combos:
            for ch in letters[digit]:
                extended.append(prefix + ch)
        combos = extended
    return combos
```

**Traps**
- `digits = ""` must return `[]`. The base case alone returns `[""]`, one empty combination.
- Undo removes the **last** character. `deleteCharAt(0)` passes `"2"` and fails on `"23"`.

## Subsets Examples

### Letter Case Permutation — LC 784

> **Key point** — despite the name, the order of the characters is fixed: each **letter** is a
> two-way choice (lower or upper) and each digit a one-way one. That is the subsets tree in its
> include / exclude form, one binary decision per position, so there are 2^L leaves for L letters.

**Reduces to**: [backtrack.md § Template 3](./backtrack.md#template-3-subsets--lc-78-) — the include / exclude form, with "lower / upper" as the two branches.

```python
# python
# LC 784 - Letter Case Permutation
# IDEA: fixed order, one decision per index: a letter branches two ways, a digit one way
# time = O(2^L * n), space = O(n)   L = letters in s; O(n) to join each answer
def letterCasePermutation(s):
    res = []
    path = []

    def backtrack(i):
        if i == len(s):
            res.append("".join(path))
            return
        ch = s[i]
        if ch.isalpha():
            options = [ch.lower(), ch.upper()]
        else:
            options = [ch]               # a digit has no choice to make
        for option in options:
            path.append(option)
            backtrack(i + 1)
            path.pop()

    backtrack(0)
    return res
```

**Traps**
- Treating it as Template 5 (permutations) and reordering characters: `"a1b2"` has 4 answers, not
  4! × 4.
- Branching on a digit as well doubles every answer.

## Permutations Examples

### Number of Squareful Arrays — LC 996

> **Key point** — two filters inside the permutation loop, both applied **before** recursing: the
> LC 47 same-level skip (sorted input, skip `nums[i]` when its equal left neighbour is not on the
> path) so equal values are not counted twice, and an adjacency test against `path[-1]` so a
> branch dies the moment a pair fails.

**Reduces to**: [backtrack.md § Template 5](./backtrack.md#template-5-permutations--lc-46-) — with the [LC 47 skip](./backtrack.md#permutations-with-duplicates--lc-47) and a pairwise check.

```python
# python
# LC 996 - Number of Squareful Arrays
# IDEA: permutations with duplicates (sort + same-level skip) + prune on the adjacent pair
# time = O(n!) worst case, far less after pruning, space = O(n)
import math

def numSquarefulPerms(nums):
    nums = sorted(nums)                  # equal values adjacent: the skip needs it
    n = len(nums)
    used = [False] * n
    path = []
    count = 0

    def is_square(x):
        root = math.isqrt(x)             # integer square root: no float rounding
        return root * root == x

    def backtrack():
        nonlocal count
        if len(path) == n:
            count += 1
            return
        for i in range(n):
            if used[i]:
                continue
            # same value as the left neighbour, which is not on the path: same level, skip
            if i > 0 and nums[i] == nums[i - 1] and not used[i - 1]:
                continue
            # the new neighbour pair must sum to a square
            if path and not is_square(path[-1] + nums[i]):
                continue
            used[i] = True
            path.append(nums[i])
            backtrack()
            path.pop()
            used[i] = False

    backtrack()
    return count
```

**Traps**
- Without the sort, the skip compares unrelated neighbours: `[2, 2, 2]` must give `1`.
- `int(x ** 0.5)` can round down on a large perfect square; `math.isqrt` cannot.

## Palindrome Partitioning Examples

### Restore IP Addresses — LC 93

> **Key point** — the same "choose where to cut" loop as LC 131, with a different gate: a piece
> is 1–3 digits, at most 255, and has no leading zero. Two things end a branch: four pieces that
> consumed the **whole** string (record it), or four pieces with digits left over (drop it). The
> prune that makes it fast: with `k` pieces still to place, the digits left must number between
> `k` and `3k`, so a branch that cannot finish is cut before it recurses.

**Reduces to**: [backtrack.md § Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) — the gate is "valid octet", and the depth is capped at 4.

```python
# python
# LC 93 - Restore IP Addresses
# IDEA: cut s into 4 pieces; each piece is 1-3 digits, <= 255, no leading zero;
#       prune when the digits left cannot fill the pieces left (need k..3k digits for k pieces)
# time = O(3^4 * n), space = O(n)   at most 3 choices per piece, 4 pieces, O(n) to join
def restoreIpAddresses(s):
    res = []
    path = []

    def backtrack(start):
        if len(path) == 4:
            if start == len(s):          # every digit used
                res.append(".".join(path))
            return
        pieces_left = 4 - len(path)
        digits_left = len(s) - start
        if digits_left < pieces_left or digits_left > 3 * pieces_left:
            return                       # cannot finish: too few, or too many, digits left
        for end in range(start + 1, min(start + 3, len(s)) + 1):
            piece = s[start:end]
            if len(piece) > 1 and piece[0] == "0":
                break                    # "0x" is invalid, and so is every longer piece
            if int(piece) > 255:
                break                    # longer pieces are larger still
            path.append(piece)
            backtrack(end)
            path.pop()

    backtrack(0)
    return res
```

**Dry run** — `s = "0000"`:

```text
path              start  pieces_left  digits_left  action
[]                0      4            4            try "0" (len 1); "00" breaks on the leading zero
["0"]             1      3            3            try "0"
["0","0"]         2      2            2            try "0"
["0","0","0"]     3      1            1            try "0"
["0","0","0","0"] 4      -            -            4 pieces and start == len(s) -> record "0.0.0.0"
```

**Traps**
- `"0000"` → `["0.0.0.0"]`: a single `0` is a valid piece; only a **longer** piece may not start
  with `0`.
- Recording at four pieces without checking `start == len(s)` returns addresses that drop digits.
- Checking only `len(s) > 12` up front prunes nothing below the root: on `"1111111111"` (10
  digits) the cut `["1"]` leaves 9 digits for 3 pieces, which is already a dead branch. The
  per-call `k..3k` bound drops it immediately.

### Word Break II — LC 140

> **Key point** — cut the string where the prefix is a dictionary word, then solve the suffix.
> The suffix starting at `start` has the same sentences whichever way the prefix was cut, so cache
> them by `start`. Without the cache, `"aaaa…aaab"` with `["a", "aa", "aaa"]` re-solves the same
> suffixes exponentially often and still finds nothing.

**Reduces to**: [backtrack.md § Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) — the gate is `piece in words`, and the recursion returns the suffix's sentences so they can be memoised.

```python
# python
# LC 140 - Word Break II
# IDEA: cut at every dictionary prefix; memoise the sentences of each suffix by its start index
# time = O(n^3) for the n x n slices of up to n chars, plus building every cached sentence
#        (exponential in n in the worst case); space = O(n^2) chars in memo even for one answer
#        ("a" * n with ["a"]: memo[i] holds a sentence of length ~2(n - i)), plus the output
def wordBreak(s, wordDict):
    words = set(wordDict)                # O(1) membership
    memo = {}                            # start -> every sentence that spells s[start:]

    def sentences_from(start):
        if start == len(s):
            return [""]                  # one way to spell the empty suffix
        if start in memo:
            return memo[start]
        sentences = []
        for end in range(start + 1, len(s) + 1):
            word = s[start:end]
            if word not in words:
                continue
            for rest in sentences_from(end):
                if rest:
                    sentences.append(word + " " + rest)
                else:
                    sentences.append(word)
        memo[start] = sentences
        return sentences

    return sentences_from(0)
```

**Traps**
- Looping over the dictionary and testing the joined prefix (instead of slicing `s`) builds
  sentences longer than `s` before rejecting them.
- If only *whether* a split exists is asked, this is LC 139: a boolean DP,
  [dp.md § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-).

## Grid Examples

### Unique Paths III — LC 980

> **Key point** — the LC 79 mark → recurse → restore skeleton, but every path must be explored to
> the end, so there is no early return: count instead. A path counts only when it reaches the end
> cell having stepped on **every** walkable cell, so a `remaining` counter rides down the recursion.

**Reduces to**: [backtrack.md § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) — returning a count, with a "covered every cell" condition at the end.

```python
# python
# LC 980 - Unique Paths III
# IDEA: grid backtrack from the start; count arrivals at the end that used every walkable cell
# time = O(3^(m*n)), space = O(m*n)   after the first step at most 3 unvisited directions
def uniquePathsIII(grid):
    rows, cols = len(grid), len(grid[0])
    walkable = 0                         # start + end + empty cells
    start_r = start_c = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] != -1:
                walkable += 1
            if grid[r][c] == 1:
                start_r, start_c = r, c
    count = 0

    def dfs(r, c, remaining):            # remaining = walkable cells not yet on the path
        nonlocal count
        if grid[r][c] == 2:
            if remaining == 0:           # the end only counts once every cell is covered
                count += 1
            return
        saved = grid[r][c]
        grid[r][c] = -1                  # mark: an obstacle for the calls below
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] != -1:
                dfs(nr, nc, remaining - 1)
        grid[r][c] = saved               # restore after the last direction

    dfs(start_r, start_c, walkable - 1)  # the start cell is already on the path
    return count
```

**Traps**
- Counting every arrival at the end: `[[1,0,0,0],[0,0,0,0],[0,0,2,-1]]` gives far more than `2`.
- Returning early on the first success, as LC 79 does, stops the count at `1`.

## Pruning Examples

### Generate Parentheses — LC 22

> **Key point** — carry two counts and test them **before** placing a bracket: `(` while fewer
> than `n` are open, `)` while fewer closers than openers are placed. Every prefix built is then
> a valid prefix, so every leaf is an answer and nothing is generated only to be thrown away.

**Reduces to**: [backtrack.md § Template 12](./backtrack.md#template-12-pruning-at-the-choice--lc-526-) — the test before recursing is the bracket balance.

```python
# python
# LC 22 - Generate Parentheses
# IDEA: count the brackets placed; '(' while open < n, ')' while close < open
# time = O(4^n / sqrt(n)), space = O(n)   the n-th Catalan number of answers, O(n) each
def generateParenthesis(n):
    res = []
    path = []

    def backtrack(open_count, close_count):
        if len(path) == 2 * n:
            res.append("".join(path))
            return
        if open_count < n:               # an opener is still available
            path.append("(")
            backtrack(open_count + 1, close_count)
            path.pop()
        if close_count < open_count:     # a closer has something to match
            path.append(")")
            backtrack(open_count, close_count + 1)
            path.pop()

    backtrack(0, 0)
    return res
```

```java
// java
// LC 22 - Generate Parentheses
// IDEA: count the brackets placed; '(' while open < n, ')' while close < open
// time = O(4^n / sqrt(n)), space = O(n)
public List<String> generateParenthesis(int n) {
    List<String> res = new ArrayList<>();
    build(n, 0, 0, new StringBuilder(), res);
    return res;
}

private void build(int n, int open, int close, StringBuilder path, List<String> res) {
    if (path.length() == 2 * n) {
        res.add(path.toString());
        return;
    }
    if (open < n) {                                   // an opener is still available
        path.append('(');
        build(n, open + 1, close, path, res);
        path.deleteCharAt(path.length() - 1);
    }
    if (close < open) {                               // a closer has something to match
        path.append(')');
        build(n, open, close + 1, path, res);
        path.deleteCharAt(path.length() - 1);
    }
}
```

**Traps**
- Generating all `2^(2n)` strings and validating each at the leaf is the obvious first idea and
  exponentially wasteful: the two counts make validation unnecessary.
- `close < open`, not `close < n`: the latter allows `")("`.
