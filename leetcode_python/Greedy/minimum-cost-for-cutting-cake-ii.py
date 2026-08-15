"""

3219. Minimum Cost for Cutting Cake II
Hard

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

1 <= m, n <= 10^5
horizontalCut.length == m - 1
verticalCut.length == n - 1
1 <= horizontalCut[i], verticalCut[i] <= 10^3

"""

# V0
# IDEA : IDENTICAL GREEDY TO LC 3218 — IT WAS ALREADY O(n log n)
#
#   a horizontal line costs its price once per vertical strip that exists
#   when it is used, so using the expensive lines while the perpendicular
#   multiplier is still small is optimal.
#
#   the only change from the easy version is the scale : m and n reach 10^5,
#   so the merge has to stay linear after the sort — which it already is.
#
#   the totals exceed 32 bits (10^5 lines times 10^3 cost times a 10^5
#   multiplier), which python integers handle natively.
#
# time = O(m log m + n log n), space = O(m + n)
class Solution(object):
    def minimumCost(self, m, n, horizontalCut, verticalCut):
        hs = sorted(horizontalCut, reverse=True)
        vs = sorted(verticalCut, reverse=True)

        i = j = 0
        rows, cols = 1, 1
        total = 0
        while i < len(hs) or j < len(vs):
            if j >= len(vs) or (i < len(hs) and hs[i] >= vs[j]):
                total += hs[i] * cols
                rows += 1
                i += 1
            else:
                total += vs[j] * rows
                cols += 1
                j += 1
        return total
