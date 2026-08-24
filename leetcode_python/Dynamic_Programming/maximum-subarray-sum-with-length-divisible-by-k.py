"""

3381. Maximum Subarray Sum With Length Divisible by K
Medium

You are given an array of integers nums and an integer k.

Return the maximum sum of a subarray of nums, such that the size of the subarray is divisible by k.


Example 1:

Input: nums = [1,2], k = 1
Output: 3
Explanation:
The subarray [1, 2] with sum 3 has length equal to 2 which is divisible by 1.

Example 2:

Input: nums = [-1,-2,-3,-4,-5], k = 4
Output: -10
Explanation:
The maximum sum subarray is [-1, -2, -3, -4] which has length equal to 4 which is divisible by 4.

Example 3:

Input: nums = [-5,1,2,-3,4], k = 2
Output: 4
Explanation:
The maximum sum subarray is [1, 2, -3, 4] which has length equal to 4 which is divisible by 2.


Constraints:

1 <= k <= nums.length <= 2 * 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : PREFIX SUMS, KEEPING THE SMALLEST PREFIX PER INDEX RESIDUE
#
#   the sum of nums[l..r] is pre[r+1] - pre[l], and the length r - l + 1 is a
#   multiple of k exactly when l and r+1 agree modulo k. so for each right
#   boundary the only competitors are the earlier boundaries in the SAME
#   residue class.
#
#   so sweep the prefix sums, and for each residue keep the smallest prefix
#   seen so far — subtracting it maximises the current subarray.
#
#   the array can be all negative, so the running best starts at -inf rather
#   than 0 (an empty subarray is not allowed).
#
"""

DP def
    sum(nums[l..r]) = pre[r+1] - pre[l], and the length r - l + 1 is a multiple
    of k EXACTLY when l and r+1 agree modulo k -> for each right boundary the
    only competitors are earlier boundaries in the SAME residue class

    best_prefix[r]: the SMALLEST prefix sum seen so far among prefixes

                    whose length % k == r

DP eq

     at index i, with pre = prefix sum of the first i+1 elements
     and r = (i + 1) % k:

        res = max( res, pre - best_prefix[r] )

        best_prefix[r] = min( best_prefix[r], pre )


    -> e.g. subtracting the smallest same-residue prefix MAXIMISES the
              current subarray

     init: best_prefix[0] = 0 (the empty prefix), rest +inf

     the array can be all negative and an empty subarray is NOT allowed,
     so res starts at -inf

     ans = res

"""
# time = O(n), space = O(k)
class Solution(object):
    def maxSubarraySum(self, nums, k):
        INF = float('inf')
        best_prefix = [INF] * k
        best_prefix[0] = 0                      # prefix of length 0, residue 0

        res = float('-inf')
        pre = 0
        for i, x in enumerate(nums):
            pre += x
            r = (i + 1) % k
            if best_prefix[r] != INF:
                res = max(res, pre - best_prefix[r])
            if pre < best_prefix[r]:
                best_prefix[r] = pre
        return res
