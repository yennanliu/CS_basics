"""

3660. Jump Game IX
Medium

You are given an integer array nums of length n.

From an index i you are allowed to jump to an index j if:

j > i and nums[j] < nums[i], or
j < i and nums[j] > nums[i].

Return an array ans of length n where ans[i] is the maximum value of nums[k] over every index k that is reachable from index i using any number of jumps (index i itself counts as reachable).


Example 1:

Input: nums = [2,1,3]
Output: [2,2,3]
Explanation:
From index 0 you can jump to index 1 because 1 < 2, and from index 1 you can jump back to index 0 because 2 > 1. Index 2 is not reachable from either, so ans[0] = ans[1] = 2.
From index 2 no jump is possible, so ans[2] = 3.

Example 2:

Input: nums = [2,3,1]
Output: [3,3,3]
Explanation:
From index 0 you can jump to index 2 because 1 < 2, and from index 2 you can jump to index 1 because 3 > 1, so every index reaches the value 3.


Constraints:

1 <= n == nums.length <= 10^5
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
