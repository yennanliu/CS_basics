# 從遞迴到動態規劃的轉換

> **範圍** — 一套機械化的流程：**遞迴 → 記憶化(memoization) → 表格化(tabulation) → 空間最佳化**，一次套用在一題上。
> **另見** — [dp.md](./dp.md)：動態規劃參考手冊；[dp_pattern.md](./dp_pattern.md)：你要轉換*成*的那些模式；[recursion.md](./recursion.md)：不含 DP 步驟的純遞迴。

- **核心想法**：把遞迴解法轉成迭代式 DP，換取更好的效能
- **何時使用**：具有重疊子問題與最佳子結構的遞迴解法
- **主要好處**：消除重複計算，把 O(n) 的呼叫堆疊空間降成 O(n) 或 O(1) 的陣列
- **常見模式**：先辨識出可記憶化的機會，再轉成由下而上的表格化

**轉換步驟：**
1. 找出base case
2. 找出遞迴關係式
3. 加上記憶化（Top-Down DP）
4. 轉成表格化（Bottom-Up DP）
5. 可以的話再做空間最佳化

---

## LeetCode 題目清單

- [Recursion](https://leetcode.com/problem-list/recursion/)
- [Memoization](https://leetcode.com/problem-list/memoization/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 0) 概念

### 0-0) 何時該把遞迴轉成 DP ⭐⭐⭐⭐

**適用 DP 的徵兆：**

> 「當你聽到題目是以下面這些句子開頭時，通常（雖然不總是）都是遞迴的好候選：『設計一個演算法計算第 n 個…』、『寫程式列出前 n 個…』、『實作一個方法計算所有的…』等等。」
> — *Cracking the Coding Interview*, 6th Edition, p.130

**DP 的必要條件：**
1. **重疊子問題**：同一個子問題被重複求解多次
2. **最佳子結構**：最佳解由子問題的最佳解組成
3. **可記憶化**：結果可以被快取下來重複使用

**辨識模式：**
- 「找出第 n 個…」
- 「計算有幾種方式…」
- 「最小／最大…」
- 「最佳化…」
- 多次以相同參數呼叫的遞迴

### 0-1) 轉換策略 ⭐⭐⭐⭐⭐

```text
Recursion (Exponential)
    ↓
Top-Down DP (Memoization)
    ↓
Bottom-Up DP (Tabulation)
    ↓
Space-Optimized DP
```

**Top-Down（記憶化）：**
- 保留遞迴結構
- 加上快取（memo）存放結果
- 從遞迴改過來很容易
- 空間：memo 佔 O(n) + 呼叫堆疊佔 O(n)

**Bottom-Up（表格化）：**
- 用迭代方式建構答案
- 從base case開始往上填 DP 表
- 沒有遞迴的額外開銷
- 空間：只有 DP 表的 O(n)

**空間最佳化：**
- 找出真正需要用到的是哪些先前狀態
- 通常可以從 O(n) 降到 O(k)，k 為常數
- 例如：費氏數列只需要最後 2 個值

---

## 1) 完整範例：遞迴 → DP

### 1-1) Fibonacci Sequence — LC 509

**題目：** 計算第 n 個費氏數，F(n) = F(n-1) + F(n-2)，F(0)=0、F(1)=1。

#### 步驟 1：樸素遞迴

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

**問題：** 大量重複計算！
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

#### 步驟 2：Top-Down DP（記憶化）

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

#### 步驟 3：Bottom-Up DP（表格化）

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

#### 步驟 4：空間最佳化的 DP

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

**小結：**

| 做法 | 時間 | 空間 | 說明 |
|----------|------|-------|-------|
| 樸素遞迴 | O(2^n) | O(n) | 指數級，n>40 就不能用 |
| 記憶化 | O(n) | O(n) + O(n) | 好寫，但仍有堆疊開銷 |
| 表格化 | O(n) | O(n) | 無遞迴，更乾淨 |
| 空間最佳化 | O(n) | O(1) | 整體最佳 |

---

### 1-2) Climbing Stairs (LC 70)

**題目：** 爬 n 階樓梯，每次可爬 1 或 2 階，共有幾種不同走法？

#### 步驟 1：遞迴

```python
def climbStairs_recursive(n):
    """Time: O(2^n), Space: O(n)"""
    if n <= 2:
        return n
    return climbStairs_recursive(n-1) + climbStairs_recursive(n-2)
```

#### 步驟 2：記憶化

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

#### 步驟 3：Bottom-Up DP

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

#### 步驟 4：空間最佳化

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

### 1-3) House Robber (LC 198) ⭐⭐⭐⭐

**題目：** 給一個房屋價值陣列，不能搶相鄰的兩戶，最多能搶到多少？

#### 步驟 1：遞迴

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

#### 步驟 2：記憶化

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

#### 步驟 3：Bottom-Up DP

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

#### 步驟 4：空間最佳化

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

### 1-4) Coin Change (LC 322) ⭐⭐⭐⭐

**題目：** 給硬幣面額陣列與目標金額，最少需要幾枚硬幣？

#### 步驟 1：遞迴

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

#### 步驟 2：記憶化

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

#### 步驟 3：Bottom-Up DP

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

## 2) 常見 LeetCode 題目

### 遞迴 → DP 的轉換

| 題目 | 難度 | 遞迴模式 | DP 類型 |
|---------|------------|-------------------|---------|
| LC 70 | Easy | Stairs = (n-1) + (n-2) | 一維 DP |
| LC 198 | Medium | Rob = max(rob, skip) | 一維 DP |
| LC 322 | Medium | 湊出金額的最少硬幣數 | 無限背包 |
| LC 509 | Easy | 費氏數列 | 經典題 |
| LC 746 | Easy | 爬樓梯最小花費 | 一維 DP |
| LC 139 | Medium | 斷句 | 字串 DP |
| LC 300 | Medium | 最長遞增 | 子序列 DP |
| LC 416 | Medium | 子集分割 | 0/1 背包 |

---

## 3) 轉換檢查清單

### ✅ 逐步指南

1. **找出base case**
   - 最單純的輸入是什麼？
   - 哪些情況可以直接回傳？

2. **找出遞迴關係式**
   - F(n) 和 F(n-1)、F(n-2) 等等的關係是什麼？
   - 每一步要做哪些選擇／決策？

3. **加上記憶化（Top-Down）**
   - 建立快取／memo 結構
   - 計算前先查快取
   - 計算後把結果存起來

4. **轉成表格化（Bottom-Up）**
   - 建立 DP 陣列
   - 填入base case
   - 由小到大遍歷子問題
   - 用遞迴關係式填表

5. **最佳化空間**
   - 找出真正需要哪些先前狀態
   - 可以的話用變數取代整個陣列

---

## 4) 面試提示

### 💡 辨識模式

**何時該懷疑是 DP：**
- 「找出第 n 個…」
- 「計算有幾種方式…」
- 「最小／最大…」
- 多次遞迴呼叫
- 重疊子問題

**轉換策略：**
1. 先從遞迴開始（比較好理解）
2. 加上記憶化（快速見效）
3. 面試官要求時再轉成 bottom-up
4. 時間允許就做空間最佳化

### 🎯 面試可以說的重點

1. **為什麼 DP 比較好：**
   - 「消除重複計算」
   - 「用空間換時間」
   - 「把指數時間變成線性時間」

2. **Top-Down 對比 Bottom-Up：**
   - 「Top-down 從遞迴改寫比較容易」
   - 「Bottom-up 效率更好（沒有堆疊開銷）」
   - 「兩者時間複雜度相同」

3. **空間最佳化：**
   - 「只需要前 k 個狀態」
   - 「可以從 O(n) 降到 O(k)」
   - 「一維 DP 題常見的做法」

### 📊 複雜度分析

| 做法 | 典型時間 | 典型空間 | 說明 |
|----------|--------------|---------------|-------|
| 樸素遞迴 | O(2^n) | O(n) | n>30 就不能用 |
| 記憶化 | O(n) | O(n) + O(n) | 好寫 |
| 表格化 | O(n) | O(n) | 效率更好 |
| 空間最佳化 | O(n) | O(1) 或 O(k) | 整體最佳 |

---

## 總結

**核心原則：**
- ✅ 遞迴 → 記憶化 → 表格化 → 空間最佳化
- ✅ 找出重疊子問題
- ✅ 記憶化保留遞迴結構，只是多了快取
- ✅ 表格化從base case開始迭代建構答案

**何時使用：**
- 「找出第 n 個…」類的題目
- 最佳化題（min/max）
- 計數題（有幾種方式）
- 每一步都要做選擇的題目

**面試策略：**
1. 先寫出遞迴解
2. 找出重疊子問題
3. 加上記憶化
4. 需要的話轉成 bottom-up
5. 可以的話做空間最佳化

**關鍵洞察：** 每一題 DP 都可以先用遞迴解出來，再用記憶化／表格化最佳化。

## 5) LC 範例

### 5-1) House Robber (LC 198) — 遞迴 → 記憶化 → DP
> 不能搶相鄰兩戶；dp[i] = max(dp[i-1], dp[i-2] + nums[i])。

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

### 5-2) Word Break (LC 139) — Top-down 遞迴 → DP
> 若 s[0..i] 可以用 wordDict 切分，則 dp[i] = true。

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

### 5-3) Edit Distance (LC 72) — 二維 DP（字串 → 字串）⭐⭐⭐⭐
> dp[i][j] = 把 s1[0..i] 轉成 s2[0..j] 所需的最少操作數。

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

### 5-4) Fibonacci Number (LC 509) — 遞迴 → 記憶化 → DP → O(1)
> 經典範例，完整展示從樸素遞迴開始的 4 個最佳化層級。

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

### 5-5) Unique Paths (LC 62) — 二維遞迴 → DP
> 遞迴 `f(i,j) = f(i-1,j) + f(i,j-1)` → 用滾動列壓縮的 bottom-up DP。

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

### 5-6) Triangle (LC 120) — Top-Down 遞迴 → Bottom-Up DP
> dp[i][j] = 從 (i,j) 走到底層的最小路徑和；把三角形遞迴改成由下而上。

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

### 5-7) Longest Palindromic Subsequence (LC 516) — 區間 DP
> `dp[i][j]` = s[i..j] 中的最長回文子序列長度；遞迴 `f(i,j)` → 依區間長度遞增由下而上填表。

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

### 5-8) Coin Change (LC 322) — 帶剪枝的遞迴 → DP
> 遞迴 `f(amount) = 1 + min(f(amount-coin))` → bottom-up 的無限背包 DP。

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

### 5-9) Combination Sum IV (LC 377) — 遞迴 → DP（順序有差）
> dp[i] = 總和為 i 的有序組合數；和背包不同，加入的順序會影響答案。

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

### 5-10) Minimum Cost to Cut a Stick (LC 1547) — 區間 DP
> dp[i][j] = 在 cut[i] 與 cut[j] 之間切完所有刀的最小成本；列舉中間那一刀。

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

### 5-11) Partition Array for Maximum Sum (LC 1043) — 一維 DP
> dp[i] = 把陣列分割到索引 i 為止的最大總和；嘗試所有大小為 1..k 的子分割。

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

## 6) 更多轉換模板（前面沒涵蓋到的狀態形狀）

第 1) 節和第 5) 節的所有題目，記憶化的鍵都是**單一索引**（或兩個沿固定方向掃描的索引）。
下面四個模板涵蓋的是打破這個假設的狀態形狀——而這些正是「遞迴 → 記憶化 → 表格」的轉換
變得有趣的地方。

### 6-0) 快速決策表 —「我的狀態長什麼形狀？」⭐⭐⭐⭐⭐

| 遞迴簽名 | 記憶化的鍵 | 能表格化嗎？ | 模板 | LC |
|---------------------|----------|-------------------|----------|-----|
| `f(i)` | `int[n]` | 可以，`i` 遞增迴圈 | §1-2 / §1-3 | 70, 198 |
| `f(i, mode)` — 附帶一個小旗標 | `int[n][K]` | 可以，`i` 跑迴圈、`mode` 展開 | **§6-1** | 122, 714, 121 |
| `f(i, k)` — `k` 由*你怎麼走到這裡*決定 | `HashMap` / 每個節點一個集合 | 可以，但要**往前推**到可達狀態 | **§6-2** | 403 |
| `f(i, j)` 在兩個序列上，其中一個索引可能**不前進** | `boolean[m+1][n+1]` | 可以，但要**反向**填（`i--`、`j--`） | **§6-3** | 10, 44 |
| `f(cell)` 在圖上，沒有明顯的掃描順序 | `int[m][n]` | 要先有明確的拓撲排序才行 | **§6-4** | 329 |

**經驗法則：** 記憶化永遠可行；表格化則要等你能講出一個順序，使每個狀態的相依項都已經算好了才行。
當你沒辦法輕鬆講出那個順序時，**就停在記憶化**——那是一個完整的答案，不是半套。

---

### 6-1) 狀態機 DP — `f(index, state)` — LC 122 ⭐⭐⭐⭐

> **和 §1-3 House Robber 的差別：** House Robber 是用*跳過一個索引*（`i+2`）來編碼「上一戶我拿了沒？」。
> 這裡的旗標沒辦法折進索引裡，所以它變成**第二個、很小的維度**。一旦你看出 `f(i, holding)`，
> 表格就是 `dp[n+1][2]`，而空間最佳化後的版本就是每個狀態一個變數。

**題目（LC 122 - Best Time to Buy and Sell Stock II）：** 交易次數不限，同一時間最多持有一股；求最大利潤。

#### 步驟 1：暴力遞迴 — 每一天有 2 種選擇

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

#### 步驟 2：記憶化 — 旗標加入鍵值

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

#### 步驟 3：表格化 — 遞迴是 `i → i+1`，所以表格要反向填

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

#### 步驟 4：空間最佳化 — 每個狀態一個變數，往前掃

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

**同一個模板，只改一行：**

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

### 6-2) 複合狀態記憶化 — 狀態不是索引 — LC 403

> **差別：** 在石頭 `i` 上可選的跳法取決於**你是用多大的一跳到這裡的**，所以 `f(i)` 根本沒有良好定義
> ——你需要 `f(i, k)`。`k` 幾乎是無界的，所以 memo 用雜湊表，而「表格」變成
> 「每顆石頭上，能落到它的**跳躍距離集合**」。

**題目（LC 403 - Frog Jump）：** 以跳距 `k` 到達石頭 `i` 後，下一跳必須是 `k-1`、`k` 或 `k+1`，
而且必須剛好落在某顆石頭上。青蛙能不能抵達最後一顆石頭？

#### 步驟 1 → 2：對 `(i, k)` 這組配對做遞迴 + 記憶化

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

#### 步驟 3：表格化 — 用**往前推**取代往回拉

這裡沒有 `dp[i] = combine(dp[i-1], ...)` 這種形式，因為在你知道是誰跳到石頭 `i` 之前，
根本不知道在 `i` 上*哪些*跳法合法。所以把方向反過來：對每顆石頭，把所有能到達它的跳距，
傳播到它能跳到的那些石頭上。

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

### 6-3) 索引可能**不前進**的雙序列比對 — LC 10

> **和 §5-3 Edit Distance 的差別：** 編輯距離每一步一定會縮小 `i`、`j` 或兩者，所以表格用遞增迴圈
> 自然就填得起來。有了 `*` 之後，`f(i+1, j)` 這一支**讓 `j` 不動**——遞迴走的是 `i → i+1`、`j → j+2`，
> 所以表格必須從**末端往回**填。先寫記憶化版本，再從遞迴呼叫讀出迴圈方向，就是整個訣竅。

**題目（LC 10 - Regular Expression Matching）：** `.` 匹配任一單一字元，`x*` 匹配零個或多個 `x`。要求完整匹配。

#### 步驟 1 → 2：先遞迴，再記憶化（遞迴本身才是難的部分）

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

#### 步驟 3：表格化 — 機械式翻譯，迴圈反著跑

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

**變化題 — LC 44 Wildcard Matching：** 這裡的 `*` 是**獨立**的符號（不是「前一個字元重複」），
所以它自己就能吃掉零個或多個字元：`dp[i][j] = dp[i][j+1] || dp[i+1][j]`。
不用往前看 `p[j+1]`，也沒有 `j+2`。

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

### 6-4) DAG 上的記憶化 — 表格化需要明確順序時 — LC 329

> **差別：** 前面每個模板都自帶掃描順序（索引、長度、列）。在一個可以往 **4 個方向任意移動**的
> 格子圖上，並沒有這種順序——所以純粹的記憶化 DFS *就是*標準解。這正是「轉成 bottom-up」
> 要付出一次排序／拓撲走訪的代價，卻只換到「不用遞迴深度」這一點好處的情況。

**題目（LC 329 - Longest Increasing Path in a Matrix）：** 上下左右移動，求最長的嚴格遞增路徑。
嚴格遞增 ⇒「移動」關係圖是一張 **DAG** ⇒ 沒有環 ⇒ 記憶化是安全的。

#### 步驟 1 + 2：遞迴加記憶化（面試時該寫的答案）

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

#### 步驟 3：表格化 — 你得*自己製造*順序（把格子依值排序）

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

**帶走的重點：** bottom-up 版本*更慢*（多一個 `log` 因子）而且更長。面試時要把這句話講出來——
「這題記憶化 DFS 就是最佳解，表格化還得先做拓撲排序」——而不是反射性地去做轉換。

---

## 7) 本文既有模板的變化題

每一列都沿用上面的某個模板；最後一欄點出唯一改變的那件事。

| LC | 題目 | 沿用 | 差異點 |
|----|---------|--------|-----------|
| 91 | Decode Ways | §1-2 Climbing Stairs | 一樣是 `f(i-1)+f(i-2)` 的形狀，但每一支都有**條件限制**：一位數只有在 `s[i] != '0'` 時成立，兩位數只有在 `10 <= s[i-1..i] <= 26` 時成立。空間最佳化到 O(1) 的做法完全相同。 |
| 337 | House Robber III | §1-3 House Robber | 同樣的搶／跳過選擇，只是搬到**樹**上。memo 是 `Map<TreeNode,Integer>`；改成後序走訪回傳 `(rob, skip)` 配對就完全不需要 memo。 |
| 63 | Unique Paths II | §5-5 Unique Paths | 多了障礙物：一樣的滾動列，只是遇到被擋住的格子時 `dp[j] = 0`，而不是 `dp[j] += dp[j-1]`。 |
| 1143 | Longest Common Subsequence | §5-3 Edit Distance | 同樣是兩個字串上的 `dp[i][j]` 格子；相同時 `dp[i-1][j-1]+1`，否則 `max(dp[i-1][j], dp[i][j-1])`。答案在 `dp[m][n]`。 |
| 718 | Maximum Length of Repeated Subarray | §5-3 Edit Distance | 就是 LCS，但要求**連續**：不相符時 `dp[i][j] = 0` 歸零，而且答案是**整張表的最大值**，不是角落那格。 |
| 647 | Palindromic Substrings | §5-7 Longest Palindromic Subsequence | 同樣的區間表，內容改成布林值：`dp[i][j] = s[i]==s[j] && (j-i<3 || dp[i+1][j-1])`；數有幾個 `true`。 |
| 494 | Target Sum | §2 的 LC 416（0/1 背包） | 狀態是 `(i, runningSum)`，而總和可能是**負的**——用雜湊表 memo，或整體平移 `total` 來當陣列索引。 |
| 221 | Maximal Square | §5-5 格子 DP | `dp[i][j] = min(up, left, diag) + 1`（是**取 min**，不是加總），答案是全域最大邊長的平方。 |
| 152 | Maximum Product Subarray | §1-3 滾動狀態 | 需要**兩個**滾動狀態（`maxEnding`、`minEnding`），因為遇到負數會讓兩者互換。 |

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

## 8) 直接套用既有模板即可（參考）

| LC | 題目 | 對應到 |
|----|---------|-----------|
| 53 | Maximum Subarray | §1-3 滾動狀態 — `f(i) = max(nums[i], nums[i] + f(i-1))`，另外維護全域最大值（Kadane） |
| 279 | Perfect Squares | §1-4 Coin Change — 硬幣就是完全平方數 `1,4,9,...`，求最少個數 |
| 55 / 45 | Jump Game / Jump Game II | `O(N^2)` 的 DP 是很自然的第一個答案；接著把貪婪 `O(N)` 的可達性／BFS 分層解法當作 follow-up 講出來 |
