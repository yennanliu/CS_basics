"""

2123. Minimum Operations to Remove Adjacent Ones in Matrix
Hard
🔒 (premium)

You are given a 0-indexed binary matrix grid. In one operation, you can flip any 1 in grid to be 0.

A binary matrix is well-isolated if there is no 1 in the matrix that is 4-directionally connected (i.e., horizontal and vertical) to another 1.

Return the minimum number of operations to make grid well-isolated.


Example 1:

Input: grid = [[1,1,0],[0,1,1],[1,1,1]]
Output: 3
Explanation:
Use 3 operations to change grid[0][1], grid[1][2], and grid[2][1] to 0.
After, no more 1's are 4-directionally connected and grid is well-isolated.

Example 2:

Input: grid = [[0,0,0],[0,0,0],[0,0,0]]
Output: 0
Explanation:
There are no 1's in grid and it is well-isolated.
No operations were done so return 0.

Example 3:

Input: grid = [[0,1],[1,0]]
Output: 0
Explanation:
None of the 1's are 4-directionally connected and grid is well-isolated.
No operations were done so return 0.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : MINIMUM VERTEX COVER ON A BIPARTITE GRID = MAXIMUM MATCHING (KÖNIG)
#
#   build a graph whose vertices are the 1-cells and whose edges join
#   4-adjacent 1s. "no two 1s adjacent" means every edge must lose at least
#   one endpoint, i.e. the flipped cells form a VERTEX COVER — and we want
#   the smallest one.
#
#   the grid is bipartite by the checkerboard colouring : (i+j) even on one
#   side, odd on the other, and every edge crosses sides. König's theorem
#   then says
#           minimum vertex cover = maximum matching
#   so the answer is just the size of a maximum bipartite matching.
#
#   Hopcroft-Karp finds it in O(E * sqrt(V)) — the plain augmenting-path
#   loop would be O(V*E), too slow for a full 300x300 grid.
#
# time = O(E * sqrt(V)) with V <= m*n and E <= 2*m*n, space = O(m*n)
from collections import deque


class Solution(object):
    def minimumOperations(self, grid):
        m, n = len(grid), len(grid[0])
        INF = float('inf')

        # left  = 1-cells on the even squares of the checkerboard
        # right = 1-cells on the odd squares
        left_id = {}
        right_id = {}
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    if (i + j) % 2 == 0:
                        left_id[(i, j)] = len(left_id)
                    else:
                        right_id[(i, j)] = len(right_id)

        adj = [[] for _ in range(len(left_id))]
        for (i, j), u in left_id.items():
            for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                v = right_id.get((i + di, j + dj))
                if v is not None:
                    adj[u].append(v)

        nl, nr = len(left_id), len(right_id)
        match_l = [-1] * nl          # left  -> right
        match_r = [-1] * nr          # right -> left
        dist = [0] * nl

        def bfs():
            q = deque()
            found = False
            for u in range(nl):
                if match_l[u] == -1:
                    dist[u] = 0
                    q.append(u)
                else:
                    dist[u] = INF
            while q:
                u = q.popleft()
                for v in adj[u]:
                    w = match_r[v]
                    if w == -1:
                        found = True
                    elif dist[w] == INF:
                        dist[w] = dist[u] + 1
                        q.append(w)
            return found

        def dfs(u):
            for v in adj[u]:
                w = match_r[v]
                if w == -1 or (dist[w] == dist[u] + 1 and dfs(w)):
                    match_l[u] = v
                    match_r[v] = u
                    return True
            dist[u] = INF
            return False

        res = 0
        while bfs():
            for u in range(nl):
                if match_l[u] == -1 and dfs(u):
                    res += 1
        return res
