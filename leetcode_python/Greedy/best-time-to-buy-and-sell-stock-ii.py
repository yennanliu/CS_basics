# V0 

# V1 
# https://blog.csdn.net/monkeyduck/article/details/38079623
# TO CHECK :  LOCAL MAX VS GLOBAL MAX ( DP VS GREEDY)
class Solution:
    # @param prices, a list of integer
    # @return an integer
    def maxProfit(self, prices):
        profit = 0
        length = len(prices)
        for i in range(0,length-1):
            if prices[i+1] > prices[i]:
                profit += prices[i+1] - prices[i]
        return profit

# V2
# https://blog.csdn.net/coder_orz/article/details/52072136
# IDEA : GREEDY 
class Solution(object):
    def maxProfit(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        res = 0
        i = 0
        while i < len(prices):
            while i < len(prices)-1 and prices[i+1] <= prices[i]:
                i += 1
            j = i + 1
            while j < len(prices)-1 and prices[j+1] >= prices[j]:
                j += 1
            res += prices[j] - prices[i] if j < len(prices) else 0
            i = j + 1
        return res

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param prices, a list of integer
    # @return an integer
    def maxProfit(self, prices):
        profit = 0
        for i in range(len(prices) - 1):
            profit += max(0, prices[i + 1] - prices[i])
        return profit

    def maxProfit2(self, prices):
        return sum(map(lambda x: max(prices[x + 1] - prices[x], 0),
                       range(len(prices[:-1]))))
