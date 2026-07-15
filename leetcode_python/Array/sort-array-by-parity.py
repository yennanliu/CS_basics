"""

905. Sort Array By Parity
Solved
Easy
Topics
premium lock icon
Companies
Given an integer array nums, move all the even integers at the beginning of the array followed by all the odd integers.

Return any array that satisfies this condition.

 

Example 1:

Input: nums = [3,1,2,4]
Output: [2,4,3,1]
Explanation: The outputs [4,2,3,1], [2,4,1,3], and [4,2,1,3] would also be accepted.
Example 2:

Input: nums = [0]
Output: [0]
 

Constraints:

1 <= nums.length <= 5000
0 <= nums[i] <= 5000

"""


# V0
# IDEA: CUSTOM SORT
class Solution(object):
    def sortArrayByParity(self, nums):
        # edge
        if len(nums) == 1:
            return nums
        nums.sort(key = lambda x: self.sort_helper(x))

        return nums

    def sort_helper(self, x):
        """
        NOTE !!!

        even val come first
        """
        if x == 0 or x % 2 == 0:
            return 0

        return 1


# V0-1
# IDEA: CUSTOM SORT (GPT)
class Solution(object):
    def sortArrayByParity(self, nums):
        nums.sort(key=lambda x: x % 2)
        return nums


# V0-2
# IDEA: CUSTOM SORT (GEMINI)
class Solution(object):
    def sortArrayByParity(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        # x % 2 is 0 for evens, 1 for odds.
        # Sorting by this naturally puts 0s (evens) before 1s (odds).
        nums.sort(key=lambda x: x % 2)
        return nums


# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82732066 
# time = O(n log n)
# space = O(n)
class Solution(object):
    def sortArrayByParity(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        return sorted(A, key = lambda x : x % 2)
        
# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def sortArrayByParity(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        i = 0
        for j in range(len(A)):
            if A[j] % 2 == 0:
                A[i], A[j] = A[j], A[i]
                i += 1
        return A
