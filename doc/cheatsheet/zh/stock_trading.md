# Best Time to Buy and Sell Stock（買賣股票的最佳時機）

> **範圍** — LC 121/122/123/188/309/714 這個買賣家族，全部統一到同一個 `dp[i][k][hold]` 狀態機底下。
> **另見**：[dp_pattern.md](./dp_pattern.md) — 通用的狀態機 DP（§7）；[kadane_algorithm.md](./kadane_algorithm.md) — 同一條遞迴式，換成最大子陣列的講法；[greedy.md](./greedy.md) — 什麼時候貪婪捷徑才成立。

> 用動態規劃處理股票交易題

## LeetCode 題目清單

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Array](https://leetcode.com/problem-list/array/)
- [Greedy](https://leetcode.com/problem-list/greedy/)

## 0) 概念

### 0-1) 題型分類

- **單次交易**：買一次、賣一次（LC 121）
- **多次交易**：交易次數不限（LC 122）
- **K 次交易**：最多 k 次交易（LC 123、LC 188）
- **帶限制**：冷凍期（LC 309）、交易手續費（LC 714）
- **特殊變形**：加權工作排程（LC 1235）

### 0-2) 模式

**狀態機 DP 模式**：
- 追蹤不同狀態：`hold`（持股）、`sold`（沒持股）
- 考慮限制條件：交易次數、冷凍期、手續費
- 依買／賣動作在狀態之間轉移

**核心洞見**：每一天我們可能處在不同狀態，而每個狀態都要各自記住最大利潤。

## 1) 通用形式

### 1-1) 基本操作

#### 狀態定義
```text
# Basic states
hold[i] = max profit when holding stock at day i
sold[i] = max profit when not holding stock at day i

# With transaction count
buy[i][k]  = max profit after at most k transactions, currently holding
sell[i][k] = max profit after at most k transactions, currently not holding
```

#### 狀態轉移
```python
# Basic transitions
hold[i] = max(hold[i-1], sold[i-1] - prices[i])  # Keep holding or buy
sold[i] = max(sold[i-1], hold[i-1] + prices[i])  # Keep not holding or sell
```

#### 模板程式碼
```python
def maxProfit(prices):
    n = len(prices)
    if n <= 1:
        return 0
    
    # Initialize states
    hold = -prices[0]  # Bought on first day
    sold = 0           # No action on first day
    
    for i in range(1, n):
        new_hold = max(hold, sold - prices[i])  # Keep holding or buy
        new_sold = max(sold, hold + prices[i])  # Keep sold or sell
        hold, sold = new_hold, new_sold
    
    return sold  # Must end without holding stock
```

## 2) LC 範例

### LC 121: Best Time to Buy and Sell Stock（單次交易）
```python
def maxProfit(prices):
    """
    At most 1 transaction (1 buy + 1 sell)
    Track minimum price seen so far and max profit
    """
    min_price = float('inf')
    max_profit = 0
    
    for price in prices:
        min_price = min(min_price, price)
        max_profit = max(max_profit, price - min_price)
    
    return max_profit

# State machine approach
def maxProfit(prices):
    hold = -prices[0]  # Max profit when holding stock
    sold = 0           # Max profit when not holding stock
    
    for i in range(1, len(prices)):
        hold = max(hold, -prices[i])         # Buy at prices[i] or keep holding
        sold = max(sold, hold + prices[i])   # Sell at prices[i] or keep sold
    
    return sold
```

### LC 122: Best Time to Buy and Sell Stock II（不限次數）
```python
def maxProfit(prices):
    """
    Unlimited transactions - greedy approach
    Buy before every price increase
    """
    profit = 0
    for i in range(1, len(prices)):
        if prices[i] > prices[i-1]:
            profit += prices[i] - prices[i-1]
    return profit

# State machine approach
def maxProfit(prices):
    hold = -prices[0]
    sold = 0
    
    for i in range(1, len(prices)):
        hold = max(hold, sold - prices[i])   # Can buy multiple times
        sold = max(sold, hold + prices[i])
    
    return sold
```

### LC 714: Best Time to Buy and Sell Stock with Transaction Fee（含手續費）
```python
def maxProfit(prices, fee):
    """
    Unlimited transactions with fee
    Pay fee when selling
    """
    hold = -prices[0]
    sold = 0
    
    for i in range(1, len(prices)):
        hold = max(hold, sold - prices[i])
        sold = max(sold, hold + prices[i] - fee)  # Subtract fee when selling
    
    return sold
```

### LC 309: Best Time to Buy and Sell Stock with Cooldown（含冷凍期）
```python
def maxProfit(prices):
    """
    Unlimited transactions with 1 day cooldown
    After selling, must wait 1 day before buying
    """
    if len(prices) <= 1:
        return 0
    
    # Three states: hold, sold (can buy tomorrow), rest (just sold, cooldown)
    hold = -prices[0]
    sold = 0
    rest = 0
    
    for i in range(1, len(prices)):
        prev_hold, prev_sold, prev_rest = hold, sold, rest
        hold = max(prev_hold, prev_rest - prices[i])  # Buy after cooldown
        sold = prev_hold + prices[i]                  # Sell -> enter cooldown
        rest = max(prev_sold, prev_rest)              # Continue resting
    
    return max(sold, rest)  # Don't hold stock at the end
```

### LC 123: Best Time to Buy and Sell Stock III（最多 2 次交易）
```python
def maxProfit(prices):
    """
    At most 2 transactions (k=2)
    Track states for each transaction
    """
    # First transaction
    buy1 = -prices[0]
    sell1 = 0
    # Second transaction
    buy2 = -prices[0]
    sell2 = 0
    
    for i in range(1, len(prices)):
        buy1 = max(buy1, -prices[i])           # First buy
        sell1 = max(sell1, buy1 + prices[i])   # First sell
        buy2 = max(buy2, sell1 - prices[i])    # Second buy (use profit from first)
        sell2 = max(sell2, buy2 + prices[i])   # Second sell
    
    return sell2
```

### LC 188: Best Time to Buy and Sell Stock IV（最多 K 次交易）
```python
def maxProfit(k, prices):
    """
    At most k transactions
    Optimize for large k (unlimited case)
    """
    n = len(prices)
    if n <= 1 or k == 0:
        return 0
    
    # If k >= n//2, it's equivalent to unlimited transactions
    if k >= n // 2:
        profit = 0
        for i in range(1, n):
            if prices[i] > prices[i-1]:
                profit += prices[i] - prices[i-1]
        return profit
    
    # DP for limited transactions
    buy = [-prices[0]] * k   # buy[i] = max profit after at most i+1 buys
    sell = [0] * k           # sell[i] = max profit after at most i+1 sells
    
    for i in range(1, n):
        for j in range(k):
            buy[j] = max(buy[j], (sell[j-1] if j > 0 else 0) - prices[i])
            sell[j] = max(sell[j], buy[j] + prices[i])
    
    return sell[k-1]
```

### LC 1235: Maximum Profit in Job Scheduling（加權工作排程）
```python
def jobScheduling(startTime, endTime, profit):
    """
    Similar to stock trading but with weighted intervals
    Use DP with binary search for optimization
    """
    jobs = sorted(zip(startTime, endTime, profit), key=lambda x: x[1])
    n = len(jobs)
    
    # dp[i] = max profit considering jobs 0 to i
    dp = [0] * n
    dp[0] = jobs[0][2]
    
    def findLatestNonOverlap(i):
        # Binary search for latest job that doesn't overlap with job i
        left, right = 0, i - 1
        result = -1
        
        while left <= right:
            mid = (left + right) // 2
            if jobs[mid][1] <= jobs[i][0]:
                result = mid
                left = mid + 1
            else:
                right = mid - 1
        
        return result
    
    for i in range(1, n):
        # Option 1: Don't take current job
        profit_without = dp[i-1]
        
        # Option 2: Take current job
        profit_with = jobs[i][2]
        latest_non_overlap = findLatestNonOverlap(i)
        if latest_non_overlap != -1:
            profit_with += dp[latest_non_overlap]
        
        dp[i] = max(profit_without, profit_with)
    
    return dp[n-1]
```

## 3) 關鍵模式與技巧

### 模式 1：狀態機 DP
- **何時用**：有多個狀態、彼此會轉移
- **狀態**：hold、sold、rest（處理冷凍期時）
- **轉移**：買入（sold -> hold）、賣出（hold -> sold）

### 模式 2：交易次數計數
- **何時用**：交易次數有上限
- **技巧**：分開追蹤買／賣的配對
- **最佳化**：若 k >= n/2，直接當成不限次數

### 模式 3：處理限制條件
- **冷凍期**：加一個 rest 狀態，賣出後延後才能買
- **手續費**：在賣出的轉移上扣掉手續費
- **多重限制**：把狀態變數組合起來

### 模式 4：空間最佳化
- **滾動變數**：只需要前一個狀態時，用變數取代陣列
- **降維**：k 很大時把 k 次交易的 DP 最佳化掉

## 4) 時間與空間複雜度

| 題目 | 時間 | 空間 | 關鍵洞見 |
|---------|------|-------|-------------|
| LC 121 | O(n) | O(1) | 追蹤最低價與最大利潤 |
| LC 122 | O(n) | O(1) | 貪婪：每次上漲前都先買進 |
| LC 714 | O(n) | O(1) | 狀態機，賣出時扣手續費 |
| LC 309 | O(n) | O(1) | 三個狀態：hold、sold、rest |
| LC 123 | O(n) | O(1) | 2 次交易用四個狀態 |
| LC 188 | O(nk) | O(k) | 通用的 k 次交易，k 很大時要最佳化 |

## 5) 常見錯誤與提醒

### 錯誤
- **漏掉限制條件**：冷凍期或手續費沒處理好
- **狀態定義寫錯**：把買／賣次數和交易次數搞混
- **邊界條件**：沒處理邊界情況（空陣列、只有一天）
- **k 很大時沒最佳化**：k >= n/2 時沒有做簡化

### 提醒
- **一律回傳 sold 狀態**：結束時絕不能還持股
- **初始化要小心**：第一天的狀態很關鍵
- **變數名要有意義**：用 `hold`、`sold`，不要用 `dp[0]`、`dp[1]`
- **不限次數時考慮貪婪**：不限交易次數的話，貪婪寫法更簡單

## 6) 相關題目
- LC 121: Best Time to Buy and Sell Stock
- LC 122: Best Time to Buy and Sell Stock II  
- LC 123: Best Time to Buy and Sell Stock III
- LC 188: Best Time to Buy and Sell Stock IV
- LC 309: Best Time to Buy and Sell Stock with Cooldown
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee
- LC 1235: Maximum Profit in Job Scheduling

---

## 7) 統一的 `dp[i][k][hold]` 狀態機 ⭐⭐⭐⭐⭐

> **要背就背這一個模板。** 六題股票題（121 / 122 / 123 / 188 / 309 / 714）都是*同一條*遞迴式——差別只在 `k` 要代什麼進去，加上兩個可選的微調（冷凍期、手續費）。面試時推導這一次，每個變形都只差一行。

### 7-1) 狀態定義與遞迴式

**狀態**

```text
dp[i][k][0] = max profit on day i, having used at most k transactions, NOT holding stock
dp[i][k][1] = max profit on day i, having used at most k transactions, HOLDING stock
```

**核心想法**：一筆交易在**買入時**計數（一買一賣 = 一筆交易）。正因為算在買入，`k` 的遞減才會出現在 `hold` 那一行——選一種慣例，然後貫徹到底。

**遞迴式**

```text
dp[i][k][0] = max( dp[i-1][k][0] ,  dp[i-1][k][1] + prices[i] )    # rest  |  sell
dp[i][k][1] = max( dp[i-1][k][1] ,  dp[i-1][k-1][0] - prices[i] )  # rest  |  buy (spends a transaction)
```

**基底情況**

```text
dp[-1][k][0] = 0        # no days elapsed, no stock  -> profit 0
dp[-1][k][1] = -INF     # impossible to hold before day 0
dp[i][0][0]  = 0        # 0 transactions allowed, not holding -> 0
dp[i][0][1]  = -INF     # impossible to hold with 0 transactions
```

**答案**：`dp[n-1][K][0]`——結束時一定是**沒有**持股。

#### 每一題怎麼代進去

| LC | 限制 | 代入什麼 |
|----|-----------|---------|
| 121 | 最多 1 筆交易 | `k = 1`（於是 `dp[i-1][k-1][0]` 塌成 `0`） |
| 122 | 不限次數 | `k = +inf` -> 整個 `k` 維度直接拿掉 |
| 123 | 最多 2 筆 | `k = 2` |
| 188 | 最多 k 筆 | `k` 照題目給（＋ `k >= n/2` 的捷徑） |
| 309 | 冷凍 1 天 | 從 `dp[i-2][k-1][0]` 買，而不是 `dp[i-1][k-1][0]` |
| 714 | 每筆交易收費 | 賣出那行變成 `dp[i-1][k][1] + prices[i] - fee` |

**為什麼 `k >= n/2` 就等於不限次數**：一筆交易至少要 2 天（一天買、一天賣），所以最多只有 `floor(n/2)` 筆交易有意義。超過之後 `k` 這個上限就綁不住任何東西了。

### 7-2) 模板——完整 3D 表格

```java
// java
// LC 188 - Best Time to Buy and Sell Stock IV (unified 3D form)
// IDEA: dp[i][k][hold] state machine; transaction is counted at BUY.
//       Direct transcription of the recurrence - use this to DERIVE, then optimize.
class Solution {
    // time = O(n*k), space = O(n*k)
    public int maxProfit(int k, int[] prices) {
        int n = prices.length;
        if (n == 0 || k == 0) return 0;
        final int NEG = Integer.MIN_VALUE / 2;   // -INF, safe from overflow
        int[][][] dp = new int[n][k + 1][2];

        for (int i = 0; i < n; i++) {
            dp[i][0][0] = 0;                     // 0 transactions -> 0 profit
            dp[i][0][1] = NEG;                   // 0 transactions -> cannot hold
            for (int j = 1; j <= k; j++) {
                if (i == 0) {                    // base row
                    dp[0][j][0] = 0;
                    dp[0][j][1] = -prices[0];
                    continue;
                }
                dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i]);
                dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i]);
            }
        }
        return dp[n - 1][k][0];
    }
}
```

```python
# python
# LC 188 - Best Time to Buy and Sell Stock IV (unified 3D form)
# IDEA: dp[i][k][hold] state machine; transaction is counted at BUY.
def maxProfit(k, prices):
    # time = O(n*k), space = O(n*k)
    n = len(prices)
    if n == 0 or k == 0:
        return 0
    NEG = float('-inf')
    dp = [[[0, NEG] for _ in range(k + 1)] for _ in range(n)]

    for i in range(n):
        dp[i][0][0], dp[i][0][1] = 0, NEG        # 0 transactions allowed
        for j in range(1, k + 1):
            if i == 0:                           # base row
                dp[0][j][0], dp[0][j][1] = 0, -prices[0]
                continue
            dp[i][j][0] = max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i])
            dp[i][j][1] = max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i])

    return dp[n - 1][k][0]
```

### 7-3) 空間最佳化——滾動的 `O(k)` 陣列

> 第 `i` 列只讀第 `i-1` 列，所以 `i` 維度可以拿掉。**走訪順序的陷阱**：同一天之內，要*先*更新 `hold[j]`，*再*更新 `sold[j]`。原地讀到當天的 `sold[j-1]` 只會模擬出「用同一個價格賣掉再買回來」，淨值為 0——無害，不會把答案灌水。

```java
// java
// LC 188 - Best Time to Buy and Sell Stock IV (rolling arrays)
// IDEA: same recurrence, i-dimension dropped. Includes the k >= n/2 unlimited shortcut,
//       which is what keeps LC 188 from TLE-ing when k is huge (k up to 1e9).
class Solution {
    // time = O(n*k), space = O(k)
    public int maxProfit(int k, int[] prices) {
        int n = prices.length;
        if (n == 0 || k == 0) return 0;

        if (k >= n / 2) {                        // cap not binding -> LC 122 greedy
            int profit = 0;
            for (int i = 1; i < n; i++) profit += Math.max(0, prices[i] - prices[i - 1]);
            return profit;
        }

        final int NEG = Integer.MIN_VALUE / 2;
        int[] hold = new int[k + 1], sold = new int[k + 1];
        java.util.Arrays.fill(hold, NEG);        // cannot hold before any buy

        for (int p : prices) {
            for (int j = 1; j <= k; j++) {
                hold[j] = Math.max(hold[j], sold[j - 1] - p);   // buy: consumes j-th transaction
                sold[j] = Math.max(sold[j], hold[j] + p);       // sell
            }
        }
        return sold[k];
    }
}
```

```python
# python
# LC 188 - Best Time to Buy and Sell Stock IV (rolling arrays)
# IDEA: same recurrence, i-dimension dropped, plus the k >= n//2 unlimited shortcut.
def maxProfit(k, prices):
    # time = O(n*k), space = O(k)
    n = len(prices)
    if n == 0 or k == 0:
        return 0

    if k >= n // 2:                              # cap not binding -> LC 122 greedy
        return sum(max(0, prices[i] - prices[i - 1]) for i in range(1, n))

    NEG = float('-inf')
    hold = [NEG] * (k + 1)
    sold = [0] * (k + 1)

    for p in prices:
        for j in range(1, k + 1):
            hold[j] = max(hold[j], sold[j - 1] - p)   # buy: consumes j-th transaction
            sold[j] = max(sold[j], hold[j] + p)       # sell

    return sold[k]
```

**驗算**：`maxProfit(2, [3,2,6,5,0,3]) == 7`、`maxProfit(2, [2,4,1]) == 2`、`maxProfit(2, [3,3,5,0,0,3,1,4]) == 6`。

#### 視覺化追蹤 — LC 123（`k=2`）跑 `[3,3,5,0,0,3,1,4]`

```text
day  price | hold[1] sold[1] | hold[2] sold[2]
  0    3   |   -3      0     |   -3      0
  1    3   |   -3      0     |   -3      0
  2    5   |   -3      2     |   -3      2
  3    0   |    0      2     |    2      2
  4    0   |    0      2     |    2      2
  5    3   |    0      3     |    2      5
  6    1   |    0      3     |    2      5
  7    4   |    0      4     |    2      6   <- answer 6 (buy 0 sell 3, buy 1 sell 4)
```

### 7-4) 變形：把冷凍期與手續費放進統一遞迴式

> **關鍵轉折**：兩個變形都不需要新模板——冷凍期只是把買入的*來源列*往前挪，手續費只是在*賣出*那條邊上減掉一個常數。兩者都是不限 `k`，所以 `k` 維度消失，只剩兩個滾動純量。

```text
LC 309 (cooldown): dp[i][1] = max(dp[i-1][1], dp[i-2][0] - prices[i])   # buy from 2 days back
LC 714 (fee):      dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i] - fee)
```

```java
// java
// LC 309 - Best Time to Buy and Sell Stock with Cooldown
// IDEA: unlimited k, so drop the k dim. Cooldown = buy reads sold[i-2], not sold[i-1].
class Solution {
    // time = O(n), space = O(1)
    public int maxProfit(int[] prices) {
        int n = prices.length;
        if (n == 0) return 0;
        int hold = -prices[0], sold = 0, prevSold = 0;   // prevSold = sold[i-2]
        for (int i = 1; i < n; i++) {
            int tmp = sold;                              // stash sold[i-1]
            hold = Math.max(hold, prevSold - prices[i]);
            sold = Math.max(sold, hold + prices[i]);
            prevSold = tmp;
        }
        return sold;
    }
}

// LC 714 - Best Time to Buy and Sell Stock with Transaction Fee
// IDEA: unlimited k; charge the fee once per transaction, on the sell edge.
class Solution2 {
    // time = O(n), space = O(1)
    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;
        if (n == 0) return 0;
        int hold = -prices[0], sold = 0;
        for (int i = 1; i < n; i++) {
            hold = Math.max(hold, sold - prices[i]);
            sold = Math.max(sold, hold + prices[i] - fee);
        }
        return sold;
    }
}
```

```python
# python
# LC 309 - Best Time to Buy and Sell Stock with Cooldown
# IDEA: unlimited k; cooldown = buy reads sold[i-2] instead of sold[i-1].
def maxProfitCooldown(prices):
    # time = O(n), space = O(1)
    if not prices:
        return 0
    hold, sold, prev_sold = -prices[0], 0, 0     # prev_sold = sold[i-2]
    for i in range(1, len(prices)):
        tmp = sold                               # stash sold[i-1]
        hold = max(hold, prev_sold - prices[i])
        sold = max(sold, hold + prices[i])
        prev_sold = tmp
    return sold


# LC 714 - Best Time to Buy and Sell Stock with Transaction Fee
# IDEA: unlimited k; charge fee once per transaction, on the sell edge.
def maxProfitFee(prices, fee):
    # time = O(n), space = O(1)
    if not prices:
        return 0
    hold, sold = -prices[0], 0
    for i in range(1, len(prices)):
        hold = max(hold, sold - prices[i])
        sold = max(sold, hold + prices[i] - fee)
    return sold
```

**驗算**：`maxProfitCooldown([1,2,3,0,2]) == 3`、`maxProfitFee([1,3,2,8,4,9], 2) == 8`。

**注意**：把手續費收在*買入*那條邊（`hold = max(hold, sold - prices[i] - fee)`）也一樣正確，答案相同——就是千萬別兩邊都收。

---

## 8) 同一副骨架，換個故事 — LC 53 ⭐⭐⭐⭐

> 這不是股票題，但骨架和**單次交易**完全一樣。面試時值得花 60 秒講，因為它顯示你看得出這層歸約，而不是硬背了六個模板。

### 8-1) LC 121 **就是**在價差上跑 Kadane

**核心想法**：第 `b` 天買、第 `s` 天賣，賺 `prices[s] - prices[b]`，望遠鏡式地展開就是 `sum(delta[b+1..s])`，其中 `delta[i] = prices[i] - prices[i-1]`。所以*單次交易的最大利潤* = *差分陣列的最大子陣列和*，再夾在 0 以上（空子陣列＝什麼都不做）。

```text
prices  = [7, 1, 5, 3, 6, 4]
delta   = [  -6, 4,-2, 3,-2]
max subarray of delta = [4,-2,3] = 5   ==  LC 121 answer 5
```

| 方向 | 說明 |
|-----------|-----------|
| 53 -> 121 | 在 `delta` 上跑 Kadane，累計和的下限夾在 0 |
| 121 -> 53 | LC 53 的 `cur` 就是 LC 121 的「我還抱著最近那次最佳買點時的利潤」 |
| 122 | 拿掉「只能一段子陣列」的限制 -> **所有**正的價差加總 |

```java
// java
// LC 53 - Maximum Subarray (Kadane)
// IDEA: cur = best sum of a subarray ENDING at i; either extend or restart at nums[i].
class Solution {
    // time = O(n), space = O(1)
    public int maxSubArray(int[] nums) {
        int cur = nums[0], best = nums[0];
        for (int i = 1; i < nums.length; i++) {
            cur  = Math.max(nums[i], cur + nums[i]);   // extend | restart
            best = Math.max(best, cur);
        }
        return best;                                   // may be negative (no empty subarray)
    }
}

// LC 121 - Best Time to Buy and Sell Stock, written AS Kadane on deltas
// IDEA: same loop, but clamp cur at 0 because "do nothing" (empty subarray) is allowed.
class Solution2 {
    // time = O(n), space = O(1)
    public int maxProfit(int[] prices) {
        int cur = 0, best = 0;
        for (int i = 1; i < prices.length; i++) {
            cur  = Math.max(0, cur + prices[i] - prices[i - 1]);   // clamp = "restart the buy here"
            best = Math.max(best, cur);
        }
        return best;
    }
}
```

```python
# python
# LC 53 - Maximum Subarray (Kadane)
# IDEA: cur = best sum of a subarray ENDING at i; either extend or restart.
def maxSubArray(nums):
    # time = O(n), space = O(1)
    cur = best = nums[0]
    for x in nums[1:]:
        cur = max(x, cur + x)          # extend | restart
        best = max(best, cur)
    return best                        # may be negative (no empty subarray)


# LC 121 - Best Time to Buy and Sell Stock, written AS Kadane on deltas
# IDEA: same loop, clamped at 0 because "do nothing" is allowed.
def maxProfit(prices):
    # time = O(n), space = O(1)
    cur = best = 0
    for i in range(1, len(prices)):
        cur = max(0, cur + prices[i] - prices[i - 1])   # clamp = "restart the buy here"
        best = max(best, cur)
    return best
```

**要講出來的那一個差別**：LC 53 不允許空子陣列（全負的輸入必須回傳最大的那個單一元素，例如 `[-1] -> -1`），而 LC 121 允許什麼都不做（`[7,6,4,3,1] -> 0`）。就那一個下限夾取，是兩題之間的全部差距。

**驗算**：`maxSubArray([-2,1,-3,4,-1,2,1,-5,4]) == 6`、`maxSubArray([-1]) == -1`、`maxProfit([7,1,5,3,6,4]) == 5`、`maxProfit([7,6,4,3,1]) == 0`。
