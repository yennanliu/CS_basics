"""

1162. As Far from Land as Possible
Medium

Given an n x n grid containing only values 0 and 1, where 0 represents water and 1 represents land, find a water cell such that its distance to the nearest land cell is maximized, and return the distance. If no land or water exists in the grid, return -1.

The distance used in this problem is the Manhattan distance: the distance between two cells (x0, y0) and (x1, y1) is |x0 - x1| + |y0 - y1|.

 

Example 1:


Input: grid = [[1,0,1],[0,0,0],[1,0,1]]
Output: 2
Explanation: The cell (1, 1) is as far as possible from all the land with distance 2.
Example 2:


Input: grid = [[1,0,0],[0,0,0],[0,0,0]]
Output: 4
Explanation: The cell (2, 2) is as far as possible from all the land with distance 4.
 

Constraints:

n == grid.length
n == grid[i].length
1 <= n <= 100
grid[i][j] is 0 or 1

"""

# V0
class Solution(object):
    def maxDistance(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        pass


# V0-1
# IDEA: MULTI SOURCE BFS (gpt)
from collections import deque

class Solution(object):
    def maxDistance(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        l = len(grid)
        w = len(grid[0])

        q = deque()

        # Start BFS from every land cell.
        """
        NOTE !!!

        we collect `land` cell
        """
        for y in range(l):
            for x in range(w):
                if grid[y][x] == 1:
                    q.append((x, y, 0))

        # All land or all water
        # NOTE !!! below
        if not q or len(q) == l * w:
            return -1

        """
        NOTE !!!

        we need visited
        """
        visited = set()

        moves = [
            (1, 0),
            (-1, 0),
            (0, 1),
            (0, -1)
        ]

        max_dist = -1

        while q:
            x, y, dist = q.popleft()

            for dx, dy in moves:
                nx = x + dx
                ny = y + dy

                if 0 <= nx < w and 0 <= ny < l:
                    if grid[ny][nx] == 0 and (nx, ny) not in visited:
                        visited.add((nx, ny))
                        q.append((nx, ny, dist + 1))

                        max_dist = max(max_dist, dist + 1)

        return max_dist



# V0-2
# IDEA: MULTI SOURCE BFS (gemini)
from collections import deque

class Solution(object):
    def maxDistance(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        rows = len(grid)
        cols = len(grid[0])
        q = deque()

        # Step 1: Enqueue all land cells (1s)
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 1:
                    q.append((r, c))

        # Edge case: If grid has NO land (len(q) == 0) or NO water (len(q) == rows * cols)
        if len(q) == 0 or len(q) == rows * cols:
            return -1

        moves = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        max_dist = -1

        # Step 2: Multi-source BFS
        while q:
            r, c = q.popleft()

            for dr, dc in moves:
                nr, nc = r + dr, c + dc

                # If neighbor is within bounds and is an unvisited water cell (0)
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 0:
                    # Store the distance directly in grid (land cells start at 1)
                    grid[nr][nc] = grid[r][c] + 1
                    max_dist = max(max_dist, grid[nr][nc] - 1)
                    q.append((nr, nc))

        return max_dist


# V0
# IDEA : BFS
# time = O(n^2)  # grid is n x n
# space = O(n^2)
class Solution(object):
    def maxDistance(self, grid):
        # edge
        if not grid:
            return -1
        l = len(grid)
        w = len(grid[0])
        lands = []
        for i in range(l):
            for j in range(w):
                if grid[i][j] == 1:
                    lands.append([i, j])
        if len(lands) == 0 or len(lands) == l * w:
            return -1
        # bfs
        q = lands
        level = 0
        moves = [[0,-1], [0,1], [1,0], [-1,0]]
        while q:
            for i in range(len(q)):
                y, x = q.pop(0)
                for m in moves:
                    _y = y + m[0]
                    _x = x + m[1]
                    if 0 <= _x < w and 0 <= _y < l and grid[_y][_x] == 0:
                        q.append([_y, _x])
                        grid[_y][_x] = 1
            level += 1
        return level - 1

# V0
# IDEA : BFS + queue (made by array)
# time = O(n^2)  # grid is n x n
# space = O(n^2)
class Solution(object):
    def maxDistance(self, grid):
            m,n = len(grid), len(grid[0])
            """
            NOTE !!! we get all (y, x) == 1 as queue
            """ 
            q = [(i,j) for i in range(m) for j in range(n) if grid[i][j] == 1] 
            if len(q) == m * n or len(q) == 0: return -1
            level = 0
            while q:
                for _ in range(len(q)):
                    i,j = q.pop(0)
                    for x,y in [(1,0), (-1, 0), (0, 1), (0, -1)]:
                        xi, yj = x+i, y+j
                        """
                        NOTE !!!
                            -> 1) we deal with ALL land ((y, x) == 1) distance on the same time
                            -> 2) so we do level+= 1 outside of for loop as once (since we only care max length)
                        """
                        if 0 <= xi < m and 0 <= yj < n and grid[xi][yj] == 0:
                            q.append((xi, yj))
                            # NOTE !! we modify visited val = 1, so avoid visit again
                            grid[xi][yj] = 1
                level += 1
            return level-1

# V1
# https://leetcode.com/problems/as-far-from-land-as-possible/discuss/360960/Python-BFS
# IDEA : BFS
# time = O(n^2)  # grid is n x n
# space = O(n^2)
class Solution(object):
    def maxDistance(self, grid):
            m,n = len(grid), len(grid[0])
            q = deque([(i,j) for i in range(m) for j in range(n) if grid[i][j] == 1])    
            if len(q) == m * n or len(q) == 0: return -1
            level = 0
            while q:
                size = len(q)
                for _ in range(size):
                    i,j = q.popleft()
                    for x,y in [(1,0), (-1, 0), (0, 1), (0, -1)]:
                        xi, yj = x+i, y+j
                        if 0 <= xi < m and 0 <= yj < n and grid[xi][yj] == 0:
                            q.append((xi, yj))
                            grid[xi][yj] = 1
                level += 1
            return level-1

# V1'
# https://leetcode.com/problems/as-far-from-land-as-possible/discuss/395330/python-bfs
# IDEA : BFS
# time = O(n^2)  # grid is n x n
# space = O(n^2)
from collections import deque
class Solution:
    def maxDistance(self, grid):
        h = len(grid)      
        q = deque([(i, j) for i in range(h) for j in range(h) if grid[i][j]])
    
        if len(q) == 0 or len(q) == h**2:
            return -1     
        dis = -1
        while q:
            qq = deque()
            for i, j in q:
                for r, c in [(i-1, j), (i+1, j), (i, j-1), (i, j+1)]:
                    if 0 <= r < h and 0 <= c < h and not grid[r][c]:
                        grid[r][c] = 1
                        qq += [(r, c)]
            q = qq
            dis += 1        
        return dis

# V2
