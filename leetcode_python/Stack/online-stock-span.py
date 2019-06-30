# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82781059
class StockSpanner(object):

    def __init__(self):
        self.a = []

    def next(self, price):
        """
        :type price: int
        :rtype: int
        """
        res = 1
        while self.a and self.a[-1][0] <= price:
            res += self.a.pop()[1]
        self.a.append((price, res))
        return res      
# Your StockSpanner object will be instantiated and called as such:
# obj = StockSpanner()
# param_1 = obj.next(price)

# V2 
# Time:  O(n)
# Space: O(n)
class StockSpanner(object):

    def __init__(self):
        self.__s = []

    def next(self, price):
        """
        :type price: int
        :rtype: int
        """
        result = 1
        while self.__s and self.__s[-1][0] <= price:
            result += self.__s.pop()[1]
        self.__s.append([price, result])
        return result