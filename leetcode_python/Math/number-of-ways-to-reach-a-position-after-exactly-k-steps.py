"""

2400. Number of Ways to Reach a Position After Exactly k Steps
Medium

You are given two positive integers startPos and endPos. Initially, you are standing at position startPos on an infinite number line. With one step, you can move either one position to the left, or one position to the right.

Given a positive integer k, return the number of different ways to reach the position endPos starting from startPos, such that you perform exactly k steps. Since the answer may be very large, return it modulo 10^9 + 7.

Two ways are considered different if the order of the steps made is not exactly the same.

Note that the number line includes negative integers.


Example 1:

Input: startPos = 1, endPos = 2, k = 3
Output: 3
Explanation: We can reach position 2 from 1 in exactly 3 steps in three ways:
- 1 -> 2 -> 3 -> 2.
- 1 -> 2 -> 1 -> 2.
- 1 -> 0 -> 1 -> 2.
It can be proven that no other way is possible, so we return 3.

Example 2:

Input: startPos = 2, endPos = 5, k = 10
Output: 0
Explanation: It is impossible to reach position 5 from position 2 in exactly 10 steps.


Constraints:

1 <= startPos, endPos, k <= 1000

"""

# V0
# IDEA : PURE COMBINATORICS — CHOOSE WHICH STEPS GO RIGHT
#
#   let d = abs(endPos - startPos). with r right steps and l left steps :
#       r + l = k   and   r - l = d
#   so r = (k + d) / 2.
#
#   that has a solution only when d <= k AND k - d is EVEN (parity : every
#   step changes the position by 1, so after k steps the displacement shares
#   k's parity). otherwise the answer is 0.
#
#   when it does, the ways are just the placements of the r right steps among
#   the k slots : C(k, r).
#
# time = O(k), space = O(1)
from math import comb


class Solution(object):
    def numberOfWays(self, startPos, endPos, k):
        MOD = 10 ** 9 + 7
        d = abs(endPos - startPos)
        if d > k or (k - d) % 2:
            return 0
        return comb(k, (k + d) // 2) % MOD
