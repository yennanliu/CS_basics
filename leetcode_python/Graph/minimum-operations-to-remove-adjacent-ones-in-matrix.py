"""

2123. Minimum Operations to Remove Adjacent Ones in Matrix
Hard
(premium / locked problem)

You are given a 0-indexed binary matrix grid. In one operation, you can flip any 1 in grid to be 0.

A binary matrix is well-isolated if there is no 1 in the matrix that is 4-directionally connected (i.e., horizontal and vertical) to another 1.

Return the minimum number of operations to make grid well-isolated.


Example 1:

Input: grid = [[1,1,0],[0,1,1],[1,1,1]]
Output: 3
Explanation: Use 3 operations to change grid[0][1], grid[1][2], and grid[2][1] to 0.
After, no more 1's are 4-directionally connected and grid is well-isolated.

Example 2:

Input: grid = [[0,0,0],[0,0,0],[0,0,0]]
Output: 0
Explanation: There are no 1's in grid and it is well-isolated.
No operations were done so return 0.

Example 3:

Input: grid = [[0,1],[1,0]]
Output: 0
Explanation: None of the 1's are 4-directionally connected and grid is well-isolated.
No operations were done so return 0.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 300
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : MINIMUM VERTEX COVER ON A BIPARTITE GRAPH = MAXIMUM MATCHING (König)
#
#   build a graph whose vertices are the cells holding a 1 and whose edges
#   join 4-directionally adjacent ones. "no two 1s adjacent" means deleting a
#   set of vertices that touches every edge — a MINIMUM VERTEX COVER.
#
#   the grid graph is bipartite : colour a cell by the parity of (i + j), and
#   every edge joins an even cell to an odd one. by König's theorem the
#   minimum vertex cover of a bipartite graph equals its MAXIMUM MATCHING.
#
#   so : run Kuhn's augmenting-path matching from every even-parity 1 and
#   return the matching size.
#
# time = O(V * E) for Kuhn's algorithm, space = O(V + E)
class Solution(object):
    def minimumOperations(self, grid):
        m, n = len(grid), len(grid[0])

        # adjacency from every EVEN-parity 1 to its ODD-parity neighbours
        g = {}
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1 and (i + j) % 2 == 0:
                    nbrs = []
                    for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                        x, y = i + di, j + dj
                        if 0 <= x < m and 0 <= y < n and grid[x][y] == 1:
                            nbrs.append((x, y))
                    g[(i, j)] = nbrs

        match = {}        # odd cell -> matched even cell

        def try_augment(u, seen):
            for v in g[u]:
                if v in seen:
                    continue
                seen.add(v)
                if v not in match or try_augment(match[v], seen):
                    match[v] = u
                    return True
            return False

        res = 0
        for u in g:
            if try_augment(u, set()):
                res += 1
        return res
