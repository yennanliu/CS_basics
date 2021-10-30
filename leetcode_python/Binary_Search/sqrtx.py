"""

Given a non-negative integer x, compute and return the square root of x.

Since the return type is an integer, the decimal digits are truncated, and only the integer part of the result is returned.

Note: You are not allowed to use any built-in exponent function or operator, such as pow(x, 0.5) or x ** 0.5.

 

Example 1:

Input: x = 4
Output: 2
Example 2:

Input: x = 8
Output: 2
Explanation: The square root of 8 is 2.82842..., and since the decimal part is truncated, 2 is returned.
 

Constraints:

0 <= x <= 231 - 1

"""

# V0
# IDEA : binary search
class Solution(object):
    def mySqrt(self, num):
        if num <= 1:
            return num
        l = 0
        r = num - 1
        while r >= l:
            mid = l + (r - l) // 2
            if mid * mid == num:
                return mid
            elif mid * mid > num:
                r = mid - 1
            else:
                l = mid + 1
        return l * l if l * l < num else l - 1

# V0'
# IDEA : binary search 
class Solution(object):
    def mySqrt(self, x):
        # NOTE : this approach calculates mid 2 times
        low, high, mid = 0, x, int(x / 2)
        while low <= high:
            if mid * mid > x:
                high = mid - 1
            else:
                low = mid + 1
            # NOTE : this approach calculates mid 2 times
            mid = int((low + high) / 2)
        return mid

# V0''
# IDEA : binary search
class Solution(object):
    def mySqrt(self, x):
        l, r = 0, x
        mid = (l + r) // 2
        while r >= l:
            if mid * mid == x: # optional
                return mid
            elif mid * mid > x:
                ### NOTE THE CONDITION
                r = mid - 1
            else:
                ### NOTE THE CONDITION
                l = mid + 1
            mid = (l + r) // 2
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