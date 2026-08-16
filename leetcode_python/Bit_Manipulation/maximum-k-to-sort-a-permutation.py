"""

3644. Maximum K to Sort a Permutation
Medium

You are given an integer array nums of length n, which is a permutation of the first n natural numbers, i.e. of [0, 1, ..., n - 1].

Given a non-negative integer k, you may repeatedly perform the following operation:

Choose two indices i and j such that (nums[i] AND nums[j]) == k, where AND denotes the bitwise AND operation, and swap nums[i] with nums[j].

Return the maximum value of k such that nums can be sorted in non-decreasing order using the operation above any number of times. If the array is already sorted, return 0.


Example 1:

Input: nums = [0,3,2,1]
Output: 1
Explanation:
nums[0] = 0 and nums[2] = 2 are already in place, so only 3 and 1 must move.
With k = 1 we may swap them, since 3 AND 1 == 1, giving [0,1,2,3].
No larger k works, because k must be a submask of every value that has to move.


Constraints:

1 <= n == nums.length <= 10^5
0 <= nums[i] <= n - 1
nums is a permutation of [0, 1, ..., n - 1].

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
