# V1 : dev 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/81748335
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
# Time:  O(n^2)
# Space: O(1)
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