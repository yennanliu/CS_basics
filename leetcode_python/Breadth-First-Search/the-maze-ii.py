# V0 

# V1
# https://www.jiuzhang.com/solution/the-maze-ii/#tag-other-lang-python
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
# Time:  O(max(r, c) * wlogw)
# Space: O(w)
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
