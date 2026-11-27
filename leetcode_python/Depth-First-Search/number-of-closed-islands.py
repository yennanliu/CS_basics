"""

1254. Number of Closed Islands
Medium

Given a 2D grid consists of 0s (land) and 1s (water). An island is a maximal
4-directionally connected group of 0s and a closed island is an island totally
(all left, top, right, bottom) surrounded by 1s.

Return the number of closed islands.

Example 1:

Input: grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]

Output: 2

Explanation:

Islands in gray are closed because they are completely surrounded by water
(group of 1s).

Example 2:

Input: grid = [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]

Output: 1

Example 3:

Input: grid = [[1,1,1,1,1,1,1],
               [1,0,0,0,0,0,1],
               [1,0,1,1,1,0,1],
               [1,0,1,0,1,0,1],
               [1,0,1,1,1,0,1],
               [1,0,0,0,0,0,1],
               [1,1,1,1,1,1,1]]

Output: 2

Constraints:

1 <= grid.length, grid[0].length <= 100

0 <= grid[i][j] <=1

"""

# V0
# IDEA : 2 PASS DFS (FLOOD THE BORDER ISLANDS FIRST, THEN COUNT WHAT IS LEFT)
#
#   An island is NOT closed exactly when one of its cells sits on the border:
#   the grid edge is not water, so that side is open.
#
#   - pass 1: DFS from every border land cell and sink its whole island (-> 1)
#   - pass 2: every land cell still left belongs to an island that never
#             touches the border -> each new DFS start is one closed island
#
#   e.g. [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]
#        pass 1 sinks the big island around the edge; the 0 at (1,2) is left
#        -> 1 closed island
#
# time = O(m * n), space = O(m * n)
import sys
from collections import deque

# a 100 x 100 all-land grid recurses up to 10^4 deep, past Python's default 1000
sys.setrecursionlimit(20000)

LAND = 0
WATER = 1
DIRECTIONS = [(-1, 0), (1, 0), (0, -1), (0, 1)]


class Solution(object):
    def closedIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # edge case: empty grid has no island
        if not grid or not grid[0]:
            return 0

        rows = len(grid)
        cols = len(grid[0])
        # work on a copy, so sinking islands does not mutate the caller's grid
        board = [row[:] for row in grid]

        # pass 1: sink every island that touches the border (it is not closed)
        for r in range(rows):
            for c in range(cols):
                on_border = r == 0 or r == rows - 1 or c == 0 or c == cols - 1
                if on_border and board[r][c] == LAND:
                    self._sink(board, r, c)

        # pass 2: whatever land survived is enclosed by water on every side
        closed_count = 0
        for r in range(rows):
            for c in range(cols):
                if board[r][c] == LAND:
                    closed_count += 1
                    # sink it so its other cells are not counted again
                    self._sink(board, r, c)

        return closed_count

    def _sink(self, board, r, c):
        """Turn the whole island containing (r, c) into water."""
        # NOTE !!! mark BEFORE recursing, or two neighbours revisit each other forever
        board[r][c] = WATER
        for dr, dc in DIRECTIONS:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < len(board) and 0 <= nc < len(board[0]) and board[nr][nc] == LAND:
                self._sink(board, nr, nc)


# V1
# IDEA : 1 PASS DFS (EACH DFS REPORTS WHETHER ITS ISLAND REACHED THE BORDER)
#
#   Start a DFS at each unvisited land cell. The island is closed iff NO cell of
#   it is on the border. The DFS must still visit the whole island even after it
#   sees a border cell, otherwise the unvisited remainder would be counted later
#   as a separate (wrongly "closed") island.
#
#   e.g. island cells (0,1),(1,1): (0,1) is on the border -> not closed, but
#        (1,1) still gets marked visited in the same DFS
#
# time = O(m * n), space = O(m * n)
class Solution2(object):
    def closedIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0

        rows = len(grid)
        cols = len(grid[0])
        visited = [[False] * cols for _ in range(rows)]

        closed_count = 0
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == LAND and not visited[r][c]:
                    if self._is_closed(grid, visited, r, c):
                        closed_count += 1

        return closed_count

    def _is_closed(self, grid, visited, r, c):
        """Visit the whole island at (r, c); return True if it never touches the border."""
        rows = len(grid)
        cols = len(grid[0])
        visited[r][c] = True

        # a border cell means one side of this island faces the open edge
        closed = not (r == 0 or r == rows - 1 or c == 0 or c == cols - 1)

        for dr, dc in DIRECTIONS:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == LAND and not visited[nr][nc]:
                # NOTE !!! recurse FIRST, then combine: `closed and self._is_closed(...)`
                #          would short-circuit and leave part of the island unvisited
                neighbour_closed = self._is_closed(grid, visited, nr, nc)
                closed = closed and neighbour_closed

        return closed


# V2
# IDEA : BFS PER ISLAND (SAME CHECK AS V1, NO RECURSION)
#
#   Same rule as V1 — an island is closed iff none of its cells is on the
#   border — but each island is explored with a queue, so there is no
#   recursion-depth limit to worry about on a large all-land grid.
#
# time = O(m * n), space = O(m * n)
class Solution3(object):
    def closedIsland(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        if not grid or not grid[0]:
            return 0

        rows = len(grid)
        cols = len(grid[0])
        visited = [[False] * cols for _ in range(rows)]

        closed_count = 0
        for start_r in range(rows):
            for start_c in range(cols):
                if grid[start_r][start_c] != LAND or visited[start_r][start_c]:
                    continue

                # explore this whole island, remembering if any cell is on the border
                touches_border = False
                visited[start_r][start_c] = True
                queue = deque([(start_r, start_c)])
                while queue:
                    r, c = queue.popleft()
                    if r == 0 or r == rows - 1 or c == 0 or c == cols - 1:
                        touches_border = True
                    for dr, dc in DIRECTIONS:
                        nr = r + dr
                        nc = c + dc
                        if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == LAND and not visited[nr][nc]:
                            # mark on push, so no cell enters the queue twice
                            visited[nr][nc] = True
                            queue.append((nr, nc))

                if not touches_border:
                    closed_count += 1

        return closed_count
