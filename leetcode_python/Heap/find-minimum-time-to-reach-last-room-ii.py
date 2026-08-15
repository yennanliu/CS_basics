"""

3342. Find Minimum Time to Reach Last Room II
Medium

There is a dungeon with n x m rooms arranged as a grid.

You are given a 2D array moveTime of size n x m, where moveTime[i][j] represents the minimum time in seconds when you can start moving to that room. You start from the room (0, 0) at time t = 0 and can move to an adjacent room. Moving between adjacent rooms takes one second for one move and two seconds for the next, alternating between the two.

Return the minimum time to reach the room (n - 1, m - 1).

Two rooms are adjacent if they share a common wall, either horizontally or vertically.


Example 1:

Input: moveTime = [[0,4],[4,4]]
Output: 7
Explanation:
The minimum time required is 7 seconds.
At time t == 4, move from room (0, 0) to room (1, 0) in one second.
At time t == 5, move from room (1, 0) to room (1, 1) in two seconds.

Example 2:

Input: moveTime = [[0,0,0,0],[0,0,0,0]]
Output: 6
Explanation:
The minimum time required is 6 seconds.
At time t == 0, move from room (0, 0) to room (1, 0) in one second.
At time t == 1, move from room (1, 0) to room (1, 1) in two seconds.
At time t == 3, move from room (1, 1) to room (1, 2) in one second.
At time t == 4, move from room (1, 2) to room (1, 3) in two seconds.

Example 3:

Input: moveTime = [[0,1],[1,2]]
Output: 4


Constraints:

2 <= n == moveTime.length <= 750
2 <= m == moveTime[i].length <= 750
0 <= moveTime[i][j] <= 10^9

"""

# V0
# IDEA : SAME DIJKSTRA, BUT THE STEP COST FOLLOWS THE CHESSBOARD PARITY
#
#   each move flips the parity of i + j, so after k moves the walker always
#   stands on a cell with (i + j) % 2 == k % 2. the costs alternate 1, 2,
#   1, 2, ... starting at 1, which means the move LEAVING a cell costs
#
#       1 when (i + j) is even, 2 when it is odd
#
#   — no extra state dimension is needed, the parity is baked into the cell.
#
#   the rest is LC 3341 : entering a room cannot happen before its moveTime,
#   so  arrival = max(now, moveTime[x][y]) + cost.
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
            cost = 1 if (i + j) % 2 == 0 else 2
            for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < m:
                    nt = max(t, moveTime[x][y]) + cost
                    if nt < best[x][y]:
                        best[x][y] = nt
                        heapq.heappush(pq, (nt, x, y))
        return best[n - 1][m - 1]
