"""
In a 2 dimensional array grid, each value grid[i][j] represents the height of a building located there. We are allowed to increase the height of any number of buildings, by any amount (the amounts can be different for different buildings). Height 0 is considered to be a building as well.

At the end, the “skyline” when viewed from all four directions of the grid, i.e. top, bottom, left, and right, must be the same as the skyline of the original grid. A city’s skyline is the outer contour of the rectangles formed by all the buildings when viewed from a distance. See the following example.

What is the maximum total sum that the height of the buildings can be increased?

Example:

Input: grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]]
Output: 35
Explanation: 
The grid is:
[ [3, 0, 8, 4], 
  [2, 4, 5, 7],
  [9, 2, 6, 3],
  [0, 3, 1, 0] ]

The skyline viewed from top or bottom is: [9, 4, 8, 7]
The skyline viewed from left or right is: [8, 7, 9, 3]

The grid after increasing the height of buildings without affecting skylines is:

gridNew = [ [8, 4, 8, 7],
            [7, 4, 7, 7],
            [9, 4, 8, 7],
            [3, 3, 3, 3] ]

Notes:

1 < grid.length = grid[0].length <= 50.
All heights grid[i][j] are in the range [0, 100].
All buildings in grid[i][j] occupy the entire grid cell: that is, they are a 1 x 1 x grid[i][j] rectangular prism.

"""

# V0 

# V1
# http://bookshadow.com/weblog/2018/03/27/leetcode-max-increase-to-keep-city-skyline/
# IDEA : ngrid[x][y] = min(mcs[y], mrs[x])
# NOTE :
#	__|_9__4__8__7__
#	8 | 8, 4, 8, 7
#	7 | 7, 4, 7, 7
#	9 | 9, 4, 8, 7
#	3 | 3, 3, 3, 3
class Solution(object):
    def maxIncreaseKeepingSkyline(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        r, c = len(grid), len(grid[0])
        mcs = map(max, *grid)
        mrs = map(max, grid)
        ans = 0
        for x in range(r):
            for y in range(c):
                ans += min(mcs[y], mrs[x]) - grid[x][y]
        return ans

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79820383
class Solution(object):
    def maxIncreaseKeepingSkyline(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        gridNew = [[0] * len(grid[0]) for _ in range(len(grid))] 
        top = [max(grid[rows][cols] for rows in range(len(grid))) for cols in range(len(grid[0]))]
        left = [max(grid[rows][cols] for cols in range(len(grid[0]))) for rows in range(len(grid))]
        for row, row_max in enumerate(left):
            for col, col_max in enumerate(top):
                gridNew[row][col] = min(row_max, col_max)
        return sum(gridNew[row][col] - grid[row][col] for row in range(len(left)) for col in range(len(top)))

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79820383
class Solution(object):
    def maxIncreaseKeepingSkyline(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]: return 0
        M, N = len(grid), len(grid[0])
        rows, cols = [0] * M, [0] * N
        for i in range(M):
            rows[i] = max(grid[i][j] for j in range(N))
        for j in range(N):
            cols[j] = max(grid[i][j] for i in range(M))
        res = 0
        for i in range(M):
            for j in range(N):
                res += min(rows[i], cols[j]) - grid[i][j]
        return res

# V2 
# Time:  O(n^2)
# Space: O(n)
import itertools
class Solution(object):
    def maxIncreaseKeepingSkyline(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        row_maxes = [max(row) for row in grid]
        col_maxes = [max(col) for col in itertools.izip(*grid)]

        return sum(min(row_maxes[r], col_maxes[c])-val \
                   for r, row in enumerate(grid) \
                   for c, val in enumerate(row))