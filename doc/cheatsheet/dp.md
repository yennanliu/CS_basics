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

### Step

Step 1. define `dp def`
Step 2. define `dp eq`
Step 3. check boundaru condition, req, edge case
Step 4. get the result

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
- **Examples**: LC 714 (Stock with Fee), LC 309 (Stock with Cooldown), LC 122 (Stock II)
- **Pattern**: Multiple DP arrays for different states
- **Key Characteristic**: State transitions depend on previous state + action constraints

**Sub-patterns:**
1. **2-State Machine** (Buy/Sell without cooldown)
   - States: `hold`, `cash`
   - Example: LC 122 (unlimited transactions)

2. **3-State Machine** (Buy/Sell with cooldown) ⭐
   - States: `hold`, `sold`, `rest`
   - Example: LC 309 (cooldown after sell)
   - Key: `rest` state prevents immediate buy after sell

3. **Multi-State Machine** (Limited transactions)
   - States: `buy1`, `sell1`, `buy2`, `sell2`, ...
   - Example: LC 123 (at most 2 transactions), LC 188 (at most k transactions)

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

### Template 5-2: State Machine DP with Cooldown (LC 309 Pattern)
```python
def state_machine_with_cooldown(prices):
    """
    DP with 3 states for stock with cooldown

    State Definition:
    - hold: Currently holding a stock
    - sold: Just sold a stock today (enters cooldown)
    - rest: In cooldown or doing nothing (not holding stock)

    Key Constraint: After selling, must cooldown for 1 day before buying again
    """
    if not prices:
        return 0

    # Initialize states
    hold = -prices[0]  # Buy on day 0
    sold = 0           # Can't sell on day 0
    rest = 0           # Doing nothing

    for i in range(1, len(prices)):
        prev_sold = sold

        # State transitions
        # 1. SOLD: Sell today (must have held yesterday)
        sold = hold + prices[i]

        # 2. HOLD: Either continue holding OR buy today (after cooldown)
        hold = max(hold, rest - prices[i])

        # 3. REST: Either rest again OR just finished cooldown from yesterday's sale
        rest = max(rest, prev_sold)

    # Max profit when not holding stock
    return max(sold, rest)
```

**State Transition Diagram for LC 309:**
```
    ┌─────────────────────────────────────────┐
    │         State Machine Flow              │
    └─────────────────────────────────────────┘

         buy            sell          cooldown
    REST ────→ HOLD ────→ SOLD ─────→ REST
     ↑                                   │
     └───────────────────────────────────┘

    Transitions:
    • REST → HOLD: Buy stock (rest - price)
    • HOLD → SOLD: Sell stock (hold + price)
    • SOLD → REST: Cooldown (no transaction)
    • REST → REST: Do nothing (rest)
    • HOLD → HOLD: Keep holding (hold)
```

**Key Insights:**
- **3 States vs 2 States**: Unlike simple stock problems (buy/sell), this needs 3 states due to cooldown
- **Cooldown Enforcement**: `rest` state ensures you can't buy immediately after selling
- **Space Optimization**: Can use O(1) space with 3 variables instead of 2D array
- **Critical Transition**: `hold = max(hold, rest - prices[i])` - can only buy after rest, not after sold

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

### Template 7: String DP (Edit Distance / Levenshtein Distance)

**Problem**: LC 72 - Edit Distance
Given two strings word1 and word2, find the minimum number of operations required to convert word1 to word2.
Operations allowed: Insert, Delete, Replace (each counts as 1 step)

**Core Pattern**: Two-String Grid DP with three transition choices

**State Definition**:
- `dp[i][j]` = minimum operations to convert `word1[0...i-1]` to `word2[0...j-1]`

**Base Cases**:
- `dp[i][0] = i` (delete all i characters from word1)
- `dp[0][j] = j` (insert all j characters to reach word2)

**Transition**:
- If `word1[i-1] == word2[j-1]`: `dp[i][j] = dp[i-1][j-1]` (no operation needed)
- Else: `dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])`
  - `dp[i-1][j] + 1`: **Delete** from word1
  - `dp[i][j-1] + 1`: **Insert** into word1
  - `dp[i-1][j-1] + 1`: **Replace** in word1

**Python Implementation**:
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

**Java Implementation** (Alternative indexing style):
```java
// LC 72: Edit Distance
public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();

    int[][] dp = new int[m + 1][n + 1];

    // Base cases: converting empty string
    for(int i = 0; i <= m; i++) {
        dp[i][0] = i;  // Delete all characters
    }

    for(int i = 0; i <= n; i++) {
        dp[0][i] = i;  // Insert all characters
    }

    // Fill DP table
    for(int i = 0; i < m; i++) {
        for(int j = 0; j < n; j++) {
            if(word1.charAt(i) == word2.charAt(j)) {
                // Characters match - no operation needed
                dp[i + 1][j + 1] = dp[i][j];
            } else {
                // Try all three operations and take minimum
                int replace = dp[i][j];      // Replace word1[i] with word2[j]
                int delete = dp[i][j + 1];   // Delete word1[i]
                int insert = dp[i + 1][j];   // Insert word2[j]

                dp[i + 1][j + 1] = Math.min(replace, Math.min(delete, insert)) + 1;
            }
        }
    }

    return dp[m][n];
}
```

**Key Insights**:
1. **Indexing Styles**: Two common approaches:
   - Style 1 (Python above): Loop `i` from 1 to m, access `s1[i-1]`, store in `dp[i][j]`
   - Style 2 (Java above): Loop `i` from 0 to m-1, access `word1[i]`, store in `dp[i+1][j+1]`

2. **The Three Operations**:
   ```
   dp[i-1][j]   dp[i-1][j-1]      dp[i-1][j]   dp[i-1][j-1]
                                ↓ (delete)     ↘ (replace)
   dp[i][j-1]   dp[i][j]    =>   dp[i][j-1] → dp[i][j]
                                   (insert)
   ```

3. **Complexity**: Time O(m×n), Space O(m×n) (optimizable to O(n))

**Example Trace**: `word1 = "horse"`, `word2 = "ros"`
```
    ""  r   o   s
""   0   1   2   3
h    1   1   2   3
o    2   2   1   2
r    3   2   2   2
s    4   3   3   2
e    5   4   4   3

Result: 3 operations (delete 'h', delete 'r', delete 'e')
Or: replace 'h'→'r', remove 'r', remove 'e'
```

**Related Problems**:
- LC 583: Delete Operation for Two Strings (variant: only delete allowed)
- LC 712: Minimum ASCII Delete Sum (variant: minimize ASCII sum)
- LC 1143: Longest Common Subsequence (maximize matches instead of minimize edits)

**File References**:
- Java Implementation: `ref_code/interviews-master/leetcode/string/EditDistance.java`
- See also: Two-String Grid Pattern section below for more context

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

```
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
```
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

```
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

#### **🔥 Side-by-Side Code Comparison**

**LC 518: Coin Change II (Combinations)**
```java
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
```
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
```
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
// LC 518: Combinations
for (int coin : coins)              // ← ITEM OUTER
    for (int i = coin; i <= amount; i++)

// LC 377: Permutations
for (int i = 1; i <= target; i++)   // ← TARGET OUTER
    for (int num : nums)
```

**Both use the EXACT SAME transition: `dp[i] += dp[i - item]`**

---

### **Critical Pattern: Understanding `if (i - coin >= 0)` in Coin Change DP**

**🔑 The Question**: Why use `if (i >= coin)` instead of `if (i == coin)`?

This is a fundamental concept in understanding how Dynamic Programming builds on previously solved **subproblems**.

#### **The Short Answer**
- `i == coin` only checks if a **single coin** matches the amount
- `i >= coin` checks if a coin can be **combined** with a previous sum to reach the amount

#### **The Logic of `i - coin > 0`**

When we calculate `dp[i]`, we aren't just looking for one coin that equals `i`. We are looking for a coin `coin` that, when subtracted from `i`, leaves a remainder that we **already know how to solve**.

- **`i`**: The total amount we are trying to reach right now
- **`coin`**: The value of the coin we just picked up
- **`i - coin`**: The "remainder" or the amount left over

If `i - coin > 0`, it means we still need more coins to reach `i`. But since we are filling the table from `0` to `amount`, we have **already calculated** the best way to make the remainder `i - coin`.

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

### **String DP Patterns**

#### **The "Two-String / Two-Sequence Grid" Pattern** 🧩

This is one of the most important DP patterns for string problems. Once you recognize this pattern, a whole class of problems becomes much easier to solve.

**Core Structure:**
- Create a 2D array `dp[m+1][n+1]` where:
  - **Rows (i)**: Represent the prefix of String A (first i characters)
  - **Columns (j)**: Represent the prefix of String B (first j characters)
  - **Cell `dp[i][j]`**: Stores the answer for those two specific prefixes

**Grid Movements (How to Choose the Move):**

Think of the grid as a game where you move from `(0,0)` to `(m,n)`:

1. **Diagonal Move (`dp[i-1][j-1]`)**: You "use" or "match" a character from **both** strings simultaneously
2. **Vertical Move (`dp[i-1][j]`)**: You "skip" or "delete" a character from String A
3. **Horizontal Move (`dp[i][j-1]`)**: You "skip" or "insert" a character from String B

**Pattern Comparison Table:**

| Problem | Goal | Match Logic (`s1[i-1] == s2[j-1]`) | Mismatch Logic | Key Insight |
|---------|------|-----------------------------------|----------------|-------------|
| **LC 1143: LCS** | Longest common length | `1 + dp[i-1][j-1]` (Diagonal + 1) | `max(dp[i-1][j], dp[i][j-1])` | Take diagonal when match, else max of skip either string |
| **LC 97: Interleaving String** | Can s3 interleave s1+s2? | `dp[i-1][j] \|\| dp[i][j-1]` | `false` | Check if we can form by taking from either string |
| **LC 115: Distinct Subsequences** | Count occurrences | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` | Can either use match or skip s char |
| **LC 72: Edit Distance** | Min edits to match | `dp[i-1][j-1]` (No cost) | `1 + min(top, left, diagonal)` | No operation needed if match, else try all 3 operations |
| **LC 583: Delete Operation** | Min deletions to make equal | `dp[i-1][j-1]` | `1 + min(dp[i-1][j], dp[i][j-1])` | Delete from either string |
| **LC 712: Min ASCII Delete Sum** | Min ASCII sum to make equal | `dp[i-1][j-1]` | `min(dp[i-1][j] + s1[i], dp[i][j-1] + s2[j])` | Track ASCII costs |

**The "Empty String" Base Case Pattern** 💡

This is **THE MOST IMPORTANT** pattern in Two-String DP:

* `dp[0][0]`: State where both strings are empty (usually `0` or `true`)
* First row `dp[0][j]`: String A is empty, only String B has characters
* First column `dp[i][0]`: String B is empty, only String A has characters

**Why `m+1` and `n+1`?**
- The `+1` gives us room for the "empty string" base case
- Without this, transitions like `dp[i-1][j]` would crash on the first character
- `dp[i][j]` represents using the first `i` characters of string 1 and first `j` characters of string 2

**Universal Template:**
```java
public int stringDP(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[][] dp = new int[m + 1][n + 1];

    // Step 1: Initialize base cases (empty string states)
    dp[0][0] = 0; // Both strings empty

    // Initialize first row (s1 is empty)
    for (int j = 1; j <= n; j++) {
        dp[0][j] = initValueForEmptyS1(j);
    }

    // Initialize first column (s2 is empty)
    for (int i = 1; i <= m; i++) {
        dp[i][0] = initValueForEmptyS2(i);
    }

    // Step 2: Fill the DP table
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            // NOTE: Use i-1 and j-1 to access string characters
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                // Characters match
                dp[i][j] = transitionOnMatch(dp, i, j);
            } else {
                // Characters don't match
                dp[i][j] = transitionOnMismatch(dp, i, j);
            }
        }
    }

    return dp[m][n];
}
```

**Space Optimization Secret** ⚡

In every "Two-String" problem, you only ever look at:
- The **current row** (`dp[i][j]`)
- The **row above** (`dp[i-1][j]`)
- The **diagonal** (`dp[i-1][j-1]`)

This means you can **always reduce space from O(m×n) to O(n)** by using:
1. A 1D array for the previous row
2. A variable to store the diagonal value
3. Rolling updates as you process each row

**Example Space-Optimized LCS:**
```java
public int longestCommonSubsequence(String s1, String s2) {
    int m = s1.length(), n = s2.length();
    int[] prev = new int[n + 1];

    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            if (s1.charAt(i-1) == s2.charAt(j-1)) {
                curr[j] = prev[j-1] + 1; // Diagonal
            } else {
                curr[j] = Math.max(prev[j], curr[j-1]); // Top or left
            }
        }
        prev = curr; // Roll forward
    }

    return prev[n];
}
```

**Quick Recognition Checklist** ✅

Use "Two-String Grid" pattern when you see:
- [ ] Two strings/sequences as input
- [ ] Need to compare characters from both strings
- [ ] Answer depends on prefixes (first i chars of s1, first j chars of s2)
- [ ] Keywords: "common", "matching", "transform", "interleaving", "subsequence"

**Common Problems:**
- LC 1143 (LCS) - Find longest common subsequence
- LC 72 (Edit Distance) - Minimum edits to transform
- LC 97 (Interleaving String) - Can s3 be formed by interleaving?
- LC 115 (Distinct Subsequences) - Count occurrences
- LC 583 (Delete Operation) - Min deletions to make equal
- LC 712 (Min ASCII Delete Sum) - Min ASCII cost to make equal
- LC 10 (Regular Expression Matching) - Pattern matching with * and .
- LC 44 (Wildcard Matching) - Pattern matching with * and ?

---

### **Classic String DP Patterns (Detailed)**

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
```
Question asks...                          → Use this pattern
─────────────────────────────────────────────────────────────
"Cooldown after action"                   → 3+ states (LC 309)
"Transaction fee/cost"                    → 2 states with cost
"Limited transactions (k times)"          → 2k states
"Unlimited transactions"                  → 2 states (hold/cash)
```

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

### 2-3-2) Best Time to Buy and Sell Stock with Cooldown (LC 309)

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

```
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
```
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

### 2-3-3) N-th Tribonacci Number

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

### 2-4) Longest Increasing Subsequence (LIS)

```java
// java
// LC 300. Longest Increasing Subsequence

/**  NOTE !!!
 *
 *  1. use 1-D DP
 *  2. Key Insight (Important):
 *
 *     - dp[i] = best LIS ending exactly at index i
 *
 *     - Inner loop checks:
 *          - "Can I extend a smaller LIS ending at j by appending nums[i]?"
 *
 *      - maxLen tracks the global maximum across all endpoints
 *
 */

// V0
// IDEA: 1D DP - O(n²) solution
public int lengthOfLIS(int[] nums) {
    if(nums == null || nums.length < 1) {
        return 0;
    }

    int n = nums.length;
    int[] dp = new int[n];

    // Each element itself is an increasing subsequence of length 1
    for(int i = 0; i < n; i++) {
        dp[i] = 1;
    }

    int res = 1;

    for(int i = 1; i < n; i++) {
        for(int j = 0; j < i; j++) {
            /**
             * NOTE !!!
             *
             *  `nums[i] > nums[j]` condition  !!!
             *
             *  -> ONLY if `right element is bigger than left element`,
             *     new length is calculated and DP array is updated
             *
             *  -> This ensures we're building an INCREASING subsequence
             *
             *  -> We check all previous elements (j < i) to see if we can
             *     extend their subsequences by adding nums[i]
             *
             */
            if(nums[i] > nums[j]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
                res = Math.max(res, dp[i]);
            }
        }
    }

    return res;
}
```

**LIS Pattern Explanation:**

| Aspect | Explanation |
|--------|-------------|
| **State Definition** | `dp[i]` = length of longest increasing subsequence ending at index `i` |
| **Initialization** | `dp[i] = 1` for all i (each element is a subsequence of length 1) |
| **Transition** | `dp[i] = max(dp[i], dp[j] + 1)` if `nums[i] > nums[j]` for all `j < i` |
| **Key Condition** | `nums[i] > nums[j]` ensures we only extend increasing subsequences |
| **Time Complexity** | O(n²) - nested loops through array |
| **Space Complexity** | O(n) - 1D DP array |
| **Result** | `max(dp[i])` for all i - maximum value in DP array |

**Why the condition `nums[i] > nums[j]` is critical:**
- We iterate through all previous elements `j` (where `j < i`)
- We check if current element `nums[i]` can extend the subsequence ending at `j`
- Only when `nums[i] > nums[j]`, we can append `nums[i]` to maintain increasing order
- `dp[j] + 1` represents extending the LIS ending at `j` by adding `nums[i]`

**Example Walkthrough:**
```
Input: nums = [10, 9, 2, 5, 3, 7, 101, 18]

Initial: dp = [1, 1, 1, 1, 1, 1, 1, 1]

i=1, nums[1]=9:  No j where nums[j] < 9, dp[1] = 1
i=2, nums[2]=2:  No j where nums[j] < 2, dp[2] = 1
i=3, nums[3]=5:  nums[2]=2 < 5, dp[3] = dp[2]+1 = 2
i=4, nums[4]=3:  nums[2]=2 < 3, dp[4] = dp[2]+1 = 2
i=5, nums[5]=7:  nums[2]=2,nums[3]=5,nums[4]=3 < 7
                 dp[5] = max(dp[2]+1, dp[3]+1, dp[4]+1) = 3
i=6, nums[6]=101: Can extend from multiple, dp[6] = 4
i=7, nums[7]=18: Can extend from multiple, dp[7] = 4

Result: max(dp) = 4
LIS: [2, 3, 7, 101] or [2, 5, 7, 101] or others
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

### State Machine DP Interview Pattern Recognition

**Quick Decision Tree:**
```
Stock/Transaction Problem?
├─ NO → Check other DP patterns
└─ YES → Continue below

Are there any constraints on transactions?
├─ NO constraints (unlimited) → 2 states (hold/cash) [LC 122]
├─ Cooldown period → 3 states (hold/sold/rest) [LC 309]
├─ Transaction fee → 2 states + fee deduction [LC 714]
├─ Limited k transactions → 2k states [LC 123, LC 188]
└─ Buy only once → Kadane's algorithm [LC 121]
```

**State Machine Pattern Comparison:**

| Constraint Type | States | State Names | Buy Condition | Sell Condition | Example LC |
|----------------|--------|-------------|---------------|----------------|------------|
| None | 2 | hold, cash | `cash - price` | `hold + price` | 122 |
| **Cooldown** | **3** | **hold, sold, rest** | `rest - price` ⚠️ | `hold + price` | **309** |
| Transaction fee | 2 | hold, cash | `cash - price` | `hold + price - fee` | 714 |
| k transactions | 2k | buy1, sell1, ... | Track transaction # | Track transaction # | 123, 188 |

**⚠️ Critical Difference in Cooldown Pattern:**
- Regular: `hold = max(hold, cash - price)` - can buy anytime
- Cooldown: `hold = max(hold, rest - price)` - can only buy after rest!

**Pattern Recognition Cheat Sheet:**

| Problem Says... | Pattern | States | Key Transition |
|----------------|---------|--------|----------------|
| "Cooldown 1 day after sell" | 3-state | hold/sold/rest | Buy from `rest` only |
| "Transaction fee of k" | 2-state | hold/cash | `cash = hold + price - fee` |
| "At most 2 transactions" | 4-state | buy1/sell1/buy2/sell2 | Track transaction count |
| "At most k transactions" | 2k-state | Dynamic | Generalized k transactions |
| "Unlimited transactions" | 2-state | hold/cash | Simple buy/sell |

**Common Interview Follow-ups:**
1. "What if cooldown is k days?" → Need k+2 states
2. "What if both cooldown AND fee?" → 3 states + fee deduction
3. "Space optimize it" → Use variables instead of arrays
4. "Prove correctness" → Show state transitions enforce constraints

### Related Topics
- **Greedy**: When local optimal leads to global
- **Backtracking**: When need all solutions
- **Divide & Conquer**: No overlapping subproblems
- **Graph Algorithms**: DP on graphs (shortest path)
- **Binary Search**: Optimization problems with monotonicity