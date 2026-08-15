"""

3341. Find Minimum Time to Reach Last Room I
Medium

There is a dungeon with n x m rooms arranged as a grid.

You are given a 2D array moveTime of size n x m, where moveTime[i][j] represents the minimum time in seconds when you can start moving to that room. You start from the room (0, 0) at time t = 0 and can move to an adjacent room. Moving between adjacent rooms takes exactly one second.

Return the minimum time to reach the room (n - 1, m - 1).

Two rooms are adjacent if they share a common wall, either horizontally or vertically.


Example 1:

Input: moveTime = [[0,4],[4,4]]
Output: 6
Explanation:
The minimum time required is 6 seconds.
At time t == 4, move from room (0, 0) to room (1, 0) in one second.
At time t == 5, move from room (1, 0) to room (1, 1) in one second.

Example 2:

Input: moveTime = [[0,0,0],[0,0,0]]
Output: 3
Explanation:
The minimum time required is 3 seconds.
At time t == 0, move from room (0, 0) to room (1, 0) in one second.
At time t == 1, move from room (1, 0) to room (1, 1) in one second.
At time t == 2, move from room (1, 1) to room (1, 2) in one second.

Example 3:

Input: moveTime = [[0,1],[1,2]]
Output: 3


Constraints:

2 <= n == moveTime.length <= 50
2 <= m == moveTime[i].length <= 50
0 <= moveTime[i][j] <= 10^9

"""

# V0
# IDEA : DIJKSTRA WHERE THE EDGE COST DEPENDS ON THE ARRIVAL TIME
#
#   the earliest one can ENTER a room is max(now, moveTime[room]) — waiting
#   is free — and the move itself costs one more second :
#
#       arrival = max(current time, moveTime[nx][ny]) + 1
#
#   that value never decreases along a path, so the usual Dijkstra argument
#   holds : the first time a room is popped from the priority queue its time
#   is final.
#
# time = O(n * m log(n * m)), space = O(n * m)
import heapq


class Solution(object):
    def minTimeToReach(self, moveTime):
        n, m = len(moveTime), len(moveTime[0])
        INF = float('inf')
        best = [[INF] * m for _ in range(n)]
        best[0][0] = 0
        pq = [(0, 0, 0)]

        while pq:
            t, i, j = heapq.heappop(pq)
            if t > best[i][j]:
                continue
            if i == n - 1 and j == m - 1:
                return t
            for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < m:
                    nt = max(t, moveTime[x][y]) + 1
                    if nt < best[x][y]:
                        best[x][y] = nt
                        heapq.heappush(pq, (nt, x, y))
        return best[n - 1][m - 1]
