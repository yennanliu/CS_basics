"""

3644. Maximum K to Sort a Permutation
Medium

You are given an integer array nums of length n, where nums is a permutation
of the numbers in the range [0..n - 1].

You may swap elements at indices i and j only if nums[i] AND nums[j] == k,
where AND denotes the bitwise AND operation and k is a non-negative integer.

Return the maximum value of k such that the array can be sorted in non-
decreasing order using any number of such swaps. If nums is already sorted,
return 0.

Example 1:

Input: nums = [0,3,2,1]
Output: 1
Explanation:
Choose k = 1. Swapping nums[1] = 3 and nums[3] = 1 is allowed since nums[1]
AND nums[3] == 1, resulting in a sorted permutation: [0, 1, 2, 3].

Example 2:

Input: nums = [0,1,3,2]
Output: 2
Explanation:
Choose k = 2. Swapping nums[2] = 3 and nums[3] = 2 is allowed since nums[2]
AND nums[3] == 2, resulting in a sorted permutation: [0, 1, 2, 3].

Example 3:

Input: nums = [3,2,1,0]
Output: 0
Explanation:
Only k = 0 allows sorting since no greater k allows the required swaps where
nums[i] AND nums[j] == k.

Constraints:

1 <= n == nums.length <= 10^5
0 <= nums[i] <= n - 1
nums is a permutation of integers from 0 to n - 1.

"""

# V0
# IDEA : k MUST BE A SUBMASK OF EVERY MISPLACED VALUE — SO TAKE THEIR AND
#
#   necessity: a value sitting on the wrong index has to be swapped at least
#   once, and every swap involving v needs (v AND w) == k, which forces k to
#   be a submask of v. so k divides (bitwise) every misplaced value, i.e.
#   k <= AND of them all.
#
#   sufficiency: let k be exactly that AND. because 0 <= k < n the VALUE k is
#   itself somewhere in the permutation, and for every misplaced v we have
#   k AND v == k, so the element holding k is a universal hub that can be
#   swapped with anything that must move. an arbitrary permutation of the
#   misplaced elements can be realised as a sequence of hub swaps, so the
#   array can be sorted.
#
#   an already sorted array needs no swap at all; the problem pins that case
#   to 0.
#
# time = O(n), space = O(1)
class Solution(object):
    def sortPermutation(self, nums):
        res = -1  # all ones, identity of AND
        for i, v in enumerate(nums):
            if v != i:
                res &= v
        return 0 if res == -1 else res
