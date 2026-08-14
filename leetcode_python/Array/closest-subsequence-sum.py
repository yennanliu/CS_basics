"""

1755. Closest Subsequence Sum
Hard

You are given an integer array nums and an integer goal.

You want to choose a subsequence of nums such that the sum of its elements is the closest possible to goal. That is, if the sum of the subsequence's elements is sum, then you want to minimize the absolute difference abs(sum - goal).

Return the minimum possible value of abs(sum - goal).

Note that a subsequence of an array is an array formed by removing some elements (possibly all or none) of the original array.

Example 1:

Input: nums = [5,-7,3,5], goal = 6
Output: 0
Explanation: Choose the whole array as a subsequence, with a sum of 6.
This is equal to the goal, so the absolute difference is 0.

Example 2:

Input: nums = [7,-9,15,-2], goal = -5
Output: 1
Explanation: Choose the subsequence [7,-9,-2], with a sum of -4.
The absolute difference is abs(-4 - (-5)) = abs(1) = 1, which is the minimum.

Example 3:

Input: nums = [1,2,3], goal = -7
Output: 7

Constraints:

1 <= nums.length <= 40
-10^7 <= nums[i] <= 10^7
-10^9 <= goal <= 10^9

"""

# V0
# IDEA : MEET IN THE MIDDLE (n <= 40 -> two halves of at most 2^20 subset sums)
#
#   2^40 subsequences is hopeless, but we can split nums in half and enumerate
#   every subset sum of each half (2^20 each). any subsequence sum is
#   left_sum + right_sum, so for each left sum l we want a right sum as close
#   as possible to goal - l -> sort the right sums and binary search.
#   NOTE : the two neighbours around the insertion point must both be checked,
#          the closest value can sit on either side.
#
# time = O(2^(n/2) * n), space = O(2^(n/2))
from bisect import bisect_left
class Solution(object):
    def minAbsDifference(self, nums, goal):
        def subset_sums(arr):
            sums = [0]
            for x in arr:
                sums = sums + [s + x for s in sums]
            return sums

        n = len(nums)
        left = subset_sums(nums[: n // 2])
        right = sorted(set(subset_sums(nums[n // 2:])))
        m = len(right)

        res = abs(goal)
        for l in left:
            need = goal - l
            i = bisect_left(right, need)
            if i < m:
                res = min(res, abs(need - right[i]))
            if i > 0:
                res = min(res, abs(need - right[i - 1]))
            if res == 0:
                return 0
        return res
