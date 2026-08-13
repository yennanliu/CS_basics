"""

996. Number of Squareful Arrays
Hard

An array is squareful if the sum of every pair of adjacent elements is a perfect square.

Given an integer array nums, return the number of permutations of nums that are squareful.

Two permutations perm1 and perm2 are different if there is some index i such that perm1[i] != perm2[i].

Example 1:

Input: nums = [1,17,8]
Output: 2
Explanation: [1,8,17] and [17,8,1] are the valid permutations.

Example 2:

Input: nums = [2,2,2]
Output: 1

Constraints:

1 <= nums.length <= 12
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : BACKTRACKING (permutations with duplicates) + PRUNING
#
#  - n <= 12, so we enumerate permutations, but prune HARD: a candidate is
#    only appended when (last_picked + candidate) is a perfect square.
#  - Duplicate handling (the classic "permutations II" trick):
#      sort first, then skip nums[i] if nums[i] == nums[i-1] and nums[i-1]
#      is NOT currently used. That forces equal values to be consumed in
#      left-to-right order, so each distinct permutation is counted once.
#  - math.isqrt gives an EXACT integer square root (no float rounding issues
#    even for sums up to 2 * 10^9).
#
# time = O(n!) worst case, far less in practice thanks to the square pruning
# space = O(n)
import math
class Solution(object):
    def numSquarefulPerms(self, nums):

        def is_square(v):
            r = math.isqrt(v)
            return r * r == v

        nums.sort()
        n = len(nums)
        used = [False] * n
        self.res = 0

        def backtrack(path):
            if len(path) == n:
                self.res += 1
                return
            for i in range(n):
                if used[i]:
                    continue
                # skip duplicates at the same recursion depth
                if i > 0 and nums[i] == nums[i - 1] and not used[i - 1]:
                    continue
                # adjacent sum must be a perfect square
                if path and not is_square(path[-1] + nums[i]):
                    continue
                used[i] = True
                path.append(nums[i])
                backtrack(path)
                path.pop()
                used[i] = False

        backtrack([])
        return self.res
