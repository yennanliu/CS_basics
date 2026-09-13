"""

374. Guess Number Higher or Lower
Easy

We are playing the Guess Game. The game is as follows:

I pick a number from 1 to n. You have to guess which number I picked (the number I picked stays the same throughout the game).

Every time you guess wrong, I will tell you whether the number I picked is higher or lower than your guess.

You call a pre-defined API int guess(int num), which returns three possible results:

-1: Your guess is higher than the number I picked (i.e. num > pick).
1: Your guess is lower than the number I picked (i.e. num < pick).
0: your guess is equal to the number I picked (i.e. num == pick).

Return the number that I picked.

Example 1:

Input: n = 10, pick = 6
Output: 6

Example 2:

Input: n = 1, pick = 1
Output: 1

Example 3:

Input: n = 2, pick = 1
Output: 1

Constraints:

1 <= n <= 2^31 - 1
1 <= pick <= n

"""

# time = O(logn)
# space = O(1)

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
# time = O(log n)
# space = O(1)
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
# time = O(log n)
# space = O(log n) (recursion call stack)
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
# time = O(log n)
# space = O(1)
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
# time = O(log n)
# space = O(1)
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
# time = O(log n)
# space = O(1)
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
