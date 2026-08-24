"""

2572. Count the Number of Square-Free Subsets
Medium

You are given a positive integer 0-indexed array nums.

A subset of the array nums is square-free if the product of its elements is a square-free integer.

A square-free integer is an integer that is divisible by no square number other than 1.

Return the number of square-free non-empty subsets of the array nums. Since the answer may be too large, return it modulo 10^9 + 7.

A non-empty subset of nums is an array that can be obtained by deleting some (possibly none but not all) elements from nums. Two subsets are different if and only if the chosen indices to delete are different.


Example 1:

Input: nums = [3,4,4,5]
Output: 3
Explanation: There are 3 square-free subsets in this example:
- The subset consisting of the 0th element [3]. The product of its elements is 3, which is a square-free integer.
- The subset consisting of the 3rd element [5]. The product of its elements is 5, which is a square-free integer.
- The subset consisting of 0th and 3rd elements [3,5]. The product of its elements is 15, which is a square-free integer.
It can be proven that there are no more than 3 square-free subsets in the given array.

Example 2:

Input: nums = [1]
Output: 1
Explanation: There is 1 square-free subset in this example:
- The subset consisting of the 0th element [1]. The product of its elements is 1, which is a square-free integer.
It can be proven that there is no more than 1 square-free subset in the given array.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 30

"""

# V0
# IDEA : BITMASK DP OVER THE 10 PRIMES <= 30
#
#   nums[i] <= 30, so only the primes [2,3,5,7,11,13,17,19,23,29] can ever
#   appear - 10 of them. A product is square-free iff no prime divides it
#   twice, so we can describe the "prime footprint" of a subset by a 10-bit
#   mask and the rule becomes: masks of chosen elements must be pairwise
#   DISJOINT.
#
#   Two prunes up front:
#     - any value whose own factorisation already repeats a prime (4, 8, 9,
#       12, 16, 18, 20, 24, 25, 27, 28) can NEVER be picked -> skip it. The
#       test x % 4 / x % 9 / x % 25 catches exactly these for x <= 30.
#     - the value 1 has an EMPTY mask, so it conflicts with nothing and can be
#       freely included or excluded. Handle it separately (see below).
#
#   DP : f[mask] = number of ways to pick a subset (of the non-1 values) whose
#   prime footprint is exactly `mask`. Process each distinct value x once; the
#   cnt[x] copies of it are interchangeable-by-index but each index is a
#   distinct subset, so picking "one x" contributes a factor cnt[x] (picking
#   two copies of x is impossible - they would share primes).
#
#       f[mask] += cnt[x] * f[mask ^ bits(x)]     for masks with bits(x) set
#
#   NOTE : the mask loop runs DOWNWARD, exactly like 0/1-knapsack. Going
#          upward would let the same value x be consumed twice in one subset.
#
#   NOTE : the 1s. Each of the cnt[1] ones is independently in or out, giving
#          a 2^cnt[1] multiplier on EVERY subset. Seeding f[0] = 2^cnt[1]
#          spreads that factor over the whole table automatically.
#
#   NOTE : the final -1 removes the single empty subset (no 1s chosen, no
#          other value chosen). Reduce mod AFTER subtracting so the result can
#          never come out negative.
#
"""

DP def
    nums[i] <= 30, so only the 10 primes [2,3,5,7,11,13,17,19,23,29] matter.
    a product is square-free iff no prime divides it twice, i.e. the chosen
    elements' 10-bit "prime footprints" are pairwise DISJOINT.

    f[mask]: number of ways to pick a subset (of the non-1 values) whose

             prime footprint is EXACTLY mask

DP eq

     for each distinct value x with mask bits(x):

        for mask DOWNWARD (0/1-knapsack order):

            f[mask] += cnt[x] * f[mask ^ bits(x)]


    -> e.g. NOTE !!! the mask loop must run DOWNWARD - going upward would
              let the same value x be consumed twice in one subset

     prunes: a value whose own factorisation repeats a prime (4, 8, 9, 12,
             16, 18, 20, 24, 25, 27, 28) can never be picked

     the 1s: each of the cnt[1] ones is freely in or out -> seed
             f[0] = 2^cnt[1] to spread that factor over the whole table

     ans = ( sum(f) - 1 ) % (10^9 + 7)      # -1 drops the empty subset

"""
# time = O(n + 30 * 2^10), space = O(2^10)
class Solution(object):
    def squareFreeSubsets(self, nums):
        MOD = 10 ** 9 + 7
        primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
        k = len(primes)

        cnt = [0] * 31
        for x in nums:
            cnt[x] += 1

        full = 1 << k
        f = [0] * full
        # every 1 is an independent free choice -> 2^cnt[1] baseline
        f[0] = pow(2, cnt[1], MOD)

        for x in range(2, 31):
            if cnt[x] == 0:
                continue
            # x itself is not square-free -> unusable
            if x % 4 == 0 or x % 9 == 0 or x % 25 == 0:
                continue
            bits = 0
            for i in range(k):
                if x % primes[i] == 0:
                    bits |= 1 << i
            # descending: each x may be used at most once per subset
            for mask in range(full - 1, 0, -1):
                if mask & bits == bits:
                    f[mask] = (f[mask] + cnt[x] * f[mask ^ bits]) % MOD

        return (sum(f) - 1) % MOD
