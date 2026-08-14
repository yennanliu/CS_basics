"""

1559. Detect Cycles in 2D Grid
Hard

Given a 2D array of characters grid of size m x n, you need to find if there exists any cycle consisting of the same value in grid.

A cycle is a path of length 4 or more in the grid that starts and ends at the same cell. From a given cell, you can move to one of the cells adjacent to it - in one of the four directions (up, down, left, or right), if it has the same value of the current cell.

Also, you cannot move to the cell that you visited in your last move. For example, the cycle (1, 1) -> (1, 2) -> (1, 1) is invalid because from (1, 2) we visited (1, 1) which was the last visited cell.

Return true if any cycle of the same value exists in grid, otherwise, return false.

Example 1:

Input: grid = [["a","a","a","a"],["a","b","b","a"],["a","b","b","a"],["a","a","a","a"]]
Output: true
Explanation: There are two valid cycles shown in different colors in the image below:

Example 2:

Input: grid = [["c","c","c","a"],["c","d","c","c"],["c","c","e","c"],["f","c","c","c"]]
Output: true
Explanation: There is only one valid cycle highlighted in the image below:

Example 3:

Input: grid = [["a","b","b"],["b","z","b"],["b","b","a"]]
Output: false

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 500
grid consists only of lowercase English letters.

"""

# V0
# IDEA : UNION FIND (an edge joining two already-connected cells closes a cycle)
#
#   build the graph whose edges join 4-adjacent cells holding the SAME
#   letter. scan every cell and only look right / down so each edge is
#   offered exactly once.
#   NOTE : in an undirected graph an edge whose two endpoints already
#          share a root creates a cycle -> and in a grid such a cycle is
#          automatically of length >= 4, which is what the problem wants.
#
# time = O(m * n * alpha), space = O(m * n)
class Solution(object):
    def containsCycle(self, grid):
        m, n = len(grid), len(grid[0])
        par = list(range(m * n))

        def find(x):
            while par[x] != x:
                par[x] = par[par[x]]
                x = par[x]
            return x

        for i in range(m):
            for j in range(n):
                for di, dj in ((0, 1), (1, 0)):
                    x, y = i + di, j + dj
                    if x < m and y < n and grid[x][y] == grid[i][j]:
                        ra, rb = find(i * n + j), find(x * n + y)
                        if ra == rb:
                            return True
                        par[ra] = rb
        return False
