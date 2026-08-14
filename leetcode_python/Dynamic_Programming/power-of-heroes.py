"""

2681. Power of Heroes
Hard

You are given a 0-indexed integer array nums representing the strength of some heroes. The power of a group of heroes is defined as follows:

Let i0, i1, ... ,ik be the indices of the heroes in a group. Then, the power of this group is max(nums[i0], nums[i1], ... ,nums[ik])^2 * min(nums[i0], nums[i1], ... ,nums[ik]).

Return the sum of the power of all non-empty groups of heroes possible. Since the sum could be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [2,1,4]
Output: 141
Explanation:
1st group: [2] has power = 2^2 * 2 = 8.
2nd group: [1] has power = 1^2 * 1 = 1.
3rd group: [4] has power = 4^2 * 4 = 64.
4th group: [2,1] has power = 2^2 * 1 = 4.
5th group: [2,4] has power = 4^2 * 2 = 32.
6th group: [1,4] has power = 4^2 * 1 = 16.
7th group: [2,1,4] has power = 4^2 * 1 = 16.
The sum of powers of all groups is 8 + 1 + 64 + 4 + 32 + 16 + 16 = 141.

Example 2:

Input: nums = [1,1,1]
Output: 7
Explanation: A total of 7 groups are possible, and the power of each group will be 1. Therefore, the sum of the powers of all groups is 7.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : SORT + RUNNING DP OVER "SUM OF WEIGHTED MAX^2"
#
#   only the max & min of a group matter, so the ORDER of nums is irrelevant
#   -> sort ascending first (a[0] <= a[1] <= ... <= a[n-1]).
#
#   fix a[i] as the group MINIMUM (ties broken by index, so every group is
#   counted exactly once). the group is a[i] plus any subset of a[i+1:].
#     - subset empty        -> max = a[i]           -> a[i]^3
#     - subset has max a[j] -> the elements strictly between i and j are free
#                              (2^(j-i-1) ways)     -> a[i] * a[j]^2 * 2^(j-i-1)
#
#   so  ans = sum_i ( a[i]^3 + a[i] * p_i ),  p_i = sum_{j>i} 2^(j-i-1) * a[j]^2
#
#   p has a clean backwards recurrence, which is the whole trick :
#       p_{i-1} = a[i]^2 + 2 * p_i
#   so one right-to-left sweep computes everything in O(n).
#   NOTE : iterate from the RIGHT so p is already the value for the current i.
#   NOTE : take the modulo everywhere - a[i]^3 alone can hit 10^27.
#
# time = O(n * log(n)) (dominated by the sort), space = O(1) extra
class Solution(object):
    def sumOfPower(self, nums):
        MOD = 10 ** 9 + 7
        nums.sort()
        ans = 0
        p = 0                                  # p_i = sum_{j>i} 2^(j-i-1)*a[j]^2
        for i in range(len(nums) - 1, -1, -1):
            x = nums[i]
            sq = x * x % MOD
            ans = (ans + sq * x + x * p) % MOD  # group of x alone + x as the min
            p = (p * 2 + sq) % MOD              # shift p to become p_{i-1}
        return ans % MOD
