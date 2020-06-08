# 694 Number of Distinct Islands
#
# Given a non-empty 2D array grid of 0’s and 1’s, an island is a group of 1’s (representing land) connected 4-directionally (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.
#
# Count the number of distinct islands. An island is considered to be the same as another if and only if one island can be translated (and not rotated or reflected) to equal the other.
#
# Example 1:
# 11000
# 11000
# 00011
# 00011
# Given the above grid map, return 1.
# Example 2:
# 11011
# 10000
# 00001
# 11011
# Given the above grid map, return 3.
#
# Notice that:
# 11
# 1
# and
# 1
# 11
# are considered different island shapes, because we do not consider reflection / rotation.
# Note: The length of each dimension in the given grid does not exceed 50.


# V0 
# IDEA : DFS
class Solution:
    def numDistinctIslands(self, grid):
        def dfs(x, y, pos, rel_pos):
            if grid[x][y] != 1:
                ### NOTICE HERE : not need to return pos, since pos already updated in place (when meet the condition),
                ### so here just need a "return" to stop the while loop
                return
            grid[x][y] = -1
            directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
            for dx, dy in directions:
                if 0 <= x+dx < row and 0 <= y+dy < col and grid[x+dx][y+dy] == 1:
                    new_rel_pos = (rel_pos[0] + dx, rel_pos[1] + dy)
                    pos.append(new_rel_pos)
                    dfs(x+dx, y+dy, pos, new_rel_pos)
            
        shapes = set()
        row, col = len(grid), len(grid[0])
        for x in range(row):
            for y in range(col):
                if grid[x][y] == 1:
                    # get the shape of island
                    pos = []
                    dfs(x, y, pos, (0, 0))
                    shapes.add(tuple(pos))            
        return len(shapes)

# V1 
# https://blog.csdn.net/danspace1/article/details/86610850
class Solution:
    def numDistinctIslands(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        def dfs(x, y, pos, rel_pos):
            if grid[x][y] != 1:
                return
            grid[x][y] = -1
            directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
            for dx, dy in directions:
                if 0 <= x+dx < row and 0 <= y+dy < col and grid[x+dx][y+dy] == 1:
                    new_rel_pos = (rel_pos[0] + dx, rel_pos[1] + dy)
                    pos.append(new_rel_pos)
                    dfs(x+dx, y+dy, pos, new_rel_pos)
            
        shapes = set()
        row, col = len(grid), len(grid[0])
        for x in range(row):
            for y in range(col):
                if grid[x][y] == 1:
                    # get the shape of island
                    pos = []
                    dfs(x, y, pos, (0, 0))
                    shapes.add(tuple(pos))            
        return len(shapes)

### Test case : dev 

# V1'
# https://www.jiuzhang.com/solution/number-of-distinct-islands/#tag-highlight-lang-python
# IDEA : DFS / BFS 
class Solution:
    """
    @param grid: a list of lists of integers
    @return: return an integer, denote the number of distinct islands
    """
    def numberofDistinctIslands(self, grid):
        self.n = len(grid)
        self.m = len(grid[0])
        
        islands = set()
        
        for i in range(self.n):
            for j in range(self.m):
                if grid[i][j] == 1:
                    aIsland = set()
                    self.dfs(i, j, grid, aIsland, i, j)
                    islands.add(tuple(aIsland))
        
        return len(islands)
                
    def dfs(self, x, y, grid, aIsland, bx, by):
        grid[x][y] = 0
        aIsland.add((x - bx, y - by))
        for i in range(4):
            nx = x + self.dx[i]
            ny = y + self.dy[i]
            if nx < 0 or nx >= self.n or ny < 0 or ny >= self.m or grid[nx][ny] != 1:
                continue
            self.dfs(nx, ny, grid, aIsland, bx, by)
        
    def __init__(self):
        self.n = 0
        self.m = 0
        self.dx = [0, 0, 1, -1]
        self.dy = [1, -1, 0, 0]

# V2 
# Time:  O(m * n)
# Space: O(m * n)
class Solution(object):
    def numDistinctIslands(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        directions = {'l':[-1,  0], 'r':[ 1,  0], \
                      'u':[ 0,  1], 'd':[ 0, -1]}

        def dfs(i, j, grid, island):
            if not (0 <= i < len(grid) and \
                    0 <= j < len(grid[0]) and \
                    grid[i][j] > 0):
                return False
            grid[i][j] *= -1
            for k, v in directions.iteritems():
                island.append(k)
                dfs(i+v[0], j+v[1], grid, island)
            return True

        islands = set()
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                island = []
                if dfs(i, j, grid, island):
                    islands.add("".join(island))
        return len(islands)