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
# IDEA : BFS + queue (made by array)
class Solution(object):
    def maxDistance(self, grid):
            m,n = len(grid), len(grid[0])
            q = [(i,j) for i in range(m) for j in range(n) if grid[i][j] == 1] 
            if len(q) == m * n or len(q) == 0: return -1
            level = 0
            while q:
                for _ in range(len(q)):
                    i,j = q.pop(0)
                    for x,y in [(1,0), (-1, 0), (0, 1), (0, -1)]:
                        xi, yj = x+i, y+j
                        if 0 <= xi < m and 0 <= yj < n and grid[xi][yj] == 0:
                            q.append((xi, yj))
                            grid[xi][yj] = 1
                level += 1
            return level-1

# V1
# https://leetcode.com/problems/as-far-from-land-as-possible/discuss/360960/Python-BFS
# IDEA : BFS
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