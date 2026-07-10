"""

309. Best Time to Buy and Sell Stock with Cooldown
Solved
Medium
Topics
premium lock icon
Companies
You are given an array prices where prices[i] is the price of a given stock on the ith day.

Find the maximum profit you can achieve. You may complete as many transactions as you like (i.e., buy one and sell one share of the stock multiple times) with the following restrictions:

After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).
Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).

 

Example 1:

Input: prices = [1,2,3,0,2]
Output: 3
Explanation: transactions = [buy, sell, cooldown, buy, sell]
Example 2:

Input: prices = [1]
Output: 0
 

Constraints:

1 <= prices.length <= 5000
0 <= prices[i] <= 1000


"""


# V0:
# IDEA: 2D DP + STATE FUNC
"""

DP def:


        dp[i][0] = max profit at day i, 
                   `holding` a stock

        dp[i][1] = max profit at day i, 
                   just `sold` a stock today

        dp[i][2] = max profit at day i, 
                   NOT holding a stock NOT not in cooldown (rest)


        # dp[i][0] = hold
        # dp[i][1] = sold
        # dp[i][2] = rest (NOT holding a stock and NOT cooldown)


DP eq:

      (split by state)


      dp[i][0] = max(
                dp[i - 1][0],              # keep holding
                dp[i - 1][2] - prices[i]   # buy today
            )

      
      dp[i][1] = dp[i - 1][0] + prices[i]  # sell today

      
      dp[i][2] = max(
                dp[i - 1][2],  # keep resting
                dp[i - 1][1]   # cooldown finished
            )

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def maxProfit(self, prices):
        if not prices:
            return 0

        n = len(prices)

        # dp[i][0] = hold
        # dp[i][1] = sold
        # dp[i][2] = rest
        # NOTE !!! row size is n
        dp = [[0] * 3 for _ in range(n)]

        dp[0][0] = -prices[0]   # buy
        dp[0][1] = 0            # impossible, but 0 works
        dp[0][2] = 0

        for i in range(1, n):
            dp[i][0] = max(
                dp[i - 1][0],              # keep holding
                dp[i - 1][2] - prices[i]   # buy today
            )

            dp[i][1] = dp[i - 1][0] + prices[i]  # sell today

            dp[i][2] = max(
                dp[i - 1][2],  # keep resting
                dp[i - 1][1]   # cooldown finished
            )

        return max(dp[n - 1][1], dp[n - 1][2])


# V0-1
# IDEA: 2D DP + STATE FUNC
# time = O(n)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if not prices or len(prices) < 2:
            return 0
            
        # BASE CASES INITIALIZATION (Day 1)
        # - float('-inf') means it's initially impossible to start in a sold state.
        # - buying a stock on day 1 drops our current capital by prices[0].
        hold = -prices[0]
        sold = float('-inf')
        rest = 0
        
        # Loop forward through the prices starting from the second day
        for i in range(1, len(prices)):
            prev_hold = hold
            prev_sold = sold
            prev_rest = rest
            
            # STATE 1: Options for holding a stock today:
            # - Keep holding the stock we bought yesterday (prev_hold)
            # - Buy a new stock today using cash from yesterday's rest state (prev_rest - prices[i])
            hold = max(prev_hold, prev_rest - prices[i])
            
            # STATE 2: Options for selling a stock today:
            # - We must have held a stock yesterday, and we sell it at today's price
            sold = prev_hold + prices[i]
            
            # STATE 3: Options for resting / cooling down today:
            # - Keep resting today (prev_rest)
            # - Transition out of a stock we sold yesterday (prev_sold)
            rest = max(prev_rest, prev_sold)
            
        # The maximum possible profit cannot end on a 'hold' state. 
        # It must be the best outcome between having sold or resting on the final day.
        return max(sold, rest)


# V1 
# http://bookshadow.com/weblog/2015/11/24/leetcode-best-time-to-buy-and-sell-stock-with-cooldown/
# time = O(n)
# space = O(n)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        size = len(prices)
        if not size:
            return 0
        buys = [None] * size
        sells = [None] * size
        sells[0], buys[0] = 0, -prices[0]
        for x in range(1, size):
            delta = prices[x] - prices[x - 1]
            sells[x] = max(buys[x - 1] + prices[x], sells[x - 1] + delta)
            buys[x] = max(buys[x - 1] - delta, \
                          sells[x - 2] - prices[x] if x > 1 else None)
        return max(sells)

# V1'
# http://bookshadow.com/weblog/2015/11/24/leetcode-best-time-to-buy-and-sell-stock-with-cooldown/
# time = O(n)
# space = O(n)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        size = len(prices)
        if size < 2:
            return 0
        buys = [None] * size
        sells = [None] * size
        sells[0], sells[1] = 0, max(0, prices[1] - prices[0])
        buys[0], buys[1] = -prices[0], max(-prices[0], -prices[1])
        for x in range(2, size):
            sells[x] = max(sells[x - 1], buys[x - 1] + prices[x])
            buys[x] = max(buys[x - 1], sells[x - 2] - prices[x])
        return sells[-1]
        
# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if not prices:
            return 0
        buy, sell, coolDown = [0] * 2, [0] * 2, [0] * 2
        buy[0] = -prices[0]
        for i in range(1, len(prices)):
            # Bought before or buy today.
            buy[i % 2] = max(buy[(i - 1) % 2],
                             coolDown[(i - 1) % 2] - prices[i])
            # Sell today.
            sell[i % 2] = buy[(i - 1) % 2] + prices[i]
            # Sold before yesterday or sold yesterday.
            coolDown[i % 2] = max(coolDown[(i - 1) % 2], sell[(i - 1) % 2])
        return max(coolDown[(len(prices) - 1) % 2],
                   sell[(len(prices) - 1) % 2])
