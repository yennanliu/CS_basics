"""

2318. Number of Distinct Roll Sequences
Hard

You are given an integer n. You roll a fair 6-sided dice n times. Determine the total number of distinct sequences of rolls possible such that the following conditions are satisfied:

The greatest common divisor of any adjacent values in the sequence is equal to 1.
There is at least a gap of 2 rolls between equal valued rolls. More formally, if the value of the ith roll is equal to the value of the jth roll, then abs(i - j) > 2.

Return the total number of distinct sequences possible. Since the answer may be very large, return it modulo 10^9 + 7.

Two sequences are considered distinct if at least one element is different.


Example 1:

Input: n = 4
Output: 184
Explanation: Some of the possible sequences are (1, 2, 3, 4), (6, 1, 2, 3), (1, 2, 3, 1), etc.
Some invalid sequences are (1, 2, 1, 3), (1, 2, 3, 6).
(1, 2, 1, 3) is invalid since the first and third roll have an equal value and abs(1 - 3) = 2 (i and j are 1-indexed).
(1, 2, 3, 6) is invalid since the greatest common divisor of 3 and 6 = 3.
There are a total of 184 distinct sequences possible, so we return 184.

Example 2:

Input: n = 2
Output: 22
Explanation: Some of the possible sequences are (1, 2), (2, 1), (3, 2).
Some invalid sequences are (3, 6), (2, 4) since the greatest common divisor is not equal to 1.
There are a total of 22 distinct sequences possible, so we return 22.


Constraints:

1 <= n <= 10^4

"""

# V0
# IDEA : DP ON THE LAST TWO ROLLS (only a 6x6 state is needed)
#
#   the constraints reach back exactly 2 positions, so the state is the
#   pair (previous roll, current roll):
#       dp[a][b] = number of valid sequences whose last two rolls are a, b
#
#   base (length 2) : dp[a][b] = 1 for every ordered pair with a != b and
#                     gcd(a+1, b+1) == 1
#   step            : nxt[b][c] = sum over a of dp[a][b]
#                     for every c with gcd(b+1, c+1) == 1, c != b, c != a
#
#   NOTE : the "gap of 2" rule means c must differ from BOTH b and a -
#          skipping the c != a check is the usual bug here.
#          n == 1 is a special case (answer 6).
#
"""

DP def
    the constraints reach back exactly TWO positions, so the state is the pair
    of last two rolls

    dp[a][b]: number of valid sequences whose last two rolls are a, b

              (only 6 x 6 = 36 states)

DP eq

     base (length 2): dp[a][b] = 1 for every ordered pair with

                      a != b and gcd(a+1, b+1) == 1

     step: nxt[b][c] += dp[a][b]

           for every c with gcd(b+1, c+1) == 1, c != b, AND c != a


    -> e.g. NOTE !!! the "equal rolls must be at least 2 apart" rule is what
              forces c != a - skipping that check is the usual bug here

     n == 1 is a special case (answer 6)

     ans = sum of all dp[a][b] after n - 2 steps, mod 10^9 + 7

"""
# time = O(n * 6^3), space = O(36)
from math import gcd
class Solution(object):
    def distinctSequences(self, n):
        MOD = 10 ** 9 + 7
        if n == 1:
            return 6

        # ok[a][b] : a can be immediately followed by b
        ok = [[gcd(a + 1, b + 1) == 1 and a != b for b in range(6)]
              for a in range(6)]

        dp = [[1 if ok[a][b] else 0 for b in range(6)] for a in range(6)]
        for _ in range(n - 2):
            nxt = [[0] * 6 for _ in range(6)]
            for a in range(6):
                for b in range(6):
                    cur = dp[a][b]
                    if not cur:
                        continue
                    for c in range(6):
                        if c != a and ok[b][c]:
                            nxt[b][c] = (nxt[b][c] + cur) % MOD
            dp = nxt

        return sum(sum(row) for row in dp) % MOD
