"""

1001. Grid Illumination
Hard

There is a 2D grid of size n x n where each cell of this grid has a lamp that is initially turned off.

You are given a 2D array of lamp positions lamps, where lamps[i] = [rowi, coli] indicates that the lamp at grid[rowi][coli] is turned on. Even if the same lamp is listed more than once, it is turned on.

When a lamp is turned on, it illuminates its cell and all other cells in the same row, column, or diagonal.

You are also given another 2D array queries, where queries[j] = [rowj, colj]. For the jth query, determine whether grid[rowj][colj] is illuminated or not. After answering the jth query, turn off the lamp at grid[rowj][colj] and its 8 adjacent lamps if they exist. A lamp is adjacent if its cell shares either a side or corner with grid[rowj][colj].

Return an array of integers ans, where ans[j] should be 1 if the cell in the jth query was illuminated, or 0 if the lamp was not.


Example 1:

Input: n = 5, lamps = [[0,0],[4,4]], queries = [[1,1],[1,0]]
Output: [1,0]
Explanation: We have the initial grid with all lamps turned off.
The 0th query asks if the lamp at grid[1][1] is illuminated or not. It is illuminated, so set ans[0] = 1.
Then, we turn off all lamps in the 3x3 square around (1,1).
The 1st query asks if the lamp at grid[1][0] is illuminated or not. It is not illuminated, so set ans[1] = 0.

Example 2:

Input: n = 5, lamps = [[0,0],[4,4]], queries = [[1,1],[1,1]]
Output: [1,1]

Example 3:

Input: n = 5, lamps = [[0,0],[0,4]], queries = [[0,4],[0,1],[1,4]]
Output: [1,1,0]


Constraints:

1 <= n <= 10^9
0 <= lamps.length <= 20000
0 <= queries.length <= 20000
lamps[i].length == 2
0 <= rowi, coli < n
queries[j].length == 2
0 <= rowj, colj < n

"""

# V0
# IDEA : HASH TABLE (count lamps per row / col / 2 diagonals)
#
#   for a lamp at (x, y):
#     - row      key = x
#     - col      key = y
#     - diagonal key = x - y   (main "\" diagonal)
#     - anti-diag key = x + y  ("/" diagonal)
#
#   a query cell is illuminated if ANY of its 4 line counters is > 0.
#   NOTE : lamps may repeat -> dedupe them first, otherwise the counters
#          get inflated and never drop back to 0 when we switch a lamp off.
#
# time = O(m + q), m = len(lamps), q = len(queries)
# space = O(m)
from collections import Counter
class Solution(object):
    def gridIllumination(self, n, lamps, queries):
        # dedupe : a cell either has a lamp or it does not
        on = set()
        for x, y in lamps:
            on.add((x, y))

        row, col, diag, anti = Counter(), Counter(), Counter(), Counter()
        for x, y in on:
            row[x] += 1
            col[y] += 1
            diag[x - y] += 1
            anti[x + y] += 1

        res = []
        for x, y in queries:
            if row[x] or col[y] or diag[x - y] or anti[x + y]:
                res.append(1)
            else:
                res.append(0)

            # switch off the lamp itself + its 8 neighbours
            for i in range(x - 1, x + 2):
                for j in range(y - 1, y + 2):
                    if i < 0 or i >= n or j < 0 or j >= n:
                        continue
                    if (i, j) in on:
                        on.remove((i, j))
                        row[i] -= 1
                        col[j] -= 1
                        diag[i - j] -= 1
                        anti[i + j] -= 1

        return res
