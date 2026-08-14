"""

2035. Partition Array Into Two Arrays to Minimize Sum Difference
Hard

You are given an integer array nums of 2 * n integers. You need to partition nums into two arrays of length n to minimize the absolute difference of the sums of the arrays. To partition nums, put each element of nums into one of the two arrays.

Return the minimum possible absolute difference.


Example 1:

Input: nums = [3,9,7,3]
Output: 2
Explanation: One optimal partition is: [3,9] and [7,3].
The absolute difference between the sums of the arrays is abs((3 + 9) - (7 + 3)) = 2.

Example 2:

Input: nums = [-36,36]
Output: 72
Explanation: One optimal partition is: [-36] and [36].
The absolute difference between the sums of the arrays is abs((-36) - (36)) = 72.

Example 3:

Input: nums = [2,-1,0,4,-2,-9]
Output: 0
Explanation: One optimal partition is: [2,4,-9] and [-1,0,-2].
The absolute difference between the sums of the arrays is abs((2 + 4 + -9) - (-1 + 0 + -2)) = 0.


Constraints:

1 <= n <= 15
nums.length == 2 * n
-10^7 <= nums[i] <= 10^7

"""

# V0
# IDEA : MEET IN THE MIDDLE (2^30 is too big, 2 * 2^15 is not)
#
#   split nums into a left half L and a right half R (n elements each).
#   any valid partition picks k elements from L and n - k from R.
#
#   enumerate every subset of each half, bucketed by POPCOUNT :
#       left[k]  = sorted list of subset sums of L using exactly k elements
#       right[k] = same for R
#
#   if the first array takes sum `a` from L (k elements) and `b` from R
#   (n - k elements), the difference is
#       abs(total - 2 * (a + b))
#   so for each `a` we want `b` as close as possible to (total / 2 - a) —
#   a binary search into the sorted right[n - k].
#
# time = O(2^n * n), space = O(2^n)
import bisect


class Solution(object):
    def minimumDifference(self, nums):
        n = len(nums) // 2
        total = sum(nums)

        def subset_sums(arr):
            groups = [[] for _ in range(len(arr) + 1)]
            for mask in range(1 << len(arr)):
                s = 0
                bits = 0
                for i in range(len(arr)):
                    if mask >> i & 1:
                        s += arr[i]
                        bits += 1
                groups[bits].append(s)
            return groups

        left = subset_sums(nums[:n])
        right = subset_sums(nums[n:])
        for g in right:
            g.sort()

        res = float('inf')
        for k in range(n + 1):
            cand = right[n - k]
            for a in left[k]:
                # want  a + b  as close as possible to total / 2
                target = total / 2.0 - a
                i = bisect.bisect_left(cand, target)
                for j in (i - 1, i):
                    if 0 <= j < len(cand):
                        res = min(res, abs(total - 2 * (a + cand[j])))
        return res
