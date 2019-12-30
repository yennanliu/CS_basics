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

# V1 
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

# V1'
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