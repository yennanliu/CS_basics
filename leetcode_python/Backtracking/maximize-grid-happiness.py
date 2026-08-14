"""

1659. Maximize Grid Happiness
Hard

You are given four integers, m, n, introvertsCount, and extrovertsCount. You have an m x n grid,
and there are two types of people: introverts and extroverts. There are introvertsCount introverts
and extrovertsCount extroverts.

You should decide how many people you want to live in the grid and assign each of them one grid cell.
Note that you do not have to have all the people living in the grid.

The happiness of each person is calculated as follows:

- Introverts start with 120 happiness and lose 30 happiness for each neighbor (introvert or extrovert).
- Extroverts start with 40 happiness and gain 20 happiness for each neighbor (introvert or extrovert).

Neighbors live in the directly adjacent cells north, east, south, and west of a person's cell.

The grid happiness is the sum of each person's happiness. Return the maximum possible grid happiness.


Example 1:

Input: m = 2, n = 3, introvertsCount = 1, extrovertsCount = 2
Output: 240
Explanation: Assume the grid is 1-indexed with coordinates (row, column).
We can put the introvert in cell (1,1) and put the extroverts in cells (1,3) and (2,3).
- Introvert at (1,1) happiness: 120 (starting happiness) - (0 * 30) (0 neighbors) = 120
- Extrovert at (1,3) happiness: 40 (starting happiness) + (1 * 20) (1 neighbor) = 60
- Extrovert at (2,3) happiness: 40 (starting happiness) + (1 * 20) (1 neighbor) = 60
The grid happiness is 120 + 60 + 60 = 240.

Example 2:

Input: m = 3, n = 1, introvertsCount = 2, extrovertsCount = 1
Output: 260
Explanation: Place the two introverts in (1,1) and (3,1) and the extrovert at (2,1).
- Introvert at (1,1) happiness: 120 (starting happiness) - (1 * 30) (1 neighbor) = 90
- Extrovert at (2,1) happiness: 40 (starting happiness) + (2 * 20) (2 neighbors) = 80
- Introvert at (3,1) happiness: 120 (starting happiness) - (1 * 30) (1 neighbor) = 90
The grid happiness is 90 + 80 + 90 = 260.

Example 3:

Input: m = 2, n = 2, introvertsCount = 4, extrovertsCount = 0
Output: 240


Constraints:

1 <= m, n <= 5
0 <= introvertsCount, extrovertsCount <= min(m * n, 6)

"""

# V0
# IDEA : PROFILE (BROKEN-PROFILE) DP + MEMO, cell by cell, base-3 mask
#
#   fill cells in row-major order. when we place cell `pos` we only need to know
#   the last n placed cells -> keep them as a base-3 number `mask`
#     digit 0        = cell pos-1 (the LEFT neighbour, if same row)
#     digit n-1      = cell pos-n (the UP   neighbour, if not first row)
#   digit value : 0 = empty, 1 = introvert, 2 = extrovert.
#
#   score the PAIR when the second member is placed, so each edge is counted once:
#     h[a][b] = (a's delta) + (b's delta), 0 if either side is empty
#     h[1][1] = -60, h[1][2] = h[2][1] = -10, h[2][2] = +40
#   plus the base 120 / 40 for the person we just placed.
#
#   NOTE : people are optional -> "leave empty" (cur = 0) is always a candidate.
#   NOTE : shifting the window drops the oldest digit -> mask % 3^(n-1), then *3 + cur.
#
# time = O(m*n * 3^n * I * E * 3), space = O(m*n * 3^n * I * E)
class Solution(object):
    def getMaxGridHappiness(self, m, n, introvertsCount, extrovertsCount):
        total = m * n
        pw = 3 ** (n - 1)
        # pair interaction table
        h = [[0, 0, 0],
             [0, -60, -10],
             [0, -10, 40]]
        base = [0, 120, 40]

        memo = {}

        def dfs(pos, mask, ic, ec):
            if pos == total or (ic == 0 and ec == 0):
                return 0
            key = (pos, mask, ic, ec)
            if key in memo:
                return memo[key]

            r, c = pos // n, pos % n
            up = mask // pw          # digit n-1 -> cell pos - n
            left = mask % 3          # digit 0   -> cell pos - 1

            best = 0
            for cur in (0, 1, 2):
                if cur == 1 and ic == 0:
                    continue
                if cur == 2 and ec == 0:
                    continue
                gain = base[cur]
                if r > 0:
                    gain += h[cur][up]
                if c > 0:
                    gain += h[cur][left]
                nmask = (mask % pw) * 3 + cur
                nic = ic - 1 if cur == 1 else ic
                nec = ec - 1 if cur == 2 else ec
                cand = gain + dfs(pos + 1, nmask, nic, nec)
                if cand > best:
                    best = cand

            memo[key] = best
            return best

        return dfs(0, 0, introvertsCount, extrovertsCount)
