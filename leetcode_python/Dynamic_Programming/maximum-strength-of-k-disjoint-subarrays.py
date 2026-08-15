"""

3077. Maximum Strength of K Disjoint Subarrays
Hard

You are given a 0-indexed array of integers nums of length n, and a positive odd integer k.

The strength of x subarrays is defined as strength = sum[1] * x - sum[2] * (x - 1) + sum[3] * (x - 2) - sum[4] * (x - 3) + ... + sum[x] * 1 where sum[i] is the sum of the elements in the ith subarray. Formally, strength is sum of (-1)^(i+1) * sum[i] * (x - i + 1) over all i's such that 1 <= i <= x.

You need to select k disjoint subarrays from nums, such that their strength is maximum.

Return the maximum possible strength that can be obtained.

Note that the selected subarrays don't need to cover the entire array.


Example 1:

Input: nums = [1,2,3,-1,2], k = 3
Output: 22
Explanation: The best possible way to select 3 subarrays is: nums[0..2], nums[3..3], and nums[4..4]. The strength is (1 + 2 + 3) * 3 - (-1) * 2 + 2 * 1 = 22.

Example 2:

Input: nums = [12,-2,-2,-2,-2], k = 5
Output: 64
Explanation: The only possible way to select 5 disjoint subarrays is: nums[0..0], nums[1..1], nums[2..2], nums[3..3], and nums[4..4]. The strength is 12 * 5 - (-2) * 4 + (-2) * 3 - (-2) * 2 + (-2) * 1 = 64.

Example 3:

Input: nums = [-1,-2,-3], k = 1
Output: -1
Explanation: The best possible way to select 1 subarray is: nums[0..0]. The strength is -1.


Constraints:

1 <= n <= 10^4
-10^9 <= nums[i] <= 10^9
1 <= k <= n
1 <= n * k <= 10^6
k is odd.

"""

# V0
# IDEA : DP OVER (SUBARRAYS USED, PREFIX LENGTH), WITH A "STILL OPEN" STATE
#
#   the coefficient of the j-th chosen subarray is fixed in advance :
#       coef[j] = (k - j + 1) with a + sign for odd j and - for even j
#   so once we know a subarray is the j-th, every element inside it is worth
#   coef[j] and nothing else about it matters.
#
#   two rolling arrays over the prefix length i :
#       open[i] = best total where the j-th subarray is still OPEN at i
#                 (it must include nums[i-1])
#       done[i] = best total where at most j subarrays are finished by i
#
#   open[i] = max(open[i-1], done_prev[i-1]) + coef * nums[i-1]
#             (extend the current one, or start it right here)
#   done[i] = max(done[i-1], open[i])
#
#   `done_prev` is the previous j's table, so the new subarray always starts
#   after the previous one ended — disjointness for free.
#
#   states before j elements have been seen are impossible, hence the -inf
#   padding.
#
# time = O(n * k), space = O(n)
class Solution(object):
    def maximumStrength(self, nums, k):
        n = len(nums)
        NEG = float('-inf')

        done_prev = [0] * (n + 1)              # j = 0 : nothing chosen yet
        for j in range(1, k + 1):
            coef = (k - j + 1) * (1 if j % 2 else -1)
            open_cur = [NEG] * (n + 1)
            done_cur = [NEG] * (n + 1)
            for i in range(1, n + 1):
                best_start = done_prev[i - 1]
                extend = open_cur[i - 1]
                base = max(best_start, extend)
                open_cur[i] = NEG if base == NEG else base + coef * nums[i - 1]
                done_cur[i] = max(done_cur[i - 1], open_cur[i])
            done_prev = done_cur

        return done_prev[n]
