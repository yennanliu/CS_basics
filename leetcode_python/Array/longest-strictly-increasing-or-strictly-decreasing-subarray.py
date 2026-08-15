"""

3105. Longest Strictly Increasing or Strictly Decreasing Subarray
Easy

You are given an array of integers nums. Return the length of the longest subarray of nums which is either strictly increasing or strictly decreasing.


Example 1:

Input: nums = [1,4,3,3,2]
Output: 2
Explanation:
The strictly increasing subarrays of nums are [1], [2], [3], [3], [4], and [1,4].
The strictly decreasing subarrays of nums are [1], [2], [3], [3], [4], [3,2], and [4,3].
Hence, we return 2.

Example 2:

Input: nums = [3,3,3,3]
Output: 1
Explanation:
The strictly increasing subarrays of nums are [3], [3], [3], and [3].
The strictly decreasing subarrays of nums are [3], [3], [3], and [3].
Hence, we return 1.

Example 3:

Input: nums = [3,2,1]
Output: 3
Explanation:
The strictly increasing subarrays of nums are [3], [2], and [1].
The strictly decreasing subarrays of nums are [3], [2], [1], [3,2], [2,1], and [3,2,1].
Hence, we return 3.


Constraints:

1 <= nums.length <= 50
1 <= nums[i] <= 50

"""

# V0
# IDEA : TWO RUNNING STREAK COUNTERS, ONE PER DIRECTION
#
#   `inc` is the length of the strictly increasing run ending here and `dec`
#   the strictly decreasing one. each extends when the comparison holds and
#   resets to 1 otherwise — equal neighbours reset BOTH, which is why
#   [3,3,3,3] answers 1.
#
# time = O(n), space = O(1)
class Solution(object):
    def longestMonotonicSubarray(self, nums):
        inc = dec = 1
        res = 1
        for i in range(1, len(nums)):
            inc = inc + 1 if nums[i] > nums[i - 1] else 1
            dec = dec + 1 if nums[i] < nums[i - 1] else 1
            res = max(res, inc, dec)
        return res
