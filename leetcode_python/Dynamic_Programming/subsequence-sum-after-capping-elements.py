"""

3685. Subsequence Sum After Capping Elements
Medium

You are given an integer array nums of size n and a positive integer k.

An array capped by value x is obtained by replacing every element nums[i]
with min(nums[i], x).

For each integer x from 1 to n, determine whether it is possible to choose
a subsequence from the array capped by x such that the sum of the chosen
elements is exactly k.

Return a 0-indexed boolean array answer of size n, where answer[i] is true
if it is possible when using x = i + 1, and false otherwise.


Example 1:

Input: nums = [4,3,2,4], k = 5
Output: [false,false,true,true]
Explanation:
For x = 1, the capped array is [1, 1, 1, 1]. Possible sums are 1, 2, 3, 4,
so it is impossible to form a sum of 5.
For x = 2, the capped array is [2, 2, 2, 2]. Possible sums are 2, 4, 6, 8,
so it is impossible to form a sum of 5.
For x = 3, the capped array is [3, 3, 2, 3]. A subsequence [2, 3] sums to 5,
so it is possible.
For x = 4, the capped array is [4, 3, 2, 4]. A subsequence [3, 2] sums to 5,
so it is possible.

Example 2:

Input: nums = [1,2,3,4,5], k = 3
Output: [true,true,true,true,true]
Explanation:
For every value of x, it is always possible to select a subsequence from the
capped array that sums exactly to 3.


Constraints:

1 <= n == nums.length <= 4000
1 <= nums[i] <= n
1 <= k <= 4000

"""

# V0
# IDEA : SPLIT AT THE CAP -- UNTOUCHED ITEMS KEEP A DP, CAPPED ONES ARE COINS
#
#   capping at x cuts the array in two: elements <= x are unchanged, and
#   every element > x becomes an identical copy of x. so the second group is
#   not a subset-sum problem at all -- picking t of them contributes exactly
#   t*x, and only the COUNT c_x of such elements matters.
#
#   that leaves: is there a subset of {nums[i] <= x} summing to k - t*x for
#   some 0 <= t <= c_x? the crucial structural point is that the first group
#   only GROWS as x increases, so a single incremental subset-sum DP, fed
#   the elements equal to x as x sweeps upward, serves every x in turn --
#   no restart per x.
#
#   the DP is a python big-int used as a bitset (bit s set <=> sum s
#   reachable), so adding an element is one shift-or, and it is truncated to
#   k bits since larger partial sums are useless. the per-x scan over t is
#   cheap too: t <= k/x, and sum_x k/x is only about k*ln(n).
#
# time = O(n * k / 64 + k log n), space = O(k / 64 + n)
class Solution(object):
    def subsequenceSumAfterCapping(self, nums, k):
        n = len(nums)
        by_val = [0] * (n + 2)
        for v in nums:
            by_val[v] += 1

        mask = (1 << (k + 1)) - 1
        reach = 1                       # bit 0 set: sum 0 is reachable
        remaining = n                   # how many elements are still > x
        ans = [False] * n
        for x in range(1, n + 1):
            for _ in range(by_val[x]):  # elements equal to x join the exact pool
                reach |= reach << x
            reach &= mask
            remaining -= by_val[x]

            ok = False
            t = 0
            while t <= remaining and t * x <= k:
                if (reach >> (k - t * x)) & 1:
                    ok = True
                    break
                t += 1
            ans[x - 1] = ok
        return ans
