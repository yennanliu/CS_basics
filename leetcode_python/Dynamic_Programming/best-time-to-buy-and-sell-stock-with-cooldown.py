# V0 ; DEV 

# V1 
# http://bookshadow.com/weblog/2015/11/24/leetcode-best-time-to-buy-and-sell-stock-with-cooldown/
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        size = len(prices)
        if not size:
            return 0
        buys = [None] * size
        sells = [None] * size
        sells[0], buys[0] = 0, -prices[0]
        for x in range(1, size):
            delta = prices[x] - prices[x - 1]
            sells[x] = max(buys[x - 1] + prices[x], sells[x - 1] + delta)
            buys[x] = max(buys[x - 1] - delta, \
                          sells[x - 2] - prices[x] if x > 1 else None)
        return max(sells)

# V1'
# http://bookshadow.com/weblog/2015/11/24/leetcode-best-time-to-buy-and-sell-stock-with-cooldown/
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        size = len(prices)
        if size < 2:
            return 0
        buys = [None] * size
        sells = [None] * size
        sells[0], sells[1] = 0, max(0, prices[1] - prices[0])
        buys[0], buys[1] = -prices[0], max(-prices[0], -prices[1])
        for x in range(2, size):
            sells[x] = max(sells[x - 1], buys[x - 1] + prices[x])
            buys[x] = max(buys[x - 1], sells[x - 2] - prices[x])
        return sells[-1]
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if not prices:
            return 0
        buy, sell, coolDown = [0] * 2, [0] * 2, [0] * 2
        buy[0] = -prices[0]
        for i in range(1, len(prices)):
            # Bought before or buy today.
            buy[i % 2] = max(buy[(i - 1) % 2],
                             coolDown[(i - 1) % 2] - prices[i])
            # Sell today.
            sell[i % 2] = buy[(i - 1) % 2] + prices[i]
            # Sold before yesterday or sold yesterday.
            coolDown[i % 2] = max(coolDown[(i - 1) % 2], sell[(i - 1) % 2])
        return max(coolDown[(len(prices) - 1) % 2],
                   sell[(len(prices) - 1) % 2])
