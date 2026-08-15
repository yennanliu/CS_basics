"""

3364. Minimum Positive Sum Subarray
Easy

You are given an integer array nums and two integers l and r. Your task is to find the minimum sum of a subarray whose size is between l and r (inclusive) and whose sum is greater than 0.

Return the minimum sum of such a subarray. If no such subarray exists, return -1.


Example 1:

Input: nums = [3, -2, 1, 4], l = 2, r = 3
Output: 1
Explanation:
The subarrays of length between l = 2 and r = 3 where the sum is greater than 0 are:
[3, -2] with a sum of 1
[1, 4] with a sum of 5
[3, -2, 1] with a sum of 2
[-2, 1, 4] with a sum of 3
Out of these, the subarray [3, -2] has a sum of 1, which is the smallest positive sum.

Example 2:

Input: nums = [-2, 2, -3, 1], l = 2, r = 3
Output: -1
Explanation:
There is no subarray of length between l and r that has a sum greater than 0.

Example 3:

Input: nums = [1, 2, 3, 4], l = 2, r = 4
Output: 3
Explanation:
The subarray [1, 2] has a length of 2 and the minimum sum greater than 0.


Constraints:

1 <= nums.length <= 100
1 <= l <= r <= nums.length
-1000 <= nums[i] <= 1000

"""

# V0
# IDEA : n <= 100 — ENUMERATE EVERY SUBARRAY IN THE LENGTH RANGE
#
#   at most 100 starts times 100 lengths, so extending a running sum from
#   each start and testing the ones whose length falls in [l, r] is well
#   within budget.
#
#   "greater than 0" is strict, so sums of exactly 0 are skipped, and -1
#   reports that nothing qualified.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def minimumSumSubarray(self, nums, l, r):
        n = len(nums)
        best = None
        for i in range(n):
            total = 0
            for j in range(i, n):
                total += nums[j]
                length = j - i + 1
                if length > r:
                    break
                if length >= l and total > 0:
                    if best is None or total < best:
                        best = total
        return -1 if best is None else best
