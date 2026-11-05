# DP Worked Examples

> **Scope** — The classic DP problems that are not a template's anchor (LC 343, 91, 62, 279, 276, 1137), each as a problem card filed under the [dp.md](./dp.md) template it reduces to, plus an index of where every other common DP problem is worked; no templates or theory of its own.
> **See also**: [dp.md](./dp.md) — the templates these cards reduce to; [knapsack.md](./knapsack.md) — the knapsack family in full; [stock_trading.md](./stock_trading.md) — every stock state-machine variant; [dp_advanced.md](./dp_advanced.md) — the rare techniques and deep dives; [dp_pattern.md](./dp_pattern.md) — a terser one-section-per-pattern template index.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)

## Overview

This is the problem set for [dp.md](./dp.md): it holds the templates, this sheet holds the
problems. Each card states the one thing the problem adds to its template (a second option per
step, a validity test, a different state) and gives one canonical solution. The sections follow
dp.md's template order; a template whose problems are all worked on its own card, or in a
dedicated sheet, has no section here.

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 343 | [Integer Break](#integer-break--lc-343) | [Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) 1-D linear DP, try every last split | Medium |
| 91 | [Decode Ways](#decode-ways--lc-91) | [Template 1a](./dp.md#template-1a-length-indexed-table-n--1-slots--lc-70) length-indexed table | Medium |
| 62 | [Unique Paths](#unique-paths--lc-62) | [Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-) 2-D grid DP, count | Medium |
| 279 | [Perfect Squares](#perfect-squares--lc-279) | [knapsack.md § Template 4](./knapsack.md#template-4-unbounded-min-count--lc-322-) unbounded min count | Medium |
| 276 | [Paint Fence](#paint-fence--lc-276) | [Template 5](./dp.md#template-5-state-machine-dp--lc-309) state machine, two states | Medium |
| 1137 | [N-th Tribonacci Number](#n-th-tribonacci-number--lc-1137) | [Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-) rolling variables | Easy |

### Worked elsewhere

Each is worked once, on the card or in the sheet that owns it.

| LC | Problem | Home |
|---|---|---|
| 53 | Maximum Subarray | [dp.md § Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) (anchor); the Kadane family is [kadane_algorithm.md](./kadane_algorithm.md) |
| 152 | Maximum Product Subarray | [kadane_algorithm.md](./kadane_algorithm.md): track the running max **and** min, since a negative swaps them |
| 70 | Climbing Stairs | [dp.md § Template 1a](./dp.md#template-1a-length-indexed-table-n--1-slots--lc-70) (anchor) |
| 139 | Word Break | [dp.md § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-) (anchor) |
| 64 | Minimum Path Sum | [dp.md § Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-) (anchor) |
| 416 / 494 / 1049 | Partition Equal Subset Sum / Target Sum / Last Stone Weight II | [knapsack.md](./knapsack.md) — Template 1 (anchor) and its LC Examples |
| 322 / 518 / 377 | Coin Change / II / Combination Sum IV | [knapsack.md](./knapsack.md) — Templates 4, 2 and 3 (anchors) |
| 309 | Best Time to Buy and Sell Stock with Cooldown | [dp.md § Template 5](./dp.md#template-5-state-machine-dp--lc-309) (anchor) |
| 121 / 122 / 123 / 188 / 714 | the other stock problems | [stock_trading.md](./stock_trading.md) |
| 198 | House Robber | [dp.md § Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-) (anchor) |

### Not worked here — the one-line reduction

| Problem | LC | Reduces to | One-line idea |
|---|---|---|---|
| Trapping Rain Water | 42 | prefix / suffix arrays | `water[i] = min(maxLeft[i], maxRight[i]) - h[i]`; two pointers remove the arrays |
| Jump Game | 55 | 1-D reachability → greedy | track the furthest reachable index |
| Jump Game II | 45 | 1-D DP → greedy levels | `dp[i] = min(dp[j] + 1)`; BFS-style levels do it in O(n) |
| Minimum Cost For Tickets | 983 | [Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) over days | `dp[d] = min(dp[d-1] + c1, dp[d-7] + c7, dp[d-30] + c30)` |
| 01 Matrix | 542 | [Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-), two passes | top-left → bottom-right, then the reverse |
| Counting Bits | 338 | [Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) | `dp[i] = dp[i >> 1] + (i & 1)` |
| Is Subsequence | 392 | LCS, degenerate | two pointers O(n); the LCS table only for many queries |
| Pascal's Triangle / II | 118 / 119 | [Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-), one rolling row | `row[j] += row[j-1]`, iterating **backward** |

## 1-D Linear DP Examples

### Integer Break — LC 343

> **Key point** — `dp[i]` is the best product from splitting `i` into **at least two** parts. For
> the last split `j + (i - j)`, the remainder can stay whole (`j * (i - j)`) or be split further
> (`j * dp[i - j]`). Both are needed: `dp[2] = 1`, yet keeping a `2` whole is worth 2.

**Reduces to**: [dp.md § Template 1](./dp.md#template-1-1-d-linear-dp--lc-53-) — `dp[i]` reads every earlier cell, with two options per split.

```python
# python
# LC 343 - Integer Break
# IDEA: dp[i] = best product of >= 2 parts summing to i; for each split, keep the rest whole or split it
# time = O(n^2), space = O(n)
def integerBreak(n):
    dp = [0] * (n + 1)
    dp[1] = 1
    for i in range(2, n + 1):
        for j in range(1, i):            # the first part is j
            keep_rest = j * (i - j)      # stop: i - j stays one part
            break_rest = j * dp[i - j]   # split i - j further
            dp[i] = max(dp[i], keep_rest, break_rest)
    return dp[n]
```

```java
// java
// LC 343 - Integer Break
// IDEA: dp[i] = best product of >= 2 parts summing to i; for each split, keep the rest whole or split it
// time = O(n^2), space = O(n)
public int integerBreak(int n) {
    int[] dp = new int[n + 1];
    dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        for (int j = 1; j < i; j++) {
            int keepRest = j * (i - j);          // stop splitting: i - j stays whole
            int breakRest = j * dp[i - j];       // split i - j further
            dp[i] = Math.max(dp[i], Math.max(keepRest, breakRest));
        }
    }
    return dp[n];
}
```

**Dry run** — `n = 10`:

```text
i      1  2  3  4  5  6  7   8   9   10
dp[i]  1  1  2  4  6  9  12  18  27  36
```

**Traps**
- Only `j * dp[i - j]`: a remainder of 2 or 3 is always split further, which loses value —
  `n = 4` gives 3 instead of 4, and `n = 6` gives 6 instead of 9.
- The O(n) greedy (as many 3s as possible, `3 + 1` → `2 + 2`) is the follow-up, not the opener:
  it needs a proof the DP does not.

## Length-Indexed Table Examples

### Decode Ways — LC 91

> **Key point** — Climbing Stairs with a validity test on each step: `dp[i]` (ways to decode the
> first `i` characters) takes `dp[i - 1]` when the last digit is `1`–`9`, and `dp[i - 2]` when the
> last two digits are `10`–`26`. A `0` can only ever be the second digit of a pair.

**Reduces to**: [dp.md § Template 1a](./dp.md#template-1a-length-indexed-table-n--1-slots--lc-70) — the `n + 1` table, where `dp[0] = 1` is the empty prefix.

```python
# python
# LC 91 - Decode Ways
# IDEA: dp[i] = ways to decode s[:i]; one-digit step if s[i-1] != '0', two-digit step if 10..26
# time = O(n), space = O(n)
def numDecodings(s):
    n = len(s)
    dp = [0] * (n + 1)
    dp[0] = 1                            # the empty prefix decodes one way
    for i in range(1, n + 1):
        if s[i - 1] != "0":              # s[i-1] alone is a letter
            dp[i] += dp[i - 1]
        if i >= 2 and 10 <= int(s[i - 2:i]) <= 26:   # s[i-2:i] together is a letter
            dp[i] += dp[i - 2]
    return dp[n]
```

```java
// java
// LC 91 - Decode Ways
// IDEA: dp[i] = ways to decode s[:i]; one-digit step if s[i-1] != '0', two-digit step if 10..26
// time = O(n), space = O(n)
public int numDecodings(String s) {
    int n = s.length();
    int[] dp = new int[n + 1];
    dp[0] = 1;                                   // the empty prefix: one way
    for (int i = 1; i <= n; i++) {
        if (s.charAt(i - 1) != '0') {            // s[i-1] decoded on its own
            dp[i] += dp[i - 1];
        }
        if (i >= 2) {
            int twoDigits = Integer.parseInt(s.substring(i - 2, i));
            if (twoDigits >= 10 && twoDigits <= 26) {   // s[i-2..i-1] decoded as a pair
                dp[i] += dp[i - 2];
            }
        }
    }
    return dp[n];
}
```

**Traps**
- `"06"` is `0`, not `1`: `06` is not a two-digit code, so the test is `10..26`, not `<= 26`.
- `s[i - 1]` is the **current** character in an `n + 1` table, not the previous one — see the
  [`s[i-1]` trap](./dp.md#the-si-1-trap--it-is-the-current-character-not-the-previous-one) under Template 1a.

**Similar** — LC 639 *(a `*` wildcard)* · LC 70 · LC 1137

## 2-D Grid DP Examples

### Unique Paths — LC 62

> **Key point** — the count of paths into a cell is the count into the cell above plus the count
> into the cell to its left, because those are the only two last moves. The first row and the
> first column are all `1`: there is one straight path to each.

**Reduces to**: [dp.md § Template 2](./dp.md#template-2-2-d-grid-dp--lc-64-) — `+` instead of `min`, and no cell costs.

```python
# python
# LC 62 - Unique Paths
# IDEA: dp[r][c] = dp[r-1][c] + dp[r][c-1]; the first row and column are 1
# time = O(m * n), space = O(m * n)
def uniquePaths(m, n):
    dp = [[1] * n for _ in range(m)]     # row 0 and column 0 stay 1
    for row in range(1, m):
        for col in range(1, n):
            dp[row][col] = dp[row - 1][col] + dp[row][col - 1]
    return dp[m - 1][n - 1]
```

```java
// java
// LC 62 - Unique Paths
// IDEA: dp[r][c] = dp[r-1][c] + dp[r][c-1]; the first row and column are 1
// time = O(m * n), space = O(m * n)
public int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];
    for (int row = 0; row < m; row++) {
        dp[row][0] = 1;                          // first column: only straight down
    }
    for (int col = 0; col < n; col++) {
        dp[0][col] = 1;                          // first row: only straight right
    }
    for (int row = 1; row < m; row++) {
        for (int col = 1; col < n; col++) {
            dp[row][col] = dp[row - 1][col] + dp[row][col - 1];
        }
    }
    return dp[m - 1][n - 1];
}
```

**Traps**
- The row needs only the row above, so one array of length `n` with `dp[col] += dp[col - 1]` is
  the O(n)-space follow-up.
- The closed form `C(m + n - 2, m - 1)` is also correct; compute it with integer arithmetic, and
  see [combinatorics_math_patterns.md](./combinatorics_math_patterns.md).

**Similar** — LC 63 *(obstacles: a blocked cell is 0)* · LC 64 · LC 980 *(every cell exactly once: backtracking, not DP)*

## Knapsack Examples

### Perfect Squares — LC 279

> **Key point** — the squares `1, 4, 9, …` are coins with unlimited supply, and `n` is the amount:
> the fewest squares summing to `n` is Coin Change. `dp[0] = 0`; every other cell starts at `n`
> (`n` ones always works), so no `-1` case exists.

**Reduces to**: [knapsack.md § Template 4](./knapsack.md#template-4-unbounded-min-count--lc-322-) — unbounded min count, with the coins generated as squares ≤ the amount.

```python
# python
# LC 279 - Perfect Squares
# IDEA: unbounded knapsack, min count: squares are the coins, n is the amount
# time = O(n * sqrt(n)), space = O(n)
def numSquares(n):
    dp = [0] + [n] * n                   # dp[a] = fewest squares summing to a; n ones is the worst
    for amount in range(1, n + 1):
        root = 1
        while root * root <= amount:     # every square that fits
            dp[amount] = min(dp[amount], dp[amount - root * root] + 1)
            root += 1
    return dp[n]
```

```java
// java
// LC 279 - Perfect Squares
// IDEA: unbounded knapsack, min count: squares are the coins, n is the amount
// time = O(n * sqrt(n)), space = O(n)
public int numSquares(int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, n);                          // n ones always works
    dp[0] = 0;
    for (int amount = 1; amount <= n; amount++) {
        for (int root = 1; root * root <= amount; root++) {
            dp[amount] = Math.min(dp[amount], dp[amount - root * root] + 1);
        }
    }
    return dp[n];
}
```

**Traps**
- A greedy "largest square first" fails: `12 = 9 + 1 + 1 + 1` (4) but `4 + 4 + 4` (3).
- The loop order does not matter for a min count; it does for counting ways
  ([knapsack.md § Loop Order](./knapsack.md#loop-order-combinations-vs-permutations)).

**Similar** — LC 322 · LC 518 · LC 377 — and a BFS over remainders solves the same problem level by level.

## State Machine DP Examples

### Paint Fence — LC 276

> **Key point** — two states for the last post: `same` (it matches the post before) and `diff`
> (it does not). "No three in a row" means a `same` post may only follow a `diff` one, so
> `same = diff_prev`, and `diff = (same_prev + diff_prev) * (k - 1)`.

**Reduces to**: [dp.md § Template 5](./dp.md#template-5-state-machine-dp--lc-309) — two states that read only the previous post, rolled into two variables.

```python
# python
# LC 276 - Paint Fence
# IDEA: same = ways the last post repeats its neighbour, diff = ways it does not; roll both forward
# time = O(n), space = O(1)
def numWays(n, k):
    if n == 0:
        return 0
    if n == 1:
        return k
    same = k                             # posts 1-2 the same colour
    diff = k * (k - 1)                   # posts 1-2 different
    for _ in range(3, n + 1):
        prev_same = same
        same = diff                      # repeat a colour only right after a change
        diff = (prev_same + diff) * (k - 1)   # any of the other k - 1 colours
    return same + diff
```

```java
// java
// LC 276 - Paint Fence
// IDEA: same = ways the last post repeats its neighbour, diff = ways it does not; roll both forward
// time = O(n), space = O(1)
public int numWays(int n, int k) {
    if (n == 0) {
        return 0;
    }
    if (n == 1) {
        return k;
    }
    int same = k;                                // posts 1-2 the same colour
    int diff = k * (k - 1);                      // posts 1-2 different
    for (int i = 3; i <= n; i++) {
        int prevSame = same;
        same = diff;                             // repeat a colour only after a change
        diff = (prevSame + diff) * (k - 1);      // any of the other k - 1 colours
    }
    return same + diff;
}
```

**Traps**
- Updating `diff` from the **new** `same`: save the old value first, as in every state machine.
- `k = 1`, `n ≥ 3` is `0`; the recurrence gets there on its own (`diff` is 0 from the start).

**Similar** — LC 198 · LC 309 · LC 740 *(take / skip on values)* · LC 790 *(tilings as states)*

## Rolling Variables Examples

### N-th Tribonacci Number — LC 1137

> **Key point** — `T(n)` reads exactly the last three values, so three variables replace the
> table. Shift them oldest-first (`a = b`, `b = c`, `c = next`), or a value is overwritten before
> it is read.

**Reduces to**: [dp.md § Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables--lc-198-) — the last rung, `k = 3` rolling variables.

```python
# python
# LC 1137 - N-th Tribonacci Number
# IDEA: T(n) = T(n-1) + T(n-2) + T(n-3); keep only the last three values
# time = O(n), space = O(1)
def tribonacci(n):
    if n == 0:
        return 0
    if n <= 2:
        return 1
    a, b, c = 0, 1, 1                    # T(i-3), T(i-2), T(i-1)
    for _ in range(3, n + 1):
        nxt = a + b + c
        a = b                            # shift oldest first
        b = c
        c = nxt
    return c
```

```java
// java
// LC 1137 - N-th Tribonacci Number
// IDEA: T(n) = T(n-1) + T(n-2) + T(n-3); keep only the last three values
// time = O(n), space = O(1)
public int tribonacci(int n) {
    if (n == 0) {
        return 0;
    }
    if (n <= 2) {
        return 1;
    }
    int a = 0, b = 1, c = 1;                     // T(i-3), T(i-2), T(i-1)
    for (int i = 3; i <= n; i++) {
        int next = a + b + c;
        a = b;                                   // shift oldest first
        b = c;
        c = next;
    }
    return c;
}
```

**Traps**
- `n = 0, 1, 2` return before the loop; the seeds `0, 1, 1` must satisfy the recurrence they start.
- The rolling-variable update order is the classic bug —
  [dp_advanced.md](./dp_advanced.md) traces it in its Rolling-Variable Deep Dive.

**Similar** — LC 70 · LC 509 · LC 746
