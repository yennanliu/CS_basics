# https://leetcode.ca/all/505.html

"""

[LeetCode] 505. The Maze II

# https://www.cnblogs.com/grandyang/p/6725380.html
 

There is a ball in a maze with empty spaces and walls. The ball can go through empty spaces by rolling up, down, left or right, but it won't stop rolling until hitting a wall. When the ball stops, it could choose the next direction.

Given the ball's start position, the destination and the maze, find the shortest distance for the ball to stop at the destination. The distance is defined by the number of empty spaces traveled by the ball from the start position (excluded) to the destination (included). If the ball cannot stop at the destination, return -1.

The maze is represented by a binary 2D array. 1 means the wall and 0 means the empty space. You may assume that the borders of the maze are all walls. The start and destination coordinates are represented by row and column indexes.

Example 1

Input 1: a maze represented by a 2D array

0 0 1 0 0
0 0 0 0 0
0 0 0 1 0
1 1 0 1 1
0 0 0 0 0

Input 2: start coordinate (rowStart, colStart) = (0, 4)
Input 3: destination coordinate (rowDest, colDest) = (4, 4)

Output: 12
Explanation: One shortest way is : left -> down -> left -> down -> right -> down -> right.
             The total distance is 1 + 1 + 3 + 1 + 2 + 2 + 2 = 12.


 

Example 2

Input 1: a maze represented by a 2D array

0 0 1 0 0
0 0 0 0 0
0 0 0 1 0
1 1 0 1 1
0 0 0 0 0

Input 2: start coordinate (rowStart, colStart) = (0, 4)
Input 3: destination coordinate (rowDest, colDest) = (3, 2)

Output: -1
Explanation: There is no way for the ball to stop at the destination.


 

Note:

There is only one ball and one destination in the maze.
Both the ball and the destination exist on an empty space, and they will not be at the same position initially.
The given maze does not contain border (like the red rectangle in the example pictures), but you could assume the border of the maze are all walls.
The maze contains at least 2 empty spaces, and both the width and height of the maze won't exceed 100.
 

"""

"""
NOTE !!!


he Maze II (LC 505) introduces a physics mechanic: 

the ball does not stop moving step-by-step; 
it must roll in a continuous direction until 
it physically collides with a wall.  

Because a path can take a long, 
winding loop but still reach a cell
faster than a shorter looking path that got stopped early,
standard BFS step-by-step tracking fails. 

-> Instead, you need Dijkstra's Algorithm 
via a Min-Heap (Priority Queue) 
to always process the globally shortest path first.




-> 

Therefore you need `Dijkstra's` algorithm, not BFS.



->

What LC 505 actually does

The ball doesn't move one cell.

It rolls until hitting a wall.

"""



# V0
# IDEA: Dijkstra + `dist` matrix (GPT)
# TODO: validate
# time = O(m*n*log(m*n)*max(m,n)), m,n = maze dimensions; each heap pop/push is O(log(mn)), and each of the 4 directions rolls up to O(max(m,n)) cells
# space = O(m*n)
import heapq

class Solution:
    def shortestDistance(self, maze, start, destination):
        m, n = len(maze), len(maze[0])

        directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]

        # NOTE !!!
        dist = [[float("inf")] * n for _ in range(m)]
        dist[start[0]][start[1]] = 0

        pq = [(0, start[0], start[1])]  # (distance, row, col)

        while pq:
            cur_dist, x, y = heapq.heappop(pq)

            if [x, y] == destination:
                return cur_dist

            """
            # NOTE !!!

            if `cur_dist > dist[x][y]`
                -> NOT possible to form a better route
                -> NO need to proceed
            """
            if cur_dist > dist[x][y]:
                continue

            for dx, dy in directions:
                nx, ny = x, y
                # NOTE !!!
                # since the ball can `keep moving`
                # -> we need to init a steps = 0
                #     -> so can calculate `how far` 
                #        we move within the `while` loop below
                steps = 0

                """
                NOTE !!!!

                since the ball can `keep moving`
                here we need `while` loop, instead of `if`
                """
                while (
                    0 <= nx + dx < m
                    and 0 <= ny + dy < n
                    and maze[nx + dx][ny + dy] == 0
                ):
                    nx += dx
                    ny += dy
                    steps += 1

                new_dist = cur_dist + steps

                if new_dist < dist[nx][ny]:
                    dist[nx][ny] = new_dist
                    heapq.heappush(pq, (new_dist, nx, ny))

        return -1

# V0-1
# IDEA: Dijkstra (GPT)
# TODO: validate
# time = O(m*n*log(m*n)*max(m,n)), m,n = maze dimensions; each heap pop/push is O(log(mn)), and each of the 4 directions rolls up to O(max(m,n)) cells
# space = O(m*n)
import heapq

class Solution:
    def shortestDistance(self, maze, start, destination):

        m = len(maze)
        n = len(maze[0])

        dist = [[float("inf")] * n for _ in range(m)]

        sx, sy = start
        dx, dy = destination

        dist[sx][sy] = 0

        pq = [(0, sx, sy)]

        directions = [
            (1, 0),
            (-1, 0),
            (0, 1),
            (0, -1)
        ]

        while pq:
            cur_dist, x, y = heapq.heappop(pq)

            if (x, y) == (dx, dy):
                return cur_dist

            if cur_dist > dist[x][y]:
                continue

            for dx_dir, dy_dir in directions:

                nx = x
                ny = y
                steps = 0


                """
                NOTE !!!!

                since the ball can `keep moving`
                here we need `while` loop, instead of `if`
                """
                while (
                    0 <= nx + dx_dir < m and
                    0 <= ny + dy_dir < n and
                    maze[nx + dx_dir][ny + dy_dir] == 0
                ):
                    nx += dx_dir
                    ny += dy_dir
                    steps += 1

                new_dist = cur_dist + steps

                """
                # NOTE !!! below
                """
                if new_dist < dist[nx][ny]:

                    # update dist with smaller one
                    dist[nx][ny] = new_dist

                    # push (insert) the smaller dist and its x, y to PQ
                    heapq.heappush(
                        pq,
                        (new_dist, nx, ny)
                    )

        return -1


# V0-2
# IDEA: Dijkstra (GEMINI)
# TODO: validate
# time = O(m*n*log(m*n)*max(m,n)), m,n = maze dimensions; each heap pop/push is O(log(mn)), and each of the 4 directions rolls up to O(max(m,n)) cells
# space = O(m*n)
import heapq

class Solution(object):
    def shortestDistance(self, maze, start, destination):
        """
        :type maze: List[List[int]]
        :type start: List[int]
        :type destination: List[int]
        :rtype: int
        """
        rows = len(maze)
        cols = len(maze[0])
        
        # Matrix to store the minimum distance required to STOP at maze[r][c]
        distance = [[float('inf')] * cols for _ in range(rows)]
        distance[start[0]][start[1]] = 0
        
        # Priority Queue (Min-Heap): stores elements as (dist_traveled, current_r, current_c)
        pq = [(0, start[0], start[1])]
        
        # Direction shifting offsets: Up, Down, Left, Right
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        
        while pq:
            curr_dist, r, c = heapq.heappop(pq)
            
            # Optimization: If we found a longer path to an already well-optimized spot, skip it
            if curr_dist > distance[r][c]:
                continue
                
            # If the globally shortest path popped from the heap reaches the target, we are done!
            if r == destination[0] and c == destination[1]:
                return curr_dist
                
            for dr, dc in directions:
                new_r, new_c = r, c
                steps = 0
                
                # CRITICAL FIX: The ball rolls continuously until it strikes a wall (1) or a border
                while 0 <= new_r + dr < rows and 0 <= new_c + dc < cols and maze[new_r + dr][new_c + dc] == 0:
                    new_r += dr
                    new_c += dc
                    steps += 1
                
                # Evaluate if this landing spot provides a shorter path than previously recorded
                if distance[r][c] + steps < distance[new_r][new_c]:
                    distance[new_r][new_c] = distance[r][c] + steps
                    heapq.heappush(pq, (distance[new_r][new_c], new_r, new_c))
                    
        return -1


# V1
# https://leetcode.ca/2017-04-18-505-The-Maze-II/
# IDEA: BFS
# time = O(m*n*max(m,n)), m,n = maze dimensions; each cell can be re-enqueued when a shorter dist is found, each expansion rolls up to O(max(m,n)) cells
# space = O(m*n)
class Solution:
    def shortestDistance(
        self, maze: List[List[int]], start: List[int], destination: List[int]
    ) -> int:
        m, n = len(maze), len(maze[0])
        dirs = (-1, 0, 1, 0, -1)
        si, sj = start
        di, dj = destination
        q = deque([(si, sj)])
        dist = [[inf] * n for _ in range(m)]
        dist[si][sj] = 0
        while q:
            i, j = q.popleft()
            for a, b in pairwise(dirs):
                x, y, k = i, j, dist[i][j]
                while 0 <= x + a < m and 0 <= y + b < n and maze[x + a][y + b] == 0:
                    x, y, k = x + a, y + b, k + 1
                if k < dist[x][y]:
                    dist[x][y] = k
                    q.append((x, y))
        return -1 if dist[di][dj] == inf else dist[di][dj]



# V1
# https://www.jiuzhang.com/solution/the-maze-ii/#tag-other-lang-python
# time = O(m*n*max(m,n)), m,n = maze dimensions; each cell visited once via `visited`, each expansion rolls up to O(max(m,n)) cells
# space = O(m*n)
from collections import deque
class Solution:
    def shortestDistance(self, maze, start, destination):
        queue = deque([(start[0], start[1], 0)])
        visited = {(start[0], start[1])}
        directions = [(1, 0), (0, 1), (-1, 0), (0, -1)]
        minDist = -1
        while queue:
            size = len(queue)
            for _ in range(size):
                x, y, front = queue.popleft()
                if x == destination[0] and y == destination[1]:
                    minDist = front if minDist == -1 else min(minDist, front)

                for dx, dy in directions:
                    nextX, nextY = x + dx, y + dy
                    count = 0
                    while self.isValid(maze, nextX, nextY):
                        nextX += dx
                        nextY += dy
                        count += 1
                    nextX -= dx
                    nextY -= dy
                    if (nextX, nextY) not in visited:
                        queue.append((nextX, nextY, front + count))
                        visited.add((nextX, nextY))
        return minDist

    def isValid(self, maze, x, y):
        rows, cols = len(maze), len(maze[0])
        return 0 <= x < rows and 0 <= y < cols and maze[x][y] == 0

# V1'
# http://bookshadow.com/weblog/2017/01/29/leetcode-the-maze-ii/
# IDEA : BFS
# time = O(m*n), m,n = maze dimensions; O(m*n) to precompute dmap stop-positions in each direction, then O(m*n) BFS over precomputed stops
# space = O(m*n)
import collections
class Solution(object):
    def findShortestWay(self, maze, ball, hole):
        """
        :type maze: List[List[int]]
        :type ball: List[int]
        :type hole: List[int]
        :rtype: str
        """
        ball, hole = tuple(ball), tuple(hole)
        dmap = collections.defaultdict(lambda: collections.defaultdict(int))
        w, h = len(maze), len(maze[0])
        for dir in 'dlru': dmap[hole][dir] = hole
        for x in range(w):
            for y in range(h):
                if maze[x][y] or (x, y) == hole: continue
                dmap[(x, y)]['u'] = dmap[(x - 1, y)]['u'] if x > 0 and dmap[(x - 1, y)]['u'] else (x, y)
                dmap[(x, y)]['l'] = dmap[(x, y - 1)]['l'] if y > 0 and dmap[(x, y - 1)]['l'] else (x, y)
        for x in range(w - 1, -1, -1):
            for y in range(h - 1, -1, -1):
                if maze[x][y] or (x, y) == hole: continue
                dmap[(x, y)]['d'] = dmap[(x + 1, y)]['d'] if x < w - 1 and dmap[(x + 1, y)]['d'] else (x, y)
                dmap[(x, y)]['r'] = dmap[(x, y + 1)]['r'] if y < h - 1 and dmap[(x, y + 1)]['r'] else (x, y)
        bmap = {ball : (0, '')}
        distance = lambda pa, pb: abs(pa[0] - pb[0]) + abs(pa[1] - pb[1])
        queue = collections.deque([(ball, 0, '')])
        while queue:
            front, dist, path = queue.popleft()
            for dir in 'dlru':
                if dir not in dmap[front]: continue
                np = dmap[front][dir]
                ndist = dist + distance(front, np)
                npath = path + dir
                if np not in bmap or (ndist, npath) < bmap[np]:
                    bmap[np] = (ndist, npath)
                    queue.append((np, ndist, npath))
        return bmap[hole][1] if hole in bmap else 'impossible'

# V1''
# https://leetcode.com/articles/the-maze-ii/?page=4
# IDEA : DFS
# JAVA 
# public class Solution {
#     public int shortestDistance(int[][] maze, int[] start, int[] dest) {
#         int[][] distance = new int[maze.length][maze[0].length];
#         for (int[] row: distance)
#             Arrays.fill(row, Integer.MAX_VALUE);
#         distance[start[0]][start[1]] = 0;
#         dfs(maze, start, distance);
#         return distance[dest[0]][dest[1]] == Integer.MAX_VALUE ? -1 : distance[dest[0]][dest[1]];
#     }
#
#     public void dfs(int[][] maze, int[] start, int[][] distance) {
#         int[][] dirs={{0,1}, {0,-1}, {-1,0}, {1,0}};
#         for (int[] dir: dirs) {
#             int x = start[0] + dir[0];
#             int y = start[1] + dir[1];
#             int count = 0;
#             while (x >= 0 && y >= 0 && x < maze.length && y < maze[0].length && maze[x][y] == 0) {
#                 x += dir[0];
#                 y += dir[1];
#                 count++;
#             }
#             if (distance[start[0]][start[1]] + count < distance[x - dir[0]][y - dir[1]]) {
#                 distance[x - dir[0]][y - dir[1]] = distance[start[0]][start[1]] + count;
#                 dfs(maze, new int[]{x - dir[0],y - dir[1]}, distance);
#             }
#         }
#     }
# }

# V1'''
# http://bookshadow.com/weblog/2017/01/29/leetcode-the-maze-ii/
# IDEA : Dijkstra ALGORITHM
# time = O((m*n)^2), m,n = maze dimensions; `min(bmap.values())` scans up to O(m*n) entries on each of O(m*n) iterations (no heap used)
# space = O(m*n)
import collections
class Solution(object):
    def findShortestWay(self, maze, ball, hole):
        """
        :type maze: List[List[int]]
        :type ball: List[int]
        :type hole: List[int]
        :rtype: str
        """
        ball, hole = tuple(ball), tuple(hole)
        dmap = collections.defaultdict(lambda: collections.defaultdict(int))
        w, h = len(maze), len(maze[0])
        for dir in 'dlru': dmap[hole][dir] = hole
        for x in range(w):
            for y in range(h):
                if maze[x][y] or (x, y) == hole: continue
                dmap[(x, y)]['u'] = dmap[(x - 1, y)]['u'] if x > 0 and dmap[(x - 1, y)]['u'] else (x, y)
                dmap[(x, y)]['l'] = dmap[(x, y - 1)]['l'] if y > 0 and dmap[(x, y - 1)]['l'] else (x, y)
        for x in range(w - 1, -1, -1):
            for y in range(h - 1, -1, -1):
                if maze[x][y] or (x, y) == hole: continue
                dmap[(x, y)]['d'] = dmap[(x + 1, y)]['d'] if x < w - 1 and dmap[(x + 1, y)]['d'] else (x, y)
                dmap[(x, y)]['r'] = dmap[(x, y + 1)]['r'] if y < h - 1 and dmap[(x, y + 1)]['r'] else (x, y)
        bmap = {ball : (0, '', ball)}
        vset = set()
        distance = lambda pa, pb: abs(pa[0] - pb[0]) + abs(pa[1] - pb[1])
        while bmap:
            dist, path, p = min(bmap.values())
            if p == hole: return path
            del bmap[p]
            vset.add(p)
            for dir in 'dlru':
                if dir not in dmap[p]: continue
                np = dmap[p][dir]
                ndist = dist + distance(p, np)
                npath = path + dir
                if np not in vset and (np not in bmap or (ndist, npath, np) < bmap[np]):
                    bmap[np] = (ndist, npath, np)
        return 'impossible'
        
# V1''''
# https://www.jiuzhang.com/solution/the-maze-ii/#tag-other-lang-python
# IDEA : BFS
# time = O(m*n*max(m,n)), m,n = maze dimensions; each cell marked visited via maze[x][y]=2 (visited once), each expansion rolls up to O(max(m,n)) cells
# space = O(m*n)
import collections
def shortestDistance(self, maze, start, destination):
        # write your code here
        D=[[-1,0],[1,0],[0,1],[0,-1]]
        n,m=len(maze),len(maze[0])
        q=collections.deque([[start[0],start[1],0]])     
        result=float('inf')
        while q:
            x,y,l=q.popleft()
            maze[x][y]=2
            if x==destination[0] and y==destination[1]:
                result=min(l,result)

            tmp=l
            for i,j in D:
                row=x+i
                col=y+j
                l+=1
                while 0<=row<n and 0<=col<m and maze[row][col]!=1:
                    row+=i
                    col+=j
                    l+=1
                
                row-=i
                col-=j
                l-=1
                if maze[row][col]==0:
                    q.append([row,col,l])
                
                l=tmp

        if result==float('inf'):
            return -1
        return result

# V1'''''
# IDEA : DFS
# JAVA
# class Solution {
# public:
#     vector<vector<int>> dirs{{0,-1},{-1,0},{0,1},{1,0}};
#     int shortestDistance(vector<vector<int>>& maze, vector<int>& start, vector<int>& destination) {
#         int m = maze.size(), n = maze[0].size();
#         vector<vector<int>> dists(m, vector<int>(n, INT_MAX));
#         dists[start[0]][start[1]] = 0;
#         helper(maze, start[0], start[1], destination, dists);
#         int res = dists[destination[0]][destination[1]];
#         return (res == INT_MAX) ? -1 : res;
#     }
#     void helper(vector<vector<int>>& maze, int i, int j, vector<int>& destination, vector<vector<int>>& dists) {
#         if (i == destination[0] && j == destination[1]) return;
#         int m = maze.size(), n = maze[0].size();
#         for (auto d : dirs) {
#             int x = i, y = j, dist = dists[x][y];
#             while (x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0) {
#                 x += d[0];
#                 y += d[1];
#                 ++dist;
#             }
#             x -= d[0];
#             y -= d[1];
#             --dist;
#             if (dists[x][y] > dist) {
#                 dists[x][y] = dist;
#                 helper(maze, x, y, destination, dists);
#             }
#         }
#     }
# };

# V2
# time = O(max(r, c) * w * log(w)), r,c = maze dimensions, w = r*c (number of cells); each heap pop/push is O(log(w)), neighbor generation rolls up to O(max(r,c))
# space = O(w)
import heapq
class Solution(object):
    def shortestDistance(self, maze, start, destination):
        """
        :type maze: List[List[int]]
        :type start: List[int]
        :type destination: List[int]
        :rtype: int
        """
        start, destination = tuple(start), tuple(destination)

        def neighbors(maze, node):
            for dir in [(-1, 0), (0, 1), (0, -1), (1, 0)]:
                cur_node, dist = list(node), 0
                while 0 <= cur_node[0]+dir[0] < len(maze) and \
                      0 <= cur_node[1]+dir[1] < len(maze[0]) and \
                      not maze[cur_node[0]+dir[0]][cur_node[1]+dir[1]]:
                    cur_node[0] += dir[0]
                    cur_node[1] += dir[1]
                    dist += 1
                yield dist, tuple(cur_node)

        heap = [(0, start)]
        visited = set()
        while heap:
            dist, node = heapq.heappop(heap)
            if node in visited: continue
            if node == destination:
                return dist
            visited.add(node)
            for neighbor_dist, neighbor in neighbors(maze, node):
                heapq.heappush(heap, (dist+neighbor_dist, neighbor))

        return -1
