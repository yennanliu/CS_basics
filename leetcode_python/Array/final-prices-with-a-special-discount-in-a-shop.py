"""

1475. Final Prices With a Special Discount in a Shop
Easy

You are given an integer array prices where prices[i] is the price of the ith item in a shop.

There is a special discount for items in the shop. If you buy the ith item, then you will receive
a discount equivalent to prices[j] where j is the minimum index such that j > i and
prices[j] <= prices[i]. Otherwise, you will not receive any discount at all.

Return an integer array answer where answer[i] is the final price you will pay for the ith item
of the shop, considering the special discount.


Example 1:

Input: prices = [8,4,6,2,3]
Output: [4,2,4,2,3]
Explanation:
For item 0 with price[0]=8 you will receive a discount equivalent to prices[1]=4, therefore, the final price you will pay is 8 - 4 = 4.
For item 1 with price[1]=4 you will receive a discount equivalent to prices[3]=2, therefore, the final price you will pay is 4 - 2 = 2.
For item 2 with price[2]=6 you will receive a discount equivalent to prices[3]=2, therefore, the final price you will pay is 6 - 2 = 4.
For items 3 and 4 you will not receive any discount at all.

Example 2:

Input: prices = [1,2,3,4,5]
Output: [1,2,3,4,5]
Explanation: In this case, for all items, you will not receive any discount at all.

Example 3:

Input: prices = [10,1,1,6]
Output: [9,0,1,6]


Constraints:

1 <= prices.length <= 500
1 <= prices[i] <= 1000

"""

# V0
# IDEA : MONOTONIC STACK ("next smaller OR equal element to the right")
#        -> scan from RIGHT to LEFT, keep an increasing stack.
#           pop everything strictly bigger than the current price,
#           whatever is left on top is the discount.
# time = O(n)
# space = O(n)
class Solution(object):
    def finalPrices(self, prices):
        n = len(prices)
        res = list(prices)
        stack = []  # monotonic increasing (bottom -> top)
        for i in range(n - 1, -1, -1):
            x = prices[i]
            """
            NOTE !!!

              condition is `stack[-1] > x` (NOT >=),
              since the discount allows prices[j] <= prices[i]
              -> an equal value is a VALID discount, so keep it.
            """
            while stack and stack[-1] > x:
                stack.pop()
            if stack:
                res[i] = x - stack[-1]
            stack.append(x)
        return res


# V1
# IDEA : BRUTE FORCE (n <= 500, so O(n^2) is fine)
# time = O(n^2)
# space = O(n)
class Solution2(object):
    def finalPrices(self, prices):
        n = len(prices)
        res = list(prices)
        for i in range(n):
            for j in range(i + 1, n):
                if prices[j] <= prices[i]:
                    res[i] = prices[i] - prices[j]
                    break
        return res
