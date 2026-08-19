# Dynamic Programming (DP)

> **Scope** — The main DP reference — state design, the full pattern catalogue, and worked LC examples with explanation; the five heaviest sub-topics live in their own sheets and are linked from here.
> **See also** — *deep dives split out of this file*: [knapsack.md](./knapsack.md) — 0/1 vs unbounded, subset sum, combinations vs permutations; [dp_string.md](./dp_string.md) — the two-sequence grid family; [dp_bitmask.md](./dp_bitmask.md) — state compression; [dp_digit.md](./dp_digit.md) — counting numbers by digit; [dp_monotonic_stack.md](./dp_monotonic_stack.md) — stack-carried DP values.
> *Neighbouring sheets*: [dp_pattern.md](./dp_pattern.md) — terse template index, one section per classic pattern; [recursion_to_dp.md](./recursion_to_dp.md) — converting a working recursion into DP step by step; [kadane_algorithm.md](./kadane_algorithm.md) — the maximum-subarray family in depth; [stock_trading.md](./stock_trading.md) — the LC 121/122/188/309/714 state machine.

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)

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

<p align="center"><img src="../pic/dp_state_transition.png"></p>

### Step

Step 1. define `dp def`
Step 2. define `dp eq`
Step 3. check boundary condition, req, edge case
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

### **Category 3-2: Game Theory / Minimax DP**
- **Description**: Two-player optimal play on arrays; each player picks from either end
- **Examples**: LC 486 (Predict the Winner), LC 877 (Stone Game), LC 1140 (Stone Game II)
- **Pattern**: dp[i][j] = max relative score difference (current player minus opponent) on subarray nums[i..j]
- **Core idea**: When the current player picks, the opponent then plays optimally on the remaining subarray. Subtracting `dp[sub]` flips perspective — the opponent's best becomes your loss.
- **Recurrence**: `dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])`
- **Base case**: `dp[i][i] = nums[i]` (only one element left, take it)
- **Answer**: `dp[0][n-1] >= 0` means the first player wins or ties

### **Category 4: Tree DP**
- **Description**: DP on tree structures
- **Examples**: LC 337 (House Robber III), LC 968 (Binary Tree Cameras)
- **Pattern**: State at each node depends on children
- **📚 Implementation**: Tree DP problems use DFS traversal for implementation. See **dfs.md Template 6 (Bottom-up DFS)** for the DFS traversal patterns used in tree DP solutions

**Sub-patterns:**
1. **Bottom-Up Tree DP** (standard)
   - Post-order DFS: state at each node computed from children
   - Examples: LC 337 (House Robber III), LC 968 (Binary Tree Cameras)
2. **Re-rooting DP** (two-pass DFS)
   - Compute answer for one root, then shift root to every other node in O(N)
   - Pass 1 (Post-order): compute subtree sizes and base answer for root 0
   - Pass 2 (Pre-order): re-root from parent to child using mathematical formula
   - Examples: LC 834 (Sum of Distances in Tree), LC 2581 (Count Number of Possible Root Nodes)

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

### Template 1: 1D Linear DP — LC 53
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

---

#### **Deep Dive: Coin Change Problems (LC 322 vs LC 518) - DP Array Sizing**

**Key Insight**: For problems involving a **target value** (amount, sum, etc.), the DP array size must be `target + 1` to accommodate all values from `0` to `target` inclusive.

##### **Why `dp[amount + 1]`?**

- `dp[i]` represents the result for **amount `i`**
- To track all amounts from `0` to `amount`, we need indices `0, 1, 2, ..., amount`
- That's `amount + 1` total positions

##### **Concrete Example: `amount = 5`**

```text
We need to represent amounts: 0, 1, 2, 3, 4, 5
                         ↓    ↓   ↓   ↓   ↓   ↓
Array indices needed:    [0]  [1] [2] [3] [4] [5]

Therefore: dp array size = 6 = amount + 1
```

Java Code:
```java
int amount = 5;
int[] dp = new int[amount + 1];  // size = 6, indices 0-5

// Now we can store results for each amount:
dp[0] = ...  // result for amount 0
dp[1] = ...  // result for amount 1
dp[2] = ...  // result for amount 2
dp[3] = ...  // result for amount 3
dp[4] = ...  // result for amount 4
dp[5] = ...  // result for amount 5
```

##### **LC 322 vs LC 518 Comparison**

| Aspect | LC 322: Coin Change | LC 518: Coin Change II |
|--------|---------------------|----------------------|
| **Goal** | Find **minimum coins** needed | Find **number of combinations** |
| **Return Type** | `int` (coin count or -1) | `int` (combination count) |
| **DP Definition** | `dp[i]` = min coins to make amount `i` | `dp[i]` = total combinations to make amount `i` |
| **DP Array Size** | `amount + 1` | `amount + 1` |
| **Base Case** | `dp[0] = 0` (0 coins for 0 amount) | `dp[0] = 1` (1 way: empty set) |
| **Loop Order** | `coin` → `amount` (both directions) | `coin` → `amount` (forward only) |
| **Transition** | `dp[i] = min(dp[i], dp[i - coin] + 1)` | `dp[i] += dp[i - coin]` |
| **Example** | `coins=[1,2,5], amount=5` → `2` (one 5) | `coins=[1,2,5], amount=5` → `4` (four ways) |

##### **Detailed Code Example: LC 518 (Coin Change II)**

```java
public int change(int amount, int[] coins) {
    // dp[i] = total number of COMBINATIONS that make up amount i
    // Index corresponds to the amount value
    
    // Example: if amount = 5
    // We need: dp[0], dp[1], dp[2], dp[3], dp[4], dp[5]
    // Therefore: dp array size = 5 + 1 = 6
    
    int[] dp = new int[amount + 1];  // Size = amount + 1
    
    // Base case: There is exactly 1 way to make amount 0 (empty set)
    dp[0] = 1;
    
    // OUTER LOOP: Iterate through each coin
    // This ensures combinations (not permutations)
    for (int coin : coins) {
        // INNER LOOP: Update dp for all reachable amounts
        for (int i = coin; i <= amount; i++) {
            // Accumulate combinations:
            // Ways to make i = current ways + ways to make (i - coin)
            dp[i] += dp[i - coin];
        }
    }
    
    return dp[amount];  // Answer is at index = amount
}
```

**Trace Example**: `amount = 5, coins = [1, 2, 5]`
```text
Initial:        dp = [1, 0, 0, 0, 0, 0]
After coin 1:   dp = [1, 1, 1, 1, 1, 1]  (all amounts reachable with 1s)
After coin 2:   dp = [1, 1, 2, 2, 3, 3]  (add combinations with 2s)
After coin 5:   dp = [1, 1, 2, 2, 3, 4]  (add combination with 5)

Result: dp[5] = 4 combinations: {5}, {2+2+1}, {2+1+1+1}, {1+1+1+1+1}
```

##### **LC 322 Code Example (for comparison)**

```java
public int coinChange(int[] coins, int amount) {
    // dp[i] = minimum coins needed to make amount i
    // Same sizing: dp array size = amount + 1
    
    int[] dp = new int[amount + 1];
    
    // Initialize all to "infinity" except dp[0]
    Arrays.fill(dp, amount + 1);  // Use amount+1 as infinity
    dp[0] = 0;  // Base case: 0 coins needed for amount 0
    
    // OUTER LOOP: For each amount (can be any order)
    for (int i = 1; i <= amount; i++) {
        // INNER LOOP: Try each coin
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] == amount + 1 ? -1 : dp[amount];
}
```

---

#### **⭐⭐⭐⭐⭐ Loop Order: Combinations vs Permutations**

This is one of the most common interview traps in unbounded knapsack / coin change problems.

**The Rule:**

| Outer Loop | Inner Loop | Counts | Example |
|------------|-----------|--------|---------|
| `for coin in coins` | `for i in range(coin, amount+1)` | **Combinations** (order doesn't matter) | LC 518 |
| `for i in range(1, amount+1)` | `for coin in coins` | **Permutations** (order matters) | LC 377 |

**Why Coins-Outer → Combinations:**

When `coins` is the outer loop, each coin is processed completely before the next coin is introduced. This means `[1, 2]` and `[2, 1]` are never both counted — the coin-1 pass "happened first" globally, so `[2, 1]` has no way to appear as a separate path.

**Why Amount-Outer → Permutations:**

When `amount` is the outer loop, for each target `i` we ask *"what was the last coin placed?"* and try every coin. So reaching `i=3` via last-coin=2 (from `dp[1]` built with coin 1) is a separate path from last-coin=1 (from `dp[2]` built with coin 2). Every ordering is counted.

**Concrete Trace: `coins = [1, 2], amount = 3`**

```text
# Combinations (coins outer):
Initial:           dp = [1, 0, 0, 0]
After coin=1:      dp = [1, 1, 1, 1]   ← all amounts built from 1s only
After coin=2:      dp = [1, 1, 2, 2]   ← can now use 2s on top of 1s

→ dp[3] = 2 : {1,1,1}, {1,2}           ← {2,1} NOT separately counted ✓


# Permutations (amount outer):
Initial:           dp = [1, 0, 0, 0]
i=1: coin=1 → dp[1]+=dp[0]=1          dp = [1, 1, 0, 0]
i=2: coin=1 → dp[2]+=dp[1]=1
     coin=2 → dp[2]+=dp[0]=1          dp = [1, 1, 2, 0]
i=3: coin=1 → dp[3]+=dp[2]=2
     coin=2 → dp[3]+=dp[1]=1          dp = [1, 1, 2, 3]

→ dp[3] = 3 : {1,1,1}, {1,2}, {2,1}   ← {2,1} counted separately ✓
```

**Code Side-by-Side:**

```python
# python — Combinations (LC 518): coins outer
def change(amount, coins):
    dp = [0] * (amount + 1)
    dp[0] = 1
    for coin in coins:                    # coin fixed first → combinations
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    return dp[amount]

# python — Permutations (LC 377): amount outer
def combinationSum4(nums, target):
    dp = [0] * (target + 1)
    dp[0] = 1
    for i in range(1, target + 1):        # amount first → permutations
        for num in nums:
            if i >= num:
                dp[i] += dp[i - num]
    return dp[target]
```

**Key Intuition — mental model:**

```text
Coins outer: "I decide to use coin-1 first, then optionally add coin-2 on top."
             → Sequence is forced: coin-1 always before coin-2 → no duplicates.

Amount outer: "To reach amount i, which coin did I place LAST?"
             → Each last-coin choice is a distinct path → all orderings counted.
```

**What about LC 322 (min coins)?**

LC 322 asks for the *minimum count*, not *how many ways* — so whether you count `[1,2]` and `[2,1]` separately doesn't matter; the minimum stays the same either way. Both loop orders are correct for LC 322.

**LeetCode Problem Map:**

| LC # | Problem | Loop Order | Reason |
|------|---------|-----------|--------|
| **518** | Coin Change II | coins outer | Count combinations |
| **377** | Combination Sum IV | amount outer | Count permutations |
| **322** | Coin Change | either | Minimize — order irrelevant |
| **39** | Combination Sum | backtracking | Combinations with arbitrary coins |
| **40** | Combination Sum II | backtracking | Combinations, each used once |

---

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

### Template 2: 2D Grid DP — LC 62

#### 🎯 Pattern (LC 64 — Minimum Path Sum)

| Aspect | Detail |
|--------|--------|
| **Pattern** | 2D Grid DP — move right/down only |
| **State** | `dp[i][j]` = min cost to reach cell `(i, j)` from `(0, 0)` |
| **Transition** | `dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])` |
| **Base Cases** | First row: prefix sum left→right; First col: prefix sum top→bottom |
| **Answer** | `dp[m-1][n-1]` |
| **Time** | O(m × n) |
| **Space** | O(m × n) standard, O(n) space-optimized |

#### 💡 Core Idea

> At each cell, the minimum cost path must have come from either **above** or **left** (only two options since movement is right/down only). Take the minimum of the two and add the current cell's value.

**Why no `visited` array needed** (unlike LC 1631):
- Movement is one-directional (right/down only) → no cycles, no revisiting
- Each cell is computed exactly once in row-major order
- DP fills naturally from top-left to bottom-right

#### **Approach 1: 2D DP** (Standard)

```java
public int minPathSum(int[][] grid) {
    int m = grid.length;
    int n = grid[0].length;
    int[][] dp = new int[m][n];

    // Base: starting cell
    dp[0][0] = grid[0][0];

    // Base: first column — only one way (from above)
    for (int i = 1; i < m; i++)
        dp[i][0] = dp[i - 1][0] + grid[i][0];

    // Base: first row — only one way (from left)
    for (int j = 1; j < n; j++)
        dp[0][j] = dp[0][j - 1] + grid[0][j];

    // Fill rest: min of coming from above vs left
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);

    return dp[m - 1][n - 1];
}
```

#### **Approach 2: In-place DP** (Modify grid directly — O(1) extra space)

```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;

    // First column prefix sum
    for (int i = 1; i < m; i++)
        grid[i][0] += grid[i - 1][0];

    // First row prefix sum
    for (int j = 1; j < n; j++)
        grid[0][j] += grid[0][j - 1];

    // Fill rest in-place
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);

    return grid[m - 1][n - 1];
}
```

**Trade-off**: Modifies the input grid. Use when space is critical and mutation is acceptable.

#### **Approach 3: Space-Optimized 1D DP** (O(m) extra space)

```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;

    // cur[i] = min cost to reach current column at row i
    int[] cur = new int[m];
    cur[0] = grid[0][0];

    // Initialize first column
    for (int i = 1; i < m; i++)
        cur[i] = cur[i - 1] + grid[i][0];

    // Process column by column
    for (int j = 1; j < n; j++) {
        cur[0] += grid[0][j];  // First row: only from left
        for (int i = 1; i < m; i++)
            cur[i] = Math.min(cur[i - 1], cur[i]) + grid[i][j];
            //                ↑ from above    ↑ current row, previous column
    }

    return cur[m - 1];
}
```

**Key Insight**: `cur[i]` before update = cost of reaching `(i, j-1)` (from left). After update of `cur[i-1]` = cost of reaching `(i-1, j)` (from above). So `min(cur[i-1], cur[i])` is exactly `min(above, left)`.

#### **Approach 4: Top-Down Memoization** (Recursive)

```java
public int minPathSum(int[][] grid) {
    int m = grid.length - 1;
    int n = grid[0].length - 1;
    int[][] dp = new int[m + 1][n + 1];
    for (int[] row : dp)
        Arrays.fill(row, -1);
    return helper(grid, m, n, dp);
}

// helper(m, n) = min path sum from (0,0) to (m,n)
private int helper(int[][] grid, int m, int n, int[][] dp) {
    if (m == 0 && n == 0) return grid[0][0];
    if (m == 0) {
        dp[m][n] = grid[m][n] + helper(grid, m, n - 1, dp);
        return dp[m][n];
    }
    if (n == 0) {
        dp[m][n] = grid[m][n] + helper(grid, m - 1, n, dp);
        return dp[m][n];
    }
    if (dp[m][n] != -1) return dp[m][n];
    // DP equation: min(come from left, come from above) + current cell
    dp[m][n] = grid[m][n] + Math.min(helper(grid, m, n - 1, dp), helper(grid, m - 1, n, dp));
    return dp[m][n];
}
```

**Key Insight**: Recurse from `(m-1, n-1)` down to `(0, 0)`. Base cases handle the first row/column (only one direction possible). Cache with `dp[m][n] != -1` guard.

#### **Approach Comparison**

| Approach | Space | Modifies Input | Notes |
|----------|-------|----------------|-------|
| Top-Down Memo | O(m×n) | No | Natural recursive thinking |
| 2D DP | O(m×n) | No | Clearest iterative; easiest to reason about |
| In-place DP | O(1) | Yes ⚠️ | Best space, but destructive |
| 1D DP (1 row) | O(m) | No | Good balance of space and clarity |

#### **⚠️ LC 64 vs LC 1631: When to Use DP vs Dijkstra**

| | LC 64 (Min Path Sum) | LC 1631 (Min Effort Path) |
|---|---|---|
| **Movement** | Right + Down only | All 4 directions |
| **Cost** | Accumulative sum | Max of step differences |
| **Revisit cells?** | No (one direction) | Yes (better path possible) |
| **Algorithm** | 2D DP | Dijkstra + min-heap |
| **`visited` needed?** | No | Yes |
| **Why DP works** | No cycles, DAG structure | DP fails: can revisit |

**Rule**: If movement is constrained to one direction (right/down) → use **2D DP**. If all 4 directions are allowed → use **Dijkstra** (or BFS with priority).

#### **Similar LeetCode Problems** 📚

| Problem | LC # | Key Difference | Algorithm |
|---------|------|----------------|-----------|
| **Minimum Path Sum** | 64 | Sum along path, right/down only | 2D DP |
| **Unique Paths** | 62 | Count paths (not minimize sum) | 2D DP |
| **Unique Paths II** | 63 | With obstacles | 2D DP (skip obstacles) |
| **Dungeon Game** | 174 | Same grid shape, but solve bottom-right → top-left (need min HP) | 2D DP (reverse direction) |
| **Triangle** | 120 | Triangle shape, top→bottom | 1D DP (bottom-up) |
| **Minimum Falling Path Sum** | 931 | Can move diagonally ±1 | 2D DP |
| **Maximal Square** | 221 | Find largest square of 1s | 2D DP (`min` of 3 neighbors) |
| **Path With Min Effort** | 1631 | 4 directions, max-diff cost | Dijkstra |
| **Shortest Path in Grid with Obstacles** | 1293 | BFS with k obstacle eliminations | BFS + state |

#### **Visual Trace Example**

```text
grid = [[1,3,1],
        [1,5,1],
        [4,2,1]]

After DP:
dp = [[1, 4, 5],
      [2, 7, 6],
      [6, 8, 7]]

Path: 1→3→1→1→1 = 7
```

```python
# Python equivalent
def grid_dp(grid):
    if not grid or not grid[0]:
        return 0
    m, n = len(grid), len(grid[0])
    dp = [[0] * n for _ in range(m)]
    dp[0][0] = grid[0][0]
    for i in range(1, m):
        dp[i][0] = dp[i-1][0] + grid[i][0]
    for j in range(1, n):
        dp[0][j] = dp[0][j-1] + grid[0][j]
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]
    return dp[m-1][n-1]
```

**File Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/MinimumPathSum.java`

### Template 3: Interval DP — LC 516
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

### Template 3-2: Classic Interval DP Pattern (LC 312 Burst Balloons Style)

**🎯 Key Insight**: Think about which element to process **LAST**, not first!

This is the hallmark of interval DP problems like Burst Balloons, Matrix Chain Multiplication, and similar problems where the order of operations matters.

**Core Pattern**:
- **State**: `dp[i][j]` = optimal value for interval `(i, j)` (often exclusive)
- **Transition**: For each element `k` in `(i, j)`, assume `k` is the **last** element processed
- **Why "Last"?** When `k` is last, the subproblems on left and right are independent

**The 3-Level Nested Loop Structure**:
```text
for length in [2, 3, ..., n+1]:        # Build from small to large intervals
    for left in [0, 1, ..., n-length]: # Try all possible left boundaries
        right = left + length           # Calculate right boundary
        for k in [left+1, ..., right-1]: # Try each element as LAST
            # dp[left][right] = combine(dp[left][k], dp[k][right], cost)
```

#### **Pattern 1: Burst Balloons (LC 312) - Exclusive Boundaries**

**Problem**: Burst all balloons to maximize coins. Bursting balloon `i` gives `nums[i-1] * nums[i] * nums[i+1]` coins.

**Key Insight**:
- Add boundaries `[1, ...nums..., 1]` to handle edge cases
- `dp[i][j]` = max coins from bursting balloons **between** `i` and `j` (exclusive)
- When `k` is the **last** balloon burst in `(i, j)`, its neighbors are `i` and `j`

**Why This Works**:
- If we think "which balloon to burst first?", the problem is hard because neighbors change
- If we think "which balloon to burst last?", when we burst `k` last:
  - All balloons in `(i, k)` are already gone → subproblem `dp[i][k]`
  - All balloons in `(k, j)` are already gone → subproblem `dp[k][j]`
  - Only `i`, `k`, `j` remain → coins = `balloons[i] * balloons[k] * balloons[j]`

**Python Implementation**:
```python
def maxCoins(nums):
    """LC 312: Burst Balloons - Classic Interval DP"""
    n = len(nums)

    # Step 1: Add boundary balloons with value 1
    balloons = [1] + nums + [1]

    # Step 2: dp[i][j] = max coins from bursting balloons between i and j (exclusive)
    dp = [[0] * (n + 2) for _ in range(n + 2)]

    # Step 3: Build from small intervals to large
    for length in range(2, n + 2):  # length of interval
        for left in range(n + 2 - length):  # left boundary
            right = left + length  # right boundary

            # Step 4: Try each balloon k as the LAST to burst in (left, right)
            for k in range(left + 1, right):
                # Coins from bursting k last (only left, k, right remain)
                coins = balloons[left] * balloons[k] * balloons[right]
                # Add coins from left and right subproblems
                total = coins + dp[left][k] + dp[k][right]
                dp[left][right] = max(dp[left][right], total)

    # Answer: burst all balloons between boundaries 0 and n+1
    return dp[0][n + 1]
```

**Java Implementation**:
```java
// LC 312: Burst Balloons
public int maxCoins(int[] nums) {
    int n = nums.length;

    // Add boundaries: [1, ...nums..., 1]
    int[] balloons = new int[n + 2];
    balloons[0] = 1;
    balloons[n + 1] = 1;
    for (int i = 0; i < n; i++) {
        balloons[i + 1] = nums[i];
    }

    // dp[i][j] = max coins from bursting balloons between i and j (exclusive)
    int[][] dp = new int[n + 2][n + 2];

    // Iterate over interval lengths (from 2 up to n+1)
    for (int len = 2; len <= n + 1; len++) {
        // i is the left boundary
        for (int i = 0; i <= n + 1 - len; i++) {
            int j = i + len; // j is the right boundary

            // Pick k as the LAST balloon to burst in interval (i, j)
            for (int k = i + 1; k < j; k++) {
                int currentCoins = balloons[i] * balloons[k] * balloons[j];
                int total = currentCoins + dp[i][k] + dp[k][j];
                dp[i][j] = Math.max(dp[i][j], total);
            }
        }
    }

    return dp[0][n + 1];
}
```

**Example Trace**: `nums = [3,1,5,8]`

After adding boundaries: `[1, 3, 1, 5, 8, 1]` (indices 0-5)

```text
Building dp[0][5] (entire interval):
  Try k=1 (value 3) as LAST:
    coins = balloons[0] * balloons[1] * balloons[5] = 1 * 3 * 1 = 3
    total = 3 + dp[0][1] + dp[1][5]

  Try k=2 (value 1) as LAST:
    coins = balloons[0] * balloons[2] * balloons[5] = 1 * 1 * 1 = 1
    total = 1 + dp[0][2] + dp[2][5]

  Try k=3 (value 5) as LAST:
    coins = balloons[0] * balloons[3] * balloons[5] = 1 * 5 * 1 = 5
    total = 5 + dp[0][3] + dp[3][5]

  Try k=4 (value 8) as LAST:
    coins = balloons[0] * balloons[4] * balloons[5] = 1 * 8 * 1 = 8
    total = 8 + dp[0][4] + dp[4][5]

Result: dp[0][5] = 167
```

#### **Pattern 2: Inclusive Boundaries Variant**

Some interval DP problems use **inclusive** boundaries where `dp[i][j]` includes elements `i` and `j`.

**Python Implementation**:
```python
def maxCoins_inclusive(nums):
    """Alternative: dp[i][j] includes balloons i through j"""
    n = len(nums)
    balloons = [1] + nums + [1]
    dp = [[0] * (n + 2) for _ in range(n + 2)]

    # Iterate through window lengths (len) from 1 to n
    for length in range(1, n + 1):
        for left in range(1, n - length + 2):
            right = left + length - 1

            # Try every balloon k in [left, right] as LAST to burst
            for k in range(left, right + 1):
                coins = (dp[left][k - 1] + dp[k + 1][right] +
                        balloons[left - 1] * balloons[k] * balloons[right + 1])
                dp[left][right] = max(dp[left][right], coins)

    return dp[1][n]
```

#### **Top-Down (Memoization) Approach**

```python
def maxCoins_topdown(nums):
    """Top-down with memoization"""
    balloons = [1] + nums + [1]
    memo = {}

    def dp(left, right):
        """Max coins from bursting balloons between left and right (exclusive)"""
        if left + 1 == right:  # No balloons between left and right
            return 0

        if (left, right) in memo:
            return memo[(left, right)]

        max_coins = 0
        # Try each balloon k as the last to burst
        for k in range(left + 1, right):
            coins = (balloons[left] * balloons[k] * balloons[right] +
                    dp(left, k) + dp(k, right))
            max_coins = max(max_coins, coins)

        memo[(left, right)] = max_coins
        return max_coins

    return dp(0, len(balloons) - 1)
```

**Java Top-Down**:
```java
public int maxCoins(int[] nums) {
    int n = nums.length;
    int[] balloons = new int[n + 2];
    balloons[0] = balloons[n + 1] = 1;
    for (int i = 0; i < n; i++) {
        balloons[i + 1] = nums[i];
    }

    int[][] dp = new int[n + 2][n + 2];
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j <= n; j++) {
            dp[i][j] = -1;  // -1 means not computed yet
        }
    }

    return burst(balloons, 0, n + 1, dp);
}

private int burst(int[] balloons, int left, int right, int[][] dp) {
    if (left + 1 == right) return 0;  // No balloons between left and right

    if (dp[left][right] != -1) {
        return dp[left][right];
    }

    int maxCoins = 0;
    for (int k = left + 1; k < right; k++) {
        int coins = balloons[left] * balloons[k] * balloons[right];
        coins += burst(balloons, left, k, dp) + burst(balloons, k, right, dp);
        maxCoins = Math.max(maxCoins, coins);
    }

    dp[left][right] = maxCoins;
    return maxCoins;
}
```

#### **Key Characteristics of This Pattern**

| Aspect | Detail |
|--------|--------|
| **State Definition** | `dp[i][j]` = optimal value for interval `(i, j)` or `[i, j]` |
| **Loop Order** | Length (outer) → Left boundary → Split point `k` |
| **Transition** | Try each `k` as the **last** element processed |
| **Time Complexity** | O(n³) - three nested loops |
| **Space Complexity** | O(n²) - 2D DP table |
| **Key Insight** | Process elements in reverse order of dependency |

#### **Common Problems Using This Pattern**

| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| **Burst Balloons** | 312 | Last balloon to burst | Hard |
| **Matrix Chain Multiplication** | N/A | Last matrix multiply | Classic |
| **Minimum Cost to Merge Stones** | 1000 | Last merge operation | Hard |
| **Remove Boxes** | 546 | Last box to remove | Hard |
| **Palindrome Partitioning II** | 132 | Min cuts (variant) | Hard |
| **Strange Printer** | 664 | Last character to print | Hard |

#### **Pattern Recognition Checklist**

Use this interval DP pattern when:
- ✅ Problem involves processing elements in an array/sequence
- ✅ Order of operations affects the result
- ✅ Subproblems become independent after choosing an operation
- ✅ Optimal solution can be built from optimal subproblems
- ✅ Keywords: "merge", "burst", "remove", "split", "multiply"

#### **Common Mistakes to Avoid**

1. **Thinking "first" instead of "last"**:
   - ❌ "Which balloon to burst first?" → Neighbors change, dependencies unclear
   - ✅ "Which balloon to burst last?" → Subproblems are independent

2. **Wrong boundary handling**:
   - Add explicit boundaries (like `[1, ...nums..., 1]`) to simplify edge cases
   - Decide if boundaries are inclusive or exclusive

3. **Off-by-one errors**:
   - Be consistent: `dp[i][j]` means `(i, j)` exclusive or `[i, j]` inclusive
   - Adjust loop ranges accordingly

4. **Incorrect loop order**:
   - Always build from smaller intervals to larger ones
   - Length must be the outermost loop

#### **Complexity Analysis**

**Time Complexity**: O(n³)
- Outer loop (length): O(n)
- Middle loop (left boundary): O(n)
- Inner loop (split point k): O(n)
- Each cell takes O(n) time to compute

**Space Complexity**: O(n²)
- 2D DP table of size `(n+2) × (n+2)`
- Can be optimized in some cases, but generally requires O(n²)

**Reference**: See `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/BurstBalloons.java` for multiple implementation variants.

### Template 3-3: Backward-i + Forward-j Loop Order (Palindrome/Substring DP)

**🎯 When to Use This Pattern**

Use this when `dp[i][j]` depends on:
- `dp[i+1][j-1]` — the **inner** substring (both boundaries shrink inward)
- `dp[i+1][j]` — row **below** (i increases)
- `dp[i][j-1]` — column **left** (j decreases)

Classic problems: **LC 516 Longest Palindromic Subsequence**, **LC 5 Longest Palindromic Substring**, **LC 647 Palindromic Substrings**.

**Core Insight: Dependency Direction Determines Loop Order**

```text
dp[i][j] depends on:
    dp[i+1][j-1]   ← diagonal (i+1, j-1): both already computed
    dp[i+1][j]     ← row below: need i+1 before i  → loop i BACKWARD
    dp[i][j-1]     ← column left: need j-1 before j → loop j FORWARD
```

So: **loop `i` from `n-1` down to `0`, loop `j` from `i+1` up to `n-1`**.

**Template (Java)**:
```java
int n = s.length();
int[][] dp = new int[n][n];

// Base case: single characters
for (int i = 0; i < n; i++) dp[i][i] = 1;

// i backwards (so dp[i+1][...] is already filled)
for (int i = n - 1; i >= 0; i--) {
    // j forwards (so dp[...][j-1] is already filled)
    for (int j = i + 1; j < n; j++) {
        if (s.charAt(i) == s.charAt(j)) {
            dp[i][j] = dp[i + 1][j - 1] + 2;   // expand palindrome
        } else {
            dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);  // skip i or j
        }
    }
}
return dp[0][n - 1];
```

**Why NOT `length`-based outer loop here?**

The length-based loop (Template 3) also works, but the backward-i + forward-j approach is more intuitive when the transition naturally reads as "expand/shrink boundaries" rather than "try a split point k".

| Approach | Outer Loop | Use When |
|---|---|---|
| Length-based (Template 3) | `length: 2 → n` | Split-point `k` problems (burst balloons, matrix chain) |
| Backward-i + Forward-j (this template) | `i: n-1 → 0` | Boundary expand/shrink problems (palindrome, LCS on same string) |

**Similar LeetCode Problems**:
- **LC 516** — Longest Palindromic Subsequence (exact template above)
- **LC 5** — Longest Palindromic Substring (same loop order, boolean dp)
- **LC 647** — Palindromic Substrings (count all palindromes)
- **LC 1048** — Longest String Chain (DFS+memo or sort-by-length DP; see `LongestStringChain.java`)
- **LC 1312** — Minimum Insertion Steps to Make a String Palindrome
- **LC 730** — Count Different Palindromic Subsequences

---

### Template 4: 0/1 Knapsack ⭐⭐⭐⭐⭐ — LC 416

#### 🎯 Core Idea

**0/1 Knapsack** = each item may be taken **at most once** (0 or 1 time).

> Given a set of items (each with weight and value) and a capacity, find the maximum value you can pack **without exceeding the capacity** and **using each item at most once**.

| Aspect | Detail |
|--------|--------|
| **State** | `dp[w]` = best value achievable with capacity exactly `w` |
| **Transition** | `dp[w] = max(dp[w], dp[w - weight[i]] + value[i])` |
| **Loop order** | Outer: items; Inner: capacity **backward** (`W → weight[i]`) |
| **Why backward** | Prevents using the same item twice in one pass |
| **Time** | O(n × W) |
| **Space** | O(W) with 1-D optimization |

---

#### 💡 Why Must the Inner Loop Go Backward?

This is **the most critical detail** in 0/1 Knapsack.

**Intuition**: in a 1-D DP array, when we process item `i` we need `dp[w - weight[i]]` to still reflect the state **before** item `i` was considered. If we iterate forward, the update to a smaller index `dp[w - weight[i]]` happens earlier in the same pass, so a later index `dp[w]` would pick it up — effectively using item `i` twice.

**Concrete trace — LC 416, `nums = [3]`, `target = 6`:**

```text
# Goal: can we pick some numbers that sum to 6?
# Only num = 3 is available, so the answer should be False.

dp = [True, False, False, False, False, False, False]
        0      1      2      3      4      5      6

❌ Forward iteration (WRONG):
   for s in range(3, 7):       # 3, 4, 5, 6
       dp[s] = dp[s] or dp[s - 3]

   s=3: dp[3] = dp[0] = True   ← 3 used once (ok so far)
   s=6: dp[6] = dp[3] = True   ← dp[3] was just SET by the same num!
                                   This counts 3 twice: 3 + 3 = 6 ❌

✅ Backward iteration (CORRECT):
   for s in range(6, 2, -1):   # 6, 5, 4, 3
       dp[s] = dp[s] or dp[s - 3]

   s=6: dp[6] = dp[3] = False  ← dp[3] is still its OLD value ✓
   s=5: dp[5] = dp[2] = False
   s=4: dp[4] = dp[1] = False
   s=3: dp[3] = dp[0] = True   ← 3 used once ✓

   Result: dp[6] = False  ✓ Correct!
```

**Key invariant**: when computing `dp[s]` in the backward pass, `dp[s - num]` has not been touched yet in this iteration, so it still holds the "pre-item-i" value. This guarantees each item is used **at most once**.

---

#### Pattern: 2-D → 1-D Derivation

```text
2-D (classic):
  dp[i][w] = max value using first i items with capacity w

  dp[i][w] = dp[i-1][w]                              # skip item i
  if w >= weight[i]:
      dp[i][w] = max(dp[i][w], dp[i-1][w-weight[i]] + value[i])   # take item i

1-D (space-optimized):
  Observe that dp[i][...] depends ONLY on dp[i-1][...].
  Collapse to one array, iterate w BACKWARD so dp[w-weight[i]]
  still holds dp[i-1][w-weight[i]] when we need it.

  dp[w] = max(dp[w], dp[w - weight[i]] + value[i])
  (backward w: W → weight[i])
```

---

#### Code Templates

```python
# python — general 0/1 Knapsack (max value)
# time = O(n * W), space = O(W)
def knapsack_01(weights, values, capacity):
    dp = [0] * (capacity + 1)
    for i in range(len(weights)):
        for w in range(capacity, weights[i] - 1, -1):   # backward!
            dp[w] = max(dp[w], dp[w - weights[i]] + values[i])
    return dp[capacity]


# python — LC 416 (boolean variant: can we reach sum target?)
# time = O(n * target), space = O(target)
def canPartition(nums):
    total = sum(nums)
    if total % 2:
        return False
    target = total // 2

    dp = [False] * (target + 1)
    dp[0] = True                             # empty subset sums to 0

    for num in nums:
        for s in range(target, num - 1, -1): # backward!
            dp[s] = dp[s] or dp[s - num]

    return dp[target]
```

```java
// java — general 0/1 Knapsack (max value)
// time = O(n * W), space = O(W)
public int knapsack01(int[] weights, int[] values, int W) {
    int[] dp = new int[W + 1];
    for (int i = 0; i < weights.length; i++) {
        for (int w = W; w >= weights[i]; w--) {   // backward!
            dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        }
    }
    return dp[W];
}

// java — LC 416 (boolean variant)
public boolean canPartition(int[] nums) {
    int total = 0;
    for (int n : nums) total += n;
    if (total % 2 != 0) return false;
    int target = total / 2;

    boolean[] dp = new boolean[target + 1];
    dp[0] = true;

    for (int num : nums) {
        for (int s = target; s >= num; s--) {     // backward!
            dp[s] = dp[s] || dp[s - num];
        }
    }
    return dp[target];
}
```

---

#### When to Use 0/1 Knapsack

| Signal in problem | Why it implies 0/1 Knapsack |
|-------------------|-----------------------------|
| "each element used **at most once**" | Defines the 0/1 constraint |
| "partition array into two subsets" | Reduce to: can subset sum = total/2? |
| "minimize / maximize difference between two groups" | Partition → knapsack |
| "assign + or − to reach target" | LC 494 hidden knapsack |
| "choose items within a budget" | Classic knapsack framing |

**Quick rule**: if items cannot be reused → 0/1 Knapsack with **backward** inner loop.

---

#### 何時使用 0/1 背包 DP？（中文速記）⭐⭐⭐⭐

**三個核心識別信號** —— 題目同時出現下面 3 點，幾乎可以確定是 0/1 背包：

| # | 特徵 | 說明 |
|---|------|------|
| **1** | 元素「**不可重複使用**」（0 或 1 的決策） | 每個物品／數字只有一個，只能 **選 (1)** 或 **不選 (0)**。對比：完全背包（Unbounded）的物品可以無限次重複選（例如無限供應的硬幣） |
| **2** | 存在明確的「**容量 / 目標限制**」 | 題目給了一個上限或目標值（背包最大容量 $W$，或子集目標和 Target），需要在不超過它的前提下做選擇與組合 |
| **3** | 求解目標是「**子集組合**」相關 | ① **最值**：容量內能裝入的最大價值？② **存在性**：能不能選出子集使和剛好等於目標？③ **方案數**：和等於目標的選法有幾種？ |

**實務上的快速判斷** —— 題目裡同時有：

1. 很多物品
2. 每個物品有一個 **cost / weight**
3. 每個物品有一個 **value / reward**
4. 每個物品 **最多只能選一次**
5. 有一個總容量 / 預算上限

→ 很可能就是 **0/1 Knapsack**。

> 例：「有 5 個物品，每個有重量和價值，背包最多裝重量 10，問最大價值是多少？」—— 典型 0/1 Knapsack。

**為什麼叫 0/1？** 每個 item 只有兩種選擇：`0` 不拿、`1` 拿，而且**不能拿第二次**。

##### 最常見的 DP 定義

`dp[j]` = 容量最多為 `j` 時，可以得到的最大 value。對每個 item `(weight, value)`：

```text
for each item:
    for j = W down to weight:          # ※ j 要倒著跑
        dp[j] = max(dp[j], dp[j - weight] + value)
```

**關鍵是 `j` 要倒著跑**。倒序的原因：你不希望同一個 item 在這一輪被重複使用——倒序時 `dp[j - weight]` 讀到的是 **上一輪（還沒用過這個 item）** 的值；正序時 `dp[j - weight]` 已經被本輪更新過，等於允許同一個 item 被選多次。詳細推導見本節上方的 **💡 Why Must the Inner Loop Go Backward?**（含 `nums = [3], target = 6` 的逐步 trace）。

##### 跟其他 Knapsack 的區別

| 類型 | 每個物品可以用幾次 | DP 容量方向 | 代表題 |
|------|------------------:|-------------|--------|
| **0/1 Knapsack** | 最多 1 次 | **由大到小（倒序）** | LC 416, 494, 1049 |
| **Unbounded Knapsack** | 無限次 | **由小到大（正序）** | LC 322, 518 |
| **Bounded Knapsack** | 最多 `k` 次 | 需要額外處理（二進制拆分成多個 0/1 物品，或單調隊列優化） | LC 2585, 1774 |

一個很好用的判斷：

> - **「每個東西只能選一次」→ 想 0/1 Knapsack（倒序）**
> - **「每個東西可以一直選」→ 想 Unbounded Knapsack（正序）**

##### 別被「背包」這個詞騙到

0/1 Knapsack 不一定真的長得像「背包」。像是 **預算分配、選課、專案選擇、投資組合、最多只能做一次的任務選擇**，只要本質是「**選或不選 + 有容量限制**」，都可能套這個 DP。LC 416（分割等和子集）、LC 494（添加 +/- 號）、LC 474（限 0 與 1 的個數）都是被包裝過的 0/1 背包。

---

#### 0/1 Knapsack vs Unbounded Knapsack

| | **0/1 Knapsack** | **Unbounded Knapsack** |
|---|---|---|
| **Reuse** | Each item at most once | Each item unlimited times |
| **Inner loop direction** | **Backward** (`W → weight`) | Forward (`weight → W`) |
| **Example** | LC 416, 494, 1049 | LC 322, 518 |
| **Why direction differs** | Backward reads OLD dp[w-weight] | Forward reads NEW dp[w-weight] (allows reuse) |

---

#### Similar LeetCode Problems

| LC # | Problem | Variant | Key Transformation |
|------|---------|---------|-------------------|
| **416** | Partition Equal Subset Sum | Boolean | `dp[s]` = can we reach sum s? |
| **494** | Target Sum | Count ways | `sum1 = (total + target) / 2`; count subsets |
| **1049** | Last Stone Weight II | Integer | Maximize subset sum ≤ total/2 |
| **474** | Ones and Zeroes | 2-D capacity | dp[i][j] = max strings using ≤ i zeros, ≤ j ones |
| **879** | Profitable Schemes | 2-D capacity | dp[profit][members] counting |
| **2915** | Length of Longest Subsequence | Boolean | Same backward pattern |

---

#### 2-D 0/1 Knapsack (Classic, for reference)

```python
def knapsack_01(weights, values, capacity):
    # time = O(n * W), space = O(n * W)
    n = len(weights)
    dp = [[0] * (capacity + 1) for _ in range(n + 1)]
    for i in range(1, n + 1):
        for w in range(capacity + 1):
            dp[i][w] = dp[i-1][w]                                    # skip
            if weights[i-1] <= w:
                dp[i][w] = max(dp[i][w],
                               dp[i-1][w - weights[i-1]] + values[i-1])  # take
    return dp[n][capacity]
```

---

> **Going deeper** — the subset-sum reduction (LC 416 / 494 / 1049), the unbounded and bounded
> variants, and the combinations-vs-permutations loop-order rule all live in
> [**knapsack.md**](./knapsack.md). This template plus the recognition table above is the part worth
> memorising; the rest is reference.

### Template 5: State Machine DP — LC 121
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

        # 3. REST: rest: either stayed in rest, or just came out of sold cooldown
        rest = max(rest, prev_sold)

    # Max profit when not holding stock
    return max(sold, rest)
```

**State Transition Diagram for LC 309:**
```text
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

### Template 6: Top-Down Memoization — LC 70
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

### Template 6-1: DFS + Memoization Pattern (Graph/Sequence Chaining)

**Problem Type**: Finding the longest chain/sequence where each element is derived from the previous by a single structural operation

#### 🎯 Pattern

| Aspect | Detail |
|--------|--------|
| **Pattern** | Chaining / Sequence DP |
| **Core Operation** | Each element is exactly one step larger/different than the previous |
| **Two Approaches** | ① Bottom-up DP (sort + remove char) ② Top-down DFS + Memoization |
| **Key Data Structure** | `Map<String, Integer> dp` or `Map<String, Integer> memo` |
| **Time Complexity** | O(N × L²) where N = words, L = max word length |
| **Space Complexity** | O(N) for map |

#### 💡 Core Idea (LC 1048 - Longest String Chain)

> **Word A is a predecessor of Word B** if you can insert exactly one letter into A to get B.

**Two equivalent ways to think about it**:
- **Forward (DFS)**: From word `w`, look for all words of length+1 that are valid successors → recurse
- **Backward (Bottom-up DP)**: For word `w`, try removing each character → check if the shorter word is a known predecessor

**Key Insight — Backward approach is simpler**:
- Sort words by length (shortest first)
- For each word, remove one character at a time to generate all possible predecessors
- If a predecessor exists in the dp map, extend its chain
- This avoids the `isOneOff` comparison entirely

```text
words = ["a","b","ba","bca","bda","bdca"]
Sorted: ["a","b","ba","bca","bda","bdca"]

dp["a"]    = 1  (no predecessors)
dp["b"]    = 1  (no predecessors)
dp["ba"]   = 2  (remove 'b' → "a" exists, remove 'a' → "b" exists → max(dp["a"], dp["b"]) + 1 = 2)
dp["bca"]  = 3  (remove 'c' → "ba" exists → dp["ba"] + 1 = 3)
dp["bda"]  = 3  (remove 'd' → "ba" exists → dp["ba"] + 1 = 3)
dp["bdca"] = 4  (remove 'c' → "bda" exists → dp["bda"] + 1 = 4)

Answer: 4
```

#### **Approach 1: Bottom-up DP** ⭐ (Recommended — simpler)

**State**: `dp[word]` = length of longest chain ending at `word`

```java
public int longestStrChain(String[] words) {
    // Step 1: Sort by word length (process predecessors before successors)
    Arrays.sort(words, (a, b) -> a.length() - b.length());

    // Step 2: dp[word] = longest chain ending at this word
    Map<String, Integer> dp = new HashMap<>();
    int maxChain = 1;

    for (String word : words) {
        int best = 1;

        // Step 3: Try removing each character to generate all predecessors
        for (int i = 0; i < word.length(); i++) {
            String prev = word.substring(0, i) + word.substring(i + 1);
            // If predecessor exists, extend its chain
            best = Math.max(best, dp.getOrDefault(prev, 0) + 1);
        }

        dp.put(word, best);
        maxChain = Math.max(maxChain, best);
    }

    return maxChain;
}
```

**Why this works**: Sorting guarantees that when we process word `w`, all words shorter than `w` are already in `dp`. Removing one character generates all possible predecessors of length `|w|-1`.

#### **Approach 2: Top-down DFS + Memoization**

**State**: `memo[word]` = length of longest chain starting from `word`

```java
private Map<Integer, List<String>> wordLengthMap;
private Map<String, Integer> memo;

public int longestStrChain(String[] words) {
    // Group words by length for O(1) lookup of next-length candidates
    wordLengthMap = new HashMap<>();
    for (String word : words) {
        wordLengthMap.putIfAbsent(word.length(), new ArrayList<>());
        wordLengthMap.get(word.length()).add(word);
    }

    int maxPath = 1;
    memo = new HashMap<>();
    for (String word : words)
        maxPath = Math.max(maxPath, dfs(word));

    return maxPath;
}

private int dfs(String word) {
    // Base case: no words of next length exist
    if (!wordLengthMap.containsKey(word.length() + 1)) return 1;
    if (memo.containsKey(word)) return memo.get(word);

    int maxPath = 0;
    // Try all words of length+1 as potential successors
    for (String nextWord : wordLengthMap.get(word.length() + 1)) {
        if (isOneOff(word, nextWord))
            maxPath = Math.max(maxPath, dfs(nextWord));
    }

    memo.put(word, maxPath + 1);
    return memo.get(word);
}

// Two-pointer: returns true if b has exactly one more char than a
private boolean isOneOff(String a, String b) {
    int count = 0;
    for (int i = 0, j = 0; i < b.length() && j < a.length() && count <= 1; i++) {
        if (a.charAt(j) != b.charAt(i)) count++;
        else j++;
    }
    return count <= 1;
}
```

#### **Approach Comparison**

| | Bottom-up DP (Approach 1) | Top-down DFS (Approach 2) |
|---|---|---|
| **Direction** | Backward: remove char to find predecessors | Forward: add char to find successors |
| **Sorting** | Required (shortest first) | Not required |
| **Helper needed** | No (substring removal is the check) | Yes (`isOneOff` two-pointer) |
| **Complexity** | O(N × L²) | O(N × L²) |
| **Simplicity** | Simpler ✅ | More verbose |

#### **Similar LeetCode Problems** 📚

| Problem | LC # | Chain Element | Operation | Pattern |
|---------|------|--------------|-----------|---------|
| **Longest String Chain** | 1048 | String | Insert 1 char | Sort + Remove char DP |
| **Longest Increasing Subsequence** | 300 | Number | Increase by any amount | Sort + 1D DP or patience sort |
| **Longest Consecutive Sequence** | 128 | Number | +1 exactly | HashSet lookup |
| **Word Ladder** | 127 | String | Change 1 char (not insert) | BFS (find shortest path) |
| **Longest Increasing Path in Matrix** | 329 | Grid cell | Move to larger neighbor | DFS + Memo on 2D grid |
| **Longest Path in Tree** | 2246 | Tree node | Parent-child edge | DFS on tree |
| **Concatenated Words** | 472 | String | One word is prefix of another | DP + word break |

**Key Distinctions**:
- LC 1048 vs LC 300: Both are "longest chain" but 1048 uses string structure; 300 uses numeric ordering
- LC 1048 vs LC 127: 1048 inserts a char (length changes); 127 replaces a char (length fixed) → BFS for shortest path
- LC 1048 vs LC 128: 1048 allows inserting anywhere; 128 requires consecutive integers

#### **Pattern Recognition Checklist** ✅

Use this pattern when:
- ✅ Building chains where each element is exactly one operation away from the next
- ✅ Predecessor-successor relation is well-defined (insert char, +1 value, etc.)
- ✅ Need the longest such chain across all possible starting points
- ✅ Same element can appear in chains from multiple different predecessors → memoize

#### **Common Pitfalls** ⚠️

1. **Forgot to sort (Bottom-up DP)**: Must sort by length first so predecessors are in `dp` when successors are processed
2. **Using `contains` instead of `getOrDefault`**: Always use `dp.getOrDefault(prev, 0) + 1` — predecessor might not exist in list
3. **Generating all successors instead of predecessors (Bottom-up)**: It's simpler to remove chars (generate predecessors) than to insert chars (generate successors) — fewer strings to generate
4. **Validation complexity (Top-down DFS)**: Use two-pointer `isOneOff` in O(L) rather than brute-force O(L²) comparison

**File Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/LongestStringChain.java`

---

### Template 7: String DP (Edit Distance / Levenshtein Distance) — LC 72

#### 🎯 **Pattern Recognition**

**When to use Edit Distance DP:**
- ✅ Converting one string to another with insert/delete/replace operations
- ✅ Finding minimum "edit distance" or "operations" between two strings
- ✅ String transformation problems (especially LeetCode medium/hard string problems)
- ✅ Two-string comparison problems where operations have costs

**Problem**: LC 72 - Edit Distance (aka Levenshtein Distance)

Given two strings `word1` and `word2`, find the **minimum number of operations** required to convert `word1` to `word2`.

Allowed operations (each counts as 1 step):
1. Insert a character
2. Delete a character
3. Replace a character

#### 💡 **Core DP Idea**

The key insight is: **When characters don't match, choose the operation that leads to the minimum cost.**

```text
At position (i, j):
  - If chars match: No cost, take solution from (i-1, j-1)
  - If they don't:
      Delete from word1:   dp[i-1][j] + 1
      Insert into word1:   dp[i][j-1] + 1
      Replace in word1:    dp[i-1][j-1] + 1
      → Take the minimum of these three
```

#### **State Definition**:
- `dp[i][j]` = minimum operations to convert `word1[0...i-1]` to `word2[0...j-1]`

#### **Base Cases**:
- `dp[i][0] = i` (delete all i characters from word1 to get empty string)
- `dp[0][j] = j` (insert all j characters into empty string to get word2)

#### **Transition**:
```text
If word1[i-1] == word2[j-1]:
    dp[i][j] = dp[i-1][j-1]  (no operation needed)
Else:
    dp[i][j] = 1 + min(
        dp[i-1][j],     # Delete from word1
        dp[i][j-1],     # Insert into word1
        dp[i-1][j-1]    # Replace in word1
    )
```

#### **Python Implementation (Bottom-Up)**:
```python
def minDistance(word1, word2):
    """LC 72: Edit Distance - Bottom-Up DP"""
    m, n = len(word1), len(word2)
    # dp[i][j] = min operations to convert word1[:i] to word2[:j]
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    # Base cases
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j

    # Fill DP table
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[i][j] = dp[i-1][j-1]
            else:
                dp[i][j] = 1 + min(
                    dp[i-1][j],    # Delete
                    dp[i][j-1],    # Insert
                    dp[i-1][j-1]   # Replace
                )

    return dp[m][n]
```

#### **Java Implementation (Bottom-Up)**:
```java
// LC 72: Edit Distance - Standard approach
public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();

    int[][] dp = new int[m + 1][n + 1];

    // Base cases
    for (int i = 0; i <= m; i++) {
        dp[i][0] = i;  // Delete all characters
    }
    for (int j = 0; j <= n; j++) {
        dp[0][j] = j;  // Insert all characters
    }

    // Fill DP table
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + Math.min(
                    dp[i - 1][j],    // Delete
                    Math.min(
                        dp[i][j - 1],    // Insert
                        dp[i - 1][j - 1] // Replace
                    )
                );
            }
        }
    }

    return dp[m][n];
}
```

#### **Implementation Variants**

**Variant 1: Top-Down Memoization (Recursion + Cache)**
```java
private int[][] memo;

public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();
    memo = new int[m][n];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            memo[i][j] = -1;
        }
    }
    return dfs(0, 0, word1, word2, m, n);
}

private int dfs(int i, int j, String word1, String word2, int m, int n) {
    // Base cases
    if (i == m) return n - j;
    if (j == n) return m - i;
    
    // Check memo
    if (memo[i][j] != -1) return memo[i][j];

    int res;
    if (word1.charAt(i) == word2.charAt(j)) {
        res = dfs(i + 1, j + 1, word1, word2, m, n);
    } else {
        res = 1 + Math.min(
            dfs(i + 1, j, word1, word2, m, n),      // Delete
            Math.min(
                dfs(i, j + 1, word1, word2, m, n),  // Insert
                dfs(i + 1, j + 1, word1, word2, m, n) // Replace
            )
        );
    }

    memo[i][j] = res;
    return res;
}
```

**Variant 2: Space-Optimized (O(n) Space)**
```java
public int minDistance(String word1, String word2) {
    int m = word1.length();
    int n = word2.length();
    
    // Use 1D array instead of 2D (only need previous row)
    int[] prev = new int[n + 1];
    for (int j = 0; j <= n; j++) {
        prev[j] = j;
    }

    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        curr[0] = i;
        
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                curr[j] = prev[j - 1];
            } else {
                curr[j] = 1 + Math.min(
                    prev[j],        // Delete
                    Math.min(
                        curr[j - 1],    // Insert
                        prev[j - 1]     // Replace
                    )
                );
            }
        }
        
        prev = curr;
    }

    return prev[n];
}
```

#### **Visual DP Table Example**

```text
Input: word1 = "horse", word2 = "ros"

       ""  r   o   s
    "" 0   1   2   3
    h  1   1   2   3
    o  2   2   1   2
    r  3   2   2   2
    s  4   3   3   2
    e  5   4   4   3

Result: dp[5][3] = 3 operations
Explanation: 
  - Replace 'h' → 'r': "rorse"
  - Delete 'r': "rose"
  - Delete 'e': "ros"
```

#### **Key Insights**

1. **Three Operations Visualization**:
   ```
   dp[i-1][j]      dp[i-1][j-1]
       ↓            ↘
   dp[i][j-1] →   dp[i][j]
   
   Delete (↓):    dp[i-1][j] + 1
   Replace (↘):   dp[i-1][j-1] + 1
   Insert (→):    dp[i][j-1] + 1
   ```

2. **Indexing Styles**:
   - **1-based indexing** (cleaner): Loop `i` from 1 to m, store in `dp[i][j]`
   - **0-based indexing** (also works): Loop `i` from 0 to m-1, store in `dp[i+1][j+1]`

3. **Complexity**:
   - **Time**: O(m × n)
   - **Space**: O(m × n) standard, O(min(m,n)) space-optimized

4. **Why we look at 3 neighbors**:
   - Characters **don't match** → pick cheapest operation
   - Characters **match** → no cost, inherit from diagonal
   - This greedy choice at each step leads to global optimum

#### **Pattern Recognition Checklist** ✅

Use this pattern when you see:
- "Minimum number of operations" + two strings → Edit Distance
- "Insert, Delete, Replace" operations → Edit Distance likely
- "Convert string A to string B" → Edit Distance
- "Levenshtein distance" or "edit distance" → Definitely this template
- String comparison where operations have equal cost (1)

#### **Common Mistakes** ⚠️

1. **Wrong indexing**: Forgetting that `dp[i][j]` uses `word1[i-1]` and `word2[j-1]`
   - ❌ `if (word1.charAt(i) == word2.charAt(j))`
   - ✅ `if (word1.charAt(i-1) == word2.charAt(j-1))`

2. **Incorrect base cases**: Not initializing the first row and column
   - Must set `dp[i][0] = i` and `dp[0][j] = j`

3. **Missing +1 for operations**: Forgetting to add 1 when characters don't match
   - ❌ `dp[i][j] = Math.min(...)`
   - ✅ `dp[i][j] = 1 + Math.min(...)`

4. **Wrong state definition**: Confusing which string index corresponds to which dimension
   - Be consistent: rows = word1, columns = word2

---

#### **Related String DP Problems** 📚

| LC # | Problem | Variant/Difference | Difficulty | Key Insight |
|------|---------|-------------------|------------|-------------|
| **72** | **Edit Distance** | Classic (Insert, Delete, Replace) | Medium | 3 operations, take min |
| **583** | Delete Operation for Two Strings | Only DELETE allowed | Medium | `dp[i][j] = dp[i-1][j] + 1` or `dp[i][j-1] + 1` |
| **712** | Minimum ASCII Delete Sum | Delete with ASCII cost | Medium | Track cost instead of count |
| **1143** | Longest Common Subsequence (LCS) | Maximize matches (opposite of edit) | Medium | Match: +1, Mismatch: max(left, top) |
| **1312** | Minimum Insertion Steps | Make string palindrome | Hard | Similar to LCS |
| **87** | Scramble String | Check if one string is scrambled version of another | Hard | 2D DP with partitioning |
| **115** | Distinct Subsequences | Count subsequences matching pattern | Hard | Counting variant |
| **44** | Wildcard Matching | Pattern matching with `?` and `*` | Hard | Extended string DP |
| **10** | Regular Expression Matching | DP pattern matching | Hard | Handle regex special chars |

#### **Comparison: LC 72 vs LC 1143 (LCS)**

| Aspect | LC 72 (Edit Distance) | LC 1143 (LCS) |
|--------|----------------------|-------------|
| **Goal** | **Minimize** operations needed | **Maximize** matching characters |
| **Operations** | Insert, Delete, Replace | Only match or skip |
| **Match** | No cost (no operation) | +1 to length |
| **Mismatch** | 1 + min(3 options) | max(skip left, skip right) |
| **DP Transition** | `dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1])` | `dp[i][j] = dp[i-1][j-1] + 1` or `max(dp[i-1][j], dp[i][j-1])` |

#### **File References**:
- **Java Implementations**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/EditDistance.java`
  - Multiple solution approaches (bottom-up, top-down, space-optimized)
  - Well-commented with detailed DP transition explanations
- **Related**: See also Template 8 (Longest Common Subsequence) for the comparison-maximization variant

### Template 7-1: One Edit Distance (LC 161) — Exactly-1-Edit Variant

#### 🎯 **Pattern Recognition**

**This is NOT the same as Edit Distance (LC 72).**

| | LC 72 Edit Distance | LC 161 One Edit Distance |
|---|---|---|
| **Goal** | Find **minimum** edit count | Check if edit count is **exactly 1** |
| **Operations** | Insert, Delete, Replace | Same three |
| **Output** | Integer (min ops) | Boolean |
| **Approach** | Full 2D DP | 2-pointer OR 2D DP (check `== 1`) |
| **Time** | O(m × n) | O(n) two-pointer, O(m×n) DP |

#### 💡 **Core Idea — Two-Pointer (O(n), preferred)**

Instead of filling the whole DP table, scan left-to-right and at the **first mismatch**, apply the only possible repair and verify the suffix immediately:

```text
Three cases at first mismatch position i:
  len(s) == len(t) → Replace s[i]:  check s[i+1..] == t[i+1..]
  len(s) <  len(t) → Insert into s: check s[i..]   == t[i+1..]
  len(s) >  len(t) → Delete from s: check s[i+1..] == t[i..]
```

Post-loop (no mismatch): valid only if `len(t) == len(s) + 1`.

**Java (two-pointer):**
```java
// LC 161 - One Edit Distance  O(n) time, O(1) space
public boolean isOneEditDistance(String s, String t) {
    int ns = s.length(), nt = t.length();
    if (ns > nt) return isOneEditDistance(t, s); // ensure s is shorter
    if (nt - ns > 1) return false;               // gap > 1 → impossible

    for (int i = 0; i < ns; i++) {
        if (s.charAt(i) != t.charAt(i)) {
            if (ns == nt) {
                // Replace: rest of both strings must match
                return s.substring(i + 1).equals(t.substring(i + 1));
            } else {
                // Insert into s (skip one char in t)
                return s.substring(i).equals(t.substring(i + 1));
            }
        }
    }
    // No mismatch in s — valid only if t has one trailing extra char
    return ns + 1 == nt;
}
```

#### 💡 **Core Idea — DP (O(m×n), same table as LC 72)**

Run the full Edit Distance DP and return `dp[m][n] == 1`:

```java
// LC 161 - DP approach
public boolean isOneEditDistance(String s, String t) {
    int ns = s.length(), nt = t.length();
    if (Math.abs(ns - nt) > 1) return false;

    int[][] dp = new int[ns + 1][nt + 1];
    for (int i = 0; i <= ns; i++) dp[i][0] = i;
    for (int j = 0; j <= nt; j++) dp[0][j] = j;

    for (int i = 1; i <= ns; i++) {
        for (int j = 1; j <= nt; j++) {
            if (s.charAt(i - 1) == t.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                               Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }
    }
    return dp[ns][nt] == 1;
}
```

**Why the early `Math.abs > 1` guard matters**: strings that differ in length by more than 1 always have edit distance ≥ 2, so we skip the O(m×n) work entirely.

#### 📐 **Why These Three DP Transitions? (Intuition with Example)**

```text
| Operation | DP Cell Used  | Meaning                                      |
|-----------|---------------|----------------------------------------------|
| Insert    | dp[i][j-1]    | Already matched s[0..i] to t[0..j-1], then insert t[j] |
| Delete    | dp[i-1][j]    | Already matched s[0..i-1] to t[0..j], then delete s[i] |
| Replace   | dp[i-1][j-1]  | Already matched s[0..i-1] to t[0..j-1], then swap s[i]→t[j] |
```

**Concrete walkthrough: s = "ab", t = "acb"**

Build the table where `dp[i][j]` = min edits to convert `s[0..i-1]` → `t[0..j-1]`:

```text
       ""   a    c    b
  ""  [ 0][ 1][ 2][ 3]
  a   [ 1][ 0][ 1][ 2]
  b   [ 2][ 1][ 1][ 1]
```

Focus on `dp[2][3]` (convert "ab" → "acb"):

```text
s[1] = 'b',  t[2] = 'b'   → chars MATCH → dp[2][3] = dp[1][2] = 1  ✓
```

Now focus on `dp[1][2]` (convert "a" → "ac") where s[0]='a', t[1]='c' (NO match):

```text
Option 1 — INSERT 'c' into s after "a":
    We already know it takes dp[1][1] = 0 ops to match "a"→"a",
    then we insert 'c' → dp[1][1] + 1 = 1
    → uses dp[i][j-1]  (same row, one column back = t is one char shorter)

Option 2 — DELETE s[0]='a' from s:
    We already know it takes dp[0][2] = 2 ops to match ""→"ac",
    then we delete 'a' → dp[0][2] + 1 = 3
    → uses dp[i-1][j]  (one row up = s is one char shorter)

Option 3 — REPLACE s[0]='a' with t[1]='c':
    We already know it takes dp[0][1] = 1 op to match ""→"a",
    then swap 'a'→'c' → dp[0][1] + 1 = 2
    → uses dp[i-1][j-1]  (diagonal = both strings one char shorter)

→ dp[1][2] = min(1, 3, 2) = 1
```

**Mental model for the three cells:**

```text
dp[i-1][j-1]  dp[i-1][j]
     ↘              ↓
dp[i][j-1]  →   dp[i][j]

  ↘ Replace      ↓ Delete (from s)
  → Insert (into s, advance t only)
```

- **`dp[i][j-1]` (left)**: t advanced one step but s didn't → we filled the gap with an **insert**
- **`dp[i-1][j]` (up)**: s advanced one step but t didn't → we removed a char from s (**delete**)
- **`dp[i-1][j-1]` (diagonal)**: both advanced → we **replaced** s[i] with t[j]

For LC 161 specifically: after filling the table, `dp[ns][nt] == 1` means exactly one of these three operations was needed.

#### **When to Use Which**

| Approach | When to Prefer |
|----------|----------------|
| Two-pointer (`substring.equals`) | Interview, O(n) time, simple to reason |
| Full DP | Already have LC 72 solution, want code reuse |

#### **Similar LC Problems**

| Problem | LC# | Relation |
|---------|-----|----------|
| Edit Distance | 72 | Generalized version (minimize ops) |
| One Edit Distance | 161 | Exactly 1 op — this pattern |
| Valid Palindrome II | 680 | At most 1 delete to form palindrome |
| Longest Common Subsequence | 1143 | Maximize matches instead of minimize ops |
| Delete Operation for Two Strings | 583 | Delete-only edit distance |

---

### Template 8: Longest Common Subsequence (LCS) — LC 1143
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

### Template 9: State Compression (Bitmask DP) — LC 847
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

### Template 10: Palindrome Substring DP ⭐⭐⭐⭐⭐ — LC 5

**Problem archetype**: LC 647 (Count Palindromic Substrings), LC 5 (Longest Palindromic Substring)

#### 🎯 Approach Comparison

| Approach | Time | Space | When to Use |
|---|---|---|---|
| Brute Force | O(n³) | O(1) | Never in interviews |
| 2D DP (length-based) | O(n²) | O(n²) | Need full dp table for other queries |
| 2D DP (backward-i) | O(n²) | O(n²) | Same as above, slightly cleaner |
| **Two Pointers (expand center)** ⭐ | O(n²) | **O(1)** | **Default — simpler, space-optimal** |
| Manacher's Algorithm | O(n) | O(n) | Competitive programming / optimal |

---

#### 💡 Core DP Idea

**State**: `dp[i][j] = True` if `s[i..j]` is a palindrome.

**Transition** — a substring is palindrome if and only if:
```text
dp[i][j] = True
  when:  s[i] == s[j]
  AND    (j - i <= 2   ← length ≤ 3, no inner to check)
         OR dp[i+1][j-1]  ← inner substring is also palindrome
```

**Why `j - i <= 2` is the base case?**
- Length 1 (`i == j`): always a palindrome — single char
- Length 2 (`j - i == 1`): palindrome iff `s[i] == s[j]` — "aa"
- Length 3 (`j - i == 2`): palindrome iff outer chars match — "aba"; inner is a single char, always valid

---

#### Approach 1: 2D DP — Length-based outer loop

```python
# IDEA: build dp table by increasing substring length
# dp[i][j] = True if s[i:j+1] is palindrome
def countSubstrings_dp_length(s):
    # time = O(n^2), space = O(n^2)
    n = len(s)
    dp = [[False] * n for _ in range(n)]
    count = 0

    for length in range(1, n + 1):          # outer: substring length
        for i in range(n - length + 1):     # inner: start index
            j = i + length - 1              # end index

            if length == 1:
                dp[i][j] = True
            elif length == 2:
                dp[i][j] = (s[i] == s[j])
            else:
                dp[i][j] = (s[i] == s[j] and dp[i+1][j-1])

            if dp[i][j]:
                count += 1

    return count
```

#### Approach 2: 2D DP — Backward-i + Forward-j ⭐ (see also Template 3-3)

```python
# IDEA: loop i backward so dp[i+1][...] is always ready when computing dp[i][...]
def countSubstrings_dp_backward(s):
    # time = O(n^2), space = O(n^2)
    n = len(s)
    dp = [[False] * n for _ in range(n)]
    count = 0

    for i in range(n - 1, -1, -1):   # i backward: ensures dp[i+1][j-1] is computed
        for j in range(i, n):         # j forward:  ensures dp[i][j-1] is computed
            if s[i] == s[j] and (j - i <= 2 or dp[i+1][j-1]):
                dp[i][j] = True
                count += 1

    return count
```

**Why backward-i?**
`dp[i][j]` depends on `dp[i+1][j-1]` (the inner substring). To read row `i+1` when filling row `i`, iterate `i` from `n-1` down to `0`.

---

#### Approach 3: Two Pointers — Expand Around Center ⭐⭐ (Recommended)

**Key insight**: every palindrome has a center. Expand outward from each possible center and count.
- **Odd-length** palindrome: center is one character — expand from `(i, i)`
- **Even-length** palindrome: center is between two characters — expand from `(i, i+1)`

```python
# IDEA: expand around center — O(1) space, no dp table needed
def countSubstrings_two_pointers(s):
    # time = O(n^2), space = O(1)
    count = 0
    n = len(s)

    def expand(l, r):
        cnt = 0
        while l >= 0 and r < n and s[l] == s[r]:
            cnt += 1
            l -= 1
            r += 1
        return cnt

    for i in range(n):
        count += expand(i, i)      # odd-length  (center at i)
        count += expand(i, i + 1)  # even-length (center between i and i+1)

    return count
```

**Visual trace for `s = "aaa"`:**
```text
Center i=0:
  odd  (0,0): "a"          → count +1
  even (0,1): "aa"         → count +1

Center i=1:
  odd  (1,1): "a"→(0,2)"aaa" → count +2  (expands to full string)
  even (1,2): "aa"         → count +1

Center i=2:
  odd  (2,2): "a"          → count +1
  even (2,3): out of bounds → count +0

Total = 6  ✓
```

---

#### Approach 4: Manacher's Algorithm — O(n)

```python
# IDEA: reuse previously computed palindrome radii to skip redundant checks
def countSubstrings_manacher(s):
    # time = O(n), space = O(n)
    def manacher(s):
        # transform: "abc" → "^#a#b#c#$"
        t = '^#' + '#'.join(s) + '#$'
        P = [0] * len(t)
        C = R = 0
        for i in range(1, len(t) - 1):
            mirror = 2 * C - i
            if R > i:
                P[i] = min(R - i, P[mirror])
            while t[i + 1 + P[i]] == t[i - 1 - P[i]]:
                P[i] += 1
            if i + P[i] > R:
                C, R = i, i + P[i]
        return P

    return sum((r + 1) // 2 for r in manacher(s))
```

---

#### Key Decision: DP vs Two Pointers

```text
Need the full dp[i][j] table? (e.g., for partitioning / further DP)
  YES → Use 2D DP (Approach 1 or 2)
  NO  → Use Two Pointers (Approach 3) — simpler + O(1) space
```

---

#### Similar LeetCode Problems

| LC # | Problem | Approach | Key Difference |
|------|---------|----------|----------------|
| **647** | Palindromic Substrings | DP or Expand | Count all palindromes |
| **5** | Longest Palindromic Substring | Expand or DP | Track max length instead of count |
| **516** | Longest Palindromic Subsequence | 2D DP (backward-i) | Subsequence (not substring), use `dp[i+1][j-1]+2` |
| **132** | Palindrome Partitioning II | DP + palindrome table | Min cuts; precompute `isPalin[i][j]` first |
| **131** | Palindrome Partitioning | Backtracking + palindrome table | All partition ways |
| **1312** | Min Insertions to Make Palindrome | 2D DP | `n - LPS(s)` |
| **680** | Valid Palindrome II | Two pointers | At most 1 delete |

---

#### Common Mistakes

1. **Checking `dp[i+1][j-1]` without the `j-i <= 2` guard**: When `j-i == 1`, `dp[i+1][j-1]` = `dp[i+1][i]` (invalid index). Always pair with `j - i <= 2` as base case.
2. **Wrong loop direction in backward-i DP**: Forgetting that `i` must go from `n-1` to `0` so `dp[i+1][...]` is already filled.
3. **Missing even-length center in expand**: Always call `expand(i, i)` AND `expand(i, i+1)` to cover both odd/even palindromes.

### Template 11: Fibonacci-like Patterns — LC 70
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

### Template 11-1: Rolling Variables — Getting the `var` Update Right ⭐⭐⭐⭐

**Pattern**: any recurrence that only looks back a **fixed** number of steps (`dp[i-1] … dp[i-k]`) needs only `k` variables, not an array. The whole difficulty moves out of the recurrence and into the *update block*: after computing `dp[i]`, the `k` variables must be shifted one slot so the next iteration sees the right window.

**Motivating problem** — *candy bar / Toblerone*: a bar is `n` pieces in a single row, you bite off 1, 2, or 3 pieces at a time. How many different ways can you eat the whole bar?

The last bite is 1, 2, or 3 pieces, and those cases are disjoint, so:

```text
dp[i] = dp[i-1] + dp[i-2] + dp[i-3]     # Tribonacci (same shape as LC 1137)

dp[0] = 1   # one way to eat nothing (the empty sequence of bites)
dp[1] = 1   # (1)
dp[2] = 2   # (1,1) (2)
dp[3] = 4   # (1,1,1) (1,2) (2,1) (3)
```

Only three previous values are ever read → three variables.

#### The update block

```python
# python
# IDEA: keep a sliding window of the last 3 dp values instead of a dp array
# time = O(n), space = O(1)
def get_ways(n):
    # 1. Base / edge cases
    if n == 0:
        return 0          # "no way to eat a bar that doesn't exist"
    if n <= 2:
        return n          # dp[1] = 1, dp[2] = 2
    if n == 3:
        return 4

    # 2. Seed: p1, p2, p3 = dp[i-3], dp[i-2], dp[i-1] for the first i we compute (i = 4)
    p1, p2, p3 = 1, 2, 4  # dp[1], dp[2], dp[3]

    # 3. Transition + update
    for i in range(4, n + 1):
        dp = p1 + p2 + p3   # dp[i] = dp[i-3] + dp[i-2] + dp[i-1]
        # update: slide the window right by one
        p1 = p2             # new dp[i-3] is the old dp[i-2]
        p2 = p3             # new dp[i-2] is the old dp[i-1]
        p3 = dp             # new dp[i-1] is the dp[i] we just computed

    return dp
```

**Key Idea**: `p1, p2, p3` are not "three numbers I happen to need" — they are *named positions relative to `i`*. Write the invariant down before the loop:

```text
at the top of iteration i:   p1 = dp[i-3],  p2 = dp[i-2],  p3 = dp[i-1]
```

Every line of the update block exists to restore that invariant for `i+1`.

#### Visual trace (`n = 6`)

```text
i        p1      p2      p3      dp = p1+p2+p3
----------------------------------------------
(seed)  dp1=1   dp2=2   dp3=4        -
4         1       2       4        7      <- dp[4] = 1+2+4
  update: p1<-2, p2<-4, p3<-7
5         2       4       7       13      <- dp[5] = 2+4+7
  update: p1<-4, p2<-7, p3<-13
6         4       7      13       24      <- dp[6] = 4+7+13

answer = 24

window slides right one slot per iteration:
dp:  [1] [2] [4]  7   13   24
      ^p1 ^p2 ^p3            i=4
          ^p1 ^p2 ^p3        i=5
              ^p1 ^p2 ^p3    i=6
```

#### Update order: the classic bug ⭐⭐⭐⭐⭐

Assign **oldest → newest** (`p1` first, `p3` last). Going the other way overwrites a value you still need:

```python
# python
# ❌ WRONG — p3 is clobbered before p2 reads it
p3 = dp     # p3 destroyed
p2 = p3     # p2 gets dp[i], not dp[i-1]
p1 = p2     # p1 gets dp[i] too — all three collapse to the same value

# ✅ RIGHT — each read happens before its target is overwritten
p1 = p2
p2 = p3
p3 = dp

# ✅ ALSO RIGHT — tuple assignment evaluates the whole RHS first, so order is irrelevant
p1, p2, p3 = p2, p3, dp
```

In Java there is no tuple assignment, so the oldest → newest order is the only option:

```java
// java
// IDEA: same 3-variable rolling window; shift oldest -> newest
// time = O(n), space = O(1)
public int getWays(int n) {
    if (n == 0) return 0;
    if (n <= 2) return n;
    if (n == 3) return 4;

    int p1 = 1, p2 = 2, p3 = 4;   // dp[1], dp[2], dp[3]
    int dp = p3;
    for (int i = 4; i <= n; i++) {
        dp = p1 + p2 + p3;
        p1 = p2;                  // must go oldest -> newest
        p2 = p3;
        p3 = dp;
    }
    return dp;
}
```

#### Return the rolling variable, not the loop temp — LC 198 ⭐⭐⭐⭐⭐

The update block creates a third name, `cur` (the freshly computed `dp[i]`). At the end of the
function it is tempting to `return cur` — it is, after all, the last value computed. Return the
**newest rolling variable** instead.

```python
# python
# LC 198 - House Robber
# IDEA: dp[i] = max(dp[i-1], dp[i-2] + nums[i]) -> only 2 vars needed
# time = O(n), space = O(1)
def rob(nums):
    if not nums:
        return 0
    n = len(nums)
    if n == 1:
        return nums[0]          # p2's seed reads nums[1], so n == 1 must leave early

    # invariant at the top of iteration i:  p1 = dp[i-2],  p2 = dp[i-1]
    p1 = nums[0]                        # dp[0]
    p2 = max(nums[0], nums[1])          # dp[1]

    for i in range(2, n):
        cur = max(p1 + nums[i], p2)     # dp[i]
        p1 = p2                         # oldest -> newest
        p2 = cur

    return p2                           # NOT `cur`
```

**Why `p2` and not `cur`:**

1. **`cur` may never be bound.** For `n == 2` the loop is `range(2, 2)` — empty — so the body never
   runs and `cur` is never created. `return cur` then raises `UnboundLocalError`, on a perfectly
   valid input (`[2, 7] -> 7`). `p2` is seeded *before* the loop, so it always exists.
2. **`p2` carries the invariant, `cur` doesn't.** `p2 = dp[last index computed]` is true after the
   seeding lines *and* after every iteration. `cur` only means "the value from the most recent
   iteration" — a statement about the loop, not about the answer.
3. **When both are defined they are equal.** The last statement of the body is `p2 = cur`, so
   whenever `cur` exists, `p2 == cur`. `p2` is correct in strictly more cases, at zero cost.

So the rule generalizes past this problem: **the answer lives in a variable that is valid before the
loop starts**, which is exactly the newest rolling variable. Same reasoning applies to LC 70
(`return p2`, not the `cur` inside the loop) and to every other fixed-window recurrence in this
section.

```text
n = 2, nums = [2, 7]

  p1 = 2                  <- dp[0]
  p2 = max(2, 7) = 7      <- dp[1]
  for i in range(2, 2):   <- zero iterations, `cur` never assigned

  return p2  -> 7         ✅
  return cur -> UnboundLocalError  ❌
```

Java shows the same bug in a different costume — a `cur` declared outside the loop needs a dummy
initializer, and whatever dummy you pick is silently returned when the loop doesn't run:

```java
// java
// LC 198 - House Robber
// IDEA: same 2-variable rolling window; return the rolling var, not the temp
// time = O(n), space = O(1)
public int rob(int[] nums) {
    if (nums == null || nums.length == 0) return 0;
    int n = nums.length;
    if (n == 1) return nums[0];

    int p1 = nums[0];                       // dp[0]
    int p2 = Math.max(nums[0], nums[1]);    // dp[1]

    for (int i = 2; i < n; i++) {
        int cur = Math.max(p1 + nums[i], p2);
        p1 = p2;
        p2 = cur;
    }
    return p2;   // hoisting `cur` out of the loop just to return it would need `int cur = 0;`
                 // -> returns 0 for n == 2 instead of the real answer
}
```

> **Bonus — the seed-free variant.** Starting from `p1 = p2 = 0` and iterating over *every* element
> removes the `n == 1` / `n == 2` special cases entirely, because `dp[-1] = dp[-2] = 0` are honest
> seeds for this recurrence:
>
> ```python
> # python
> # LC 198 - House Robber (no edge cases)
> # time = O(n), space = O(1)
> def rob(nums):
>     p1, p2 = 0, 0                   # dp[i-2], dp[i-1]
>     for num in nums:
>         p1, p2 = p2, max(p2, p1 + num)
>     return p2
> ```
>
> Works because `max(0, 0 + nums[0]) = nums[0]` reproduces the seed the long version wrote by hand.
> Contrast with the Tribonacci seeding trap above: there, `dp[0] = 1` — the zero seed is *not*
> always the right one, so check it against the recurrence each time.

#### Seeding: the values must satisfy the recurrence

`dp[0]` is the usual trap. The *answer* for an empty bar is arguably `0`, but the *recurrence* needs `dp[0] = 1`, because `dp[3] = dp[2] + dp[1] + dp[0] = 2 + 1 + 1 = 4`. Use `0` only as an early-return for the caller, never as a seed:

```python
# python
if n == 0:
    return 0        # answer for the caller
p0 = 1              # seed for the recurrence — a different number on purpose
```

Rule of thumb: after seeding, hand-compute the **first** loop iteration and check it against a brute-force count. If `dp[4]` doesn't come out to `7`, the seeds are wrong, not the loop.

#### Generalizing to `k` steps back

For `dp[i] = sum(dp[i-1] … dp[i-k])`, named variables stop scaling. Two clean options:

```python
# python
# IDEA: k-step rolling window with a deque — O(k) space instead of O(n)
#       returns the recurrence value, so ways_k(0, k) == dp[0] == 1
# time = O(n), space = O(k)
from collections import deque

def ways_k(n, k):
    window = deque([1])            # dp[0] = 1
    total = 1                      # running sum of the window
    for i in range(1, n + 1):
        cur = total
        window.append(cur)
        total += cur
        if len(window) > k:        # drop dp[i-k], it's out of range now
            total -= window.popleft()
    return window[-1]
```

```python
# python
# IDEA: circular buffer — dp[i] lives at index i % k, no shifting at all
# time = O(n * k), space = O(k)
def ways_k_mod(n, k):
    dp = [0] * k
    dp[0] = 1                      # dp[0] = 1
    for i in range(1, n + 1):
        dp[i % k] = sum(dp[(i - j) % k] for j in range(1, min(k, i) + 1))
    return dp[n % k]
```

The `i % k` trick removes the update block entirely — nothing is shifted, the slot for `dp[i]` simply reuses the slot of `dp[i-k]`, which is exactly the value that just fell out of the window. Same idea powers the 1D-rolling-array optimization in 2D DP (`dp[i % 2][j]`).

#### Rolling-variable checklist

| Step | Question to ask |
|------|-----------------|
| **1. Depth** | How many steps back does the recurrence read? That's how many variables. |
| **2. Invariant** | Write `p1 = dp[i-k] … pk = dp[i-1]` as a comment above the loop. |
| **3. Seed** | Do the seeds satisfy the recurrence (not just the problem statement)? |
| **4. Start index** | Loop from the first `i` whose whole window is seeded (here `i = 4`). |
| **5. Update order** | Oldest → newest, or one tuple assignment. Never newest → oldest. |
| **6. Return** | Return the **newest rolling variable** (`p3` / `p2`), never the loop-body temp — the temp is unbound when the loop runs zero times. Make sure the `n < start` cases returned early. |

#### Same update pattern, other problems

| Problem | Recurrence | Vars |
|---------|------------|------|
| LC 70 Climbing Stairs | `dp[i] = dp[i-1] + dp[i-2]` | 2 |
| LC 1137 N-th Tribonacci | `dp[i] = dp[i-1] + dp[i-2] + dp[i-3]` | 3 |
| Candy bar (bite 1/2/3) | `dp[i] = dp[i-1] + dp[i-2] + dp[i-3]` | 3 |
| LC 198 House Robber | `dp[i] = max(dp[i-1], dp[i-2] + nums[i])` | 2 |
| LC 746 Min Cost Climbing Stairs | `dp[i] = min(dp[i-1], dp[i-2]) + cost[i]` | 2 |
| LC 91 Decode Ways | `dp[i] = dp[i-1]·ok1 + dp[i-2]·ok2` | 2 |
| Bite any of `k` sizes | `dp[i] = sum(dp[i-c] for c in sizes)` | `max(sizes)` |

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
| **Bounded** | dp[i][w] with a per-item count cap | Binary-split each item into 0/1 copies, then 0/1 knapsack | LC 2585 Ways to Earn Points |

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

### **Knapsack Loop Order & Deep Dive** → [knapsack.md](./knapsack.md)

Everything in the knapsack family is decided by two choices: **which loop is outer**, and **which
direction the inner loop runs**. Memorise this table; the reasoning behind every row is in
[knapsack.md](./knapsack.md).

| Goal | Outer loop | Inner loop | Recurrence | LC |
|------|-----------|------------|------------|----|
| **0/1** — each item at most once | items | capacity, **backward** | `dp[w] = max(dp[w], dp[w-wt] + val)` | 416, 494, 1049 |
| **Unbounded, count combinations** — `{1,2}` == `{2,1}` | items | amount, forward | `dp[a] += dp[a-coin]` | 518 |
| **Unbounded, count permutations** — `{1,2}` != `{2,1}` | amount | items | `dp[a] += dp[a-coin]` | 377 |
| **Unbounded, min/max** — order irrelevant | either | either | `dp[a] = min(dp[a], dp[a-coin] + 1)` | 322, 279 |

**Quick rule**: *cannot* reuse an item → 0/1, backward inner loop. *Can* reuse → unbounded, forward
inner loop; then ask whether order matters to pick the loop nesting.

[**knapsack.md**](./knapsack.md) covers, in full:

- **0/1 Knapsack & Subset Sum deep dive** — the partition reduction, the `(total + target) / 2`
  transform for LC 494, boolean vs counting vs max-value variants, and the common pitfalls.
- **Combinations vs Permutations** — visual summary of the four core loop orders, code templates
  per pattern, and the pattern-selection decision tree.
- **Combinations vs Permutations vs 0/1 Knapsack** — side-by-side code, a step-by-step trace of the
  same input under both loop orders, and a when-to-use-which table.
- **`if (i - coin >= 0)` in Coin Change** — why the guard is `>=` and not `==`.

---

### **String DP Patterns** → [dp_string.md](./dp_string.md)

Almost every two-string problem is the **same grid**: `dp[i][j]` = the answer for the first `i`
characters of `s1` and the first `j` characters of `s2`. Only the transition changes.

| Move | Meaning |
|------|---------|
| **Diagonal** `dp[i-1][j-1]` | consume a character from **both** strings (they matched) |
| **Vertical** `dp[i-1][j]` | skip / delete a character from `s1` |
| **Horizontal** `dp[i][j-1]` | skip / insert a character from `s2` |

| Problem | LC | On match | On mismatch |
|---------|----|----------|-------------|
| Longest Common Subsequence | 1143 | `1 + dp[i-1][j-1]` | `max(dp[i-1][j], dp[i][j-1])` |
| Edit Distance | 72 | `dp[i-1][j-1]` | `1 + min(replace, delete, insert)` |
| Distinct Subsequences | 115 | `dp[i-1][j-1] + dp[i-1][j]` | `dp[i-1][j]` |
| Interleaving String | 97 | `dp[i-1][j] && s1[i-1]==s3[i+j-1]` (or the `s2` side) | `False` |

The templates for LC 72 and LC 1143 stay above ([Template 7](#template-7-string-dp-edit-distance--levenshtein-distance--lc-72),
[Template 8](#template-8-longest-common-subsequence-lcs--lc-1143)).

[**dp_string.md**](./dp_string.md) covers, in full:

- **The two-string / two-sequence grid pattern** — full comparison table across LC 1143 / 72 / 115 /
  583 / 712 / 10 / 44.
- **Deep dive: prefix-based (1-indexed) indexing** — why the table is `dp[m+1][n+1]`, and the
  off-by-one bugs the 0-indexed version causes.
- **Interleaving String (LC 97)** — base cases, the 1-D space optimisation, and neighbours.
- **Valid Parenthesis String (LC 678)** — the wildcard state problem solved by DP, greedy range, and
  two-stack, side by side.

---

### **State Compression (Bitmask DP)** → [dp_bitmask.md](./dp_bitmask.md)

**When it applies**: the state is "which subset of items have I used", and `n <= 20` (so `2^n` masks
is tractable). `dp[mask]` = best answer having already handled exactly the items in `mask`.

```python
# python — the shape of every bitmask DP
# time = O(2^n * n), space = O(2^n)
for mask in range(1 << n):
    for i in range(n):
        if mask & (1 << i):                      # item i already used
            prev = mask ^ (1 << i)
            dp[mask] = best(dp[mask], dp[prev] + cost(prev, i))
```

| Bit op | Meaning |
|--------|---------|
| `mask & (1 << i)` | is item `i` in the set? |
| `mask \| (1 << i)` | add item `i` |
| `mask ^ (1 << i)` | remove item `i` (only when it is present) |
| `sub = (sub - 1) & mask` | enumerate submasks of `mask` |
| `bin(mask).count('1')` | popcount = how many items used |

[**dp_bitmask.md**](./dp_bitmask.md) covers, in full: the complete bit-operation reference, TSP and
assignment templates, submask-enumeration DP, complexity sizing, and the classic pitfalls
(`1L << n` for `n >= 31`, wrong submask loop, popcount-as-index).

---

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

---

### Template 12-0: Game Theory / Minimax DP (LC 486 Predict the Winner Style)

**Pattern**: Two players take turns picking from either end of an array, both play optimally. Determine if player 1 can win.

**Core Idea**: Define `dp[i][j]` as the **max score difference** (current player − opponent) on subarray `nums[i..j]`. When you pick `nums[i]`, the opponent then faces `dp[i+1][j]` — which is *their* best relative score. Subtracting it gives your net advantage.

**Why subtract?** After you pick, it's the opponent's turn. `dp[i+1][j]` is the opponent's max advantage on the remaining subarray. From your perspective, that advantage works against you, so you subtract it.

```java
// LC 486. Predict the Winner — 2D DP
public boolean predictTheWinner(int[] nums) {
    int n = nums.length;
    int[][] dp = new int[n][n];

    // Base case: single element → take it
    for (int i = 0; i < n; i++) {
        dp[i][i] = nums[i];
    }

    // Fill by increasing subarray length
    for (int len = 2; len <= n; len++) {
        for (int i = 0; i <= n - len; i++) {
            int j = i + len - 1;
            dp[i][j] = Math.max(
                nums[i] - dp[i + 1][j],   // pick left
                nums[j] - dp[i][j - 1]    // pick right
            );
        }
    }

    return dp[0][n - 1] >= 0;  // player 1 wins or ties
}
```

**Similar LeetCode Problems:**
| LC # | Problem | Notes |
|------|---------|-------|
| 486 | Predict the Winner | Core minimax interval DP |
| 877 | Stone Game | Same structure; always true for even-length (math proof) |
| 1140 | Stone Game II | Minimax + variable take count (suffix sum optimization) |
| 1406 | Stone Game III | 1D suffix minimax, pick 1-3 stones |
| 464 | Can I Win | Bitmask + minimax (state compression variant) |
| 294 | Flip Game II | Minimax with memoization |

---

### Template 12: Digit DP → [dp_digit.md](./dp_digit.md)

**When it applies**: "how many numbers in `[L, R]` satisfy some **digit-level** property?" — the
range is far too large to iterate, but the number has only ~18 digits.

**The three state variables** (memorise these; everything else is problem-specific):

| State | Meaning | Why it exists |
|-------|---------|---------------|
| `pos` | which digit position we are filling | the recursion index |
| `tight` | is the prefix still equal to the bound's prefix? | if yes, this digit is capped at `bound[pos]`; if no, `0..9` is free |
| `started` | have we placed a non-zero digit yet? | separates a real leading digit from padding zeros |

**Range trick**: `answer(L, R) = count(R) - count(L - 1)`.

```python
# python — digit DP skeleton
# time = O(len(digits) * states * 10), space = O(len(digits) * states)
@lru_cache(None)
def dfs(pos, tight, started, *extra):
    if pos == len(digits):
        return 1 if started else 0
    limit = digits[pos] if tight else 9
    total = 0
    for d in range(0, limit + 1):
        total += dfs(pos + 1, tight and d == limit, started or d > 0, *extra)
    return total
```

[**dp_digit.md**](./dp_digit.md) covers, in full: the universal template with commentary, LC 233
(count digit `1`), LC 902 (numbers built from a digit set), digit-sum and no-consecutive-digit
variants, pruning, and the memoisation-key pitfalls.

---

### Template 13: Weighted Interval Scheduling — DP + Binary Search ⭐⭐⭐⭐⭐ — LC 1235

> **Pattern**: items are *intervals* with a value; picking one forbids every interval that overlaps it. Sorting by **end time** turns "which items are still compatible?" into a **binary search** on a prefix of the DP array.

#### 🎯 Pattern Recognition

| Signal | Meaning |
|--------|---------|
| Input is `(start, end, value)` triples | Interval DP over *items*, not over ranges |
| "non-overlapping" / "cannot attend two at once" | Weighted interval scheduling |
| Values differ per interval | Greedy (activity selection) **fails** → must DP |
| n up to 5·10⁴ | O(n²) too slow → binary search the predecessor |

> ⚠️ Classic greedy "pick earliest finishing" only works when every interval is worth the same. With weights you must compare *take* vs *skip*.

#### 💡 Core Idea

```text
sort jobs by endTime
dp[i] = max profit using the first i jobs (sorted order)

take    : profit[i] + dp[p(i)]     where p(i) = # of jobs whose end <= start[i]
skip    : dp[i-1]
dp[i]   = max(take, skip)

p(i) is found by binary search over the (sorted) end times already in dp.
```

**Recurrence**: `dp[i] = max(dp[i-1], profit_i + dp[bisect_right(ends, start_i)])`

```java
// java
// LC 1235 - Maximum Profit in Job Scheduling
// IDEA: sort by end time; dp[i] = best profit among first i jobs;
//       binary search the last job that finishes at or before job i's start.
// time = O(n log n), space = O(n)
public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    int n = startTime.length;
    int[][] jobs = new int[n][3];
    for (int i = 0; i < n; i++) {
        jobs[i] = new int[]{endTime[i], startTime[i], profit[i]};
    }
    Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0])); // by end time

    int[] ends = new int[n + 1];   // ends[0] = 0  (sentinel "no job")
    int[] dp = new int[n + 1];     // dp[0]   = 0

    for (int i = 1; i <= n; i++) {
        int s = jobs[i - 1][1], p = jobs[i - 1][2];

        /** NOTE !!! binary search on ends[0..i-1] :
         *  largest idx with ends[idx] <= s  ->  dp[idx] is the compatible prefix
         */
        int lo = 0, hi = i - 1, idx = 0;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (ends[mid] <= s) { idx = mid; lo = mid + 1; }
            else { hi = mid - 1; }
        }

        dp[i] = Math.max(dp[i - 1], dp[idx] + p);  // skip vs take
        ends[i] = jobs[i - 1][0];
    }
    return dp[n];
}
```

```python
# python
# LC 1235 - Maximum Profit in Job Scheduling
# IDEA: sort by end time, dp is non-decreasing, so bisect over the end-time list
# time = O(n log n), space = O(n)
import bisect

def jobScheduling(startTime, endTime, profit):
    jobs = sorted(zip(endTime, startTime, profit))   # sort by end time
    ends = [0]      # sentinel: "no job taken"
    dp = [0]

    for e, s, p in jobs:
        # last job whose end <= s  (compatible predecessor)
        i = bisect.bisect_right(ends, s) - 1
        dp.append(max(dp[-1], dp[i] + p))            # skip vs take
        ends.append(e)

    return dp[-1]
```

**Why `dp` stays sorted**: `dp[i] = max(dp[i-1], ...) >= dp[i-1]`, so the prefix maximum *is* `dp[i]` — no extra running-max needed.

#### Variation: extra "budget" dimension — LC 1751

> **Twist**: same sort + binary search, but you may attend **at most `k`** events → add a second state dimension `k`. Also note events are *inclusive day ranges*, so the predecessor must end **strictly before** the current start.

```python
# python
# LC 1751 - Maximum Number of Events That Can Be Attended II
# IDEA: LC 1235 + a "how many picks left" dimension; dp[i][t] = best value using
#       first i events (sorted by end) while attending at most t of them.
# time = O(n * k * log n), space = O(n * k)
import bisect

def maxValue(events, k):
    events.sort(key=lambda e: e[1])                   # by end day
    n = len(events)
    ends = [0] + [e[1] for e in events]
    dp = [[0] * (k + 1) for _ in range(n + 1)]

    for i in range(1, n + 1):
        s, e, v = events[i - 1]
        # last event ending strictly before s (days are inclusive)
        j = bisect.bisect_right(ends, s - 1, 0, i) - 1
        for t in range(1, k + 1):
            dp[i][t] = max(dp[i - 1][t],              # skip event i
                           dp[j][t - 1] + v)          # take event i
    return dp[n][k]
```

#### Similar LeetCode Problems 📚

| Problem | LC # | Twist |
|---------|------|-------|
| Maximum Profit in Job Scheduling | 1235 | Base pattern |
| Maximum Number of Events That Can Be Attended II | 1751 | + "at most k picks" dimension |
| Russian Doll Envelopes | 354 | Same "sort then DP on a prefix", but LIS instead of intervals |

---

### Template 14: Partition Array into K Contiguous Groups ⭐⭐⭐⭐ — LC 1335

> **Pattern**: split an array into exactly `k` **contiguous** blocks and optimise a cost that is `sum / max` over blocks. Distinct from Interval DP (Template 3): here the split points are the decision, and the blocks must cover the array left to right.

#### 🎯 Pattern Recognition

- "divide into `d` days / `k` subarrays / `m` segments"
- Order of elements is fixed (no reordering, no skipping)
- Cost of a block is computable incrementally while scanning (`max`, prefix sum)

#### 💡 Core Idea

```text
dp[k][i] = best cost to cover jobs[i:] using exactly k blocks

dp[k][i] = min over j in [i, n-k] of ( cost(i..j) + dp[k-1][j+1] )
                                       ^ this block   ^ the rest

base: dp[0][n] = 0, dp[0][i<n] = INF   (0 blocks must consume 0 jobs)
answer: dp[d][0];  impossible when n < d
```

**Key trick**: extend `j` outward and maintain `cost(i..j)` in O(1) (running `max` here, running prefix sum for LC 410) — that keeps the transition O(n) instead of O(n²).

```java
// java
// LC 1335 - Minimum Difficulty of a Job Schedule
// IDEA: dp[day][i] = min total difficulty to finish jobs[i:] in `day` days;
//       inner loop grows the current day's block, tracking its running max.
// time = O(d * n^2), space = O(n)  (rolling over the day dimension)
public int minDifficulty(int[] jobDifficulty, int d) {
    int n = jobDifficulty.length;
    if (n < d) return -1;                 // not enough jobs: each day needs >= 1

    final int INF = Integer.MAX_VALUE / 2;
    int[] dp = new int[n + 1];
    Arrays.fill(dp, INF);
    dp[n] = 0;                            // 0 jobs left with 0 days left

    for (int day = 1; day <= d; day++) {
        int[] ndp = new int[n + 1];
        Arrays.fill(ndp, INF);
        // i can start at most at n-day, leaving >= 1 job per remaining day
        for (int i = 0; i <= n - day; i++) {
            int mx = 0;
            for (int j = i; j <= n - day; j++) {   // today handles jobs[i..j]
                mx = Math.max(mx, jobDifficulty[j]);
                ndp[i] = Math.min(ndp[i], mx + dp[j + 1]);
            }
        }
        dp = ndp;
    }
    return dp[0];
}
```

```python
# python
# LC 1335 - Minimum Difficulty of a Job Schedule
# IDEA: same recurrence, rolling 1D array over the "day" dimension
# time = O(d * n^2), space = O(n)
def minDifficulty(jobDifficulty, d):
    n = len(jobDifficulty)
    if n < d:
        return -1

    INF = float('inf')
    dp = [INF] * (n + 1)
    dp[n] = 0                                   # 0 jobs left, 0 days left

    for day in range(1, d + 1):
        ndp = [INF] * (n + 1)
        for i in range(n - day + 1):            # start of today's block
            mx = 0
            for j in range(i, n - day + 1):     # end of today's block
                mx = max(mx, jobDifficulty[j])
                if dp[j + 1] < INF:
                    ndp[i] = min(ndp[i], mx + dp[j + 1])
        dp = ndp

    return dp[0]
```

#### Variation: minimise the maximum block — LC 410

> **Twist**: identical partition skeleton, but the objective is `min over splits of (max block sum)` → the transition combines with `max` instead of `+`. (LC 410 also has the famous O(n log S) *binary-search-on-the-answer* solution; the DP below is what interviewers ask you to derive first.)

```python
# python
# LC 410 - Split Array Largest Sum
# IDEA: dp[t][i] = min possible "largest subarray sum" when splitting nums[:i] into t parts
# time = O(k * n^2), space = O(k * n)
def splitArray(nums, k):
    n = len(nums)
    pre = [0] * (n + 1)
    for i, v in enumerate(nums):
        pre[i + 1] = pre[i] + v

    INF = float('inf')
    dp = [[INF] * (n + 1) for _ in range(k + 1)]
    dp[0][0] = 0

    for t in range(1, k + 1):
        for i in range(1, n + 1):
            for j in range(t - 1, i):           # last part = nums[j:i]
                dp[t][i] = min(dp[t][i], max(dp[t - 1][j], pre[i] - pre[j]))

    return dp[k][n]
```

| Problem | LC # | Block cost | Combine |
|---------|------|-----------|---------|
| Minimum Difficulty of a Job Schedule | 1335 | `max` of block | `+` across blocks |
| Split Array Largest Sum | 410 | `sum` of block | `max` across blocks |

---

### Template 15: DP with an Extra "Last Move" State Dimension ⭐⭐⭐⭐⭐ — LC 403

> **Pattern**: position alone is **not** a valid state — what you can do next depends on *how you got here*. Add the last transition to the state: `dp[position][lastMove]`. Whenever a naive `dp[i]` gives wrong answers because "the same cell is reachable in different ways with different futures", this is the fix.

#### 🎯 Pattern Recognition

| Signal | Extra dimension to add |
|--------|------------------------|
| "next jump must be k-1, k or k+1" | last jump size |
| "cannot use the same direction twice" | last direction |
| "at most 2 in a row" | run length so far |
| "cooldown after selling" | last action (see Template 5-2) |

#### 💡 Core Idea (LC 403 Frog Jump)

```text
state  : (stone index i, jump size k that landed on i)
init   : (0, 0)
move   : from (i, k) you may jump k-1, k or k+1 (must be > 0)
         land on stone at position stones[i] + nk  ->  state (j, nk)
answer : any state on the last stone is reachable

dp[i] = set of jump sizes that can land ON stone i
```

Because jumps only move **forward** (`j > i`), a single left-to-right sweep is enough — no recursion needed.

```java
// java
// LC 403 - Frog Jump
// IDEA: dp.get(i) = set of jump sizes that can land on stone i;
//       push forward to stones[i] + (k-1 | k | k+1) via a position -> index map.
// time = O(n^2), space = O(n^2)
public boolean canCross(int[] stones) {
    int n = stones.length;
    Map<Integer, Integer> pos = new HashMap<>();   // stone position -> index
    for (int i = 0; i < n; i++) pos.put(stones[i], i);

    List<Set<Integer>> dp = new ArrayList<>();
    for (int i = 0; i < n; i++) dp.add(new HashSet<>());
    dp.get(0).add(0);                              // start: landed with jump 0

    for (int i = 0; i < n; i++) {
        for (int k : dp.get(i)) {
            for (int nk = k - 1; nk <= k + 1; nk++) {
                if (nk <= 0) continue;             // jump must move forward
                Integer j = pos.get(stones[i] + nk);
                /** NOTE !!! j > i guarantees we only write to FUTURE stones,
                 *  so mutating dp while scanning is safe. */
                if (j != null && j > i) dp.get(j).add(nk);
            }
        }
    }
    return !dp.get(n - 1).isEmpty();
}
```

```python
# python
# LC 403 - Frog Jump
# IDEA: dp[i] = set of jump sizes that can land on stone i (forward propagation)
# time = O(n^2), space = O(n^2)
def canCross(stones):
    n = len(stones)
    pos = {s: i for i, s in enumerate(stones)}     # position -> index
    dp = [set() for _ in range(n)]
    dp[0].add(0)                                   # start with jump size 0

    for i in range(n):
        for k in dp[i]:
            for nk in (k - 1, k, k + 1):
                if nk <= 0:
                    continue
                j = pos.get(stones[i] + nk)
                if j is not None and j > i:        # only forward
                    dp[j].add(nk)

    return len(dp[n - 1]) > 0
```

#### Common Pitfalls ⚠️

- **Forgetting `nk > 0`** — a jump of 0 (or negative) would loop forever on the same stone.
- **Using `dp[i] = boolean`** — reachability alone loses the jump size and gives wrong answers (e.g. `[0,1,3,6,10,13,14]`).
- **Not deduplicating** — use a `Set` per stone, otherwise the state space blows up.
- Top-down `memo[(i, k)] -> boolean` with DFS is the equivalent formulation; same complexity.

---

### Template 16: Step-Indexed Counting / Probability DP ⭐⭐⭐⭐ — LC 935

> **Pattern**: a **small state graph** (10 phone keys, an n×n board, a 1D array) plus a **fixed number of moves**. Answer = "how many ways / with what probability am I at each state after `t` steps". The DP layer is the step count, so you always roll one layer at a time.

#### 💡 Core Idea

```text
dp[t][v] = ways (or probability) to be at state v after t steps
dp[t][v] = sum over u with edge u -> v of dp[t-1][u]

counting     -> take everything mod 1e9+7
probability  -> divide each contribution by the out-degree
```

Only `dp[t-1]` is needed → keep two arrays (`dp`, `ndp`) instead of a `steps × V` table.

```java
// java
// LC 935 - Knight Dialer
// IDEA: dp[d] = # of distinct numbers of current length ending on digit d;
//       one layer per additional dialed digit.
// time = O(n * 10 * 3) = O(n), space = O(10) = O(1)
public int knightDialer(int n) {
    final int MOD = 1_000_000_007;
    // knight moves on the phone pad (5 is unreachable)
    int[][] moves = {{4,6},{6,8},{7,9},{4,8},{0,3,9},{},{0,1,7},{2,6},{1,3},{2,4}};

    long[] dp = new long[10];
    Arrays.fill(dp, 1);                 // length-1 numbers: each digit once

    for (int step = 1; step < n; step++) {
        long[] ndp = new long[10];
        for (int d = 0; d < 10; d++) {
            for (int nxt : moves[d]) {
                ndp[nxt] = (ndp[nxt] + dp[d]) % MOD;
            }
        }
        dp = ndp;                       // roll the layer
    }

    long res = 0;
    for (long v : dp) res = (res + v) % MOD;
    return (int) res;
}
```

```python
# python
# LC 935 - Knight Dialer
# IDEA: same layer-rolling; MOVES is the knight-move adjacency of the keypad
# time = O(n), space = O(1)
MOD = 10 ** 9 + 7
MOVES = {0: [4, 6], 1: [6, 8], 2: [7, 9], 3: [4, 8], 4: [0, 3, 9],
         5: [],     6: [0, 1, 7], 7: [2, 6], 8: [1, 3], 9: [2, 4]}

def knightDialer(n):
    dp = [1] * 10                       # numbers of length 1
    for _ in range(n - 1):
        ndp = [0] * 10
        for d in range(10):
            for nxt in MOVES[d]:
                ndp[nxt] = (ndp[nxt] + dp[d]) % MOD
        dp = ndp
    return sum(dp) % MOD
```

#### Variation: probability instead of count — LC 688

> **Twist**: states are board cells, each move picks 1 of 8 directions uniformly, and moves off the board are *lost* — so the layer sums decay. Answer = sum of the final layer.

```python
# python
# LC 688 - Knight Probability in Chessboard
# IDEA: dp[r][c] = probability of standing on (r,c) after t moves; spread /8 each step
# time = O(k * n^2 * 8), space = O(n^2)
def knightProbability(n, k, row, column):
    dirs = [(1,2),(2,1),(-1,2),(-2,1),(1,-2),(2,-1),(-1,-2),(-2,-1)]
    dp = [[0.0] * n for _ in range(n)]
    dp[row][column] = 1.0

    for _ in range(k):
        ndp = [[0.0] * n for _ in range(n)]
        for r in range(n):
            for c in range(n):
                if dp[r][c] == 0.0:
                    continue
                for dr, dc in dirs:
                    nr, nc = r + dr, c + dc
                    if 0 <= nr < n and 0 <= nc < n:   # off-board = fell off
                        ndp[nr][nc] += dp[r][c] / 8.0
        dp = ndp

    return sum(map(sum, dp))
```

#### Variation: bound the reachable state space — LC 1269

> **Twist**: `arrLen` can be 10⁶ but with only `steps` moves you can never pass index `steps // 2` (you must walk back). Clamping the state space to `min(arrLen, steps // 2 + 1)` is what makes this pass.

```python
# python
# LC 1269 - Number of Ways to Stay in the Same Place After Some Steps
# IDEA: dp[i] = ways to be at index i; each step move left / stay / right
# time = O(steps * min(arrLen, steps/2)), space = O(min(arrLen, steps/2))
def numWays(steps, arrLen):
    MOD = 10 ** 9 + 7
    m = min(arrLen, steps // 2 + 1)     # unreachable indices pruned away
    dp = [0] * m
    dp[0] = 1

    for _ in range(steps):
        ndp = [0] * m
        for i in range(m):
            if dp[i]:
                for j in (i - 1, i, i + 1):
                    if 0 <= j < m:
                        ndp[j] = (ndp[j] + dp[i]) % MOD
        dp = ndp

    return dp[0]                        # must end back at index 0
```

#### Pattern Recognition Checklist ✅

- [ ] Fixed number of steps / rounds given as input (`n`, `k`, `steps`)
- [ ] State space is small and the transition graph is fixed
- [ ] Question asks "how many ways" (mod 1e9+7) or "with what probability"
- [ ] → roll one layer per step; O(1)-ish space in the state dimension

---

### Other High-Frequency DP Problems (quick reference)

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

---

## Category 9: Monotonic Stack + DP → [dp_monotonic_stack.md](./dp_monotonic_stack.md)

> Categories 1-8 are summarised under [Problem Categories](#problem-categories). This one lives in
> its own file because its templates are long.

**Signal**: a brute-force that **removes elements round by round** (each element is deleted once a
bigger element reaches it), or an area/rectangle question over a histogram. Both are O(n^2) if
simulated and O(n) with a monotonic stack that carries a DP value.

| Problem | LC | What the stack carries |
|---------|----|------------------------|
| Steps to Make Array Non-decreasing | 2289 | `dp[i]` = rounds element `i` survives; a popped chain contributes `max(...)` |
| Largest Rectangle in Histogram | 84 | index of the previous smaller bar → width of the rectangle ending here |
| Maximal Rectangle | 85 | LC 84 applied row by row over a running histogram |
| Maximal Square / Count Square Submatrices | 221 / 1277 | `dp[i][j] = 1 + min(up, left, up-left)` (grid DP, not a stack) |
| Flip String to Monotone Increasing | 926 | one-pass counter DP |

[**dp_monotonic_stack.md**](./dp_monotonic_stack.md) covers, in full: the survival-round transition
with a worked trace, the LC 2289 template in Java and Python, the histogram area DP, the maximal
square recurrence, and the LC 926 one-pass DP.

---

**Keywords**: DP, dynamic programming, memoization, tabulation, optimal substructure, overlapping subproblems, state transition, knapsack, LCS, LIS, interval DP, tree DP, state machine, bitmask, monotonic stack, mono stack, stack DP

---

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

## LC Examples

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

### 2-7) Longest Increasing Subsequence (LC 300) — LIS Binary Search / DP

> O(n log n): maintain tails array with binary search; tails[i] = smallest tail of LIS of length i+1.

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
```text
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

### 2-8) Sum of Distances in Tree (LC 834) — Re-rooting DP

> Two-pass DFS: compute root distances, then re-root by adjusting parent answer by ±subtree size.

```java
// java
// LC 834
// Reference: leetcode_java/src/main/java/LeetCodeJava/Tree/SumOfDistancesInTree.java

/**
 * Problem: Given an undirected tree with n nodes, return an array where
 * answer[i] = sum of distances between node i and all other nodes.
 *
 * Example:
 * Input: n = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 * Output: [8,12,6,10,10,10]
 *
 * WHY RE-ROOTING DP?
 *
 * A naive BFS/DFS from every node → O(N²), TLE for N = 3×10⁴.
 * Re-rooting DP solves it in O(N) with two DFS passes.
 *
 * KEY INSIGHT (Re-rooting Formula):
 *
 *   When moving root from node u to its child v:
 *     - count[v] nodes get 1 unit CLOSER  (they are in v's subtree)
 *     - (N - count[v]) nodes get 1 unit FARTHER (they are outside v's subtree)
 *
 *   Therefore:
 *     ans[v] = ans[u] - count[v] + (N - count[v])
 *
 * ALGORITHM:
 *
 *   Pass 1 — Post-order DFS (bottom-up):
 *     For each node u, compute:
 *       count[u] = size of u's subtree (including u)
 *       ans[u]   = sum of distances from u to all nodes in u's subtree
 *     After this pass, ans[root] is correct (total distance from root to all nodes).
 *
 *   Pass 2 — Pre-order DFS (top-down, re-root):
 *     For each edge u→v, compute ans[v] from ans[u] using the formula above.
 *     This propagates the correct answer to every node.
 *
 * Time:  O(N) — two DFS passes
 * Space: O(N) — adjacency list + count[] + ans[]
 */

// V0-1: Re-rooting DP (clean implementation)
int[] ans;
int[] count;
List<Set<Integer>> adj;
int n;

public int[] sumOfDistancesInTree(int n, int[][] edges) {
    this.n = n;
    ans = new int[n];
    count = new int[n];
    adj = new ArrayList<>();

    for (int i = 0; i < n; i++)
        adj.add(new HashSet<>());
    for (int[] e : edges) {
        adj.get(e[0]).add(e[1]);
        adj.get(e[1]).add(e[0]);
    }

    // Pass 1: Post-order DFS → compute count[] and ans[0]
    dfs1(0, -1);

    // Pass 2: Pre-order DFS → re-root to compute all ans[i]
    dfs2(0, -1);

    return ans;
}

// Post-order: count subtree sizes, accumulate distances for root
private void dfs1(int u, int parent) {
    count[u] = 1;
    for (int v : adj.get(u)) {
        if (v == parent) continue;
        dfs1(v, u);
        count[u] += count[v];
        // Distance from u to all nodes in v's subtree
        // = (dist from v to its subtree) + (number of nodes in v's subtree)
        ans[u] += ans[v] + count[v];
    }
}

// Pre-order: shift root from parent u to child v
private void dfs2(int u, int parent) {
    for (int v : adj.get(u)) {
        if (v == parent) continue;
        // Re-rooting formula:
        // count[v] nodes get closer, (n - count[v]) nodes get farther
        ans[v] = ans[u] - count[v] + (n - count[v]);
        dfs2(v, u);
    }
}

/**
 * STEP-BY-STEP EXAMPLE:
 *
 *        0
 *       / \
 *      1   2
 *         /|\
 *        3  4  5
 *
 * n = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 *
 * Pass 1 (Post-order, root=0):
 *   count = [6, 1, 4, 1, 1, 1]
 *   ans   = [8, 0, 3, 0, 0, 0]   ← only ans[0]=8 is the full answer
 *
 *   ans[2] = ans[3]+count[3] + ans[4]+count[4] + ans[5]+count[5]
 *          = 0+1 + 0+1 + 0+1 = 3
 *   ans[0] = ans[1]+count[1] + ans[2]+count[2]
 *          = 0+1 + 3+4 = 8 ✓ (matches expected output)
 *
 * Pass 2 (Pre-order, re-root):
 *   ans[1] = ans[0] - count[1] + (6 - count[1]) = 8 - 1 + 5 = 12 ✓
 *   ans[2] = ans[0] - count[2] + (6 - count[2]) = 8 - 4 + 2 = 6  ✓
 *   ans[3] = ans[2] - count[3] + (6 - count[3]) = 6 - 1 + 5 = 10 ✓
 *   ans[4] = ans[2] - count[4] + (6 - count[4]) = 6 - 1 + 5 = 10 ✓
 *   ans[5] = ans[2] - count[5] + (6 - count[5]) = 6 - 1 + 5 = 10 ✓
 *
 * Final: [8, 12, 6, 10, 10, 10] ✓
 */
```

```python
# python
# LC 834 Sum of Distances in Tree
# Re-rooting DP template

# V0
# IDEA: Re-rooting DP (two-pass DFS)
class Solution:
    def sumOfDistancesInTree(self, n, edges):
        adj = [[] for _ in range(n)]
        for u, v in edges:
            adj[u].append(v)
            adj[v].append(u)

        count = [1] * n  # subtree size
        ans = [0] * n

        # Pass 1: Post-order DFS (iterative to avoid recursion limit)
        # Compute count[] and ans[0]
        order = []
        visited = [False] * n
        parent = [-1] * n
        stack = [0]
        visited[0] = True
        while stack:
            u = stack.pop()
            order.append(u)
            for v in adj[u]:
                if not visited[v]:
                    visited[v] = True
                    parent[v] = u
                    stack.append(v)

        # Process in reverse order (post-order)
        for u in reversed(order):
            for v in adj[u]:
                if v == parent[u]:
                    continue
                count[u] += count[v]
                ans[u] += ans[v] + count[v]

        # Pass 2: Pre-order DFS (re-root)
        for u in order:
            for v in adj[u]:
                if v == parent[u]:
                    continue
                ans[v] = ans[u] - count[v] + (n - count[v])

        return ans
```

#### Re-rooting DP Template (General)

```java
/**
 * RE-ROOTING DP TEMPLATE
 *
 * Use when: "compute some aggregate for EVERY node as root" on a tree
 *
 * Pattern:
 *   1. Post-order DFS: compute answer for one fixed root (node 0)
 *   2. Pre-order DFS: re-root from parent → child using a transition formula
 *
 * The transition formula depends on the problem:
 *   LC 834: ans[v] = ans[u] - count[v] + (n - count[v])
 *   General: ans[child] = f(ans[parent], subtree_info[child], n)
 *
 * Time:  O(N)
 * Space: O(N)
 *
 * SIMILAR PROBLEMS:
 * | Problem                                  | LC #  | Re-rooting Formula / Key Idea                    |
 * |------------------------------------------|-------|--------------------------------------------------|
 * | Sum of Distances in Tree                 | 834   | ans[v] = ans[u] - count[v] + (n - count[v])     |
 * | Count Number of Possible Root Nodes      | 2581  | Track "good" edges, adjust count when re-rooting |
 * | Minimum Edge Weight Equilibrium Queries   | 2846  | Re-root with edge frequency tracking             |
 * | Sum of Prefix Scores of Strings (on Trie)| 2416  | Similar two-pass idea on trie structure           |
 *
 * WHEN TO SUSPECT RE-ROOTING:
 * - "For every node, compute ..." on a tree
 * - Naive per-node DFS/BFS gives O(N²) → need O(N)
 * - Answer for child can be derived from parent's answer
 */
```

### 2-9) Perfect Squares (LC 279) — Unbounded Knapsack (Min Count)

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

### 2-10) Integer Break (LC 343) — Linear DP (Break vs No-Break)

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

### 2-11) Paint Fence (LC 276) — Two-State DP (same / different color)

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

## Decision Framework

### Pattern Selection Strategy

```text
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

### Quick Decision Tree: Which DP Pattern to Use?

#### Decision Flowchart

```text
START: What type of problem are you solving?
│
├─ Working with a SINGLE SEQUENCE/ARRAY?
│  │
│  ├─ Linear dependencies (dp[i] from dp[i-1]) → Category 1 (Linear DP)
│  │                                               Examples: LC 70, 198, 300
│  │
│  └─ Selection with constraints → Category 6 (Knapsack DP)
│                                    Examples: LC 416, 494, 518
│
├─ Working with a 2D GRID/MATRIX?
│  │
│  └─ Path counting, min/max path → Category 2 (Grid/2D DP)
│                                     Examples: LC 62, 64, 221
│
├─ Working with INTERVALS/SUBARRAYS?
│  │
│  └─ Optimal split, merge, or partition → Category 3 (Interval DP)
│                                           Examples: LC 312, 1000, 516
│
├─ Working with TREE structures?
│  │
│  ├─ State at node depends on children → Category 4 (Tree DP)
│  │                                       Examples: LC 337, 968, 124
│  │
│  └─ "For every node, compute ..." → Re-rooting DP (two-pass DFS)
│                                       Examples: LC 834, 2581
│
├─ Working with STRINGS?
│  │
│  ├─ Two strings (matching/alignment) → Category 7 (String DP)
│  │                                      Examples: LC 72, 1143, 583
│  │
│  └─ Single string (palindrome, split) → Also Category 7
│                                          Examples: LC 5, 131, 647
│
├─ Problem has MULTIPLE STATES with transitions?
│  │
│  └─ Stock trading, state machines → Category 5 (State Machine DP)
│                                      Examples: LC 122, 309, 714
│
└─ Need to track SUBSET/VISITED items efficiently?
   │
   └─ Use bitmask to compress state → Category 8 (State Compression DP)
                                       Examples: LC 691, 847, 1723
```

#### Quick Pattern Selection Table

| Problem Type | Recognition Keywords | DP Category | Example Problems |
|--------------|---------------------|-------------|------------------|
| **Fibonacci-like** | "nth number", "climbing stairs", "decode ways" | Linear DP | LC 70, 91, 746 |
| **House Robber** | "non-adjacent", "cannot pick consecutive" | Linear DP | LC 198, 213, 337 |
| **Longest Increasing** | "longest increasing", "LIS", "envelope" | Linear DP | LC 300, 354, 673 |
| **Path Counting** | "unique paths", "number of ways to reach" | Grid DP | LC 62, 63, 980 |
| **Path Sum (Min/Max)** | "minimum path sum", "maximum sum" | Grid DP | LC 64, 120, 174 |
| **Square/Rectangle** | "maximal square", "largest rectangle" | Grid DP | LC 221, 85 |
| **Interval Problems** | "burst balloons", "merge stones", "palindrome partition" | Interval DP | LC 312, 1000, 516 |
| **Tree Problems** | "house robber on tree", "tree cameras" | Tree DP | LC 337, 968 |
| **Tree Re-rooting** | "for every node compute", "sum of distances" | Re-rooting DP | LC 834, 2581 |
| **Stock Trading** | "buy and sell stock", "transaction", "cooldown" | State Machine | LC 122, 309, 714 |
| **Knapsack (0/1)** | "subset sum", "partition", "target sum" | Knapsack DP | LC 416, 494 |
| **Knapsack (Unbounded)** | "coin change", "unlimited supply" | Knapsack DP | LC 322, 518 |
| **Edit Distance** | "edit distance", "minimum operations" | String DP | LC 72, 583, 712 |
| **LCS/LPS** | "longest common subsequence", "palindrome" | String DP | LC 1143, 516, 647 |
| **Bitmask/Subset** | "visit all nodes", "assign tasks", "TSP" | State Compression | LC 847, 1723, 691 |

#### Recognition Patterns by Keywords

**Linear sequence keywords** → Category 1 (Linear DP)
- "nth Fibonacci", "climbing stairs", "decode ways"
- "house robber", "non-adjacent", "skip adjacent"
- "longest increasing subsequence", "LIS"

**Grid/Matrix keywords** → Category 2 (Grid/2D DP)
- "grid", "matrix", "m x n"
- "unique paths", "number of ways"
- "minimum/maximum path sum"
- "maximal square", "largest rectangle"

**Interval/Subarray keywords** → Category 3 (Interval DP)
- "burst", "merge", "split", "partition"
- "optimal way to cut/divide"
- "minimum cost to merge"
- "palindrome partitioning"

**Tree keywords** → Category 4 (Tree DP)
- "binary tree", "tree structure"
- "each node", "children", "parent"
- "rob houses on tree", "cameras on tree"
- "for every node compute", "sum of distances" → Re-rooting DP (LC 834)

**State transition keywords** → Category 5 (State Machine DP)
- "buy and sell stock"
- "cooldown", "transaction fee"
- "at most k transactions"
- "multiple states"

**Selection with constraints** → Category 6 (Knapsack DP)
- "subset sum", "partition equal"
- "target sum", "combination sum"
- "0/1 knapsack", "unbounded knapsack"
- "coin change", "unlimited supply"

**String matching keywords** → Category 7 (String DP)
- "edit distance", "minimum operations"
- "longest common subsequence (LCS)"
- "palindrome subsequence/substring"
- "string transformation"

**Subset/Visited tracking** → Category 8 (State Compression DP)
- "visit all nodes", "shortest path visiting all"
- "assign tasks", "match workers"
- "traveling salesman problem (TSP)"
- "subset enumeration with constraints"

#### Quick Decision Examples

1. **"Find minimum path sum in a grid"**
   - Keywords: "grid", "minimum path sum"
   - Decision: Category 2 (Grid/2D DP)
   - Template: dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + grid[i][j]

2. **"Count ways to make change with coins"**
   - Keywords: "coin change", "unlimited supply"
   - Decision: Category 6 (Unbounded Knapsack DP)
   - Template: dp[amount] = sum of dp[amount - coin]

3. **"Find edit distance between two strings"**
   - Keywords: "edit distance", "two strings"
   - Decision: Category 7 (String DP)
   - Template: dp[i][j] with insert/delete/replace operations

4. **"Buy and sell stock with cooldown"**
   - Keywords: "stock", "cooldown"
   - Decision: Category 5 (State Machine DP)
   - Template: 3 states (hold, sold, rest)

5. **"Shortest path visiting all nodes in graph"**
   - Keywords: "visit all nodes", "shortest path"
   - Decision: Category 8 (State Compression DP)
   - Template: dp[mask][node] with bitmask for visited nodes

6. **"Burst balloons to maximize coins"**
   - Keywords: "burst", "maximize"
   - Decision: Category 3 (Interval DP)
   - Template: dp[i][j] for interval [i, j]

7. **"Rob houses on a binary tree"**
   - Keywords: "tree", "rob", "non-adjacent"
   - Decision: Category 4 (Tree DP)
   - Template: Bottom-up DFS with two states per node

#### Pro Tips for Pattern Selection

- **One sequence** → Linear DP (Category 1)
- **Two sequences** → Usually String DP (Category 7) or 2D DP
- **Grid movement** → Grid DP (Category 2)
- **Interval splitting** → Interval DP (Category 3) - often O(n³)
- **Tree traversal** → Tree DP (Category 4) - use DFS
- **Multiple states** → State Machine (Category 5) - draw state diagram first
- **Weight/capacity constraint** → Knapsack (Category 6)
- **String matching/transform** → String DP (Category 7)
- **Visit all/subset** → State Compression (Category 8) - use bitmask

#### Common Pitfalls

- **Interval DP**: Remember to iterate length from small to large
- **Knapsack**: 0/1 requires reverse iteration for space optimization
- **State Machine**: Draw state transition diagram before coding
- **Tree DP**: Use bottom-up DFS (postorder traversal); for "every node as root" problems, use Re-rooting DP (two-pass DFS, LC 834)
- **State Compression**: Check if n ≤ 20 (2^20 states is feasible)
- **String DP**: Define dp[i][j] carefully (length vs index)

---

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
```text
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

---


## Advanced DP Techniques — Monotonic Queue, Re-rooting, Stack DP

### DP with Monotonic Queue Optimization — LC 1425
When `dp[i] = max(dp[j]) + f(i)` for `j` in a sliding window `[i-k, i-1]`, use monotonic deque to reduce O(n²) → O(n).

```python
from collections import deque

# LC 1425 Constrained Subsequence Sum
def constrainedSubsetSum(nums, k):
    n = len(nums)
    dp = nums[:]       # dp[i] = max sum of subsequence ending at i
    dq = deque()       # decreasing deque of indices by dp value

    for i in range(n):
        # Best previous dp in window [i-k, i-1]
        if dq and dp[dq[0]] > 0:
            dp[i] = max(dp[i], dp[dq[0]] + nums[i])
        # Maintain decreasing order
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        dq.append(i)
        # Remove elements outside window
        if dq[0] == i - k:
            dq.popleft()

    return max(dp)
```

### Re-rooting DP — LC 834 (Sum of Distances in Tree)
Compute answer for every node as root in O(n) using two DFS passes instead of O(n²).

```python
def sumOfDistancesInTree(n, edges):
    from collections import defaultdict
    graph = defaultdict(set)
    for u, v in edges:
        graph[u].add(v); graph[v].add(u)

    count = [1] * n    # subtree size rooted at node (1st DFS)
    ans = [0] * n      # answer for root=0 first, then re-root

    # Pass 1: DFS from root=0, compute count[] and ans[0]
    def dfs1(node, parent):
        for child in graph[node]:
            if child != parent:
                dfs1(child, node)
                count[node] += count[child]
                ans[0] += count[child]   # each node in subtree is 1 farther from root
    dfs1(0, -1)

    # Pass 2: re-root — when moving root from parent to child:
    # ans[child] = ans[parent] - count[child] + (n - count[child])
    def dfs2(node, parent):
        for child in graph[node]:
            if child != parent:
                ans[child] = ans[node] - count[child] + (n - count[child])
                dfs2(child, node)
    dfs2(0, -1)
    return ans
```

### Largest Rectangle in Histogram — LC 84 (Stack DP)
Use monotonic stack to find the largest rectangle area in O(n).

```python
def largestRectangleArea(heights):
    stack = []   # increasing stack of indices
    max_area = 0
    heights.append(0)   # sentinel to flush stack

    for i, h in enumerate(heights):
        while stack and heights[stack[-1]] >= h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)
    return max_area

# LC 85 Maximal Rectangle in Matrix — apply LC 84 row by row
def maximalRectangle(matrix):
    if not matrix: return 0
    n = len(matrix[0])
    heights = [0] * n
    ans = 0
    for row in matrix:
        heights = [heights[j] + int(row[j]) if row[j] != '0' else 0 for j in range(n)]
        ans = max(ans, largestRectangleArea(heights[:]))
    return ans
```

### State Machine DP Quick Reference
```text
Stock Problems State Transitions:
  held    = max(held,      rest - price)    # buy: rest → held
  sold    = held + price                    # sell: held → sold
  rest    = max(rest,      sold)            # cooldown: sold → rest

Variants:
  - No cooldown (LC 122):   held = max(held, rest - price); rest = max(rest, sold); sold = held_prev + price
  - With cooldown (LC 309): above with sold → rest (not directly back to held)
  - At most k tx (LC 188):  held[k] = max(held[k], rest[k-1] - price); sold[k] = held[k] + price
  - With fee (LC 714):      held = max(held, rest - price); rest = max(rest, held + price - fee)
```

### Interview tips — dp
| Signal | Pattern |
|--------|---------|
| "max/min subarray with sliding constraint" | Monotonic queue DP |
| "answer for every node as root" | Re-rooting (2 DFS passes) |
| "largest rectangle / maximal square" | Stack DP or DP on prefix heights |
| "game: two players pick optimally" | Minimax DP: dp[i][j] = score diff |
| "count numbers with digit constraint" | Digit DP: (pos, tight, accumulator) |
| "break string into valid words" | Memoized DP + word set |
| "stock buy/sell variants" | State machine (held/sold/rest) |
| "edit distance, LCS, interleaving" | 2D DP → 1D space optimization |