"""

1493. Longest Subarray of 1's After Deleting One Element
Medium

Given a binary array nums, you should delete one element from it.

Return the size of the longest non-empty subarray containing only 1's in the resulting array. Return 0 if there is no such subarray.


Example 1:

Input: nums = [1,1,0,1]
Output: 3
Explanation: After deleting the number in position 2, [1,1,1] contains 3 numbers with value of 1's.

Example 2:

Input: nums = [0,1,1,1,0,1,1,0,1]
Output: 5
Explanation: After deleting the number in position 4, [0,1,1,1,1,1,0,1] longest subarray with value of 1's is [1,1,1,1,1].

Example 3:

Input: nums = [1,1,1]
Output: 2
Explanation: You must delete one element.


Constraints:

1 <= nums.length <= 10^5
nums[i] is either 0 or 1.

"""

# V0
# IDEA : SLIDING WINDOW (longest window holding at most one 0)
#
#   deleting exactly one element from a window of 1's + at most one 0
#   leaves (window length - 1) ones. so :
#
#     answer = max window length containing <= one 0, minus 1
#
#   slide right pointer i, count zeros; while zeros > 1 shrink from left.
#   NOTE : we record `i - j` (NOT i - j + 1) which already subtracts the
#          one deleted element, so the all-ones case correctly gives n - 1.
#
# time = O(n), space = O(1)
class Solution(object):
    def longestSubarray(self, nums):
        res = 0
        zeros = 0
        j = 0
        for i in range(len(nums)):
            if nums[i] == 0:
                zeros += 1
            while zeros > 1:
                if nums[j] == 0:
                    zeros -= 1
                j += 1
            res = max(res, i - j)
        return res
