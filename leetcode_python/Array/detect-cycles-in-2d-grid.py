"""

1559. Detect Cycles in 2D Grid
Hard

Given a 2D array of characters grid of size m x n, you need to find if there exists any cycle consisting of the same value in grid.

A cycle is a path of length 4 or more in the grid that starts and ends at the same cell. From a given cell, you can move to one of the cells adjacent to it - in one of the four directions (up, down, left, or right), if it has the same value of the current cell.

Also, you cannot move to the cell that you visited in your last move. For example, the cycle (1, 1) -> (1, 2) -> (1, 1) is invalid because from (1, 2) we visited (1, 1) which was the last visited cell.

Return true if any cycle of the same value exists in grid, otherwise, return false.

Example 1:

Input: grid = [["a","a","a","a"],["a","b","b","a"],["a","b","b","a"],["a","a","a","a"]]
Output: true
Explanation: There are two valid cycles shown in different colors in the image below:

Example 2:

Input: grid = [["c","c","c","a"],["c","d","c","c"],["c","c","e","c"],["f","c","c","c"]]
Output: true
Explanation: There is only one valid cycle highlighted in the image below:

Example 3:

Input: grid = [["a","b","b"],["b","z","b"],["b","b","a"]]
Output: false

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 500
grid consists only of lowercase English letters.

"""

# V0
# IDEA : UNION FIND (an edge joining two already-connected cells closes a cycle)
#
#   build the graph whose edges join 4-adjacent cells holding the SAME
#   letter. scan every cell and only look right / down so each edge is
#   offered exactly once.
#   NOTE : in an undirected graph an edge whose two endpoints already
#          share a root creates a cycle -> and in a grid such a cycle is
#          automatically of length >= 4, which is what the problem wants.
#
# time = O(m * n * alpha), space = O(m * n)
class Solution(object):
    def containsCycle(self, grid):
        m, n = len(grid), len(grid[0])
        par = list(range(m * n))

        def find(x):
            while par[x] != x:
                par[x] = par[par[x]]
                x = par[x]
            return x

        for i in range(m):
            for j in range(n):
                for di, dj in ((0, 1), (1, 0)):
                    x, y = i + di, j + dj
                    if x < m and y < n and grid[x][y] == grid[i][j]:
                        ra, rb = find(i * n + j), find(x * n + y)
                        if ra == rb:
                            return True
                        par[ra] = rb
        return False


# V0-1
# IDEA : ITERATIVE DFS + PARENT TRACKING
#
#   flood fill every same-letter component with an explicit stack, pushing
#   (cell, the cell we came from). if a neighbour is already visited and it is
#   NOT the cell we came from, we have reached it by a second route -> cycle.
#
#   NOTE : the "came from" test is exactly the problem's "you cannot move back
#          to the cell of your last move" rule; and a grid graph is bipartite
#          so it has no triangles -> every cycle it contains is already of
#          length >= 4.
#
#   NOTE : written iteratively on purpose -- a 500 x 500 single-letter grid is
#          250_000 cells deep and would blow python's recursion limit.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def containsCycle(self, grid):
        m, n = len(grid), len(grid[0])
        seen = [[False] * n for _ in range(m)]
        for si in range(m):
            for sj in range(n):
                if seen[si][sj]:
                    continue
                val = grid[si][sj]
                seen[si][sj] = True
                stack = [(si, sj, -1, -1)]
                while stack:
                    i, j, pi, pj = stack.pop()
                    for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                        x, y = i + di, j + dj
                        if not (0 <= x < m and 0 <= y < n):
                            continue
                        if grid[x][y] != val or (x == pi and y == pj):
                            continue
                        if seen[x][y]:
                            return True
                        seen[x][y] = True
                        stack.append((x, y, i, j))
        return False


# V0-2
# IDEA : FLOOD FILL + EULER COUNT (a connected component is a tree iff E == V - 1)
#
#   BFS each same-letter component and count its vertices V and its edges E
#   (each edge counted exactly once, by letting every cell report only its
#   right / down same-letter neighbours).
#   a connected graph always has E >= V - 1, with equality only for a tree,
#   so E >= V proves the component holds a cycle -- no parent bookkeeping and
#   no union-find needed.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def containsCycle(self, grid):
        import collections
        m, n = len(grid), len(grid[0])
        seen = [[False] * n for _ in range(m)]
        for si in range(m):
            for sj in range(n):
                if seen[si][sj]:
                    continue
                val = grid[si][sj]
                seen[si][sj] = True
                q = collections.deque([(si, sj)])
                verts = edges = 0
                while q:
                    i, j = q.popleft()
                    verts += 1
                    for di, dj in ((0, 1), (1, 0)):
                        x, y = i + di, j + dj
                        if x < m and y < n and grid[x][y] == val:
                            edges += 1
                    for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                        x, y = i + di, j + dj
                        if 0 <= x < m and 0 <= y < n and not seen[x][y] \
                                and grid[x][y] == val:
                            seen[x][y] = True
                            q.append((x, y))
                if edges >= verts:
                    return True
        return False
