"""

1833. Maximum Ice Cream Bars
Medium

It is a sweltering summer day, and a boy wants to buy some ice cream bars.

At the store, there are n ice cream bars. You are given an array costs of length n, where costs[i] is the price of the ith ice cream bar in coins. The boy initially has coins coins to spend, and he wants to buy as many ice cream bars as possible.

Note: The boy can buy the ice cream bars in any order.

Return the maximum number of ice cream bars the boy can buy with coins coins.

You must solve the problem by counting sort.


Example 1:

Input: costs = [1,3,2,4,1], coins = 7
Output: 4
Explanation: The boy can buy ice cream bars at indices 0,1,2,4 for a total price of 1 + 3 + 2 + 1 = 7.

Example 2:

Input: costs = [10,6,8,7,7,8], coins = 5
Output: 0
Explanation: The boy cannot afford any of the ice cream bars.

Example 3:

Input: costs = [1,6,3,1,2,5], coins = 20
Output: 6
Explanation: The boy can buy all the ice cream bars for a total price of 1 + 6 + 3 + 1 + 2 + 5 = 18.


Constraints:

costs.length == n
1 <= n <= 10^5
1 <= costs[i] <= 10^5
1 <= coins <= 10^8

"""

# V0
# IDEA : GREEDY (buy cheapest first) IMPLEMENTED WITH COUNTING SORT
#
#   exchange argument : if an optimal purchase skips a cheaper bar in favour of
#   a dearer one, swapping them keeps the count and does not raise the price.
#   so always buy in ascending cost order until the money runs out.
#
#   costs[i] <= 10^5, so instead of an O(n log n) sort we bucket the prices and
#   sweep price = 1..10^5, taking as many bars of that price as we can afford:
#     take = min(cnt[price], coins // price)
#
#   NOTE : the sweep is O(M + n) with M = 10^5, which is the counting-sort
#          solution the statement asks for.
#
# time = O(n + M), space = O(M)
class Solution(object):
    def maxIceCream(self, costs, coins):
        M = 100000
        cnt = [0] * (M + 1)
        for c in costs:
            cnt[c] += 1

        res = 0
        for price in range(1, M + 1):
            if coins < price:
                break
            if cnt[price]:
                take = min(cnt[price], coins // price)
                res += take
                coins -= take * price
        return res
