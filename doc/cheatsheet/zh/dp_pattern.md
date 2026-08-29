# DP 模式總表

> **範圍** — **模板索引**：每個經典 DP 模式各佔一小節（Kadane、LIS、MCM、LCS、背包、狀態機、格子、位元遮罩、數位、樹上 DP、正規表達式、加權區間排程、切分、記憶化 DAG）。
> **另見**：[dp.md](./dp.md) — 這些模板背後的解釋與實作範例；[recursion_to_dp.md](./recursion_to_dp.md) — 怎麼從一段遞迴*推導*出其中一個模板。

- https://leetcode.com/discuss/study-guide/1308617/Dynamic-Programming-Patterns

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 1. Kadane 演算法（最大子陣列） ⭐⭐⭐⭐⭐

**模式**：求一段連續子陣列的最大／最小和。

**核心想法**：在每個位置決定要延續當前子陣列，還是重新開一段。

**遞迴式**：`dp[i] = max(nums[i], dp[i-1] + nums[i])`

**時間複雜度**：O(n) | **空間複雜度**：O(1)

### 模板程式碼：

**Python：**
```python
def maxSubArray(nums):
    max_sum = nums[0]
    current_sum = nums[0]

    for i in range(1, len(nums)):
        current_sum = max(nums[i], current_sum + nums[i])
        max_sum = max(max_sum, current_sum)

    return max_sum
```

**Java：**
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

### 常見題目：
- LC 53: Maximum Subarray
- LC 152: Maximum Product Subarray
- LC 918: Maximum Sum Circular Subarray
- LC 1749: Maximum Alternating Sum
- 二進位字串中 0 與 1 個數差的最大值
- 最小和的連續子陣列
- 最大和的遞增連續子陣列
- 二維矩陣中的最大和矩形


## 2. 最長遞增子序列（LIS） ⭐⭐⭐⭐

**模式**：找出元素遞增的最長子序列。

**核心想法**：對每個元素，求出以該位置結尾的最長遞增子序列。

**遞迴式**：對所有滿足 `nums[j] < nums[i]` 的 `j < i`，`dp[i] = max(dp[j] + 1)`

**時間複雜度**：O(n²)，或搭配二分搜尋的 O(n log n) | **空間複雜度**：O(n)

### 模板程式碼（O(n²) 版）：

**Python：**
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

**Java：**
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

### 模板程式碼（搭配二分搜尋的 O(n log n) 版）：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 300: Longest Increasing Subsequence
- LC 673: Number of Longest Increasing Subsequence
- LC 334: Increasing Triplet Subsequence
- LC 1626: Best Team with No Conflicts
- LC 1964: Find the Longest Valid Obstacle Course at Each Position
- LC 2111: Minimum Number of Removals to Make Mountain Array
- LC 354: Russian Doll Envelopes（二維 LIS，見下方變形）
- LC 1048: Longest String Chain（「比較小」＝*是前驅字串*；先依字長排序，再套 LIS 邏輯）
- 最大和遞增子序列
- 印出 LIS（`Longest Increasing Subsequence`）
- 和幾乎為 K 的 LIS

### 變形：二維 LIS — Russian Doll Envelopes（LC 354）

> **轉折**：寬度**升冪**排序，但寬度相同時改用高度**降冪**當次序。這個降冪的平手規則讓兩個等寬信封不可能互相嵌套，於是問題塌成單純在高度上跑 O(n log n) 的 LIS。

**Java：**
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

**Python：**
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


## 3. 矩陣連乘（MCM）／區間 DP ⭐⭐⭐⭐

**模式**：在不同位置切開，把問題拆成子問題，再把結果合起來。

**核心想法**：試遍所有切分區間的方式，取最佳的那個。

**遞迴式**：對 `[i, j)` 中所有 `k`，`dp[i][j] = min/max(dp[i][k] + dp[k+1][j] + cost)`

**時間複雜度**：O(n³) | **空間複雜度**：O(n²)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 312: Burst Balloons
- LC 1039: Minimum Score Triangulation of Polygon
- LC 87: Scramble String
- LC 131: Palindrome Partitioning
- LC 132: Palindrome Partitioning II
- LC 1547: Minimum Cost to Cut a Stick
- LC 1000: Minimum Cost to Merge Stones
- LC 96 / LC 95: Unique Binary Search Trees (I / II) — 以**根節點**切分區間：`dp[n] = Σ dp[i-1] * dp[n-i]`（卡特蘭數）；LC 95 回傳的是樹本身而不是數量
- 布林運算式加括號使其為 True
- 運算式的最小／最大值
- 丟雞蛋問題


## 4. 最長共同子序列（LCS） ⭐⭐⭐⭐⭐

**模式**：找出兩個序列共同的最長子序列。

**核心想法**：字元相同就延長 LCS；不同就取「跳過其中一邊」的較大值。

**遞迴式**：
- 若 `s1[i] == s2[j]`：`dp[i][j] = dp[i-1][j-1] + 1`
- 否則：`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**時間複雜度**：O(m*n) | **空間複雜度**：O(m*n) 或 O(min(m,n))

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
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
- LC 97: Interleaving String（同一張雙字串格子，但 `dp[i][j]` = `s1[0..i)` + `s2[0..j)` *能不能*交錯成 `s3[0..i+j)`）
- LC 718: Maximum Length of Repeated Subarray（這其實是陣列上的最長共同**子字串**——不相符就歸零）
- 最長共同子字串
- 印出 LCS / SCS
- 把字串 a 變成 b 的最少插入／刪除次數
- 最長重複子序列
- 子序列模式比對
- 計算 a 以子序列形式在 b 中出現幾次

## 5. 完全背包 ⭐⭐⭐⭐

**模式**：物品數量無限，選出組合來最大化／最小化價值，或計算方案數。

**核心想法**：每個物品可以重複使用。要決定的是：再拿一次當前物品，還是換到下一個物品。

**遞迴式**：`dp[i][w] = max(dp[i-1][w], dp[i][w-weight[i]] + value[i])`

**時間複雜度**：O(n*W) | **空間複雜度**：O(W)

### 與 0/1 背包的關鍵差異

| 變形 | 迴圈順序 | 為什麼 |
|---------|-----------|-----|
| 0/1 背包 | 外層：物品，內層：容量**反向** | 每個物品最多用一次 |
| 完全背包 | 外層：物品，內層：容量**正向** | 物品可以重複使用 |

### 模板程式碼（以 Coin Change 為例）：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 322: Coin Change（最少硬幣數）
- LC 518: Coin Change II（方案數）
- LC 377: Combination Sum IV
- LC 139: Word Break
- LC 140: Word Break II（同樣的 `dp[i]` 切分判斷，但記憶化的是**句子清單**而不是布林值）
- LC 472: Concatenated Words（拿*其他*單字當字典，對每個單字跑 Word Break；先依長度排序，字典裡就只會有比較短的字）
- LC 279: Perfect Squares（硬幣 = 所有 ≤ n 的完全平方數；求最少個數）
- LC 1155: Number of Dice Rolls With Target Sum（有界／分組背包：剛好 `k` 顆骰子，每顆貢獻 1..f）
- LC 983: Minimum Cost For Tickets
- 切鋼條問題
- 剪緞帶最大段數
- 數字分割


## 6. 0/1 背包 ⭐⭐⭐⭐⭐

**模式**：每個物品最多用一次，選出組合來最大化／最小化價值，或計算方案數。

**核心想法**：對每個物品，決定拿或不拿。

**遞迴式**：`dp[i][w] = max(dp[i-1][w], dp[i-1][w-weight[i]] + value[i])`

**時間複雜度**：O(n*W) | **空間複雜度**：O(W)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 416: Partition Equal Subset Sum
- LC 494: Target Sum
- LC 698: Partition to K Equal Sum Subsets
- LC 1049: Last Stone Weight II
- LC 474: Ones and Zeroes（二維背包）
- 子集合和
- 和為指定值的子集合個數
- 最小子集合和差
- 差值為指定值的子集合個數


## 7. 狀態機 DP ⭐⭐⭐⭐

**模式**：狀態會依動作／決策而轉移的問題。

**核心想法**：追蹤各種狀態以及它們之間的轉移。買賣股票題最常見。

**時間複雜度**：O(n*狀態數) | **空間複雜度**：O(狀態數)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 121: Best Time to Buy and Sell Stock
- LC 122: Best Time to Buy and Sell Stock II
- LC 123: Best Time to Buy and Sell Stock III
- LC 188: Best Time to Buy and Sell Stock IV
- LC 309: Best Time to Buy and Sell Stock with Cooldown
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee
- LC 198: House Robber（搶／不搶兩種狀態）
- LC 213: House Robber II
- LC 801: Minimum Swaps To Make Sequences Increasing（每個索引 2 種狀態：**交換過** / **保持原樣**；轉移是否合法要同時看 `A`/`B` 的比較結果）
- LC 926: Flip String to Monotone Increasing（2 種狀態：前綴結尾是 `0` / 結尾是 `1`；翻轉成本依狀態各自累加）


## 8. 格子路徑 DP ⭐⭐⭐⭐

**模式**：在格子上計算路徑數，或求最小／最大成本路徑。

**核心想法**：每個格子只取決於能走到它的那些格子（通常是上方、左方或斜對角）。

**遞迴式**：`dp[i][j] = dp[i-1][j] + dp[i][j-1]`（用於計算路徑數）

**時間複雜度**：O(m*n) | **空間複雜度**：O(n)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 62: Unique Paths
- LC 63: Unique Paths II
- LC 64: Minimum Path Sum
- LC 120: Triangle
- LC 174: Dungeon Game
- LC 221: Maximal Square
- LC 931: Minimum Falling Path Sum
- LC 1594: Maximum Non Negative Product in a Matrix
- LC 1277: Count Square Submatrices with All Ones（遞迴式和 LC 221 Maximal Square 完全一樣——把 dp 表**加總**起來，而不是取最大值）
- LC 688: Knight Probability in Chessboard（機率格子 DP：`dp[k][r][c]` = 走 `k` 步後仍在棋盤上的機率；8 種走法各帶 `1/8` 的權重）
- LC 764: Largest Plus Sign（四個方向的連續長度前綴 DP——上／下／左／右——再在每格取最小值）


## 9. 位元遮罩 DP

**模式**：資料規模很小（n ≤ 20）時，用位元遮罩表示子集合／狀態。

**核心想法**：每個位元代表某個元素有沒有被選中／走訪過。走遍所有可能狀態。

**時間複雜度**：O(2^n * n) 或 O(2^n * n²) | **空間複雜度**：O(2^n)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 847: Shortest Path Visiting All Nodes
- LC 943: Find the Shortest Superstring
- LC 1125: Smallest Sufficient Team
- LC 1434: Number of Ways to Wear Different Hats to Each Other
- LC 1595: Minimum Cost to Connect Two Groups of Points
- LC 2172: Maximum AND Sum of Array
- LC 464: Can I Win（用位元遮罩記*已使用的數字*，再加上賽局理論的勝負記憶化）
- LC 691: Stickers to Spell Word（用位元遮罩記目標字串裡哪些字母已經湊齊）
- 旅行推銷員問題
- 指派問題


## 10. 數位 DP

**模式**：計算某個範圍內滿足特定「數位性質」的數有幾個。

**核心想法**：一位一位把數字組出來，同時追蹤各種限制（是否貼著上界、前導零等等）。

**時間複雜度**：O(位數 * 狀態數) | **空間複雜度**：O(位數 * 狀態數)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 233: Number of Digit One
- LC 357: Count Numbers with Unique Digits
- LC 600: Non-negative Integers without Consecutive Ones
- LC 902: Numbers At Most N Given Digit Set
- LC 1012: Numbers With Repeated Digits
- LC 2376: Count Special Integers


### 另一種模板——計算 `[0, n]` 中滿足性質 P 的整數個數
> LC 233、LC 1012。想法和上面一樣，只是把狀態明確寫成 `(position, tight, ...)`。
計算 `[1, n]` 中滿足某個數位限制的數的個數。

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

關鍵狀態變數：
- `pos`：當前的數位位置
- `tight`：是否仍然被 `n` 的位數卡住
- 任何題目專屬的計數器（1 的個數、數位和等等）


## 11. 樹上 DP

**模式**：依子樹的值算出樹節點上的值。

**核心想法**：用 DFS／後序走訪先解完子節點，再在父節點把結果合起來。

**時間複雜度**：O(n) | **空間複雜度**：O(樹高)

### 模板程式碼：

**Python：**
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

**Java：**
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

### 常見題目：
- LC 124: Binary Tree Maximum Path Sum
- LC 337: House Robber III
- LC 543: Diameter of Binary Tree
- LC 687: Longest Univalue Path
- LC 968: Binary Tree Cameras
- LC 979: Distribute Coins in Binary Tree
- LC 1130: Minimum Cost Tree From Leaf Values
- LC 2246: Longest Path With Different Adjacent Characters


## 12. 萬用字元／正規表達式比對 DP ⭐⭐⭐⭐⭐

**辨識訊號**：有兩個字串，但它們**不對稱**——一個是文字 `s`，另一個是含有萬用字元（`*`、`?`、`.`）的*樣式* `p`。答案是布林值。貪婪會失敗，因為 `*` 可以吃掉任意多個字元。

**模式**：和 LCS 一樣的二維前綴格子，但轉移是由**樣式字元**驅動，而不是由「相等」驅動。

**核心想法**：`dp[i][j]` = `s[0..i)` 能不能比對上 `p[0..j)`。`*` 給你一個二選一：*再吃掉一個文字字元*（停在 `*` 上）或*把 `*` 丟掉*。

**遞迴式**（LC 44，`*` = 任意序列）：
- `p[j-1] == '*'` → `dp[i][j] = dp[i-1][j]（星號吃掉 s[i-1]） || dp[i][j-1]（星號什麼都不吃）`
- `p[j-1] == '?'` 或字元相等 → `dp[i][j] = dp[i-1][j-1]`

**時間複雜度**：O(m*n) | **空間複雜度**：O(m*n) → 用滾動列可降到 O(n)

> ⚠️ **這題大家都死在基底列**：當樣式是一串 `*` 時，`dp[0][j]`（空文字）必須保持 `true`——否則 `"" vs "***"` 就會錯。

### 模板程式碼：

**Java：**
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

**Python：**
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

### 變形：`*` 綁定前一個字元 — LC 10 Regular Expression Matching

> **轉折**：在正規表達式裡，`*` 是**作用在 `p[j-2]` 上的量詞**，不是一個獨立的萬用字元。所以「用零次」要跳過**兩個**樣式字元（`dp[i][j-2]`），而「再用一次」只有在 `p[j-2]` 真的比對得上 `s[i-1]` 時才允許。

**Java：**
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

**Python：**
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

### LC 44 vs LC 10 — 只差這兩行

| | LC 44 的 `*`（萬用字元） | LC 10 的 `*`（量詞） |
|---|---|---|
| 意義 | 任意序列，獨立存在 | `p[j-2]` 出現 0 次以上 |
| 「比對空字串」 | `dp[i][j-1]`（丟掉 1 個字元） | `dp[i][j-2]`（丟掉 2 個字元） |
| 「再多比對一個」 | `dp[i-1][j]` — 永遠允許 | `dp[i-1][j]` — 只有 `p[j-2]` 比對得上 `s[i-1]` 才行 |
| 基底列 | 遇到 `*` 時 `dp[0][j] = dp[0][j-1]` | 遇到 `*` 時 `dp[0][j] = dp[0][j-2]` |

### 常見題目：
- LC 44: Wildcard Matching
- LC 10: Regular Expression Matching
- LC 97: Interleaving String（格子形狀相同，轉移不同）
- LC 72: Edit Distance（格子形狀相同，求最小成本而不是布林值）


## 13. 加權區間排程 DP（排序 + 二分搜尋）

**辨識訊號**：物件是**帶價值的區間**（`start`、`end`、`profit`），你要選一組**互不重疊**的子集合把價值最大化，而且 `n` 很大（10⁴–10⁵），所以「跟前面每一個都比一次」的 O(n²) DP 太慢。一旦區間各自帶了不同權重，純貪婪（像「最多能選幾個不重疊區間」那種）就**不管用**了。

**模式**：依**結束時間**排序，然後用二分搜尋找出每個物件的前驅。

**核心想法**：依結束時間排序後，`dp[i]` = 用前 `i` 個工作能拿到的最佳利潤。工作 `i` 要嘛不選（`dp[i-1]`），要嘛選；選的話，所有與它相容的工作剛好是排序後陣列的一段**前綴**——一次二分搜尋就找得到。

**遞迴式**：`dp[i] = max(dp[i-1], dp[p(i)] + profit[i])`，其中 `p(i)` = 滿足 `end <= start[i]` 的工作數量

**時間複雜度**：O(n log n) | **空間複雜度**：O(n)

### 模板程式碼：

**Java：**
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

**Python：**
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

### 變形：加一個「最多選 k 個」的維度 — LC 1751 Maximum Number of Events That Can Be Attended II

> **轉折**：一樣是「依結束時間排序 + 二分搜尋」的骨架，只是多一個表示預算的維度：
> `dp[i][j] = max(dp[i-1][j], dp[p(i)][j-1] + value_i)` — O(n·k·log n)。

### 常見題目：
- LC 1235: Maximum Profit in Job Scheduling
- LC 1751: Maximum Number of Events That Can Be Attended II（上限 `k` 個區間）
- LC 646: Maximum Length of Pair Chain（沒有權重 → 貪婪也行）
- LC 300: Longest Increasing Subsequence（一維版本的「用二分搜尋找前驅」）


## 14. 切成 K 段連續區塊（切分 DP）

**辨識訊號**：「把陣列切成／把工作排成**剛好 `k` 段連續的部分**」，而目標函數定義在這些部分上（各段最大值的總和、各段總和的最大值、各段成本的總和）。注意這些部分必須是**連續的**——這正是它和背包的分野。

**模式**：二維 DP，第二個維度是*還剩幾段可以用*。和區間／MCM DP 不同：MCM 是遞迴地切成兩半；這裡是從左到右把序列切成 `k` 個區塊。

**核心想法**：`dp[i][k]` = 用剛好 `k` 段覆蓋後綴 `a[i..n)` 的最佳成本。列舉**第一段**在哪裡結束，並且邊走邊維護第一段的 `max`／`sum`，讓每次轉移都是 O(1)。

**遞迴式**：`dp[i][k] = min over j >= i of ( cost(a[i..j]) + dp[j+1][k-1] )`

**時間複雜度**：O(n² * k) | **空間複雜度**：O(n * k)

> 可行性守衛：若 `n < k`，元素不夠湊出 `k` 個非空的部分 → 回傳 -1。

### 模板程式碼：

**Java：**
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

**Python：**
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

### 變形：最小化最大的那一段，而不是總和 — LC 410 Split Array Largest Sum

> **轉折**：目標函數是用 `max` 而不是 `+` 把各段合起來：
> `dp[i][t] = min over j of max( dp[j][t-1], sum(a[j..i)) )` — O(n²k)。
> 因為答案具有單調性（「每段都 ≤ X 的切法存在嗎？」），LC 410 *也*可以用**在答案上二分搜尋**在 O(n log ΣA) 解掉——面試時兩種都提，然後實作二分搜尋那版。

### 常見題目：
- LC 1335: Minimum Difficulty of a Job Schedule
- LC 410: Split Array Largest Sum（也可以在答案上二分搜尋）
- LC 813: Largest Sum of Averages
- LC 1043: Partition Array for Maximum Sum（區塊長度上限為 `k`——那個*上限*取代了「段數」這個維度）
- LC 132: Palindrome Partitioning II（每段都必須是迴文；最小化段數）


## 15. 隱式 DAG 上的記憶化 DFS

**辨識訊號**：移動**不**限制在「往右／往下」，也看不出明顯的處理順序——但有一個**嚴格單調的限制**（格子值嚴格遞增、跳躍嚴格往前）保證不會有環。這讓狀態圖變成一個 DAG，於是單純的 DFS + 記憶化就合法了，複雜度是 O(狀態數)。

**模式**：當你沒辦法輕鬆地手動把狀態拓撲排序時，就讓遞迴自己去發現順序，每個狀態只快取一次。（對照第 8 節的格子路徑 DP，那裡的列優先順序*就是*拓撲順序。）

**核心想法**：`memo[state]` = 從該狀態*出發*的子問題答案。每個狀態只展開一次；每條邊只鬆弛一次。

**遞迴式**：對單調限制允許的邊 `u → v`，`f(u) = 1 + max(f(v))`（沒有這種邊時 `f(u) = 1`）

**時間複雜度**：O(V + E)，格子的話是 O(m*n) | **空間複雜度**：O(m*n)

### 模板程式碼：

**Java：**
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

**Python：**
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

> ⚠️ **Python 的遞迴深度**：記憶化省的是*計算量*，不是*堆疊深度*——一條遞增路徑可能橫跨全部 `M*N` 個格子（LC 329 允許 200×200 = 40000），遠遠超過 CPython 預設的 1000 層。加上 `sys.setrecursionlimit(10**6)`（Java 沒這問題），或者改寫成迭代版：照拓撲順序一層層剝掉格子（先處理出度為 0 的，再用 BFS 一層一層來）。

### 變形：狀態必須帶上「上一步」 — LC 403 Frog Jump

> **轉折**：光知道某顆石頭可達還不夠——下一步能跳多遠取決於你剛剛跳了多遠，所以狀態是 `(石頭索引, 上一次跳躍距離)` 這個配對。看出「位置本身不構成狀態」就是這題的全部。

**Java：**
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

**Python：**
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

> ⚠️ **Python 的遞迴深度**：同樣的警告——青蛙可以連跳到 `N` 顆石頭（LC 403 允許 2000），所以要調高 `sys.setrecursionlimit`，或改用標準的迭代版：`reach[i]` = 能落在石頭 `i` 上的跳躍距離集合，由左往右填。

### 常見題目：
- LC 329: Longest Increasing Path in a Matrix
- LC 403: Frog Jump（狀態 = 位置 + 上一次跳躍距離）
- LC 1048: Longest String Chain（在單字上建 DAG；邊 = 「刪掉一個字元」）
- LC 787: Cheapest Flights Within K Stops（狀態 = `(city, stops used)`；正是那個轉機計數讓它無環）


## 16. 賽局理論／Minimax DP

狀態：`dp[i][j]` = 子陣列 `[i..j]` 上的最佳分數差（當前玩家 − 對手）。

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


## 17. 搭配拓撲排序的 DAG 上 DP

當 DP 轉移只會從 DAG 中較前面的節點指向較後面的節點時，就照拓撲順序處理。

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


## 18. 單調佇列的 DP 最佳化

當 DP 轉移形如「`dp[i] = max/min(dp[j]) + cost`，其中 `j` 落在一個滑動視窗裡」時，用單調雙端佇列把 O(n²) 壓成 O(n)。

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

## DP 解題的關鍵步驟

1. **判斷這是不是 DP 題**：找最佳子結構與重疊子問題
2. **定義狀態**：哪些參數能唯一決定一個子問題？
3. **定義遞迴關係**：子問題之間怎麼互相牽連？
4. **找出基底情況**：最小的子問題長什麼樣？
5. **決定做法**：由上而下（記憶化）還是由下而上（表格化）？
6. **最佳化空間**：能不能降維，或改用滾動陣列？


## DP 最佳化技巧

- **空間最佳化**：只需要前一列／前一行時，用一維陣列取代二維
- **滾動陣列**：只保留最近 k 列／k 個狀態，而不是全部
- **狀態壓縮**：用位元遮罩把狀態壓起來
- **單調佇列／堆疊**：最佳化以視窗為基礎的 DP（滑動視窗最大值）
- **矩陣快速冪**：處理 n 很大的線性遞迴
- **凸包優化（Convex Hull Trick）**：最佳化某些特定形式的遞迴關係


### 記憶化 vs 表格化：什麼時候用哪個
| 面向 | 記憶化（由上而下） | 表格化（由下而上） |
|--------|----------------------|----------------------|
| 程式碼清晰度 | 貼近遞迴 → 比較好寫 | 需要明確安排順序 |
| 空間 | 堆疊框架 + 快取 | 只有 DP 表 |
| 子問題 | 只算需要用到的子問題 | 所有子問題都算 |
| 面試預設 | 從這裡開始 | 被要求 O(1) 空間時再換 |
| 無窮遞迴風險 | 有（遇到環） | 沒有 |

**原則**：面試時先用記憶化（比較好驗證正確性），如果空間是個問題，再最佳化成表格化。


## LC 範例

### 2-1) Climbing Stairs (LC 70) — 一維線性 DP
> dp[i] = dp[i-1] + dp[i-2]；費氏數列風格的 DP。模式看第 2 節（LIS）；同樣的滾動變數空間最佳化在這裡也適用。

### 2-2) Coin Change (LC 322) — 完全背包 DP
> dp[i] = 湊出金額 i 所需的最少硬幣數；把所有幣值都試一遍。

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

### 2-3) Longest Increasing Subsequence (LC 300) — LIS DP／二分搜尋
> dp[i] = 以索引 i 結尾的 LIS 長度；用 patience sorting 最佳化。O(n²) 與 O(n log n) 兩份模板都在第 2 節（LIS）。

### 2-4) Partition Equal Subset Sum (LC 416) — 0/1 背包 DP
> dp[j] = 是否存在總和為 j 的子集合；走訪物品並由右往左更新 dp。完整模板與「為什麼要反向走訪」的說明在第 6 節（0/1 背包）。

### 2-5) Unique Paths (LC 62) — 二維格子 DP
> dp[i][j] = 走到 (i,j) 的路徑數 = dp[i-1][j] + dp[i][j-1]；第一列／第一行都是 1。

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

### 2-6) Decode Ways (LC 91) — 一維 DP
> dp[i] = 解碼 s[0..i-1] 的方法數；同時考慮 1 位數與 2 位數的解碼。

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

### 2-7) Longest Common Subsequence (LC 1143) — 二維字串 DP
> dp[i][j] = s1[0..i-1] 與 s2[0..j-1] 的 LCS；相符時取斜對角 + 1，否則取相鄰兩格的最大值。

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

### 2-8) Burst Balloons (LC 312) — 區間 DP
> dp[i][j] = 戳破 i 與 j 之間所有氣球能拿到的最大金幣；把每顆都試著當成最後戳的那顆。

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

### 2-9) Best Time to Buy and Sell Stock with Cooldown (LC 309) — 狀態機 DP
> 三個狀態：hold、sold、rest；轉移規則強制賣出後要冷凍一天。

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

### 2-10) Minimum Path Sum (LC 64) — 格子 DP
> dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])；先把邊界初始化好。

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

### 2-11) Target Sum (LC 494) — DP／帶記憶化的 DFS
> 為每個數字指派 + 或 −；dp[j] = 湊出總和 j 的方法數。

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
