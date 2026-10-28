"""

123. Best Time to Buy and Sell Stock III
Hard

You are given an array prices where prices[i] is the price of a given stock on the ith day.

Find the maximum profit you can achieve. You may complete at most two transactions.

Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).

 

Example 1:

Input: prices = [3,3,5,0,0,3,1,4]
Output: 6
Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
Example 2:

Input: prices = [1,2,3,4,5]
Output: 4
Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
Note that you cannot buy on day 1, buy on day 2 and sell them later, as you are engaging multiple transactions at the same time. You must sell before buying again.
Example 3:

Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transaction is done, i.e. max profit = 0.
 

Constraints:

1 <= prices.length <= 105
0 <= prices[i] <= 105

"""


# V0
# IDEA: 1D DP (4 rolling states)
"""

DP def
    at most 2 transactions -> 4 rolling states

    buy1 : best profit after OPENING  the 1st transaction (holding stock)
    sell1: best profit after CLOSING  the 1st transaction (no stock)
    buy2 : best profit after OPENING  the 2nd transaction (holding stock)
    sell2: best profit after CLOSING  the 2nd transaction (no stock)

DP eq

     for each price p:

        buy1  = max(buy1,  0     - p)
        sell1 = max(sell1, buy1  + p)
        buy2  = max(buy2,  sell1 - p)
        sell2 = max(sell2, buy2  + p)

     ans = sell2

"""
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

        # nothing bought yet -> holding states start at -inf
        buy1 = float('-inf')
        sell1 = 0
        buy2 = float('-inf')
        sell2 = 0

        for p in prices:
            # open the 1st transaction (start from 0 profit)
            buy1 = max(buy1, -p)
            # close the 1st transaction
            sell1 = max(sell1, buy1 + p)
            # NOTE !!! open the 2nd transaction with the 1st one's profit
            buy2 = max(buy2, sell1 - p)
            # close the 2nd transaction
            sell2 = max(sell2, buy2 + p)

        # sell2 >= sell1 (2nd transaction can be a no-op), so it is the answer
        return sell2


# V1
# https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/solution/
# IDEA: Bidirectional Dynamic Programming
"""

DP def
    split the array at i: 1st transaction in prices[0..i], 2nd in prices[i+1..n-1]

    left[i]  = best 1-transaction profit within prices[0..i]
    right[i] = best 1-transaction profit within prices[i..n-1]

DP eq

     left[i]  = max(left[i-1],  prices[i] - min(prices[0..i-1]))
     right[i] = max(right[i+1], max(prices[i+1..n-1]) - prices[i])

     ans = max( left[i] + right[i+1] )

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if len(prices) <= 1:
            return 0

        left_min = prices[0]
        right_max = prices[-1]

        length = len(prices)
        left_profits = [0] * length
        # pad the right DP array with an additional zero for convenience.
        right_profits = [0] * (length + 1)

        # construct the bidirectional DP array
        for l in range(1, length):
            left_profits[l] = max(left_profits[l-1], prices[l] - left_min)
            left_min = min(left_min, prices[l])

            r = length - 1 - l
            right_profits[r] = max(right_profits[r+1], right_max - prices[r])
            right_max = max(right_max, prices[r])

        max_profit = 0
        for i in range(0, length):
            max_profit = max(max_profit, left_profits[i] + right_profits[i+1])

        return max_profit


# V2
# https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/solution/
# IDEA: One-pass Simulation
"""

    t1_cost  : lowest price paid for the 1st transaction so far
    t1_profit: best profit of the 1st transaction so far
    t2_cost  : lowest NET cost of the 2nd buy (price - t1_profit)
    t2_profit: best total profit of both transactions so far

    -> same idea as V0, written as costs instead of buy states
       (t1_cost = -buy1, t2_cost = -buy2)

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        t1_cost, t2_cost = float('inf'), float('inf')
        t1_profit, t2_profit = 0, 0

        for price in prices:
            # the maximum profit if only one transaction is allowed
            t1_cost = min(t1_cost, price)
            t1_profit = max(t1_profit, price - t1_cost)
            # reinvest the gained profit in the second transaction
            t2_cost = min(t2_cost, price - t1_profit)
            t2_profit = max(t2_profit, price - t2_cost)

        return t2_profit
