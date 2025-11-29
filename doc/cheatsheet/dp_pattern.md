# DP Pattern

- https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns



## 1. Kadane's Algorithm (Maximum Subarray)

**Pattern**: Find the maximum/minimum sum of a contiguous subarray.

**Key Idea**: At each position, decide whether to extend the current subarray or start a new one.

**Recurrence**: `dp[i] = max(nums[i], dp[i-1] + nums[i])`

**Time Complexity**: O(n) | **Space Complexity**: O(1)

### Template Code:

**Python:**
```python
def maxSubArray(nums):
    max_sum = nums[0]
    current_sum = nums[0]

    for i in range(1, len(nums)):
        current_sum = max(nums[i], current_sum + nums[i])
        max_sum = max(max_sum, current_sum)

    return max_sum
```

**Java:**
```java
public int maxSubArray(int[] nums) {
    int maxSum = nums[0];
    int currentSum = nums[0];

    for (int i = 1; i < nums.length; i++) {
        currentSum = Math.max(nums[i], currentSum + nums[i]);
        maxSum = Math.max(maxSum, currentSum);
    }

    return maxSum;
}
```

### Common Problems:
- LC 53: Maximum Subarray
- LC 152: Maximum Product Subarray
- LC 918: Maximum Sum Circular Subarray
- LC 1749: Maximum Alternating Sum
- Maximum difference of 0's and 1's in a binary string
- Smallest sum contiguous subarray
- Largest sum increasing contiguous subarray
- Maximum Sum Rectangle In A 2D Matrix


## 2. Longest Increasing Subsequence (LIS)

**Pattern**: Find the longest subsequence where elements are in increasing order.

**Key Idea**: For each element, find the longest increasing subsequence ending at that position.

**Recurrence**: `dp[i] = max(dp[j] + 1)` for all `j < i` where `nums[j] < nums[i]`

**Time Complexity**: O(n²) or O(n log n) with binary search | **Space Complexity**: O(n)

### Template Code (O(n²) approach):

**Python:**
```python
def lengthOfLIS(nums):
    if not nums:
        return 0

    n = len(nums)
    dp = [1] * n

    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)

    return max(dp)
```

**Java:**
```java
public int lengthOfLIS(int[] nums) {
    if (nums.length == 0) return 0;

    int n = nums.length;
    int[] dp = new int[n];
    Arrays.fill(dp, 1);

    for (int i = 1; i < n; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
    }

    return Arrays.stream(dp).max().getAsInt();
}
```

### Template Code (O(n log n) with Binary Search):

**Python:**
```python
def lengthOfLIS(nums):
    tails = []

    for num in nums:
        left, right = 0, len(tails)
        while left < right:
            mid = (left + right) // 2
            if tails[mid] < num:
                left = mid + 1
            else:
                right = mid
        if left == len(tails):
            tails.append(num)
        else:
            tails[left] = num

    return len(tails)
```

**Java:**
```java
public int lengthOfLIS(int[] nums) {
    List<Integer> tails = new ArrayList<>();

    for (int num : nums) {
        int left = 0, right = tails.size();
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (tails.get(mid) < num) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        if (left == tails.size()) {
            tails.add(num);
        } else {
            tails.set(left, num);
        }
    }

    return tails.size();
}
```

### Common Problems:
- LC 300: Longest Increasing Subsequence
- LC 673: Number of Longest Increasing Subsequence
- LC 334: Increasing Triplet Subsequence
- LC 1626: Best Team with No Conflicts
- LC 1964: Find the Longest Valid Obstacle Course at Each Position
- LC 2111: Minimum Number of Removals to Make Mountain Array
- Maximum Sum Increasing Subsequence
- Print LIS (`Longest Increasing Subsequence`)
- LIS having sum almost K


## 3. Matrix Chain Multiplication (MCM) / Interval DP

**Pattern**: Divide a problem into subproblems by splitting at different positions and combining results.

**Key Idea**: Try all possible ways to partition the interval and take the optimal one.

**Recurrence**: `dp[i][j] = min/max(dp[i][k] + dp[k+1][j] + cost)` for all `k` in `[i, j)`

**Time Complexity**: O(n³) | **Space Complexity**: O(n²)

### Template Code:

**Python:**
```python
def mcm(arr):
    n = len(arr)
    dp = [[0] * n for _ in range(n)]

    # length is the chain length
    for length in range(2, n):
        for i in range(n - length):
            j = i + length
            dp[i][j] = float('inf')
            for k in range(i + 1, j):
                cost = dp[i][k] + dp[k][j] + arr[i] * arr[k] * arr[j]
                dp[i][j] = min(dp[i][j], cost)

    return dp[0][n-1]

# For problems like burst balloons (bottom-up)
def maxCoins(nums):
    nums = [1] + nums + [1]
    n = len(nums)
    dp = [[0] * n for _ in range(n)]

    for length in range(2, n):
        for left in range(n - length):
            right = left + length
            for i in range(left + 1, right):
                coins = nums[left] * nums[i] * nums[right]
                coins += dp[left][i] + dp[i][right]
                dp[left][right] = max(dp[left][right], coins)

    return dp[0][n-1]
```

**Java:**
```java
public int mcm(int[] arr) {
    int n = arr.length;
    int[][] dp = new int[n][n];

    for (int length = 2; length < n; length++) {
        for (int i = 0; i < n - length; i++) {
            int j = i + length;
            dp[i][j] = Integer.MAX_VALUE;
            for (int k = i + 1; k < j; k++) {
                int cost = dp[i][k] + dp[k][j] + arr[i] * arr[k] * arr[j];
                dp[i][j] = Math.min(dp[i][j], cost);
            }
        }
    }

    return dp[0][n-1];
}

// For problems like burst balloons
public int maxCoins(int[] nums) {
    int[] arr = new int[nums.length + 2];
    arr[0] = 1;
    arr[arr.length - 1] = 1;
    System.arraycopy(nums, 0, arr, 1, nums.length);

    int n = arr.length;
    int[][] dp = new int[n][n];

    for (int length = 2; length < n; length++) {
        for (int left = 0; left < n - length; left++) {
            int right = left + length;
            for (int i = left + 1; i < right; i++) {
                int coins = arr[left] * arr[i] * arr[right];
                coins += dp[left][i] + dp[i][right];
                dp[left][right] = Math.max(dp[left][right], coins);
            }
        }
    }

    return dp[0][n-1];
}
```

### Common Problems:
- LC 312: Burst Balloons
- LC 1039: Minimum Score Triangulation of Polygon
- LC 87: Scramble String
- LC 131: Palindrome Partitioning
- LC 132: Palindrome Partitioning II
- LC 1547: Minimum Cost to Cut a Stick
- LC 1000: Minimum Cost to Merge Stones
- Evaluate Expression to True / Boolean Parenthesization
- Minimum / Maximum Value of an Expression
- Egg Dropping Problem


## 4. Longest Common Subsequence (LCS)

**Pattern**: Find the longest subsequence common to two sequences.

**Key Idea**: If characters match, extend the LCS; otherwise, take the maximum from skipping either character.

**Recurrence**:
- If `s1[i] == s2[j]`: `dp[i][j] = dp[i-1][j-1] + 1`
- Else: `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**Time Complexity**: O(m*n) | **Space Complexity**: O(m*n) or O(min(m,n))

### Template Code:

**Python:**
```python
def longestCommonSubsequence(text1, text2):
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])

    return dp[m][n]

# Space optimized version
def longestCommonSubsequence_optimized(text1, text2):
    m, n = len(text1), len(text2)
    prev = [0] * (n + 1)

    for i in range(1, m + 1):
        curr = [0] * (n + 1)
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                curr[j] = prev[j-1] + 1
            else:
                curr[j] = max(prev[j], curr[j-1])
        prev = curr

    return prev[n]

# Longest Common Substring (different from LCS!)
def longestCommonSubstring(text1, text2):
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    max_length = 0

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
                max_length = max(max_length, dp[i][j])
            else:
                dp[i][j] = 0  # Key difference: reset to 0

    return max_length
```

**Java:**
```java
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m + 1][n + 1];

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1] + 1;
            } else {
                dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
    }

    return dp[m][n];
}

// Space optimized version
public int longestCommonSubsequence_optimized(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[] prev = new int[n + 1];

    for (int i = 1; i <= m; i++) {
        int[] curr = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                curr[j] = prev[j-1] + 1;
            } else {
                curr[j] = Math.max(prev[j], curr[j-1]);
            }
        }
        prev = curr;
    }

    return prev[n];
}

// Longest Common Substring
public int longestCommonSubstring(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m + 1][n + 1];
    int maxLength = 0;

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (text1.charAt(i-1) == text2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1] + 1;
                maxLength = Math.max(maxLength, dp[i][j]);
            } else {
                dp[i][j] = 0;
            }
        }
    }

    return maxLength;
}
```

### Common Problems:
- LC 1143: Longest Common Subsequence
- LC 72: Edit Distance
- LC 583: Delete Operation for Two Strings
- LC 712: Minimum ASCII Delete Sum for Two Strings
- LC 1092: Shortest Common Supersequence
- LC 516: Longest Palindromic Subsequence
- LC 5: Longest Palindromic Substring
- LC 647: Palindromic Substrings
- LC 115: Distinct Subsequences
- LC 392: Is Subsequence
- Longest Common Substring
- Print LCS / SCS
- Minimum insertions/deletions to transform string a to b
- Largest Repeating Subsequence
- Subsequence Pattern Matching
- Count number of times a appears as subsequence in b

## 5. Unbounded Knapsack

**Pattern**: Select items with unlimited quantity to maximize/minimize value or count combinations.

**Key Idea**: For each item, you can use it multiple times. Decision: include the current item again or move to the next item.

**Recurrence**: `dp[i][w] = max(dp[i-1][w], dp[i][w-weight[i]] + value[i])`

**Time Complexity**: O(n*W) | **Space Complexity**: O(W)

### Template Code:

**Python:**
```python
# Unbounded Knapsack - Maximum Value
def unboundedKnapsack(weights, values, capacity):
    dp = [0] * (capacity + 1)

    for w in range(capacity + 1):
        for i in range(len(weights)):
            if weights[i] <= w:
                dp[w] = max(dp[w], dp[w - weights[i]] + values[i])

    return dp[capacity]

# Coin Change - Minimum Coins (LC 322)
def coinChange(coins, amount):
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0

    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] = min(dp[i], dp[i - coin] + 1)

    return dp[amount] if dp[amount] != float('inf') else -1

# Coin Change - Number of Ways (LC 518)
def change(amount, coins):
    dp = [0] * (amount + 1)
    dp[0] = 1

    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]

    return dp[amount]
```

**Java:**
```java
// Unbounded Knapsack - Maximum Value
public int unboundedKnapsack(int[] weights, int[] values, int capacity) {
    int[] dp = new int[capacity + 1];

    for (int w = 0; w <= capacity; w++) {
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= w) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
    }

    return dp[capacity];
}

// Coin Change - Minimum Coins (LC 322)
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);
    dp[0] = 0;

    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] = Math.min(dp[i], dp[i - coin] + 1);
        }
    }

    return dp[amount] > amount ? -1 : dp[amount];
}

// Coin Change - Number of Ways (LC 518)
public int change(int amount, int[] coins) {
    int[] dp = new int[amount + 1];
    dp[0] = 1;

    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] += dp[i - coin];
        }
    }

    return dp[amount];
}
```

### Common Problems:
- LC 322: Coin Change (Minimum Coins)
- LC 518: Coin Change II (Number of Ways)
- LC 377: Combination Sum IV
- LC 139: Word Break
- LC 983: Minimum Cost For Tickets
- Rod Cutting Problem
- Maximum Ribbon Cut
- Number Partitioning


## 6. 0/1 Knapsack

**Pattern**: Select items (each item can be used at most once) to maximize/minimize value or count combinations.

**Key Idea**: For each item, decide whether to include it or not.

**Recurrence**: `dp[i][w] = max(dp[i-1][w], dp[i-1][w-weight[i]] + value[i])`

**Time Complexity**: O(n*W) | **Space Complexity**: O(W)

### Template Code:

**Python:**
```python
# 0/1 Knapsack - Maximum Value
def knapsack(weights, values, capacity):
    n = len(weights)
    dp = [0] * (capacity + 1)

    for i in range(n):
        # Traverse backwards to avoid using same item twice
        for w in range(capacity, weights[i] - 1, -1):
            dp[w] = max(dp[w], dp[w - weights[i]] + values[i])

    return dp[capacity]

# Subset Sum (can we make target sum?)
def canPartition(nums, target):
    dp = [False] * (target + 1)
    dp[0] = True

    for num in nums:
        for i in range(target, num - 1, -1):
            dp[i] = dp[i] or dp[i - num]

    return dp[target]

# Count of Subsets with Given Sum
def countSubsets(nums, target):
    dp = [0] * (target + 1)
    dp[0] = 1

    for num in nums:
        for i in range(target, num - 1, -1):
            dp[i] += dp[i - num]

    return dp[target]

# Target Sum (LC 494)
def findTargetSumWays(nums, target):
    total = sum(nums)
    if abs(target) > total or (total + target) % 2 != 0:
        return 0

    # Transform to subset sum problem
    subset_sum = (total + target) // 2
    dp = [0] * (subset_sum + 1)
    dp[0] = 1

    for num in nums:
        for i in range(subset_sum, num - 1, -1):
            dp[i] += dp[i - num]

    return dp[subset_sum]
```

**Java:**
```java
// 0/1 Knapsack - Maximum Value
public int knapsack(int[] weights, int[] values, int capacity) {
    int n = weights.length;
    int[] dp = new int[capacity + 1];

    for (int i = 0; i < n; i++) {
        for (int w = capacity; w >= weights[i]; w--) {
            dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
        }
    }

    return dp[capacity];
}

// Subset Sum (can we make target sum?)
public boolean canPartition(int[] nums, int target) {
    boolean[] dp = new boolean[target + 1];
    dp[0] = true;

    for (int num : nums) {
        for (int i = target; i >= num; i--) {
            dp[i] = dp[i] || dp[i - num];
        }
    }

    return dp[target];
}

// Count of Subsets with Given Sum
public int countSubsets(int[] nums, int target) {
    int[] dp = new int[target + 1];
    dp[0] = 1;

    for (int num : nums) {
        for (int i = target; i >= num; i--) {
            dp[i] += dp[i - num];
        }
    }

    return dp[target];
}

// Target Sum (LC 494)
public int findTargetSumWays(int[] nums, int target) {
    int total = 0;
    for (int num : nums) total += num;

    if (Math.abs(target) > total || (total + target) % 2 != 0) {
        return 0;
    }

    int subsetSum = (total + target) / 2;
    int[] dp = new int[subsetSum + 1];
    dp[0] = 1;

    for (int num : nums) {
        for (int i = subsetSum; i >= num; i--) {
            dp[i] += dp[i - num];
        }
    }

    return dp[subsetSum];
}
```

### Common Problems:
- LC 416: Partition Equal Subset Sum
- LC 494: Target Sum
- LC 698: Partition to K Equal Sum Subsets
- LC 1049: Last Stone Weight II
- LC 474: Ones and Zeroes (2D Knapsack)
- Subset Sum
- Count of Subsets with Given Sum
- Minimum Subset Sum Difference
- Number of Subsets with Given Difference


## 7. State Machine DP

**Pattern**: Problems with states that transition based on actions/decisions.

**Key Idea**: Track different states and transitions between them. Common in buy/sell stock problems.

**Time Complexity**: O(n*states) | **Space Complexity**: O(states)

### Template Code:

**Python:**
```python
# Best Time to Buy and Sell Stock with Cooldown (LC 309)
def maxProfit(prices):
    if not prices:
        return 0

    # States: hold stock, sold (cooldown), rest (can buy)
    hold = -prices[0]
    sold = 0
    rest = 0

    for i in range(1, len(prices)):
        prev_hold = hold
        prev_sold = sold
        prev_rest = rest

        hold = max(prev_hold, prev_rest - prices[i])  # Keep holding or buy
        sold = prev_hold + prices[i]  # Sell
        rest = max(prev_rest, prev_sold)  # Rest or after cooldown

    return max(sold, rest)

# Best Time to Buy and Sell Stock with Transaction Fee (LC 714)
def maxProfit_fee(prices, fee):
    cash = 0  # Not holding stock
    hold = -prices[0]  # Holding stock

    for i in range(1, len(prices)):
        cash = max(cash, hold + prices[i] - fee)
        hold = max(hold, cash - prices[i])

    return cash
```

**Java:**
```java
// Best Time to Buy and Sell Stock with Cooldown (LC 309)
public int maxProfit(int[] prices) {
    if (prices.length == 0) return 0;

    int hold = -prices[0];
    int sold = 0;
    int rest = 0;

    for (int i = 1; i < prices.length; i++) {
        int prevHold = hold;
        int prevSold = sold;
        int prevRest = rest;

        hold = Math.max(prevHold, prevRest - prices[i]);
        sold = prevHold + prices[i];
        rest = Math.max(prevRest, prevSold);
    }

    return Math.max(sold, rest);
}

// Best Time to Buy and Sell Stock with Transaction Fee (LC 714)
public int maxProfit(int[] prices, int fee) {
    int cash = 0;
    int hold = -prices[0];

    for (int i = 1; i < prices.length; i++) {
        cash = Math.max(cash, hold + prices[i] - fee);
        hold = Math.max(hold, cash - prices[i]);
    }

    return cash;
}
```

### Common Problems:
- LC 121: Best Time to Buy and Sell Stock
- LC 122: Best Time to Buy and Sell Stock II
- LC 123: Best Time to Buy and Sell Stock III
- LC 188: Best Time to Buy and Sell Stock IV
- LC 309: Best Time to Buy and Sell Stock with Cooldown
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee
- LC 198: House Robber (rob/not rob states)
- LC 213: House Robber II


## 8. Grid Path DP

**Pattern**: Count paths or find minimum/maximum cost paths in a grid.

**Key Idea**: Each cell depends on cells that can reach it (usually top, left, or diagonal).

**Recurrence**: `dp[i][j] = dp[i-1][j] + dp[i][j-1]` (for counting paths)

**Time Complexity**: O(m*n) | **Space Complexity**: O(n)

### Template Code:

**Python:**
```python
# Unique Paths (LC 62)
def uniquePaths(m, n):
    dp = [1] * n

    for i in range(1, m):
        for j in range(1, n):
            dp[j] += dp[j-1]

    return dp[n-1]

# Minimum Path Sum (LC 64)
def minPathSum(grid):
    m, n = len(grid), len(grid[0])
    dp = [0] * n
    dp[0] = grid[0][0]

    # Initialize first row
    for j in range(1, n):
        dp[j] = dp[j-1] + grid[0][j]

    # Process remaining rows
    for i in range(1, m):
        dp[0] += grid[i][0]
        for j in range(1, n):
            dp[j] = min(dp[j], dp[j-1]) + grid[i][j]

    return dp[n-1]

# Unique Paths with Obstacles (LC 63)
def uniquePathsWithObstacles(grid):
    if not grid or grid[0][0] == 1:
        return 0

    m, n = len(grid), len(grid[0])
    dp = [0] * n
    dp[0] = 1

    for i in range(m):
        for j in range(n):
            if grid[i][j] == 1:
                dp[j] = 0
            elif j > 0:
                dp[j] += dp[j-1]

    return dp[n-1]
```

**Java:**
```java
// Unique Paths (LC 62)
public int uniquePaths(int m, int n) {
    int[] dp = new int[n];
    Arrays.fill(dp, 1);

    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[j] += dp[j-1];
        }
    }

    return dp[n-1];
}

// Minimum Path Sum (LC 64)
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    dp[0] = grid[0][0];

    for (int j = 1; j < n; j++) {
        dp[j] = dp[j-1] + grid[0][j];
    }

    for (int i = 1; i < m; i++) {
        dp[0] += grid[i][0];
        for (int j = 1; j < n; j++) {
            dp[j] = Math.min(dp[j], dp[j-1]) + grid[i][j];
        }
    }

    return dp[n-1];
}

// Unique Paths with Obstacles (LC 63)
public int uniquePathsWithObstacles(int[][] grid) {
    if (grid.length == 0 || grid[0][0] == 1) return 0;

    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    dp[0] = 1;

    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (grid[i][j] == 1) {
                dp[j] = 0;
            } else if (j > 0) {
                dp[j] += dp[j-1];
            }
        }
    }

    return dp[n-1];
}
```

### Common Problems:
- LC 62: Unique Paths
- LC 63: Unique Paths II
- LC 64: Minimum Path Sum
- LC 120: Triangle
- LC 174: Dungeon Game
- LC 221: Maximal Square
- LC 931: Minimum Falling Path Sum
- LC 1594: Maximum Non Negative Product in a Matrix


## 9. Bitmask DP

**Pattern**: Use bitmask to represent subsets/states in problems with small constraints (n ≤ 20).

**Key Idea**: Each bit represents whether an element is included/visited. Iterate through all possible states.

**Time Complexity**: O(2^n * n) or O(2^n * n²) | **Space Complexity**: O(2^n)

### Template Code:

**Python:**
```python
# Traveling Salesman Problem (TSP)
def tsp(graph):
    n = len(graph)
    ALL_VISITED = (1 << n) - 1
    dp = [[float('inf')] * n for _ in range(1 << n)]
    dp[1][0] = 0  # Start from node 0

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

    return min(dp[ALL_VISITED][i] + graph[i][0] for i in range(n))

# Shortest Path Visiting All Nodes (LC 847)
def shortestPathLength(graph):
    n = len(graph)
    target = (1 << n) - 1
    queue = [(i, 1 << i, 0) for i in range(n)]  # (node, mask, dist)
    visited = {(i, 1 << i) for i in range(n)}

    while queue:
        node, mask, dist = queue.pop(0)
        if mask == target:
            return dist

        for neighbor in graph[node]:
            new_mask = mask | (1 << neighbor)
            if (neighbor, new_mask) not in visited:
                visited.add((neighbor, new_mask))
                queue.append((neighbor, new_mask, dist + 1))

    return -1
```

**Java:**
```java
// Traveling Salesman Problem (TSP)
public int tsp(int[][] graph) {
    int n = graph.length;
    int ALL_VISITED = (1 << n) - 1;
    int[][] dp = new int[1 << n][n];

    for (int[] row : dp) {
        Arrays.fill(row, Integer.MAX_VALUE / 2);
    }
    dp[1][0] = 0;

    for (int mask = 0; mask < (1 << n); mask++) {
        for (int u = 0; u < n; u++) {
            if ((mask & (1 << u)) == 0) continue;

            for (int v = 0; v < n; v++) {
                if ((mask & (1 << v)) != 0) continue;

                int newMask = mask | (1 << v);
                dp[newMask][v] = Math.min(dp[newMask][v],
                                          dp[mask][u] + graph[u][v]);
            }
        }
    }

    int result = Integer.MAX_VALUE;
    for (int i = 0; i < n; i++) {
        result = Math.min(result, dp[ALL_VISITED][i] + graph[i][0]);
    }
    return result;
}
```

### Common Problems:
- LC 847: Shortest Path Visiting All Nodes
- LC 943: Find the Shortest Superstring
- LC 1125: Smallest Sufficient Team
- LC 1434: Number of Ways to Wear Different Hats to Each Other
- LC 1595: Minimum Cost to Connect Two Groups of Points
- LC 2172: Maximum AND Sum of Array
- Traveling Salesman Problem
- Assignment Problem


## 10. Digit DP

**Pattern**: Count numbers in a range satisfying certain digit properties.

**Key Idea**: Build numbers digit by digit, tracking constraints (tight bound, leading zeros, etc.).

**Time Complexity**: O(digits * states) | **Space Complexity**: O(digits * states)

### Template Code:

**Python:**
```python
# Count numbers with unique digits (LC 357)
def countNumbersWithUniqueDigits(n):
    if n == 0:
        return 1

    result = 10  # For n=1
    unique_digits = 9
    available = 9

    for i in range(2, n + 1):
        unique_digits *= available
        result += unique_digits
        available -= 1

    return result

# Numbers At Most N Given Digit Set (LC 902)
def atMostNGivenDigitSet(digits, n):
    s = str(n)
    k = len(s)
    dp = [0] * (k + 1)
    dp[k] = 1

    for i in range(k - 1, -1, -1):
        for d in digits:
            if d < s[i]:
                dp[i] += len(digits) ** (k - i - 1)
            elif d == s[i]:
                dp[i] += dp[i + 1]

    # Add numbers with fewer digits
    for i in range(1, k):
        dp[0] += len(digits) ** i

    return dp[0]
```

**Java:**
```java
// Count numbers with unique digits (LC 357)
public int countNumbersWithUniqueDigits(int n) {
    if (n == 0) return 1;

    int result = 10;
    int uniqueDigits = 9;
    int available = 9;

    for (int i = 2; i <= n && available > 0; i++) {
        uniqueDigits *= available;
        result += uniqueDigits;
        available--;
    }

    return result;
}

// Numbers At Most N Given Digit Set (LC 902)
public int atMostNGivenDigitSet(String[] digits, int n) {
    String s = String.valueOf(n);
    int k = s.length();
    int[] dp = new int[k + 1];
    dp[k] = 1;

    for (int i = k - 1; i >= 0; i--) {
        char c = s.charAt(i);
        for (String d : digits) {
            if (d.charAt(0) < c) {
                dp[i] += Math.pow(digits.length, k - i - 1);
            } else if (d.charAt(0) == c) {
                dp[i] += dp[i + 1];
            }
        }
    }

    for (int i = 1; i < k; i++) {
        dp[0] += Math.pow(digits.length, i);
    }

    return dp[0];
}
```

### Common Problems:
- LC 233: Number of Digit One
- LC 357: Count Numbers with Unique Digits
- LC 600: Non-negative Integers without Consecutive Ones
- LC 902: Numbers At Most N Given Digit Set
- LC 1012: Numbers With Repeated Digits
- LC 2376: Count Special Integers


## 11. DP on Trees

**Pattern**: Compute values on tree nodes based on subtree values.

**Key Idea**: Use DFS/post-order traversal to solve for children first, then combine results at parent.

**Time Complexity**: O(n) | **Space Complexity**: O(height)

### Template Code:

**Python:**
```python
# House Robber III (LC 337)
def rob(root):
    def dfs(node):
        if not node:
            return (0, 0)  # (rob, not_rob)

        left = dfs(node.left)
        right = dfs(node.right)

        # If rob current node, can't rob children
        rob_current = node.val + left[1] + right[1]
        # If not rob current, take max of children
        not_rob_current = max(left) + max(right)

        return (rob_current, not_rob_current)

    return max(dfs(root))

# Binary Tree Maximum Path Sum (LC 124)
def maxPathSum(root):
    max_sum = float('-inf')

    def dfs(node):
        nonlocal max_sum
        if not node:
            return 0

        # Get max sum from left and right subtrees (ignore negative)
        left = max(0, dfs(node.left))
        right = max(0, dfs(node.right))

        # Update max_sum considering path through current node
        max_sum = max(max_sum, node.val + left + right)

        # Return max sum ending at current node
        return node.val + max(left, right)

    dfs(root)
    return max_sum
```

**Java:**
```java
// House Robber III (LC 337)
public int rob(TreeNode root) {
    int[] result = dfs(root);
    return Math.max(result[0], result[1]);
}

private int[] dfs(TreeNode node) {
    if (node == null) return new int[]{0, 0};

    int[] left = dfs(node.left);
    int[] right = dfs(node.right);

    int rob = node.val + left[1] + right[1];
    int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);

    return new int[]{rob, notRob};
}

// Binary Tree Maximum Path Sum (LC 124)
int maxSum = Integer.MIN_VALUE;

public int maxPathSum(TreeNode root) {
    dfs(root);
    return maxSum;
}

private int dfs(TreeNode node) {
    if (node == null) return 0;

    int left = Math.max(0, dfs(node.left));
    int right = Math.max(0, dfs(node.right));

    maxSum = Math.max(maxSum, node.val + left + right);

    return node.val + Math.max(left, right);
}
```

### Common Problems:
- LC 124: Binary Tree Maximum Path Sum
- LC 337: House Robber III
- LC 543: Diameter of Binary Tree
- LC 687: Longest Univalue Path
- LC 968: Binary Tree Cameras
- LC 979: Distribute Coins in Binary Tree
- LC 1130: Minimum Cost Tree From Leaf Values
- LC 2246: Longest Path With Different Adjacent Characters


## Key DP Problem-Solving Steps

1. **Identify if it's a DP problem**: Look for optimal substructure and overlapping subproblems
2. **Define the state**: What parameters uniquely identify a subproblem?
3. **Define the recurrence relation**: How do subproblems relate?
4. **Identify base cases**: What are the smallest subproblems?
5. **Decide on approach**: Top-down (memoization) vs Bottom-up (tabulation)
6. **Optimize space**: Can you reduce dimensions or use rolling arrays?


## DP Optimization Techniques

- **Space Optimization**: Use 1D array instead of 2D when only previous row/column is needed
- **Rolling Array**: Keep only last k rows/states instead of all
- **State Compression**: Use bitmask to compress states
- **Monotonic Queue/Stack**: Optimize window-based DP (sliding window maximum)
- **Matrix Exponentiation**: For linear recurrences with large n
- **Convex Hull Trick**: For optimizing certain recurrence relations