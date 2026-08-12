# Recursion to Dynamic Programming Conversion

> **Scope** — The mechanical pipeline: **recursion → memoisation → tabulation → space-optimised**, applied to one problem at a time.
> **See also**: [dp.md](./dp.md) — the DP reference; [dp_pattern.md](./dp_pattern.md) — the pattern you are converting *to*; [recursion.md](./recursion.md) — plain recursion without the DP step.

- **Core idea**: Transform recursive solutions into iterative DP for better performance
- **When to use**: Recursive solutions with overlapping subproblems and optimal substructure
- **Key benefits**: Eliminate redundant calculations, reduce space from O(n) stack to O(n) or O(1) array
- **Common pattern**: Recognize memoization opportunities, then convert to bottom-up tabulation

**Conversion Steps:**
1. Identify base cases
2. Find recursive relation
3. Add memoization (Top-Down DP)
4. Convert to tabulation (Bottom-Up DP)
5. Optimize space if possible

---

## LeetCode Problem Lists

- [Recursion](https://leetcode.com/problem-list/recursion/)
- [Memoization](https://leetcode.com/problem-list/memoization/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 0) Concept

### 0-0) When to Convert Recursion to DP

**Indicators that DP is applicable:**

> "When you hear a problem beginning with the following statements, it's often (though not always) a good candidate for recursion: 'Design an algorithm to compute the nth...', 'Write code to list the first n...', 'Implement a method to compute all...', and so on."
> — *Cracking the Coding Interview*, 6th Edition, p.130

**Requirements for DP:**
1. **Overlapping Subproblems**: Same subproblems solved multiple times
2. **Optimal Substructure**: Optimal solution contains optimal solutions to subproblems
3. **Memoization Opportunity**: Results can be cached for reuse

**Recognition Patterns:**
- "Find the nth..."
- "Count ways to..."
- "Minimum/maximum..."
- "Optimize..."
- Multiple recursive calls with same parameters

### 0-1) Conversion Strategy

```text
Recursion (Exponential)
    ↓
Top-Down DP (Memoization)
    ↓
Bottom-Up DP (Tabulation)
    ↓
Space-Optimized DP
```

**Top-Down (Memoization):**
- Keep recursive structure
- Add cache (memo) to store results
- Easy to implement from recursion
- Space: O(n) for memo + O(n) for call stack

**Bottom-Up (Tabulation):**
- Build solution iteratively
- Fill DP table from base cases up
- No recursion overhead
- Space: O(n) for DP table only

**Space Optimization:**
- Identify what previous states are actually needed
- Often can reduce from O(n) to O(k) where k is constant
- Example: Fibonacci only needs last 2 values

---

## 1) Complete Examples: Recursion → DP

### 1-1) Fibonacci Sequence — LC 509

**Problem:** Compute the nth Fibonacci number where F(n) = F(n-1) + F(n-2), F(0)=0, F(1)=1.

#### Step 1: Naive Recursion

```python
# Python - Naive Recursion
def fib_recursive(n):
    """
    Time: O(2^n) - exponential
    Space: O(n) - call stack depth
    """
    if n <= 1:
        return n
    return fib_recursive(n-1) + fib_recursive(n-2)
```

```java
// Java - Naive Recursion
public int fibRecursive(int n) {
    /**
     * time = O(2^N) - exponential
     * space = O(N) - call stack
     */
    if (n <= 1) return n;
    return fibRecursive(n-1) + fibRecursive(n-2);
}
```

**Problem:** Massive redundant calculations!
```text
fib(5)
├── fib(4)
│   ├── fib(3)
│   │   ├── fib(2)
│   │   │   ├── fib(1)
│   │   │   └── fib(0)
│   │   └── fib(1)
│   └── fib(2)  <- Computed again!
│       ├── fib(1)
│       └── fib(0)
└── fib(3)      <- Computed again!
    ├── fib(2)  <- Computed again!
    │   ├── fib(1)
    │   └── fib(0)
    └── fib(1)
```

#### Step 2: Top-Down DP (Memoization)

```python
# Python - Top-Down DP
def fib_memo(n, memo=None):
    """
    Time: O(n) - each subproblem solved once
    Space: O(n) - memo dict + call stack
    """
    if memo is None:
        memo = {}

    if n <= 1:
        return n

    if n in memo:
        return memo[n]

    memo[n] = fib_memo(n-1, memo) + fib_memo(n-2, memo)
    return memo[n]
```

```java
// Java - Top-Down DP
public int fibMemo(int n) {
    return fibMemoHelper(n, new int[n+1]);
}

private int fibMemoHelper(int n, int[] memo) {
    /**
     * time = O(N)
     * space = O(N) - memo + call stack
     */
    if (n <= 1) return n;

    if (memo[n] != 0) return memo[n];

    memo[n] = fibMemoHelper(n-1, memo) + fibMemoHelper(n-2, memo);
    return memo[n];
}
```

#### Step 3: Bottom-Up DP (Tabulation)

```python
# Python - Bottom-Up DP
def fib_dp(n):
    """
    Time: O(n)
    Space: O(n) - DP table
    """
    if n <= 1:
        return n

    dp = [0] * (n + 1)
    dp[0] = 0
    dp[1] = 1

    for i in range(2, n + 1):
        dp[i] = dp[i-1] + dp[i-2]

    return dp[n]
```

```java
// Java - Bottom-Up DP
public int fibDP(int n) {
    /**
     * time = O(N)
     * space = O(N)
     */
    if (n <= 1) return n;

    int[] dp = new int[n + 1];
    dp[0] = 0;
    dp[1] = 1;

    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i-1] + dp[i-2];
    }

    return dp[n];
}
```

#### Step 4: Space-Optimized DP

```python
# Python - Space Optimized
def fib_optimized(n):
    """
    Time: O(n)
    Space: O(1) - only 2 variables
    """
    if n <= 1:
        return n

    prev2, prev1 = 0, 1

    for i in range(2, n + 1):
        current = prev1 + prev2
        prev2 = prev1
        prev1 = current

    return prev1
```

```java
// Java - Space Optimized
public int fibOptimized(int n) {
    /**
     * time = O(N)
     * space = O(1)
     */
    if (n <= 1) return n;

    int prev2 = 0, prev1 = 1;

    for (int i = 2; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

**Summary:**

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Naive Recursion | O(2^n) | O(n) | Exponential, unusable for n>40 |
| Memoization | O(n) | O(n) + O(n) | Easy to code, still has stack overhead |
| Tabulation | O(n) | O(n) | No recursion, cleaner |
| Space Optimized | O(n) | O(1) | Best overall |

---

### 1-2) Climbing Stairs (LC 70)

**Problem:** Climbing n stairs, can climb 1 or 2 steps. How many distinct ways?

#### Step 1: Recursion

```python
def climbStairs_recursive(n):
    """Time: O(2^n), Space: O(n)"""
    if n <= 2:
        return n
    return climbStairs_recursive(n-1) + climbStairs_recursive(n-2)
```

#### Step 2: Memoization

```python
def climbStairs_memo(n, memo=None):
    """Time: O(n), Space: O(n)"""
    if memo is None:
        memo = {}

    if n <= 2:
        return n

    if n in memo:
        return memo[n]

    memo[n] = climbStairs_memo(n-1, memo) + climbStairs_memo(n-2, memo)
    return memo[n]
```

#### Step 3: Bottom-Up DP

```java
// LC 70 - Climbing Stairs
public int climbStairs(int n) {
    /**
     * time = O(N)
     * space = O(N)
     */
    if (n <= 2) return n;

    int[] dp = new int[n + 1];
    dp[1] = 1;
    dp[2] = 2;

    for (int i = 3; i <= n; i++) {
        dp[i] = dp[i-1] + dp[i-2];
    }

    return dp[n];
}
```

#### Step 4: Space Optimized

```java
public int climbStairsOptimized(int n) {
    /**
     * time = O(N)
     * space = O(1)
     */
    if (n <= 2) return n;

    int prev2 = 1, prev1 = 2;

    for (int i = 3; i <= n; i++) {
        int current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

---

### 1-3) House Robber (LC 198)

**Problem:** Array of house values. Can't rob adjacent houses. Maximum robbery amount?

#### Step 1: Recursion

```python
def rob_recursive(nums, i=0):
    """
    Time: O(2^n)
    Space: O(n)
    """
    if i >= len(nums):
        return 0

    # Choice: rob current house or skip
    rob_current = nums[i] + rob_recursive(nums, i+2)
    skip_current = rob_recursive(nums, i+1)

    return max(rob_current, skip_current)
```

#### Step 2: Memoization

```python
def rob_memo(nums, i=0, memo=None):
    """Time: O(n), Space: O(n)"""
    if memo is None:
        memo = {}

    if i >= len(nums):
        return 0

    if i in memo:
        return memo[i]

    rob_current = nums[i] + rob_memo(nums, i+2, memo)
    skip_current = rob_memo(nums, i+1, memo)

    memo[i] = max(rob_current, skip_current)
    return memo[i]
```

#### Step 3: Bottom-Up DP

```java
// LC 198 - House Robber
public int rob(int[] nums) {
    /**
     * time = O(N)
     * space = O(N)
     */
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];

    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    dp[1] = Math.max(nums[0], nums[1]);

    for (int i = 2; i < nums.length; i++) {
        // Choice: rob current + dp[i-2] OR skip current (dp[i-1])
        dp[i] = Math.max(nums[i] + dp[i-2], dp[i-1]);
    }

    return dp[nums.length - 1];
}
```

#### Step 4: Space Optimized

```java
public int robOptimized(int[] nums) {
    /**
     * time = O(N)
     * space = O(1)
     */
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];

    int prev2 = nums[0];
    int prev1 = Math.max(nums[0], nums[1]);

    for (int i = 2; i < nums.length; i++) {
        int current = Math.max(nums[i] + prev2, prev1);
        prev2 = prev1;
        prev1 = current;
    }

    return prev1;
}
```

---

### 1-4) Coin Change (LC 322)

**Problem:** Array of coin denominations, target amount. Minimum coins needed?

#### Step 1: Recursion

```python
def coinChange_recursive(coins, amount):
    """
    Time: O(S^n) where S = amount, n = coins
    Space: O(amount)
    """
    if amount == 0:
        return 0
    if amount < 0:
        return -1

    min_coins = float('inf')

    for coin in coins:
        result = coinChange_recursive(coins, amount - coin)
        if result >= 0:
            min_coins = min(min_coins, result + 1)

    return min_coins if min_coins != float('inf') else -1
```

#### Step 2: Memoization

```python
def coinChange_memo(coins, amount, memo=None):
    """Time: O(S × n), Space: O(S)"""
    if memo is None:
        memo = {}

    if amount == 0:
        return 0
    if amount < 0:
        return -1
    if amount in memo:
        return memo[amount]

    min_coins = float('inf')

    for coin in coins:
        result = coinChange_memo(coins, amount - coin, memo)
        if result >= 0:
            min_coins = min(min_coins, result + 1)

    memo[amount] = min_coins if min_coins != float('inf') else -1
    return memo[amount]
```

#### Step 3: Bottom-Up DP

```java
// LC 322 - Coin Change
public int coinChange(int[] coins, int amount) {
    /**
     * time = O(S × N) where S = amount, N = coins
     * space = O(S)
     */
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1); // Infinity placeholder

    dp[0] = 0; // Base case

    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (coin <= i) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }

    return dp[amount] > amount ? -1 : dp[amount];
}
```

---

## 2) Common LeetCode Problems

### Recursion → DP Conversions

| Problem | Difficulty | Recursive Pattern | DP Type |
|---------|------------|-------------------|---------|
| LC 70 | Easy | Stairs = (n-1) + (n-2) | 1D DP |
| LC 198 | Medium | Rob = max(rob, skip) | 1D DP |
| LC 322 | Medium | Min coins for amount | Unbounded knapsack |
| LC 509 | Easy | Fibonacci | Classic |
| LC 746 | Easy | Min cost climbing | 1D DP |
| LC 139 | Medium | Word break | String DP |
| LC 300 | Medium | Longest increasing | Subsequence DP |
| LC 416 | Medium | Partition subset | 0/1 knapsack |

---

## 3) Conversion Checklist

### ✅ Step-by-Step Guide

1. **Identify Base Cases**
   - What are the simplest inputs?
   - What can be returned immediately?

2. **Find Recursive Relation**
   - How does F(n) relate to F(n-1), F(n-2), etc.?
   - What choices/decisions are made at each step?

3. **Add Memoization (Top-Down)**
   - Create cache/memo structure
   - Check cache before computing
   - Store result after computing

4. **Convert to Tabulation (Bottom-Up)**
   - Create DP array
   - Fill base cases
   - Iterate from small to large subproblems
   - Use recurrence relation to fill table

5. **Optimize Space**
   - Identify which previous states are needed
   - Use variables instead of full array if possible

---

## 4) Interview Tips

### 💡 Recognition Patterns

**When to suspect DP:**
- "Find the nth..."
- "Count ways to..."
- "Minimum/maximum..."
- Multiple recursive calls
- Overlapping subproblems

**Conversion Strategy:**
1. Start with recursion (easier to understand)
2. Add memoization (quick win)
3. Convert to bottom-up if asked
4. Optimize space if time permits

### 🎯 Interview Talking Points

1. **Why DP is better:**
   - "Eliminates redundant calculations"
   - "Trades space for time"
   - "Linear time instead of exponential"

2. **Top-Down vs Bottom-Up:**
   - "Top-down is easier to code from recursion"
   - "Bottom-up is more efficient (no stack overhead)"
   - "Both have same time complexity"

3. **Space Optimization:**
   - "Only need previous k states"
   - "Can reduce from O(n) to O(k)"
   - "Common for 1D DP problems"

### 📊 Complexity Analysis

| Approach | Typical Time | Typical Space | Notes |
|----------|--------------|---------------|-------|
| Naive Recursion | O(2^n) | O(n) | Unusable for n>30 |
| Memoization | O(n) | O(n) + O(n) | Easy to code |
| Tabulation | O(n) | O(n) | More efficient |
| Space Optimized | O(n) | O(1) or O(k) | Best overall |

---

## Summary

**Core Principles:**
- ✅ Recursion → Memoization → Tabulation → Space Optimization
- ✅ Look for overlapping subproblems
- ✅ Memoization keeps recursive structure, adds cache
- ✅ Tabulation builds solution iteratively from base cases

**When to Use:**
- "Find the nth..." problems
- Optimization problems (min/max)
- Counting problems (number of ways)
- Problems with choices at each step

**Interview Strategy:**
1. Start with recursive solution
2. Identify overlapping subproblems
3. Add memoization
4. Convert to bottom-up if needed
5. Optimize space if possible

**Key Insight:** Every DP problem can be solved recursively first, then optimized with memoization/tabulation.

## 5) LC Examples

### 5-1) House Robber (LC 198) — Recursion → Memoization → DP
> Cannot rob two adjacent houses; dp[i] = max(dp[i-1], dp[i-2] + nums[i]).

```java
// LC 198 - House Robber
// IDEA: DP — dp[i] = max money robbing up to house i
// time = O(N), space = O(1)
public int rob(int[] nums) {
    if (nums.length == 1) return nums[0];
    int prev2 = nums[0], prev1 = Math.max(nums[0], nums[1]);
    for (int i = 2; i < nums.length; i++) {
        int curr = Math.max(prev1, prev2 + nums[i]);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

### 5-2) Word Break (LC 139) — Top-down Recursion → DP
> dp[i] = true if s[0..i] can be segmented using wordDict.

```java
// LC 139 - Word Break
// IDEA: DP — dp[i] means s[0..i-1] can be segmented
// time = O(N^2), space = O(N)
public boolean wordBreak(String s, List<String> wordDict) {
    Set<String> dict = new HashSet<>(wordDict);
    int n = s.length();
    boolean[] dp = new boolean[n + 1];
    dp[0] = true;
    for (int i = 1; i <= n; i++)
        for (int j = 0; j < i; j++)
            if (dp[j] && dict.contains(s.substring(j, i))) {
                dp[i] = true;
                break;
            }
    return dp[n];
}
```

### 5-3) Edit Distance (LC 72) — 2D DP (String → String)
> dp[i][j] = min operations to convert s1[0..i] to s2[0..j].

```java
// LC 72 - Edit Distance
// IDEA: 2D DP — dp[i][j] = min ops to convert word1[0..i-1] to word2[0..j-1]
// time = O(M*N), space = O(M*N)
public int minDistance(String word1, String word2) {
    int m = word1.length(), n = word2.length();
    int[][] dp = new int[m + 1][n + 1];
    for (int i = 0; i <= m; i++) dp[i][0] = i;
    for (int j = 0; j <= n; j++) dp[0][j] = j;
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            if (word1.charAt(i-1) == word2.charAt(j-1))
                dp[i][j] = dp[i-1][j-1];
            else
                dp[i][j] = 1 + Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
    return dp[m][n];
}
```

### 5-4) Fibonacci Number (LC 509) — Recursion → Memoization → DP → O(1)
> Classic example showing all 4 levels of optimization from naive recursion.

```java
// LC 509 - Fibonacci Number
// IDEA: Iterative DP — O(N) time, O(1) space (vs O(2^N) naive recursion)
// time = O(N), space = O(1)
public int fib(int n) {
    if (n <= 1) return n;
    int prev2 = 0, prev1 = 1;
    for (int i = 2; i <= n; i++) {
        int curr = prev1 + prev2;
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

### 5-5) Unique Paths (LC 62) — 2D Recursion → DP
> Recursion `f(i,j) = f(i-1,j) + f(i,j-1)` → bottom-up DP with row compression.

```java
// LC 62 - Unique Paths
// IDEA: DP with 1D rolling array — dp[j] = paths to reach column j in current row
// time = O(M*N), space = O(N)
public int uniquePaths(int m, int n) {
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            dp[j] += dp[j-1];
    return dp[n-1];
}
```

### 5-6) Triangle (LC 120) — Top-Down Recursion → Bottom-Up DP
> dp[i][j] = min path sum from (i,j) to bottom; convert triangle recursion bottom-up.

```java
// LC 120 - Triangle
// IDEA: Bottom-up DP in-place — start from second-to-last row, accumulate minimum path
// time = O(N^2), space = O(1) modifying input
public int minimumTotal(List<List<Integer>> triangle) {
    int n = triangle.size();
    int[] dp = new int[n];
    for (int i = 0; i < n; i++) dp[i] = triangle.get(n-1).get(i);
    for (int row = n-2; row >= 0; row--)
        for (int col = 0; col <= row; col++)
            dp[col] = triangle.get(row).get(col) + Math.min(dp[col], dp[col+1]);
    return dp[0];
}
```

### 5-7) Longest Palindromic Subsequence (LC 516) — Interval DP
> `dp[i][j]` = LPS length in s[i..j]; recursion `f(i,j)` → bottom-up by increasing length.

```java
// LC 516 - Longest Palindromic Subsequence
// IDEA: Interval DP — dp[i][j] = LPS in s[i..j]
// time = O(N^2), space = O(N^2)
public int longestPalindromeSubseq(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];
    for (int i = 0; i < n; i++) dp[i][i] = 1;
    for (int len = 2; len <= n; len++)
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            if (s.charAt(i) == s.charAt(j)) dp[i][j] = dp[i+1][j-1] + 2;
            else dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
        }
    return dp[0][n-1];
}
```

### 5-8) Coin Change (LC 322) — Recursion with Pruning → DP
> Recursion `f(amount) = 1 + min(f(amount-coin))` → bottom-up unbounded knapsack DP.

```java
// LC 322 - Coin Change
// IDEA: Bottom-up DP — dp[i] = min coins for amount i; unbounded knapsack
// time = O(amount * |coins|), space = O(amount)
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);
    dp[0] = 0;
    for (int i = 1; i <= amount; i++)
        for (int coin : coins)
            if (coin <= i) dp[i] = Math.min(dp[i], dp[i - coin] + 1);
    return dp[amount] > amount ? -1 : dp[amount];
}
```

### 5-9) Combination Sum IV (LC 377) — Recursion → DP (Order Matters)
> dp[i] = number of ordered combinations summing to i; unlike knapsack, order of adding matters.

```java
// LC 377 - Combination Sum IV
// IDEA: DP — dp[i] = number of ordered ways to reach sum i
// time = O(target * |nums|), space = O(target)
public int combinationSum4(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1;
    for (int i = 1; i <= target; i++)
        for (int num : nums)
            if (num <= i) dp[i] += dp[i - num];
    return dp[target];
}
```

### 5-10) Minimum Cost to Cut a Stick (LC 1547) — Interval DP
> dp[i][j] = min cost to make all cuts between cut[i] and cut[j]; try each middle cut.

```java
// LC 1547 - Minimum Cost to Cut a Stick
// IDEA: Interval DP — insert endpoints; dp[i][j] = min cost for cuts between i and j
// time = O(M^3), space = O(M^2)  M = cuts.length
public int minCost(int n, int[] cuts) {
    int m = cuts.length;
    int[] c = new int[m + 2];
    c[0] = 0; c[m+1] = n;
    for (int i = 0; i < m; i++) c[i+1] = cuts[i];
    Arrays.sort(c);
    int[][] dp = new int[m+2][m+2];
    for (int len = 2; len <= m+1; len++)
        for (int i = 0; i + len <= m+1; i++) {
            int j = i + len;
            dp[i][j] = Integer.MAX_VALUE;
            for (int k = i+1; k < j; k++)
                dp[i][j] = Math.min(dp[i][j], c[j]-c[i] + dp[i][k] + dp[k][j]);
        }
    return dp[0][m+1];
}
```

### 5-11) Partition Array for Maximum Sum (LC 1043) — 1D DP
> dp[i] = max sum of partitioning array up to index i; try all sub-partitions of size 1..k.

```java
// LC 1043 - Partition Array for Maximum Sum
// IDEA: DP — dp[i] = max sum when array[0..i-1] is partitioned
// time = O(N * k), space = O(N)
public int maxSumAfterPartitioning(int[] arr, int k) {
    int n = arr.length;
    int[] dp = new int[n + 1];
    for (int i = 1; i <= n; i++) {
        int maxVal = 0;
        for (int j = 1; j <= k && i - j >= 0; j++) {
            maxVal = Math.max(maxVal, arr[i-j]);
            dp[i] = Math.max(dp[i], dp[i-j] + maxVal * j);
        }
    }
    return dp[n];
}
```

---

## 6) More Conversion Templates (state shapes not covered above)

Everything in sections 1) and 5) memoizes on **one index** (or two indices scanned in a fixed
direction). The four templates below cover the state shapes that break that assumption — and they
are exactly where the recursion → memo → table conversion gets interesting.

### 6-0) Quick Decision Table — "what shape is my state?"

| Recursive signature | Memo key | Can you tabulate? | Template | LC |
|---------------------|----------|-------------------|----------|-----|
| `f(i)` | `int[n]` | Yes, loop `i` ascending | §1-2 / §1-3 | 70, 198 |
| `f(i, mode)` — a small flag rides along | `int[n][K]` | Yes, loop `i`, unroll `mode` | **§6-1** | 122, 714, 121 |
| `f(i, k)` — `k` derived from *how you got here* | `HashMap` / set-per-node | Yes, but push **forward** into reachable states | **§6-2** | 403 |
| `f(i, j)` on two sequences, one index may **not** advance | `boolean[m+1][n+1]` | Yes, but fill **backwards** (`i--`, `j--`) | **§6-3** | 10, 44 |
| `f(cell)` on a graph, no obvious scan order | `int[m][n]` | Only after an explicit topological order | **§6-4** | 329 |

**Rule of thumb:** memoization always works; tabulation only works once you can name an order in
which every state's dependencies are already computed. When you cannot name that order cheaply,
**stop at memoization** — that is a complete answer, not a half answer.

---

### 6-1) State-Machine DP — `f(index, state)` — LC 122

> **Twist vs §1-3 House Robber:** House Robber encodes "did I take the last one?" by *skipping an
> index* (`i+2`). Here the flag cannot be folded into the index, so it becomes a **second, tiny
> dimension**. Once you see `f(i, holding)`, the table is `dp[n+1][2]` and the space-optimized form
> is one variable per state.

**Problem (LC 122 - Best Time to Buy and Sell Stock II):** unlimited transactions, hold at most one
share; maximise profit.

#### Step 1: Brute-force recursion — every day, 2 choices

```python
# python
# IDEA: at day i you are either holding or free; try "do nothing" vs "trade"
# time = O(2^N), space = O(N)
def maxProfit_rec(prices):
    def f(i, holding):
        if i == len(prices):
            return 0
        best = f(i + 1, holding)                        # do nothing
        if holding:
            best = max(best, prices[i] + f(i + 1, False))   # sell
        else:
            best = max(best, -prices[i] + f(i + 1, True))   # buy
        return best
    return f(0, False)
```

```java
// java
// LC 122 - Best Time to Buy and Sell Stock II
// IDEA: recursion on (day, holding) — do nothing / buy / sell
// time = O(2^N), space = O(N)
public int maxProfitRec(int[] prices) {
    return f(prices, 0, false);
}
private int f(int[] prices, int i, boolean holding) {
    if (i == prices.length) return 0;
    int best = f(prices, i + 1, holding);                                       // do nothing
    if (holding) best = Math.max(best, prices[i] + f(prices, i + 1, false));    // sell
    else         best = Math.max(best, -prices[i] + f(prices, i + 1, true));    // buy
    return best;
}
```

#### Step 2: Memoization — the flag joins the key

```python
# python
# IDEA: only 2N distinct (i, holding) states exist
# time = O(N), space = O(N)
from functools import lru_cache

def maxProfit_memo(prices):
    @lru_cache(None)
    def f(i, holding):
        if i == len(prices):
            return 0
        best = f(i + 1, holding)
        if holding:
            best = max(best, prices[i] + f(i + 1, False))
        else:
            best = max(best, -prices[i] + f(i + 1, True))
        return best
    return f(0, False)
```

```java
// java
// LC 122 - memoized: memo[i][hold], hold in {0,1}
// time = O(N), space = O(N)
public int maxProfitMemo(int[] prices) {
    return g(prices, 0, 0, new Integer[prices.length][2]);
}
private int g(int[] prices, int i, int hold, Integer[][] memo) {
    if (i == prices.length) return 0;
    if (memo[i][hold] != null) return memo[i][hold];
    int best = g(prices, i + 1, hold, memo);
    if (hold == 1) best = Math.max(best, prices[i] + g(prices, i + 1, 0, memo));
    else           best = Math.max(best, -prices[i] + g(prices, i + 1, 1, memo));
    return memo[i][hold] = best;
}
```

#### Step 3: Tabulation — recursion went `i → i+1`, so the table fills backwards

```java
// java
// LC 122 - bottom-up: dp[i][0] = best from day i while free, dp[i][1] = while holding
// time = O(N), space = O(N)
public int maxProfitTable(int[] prices) {
    int n = prices.length;
    int[][] dp = new int[n + 1][2];                 // dp[n][*] = 0 → base case
    for (int i = n - 1; i >= 0; i--) {
        dp[i][0] = Math.max(dp[i + 1][0], -prices[i] + dp[i + 1][1]);
        dp[i][1] = Math.max(dp[i + 1][1],  prices[i] + dp[i + 1][0]);
    }
    return dp[0][0];
}
```

#### Step 4: Space optimization — one variable per state, scan forward

```java
// java
// LC 122 - two rolling states; note prevFree so both updates use the SAME old row
// time = O(N), space = O(1)
public int maxProfitOptimized(int[] prices) {
    int free = 0, hold = Integer.MIN_VALUE / 2;
    for (int p : prices) {
        int prevFree = free;
        free = Math.max(free, hold + p);        // sell today
        hold = Math.max(hold, prevFree - p);    // buy today
    }
    return free;
}
```

```python
# python
# IDEA: tuple assignment evaluates the RHS first, so no temp variable is needed
# time = O(N), space = O(1)
def maxProfit_opt(prices):
    free, hold = 0, float('-inf')
    for p in prices:
        free, hold = max(free, hold + p), max(hold, free - p)
    return free
```

**Same template, one line changed:**

```java
// java
// LC 714 - Best Time to Buy and Sell Stock with Transaction Fee
// IDEA: identical state machine; pay the fee on the sell edge
// time = O(N), space = O(1)
public int maxProfitWithFee(int[] prices, int fee) {
    int free = 0, hold = Integer.MIN_VALUE / 2;
    for (int p : prices) {
        int prevFree = free;
        free = Math.max(free, hold + p - fee);   // <-- only change
        hold = Math.max(hold, prevFree - p);
    }
    return free;
}

// LC 121 - Best Time to Buy and Sell Stock (one transaction only)
// IDEA: "at most one buy" ⇒ entering `hold` must start from 0 profit, not from `free`
// time = O(N), space = O(1)
public int maxProfitOnce(int[] prices) {
    int free = 0, hold = Integer.MIN_VALUE / 2;
    for (int p : prices) {
        free = Math.max(free, hold + p);
        hold = Math.max(hold, -p);               // <-- not prevFree - p
    }
    return free;
}
```

---

### 6-2) Composite-State Memo — the state is not an index — LC 403

> **Twist:** the choice set at stone `i` depends on **the jump that got you there**, so `f(i)` is
> not well defined — you need `f(i, k)`. `k` is unbounded-ish, so the memo is a hash map, and the
> table is "per stone, the **set** of jump sizes that can land on it".

**Problem (LC 403 - Frog Jump):** from stone `i` reached with jump `k`, the next jump must be
`k-1`, `k`, or `k+1` and must land exactly on a stone. Can the frog reach the last stone?

#### Step 1 → 2: Recursion + memo on the pair `(i, k)`

```python
# python
# IDEA: f(i, k) = can we finish from stone i, having arrived with jump k
# time = O(N^2), space = O(N^2)  (at most N distinct jumps per stone)
from functools import lru_cache

def canCross(stones):
    idx = {s: i for i, s in enumerate(stones)}      # stone value -> index, O(1) landing test

    @lru_cache(None)
    def f(i, k):
        if i == len(stones) - 1:
            return True
        for step in (k - 1, k, k + 1):
            if step <= 0:
                continue
            nxt = idx.get(stones[i] + step)
            if nxt is not None and f(nxt, step):
                return True
        return False

    return f(0, 0)                                  # jump 0 forces the first jump to be 1
```

```java
// java
// LC 403 - Frog Jump (top-down)
// IDEA: memo keyed on the PAIR (stone index, incoming jump) — pack it into a long
// time = O(N^2), space = O(N^2)
public boolean canCross(int[] stones) {
    Map<Integer, Integer> idx = new HashMap<>();
    for (int i = 0; i < stones.length; i++) idx.put(stones[i], i);
    return dfs(stones, idx, 0, 0, new HashMap<>());
}
private boolean dfs(int[] stones, Map<Integer, Integer> idx, int i, int k, Map<Long, Boolean> memo) {
    if (i == stones.length - 1) return true;
    long key = ((long) i << 20) | k;                // k <= n, so 20 bits is plenty
    Boolean cached = memo.get(key);
    if (cached != null) return cached;
    boolean ok = false;
    for (int step = k - 1; step <= k + 1 && !ok; step++) {
        if (step <= 0) continue;
        Integer nxt = idx.get(stones[i] + step);
        if (nxt != null) ok = dfs(stones, idx, nxt, step, memo);
    }
    memo.put(key, ok);
    return ok;
}
```

#### Step 3: Tabulation — **push forward** instead of pulling backward

There is no `dp[i] = combine(dp[i-1], ...)` here, because you cannot know *which* jumps are legal at
stone `i` until you know who jumped to it. So flip the direction: for each stone, propagate every
jump size it can be reached with to the stones it can reach.

```java
// java
// LC 403 - bottom-up: dp[stone] = set of jump sizes that can land on that stone
// time = O(N^2), space = O(N^2)
public boolean canCrossTable(int[] stones) {
    Map<Integer, Set<Integer>> dp = new HashMap<>();
    for (int s : stones) dp.put(s, new HashSet<>());
    dp.get(stones[0]).add(0);
    for (int s : stones)                            // stones are sorted ⇒ valid processing order
        for (int k : dp.get(s))
            for (int step = k - 1; step <= k + 1; step++) {
                if (step <= 0) continue;
                Set<Integer> nxt = dp.get(s + step); // step > 0 ⇒ never mutates the set being iterated
                if (nxt != null) nxt.add(step);
            }
    return !dp.get(stones[stones.length - 1]).isEmpty();
}
```

```python
# python
# IDEA: same forward push; dict of sets replaces the 2D table
# time = O(N^2), space = O(N^2)
def canCross_table(stones):
    dp = {s: set() for s in stones}
    dp[stones[0]].add(0)
    for s in stones:
        for k in dp[s]:
            for step in (k - 1, k, k + 1):
                if step > 0 and s + step in dp:
                    dp[s + step].add(step)
    return len(dp[stones[-1]]) > 0
```

---

### 6-3) Two-Sequence Matching where an index may **not** advance — LC 10

> **Twist vs §5-3 Edit Distance:** edit distance always shrinks `i`, `j`, or both, so the table
> fills naturally with ascending loops. With `*`, the branch `f(i+1, j)` **keeps `j` fixed** —
> the recursion runs `i → i+1`, `j → j+2`, so the table must be filled from the **end backwards**.
> Writing the memo first and then reading off the loop directions from the recursive calls is the
> whole trick.

**Problem (LC 10 - Regular Expression Matching):** `.` matches any single char, `x*` matches zero
or more `x`. Full match required.

#### Step 1 → 2: Recursion, then memo (the recursion is already the hard part)

```python
# python
# IDEA: f(i, j) = does s[i:] match p[j:]; a '*' means "drop the pair" OR "consume one char of s"
# time = O(M*N), space = O(M*N)
from functools import lru_cache

def isMatch(s, p):
    @lru_cache(None)                 # delete this line and it is the O(2^N) brute force
    def f(i, j):
        if j == len(p):
            return i == len(s)
        first = i < len(s) and p[j] in (s[i], '.')
        if j + 1 < len(p) and p[j + 1] == '*':
            return f(i, j + 2) or (first and f(i + 1, j))   # zero copies | one more copy
        return first and f(i + 1, j + 1)
    return f(0, 0)
```

```java
// java
// LC 10 - Regular Expression Matching (top-down)
// IDEA: memo[i][j] over suffixes; '*' branches into (skip pattern pair) | (consume one char)
// time = O(M*N), space = O(M*N)
public boolean isMatch(String s, String p) {
    return dfs(s, p, 0, 0, new Boolean[s.length() + 1][p.length() + 1]);
}
private boolean dfs(String s, String p, int i, int j, Boolean[][] memo) {
    if (memo[i][j] != null) return memo[i][j];
    boolean ans;
    if (j == p.length()) {
        ans = (i == s.length());
    } else {
        boolean first = i < s.length() && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.');
        if (j + 1 < p.length() && p.charAt(j + 1) == '*')
            ans = dfs(s, p, i, j + 2, memo) || (first && dfs(s, p, i + 1, j, memo));
        else
            ans = first && dfs(s, p, i + 1, j + 1, memo);
    }
    return memo[i][j] = ans;
}
```

#### Step 3: Tabulation — mechanical translation, loops run backwards

```java
// java
// LC 10 - bottom-up. dp[i][j] = s[i..] matches p[j..]; base case dp[m][n] = true
// time = O(M*N), space = O(M*N)
public boolean isMatchTable(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[m][n] = true;                                     // empty vs empty
    for (int i = m; i >= 0; i--)                         // i descends: recursion used i+1
        for (int j = n - 1; j >= 0; j--) {               // j descends: recursion used j+1, j+2
            boolean first = i < m && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.');
            if (j + 1 < n && p.charAt(j + 1) == '*')
                dp[i][j] = dp[i][j + 2] || (first && dp[i + 1][j]);
            else
                dp[i][j] = first && dp[i + 1][j + 1];
        }
    return dp[0][0];
}
```

```python
# python
# time = O(M*N), space = O(M*N)
def isMatch_table(s, p):
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[m][n] = True
    for i in range(m, -1, -1):
        for j in range(n - 1, -1, -1):
            first = i < m and p[j] in (s[i], '.')
            if j + 1 < n and p[j + 1] == '*':
                dp[i][j] = dp[i][j + 2] or (first and dp[i + 1][j])
            else:
                dp[i][j] = first and dp[i + 1][j + 1]
    return dp[0][0]
```

**Variation — LC 44 Wildcard Matching:** here `*` is a **standalone** token (not "previous char
repeated"), so it consumes zero-or-more chars by itself: `dp[i][j] = dp[i][j+1] || dp[i+1][j]`.
No look-ahead at `p[j+1]`, and no `j+2`.

```java
// java
// LC 44 - Wildcard Matching
// IDEA: same backwards table; '*' = use zero chars (j+1) OR eat one char of s (i+1)
// time = O(M*N), space = O(M*N)
public boolean isMatchWildcard(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[m][n] = true;
    for (int i = m; i >= 0; i--)
        for (int j = n - 1; j >= 0; j--) {
            if (p.charAt(j) == '*')
                dp[i][j] = dp[i][j + 1] || (i < m && dp[i + 1][j]);
            else {
                boolean first = i < m && (p.charAt(j) == s.charAt(i) || p.charAt(j) == '?');
                dp[i][j] = first && dp[i + 1][j + 1];
            }
        }
    return dp[0][0];
}
```

---

### 6-4) Memo on a DAG — when tabulation needs an explicit order — LC 329

> **Twist:** every earlier template had a built-in scan order (index, length, row). On a grid where
> you may move in **any** of 4 directions, there is none — so plain memoized DFS *is* the intended
> solution. This is the case where "convert to bottom-up" costs you a sort/topological pass and
> buys nothing but the removal of recursion depth.

**Problem (LC 329 - Longest Increasing Path in a Matrix):** longest strictly increasing path,
moving up/down/left/right. Strict increase ⇒ the "move" graph is a **DAG** ⇒ no cycles ⇒ memo is safe.

#### Steps 1 + 2: Recursion with memo (the answer you should write in an interview)

```java
// java
// LC 329 - Longest Increasing Path in a Matrix
// IDEA: memo[i][j] = longest increasing path STARTING at (i,j); each cell computed once
// time = O(M*N), space = O(M*N)
private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

public int longestIncreasingPath(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length, best = 0;
    int[][] memo = new int[m][n];                     // 0 == "not computed" (a path is always >= 1)
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            best = Math.max(best, dfs(matrix, i, j, memo));
    return best;
}
private int dfs(int[][] g, int i, int j, int[][] memo) {
    if (memo[i][j] != 0) return memo[i][j];
    int best = 1;
    for (int[] d : DIRS) {
        int x = i + d[0], y = j + d[1];
        if (x < 0 || y < 0 || x >= g.length || y >= g[0].length || g[x][y] <= g[i][j]) continue;
        best = Math.max(best, 1 + dfs(g, x, y, memo));
    }
    return memo[i][j] = best;
}
```

```python
# python
# IDEA: no visited set needed — strict increase already forbids revisiting
# time = O(M*N), space = O(M*N)
from functools import lru_cache

def longestIncreasingPath(matrix):
    m, n = len(matrix), len(matrix[0])

    @lru_cache(None)
    def dfs(i, j):
        best = 1
        for x, y in ((i+1, j), (i-1, j), (i, j+1), (i, j-1)):
            if 0 <= x < m and 0 <= y < n and matrix[x][y] > matrix[i][j]:
                best = max(best, 1 + dfs(x, y))
        return best

    return max(dfs(i, j) for i in range(m) for j in range(n))
```

#### Step 3: Tabulation — you must *manufacture* the order (sort cells by value)

```python
# python
# IDEA: process cells in ascending value — then every smaller neighbour is already final
# time = O(M*N*log(M*N)) because of the sort, space = O(M*N)
def longestIncreasingPath_table(matrix):
    m, n = len(matrix), len(matrix[0])
    order = sorted((matrix[i][j], i, j) for i in range(m) for j in range(n))
    dp = [[1] * n for _ in range(m)]
    best = 0
    for v, i, j in order:
        for x, y in ((i+1, j), (i-1, j), (i, j+1), (i, j-1)):
            if 0 <= x < m and 0 <= y < n and matrix[x][y] < v:
                dp[i][j] = max(dp[i][j], dp[x][y] + 1)   # dp[x][y] is already final
        best = max(best, dp[i][j])
    return best
```

```java
// java
// LC 329 - bottom-up via an explicit topological order (cells sorted ascending by value)
// time = O(M*N*log(M*N)), space = O(M*N)
public int longestIncreasingPathTable(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length;
    Integer[] order = new Integer[m * n];
    for (int c = 0; c < m * n; c++) order[c] = c;
    Arrays.sort(order, (a, b) -> matrix[a / n][a % n] - matrix[b / n][b % n]);
    int[][] dp = new int[m][n];
    int best = 0;
    for (int c : order) {
        int i = c / n, j = c % n;
        dp[i][j] = 1;
        for (int[] d : DIRS) {
            int x = i + d[0], y = j + d[1];
            if (x < 0 || y < 0 || x >= m || y >= n || matrix[x][y] >= matrix[i][j]) continue;
            dp[i][j] = Math.max(dp[i][j], dp[x][y] + 1);
        }
        best = Math.max(best, dp[i][j]);
    }
    return best;
}
```

**Takeaway:** the bottom-up version is *slower* (extra `log` factor) and longer. Say this out loud in
an interview — "memoized DFS is optimal here; tabulation would need a topological sort" — instead of
converting reflexively.

---

## 7) Variations of templates already in this doc

Each row reuses a template above; the last column names the single thing that changes.

| LC | Problem | Reuses | The twist |
|----|---------|--------|-----------|
| 91 | Decode Ways | §1-2 Climbing Stairs | Same `f(i-1)+f(i-2)` shape, but each branch is **gated**: 1-digit only if `s[i] != '0'`, 2-digit only if `10 <= s[i-1..i] <= 26`. Space-opt to O(1) is identical. |
| 337 | House Robber III | §1-3 House Robber | Same rob/skip choice on a **tree**. Memo is `Map<TreeNode,Integer>`; returning a `(rob, skip)` pair from post-order removes the memo entirely. |
| 63 | Unique Paths II | §5-5 Unique Paths | Obstacles: same rolling row, but `dp[j] = 0` on a blocked cell instead of `dp[j] += dp[j-1]`. |
| 1143 | Longest Common Subsequence | §5-3 Edit Distance | Same `dp[i][j]` grid over two strings; on match `dp[i-1][j-1]+1`, else `max(dp[i-1][j], dp[i][j-1])`. Answer at `dp[m][n]`. |
| 718 | Maximum Length of Repeated Subarray | §5-3 Edit Distance | LCS but **contiguous**: a mismatch resets `dp[i][j] = 0`, and the answer is the **max over the whole table**, not the corner cell. |
| 647 | Palindromic Substrings | §5-7 Longest Palindromic Subsequence | Same interval table, boolean payload: `dp[i][j] = s[i]==s[j] && (j-i<3 || dp[i+1][j-1])`; count the `true`s. |
| 494 | Target Sum | §2 LC 416 (0/1 knapsack) | State is `(i, runningSum)` and the sum can go **negative** — use a hash-map memo, or shift by `total` to index an array. |
| 221 | Maximal Square | §5-5 grid DP | `dp[i][j] = min(up, left, diag) + 1` (a **min**, not a sum), and the answer is the global max side, squared. |
| 152 | Maximum Product Subarray | §1-3 rolling state | Needs **two** rolling states (`maxEnding`, `minEnding`) because a negative number swaps them. |

```java
// java
// LC 91 - Decode Ways  (variation of §1-2: constrained Fibonacci)
// IDEA: prev1 = ways to decode s[0..i-1], prev2 = ways to decode s[0..i-2]
// time = O(N), space = O(1)
public int numDecodings(String s) {
    if (s.charAt(0) == '0') return 0;
    int prev2 = 1, prev1 = 1;
    for (int i = 1; i < s.length(); i++) {
        int cur = 0;
        if (s.charAt(i) != '0') cur += prev1;                       // take 1 digit
        int two = (s.charAt(i-1) - '0') * 10 + (s.charAt(i) - '0');
        if (two >= 10 && two <= 26) cur += prev2;                   // take 2 digits
        prev2 = prev1;
        prev1 = cur;
    }
    return prev1;
}
```

```python
# python
# LC 91 - Decode Ways
# time = O(N), space = O(1)
def numDecodings(s):
    if s[0] == '0':
        return 0
    prev2, prev1 = 1, 1
    for i in range(1, len(s)):
        cur = 0
        if s[i] != '0':
            cur += prev1
        if 10 <= int(s[i-1:i+1]) <= 26:
            cur += prev2
        prev2, prev1 = prev1, cur
    return prev1
```

```java
// java
// LC 337 - House Robber III  (variation of §1-3: same choice, tree-shaped)
// IDEA: post-order returns {rob this node, skip this node} — the pair IS the memo
// time = O(N), space = O(H)
public int rob(TreeNode root) {
    int[] r = robPair(root);
    return Math.max(r[0], r[1]);
}
private int[] robPair(TreeNode node) {
    if (node == null) return new int[]{0, 0};
    int[] l = robPair(node.left), r = robPair(node.right);
    int rob  = node.val + l[1] + r[1];                          // children must be skipped
    int skip = Math.max(l[0], l[1]) + Math.max(r[0], r[1]);     // children free to choose
    return new int[]{rob, skip};
}
```

```python
# python
# LC 337 - House Robber III
# time = O(N), space = O(H)
def rob(root):
    def helper(node):
        if not node:
            return (0, 0)                       # (rob node, skip node)
        l, r = helper(node.left), helper(node.right)
        return (node.val + l[1] + r[1], max(l) + max(r))
    return max(helper(root))
```

---

## 8) Fits an existing template as-is (reference)

| LC | Problem | Maps onto |
|----|---------|-----------|
| 53 | Maximum Subarray | §1-3 rolling state — `f(i) = max(nums[i], nums[i] + f(i-1))`, keep a global max (Kadane) |
| 279 | Perfect Squares | §1-4 Coin Change — coins are the perfect squares `1,4,9,...`, minimise count |
| 55 / 45 | Jump Game / Jump Game II | DP `O(N^2)` is the natural first answer; state the greedy `O(N)` reachability/BFS-layer solution as the follow-up |
