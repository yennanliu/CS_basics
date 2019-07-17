# Time:  O(logn)
# Space: O(1)

# Given an array nums containing n + 1 integers where each integer is
# between 1 and n (inclusive), prove that at least one duplicate number
# must exist. Assume that there is only one duplicate number, find the duplicate one.
#
# Note:
# You must not modify the array (assume the array is read only).
# You must use only constant, O(1) extra space.
# Your runtime complexity should be less than O(n2).
# There is only one duplicate number in the array, but it could be repeated more than once.

# The guess API is already defined for you.
# @param num, your guess
# @return -1 if my number is lower, 1 if my number is higher, otherwise return 0
# def guess(num):

# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/52038995
# IDEA : ITERATION 
# The guess API is already defined for you.
# @param num, your guess
# @return -1 if my number is lower, 1 if my number is higher, otherwise return 0
# def guess(num):
class Solution(object):
    def guessNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while True:
            mid = (left + right) / 2
            if guess(mid) == 1:
                left = mid + 1
            elif guess(mid) == -1:
                right = mid - 1
            else:
                return mid

# V1' 
# https://blog.csdn.net/coder_orz/article/details/52038995
# IDEA : RECURSION 
class Solution(object):
    def guessNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        return self.guessIn(1, n)

    def guessIn(self, left, right):
        mid = (left + right) / 2
        if guess(mid) == 0:
            return mid
        else:
            return self.guessIn(left, mid-1) if guess(mid) == -1 else self.guessIn(mid+1, right)

# V2
class Solution(object):
    def guessNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left <= right:
            mid = left + (right - left) / 2
            if Solution.guessNumber(mid) == 0:
                return  mid 
            elif Solution.guessNumber(mid) < 0: # noqa
                right = mid - 1
            else:
                left = mid + 1
        return left

# V3
class Solution(object):
    def guessNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left <= right:
            mid = left + (right - left) / 2
            if Solution.guessNumber(mid) <= 0: # noqa
                right = mid - 1
            else:
                left = mid + 1
        return left

# V4 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def guessNumber(self, n):
        """
        :type n: int
        :rtype: int
        """
        left, right = 1, n
        while left <= right:
            mid = left + (right - left) / 2
            if guess(mid) <= 0: # noqa
                right = mid - 1
            else:
                left = mid + 1
        return left
