# DP Pattern

> **Scope** — **Template index** — one short section per classic DP pattern (Kadane, LIS, MCM, LCS, knapsack, state machine, grid, bitmask, digit, tree DP, regex, interval scheduling, split, memoised DAG).
> **See also**: [dp.md](./dp.md) — the explanations and worked examples behind these templates; [recursion_to_dp.md](./recursion_to_dp.md) — how to *derive* one of these from a recursion.

- https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 1. Kadane's Algorithm (Maximum Subarray) ⭐⭐⭐⭐⭐

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


## 2. Longest Increasing Subsequence (LIS) ⭐⭐⭐⭐

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
- LC 354: Russian Doll Envelopes (2D LIS — see variation below)
- LC 1048: Longest String Chain ("smaller" = *is a predecessor string*; sort by word length first, then LIS logic)
- Maximum Sum Increasing Subsequence
- Print LIS (`Longest Increasing Subsequence`)
- LIS having sum almost K

### Variation: 2D LIS — Russian Doll Envelopes (LC 354)

> **Twist**: sort widths **ascending** but break width ties by height **descending**. The descending tie-break makes two envelopes of equal width impossible to chain, so the answer collapses to a plain O(n log n) LIS over the heights.

**Java:**
```java
// java
// LC 354 - Russian Doll Envelopes
// IDEA: sort (w asc, h desc) -> the answer is LIS over heights (patience sorting)
// time = O(N log N), space = O(N)
public int maxEnvelopes(int[][] envelopes) {
    Arrays.sort(envelopes, (a, b) -> a[0] == b[0] ? b[1] - a[1] : a[0] - b[0]);
    List<Integer> tails = new ArrayList<>();
    for (int[] e : envelopes) {
        int lo = 0, hi = tails.size();
        while (lo < hi) {                       // lower_bound on height
            int mid = lo + (hi - lo) / 2;
            if (tails.get(mid) < e[1]) lo = mid + 1;
            else hi = mid;
        }
        if (lo == tails.size()) tails.add(e[1]);
        else tails.set(lo, e[1]);
    }
    return tails.size();
}
```

**Python:**
```python
# python
# LC 354 - Russian Doll Envelopes
# IDEA: sort (w asc, h desc) -> LIS over heights
# time = O(N log N), space = O(N)
from bisect import bisect_left

def maxEnvelopes(envelopes):
    envelopes.sort(key=lambda e: (e[0], -e[1]))
    tails = []
    for _, h in envelopes:
        i = bisect_left(tails, h)   # strict increase -> lower_bound
        if i == len(tails):
            tails.append(h)
        else:
            tails[i] = h
    return len(tails)
```


## 3. Matrix Chain Multiplication (MCM) / Interval DP ⭐⭐⭐⭐

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
    for length in range(2, n + 1):
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

    for length in range(2, n + 1):
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

    for (int length = 2; length <= n; length++) {
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

    for (int length = 2; length <= n; length++) {
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
- LC 96 / LC 95: Unique Binary Search Trees (I / II) — split the interval on the **root**: `dp[n] = Σ dp[i-1] * dp[n-i]` (Catalan numbers); LC 95 returns the trees instead of the count
- Evaluate Expression to True / Boolean Parenthesization
- Minimum / Maximum Value of an Expression
- Egg Dropping Problem


## 4. Longest Common Subsequence (LCS) ⭐⭐⭐⭐⭐

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

# Space optimized version (2D → 1D rolling array)
# prev[j] represents dp[i-1][j]; curr[j] represents dp[i][j]
# curr[0] = 0 is always the base case (empty prefix of text2 → LCS length 0)
def longestCommonSubsequence_optimized(text1, text2):
    m, n = len(text1), len(text2)
    prev = [0] * (n + 1)

    for i in range(1, m + 1):
        curr = [0] * (n + 1)  # curr[0] = 0 is the base case boundary
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
- LC 97: Interleaving String (same two-string grid, but `dp[i][j]` = *can* `s1[0..i)` + `s2[0..j)` interleave into `s3[0..i+j)`)
- LC 718: Maximum Length of Repeated Subarray (this is Longest Common **Substring** on arrays — reset to 0 on mismatch)
- Longest Common Substring
- Print LCS / SCS
- Minimum insertions/deletions to transform string a to b
- Largest Repeating Subsequence
- Subsequence Pattern Matching
- Count number of times a appears as subsequence in b

## 5. Unbounded Knapsack ⭐⭐⭐⭐

**Pattern**: Select items with unlimited quantity to maximize/minimize value or count combinations.

**Key Idea**: For each item, you can use it multiple times. Decision: include the current item again or move to the next item.

**Recurrence**: `dp[i][w] = max(dp[i-1][w], dp[i][w-weight[i]] + value[i])`

**Time Complexity**: O(n*W) | **Space Complexity**: O(W)

### Key Difference from 0/1 Knapsack

| Variant | Loop Order | Why |
|---------|-----------|-----|
| 0/1 Knapsack | Outer: items, Inner: capacity **reverse** | Each item used at most once |
| Unbounded Knapsack | Outer: items, Inner: capacity **forward** | Item can be reused |

### Template Code (Coin Change examples):

**Python:**
```python
# Coin Change - Minimum Coins (LC 322)
def coinChange(coins, amount):
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0

    for coin in coins:
        for i in range(coin, amount + 1):  # forward = unbounded
            dp[i] = min(dp[i], dp[i - coin] + 1)

    return dp[amount] if dp[amount] != float('inf') else -1

# Coin Change - Number of Ways (LC 518)
def change(amount, coins):
    dp = [0] * (amount + 1)
    dp[0] = 1

    for coin in coins:
        for i in range(coin, amount + 1):  # forward = unbounded
            dp[i] += dp[i - coin]

    return dp[amount]
```

**Java:**
```java
// Coin Change - Minimum Coins (LC 322)
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount + 1];
    Arrays.fill(dp, amount + 1);
    dp[0] = 0;

    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {  // forward = unbounded
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
        for (int i = coin; i <= amount; i++) {  // forward = unbounded
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
- LC 139: Word Break (the prefix-partition spelling of this recurrence — [Template 1b](./dp.md#template-1b-prefix-partition-dp---lc-139))
- LC 140: Word Break II (same `dp[i]` split test, but memoize **lists of sentences** instead of booleans)
- LC 472: Concatenated Words (run Word Break on each word using the *other* words as the dictionary; sort by length so only shorter words are in the dict)
- LC 279: Perfect Squares (coins = the perfect squares ≤ n; minimize count)
- LC 1155: Number of Dice Rolls With Target Sum (bounded/group knapsack: exactly `k` dice, each contributing 1..f)
- LC 983: Minimum Cost For Tickets
- Rod Cutting Problem
- Maximum Ribbon Cut
- Number Partitioning


## 6. 0/1 Knapsack ⭐⭐⭐⭐⭐

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


## 7. State Machine DP ⭐⭐⭐⭐

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
- LC 801: Minimum Swaps To Make Sequences Increasing (2 states per index: **swapped** / **kept**; transition legality depends on both `A`/`B` comparisons)
- LC 926: Flip String to Monotone Increasing (2 states: prefix ending in `0` / ending in `1`; flipping cost accumulates per state)


## 8. Grid Path DP ⭐⭐⭐⭐

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
- LC 1277: Count Square Submatrices with All Ones (identical recurrence to LC 221 Maximal Square — **sum** the dp table instead of taking the max)
- LC 688: Knight Probability in Chessboard (probability grid DP: `dp[k][r][c]` = prob. of still being on the board after `k` moves; each of the 8 moves carries weight `1/8`)
- LC 764: Largest Plus Sign (4 directional prefix-run DP passes — up/down/left/right — then take the min at each cell)


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
- LC 464: Can I Win (bitmask of *used numbers* + game-theory win/lose memo)
- LC 691: Stickers to Spell Word (bitmask over the letters of the target that are already covered)
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
    # when n > 10, answer is fixed (9 + 9*9 + 9*9*8 + ... for 1-10 digits)
    # because there are only 10 distinct digits (0-9), so available hits 0 after 10 digits.
    # The `available > 0` guard in Java's loop handles this; Python stops naturally at available=0.

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


### Alternative template — count integers in `[0, n]` with property P
> LC 233, LC 1012. Same idea as above, written with an explicit `(position, tight, ...)` state.
Count numbers in `[1, n]` satisfying a digit constraint.

```python
# Template: count integers in [0, n] with property P
# State: (position, tight, count_so_far, ...)
def digitDP(n: int) -> int:
    digits = list(map(int, str(n)))
    from functools import lru_cache

    @lru_cache(maxsize=None)
    def dp(pos, tight, count):
        if pos == len(digits):
            return count  # or return 1, depending on problem
        limit = digits[pos] if tight else 9
        result = 0
        for d in range(0, limit + 1):
            # compare against digits[pos], NOT `limit` — they are only equal while
            # tight is True, and writing it this way survives later edits
            result += dp(pos + 1, tight and d == digits[pos], count + (d == 1))
        return result

    return dp(0, True, 0)
```

Key state variables:
- `pos`: current digit position
- `tight`: whether we're still bounded by `n`'s digits
- Any problem-specific counter (number of 1s, digit sum, etc.)


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


## 12. Wildcard / Regex Pattern Matching DP ⭐⭐⭐⭐⭐

**Recognition Signal**: two strings, but they are **not symmetric** — one is text `s`, the other is a *pattern* `p` containing wildcards (`*`, `?`, `.`). Answer is a boolean. Greedy fails because `*` may consume any number of characters.

**Pattern**: same 2D prefix grid as LCS, but the transition is driven by the **pattern character**, not by equality.

**Key Idea**: `dp[i][j]` = does `s[0..i)` match `p[0..j)`. A `*` gives you a two-way choice — *consume one more text char* (stay on `*`) or *drop the `*`*.

**Recurrence** (LC 44, `*` = any sequence):
- `p[j-1] == '*'` → `dp[i][j] = dp[i-1][j] (star eats s[i-1]) || dp[i][j-1] (star eats nothing)`
- `p[j-1] == '?'` or chars equal → `dp[i][j] = dp[i-1][j-1]`

**Time Complexity**: O(m*n) | **Space Complexity**: O(m*n) → O(n) with a rolling row

> ⚠️ **The base row is where people lose this problem**: `dp[0][j]` (empty text) must stay `true` while the pattern is a run of `*` — otherwise `"" vs "***"` fails.

### Template Code:

**Java:**
```java
// java
// LC 44 - Wildcard Matching
// IDEA: dp[i][j] = s[0..i) matches p[0..j); '*' = (eat one char) OR (match empty)
// time = O(M*N), space = O(M*N)
public boolean isMatch(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[0][0] = true;
    for (int j = 1; j <= n; j++)                       // empty text vs leading "***"
        if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 1];

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            char pc = p.charAt(j - 1);
            if (pc == '*')
                dp[i][j] = dp[i - 1][j] || dp[i][j - 1];
            else if (pc == '?' || pc == s.charAt(i - 1))
                dp[i][j] = dp[i - 1][j - 1];
        }
    }
    return dp[m][n];
}
```

**Python:**
```python
# python
# LC 44 - Wildcard Matching
# IDEA: dp[i][j] = s[0..i) matches p[0..j); '*' = (eat one char) OR (match empty)
# time = O(M*N), space = O(M*N)
def isMatch(s, p):
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[0][0] = True
    for j in range(1, n + 1):
        if p[j-1] == '*':
            dp[0][j] = dp[0][j-1]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if p[j-1] == '*':
                dp[i][j] = dp[i-1][j] or dp[i][j-1]
            elif p[j-1] == '?' or p[j-1] == s[i-1]:
                dp[i][j] = dp[i-1][j-1]

    return dp[m][n]
```

### Variation: `*` binds to the previous character — LC 10 Regular Expression Matching

> **Twist**: in regex, `*` is a **quantifier on `p[j-2]`**, not a standalone wildcard. So "use zero occurrences" skips **two** pattern chars (`dp[i][j-2]`), and "use one more occurrence" is only allowed when `p[j-2]` actually matches `s[i-1]`.

**Java:**
```java
// java
// LC 10 - Regular Expression Matching
// IDEA: '*' quantifies p[j-2]: zero occurrence -> dp[i][j-2]; one more -> dp[i-1][j] if p[j-2] matches
// time = O(M*N), space = O(M*N)
public boolean isMatch(String s, String p) {
    int m = s.length(), n = p.length();
    boolean[][] dp = new boolean[m + 1][n + 1];
    dp[0][0] = true;
    for (int j = 2; j <= n; j++)                       // "a*b*c*" can match empty text
        if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 2];

    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            char pc = p.charAt(j - 1);
            if (pc == '*') {
                char prev = p.charAt(j - 2);
                dp[i][j] = dp[i][j - 2];               // zero occurrence of prev
                if (prev == '.' || prev == s.charAt(i - 1))
                    dp[i][j] = dp[i][j] || dp[i - 1][j];   // one more occurrence
            } else if (pc == '.' || pc == s.charAt(i - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            }
        }
    }
    return dp[m][n];
}
```

**Python:**
```python
# python
# LC 10 - Regular Expression Matching
# IDEA: '*' quantifies p[j-2]: zero occurrence -> dp[i][j-2]; one more -> dp[i-1][j]
# time = O(M*N), space = O(M*N)
def isMatch(s, p):
    m, n = len(s), len(p)
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    dp[0][0] = True
    for j in range(2, n + 1):
        if p[j-1] == '*':
            dp[0][j] = dp[0][j-2]

    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if p[j-1] == '*':
                dp[i][j] = dp[i][j-2]                       # zero occurrence
                if p[j-2] in ('.', s[i-1]):
                    dp[i][j] = dp[i][j] or dp[i-1][j]       # one more occurrence
            elif p[j-1] in ('.', s[i-1]):
                dp[i][j] = dp[i-1][j-1]

    return dp[m][n]
```

### LC 44 vs LC 10 — the only two lines that differ

| | LC 44 `*` (wildcard) | LC 10 `*` (quantifier) |
|---|---|---|
| Meaning | any sequence, standalone | 0+ copies of `p[j-2]` |
| "match empty" | `dp[i][j-1]` (drop 1 char) | `dp[i][j-2]` (drop 2 chars) |
| "match one more" | `dp[i-1][j]` — always allowed | `dp[i-1][j]` — only if `p[j-2]` matches `s[i-1]` |
| Base row | `dp[0][j] = dp[0][j-1]` if `*` | `dp[0][j] = dp[0][j-2]` if `*` |

### Common Problems:
- LC 44: Wildcard Matching
- LC 10: Regular Expression Matching
- LC 97: Interleaving String (same grid shape, different transition)
- LC 72: Edit Distance (same grid shape, min-cost instead of boolean)


## 13. Weighted Interval Scheduling DP (Sort + Binary Search)

**Recognition Signal**: items are **intervals with a value** (`start`, `end`, `profit`), you must pick a **non-overlapping** subset maximizing value, and `n` is large (10⁴–10⁵) so an O(n²) "compare with every earlier item" DP is too slow. Pure greedy (as in "max number of non-overlapping intervals") does **not** work once intervals carry different weights.

**Pattern**: sort by **end time**, then each item's predecessor is found by binary search.

**Key Idea**: after sorting by end time, `dp[i]` = best profit using the first `i` jobs. Job `i` either isn't taken (`dp[i-1]`) or is taken, in which case everything compatible with it is a **prefix** of the sorted array — found with one binary search.

**Recurrence**: `dp[i] = max(dp[i-1], dp[p(i)] + profit[i])`, where `p(i)` = number of jobs whose `end <= start[i]`

**Time Complexity**: O(n log n) | **Space Complexity**: O(n)

### Template Code:

**Java:**
```java
// java
// LC 1235 - Maximum Profit in Job Scheduling
// IDEA: sort by end time; dp[i] = max(dp[i-1], dp[p(i)] + profit_i), p(i) via binary search
// time = O(N log N), space = O(N)
public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
    int n = startTime.length;
    int[][] jobs = new int[n][3];                      // {end, start, profit}
    for (int i = 0; i < n; i++)
        jobs[i] = new int[]{endTime[i], startTime[i], profit[i]};
    Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0]));

    int[] ends = new int[n + 1];                       // ends[0] = 0 sentinel, ends[i] = end of i-th job
    int[] dp = new int[n + 1];                         // dp[0] = 0
    for (int i = 1; i <= n; i++) {
        int idx = upperBound(ends, i, jobs[i - 1][1]) - 1;   // last job ending <= start_i
        dp[i] = Math.max(dp[i - 1], dp[idx] + jobs[i - 1][2]);
        ends[i] = jobs[i - 1][0];
    }
    return dp[n];
}

// first index in arr[0..len) whose value > target
private int upperBound(int[] arr, int len, int target) {
    int lo = 0, hi = len;
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (arr[mid] <= target) lo = mid + 1;
        else hi = mid;
    }
    return lo;
}
```

**Python:**
```python
# python
# LC 1235 - Maximum Profit in Job Scheduling
# IDEA: sort by end time; dp[i] = max(dp[i-1], dp[p(i)] + profit_i), p(i) via bisect
# time = O(N log N), space = O(N)
from bisect import bisect_right

def jobScheduling(startTime, endTime, profit):
    jobs = sorted(zip(endTime, startTime, profit))   # sort by end time
    ends, dp = [0], [0]                              # sentinel: "no job taken" -> profit 0
    for e, s, p in jobs:
        i = bisect_right(ends, s) - 1                # last job ending <= s
        dp.append(max(dp[-1], dp[i] + p))
        ends.append(e)
    return dp[-1]
```

### Variation: add a "at most k picks" dimension — LC 1751 Maximum Number of Events That Can Be Attended II

> **Twist**: same sort-by-end + binary-search skeleton, one extra dimension for the budget:
> `dp[i][j] = max(dp[i-1][j], dp[p(i)][j-1] + value_i)` — O(n·k·log n).

### Common Problems:
- LC 1235: Maximum Profit in Job Scheduling
- LC 1751: Maximum Number of Events That Can Be Attended II (bounded to `k` intervals)
- LC 646: Maximum Length of Pair Chain (unweighted → greedy also works)
- LC 300: Longest Increasing Subsequence (same "predecessor by binary search" idea in a 1D setting)


## 14. Partition into K Contiguous Groups (Split DP)

**Recognition Signal**: "split the array / schedule the jobs into **exactly `k` contiguous parts**", and the objective is over the parts (sum of the maxes, the max of the sums, sum of the costs). Note the parts must stay **contiguous** — this is what separates it from knapsack.

**Pattern**: a two-dimension DP where the second dimension is *how many parts are still available*. Different from interval/MCM DP: MCM splits into 2 halves recursively; here you cut the sequence left-to-right into `k` blocks.

**Key Idea**: `dp[i][k]` = best cost of covering the suffix `a[i..n)` with exactly `k` parts. Enumerate where the **first** part ends, and keep a running `max`/`sum` of that first block so each transition is O(1).

**Recurrence**: `dp[i][k] = min over j >= i of ( cost(a[i..j]) + dp[j+1][k-1] )`

**Time Complexity**: O(n² * k) | **Space Complexity**: O(n * k)

> Feasibility guard: if `n < k` there aren't enough elements to form `k` non-empty parts → return -1.

### Template Code:

**Java:**
```java
// java
// LC 1335 - Minimum Difficulty of a Job Schedule
// IDEA: dp[i][k] = min difficulty to finish jobs[i..n) in k days; cut off day 1 at every j
// time = O(N^2 * D), space = O(N * D)
public int minDifficulty(int[] jobDifficulty, int d) {
    int n = jobDifficulty.length;
    if (n < d) return -1;                    // not enough jobs to fill d days
    int[][] memo = new int[n][d + 1];
    for (int[] row : memo) Arrays.fill(row, -1);
    return dfs(jobDifficulty, 0, d, memo);
}

private int dfs(int[] a, int i, int k, int[][] memo) {
    if (memo[i][k] != -1) return memo[i][k];
    int n = a.length, best;
    if (k == 1) {                            // last day takes every remaining job
        best = 0;
        for (int j = i; j < n; j++) best = Math.max(best, a[j]);
    } else {
        best = Integer.MAX_VALUE;
        int cur = 0;                         // running max of today's block
        for (int j = i; j <= n - k; j++) {   // leave >= k-1 jobs for the other days
            cur = Math.max(cur, a[j]);
            best = Math.min(best, cur + dfs(a, j + 1, k - 1, memo));
        }
    }
    memo[i][k] = best;
    return best;
}
```

**Python:**
```python
# python
# LC 1335 - Minimum Difficulty of a Job Schedule
# IDEA: dp(i, k) = min difficulty for jobs[i..n) in k days; enumerate today's last job
# time = O(N^2 * D), space = O(N * D)
from functools import lru_cache

def minDifficulty(jobDifficulty, d):
    n = len(jobDifficulty)
    if n < d:
        return -1

    @lru_cache(maxsize=None)
    def go(i, k):
        if k == 1:
            return max(jobDifficulty[i:])
        best, cur = float('inf'), 0
        for j in range(i, n - k + 1):        # keep >= k-1 jobs for later days
            cur = max(cur, jobDifficulty[j])
            best = min(best, cur + go(j + 1, k - 1))
        return best

    return go(0, d)
```

### Variation: minimize the LARGEST part instead of the sum — LC 410 Split Array Largest Sum

> **Twist**: the objective combines parts with `max` rather than `+`:
> `dp[i][t] = min over j of max( dp[j][t-1], sum(a[j..i)) )` — O(n²k).
> Because the answer is monotone ("can we split with every part ≤ X?"), LC 410 is *also* solvable by **binary search on the answer** in O(n log ΣA) — mention both in an interview and implement the binary search one.

### Common Problems:
- LC 1335: Minimum Difficulty of a Job Schedule
- LC 410: Split Array Largest Sum (also binary search on answer)
- LC 813: Largest Sum of Averages
- LC 1043: Partition Array for Maximum Sum (block length capped at `k` — the *cap* replaces the part-count dimension)
- LC 132: Palindrome Partitioning II (parts must be palindromes; minimize part count)


## 15. Memoized DFS on an Implicit DAG

**Recognition Signal**: movement is **not** restricted to "right/down" and there is no obvious processing order — but a **strict monotonicity constraint** (strictly increasing cell value, strictly forward jump) guarantees no cycles. That turns the state graph into a DAG, so plain DFS + memo is legal and O(states).

**Pattern**: when you cannot easily topologically sort the states by hand, let recursion discover the order and cache each state once. (Contrast Section 8 Grid Path DP, where row-major order *is* the topological order.)

**Key Idea**: `memo[state]` = answer of the subproblem *starting* at that state. Every state is expanded once; each edge is relaxed once.

**Recurrence**: `f(u) = 1 + max(f(v))` over edges `u → v` allowed by the monotone constraint (`f(u) = 1` if none)

**Time Complexity**: O(V + E) = O(m*n) for a grid | **Space Complexity**: O(m*n)

### Template Code:

**Java:**
```java
// java
// LC 329 - Longest Increasing Path in a Matrix
// IDEA: strictly-increasing moves => implicit DAG => DFS + memo; memo[r][c] = longest path starting at (r,c)
// time = O(M*N), space = O(M*N)
private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

public int longestIncreasingPath(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length, res = 0;
    int[][] memo = new int[m][n];                 // 0 = not computed
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++)
            res = Math.max(res, dfs(matrix, r, c, memo));
    return res;
}

private int dfs(int[][] g, int r, int c, int[][] memo) {
    if (memo[r][c] != 0) return memo[r][c];
    int best = 1;
    for (int[] d : DIRS) {
        int nr = r + d[0], nc = c + d[1];
        if (nr >= 0 && nr < g.length && nc >= 0 && nc < g[0].length
                && g[nr][nc] > g[r][c])           // strict '>' => no cycle, no visited-set needed
            best = Math.max(best, 1 + dfs(g, nr, nc, memo));
    }
    memo[r][c] = best;
    return best;
}
```

**Python:**
```python
# python
# LC 329 - Longest Increasing Path in a Matrix
# IDEA: strictly-increasing moves => implicit DAG => DFS + memo
# time = O(M*N), space = O(M*N)
def longestIncreasingPath(matrix):
    if not matrix or not matrix[0]:
        return 0
    m, n = len(matrix), len(matrix[0])
    memo = [[0] * n for _ in range(m)]

    def dfs(r, c):
        if memo[r][c]:
            return memo[r][c]
        best = 1
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n and matrix[nr][nc] > matrix[r][c]:
                best = max(best, 1 + dfs(nr, nc))
        memo[r][c] = best
        return best

    return max(dfs(r, c) for r in range(m) for c in range(n))
```

> ⚠️ **Python recursion depth**: memoization cuts *work*, not *stack depth* — one increasing path can span all `M*N` cells (LC 329 allows 200×200 = 40 000), far past CPython's default 1000-frame limit. Add `sys.setrecursionlimit(10**6)` (Java is fine), or switch to the iterative form: peel cells in topological order (outdegree-0 first, BFS layer by layer).

### Variation: the state must carry the LAST MOVE — LC 403 Frog Jump

> **Twist**: reachability of a stone is not enough — the next legal jumps depend on the jump you just made, so the state is the pair `(stone index, last jump size)`. Recognizing that "position alone is not a state" is the whole problem.

**Java:**
```java
// java
// LC 403 - Frog Jump
// IDEA: state = (stone index, last jump k); next jump ∈ {k-1, k, k+1}; memo on the pair
// time = O(N^2), space = O(N^2)
public boolean canCross(int[] stones) {
    int n = stones.length;
    Map<Integer, Integer> idx = new HashMap<>();          // stone position -> index
    for (int i = 0; i < n; i++) idx.put(stones[i], i);
    Boolean[][] memo = new Boolean[n][n + 1];             // k never exceeds n
    return dfs(stones, idx, 0, 0, memo);
}

private boolean dfs(int[] stones, Map<Integer, Integer> idx, int i, int k, Boolean[][] memo) {
    if (i == stones.length - 1) return true;
    if (memo[i][k] != null) return memo[i][k];
    boolean ok = false;
    for (int step = k - 1; step <= k + 1 && !ok; step++) {
        if (step <= 0) continue;
        Integer nxt = idx.get(stones[i] + step);
        if (nxt != null) ok = dfs(stones, idx, nxt, step, memo);
    }
    memo[i][k] = ok;
    return ok;
}
```

**Python:**
```python
# python
# LC 403 - Frog Jump
# IDEA: state = (stone index, last jump k); next jump in {k-1, k, k+1}
# time = O(N^2), space = O(N^2)
from functools import lru_cache

def canCross(stones):
    idx = {s: i for i, s in enumerate(stones)}
    n = len(stones)

    @lru_cache(maxsize=None)
    def go(i, k):
        if i == n - 1:
            return True
        for step in (k - 1, k, k + 1):
            if step > 0 and stones[i] + step in idx:
                if go(idx[stones[i] + step], step):
                    return True
        return False

    return go(0, 0)
```

> ⚠️ **Python recursion depth**: same caveat — the frog can chain up to `N` stones (LC 403 allows 2000), so raise `sys.setrecursionlimit`, or use the standard iterative version: `reach[i]` = set of jump sizes that can land on stone `i`, filled left to right.

### Common Problems:
- LC 329: Longest Increasing Path in a Matrix
- LC 403: Frog Jump (state = position + last jump size)
- LC 1048: Longest String Chain (DAG over words; edge = "delete one char")
- LC 787: Cheapest Flights Within K Stops (state = `(city, stops used)`; the stop counter is what makes it acyclic)


## 16. Game Theory / Minimax DP

State: `dp[i][j]` = best score difference (current player − opponent) for subarray `[i..j]`.

```python
# LC 877 Stone Game — is first player guaranteed to win?
def stoneGame(piles):
    n = len(piles)
    # dp[i][j] = max score diff the current player can achieve on piles[i..j]
    dp = [[0] * n for _ in range(n)]
    for i in range(n):
        dp[i][i] = piles[i]
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])
    return dp[0][n-1] > 0

# LC 486 Predict the Winner — generalized version
def predictTheWinner(nums):
    n = len(nums)
    dp = [[0]*n for _ in range(n)]
    for i in range(n): dp[i][i] = nums[i]
    for length in range(2, n+1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])
    return dp[0][n-1] >= 0
```


## 17. DP on a DAG with Topological Sort

When DP transitions only go from earlier to later nodes in a DAG, process in topological order.

```python
# General DAG DP template
from collections import defaultdict, deque

def dag_dp(n, edges, source, source_value):
    graph = defaultdict(list)
    in_degree = [0] * n
    for u, v, w in edges:
        graph[u].append((v, w))
        in_degree[v] += 1

    dp = [float('-inf')] * n
    # NOTE: seed the ACTUAL source, not node 0. Nodes unreachable from `source`
    #       keep -inf, so they never win the final max().
    dp[source] = source_value

    # Kahn's traversal still starts from EVERY in-degree-0 node — that is what
    # guarantees topological order — but only `source` carries a real value.
    queue = deque([i for i in range(n) if in_degree[i] == 0])
    while queue:
        u = queue.popleft()
        for v, w in graph[u]:
            if dp[u] != float('-inf'):        # don't propagate -inf
                dp[v] = max(dp[v], dp[u] + w)
            in_degree[v] -= 1
            if in_degree[v] == 0:
                queue.append(v)
    return max(dp)
```


## 18. Monotonic Queue DP Optimization

When DP transition is `dp[i] = max/min(dp[j]) + cost` for `j` in a sliding window, use a monotonic deque for O(n) instead of O(n²).

```python
# dp[i] = max(dp[j]) + nums[i]  for j in [i-k, i-1]
from collections import deque

def slidingWindowDP(nums, k):
    n = len(nums)
    dp = [0] * n
    dp[0] = nums[0]
    dq = deque([0])  # stores indices, dp values are decreasing

    for i in range(1, n):
        # Remove indices outside window
        while dq and dq[0] < i - k:
            dq.popleft()
        dp[i] = dp[dq[0]] + nums[i]
        # Maintain decreasing order
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        dq.append(i)
    return dp[-1]
```

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


### Memoization vs Tabulation: When to Use Each
| Aspect | Memoization (Top-down) | Tabulation (Bottom-up) |
|--------|----------------------|----------------------|
| Code clarity | Closer to recursion → easier to write | Explicit order needed |
| Space | Stack frames + cache | Only DP table |
| Subproblems | Only computes needed subproblems | Computes all subproblems |
| Interview default | Start here | Switch if asked for O(1) space |
| Infinite recursion risk | Yes (cycles) | No |

**Rule**: In interviews, start with memoization (easier to verify correctness), then optimize to tabulation if space is a concern.


## LC Examples

### 2-1) Climbing Stairs (LC 70) — 1D Linear DP
> dp[i] = dp[i-1] + dp[i-2]; Fibonacci-style DP. See Section 2 (LIS) for the pattern; same space-optimized rolling-variable approach applies here.

### 2-2) Coin Change (LC 322) — Unbounded Knapsack DP
> dp[i] = minimum coins needed to make amount i; try all coin denominations.

```java
// LC 322 - Coin Change
// IDEA: dp[i] = min coins for amount i; dp[i] = min(dp[i], dp[i-coin] + 1)
// time = O(amount * coins), space = O(amount)
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

### 2-3) Longest Increasing Subsequence (LC 300) — LIS DP / Binary Search
> dp[i] = length of LIS ending at index i; optimized with patience sorting. See Section 2 (LIS) for both O(n²) and O(n log n) templates.

### 2-4) Partition Equal Subset Sum (LC 416) — 0/1 Knapsack DP
> dp[j] = true if subset summing to j exists; iterate items and update dp right to left. See Section 6 (0/1 Knapsack) for the full template and reverse-iteration explanation.

### 2-5) Unique Paths (LC 62) — 2D Grid DP
> dp[i][j] = paths to reach (i,j) = dp[i-1][j] + dp[i][j-1]; first row/col = 1.

```java
// LC 62 - Unique Paths
// IDEA: 2D DP — dp[i][j] = dp[i-1][j] + dp[i][j-1]
// time = O(M*N), space = O(N) with row compression
public int uniquePaths(int m, int n) {
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            dp[j] += dp[j-1];
    return dp[n-1];
}
```

### 2-6) Decode Ways (LC 91) — 1D DP
> dp[i] = number of ways to decode s[0..i-1]; consider 1-digit and 2-digit decodings.

```java
// LC 91 - Decode Ways
// IDEA: DP — dp[i] = ways to decode s[0..i-1]; check 1-char and 2-char decodings
// time = O(N), space = O(1)
public int numDecodings(String s) {
    int n = s.length(), prev2 = 1, prev1 = s.charAt(0) == '0' ? 0 : 1;
    for (int i = 2; i <= n; i++) {
        int curr = 0;
        int one = s.charAt(i-1) - '0';
        int two = Integer.parseInt(s.substring(i-2, i));
        if (one != 0) curr += prev1;
        if (two >= 10 && two <= 26) curr += prev2;
        prev2 = prev1; prev1 = curr;
    }
    return prev1;
}
```

### 2-7) Longest Common Subsequence (LC 1143) — 2D String DP
> dp[i][j] = LCS of s1[0..i-1] and s2[0..j-1]; diagonal + 1 on match, else max of neighbors.

```java
// LC 1143 - Longest Common Subsequence
// IDEA: 2D DP — dp[i][j] = LCS length for s1[0..i-1] and s2[0..j-1]
// time = O(M*N), space = O(M*N)
public int longestCommonSubsequence(String text1, String text2) {
    int m = text1.length(), n = text2.length();
    int[][] dp = new int[m+1][n+1];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            dp[i][j] = text1.charAt(i-1) == text2.charAt(j-1)
                ? dp[i-1][j-1] + 1
                : Math.max(dp[i-1][j], dp[i][j-1]);
    return dp[m][n];
}
```

### 2-8) Burst Balloons (LC 312) — Interval DP
> dp[i][j] = max coins from bursting all balloons between i and j; try each as last to burst.

```java
// LC 312 - Burst Balloons
// IDEA: Interval DP — dp[i][j] = max coins when k is the LAST balloon burst in (i,j)
// time = O(N^3), space = O(N^2)
public int maxCoins(int[] nums) {
    int n = nums.length;
    int[] arr = new int[n+2];
    arr[0] = arr[n+1] = 1;
    for (int i = 0; i < n; i++) arr[i+1] = nums[i];
    int[][] dp = new int[n+2][n+2];
    for (int len = 1; len <= n; len++)
        for (int l = 1; l <= n-len+1; l++) {
            int r = l + len - 1;
            for (int k = l; k <= r; k++)
                dp[l][r] = Math.max(dp[l][r], dp[l][k-1] + arr[l-1]*arr[k]*arr[r+1] + dp[k+1][r]);
        }
    return dp[1][n];
}
```

### 2-9) Best Time to Buy and Sell Stock with Cooldown (LC 309) — State Machine DP
> Three states: hold, sold, rest; transitions enforce cooldown after selling.

```java
// LC 309 - Best Time to Buy and Sell Stock with Cooldown
// IDEA: State machine DP — hold, sold, rest states
// time = O(N), space = O(1)
public int maxProfit(int[] prices) {
    int hold = Integer.MIN_VALUE, sold = 0, rest = 0;
    for (int price : prices) {
        int prevSold = sold;
        hold = Math.max(hold, rest - price);   // buy from rest state
        sold = hold + price;                    // sell
        rest = Math.max(rest, prevSold);        // cooldown or stay rest
    }
    return Math.max(sold, rest);
}
```

### 2-10) Minimum Path Sum (LC 64) — Grid DP
> dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1]); initialize borders first.

```java
// LC 64 - Minimum Path Sum
// IDEA: DP — dp[i][j] = min cost to reach (i,j); modify grid in-place
// time = O(M*N), space = O(1)
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    for (int i = 1; i < m; i++) grid[i][0] += grid[i-1][0];
    for (int j = 1; j < n; j++) grid[0][j] += grid[0][j-1];
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            grid[i][j] += Math.min(grid[i-1][j], grid[i][j-1]);
    return grid[m-1][n-1];
}
```

### 2-11) Target Sum (LC 494) — DP / DFS with Memo
> Assign + or − to each number; dp[j] = number of ways to reach sum j.

```java
// LC 494 - Target Sum
// IDEA: DP — equivalent to subset sum with positive/negative assignment
// time = O(N * sum), space = O(sum)
public int findTargetSumWays(int[] nums, int target) {
    int sum = 0;
    for (int n : nums) sum += n;
    if (Math.abs(target) > sum || (sum + target) % 2 != 0) return 0;
    int pos = (sum + target) / 2;
    int[] dp = new int[pos + 1];
    dp[0] = 1;
    for (int num : nums)
        for (int j = pos; j >= num; j--)
            dp[j] += dp[j - num];
    return dp[pos];
}
```

---