"""

3539. Find Sum of Array Product of Magical Sequences
Hard

You are given two integers, m and k, and an integer array nums.

A sequence of integers seq is called magical if:

seq has a size of m.

0 <= seq[i] < nums.length

The binary representation of 2^seq[0] + 2^seq[1] + ... + 2^seq[m - 1] has k set
bits.

The array product of this sequence is defined as prod(seq) = (nums[seq[0]] *
nums[seq[1]] * ... * nums[seq[m - 1]]).

Return the sum of the array products for all valid magical sequences.

Since the answer may be large, return it modulo 10^9 + 7.

A set bit refers to a bit in the binary representation of a number that has a
value of 1.

Example 1:

Input: m = 5, k = 5, nums = [1,10,100,10000,1000000]

Output: 991600007

Explanation:

All permutations of [0, 1, 2, 3, 4] are magical sequences, each with an array
product of 10^13.

Example 2:

Input: m = 2, k = 2, nums = [5,4,3,2,1]

Output: 170

Explanation:

The magical sequences are [0, 1], [0, 2], [0, 3], [0, 4], [1, 0], [1, 2], [1,
3], [1, 4], [2, 0], [2, 1], [2, 3], [2, 4], [3, 0], [3, 1], [3, 2], [3, 4], [4,
0], [4, 1], [4, 2], and [4, 3].

Example 3:

Input: m = 1, k = 1, nums = [28]

Output: 28

Explanation:

The only magical sequence is [0].

Constraints:

1 <= k <= m <= 30

1 <= nums.length <= 50

1 <= nums[i] <= 10^8

"""

# V0
# IDEA : COUNT BY MULTIPLICITY PER INDEX, AND ADD 2^index WITH A RUNNING CARRY
#
#   a sequence only matters through how many times each index is used: if index
#   i appears c_i times it contributes nums[i]^c_i to the product, and the
#   number of orderings is the multinomial m! / prod(c_i!).  building the
#   choices index by index, "choose c_i of the m - used remaining slots" gives
#   that multinomial for free.
#
#   the awkward part is the set-bit condition, because 2^i is added c_i times
#   and the additions carry.  but if the indices are processed in increasing
#   order, bit i is finalised the moment index i is handled: its value is
#   (c_i + carry) & 1 and the rest, (c_i + carry) >> 1, moves on as the carry
#   into bit i + 1.  so a single small carry is all the state that is needed,
#   and it never exceeds m / 2 because the value seen so far is below m * 2^i.
#
#   after the last index the leftover carry still spells out high bits, so the
#   final tally adds popcount(carry) to the bits counted along the way.
#
# time = O(n * m^2 * k), space = O(m^2 * k)
class Solution(object):
    def magicalSum(self, m, k, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)
        comb = [[0] * (m + 1) for _ in range(m + 1)]
        for i in range(m + 1):
            comb[i][0] = 1
            for j in range(1, i + 1):
                comb[i][j] = (comb[i - 1][j - 1] + comb[i - 1][j]) % MOD

        # state -> (used, carry, bits) : weighted count so far
        dp = {(0, 0, 0): 1}
        for x in nums:
            x %= MOD
            pw = [1] * (m + 1)
            for c in range(1, m + 1):
                pw[c] = pw[c - 1] * x % MOD
            nxt = {}
            for (used, carry, bits), val in dp.items():
                room = m - used
                cb = comb[room]
                for c in range(room + 1):
                    tot = c + carry
                    nb = bits + (tot & 1)
                    if nb > k:
                        continue
                    key = (used + c, tot >> 1, nb)
                    add = val * cb[c] % MOD * pw[c] % MOD
                    nxt[key] = (nxt.get(key, 0) + add) % MOD
            dp = nxt

        ans = 0
        for (used, carry, bits), val in dp.items():
            if used == m and bits + bin(carry).count("1") == k:
                ans = (ans + val) % MOD
        return ans
