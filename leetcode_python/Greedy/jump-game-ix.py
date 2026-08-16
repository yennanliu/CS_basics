"""

3660. Jump Game IX
Medium

You are given an integer array nums.

From any index i, you can jump to another index j under the following rules:

Jump to index j where j > i is allowed only if nums[j] < nums[i].

Jump to index j where j < i is allowed only if nums[j] > nums[i].

For each index i, find the maximum value in nums that can be reached by
following any sequence of valid jumps starting at i.

Return an array ans where ans[i] is the maximum value reachable starting
from index i.

Example 1:

Input: nums = [2,1,3]
Output: [2,2,3]
Explanation:
For i = 0: No jump increases the value.
For i = 1: Jump to j = 0 as nums[j] = 2 is greater than nums[i].
For i = 2: Since nums[2] = 3 is the maximum value in nums, no jump increases
the value.
Thus, ans = [2, 2, 3].

Example 2:

Input: nums = [2,3,1]
Output: [3,3,3]
Explanation:
For i = 0: Jump forward to j = 2 as nums[j] = 1 is less than nums[i] = 2,
then from i = 2 jump to j = 1 as nums[j] = 3 is greater than nums[2].
For i = 1: Since nums[1] = 3 is the maximum value in nums, no jump increases
the value.
For i = 2: Jump to j = 1 as nums[j] = 3 is greater than nums[2] = 1.
Thus, ans = [3, 3, 3].

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : THE JUMP RELATION IS THE (SYMMETRIC) INVERSION GRAPH
#
#   look at the two rules for a fixed pair i < j: "i -> j when nums[j] <
#   nums[i]" and "j -> i when nums[i] > nums[j]" are the SAME condition. so
#   the graph is undirected and its edges are exactly the inversions.
#
#   the connected components of an inversion graph are the maximal blocks of
#   the classic "split into sorted chunks" decomposition: a cut after index i
#   is legal precisely when max(nums[0..i]) <= min(nums[i+1..n-1]), since no
#   inversion crosses such a cut, and inside a block every prefix max exceeds
#   some later element, which chains the whole block together.
#
#   so one left-to-right pass with a running max and a precomputed suffix min
#   cuts the array into components, and every index takes its block maximum.
#
# time = O(n), space = O(n)
class Solution(object):
    def maxValue(self, nums):
        n = len(nums)
        suf_min = [0] * (n + 1)
        suf_min[n] = float('inf')
        for i in range(n - 1, -1, -1):
            suf_min[i] = min(suf_min[i + 1], nums[i])

        ans = [0] * n
        run_max = float('-inf')
        start = 0
        for i in range(n):
            if nums[i] > run_max:
                run_max = nums[i]
            if run_max <= suf_min[i + 1]:
                for j in range(start, i + 1):
                    ans[j] = run_max
                start = i + 1
                run_max = float('-inf')
        return ans
