# Dynamic Programming (DP)

## Overview
**Dynamic Programming** is an algorithmic paradigm that solves complex problems by breaking them down into simpler subproblems and storing their solutions to avoid redundant computations.

### Key Properties
- **Time Complexity**: Problem-specific, typically O(n²) or O(n³)
- **Space Complexity**: O(n) to O(n²) for memoization table
- **Core Idea**: Trade space for time by memoizing overlapping subproblems
- **When to Use**: Problems with optimal substructure and overlapping subproblems
- **Key Techniques**: Memoization (top-down) and Tabulation (bottom-up)

### Core Characteristics
- **Optimal Substructure**: Optimal solution contains optimal solutions to subproblems
- **Overlapping Subproblems**: Same subproblems solved multiple times
- **Memoization**: Store results to avoid recomputation
- **State Transition**: Define relationship between states

- Core var: `state`, `transition`
- NOTE !!! can put `everything` into `state`

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/dp_state_transition.png"></p>

### References
- [Dynamic Programming Patterns](https://leetcode.com/discuss/general-discussion/458695/dynamic-programming-patterns)
- [DP Tutorial](https://www.geeksforgeeks.org/dynamic-programming/) 

## Problem Categories

### **Category 1: Linear DP**
- **Description**: Single sequence problems with linear dependencies
- **Examples**: LC 70 (Climbing Stairs), LC 198 (House Robber), LC 300 (LIS)
- **Pattern**: dp[i] depends on dp[i-1], dp[i-2], etc.

### **Category 2: Grid/2D DP**
- **Description**: Problems on 2D grids or matrices
- **Examples**: LC 62 (Unique Paths), LC 64 (Minimum Path Sum), LC 221 (Maximal Square)
- **Pattern**: dp[i][j] depends on neighbors

### **Category 3: Interval DP**
- **Description**: Problems on intervals or subarrays
- **Examples**: LC 312 (Burst Balloons), LC 1000 (Minimum Cost to Merge Stones)
- **Pattern**: dp[i][j] for interval [i, j]

### **Category 4: Tree DP**
- **Description**: DP on tree structures
- **Examples**: LC 337 (House Robber III), LC 968 (Binary Tree Cameras)
- **Pattern**: State at each node depends on children

### **Category 5: State Machine DP**
- **Description**: Problems with multiple states and transitions
- **Examples**: LC 714 (Stock with Fee), LC 309 (Stock with Cooldown)
- **Pattern**: Multiple DP arrays for different states

### **Category 6: Knapsack DP**
- **Description**: Selection problems with constraints
- **Examples**: LC 416 (Partition Equal Subset), LC 494 (Target Sum)
- **Pattern**: dp[i][j] for items and capacity/target

### **Category 7: String DP**
- **Description**: String matching, transformation, and subsequence problems
- **Examples**: LC 72 (Edit Distance), LC 1143 (LCS), LC 5 (Longest Palindromic Substring)
- **Pattern**: dp[i][j] for positions in two strings

### **Category 8: State Compression DP**
- **Description**: Use bitmask to represent states, optimize space complexity
- **Examples**: LC 691 (Stickers to Spell Word), LC 847 (Shortest Path Visiting All Nodes)
- **Pattern**: dp[mask] where mask represents visited/selected items

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | State Definition | When to Use |
|---------------|----------|------------------|-------------|
| **1D Linear** | Single sequence | dp[i] = state at position i | Fibonacci-like problems |
| **2D Grid** | Matrix paths | dp[i][j] = state at (i,j) | Path counting, min/max |
| **Interval** | Subarray/substring | dp[i][j] = [i,j] interval | Palindrome, partition |
| **Knapsack** | Selection with limit | dp[i][w] = items & weight | 0/1, unbounded selection |
| **State Machine** | Multiple states | dp[i][state] = at i in state | Buy/sell stocks |

### Universal DP Template
```python
def dp_solution(input_data):
    # Step 1: Define state
    # dp[i] represents...
    
    # Step 2: Initialize base cases
    dp = initialize_dp_array()
    dp[0] = base_value
    
    # Step 3: State transition
    for i in range(1, n):
        # Apply recurrence relation
        dp[i] = f(dp[i-1], dp[i-2], ...)
    
    # Step 4: Return answer
    return dp[n-1]
```

### Template 1: 1D Linear DP
```python
def linear_dp(nums):
    """Classic 1D DP for sequence problems"""
    n = len(nums)
    if n == 0:
        return 0

    # State: dp[i] = optimal value at position i
    dp = [0] * n
    dp[0] = nums[0]

    for i in range(1, n):
        # Transition: current vs previous
        dp[i] = max(dp[i-1], nums[i])
        # Or with skip: dp[i] = max(dp[i-1], dp[i-2] + nums[i])

    return dp[n-1]
```

### 1D DP: Array Sizing and Loop Bounds (`n` vs `n+1`)

**Key Question**: Why do some 1D DP problems loop from `0 to n`, while others loop from `0 to n+1`?

The difference comes down to **what a single index in your DP array represents**. Here are the three main reasons:

#### **1. "Indices" vs "Count" (The Offset)**

This is the most frequent reason for the difference.

- **Loop to `n` (Array size = `n`)**: You are treating the index as a **specific element** in the input array
  - `dp[i]` means "the best result using the i-th element"
  - Example: `dp[3]` represents "result at element index 3"

- **Loop to `n+1` (Array size = `n+1`)**: You are treating the index as a **quantity** or **length**
  - `dp[i]` means "the best result using the first i elements"
  - Example: `dp[3]` represents "result using first 3 elements"

**Example: LC 198 (House Robber)**
```python
# Approach 1: Array size = n (index-based)
def rob_v1(nums):
    n = len(nums)
    if n == 0: return 0
    if n == 1: return nums[0]

    dp = [0] * n
    dp[0] = nums[0]
    dp[1] = max(nums[0], nums[1])

    for i in range(2, n):  # Loop to n
        dp[i] = max(dp[i-1], dp[i-2] + nums[i])

    return dp[n-1]  # Answer at last index

# Approach 2: Array size = n+1 (count-based)
def rob_v2(nums):
    n = len(nums)
    dp = [0] * (n + 1)  # Extra space for "0 houses"
    dp[0] = 0  # Robbing 0 houses = 0
    dp[1] = nums[0] if n > 0 else 0

    for i in range(2, n + 1):  # Loop to n+1
        dp[i] = max(dp[i-1], dp[i-2] + nums[i-1])  # Note: nums[i-1]

    return dp[n]  # Answer at position n
```

#### **2. Physical "Steps" vs "Goals"**

In problems involving movement (stairs, paths), the "goal" is one step **past** the last index.

**Example: LC 70 (Climbing Stairs) / LC 746 (Min Cost Climbing Stairs)**

If `cost = [10, 15, 20]` (indices 0, 1, 2):
- These are the steps you can stand on
- The "Floor" (goal) is at index **3**
- Therefore, `dp` array needs size `n + 1` to include the landing

```python
# LC 746: Min Cost Climbing Stairs
def minCostClimbingStairs(cost):
    n = len(cost)
    dp = [0] * (n + 1)  # Need n+1 for the "top floor"

    # You can start from step 0 or step 1
    dp[0] = 0
    dp[1] = 0

    for i in range(2, n + 1):  # Loop to n+1
        # You can arrive from i-1 or i-2
        dp[i] = min(dp[i-1] + cost[i-1], dp[i-2] + cost[i-2])

    return dp[n]  # The top floor is at position n
```

#### **3. Handling the "Empty" Base Case**

Many DP problems need a base case representing "nothing" (target sum = 0, empty string, etc.).

**Examples**:
- **Knapsack/Coin Change**: Need `dp[target + 1]` because `dp[0]` represents sum = 0
- **Longest Common Subsequence**: Use `(n+1) x (m+1)` matrix where first row/column = empty string

```python
# LC 322: Coin Change
def coinChange(coins, amount):
    dp = [float('inf')] * (amount + 1)  # Need amount+1
    dp[0] = 0  # Base case: 0 coins needed for amount 0

    for i in range(1, amount + 1):  # Loop to amount+1
        for coin in coins:
            if i >= coin:
                dp[i] = min(dp[i], dp[i - coin] + 1)

    return dp[amount] if dp[amount] != float('inf') else -1
```

#### **Comparison Summary**

| Feature | Loop `0` to `n` | Loop `0` to `n+1` |
|---------|-----------------|-------------------|
| **Array Size** | `new int[n]` | `new int[n + 1]` |
| **`dp[i]` meaning** | Result at element `i` | Result considering first `i` items |
| **Typical Base Case** | `dp[0]` and `dp[1]` | `dp[0]` is the "empty" state |
| **Access Pattern** | `dp[i]` ↔ `nums[i]` | `dp[i]` ↔ `nums[i-1]` |
| **Final Answer** | `dp[n - 1]` | `dp[n]` |
| **Use Case** | Direct element mapping | Count/quantity problems, "goal" beyond array |

#### **💡 Pro Tips**

1. **Struggling with off-by-one errors?** Try the `n+1` approach
   - It allows index `i` to represent the i-th item
   - Keeps `dp[0]` as a "safe" dummy value for base case
   - Cleaner alignment between problem size and array index

2. **When to use which?**
   - Use `n+1` when: Problem describes "first i items", "i steps", or needs "empty" base case
   - Use `n` when: Direct element-to-index mapping makes more sense

3. **Rewriting between styles**:
   - `n` → `n+1`: Add 1 to array size, shift base cases, adjust `nums[i]` to `nums[i-1]`
   - `n+1` → `n`: Remove dummy index, handle base cases explicitly, use direct indexing

#### **Side-by-Side Example: LC 70 (Climbing Stairs)**

```python
# Style 1: Array size = n+1 (RECOMMENDED for this problem)
def climbStairs_v1(n):
    if n <= 2:
        return n

    dp = [0] * (n + 1)
    dp[1] = 1
    dp[2] = 2

    for i in range(3, n + 1):
        dp[i] = dp[i-1] + dp[i-2]

    return dp[n]

# Style 2: Array size = n (requires careful handling)
def climbStairs_v2(n):
    if n <= 2:
        return n

    dp = [0] * n
    dp[0] = 1  # 1 way to reach step 1
    dp[1] = 2  # 2 ways to reach step 2

    for i in range(2, n):
        dp[i] = dp[i-1] + dp[i-2]

    return dp[n-1]
```

**Note**: For Climbing Stairs, the `n+1` style is more intuitive because:
- `dp[i]` naturally represents "number of ways to reach step i"
- Step `n` is the goal, so `dp[n]` is the answer
- Avoids the mental overhead of mapping "step i" to "index i-1"

### Template 2: 2D Grid DP
```python
def grid_dp(grid):
    """2D DP for grid/matrix problems"""
    if not grid or not grid[0]:
        return 0
    
    m, n = len(grid), len(grid[0])
    dp = [[0] * n for _ in range(m)]
    
    # Initialize first row and column
    dp[0][0] = grid[0][0]
    for i in range(1, m):
        dp[i][0] = dp[i-1][0] + grid[i][0]
    for j in range(1, n):
        dp[0][j] = dp[0][j-1] + grid[0][j]
    
    # Fill DP table
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
    
    return dp[m-1][n-1]
```

### Template 3: Interval DP
```python
def interval_dp(arr):
    """DP for interval/subarray problems"""
    n = len(arr)
    # dp[i][j] = optimal value for interval [i, j]
    dp = [[0] * n for _ in range(n)]
    
    # Base case: single elements
    for i in range(n):
        dp[i][i] = arr[i]
    
    # Iterate by interval length
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            # Try all split points
            for k in range(i, j):
                dp[i][j] = max(dp[i][j], 
                              dp[i][k] + dp[k+1][j] + cost(i, j))
    
    return dp[0][n-1]
```

### Template 4: 0/1 Knapsack
```python
def knapsack_01(weights, values, capacity):
    """0/1 Knapsack problem"""
    n = len(weights)
    # dp[i][w] = max value with first i items, capacity w
    dp = [[0] * (capacity + 1) for _ in range(n + 1)]
    
    for i in range(1, n + 1):
        for w in range(capacity + 1):
            # Don't take item i-1
            dp[i][w] = dp[i-1][w]
            # Take item i-1 if possible
            if weights[i-1] <= w:
                dp[i][w] = max(dp[i][w], 
                              dp[i-1][w-weights[i-1]] + values[i-1])
    
    return dp[n][capacity]
```

### Template 5: State Machine DP
```python
def state_machine_dp(prices, fee=0):
    """DP with multiple states (stock problems)"""
    if not prices:
        return 0
    
    n = len(prices)
    # States: hold stock, not hold stock
    hold = -prices[0]
    cash = 0
    
    for i in range(1, n):
        # Transition between states
        prev_hold = hold
        hold = max(hold, cash - prices[i])  # Buy
        cash = max(cash, prev_hold + prices[i] - fee)  # Sell
    
    return cash
```

### Template 6: Top-Down Memoization
```python
def top_down_dp(nums):
    """Top-down DP with memoization"""
    memo = {}

    def dp(i):
        # Base case
        if i < 0:
            return 0
        if i == 0:
            return nums[0]

        # Check memo
        if i in memo:
            return memo[i]

        # Recurrence relation
        result = max(dp(i-1), dp(i-2) + nums[i])
        memo[i] = result
        return result

    return dp(len(nums) - 1)
```

### Template 7: String DP (Edit Distance)
```python
def string_dp(s1, s2):
    """String DP for edit distance problems"""
    m, n = len(s1), len(s2)
    # dp[i][j] = min operations to convert s1[:i] to s2[:j]
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    # Initialize base cases
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i-1] == s2[j-1]:
                dp[i][j] = dp[i-1][j-1]  # No operation needed
            else:
                dp[i][j] = 1 + min(
                    dp[i-1][j],    # Delete
                    dp[i][j-1],    # Insert
                    dp[i-1][j-1]   # Replace
                )

    return dp[m][n]
```

### Template 8: Longest Common Subsequence (LCS)
```python
def lcs_dp(text1, text2):
    """LCS pattern for string matching"""
    m, n = len(text1), len(text2)
    # dp[i][j] = LCS length of text1[:i] and text2[:j]
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])

    return dp[m][n]
```

### Template 9: State Compression (Bitmask DP)
```python
def state_compression_dp(graph):
    """Traveling Salesman Problem using bitmask DP"""
    n = len(graph)
    # dp[mask][i] = min cost to visit all cities in mask, ending at city i
    dp = [[float('inf')] * n for _ in range(1 << n)]
    dp[1][0] = 0  # Start at city 0

    for mask in range(1 << n):
        for u in range(n):
            if not (mask & (1 << u)):
                continue

            for v in range(n):
                if mask & (1 << v):
                    continue

                new_mask = mask | (1 << v)
                dp[new_mask][v] = min(dp[new_mask][v],
                                    dp[mask][u] + graph[u][v])

    # Return to starting city
    return min(dp[(1 << n) - 1][i] + graph[i][0] for i in range(1, n))
```

### Template 10: Palindrome DP
```python
def palindrome_dp(s):
    """Check palindromic substrings"""
    n = len(s)
    # dp[i][j] = True if s[i:j+1] is palindrome
    dp = [[False] * n for _ in range(n)]

    # Single characters are palindromes
    for i in range(n):
        dp[i][i] = True

    # Check 2-character palindromes
    for i in range(n - 1):
        if s[i] == s[i + 1]:
            dp[i][i + 1] = True

    # Check longer palindromes
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            if s[i] == s[j] and dp[i + 1][j - 1]:
                dp[i][j] = True

    return dp
```

**Key Palindrome DP Pattern:**
```
Core Transition Equation:
    dp[i][j] = true
       if:
           s[i] == s[j] AND
           (j - i <= 2 OR dp[i + 1][j - 1])

Explanation:
    - dp[i][j] represents whether substring s[i...j] is a palindrome
    - s[i] == s[j]: Characters at both ends must match
    - j - i <= 2: Handles base cases (length 1, 2, or 3 substrings)
    - dp[i + 1][j - 1]: Inner substring must also be a palindrome

Example: For "babad"
    - dp[0][2] = true because s[0]='b' == s[2]='b' AND j-i=2 (length 3)
    - dp[1][3] = true because s[1]='a' == s[3]='a' AND dp[2][2]=true
```

### Template 11: Fibonacci-like Patterns
```python
def fibonacci_variants():
    """Common Fibonacci-like patterns"""

    # 1. Classic Fibonacci
    def fibonacci(n):
        if n <= 1:
            return n
        prev2, prev1 = 0, 1
        for _ in range(2, n + 1):
            current = prev1 + prev2
            prev2, prev1 = prev1, current
        return prev1

    # 2. Climbing Stairs
    def climbStairs(n):
        if n <= 2:
            return n
        prev2, prev1 = 1, 2
        for _ in range(3, n + 1):
            current = prev1 + prev2
            prev2, prev1 = prev1, current
        return prev1

    # 3. House Robber
    def rob(nums):
        if not nums:
            return 0
        if len(nums) <= 2:
            return max(nums)

        prev2, prev1 = nums[0], max(nums[0], nums[1])
        for i in range(2, len(nums)):
            current = max(prev1, prev2 + nums[i])
            prev2, prev1 = prev1, current
        return prev1
```

## Comprehensive Pattern Analysis

### **1D DP Patterns**

| Problem Type | Recurrence | Example | Time | Space |
|--------------|------------|---------|------|-------|
| **Fibonacci** | dp[i] = dp[i-1] + dp[i-2] | LC 70 Climbing Stairs | O(n) | O(1) |
| **House Robber** | dp[i] = max(dp[i-1], dp[i-2] + nums[i]) | LC 198 House Robber | O(n) | O(1) |
| **Decode Ways** | dp[i] = dp[i-1] + dp[i-2] (if valid) | LC 91 Decode Ways | O(n) | O(1) |
| **Word Break** | dp[i] = OR(dp[j] AND s[j:i] in dict) | LC 139 Word Break | O(n²) | O(n) |

**Template for 1D Linear DP**:
```python
def linear_dp_optimized(nums):
    """Space-optimized 1D DP"""
    n = len(nums)
    if n == 0:
        return 0
    if n == 1:
        return nums[0]

    # Only need previous two states
    prev2 = nums[0]
    prev1 = max(nums[0], nums[1])

    for i in range(2, n):
        current = max(prev1, prev2 + nums[i])
        prev2, prev1 = prev1, current

    return prev1
```

### **2D DP Patterns**

| Problem Type | Recurrence | Example | Time | Space |
|--------------|------------|---------|------|-------|
| **Unique Paths** | dp[i][j] = dp[i-1][j] + dp[i][j-1] | LC 62 Unique Paths | O(m×n) | O(n) |
| **Min Path Sum** | dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j] | LC 64 Min Path Sum | O(m×n) | O(n) |
| **LCS** | dp[i][j] = dp[i-1][j-1] + 1 if match else max(...) | LC 1143 LCS | O(m×n) | O(n) |
| **Edit Distance** | dp[i][j] = min(insert, delete, replace) | LC 72 Edit Distance | O(m×n) | O(n) |

**Template for 2D DP with Space Optimization**:
```python
def grid_dp_optimized(grid):
    """Space-optimized 2D DP"""
    if not grid or not grid[0]:
        return 0

    m, n = len(grid), len(grid[0])
    # Only need previous row
    prev = [0] * n
    prev[0] = grid[0][0]

    # Initialize first row
    for j in range(1, n):
        prev[j] = prev[j-1] + grid[0][j]

    # Process remaining rows
    for i in range(1, m):
        curr = [0] * n
        curr[0] = prev[0] + grid[i][0]

        for j in range(1, n):
            curr[j] = min(prev[j], curr[j-1]) + grid[i][j]

        prev = curr

    return prev[n-1]
```

### **Knapsack Patterns**

| Variant | State Definition | Transition | Example |
|---------|------------------|------------|---------|
| **0/1 Knapsack** | dp[i][w] = max value with i items, weight w | dp[i][w] = max(dp[i-1][w], dp[i-1][w-weight[i]] + value[i]) | LC 416 Partition |
| **Unbounded** | dp[w] = max value with weight w | dp[w] = max(dp[w], dp[w-weight[i]] + value[i]) | LC 322 Coin Change |
| **Multiple** | dp[i][w] with count constraint | Similar to 0/1 but with quantity limits | LC 1449 Form Largest Number |

**Space-Optimized Knapsack**:
```python
def knapsack_optimized(weights, values, capacity):
    """1D array knapsack"""
    dp = [0] * (capacity + 1)

    for i in range(len(weights)):
        # Iterate backwards to avoid using updated values
        for w in range(capacity, weights[i] - 1, -1):
            dp[w] = max(dp[w], dp[w - weights[i]] + values[i])

    return dp[capacity]
```

### **Critical Pattern: Loop Order in Unbounded Knapsack (Combinations vs Permutations)**

**🔑 Key Insight**: In unbounded knapsack problems (like Coin Change), the **order of nested loops** determines whether you count **combinations** or **permutations**.

#### **Pattern 1: Combinations (Outer: Coins, Inner: Amount)**
```java
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
```
After coin 1: dp = [1, 1, 1, 1]  // {}, {1}, {1,1}, {1,1,1}
After coin 2: dp = [1, 1, 2, 2]  // + {2}, {1,2}
Result: 2 combinations → {1,1,1}, {1,2}
```

#### **Pattern 2: Permutations (Outer: Amount, Inner: Coins)**
```java
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
```
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
```
Input: amount = 5, coins = [1,2,5]
Output: 4
Combinations: {5}, {2,2,1}, {2,1,1,1}, {1,1,1,1,1}

Input: amount = 3, coins = [2]
Output: 0
Explanation: Cannot make 3 with only coins of 2
```

### **String DP Patterns**

| Problem Type | Pattern | Complexity | Notes |
|--------------|---------|------------|-------|
| **Edit Distance** | dp[i][j] = operations to transform s1[:i] to s2[:j] | O(m×n) | Insert/Delete/Replace |
| **LCS/LIS** | dp[i][j] = length of common subsequence | O(m×n) | Can optimize LIS to O(n log n) |
| **Palindrome** | dp[i][j] = is s[i:j+1] palindrome | O(n²) | Expand around centers |
| **Word Break** | dp[i] = can break s[:i] | O(n³) | Check all possible breaks |

### **State Compression Patterns**

**When to Use Bitmask DP**:
- Small state space (≤ 20 items)
- Need to track which items are selected/visited
- Permutation/combination problems
- Traveling salesman variants

**Common Bitmask Operations**:
```python
# Check if i-th bit is set
if mask & (1 << i):
    pass

# Set i-th bit
new_mask = mask | (1 << i)

# Unset i-th bit
new_mask = mask & ~(1 << i)

# Iterate through all submasks
submask = mask
while submask:
    # Process submask
    submask = (submask - 1) & mask
```

### **Advanced DP Patterns**

#### **Interval DP Template**:
```python
def interval_dp(arr):
    """Matrix chain multiplication style"""
    n = len(arr)
    dp = [[0] * n for _ in range(n)]

    # Length of interval
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = float('inf')

            # Try all possible split points
            for k in range(i, j):
                cost = dp[i][k] + dp[k+1][j] + arr[i] * arr[k+1] * arr[j+1]
                dp[i][j] = min(dp[i][j], cost)

    return dp[0][n-2] if n > 1 else 0
```

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

### **Knapsack Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Partition Equal Subset Sum | 416 | 0/1 Knapsack | Medium |
| Target Sum | 494 | Sum to target | Medium |
| Last Stone Weight II | 1049 | Min difference | Medium |
| Ones and Zeroes | 474 | 2D Knapsack | Medium |
| Coin Change 2 | 518 | Unbounded (Coin→Amount for combinations) | Medium |

### **State Machine Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Best Time to Buy and Sell Stock II | 122 | Multiple transactions | Easy |
| Stock with Cooldown | 309 | State transitions | Medium |
| Stock with Transaction Fee | 714 | Fee consideration | Medium |
| Stock III | 123 | At most 2 transactions | Hard |
| Stock IV | 188 | At most k transactions | Hard |

## LC Examples

### 2-1) Unique Paths

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


### 2-2) Maximum Product Subarray
```python
# NOTE : there is also brute force approach
# V0
# IDEA : brute force + product
class Solution(object):
    def maxProduct(self, A):
        global_max, local_max, local_min = float("-inf"), 1, 1
        for x in A:
            local_max = max(1, local_max)
            if x > 0:
                local_max, local_min = local_max * x, local_min * x
            else:
                local_max, local_min = local_min * x, local_max * x
            global_max = max(global_max, local_max)
        return global_max

# V1
# IDEA : BRUTE FORCE (TLE)
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
# IDEA : DP
# https://leetcode.com/problems/maximum-product-subarray/solution/
# LC 152
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

### 2-3) Best Time to Buy and Sell Stock with Transaction Fee

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

### 2-3) N-th Tribonacci Number

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

## Decision Framework

### Pattern Selection Strategy

```
DP Problem Identification Flowchart:

1. Can the problem be broken into subproblems?
   ├── NO → Not a DP problem
   └── YES → Continue to 2

2. Do subproblems overlap?
   ├── NO → Use Divide & Conquer
   └── YES → Continue to 3

3. Does it have optimal substructure?
   ├── NO → Not a DP problem
   └── YES → Use DP, continue to 4

4. What type of DP pattern?
   ├── Single sequence → Linear DP (Template 1)
   ├── 2D grid/matrix → Grid DP (Template 2)
   ├── Interval/substring → Interval DP (Template 3)
   ├── Selection with limit → Knapsack (Template 4)
   └── Multiple states → State Machine (Template 5)

5. Implementation approach?
   ├── Recursive structure clear → Top-down memoization
   └── Iterative structure clear → Bottom-up tabulation
```

### When to Use DP vs Other Approaches

| Problem Type | Use DP | Use Alternative | Alternative |
|-------------|--------|-----------------|-------------|
| Optimization (min/max) | ✅ | Sometimes | Greedy if optimal |
| Count ways/paths | ✅ | - | - |
| Decision (yes/no) | ✅ | Sometimes | Greedy/DFS |
| All solutions needed | ❌ | ✅ | Backtracking |
| No overlapping subproblems | ❌ | ✅ | Divide & Conquer |
| Greedy choice property | ❌ | ✅ | Greedy |

## Summary & Quick Reference

### Complexity Quick Reference
| Pattern | Time Complexity | Space Complexity | Space Optimization |
|---------|-----------------|------------------|-------------------|
| 1D Linear | O(n) | O(n) | O(1) with variables |
| 2D Grid | O(m×n) | O(m×n) | O(n) with rolling array |
| Interval | O(n³) typical | O(n²) | Usually not possible |
| 0/1 Knapsack | O(n×W) | O(n×W) | O(W) with 1D array |
| State Machine | O(n×k) | O(k) | Already optimized |

### State Definition Guidelines
```python
# 1D: Position/index based
dp[i] = "optimal value considering first i elements"

# 2D: Two dimensions
dp[i][j] = "optimal value for subproblem (i, j)"

# Interval: Range based  
dp[i][j] = "optimal value for interval [i, j]"

# Boolean: Decision problems
dp[i] = "whether target i is achievable"
```

### Common Recurrence Relations

#### **Sum/Count Patterns**
```python
# Fibonacci-like
dp[i] = dp[i-1] + dp[i-2]

# Include/exclude current
dp[i] = dp[i-1] + (dp[i-2] + nums[i])
```

#### **Min/Max Patterns**
```python
# Take or skip
dp[i] = max(dp[i-1], dp[i-2] + nums[i])

# Best from all previous
dp[i] = max(dp[j] + score(j, i) for j < i)
```

#### **Grid Patterns**
```python
# Path counting
dp[i][j] = dp[i-1][j] + dp[i][j-1]

# Min path
dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
```

### Problem-Solving Steps
1. **Identify if DP applicable**: Check for overlapping subproblems
2. **Define state**: What does dp[i] represent?
3. **Find recurrence**: How do states relate?
4. **Identify base cases**: Initial values
5. **Determine iteration order**: Bottom-up direction
6. **Optimize space**: Can we use rolling array?

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Wrong state definition
- Missing base cases
- Incorrect iteration order
- Not handling edge cases
- Integer overflow in large problems

**✅ Best Practices:**
- Start with recursive solution, then optimize
- Draw small examples to find patterns
- Check array bounds carefully
- Consider space optimization after correctness
- Use meaningful variable names for states

### Space Optimization Techniques

#### **Rolling Array**
```python
# From O(n²) to O(n)
# Instead of dp[i][j], use dp[2][j]
curr = [0] * n
prev = [0] * n
for i in range(m):
    curr, prev = prev, curr
    # Update curr based on prev
```

#### **State Compression**
```python
# From O(n) to O(1) for Fibonacci-like
prev2, prev1 = 0, 1
for i in range(2, n):
    curr = prev1 + prev2
    prev2, prev1 = prev1, curr
```

### Interview Tips
1. **Start simple**: Write recursive solution first
2. **Identify subproblems**: Draw recursion tree
3. **Add memoization**: Convert to top-down DP
4. **Consider bottom-up**: Often more efficient
5. **Optimize space**: Impress with rolling array
6. **Test with examples**: Trace through small inputs

### Related Topics
- **Greedy**: When local optimal leads to global
- **Backtracking**: When need all solutions
- **Divide & Conquer**: No overlapping subproblems
- **Graph Algorithms**: DP on graphs (shortest path)
- **Binary Search**: Optimization problems with monotonicity