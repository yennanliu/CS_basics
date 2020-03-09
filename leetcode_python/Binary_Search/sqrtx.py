# V0 
# IDEA : binary search 
class Solution(object):
    def mySqrt(self, x):
        """
        :type x: int
        :rtype: int
        """
        low, high, mid = 0, x, (x+0)//2
        while low <= high:
            if mid * mid > x:
                high = mid - 1
            else:
                low = mid + 1
            mid = (low+high)//2
        return mid

# V1 
# http://bookshadow.com/weblog/2015/08/29/leetcode-sqrtx/
# https://blog.csdn.net/fuxuemingzhu/article/details/79254648
# IDEA : binary search 
class Solution(object):
    def mySqrt(self, x):
        """
        :type x: int
        :rtype: int
        """
        low, high, mid = 0, x, int(x / 2)
        while low <= high:
            if mid * mid > x:
                high = mid - 1
            else:
                low = mid + 1
            mid = int((low + high) / 2)
        return mid

# V1' 
# http://bookshadow.com/weblog/2015/08/29/leetcode-sqrtx/
# IDEA : Newton's method 
# https://zh.wikipedia.org/wiki/%E7%89%9B%E9%A1%BF%E6%B3%95
class Solution(object):
    def mySqrt(self, x):
        """
        :type x: int
        :rtype: int
        """
        t = x
        while t * t > x:
            t = int(t / 2.0 + x / (2.0 * t))
        return t

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def mySqrt(self, x):
        """
        :type x: int
        :rtype: int
        """
        if x < 2:
            return x

        left, right = 1, x // 2
        while left <= right:
            mid = left + (right - left) // 2
            if mid > x / mid:
                right = mid - 1
            else:
                left = mid + 1
        return left - 1