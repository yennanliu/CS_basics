"""

1034. Coloring A Border
Medium

You are given an m x n integer matrix grid, and three integers row, col, and color.
Each value in the grid represents the color of the grid square at that location.

Two squares are called adjacent if they are next to each other in any of the 4 directions.

Two squares belong to the same connected component if they have the same color and
they are adjacent.

The border of a connected component is all the squares in the connected component
that are either adjacent to (at least) a square not in the component, or on the
boundary of the grid (the first or last row or column).

You should color the border of the connected component that contains the square
grid[row][col] with color.

Return the final grid.


Example 1:

Input: grid = [[1,1],[1,2]], row = 0, col = 0, color = 3
Output: [[3,3],[3,2]]

Example 2:

Input: grid = [[1,2,2],[2,3,2]], row = 0, col = 1, color = 3
Output: [[1,3,3],[2,3,3]]

Example 3:

Input: grid = [[1,1,1],[1,1,1],[1,1,1]], row = 1, col = 1, color = 2
Output: [[2,2,2],[2,1,2],[2,2,2]]


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
1 <= grid[i][j], color <= 1000
0 <= row < m
0 <= col < n

"""

# V0
# IDEA : BFS (collect whole component first, then repaint only its border)
#
#  NOTE : we must NOT repaint while exploring, otherwise the new color
#         breaks the "same color" test of the traversal.
#         -> pass 1 : collect component cells
#         -> pass 2 : a cell is a border cell if some 4-dir neighbor is
#                     out of grid, or is NOT in the component
#
# time = O(m * n)
# space = O(m * n)
from collections import deque
class Solution(object):
    def colorBorder(self, grid, row, col, color):
        m, n = len(grid), len(grid[0])
        origin = grid[row][col]

        # pass 1 : BFS over the connected component
        component = set([(row, col)])
        q = deque([(row, col)])
        while q:
            r, c = q.popleft()
            for dr, dc in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n:
                    if grid[nr][nc] == origin and (nr, nc) not in component:
                        component.add((nr, nc))
                        q.append((nr, nc))

        # pass 2 : find border cells
        border = []
        for r, c in component:
            for dr, dc in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                nr, nc = r + dr, c + dc
                if not (0 <= nr < m and 0 <= nc < n) or (nr, nc) not in component:
                    border.append((r, c))
                    break

        # pass 3 : repaint
        for r, c in border:
            grid[r][c] = color

        return grid


# V1
# IDEA : DFS (recursive, same 3-pass logic)
# time = O(m * n)
# space = O(m * n)
class Solution2(object):
    def colorBorder(self, grid, row, col, color):
        m, n = len(grid), len(grid[0])
        origin = grid[row][col]
        component = set()

        def dfs(r, c):
            component.add((r, c))
            for dr, dc in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n:
                    if grid[nr][nc] == origin and (nr, nc) not in component:
                        dfs(nr, nc)

        dfs(row, col)

        for r, c in component:
            is_border = False
            for dr, dc in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                nr, nc = r + dr, c + dc
                if not (0 <= nr < m and 0 <= nc < n) or (nr, nc) not in component:
                    is_border = True
                    break
            if is_border:
                grid[r][c] = color
        return grid
