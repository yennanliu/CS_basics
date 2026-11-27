"""

1091. Shortest Path in Binary Matrix
Medium

Given an n x n binary matrix grid, return the length of the shortest clear path in
the matrix. If there is no clear path, return -1.

A clear path in a binary matrix is a path from the top-left cell (i.e., (0, 0)) to
the bottom-right cell (i.e., (n - 1, n - 1)) such that:

- All the visited cells of the path are 0.
- All the adjacent cells of the path are 8-directionally connected (i.e., they are
  different and they share an edge or a corner).

The length of a clear path is the number of visited cells of this path.

Example 1:

Input: grid = [[0,1],[1,0]]

Output: 2

Example 2:

Input: grid = [[0,0,0],[1,1,0],[1,1,0]]

Output: 4

Example 3:

Input: grid = [[1,0,0],[1,1,0],[1,1,0]]

Output: -1

Constraints:

n == grid.length

n == grid[i].length

1 <= n <= 100

grid[i][j] is 0 or 1

"""

# V0
# IDEA : BFS ON THE GRID (8 DIRECTIONS, LEVEL BY LEVEL = PATH LENGTH)
#
#   Every move costs the same (one more cell), so BFS reaches each cell first
#   along a shortest path: when (n-1, n-1) is popped, its distance is the answer.
#
#   - a cell is a node; an edge joins two 0-cells that share a side OR a corner
#   - the path length counts CELLS, so the start cell has length 1, not 0
#   - if the start or the end is blocked, no path can exist -> -1 at once
#   - mark visited when a cell is PUSHED, so no cell enters the queue twice
#
#   e.g. [[0,0,0],[1,1,0],[1,1,0]]
#        (0,0):1 -> (0,1):2 -> (0,2):3 / (1,2):3 -> (2,2):4  -> 4
#
# time = O(n^2), space = O(n^2)
from collections import deque
import heapq


class Solution(object):
    def shortestPathBinaryMatrix(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        n = len(grid)
        # edge case: blocked start or blocked end -> no clear path
        if grid[0][0] == 1 or grid[n - 1][n - 1] == 1:
            return -1
        # edge case: a 1 x 1 open grid is a path of one cell
        if n == 1:
            return 1

        DIRECTIONS = [(-1, -1), (-1, 0), (-1, 1),
                      (0, -1),           (0, 1),
                      (1, -1),  (1, 0),  (1, 1)]

        # separate visited grid, so the input is not mutated
        visited = [[False] * n for _ in range(n)]
        visited[0][0] = True
        # queue holds (row, col, length of the path that ends at this cell)
        queue = deque([(0, 0, 1)])

        while queue:
            r, c, length = queue.popleft()
            for dr, dc in DIRECTIONS:
                nr = r + dr
                nc = c + dc
                if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 0 and not visited[nr][nc]:
                    # NOTE !!! check the target on push: the first time it is
                    #          reached is already the shortest (BFS order)
                    if nr == n - 1 and nc == n - 1:
                        return length + 1
                    visited[nr][nc] = True
                    queue.append((nr, nc, length + 1))

        # the whole reachable region was explored without meeting the target
        return -1


# V1
# IDEA : A* SEARCH (BFS GUIDED BY A DISTANCE ESTIMATE)
#
#   Same graph, but the frontier is a min-heap ordered by
#       f = cells walked so far + estimate of cells still needed.
#   With 8-directional unit moves, the fewest extra moves to the target is the
#   Chebyshev distance max(|dr|, |dc|). It never overestimates (admissible) and
#   never drops by more than 1 per move (consistent), so the first time the
#   target is popped its length is optimal, exactly as in Dijkstra.
#
#   Worst case is no better than V0 (plus a log factor), but on open grids it
#   expands far fewer cells. A good follow-up answer; V0 is the one to write first.
#
# time = O(n^2 * log n), space = O(n^2)
class Solution2(object):
    def shortestPathBinaryMatrix(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        n = len(grid)
        if grid[0][0] == 1 or grid[n - 1][n - 1] == 1:
            return -1

        DIRECTIONS = [(-1, -1), (-1, 0), (-1, 1),
                      (0, -1),           (0, 1),
                      (1, -1),  (1, 0),  (1, 1)]

        def estimate(r, c):
            """Fewest moves left to (n-1, n-1) with diagonal steps allowed."""
            return max(n - 1 - r, n - 1 - c)

        # best_length[r][c] = shortest path length found so far to (r, c)
        INF = float('inf')
        best_length = [[INF] * n for _ in range(n)]
        best_length[0][0] = 1
        # heap holds (estimated total length, length so far, row, col)
        heap = [(1 + estimate(0, 0), 1, 0, 0)]

        while heap:
            _, length, r, c = heapq.heappop(heap)
            if r == n - 1 and c == n - 1:
                return length
            # a stale entry: this cell was already reached by a shorter path
            if length > best_length[r][c]:
                continue
            for dr, dc in DIRECTIONS:
                nr = r + dr
                nc = c + dc
                if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 0:
                    new_length = length + 1
                    if new_length < best_length[nr][nc]:
                        best_length[nr][nc] = new_length
                        heapq.heappush(heap, (new_length + estimate(nr, nc), new_length, nr, nc))

        return -1
