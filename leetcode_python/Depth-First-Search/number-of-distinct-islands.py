# V0 

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