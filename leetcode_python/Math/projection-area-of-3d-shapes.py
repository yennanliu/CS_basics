"""

883. Projection Area of 3D Shapes
Easy

You are given an n x n grid where we place some 1 x 1 x 1 cubes that are axis-aligned with the x, y, and z axes.

Each value v = grid[i][j] represents a tower of v cubes placed on top of the cell (i, j).

We view the projection of these cubes onto the xy, yz, and zx planes.

A projection is like a shadow, that maps our 3-dimensional figure to a 2-dimensional plane. We are viewing the "shadow" when looking at the cubes from the top, the front, and the side.

Return the total area of all three projections.

Example 1:

https://s3-lc-upload.s3.amazonaws.com/uploads/2018/08/02/shadow.png

Input: grid = [[1,2],[3,4]]
Output: 17
Explanation: Here are the three projections ("shadows") of the shape made with each axis-aligned plane.

Example 2:

Input: grid = [[2]]
Output: 5

Example 3:

Input: grid = [[1,0],[0,2]]
Output: 8

Constraints:

n == grid.length == grid[i].length
1 <= n <= 50
0 <= grid[i][j] <= 50

"""

# V0

# V1 : dev 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/81748335
# time = O(n^2)  # n = grid dimension
# space = O(1)
class Solution(object):
    def projectionArea(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        top, front, side = 0, 0, 0
        n = len(grid)
        for i in range(n):
            x, y = 0, 0
            for j in range(n):
                if grid[i][j] != 0:
                    top += 1
                x = max(x, grid[i][j])
                y = max(y, grid[j][i])
            front += x
            side += y
        return top + front + side

# V3 
# https://www.cnblogs.com/seyjs/p/9538305.html
# time = O(n^2)  # n = grid dimension
# space = O(n)  # front and side arrays
class Solution(object):
    def projectionArea(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        front = [0] * len(grid)
        side = [0] * len(grid)
        top = 0
        for i in range(len(grid)):
            for j in range(len(grid[i])):
                if grid[i][j] == 0:
                    continue
                top += 1
                front[i] = max(front[i],grid[i][j])
                side[j] = max(side[j],grid[i][j])
        return top + sum(front) + sum(side)

# V4
# time = O(n^2)
# space = O(1)
class Solution(object):
    def projectionArea(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        result = 0
        for i in range(len(grid)):
            max_row, max_col = 0, 0
            for j in range(len(grid)):
                if grid[i][j]:
                    result += 1
                max_row = max(max_row, grid[i][j])
                max_col = max(max_col, grid[j][i])
            result += max_row + max_col
        return result