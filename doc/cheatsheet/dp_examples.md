# DP Worked Examples

> **Scope** — The worked-solution archive behind [dp.md](./dp.md): one canonical Java / Python solution per classic DP problem plus the problems-by-pattern index, with no templates or theory of its own.
> **See also**: [dp.md](./dp.md) — the templates these solutions instantiate; [dp_advanced.md](./dp_advanced.md) — the rare techniques and deep dives; [dp_pattern.md](./dp_pattern.md) — a terser one-section-per-pattern template index; [stock_trading.md](./stock_trading.md) — every stock state-machine variant.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)

## Overview

Solutions only. Each example states the state definition, the transition, and one implementation per
language; the reasoning that produced it lives in the matching template in [dp.md](./dp.md).

### Key Properties
- **Complexity**: stated per solution, in the first comment of each code block
- **Core Idea**: read the template first, then the solution — not the other way round
- **When to Use**: revision, and checking your own solution against a canonical one

## Problems by Pattern

### **Linear DP Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Climbing Stairs | 70 | dp[i] = dp[i-1] + dp[i-2] | Easy |
| House Robber | 198 | Max with skip | Medium |
| Longest Increasing Subsequence | 300 | O(n²) or O(nlogn) | Medium |
| Maximum Subarray | 53 | Kadane's algorithm | Easy |
| Decode Ways | 91 | String DP | Medium |
| Word Break | 139 | Dictionary DP | Medium |
| Coin Change | 322 | Min coins | Medium |
| Integer Break | 343 | Max product, break vs no-break | Medium |

### **2D Grid Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Unique Paths | 62 | Path counting | Medium |
| Minimum Path Sum | 64 | Min cost path | Medium |
| Maximal Square | 221 | 2D expansion | Medium |
| Dungeon Game | 174 | Backward DP | Hard |
| Cherry Pickup | 741 | 3D DP | Hard |
| Number of Paths with Max Score | 1301 | Multi-value DP | Hard |

### **Interval DP Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Longest Palindromic Substring | 5 | Expand or DP | Medium |
| Palindrome Partitioning II | 132 | Min cuts | Hard |
| Burst Balloons | 312 | Interval multiplication | Hard |
| Minimum Cost to Merge Stones | 1000 | K-way merge | Hard |
| Strange Printer | 664 | Interval printing | Hard |

### **Game Theory / Minimax DP Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Predict the Winner | 486 | Relative score diff dp[i][j] | Medium |
| Stone Game | 877 | Same as 486 (always true for even length) | Medium |
| Stone Game II | 1140 | Minimax with variable take count | Medium |
| Stone Game III | 1406 | Suffix minimax DP | Hard |
| Optimal Division | 553 | Greedy insight from game theory | Medium |

### **Knapsack Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Partition Equal Subset Sum | 416 | 0/1 Knapsack | Medium |
| Target Sum | 494 | Sum to target | Medium |
| Last Stone Weight II | 1049 | Min difference | Medium |
| Ones and Zeroes | 474 | 2D Knapsack | Medium |
| **Perfect Squares** | **279** | **Unbounded (squares as coins, min count)** | **Medium** |
| **Coin Change 2** | **518** | **Unbounded (Coin→Amount = Combinations)** | **Medium** |
| **Combination Sum IV** | **377** | **Unbounded (Amount→Coin = Permutations)** | **Medium** |

### **State Machine Problems**
| Problem | LC # | Key Technique | Difficulty | States | Pattern |
|---------|------|---------------|------------|--------|---------|
| Best Time to Buy and Sell Stock II | 122 | Multiple transactions | Easy | 2 states | hold/cash |
| **Stock with Cooldown** | **309** | **3-state transitions** | **Medium** | **3 states** | **hold/sold/rest** |
| Stock with Transaction Fee | 714 | Fee consideration | Medium | 2 states | hold/cash |
| Stock III | 123 | At most 2 transactions | Hard | 4 states | buy1/sell1/buy2/sell2 |
| Stock IV | 188 | At most k transactions | Hard | 2k states | Dynamic states |

**Core Pattern Analysis: Stock Problems**

| Problem | Constraint | States Needed | Key Difference |
|---------|-----------|---------------|----------------|
| **LC 122** | Unlimited transactions | 2 (hold/cash) | Simple buy/sell |
| **LC 309** | Cooldown after sell | 3 (hold/sold/rest) | Need rest state |
| **LC 714** | Transaction fee | 2 (hold/cash) | Deduct fee when sell |
| **LC 123** | At most 2 transactions | 4 (2 buy/sell pairs) | Track transaction count |
| **LC 188** | At most k transactions | 2k states | Generalized k transactions |

**State Machine Pattern Recognition:**
```text
Question asks...                          → Use this pattern
─────────────────────────────────────────────────────────────
"Cooldown after action"                   → 3+ states (LC 309)
"Transaction fee/cost"                    → 2 states with cost
"Limited transactions (k times)"          → 2k states
"Unlimited transactions"                  → 2 states (hold/cash)
```

## Other High-Frequency DP Problems

> These are famous problems whose recurrence is a direct instance of a template already above — listed so the mapping is explicit.

| Problem | LC # | Template it reduces to | One-line idea |
|---------|------|------------------------|---------------|
| Trapping Rain Water | 42 | Prefix/suffix arrays (1D DP) | `water[i] = min(maxLeft[i], maxRight[i]) - h[i]`; two-pointer removes the arrays |
| Jump Game | 55 | 1D reachability DP → greedy | track furthest reachable index |
| Jump Game II | 45 | 1D DP → BFS-style greedy levels | `dp[i] = min(dp[j]+1)`, greedy does it in O(n) |
| Minimum Cost For Tickets | 983 | 1D linear DP over days | `dp[d] = min(dp[d-1]+c1, dp[d-7]+c7, dp[d-30]+c30)` |
| 01 Matrix | 542 | 2D grid DP (two passes) | pass 1 top-left→bottom-right, pass 2 reverse |
| Counting Bits | 338 | 1D DP with bit trick | `dp[i] = dp[i >> 1] + (i & 1)` |
| Is Subsequence | 392 | LCS degenerate case | two pointers O(n); LCS table if follow-up asks many queries |
| Pascal's Triangle / II | 118 / 119 | 1D rolling row | `row[j] += row[j-1]` iterating **backward** |

## Worked Examples

### 2-1) Unique Paths (LC 62) — Grid DP Count Paths

> dp[i][j] = dp[i-1][j] + dp[i][j-1]; paths from top-left to bottom-right on m×n grid.

```java
// java

// LC 62
// V0
// IDEA: 2D DP (fixed by gpt)
public int uniquePaths(int m, int n) {
    if (m == 0 || n == 0)
        return 0;

    int[][] dp = new int[m][n];

    /**  NOTE !!! init val as below
     *
     *  -> First row and first column = 1 path
     *    (only one way to go right/down)
     */
    for (int i = 0; i < m; i++) {
        dp[i][0] = 1;
    }
    for (int j = 0; j < n; j++) {
        dp[0][j] = 1;
    }

    // Fill the rest of the DP table
    // NOTE !!! i, j both start from 1
    // `(0, y), (x, 0)` already been initialized
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            /**  DP equation
             *
             *   dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
             */
            dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
        }
    }

    return dp[m - 1][n - 1];
}
```


```python
# 62. Unique Paths
# V0
# IDEA : BFS + dp (memory)
class Solution:
    def uniquePaths(self, m, n):

        # NOTE !!! we init paths as below
        paths = [[1]*n for _ in range(m)]
        
        q = deque()
        q.append((0,0))
        
        while q:
            row, col = q.popleft()
            
            if row == m or col == n or paths[row][col] > 1:
                continue 
            
            if row-1 >= 0 and col-1 >= 0:
                paths[row][col] = paths[row-1][col] + paths[row][col-1]
            
            q.append((row+1, col))
            q.append((row, col+1))
        
        return paths[-1][-1]

# V0'
# IDEA : DP
class Solution:
    def uniquePaths(self, m: int, n: int) -> int:
        d = [[1] * n for _ in range(m)]

        for col in range(1, m):
            for row in range(1, n):
                d[col][row] = d[col - 1][row] + d[col][row - 1]

        return d[m - 1][n - 1]
```


### 2-2) Maximum Product Subarray (LC 152) — Track Min/Max Product

> Track both max and min at each step (min can become max when multiplied by negative).

```python
# NOTE : there is also brute force approach
# V0
# IDEA : BRUTE FORCE (TLE) — the O(n^2) baseline
# https://leetcode.com/problems/maximum-product-subarray/solution/
class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if len(nums) == 0:
            return 0

        result = nums[0]

        for i in range(len(nums)):
            accu = 1
            for j in range(i, len(nums)):
                accu *= nums[j]
                result = max(result, accu)

        return result

# V1
# IDEA : DP (canonical — track running max AND min, a negative flips them)
# https://leetcode.com/problems/maximum-product-subarray/solution/
class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if len(nums) == 0:
            return 0

        max_so_far = nums[0]
        min_so_far = nums[0]
        result = max_so_far

        for i in range(1, len(nums)):
            curr = nums[i]
            temp_max = max(curr, max_so_far * curr, min_so_far * curr)
            min_so_far = min(curr, max_so_far * curr, min_so_far * curr)

            max_so_far = temp_max

            result = max(max_so_far, result)

        return result
```

```java
// java
// LC 152

// V0
// IDEA : DP
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Dynamic_Programming/maximum-product-subarray.py#L69
// IDEA : cur max = max (cur, cur * dp[k-1])
//        But, also needs to consider "minus number"
//        -> e.g.  (-1) * (-3) = 3
//        -> so we NEED to track maxSoFar, and minSoFar
public int maxProduct(int[] nums) {

    // null check
    if (nums.length == 0){
        return 0;
    }
    // init
    int maxSoFar = nums[0];
    int minSoFar = nums[0];
    int res = maxSoFar;

    for (int i = 1; i < nums.length; i++){

        int cur = nums[i];
        /**
         *  or, can use below trick to get max in 3 numbers
         *
         *   max = Math.max(Math.max(max * nums[i], min * nums[i]), nums[i]);
         *   min = Math.min(Math.min(temp * nums[i], min * nums[i]), nums[i]);
         *
         */
        int tmpMax = findMax(cur, maxSoFar * cur, minSoFar * cur);
        minSoFar = findMin(cur, maxSoFar * cur, minSoFar * cur);
        maxSoFar = tmpMax;

        res = Math.max(maxSoFar, res);
    }

    return res;
}

private int findMax(int a, int b, int c){
    if (a >= b && a >= c){
        return a;
    }
    else if (b >= a && b >= c){
        return b;
    }else{
        return c;
    }
}

private int findMin(int a, int b, int c){
    if (a <= b && a <= c){
        return a;
    }
    else if (b <= a && b <= c){
        return b;
    }else{
        return c;
    }
}
```

### 2-3) Best Time to Buy and Sell Stock with Transaction Fee (LC 714) — Two-State DP

> hold/free states; transition: hold = max(hold, free-price), free = max(free, hold+price-fee).

```java
// java
// LC 714

  // V0-1
  // IDEA: DP (gpt)
  /**
   * Solution Explanation:
   *
   *
   *  -  Use two variables to represent the state:
   *      1. hold: The maximum profit achievable
   *               while holding a stock at day i.
   *
   *      2. cash: The maximum profit achievable
   *               while not holding a stock at day i.
   *
   *  - Transition equations:
   *    - If holding a stock:
   *       hold = max(hold, cash - price[i])
   *
   *       NOTE: 2 cases we hold th stock: 1) already hold from previous day 2) buy a new stock today
   *       (`hold`: You already held the stock from a previous day -> If you decided not to make any changes today, then the profit remains the same as the previous hold.)
   *       (`cash - price[i]`: You buy the stock today -> To buy the stock today, you need to spend money, reducing your profit. The cost to buy the stock is prices[i]. However, the amount of money you can spend is the maximum profit you had when you were not holding a stock previously (cash).)
   *
   * (You either keep holding or buy a new stock.)
   *    - If not holding a stock:
   *       cash = max(cash, hold + price[i] - fee)
   *
   *
   * (You either keep not holding or sell the stock and pay the fee.)
   *    - Initialize:
   *       - hold = -prices[0] (If you buy the stock on the first day).
   *       -  cash = 0 (You haven’t made any transactions yet).
   *
   */
  /**
   *  Example Walkthrough:
   *
   * Input:
   *    •   Prices: [1, 3, 2, 8, 4, 9]
   *    •   Fee: 2
   *
   * Steps:
   *    1.  Day 0:
   *    •   hold = -1 (Buy the stock at price 1).
   *    •   cash = 0.
   *    2.  Day 1:
   *    •   cash = max(0, -1 + 3 - 2) = 0 (No selling since profit is 0).
   *    •   hold = max(-1, 0 - 3) = -1 (No buying since it’s already better to hold).
   *    3.  Day 2:
   *    •   cash = max(0, -1 + 2 - 2) = 0.
   *    •   hold = max(-1, 0 - 2) = -1.
   *    4.  Day 3:
   *    •   cash = max(0, -1 + 8 - 2) = 5 (Sell at price 8).
   *    •   hold = max(-1, 5 - 8) = -1.
   *    5.  Day 4:
   *    •   cash = max(5, -1 + 4 - 2) = 5.
   *    •   hold = max(-1, 5 - 4) = 1.
   *    6.  Day 5:
   *    •   cash = max(5, 1 + 9 - 2) = 8 (Sell at price 9).
   *    •   hold = max(1, 5 - 9) = 1.
   *
   * Output:
   *    •   cash = 8 (Max profit).
   *
   */
  public int maxProfit_0_1(int[] prices, int fee) {
        // Edge case
        if (prices == null || prices.length == 0) {
            return 0;
        }

        // Initialize states
        int hold = -prices[0]; // Maximum profit when holding a stock
        int cash = 0; // Maximum profit when not holding a stock

        // Iterate through prices
        for (int i = 1; i < prices.length; i++) {
            /**
             *  NOTE !!! there are 2 dp equations (e.g. cash, hold)
             */
            // Update cash and hold states
            cash = Math.max(cash, hold + prices[i] - fee); // Sell the stock
            hold = Math.max(hold, cash - prices[i]); // Buy the stock
        }

        // The maximum profit at the end is when not holding any stock
        return cash;
    }
```

### 2-4) Best Time to Buy and Sell Stock with Cooldown (LC 309) — Three-State DP

> States: held/sold/rest; sold → rest → held cycle enforces one-day cooldown.

```java
// java
// LC 309. Best Time to Buy and Sell Stock with Cooldown

/**
 * Problem: You can buy and sell stock multiple times, but after selling,
 * you must cooldown for 1 day before buying again.
 *
 * Key Insight: This requires 3 states instead of the typical 2 states
 * because we need to track the cooldown period.
 */

// V0-1: 2D DP (n x 3 array) - Most Intuitive
/**
 * State Definition:
 * dp[i][0] = Max profit on day i if we HOLD a stock
 * dp[i][1] = Max profit on day i if we just SOLD a stock
 * dp[i][2] = Max profit on day i if we are RESTING (cooldown/do nothing)
 *
 * State Transition Equations:
 * 1. HOLD:  dp[i][0] = max(dp[i-1][0], dp[i-1][2] - prices[i])
 *    - Either held from yesterday OR bought today (after rest)
 *
 * 2. SOLD:  dp[i][1] = dp[i-1][0] + prices[i]
 *    - Must have held stock yesterday, sell at today's price
 *
 * 3. REST:  dp[i][2] = max(dp[i-1][2], dp[i-1][1])
 *    - Either rested yesterday OR just finished cooldown from sale
 *
 * Why 3 States?
 * - HOLD: Represents actively holding stock
 * - SOLD: Triggers the cooldown (can't buy tomorrow)
 * - REST: Free to make any action (cooldown complete or never started)
 */
public int maxProfit(int[] prices) {
    if (prices == null || prices.length <= 1)
        return 0;

    int n = prices.length;
    int[][] dp = new int[n][3];

    // Base Case: Day 0
    dp[0][0] = -prices[0]; // Bought on day 0
    dp[0][1] = 0;          // Can't sell on day 0
    dp[0][2] = 0;          // Doing nothing

    for (int i = 1; i < n; i++) {
        // HOLD: Either held yesterday OR bought today (after rest)
        dp[i][0] = Math.max(dp[i-1][0], dp[i-1][2] - prices[i]);

        // SOLD: Held yesterday and sell today
        dp[i][1] = dp[i-1][0] + prices[i];

        // REST: Either rested yesterday OR cooldown from yesterday's sale
        dp[i][2] = Math.max(dp[i-1][2], dp[i-1][1]);
    }

    // Max profit when not holding stock on last day
    return Math.max(dp[n-1][1], dp[n-1][2]);
}

// V0-2: Space Optimized (O(1) space) - Interview Favorite
/**
 * Since we only need previous day's state, we can use 3 variables
 * instead of a 2D array.
 *
 * This is the preferred solution for interviews due to O(1) space.
 */
public int maxProfit_optimized(int[] prices) {
    if (prices == null || prices.length == 0)
        return 0;

    int hold = -prices[0]; // Holding a stock
    int sold = 0;          // Just sold (in cooldown trigger)
    int rest = 0;          // Resting (free to act)

    for (int i = 1; i < prices.length; i++) {
        // Save previous sold state (needed for rest calculation)
        int prevSold = sold;

        // State transitions
        sold = hold + prices[i];                 // Sell today
        hold = Math.max(hold, rest - prices[i]); // Hold or buy today
        rest = Math.max(rest, prevSold);         // Rest or finish cooldown
    }

    // Max profit when not holding stock
    return Math.max(sold, rest);
}
```

**Example Walkthrough: prices = [1,2,3,0,2]**

```text
Day | Price | HOLD  | SOLD | REST | Action Taken
----|-------|-------|------|------|-------------
 0  |   1   |  -1   |  0   |  0   | Buy at 1
 1  |   2   |  -1   |  1   |  0   | Sell at 2 (profit = 1)
 2  |   3   |  -1   |  2   |  1   | Sell at 3 (profit = 2)
 3  |   0   |   1   |  2   |  2   | Buy at 0 (after cooldown)
 4  |   2   |   1   |  3   |  2   | Sell at 2 (profit = 3)

Optimal path: Buy@1 → Sell@2 → Cooldown → Buy@0 → Sell@2
Max Profit: 3
```

**State Transition Trace (Day 4):**
```text
Previous State (Day 3):
  hold = 1, sold = 2, rest = 2

Current Price: prices[4] = 2

Calculate New States:
  prevSold = sold = 2  (save before update)

  sold = hold + prices[4] = 1 + 2 = 3  ✅ (sell the stock we bought at 0)
  hold = max(hold, rest - prices[4])
       = max(1, 2 - 2)
       = max(1, 0) = 1  (keep holding, don't buy)
  rest = max(rest, prevSold)
       = max(2, 2) = 2  (stay in rest)

Final Answer: max(sold, rest) = max(3, 2) = 3
```

**Key Differences from Regular Stock Problems:**

| Aspect | Regular Stock (LC 122) | Stock with Cooldown (LC 309) |
|--------|------------------------|------------------------------|
| **States** | 2 (hold, cash) | 3 (hold, sold, rest) |
| **Constraint** | None | Must cooldown after sell |
| **Buy Transition** | `hold = max(hold, cash - price)` | `hold = max(hold, rest - price)` |
| **Why Different?** | Can buy anytime | Can only buy after rest (not immediately after sold) |
| **Space** | O(1) - 2 variables | O(1) - 3 variables |
| **Complexity** | O(n) time | O(n) time |

**Common Mistakes:**
1. ❌ Using 2 states instead of 3 (ignores cooldown)
2. ❌ `hold = max(hold, sold - prices[i])` (can't buy right after selling!)
3. ❌ Forgetting to save `prevSold` before updating (wrong rest calculation)
4. ❌ Returning `max(hold, sold, rest)` (can't end while holding)

**Why This Pattern Works:**
- **SOLD state**: Acts as a "gate" - after entering, you must go through REST
- **REST state**: "Unlocks" the ability to BUY again
- **HOLD state**: Blocks you from RESTING (must sell first)

This creates a forced flow: `HOLD → SOLD → REST → HOLD`, ensuring cooldown compliance.

**Similar Problems:**
- LC 122: Best Time to Buy and Sell Stock II (no cooldown, simpler)
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee (2 states + fee)
- LC 123: Best Time to Buy and Sell Stock III (4 states for 2 transactions)
- LC 188: Best Time to Buy and Sell Stock IV (2k states for k transactions)

### 2-5) N-th Tribonacci Number (LC 1137) — Rolling Three Variables

> T(n) = T(n-1) + T(n-2) + T(n-3); maintain three rolling variables, no array needed.

```java
// java
// LC 1137. N-th Tribonacci Number

// V0
// IDEA: DP (fixed by gpt)
public int tribonacci(int n) {
    if (n == 0)
        return 0;
    if (n == 1 || n == 2)
        return 1;

    // NOTE !!! below, array size is `n + 1`
    int[] dp = new int[n + 1];
    dp[0] = 0;
    dp[1] = 1;
    dp[2] = 1;

    // NOTE !!! below, we loop from i = 3 to `i <= n`
    for (int i = 3; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2] + dp[i - 3];
    }

    return dp[n];
}
```

### 2-6) Decode Ways (LC 91) — Linear DP with One/Two Digit Check

> dp[i] = ways to decode s[0..i]; add dp[i-1] if single digit valid, dp[i-2] if two digits valid.

```java
// java
// LC 91. Decode Ways

/**
 *  Core idea:
 *
 *  - Variation of Climbing Stairs with validity constraints
 *  - At each position i, we try to decode in 2 ways:
 *    1) Single digit (1-9): dp[i] += dp[i-1]
 *    2) Two digits (10-26): dp[i] += dp[i-2]
 *  - '0' cannot be decoded alone — it must pair with '1' or '2'
 *
 *  dp[i] = number of ways to decode the first i characters
 *
 *  Base cases:
 *    dp[0] = 1 (empty string = 1 way)
 *    dp[1] = 1 (if first char != '0')
 */

// Pattern:
public int numDecodings(String s) {
    if (s == null || s.length() == 0 || s.charAt(0) == '0') {
        return 0;
    }

    int n = s.length();
    int[] dp = new int[n + 1];

    // Base cases
    dp[0] = 1;
    dp[1] = 1;

    for (int i = 2; i <= n; i++) {

        /**  NOTE !!!
         *
         *  Check `one-digit` decoding
         *
         */
        // Check one-digit decoding
        int oneDigit = Integer.parseInt(s.substring(i - 1, i));
        if (oneDigit >= 1 && oneDigit <= 9) {
            dp[i] += dp[i - 1];
        }

        /**  NOTE !!!
         *
         *  Check `two-digit` decoding
         *
         */
        // Check two-digit decoding
        int twoDigits = Integer.parseInt(s.substring(i - 2, i));
        if (twoDigits >= 10 && twoDigits <= 26) {
            dp[i] += dp[i - 2];
        }

    }

    return dp[n];
}
```

**Similar LCs:**
- LC 91 Decode Ways
- LC 639 Decode Ways II (with `*` wildcard)
- LC 70 Climbing Stairs (same Fibonacci-like structure)
- LC 509 Fibonacci Number
- LC 1137 N-th Tribonacci Number

### 2-7) Perfect Squares (LC 279) — Unbounded Knapsack (Min Count)

> **Core Idea**: Treat each perfect square (1, 4, 9, 16, ...) as a "coin denomination." Find the minimum number of coins to make amount `n`. This is exactly the **Coin Change** pattern (LC 322).

**Pattern**: Unbounded Knapsack — each square can be used unlimited times, minimize count.

```java
// LC 279 - Perfect Squares (DP approach)
// IDEA: same as Coin Change — squares are coins, n is the target amount
// time = O(N * sqrt(N)), space = O(N)
public int numSquares(int n) {
    int[] dp = new int[n + 1];
    Arrays.fill(dp, n + 1); // max possible is n (all 1s)
    dp[0] = 0;

    for (int i = 1; i <= n; i++) {
        for (int j = 1; j * j <= i; j++) {
            int square = j * j;
            dp[i] = Math.min(dp[i], dp[i - square] + 1);
        }
    }

    return dp[n];
}
```

**Why this works like Coin Change**:
| | Coin Change (LC 322) | Perfect Squares (LC 279) |
|---|---|---|
| **"Coins"** | Given coin denominations | Perfect squares: 1, 4, 9, 16, ... |
| **"Amount"** | Target amount | Target `n` |
| **Goal** | Min coins to reach amount | Min squares to sum to `n` |
| **Recurrence** | `dp[i] = min(dp[i], dp[i - coin] + 1)` | `dp[i] = min(dp[i], dp[i - j*j] + 1)` |
| **Reuse allowed?** | Yes (unbounded) | Yes (unbounded) |

**Note**: No need to sort the squares — loop order doesn't matter for min-count DP (sorting helps greedy/backtracking, not here).

**Similar LeetCode Problems**:
| Problem | LC # | Similarity |
|---------|------|-----------|
| Coin Change | 322 | Identical pattern — min coins for amount |
| Coin Change 2 | 518 | Same coins idea but counting combinations |
| Combination Sum IV | 377 | Same coins idea but counting permutations |
| Climbing Stairs | 70 | Simpler version — steps of 1 or 2 |

### 2-8) Integer Break (LC 343) — Linear DP (Break vs No-Break)

**Pattern**: Linear DP — for each integer `i`, try all split points `j` and decide whether to break `(i-j)` further or not.

**Core DP Idea**:
```text
dp[i] = max product by breaking integer i into at least 2 positive integers

Transition:
  dp[i] = max over all j in [1, i-1] of:
    max(j * (i - j),      // don't break (i-j) further
        j * dp[i - j])    // break (i-j) further using its best product
```

The key insight is the **break vs no-break choice**: when splitting `i` into `j + (i-j)`, the remainder `(i-j)` can either be kept as-is or broken further (using `dp[i-j]`). We must consider both because `dp[i-j]` assumes at least 2 parts, but sometimes using `(i-j)` directly is better (e.g., `dp[2]=1` but the value `2` itself is larger).

**Greedy shortcut**: Break into as many 3s as possible. If remainder is 1, replace `3+1` with `2+2` (since `2×2 > 3×1`).

```java
// DP approach
public int integerBreak(int n) {
    int[] dp = new int[n + 1];
    dp[1] = 1;
    for (int i = 2; i <= n; i++) {
        for (int j = 1; j < i; j++) {
            dp[i] = Math.max(dp[i],
                    Math.max(j * (i - j), j * dp[i - j]));
        }
    }
    return dp[n];
}
```

**DP Table for n=10**:
```text
i:      1  2  3  4  5  6  7  8  9  10
dp[i]:  1  1  2  4  6  9 12 18 27 36
```

**Similar LeetCode Problems**:
| Problem | LC # | Similarity |
|---------|------|-----------|
| Perfect Squares | 279 | Min count to sum to n (unbounded knapsack variant) |
| Coin Change | 322 | Optimize over all ways to decompose n |
| Unique Binary Search Trees | 96 | Try all split points, combine subproblem results |
| Maximum Product Subarray | 152 | Maximize product with DP |
| Partition to K Equal Sum Subsets | 698 | Partition integer into parts with constraint |

### 2-9) Paint Fence (LC 276) — Two-State DP (same / different color)

> Paint `n` posts with `k` colors such that **no more than 2 adjacent posts share a color**. Split each post into two states — "same as previous" vs "different from previous" — and roll them forward.

#### 1. Core Idea

Track two states for the last post `i`:

- `same[i]` = # ways where post `i` has the **same** color as post `i-1`
- `diff[i]` = # ways where post `i` has a **different** color from post `i-1`

The "no more than 2 adjacent same" rule means: **you can only paint the same color if the previous two posts were different** (otherwise you'd create 3-in-a-row). This is exactly why `same[i]` depends on `diff[i-1]`.

```text
same[i] = diff[i - 1]                        # only extend a "different" run, else 3 in a row
diff[i] = (same[i-1] + diff[i-1]) * (k - 1)  # pick any of the other (k-1) colors

Base: same[0] = 0, diff[0] = k   (first post: k choices, "different" by convention)
Answer = same[n-1] + diff[n-1]
```

#### 2. Pattern

**Two-state linear DP with rolling variables** — same family as the stock state-machine problems (LC 309/714), but the states here encode a *local adjacency constraint* rather than buy/sell. Because each state only needs the previous step, collapse the array into two scalars → **O(n) time, O(1) space**.

```python
# python — LC 276 Paint Fence
# time = O(n), space = O(n) (array version, mirrors the state definition)
class Solution:
    def numWays(self, n: int, k: int) -> int:
        if n == 0:
            return 0
        # dp[i][0] = same color as prev, dp[i][1] = different color
        dp = [[0] * 2 for _ in range(n)]
        dp[0][1] = k                       # first post: k ways, treated as "different"
        for i in range(1, n):
            dp[i][0] = dp[i - 1][1]                          # same -> prev must be different
            dp[i][1] = (dp[i - 1][0] + dp[i - 1][1]) * (k - 1)  # different -> any other color
        return sum(dp[-1])
```

```python
# python — O(1) space (rolling two variables)
class Solution:
    def numWays(self, n: int, k: int) -> int:
        if n == 0:
            return 0
        if n == 1:
            return k
        same, diff = k, k * (k - 1)        # base for first 2 posts
        for _ in range(3, n + 1):
            same, diff = diff, (same + diff) * (k - 1)
        return same + diff
```

```java
// java — LC 276 Paint Fence
// time = O(n), space = O(1)
public int numWays(int n, int k) {
    if (n == 0) return 0;
    if (n == 1) return k;
    int same = k, diff = k * (k - 1);      // base for first 2 posts
    for (int i = 3; i <= n; i++) {
        int prevDiff = diff;
        diff = (same + diff) * (k - 1);    // different from previous
        same = prevDiff;                   // same requires previous two differ
    }
    return same + diff;
}
```

**Edge case**: if `n > 2 && k == 1`, the answer is `0` (can't avoid 3-in-a-row with only one color).

#### 3. Similar LeetCode Problems

| Problem | LC # | Similarity |
|---------|------|-----------|
| Climbing Stairs | 70 | Fibonacci-like rolling recurrence, O(1) space |
| House Robber | 198 | Two implicit states (rob / skip) rolled forward |
| Best Time to Buy/Sell with Cooldown | 309 | Multi-state machine with adjacency-style constraint |
| Best Time to Buy/Sell with Fee | 714 | Two rolling states (hold/cash) |
| Delete and Earn | 740 | Take/skip state DP on values |
| Domino and Tromino Tiling | 790 | Count tilings via rolling state transitions |
