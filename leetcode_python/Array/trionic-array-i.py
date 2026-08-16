"""

3637. Trionic Array I
Easy

You are given an integer array nums of length n.

An array is trionic if there exist indices 0 < p < q < n − 1 such that:

nums[0...p] is strictly increasing,

nums[p...q] is strictly decreasing,

nums[q...n − 1] is strictly increasing.

Return true if nums is trionic, otherwise return false.

Example 1:

Input: nums = [1,3,5,4,2,6]
Output: true
Explanation:
Pick p = 2, q = 4:
nums[0...2] = [1, 3, 5] is strictly increasing (1 < 3 < 5).
nums[2...4] = [5, 4, 2] is strictly decreasing (5 > 4 > 2).
nums[4...5] = [2, 6] is strictly increasing (2 < 6).

Example 2:

Input: nums = [2,1,3]
Output: false
Explanation:
There is no way to pick p and q to form the required three segments.

Constraints:

3 <= n <= 100
-1000 <= nums[i] <= 1000

"""

# V0
# IDEA : WALK THE THREE MONOTONE RUNS GREEDILY
#
#   the three sections are forced: the first one must be the maximal strictly
#   increasing run starting at index 0 (stopping earlier would leave an
#   increase at the start of the "decreasing" section), the second must be the
#   maximal strictly decreasing run after it, and the rest must then be
#   strictly increasing all the way to the end.
#
#   each section is required to be non-empty as a *step*, i.e. p > 0, q > p
#   and n - 1 > q, which is exactly "the pointer actually advanced".
#
# time = O(n), space = O(1)
class Solution(object):
    def isTrionic(self, nums):
        n = len(nums)
        i = 0
        while i + 1 < n and nums[i] < nums[i + 1]:
            i += 1
        if i == 0 or i == n - 1:
            return False
        p = i
        while i + 1 < n and nums[i] > nums[i + 1]:
            i += 1
        if i == p or i == n - 1:
            return False
        q = i
        while i + 1 < n and nums[i] < nums[i + 1]:
            i += 1
        return i == n - 1 and q < n - 1
