"""

3573. Best Time to Buy and Sell Stock V
Medium

You are given an integer array prices where prices[i] is the price of a stock in dollars on the ith day, and an integer k.

You are allowed to make at most k transactions, where each transaction can be either of the following:

Normal transaction: buy on day i, then sell on a later day j where i < j. You profit prices[j] - prices[i].
Short selling transaction: sell on day i, then buy back on a later day j where i < j. You profit prices[i] - prices[j].

Note that you must complete each transaction before starting another. Additionally, you can't buy or sell on the same day you are completing another transaction.

Return the maximum total profit you can earn by making at most k transactions.


Example 1:

Input: prices = [1,7,9,8,2], k = 2
Output: 14
Explanation:
We can make $14 of profit through 2 transactions:
A normal transaction: buy the stock on day 0 for $1 then sell it on day 2 for $9.
A short selling transaction: sell the stock on day 3 for $8 then buy back on day 4 for $2.

Example 2:

Input: prices = [12,16,19,19,8,1,19,13,9], k = 3
Output: 36
Explanation:
We can make $36 of profit through 3 transactions:
A normal transaction: buy the stock on day 0 for $12 then sell it on day 2 for $19.
A short selling transaction: sell the stock on day 3 for $19 then buy back on day 4 for $8.
A normal transaction: buy the stock on day 5 for $1 then sell it on day 6 for $19.


Constraints:

2 <= prices.length <= 10^3
1 <= prices[i] <= 10^9
1 <= k <= prices.length / 2

"""

# V0
# IDEA : 3-STATE DP (IDLE / LONG OPEN / SHORT OPEN) INDEXED BY TRANSACTION COUNT
#
#   at the end of every day the portfolio is in exactly one of three states,
#   and the profit is fully determined by that state plus how many
#   transactions have already been closed.
#
#   the "no buying and selling on the same day" rule falls out for free by
#   building the new day's table only from the *previous* day's table: an
#   open position must have been opened strictly earlier than the day it is
#   closed.
#
#   short selling is just a long trade with the sign flipped, so it reuses
#   the same recurrence with +p / -p swapped.
#
# time = O(n * k), space = O(k)
class Solution(object):
    def maximumProfit(self, prices, k):
        NEG = float('-inf')
        # idle[j] : best profit with at most j closed transactions, no position
        idle = [0] * (k + 1)
        # lng[j] / sht[j] : an open long / short whose close will be trade j
        lng = [NEG] * (k + 1)
        sht = [NEG] * (k + 1)

        for p in prices:
            n_idle = idle[:]
            n_lng = lng[:]
            n_sht = sht[:]
            for j in range(1, k + 1):
                if idle[j - 1] - p > n_lng[j]:
                    n_lng[j] = idle[j - 1] - p
                if idle[j - 1] + p > n_sht[j]:
                    n_sht[j] = idle[j - 1] + p
                cand = lng[j] + p
                if cand > n_idle[j]:
                    n_idle[j] = cand
                cand = sht[j] - p
                if cand > n_idle[j]:
                    n_idle[j] = cand
                if n_idle[j - 1] > n_idle[j]:
                    n_idle[j] = n_idle[j - 1]
            idle, lng, sht = n_idle, n_lng, n_sht
        return idle[k]
