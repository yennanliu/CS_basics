"""

2025. Maximum Number of Ways to Partition an Array
Hard

You are given a 0-indexed integer array nums of length n. The number of ways to partition nums is the number of pivot indices that satisfy both conditions:

1 <= pivot < n
nums[0] + nums[1] + ... + nums[pivot - 1] == nums[pivot] + nums[pivot + 1] + ... + nums[n - 1]

You are also given an integer k. You can choose to change the value of one element of nums to k, or to leave the array unchanged.

Return the maximum possible number of ways to partition nums to satisfy both conditions after changing at most one element.


Example 1:

Input: nums = [2,-1,2], k = 3
Output: 1
Explanation: One optimal approach is to change nums[0] to k. The array becomes [3,-1,2].
There is one way to partition the array:
- For pivot = 2, we have the partition [3,-1 | 2]: 3 + -1 == 2.

Example 2:

Input: nums = [0,0,0], k = 1
Output: 2
Explanation: The optimal approach is to leave the array unchanged.
There are two ways to partition the array:
- For pivot = 1, we have the partition [0 | 0,0]: 0 == 0 + 0.
- For pivot = 2, we have the partition [0,0 | 0]: 0 + 0 == 0.

Example 3:

Input: nums = [22,4,-25,-20,-15,15,-16,7,19,-10,0,-13,-14], k = -33
Output: 4
Explanation: One optimal approach is to change nums[2] to k. The array becomes [22,4,-33,-20,-15,15,-16,7,19,-10,0,-13,-14].
There are four ways to partition the array.


Constraints:

n == nums.length
2 <= n <= 10^5
-10^5 <= k, nums[i] <= 10^5

"""

# V0
# IDEA : PREFIX SUMS + TWO COUNTERS OF THE "DIFFERENCE" 2*prefix - total
#
#   for pivot p (1 <= p < n) the split is balanced iff
#       2 * prefix[p] == total          i.e.  diff(p) = 2*prefix[p] - total == 0
#
#   changing nums[i] to k shifts total by  delta = k - nums[i]  and shifts
#   prefix[p] by delta only for p > i. so after the change :
#       p <= i :  diff'(p) = 2*prefix[p] - (total + delta) = diff(p) - delta
#       p >  i :  diff'(p) = diff(p) + 2*delta - delta     = diff(p) + delta
#
#   so the count for "change index i" is
#       #{ p <= i : diff(p) == delta } + #{ p > i : diff(p) == -delta }
#
#   sweep i from 0..n-1 moving pivots from a `right` counter into a `left`
#   counter, and read both lookups in O(1).
#
#   trying every i and recounting every pivot is O(n^2) — too slow at 10^5.
#
#   NOTE : pivots live in [1, n-1]; index i = 0 starts with an empty left.
#          the "leave unchanged" case is just the plain count of diff(p) == 0.
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def waysToPartition(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        n = len(nums)

        # prefix[p] = nums[0] + ... + nums[p - 1]
        prefix = [0] * (n + 1)
        for i in range(n):
            prefix[i + 1] = prefix[i] + nums[i]
        total = prefix[n]

        # right[d] = how many pivots p > i have diff(p) == d
        # left[d]  = how many pivots p <= i have diff(p) == d
        right = defaultdict(int)
        left = defaultdict(int)
        for pivot in range(1, n):
            right[2 * prefix[pivot] - total] += 1

        # option 1 : leave the array unchanged
        best = right[0]

        # option 2 : change nums[i] to k, for every i
        for i in range(n):
            delta = k - nums[i]
            # NOTE !!! left side needs diff == delta, right side diff == -delta
            best = max(best, left[delta] + right[-delta])

            # pivot p = i + 1 moves from the right side to the left side
            if i + 1 < n:
                diff = 2 * prefix[i + 1] - total
                right[diff] -= 1
                left[diff] += 1

        return best
