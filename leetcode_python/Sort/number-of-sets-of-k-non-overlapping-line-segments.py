"""

1621. Number of Sets of K Non-Overlapping Line Segments
Medium

Given n points on a 1-D plane, where the ith point (from 0 to n-1) is at x = i, find the number of ways we can draw exactly k non-overlapping line segments such that each segment covers two or more points. The endpoints of each segment must have integral coordinates. The k line segments do not have to cover all n points, and they are allowed to share endpoints.

Return the number of ways we can draw k non-overlapping line segments. Since this number can be huge, return it modulo 10^9 + 7.


Example 1:

Input: n = 4, k = 2
Output: 5
Explanation: The two line segments are shown in red and blue.
The image above shows the 5 different ways {(0,2),(2,3)}, {(0,1),(1,3)}, {(0,1),(2,3)}, {(1,2),(2,3)}, {(0,1),(1,2)}.

Example 2:

Input: n = 3, k = 1
Output: 3
Explanation: The 3 ways are {(0,1)}, {(0,2)}, {(1,2)}.

Example 3:

Input: n = 30, k = 7
Output: 796297179
Explanation: The total number of possible ways to draw 7 line segments is 3796297200. Taking this number modulo 10^9 + 7 gives us 796297179.


Constraints:

2 <= n <= 1000
1 <= k <= n-1

"""

# V0
# IDEA : COMBINATORICS -- the answer is C(n + k - 1, 2k)
#
#   a configuration is fixed by the 2k endpoints x1 <= x2 <= ... <= x2k
#   in [0, n-1], with the only constraints being x1 < x2, x3 < x4, ...
#   (a segment covers >= 2 points) while CONSECUTIVE segments may share an
#   endpoint (x2 <= x3 is allowed).
#
#   the standard "make it strict" substitution yi = xi + (i-1) turns this
#   weakly-increasing-with-k-strict-gaps sequence into a strictly
#   increasing choice of 2k values from n + k - 1 slots, i.e.
#     C(n + k - 1, 2k)
#
#   sanity check : n=4,k=2 -> C(5,4) = 5 ; n=3,k=1 -> C(3,2) = 3. matches.
#
# time = O(k), space = O(1)
class Solution(object):
    def numberOfSets(self, n, k):
        MOD = 10 ** 9 + 7
        top = n + k - 1
        r = 2 * k
        if r > top:
            return 0
        # exact integer binomial, then reduce (python ints are unbounded)
        num = 1
        for i in range(r):
            num = num * (top - i) // (i + 1)
        return num % MOD
