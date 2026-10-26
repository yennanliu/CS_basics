"""

2290. Minimum Obstacle Removal to Reach Corner
Hard

You are given a 0-indexed 2D integer array grid of size m x n. Each cell has
one of two values:

0 represents an empty cell,
1 represents an obstacle that may be removed.

You can move up, down, left, or right from and to an empty cell.

Return the minimum number of obstacles to remove so you can move from the upper
left corner (0, 0) to the lower right corner (m - 1, n - 1).

Example 1:

Input: grid = [[0,1,1],[1,1,0],[1,1,0]]

Output: 2

Explanation:

We can remove the obstacles at (0, 1) and (0, 2) to create a path from (0, 0)
to (2, 2). It can be shown that we need to remove at least 2 obstacles, so we
return 2. Note that there may be other ways to remove 2 obstacles to create a
path.

Example 2:

Input: grid = [[0,1,0,0,0],[0,1,0,1,0],[0,0,0,1,0]]

Output: 0

Explanation:

We can move from (0, 0) to (2, 4) without removing any obstacles, so we
return 0.

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 10^5
2 <= m * n <= 10^5
grid[i][j] is either 0 or 1.
grid[0][0] == grid[m - 1][n - 1] == 0

"""

# V0
# IDEA : DIJKSTRA (MIN-HEAP ON "OBSTACLES REMOVED SO FAR")
#
#   Treat every cell as a node and every 4-directional move as an edge whose
#   weight is the value of the cell we step INTO (1 = must remove an obstacle,
#   0 = free). The answer is then the shortest weighted path (0,0) -> (m-1,n-1).
#
#   costs[r][c] keeps the cheapest removal count found so far for each cell.
#   The heap always pops the cheapest frontier cell, so the first time the
#   target is popped its cost is final (all weights are >= 0).
#
#   e.g. grid = [[0,1,1],[1,1,0],[1,1,0]]
#        (0,0)->(0,1) cost 1 ->(0,2) cost 2 ->(1,2) cost 2 ->(2,2) cost 2 -> 2
#
# time = O(m * n * log(m * n)), space = O(m * n)
import heapq
from collections import deque


class Solution(object):
    def minimumObstacles(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # edge case: empty grid
        if not grid or not grid[0]:
            return 0

        rows = len(grid)
        cols = len(grid[0])
        moves = [(0, 1), (0, -1), (1, 0), (-1, 0)]

        # cheapest number of removals known so far to reach each cell
        costs = [[float('inf')] * cols for _ in range(rows)]
        costs[0][0] = 0

        # min-heap of (removals so far, row, col)
        heap = [(0, 0, 0)]

        while heap:
            cost, r, c = heapq.heappop(heap)

            # first pop of the target is its shortest distance
            if r == rows - 1 and c == cols - 1:
                return cost

            # NOTE !!! skip stale heap entries: a cheaper path to (r, c)
            #          was already processed after this one was pushed
            if cost > costs[r][c]:
                continue

            for dr, dc in moves:
                nr = r + dr
                nc = c + dc
                if nr < 0 or nr >= rows or nc < 0 or nc >= cols:
                    continue

                # stepping into an obstacle costs one removal
                new_cost = cost + grid[nr][nc]

                # only keep the path if it improves this neighbour
                if new_cost < costs[nr][nc]:
                    costs[nr][nc] = new_cost
                    heapq.heappush(heap, (new_cost, nr, nc))

        # unreachable for valid input: any cell can be reached by removing walls
        return -1


# V1
# IDEA : 0-1 BFS (DEQUE: 0-WEIGHT EDGES TO THE FRONT, 1-WEIGHT TO THE BACK)
#
#   Every edge weight is 0 or 1, so a heap is more than we need. Keep a deque
#   whose costs never differ by more than 1 from front to back:
#     - moving into an empty cell (weight 0) keeps the same cost -> appendleft
#     - moving into an obstacle   (weight 1) costs one more      -> append
#   Popping from the front therefore always yields the cheapest cell, exactly
#   like Dijkstra's heap, but each push / pop is O(1).
#
#   NOTE : the optimal bound; derive V0 first in an interview, then mention
#          this as the follow-up when the weights are only 0 / 1.
#
# time = O(m * n), space = O(m * n)
class Solution2(object):
    def minimumObstacles(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # edge case: empty grid
        if not grid or not grid[0]:
            return 0

        rows = len(grid)
        cols = len(grid[0])
        moves = [(0, 1), (0, -1), (1, 0), (-1, 0)]

        # cheapest number of removals known so far to reach each cell
        costs = [[float('inf')] * cols for _ in range(rows)]
        costs[0][0] = 0

        # deque of (row, col); costs along it are non-decreasing
        queue = deque([(0, 0)])

        while queue:
            r, c = queue.popleft()

            for dr, dc in moves:
                nr = r + dr
                nc = c + dc
                if nr < 0 or nr >= rows or nc < 0 or nc >= cols:
                    continue

                weight = grid[nr][nc]
                new_cost = costs[r][c] + weight

                if new_cost < costs[nr][nc]:
                    costs[nr][nc] = new_cost
                    # NOTE !!! free move goes to the front so it is processed
                    #          before any cell that needed one more removal
                    if weight == 0:
                        queue.appendleft((nr, nc))
                    else:
                        queue.append((nr, nc))

        return costs[rows - 1][cols - 1]
