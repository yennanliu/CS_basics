"""

2431. Maximize Total Tastiness of Purchased Fruits
Medium
(premium / locked problem)

You are given two non-negative integer arrays price and tastiness, both arrays have the same length n. You are also given two non-negative integers maxAmount and maxCoupons.

For every integer i in range [0, n - 1]:

price[i] describes the price of the ith fruit.
tastiness[i] describes the tastiness of the ith fruit.

You want to purchase some fruits such that the total tastiness is maximized and the total price does not exceed maxAmount.

Additionally, you can use a coupon to purchase a fruit for half of its price (rounded down to the closest integer). You can use at most maxCoupons coupons.

Return the maximum total tastiness that can be purchased.

Note that:

You can purchase each fruit at most once.
You can use coupons on some fruit at most once.


Example 1:

Input: price = [10,20,20], tastiness = [5,8,8], maxAmount = 20, maxCoupons = 1
Output: 13
Explanation: It is possible to make total tastiness 13 in following way:
- Buy first fruit without coupon, so that total price = 0 + 10 and total tastiness = 0 + 5.
- Buy second fruit with coupon, so that total price = 10 + 10 and total tastiness = 5 + 8.
- Do not buy third fruit, so that total price = 20 and total tastiness = 13.
It can be proven that 13 is the maximum total tastiness that can be obtained.

Example 2:

Input: price = [10,15,7], tastiness = [5,8,20], maxAmount = 10, maxCoupons = 2
Output: 28
Explanation: It is possible to make total tastiness 28 in following way:
- Do not buy first fruit, so that total price = 0 and total tastiness = 0.
- Buy second fruit with coupon, so that total price = 0 + 7 and total tastiness = 0 + 8.
- Buy third fruit with coupon, so that total price = 7 + 3 and total tastiness = 8 + 20.
It can be proven that 28 is the maximum total tastiness that can be obtained.


Constraints:

n == price.length == tastiness.length
1 <= n <= 100
0 <= price[i], tastiness[i] <= 1000
0 <= maxAmount <= 1000
0 <= maxCoupons <= 5

"""

# V0
# IDEA : 0/1 KNAPSACK WITH A SECOND DIMENSION FOR THE COUPONS
#
#   each fruit offers three choices — skip, buy at full price, or buy at
#   half price spending one coupon — so the state is
#       dp[amount][coupons] = best tastiness with that budget and that many
#                             coupons still available
#
#   iterating the budget and the coupon count DOWNWARD keeps every fruit
#   usable at most once, exactly like the 1D knapsack trick.
#
#   the coupon dimension is tiny (maxCoupons <= 5), so the table stays small.
#
# time = O(n * maxAmount * maxCoupons), space = O(maxAmount * maxCoupons)
class Solution(object):
    def maxTastiness(self, price, tastiness, maxAmount, maxCoupons):
        dp = [[0] * (maxCoupons + 1) for _ in range(maxAmount + 1)]

        for p, t in zip(price, tastiness):
            half = p // 2
            for amount in range(maxAmount, -1, -1):
                for coupons in range(maxCoupons, -1, -1):
                    if amount >= p:
                        dp[amount][coupons] = max(dp[amount][coupons],
                                                  dp[amount - p][coupons] + t)
                    if coupons >= 1 and amount >= half:
                        dp[amount][coupons] = max(dp[amount][coupons],
                                                  dp[amount - half][coupons - 1] + t)
        return dp[maxAmount][maxCoupons]
