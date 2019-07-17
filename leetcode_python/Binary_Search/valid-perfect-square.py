# V0 

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