"""

3565. Sequential Grid Path Cover
Medium

You are given a 2D array grid of size m x n, and an integer k. There are k
cells in grid containing the values from 1 to k exactly once, and the rest
of the cells have a value 0.

You can start at any cell, and move from a cell to its neighbors (up, down,
left, or right). You must find a path in grid which:

Visits each cell in grid exactly once.
Visits the cells with values from 1 to k in order.

Return a 2D array result of size (m * n) x 2, where result[i] = [xi, yi]
represents the ith cell visited in the path. If there are multiple such
paths, you may return any one of them.

If no such path exists, return an empty array.


Example 1:

Input: grid = [[0,0,0],[0,1,2]], k = 2
Output: [[0,0],[1,0],[1,1],[1,2],[0,2],[0,1]]
Explanation:
The path starts at [0,0], walks down and across the bottom row (picking up
1 then 2 in order), then comes back along the top row.

Example 2:

Input: grid = [[1,0,4],[3,0,2]], k = 4
Output: []
Explanation:
There is no possible path that satisfies the conditions.


Constraints:

1 <= m == grid.length <= 5
1 <= n == grid[i].length <= 5
1 <= k <= m * n
0 <= grid[i][j] <= k
grid contains all integers between 1 and k exactly once.

"""

# V0
# IDEA : BACKTRACKING WITH THE "NEXT WANTED LABEL" AS PART OF THE STATE
#
#   the path is a hamiltonian path in the grid graph, so brute force search
#   is unavoidable; the ordering constraint is what makes it cheap. carry
#   the next label we still owe, v (starting at 1). a cell may be stepped on
#   only if it is blank or carries exactly v — a cell holding a label larger
#   than v would jump the queue, and one holding a smaller label was already
#   consumed. that single test enforces "1..k in order" locally, so no
#   global check at the end is needed.
#
#   it also prunes the search hard: the moment the walk drifts away from the
#   next label, every labelled neighbour except one is closed off.
#
#   the start cell is likewise restricted to blank or 1, and since the walk
#   ends only when all m*n cells are on the path, covering every cell exactly
#   once is guaranteed by construction.
#
#   visited is kept as a bitmask over the <= 25 cells, so undoing a step is
#   a single xor.
#
# time = O(m * n * 3^(m * n)) worst case, space = O(m * n)
class Solution(object):
    def findPath(self, grid, k):
        m, n = len(grid), len(grid[0])
        total = m * n
        path = []
        seen = [0]

        def dfs(i, j, v):
            path.append([i, j])
            if len(path) == total:
                return True

            seen[0] |= 1 << (i * n + j)
            if grid[i][j] == v:
                v += 1

            for a, b in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                x, y = i + a, j + b
                if 0 <= x < m and 0 <= y < n:
                    if seen[0] & (1 << (x * n + y)):
                        continue
                    if grid[x][y] == 0 or grid[x][y] == v:
                        if dfs(x, y, v):
                            return True

            seen[0] ^= 1 << (i * n + j)
            path.pop()
            return False

        for i in range(m):
            for j in range(n):
                if grid[i][j] == 0 or grid[i][j] == 1:
                    if dfs(i, j, 1):
                        return path
                    del path[:]
                    seen[0] = 0
        return []
