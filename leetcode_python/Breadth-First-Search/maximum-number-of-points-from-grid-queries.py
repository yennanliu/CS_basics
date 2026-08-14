"""

2503. Maximum Number of Points From Grid Queries
Hard

You are given an m x n integer matrix grid and an array queries of size k.

Find an array answer of size k such that for each integer queries[i] you start in the top left cell of the matrix and repeat the following process:

If queries[i] is strictly greater than the value of the current cell that you are in, then you get one point if it is your first time visiting this cell, and you can move to any adjacent cell in all 4 directions: up, down, left, and right.
Otherwise, you do not get any points, and you end this process.

After the process, answer[i] is the maximum number of points you can get. Note that for each query you are allowed to visit the same cell multiple times.

Return the resulting array answer.


Example 1:

Input: grid = [[1,2,3],[2,5,7],[3,5,1]], queries = [5,6,2]
Output: [5,8,1]
Explanation: The diagrams above show which cells we visit to get points for each query.

Example 2:

Input: grid = [[5,2,1],[1,1,2]], queries = [3]
Output: [0]
Explanation: We can not get any points because the value of the top left cell is already greater than or equal to 3.


Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 1000
4 <= m * n <= 10^5
k == queries.length
1 <= k <= 10^4
1 <= grid[i][j], queries[i] <= 10^6

"""

# V0
# IDEA : OFFLINE QUERIES (SORT) + BEST-FIRST BFS WITH A MIN HEAP
#
#   for a single query q the score is "size of the 4-connected region of
#   cells < q that contains (0,0)". regions only GROW as q grows, so sort
#   the queries ascending and expand one shared frontier incrementally.
#
#   the frontier is a min heap keyed by cell value (a Dijkstra-flavoured
#   BFS): pop the cheapest reachable cell; while its value < q it is
#   unlocked -> count it and push its 4 neighbours. stop as soon as the heap
#   top is >= q, since nothing cheaper is reachable any more.
#
#   NOTE : queries must be answered in their ORIGINAL order, so carry the
#          index along when sorting.
#   NOTE : mark a cell visited when it is PUSHED, not when popped, otherwise
#          it can enter the heap many times.
#   NOTE : the comparison is STRICT (< q); a cell equal to q blocks.
#
# time = O(m*n*log(m*n) + k*log k), space = O(m*n)
import heapq
class Solution(object):
    def maxPoints(self, grid, queries):
        m, n = len(grid), len(grid[0])
        order = sorted(range(len(queries)), key=lambda i: queries[i])

        res = [0] * len(queries)
        visited = [[False] * n for _ in range(m)]
        visited[0][0] = True
        heap = [(grid[0][0], 0, 0)]
        cnt = 0

        for qi in order:
            q = queries[qi]
            while heap and heap[0][0] < q:
                _, x, y = heapq.heappop(heap)
                cnt += 1
                for dx, dy in ((-1, 0), (1, 0), (0, -1), (0, 1)):
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < m and 0 <= ny < n and not visited[nx][ny]:
                        visited[nx][ny] = True
                        heapq.heappush(heap, (grid[nx][ny], nx, ny))
            res[qi] = cnt
        return res
