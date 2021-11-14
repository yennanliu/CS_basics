"""

Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), 
return the number of islands.

An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. 
You may assume all four edges of the grid are all surrounded by water.

Example 1:

Input: grid = [
  ["1","1","1","1","0"],
  ["1","1","0","1","0"],
  ["1","1","0","0","0"],
  ["0","0","0","0","0"]
]
Output: 1

Example 2:

Input: grid = [
  ["1","1","0","0","0"],
  ["1","1","0","0","0"],
  ["0","0","1","0","0"],
  ["0","0","0","1","1"]
]
Output: 3
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
grid[i][j] is '0' or '1'.

"""

# V0 
# IDEA : DFS
class Solution(object):
    def numIslands(self, grid):
        def dfs(grid, item):
            if grid[item[0]][item[1]] == "0":
                return

            ### NOTE : MAKE grid[item[0]][item[1]] = 0 -> avoid visit again
            grid[item[0]][item[1]] = 0
            moves = [(0,1),(0,-1),(1,0),(-1,0)]
            for move in moves:
                _x = item[0] + move[0]
                _y = item[1] + move[1]
                ### NOTE : the boundary
                #       -> _x < l, _y < w
                if 0 <= _x < l and 0 <= _y < w and grid[_x][_y] != 0:
                    dfs(grid, [_x, _y])
  
        if not grid:
            return 0
        res = 0
        l = len(grid)
        w = len(grid[0])
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "1":
                    ### NOTE : we go through every "1" in grids, and run dfs once
                    #         -> once dfs completed, we make res += 1 in each iteration
                    dfs(grid, [i,j])
                    res += 1
        return res

# V0' 
# IDEA : DFS
class Solution:
    def numIslands(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        res = 0
        for r in range(len(grid)):
            for c in range(len(grid[0])):
                if grid[r][c] == "1":
                    self.dfs(grid, r, c)
                    res += 1
        return res
        
    def dfs(self, grid, i, j):
        dirs = [[-1, 0], [0, 1], [0, -1], [1, 0]]
        # make the visited element as "0" in order to avoid visit
        # we can maintain a visited list for same purpose as well
        grid[i][j] = "0"
        for dir in dirs:
            nr, nc = i + dir[0], j + dir[1]
            if nr >= 0 and nc >= 0 and nr < len(grid) and nc < len(grid[0]):
                if grid[nr][nc] == "1":
                    self.dfs(grid, nr, nc)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81126995
# IDEA : DFS 
class Solution:
    def numIslands(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        res = 0
        for r in range(len(grid)):
            for c in range(len(grid[0])):
                if grid[r][c] == "1":
                    self.dfs(grid, r, c)
                    res += 1
        return res
        
    def dfs(self, grid, i, j):
        dirs = [[-1, 0], [0, 1], [0, -1], [1, 0]]
        grid[i][j] = "0"
        for dir in dirs:
            nr, nc = i + dir[0], j + dir[1]
            if nr >= 0 and nc >= 0 and nr < len(grid) and nc < len(grid[0]):
                if grid[nr][nc] == "1":
                    self.dfs(grid, nr, nc)

### Test case : dev 

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/81126995
# IDEA : BFS
class Solution(object):
    def numIslands(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        if not grid or not grid[0]: return 0
        M, N = len(grid), len(grid[0])
        que = collections.deque()
        res = 0
        directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        for i in range(M):
            for j in range(N):
                if grid[i][j] == '1':
                    res += 1
                    grid[i][j] = '0'
                    que.append((i, j))
                    while que:
                        x, y = que.pop()
                        for d in directions:
                            nx, ny = x + d[0], y + d[1]
                            if 0 <= nx < M and 0 <= ny < N and grid[nx][ny] == '1':
                                grid[nx][ny] = '0'
                                que.append((nx, ny))
        return res

# V1''
# https://www.jiuzhang.com/solution/number-of-big-islands/#tag-highlight-lang-python
# IDEA : BFS 
class Solution:
    """
    @param grid: a 2d boolean array
    @param k: an integer
    @return: the number of Islands
    """
    def numsofIsland(self, grid, k):
        # Write your code here
        if not grid or len(grid)==0 or len(grid[0])==0: return 0
        
        rows, cols = len(grid), len(grid[0])
        visited = [[False for i in range(cols)] for i in range(rows)]
        
        res = 0
        for i in range(rows):
            for j in range(cols):
                if visited[i][j]==False and grid[i][j] == 1:
                    check = self.bfs(grid, visited, i,j,k)
                    if check: res+=1
        return res 
    
    def bfs(self, grid, visited, x, y, k):
        rows, cols = len(grid), len(grid[0])
        
        import collections
        queue = collections.deque([(x, y)])
        visited[x][y] = True
        res = 0
        while queue:
            item = queue.popleft()
            res+=1 
            for idx, idy in ((1,0),(-1,0),(0,1),(0,-1)):
                x_new, y_new = item[0]+idx, item[1]+idy
                if x_new < 0 or x_new >= rows or y_new < 0 or y_new >= cols or visited[x_new][y_new] or grid[x_new][y_new] == 0: continue
                queue.append((x_new, y_new))
                visited[x_new][y_new] = True
        
        return res >= k

# V2 
# Time:  O(m * n)
# Space: O(m * n)
class Solution(object):
    # @param {boolean[][]} grid a boolean 2D matrix
    # @return {int} an integer
    def numIslands(self, grid):
        if not grid:
            return 0

        row = len(grid)
        col = len(grid[0])
        count = 0
        for i in xrange(row):
            for j in xrange(col):
                if grid[i][j] == '1':
                    self.dfs(grid, row, col, i, j)
                    count += 1
        return count

    def dfs(self, grid, row, col, x, y):
        if grid[x][y] == '0':
            return
        grid[x][y] = '0'

        if x != 0:
            self.dfs(grid, row, col, x - 1, y)
        if x != row - 1:
            self.dfs(grid, row, col, x + 1, y)
        if y != 0:
            self.dfs(grid, row, col, x, y - 1)
        if y != col - 1:
            self.dfs(grid, row, col, x, y + 1)
