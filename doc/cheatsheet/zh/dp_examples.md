# DP 實戰題解

> **範圍** — [dp.md](./dp.md) 背後的題解檔案庫：每一題經典 DP 各一份標準 Java / Python 解法，加上依模式分類的題目索引，本身不放模板也不談理論。
> **另見**：[dp.md](./dp.md) — 這些解法所實作的模板；[dp_advanced.md](./dp_advanced.md) — 冷門技巧與深入探討；[dp_pattern.md](./dp_pattern.md) — 更精簡的「一個模式一節」模板索引；[stock_trading.md](./stock_trading.md) — 所有股票狀態機的變形。

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Memoization](https://leetcode.com/problem-list/memoization/)

## 總覽

只有解法。每個範例都會寫清楚狀態定義、轉移，以及每種語言各一份實作；推導出它的思路放在 [dp.md](./dp.md) 對應的模板裡。

### 關鍵性質
- **複雜度**：逐解法標示，寫在每段程式碼的第一行註解
- **核心想法**：先看模板、再看解法，順序別反過來
- **什麼時候用**：複習，以及拿自己的解法對照標準解

## 依模式分類的題目

### **線性 DP 題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Climbing Stairs | 70 | dp[i] = dp[i-1] + dp[i-2] | Easy |
| House Robber | 198 | 取最大值並跳過相鄰 | Medium |
| Longest Increasing Subsequence | 300 | O(n²) 或 O(nlogn) | Medium |
| Maximum Subarray | 53 | Kadane 演算法 | Easy |
| Decode Ways | 91 | 字串 DP | Medium |
| Word Break | 139 | 字典 DP | Medium |
| Coin Change | 322 | 最少硬幣數 | Medium |
| Integer Break | 343 | 最大乘積，拆 vs 不拆 | Medium |

### **二維網格題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Unique Paths | 62 | 路徑計數 | Medium |
| Minimum Path Sum | 64 | 最小成本路徑 | Medium |
| Maximal Square | 221 | 二維擴張 | Medium |
| Dungeon Game | 174 | 反向 DP | Hard |
| Cherry Pickup | 741 | 三維 DP | Hard |
| Number of Paths with Max Score | 1301 | 多值 DP | Hard |

### **區間 DP 題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Longest Palindromic Substring | 5 | 中心擴張或 DP | Medium |
| Palindrome Partitioning II | 132 | 最少切割數 | Hard |
| Burst Balloons | 312 | 區間相乘 | Hard |
| Minimum Cost to Merge Stones | 1000 | K 路合併 | Hard |
| Strange Printer | 664 | 區間列印 | Hard |

### **賽局理論／Minimax DP 題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Predict the Winner | 486 | 相對分差 dp[i][j] | Medium |
| Stone Game | 877 | 同 486（偶數長度時恆為 true） | Medium |
| Stone Game II | 1140 | 可拿數量會變動的 minimax | Medium |
| Stone Game III | 1406 | 後綴 minimax DP | Hard |
| Optimal Division | 553 | 從賽局理論得到的貪婪洞見 | Medium |

### **背包題目**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Partition Equal Subset Sum | 416 | 0/1 背包 | Medium |
| Target Sum | 494 | 湊到目標值 | Medium |
| Last Stone Weight II | 1049 | 最小差值 | Medium |
| Ones and Zeroes | 474 | 二維背包 | Medium |
| **Perfect Squares** | **279** | **完全背包（把平方數當硬幣，求最少個數）** | **Medium** |
| **Coin Change 2** | **518** | **完全背包（硬幣→金額 = 組合數）** | **Medium** |
| **Combination Sum IV** | **377** | **完全背包（金額→硬幣 = 排列數）** | **Medium** |

### **狀態機題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 狀態數 | 模式 |
|---------|------|---------------|------------|--------|---------|
| Best Time to Buy and Sell Stock II | 122 | 多次交易 | Easy | 2 個狀態 | hold/cash |
| **Stock with Cooldown** | **309** | **三狀態轉移** | **Medium** | **3 個狀態** | **hold/sold/rest** |
| Stock with Transaction Fee | 714 | 要考慮手續費 | Medium | 2 個狀態 | hold/cash |
| Stock III | 123 | 最多 2 次交易 | Hard | 4 個狀態 | buy1/sell1/buy2/sell2 |
| Stock IV | 188 | 最多 k 次交易 | Hard | 2k 個狀態 | 動態狀態 |

**核心模式分析：股票題**

| 題目 | 限制 | 需要的狀態 | 關鍵差異 |
|---------|-----------|---------------|----------------|
| **LC 122** | 交易次數不限 | 2（hold/cash） | 單純買賣 |
| **LC 309** | 賣出後要冷卻 | 3（hold/sold/rest） | 需要 rest 狀態 |
| **LC 714** | 有交易手續費 | 2（hold/cash） | 賣出時扣手續費 |
| **LC 123** | 最多 2 次交易 | 4（2 組買賣） | 要追蹤交易次數 |
| **LC 188** | 最多 k 次交易 | 2k 個狀態 | k 次交易的一般化 |

**狀態機模式辨識：**
```text
Question asks...                          → Use this pattern
─────────────────────────────────────────────────────────────
"Cooldown after action"                   → 3+ states (LC 309)
"Transaction fee/cost"                    → 2 states with cost
"Limited transactions (k times)"          → 2k states
"Unlimited transactions"                  → 2 states (hold/cash)
```

## 其他高頻 DP 題目

> 這些是很有名的題目，遞迴式直接就是上面某個模板的實例 —— 列出來是為了讓對應關係一目了然。

| 題目 | LC # | 化約到哪個模板 | 一句話想法 |
|---------|------|------------------------|---------------|
| Trapping Rain Water | 42 | 前綴／後綴陣列（一維 DP） | `water[i] = min(maxLeft[i], maxRight[i]) - h[i]`；改雙指標就不用那兩個陣列 |
| Jump Game | 55 | 一維可達性 DP → 貪婪 | 追蹤能到達的最遠索引 |
| Jump Game II | 45 | 一維 DP → BFS 式的貪婪分層 | `dp[i] = min(dp[j]+1)`，貪婪可以 O(n) 做完 |
| Minimum Cost For Tickets | 983 | 在天數上的一維線性 DP | `dp[d] = min(dp[d-1]+c1, dp[d-7]+c7, dp[d-30]+c30)` |
| 01 Matrix | 542 | 二維網格 DP（掃兩趟） | 第一趟左上→右下，第二趟反過來 |
| Counting Bits | 338 | 一維 DP 加位元小技巧 | `dp[i] = dp[i >> 1] + (i & 1)` |
| Is Subsequence | 392 | LCS 的退化情況 | 雙指標 O(n)；如果追問要處理大量查詢，就改用 LCS 表 |
| Pascal's Triangle / II | 118 / 119 | 一維滾動列 | `row[j] += row[j-1]`，而且要**由後往前**走 |

## 實戰題解

### 2-1) Unique Paths (LC 62) — 網格 DP 路徑計數

> dp[i][j] = dp[i-1][j] + dp[i][j-1]；在 m×n 網格上從左上走到右下的路徑數。

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


### 2-2) Maximum Product Subarray (LC 152) — 同時追蹤最小／最大乘積

> 每一步都要同時追最大值和最小值（最小值乘上負數之後可能翻成最大值）。

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

### 2-3) Best Time to Buy and Sell Stock with Transaction Fee (LC 714) — 雙狀態 DP

> hold/free 兩個狀態；轉移：hold = max(hold, free-price)、free = max(free, hold+price-fee)。

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

### 2-4) Best Time to Buy and Sell Stock with Cooldown (LC 309) — 三狀態 DP

> 狀態：held/sold/rest；sold → rest → held 這個循環強制了一天的冷卻期。

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

**範例走查：prices = [1,2,3,0,2]**

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

**狀態轉移追蹤（第 4 天）：**
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

**和一般股票題的關鍵差別：**

| 面向 | 一般股票題 (LC 122) | 有冷卻期 (LC 309) |
|--------|------------------------|------------------------------|
| **狀態** | 2（hold、cash） | 3（hold、sold、rest） |
| **限制** | 無 | 賣出後必須冷卻 |
| **買入轉移** | `hold = max(hold, cash - price)` | `hold = max(hold, rest - price)` |
| **為什麼不同？** | 隨時都能買 | 只有 rest 之後才能買（不能剛賣完就買） |
| **空間** | O(1) - 2 個變數 | O(1) - 3 個變數 |
| **複雜度** | O(n) 時間 | O(n) 時間 |

**常見錯誤：**
1. ❌ 只用 2 個狀態而不是 3 個（等於忽略了冷卻期）
2. ❌ 寫成 `hold = max(hold, sold - prices[i])`（賣完不能馬上買！）
3. ❌ 忘了在更新前先存下 `prevSold`（rest 就會算錯）
4. ❌ 回傳 `max(hold, sold, rest)`（結束時不能還抱著股票）

**這個模式為什麼成立：**
- **SOLD 狀態**：像一道「閘門」—— 一旦進來，就一定要經過 REST
- **REST 狀態**：把再次 BUY 的能力「解鎖」
- **HOLD 狀態**：擋住你去 REST（得先賣掉）

於是就形成一條被迫走的流程：`HOLD → SOLD → REST → HOLD`，冷卻期自然成立。

**相似題目：**
- LC 122: Best Time to Buy and Sell Stock II（沒有冷卻期，更單純）
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee（2 個狀態 + 手續費）
- LC 123: Best Time to Buy and Sell Stock III（2 次交易，4 個狀態）
- LC 188: Best Time to Buy and Sell Stock IV（k 次交易，2k 個狀態）

### 2-5) N-th Tribonacci Number (LC 1137) — 滾動三個變數

> T(n) = T(n-1) + T(n-2) + T(n-3)；維護三個滾動變數就好，不需要陣列。

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

### 2-6) Decode Ways (LC 91) — 線性 DP，檢查一位／兩位數

> dp[i] = 解碼 s[0..i] 的方法數；單一位數合法就加 dp[i-1]，兩位數合法就加 dp[i-2]。

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

**相似 LC：**
- LC 91 Decode Ways
- LC 639 Decode Ways II（有 `*` 萬用字元）
- LC 70 Climbing Stairs（同樣是類 Fibonacci 的結構）
- LC 509 Fibonacci Number
- LC 1137 N-th Tribonacci Number

### 2-7) Perfect Squares (LC 279) — 完全背包（最少個數）

> **核心想法**：把每個完全平方數（1、4、9、16……）當成一種「幣值」，求湊出金額 `n` 所需的最少硬幣數。這就是 **Coin Change**（LC 322）的模式，一模一樣。

**模式**：完全背包 —— 每個平方數可以用無限次，目標是最小化個數。

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

**為什麼跟 Coin Change 是同一回事**：
| | Coin Change (LC 322) | Perfect Squares (LC 279) |
|---|---|---|
| **「硬幣」** | 題目給的幣值 | 完全平方數：1、4、9、16…… |
| **「金額」** | 目標金額 | 目標值 `n` |
| **目標** | 湊到金額的最少硬幣數 | 加總成 `n` 的最少平方數個數 |
| **遞迴式** | `dp[i] = min(dp[i], dp[i - coin] + 1)` | `dp[i] = min(dp[i], dp[i - j*j] + 1)` |
| **可以重複用？** | 可以（完全背包） | 可以（完全背包） |

**注意**：平方數不需要排序 —— 求最少個數的 DP 不在意迴圈順序（排序是給貪婪／回溯用的，這裡沒用）。

**相似 LeetCode 題目**：
| 題目 | LC # | 相似之處 |
|---------|------|-----------|
| Coin Change | 322 | 完全相同的模式 —— 湊到金額的最少硬幣數 |
| Coin Change 2 | 518 | 硬幣的想法一樣，但改成算組合數 |
| Combination Sum IV | 377 | 硬幣的想法一樣，但改成算排列數 |
| Climbing Stairs | 70 | 更簡單的版本 —— 每步只能走 1 或 2 |

### 2-8) Integer Break (LC 343) — 線性 DP（拆 vs 不拆）

**模式**：線性 DP —— 對每個整數 `i`，試遍所有切點 `j`，再決定 `(i-j)` 要不要繼續往下拆。

**核心 DP 想法**：
```text
dp[i] = max product by breaking integer i into at least 2 positive integers

Transition:
  dp[i] = max over all j in [1, i-1] of:
    max(j * (i - j),      // don't break (i-j) further
        j * dp[i - j])    // break (i-j) further using its best product
```

關鍵洞見是**拆 vs 不拆的選擇**：把 `i` 切成 `j + (i-j)` 時，剩下的 `(i-j)` 可以原封不動留著，也可以繼續拆（用 `dp[i-j]`）。兩種都要考慮，因為 `dp[i-j]` 預設至少拆成 2 份，但有時候直接用 `(i-j)` 反而比較好（例如 `dp[2]=1`，但 `2` 這個值本身更大）。

**貪婪捷徑**：盡量拆成 3。如果餘數是 1，就把 `3+1` 換成 `2+2`（因為 `2×2 > 3×1`）。

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

**n=10 的 DP 表**：
```text
i:      1  2  3  4  5  6  7  8  9  10
dp[i]:  1  1  2  4  6  9 12 18 27 36
```

**相似 LeetCode 題目**：
| 題目 | LC # | 相似之處 |
|---------|------|-----------|
| Perfect Squares | 279 | 湊到 n 的最少個數（完全背包變形） |
| Coin Change | 322 | 在所有分解 n 的方式上做最佳化 |
| Unique Binary Search Trees | 96 | 試遍所有切點，再合併子問題的結果 |
| Maximum Product Subarray | 152 | 用 DP 最大化乘積 |
| Partition to K Equal Sum Subsets | 698 | 在限制下把整數切成若干份 |

### 2-9) Paint Fence (LC 276) — 雙狀態 DP（同色／異色）

> 用 `k` 種顏色漆 `n` 根柱子，且**相鄰同色的柱子不能超過 2 根**。把每根柱子拆成兩個狀態 —— 「和前一根同色」 vs 「和前一根不同色」 —— 然後往前滾動。

#### 1. 核心想法

對最後一根柱子 `i` 追蹤兩個狀態：

- `same[i]` = 柱子 `i` 和 `i-1` **同色**的方法數
- `diff[i]` = 柱子 `i` 和 `i-1` **不同色**的方法數

「相鄰同色不超過 2 根」這條規則意思是：**只有在前兩根不同色時，才可以漆成同色**（否則就變成連續 3 根）。這正是 `same[i]` 依賴 `diff[i-1]` 的原因。

```text
same[i] = diff[i - 1]                        # only extend a "different" run, else 3 in a row
diff[i] = (same[i-1] + diff[i-1]) * (k - 1)  # pick any of the other (k-1) colors

Base: same[0] = 0, diff[0] = k   (first post: k choices, "different" by convention)
Answer = same[n-1] + diff[n-1]
```

#### 2. 模式

**用滾動變數的雙狀態線性 DP** —— 和股票狀態機那一家（LC 309/714）是同一類，只是這裡的狀態編碼的是*局部相鄰限制*，不是買賣。因為每個狀態只需要前一步，把陣列壓成兩個純量即可 → **O(n) 時間、O(1) 空間**。

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

**邊界情況**：如果 `n > 2 && k == 1`，答案是 `0`（只有一種顏色，躲不掉連續 3 根）。

#### 3. 相似 LeetCode 題目

| 題目 | LC # | 相似之處 |
|---------|------|-----------|
| Climbing Stairs | 70 | 類 Fibonacci 的滾動遞迴式，O(1) 空間 |
| House Robber | 198 | 兩個隱含狀態（搶／跳過）往前滾 |
| Best Time to Buy/Sell with Cooldown | 309 | 帶相鄰式限制的多狀態機 |
| Best Time to Buy/Sell with Fee | 714 | 兩個滾動狀態（hold/cash） |
| Delete and Earn | 740 | 在數值上做取／不取的狀態 DP |
| Domino and Tromino Tiling | 790 | 用滾動狀態轉移數鋪法 |
