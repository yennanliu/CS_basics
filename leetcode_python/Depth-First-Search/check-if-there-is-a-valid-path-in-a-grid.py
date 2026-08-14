"""

1391. Check if There is a Valid Path in a Grid
Medium

You are given an m x n grid. Each cell of grid represents a street. The street of grid[i][j] can be:

1 which means a street connecting the left cell and the right cell.
2 which means a street connecting the upper cell and the lower cell.
3 which means a street connecting the left cell and the lower cell.
4 which means a street connecting the right cell and the lower cell.
5 which means a street connecting the left cell and the upper cell.
6 which means a street connecting the right cell and the upper cell.

You will initially start at the street of the upper-left cell (0, 0). A valid path in the grid is a path that starts from the upper left cell (0, 0) and ends at the bottom-right cell (m - 1, n - 1). The path should only follow the streets.

Notice that you are not allowed to change any street.

Return true if there is a valid path in the grid or false otherwise.


Example 1:

Input: grid = [[2,4,3],[6,5,2]]
Output: true
Explanation: As shown you can start at cell (0, 0) and visit all the cells of the grid to reach (m - 1, n - 1).

Example 2:

Input: grid = [[1,2,1],[1,2,1]]
Output: false
Explanation: As shown you the street at cell (0, 0) is not connected with any street of any other cell and you will get stuck at cell (0, 0)

Example 3:

Input: grid = [[1,1,2]]
Output: false
Explanation: You will get stuck at cell (0, 1) and you cannot reach cell (0, 2).


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
1 <= grid[i][j] <= 6

"""

# V0
# IDEA : UNION FIND (a street type = a fixed pair of open sides)
#
#   encode each street as the set of directions it opens onto:
#     1 -> L,R   2 -> U,D   3 -> L,D   4 -> R,D   5 -> L,U   6 -> R,U
#
#   two adjacent cells are really connected only if BOTH open toward
#   each other, so we union (i, j) with a neighbour only when the
#   neighbour's street contains the opposite direction.
#   NOTE : checking only one side is the classic bug -> "1" next to "2"
#          would look joined, but neither actually opens to the other.
#   answer = cell 0 and cell m*n-1 end up in the same component.
#
# time = O(m * n * alpha(m * n)), space = O(m * n)
class Solution(object):
    def hasValidPath(self, grid):
        m, n = len(grid), len(grid[0])

        # open sides per street type (1-indexed)
        opens = {
            1: set("LR"),
            2: set("UD"),
            3: set("LD"),
            4: set("RD"),
            5: set("LU"),
            6: set("RU"),
        }
        # direction -> (di, dj, opposite direction)
        moves = {
            "L": (0, -1, "R"),
            "R": (0, 1, "L"),
            "U": (-1, 0, "D"),
            "D": (1, 0, "U"),
        }

        parent = list(range(m * n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb

        for i in range(m):
            for j in range(n):
                for d in opens[grid[i][j]]:
                    di, dj, back = moves[d]
                    x, y = i + di, j + dj
                    if x < 0 or x >= m or y < 0 or y >= n:
                        continue
                    # both sides must open toward each other
                    if back in opens[grid[x][y]]:
                        union(i * n + j, x * n + y)

        return find(0) == find(m * n - 1)
