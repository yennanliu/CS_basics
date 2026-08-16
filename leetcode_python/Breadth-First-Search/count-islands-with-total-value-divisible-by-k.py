"""

3619. Count Islands With Total Value Divisible by K
Medium

You are given an m x n matrix grid and a positive integer k. An island is a
group of positive integers (representing land) that are 4-directionally
connected (horizontally or vertically).

The total value of an island is the sum of the values of all cells in the
island.

Return the number of islands with a total value divisible by k.


Example 1:

Input: grid = [[0,2,1,0,0],[0,5,0,0,5],[0,0,1,0,0],[0,1,4,7,0],[0,2,0,0,8]],
k = 5
Output: 2
Explanation:
The grid contains four islands. The islands highlighted in blue have a total
value that is divisible by 5, while the islands highlighted in red do not.

Example 2:

Input: grid = [[3,0,3,0], [0,3,0,3], [3,0,3,0]], k = 3
Output: 6
Explanation:
The grid contains six islands, each with a total value that is divisible by 3.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 1000
1 <= m * n <= 10^5
0 <= grid[i][j] <= 10^6
1 <= k <= 10^6

"""

# V0
# IDEA : BFS FLOOD FILL, SUMMING EACH COMPONENT AS IT IS CONSUMED
#
#   an island is just a connected component of the "positive value" cells
#   under 4-directional adjacency, so one flood fill per unvisited land cell
#   enumerates every island exactly once — the fill consumes the whole
#   component, so no later scan position can restart the same island.
#
#   the total value can be accumulated during the very same traversal, since
#   every cell is dequeued exactly once. marking a cell as visited at the
#   moment it is *enqueued* (rather than dequeued) is what keeps a cell from
#   being pushed twice by two different neighbours.
#
#   bfs rather than recursion matters here: a single snake-shaped island may
#   cover all 10^5 cells, which would blow python's recursion limit.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def countIslands(self, grid, k):
        m, n = len(grid), len(grid[0])
        seen = [[False] * n for _ in range(m)]
        res = 0

        for si in range(m):
            for sj in range(n):
                if grid[si][sj] <= 0 or seen[si][sj]:
                    continue
                seen[si][sj] = True
                queue = [(si, sj)]
                total = 0
                while queue:
                    nxt = []
                    for i, j in queue:
                        total += grid[i][j]
                        for x, y in ((i - 1, j), (i + 1, j), (i, j - 1), (i, j + 1)):
                            if 0 <= x < m and 0 <= y < n:
                                if grid[x][y] > 0 and not seen[x][y]:
                                    seen[x][y] = True
                                    nxt.append((x, y))
                    queue = nxt
                if total % k == 0:
                    res += 1
        return res
