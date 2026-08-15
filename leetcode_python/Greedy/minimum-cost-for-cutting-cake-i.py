"""

3218. Minimum Cost for Cutting Cake I
Medium

There is an m x n cake that needs to be cut into 1 x 1 pieces.

You are given integers m, n, and two arrays:

horizontalCut of size m - 1, where horizontalCut[i] represents the cost to cut along the horizontal line i.
verticalCut of size n - 1, where verticalCut[j] represents the cost to cut along the vertical line j.

In one operation, you can choose any piece of cake that is not yet a 1 x 1 square and perform one of the following cuts:

Cut along a horizontal line i at a cost of horizontalCut[i].
Cut along a vertical line j at a cost of verticalCut[j].

After the cut, the piece of cake is divided into two distinct pieces.

The cost of a cut depends only on the initial cost of the line and does not change.

Return the minimum total cost to cut the entire cake into 1 x 1 pieces.


Example 1:

Input: m = 3, n = 2, horizontalCut = [1,3], verticalCut = [5]
Output: 13
Explanation:
Perform a cut on the vertical line 0 with cost 5, current total cost is 5.
Perform a cut on the horizontal line 0 on 3 x 1 subgrid with cost 1.
Perform a cut on the horizontal line 0 on 3 x 1 subgrid with cost 1.
Perform a cut on the horizontal line 1 on 2 x 1 subgrid with cost 3.
Perform a cut on the horizontal line 1 on 2 x 1 subgrid with cost 3.
The total cost is 5 + 1 + 1 + 3 + 3 = 13.

Example 2:

Input: m = 2, n = 2, horizontalCut = [7], verticalCut = [4]
Output: 15
Explanation:
Perform a cut on the horizontal line 0 with cost 7.
Perform a cut on the vertical line 0 on 1 x 2 subgrid with cost 4.
Perform a cut on the vertical line 0 on 1 x 2 subgrid with cost 4.
The total cost is 7 + 4 + 4 = 15.


Constraints:

1 <= m, n <= 20
horizontalCut.length == m - 1
verticalCut.length == n - 1
1 <= horizontalCut[i], verticalCut[i] <= 10^3

"""

# V0
# IDEA : EXPENSIVE LINES FIRST — A LINE IS PAID ONCE PER PIECE IT CROSSES
#
#   cutting horizontal line i has to be repeated once for every vertical
#   strip that currently exists, so its total price is
#       horizontalCut[i] * (number of vertical pieces so far)
#   and symmetrically for vertical lines.
#
#   every cut inevitably multiplies the OTHER direction's future costs, so
#   the expensive lines should be used while the multiplier is still small —
#   sort both lists descending and merge, always taking the pricier line next
#   (exchange argument).
#
# time = O(m log m + n log n), space = O(m + n)
class Solution(object):
    def minimumCost(self, m, n, horizontalCut, verticalCut):
        hs = sorted(horizontalCut, reverse=True)
        vs = sorted(verticalCut, reverse=True)

        i = j = 0
        rows, cols = 1, 1               # pieces along each axis so far
        total = 0
        while i < len(hs) or j < len(vs):
            if j >= len(vs) or (i < len(hs) and hs[i] >= vs[j]):
                total += hs[i] * cols   # one cut per existing vertical strip
                rows += 1
                i += 1
            else:
                total += vs[j] * rows
                cols += 1
                j += 1
        return total
