"""

1994. The Number of Good Subsets
Hard

You are given an integer array nums. We call a subset of nums good if its product can be represented as a product of one or more distinct prime numbers.

For example, if nums = [1, 2, 3, 4]:
- [2, 3], [1, 2, 3], and [1, 3] are good subsets with products 6 = 2*3, 6 = 2*3, and 3 = 3 respectively.
- [1, 4] and [4] are not good subsets with products 4 = 2*2 and 4 = 2*2 respectively.

Return the number of different good subsets in nums modulo 10^9 + 7.

A subset of nums is any array that can be obtained by deleting some (possibly none or all) elements from nums. Two subsets are different if and only if the chosen indices to delete are different.


Example 1:

Input: nums = [1,2,3,4]
Output: 6
Explanation: The good subsets are:
- [1,2]: product is 2, which is the product of distinct prime 2.
- [1,2,3]: product is 6, which is the product of distinct primes 2 and 3.
- [1,3]: product is 3, which is the product of distinct prime 3.
- [2]: product is 2, which is the product of distinct prime 2.
- [2,3]: product is 6, which is the product of distinct primes 2 and 3.
- [3]: product is 3, which is the product of distinct prime 3.

Example 2:

Input: nums = [4,2,3,15]
Output: 5
Explanation: The good subsets are:
- [2]: product is 2, which is the product of distinct prime 2.
- [2,3]: product is 6, which is the product of distinct primes 2 and 3.
- [2,15]: product is 30, which is the product of distinct primes 2, 3, and 5.
- [3]: product is 3, which is the product of distinct prime 3.
- [15]: product is 15, which is the product of distinct primes 3 and 5.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 30

"""

# V0
# IDEA : BITMASK KNAPSACK OVER THE 10 PRIMES BELOW 30
#
#   values are <= 30, so only the primes 2,3,5,7,11,13,17,19,23,29 matter.
#   a usable value must be SQUARE-FREE - anything divisible by 4, 9 or 25
#   repeats a prime and can never appear in a good subset.
#
#   encode each square-free value as a 10-bit mask of the primes it uses.
#   f[state] = number of subsets whose combined prime set is exactly `state`.
#   adding value x (mask m, cnt[x] copies, each copy interchangeable) :
#       f[state] += cnt[x] * f[state ^ m]   for every state containing m
#   iterating `state` downward makes each distinct VALUE usable at most once
#   (using x twice would repeat its primes anyway).
#
#   NOTE : 1 contributes no primes but doubles every subset, so seed
#          f[0] = 2^cnt[1]. the answer excludes state 0 (needs >= 1 prime).
#
"""

DP def
    values are <= 30, so only the 10 primes below 30 matter. a usable value
    must be SQUARE-FREE - anything divisible by 4, 9 or 25 repeats a prime and
    can never appear in a good subset.

    f[state]: number of subsets whose combined prime set is EXACTLY `state`

              (state = a 10-bit mask of primes)

DP eq

     for each square-free value x with mask m and cnt[x] copies:

        for state DOWNWARD:

            f[state] += cnt[x] * f[state ^ m]     for states containing m


    -> e.g. iterating `state` downward makes each distinct VALUE usable at
              most once - using x twice would repeat its primes anyway.
              the cnt[x] factor is which of the interchangeable copies is used.

     NOTE !!! 1 contributes NO primes but DOUBLES every subset, so seed
              f[0] = 2^cnt[1]

     ans = sum of f[state] for state != 0    (a good subset needs >= 1 prime)
           mod 10^9 + 7

"""
# time = O(30 * 2^10), space = O(2^10)
from collections import Counter
class Solution(object):
    def numberOfGoodSubsets(self, nums):
        MOD = 10 ** 9 + 7
        primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29]
        k = len(primes)
        full = 1 << k

        cnt = Counter(nums)
        f = [0] * full
        f[0] = pow(2, cnt[1], MOD)      # every "1" can be in or out

        for x in range(2, 31):
            if cnt[x] == 0:
                continue
            if x % 4 == 0 or x % 9 == 0 or x % 25 == 0:
                continue                # not square-free
            mask = 0
            for i, p in enumerate(primes):
                if x % p == 0:
                    mask |= 1 << i
            for state in range(full - 1, 0, -1):
                if state & mask == mask:
                    f[state] = (f[state] + cnt[x] * f[state ^ mask]) % MOD

        return sum(f[1:]) % MOD
