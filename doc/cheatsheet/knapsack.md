# Knapsack DP (0/1, Unbounded, Coin Change)

> **Scope** — The knapsack family in full: 0/1 vs unbounded vs bounded vs group, the subset-sum reduction, why the 0/1 inner loop runs backward, and the loop-order rule that separates combinations from permutations.
> **See also**: [dp.md](./dp.md) — the one-screen knapsack template and the rest of the DP patterns; [knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包的中文詳解 — a Traditional Chinese walkthrough of the 0/1 case only; [combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — counting without DP.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [0/1 Knapsack](https://leetcode.com/list/9zsdxlj0/)

## Overview

### Key Properties

- **Complexity**: `O(n * W)` time, `O(W)` space after the 1-D rollup — `n` items, `W` capacity/target.
- **Core Idea**: every item is a **take / skip** decision (a **pick-one-of-many** decision once an
  item carries a menu of options), and the DP dimension that separates the
  variants is the **capacity axis** — whether the inner loop reads values that already include the
  current item.
- **When to Use**: a fixed set of items each with a cost, a hard capacity/target, and a
  max / feasibility / count-the-ways question over subsets.

### The one table that decides everything

| Variant | Reuse | Outer loop | Inner loop | LC |
|---------|-------|-----------|------------|----|
| **0/1** | each item ≤ 1 time | items | capacity, **backward** | 416, 494, 1049, 474 |
| **Unbounded — combinations** | unlimited, order **doesn't** matter | items | amount, forward | 518 |
| **Unbounded — permutations** | unlimited, order **does** matter | amount | items | 377 |
| **Unbounded — min/max** | unlimited, order irrelevant | either | either | 322, 279, 1449 |
| **Bounded** | each item ≤ `k` times | items (binary-split into 0/1 copies) | capacity, backward | 2585, 1774 |
| **Group** | ≤ 1 **option** of each item | items | capacity **backward, outer**; options inner | 4040, 1155, 2218 |

### References

- [dp.md](./dp.md) — the short knapsack template and the rest of the DP pattern family
- [knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包中文詳解：state 定義、倒序 trace、LC 494/416 解題流程
- [Knapsack problem — Wikipedia](https://en.wikipedia.org/wiki/Knapsack_problem)

## Recognition

Constraint cues follow the ruler in [complexity_cheatsheet.md](./complexity_cheatsheet.md): every template here is `O(n * W)`, so it fits when `n * W` is around 10⁷ or less.

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "partition into two subsets with **equal** sum", "can some subset sum to exactly X" | n ≤ 200, values ≤ 100 → sum ≤ 2·10⁴ | [Template 1](#template-1-01-knapsack-subset-sum--lc-416-) 0/1 subset sum | an item may be reused → [Template 4](#template-4-unbounded-min-count--lc-322-) / [Template 2](#template-2-unbounded-count-combinations--lc-518-) |
| "assign `+` or `-` to each number", "split into two piles, minimise the difference" | sum ≤ 1000 | [Template 1](#template-1-01-knapsack-subset-sum--lc-416-) after the algebra ([LC 494](#target-sum--lc-494), [LC 1049](#last-stone-weight-ii--lc-1049)) | items may be left out *and* you want the balanced height → [Template 5](#template-5-difference-keyed-knapsack--lc-956-) |
| "number of **combinations** that make up the amount", unlimited coins | amount ≤ 5000 | [Template 2](#template-2-unbounded-count-combinations--lc-518-) items outer | "different sequences are counted as different" → [Template 3](#template-3-unbounded-count-permutations--lc-377-) |
| "number of possible combinations", but `(1, 2)` and `(2, 1)` are both counted | target ≤ 1000 | [Template 3](#template-3-unbounded-count-permutations--lc-377-) amount outer | the lists themselves are wanted, not a count → [backtrack.md](./backtrack.md) (LC 39) |
| "**fewest** coins / least number of perfect squares to sum to n" | amount ≤ 10⁴ | [Template 4](#template-4-unbounded-min-count--lc-322-) | the number of ways is asked → [Template 2](#template-2-unbounded-count-combinations--lc-518-) |
| "two **disjoint** subsets with equal sum, maximise it", items may be discarded | n ≤ 20, sum ≤ 5000 | [Template 5](#template-5-difference-keyed-knapsack--lc-956-) difference key | n ≤ 30 with huge values → meet in the middle (LC 2035) |
| "each element may be used in **one of several forms**", "one die / one pile / one type" | sum ≤ 10³–10⁴ | [Template 6](#template-6-group-knapsack--lc-4040-) group knapsack | one menu shared by every position, reusable → [Template 4](#template-4-unbounded-min-count--lc-322-) (LC 1449) |
| "can the string be segmented into dictionary words" | n ≤ 300 | not a sum: the items are concatenated | item-outer is simply wrong here → [dp_loop_order.md](./dp_loop_order.md#1-4-the-commutativity-test--when-item-outer-is-legal-) |

## Problem Categories

| Category | Question it answers | Answer type | LC |
|----------|--------------------|-------------|----|
| **Subset feasibility** | can *some* subset hit exactly this sum? | boolean | 416, 1049, 2915 |
| **Subset counting** | how many subsets hit it? | int (ways) | 494, 518 |
| **Best value under a cap** | most value that fits the capacity? | int (max) | classic 0/1, 474, 879 |
| **Fewest items to a target** | min coins / squares to make the amount? | int (min) or -1 | 322, 279 |
| **Ordered vs unordered counting** | is `1+2` the same as `2+1`? | decides the loop nesting | 518 vs 377 |
| **One form per item** | each item may be used in one of several forms — which? | int (min cost) | 4040, 1155, 2218 |

## Templates & Algorithms

### Template Comparison Table

| Template | When the problem says… | Loop order | Inner direction | Transition |
|---|---|---|---|---|
| [1: 0/1 subset sum](#template-1-01-knapsack-subset-sum--lc-416-) | "use each item at most once" | items → capacity | **backward** | `dp[s] = dp[s] or dp[s - w]` |
| [2: unbounded, combinations](#template-2-unbounded-count-combinations--lc-518-) | "count ways", order does not matter | items → amount | forward | `dp[a] += dp[a - coin]` |
| [3: unbounded, permutations](#template-3-unbounded-count-permutations--lc-377-) | "count ways", order matters | amount → items | forward | `dp[t] += dp[t - num]` |
| [4: unbounded, min count](#template-4-unbounded-min-count--lc-322-) | "fewest items", unlimited supply | either | forward | `dp[a] = min(dp[a], dp[a - coin] + 1)` |
| [5: difference key](#template-5-difference-keyed-knapsack--lc-956-) | two piles, only the gap matters | items → difference | snapshot | three choices per item |
| [6: group](#template-6-group-knapsack--lc-4040-) | each item has a menu of forms | items → capacity **outer** → options | backward or snapshot | best over the menu |

### Template 1: 0/1 Knapsack (Subset Sum) — LC 416 ⭐⭐⭐⭐⭐

**Recognise** — "partition into two subsets with equal sum", "can some subset reach exactly X",
"split into two groups", "each element used **at most once**". n ≤ 200 and sum ≤ 2·10⁴ →
an `O(n * sum)` table is expected.

**Key point** — after item `i` is processed, `dp[s]` is true exactly when some subset of the
first `i` items sums to `s`. The inner loop runs **backward**, so `dp[s - num]` is still the value
from *before* this item: each item is offered once per pass, never twice.

- **State**: `dp[s]` = can some subset of the items seen so far sum to exactly `s`
- **Recurrence**: `dp[s] = dp[s] or dp[s - num]` — skip `num`, or take it on top of `s - num`
- **Base**: `dp[0] = True` — the empty subset
- **Order**: items outer, `s` from `target` down to `num`
- **Answer**: `dp[target]`, with `target = total // 2`

```python
# python
# LC 416 - Partition Equal Subset Sum
# IDEA: 0/1 knapsack on sums -- can a subset reach total / 2? inner loop backward = each num once
# time = O(n * target), space = O(target)
def canPartition(nums):
    total = sum(nums)
    if total % 2 == 1:                    # an odd total cannot split into two equal halves
        return False
    target = total // 2
    dp = [False] * (target + 1)           # dp[s] = some subset of the nums so far sums to s
    dp[0] = True                          # the empty subset
    for num in nums:
        for s in range(target, num - 1, -1):   # BACKWARD: dp[s - num] is still the old row
            if dp[s - num]:
                dp[s] = True
    return dp[target]
```

```java
// java
// LC 416 - Partition Equal Subset Sum
// IDEA: 0/1 knapsack on sums -- can a subset reach total / 2? inner loop backward = each num once
// time = O(n * target), space = O(target)
public boolean canPartition(int[] nums) {
    int total = 0;
    for (int num : nums) {
        total += num;
    }
    if (total % 2 == 1) {                  // an odd total cannot split into two equal halves
        return false;
    }
    int target = total / 2;
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;                          // the empty subset
    for (int num : nums) {
        for (int s = target; s >= num; s--) {   // BACKWARD: dp[s - num] is still the old row
            if (dp[s - num]) {
                dp[s] = true;
            }
        }
    }
    return dp[target];
}
```

The same loop answers three questions; only the transition changes:

```text
can we reach s?        dp[s] = dp[s] or dp[s - w]           base dp[0] = True
how many subsets?      dp[s] += dp[s - w]                   base dp[0] = 1     (LC 494)
best value within s?   dp[s] = max(dp[s], dp[s - w] + v)    base dp[*] = 0     (classic knapsack)
```

**Complexity**
- **Time O(n · target)**: the nested `for num` × `for s in range(target, …)` touches each
  (item, sum) pair once.
- **Space O(target)**: one row, `dp = [False] * (target + 1)`, rolled in place — which is only
  legal because the inner loop runs backward.

**Dry run** — why backward, with one item `3` and `target = 9`:

```text
forward  s=3: dp[3] = dp[0] = T
         s=6: dp[6] = dp[3] = T    <- dp[3] was set by THIS item a moment ago: 3 used twice
         s=9: dp[9] = dp[6] = T    <- and three times
backward s=9: dp[9] = dp[6] (old, F)
         s=6: dp[6] = dp[3] (old, F)
         s=3: dp[3] = dp[0] = T    <- the only sum one 3 can make
```

**Traps**
- **Forward inner loop** turns this into unbounded knapsack: `nums = [3]`, `target = 6` reports
  `True` by using the single `3` twice.
- **Odd total**: `[1, 2, 3, 5]` has total 11; `11 // 2 = 5` is reachable (`2 + 3`), so skipping the
  parity check returns `True` for an impossible split.
- **Mixing up the transition**: `or` for feasibility, `+=` for counts, `max(…, dp[s - w] + v)` for
  value. A `max` on a boolean table or `or` on a count table compiles and is wrong.
- **Large sums**: values × n can pass `int` range in Java when the transition sums values; use
  `long`.

**Not this if** — an item can be reused → [Template 4](#template-4-unbounded-min-count--lc-322-)
or [Template 2](#template-2-unbounded-count-combinations--lc-518-) (forward loop); items may be
discarded and the balanced height is wanted → [Template 5](#template-5-difference-keyed-knapsack--lc-956-);
an item comes in several forms → [Template 6](#template-6-group-knapsack--lc-4040-).

**Drill** — LC 416 ⭐ · LC 1049 *(minimise the gap: largest reachable `s ≤ total // 2`)* ·
LC 494 *(count subsets: `+=`, after the algebra)* · LC 474 *(two capacities, a 2-D row)* ·
LC 879 *(count, with a profit axis capped at `minProfit`)*

### Loop Order: Combinations vs Permutations

In unbounded knapsack the **order of the two loops** decides whether you count **combinations**
or **permutations**. The transition is the same line, `dp[i] += dp[i - item]`; only the nesting
moves. [Template 2](#template-2-unbounded-count-combinations--lc-518-) and
[Template 3](#template-3-unbounded-count-permutations--lc-377-) are the two halves.

> Both orders here are *correct* DPs answering different questions — that is special to sums. When
> the items are concatenated rather than added (LC 139 Word Break), the item-outer order is not a
> different question, it is simply wrong: [dp_loop_order.md](./dp_loop_order.md#1-4-the-commutativity-test--when-item-outer-is-legal-).

| Aspect | LC 518: Coin Change II (combinations) | LC 377: Combination Sum IV (permutations) |
|--------|------------------------------------------|---------------------------------------------|
| **What it counts** | Unique sets (order doesn't matter) | Different sequences (order matters) |
| **Example** | [1,2] = [2,1] (same) | [1,2] ≠ [2,1] (different) |
| **Outer Loop** | `for coin in coins` | `for t in range(1, target + 1)` |
| **Inner Loop** | `for a in range(coin, amount + 1)` | `for num in nums` |
| **DP Transition** | `dp[a] += dp[a - coin]` | `dp[t] += dp[t - num]` |
| **Base Case** | `dp[0] = 1` | `dp[0] = 1` |
| **Result for nums=[1,2], target=3** | **2**: {1,1,1}, {1,2} | **3**: {1,1,1}, {1,2}, {2,1} |
| **Why it works** | every use of coin 1 is placed before coin 2 is offered → only non-decreasing sequences | for each sum, every number is tried as the *last* one → every ordering |

Memory trick: **C**oin outer counts **C**ombinations; **A**mount outer counts **A**rrangements.

### Template 2: Unbounded Count, Combinations — LC 518 ⭐⭐⭐⭐

**Recognise** — "the number of **combinations** that make up that amount", "you may assume an
infinite number of each coin", and `{1,2}` / `{2,1}` are one answer. amount ≤ 5000, coins ≤ 300 →
`O(n * amount)`.

**Key point** — coins are the **outer** loop, so when coin `c` is being offered every earlier coin
is already finished: a sum can only be built in non-decreasing coin order, and each multiset is
counted once. The inner loop runs **forward**, so `dp[a - coin]` may already include this coin —
that is the unlimited supply.

```python
# python
# LC 518 - Coin Change II
# IDEA: coins OUTER (each multiset built in one canonical order), amount inner FORWARD (reuse)
# time = O(n * amount), space = O(amount)
def change(amount, coins):
    dp = [0] * (amount + 1)        # dp[a] = combinations of the coins so far that sum to a
    dp[0] = 1                      # one way to make 0: take nothing
    for coin in coins:
        for a in range(coin, amount + 1):   # forward: dp[a - coin] may already use this coin
            dp[a] += dp[a - coin]
    return dp[amount]
```

```java
// java
// LC 518 - Coin Change II
// IDEA: coins OUTER (each multiset built in one canonical order), amount inner FORWARD (reuse)
// time = O(n * amount), space = O(amount)
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;                                  // one way to make 0: take nothing
    for (int coin : coins) {
        for (int a = coin; a <= amount; a++) {  // forward: dp[a - coin] may already use this coin
            dp[a] += dp[a - coin];
        }
    }
    return dp[amount];
}
```

**Complexity**
- **Time O(n · amount)**: `for coin` × `for a in range(coin, amount + 1)`, one addition each.
- **Space O(amount)**: the single row `dp`.

**Dry run** — `coins = [1, 2]`, `amount = 3`:

```text
start          dp = [1, 0, 0, 0]
coin 1  a=1..3 dp = [1, 1, 1, 1]     {1} {1,1} {1,1,1}
coin 2  a=2    dp[2] += dp[0] -> 2   {1,1} {2}
        a=3    dp[3] += dp[1] -> 2   {1,1,1} {1,2}   -- no {2,1}: coin 1 is finished
answer  dp[3] = 2
```

**Traps**
- **Swapping the loops** silently answers LC 377: `coins = [1, 2]`, `amount = 3` returns 3.
- **`dp[0] = 0`**: every count stays 0. The empty combination is the one way to make 0.
- **Backward inner loop** turns it into 0/1: `amount = 4`, `coins = [2]` returns 0 instead of 1.

**Not this if** — the statement counts `(1, 2)` and `(2, 1)` separately →
[Template 3](#template-3-unbounded-count-permutations--lc-377-); each item at most once →
[Template 1](#template-1-01-knapsack-subset-sum--lc-416-) with `+=`.

**Drill** — LC 518 · LC 1641 *(vowel strings = combinations with repetition)* · LC 2585
*(each type at most `count` times: bounded, read as a group)*

### Template 3: Unbounded Count, Permutations — LC 377 ⭐⭐⭐⭐

**Recognise** — "the number of possible combinations that add up to target", with the note that
**different sequences are counted as different**; "number of ways to climb with steps from a set".
target ≤ 1000 → `O(n * target)`.

**Key point** — the amount is the **outer** loop, so `dp[t]` asks *which number came last?* and
tries every number at every sum: `dp[t] = Σ dp[t - num]`. Two sequences that differ only in order
end in different numbers at some step, so both are counted.

```python
# python
# LC 377 - Combination Sum IV
# IDEA: amount OUTER, numbers inner -- dp[t] sums over the number placed LAST, so orders differ
# time = O(n * target), space = O(target)
def combinationSum4(nums, target):
    dp = [0] * (target + 1)        # dp[t] = ordered sequences summing to t
    dp[0] = 1                      # the empty sequence
    for t in range(1, target + 1):
        for num in nums:           # try every number as the last one
            if t - num >= 0:
                dp[t] += dp[t - num]
    return dp[target]
```

```java
// java
// LC 377 - Combination Sum IV
// IDEA: amount OUTER, numbers inner -- dp[t] sums over the number placed LAST, so orders differ
// time = O(n * target), space = O(target)
public int combinationSum4(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1;                                  // the empty sequence
    for (int t = 1; t <= target; t++) {
        for (int num : nums) {                  // try every number as the last one
            if (t - num >= 0) {
                dp[t] += dp[t - num];
            }
        }
    }
    return dp[target];
}
```

**Complexity**
- **Time O(target · n)**: `for t` × `for num`, one addition each.
- **Space O(target)**: the single row `dp`.

**Dry run** — `nums = [1, 2]`, `target = 3`:

```text
t=1  last 1: dp[0]=1                    dp[1] = 1   {1}
t=2  last 1: dp[1]=1, last 2: dp[0]=1   dp[2] = 2   {1,1} {2}
t=3  last 1: dp[2]=2, last 2: dp[1]=1   dp[3] = 3   {1,1,1} {2,1} {1,2}
```

**Traps**
- **Items outer** gives the combinations count: `nums = [1, 2, 3]`, `target = 4` returns 4, not 7.
- **Intermediate overflow**: on the judge's tests some `dp[t]` below `target` exceed 32 bits. The
  answer is promised to fit, and every step is an addition, so Java's wrapping `int` still ends on
  the right value; in C++ signed overflow is undefined — use `unsigned`.
- **Negative numbers** (the follow-up) make the count infinite: `[1, -1]` loops forever. The
  template assumes positive numbers.

**Not this if** — order does not matter → [Template 2](#template-2-unbounded-count-combinations--lc-518-);
the sequences themselves are wanted → [backtrack.md](./backtrack.md).

**Drill** — LC 377 · LC 70 ⭐ *(nums = {1, 2})* · LC 2466 *(steps `zero` and `one`, sum over a range)*

### Template 4: Unbounded Min Count — LC 322 ⭐⭐⭐⭐⭐

**Recognise** — "the **fewest** number of coins", "least number of perfect square numbers",
unlimited supply of each, return -1 if impossible. amount ≤ 10⁴, coins ≤ 12 → `O(n * amount)`.
Greedy (largest coin first) is the tempting wrong answer.

**Key point** — `dp[a]` is the fewest coins summing to exactly `a`, for every `a` below the current
one. The last coin is *some* `coin ≤ a`, and the rest is the already-solved `dp[a - coin]`; take the
minimum over the choices. Order does not matter for a minimum, so either loop nesting works.

```python
# python
# LC 322 - Coin Change
# IDEA: dp[a] = min over the last coin of dp[a - coin] + 1; INF = amount + 1 marks "unreachable"
# time = O(amount * n), space = O(amount)
def coinChange(coins, amount):
    INF = amount + 1                  # more coins than any real answer can use
    dp = [INF] * (amount + 1)         # dp[a] = fewest coins summing to exactly a
    dp[0] = 0                         # zero coins make 0
    for a in range(1, amount + 1):
        for coin in coins:
            if a - coin >= 0:         # the coin fits; dp[a - coin] is already final
                dp[a] = min(dp[a], dp[a - coin] + 1)
    return -1 if dp[amount] == INF else dp[amount]
```

```java
// java
// LC 322 - Coin Change
// IDEA: dp[a] = min over the last coin of dp[a - coin] + 1; INF = amount + 1 marks "unreachable"
// time = O(amount * n), space = O(amount)
public int coinChange(int[] coins, int amount) {
    int INF = amount + 1;                       // more coins than any real answer can use
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, INF);
    dp[0] = 0;                                  // zero coins make 0
    for (int a = 1; a <= amount; a++) {
        for (int coin : coins) {
            if (a - coin >= 0) {                // the coin fits; dp[a - coin] is already final
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
            }
        }
    }
    return dp[amount] == INF ? -1 : dp[amount];
}
```

**Complexity**
- **Time O(amount · n)**: `for a in range(1, amount + 1)` × `for coin in coins`.
- **Space O(amount)**: the single row `dp`.

**Dry run** — `coins = [1, 2, 5]`, `amount = 11`. Each `dp[a]` reads cells to its left that are
already final:

| a | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | **11** |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| **dp[a]** | 0 | 1 | 1 | 2 | 2 | 1 | 2 | 2 | 3 | 3 | 2 | **3** |

`dp[11] = min(dp[10] + 1, dp[9] + 1, dp[6] + 1) = min(3, 4, 3) = 3` → `5 + 5 + 1`.

Why the guard is `a - coin >= 0` and not `a == coin`:

| `a - coin` | Meaning | What the one line does |
| --- | --- | --- |
| **Negative** (`< 0`) | the coin is too big for this amount | skipped by the guard |
| **Zero** (`== 0`) | this single coin is the whole amount | `dp[0] + 1 = 1` — `dp[0]` is seeded |
| **Positive** (`> 0`) | the coin fits; the remainder is a solved subproblem | `dp[a - coin] + 1` |

**Traps**
- **`if a == coin`** only ever finds one-coin answers: `coins = [2]`, `amount = 4` returns -1
  instead of 2, because `dp[4]` never reads `dp[2]`.
- **Returning `dp[amount]` without the `INF` check**: `coins = [2]`, `amount = 3` returns 4.
- **Greedy**: `coins = [1, 3, 4]`, `amount = 6` — largest-first takes `4 + 1 + 1` = 3 coins; the
  DP finds `3 + 3` = 2.
- **`Integer.MAX_VALUE` as INF in Java**: `dp[a - coin] + 1` overflows to negative and wins every
  `min`. `amount + 1` is safe.

**Not this if** — the number of *ways* is asked → [Template 2](#template-2-unbounded-count-combinations--lc-518-);
each coin usable once → [Template 1](#template-1-01-knapsack-subset-sum--lc-416-) with `min`
and a backward loop.

**Drill** — LC 322 ⭐ · LC 279 *(the coins are the squares ≤ n)* · LC 1449 *(maximise digit count
first, then build the largest string)*

### Template 5: Difference-Keyed Knapsack — LC 956 ⭐⭐⭐⭐

**Recognise** — "split into two **disjoint** subsets with **equal** sum, return the largest
possible sum", where items may be **left out**; more generally, two piles where only the **gap**
between them matters. n ≤ 20 and sum ≤ 5000 → `O(n * sum)`.

**Key point** — key the table by the **difference** between the two piles, not by a total:
`dp[d]` is the tallest *taller* pile over every way to place some of the items so far into two
piles that differ by exactly `d`. Each item has **three** choices — skip, taller pile, shorter
pile — and the answer is `dp[0]`.

```text
dp[capacity] = best value     ->   "how much have I packed?"     (Templates 1-4)
dp[difference] = best height  ->   "how unbalanced are the two piles?"

skip it            ->  (d,        taller)                 unchanged
put on TALLER      ->  (d + x,    taller + x)             the gap widens
put on SHORTER     ->  (|d - x|,  max(taller, shorter+x)) the gap closes, and may flip sides
                       where shorter = taller - d
```

The `|d - x|` and the `max(...)` together handle the short pile overtaking the tall one: the
piles swap roles and the difference reflects back off zero.

```python
# python
# LC 956 - Tallest Billboard
# IDEA: dict from difference -> tallest "taller side"; only reachable differences are stored
# time = O(n * sum), space = O(sum)
def tallestBillboard(rods):
    dp = {0: 0}                        # difference -> height of the taller support
    for x in rods:
        prev = dict(dp)                # snapshot: each rod is placed at most once
        for d, taller in prev.items():
            shorter = taller - d
            dp[d + x] = max(dp.get(d + x, 0), taller + x)       # x on the taller side
            nd = abs(d - x)                                    # x on the shorter side
            dp[nd] = max(dp.get(nd, 0), max(taller, shorter + x))
    return dp[0]                       # difference 0 = equal supports
```

```java
// java
// LC 956 - Tallest Billboard
// IDEA: key the table by the DIFFERENCE between the two supports, store the taller
//       support's height. Answer = dp[0], the best height at difference zero.
// time = O(n * sum), space = O(sum)
public int tallestBillboard(int[] rods) {
    int sum = 0;
    for (int r : rods) {
        sum += r;
    }
    int[] dp = new int[sum + 1];
    Arrays.fill(dp, -1);          // -1 = unreachable; 0 is a real, reachable height
    dp[0] = 0;
    for (int x : rods) {
        int[] prev = dp.clone();  // "skip x" is prev carried forward, already in dp
        for (int d = 0; d <= sum; d++) {
            if (prev[d] < 0) {
                continue;
            }
            int taller = prev[d];
            int shorter = taller - d;
            if (d + x <= sum) {                       // x joins the taller pile
                dp[d + x] = Math.max(dp[d + x], taller + x);
            }
            int nd = Math.abs(d - x);                 // x joins the shorter pile
            dp[nd] = Math.max(dp[nd], Math.max(taller, shorter + x));
        }
    }
    return dp[0];
}
```

**Complexity**
- **Time O(n · sum)**: `for x in rods` × at most `sum + 1` differences in `prev`.
- **Space O(sum)**: one entry per reachable difference, plus the `prev` snapshot.

**Dry run** — `rods = [1, 2, 3, 6]`:

```text
start        {0: 0}
after 1      {0: 0, 1: 1}
after 2      {0: 0, 1: 2, 2: 2, 3: 3}
after 3      {0: 3, 1: 3, 2: 4, 3: 3, 4: 5, 5: 5, 6: 6}
after 6      {0: 6, 1: 6, 2: 7, 3: 6, ...}   -> dp[0] = 6, supports {6} and {1,2,3}
```

`dp[0]` jumps from `0` to `3` when the third rod balances `{3}` against `{1,2}`, and to `6` when the
`6` balances the whole rest.

**Traps**
- **Initialising the array to `0` instead of `-1`**: a zero-filled array claims every difference is
  reachable for free; `rods = [1, 2]` then returns 1 instead of 0.
- **Mutating `dp` while iterating it** (Python) or forgetting the `clone()` (Java): without the
  snapshot one rod lands on both piles in one pass.
- **Storing the shorter side instead of the taller**: either convention works, but the transitions
  differ — derive `shorter = taller - d` from the one you chose.
- **`d + x` past the array** in Java: cap at `sum`; a difference above the total is meaningless.

**Not this if** — no item may be discarded and only feasibility is asked → a total key is enough,
[Template 1](#template-1-01-knapsack-subset-sum--lc-416-) (LC 416, LC 1049); n ≤ 30 with values up
to 10⁷ → meet in the middle (LC 2035), the difference table is too wide.

**Drill** — LC 1049 *(the same idea, read with a total key)* · LC 956 · LC 2035 *(meet in the middle)*

### Template 6: Group Knapsack — LC 4040 ⭐⭐⭐⭐

**Recognise** — an item can be **used in one of several forms**, or not at all: one die and its
faces, one pile and its prefixes, one element and every value it can be transformed into. The
capacity is still a sum ≤ 10³–10⁴ → `O(n * W * |group|)`.

| Problem | The group is… | Per-group rule |
|---|---|---|
| **LC 4040** Minimum Operations to Form Subset Sum I | one `x` and every value it can be transformed into, with the op count as the cost | ≤ 1 |
| LC 1155 Number of Dice Rolls With Target Sum | one die and its faces `1..f` | **exactly** 1 |
| LC 2218 Maximum Value of K Coins From Piles | one pile and each of its prefixes (`0..k` coins) | ≤ 1 |
| LC 2585 Number of Ways to Earn Points | one question type used `0, 1, … count` times | ≤ 1 |

**Key point** — the capacity axis does not change; only the inner choice does:
`dp[s] = best(dp[s], best over the item's options of dp[s - w] + c)`. Every option of one item
must read the state from **before** that item, so at most one form is taken — either by
snapshotting `dp`, or by putting the **capacity loop outside** the options loop and running it
backward.

```text
✅ snapshot         read prev, write into a copy   every option competes against the state BEFORE the item
✅ capacity OUTER   for s in W..0:                  dp[s-w] is a smaller index, not yet touched by this item
                        for (w, c) in group:
❌ options OUTER    for (w, c) in group:            option B reads a dp[] that option A has already updated
                        for s in W..w:
```

LC 4040's menu: an element `x` may be doubled and halved, **but all multiplications come before
all divisions**. A mixed run `k` doublings then `j` halvings lands on `x * 2^(k-j)` — which a pure
chain reaches at cost `|k - j|` instead of `k + j` — so the menu is just the two pure chains, each
cut once it stops being useful:

```text
x, 2x, 4x, ...        k doublings, cost k   (stop past sum: it only grows)
x, x//2, x//4, ...    k halvings,  cost k   (stop at 0: it never helps a positive sum)

x = 10, sum = 13  ->  (10, 0), (5, 1), (2, 2), (1, 3)      20 is already over sum
x =  2, sum = 13  ->  (2, 0), (4, 1), (8, 2), (1, 1)
```

```python
# python
# LC 4040 - Minimum Operations to Form Subset Sum I
# IDEA: group knapsack -- each x is a menu of (value, ops); a snapshot of dp lets at most one
#       entry per x be taken
# time = O(n * sum * log(max(x, sum))), space = O(sum)
def minOperations(nums, sum):
    INF = float('inf')
    dp = [INF] * (sum + 1)                 # dp[s] = min ops for a subset summing to s
    dp[0] = 0
    for x in nums:
        options = []                       # the menu for x: (value, ops)
        value, op = x, 0
        while value <= sum:                # x itself (0 ops), 2x, 4x, ...
            options.append((value, op))
            value *= 2
            op += 1
        value, op = x, 0
        while value > 0:                   # x//2, x//4, ...
            value //= 2
            op += 1
            if 0 < value <= sum:
                options.append((value, op))
        new_dp = dp[:]                     # snapshot: every option reads the pre-x state
        for value, cost in options:
            for s in range(value, sum + 1):
                if dp[s - value] != INF:
                    new_dp[s] = min(new_dp[s], dp[s - value] + cost)
        dp = new_dp
    return -1 if dp[sum] == INF else dp[sum]
```

```java
// java
// LC 4040 - Minimum Operations to Form Subset Sum I
// IDEA: same group knapsack, written in place with the capacity loop OUTER and backward
// time = O(n * sum * log(max(x, sum))), space = O(sum)
public int minOperations(int[] nums, int sum) {
    final int INF = Integer.MAX_VALUE / 2;     // + cost cannot overflow
    int[] dp = new int[sum + 1];
    Arrays.fill(dp, INF);
    dp[0] = 0;
    for (int x : nums) {
        List<int[]> options = new ArrayList<>();
        long value = x;
        int op = 0;
        while (value <= sum) {                 // x itself (0 ops), 2x, 4x, ...
            options.add(new int[]{(int) value, op});
            value *= 2;
            op++;
        }
        value = x;
        op = 0;
        while (value > 0) {                    // x/2, x/4, ...
            value /= 2;
            op++;
            if (value > 0 && value <= sum) {
                options.add(new int[]{(int) value, op});
            }
        }
        for (int s = sum; s >= 0; s--) {       // capacity OUTER, backward
            for (int[] option : options) {     // options INNER: one form per x
                int v = option[0];
                int cost = option[1];
                if (s >= v && dp[s - v] != INF) {
                    dp[s] = Math.min(dp[s], dp[s - v] + cost);
                }
            }
        }
    }
    return dp[sum] >= INF ? -1 : dp[sum];
}
```

**Complexity**
- **Time O(n · sum · |menu|)**: `for x` × `for s` × the options, and each chain has
  `O(log(max(x, sum)))` entries because it doubles or halves.
- **Space O(sum)**: `dp` plus one snapshot `new_dp` (none in the Java form).

**Dry run** — `nums = [10, 2]`, `sum = 13`:

```text
start        {0:0}
after 10     {0:0, 1:3, 2:2, 5:1, 10:0}
after 2      {0:0, 1:1, 2:0, 3:3, 4:1, 5:1, 6:2, 7:1, 8:2, 9:2, 10:0, 11:1, 12:0, 13:3}
                                              dp[13] = dp[5] + 2 = 1 + 2 -> 10->5 and 2->4->8
```

`dp[1]` improves from `3` to `1`: the first group reached `1` only as `10 -> 5 -> 2 -> 1`, the
second gets there with `2 -> 1`. Each cell is the *cheapest* way to that sum.

**Traps**
- **Options outer with an in-place `dp`** — the 0/1 muscle memory. `nums = [5]`, `sum = 3`: the menu
  is `{(2, 1), (1, 2)}` and that loop builds `2 + 1 = 3` from one element, reporting 3 instead of -1.
- **Dropping the zero-cost option**: without `(x, 0)`, `nums = [4]`, `sum = 4` returns -1.
- **Enumerating mixed chains** is wasted work *here*; if divide-then-multiply were allowed
  (`5 -> 2 -> 4`) the two pure chains would be **incomplete**. Re-read the rule before reusing the
  shortcut.
- **Unbounded chains**: the doubling chain never returns once it passes `sum`, and a halved `0`
  never helps — both are infinite loops without the cut.
- **`dp[sum]` without the `INF` check** returns a sentinel instead of -1; in Java keep `INF` at
  `MAX_VALUE / 2`.

**Not this if** — one unbounded menu shared by every position (LC 1449) →
[Template 4](#template-4-unbounded-min-count--lc-322-), not a group; the capacity is a pair
`(zeros, ones)` but each item has one form (LC 474) → plain
[Template 1](#template-1-01-knapsack-subset-sum--lc-416-) with a 2-D row. Bounded knapsack *is* a
group knapsack — "up to `k` times" is the group `{(w, v), (2w, 2v), …, (kw, kv)}`; the binary split
in the table at the top is the faster spelling.

**Drill** — LC 1155 *(exactly one face: no skip, count with `+=`)* · LC 2585 *(type used
`0..count` times)* · LC 2218 *(prefix-sum each pile, then `max`)* · LC 4040 *(the menu is derived,
not given)*

## LC Examples

### Last Stone Weight II — LC 1049

> **Key point** — smashing stones is splitting them into two piles and subtracting:
> `|S - (total - S)| = total - 2S`. Minimising it means finding the **largest reachable subset sum
> `S ≤ total // 2`**, which is a feasibility question.

**Reduces to**: [Template 1](#template-1-01-knapsack-subset-sum--lc-416-) — the same boolean row,
read from `total // 2` downward instead of only at `total // 2`.

```python
# python
# LC 1049 - Last Stone Weight II
# IDEA: 0/1 subset sum up to total // 2; the answer is total - 2 * (largest reachable sum)
# time = O(n * total), space = O(total)
def lastStoneWeightII(stones):
    total = sum(stones)
    half = total // 2
    dp = [False] * (half + 1)             # dp[s] = some subset of the stones sums to s
    dp[0] = True
    for stone in stones:
        for s in range(half, stone - 1, -1):   # backward: each stone once
            if dp[s - stone]:
                dp[s] = True
    for s in range(half, -1, -1):         # the most balanced split
        if dp[s]:
            return total - 2 * s
```

```java
// java
// LC 1049 - Last Stone Weight II
// IDEA: 0/1 subset sum up to total / 2; the answer is total - 2 * (largest reachable sum)
// time = O(n * total), space = O(total)
public int lastStoneWeightII(int[] stones) {
    int total = 0;
    for (int stone : stones) {
        total += stone;
    }
    int half = total / 2;
    boolean[] dp = new boolean[half + 1];
    dp[0] = true;
    for (int stone : stones) {
        for (int s = half; s >= stone; s--) {   // backward: each stone once
            if (dp[s - stone]) {
                dp[s] = true;
            }
        }
    }
    for (int s = half; s >= 0; s--) {           // the most balanced split
        if (dp[s]) {
            return total - 2 * s;
        }
    }
    return total;                               // unreachable: dp[0] is always true
}
```

```text
stones = [2, 7, 4, 1, 8, 1], total = 23, half = 11

after 2      reachable {0, 2}
after 7      reachable {0, 2, 7, 9}
after 4      reachable {0, 2, 4, 6, 7, 9, 11}
...          11 stays reachable  ->  answer = 23 - 2 * 11 = 1
```

### Target Sum — LC 494

> **Key point** — let `P` be the sum of the numbers given `+`. Then `P - (total - P) = T`, so
> `P = (total + T) / 2`: counting sign assignments is counting **subsets that sum to `P`**. Two
> guards come first — `abs(T) > total` and an odd `total + T` both mean 0 ways.

**Reduces to**: [Template 1](#template-1-01-knapsack-subset-sum--lc-416-) — with the counting
transition `dp[s] += dp[s - num]` and `dp[0] = 1`.

```python
# python
# LC 494 - Target Sum
# IDEA: reduce "assign +/-" to "count subsets summing to (total + T) / 2", then 0/1 knapsack
# time = O(n * P), space = O(P)
def findTargetSumWays(nums, target):
    total = sum(nums)
    if abs(target) > total or (total + target) % 2 == 1:   # P negative or not an integer
        return 0
    plus = (total + target) // 2
    dp = [0] * (plus + 1)                 # dp[s] = subsets of the nums so far summing to s
    dp[0] = 1                             # the empty subset
    for num in nums:
        for s in range(plus, num - 1, -1):    # backward: each num once
            dp[s] += dp[s - num]
    return dp[plus]
```

```java
// java
// LC 494 - Target Sum
// IDEA: reduce "assign +/-" to "count subsets summing to (total + T) / 2", then 0/1 knapsack
// time = O(n * P), space = O(P)
public int findTargetSumWays(int[] nums, int target) {
    int total = 0;
    for (int num : nums) {
        total += num;
    }
    if (Math.abs(target) > total || (total + target) % 2 != 0) {   // P negative or not an integer
        return 0;
    }
    int plus = (total + target) / 2;
    int[] dp = new int[plus + 1];
    dp[0] = 1;                                  // the empty subset
    for (int num : nums) {
        for (int s = plus; s >= num; s--) {     // backward: each num once
            dp[s] += dp[s - num];
        }
    }
    return dp[plus];
}
```

A zero in `nums` is handled for free: `range(plus, -1, -1)` includes `s = 0`, so `dp[0]` doubles —
`[0, 0, 1]`, `T = 1` gives 4.

## Pattern Selection Strategy

```text
Does one item offer SEVERAL mutually exclusive options?
│
├─ YES ──► Group Knapsack  [4040, 1155, 2218, 2585]
│          for group in groups:
│              for w in range(W, -1, -1):             # CAPACITY outer, backward
│                  for (weight, cost) in group:       # options inner
│          └─ or snapshot dp and let every option read the pre-group state
│          └─ options-outer + in-place dp is the classic bug: two forms of one item
│
└─ NO  ──► Is each item reusable?
    │
    ├─ NO  ──► 0/1 Knapsack
    │          for item in items:
    │              for w in range(W, weight-1, -1):     # BACKWARD
    │          └─ asks "can we hit the sum?"  -> boolean dp
    │          └─ asks "how many ways?"       -> dp[j] += dp[j-w]
    │          └─ asks "best value?"          -> dp[j] = max(dp[j], dp[j-w]+v)
    │
    └─ YES ──► Does order matter?
               │
               ├─ NO  (combinations, {1,2} == {2,1})  ──► items outer, amount inner  [518]
               ├─ YES (permutations, {1,2} != {2,1})  ──► amount outer, items inner  [377]
               └─ Min/max only (order irrelevant)     ──► either nesting             [322, 279]
```

## Drill Path

| Stage | Problems (easy → hard) | What it adds |
|---|---|---|
| **Core** ⭐ | LC 322 · LC 416 | the min-count row and the backward 0/1 row, once each |
| **Practice** | LC 279 · LC 518 · LC 377 · LC 1049 · LC 494 · LC 474 | loop order for counts, the partition algebra, a 2-D capacity |
| **Stretch** | LC 1155 · LC 956 · LC 879 · LC 1449 · LC 4040 | group menus, a difference key, a capped profit axis, rebuilding the answer |

## Summary

| If you remember one thing per row | … it is this |
|---|---|
| **0/1 vs unbounded** | the *inner loop direction*: backward blocks reuse, forward permits it |
| **Combinations vs permutations** | the *loop nesting*: items-outer counts sets, amount-outer counts sequences |
| **Partition problems** | "split into two equal halves" ⇒ subset-sum to `total / 2` |
| **LC 494 Target Sum** | `sum1 = (total + T) / 2`, but guard `abs(T) <= total` and `(total + T)` even first |
| **The guard** | `i - coin >= 0`, not `> 0` — the `== 0` case reads the seeded `dp[0]` |
| **Bounded knapsack** | binary-split each item into `1, 2, 4, …` copies, then run plain 0/1 |
| **Group knapsack** | put the *capacity* loop outside the *options* loop — options-outer takes two forms of one item |
| **LC 4040** | the group is the two pure `x2` / `x//2` chains, cost = length; "multiply before divide" is what makes mixed runs redundant |
