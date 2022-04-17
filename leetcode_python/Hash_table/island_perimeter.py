"""

463. Island Perimeter
Easy

You are given row x col grid representing a map where grid[i][j] = 1 represents land and grid[i][j] = 0 represents water.

Grid cells are connected horizontally/vertically (not diagonally). The grid is completely surrounded by water, and there is exactly one island (i.e., one or more connected land cells).

The island doesn't have "lakes", meaning the water inside isn't connected to the water around the island. One cell is a square with side length 1. The grid is rectangular, width and height don't exceed 100. Determine the perimeter of the island.

 

Example 1:


Input: grid = [[0,1,0,0],[1,1,1,0],[0,1,0,0],[1,1,0,0]]
Output: 16
Explanation: The perimeter is the 16 yellow stripes in the image above.
Example 2:

Input: grid = [[1]]
Output: 4
Example 3:

Input: grid = [[1,0]]
Output: 4
 

Constraints:

row == grid.length
col == grid[i].length
1 <= row, col <= 100
grid[i][j] is 0 or 1.
There is exactly one island in grid.

"""

# V0 
class Solution:
    def islandPerimeter(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        M, N = len(grid), len(grid[0])
        counts = 0
        neighbors = 0
        for i in range(M):
            for j in range(N):
                if grid[i][j] == 1:
                    counts += 1
                    if i < M - 1 and grid[i + 1][j] == 1:
                            neighbors += 1
                    if j < N - 1  and grid[i][j + 1] == 1:
                            neighbors += 1
        return 4 * counts - 2 * neighbors

# V0'
# IDEA : SIMPLE COUNTING
class Solution:
    def islandPerimeter(self, grid):
        
        rows = len(grid)
        cols = len(grid[0])
        
        result = 0
        
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 1:
                    if r == 0:
                        up = 0
                    else:
                        up = grid[r-1][c]
                    if c == 0:
                        left = 0
                    else:
                        left = grid[r][c-1]
                    if r == rows-1:
                        down = 0
                    else:
                        down = grid[r+1][c]
                    if c == cols-1:
                        right = 0
                    else:
                        right = grid[r][c+1]
                        
                    result += 4-(up+left+right+down)
                
        return result
                
# V1
# IDEA : SIMPLE COUNTING
# https://leetcode.com/problems/island-perimeter/solution/
class Solution:
    def islandPerimeter(self, grid: List[List[int]]) -> int:
        
        rows = len(grid)
        cols = len(grid[0])
        
        result = 0
        
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 1:
                    if r == 0:
                        up = 0
                    else:
                        up = grid[r-1][c]
                    if c == 0:
                        left = 0
                    else:
                        left = grid[r][c-1]
                    if r == rows-1:
                        down = 0
                    else:
                        down = grid[r+1][c]
                    if c == cols-1:
                        right = 0
                    else:
                        right = grid[r][c+1]
                        
                    result += 4-(up+left+right+down)
                
        return result

# V1'
# IDEA : BETTER COUNTING
# https://leetcode.com/problems/island-perimeter/solution/
class Solution:
    def islandPerimeter(self, grid: List[List[int]]) -> int:
        rows = len(grid)
        cols = len(grid[0])
        
        result = 0
        
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 1:
                    result += 4
                    
                    if r > 0 and grid[r-1][c] == 1:
                        result -= 2
                        
                    if c > 0 and grid[r][c-1] == 1:
                        result -= 2
        
        return result

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/83868905
class Solution:
    def islandPerimeter(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        M, N = len(grid), len(grid[0])
        counts = 0
        neighbors = 0
        for i in range(M):
            for j in range(N):
                if grid[i][j] == 1:
                    counts += 1
                    if i < M - 1:
                        if grid[i + 1][j] == 1:
                            neighbors += 1
                    if j < N - 1:
                        if grid[i][j + 1] == 1:
                            neighbors += 1
        return 4 * counts - 2 * neighbors

# V1'''
# https://www.jiuzhang.com/solution/island-perimeter/#tag-highlight-lang-python
class Solution:
    """
    @param grid: a 2D array
    @return: the perimeter of the island
    """
    def islandPerimeter(self, grid):
        # Write your code here
        rowN = len(grid)
        colN = len(grid[0])
        topBottom = [0] * colN
        grid = [topBottom] + grid + [topBottom]
        grid = [[0] + x + [0] for x in grid]

        perimeter = 0
        for i in range(rowN + 2):
            for j in range(colN + 2):
                elem = grid[i][j]
                if elem == 1:
                    up = 1 - grid[i-1][j]
                    down = 1 - grid[i+1][j]
                    left = 1 - grid[i][j-1]
                    right = 1 - grid[i][j+1]
                    perimeter += (up + down + left + right)
        return perimeter

# V2