"""

1708. Largest Subarray Length K
Easy

An array A is larger than some array B if for the first index i where A[i] != B[i], A[i] > B[i].

For example, consider 0-indexing:

[1,3,2,4] > [1,2,2,4], since at index 1, 3 > 2.
[1,4,4,4] < [2,1,1,1], since at index 0, 1 < 2.

A subarray is a contiguous subsequence of the array.

Given an integer array nums of distinct integers, return the largest subarray of nums of length k.


Example 1:

Input: nums = [1,4,5,2,3], k = 3
Output: [5,2,3]
Explanation: The subarrays of size 3 are: [1,4,5], [4,5,2], and [5,2,3].
Of these, [5,2,3] is the largest.

Example 2:

Input: nums = [1,4,5,2,3], k = 4
Output: [4,5,2,3]
Explanation: The subarrays of size 4 are: [1,4,5,2], and [4,5,2,3].
Of these, [4,5,2,3] is the largest.

Example 3:

Input: nums = [1,4,5,2,3], k = 1
Output: [5]


Constraints:

1 <= k <= nums.length <= 10^5
1 <= nums[i] <= 10^9
All the integers of nums are unique.


Follow up: What if the integers in nums are not distinct?

"""

# V0
# IDEA : GREEDY (all values are DISTINCT -> the first element decides everything)
#
#   comparison is lexicographic, so the subarray with the biggest FIRST
#   element already wins - no tie is ever possible because nums has no
#   duplicates. so we only need argmax over the legal start positions.
#
#   a subarray of length k must start in [0, n - k], hence:
#     i = index of max(nums[0 .. n-k])
#     answer = nums[i : i + k]
#
#   NOTE : the follow-up (duplicates allowed) would need tie-breaking -
#          on a tie you must compare the next element, e.g. keep all
#          candidate starts and narrow them position by position.
#
# time = O(n), space = O(1) excluding the output
class Solution(object):
    def largestSubarray(self, nums, k):
        n = len(nums)
        best = 0
        for i in range(1, n - k + 1):
            if nums[i] > nums[best]:
                best = i
        return nums[best:best + k]
