# Knapsack DP (0/1, Unbounded, Coin Change)

> **Scope** — The knapsack family in full: 0/1 vs unbounded vs bounded, the subset-sum reduction, why the 0/1 inner loop runs backward, and the loop-order rule that separates combinations from permutations.
> **See also**: [dp.md](./dp.md) — the one-screen knapsack template and the rest of the DP patterns; [knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包的中文詳解 — a Traditional Chinese walkthrough of the 0/1 case only; [combinatorics_math_patterns.md](./combinatorics_math_patterns.md) — counting without DP.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [0/1 Knapsack](https://leetcode.com/list/9zsdxlj0/)

## Overview

### Key Properties

- **Complexity**: `O(n * W)` time, `O(W)` space after the 1-D rollup — `n` items, `W` capacity/target.
- **Core Idea**: every item is a **take / skip** decision, and the DP dimension that separates the
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

### References

- [dp.md](./dp.md) — the short knapsack template and the rest of the DP pattern family
- [knapsack_01_zh.md](./knapsack_01_zh.md) — 0/1 背包中文詳解：state 定義、倒序 trace、LC 494/416 解題流程
- [Knapsack problem — Wikipedia](https://en.wikipedia.org/wiki/Knapsack_problem)

## Problem Categories

| Category | Question it answers | Answer type | LC |
|----------|--------------------|-------------|----|
| **Subset feasibility** | can *some* subset hit exactly this sum? | boolean | 416, 1049, 2915 |
| **Subset counting** | how many subsets hit it? | int (ways) | 494, 518 |
| **Best value under a cap** | most value that fits the capacity? | int (max) | classic 0/1, 474, 879 |
| **Fewest items to a target** | min coins / squares to make the amount? | int (min) or -1 | 322, 279 |
| **Ordered vs unordered counting** | is `1+2` the same as `2+1`? | decides the loop nesting | 518 vs 377 |

## Templates & Algorithms

### Loop Order: Combinations vs Permutations

**🔑 Key Insight**: In unbounded knapsack problems (like Coin Change), the **order of nested loops** determines whether you count **combinations** or **permutations**.

---

#### **🎯 Ultimate Cheat Sheet: When to Use Which Pattern**

| When Problem Says... | Pattern to Use | Loop Order | Direction | DP Transition | Example LC |
|---------------------|----------------|------------|-----------|---------------|------------|
| "Count ways" + order doesn't matter | **Combinations** | Item → Target | Forward | `dp[i] += dp[i-item]` | **518** |
| "Count ways" + order matters | **Permutations** | Target → Item | Forward | `dp[i] += dp[i-item]` | **377** |
| "Use each item once" + find max/min | **0/1 Knapsack** | Item → Capacity | **Backward** | `dp[w] = max(dp[w], ...)` | **416** |
| "Unlimited items" + find max/min | **Unbounded Knapsack** | Item → Capacity | Forward | `dp[i] = min(dp[i], ...)` | **322** |

**⚡ Quick Recognition (识别):**
- See "different sequences" or "different orderings" → **Permutations** (Target outer)
- See "number of combinations" or "unique ways" → **Combinations** (Item outer)
- See "each element at most once" → **0/1 Knapsack** (Backward)
- See "minimum coins" or "fewest items" → **Unbounded Knapsack** (Forward)

---

#### **📊 Visual Summary: The Four Core Patterns**

```text
┌─────────────────────────────────────────────────────────────────────────┐
│                     DP KNAPSACK PATTERN MATRIX                          │
└─────────────────────────────────────────────────────────────────────────┘

                          COUNT WAYS              FIND MIN/MAX
                    ┌──────────────────┬──────────────────────────┐
                    │                  │                          │
ORDER MATTERS?      │  PERMUTATIONS    │   Not typically used     │
(Yes)              │  LC 377          │   (Use Permutations      │
                    │  Target→Item     │    for counting)         │
                    │  Forward         │                          │
                    ├──────────────────┼──────────────────────────┤
                    │                  │                          │
ORDER DOESN'T       │  COMBINATIONS    │   UNBOUNDED KNAPSACK     │
MATTER              │  LC 518          │   LC 322                 │
(No)                │  Item→Target     │   Item→Capacity          │
                    │  Forward         │   Forward                │
                    ├──────────────────┼──────────────────────────┤
                    │                  │                          │
USE EACH ONCE       │  Not typical     │   0/1 KNAPSACK           │
(Constraint)        │  (Can adapt      │   LC 416                 │
                    │   0/1 pattern)   │   Item→Capacity          │
                    │                  │   BACKWARD ⚠️            │
                    └──────────────────┴──────────────────────────┘

Legend:
  Item→Target     = Outer loop: items,    Inner loop: target
  Target→Item     = Outer loop: target,   Inner loop: items
  Forward         = Inner loop: i to target (allows reuse)
  BACKWARD ⚠️     = Inner loop: target to i (prevents reuse)
```

**🎯 Decision Flow:**
```text
Start
  │
  ├─ Question asks "count ways"?
  │   │
  │   ├─ YES → Order matters?
  │   │         ├─ YES → Permutations (Target→Item) [LC 377]
  │   │         └─ NO  → Combinations (Item→Target) [LC 518]
  │   │
  │   └─ NO  → Question asks "min/max"?
  │             │
  │             ├─ Each item once?
  │             │   ├─ YES → 0/1 Knapsack (BACKWARD) [LC 416]
  │             │   └─ NO  → Unbounded (FORWARD) [LC 322]
  │             │
  │             └─ Unknown → Check problem constraints
```

---

#### **📋 Master Pattern Table: DP Transitions by Problem Type**

| Pattern Type | Loop Order | DP Transition | What It Counts | Mental Model | Example | Result |
|--------------|------------|---------------|----------------|--------------|---------|--------|
| **COMBINATIONS**<br>(Order doesn't matter) | **ITEM → TARGET**<br><br>`for item in items:`<br>&nbsp;&nbsp;`for i in range(item, target+1):` | `dp[i] += dp[i - item]` | Unique sets<br>[1,2] = [2,1] | "Process all uses of item-1, then all uses of item-2"<br><br>Forces canonical order | LC 518<br>coins=[1,2]<br>amount=3 | **2 ways**<br>{1,1,1}<br>{1,2} |
| **PERMUTATIONS**<br>(Order matters) | **TARGET → ITEM**<br><br>`for i in range(1, target+1):`<br>&nbsp;&nbsp;`for item in items:` | `dp[i] += dp[i - item]` | Different orderings<br>[1,2] ≠ [2,1] | "For each target, try every item as the 'last' one"<br><br>Allows any order | LC 377<br>nums=[1,2]<br>target=3 | **3 ways**<br>{1,1,1}<br>{1,2}<br>{2,1} |
| **0/1 KNAPSACK**<br>(Use each once) | **ITEM → CAPACITY**<br>(backwards)<br><br>`for item in items:`<br>&nbsp;&nbsp;`for w in range(W, weight-1, -1):` | `dp[w] = max(dp[w],`<br>`dp[w-weight[i]] + value[i])` | Max/min with constraint<br>Each item used ≤ 1 time | "Must iterate backwards to avoid using same item twice in one pass" | LC 416<br>Partition<br>Subset | True/False<br>or Max value |
| **UNBOUNDED KNAPSACK**<br>(Unlimited use) | **ITEM → CAPACITY**<br>(forwards)<br><br>`for item in items:`<br>&nbsp;&nbsp;`for w in range(weight, W+1):` | `dp[w] = max(dp[w],`<br>`dp[w-weight[i]] + value[i])` | Max/min without constraint<br>Each item used unlimited | "Iterate forwards - can use updated values in same pass" | LC 322<br>Coin Change<br>(min coins) | Min count<br>or -1 |

---

#### **💻 Code Templates by Pattern**

```java
// java
// IDEA: the four knapsack loop orders side by side — each differs only in nesting/direction
// time = O(n * W), space = O(W)
// ============================================
// PATTERN 1: COMBINATIONS (Item → Target)
// ============================================
// LC 518: Coin Change II
public int countCombinations(int target, int[] items) {
    int[] dp = new int[target + 1];
    dp[0] = 1;  // Base: one way to make 0

    // OUTER: Items/Coins
    for (int item : items) {
        // INNER: Target/Amount (forward)
        for (int i = item; i <= target; i++) {
            dp[i] += dp[i - item];  // ← Same transition
        }
    }
    return dp[target];
}

// ============================================
// PATTERN 2: PERMUTATIONS (Target → Item)
// ============================================
// LC 377: Combination Sum IV
public int countPermutations(int target, int[] items) {
    int[] dp = new int[target + 1];
    dp[0] = 1;  // Base: one way to make 0

    // OUTER: Target/Amount
    for (int i = 1; i <= target; i++) {
        // INNER: Items/Coins
        for (int item : items) {
            if (i >= item) {
                dp[i] += dp[i - item];  // ← Same transition
            }
        }
    }
    return dp[target];
}

// ============================================
// PATTERN 3: 0/1 KNAPSACK (Item → Capacity BACKWARDS)
// ============================================
// LC 416: Partition Equal Subset Sum
public boolean canPartition(int[] nums, int target) {
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;  // Base: can make 0

    // OUTER: Items
    for (int num : nums) {
        // INNER: Capacity (BACKWARDS to prevent reuse)
        for (int w = target; w >= num; w--) {
            dp[w] = dp[w] || dp[w - num];  // ← Different transition (OR)
        }
    }
    return dp[target];
}

// ============================================
// PATTERN 4: UNBOUNDED KNAPSACK (Item → Capacity FORWARDS)
// ============================================
// LC 322: Coin Change (minimum coins)
public int minCoins(int target, int[] coins) {
    int[] dp = new int[target + 1];
    Arrays.fill(dp, target + 1);  // Infinity
    dp[0] = 0;  // Base: 0 coins for 0 amount

    // OUTER: Items/Coins
    for (int coin : coins) {
        // INNER: Target (FORWARDS allows reuse)
        for (int i = coin; i <= target; i++) {
            dp[i] = Math.min(dp[i], dp[i - coin] + 1);  // ← Different transition (MIN)
        }
    }
    return dp[target] > target ? -1 : dp[target];
}
```

**🔑 Key Observations:**
1. **Same DP Transition (`dp[i] += dp[i - item]`)** for:
   - Combinations (Item → Target)
   - Permutations (Target → Item)
   - **Only difference**: Loop order!

2. **Different DP Transitions** for:
   - 0/1 Knapsack: `dp[w] = dp[w] || dp[w - num]` (boolean OR or MAX)
   - Unbounded Knapsack: `dp[i] = min(dp[i], dp[i - coin] + 1)` (MIN/MAX)

3. **Direction Matters** for knapsack:
   - Backwards → prevents reuse (0/1)
   - Forwards → allows reuse (unbounded)

---

#### **🎯 Pattern Selection Decision Tree**

```text
Question: What does the problem ask for?

├─ "Count number of ways/combinations to reach target"
│  ├─ Order matters? (e.g., [1,2] ≠ [2,1])
│  │  ├─ YES → Use PERMUTATIONS pattern (Target → Item)
│  │  │         Example: LC 377 Combination Sum IV
│  │  └─ NO  → Use COMBINATIONS pattern (Item → Target)
│  │            Example: LC 518 Coin Change II
│  │
│  └─ Can reuse items?
│     ├─ YES → Unbounded, iterate forwards
│     └─ NO  → 0/1 Knapsack, iterate backwards
│
└─ "Find minimum/maximum value"
   ├─ Can reuse items?
   │  ├─ YES → Unbounded Knapsack (forwards)
   │  │         Example: LC 322 Coin Change (min coins)
   │  └─ NO  → 0/1 Knapsack (backwards)
   │            Example: LC 416 Partition Equal Subset Sum
   │
   └─ Always use (Item → Capacity) order
```

---

### **Deep Dive: 0/1 Knapsack & Subset Sum Pattern** 🎒

This pattern is fundamental and appears in many disguised forms. Last Stone Weight II is a great example of recognizing when a problem is secretly a subset sum problem.

#### **When to Use This Pattern**

Use **0/1 Knapsack / Subset Sum** when you see:

| Indicator | What It Means | Example |
|-----------|--------------|---------|
| "Partition" or "split into two groups" | Divide items into subsets | LC 1049 (Last Stone Weight II) |
| "Maximize/minimize the difference" | Find optimal partition | LC 1049, 494 |
| "Can you achieve sum X?" | Check if specific sum possible | LC 416 (Equal Subset Partition) |
| "Each item used at most once" | 0/1 constraint (not unlimited) | All of above |
| "Minimize difference between groups" | Partition into balanced groups | LC 1049 |

**Key Recognition**: If you see "partition" or "divide into two groups" → think **0/1 Knapsack**.

#### **Core Idea: The Mathematical Transformation** 🧮

**Problem**: Partition array into two groups and minimize difference.

```text
Given: stones = [2, 7, 4, 1, 8, 1]
Total sum = 23

Goal: Split into two groups with min |sum1 - sum2|

Mathematical insight:
  Let sum1 = S (sum of group 1)
  Then sum2 = total - S (sum of group 2)
  
  Difference = |sum1 - sum2| = |S - (total - S)| = |2S - total|
  
  To minimize this: Maximize S such that S ≤ total/2
  
  Result = total - 2*S (where S is the largest achievable sum ≤ total/2)
```

**Why This Works**:
- Find the largest subset sum that doesn't exceed `total / 2`
- This gives the most balanced partition possible
- The remaining group has sum = `total - S`
- Their difference = `(total - S) - S = total - 2*S`

#### **Pattern: Two Variants**

**Variant 1: Boolean DP (Can we achieve this sum?)**

```java
// java
// LC 1049 - Last Stone Weight II
// IDEA: variant 1 — boolean subset sum; can we reach exactly `sum`?
// time = O(n * total), space = O(total)
public int lastStoneWeightII(int[] stones) {
    int total = 0;
    for (int stone : stones) {
        total += stone;
    }

    int target = total / 2;
    
    // dp[j] = can we achieve sum j?
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;  // Base: always can make sum 0 (choose nothing)

    // For each stone
    for (int stone : stones) {
        // Iterate BACKWARDS to prevent using same stone twice
        for (int j = target; j >= stone; j--) {
            dp[j] = dp[j] || dp[j - stone];  // Can achieve j if:
                                              // (already could) OR (could make j-stone and add this stone)
        }
    }

    // Find largest achievable sum ≤ target
    for (int j = target; j >= 0; j--) {
        if (dp[j]) {
            return total - 2 * j;
        }
    }

    return 0;
}
```

**Variant 2: Integer DP (Maximum value achievable)**

```java
// java
// LC 1049 - Last Stone Weight II
// IDEA: variant 2 — maximise the achievable subset sum <= total/2, answer = total - 2*best
// time = O(n * total), space = O(total)
public int lastStoneWeightII(int[] stones) {
    int total = 0;
    for (int stone : stones) {
        total += stone;
    }

    int target = total / 2;
    
    // dp[j] = maximum sum we can achieve ≤ j
    int[] dp = new int[target + 1];
    dp[0] = 0;  // Base: can make sum 0

    // For each stone
    for (int stone : stones) {
        // Iterate BACKWARDS to prevent reuse
        for (int j = target; j >= stone; j--) {
            // Either skip this stone (dp[j])
            // Or include it and add to best we could do with j-stone (dp[j-stone] + stone)
            dp[j] = Math.max(dp[j], dp[j - stone] + stone);
        }
    }

    return total - 2 * dp[target];
}
```

#### **Why Iterate BACKWARDS? (The Critical Detail)**

```text
❌ WRONG: Forward iteration (causes reuse)
for (int j = stone; j <= target; j++) {
    dp[j] = dp[j] || dp[j - stone];
}
Problem: When we update dp[j], we're using the NEW value of dp[j-stone]
         which might have already been updated by the same stone in this iteration.
         This allows using the same stone multiple times!

Example with stone=3, target=9:
  j=3: dp[3] = dp[0] = true ✓
  j=6: dp[6] = dp[3] = true ✓ BUT dp[3] was just updated by the same stone!
  j=9: dp[9] = dp[6] = true ✓ Again, using same stone multiple times!

✅ CORRECT: Backward iteration (prevents reuse)
for (int j = target; j >= stone; j--) {
    dp[j] = dp[j] || dp[j - stone];
}
Reason: We process from right to left, so dp[j-stone] is always from the PREVIOUS iteration
        (before this stone was considered). So we use each stone only once.

Example with stone=3, target=9:
  j=9: dp[9] = dp[6] (old value from previous stone) ✓
  j=6: dp[6] = dp[3] (old value from previous stone) ✓
  j=3: dp[3] = dp[0] (old value from previous stone) ✓
```

#### **Complete Example: Last Stone Weight II**

```text
stones = [2, 7, 4, 1, 8, 1]
total = 23
target = 23 / 2 = 11

Initial: dp = [T, F, F, F, F, F, F, F, F, F, F, F]

After stone 2:
  dp[2] = T (can make sum 2)
  dp = [T, F, T, F, F, F, F, F, F, F, F, F]

After stone 7:
  dp[9] = T (can make 2+7)
  dp[7] = T
  dp[2] = T (unchanged)
  dp = [T, F, T, F, F, F, F, T, F, T, F, F]

After stone 4:
  dp[11] = T (can make 7+4)
  dp[9] = T (unchanged)
  dp[6] = T (can make 2+4)
  dp[4] = T
  dp = [T, F, T, F, T, F, T, T, F, T, F, T]

... continue for remaining stones ...

Final: Find largest j ≤ 11 where dp[j] = T
       Result = 23 - 2 * j
```

#### **Similar LeetCode Problems** 📚

| Problem | Goal | Transformation | Complexity |
|---------|------|-----------------|-----------|
| **LC 1049: Last Stone II** | Min weight of last stone | Partition into two groups, minimize difference | O(n × sum/2) |
| **LC 416: Partition Equal Subset** | Can partition into equal sums? | Can achieve sum = total/2? | O(n × sum/2) |
| **LC 494: Target Sum** | Count ways to reach target | Treat as: group(+) sum1, group(-) sum2; solve sum1 - sum2 = target | O(n × sum) |
| **LC 879: Profitable Schemes** | Count valid profit schemes | DP on (company count, profit) | O(n × k × p) |

**Transformation Examples**:

**LC 416 (Partition Equal Subset)**:
```text
Question: Can we partition into two equal subsets?
Answer: Can we achieve sum = total/2?
DP: boolean[] dp where dp[j] = can we make sum j?
Return: dp[total/2]
```

**LC 494 (Target Sum)**:
```text
Question: Assign +/- to reach target T
Transformation: Let sum1 = sum of items with +
                Let sum2 = sum of items with -
                sum1 - sum2 = T
                sum1 + sum2 = total (all items)
                
                Solving: sum1 = (total + T) / 2

Feasibility first (both are required before the DP runs):
    abs(T) > total          -> 0 ways: even all-plus or all-minus cannot reach T
    (total + T) is odd      -> 0 ways: sum1 would not be an integer

So: This is 0/1 knapsack! Find count of subsets with sum = (total + T) / 2
DP: int[] dp where dp[j] = count of ways to make sum j
Return: dp[(total + T) / 2]
```

```java
// java
// LC 494 - Target Sum
// IDEA: reduce "assign +/-" to "count subsets summing to (total + T) / 2", then 0/1 knapsack
// time = O(n * target), space = O(target)
public int findTargetSumWays(int[] nums, int target) {
    int total = 0;
    for (int x : nums) total += x;

    // NOTE !!! guard before the division — otherwise `sub` is negative or non-integral
    if (Math.abs(target) > total || ((total + target) % 2) != 0) return 0;

    int sub = (total + target) / 2;
    int[] dp = new int[sub + 1];
    dp[0] = 1;                                  // one way to make 0: pick nothing
    for (int num : nums) {
        for (int j = sub; j >= num; j--) {      // backward -> each num used at most once
            dp[j] += dp[j - num];
        }
    }
    return dp[sub];
}
```

#### **Common Pitfalls** ⚠️

1. **Iterating forwards instead of backwards**
   - Will allow reusing same item multiple times
   - Use backwards iteration for 0/1 knapsack

2. **Wrong DP transition**
   - For boolean: `dp[j] = dp[j] || dp[j - weight]`
   - For integer sum: `dp[j] = Math.max(dp[j], dp[j - weight] + weight)`
   - For counting ways: `dp[j] += dp[j - weight]`
   - Don't mix these up!

3. **Not recognizing the "partition" pattern**
   - "Difference between groups" → Think partition
   - "Split into two teams" → Think partition
   - "Divide array" → Think partition

4. **Integer overflow with sum**
   - When total sum is large, watch for overflow
   - Consider using long if needed

---

#### **⚡ Quick Reference: Loop Order → Problem Type**

| Outer Loop | Inner Loop | Pattern Name | Use When | Problems |
|------------|------------|--------------|----------|----------|
| **Items/Coins** | **Target/Amount** | Combinations | Count unique sets (order doesn't matter) | LC 518 |
| **Target/Amount** | **Items/Coins** | Permutations | Count sequences (order matters) | LC 377 |
| **Items** (backwards) | **Capacity** | 0/1 Knapsack | Each item used once, find max/min | LC 416, 494 |
| **Items** (forwards) | **Capacity** | Unbounded Knapsack | Unlimited items, find max/min | LC 322 |

---

#### **Quick Comparison Table**

| Aspect | Combinations (LC 518) | Permutations (LC 377) |
|--------|----------------------|----------------------|
| **Loop Order** | Coin → Amount | Amount → Coin |
| **Order Matters?** | ❌ No: [1,2] = [2,1] | ✅ Yes: [1,2] ≠ [2,1] |
| **Problem Type** | Coin Change II | Combination Sum IV |
| **Outer Loop** | `for (int coin : coins)` | `for (int i = 1; i <= target; i++)` |
| **Inner Loop** | `for (int i = coin; i <= amount; i++)` | `for (int num : nums)` |
| **Example** | amount=3, coins=[1,2] → 2 ways | target=3, nums=[1,2] → 3 ways |

---

#### **Pattern 1: Combinations (Outer: Coins, Inner: Amount)**
```java
// java
// IDEA: coins outer, amount inner -> each coin is offered once, so sets are counted
// time = O(n * amount), space = O(amount)
// LC 518: Coin Change II - Count combinations
// Example: [1,2] and [2,1] are the SAME combination
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1; // Base case: 1 way to make amount 0

    // OUTER LOOP: Iterate through each coin
    // This ensures we process all uses of one coin before moving to the next,
    // which prevents duplicate combinations like [1,2] and [2,1].
    for (int coin : coins) {
        // INNER LOOP: Update dp table for all amounts reachable by this coin
        for (int i = coin; i <= amount; i++) {
            // Number of ways to make amount 'i' is:
            // (Current ways) + (Ways to make 'i - coin')
            dp[i] += dp[i - coin];
        }
    }

    return dp[amount];
}
```

**Why This Works**:
- Process coins one at a time (e.g., first all 1s, then all 2s, then all 5s)
- By the time you use coin `2`, you've finished all calculations with coin `1`
- Impossible to place a `1` after a `2`, forcing non-decreasing order
- Result: Only **combinations** (order doesn't matter)

**Example Trace**: `coins = [1,2], amount = 3`
```text
After coin 1: dp = [1, 1, 1, 1]  // {}, {1}, {1,1}, {1,1,1}
After coin 2: dp = [1, 1, 2, 2]  // + {2}, {1,2}
Result: 2 combinations → {1,1,1}, {1,2}
```

#### **Pattern 2: Permutations (Outer: Amount, Inner: Coins)**
```java
// java
// IDEA: amount outer, coins inner -> every coin is retried at every amount, so orderings count
// time = O(n * target), space = O(target)
// LC 377: Combination Sum IV - Count permutations
// Example: [1,2] and [2,1] are DIFFERENT permutations
public int combinationSum4(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1;

    // OUTER LOOP: Iterate through each amount
    // For each amount, try all coins to see which was "last added"
    for (int i = 1; i <= target; i++) {
        // INNER LOOP: Try each coin for current amount
        for (int num : nums) {
            if (i >= num) {
                dp[i] += dp[i - num];
            }
        }
    }

    return dp[target];
}
```

**Why This Counts Permutations**:
- For each amount, ask: "What was the **last coin** I added?"
- Every coin can be the "last" coin at each step
- Result: **Permutations** (order matters)

**Example Trace**: `nums = [1,2], target = 3`
```text
dp[1]: Use 1 → [1] (1 way)
dp[2]: Use 1 → [1,1], Use 2 → [2] (2 ways)
dp[3]: From dp[2] add 1 → [1,1,1], [2,1]
       From dp[1] add 2 → [1,2]
Result: 3 permutations → {1,1,1}, {1,2}, {2,1}
```

#### **Comparison Table**

| Loop Order | Result Type | Problem Example | Use Case |
|------------|-------------|-----------------|----------|
| **Outer: Coin**<br>Inner: Amount | **Combinations**<br>(Order doesn't matter) | LC 518 Coin Change II | Count unique coin combinations |
| **Outer: Amount**<br>Inner: Coin | **Permutations**<br>(Order matters) | LC 377 Combination Sum IV | Count different orderings |

#### **🔥 Side-by-Side Code Comparison**

**LC 518: Coin Change II (Combinations)**
```java
// java
// LC 518 - Coin Change II
// IDEA: combinations — coins outer
// time = O(n * amount), space = O(amount)
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1; // Base: 1 way to make 0

    // CRITICAL: Coin outer loop = COMBINATIONS
    for (int coin : coins) {              // ← Process coins one by one
        for (int i = coin; i <= amount; i++) {  // ← Update all amounts for this coin
            dp[i] += dp[i - coin];
        }
    }
    return dp[amount];
}

// Example: amount=3, coins=[1,2]
// Result: 2 combinations
// {1,1,1}, {1,2}  (Note: [1,2] and [2,1] counted as same)
```

**LC 377: Combination Sum IV (Permutations)**
```java
// java
// LC 377 - Combination Sum IV
// IDEA: permutations — amount outer
// time = O(n * target), space = O(target)
public int combinationSum4(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1; // Base: 1 way to make 0

    // CRITICAL: Amount outer loop = PERMUTATIONS
    for (int i = 1; i <= target; i++) {   // ← Process each amount
        for (int num : nums) {            // ← Try every number for this amount
            if (i >= num) {
                dp[i] += dp[i - num];
            }
        }
    }
    return dp[target];
}

// Example: target=3, nums=[1,2]
// Result: 3 permutations
// {1,1,1}, {1,2}, {2,1}  (Note: [1,2] and [2,1] are different)
```

#### **🔍 Detailed Trace Comparison: Why Loop Order Matters**

**Example: nums/coins = [1, 2], target/amount = 3**

**LC 518 (Combinations - Coin Outer):**
```text
Initialize: dp = [1, 0, 0, 0]

Process coin 1:
  i=1: dp[1] += dp[0] = 1    → [1, 1, 0, 0]  // ways: {1}
  i=2: dp[2] += dp[1] = 1    → [1, 1, 1, 0]  // ways: {1,1}
  i=3: dp[3] += dp[2] = 1    → [1, 1, 1, 1]  // ways: {1,1,1}

Process coin 2:
  i=2: dp[2] += dp[0] = 1+1=2 → [1, 1, 2, 1]  // ways: {1,1}, {2}
  i=3: dp[3] += dp[1] = 1+1=2 → [1, 1, 2, 2]  // ways: {1,1,1}, {1,2}
                                              // Note: Can't get {2,1} because
                                              // all coin-1 uses are done before coin-2

Final: dp[3] = 2  ✅ Only {1,1,1} and {1,2}
```

**LC 377 (Permutations - Amount Outer):**
```text
Initialize: dp = [1, 0, 0, 0]

i=1 (building sum 1):
  Try 1: dp[1] += dp[0] = 1   → [1, 1, 0, 0]  // ways: {1}
  Try 2: skip (2 > 1)

i=2 (building sum 2):
  Try 1: dp[2] += dp[1] = 1   → [1, 1, 1, 0]  // {1} + 1 = {1,1}
  Try 2: dp[2] += dp[0] = 1+1=2 → [1, 1, 2, 0]  // {} + 2 = {2}

i=3 (building sum 3):
  Try 1: dp[3] += dp[2] = 2   → [1, 1, 2, 2]  // {1,1} + 1 = {1,1,1}
                                              // {2} + 1 = {2,1}  ✅
  Try 2: dp[3] += dp[1] = 2+1=3 → [1, 1, 2, 3]  // {1} + 2 = {1,2}  ✅

Final: dp[3] = 3  ✅ All three: {1,1,1}, {1,2}, {2,1}
```

**Key Insight:**
- **LC 518 (Coin Outer)**: Once you finish processing coin-1, you never revisit it. This forces a canonical order (all 1s before all 2s), preventing duplicates like {1,2} and {2,1}.
- **LC 377 (Amount Outer)**: For each sum, you ask "what was the **last** number added?" Every number can be "last", allowing both {1,2} and {2,1}.

---

#### **When to Use Which**

**Use Combinations (Coin → Amount)** when:
- Problem asks for "number of ways" without considering order
- [1,2,5] and [2,1,5] should be counted once
- Keywords: "combinations", "unique sets"

**Use Permutations (Amount → Coin)** when:
- Problem asks for different sequences/orderings
- [1,2] and [2,1] should be counted separately
- Keywords: "permutations", "different orderings", "sequences"

#### **Complete Java Example: LC 518 Coin Change II**
```java
// java
// LC 518 - Coin Change II
// IDEA: count the ways to form each amount; coins outer keeps `{1,2}` and `{2,1}` as one
// time = O(n * amount), space = O(amount)
public int change(int amount, int[] coins) {
    // dp[i] = total number of combinations that make up amount i
    int[] dp = new int[amount + 1];

    // Base case: There is exactly 1 way to make 0 amount (empty set)
    dp[0] = 1;

    // CRITICAL: Coin outer loop = COMBINATIONS
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] += dp[i - coin];
        }
    }

    return dp[amount];
}
```

**Test Cases**:
```text
Input: amount = 5, coins = [1,2,5]
Output: 4
Combinations: {5}, {2,2,1}, {2,1,1,1}, {1,1,1,1,1}

Input: amount = 3, coins = [2]
Output: 0
Explanation: Cannot make 3 with only coins of 2
```

#### **📚 Problem References**

| Problem | LC # | Loop Order | What it Counts | File Reference |
|---------|------|------------|----------------|----------------|
| **Coin Change II** | 518 | Coin → Amount | Combinations (order doesn't matter) | `leetcode_java/.../CoinChange2.java` |
| **Combination Sum IV** | 377 | Amount → Coin | Permutations (order matters) | `leetcode_java/.../CombinationSumIV.java` |

**💡 Memory Trick:**
- **"Coin first" = Combinations** (both start with 'C')
- **"Amount first" = Arrangements/Permutations** (both start with 'A')

---

#### **📝 Final Summary: Complete Pattern Comparison**

| Aspect | LC 518: Coin Change II<br>(Combinations) | LC 377: Combination Sum IV<br>(Permutations) |
|--------|------------------------------------------|---------------------------------------------|
| **What it counts** | Unique sets (order doesn't matter) | Different sequences (order matters) |
| **Example** | [1,2] = [2,1] (same) | [1,2] ≠ [2,1] (different) |
| **Outer Loop** | `for (int coin : coins)` | `for (int i = 1; i <= target; i++)` |
| **Inner Loop** | `for (int i = coin; i <= amount; i++)` | `for (int num : nums)` |
| **DP Transition** | `dp[i] += dp[i - coin]` | `dp[i] += dp[i - num]` |
| **Base Case** | `dp[0] = 1` | `dp[0] = 1` |
| **Result for<br>nums=[1,2], target=3** | **2** combinations:<br>{1,1,1}, {1,2} | **3** permutations:<br>{1,1,1}, {1,2}, {2,1} |
| **Why it works** | Processing coin-1 completely before coin-2 forces canonical order → no {2,1} | For each sum, try every number as "last" → allows all orderings |
| **File Reference** | `CoinChange2.java` | `CombinationSumIV.java` |

**🔥 The ONLY Difference:**
```java
// java
// IDEA: the two nestings printed together — the only difference is which loop is outer
// time = O(n * amount), space = O(amount)
// LC 518: Combinations
for (int coin : coins)              // ← ITEM OUTER
    for (int i = coin; i <= amount; i++)

// LC 377: Permutations
for (int i = 1; i <= target; i++)   // ← TARGET OUTER
    for (int num : nums)
```

**Both use the EXACT SAME transition: `dp[i] += dp[i - item]`**

---

### Why the Guard Is `if (i - coin >= 0)`, Not `if (i == coin)`

**🔑 The Question**: Why use `if (i >= coin)` instead of `if (i == coin)`?

This is a fundamental concept in understanding how Dynamic Programming builds on previously solved **subproblems**.

#### **The Short Answer**
- `i == coin` only checks if a **single coin** matches the amount
- `i >= coin` checks if a coin can be **combined** with a previous sum to reach the amount

#### **The Logic of `i - coin >= 0`**

When we calculate `dp[i]`, we aren't just looking for one coin that equals `i`. We are looking for a coin `coin` that, when subtracted from `i`, leaves a remainder that we **already know how to solve**.

- **`i`**: The total amount we are trying to reach right now
- **`coin`**: The value of the coin we just picked up
- **`i - coin`**: The "remainder" or the amount left over

If `i - coin >= 0`, the coin fits and the remainder is a subproblem we have **already calculated**, because we fill the table from `0` up to `amount`. The `== 0` case is not special-cased: it reads `dp[0]`, which the base case already set. That is exactly why the guard is `>=` and not `>`.

**The DP looks back at `dp[i - coin]`** to reuse that solution!

#### **A Concrete Example**

Imagine `coins = [2]` and we want to find `dp[4]` (how to make 4 cents).

1. We try the coin `coin = 2`
2. `i - coin` is `4 - 2 = 2`
3. Since `2 > 0`, we don't stop. We look at `dp[2]`
4. We already calculated `dp[2] = 1` (it took one 2-cent coin to make 2 cents)
5. So, `dp[4] = dp[2] + 1 = 2`

**If we only used `if (i - coin == 0)`:**
- We would only ever find that `dp[2] = 1`
- When we got to `dp[4]`, the condition `4 - 2 == 0` would be **false**
- We would incorrectly conclude that we can't make 4 cents!

#### **The Three Scenarios**

When checking `i - coin`:

| Result of `i - coin` | Meaning | Action |
| --- | --- | --- |
| **Negative** (`< 0`) | The coin is too big for this amount | Skip this coin |
| **Zero** (`== 0`) | This single coin matches the amount perfectly | `dp[i] = 1` |
| **Positive** (`> 0`) | This coin fits, and we need to check the "remainder" | `dp[i] = dp[remainder] + 1` |

The last two rows are the **same line of code** — `dp[i] = dp[i - coin] + 1` — because `dp[0]` is
already seeded to `0`. That is why one guard, `i - coin >= 0`, covers both.

#### **💡 Key Insight**

The condition `if (i >= coin)` covers both the case where a coin matches exactly **and** the case where a coin is just one piece of a larger puzzle.

#### **Complete Example with Trace**

**Input**: `coins = [1,2,5], amount = 11`

**Setup**:
- **DP Array**: `int[12]` (Indices 0 to 11)
- **Initialization**: `dp[0] = 0`, all others = `12` (our "Infinity")

**Step-by-Step Trace**:

**Amounts 1 through 4**:
- **At `i=1`**: Only coin `1` fits (`1 >= 1`). `dp[1] = dp[0] + 1 = 1`
- **At `i=2`**:
  - Coin `1`: `dp[2] = dp[1] + 1 = 2`
  - Coin `2`: `dp[2] = dp[0] + 1 = 1` (Winner: Min is 1)
- **At `i=3`**:
  - Coin `1`: `dp[3] = dp[2] + 1 = 2`
  - Coin `2`: `dp[3] = dp[1] + 1 = 2`
  - `dp[3] = 2` (e.g., `2+1` or `1+1+1`)
- **At `i=4`**:
  - Coin `1`: `dp[4] = dp[3] + 1 = 3`
  - Coin `2`: `dp[4] = dp[2] + 1 = 2`
  - `dp[4] = 2` (e.g., `2+2`)

**Amount 5 (The first big jump)**:
- Coin `1`: `dp[5] = dp[4] + 1 = 3`
- Coin `2`: `dp[5] = dp[3] + 1 = 3`
- **Coin `5`**: `dp[5] = dp[0] + 1 = 1`
- **Result**: `dp[5] = 1` (Matches perfectly)

**Amount 10**:
- Coin `1`: `dp[10] = dp[9] + 1 = 4`
- Coin `2`: `dp[10] = dp[8] + 1 = 4`
- **Coin `5`**: `dp[10] = dp[5] + 1 = 2`
- **Result**: `dp[10] = 2` (this represents `5+5`)

**The Final Goal: Amount 11**:
1. **Try Coin `1`**:
   - Remainder: `11 - 1 = 10`
   - Look up `dp[10]`: It is `2`
   - Calculation: `dp[11] = dp[10] + 1 = 3`

2. **Try Coin `2`**:
   - Remainder: `11 - 2 = 9`
   - Look up `dp[9]`: It is `3` (e.g., `5+2+2`)
   - Calculation: `dp[11] = dp[9] + 1 = 4`

3. **Try Coin `5`**:
   - Remainder: `11 - 5 = 6`
   - Look up `dp[6]`: It is `2` (e.g., `5+1`)
   - Calculation: `dp[11] = dp[6] + 1 = 3`

**Final Comparison**: `dp[11] = min(3, 4, 3) = 3`

#### **Why the remainder `i - coin > 0` worked**

When calculating for **11**, the algorithm didn't have to "re-solve" how to make 10 or 6. It just looked at the table:
- "Oh, I know the best way to make **10** is **2** coins (`5+5`)"
- "If I add my **1** coin to that, I get **11** using **3** coins (`5+5+1`)"

#### **Summary Table (Simplified)**

| i | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | **11** |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| **dp[i]** | 0 | 1 | 1 | 2 | 2 | 1 | 2 | 2 | 3 | 3 | 2 | **3** |

#### **The DP Code Pattern**

```java
// java
// LC 322 - Coin Change
// IDEA: min coins per amount; order is irrelevant so either nesting works
// time = O(n * amount), space = O(amount)
public int coinChange(int[] coins, int amount) {
    if (amount == 0) return 0;

    // dp[i] = min coins to make amount i
    int[] dp = new int[amount + 1];

    // Initialize with "Infinity" (amount + 1 is safe)
    Arrays.fill(dp, amount + 1);

    // Base case: 0 coins needed for 0 amount
    dp[0] = 0;

    // Iterate through every amount from 1 to amount
    for (int i = 1; i <= amount; i++) {
        // For each amount, try every coin
        for (int coin : coins) {
            // CRITICAL CONDITION: Check if coin fits
            if (i >= coin) {
                // DP equation: Min of (current value) OR
                // (1 coin + coins needed for remainder)
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }

    // If value is still "Infinity", we couldn't reach it
    return dp[amount] > amount ? -1 : dp[amount];
}
```

**Reference**: See `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/CoinChange.java:356-408` for detailed implementation.

---

## Pattern Selection Strategy

```text
Is each item reusable?
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

## Summary

| If you remember one thing per row | … it is this |
|---|---|
| **0/1 vs unbounded** | the *inner loop direction*: backward blocks reuse, forward permits it |
| **Combinations vs permutations** | the *loop nesting*: items-outer counts sets, amount-outer counts sequences |
| **Partition problems** | "split into two equal halves" ⇒ subset-sum to `total / 2` |
| **LC 494 Target Sum** | `sum1 = (total + T) / 2`, but guard `abs(T) <= total` and `(total + T)` even first |
| **The guard** | `i - coin >= 0`, not `> 0` — the `== 0` case reads the seeded `dp[0]` |
| **Bounded knapsack** | binary-split each item into `1, 2, 4, …` copies, then run plain 0/1 |
