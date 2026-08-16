"""

3596. Minimum Cost Path with Alternating Directions I
Medium

You are given two integers m and n representing the number of rows and
columns of a grid, respectively.

The cost to enter cell (i, j) is defined as (i + 1) * (j + 1).

The path will always begin by entering cell (0, 0) on move 1 and paying the
entrance cost.

At each step, you move to an adjacent cell, following an alternating
pattern:

On odd-numbered moves, you must move either right or down.
On even-numbered moves, you must move either left or up.

Return the minimum total cost required to reach (m - 1, n - 1). If it is
impossible, return -1.


Example 1:

Input: m = 1, n = 1
Output: 1
Explanation:
You start at cell (0, 0).
The cost to enter (0, 0) is (0 + 1) * (0 + 1) = 1.
Since you're at the destination, the total cost is 1.

Example 2:

Input: m = 2, n = 1
Output: 3
Explanation:
You start at cell (0, 0) with cost (0 + 1) * (0 + 1) = 1.
Move 1 (odd): You can move down to (1, 0) with cost (1 + 1) * (0 + 1) = 2.
Thus, the total cost is 1 + 2 = 3.


Constraints:

1 <= m, n <= 10^6

"""

# V0
# IDEA : PARITY INVARIANT ON (row + col) — ONLY THREE GRIDS ARE SOLVABLE
#
#   look at what a *pair* of consecutive moves does to the quantity
#   (row + col). the odd move adds 1 to it (right or down), the even move
#   subtracts 1 from it (left or up). so every completed odd/even pair
#   leaves row + col exactly where it started.
#
#   we begin at (0, 0) with row + col = 0. therefore after an even number
#   of moves row + col is still 0, which pins us back at (0, 0) since both
#   coordinates are non-negative; after an odd number of moves row + col
#   is exactly 1, i.e. we are at (0, 1) or (1, 0). no other cell in the
#   grid is ever reachable, no matter how long we wander.
#
#   so the destination (m - 1, n - 1) is reachable only when
#   (m - 1) + (n - 1) is 0 or 1 — the 1x1, 2x1 and 1x2 grids. in the 1x1
#   case we pay just the entrance cost 1; in the other two we pay 1 for
#   (0, 0) plus 2 for the single neighbour, and there is nothing to
#   optimise because there is only one path.
#
# time = O(1), space = O(1)
class Solution(object):
    def minCost(self, m, n):
        if m == 1 and n == 1:
            return 1
        if (m == 2 and n == 1) or (m == 1 and n == 2):
            return 3
        return -1
