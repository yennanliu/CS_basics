"""

[LeetCode] 1730. Shortest Path to Get Food

# https://www.cnblogs.com/cnoodle/p/15645191.html

You are starving and you want to eat food as quickly as possible. You want to find the shortest path to arrive at any food cell.

You are given an m x n character matrix, grid, of these different types of cells:

'*' is your location. There is exactly one '*' cell.
'#' is a food cell. There may be multiple food cells.
'O' is free space, and you can travel through these cells.
'X' is an obstacle, and you cannot travel through these cells.
You can travel to any adjacent cell north, east, south, or west of your current location if there is not an obstacle.

Return the length of the shortest path for you to reach any food cell. If there is no path for you to reach food, return -1.

Example 1:



Input: grid = [["X","X","X","X","X","X"],["X","*","O","O","O","X"],["X","O","O","#","O","X"],["X","X","X","X","X","X"]]
Output: 3
Explanation: It takes 3 steps to reach the food.
Example 2:



Input: grid = [["X","X","X","X","X"],["X","*","X","O","X"],["X","O","X","#","X"],["X","X","X","X","X"]]
Output: -1
Explanation: It is not possible to reach the food.
Example 3:



Input: grid = [["X","X","X","X","X","X","X","X"],["X","*","O","X","O","#","O","X"],["X","O","O","X","O","O","X","X"],["X","O","O","O","O","#","O","X"],["X","X","X","X","X","X","X","X"]]
Output: 6
Explanation: There can be multiple food cells. It only takes 6 steps to reach the bottom food.
Example 4:

Input: grid = [["O","*"],["#","O"]]
Output: 2
Example 5:

Input: grid = [["X","*"],["#","X"]]
Output: -1
Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 200
grid[row][col] is '*', 'X', 'O', or '#'.
The grid contains exactly one '*'.

"""

# V0
# IDEA : BFS
class Solution:
    def getFood(self, grid):
        # edge case
        if not grid:
            return 0
        l = len(grid)
        w = len(grid[0])
        #initx = inity = -1
        
        # step 1)
        # find (initx, inity) ("*" location)
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "*":
                    initx = i
                    inity = j
                    break

        dirt = [(1,0),(0,1),(-1,0),(0,-1)]
        q = []
        
        # step 2) start from (initx, inity)
        q.append((initx, inity))
        step = 1
        seen = set()
        seen.add((initx,inity))
        # step 3) bfs
        while q:
            size = len(q)
            #print(q)
            for _ in range(size):
                x,y = q.pop(0)
                
                for dx,dy in dirt:
                    newx = x + dx
                    newy = y + dy
                    if 0 <= newx < l and 0 <= newy < w and (newx, newy) not in seen:
                        if grid[newx][newy] == '#':
                            return step
                        elif grid[newx][newy] == 'O':
                            q.append((newx, newy))
                            seen.add((newx,newy))
                        elif grid[newx][newy] == 'X':
                            seen.add((newx,newy))                          
            step += 1
        return -1

# V1
# https://blog.csdn.net/sinat_30403031/article/details/117352902
# https://blog.csdn.net/Changxing_J/article/details/117224537
# https://www.xknote.com/blog/559651.html
# IDEA : BFS
class Solution:
    def getFood(self, grid):
        Row = len(grid)
        Col = len(grid[0])
        initx = inity = -1
        for i in range(Row):
            for j in range(Col):
                if grid[i][j] == "*":
                    initx = i
                    inity = j
                    break
        dirt = [(1,0),(0,1),(-1,0),(0,-1)]
        que = collections.deque()
        que.append((initx, inity))
        step = 1
        seen = set()
        seen.add((initx,inity))
        while que:
            size = len(que)
            #print(que)
            for _ in range(size):
                x,y = que.popleft()
                
                for dx,dy in dirt:
                    newx = x + dx
                    newy = y + dy
                    if 0 <= newx < Row and 0 <= newy < Col and (newx, newy) not in seen:
                        if grid[newx][newy] == '#':
                            return step
                        elif grid[newx][newy] == 'O':
                            que.append((newx, newy))
                            seen.add((newx,newy))
                        elif grid[newx][newy] == 'X':
                            seen.add((newx,newy))                          
            step += 1
        return -1

# V1'
# IDEA : BFS
# JAVA
# https://www.cnblogs.com/cnoodle/p/15645191.html
# class Solution {
#     int[][] DIRS = { { 0, -1 }, { 0, 1 }, { 1, 0 }, { -1, 0 } };
#
#     public int getFood(char[][] grid) {
#         int m = grid.length;
#         int n = grid[0].length;
#         Queue<int[]> queue = new LinkedList<>();
#         for (int i = 0; i < m; i++) {
#             for (int j = 0; j < n; j++) {
#                 if (grid[i][j] == '*') {
#                     queue.offer(new int[] { i, j });
#                     break;
#                 }
#             }
#         }
#         boolean[][] visited = new boolean[m][n];
#
#         int step = 0;
#         while (!queue.isEmpty()) {
#             int size = queue.size();
#             for (int i = 0; i < size; i++) {
#                 int[] cur = queue.poll();
#                 int x = cur[0];
#                 int y = cur[1];
#                 if (grid[x][y] == '#') {
#                     return step;
#                 }
#                 for (int[] dir : DIRS) {
#                     int r = x + dir[0];
#                     int c = y + dir[1];
#                     if (r >= 0 && r < m && c >= 0 && c < n && grid[r][c] != 'X' && !visited[r][c]) {
#                         visited[r][c] = true;
#                         queue.offer(new int[] { r, c });
#                     }
#                 }
#             }
#             step++;
#         }
#         return -1;
#     }
# }

# V2
# Time:  O(m * n)
# Space: O(m + n)
class Solution(object):
    def getFood(self, grid):
        """
        :type grid: List[List[str]]
        :rtype: int
        """
        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

        q = []
        for r in xrange(len(grid)):
            for c in xrange(len(grid[0])):
                if grid[r][c] == '*':
                    q.append((r, c))
                    break
        
        result = 0
        while q:
            result += 1
            new_q = []
            for r, c in q:
                for dr, dc in directions:
                    nr, nc = r+dr, c+dc
                    if not (0 <= nr < len(grid) and
                            0 <= nc < len(grid[0]) and
                            grid[nr][nc] != 'X'):
                        continue
                    if grid[nr][nc] == '#':
                        return result
                    grid[nr][nc] = 'X'
                    new_q.append((nr, nc))
            q = new_q 
        return -1