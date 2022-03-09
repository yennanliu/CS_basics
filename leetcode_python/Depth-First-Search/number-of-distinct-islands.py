"""

Leetcode 694. Number of Distinct Islands (Python)
Related Topic
Depth-First-Search.

Description
Given a non-empty 2D array grid of 0’s and 1’s, an island is a group of 1’s (representing land) connected 4-directionally (horizontal or vertical.) You may assume all four edges of the grid are surrounded by water.

Count the number of distinct islands. An island is considered to be the same as another if and only if one island can be translated (and not rotated or reflected) to equal the other.

Sample I/O
Example 1
11000
11000
00011
00011
Given the above grid map, return 1.

Example 2
11011
10000
00001
11011
Given the above grid map, return 3.

Notice That:

11
1
and

 1
11
are considered different island shapes, because we do not consider reflection / rotation.

Note:
The length of each dimension in the given grid does not exceed 50.

"""

# V0 
# IDEA : DFS
class Solution:
    def numDistinctIslands(self, grid):
        def dfs(x, y, pos, rel_pos):
            if grid[x][y] != 1:
                ### NOTICE HERE : not need to return pos, since pos already updated in place (when meet the condition),
                ### so here just need a "return" to stop the while loop
                return
            ### NOTE : we make visited point as -1, to avoid re-visit
            grid[x][y] = -1
            directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
            for dx, dy in directions:
                if 0 <= x+dx < row and 0 <= y+dy < col and grid[x+dx][y+dy] == 1:
                    ### NOTE : the trick here (only add dx, dy, but not the actual x-axis, y-axis)
                    new_rel_pos = (rel_pos[0] + dx, rel_pos[1] + dy)
                    ### NOTE : we add the current visit point to pos
                    #         -> since in this problem we ONLY need DISTINCT islands
                    pos.append(new_rel_pos)
                    dfs(x+dx, y+dy, pos, new_rel_pos)
            
        shapes = set()
        row, col = len(grid), len(grid[0])
        for x in range(row):
            for y in range(col):
                if grid[x][y] == 1:
                    # get the shape of island
                    pos = []
                    # NOTE : (0, 0) as init rel_pos
                    dfs(x, y, pos, (0, 0))
                    shapes.add(tuple(pos))            
        return len(shapes)

# V0'
# IDEA : DFS
# TODO : fix below
# class Solution:
#     def numDistinctIslands(self, grid):
#         def normalize(arr):
#             x_min = min( x[1] for x in arr)
#             y_min = min( x[0] for x in arr)
#             _arr = [ [x[0]-y_min, x[1] - x_min] for x in arr ]
#             return _arr
#
#         def dfs(x, y, cur, visited):
#             moves = [[0,1],[0,-1],[1,0],[-1,0]]
#             for m in moves:
#                 _x = x + m[1]
#                 _y = y + m[0]
#                 if 0 <= _x < w and 0 <= _y < l and grid[_y][_x] != 0 and [_y,_x] not in visited:
#                     cur.append([_y,_x])
#                     #cur.append([m[0],m[1]])
#                     visited.append([_y,_x])
#                     dfs(_x, _y, cur, visited)
#            
#         # edge case
#         if not grid:
#             return 0
#         w = len(grid[0])
#         l = len(grid)
#         res = []
#         visited = []
#         for i in range(l):
#             for j in range(w):
#                 cur = []
#                 if not dfs(j, i, cur, visited):
#                     if cur:
#                         #print ("cur = " + str(cur))
#                         #print ("_cur = " + str(cur))
#                         _cur = normalize(cur)
#                         _cur.sort()
#                         if _cur not in res:
#                             res.append(_cur)
#         print ("res = " + str(res))
#         return len(res)

# V0'
# IDEA DFS
# TODO : validate it
class Solution(object):
    def numIslands(self, grid):
        def dfs(grid, item):
            if not grid:
                return
            # avoid visit again
            tmp.append([item[0],item[1]])
            grid[item[0]][item[1]] = "-1"
            moves = [(0,1),(0,-1),(1,0),(-1,0)]
            for move in moves:
                _x = item[0] + move[0]
                _y = item[1] + move[1]
                if 0 <= _x < l and 0 <= _y < w and grid[_x][_y] == "1":
                    dfs(grid, [_x, _y])
        if not grid:
            return 0
        res = 0
        l = len(grid)
        w = len(grid[0])
        cache = []
        tmp = []
        for i in range(l):
            for j in range(w):
                print ((i,j))
                if grid[i][j] == "1":
                    tmp = []
                    dfs(grid, [i,j])
                    tmp.sort()
                    _tmp = [[x[0]  - tmp[0][0], x[1] - tmp[0][1]] for x in tmp]
                    #print ("_tmp = " + str(_tmp) + " cache = " + str(cache))
                    if _tmp not in cache:
                        cache.append(tmp)
                        res += 1
        return res

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

# V1
# IDEA : Brute Force
# https://leetcode.com/problems/number-of-distinct-islands/solution/
class Solution:
    def numDistinctIslands(self, grid: List[List[int]]) -> int:
        
        def current_island_is_unique():
            for other_island in unique_islands:
                if len(other_island) != len(current_island):
                    continue
                for cell_1, cell_2 in zip(current_island, other_island):
                    if cell_1 != cell_2:
                        break
                else:
                    return False
            return True
            
        # Do a DFS to find all cells in the current island.
        def dfs(row, col):
            if row < 0 or col < 0 or row >= len(grid) or col >= len(grid[0]):
                return
            if (row, col) in seen or not grid[row][col]:
                return
            seen.add((row, col))
            current_island.append((row - row_origin, col - col_origin))
            dfs(row + 1, col)
            dfs(row - 1, col)
            dfs(row, col + 1)
            dfs(row, col - 1)
        
        # Repeatedly start DFS's as long as there are islands remaining.
        seen = set()
        unique_islands = []
        for row in range(len(grid)):
            for col in range(len(grid[0])):
                current_island = []
                row_origin = row
                col_origin = col
                dfs(row, col)
                if not current_island or not current_island_is_unique():
                    continue
                unique_islands.append(current_island)
        print(unique_islands)
        return len(unique_islands)

# V1
# IDEA : Hash By Local Coordinates
# https://leetcode.com/problems/number-of-distinct-islands/solution/
class Solution:
    def numDistinctIslands(self, grid: List[List[int]]) -> int:

        # Do a DFS to find all cells in the current island.
        def dfs(row, col):
            if row < 0 or col < 0 or row >= len(grid) or col >= len(grid[0]):
                return
            if (row, col) in seen or not grid[row][col]:
                return
            seen.add((row, col))
            current_island.add((row - row_origin, col - col_origin))
            dfs(row + 1, col)
            dfs(row - 1, col)
            dfs(row, col + 1)
            dfs(row, col - 1)
        
        # Repeatedly start DFS's as long as there are islands remaining.
        seen = set()
        unique_islands = set()
        for row in range(len(grid)):
            for col in range(len(grid[0])):
                current_island = set()
                row_origin = row
                col_origin = col
                dfs(row, col)
                if current_island:
                    unique_islands.add(frozenset(current_island))
        
        return len(unique_islands)

# V1
# IDEA : Hash By Path Signature
# https://leetcode.com/problems/number-of-distinct-islands/solution/
class Solution:
    def numDistinctIslands(self, grid: List[List[int]]) -> int:

        # Do a DFS to find all cells in the current island.
        def dfs(row, col, direction):
            if row < 0 or col < 0 or row >= len(grid) or col >= len(grid[0]):
                return
            if (row, col) in seen or not grid[row][col]:
                return
            seen.add((row, col))
            path_signature.append(direction)
            dfs(row + 1, col, "D")
            dfs(row - 1, col, "U")
            dfs(row, col + 1, "R")
            dfs(row, col - 1, "L")
            path_signature.append("0")
        
        # Repeatedly start DFS's as long as there are islands remaining.
        seen = set()
        unique_islands = set()
        for row in range(len(grid)):
            for col in range(len(grid[0])):
                path_signature = []
                dfs(row, col, "0")
                if path_signature:
                    unique_islands.add(tuple(path_signature))
        
        return len(unique_islands)

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