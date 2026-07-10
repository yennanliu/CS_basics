# V1  : DEV 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/83574784
# given equation : z = m * x + n * y
# find if it has solution integer pair (m, n )
# time = O(log(min(x, y)))
# space = O(log(min(x, y)))  # recursive gcd call stack
class Solution(object):
    def canMeasureWater(self, x, y, z):
        """
        :type x: int
        :type y: int
        :type z: int
        :rtype: bool
        """
        return z == 0 or (x + y >= z and z % self.gcd(x, y) == 0)
        
    def gcd(self, x, y):
        return x if y == 0 else self.gcd(y, x % y)

# V3
# time = O(logn), n is the max of (x, y)
# space = O(1)
class Solution(object):
    def canMeasureWater(self, x, y, z):
        """
        :type x: int
        :type y: int
        :type z: int
        :rtype: bool
        """
        def gcd(a, b):
            while b:
                a, b = b, a%b
            return a

        # The problem is to solve:
        # - check z <= x + y
        # - check if there is any (a, b) integers s.t. ax + by = z
        return z == 0 or ((z <= x + y) and (z % gcd(x, y) == 0))