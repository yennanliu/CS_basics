# V0 
# IDEA : GREEDY
class Solution:
    def maxProfit(self, prices):
        profit = 0
        for i in range(0,len(prices)-1):
            if prices[i+1] > prices[i]:
                # have to sale last stock, then buy a new one
                # and sum up the price difference into profit
                profit += prices[i+1] - prices[i]
        return profit

# V1 
# https://blog.csdn.net/monkeyduck/article/details/38079623
# IDEA : GREEDY
# TO CHECK :  LOCAL MAX VS GLOBAL MAX ( DP VS GREEDY)
# DEMO1
# prices = [1,2,3]
# -> when price = 1, => r=0+(2)-1=1
# -> when price = 2, => r=1+(3)-2=2
# -> so profit=2
# DEMO2
# prices = [7,4,3]
# -> when price = 7, => r=0
# -> when price = 4, => r=0+0
# -> so profit=0
class Solution:
    # @param prices, a list of integer
    # @return an integer
    def maxProfit(self, prices):
        profit = 0
        length = len(prices)
        for i in range(0,length-1):
            if prices[i+1] > prices[i]:
                # have to sale last stock, then buy a new one
                # and sum up the price difference into profit
                profit += prices[i+1] - prices[i]
        return profit

### Test case
s=Solution()
assert s.maxProfit([1,2,3])==2
assert s.maxProfit([7,4,3])==0
assert s.maxProfit([0,1,0])==1
assert s.maxProfit([])==0
assert s.maxProfit([0])==0
assert s.maxProfit([1])==0
assert s.maxProfit([99])==0
assert s.maxProfit([1,2])==1
assert s.maxProfit([-1])==0
s.maxProfit([-1,-1])==0
s.maxProfit([-1,-1,-1])==0
assert s.maxProfit([_ for _ in range(99)])==98

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
