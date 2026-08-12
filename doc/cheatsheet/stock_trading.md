# Best Time to Buy and Sell Stock

> **Scope** — The LC 121/122/123/188/309/714 buy-and-sell family, unified under one `dp[i][k][hold]` state machine.
> **See also**: [dp_pattern.md](./dp_pattern.md) — generic state-machine DP (§7); [kadane_algorithm.md](./kadane_algorithm.md) — the same recurrence as a max-subarray problem; [greedy.md](./greedy.md) — when the greedy shortcut is valid.

> Dynamic Programming approach to stock trading problems

## LeetCode Problem Lists

- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)
- [Array](https://leetcode.com/problem-list/array/)
- [Greedy](https://leetcode.com/problem-list/greedy/)

## 0) Concept

### 0-1) Types

- **Single Transaction**: Buy once, sell once (LC 121)
- **Multiple Transactions**: Unlimited transactions (LC 122)
- **K Transactions**: At most k transactions (LC 123, LC 188)
- **With Constraints**: Cooldown period (LC 309), transaction fee (LC 714)
- **Special Variants**: Weighted job scheduling (LC 1235)

### 0-2) Pattern

**State Machine DP Pattern**:
- Track different states: `hold` (own stock), `sold` (don't own stock)
- Consider constraints: transaction count, cooldown, fees
- Transition between states based on buy/sell actions

**Core Insight**: At each day, we can be in different states and need to track the maximum profit for each state.

## 1) General Form

### 1-1) Basic OP

#### State Definition
```text
# Basic states
hold[i] = max profit when holding stock at day i
sold[i] = max profit when not holding stock at day i

# With transaction count
buy[i][k]  = max profit after at most k transactions, currently holding
sell[i][k] = max profit after at most k transactions, currently not holding
```

#### State Transitions
```python
# Basic transitions
hold[i] = max(hold[i-1], sold[i-1] - prices[i])  # Keep holding or buy
sold[i] = max(sold[i-1], hold[i-1] + prices[i])  # Keep not holding or sell
```

#### Template Code
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

## 2) LC Examples

### LC 121: Best Time to Buy and Sell Stock (Single Transaction)
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

### LC 122: Best Time to Buy and Sell Stock II (Unlimited Transactions)
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

### LC 714: Best Time to Buy and Sell Stock with Transaction Fee
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

### LC 309: Best Time to Buy and Sell Stock with Cooldown
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

### LC 123: Best Time to Buy and Sell Stock III (At Most 2 Transactions)
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

### LC 188: Best Time to Buy and Sell Stock IV (At Most K Transactions)
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

### LC 1235: Maximum Profit in Job Scheduling (Weighted Job Scheduling)
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

## 3) Key Patterns & Techniques

### Pattern 1: State Machine DP
- **When**: Multiple states with transitions
- **States**: hold, sold, rest (for cooldown)
- **Transitions**: Buy (sold -> hold), Sell (hold -> sold)

### Pattern 2: Transaction Counting
- **When**: Limited number of transactions
- **Technique**: Track buy/sell pairs separately
- **Optimization**: If k >= n/2, treat as unlimited

### Pattern 3: Constraint Handling
- **Cooldown**: Add rest state, delay buy after sell
- **Transaction Fee**: Subtract fee during sell transition
- **Multiple Constraints**: Combine state variables

### Pattern 4: Space Optimization
- **Rolling Variables**: Use variables instead of arrays when only previous state needed
- **Dimension Reduction**: Optimize k-transaction DP for large k

## 4) Time & Space Complexity

| Problem | Time | Space | Key Insight |
|---------|------|-------|-------------|
| LC 121 | O(n) | O(1) | Track min price and max profit |
| LC 122 | O(n) | O(1) | Greedy: buy before every increase |
| LC 714 | O(n) | O(1) | State machine with fee on sell |
| LC 309 | O(n) | O(1) | Three states: hold, sold, rest |
| LC 123 | O(n) | O(1) | Four states for 2 transactions |
| LC 188 | O(nk) | O(k) | General k-transaction, optimize for large k |

## 5) Common Mistakes & Tips

### Mistakes
- **Forgetting constraints**: Not handling cooldown or fees properly
- **Wrong state definition**: Confusing buy/sell counts vs. transaction counts
- **Boundary conditions**: Not handling edge cases (empty array, single day)
- **Large k optimization**: Not optimizing when k >= n/2

### Tips
- **Always return sold state**: Never hold stock at the end
- **Initialize carefully**: First day states matter
- **Use meaningful variable names**: `hold`, `sold` instead of `dp[0]`, `dp[1]`
- **Consider greedy when unlimited**: Simpler approach for unlimited transactions

## 6) Related Problems
- LC 121: Best Time to Buy and Sell Stock
- LC 122: Best Time to Buy and Sell Stock II  
- LC 123: Best Time to Buy and Sell Stock III
- LC 188: Best Time to Buy and Sell Stock IV
- LC 309: Best Time to Buy and Sell Stock with Cooldown
- LC 714: Best Time to Buy and Sell Stock with Transaction Fee
- LC 1235: Maximum Profit in Job Scheduling

---

## 7) Unified `dp[i][k][hold]` State Machine ⭐⭐⭐⭐⭐

> **The one template to memorize.** All six stock problems (121 / 122 / 123 / 188 / 309 / 714) are the *same* recurrence — they differ only in what you plug in for `k`, and in two optional tweaks (cooldown, fee). Derive this once in the interview and every variant falls out in one line.

### 7-1) State Definition & Recurrence

**State**

```text
dp[i][k][0] = max profit on day i, having used at most k transactions, NOT holding stock
dp[i][k][1] = max profit on day i, having used at most k transactions, HOLDING stock
```

**Key Idea**: a transaction is counted **at the buy** (one buy + one sell = one transaction). Counting at buy is what makes `k` decrement appear in the `hold` line — pick one convention and stay with it.

**Recurrence**

```text
dp[i][k][0] = max( dp[i-1][k][0] ,  dp[i-1][k][1] + prices[i] )    # rest  |  sell
dp[i][k][1] = max( dp[i-1][k][1] ,  dp[i-1][k-1][0] - prices[i] )  # rest  |  buy (spends a transaction)
```

**Base cases**

```text
dp[-1][k][0] = 0        # no days elapsed, no stock  -> profit 0
dp[-1][k][1] = -INF     # impossible to hold before day 0
dp[i][0][0]  = 0        # 0 transactions allowed, not holding -> 0
dp[i][0][1]  = -INF     # impossible to hold with 0 transactions
```

**Answer**: `dp[n-1][K][0]` — always end **not** holding.

#### How every LC problem instantiates it

| LC | Constraint | Plug in |
|----|-----------|---------|
| 121 | at most 1 transaction | `k = 1` (so `dp[i-1][k-1][0]` collapses to `0`) |
| 122 | unlimited | `k = +inf` -> drop the `k` dimension entirely |
| 123 | at most 2 | `k = 2` |
| 188 | at most k | `k` as given (+ the `k >= n/2` shortcut) |
| 309 | cooldown 1 day | buy from `dp[i-2][k-1][0]` instead of `dp[i-1][k-1][0]` |
| 714 | fee per transaction | sell line becomes `dp[i-1][k][1] + prices[i] - fee` |

**Why `k >= n/2` means unlimited**: each transaction needs at least 2 days (one buy day, one sell day), so at most `floor(n/2)` transactions are ever useful. Beyond that the `k` cap is not binding.

### 7-2) Template — full 3D table

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

### 7-3) Space-optimized — rolling `O(k)` arrays

> Row `i` only reads row `i-1`, so drop the `i` dimension. **Iteration-order trap**: update `hold[j]` *before* `sold[j]` within the same day. The in-place read of `sold[j-1]` from the current day only ever models "sell and re-buy at the same price", which nets 0 — harmless, never inflates the answer.

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

**Sanity**: `maxProfit(2, [3,2,6,5,0,3]) == 7`, `maxProfit(2, [2,4,1]) == 2`, `maxProfit(2, [3,3,5,0,0,3,1,4]) == 6`.

#### Visual Trace — LC 123 (`k=2`) on `[3,3,5,0,0,3,1,4]`

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

### 7-4) Variation: cooldown & fee inside the unified recurrence

> **The twist**: neither variant needs a new template — cooldown moves the *source row* of the buy, and the fee is a constant subtracted on the *sell* edge. Both are unlimited-`k`, so the `k` dimension disappears and only two rolling scalars survive.

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

**Sanity**: `maxProfitCooldown([1,2,3,0,2]) == 3`, `maxProfitFee([1,3,2,8,4,9], 2) == 8`.

**Note**: charging the fee on the *buy* edge (`hold = max(hold, sold - prices[i] - fee)`) is equally valid and returns the same answer — just never charge it on both.

---

## 8) Same Skeleton, Different Story — LC 53 ⭐⭐⭐⭐

> Not a stock problem, but the **same one-transaction skeleton**. Worth 60 seconds in an interview because it shows you see the reduction rather than memorized six templates.

### 8-1) LC 121 **is** Kadane on the price deltas

**Key Idea**: buying at day `b` and selling at day `s` earns `prices[s] - prices[b]`, which telescopes into `sum(delta[b+1..s])` where `delta[i] = prices[i] - prices[i-1]`. So *max profit with one transaction* = *max subarray sum of the delta array*, clamped at 0 (the empty subarray = do nothing).

```text
prices  = [7, 1, 5, 3, 6, 4]
delta   = [  -6, 4,-2, 3,-2]
max subarray of delta = [4,-2,3] = 5   ==  LC 121 answer 5
```

| Direction | Statement |
|-----------|-----------|
| 53 -> 121 | run Kadane on `delta`, floor the running sum at 0 |
| 121 -> 53 | LC 53's `cur` is exactly LC 121's "profit if I still hold from the best recent buy" |
| 122 | drop the "one subarray" restriction -> sum of **all** positive deltas |

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

**The one difference to say out loud**: LC 53 forbids the empty subarray (all-negative input must return the largest single element, e.g. `[-1] -> -1`), while LC 121 allows doing nothing (`[7,6,4,3,1] -> 0`). That single clamp is the entire gap between the two problems.

**Sanity**: `maxSubArray([-2,1,-3,4,-1,2,1,-5,4]) == 6`, `maxSubArray([-1]) == -1`, `maxProfit([7,1,5,3,6,4]) == 5`, `maxProfit([7,6,4,3,1]) == 0`.