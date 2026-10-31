# https://leetcode.ca/2016-09-11-286-Walls-and-Gates/

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
# IDEA: BFS
# time = O(m*n), m,n = grid dimensions; multi-source BFS visits each cell at most once
# space = O(m*n)
from collections import deque

class Solution:
    def wallsAndGates(self, rooms):
        if not rooms or not rooms[0]:
            return

        l = len(rooms)
        w = len(rooms[0])

        q = deque()

        # Add all gates to queue
        for y in range(l):
            for x in range(w):
                if rooms[y][x] == 0:
                    q.append((x, y, 0))

        dirs = [(0, 1), (0, -1), (-1, 0), (1, 0)]

        while q:
            x, y, moves = q.popleft()

            for dx, dy in dirs:
                x_ = x + dx
                y_ = y + dy

                if 0 <= x_ < w and 0 <= y_ < l:

                    # If we found a shorter path
                    if rooms[y_][x_] > moves + 1:
                        rooms[y_][x_] = moves + 1
                        q.append((x_, y_, moves + 1))

# V0-0-1
# IDEA: MULTI SOURCE BFS (gemini)
from collections import deque

class Solution:

  def wallsAndGates(self, rooms):
    """:type rooms: List[List[int]]

    :rtype: None Do not return anything, modify rooms in-place instead.
    """
    if not rooms or not rooms[0]:
      return

    l = len(rooms)
    w = len(rooms[0])

    # 必須使用 deque 才能支援高效的 popleft()
    q = deque()

    # 1. 將所有閘門（值為 0）加入佇列作為多源 BFS 的起點
    for y in range(l):
      for x in range(w):
        if rooms[y][x] == 0:
          q.append((x, y, 0))  # 儲存 (x, y, 當前距離)

    # 四個移動方向：右、左、下、上
    moves = [[0, 1], [0, -1], [1, 0], [-1, 0]]

    # 2. 執行 BFS
    while q:
      x, y, dist = q.popleft()

      for dx, dy in moves:
        x_ = x + dx
        y_ = y + dy

        """
        NOTE !!!

        -> we DON'T really need `visited` for this LC.

        -> 

            可以省略獨立的 visited 集合：

            在多源 BFS 中，我們不需要額外維護 visited 集合。
            因為空房間初始時的數值是無限大（通常為 2147483647 或 INF），
            我們只要檢查該格子是否為未走訪過的空房間（
            即 rooms[y_][x_] == 2147483647），
            一旦走訪過就直接更新它的數值，
            這樣就能自然防止重複訪問並保證最短路徑。

        """
        # 檢查是否在網格範圍內，且該格子是未造訪過的空房間（通常以 2147483647 代表無限大 INF）
        if 0 <= x_ < w and 0 <= y_ < l and rooms[y_][x_] == 2147483647:
          # 更新距離為到門的最短步數
          rooms[y_][x_] = dist + 1
          # 將新房間推入佇列繼續向外擴散
          q.append((x_, y_, dist + 1))


# V0-0-2
# IDEA: MULTI SOURCE BFS (GPT)
from collections import deque

class Solution:
    def wallsAndGates(self, rooms):
        """
        :type rooms: List[List[int]]
        :rtype: None
        """
        # Edge case
        if not rooms or not rooms[0]:
            return

        l = len(rooms)
        w = len(rooms[0])

        # Queue: [x, y, dist]
        q = deque()

        # Visited coordinates
        visited = set()

        # Put all gates into the queue
        for y in range(l):
            for x in range(w):
                if rooms[y][x] == 0:
                    q.append([x, y, 0])
                    visited.add((x, y))

        # Four directions
        moves = [
            [1, 0],
            [-1, 0],
            [0, 1],
            [0, -1]
        ]

        # Multi-source BFS
        while q:
            x, y, dist = q.popleft()

            for dx, dy in moves:
                x_ = x + dx
                y_ = y + dy

                if 0 <= x_ < w and 0 <= y_ < l:
                    # Only visit INF rooms
                    if rooms[y_][x_] == 2147483647 and (x_, y_) not in visited:
                        rooms[y_][x_] = dist + 1

                        visited.add((x_, y_))
                        q.append([x_, y_, dist + 1])



# V0-1
# IDEA : BFS
# time = O(m*n), m,n = grid dimensions; q grows with gates found so far, each cell relaxed only when a shorter distance is found
# space = O(m*n)
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


# V0-2
# IDEA: BFS
# time = O(m*n), m,n = grid dimensions; multi-source BFS visits each cell at most once (guarded by the INF check)
# space = O(m*n)
from collections import deque

class Solution:
    def wallsAndGates(self, rooms):
        if not rooms or not rooms[0]:
            return

        rows, cols = len(rooms), len(rooms[0])
        q = deque()

        for r in range(rows):
            for c in range(cols):
                if rooms[r][c] == 0:
                    q.append((r, c))

        dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)]

        while q:
            r, c = q.popleft()

            for dr, dc in dirs:
                nr, nc = r + dr, c + dc

                if (
                    0 <= nr < rows and
                    0 <= nc < cols and
                    rooms[nr][nc] == 2147483647
                ):
                    rooms[nr][nc] = rooms[r][c] + 1
                    q.append((nr, nc))


# V0-3
# IDEA : BFS
# time = O(m*n), m,n = grid dimensions; multi-source BFS visits each cell at most once (guarded by the INF check)
# space = O(m*n)
from collections import deque

class Solution:
    def wallsAndGates(self, rooms: list[list[int]]) -> None:
        """
        Do not return anything, modify rooms in-place instead.
        """
        if not rooms or not rooms[0]:
            return
            
        l = len(rooms)
        w = len(rooms[0])
        
        # We can push coordinates directly into our queue to save memory
        q = deque()
        
        # Step 1: Find all gates (0) and add them to the queue as starting points
        for y in range(l):
            for x in range(w):
                if rooms[y][x] == 0:
                    q.append((x, y))
                    
        dirs = [[0, 1], [0, -1], [-1, 0], [1, 0]]
        
        # Step 2: Multi-source BFS
        while q:
            # CRITICAL FIX: Use len(q) and .popleft() for Python deques
            x, y = q.popleft()
            
            for d in dirs:
                x_ = x + d[0]
                y_ = y + d[1] # CRITICAL FIX: Fixed assignment typo
                
                # Boundary check
                if 0 <= x_ < w and 0 <= y_ < l:
                    # CRITICAL FIX: Only step into unvisited empty rooms (INF = 2147483647).
                    # This eliminates the need for expensive min() calculations!
                    if rooms[y_][x_] == 2147483647:
                        # The new room's score is exactly the parent room's score + 1
                        rooms[y_][x_] = rooms[y][x] + 1
                        q.append((x_, y_))


# V0-4
# IDEA : DFS  -> TLE (time out error)
# time = O((m*n)^2), m,n = grid dimensions; a cell can be revisited/updated many times as `dist` improves via different DFS paths (no visited-once guarantee)
# space = O(m*n), recursion stack can reach O(m*n) depth
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
# time = O((m*n)^2), m,n = grid dimensions; a cell can be revisited/updated many times as `dist` improves via different DFS paths (no visited-once guarantee)
# space = O(m*n), recursion stack can reach O(m*n) depth
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
# time = O(m*n), m,n = grid dimensions; q grows with gates found so far, each cell relaxed only when a shorter distance is found
# space = O(m*n)
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
# time = O(g*m*n), m,n = grid dimensions, g = number of gates; a fresh BFS (with its own visited set) is run from each gate
# space = O(m*n)
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
# time = O(m*n), m,n = grid dimensions; multi-source BFS visits each cell at most once
# space = O(g), g = number of gates (initial queue size; grid mutated in place)
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