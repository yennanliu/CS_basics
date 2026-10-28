"""

714. Best Time to Buy and Sell Stock with Transaction Fee
Medium

You are given an array prices where prices[i] is the price of a given stock on
the ith day, and an integer fee representing a transaction fee.

Find the maximum profit you can achieve. You may complete as many transactions
as you like, but you need to pay the transaction fee for each transaction.

Note:

You may not engage in multiple transactions simultaneously (i.e., you must sell
the stock before you buy again).
The transaction fee is only charged once for each stock purchase and sale.

Example 1:

Input: prices = [1,3,2,8,4,9], fee = 2
Output: 8
Explanation: The maximum profit can be achieved by:
- Buying at prices[0] = 1
- Selling at prices[3] = 8
- Buying at prices[4] = 4
- Selling at prices[5] = 9
The total profit is ((8 - 1) - 2) + ((9 - 4) - 2) = 8.

Example 2:

Input: prices = [1,3,7,5,10,3], fee = 3
Output: 6

Constraints:

1 <= prices.length <= 5 * 10^4
1 <= prices[i] < 5 * 10^4
0 <= fee < 5 * 10^4

"""


# V0
# IDEA: 1D DP (2 rolling states: cash, hold)
"""

DP def
    fee is paid once per completed transaction -> charge it on the sell

    cash: max profit so far while holding NO stock
    hold: max profit so far while HOLDING the stock

DP eq

     for each price p:

        cash = max(cash, hold + p - fee)   # rest, or sell today (pay fee)
        hold = max(hold, cash - p)         # rest, or buy today

     init: cash = 0, hold = -prices[0]   (bought on day 0)
     ans = cash   (ending with stock in hand is never better)

     e.g. prices = [1,3,2,8,4,9], fee = 2
          p : 1    3    2    8    4    9
       cash : 0    0    0    5    5    8
       hold : -1  -1   -1   -1    1    1

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices, fee):
        """
        :type prices: List[int]
        :type fee: int
        :rtype: int
        """
        if not prices:
            return 0

        # day 0: either do nothing, or buy
        cash = 0
        hold = -prices[0]

        for i in range(1, len(prices)):
            price = prices[i]
            # sell today (pay the fee) or keep resting without stock
            cash = max(cash, hold + price - fee)
            # NOTE !!! using the cash just updated is safe: selling and
            #          re-buying on the same day only loses the fee
            hold = max(hold, cash - price)

        return cash


# V0-1
# IDEA: 2D DP TABLE (same states as V0, one row per day)
"""

DP def
    dp[i][0] = max profit at the end of day i, holding NO stock
    dp[i][1] = max profit at the end of day i, HOLDING the stock

DP eq
    dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i] - fee)
    dp[i][1] = max(dp[i-1][1], dp[i-1][0] - prices[i])

    base: dp[0][0] = 0, dp[0][1] = -prices[0]
    ans : dp[n-1][0]

    -> row i only reads row i-1, which is why V0 can roll it into 2 variables.
       Easier to derive live; write this first, then compress.

"""
# time = O(n)
# space = O(n)
class Solution2(object):
    def maxProfit(self, prices, fee):
        """
        :type prices: List[int]
        :type fee: int
        :rtype: int
        """
        if not prices:
            return 0

        n = len(prices)
        # dp[i] = [profit with no stock, profit holding stock]
        dp = [[0, 0] for _ in range(n)]
        dp[0][0] = 0
        dp[0][1] = -prices[0]

        for i in range(1, n):
            # rest without stock, or sell the held stock today
            dp[i][0] = max(dp[i - 1][0], dp[i - 1][1] + prices[i] - fee)
            # keep holding, or buy today with yesterday's cash
            dp[i][1] = max(dp[i - 1][1], dp[i - 1][0] - prices[i])

        return dp[n - 1][0]


# V1
# IDEA: GREEDY (track the effective buy price)
"""

    min_cost = the price we are effectively "holding at"

    - price drops below min_cost  -> buying here is strictly better, move it
    - price > min_cost + fee      -> a sale is profitable, take it now:
                                     profit += price - min_cost - fee

    NOTE !!! after taking a profit, set min_cost = price - fee (not price).
             If the price keeps rising tomorrow, "re-buying" at price - fee
             lets us extend this same sale WITHOUT paying a second fee:
             (p2 - (p1 - fee) - fee) = p2 - p1, exactly the extra gain.

    e.g. prices = [1,3,2,8,4,9], fee = 2
         8 > 1 + 2 -> +5, min_cost = 6
         4 < 6     -> min_cost = 4
         9 > 4 + 2 -> +3            total = 8

    -> same answer as the DP, but the min_cost = price - fee step is hard
       to justify live; learn V0 first.

"""
# time = O(n)
# space = O(1)
class Solution3(object):
    def maxProfit(self, prices, fee):
        """
        :type prices: List[int]
        :type fee: int
        :rtype: int
        """
        if not prices:
            return 0

        min_cost = prices[0]
        profit = 0

        for i in range(1, len(prices)):
            price = prices[i]
            if price < min_cost:
                # a cheaper buy point: switch to it
                min_cost = price
            elif price > min_cost + fee:
                # selling here beats the fee: lock in the gain
                profit += price - min_cost - fee
                # keep the option to extend this sale for free (see NOTE)
                min_cost = price - fee

        return profit
