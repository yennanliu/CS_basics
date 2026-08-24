"""

2184. Number of Ways to Build Sturdy Brick Wall
Medium

You are given integers height and width which specify the dimensions of a brick wall you are building. You are also given a 0-indexed array of unique integers bricks, where the ith brick has a height of 1 and a width of bricks[i]. You have an infinite supply of each type of brick and bricks may not be rotated.

Each row in the wall must be exactly width units long. For the wall to be sturdy, adjacent rows in the wall should not join bricks at the same location, except at the ends of the wall.

Return the number of ways to build a sturdy wall. Since the answer may be very large, return it modulo 10^9 + 7.

Example 1:

Input: height = 2, width = 3, bricks = [1,2]
Output: 2
Explanation:
The first two walls in the diagram show the only two ways to build a sturdy brick wall.
Note that the third wall in the diagram is not sturdy because adjacent rows join bricks 2 units from the left.

Example 2:

Input: height = 1, width = 1, bricks = [5]
Output: 0
Explanation:
There are no ways to build a sturdy wall because the only type of brick we have is longer than the width of the wall.

Constraints:

1 <= height <= 100
1 <= width <= 10
1 <= bricks.length <= 10
1 <= bricks[i] <= 10
All the values of bricks are unique.

"""

# V0
# IDEA : BITMASK DP OVER ROW "JOINT" PATTERNS + SUBSET-SUM TRANSFORM
#
#   a row is fully described by the set of internal joint offsets
#   1..width-1 -> a bitmask of width-1 bits (width <= 10 -> <= 512 rows).
#   two rows may be stacked iff their masks share no bit (m1 & m2 == 0).
#   dp[m] = # of walls of the current height whose top row is m;
#   the transition needs sum of dp[m2] over all m2 subset of ~m, which a
#   sum-over-subsets (SOS) transform gives in O(2^w * w) instead of
#   O(4^w) pairwise checks.
#   NOTE : build the legal masks by a small DFS over the brick widths.
#
"""

DP def
    a row is fully described by its set of INTERNAL JOINT offsets 1..width-1
    -> a bitmask of width-1 bits (width <= 10, so <= 512 legal rows).
    two rows may be stacked iff their masks share NO bit.

    dp[m]: number of walls of the current height whose TOP row is m

DP eq

     dp_new[m] = sum of dp[m2]   over every m2 with m & m2 == 0

               = sum of dp[m2]   over every m2 SUBSET of ~m


    -> e.g. that "sum over subsets" is a SOS (sum-over-subsets) transform,
              O(2^w * w) instead of the O(4^w) pairwise check

     the legal row masks are enumerated by a small DFS over the brick widths

     init: dp[m] = 1 for every legal row m (height 1)
     ans = sum(dp) after `height` rows, mod 10^9 + 7

"""
# time = O(height * 2^width * width), space = O(2^width)
class Solution(object):
    def buildWall(self, height, width, bricks):
        MOD = 10 ** 9 + 7
        bits = width - 1
        full = (1 << bits) - 1

        # all rows of total length == width -> mask of internal joints
        rows = set()
        stack = [(0, 0)]                    # (filled length, mask so far)
        seen = set(stack)
        while stack:
            length, mask = stack.pop()
            if length == width:
                rows.add(mask)
                continue
            for b in bricks:
                nl = length + b
                if nl > width:
                    continue
                nm = mask if nl == width else mask | (1 << (nl - 1))
                if (nl, nm) not in seen:
                    seen.add((nl, nm))
                    stack.append((nl, nm))
        if not rows:
            return 0

        dp = [0] * (1 << bits)
        for m in rows:
            dp[m] = 1

        for _ in range(height - 1):
            f = dp[:]                        # SOS : f[x] = sum of dp[y], y subset x
            for b in range(bits):
                bit = 1 << b
                for x in range(1 << bits):
                    if x & bit:
                        f[x] = (f[x] + f[x ^ bit]) % MOD
            ndp = [0] * (1 << bits)
            for m in rows:
                ndp[m] = f[full ^ m]
            dp = ndp

        return sum(dp) % MOD
