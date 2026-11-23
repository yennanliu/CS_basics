"""

695. Max Area of Island
Medium

You are given an m x n binary matrix grid. An island is a group of 1's (representing land) connected 4-directionally (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.

The area of an island is the number of cells with a value 1 in the island.

Return the maximum area of an island in grid. If there is no island, return 0.

 

Example 1:


Input: grid = [[0,0,1,0,0,0,0,1,0,0,0,0,0],[0,0,0,0,0,0,0,1,1,1,0,0,0],[0,1,1,0,1,0,0,0,0,0,0,0,0],[0,1,0,0,1,1,0,0,1,0,1,0,0],[0,1,0,0,1,1,0,0,1,1,1,0,0],[0,0,0,0,0,0,0,0,0,0,1,0,0],[0,0,0,0,0,0,0,1,1,1,0,0,0],[0,0,0,0,0,0,0,1,1,0,0,0,0]]
Output: 6
Explanation: The answer is not 11, because the island must be connected 4-directionally.
Example 2:

Input: grid = [[0,0,0,0,0,0,0,0]]
Output: 0
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA: DFS + VISITED
# time = O(m * n)
# space = O(m * n)  # recursion stack in the worst case
class Solution(object):
    def maxAreaOfIsland(self, grid):
        # edge

        l = len(grid)
        w = len(grid[0])

        self.max_area = 0

        for y in range(l):
            for x in range(w):
                if grid[y][x] == 1:
                    self.max_area = max(
                    self.max_area, 
                    self.helper(grid, x, y)
                    )

        return self.max_area



    def helper(self, grid, x, y):
        l = len(grid)
        w = len(grid[0])

        if x < 0 or x >= w or y < 0 or y >= l or grid[y][x] != 1:
            return 0

        # mark as visited
        grid[y][x] = -1

        return ( self.helper(grid, x-1, y) + 
                 self.helper(grid, x+1, y) +
                 self.helper(grid, x, y-1) +
                 self.helper(grid, x, y+1) ) + 1


# V0-0-1
# IDEA: DFS + class level var (self.cur_area) (gpt)
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0

        l = len(grid)
        w = len(grid[0])

        max_area = 0

        for y in range(l):
            for x in range(w):
                if grid[y][x] == 1:

                    """
                    NOTE !!!

                    we need to set `cur_area` as `self.cur_area`
                    if want to use below dfs approach.

                    -> so its value can be accumulated; NOT being
                       reset in every dfs call
                    """
                    # Reset area for each new island
                    self.cur_area = 0

                    self.dfs(grid, x, y)

                    max_area = max(max_area, self.cur_area)

        return max_area

    def dfs(self, grid, x, y):
        l = len(grid)
        w = len(grid[0])

        # Boundary check
        if x < 0 or x >= w or y < 0 or y >= l:
            return

        # Water or visited
        if grid[y][x] != 1:
            return

        # Mark visited
        grid[y][x] = -1

        # Update shared instance variable
        self.cur_area += 1

        # DFS in 4 directions
        self.dfs(grid, x - 1, y)
        self.dfs(grid, x + 1, y)
        self.dfs(grid, x, y - 1)
        self.dfs(grid, x, y + 1)


"""
NOTE !!! below is WRONG !!!


-> Reason:

四個方向的 self.dfs(...) 呼叫沒有把面積加總回來（int 不可變傳值陷阱）：

在目前這段寫法裡，假設 (x, y) 自己是第 1 格，往左走找到了 2 格、往右走找到了 3 格：

往左呼叫 self.dfs(grid, x-1, y, cur_area) 算出來的結果沒有接住、也沒有加進 cur_area；

往右呼叫也是如此；

最後 return cur_area 只會回傳在當前這層加了 1 之後的值，無法把各個分支探索到的面積彙總成整座島嶼的總面積。

解決方式是讓 dfs(grid, x, y) 不靠外部傳入累加器 cur_area，而是直接回傳「從 (x, y) 往下探索到的總面積」，並把四個方向的回傳值全部加起來。



```
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # edge
        if not grid or len(grid) == 0:
          return 0

        l = len(grid)
        w = len(grid[0])

        max_area = 0

        for y in range(l):
          for x in range(w):
            if grid[y][x] == 1:

              cur_area = self.dfs(grid, x, y, 0)
              max_area = max(max_area, cur_area)


        return max_area


    def dfs(self, grid, x, y, cur_area):
        # edge

        l = len(grid)
        w = len(grid[0])

        if x < 0 or x >= w or y < 0 or y >= l:
          return

        if grid[y][x] != 1:
          return

        # NOTE !!!
        # below is wrong !!!
        cur_area += 1

        grid[y][x] = -1

        self.dfs(grid, x-1, y, cur_area)
        self.dfs(grid, x+1, y, cur_area)
        self.dfs(grid, x, y+1, cur_area)
        self.dfs(grid, x, y-1, cur_area)

        return cur_area
```

"""


# V0-1
# IDEA: DFS + VISITED
# time = O(m * n)
# space = O(m * n)  # recursion stack in the worst case
class Solution(object):
    def maxAreaOfIsland(self, grid):
        if not grid or not grid[0]:
            return 0

        l = len(grid)
        w = len(grid[0])

        self.max_area = 0

        for y in range(l):
            for x in range(w):
                if grid[y][x] == 1:
                    self.max_area = max(
                        self.max_area,
                        self.helper(grid, x, y)
                    )

        return self.max_area

    def helper(self, grid, x, y):
        l = len(grid)
        w = len(grid[0])

        if x < 0 or x >= w or y < 0 or y >= l or grid[y][x] != 1:
            return 0

        # mark visited
        grid[y][x] = -1

        return (
            1
            + self.helper(grid, x - 1, y)
            + self.helper(grid, x + 1, y)
            + self.helper(grid, x, y - 1)
            + self.helper(grid, x, y + 1)
        )


# V0-2
# IDEA : DFS
# * PLEASE NOTE THAT IT IS NEEDED TO GO THROUGH EVERY ELEMENT IN THE GRID
#   AND RUN THE DFS WITH IN THIS PROBLEM
# time = O(m * n)
# space = O(m * n)  # recursion stack in the worst case
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

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS
# * PLEASE NOTE THAT IT IS NEEDED TO GO THROUGH EVERY ELEMENT IN THE GRID
#   AND RUN THE DFS WITH IN THIS PROBLEM
# time = O(m * n)
# space = O(m * n)  # recursion stack in the worst case
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

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : DFS
# time = O(m * n)
# space = O(m * n)  # recursion stack in the worst case
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

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79182435
# IDEA : BFS 
# DEV 

# V1'''
# https://www.jiuzhang.com/solution/max-area-of-island/#tag-highlight-lang-python
# IDEA : DFS
# time = O(m * n)
# space = O(m * n)  # recursion stack in the worst case
class Solution(object):
    def maxAreaOfIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        GET EACH ISLAND AREA VIA DFS
        """
        if not grid: return
        
        rows, cols = len(grid), len(grid[0])
        max_area = -sys.maxint - 1
        
        for i in range(rows):
            for j in range(cols):
                if grid[i][j] == 1:
                    max_area = max(max_area,self.doDfs(grid,i,j,1))
                    
        return max(0,max_area)
    
    def doDfs(self,grid,i,j,count):

        grid[i][j] = 0

        for m,n in [(i-1,j),(i+1,j),(i,j-1),(i,j+1)]:
            if(m>=0 and m<len(grid) and n>=0 and n<len(grid[0]) and grid[m][n] == 1):
                count = 1 + self.doDfs(grid,m,n,count)
        
        return count

# V2
# time = O(m * n)
# space = O(m * n)  # the max depth of dfs may be m * n
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