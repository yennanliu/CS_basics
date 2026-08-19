# Monotonic Stack + DP

> **Scope** — Problems where a monotonic stack carries a DP value: "how many rounds does this element survive", largest-rectangle-style area DP, and the maximal-square / count-squares grid recurrences.
> **See also**: [monotonic_stack.md](./monotonic_stack.md) — the plain stack technique for next-greater / previous-smaller queries, with no DP on top; [dp.md](./dp.md) — the rest of the DP patterns.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## Overview

### Key Properties

- **Complexity**: `O(n)` time, `O(n)` space — each index is pushed and popped at most once, and the
  DP value rides along on the stack entry.
- **Core Idea**: the brute force *simulates rounds* of removals and is `O(n^2)`. A monotonic stack
  collapses the whole simulation: when a bigger element pops a run of smaller ones, the DP value it
  inherits is the **max** of the popped chain, not a fresh count.
- **When to Use**: "how many rounds until the array stops changing", "largest rectangle/square", or
  any per-element answer that depends on the nearest bigger/smaller neighbour.

### Problem Categories

| Category | Question | LC |
|----------|----------|----|
| **Survival rounds** | how many passes until element `i` is deleted? | 2289 |
| **Histogram area** | largest rectangle bounded by the nearest smaller bars | 84, 85 |
| **Grid squares** | largest / count of all-1 square submatrices | 221, 1277 |
| **One-pass counter DP** | cheapest split point in a single scan | 926 |

### References

- [monotonic_stack.md](./monotonic_stack.md) — the plain stack technique, no DP layered on top
- [dp.md](./dp.md) — the rest of the DP pattern family

## Templates & Algorithms

### Pattern Overview

**When to use**: Problems where each element depends on how long it "survives" before being dominated/removed by a larger element to its left (or right). The key signal is a **simulation that removes elements round by round** — the brute-force is O(N²) per step; the stack+DP collapses the whole process to O(N).

**Core Idea**:
- Maintain a **monotonic decreasing stack** of indices.
- `dp[i]` = number of rounds element `i` survives before being removed (0 if never removed).
- When a new element `nums[i]` pops smaller elements off the stack, those smaller elements will be removed. The key insight: if `nums[i]` must wait for a previously-popped element's chain to clear first, `dp[i]` inherits the **maximum** wait time seen so far.

**Transition (left-to-right scan)**:
```text
currentSteps = 0
while stack not empty AND nums[i] >= nums[stack.top()]:
    currentSteps = max(currentSteps, dp[stack.pop()])

if stack not empty:          // a larger element still blocks nums[i]
    dp[i] = currentSteps + 1
else:                        // nums[i] is a new global maximum — never removed
    dp[i] = 0

answer = max(dp[i]) for all i
```

**Transition (right-to-left scan — alternative)**:
```text
for i from n-1 down to 0:
    maxSteps = 0
    while stack not empty AND nums[i] > nums[stack.top()]:
        maxSteps = max(maxSteps + 1, dp[stack.pop()])
    dp[i] = maxSteps
    res = max(res, dp[i])
    stack.push(i)
```

---

### Template: LC 2289 — Steps to Make Array Non-Decreasing

**Problem**: Each step removes every element `nums[i]` where `nums[i-1] > nums[i]`. Return the number of steps until the array is non-decreasing.

**Why Mono Stack + DP works**:
- Each element is eventually eaten by the first larger element to its left.
- The number of steps for `nums[i]` to be eaten equals 1 plus the maximum steps needed by any intermediate smaller element between `nums[i]` and its "killer".
- The monotonic stack tracks exactly who the current "killer" is.

**Java — left-to-right (forward scan)**:
```java
// java
// LC 2289 - Steps to Make Array Non-decreasing
// IDEA: forward scan; a popped chain hands its max survival time to the element that ate it
// time = O(n), space = O(n)
public int totalSteps(int[] nums) {
    int n = nums.length, maxSteps = 0;
    int[] dp = new int[n];
    Stack<Integer> stack = new Stack<>();   // monotonic decreasing (by value)

    for (int i = 0; i < n; i++) {
        int currentSteps = 0;

        // Pop elements that nums[i] will outlive (nums[i] >= them)
        while (!stack.isEmpty() && nums[i] >= nums[stack.peek()]) {
            currentSteps = Math.max(currentSteps, dp[stack.pop()]);
        }

        if (!stack.isEmpty()) {
            // A larger element still exists to the left → nums[i] will be removed
            dp[i] = currentSteps + 1;
            maxSteps = Math.max(maxSteps, dp[i]);
        }
        // else dp[i] = 0 (never removed)

        stack.push(i);
    }
    return maxSteps;
}
```

**Java — right-to-left (backward scan)**:
```java
// java
// LC 2289 - Steps to Make Array Non-decreasing
// IDEA: same recurrence scanned right-to-left; dp[i] = rounds before nums[i] disappears
// time = O(n), space = O(n)
public int totalSteps(int[] nums) {
    int n = nums.length, res = 0;
    int[] dp = new int[n];
    Stack<Integer> stack = new Stack<>();

    for (int i = n - 1; i >= 0; i--) {
        int maxSteps = 0;
        while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
            maxSteps = Math.max(maxSteps + 1, dp[stack.pop()]);
        }
        dp[i] = maxSteps;
        res = Math.max(res, dp[i]);
        stack.push(i);
    }
    return res;
}
```

**Dry-run: `nums = [10, 1, 2, 7, 1, 3]`** (forward scan)

| i | nums[i] | Pops | currentSteps | dp[i] | stack (indices) |
|---|---------|------|-------------|-------|-----------------|
| 0 | 10 | — | 0 | 0 | [0] |
| 1 | 1 | none (1 < 10) | 0 | **1** | [0,1] |
| 2 | 2 | pop 1 (2≥1), dp[1]=1 | 1 | **2** | [0,2] |
| 3 | 7 | pop 2 (7≥2), dp[2]=2 | 2 | **3** | [0,3] |
| 4 | 1 | none (1 < 7) | 0 | **1** | [0,3,4] |
| 5 | 3 | pop 4 (3≥1), dp[4]=1 | 1 | **2** | [0,3,5] |

Answer = **3**.

Why `dp[3] = 3`? Element `7` must wait: step 1 removes `1`, step 2 removes `2`, only then can `10` eat `7` in step 3.

---

### Key Insights

1. **`Math.max(currentSteps, dp[stack.pop()])`** — when `nums[i]` pops multiple elements, it inherits the *longest* chain of removals it had to wait for, not just the most recent one.
2. **`dp[i] = 0`** when the stack is empty — `nums[i]` is a new global maximum and is never removed.
3. The **stack invariant** (monotone decreasing by value) ensures that every element still on the stack has a larger element waiting to its left.

---

### **Maximal Square / Count Squares Pattern (LC 1277, LC 221)** 🟦

#### 🎯 Pattern — Maximal Square

| Aspect | Detail |
|--------|--------|
| **Category** | 2D Grid DP — Bottom-right Corner Expansion |
| **State** | `dp[i][j]` = side length of the **largest all-ones square** whose bottom-right corner is at `(i, j)` |
| **Transition** | `dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1` |
| **Base Cases** | First row or first column: `dp[i][j] = matrix[i][j]` (at most 1×1) |
| **Answer (LC 1277)** | Sum of all `dp[i][j]` values — each value counts how many squares end here |
| **Answer (LC 221)** | `max(dp[i][j])²` — largest square area |
| **Time** | O(m × n) |
| **Space** | O(m × n) standard, O(n) space-optimized |

#### 💡 Core Idea — Maximal Square

**The "Magic" Transition**: `dp[i][j] = min(top, left, top-left) + 1`

> If the three neighbors all support a square of side `k`, then `(i, j)` can be the bottom-right of a square of side `k+1`. The **minimum** of the three determines the bottleneck.

**Why `dp[i][j]` also equals the count of squares ending at `(i, j)`**:
- A cell with `dp[i][j] = 3` can be the bottom-right corner of squares of size 1×1, 2×2, and 3×3
- So it contributes **3** to the total count
- Summing all `dp[i][j]` = total count of all squares (LC 1277)

```text
Matrix:       dp values:     Contribution:
0 1 1 1       0 1 1 1        0+1+1+1 = 3   (row 0)
1 1 1 1  →    1 1 2 2   →   1+1+2+2 = 6   (row 1)
0 1 1 1       0 1 2 3        0+1+2+3 = 6   (row 2)
                                  Total = 15 ✓
```

#### **Java Implementation (Bottom-Up 2D DP)**

```java
// java
// IDEA: the square recurrence, isolated
// LC 1277: Count Square Submatrices with All Ones
public int countSquares(int[][] matrix) {
    int rows = matrix.length, cols = matrix[0].length;
    int[][] dp = new int[rows][cols];
    int result = 0;

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (matrix[i][j] == 1) {
                if (i == 0 || j == 0) {
                    dp[i][j] = 1;  // first row/col: only 1×1 possible
                } else {
                    dp[i][j] = Math.min(
                        Math.min(dp[i-1][j], dp[i][j-1]),
                        dp[i-1][j-1]
                    ) + 1;
                }
                result += dp[i][j];  // dp[i][j] = count of squares ending here
            }
        }
    }
    return result;
}
```

**Alternative with (n+1) × (m+1) sizing (avoids first-row/col special case)**:
```java
// java
// LC 1277 - Count Square Submatrices with All Ones
// IDEA: dp[i][j] = side of the largest all-1 square ending at (i,j); summing dp counts them all
// time = O(m * n), space = O(m * n)
public int countSquares(int[][] matrix) {
    int row = matrix.length, col = matrix[0].length;
    int[][] dp = new int[row + 1][col + 1];  // +1 removes boundary check
    int ans = 0;
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++) {
            if (matrix[i][j] == 1) {
                dp[i+1][j+1] = Math.min(
                    Math.min(dp[i][j+1], dp[i+1][j]),
                    dp[i][j]
                ) + 1;
                ans += dp[i+1][j+1];
            }
        }
    }
    return ans;
}
```

#### **Space-Optimized (O(n) 1D DP)**

```java
// java
// LC 1277 - Count Square Submatrices with All Ones
// IDEA: same recurrence rolled onto one row; keep the old up-left value in a temp
// time = O(m * n), space = O(n)
public int countSquares(int[][] matrix) {
    int row = matrix.length, col = matrix[0].length, result = 0, prev = 0;
    int[] dp = new int[col + 1];

    for (int i = 1; i <= row; i++) {
        for (int j = 1; j <= col; j++) {
            int temp = dp[j];
            if (matrix[i-1][j-1] == 1) {
                dp[j] = 1 + Math.min(prev, Math.min(dp[j-1], dp[j]));
                result += dp[j];
            } else {
                dp[j] = 0;
            }
            prev = temp;
        }
    }
    return result;
}
```

#### **LC 1277 vs LC 221 Comparison**

| Aspect | LC 1277: Count Squares | LC 221: Maximal Square |
|--------|------------------------|------------------------|
| **Goal** | Count ALL squares of all sizes | Find the LARGEST square |
| **DP transition** | Same: `min(top, left, diagonal) + 1` | Same: `min(top, left, diagonal) + 1` |
| **Answer** | `sum(dp[i][j])` | `max(dp[i][j])²` |
| **Key insight** | `dp[i][j]` counts squares ending here | `dp[i][j]` is the side length |
| **Difficulty** | Medium | Medium |

#### **Why `min` and Not `max`?**

```text
Consider:    dp[i-1][j] = 3   →  top supports 3×3
             dp[i][j-1] = 1   →  left supports 1×1 only
             dp[i-1][j-1] = 2  →  diagonal supports 2×2

Even though top supports 3×3, the LEFT neighbor only supports 1×1.
If you tried to make a 2×2 square ending at (i,j), the cell one
column left would need to support a 2×2 — but it only supports 1×1.
So the bottleneck is min(3, 1, 2) = 1 → dp[i][j] = 2.
```

The `min` ensures all three "arms" of the square are simultaneously valid.

#### **Similar LeetCode Problems (Maximal Square)** 📚

| Problem | LC # | Key Difference | Algorithm |
|---------|------|----------------|-----------|
| **Count Square Submatrices** | 1277 | Count ALL squares (sum dp) | 2D DP (min of 3 neighbors) |
| **Maximal Square** | 221 | Find LARGEST square (max dp) | 2D DP (min of 3 neighbors) |
| **Maximal Rectangle** | 85 | Any rectangle of 1s, not just squares | Histogram + stack (per row) |
| **Count Submatrices with All Ones** | 1504 | All rectangles, not just squares | Row compression + prefix sums |
| **Largest Plus Sign** | 764 | Plus-shape instead of square | DP in 4 directions |
| **Minimum Path Sum** | 64 | Min-cost path (not all-ones shape) | 2D DP (min of 2 neighbors) |

#### **Pattern Recognition Checklist (Maximal Square)** ✅

Use this pattern when:
- ✅ Grid contains 0s and 1s
- ✅ Problem asks about **squares** (not rectangles) of all-ones
- ✅ Need to count or find max square(s) in a binary matrix
- ✅ Keywords: "square submatrix", "all ones", "count squares"

**File Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/CountSquareSubmatricesWithAllOnes.java`

---

### Template: One-Pass DP — Flip String to Monotone Increasing (LC 926)

#### 🎯 Pattern — Flip String

| Aspect | Detail |
|--------|--------|
| **Pattern** | One-pass 1D DP with two running state variables |
| **State** | `flips` = min flips to make prefix monotone; `ones` = count of `'1'`s seen so far |
| **Transition** | Only triggered when a `'0'` is encountered (conflict with prior `'1'`s) |
| **Time** | O(n) |
| **Space** | O(1) |

#### 💡 Core Idea — Flip String

> A monotone-increasing binary string looks like `000...111`.  
> Imagine scanning left-to-right and maintaining an invisible **split point**: everything left of it must be `0`, everything right must be `1`.

When we see a `'1'`, we just count it (`ones++`) — it doesn't force a flip yet.  
When we see a `'0'` **after** some `'1'`s, there's a conflict. Two choices:

1. **Flip this `'0'` → `'1'`**: costs `flips + 1` (keep all previous decisions, pay 1 more)
2. **Flip all previous `'1'`s → `'0'`**: costs `ones` (reset the prefix, undo all prior `'1'`s)

Take the cheaper option: `flips = min(flips + 1, ones)`

**Key insight**: `ones` is the "undo cost" — how expensive it would be to backtrack and flip everything seen so far to `0`.

#### **Core Code (Java)**

```java
// java
// LC 926 - Flip String to Monotone Increasing
// IDEA: one pass — either flip this 1 to 0, or flip every 1 seen so far
// time = O(n), space = O(1)
// LC 926 — O(n) time, O(1) space
public int minFlipsMonoIncr(String s) {
    int flips = 0;   // min flips to make prefix monotone
    int ones = 0;    // count of '1's seen so far

    for (char c : s.toCharArray()) {
        if (c == '1') {
            ones++;              // potential future cost if we later want all-0 prefix
        } else {                 // c == '0' — conflict with prior '1's
            // choice 1: flip this '0' → '1'  : cost = flips + 1
            // choice 2: flip all prior '1'→'0': cost = ones
            flips = Math.min(flips + 1, ones);
        }
    }

    return flips;
}
```

#### **Dry Run: `s = "00110"`**

| i | char | ones | flips (before) | transition | flips (after) |
|---|------|------|----------------|-----------|---------------|
| 0 | `'0'` | 0 | 0 | min(0+1, 0)=0 | **0** |
| 1 | `'0'` | 0 | 0 | min(0+1, 0)=0 | **0** |
| 2 | `'1'` | 1 | 0 | ones++ | **0** |
| 3 | `'1'` | 2 | 0 | ones++ | **0** |
| 4 | `'0'` | 2 | 0 | min(0+1, 2)=1 | **1** |

Result: `1` ✅ (flip last `'0'` → `'1'`: `"00111"`)

#### **Dry Run: `s = "00011000"`**

| i | char | ones | flips |
|---|------|------|-------|
| 0-2 | `'0'` | 0 | 0 |
| 3-4 | `'1'` | 2 | 0 |
| 5 | `'0'` | 2 | min(0+1,2)=1 |
| 6 | `'0'` | 2 | min(1+1,2)=2 |
| 7 | `'0'` | 2 | min(2+1,2)=2 |

Result: `2` ✅ (flip the two `'1'`s → `'0'`: `"00000000"`)

#### **Alternative: Two-pass prefix sum approach**

```java
// java
// LC 926 - Flip String to Monotone Increasing
// IDEA: prefix-sum variant — try every split point between the 0-block and the 1-block
// time = O(n), space = O(1)
// Count total zeroes first, then scan for the best "split point"
public int minFlipsMonoIncr(String s) {
    int zeroes = 0, ones = 0;
    for (char c : s.toCharArray()) if (c == '0') zeroes++;

    int output = zeroes;  // worst case: flip all '0' → '1'
    for (char c : s.toCharArray()) {
        if (c == '0') zeroes--;      // this '0' is now on the right → must flip
        else          ones++;        // this '1' is on the left → must flip
        output = Math.min(output, zeroes + ones);
    }
    return output;
}
```

Both approaches are O(n) / O(1). The one-pass version is more elegant for interviews.

#### **Similar LeetCode Problems (Flip String)** 📚

| Problem | LC # | Similarity | Key Variable |
|---------|------|-----------|--------------|
| **Flip String to Monotone Increasing** | 926 | Exact pattern | `flips`, `ones` |
| **Minimum Number of Flips to Make Binary String Alternating** | 1888 | Flip to alternating pattern | Sliding window + parity count |
| **Make Array Non-decreasing / Non-increasing** | — | Same "split point" idea | Prefix/suffix min-max |
| **Partition Array into Disjoint Intervals** | 915 | Left-max ≤ right-min | Running max/min |
| **Maximum Subarray** (Kadane's) | 53 | Running state: keep or restart | `maxEndingHere` |
| **Best Time to Buy and Sell Stock** | 121 | Running min (buy price) | `minPrice`, `maxProfit` |
| **Count Binary Substrings** | 696 | Scan binary runs | `prev`, `cur` group counts |

#### **Pattern Recognition Checklist (Flip String)** ✅

Use this pattern when:
- ✅ Binary string transformation into a target shape (`000...111`, `010101...`, etc.)
- ✅ At each position, **two choices** exist and costs depend on prior decisions
- ✅ O(1) space is achievable because you only need running counters, not the full history
- ✅ Keywords: "minimum flips", "monotone", "non-decreasing binary", "partition into prefix/suffix"

**File Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/FlipStringToMonotoneIncreasing.java`

## Problems by Pattern

### Similar LeetCode Problems

| LC # | Problem | What the Stack+DP Tracks | Difficulty |
|------|---------|--------------------------|------------|
| **2289** | Steps to Make Array Non-Decreasing | Rounds until element removed | Medium |
| **84** | Largest Rectangle in Histogram | Previous smaller bar index | Hard |
| **85** | Maximal Rectangle | Row-by-row histogram (uses LC 84) | Hard |
| **907** | Sum of Subarray Minimums | Contribution of each min element | Medium |
| **1856** | Maximum Subarray Min-Product | Max product using mono stack | Medium |
| **739** | Daily Temperatures | Days until warmer temperature | Medium |
| **901** | Online Stock Span | Days since last higher price | Medium |
| **456** | 132 Pattern | Track min prefix + mono stack | Medium |
| **2866** | Beautiful Towers II | Max height contribution left+right | Medium |

**Pattern recognition checklist**:
- ✅ Problem involves removing/consuming elements round by round
- ✅ Each element is dominated by the first larger/smaller neighbor
- ✅ "How many steps/rounds" until an element is eliminated
- ✅ Brute-force simulation would be O(N²); need O(N)
- ✅ Answer is a max over individual element costs

**Common Pitfalls**:
- Using `>` vs `>=` in the while condition changes whether equal-valued elements eat each other — match exactly to the problem's removal rule.
- In the forward scan, `dp[i] = 0` (no assignment needed) for stack-empty case; forgetting this means global maxima get wrong dp values.
- Don't confuse left-scan (`>=`) with right-scan (`>`) — they encode different "who eats whom" semantics.

---

## Summary

| Shape | Recognise it by | Core line |
|-------|-----------------|-----------|
| **Survival rounds** | "repeat until the array stops changing" | `cur = max(cur + 1, dp[popped])` while popping |
| **Histogram area** | "largest rectangle / area under bars" | width = `i - stack.peek() - 1` after popping |
| **Square in a grid** | "largest all-1 square" | `dp[i][j] = 1 + min(up, left, up-left)` |
| **One-pass counter** | "cheapest single split point" | track `onesSoFar` and `flips = min(flips + 1, onesSoFar)` |

**Why `max` and not `+1` in the survival recurrence**: a tall element must wait for *every* chain it
swallows to finish, so it inherits the slowest of them — not the sum, and not a fresh count.
