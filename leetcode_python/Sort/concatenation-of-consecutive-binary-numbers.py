"""

1680. Concatenation of Consecutive Binary Numbers
Medium

Given an integer n, return the decimal value of the binary string formed by concatenating the binary
representations of 1 to n in order, modulo 10^9 + 7.


Example 1:

Input: n = 1
Output: 1
Explanation: "1" in binary corresponds to the decimal value 1.

Example 2:

Input: n = 3
Output: 27
Explanation: In binary, 1, 2, and 3 corresponds to "1", "10", and "11".
After concatenating them, we have "11011", which corresponds to the decimal value 27.

Example 3:

Input: n = 12
Output: 505379714
Explanation: The concatenation results in "1101110010111011110001001101010111100".
The decimal value of that is 118505380540.
After modulo 10^9 + 7, the result is 505379714.


Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : BIT MANIPULATION, INCREMENTAL SHIFT (never build the giant string)
#
#   appending the binary form of i to the accumulated value is just
#     res = res * 2^len(i) + i        where len(i) = number of bits of i
#   so keep a running value modulo 1e9+7 and shift it left by len(i) each step.
#
#   NOTE : len(i) only grows when i is a power of two, so tracking it with a
#          counter avoids recomputing bit lengths (kept explicit here for clarity).
#   NOTE : the modulo is applied every step -- the raw number has ~10^6 bits.
#
# time = O(n), space = O(1)
class Solution(object):
    def concatenatedBinary(self, n):
        MOD = 10 ** 9 + 7
        res = 0
        bits = 0                    # number of bits of the current i
        for i in range(1, n + 1):
            if i & (i - 1) == 0:    # i is a power of two -> one more bit
                bits += 1
            res = ((res << bits) | i) % MOD
        return res
