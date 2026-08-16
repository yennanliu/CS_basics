"""

3603. Minimum Cost Path with Alternating Directions II
Medium

You are given two integers m and n representing the number of rows and
columns of a grid, respectively.

The cost to enter cell (i, j) is defined as (i + 1) * (j + 1).

You are also given a 2D integer array waitCost where waitCost[i][j] defines
the cost to wait on that cell.

The path will always begin by entering cell (0, 0) on move 1 and paying the
entrance cost.

At each step, you follow an alternating pattern:

On odd-numbered seconds, you must move right or down to an adjacent cell,
paying its entry cost.
On even-numbered seconds, you must wait in place for exactly one second and
pay waitCost[i][j] during that second.

Return the minimum total cost required to reach (m - 1, n - 1).


Example 1:

Input: m = 1, n = 2, waitCost = [[1,2]]
Output: 3
Explanation:
The optimal path is:
Start at cell (0, 0) at second 1 with entry cost (0 + 1) * (0 + 1) = 1.
Second 1: Move right to cell (0, 1) with entry cost (0 + 1) * (1 + 1) = 2.
Thus, the total cost is 1 + 2 = 3.

Example 2:

Input: m = 2, n = 2, waitCost = [[3,5],[2,4]]
Output: 9
Explanation:
The optimal path is:
Start at cell (0, 0) at second 1 with entry cost (0 + 1) * (0 + 1) = 1.
Second 1: Move down to cell (1, 0) with entry cost (1 + 1) * (0 + 1) = 2.
Second 2: Wait at cell (1, 0), paying waitCost[1][0] = 2.
Second 3: Move right to cell (1, 1) with entry cost (1 + 1) * (1 + 1) = 4.
Thus, the total cost is 1 + 2 + 2 + 4 = 9.

Example 3:

Input: m = 2, n = 3, waitCost = [[6,1,4],[3,2,5]]
Output: 16
Explanation:
The optimal path is:
Start at cell (0, 0) at second 1 with entry cost (0 + 1) * (0 + 1) = 1.
Second 1: Move right to cell (0, 1) with entry cost (0 + 1) * (1 + 1) = 2.
Second 2: Wait at cell (0, 1), paying waitCost[0][1] = 1.
Second 3: Move down to cell (1, 1) with entry cost (1 + 1) * (1 + 1) = 4.
Second 4: Wait at cell (1, 1), paying waitCost[1][1] = 2.
Second 5: Move right to cell (1, 2) with entry cost (1 + 1) * (2 + 1) = 6.
Thus, the total cost is 1 + 2 + 1 + 4 + 2 + 6 = 16.


Constraints:

1 <= m, n <= 10^5
2 <= m * n <= 10^5
waitCost.length == m
waitCost[0].length == n
0 <= waitCost[i][j] <= 10^5

"""

# V0
# IDEA : GRID DP, WITH THE WAIT COST FOLDED ONTO THE CELL YOU LEAVE
#
#   the alternation is not a real constraint here: every odd second is a
#   move right/down and every even second is a forced wait, so the walk is
#   just an ordinary monotone lattice path and the waits are bookkeeping
#   attached to it. what we pay is the entry cost of every cell on the
#   path, plus one wait for every cell that we *leave* — because a wait
#   only happens on the second right after arriving somewhere and right
#   before stepping off again.
#
#   two cells escape the wait: the start, since second 1 is a move (we
#   never idle on (0, 0)), and the destination, since we stop the moment
#   we enter it. so the wait of a cell should be charged at the moment we
#   step *out* of it, and (0, 0) is the single exception where that charge
#   is waived.
#
#   with that framing dp[i][j] = cheapest cost of arriving at (i, j),
#   entry cost of (i, j) included and its own wait not yet paid. a
#   predecessor contributes its dp value plus its wait, and the answer is
#   simply dp[m-1][n-1] — the destination's wait never enters.
#
#   m * n <= 10^5 but a single dimension may be 10^5, so we roll one row.
#
# time = O(m * n), space = O(n)
class Solution(object):
    def minCost(self, m, n, waitCost):
        # the only cell we never pay a wait for on leaving is (0, 0)
        top = list(waitCost[0])
        top[0] = 0

        # row 0: the path can only run rightwards
        prev = [0] * n
        prev[0] = 1
        for j in range(1, n):
            prev[j] = prev[j - 1] + top[j - 1] + (j + 1)

        for i in range(1, m):
            above = top if i == 1 else waitCost[i - 1]
            row = waitCost[i]
            cur = [0] * n
            # column 0 can only be entered from directly above
            cur[0] = prev[0] + above[0] + (i + 1)
            for j in range(1, n):
                from_up = prev[j] + above[j]
                from_left = cur[j - 1] + row[j - 1]
                cur[j] = (from_up if from_up < from_left else from_left) \
                    + (i + 1) * (j + 1)
            prev = cur

        return prev[n - 1]
