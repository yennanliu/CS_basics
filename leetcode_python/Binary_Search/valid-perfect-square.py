"""

367. Valid Perfect Square
Easy

Given a positive integer num, write a function which returns True if num is a perfect square else False.

Follow up: Do not use any built-in library function such as sqrt.

 

Example 1:

Input: num = 16
Output: true
Example 2:

Input: num = 14
Output: false
 

Constraints:

1 <= num <= 2^31 - 1

"""

# V0
# IDEA : use the fact 
#         -> that if there is no i * i == num for  i <= num
#         -> num is NOT a square number
class Solution(object):
    def isPerfectSquare(self, num):
        i = 1
        while i * i <= num:
            #print ("i = " + str(i))
            if i * i == num:
                return True
            i += 1
        return False

# V0'
# IDEA : BINARY SEARCH
class Solution(object):
    def isPerfectSquare(self, num):
        left, right = 0, num
        while left <= right:
            mid = (left + right) / 2
            if mid * mid >= num:
                right = mid - 1
            else:
                left = mid + 1
        return left * left == num

# V1 
# http://bookshadow.com/weblog/2016/06/26/leetcode-valid-perfect-square/
# IDEA : Newton's method 
class Solution(object):
    def isPerfectSquare(self, num):
        """
        :type num: int
        :rtype: bool
        """
        x = num
        while x * x > num:
            x = (x + num / x) / 2
        return x * x == num

# V1' 
# http://bookshadow.com/weblog/2016/06/26/leetcode-valid-perfect-square/
# IDEA : Binary search 
class Solution(object):
    def isPerfectSquare(self, num):
        """
        :type num: int
        :rtype: bool
        """
        left, right = 0, num
        while left <= right:
            mid = (left + right) / 2
            if mid * mid >= num:
                right = mid - 1
            else:
                left = mid + 1
        return left * left == num
        
# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def isPerfectSquare(self, num):
        """
        :type num: int
        :rtype: bool
        """
        left, right = 1, num
        while left <= right:
            mid = left + (right - left) / 2
            if mid >= num / mid:
                right = mid - 1
            else:
                left = mid + 1
        return left == num / left and num % left == 0