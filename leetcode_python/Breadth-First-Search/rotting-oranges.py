"""

994. Rotting Oranges
Medium

You are given an m x n grid where each cell can have one of three values:

0 representing an empty cell,
1 representing a fresh orange, or
2 representing a rotten orange.
Every minute, any fresh orange that is 4-directionally adjacent to a rotten orange becomes rotten.

Return the minimum number of minutes that must elapse until no cell has a fresh orange. If this is impossible, return -1.

 

Example 1:


Input: grid = [[2,1,1],[1,1,0],[0,1,1]]
Output: 4
Example 2:

Input: grid = [[2,1,1],[0,1,1],[1,0,1]]
Output: -1
Explanation: The orange in the bottom left corner (row 2, column 0) is never rotten, because rotting only happens 4-directionally.
Example 3:

Input: grid = [[0,2]]
Output: 0
Explanation: Since there are already no fresh oranges at minute 0, the answer is just 0.
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 10
grid[i][j] is 0, 1, or 2.

"""

# V0
class Solution(object):
    def orangesRotting(self, grid):
        n = len(grid)
        m = len(grid[0])
        count = 0
        q = []
        for i in range(n):
            for j in range(m):
                if grid[i][j] == 1:
                    count += 1
                elif grid[i][j] == 2:
                    q.append((i,j,0))
        seen = set()
        while q:
            y, x, d = q.pop(0)
            dirs = [(-1, 0), (+1, 0), (0, -1), (0, +1)]
            for _dir in dirs:
                x1 = x + _dir[0]
                y1 = y + _dir[1]
                if 0 <= y1 < n and 0 <= x1 < m and (y1, x1) not in seen and grid[y1][x1] == 1:
                    seen.add((y1,x1))
                    count -= 1
                    if count == 0:
                        return d+1
                    q.append((y1, x1, d+1))
        return 0 if count == 0 else -1

# V1
# IDEA : BFS
# https://leetcode.com/problems/rotting-oranges/discuss/239032/Python-solution
class Solution(object):
    def orangesRotting(self, grid):
        n = len(grid)
        m = len(grid[0])
        count = 0
        q = collections.deque()
        for i in range(n):
            for j in range(m):
                if grid[i][j] == 1:
                    count += 1
                elif grid[i][j] == 2:
                    q.append((i,j,0))
        seen = set()
        while q:
            y, x, d = q.popleft()
            dirs = {(y-1,x),(y+1,x),(y,x+1),(y,x-1)}
            for y1,x1 in dirs:
                if 0 <= y1 < n and 0 <= x1 < m and (y1, x1) not in seen and grid[y1][x1] == 1:
                    seen.add((y1,x1))
                    count -= 1
                    if count == 0:
                        return d+1
                    q.append((y1, x1, d+1))
        return 0 if count == 0 else -1

# V1'
# IDEA : BFS
# https://leetcode.com/problems/rotting-oranges/discuss/781882/Python-by-BFS-%2B-timestamp-w-Demo
from collections import deque
class Solution:
    def orangesRotting(self, grid):
        
        # Constant for grid state
        VISITED = -1
        EMPTY = 0
        FRESH = 1
        ROTTEN = 2
        
        # Get dimension of grid
        h, w = len(grid), len(grid[0])
        
        # record for fresh oranges
        fresh_count = 0
        
        # record for position of initial rotten oranges
        rotten_grid = []       
        
        # board prescan for parameter initialization
        for y in range(h):
            for x in range(w):
                
                if grid[y][x] == FRESH:
                    fresh_count += 1
                    
                elif grid[y][x] == ROTTEN:
                    rotten_grid.append( (x, y, 0) )
                    
        traversal_queue = deque(rotten_grid)
        
        time_record = 0
        
        # BFS to rotting oranges
        while traversal_queue:
            
            x, y, time_stamp = traversal_queue.popleft()
            
            for dx, dy in ( (-1, 0), (+1, 0), (0, -1), (0, +1) ):
                
                next_x, next_y = x + dx, y + dy
                
                if 0 <= next_x < w and 0 <= next_y < h and grid[next_y][next_x] == FRESH:
                    
                    fresh_count -= 1
                    grid[next_y][next_x] = VISITED
                    
                    # update time record
                    time_record = time_stamp + 1 
                    
                    # adding current rotten orange to traversal queue
                    traversal_queue.append( (next_x, next_y, time_stamp + 1) )
        
        
        if fresh_count == 0:
            return time_record
        else:
            return -1

# V1''
# IDEA : BFS
# https://leetcode.com/problems/rotting-oranges/discuss/238862/Python-bfs
class Solution:
    def orangesRotting(self, g):
        q, good = [], 0
        for i, r in enumerate(g):
            for j, c in enumerate(r):
                if c == 2: q.append((i, j))
                elif c == 1: good += 1

        total, m, n = 0, len(g), len(g[0])                       
        while q:
            nxt_q = []
            if good == 0: return total
            total += 1
            for i, j in q:
                for x, y in [(i-1, j), (i+1, j), (i, j-1), (i, j+1)]:
                    if 0 <= x < m and 0 <= y < n and g[x][y] == 1:
                        g[x][y] = 2
                        good -= 1                        
                        nxt_q.append((x, y))
            q = nxt_q
        return total if good == 0 else -1

# V1'''
# IDEA : BFS
# https://leetcode.com/problems/rotting-oranges/discuss/388104/Python-10-lines-BFS-beat-97
class Solution:
    def orangesRotting(self, grid):
        row, col = len(grid), len(grid[0])
        rotting = {(i, j) for i in range(row) for j in range(col) if grid[i][j] == 2}
        fresh = {(i, j) for i in range(row) for j in range(col) if grid[i][j] == 1}
        timer = 0
        while fresh:
            if not rotting: return -1
            rotting = {(i+di, j+dj) for i, j in rotting for di, dj in [(0, 1), (1, 0), (0, -1), (-1, 0)] if (i+di, j+dj) in fresh}
            fresh -= rotting
            timer += 1
        return timer

# V1''''
# IDEA : DP
# https://leetcode.com/problems/rotting-oranges/discuss/342939/Python-DP-Solution
class Solution:
    def orangesRotting(self, grid):
        m = len(grid)
        n = len(grid[0])
        DP = [[[float('inf') for k in range(m*n)] for j in range(n)] for i in range(m)]
        for i in range(m):
            for j in range(n):
                for t in range(m*n):
                    if grid[i][j] == 2:
                        DP[i][j][t] = 0
                    else:
                        DP[i][j][t] = float('inf')
                    
        for t in range(1, m*n):
            for i in range(m):
                for j in range(n):
                    if i + 1 < m and grid[i + 1][j] != 0:
                        DP[i][j][t] = min(DP[i][j][t], 1 + DP[i + 1][j][t - 1])
                    if i - 1 >= 0 and grid[i - 1][j] != 0:
                        DP[i][j][t] = min(DP[i][j][t], 1 + DP[i - 1][j][t - 1])
                    if j + 1 < n and grid[i][j + 1] != 0:
                        DP[i][j][t] = min(DP[i][j][t], 1 + DP[i][j + 1][t - 1])
                    if j - 1 >= 0 and grid[i][j - 1] != 0:
                        DP[i][j][t] = min(DP[i][j][t], 1 + DP[i][j - 1][t - 1])
                        
        res = -float('inf')
        noOnesFlag = True
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1 and DP[i][j][m*n - 1] > res:
                    res = DP[i][j][m*n - 1]
                    noOnesFlag = False
        if noOnesFlag == True:
            return 0
        return res if res < float('inf') else -1

# V2