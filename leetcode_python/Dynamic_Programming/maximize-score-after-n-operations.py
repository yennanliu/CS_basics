"""

1799. Maximize Score After N Operations
Hard

You are given nums, an array of positive integers of size 2 * n. You must perform n operations on this array.

In the i^th operation (1-indexed), you will:

Choose two elements, x and y.
Receive a score of i * gcd(x, y).
Remove x and y from nums.

Return the maximum score you can receive after performing n operations.

The function gcd(x, y) is the greatest common divisor of x and y.

Example 1:

Input: nums = [1,2]
Output: 1
Explanation: The optimal choice of operations is:
(1 * gcd(1, 2)) = 1

Example 2:

Input: nums = [3,4,6,8]
Output: 11
Explanation: The optimal choice of operations is:
(1 * gcd(3, 6)) + (2 * gcd(4, 8)) = 3 + 8 = 11

Example 3:

Input: nums = [1,2,3,4,5,6]
Output: 14
Explanation: The optimal choice of operations is:
(1 * gcd(1, 5)) + (2 * gcd(2, 4)) + (3 * gcd(3, 6)) = 1 + 4 + 9 = 14

Constraints:

1 <= n <= 7
nums.length == 2 * n
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : BITMASK DP OVER THE SET OF ALREADY-USED INDICES (2n <= 14)
#
#   f[mask] = best score once exactly the indices in `mask` have been consumed.
#   only masks with an even popcount are reachable, and popcount(mask) // 2
#   operations have happened, so the NEXT pair chosen inside `mask` is worth
#   (popcount(mask) // 2) * gcd(...).
#     f[mask] = max over pairs (i, j) both in mask of
#               f[mask ^ (1<<i) ^ (1<<j)] + (popcount(mask) // 2) * gcd[i][j]
#   NOTE : pre-computing gcd for all O((2n)^2) pairs keeps the DP loop cheap.
#
"""

DP def
    (BITMASK DP over the set of already-used indices, 2n <= 14)

    f[mask]: best score once EXACTLY the indices in `mask` have been consumed

             -> only EVEN popcounts are reachable, and
                popcount(mask) // 2 operations have happened

DP eq

     f[mask] = max over pairs (i, j) both in mask of

                  f[mask ^ (1<<i) ^ (1<<j)] + (popcount(mask) // 2) * gcd[i][j]


    -> e.g. the operation index is DERIVED from popcount, so it needs no
              extra dimension - the pair removed last is the
              (popcount // 2)-th operation

     pre-compute gcd for all O((2n)^2) pairs to keep the loop cheap

     init: f[0] = 0
     ans = f[(1 << 2n) - 1]

"""
# time = O(2^(2n) * (2n)^2), space = O(2^(2n))
class Solution(object):
    def maxScore(self, nums):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        m = len(nums)
        gs = [[0] * m for _ in range(m)]
        for i in range(m):
            for j in range(i + 1, m):
                gs[i][j] = gcd(nums[i], nums[j])

        full = 1 << m
        f = [0] * full
        for mask in range(full):
            bits = bin(mask).count("1")
            if bits % 2:
                continue
            op = bits // 2
            for i in range(m):
                if not (mask >> i) & 1:
                    continue
                for j in range(i + 1, m):
                    if not (mask >> j) & 1:
                        continue
                    cand = f[mask ^ (1 << i) ^ (1 << j)] + op * gs[i][j]
                    if cand > f[mask]:
                        f[mask] = cand
        return f[full - 1]
