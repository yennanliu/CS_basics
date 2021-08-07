"""

Implement pow(x, n), which calculates x raised to the power n (i.e., xn).

 

Example 1:

Input: x = 2.00000, n = 10
Output: 1024.00000
Example 2:

Input: x = 2.10000, n = 3
Output: 9.26100
Example 3:

Input: x = 2.00000, n = -2
Output: 0.25000
Explanation: 2-2 = 1/22 = 1/4 = 0.25
 

Constraints:

-100.0 < x < 100.0
-231 <= n <= 231-1
-104 <= xn <= 104


"""

# V0
# IDEA : RECURSION
# EXAMPLE :
# myPow(2, 10)
# -> myPow(4, 5)
# -> 4 * myPow(4, 4)
# -> 4 * myPow(16, 2)
# -> 4 * myPow(256, 1)
# -> 4 * 256 * myPow(myPow, 0)
# -> 4 * 256 * 1 
# => 1024 (final result)
class Solution(object):
    def myPow(self, x, n):
        if n == 0:
            return 1
        if n < 0:
            x = 1 / x
            n = -n
        ### NOTE THIS CONDITION
        if n % 2 == 1:
            return x * self.myPow(x, n - 1) ### NOTE : x * self.myPow(x, n - 1)
        return self.myPow(x * x, n / 2) ### NOTE : x * x

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82960833
class Solution(object):
    def myPow(self, x, n):
        """
        :type x: float
        :type n: int
        :rtype: float
        """
        if n == 0:
            return 1
        if n < 0:
            x = 1 / x
            n = -n
        if n % 2:
            return x * self.myPow(x, n - 1)
        return self.myPow(x * x, n / 2)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82960833
# IDEA : ITERATION (二分求冪)
# https://segmentfault.com/a/1190000020655190
class Solution(object):
    def myPow(self, x, n):
        """
        :type x: float
        :type n: int
        :rtype: float
        """
        if n == 0:
            return 1
        if n < 0:
            x = 1 / x
            n = -n
        ans = 1
        res = 1
        while n:
            if n % 2:
                ans *= x
            n >>= 1
            x *= x
        return ans

# V2 
# Time:  O(logn) = O(1)
# Space: O(1)
class Solution(object):
    def myPow(self, x, n):
        """
        :type x: float
        :type n: int
        :rtype: float
        """
        result = 1
        abs_n = abs(n)
        while abs_n:
            if abs_n & 1:
                result *= x
            abs_n >>= 1
            x *= x
        return 1 / result if n < 0 else result
        
# V3 
# Time:  O(logn)
# Space: O(logn)
# Recursive solution.
class Solution2(object):
    def myPow(self, x, n):
        """
        :type x: float
        :type n: int
        :rtype: float
        """
        if n < 0 and n != -n:
            return 1.0 / self.myPow(x, -n)
        if n == 0:
            return 1
        v = self.myPow(x, n / 2)
        if n % 2 == 0:
            return v * v
        else:
            return v * v * x