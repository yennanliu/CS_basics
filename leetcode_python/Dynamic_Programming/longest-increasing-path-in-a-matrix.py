"""

329. Longest Increasing Path in a Matrix
Hard

Given an m x n integers matrix, return the length of the longest increasing
path in matrix.

From each cell, you can either move in four directions: left, right, up, or
down. You may not move diagonally or move outside the boundary (i.e.,
wrap-around is not allowed).


Example 1:

Input: matrix = [[9,9,4],[6,6,8],[2,1,1]]
Output: 4
Explanation: The longest increasing path is [1, 2, 6, 9].

Example 2:

Input: matrix = [[3,4,5],[3,2,6],[2,2,1]]
Output: 4
Explanation: The longest increasing path is [3, 4, 5, 6]. Moving diagonally
is not allowed.

Example 3:

Input: matrix = [[1]]
Output: 1


Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 200
0 <= matrix[i][j] <= 2^31 - 1

"""

# V0
# IDEA : DFS + MEMOIZATION (2D DP, TOP-DOWN)
#
#   longest[r][c] = length of the longest increasing path STARTING at (r, c)
#       longest[r][c] = 1 + max(longest[nr][nc]) over the 4 neighbours with
#                           matrix[nr][nc] > matrix[r][c]   (or 1 if none)
#
#   why no visited set is needed: a path only ever moves to a STRICTLY
#   larger value, so it can never return to a cell -> the "graph" is a DAG,
#   and every cell's answer is fixed once computed -> memoize it.
#
#   brute force (DFS from every cell with no memo) re-walks the same paths
#   and is exponential; the memo makes each cell computed exactly once.
#
#   e.g. [[9,9,4],[6,6,8],[2,1,1]] : longest from 1 (row 2) = 1 -> 2 -> 6 -> 9 = 4
#
# time = O(m * n), space = O(m * n)
import sys
from collections import deque

DIRECTIONS = ((1, 0), (-1, 0), (0, 1), (0, -1))


class Solution(object):
    def longestIncreasingPath(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: int
        """
        if not matrix or not matrix[0]:
            return 0

        rows = len(matrix)
        cols = len(matrix[0])

        # NOTE !!! a single increasing chain can visit all 200 * 200 cells,
        #          which is far past Python's default recursion limit (1000)
        sys.setrecursionlimit(max(sys.getrecursionlimit(), rows * cols + 100))

        # memo[r][c] == 0 means "not computed yet" (every real answer is >= 1)
        memo = [[0] * cols for _ in range(rows)]

        def dfs(r, c):
            """Length of the longest increasing path starting at (r, c)."""
            if memo[r][c] != 0:
                return memo[r][c]

            best = 1  # the path made of (r, c) alone
            for dr, dc in DIRECTIONS:
                nr = r + dr
                nc = c + dc
                inside = 0 <= nr < rows and 0 <= nc < cols
                # only step to a strictly larger neighbour
                if inside and matrix[nr][nc] > matrix[r][c]:
                    best = max(best, 1 + dfs(nr, nc))

            memo[r][c] = best
            return best

        answer = 0
        for r in range(rows):
            for c in range(cols):
                answer = max(answer, dfs(r, c))
        return answer


# V1
# IDEA : TOPOLOGICAL SORT (KAHN'S BFS), PEEL THE DAG LAYER BY LAYER
#
#   node = a cell, edge = small -> large between neighbours.
#   in_degree[r][c] = how many neighbours are strictly SMALLER than (r, c).
#
#   cells with in_degree 0 are local minima — every increasing path can start
#   at one. BFS removes one whole layer per round; a cell joins the queue once
#   all of its smaller neighbours are removed. The number of rounds equals
#   the number of cells on the longest path, so it IS the answer.
#
#   no recursion at all -> the safest version under Python's stack limit.
#
# time = O(m * n), space = O(m * n)
class Solution2(object):
    def longestIncreasingPath(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: int
        """
        if not matrix or not matrix[0]:
            return 0

        rows = len(matrix)
        cols = len(matrix[0])

        # count incoming edges: each strictly smaller neighbour points at us
        in_degree = [[0] * cols for _ in range(rows)]
        for r in range(rows):
            for c in range(cols):
                for dr, dc in DIRECTIONS:
                    nr = r + dr
                    nc = c + dc
                    if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] < matrix[r][c]:
                        in_degree[r][c] += 1

        # start from every local minimum
        queue = deque()
        for r in range(rows):
            for c in range(cols):
                if in_degree[r][c] == 0:
                    queue.append((r, c))

        layers = 0
        while queue:
            layers += 1
            # process exactly one layer per round
            for _ in range(len(queue)):
                r, c = queue.popleft()
                for dr, dc in DIRECTIONS:
                    nr = r + dr
                    nc = c + dc
                    if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] > matrix[r][c]:
                        in_degree[nr][nc] -= 1
                        # all its smaller neighbours are gone -> next layer
                        if in_degree[nr][nc] == 0:
                            queue.append((nr, nc))

        return layers


# V2
# IDEA : BOTTOM-UP DP, CELLS SWEPT IN ASCENDING VALUE ORDER
#
#   ending[r][c] = length of the longest increasing path ENDING at (r, c)
#       ending[r][c] = 1 + max(ending[nr][nc]) over neighbours with a SMALLER value
#
#   every dependency points at a smaller value, so sorting the cells by value
#   guarantees each neighbour we read is already final. Same idea as
#   LC 2328 (number-of-increasing-paths-in-a-grid.py), with max instead of sum.
#
#   NOTE : the extra log factor from sorting makes this the slowest of the
#          three; learn V0 first, use V1 when recursion depth is a concern.
#
# time = O(m * n * log(m * n)), space = O(m * n)
class Solution3(object):
    def longestIncreasingPath(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: int
        """
        if not matrix or not matrix[0]:
            return 0

        rows = len(matrix)
        cols = len(matrix[0])

        cells = []
        for r in range(rows):
            for c in range(cols):
                cells.append((matrix[r][c], r, c))
        cells.sort()

        ending = [[1] * cols for _ in range(rows)]
        answer = 1
        for value, r, c in cells:
            for dr, dc in DIRECTIONS:
                nr = r + dr
                nc = c + dc
                # a smaller neighbour was swept earlier, so its value is final
                if 0 <= nr < rows and 0 <= nc < cols and matrix[nr][nc] < value:
                    ending[r][c] = max(ending[r][c], ending[nr][nc] + 1)
            answer = max(answer, ending[r][c])

        return answer
