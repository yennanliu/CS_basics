"""

3640. Trionic Array II
Hard

You are given an integer array nums of length n.

A subarray nums[l...r] (with 0 <= l < r < n) is trionic if there exist indices l < p < q < r such that:

nums[l...p] is strictly increasing,
nums[p...q] is strictly decreasing,
nums[q...r] is strictly increasing.

Return the maximum sum of any trionic subarray of nums.


Example 1:

Input: nums = [1,3,5,4,2,6]
Output: 21
Explanation:
Pick l = 0, p = 2, q = 4, r = 5:
nums[0...2] = [1, 3, 5] is strictly increasing.
nums[2...4] = [5, 4, 2] is strictly decreasing.
nums[4...5] = [2, 6] is strictly increasing.
The sum is 1 + 3 + 5 + 4 + 2 + 6 = 21.


Constraints:

4 <= n <= 10^5
-10^9 <= nums[i] <= 10^9
It is guaranteed that at least one trionic subarray exists.

"""

# V0
# IDEA : LINEAR DP OVER THE THREE PHASES (UP -> DOWN -> UP)
#
#   read the shape as a tiny automaton whose state is "which of the three
#   monotone phases am i currently in". walking i from left to right, each
#   state keeps the best sum of a partial trionic subarray that ENDS at i.
#
#     inc[i]  best sum of a strictly increasing run ending at i (>= 1 element)
#     up[i]   phase 1 finished at i, so at least 2 elements  -> inc[i-1]+nums[i]
#     down[i] phase 2, at least one step down               -> max(up,down)+nums[i]
#     top[i]  phase 3, at least one step up after the dip   -> max(down,top)+nums[i]
#
#   the "start later" freedom is already inside inc: inc[i] drops the prefix
#   whenever it is negative, which is exactly kadane. the later phases must
#   NOT restart, since their prefix is what makes the subarray trionic, so
#   they only ever extend.
#
#   the answer is the best top[i] — reaching state top guarantees l<p<q<r.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxSumTrionic(self, nums):
        n = len(nums)
        NEG = float('-inf')
        inc = nums[0]
        up = down = top = NEG
        best = NEG
        for i in range(1, n):
            x = nums[i]
            p_inc, p_up, p_down, p_top = inc, up, down, top
            if x > nums[i - 1]:
                inc = x + (p_inc if p_inc > 0 else 0)
                up = p_inc + x
                down = NEG
                top = (max(p_down, p_top) + x) if max(p_down, p_top) != NEG else NEG
            elif x < nums[i - 1]:
                inc = x
                up = NEG
                down = (max(p_up, p_down) + x) if max(p_up, p_down) != NEG else NEG
                top = NEG
            else:
                inc = x
                up = down = top = NEG
            if top > best:
                best = top
        return best
