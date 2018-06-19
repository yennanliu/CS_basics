"""

Say you have an array for which the ith element is the price of a given stock on day i.

If you were only permitted to complete at most one transaction (i.e., buy one and sell one share of the stock), design an algorithm to find the maximum profit.

Note that you cannot sell a stock before you buy one.

Example 1:

Input: [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
             Not 7-1 = 6, as selling price needs to be larger than buying price.
Example 2:

Input: [7,6,4,3,1]
Output: 0
Explanation: In this case, no transaction is done, i.e. max profit = 0.


"""


# V1  : dev (run time error)
"""
class Solution:
    def maxProfit(self, prices):
        price_list = []
        min_max_parice=[]
        for index, price in enumerate(prices):
            if index >= len(prices)-1 :
                break 
            else:
                prices_ = prices[index+1:]
                profit = max(prices_) - price
                if profit>0:
                    price_list.append(profit)
                else:
                    price_list.append(0)
        if len(price_list) == 0:
            return 0
        else:
            return max(price_list)
       

"""


# V2 
# https://github.com/yennanliu/LeetCode/blob/master/Python/best-time-to-buy-and-sell-stock.py
class Solution(object):
    # @param prices, a list of integer
    # @return an integer
    def maxProfit(self, prices):
        max_profit= 0 
        min_price = float("inf")
        for price in prices:
            min_price = min(min_price, price)
            max_profit = max(max_profit, price - min_price)
        return max_profit







