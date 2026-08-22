"""

1267. Count Servers that Communicate
Medium

You are given a map of a server center, represented as a m * n integer matrix grid,
where 1 means that on that cell there is a server and 0 means that it is no server.
Two servers are said to communicate if they are on the same row or on the same column.

Return the number of servers that communicate with any other server.


Example 1:

Input: grid = [[1,0],[0,1]]
Output: 0
Explanation: No servers can communicate with others.

Example 2:

Input: grid = [[1,0],[1,1]]
Output: 3
Explanation: All three servers can communicate with at least one other server.

Example 3:

Input: grid = [[1,1,0,0],[0,0,1,0],[0,0,1,0],[0,0,0,1]]
Output: 4
Explanation: The two servers in the first row can communicate with each other. The two servers in the third column can communicate with each other. The server at right bottom corner can't communicate with any other server.


Constraints:

m == grid.length
n == grid[i].length
1 <= m <= 250
1 <= n <= 250
grid[i][j] == 0 or 1

"""

# V0
# IDEA : COUNTING (row count + col count)
#        a server talks to someone iff its row has >= 2 servers
#        OR its column has >= 2 servers
# time = O(m * n)
# space = O(m + n)
class Solution(object):
    def countServers(self, grid):
        m, n = len(grid), len(grid[0])
        rows = [0] * m
        cols = [0] * n
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    rows[i] += 1
                    cols[j] += 1

        res = 0
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1 and (rows[i] > 1 or cols[j] > 1):
                    res += 1
        return res


# V0-1
# IDEA : UNION-FIND OVER ROW NODES AND COLUMN NODES
#
#   make m + n DSU nodes, one per row and one per column; a server at (i, j)
#   glues row i to column j. servers that land in the same component form one
#   communicating cluster, so a server counts iff its component holds >= 2
#   servers.
#
#   NOTE : this is equivalent to the "row-mate or column-mate" rule — a
#          server alone in both its row and its column keeps its two nodes
#          isolated, so it is the only server in its component; and any
#          component with >= 2 servers necessarily gives each of them a mate.
#
# time = O(m * n), space = O(m + n)
class Solution(object):
    def countServers(self, grid):
        m, n = len(grid), len(grid[0])
        parent = list(range(m + n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]     # path halving
                x = parent[x]
            return x

        servers = []
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    servers.append(i)
                    ri, rj = find(i), find(m + j)
                    if ri != rj:
                        parent[ri] = rj

        size = {}
        for i in servers:
            root = find(i)
            size[root] = size.get(root, 0) + 1
        return sum(1 for i in servers if size[find(i)] > 1)


# V0-2
# IDEA : BFS OVER THE ROW/COLUMN BIPARTITE GRAPH
#
#   the same clustering as the DSU version, reached by traversal instead:
#   starting from an unvisited row, hop to every column that holds a server in
#   it, from each of those columns hop to every row that holds a server in it,
#   and repeat. one BFS sweeps up a whole cluster; count its servers (each
#   server is tallied exactly once, when its own row is dequeued) and add them
#   all if the cluster holds more than one.
#
# time = O(m * n), space = O(m + n)
from collections import deque
class Solution(object):
    def countServers(self, grid):
        m, n = len(grid), len(grid[0])
        seen_row = [False] * m
        seen_col = [False] * n
        res = 0

        for start in range(m):
            if seen_row[start] or 1 not in grid[start]:
                continue
            seen_row[start] = True
            q = deque([(0, start)])               # (0 = row node, 1 = col node)
            found = 0
            while q:
                kind, idx = q.popleft()
                if kind == 0:
                    for j in range(n):
                        if grid[idx][j] == 1:
                            found += 1
                            if not seen_col[j]:
                                seen_col[j] = True
                                q.append((1, j))
                else:
                    for i in range(m):
                        if grid[i][idx] == 1 and not seen_row[i]:
                            seen_row[i] = True
                            q.append((0, i))
            if found > 1:
                res += found
        return res
