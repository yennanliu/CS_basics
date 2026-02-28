# Recursion to Dynamic Programming Conversion

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

```
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

### 1-1) Fibonacci Sequence

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
```
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
