"""

188. Best Time to Buy and Sell Stock IV
Hard

You are given an integer array prices where prices[i] is the price of a given stock on the
ith day, and an integer k.

Find the maximum profit you can achieve. You may complete at most k transactions:
i.e. you may buy at most k times and sell at most k times.

Note: You may not engage in multiple transactions simultaneously
(i.e., you must sell the stock before you buy again).


Example 1:

Input: k = 2, prices = [2,4,1]
Output: 2
Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.

Example 2:

Input: k = 2, prices = [3,2,6,5,0,3]
Output: 7
Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.


Constraints:

1 <= k <= 100
1 <= prices.length <= 1000
0 <= prices[i] <= 1000

"""

# V0
# IDEA : DP over (transaction count, holding state), rolled into 1D arrays
#
#  DP def (after processing day i):
#    - buy[j]  = max profit having STARTED the j-th transaction and still holding stock
#    - sell[j] = max profit having COMPLETED j transactions and holding nothing
#
#  DP eq (for each price p, for j in 1..k):
#    - buy[j]  = max(buy[j],  sell[j-1] - p)   # open the j-th transaction
#    - sell[j] = max(sell[j], buy[j]   + p)    # close the j-th transaction
#
#  NOTE: updating buy[j] BEFORE sell[j] within the same day is fine here -
#  it would only model buy-and-sell on the same day, which yields 0 profit.
#
# time = O(n * k)
# space = O(k)
class Solution(object):
    def maxProfit(self, k, prices):
        n = len(prices)
        if n < 2 or k <= 0:
            return 0

        # a transaction needs >= 2 days, so more than n//2 transactions is useless
        # (this also keeps the loop cheap when k is huge)
        k = min(k, n // 2)
        if k == 0:
            return 0

        NEG = float("-inf")
        buy = [NEG] * (k + 1)   # buy[0] unused
        sell = [0] * (k + 1)    # sell[0] = 0 -> zero transactions, zero profit

        for p in prices:
            for j in range(1, k + 1):
                buy[j] = max(buy[j], sell[j - 1] - p)
                sell[j] = max(sell[j], buy[j] + p)

        return sell[k]
