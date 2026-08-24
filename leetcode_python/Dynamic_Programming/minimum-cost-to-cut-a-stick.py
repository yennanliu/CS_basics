"""

1547. Minimum Cost to Cut a Stick
Hard

Given a wooden stick of length n units. The stick is labelled from 0 to n.

Given an integer array cuts where cuts[i] denotes a position you should perform a cut at.

You should perform the cuts in order, you can change the order of the cuts as you wish.

The cost of one cut is the length of the stick to be cut, the total cost is the sum of costs of all cuts. When you cut a stick, it will be split into two smaller sticks (i.e. the sum of their lengths is the length of the stick before the cut). Please refer to the first example for a better explanation.

Return the minimum total cost of the cuts.


Example 1:

Input: n = 7, cuts = [1,3,4,5]
Output: 16
Explanation: Using cuts order = [1, 3, 4, 5] as in the input leads to the following scenario:
The first cut is done to a rod of length 7 so the cost is 7. The second cut is done to a rod of length 6 (i.e. the second part of the first cut), the third is done to a rod of length 4 and the last cut is to a rod of length 3. The total cost is 7 + 6 + 4 + 3 = 20.
Rearranging the cuts to be [3, 5, 1, 4] for example will lead to a scenario with total cost = 16 (as shown in the example photo 7 + 4 + 3 + 2 = 16).

Example 2:

Input: n = 9, cuts = [5,6,1,4,2]
Output: 22
Explanation: If you try the given cuts ordering the cost will be 25.
There are much ordering with total cost <= 25, for example, the order [4, 6, 5, 2, 1] has total cost = 22 which is the minimum possible.


Constraints:

2 <= n <= 10^6
1 <= cuts.length <= min(n - 1, 100)
1 <= cuts[i] <= n - 1
All the integers in cuts array are distinct.

"""

# V0
# IDEA : INTERVAL DP (same shape as matrix-chain / burst balloons)
#
#   add the two stick ends 0 and n to `cuts` and sort. now every piece of
#   the final stick is exactly some interval [cuts[i], cuts[j]].
#
#   f[i][j] = min cost to make ALL the cuts strictly inside (cuts[i], cuts[j])
#
#     f[i][j] = 0                                        if j <= i + 1
#     f[i][j] = min over i < k < j of
#                 f[i][k] + f[k][j] + (cuts[j] - cuts[i])
#
#   the (cuts[j] - cuts[i]) term is the cost of whichever cut we choose to
#   make FIRST in this interval — at that moment the piece is still whole,
#   so its length is the full interval width regardless of which k we pick.
#
#   iterate by increasing interval width so sub-intervals are ready.
#   NOTE : n can be 10^6 but only m = len(cuts) + 2 <= 102 positions matter,
#          so the DP is over m, not n.
#
"""

DP def
    add the stick ends 0 and n to `cuts` and SORT - now every piece of the
    final stick is exactly some interval [pos[i], pos[j]]

    f[i][j]: MIN cost to make ALL the cuts strictly inside (pos[i], pos[j])

DP eq

     f[i][j] = 0                                   if j <= i + 1

     f[i][j] = min over i < k < j of

                  f[i][k] + f[k][j] + (pos[j] - pos[i])


    -> e.g. the (pos[j] - pos[i]) term is the cost of whichever cut is made
              FIRST in this interval - at that moment the piece is still
              whole, so its length is the full interval width no matter
              which k is chosen

     iterate by increasing interval WIDTH so sub-intervals are ready

     NOTE !!! n can be 10^6 but only m = len(cuts) + 2 <= 102 positions
              matter, so the DP is over m, not n

     ans = f[0][m-1]

"""
# time = O(m^3), space = O(m^2)
class Solution(object):
    def minCost(self, n, cuts):
        pos = sorted(cuts + [0, n])
        m = len(pos)
        f = [[0] * m for _ in range(m)]

        for width in range(2, m):
            for i in range(m - width):
                j = i + width
                best = float('inf')
                for k in range(i + 1, j):
                    cand = f[i][k] + f[k][j]
                    if cand < best:
                        best = cand
                f[i][j] = best + pos[j] - pos[i]
        return f[0][m - 1]
