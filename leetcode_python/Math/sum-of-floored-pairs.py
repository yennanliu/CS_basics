"""

1862. Sum of Floored Pairs
Hard

Given an integer array nums, return the sum of floor(nums[i] / nums[j]) for all pairs of indices 0 <= i, j < nums.length in the array. Since the answer may be too large, return it modulo 10^9 + 7.

The floor() function returns the integer part of the division.


Example 1:

Input: nums = [2,5,9]
Output: 10
Explanation:
floor(2 / 5) = floor(2 / 9) = floor(5 / 9) = 0
floor(2 / 2) = floor(5 / 5) = floor(9 / 9) = 1
floor(5 / 2) = 2
floor(9 / 2) = 4
floor(9 / 5) = 1
We calculate the floor of the division for every pair of indices in the array then sum them up.

Example 2:

Input: nums = [7,7,7,7,7,7,7]
Output: 49


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : COUNTING + PREFIX SUM OVER MULTIPLES (harmonic series)
#
#   group by the DIVISOR value y. for a fixed y, floor(x / y) == d exactly
#   when x lies in the block [d*y, d*y + y - 1].
#   so the contribution of divisor y is
#     cnt[y] * sum_over_d ( d * (#numbers inside [d*y, d*y + y - 1]) )
#   and "#numbers inside a range" is O(1) with a prefix count array.
#
#   the d-loop runs mx/y times, so the total work is
#     mx/1 + mx/2 + ... + mx/mx = O(mx * log mx)   (harmonic sum).
#
#   NOTE : clip the upper bound with min(mx, d*y + y - 1) so the prefix
#          array is never read out of range.
#
# time = O(n + M log M), M = max(nums) <= 10^5
# space = O(M)
from collections import Counter
class Solution(object):
    def sumOfFlooredPairs(self, nums):
        MOD = 10 ** 9 + 7
        cnt = Counter(nums)
        mx = max(nums)

        # pre[v] = how many numbers are <= v
        pre = [0] * (mx + 1)
        for v in range(1, mx + 1):
            pre[v] = pre[v - 1] + cnt[v]

        res = 0
        for y in range(1, mx + 1):
            if not cnt[y]:
                continue
            d = 1
            while d * y <= mx:
                lo = d * y
                hi = min(mx, lo + y - 1)
                res += cnt[y] * d * (pre[hi] - pre[lo - 1])
                res %= MOD
                d += 1

        return res % MOD
