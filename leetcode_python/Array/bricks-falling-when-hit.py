"""

803. Bricks Falling When Hit
Hard

You are given an m x n binary grid, where each 1 represents a brick and
0 represents an empty space. A brick is stable if:

It is directly connected to the top of the grid, or
At least one other brick in its four adjacent cells is stable.

You are also given an array hits, which is a sequence of erasures we want to apply.
Each time we want to erase the brick at the location hits[i] = (rowi, coli).
The brick on that location (if it exists) will disappear. Some other bricks may no
longer be stable because of that erasure and will fall. Once a brick falls, it is
immediately erased from the grid (i.e., it does not land on other stable bricks).

Return an array result, where each result[i] is the number of bricks that will fall
after the ith erasure is applied.

Note that an erasure may refer to a location with no brick,
and if it does, no bricks drop.


Example 1:

Input: grid = [[1,0,0,0],[1,1,1,0]], hits = [[1,0]]
Output: [2]
Explanation: Starting with the grid:
[[1,0,0,0],
 [1,1,1,0]]
We erase the brick at (1,0), resulting in the grid:
[[1,0,0,0],
 [0,1,1,0]]
The two remaining bricks on row 1 are no longer stable as they are no longer
connected to the top nor adjacent to another stable brick, so they will fall.
The resulting grid is:
[[1,0,0,0],
 [0,0,0,0]]
Hence the result is [2].

Example 2:

Input: grid = [[1,0,0,0],[1,1,0,0]], hits = [[1,1],[1,0]]
Output: [0,0]
Explanation: no brick ever loses its connection to the top,
so nothing falls after either erasure.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 200
grid[i][j] is 0 or 1.
1 <= hits.length <= 4 * 10^4
hits[i].length == 2
0 <= xi <= m - 1
0 <= yi <= n - 1
All (xi, yi) are unique.

"""

# V0
# IDEA : UNION FIND + REVERSE TIME ("re-add the bricks")
#
#   Union-Find can merge components but cannot split them, so we run time
#   BACKWARDS:
#
#     1) erase every hit brick up front -> the "final" grid
#     2) union-find the survivors, with a virtual node TOP joined to row 0
#     3) walk the hits in reverse and ADD each brick back.
#        The number of bricks that fell at that hit equals
#            (size of TOP component after) - (before) - 1
#        (the "-1" excludes the re-added brick itself)
#
# time  = O(m * n * alpha + h * alpha),  h = len(hits)
# space = O(m * n)
class Solution(object):
    def hitBricks(self, grid, hits):
        m, n = len(grid), len(grid[0])
        TOP = m * n                     # virtual "ceiling" node
        parent = list(range(m * n + 1))
        size = [1] * (m * n + 1)

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]   # path halving
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                size[rb] += size[ra]

        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        # 1) build the grid AFTER all hits are applied
        g = [row[:] for row in grid]
        for r, c in hits:
            g[r][c] = 0

        def connect(r, c):
            """join cell (r,c) with TOP (if on row 0) and its brick neighbours"""
            if r == 0:
                union(r * n + c, TOP)
            for dr, dc in dirs:
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n and g[nr][nc] == 1:
                    union(r * n + c, nr * n + nc)

        # 2) union-find over the surviving bricks
        for r in range(m):
            for c in range(n):
                if g[r][c] == 1:
                    connect(r, c)

        # 3) replay the hits in reverse, adding bricks back
        res = [0] * len(hits)
        for i in range(len(hits) - 1, -1, -1):
            r, c = hits[i]
            if grid[r][c] == 0:
                continue                # the hit removed nothing
            before = size[find(TOP)]
            g[r][c] = 1
            connect(r, c)
            after = size[find(TOP)]
            # -1 : the brick we just put back is not a "fallen" brick
            res[i] = max(0, after - before - 1)

        return res
