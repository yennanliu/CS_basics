# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS 
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        row, col = len(grid), len(grid[0])
        answer = 0
        def dfs(i, j):
            if 0 <= i <= row - 1 and 0 <= j <= col - 1 and grid[i][j]:
                grid[i][j] = 0
                return  1 + dfs(i - 1, j) + dfs(i + 1, j) + dfs(i, j - 1) + dfs(i, j + 1)
            return 0
        return max(dfs(i, j) for i in range(row) for j in range(col))

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS 
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        self.res = 0
        self.island = 0
        M, N = len(grid), len(grid[0])
        for i in range(M):
            for j in range(N):
                if grid[i][j]:
                    self.dfs(grid, i, j)
                    self.res = max(self.res, self.island)
                    self.island = 0
        return self.res
    
    def dfs(self, grid, i, j): # ensure grid[i][j] == 1
        M, N = len(grid), len(grid[0])
        grid[i][j] = 0
        self.island += 1
        dirs = [(0, 1), (0, -1), (-1, 0), (1, 0)]
        for d in dirs:
            x, y = i + d[0], j + d[1]
            if 0 <= x < M and 0 <= y < N and grid[x][y]:
                self.dfs(grid, x, y)

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : BFS 
# DEV 

# V2 
# Time:  O(m * n)
# Space: O(m * n), the max depth of dfs may be m * n
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        directions = [[-1,  0], [ 1,  0], [ 0,  1], [ 0, -1]]

        def dfs(i, j, grid, area):
            if not (0 <= i < len(grid) and \
                    0 <= j < len(grid[0]) and \
                    grid[i][j] > 0):
                return False
            grid[i][j] *= -1
            area[0] += 1
            for d in directions:
                dfs(i+d[0], j+d[1], grid, area)
            return True

        result = 0
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                area = [0]
                if dfs(i, j, grid, area):
                    result = max(result, area[0])
        return result