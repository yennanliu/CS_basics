"""

499. The Maze III
Hard

There is a ball in a maze with empty spaces (represented as 0) and walls (represented as 1).
The ball can go through the empty spaces by rolling up, down, left or right, but it won't stop
rolling until hitting a wall. When the ball stops, it could choose the next direction
(must be different from last chosen direction). There is also a hole in this maze.
The ball will drop into the hole if it rolls onto the hole.

Given the m x n maze, the ball's position ball and the hole's position hole,
where ball = [ballrow, ballcol] and hole = [holerow, holecol],
return a string instructions of all the instructions that the ball should follow to drop in the hole
with the shortest distance possible. If there are multiple valid instructions,
return the lexicographically minimum one. If the ball can't drop in the hole, return "impossible".

If there is a way for the ball to drop in the hole, the answer instructions should contain the
characters 'u' (i.e., up), 'd' (i.e., down), 'l' (i.e., left), and 'r' (i.e., right).

The distance is the number of empty spaces traveled by the ball from the start position (excluded)
to the destination (included).

You may assume that the borders of the maze are all walls (see examples).

Example 1:

Input: maze = [[0,0,0,0,0],[1,1,0,0,1],[0,0,0,0,0],[0,1,0,0,1],[0,1,0,0,0]], ball = [4,3], hole = [0,1]
Output: "lul"
Explanation: There are two shortest ways for the ball to drop into the hole.
The first way is left -> up -> left, represented by "lul".
The second way is up -> left, represented by 'ul'.
Both ways have shortest distance 6, but the first way is lexicographically smaller
because 'l' < 'u'. So the output is "lul".

Example 2:

Input: maze = [[0,0,0,0,0],[1,1,0,0,1],[0,0,0,0,0],[0,1,0,0,1],[0,1,0,0,0]], ball = [4,3], hole = [3,0]
Output: "impossible"
Explanation: The ball cannot reach the hole.

Example 3:

Input: maze = [[0,0,0,0,0,0,0],[0,0,1,0,0,1,0],[0,0,0,0,1,0,0],[0,0,0,0,0,0,1]], ball = [0,4], hole = [3,5]
Output: "dldr"


Constraints:

m == maze.length
n == maze[i].length
1 <= m, n <= 100
maze[i][j] is 0 or 1.
ball.length == 2
hole.length == 2
0 <= ballrow, holerow <= m
0 <= ballcol, holecol <= n
Both the ball and the hole exist in an empty space, and they will not be in the same position initially.
The maze contains at least 2 empty spaces.

"""

# V0
# IDEA : DIJKSTRA (BFS + priority queue)
#        -> a "move" = roll until a wall (or until falling into the hole)
#        -> heap key = (distance, path), so popping order is
#           shortest distance first, then lexicographically smallest path
#        -> pop the hole => that is the answer
#
#        NOTE : ordering by (dist, path) is consistent, since equal distance means
#               equal number of moves, so appending a char keeps the relative order.
# time = O(m * n * max(m, n) * log(m * n))
# space = O(m * n)
import heapq
class Solution(object):
    def findShortestWay(self, maze, ball, hole):
        m, n = len(maze), len(maze[0])
        hole_r, hole_c = hole[0], hole[1]

        # keep dirs in lexicographic order of their labels (not required, but tidy)
        dirs = [(1, 0, "d"), (0, -1, "l"), (0, 1, "r"), (-1, 0, "u")]

        pq = [(0, "", ball[0], ball[1])]
        visited = set()

        while pq:
            dist, path, r, c = heapq.heappop(pq)

            if (r, c) in visited:
                continue
            visited.add((r, c))

            if r == hole_r and c == hole_c:
                return path

            for dr, dc, ch in dirs:
                nr, nc, nd = r, c, dist
                # roll until hitting a wall / the border, or dropping into the hole
                while 0 <= nr + dr < m and 0 <= nc + dc < n and maze[nr + dr][nc + dc] == 0:
                    nr += dr
                    nc += dc
                    nd += 1
                    if nr == hole_r and nc == hole_c:
                        break

                # the ball must actually move
                if (nr, nc) != (r, c) and (nr, nc) not in visited:
                    heapq.heappush(pq, (nd, path + ch, nr, nc))

        return "impossible"
