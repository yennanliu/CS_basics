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


# V0-1
# IDEA : BRUTE FORCE (recompute the stable set after every hit)
#
#   apply the hits in the given order; after each one, flood fill from row 0 to
#   mark what is still attached to the ceiling, then erase every unmarked brick
#   and report how many were erased.
#
#   SUBTLETY : the input grid may already contain bricks that hang on nothing.
#   They are NOT counted as falling (the same convention V0's union-find gets
#   for free, since they never join the TOP component), so drop them in a
#   silent pre-pass before the first hit is applied.
#
#   obviously correct, and it is the reference the reverse-time solutions are
#   checked against -- but it rescans the whole board once per hit, so it is
#   far too slow for the largest inputs (h up to 4 * 10^4).
#
# time  = O(h * m * n),  h = len(hits)
# space = O(m * n)
class Solution(object):
    def hitBricks(self, grid, hits):
        m, n = len(grid), len(grid[0])
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))
        g = [row[:] for row in grid]

        def stable():
            """boolean grid of the bricks currently connected to row 0"""
            seen = [[False] * n for _ in range(m)]
            stack = []
            for c in range(n):
                if g[0][c] == 1:
                    seen[0][c] = True
                    stack.append((0, c))
            while stack:
                r, c = stack.pop()
                for dr, dc in dirs:
                    nr, nc = r + dr, c + dc
                    if (0 <= nr < m and 0 <= nc < n
                            and g[nr][nc] == 1 and not seen[nr][nc]):
                        seen[nr][nc] = True
                        stack.append((nr, nc))
            return seen

        def drop_unstable():
            """erase every brick not connected to row 0, return how many"""
            seen = stable()
            gone = 0
            for i in range(m):
                for j in range(n):
                    if g[i][j] == 1 and not seen[i][j]:
                        g[i][j] = 0
                        gone += 1
            return gone

        drop_unstable()                     # pre-existing floaters, uncounted

        res = []
        for r, c in hits:
            if g[r][c] == 0:
                res.append(0)               # the hit removed nothing
                continue
            g[r][c] = 0
            res.append(drop_unstable())
        return res


# V0-2
# IDEA : REVERSE TIME + DFS FLOOD FILL (grid marking instead of union-find)
#
#   same "add the bricks back" trick as V0, but connectivity is tracked by
#   painting the grid rather than by a disjoint-set forest :
#
#     1) erase every hit brick, then flood fill from row 0 turning each stable
#        brick 1 -> 2
#     2) walk the hits backwards and put the brick back as a 1.  if it now
#        touches the ceiling (row 0) or any 2, flood fill from it : every cell
#        newly painted 2 is a brick that had fallen at that hit, minus the
#        re-added brick itself
#
#   the cost is linear because a cell is painted 2 at most ONCE over the whole
#   run -- the fills only ever recurse into cells still holding a 1.
#
# time  = O(m * n + h)
# space = O(m * n)
class Solution(object):
    def hitBricks(self, grid, hits):
        m, n = len(grid), len(grid[0])
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        g = [row[:] for row in grid]
        for r, c in hits:
            g[r][c] = 0

        def fill(r, c):
            """paint the 1-component at (r,c) as stable (2), return its size"""
            if g[r][c] != 1:
                return 0
            g[r][c] = 2
            stack = [(r, c)]
            cnt = 1
            while stack:
                x, y = stack.pop()
                for dx, dy in dirs:
                    nx, ny = x + dx, y + dy
                    if 0 <= nx < m and 0 <= ny < n and g[nx][ny] == 1:
                        g[nx][ny] = 2
                        cnt += 1
                        stack.append((nx, ny))
            return cnt

        for c in range(n):
            fill(0, c)

        def hangs_on_ceiling(r, c):
            if r == 0:
                return True
            for dr, dc in dirs:
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n and g[nr][nc] == 2:
                    return True
            return False

        res = [0] * len(hits)
        for i in range(len(hits) - 1, -1, -1):
            r, c = hits[i]
            if grid[r][c] == 0:
                continue                    # the hit removed nothing
            g[r][c] = 1
            if hangs_on_ceiling(r, c):
                # -1 : the brick we just put back did not "fall"
                res[i] = fill(r, c) - 1
        return res
