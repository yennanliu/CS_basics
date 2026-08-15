"""

2812. Find the Safest Path in a Grid
Medium

You are given a 0-indexed 2D matrix grid of size n x n, where (r, c) represents:

A cell containing a thief if grid[r][c] = 1
An empty cell if grid[r][c] = 0

You are initially positioned at cell (0, 0). In one move, you can move to any adjacent cell in the grid, including cells containing thieves.

The safeness factor of a path on the grid is defined as the minimum manhattan distance from any cell in the path to any thief in the grid.

Return the maximum safeness factor of all paths leading to cell (n - 1, n - 1).

An adjacent cell of cell (r, c), is one of the cells (r, c + 1), (r, c - 1), (r + 1, c) and (r - 1, c) if it exists.

The Manhattan distance between two cells (a, b) and (x, y) is equal to |a - x| + |b - y|, where |val| denotes the absolute value of val.


Example 1:

Input: grid = [[1,0,0],[0,0,0],[0,0,1]]
Output: 0
Explanation: All paths from (0, 0) to (n - 1, n - 1) go through the thieves in cells (0, 0) and (n - 1, n - 1).

Example 2:

Input: grid = [[0,0,1],[0,0,0],[0,0,0]]
Output: 2
Explanation: The path depicted in the picture above has a safeness factor of 2 since:
- The closest cell of the path to the thief at cell (0, 2) is cell (0, 0). The distance between them is | 0 - 0 | + | 0 - 2 | = 2.
It can be shown that there are no other paths with a higher safeness factor.

Example 3:

Input: grid = [[0,0,0,1],[0,0,0,0],[0,0,0,0],[1,0,0,0]]
Output: 2
Explanation: The path depicted in the picture above has a safeness factor of 2 since:
- The closest cell of the path to the thief at cell (0, 3) is cell (1, 2). The distance between them is | 0 - 1 | + | 3 - 2 | = 2.
- The closest cell of the path to the thief at cell (3, 0) is cell (3, 2). The distance between them is | 3 - 3 | + | 0 - 2 | = 2.
It can be shown that there are no other paths with a higher safeness factor.


Constraints:

1 <= grid.length == n <= 400
grid[i].length == n
grid[i][j] is either 0 or 1.
There is at least one thief in the grid.

"""

# V0
# IDEA : MULTI-SOURCE BFS + MAX-MIN (WIDEST PATH) DIJKSTRA
#
#   Step 1 : multi-source BFS starting from EVERY thief at once gives
#            dist[r][c] = manhattan distance from (r, c) to the nearest thief.
#            (On a 4-neighbour grid with no obstacles, BFS layers == manhattan
#             distance, so no extra work is needed.)
#
#   Step 2 : we now want the path (0,0) -> (n-1,n-1) whose MINIMUM dist value
#            is as large as possible - the classic "widest path" / bottleneck
#            shortest path problem. Run a Dijkstra variant with a max-heap
#            where the cost of a path is min(cost so far, dist[next]) and we
#            always expand the currently best (largest) bottleneck.
#            The first time we pop the destination, its bottleneck is optimal.
#
#   NOTE : the endpoints count too - if (0,0) or (n-1,n-1) holds a thief its
#          dist is 0 and the answer is 0. Seeding the heap with dist[0][0]
#          handles that for free.
#
#   NOTE : `best` doubles as the visited marker; a cell is only pushed when we
#          strictly improve its bottleneck, so each cell is finalised once.
#
# time = O(n^2 * log n), space = O(n^2)
import heapq
from collections import deque


class Solution(object):
    def maximumSafenessFactor(self, grid):
        n = len(grid)
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        # --- step 1 : multi-source BFS from all thieves ---
        dist = [[-1] * n for _ in range(n)]
        q = deque()
        for i in range(n):
            for j in range(n):
                if grid[i][j] == 1:
                    dist[i][j] = 0
                    q.append((i, j))
        while q:
            i, j = q.popleft()
            d = dist[i][j] + 1
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < n and dist[x][y] == -1:
                    dist[x][y] = d
                    q.append((x, y))

        # --- step 2 : bottleneck (max-min) Dijkstra with a max-heap ---
        best = [[-1] * n for _ in range(n)]
        best[0][0] = dist[0][0]
        heap = [(-dist[0][0], 0, 0)]
        while heap:
            negd, i, j = heapq.heappop(heap)
            d = -negd
            if d < best[i][j]:
                continue
            if i == n - 1 and j == n - 1:
                return d
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < n and 0 <= y < n:
                    nd = d if d < dist[x][y] else dist[x][y]
                    if nd > best[x][y]:
                        best[x][y] = nd
                        heapq.heappush(heap, (-nd, x, y))
        return 0
