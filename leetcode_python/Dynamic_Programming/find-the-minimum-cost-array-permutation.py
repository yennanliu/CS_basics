"""

3149. Find the Minimum Cost Array Permutation
Hard

You are given an array nums which is a permutation of [0, 1, 2, ..., n - 1]. The score of any permutation of [0, 1, 2, ..., n - 1] named perm is defined as:

score(perm) = |perm[0] - nums[perm[1]]| + |perm[1] - nums[perm[2]]| + ... + |perm[n - 1] - nums[perm[0]]|

Return the permutation perm which has the minimum possible score. If multiple permutations exist with this score, return the one that is lexicographically smallest among them.


Example 1:

Input: nums = [1,0,2]
Output: [0,1,2]
Explanation:
The lexicographically smallest permutation with minimum cost is [0,1,2]. The cost of this permutation is |0 - 0| + |1 - 2| + |2 - 1| = 2.

Example 2:

Input: nums = [0,2,1]
Output: [0,2,1]
Explanation:
The lexicographically smallest permutation with minimum cost is [0,2,1]. The cost of this permutation is |0 - 1| + |2 - 2| + |1 - 0| = 2.


Constraints:

2 <= n == nums.length <= 14
nums is a permutation of [0, 1, 2, ..., n - 1].

"""

# V0
# IDEA : HAMILTONIAN-CYCLE BITMASK DP, ANCHORED AT 0
#
#   the score is CYCLIC — the last term wraps back to perm[0] — so rotating a
#   permutation leaves its cost unchanged. every cycle therefore has a
#   rotation starting with 0, and starting with 0 is also the lexicographic
#   best, so perm[0] = 0 loses nothing.
#
#   with that anchor it is a travelling-salesman DP over
#       f(mask, last) = cheapest way to place the remaining indices, ending
#                       with the wrap-around term back to 0
#   stepping to an unused j costs |last - nums[j]|, and the base case
#   f(full, last) closes the cycle with |last - nums[0]|.
#
#   the answer is then rebuilt greedily : at each step take the SMALLEST j
#   that attains the optimum, which yields the lexicographically smallest
#   permutation among the minimum-cost ones.
#
# time = O(2^n * n^2), space = O(2^n * n)
class Solution(object):
    def findPermutation(self, nums):
        n = len(nums)
        full = (1 << n) - 1
        memo = {}

        def f(mask, last):
            if mask == full:
                return abs(last - nums[0])
            key = (mask, last)
            if key in memo:
                return memo[key]
            best = float('inf')
            for j in range(n):
                if not (mask >> j) & 1:
                    best = min(best, abs(last - nums[j]) + f(mask | (1 << j), j))
            memo[key] = best
            return best

        perm = [0]
        mask, last = 1, 0
        while mask != full:
            for j in range(n):                 # ascending -> lexicographically smallest
                if not (mask >> j) & 1:
                    if abs(last - nums[j]) + f(mask | (1 << j), j) == f(mask, last):
                        perm.append(j)
                        mask |= 1 << j
                        last = j
                        break
        return perm
