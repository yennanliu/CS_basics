"""

2466. Count Ways To Build Good Strings
Medium

Given the integers zero, one, low, and high, we can construct a string by starting with an empty string, and then at each step perform either of the following:

Append the character '0' zero times.
Append the character '1' one times.

This can be performed any number of times.

A good string is a string constructed by the above process having a length between low and high (inclusive).

Return the number of different good strings that can be constructed satisfying these properties. Since the answer can be large, return it modulo 10^9 + 7.


Example 1:

Input: low = 3, high = 3, zero = 1, one = 1
Output: 8
Explanation:
One possible valid good string is "011".
It can be constructed as follows: "" -> "0" -> "01" -> "011".
All binary strings from "000" to "111" are good strings in this example.

Example 2:

Input: low = 2, high = 3, zero = 1, one = 2
Output: 5
Explanation: The good strings are "00", "11", "000", "110", and "011".


Constraints:

1 <= low <= high <= 10^5
1 <= zero, one <= 10^5

"""

# V0
# IDEA : COUNT BY LENGTH — ONLY THE LENGTH MATTERS, NOT THE CONTENT
#
#   each move appends a fixed-size block, so the number of distinct strings
#   of length i is
#       dp[i] = dp[i - zero] + dp[i - one]
#   with dp[0] = 1 (the empty string). two different move SEQUENCES always
#   give two different strings, since the blocks are all-'0' or all-'1'.
#
#   the answer sums dp over the allowed lengths [low, high].
#
"""

DP def
    each move appends a fixed-size block of all-'0' or all-'1', so only the
    LENGTH matters - two different move sequences always give two different
    strings

    dp[i]: number of distinct strings of length exactly i

DP eq

     dp[i] = dp[i - zero] + dp[i - one]


    -> e.g.
         dp[i] = dp[i - zero]   # last move appended `zero` zeros
               + dp[i - one]    # last move appended `one`  ones

     init: dp[0] = 1 (the empty string)
     ans = sum(dp[low..high]) % (10^9 + 7)

"""
# time = O(high), space = O(high)
class Solution(object):
    def countGoodStrings(self, low, high, zero, one):
        MOD = 10 ** 9 + 7
        dp = [0] * (high + 1)
        dp[0] = 1

        for i in range(1, high + 1):
            if i >= zero:
                dp[i] += dp[i - zero]
            if i >= one:
                dp[i] += dp[i - one]
            dp[i] %= MOD

        return sum(dp[low:high + 1]) % MOD
