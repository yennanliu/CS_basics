"""

2741. Special Permutations
Medium

You are given a 0-indexed integer array nums containing n distinct positive integers. A permutation of nums is called special if:

For all indexes 0 <= i < n - 1, either nums[i] % nums[i+1] == 0 or nums[i+1] % nums[i] == 0.

Return the total number of special permutations. As the answer could be large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [2,3,6]
Output: 2
Explanation: [3,6,2] and [2,6,3] are the two special permutations of nums.

Example 2:

Input: nums = [1,4,3]
Output: 2
Explanation: [3,1,4] and [4,1,3] are the two special permutations of nums.


Constraints:

2 <= nums.length <= 14
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : BACKTRACKING COMPRESSED INTO BITMASK DP (STATE COMPRESSION)
#
#   Plain backtracking over all n! orders is 14! ~ 8.7e10 — way too slow.
#   But the count of ways to FINISH a permutation only depends on:
#       (1) WHICH numbers are already used  -> a 14-bit mask
#       (2) WHICH number was placed LAST    -> an index
#   The actual order of the used prefix is irrelevant. So memoizing on
#   (mask, last) collapses n! branches into n * 2^n states.
#
#   dp[mask][last] = number of ways to extend a prefix that used exactly the
#                    numbers in `mask` and ended with nums[last].
#
#   Transition : append any unused j whose value divides / is divided by
#   nums[last]. Base case : mask == full -> 1 (one complete permutation).
#
#   NOTE : the answer sums over EVERY possible starting index, since any
#          number may open the permutation.
#
#   NOTE : take the modulo on every accumulation; the raw count of a 14
#          element permutation set can exceed 10^9.
#
#   NOTE : precompute the divisibility adjacency as bitmasks so the inner
#          loop is a single `ok[last] & ~mask` scan instead of n modulos.
#
# time = O(n^2 * 2^n), space = O(n * 2^n)
class Solution(object):
    def specialPerm(self, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)
        full = (1 << n) - 1

        # ok[i] = bitmask of the j that may directly follow / precede i
        ok = [0] * n
        for i in range(n):
            for j in range(n):
                if i != j and (nums[i] % nums[j] == 0 or nums[j] % nums[i] == 0):
                    ok[i] |= 1 << j

        # dp[mask][last]; iterate masks descending so dp[bigger mask] is ready
        dp = [[0] * n for _ in range(1 << n)]
        for last in range(n):
            dp[full][last] = 1

        for mask in range(full - 1, 0, -1):
            free = full ^ mask
            for last in range(n):
                if not (mask >> last) & 1:
                    continue
                cand = ok[last] & free
                total = 0
                while cand:
                    j = (cand & -cand).bit_length() - 1
                    total += dp[mask | (1 << j)][j]
                    cand &= cand - 1
                dp[mask][last] = total % MOD

        res = 0
        for i in range(n):
            res += dp[1 << i][i]
        return res % MOD
