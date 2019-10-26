# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79888528
class Solution:
    def maxProfit(self, prices, fee):
        """
        :type prices: List[int]
        :type fee: int
        :rtype: int
        """
        cash = 0
        hold = -prices[0]
        for i in range(1, len(prices)):
            cash = max(cash, hold + prices[i] - fee)
            hold = max(hold, cash - prices[i])
        return cash
        
# V1'
# https://www.jiuzhang.com/solution/best-time-to-buy-and-sell-stock-with-transaction-fee/#tag-highlight-lang-python
class Solution:
    """
    @param prices: a list of integers
    @param fee: a integer
    @return: return a integer
    """
    def maxProfit(self, prices, fee):
        a, b = 0, -prices[0]
        for i in range(1, len(prices)):
            a, b = max(a, b + prices[i] - fee), max(b, a - prices[i])
        return a

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxProfit(self, prices, fee):
        """
        :type prices: List[int]
        :type fee: int
        :rtype: int
        """
        cash, hold = 0, -prices[0]
        for i in range(1, len(prices)):
            cash = max(cash, hold+prices[i]-fee)
            hold = max(hold, cash-prices[i])
        return cash