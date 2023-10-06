"""

286. Walls and Gates
Medium

company
DoorDash
company
Amazon
company
Facebook

You are given an m x n grid rooms initialized with these three possible values.

-1 A wall or an obstacle.
0 A gate.
INF Infinity means an empty room. We use the value 231 - 1 = 2147483647 to represent INF as you may assume that the distance to a gate is less than 2147483647.
Fill each empty room with the distance to its nearest gate. If it is impossible to reach a gate, it should be filled with INF.

 

Example 1:


Input: rooms = [[2147483647,-1,0,2147483647],[2147483647,2147483647,2147483647,-1],[2147483647,-1,2147483647,-1],[0,-1,2147483647,2147483647]]
Output: [[3,-1,0,1],[2,2,1,-1],[1,-1,2,-1],[0,-1,3,4]]
Example 2:

Input: rooms = [[-1]]
Output: [[-1]]
 

Constraints:

m == rooms.length
n == rooms[i].length
1 <= m, n <= 250
rooms[i][j] is -1, 0, or 231 - 1.

"""

# V0
# IDEA : BFS 
class Solution:
    def wallsAndGates(self, rooms):
        # base case:
        if not rooms:
            return
        row, col = len(rooms), len(rooms[0])
        ### NOTE BELOW TRICK : find the index of a gate
        q = [(i, j) for i in range(row) for j in range(col) if rooms[i][j] == 0]
        for x, y in q:
            """
            NOTE !!! we do BFS with gate points

            -> since BFS make sure shortest distance, and shortest distance between gete and space is what we need
            -> get the distance from a gate
            """
            distance = rooms[x][y]+1
            directions = [(-1,0), (1,0), (0,-1), (0,1)]
            for dx, dy in directions:
                # find the INF around the gate
                new_x, new_y = x+dx, y+dy
                if 0 <= new_x < row and 0 <= new_y < col and rooms[new_x][new_y] == 2147483647:
                    # update the value
                    rooms[new_x][new_y] = distance
                    q.append((new_x, new_y))

# V0 
# IDEA : DFS  -> TLE (time out error)
class Solution:
    def wallsAndGates(self, rooms: List[List[int]]) -> None:
        """
        Do not return anything, modify rooms in-place instead.
        """
        # base case
        if not rooms: return
        
        row, col = len(rooms), len(rooms[0])
        for i in range(row):
            for j in range(col):
                if rooms[i][j] == 0:
                    self.dfs(rooms, i, j, 0)
                    
    def dfs(self, rooms, x, y, dist):
        row, col = len(rooms), len(rooms[0])
        if x < 0 or x >= row or y < 0 or y >= col or rooms[x][y] < dist:
            return
        rooms[x][y] = dist
        for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:
            self.dfs(rooms, x+dx, y+dy, dist+1)

# V1
# https://blog.csdn.net/danspace1/article/details/86615657
# IDEA : DFS  -> TLE (time out error)
# IDEA : 
# 1) GO THROUGH 2D GRID, GOT THE ZEROS (rooms[i][j] == 0)
# 2) DOING DFS OPERATION ON THE ZEROS
# 3) DFS OPERATION : 
#   -> START FROM ZERO, GO THROUGH 2D GRID, 
#   -> IF THERE IS ELEMENT == INF, -> RETURN THE DISTANCE BETWEEN INF AND ZERO
#   -> STOP THE LOOP WHILE ALREADY GOT THE MIN DISATANCE
# 4) REPEAT STEP 1) -> STEP 3) THEN RETURN THE UPDATED 2D GRID 
class Solution:
    def wallsAndGates(self, rooms: List[List[int]]) -> None:
        """
        Do not return anything, modify rooms in-place instead.
        """
        # base case
        if not rooms: return
        
        row, col = len(rooms), len(rooms[0])
        for i in range(row):
            for j in range(col):
                if rooms[i][j] == 0:
                    self.dfs(rooms, i, j, 0)
                    
    def dfs(self, rooms, x, y, dist):
        row, col = len(rooms), len(rooms[0])
        if x < 0 or x >= row or y < 0 or y >= col or rooms[x][y] < dist:
            return
        rooms[x][y] = dist
        for dx, dy in [(-1,0),(1,0),(0,-1),(0,1)]:
            self.dfs(rooms, x+dx, y+dy, dist+1)

# V1'
# https://blog.csdn.net/danspace1/article/details/86615657
# IDEA : BFS 
# IDEA : START FROM "DOOR", THEN FIND THE empty room (INF)
class Solution:
    def wallsAndGates(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: void Do not return anything, modify rooms in-place instead.
        """
        # base case:
        if not rooms:
            return
        row, col = len(rooms), len(rooms[0])
        # find the index of a gate
        q = [(i, j) for i in range(row) for j in range(col) if rooms[i][j] == 0]
        for x, y in q:
            # get the distance from a gate
            distance = rooms[x][y]+1
            directions = [(-1,0), (1,0), (0,-1), (0,1)]
            for dx, dy in directions:
                # find the INF around the gate
                new_x, new_y = x+dx, y+dy
                if 0 <= new_x < row and 0 <= new_y < col and rooms[new_x][new_y] == 2147483647:
                    # update the value
                    rooms[new_x][new_y] = distance
                    q.append((new_x, new_y))

# V1''
# https://www.jiuzhang.com/solution/walls-and-gates/#tag-highlight-lang-python
# IDEA : DFS 
class Solution:
    """
    @param rooms: m x n 2D grid
    @return: nothing
    """
    def wallsAndGates(self, rooms):
        # write your code here
        # 0 gate
        # -1 wall
        for row in range(len(rooms)):
            for col in range(len(rooms[0])):
                if rooms[row][col] != 0:
                    continue
                self.bfs(rooms, row, col)
        
    def bfs(self, rooms, row, col):
        dir = [(1, 0), (-1, 0), (0, 1), (0, -1)]
        visited = set([(row, col)])
        cur_state = (row, col, 0)
        import queue
        que = queue.Queue()
        que.put(cur_state)
        while not que.empty():
            cur_row, cur_col, cur_step = que.get()
            if rooms[cur_row][cur_col] < cur_step:
                continue
            rooms[cur_row][cur_col] = cur_step
            next_step = cur_step + 1
            for (drow, dcol) in dir:
                next_row, next_col = cur_row + drow, cur_col + dcol
                if next_row < 0 or next_row >= len(rooms) or next_col < 0 or next_col >= len(rooms[0]):
                    continue
                if rooms[next_row][next_col] == -1 or rooms[next_row][next_col] == 0:
                    continue
                if (next_row, next_col) in visited:
                    continue
                visited.add((next_row, next_col))
                que.put((next_row, next_col, next_step))

# V2 
# Time:  O(m * n)
# Space: O(g)
from collections import deque
class Solution(object):
    def wallsAndGates(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: void Do not return anything, modify rooms in-place instead.
        """
        INF = 2147483647
        q = deque([(i, j) for i, row in enumerate(rooms) for j, r in enumerate(row) if not r])
        while q:
            (i, j) = q.popleft()
            for I, J in (i+1, j), (i-1, j), (i, j+1), (i, j-1):
                if 0 <= I < len(rooms) and 0 <= J < len(rooms[0]) and \
                   rooms[I][J] == INF:
                    rooms[I][J] = rooms[i][j] + 1
                    q.append((I, J))