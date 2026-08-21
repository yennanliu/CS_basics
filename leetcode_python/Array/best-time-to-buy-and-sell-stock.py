"""


121. Best Time to Buy and Sell Stock
Solved
Easy
Topics
premium lock icon
Companies
You are given an array prices where prices[i] is the price of a given stock on the ith day.

You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.

Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.

 

Example 1:

Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
Example 2:

Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transactions are done and the max profit = 0.
 

Constraints:

1 <= prices.length <= 105
0 <= prices[i] <= 104


"""


# V0
# IDEA: 2 POINTERS (GPT)
# time = O(n)
# space = O(1)
"""
CORE IDEA:

->


At each day, 
keep track of the `cheapest` price seen so far,

then calculate the `profit` if you sell today.

"""
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        n = len(prices)

        if n < 2:
            return 0

        global_max = 0
        min_price = prices[0]

        for r in range(1, n):
            sell = prices[r]

            # Best profit if we sell today
            global_max = max(global_max, sell - min_price)

            # Best buying price for future days
            min_price = min(min_price, sell)

        return global_max


# V0
# IDEA : array op + problem understanding
# time = O(n)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices):
        if len(prices) == 0:
            return 0
        ### NOTE : we define 1st minPrice as prices[0]
        minPrice = prices[0]
        maxProfit = 0
        ### NOTE : we only loop prices ONCE
        for p in prices:
            # only if p < minPrice, we get minPrice
            if p < minPrice:
                minPrice = p
            ### NOTE : only if p - minPrice > maxProfit, we get maxProfit
            elif p - minPrice > maxProfit:
                maxProfit = p - minPrice
        return maxProfit

# V0''
# IDEA : BRUTE FORCE (time out error)
# time = O(n^2)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices):
        res = 0
        for i in range(len(prices)):
            for j in range(i+1, len(prices)):
                #print ("i = " + str(i) + " j = " + str(j))
                if prices[j] > prices[i]:
                    diff = prices[j] - prices[i]
                    res = max(res, diff)
        return res

# V1 
# https://blog.csdn.net/coder_orz/article/details/51520971
# TO NOTE : CAN ONLY DO ONE TRANSACTION IN THE PROBLEM
# time = O(n)
# space = O(1)
class Solution(object):
    def maxProfit(self, prices):
        if len(prices) == 0:
            return 0
        minPrice = prices[0]
        maxProfit = 0
        for p in prices:
            if p < minPrice:
                minPrice = p
            elif p - minPrice > maxProfit:
                maxProfit = p - minPrice
        return maxProfit

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51520971
# IDEA : DP
# time = O(n)
# space = O(n)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if len(prices) == 0:
            return 0
        minPrice = prices[0]
        dp = [0] * len(prices)
        for i in range(0, len(prices)):
            dp[i] = max(dp[i-1], prices[i] - minPrice)
            minPrice = min(minPrice, prices[i])
        return dp[-1]

# V1''
# https://www.jiuzhang.com/solution/best-time-to-buy-and-sell-stock/#tag-highlight-lang-python
# time = O(n)
# space = O(1)
class Solution:
    """
    @param prices: Given an integer array
    @return: Maximum profit
    """
    def maxProfit(self, prices):
        # write your code here
        total = 0
        low, high = sys.maxint, 0
        for x in prices:
            if x - low > total:
                total = x - low
            if x < low:
                low = x
        return total

# V2 
# time = O(n)
# space = O(1)
class Solution(object):
    # @param prices, a list of integer
    # @return an integer
    def maxProfit(self, prices):
        max_profit, min_price = 0, float("inf")
        for price in prices:
            min_price = min(min_price, price)
            max_profit = max(max_profit, price - min_price)
        return max_profit
