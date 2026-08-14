"""

2501. Longest Square Streak in an Array
Medium

You are given an integer array nums. A subsequence of nums is called a square streak if:

The length of the subsequence is at least 2, and
after sorting the subsequence, each element (except the first element) is the square of the previous number.

Return the length of the longest square streak in nums, or return -1 if there is no square streak.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [4,3,6,16,8,2]
Output: 3
Explanation: Choose the subsequence [4,16,2]. After sorting it, it becomes [2,4,16].
- 4 = 2 * 2.
- 16 = 4 * 4.
Therefore, [4,16,2] is a square streak.
It can be shown that every subsequence of length 4 is not a square streak.

Example 2:

Input: nums = [2,3,5,6,7]
Output: -1
Explanation: There is no square streak in nums so return -1.


Constraints:

2 <= nums.length <= 10^5
2 <= nums[i] <= 10^5

"""

# V0
# IDEA : HASH SET + FOLLOW THE SQUARING CHAIN
#
#   the order in the array is irrelevant (any subsequence can be sorted), so
#   the problem is just "longest chain x, x^2, x^4, ... all present".
#
#   dump nums into a set, then from every value keep squaring while the
#   value is still in the set, counting the hops.
#
#   NOTE : chains are extremely short -- nums[i] <= 10^5 and squaring at
#          least doubles the exponent, so a chain is at most
#          2 -> 4 -> 16 -> 256 -> 65536, i.e. length 5. that makes the whole
#          scan effectively O(n).
#   NOTE : a streak needs length >= 2, so only accept t > 1; otherwise -1.
#
# time = O(n * log log M), space = O(n)
class Solution(object):
    def longestSquareStreak(self, nums):
        seen = set(nums)
        res = -1
        for x in nums:
            t = 0
            cur = x
            while cur in seen:
                cur *= cur
                t += 1
            if t > 1:
                res = max(res, t)
        return res
