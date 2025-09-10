# Best Time to Buy and Sell Stock
> Dynamic Programming approach to stock trading problems

## 0) Concept

### 0-1) Types

- **Single Transaction**: Buy once, sell once (LC 121)
- **Multiple Transactions**: Unlimited transactions (LC 122)
- **K Transactions**: At most k transactions (LC 123, LC 188)
- **With Constraints**: Cooldown period (LC 309), transaction fee (LC 714)
- **Special Variants**: With jump game (LC 1235)

### 0-2) Pattern

**State Machine DP Pattern**:
- Track different states: `hold` (own stock), `sold` (don't own stock)
- Consider constraints: transaction count, cooldown, fees
- Transition between states based on buy/sell actions

**Core Insight**: At each day, we can be in different states and need to track the maximum profit for each state.

## 1) General Form

### 1-1) Basic OP

#### State Definition
```python
# Basic states
hold[i] = max profit when holding stock at day i
sold[i] = max profit when not holding stock at day i

# With transaction count
buy[i][k] = max profit after at most k transactions, currently holding
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