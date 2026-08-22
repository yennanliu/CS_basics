"""

3197. Find the Minimum Area to Cover All Ones II
Hard

You are given a 2D binary array grid. You need to find 3 non-overlapping rectangles having non-zero areas with horizontal and vertical sides such that all the 1's in grid lie inside these rectangles.

Return the minimum possible sum of the area of these rectangles.

Note that the rectangles are allowed to touch.


Example 1:

Input: grid = [[1,0,1],[1,1,1]]
Output: 5
Explanation:
The 1's at (0, 0) and (1, 0) are covered by a rectangle of area 2.
The 1's at (0, 2) and (1, 2) are covered by a rectangle of area 2.
The 1 at (1, 1) is covered by a rectangle of area 1.

Example 2:

Input: grid = [[1,0,1,0],[0,1,0,1]]
Output: 5
Explanation:
The 1's at (0, 0) and (0, 2) are covered by a rectangle of area 3.
The 1 at (1, 1) is covered by a rectangle of area 1.
The 1 at (1, 3) is covered by a rectangle of area 1.


Constraints:

1 <= grid.length, grid[i].length <= 30
grid[i][j] is either 0 or 1.
The input is generated such that there are at least three 1's in grid.

"""

# V0
# IDEA : THREE NON-OVERLAPPING RECTANGLES = TWO GUILLOTINE CUTS, SIX SHAPES
#
#   three axis-aligned rectangles that do not overlap and together cover the
#   grid's 1s can always be laid out by cutting the board twice :
#
#       two horizontal cuts        (three stacked bands)
#       two vertical cuts          (three side-by-side strips)
#       one horizontal cut, then a vertical cut on the top part
#       one horizontal cut, then a vertical cut on the bottom part
#       one vertical cut, then a horizontal cut on the left part
#       one vertical cut, then a horizontal cut on the right part
#
#   for any region the best rectangle is the bounding box of the 1s inside
#   it, so the whole search is "enumerate the cuts, add three bounding-box
#   areas". with a 30x30 grid that is at most a few hundred thousand region
#   scans once the bounding box is memoised per rectangle of the grid.
#
#   an EMPTY region contributes 0 — every 1 still lands somewhere.
#
# time = O(m * n * (m + n) * ...) — small for 30x30, space = O(m * n)
class Solution(object):
    def minimumSum(self, grid):
        m, n = len(grid), len(grid[0])

        memo = {}

        def area(r1, r2, c1, c2):
            """bounding-box area of the 1s inside rows [r1,r2), cols [c1,c2)"""
            key = (r1, r2, c1, c2)
            if key in memo:
                return memo[key]
            top, bot, left, right = m, -1, n, -1
            for i in range(r1, r2):
                row = grid[i]
                for j in range(c1, c2):
                    if row[j]:
                        if i < top:
                            top = i
                        if i > bot:
                            bot = i
                        if j < left:
                            left = j
                        if j > right:
                            right = j
            res = 0 if bot == -1 else (bot - top + 1) * (right - left + 1)
            memo[key] = res
            return res

        best = float('inf')

        # three horizontal bands
        for a in range(1, m):
            for b in range(a + 1, m):
                best = min(best, area(0, a, 0, n) + area(a, b, 0, n) + area(b, m, 0, n))

        # three vertical strips
        for a in range(1, n):
            for b in range(a + 1, n):
                best = min(best, area(0, m, 0, a) + area(0, m, a, b) + area(0, m, b, n))

        # one horizontal cut, then split one side vertically
        for a in range(1, m):
            for c in range(1, n):
                best = min(best, area(0, a, 0, c) + area(0, a, c, n) + area(a, m, 0, n))
                best = min(best, area(0, a, 0, n) + area(a, m, 0, c) + area(a, m, c, n))

        # one vertical cut, then split one side horizontally
        for c in range(1, n):
            for a in range(1, m):
                best = min(best, area(0, a, 0, c) + area(a, m, 0, c) + area(0, m, c, n))
                best = min(best, area(0, m, 0, c) + area(0, a, c, n) + area(a, m, c, n))

        return best


# V0-1
# IDEA : RECURSIVE GUILLOTINE DP — dfs(region, k) INSTEAD OF 6 HAND-WRITTEN CASES
#
#   the same guillotine insight as V0, but expressed as a recurrence over
#   (region, number of rectangles left) :
#
#       f(R, 1) = bounding-box area of the 1s in R
#       f(R, k) = min over every horizontal / vertical cut of R into A|B
#                 and every split k = i + (k - i) of  f(A, i) + f(B, k - i)
#
#   the six layouts of V0 fall out automatically as the six ways the two cuts
#   can be nested, and the recurrence generalises to any k (LC 3195 is k = 2)
#   without writing new cases. memoised on (r1, r2, c1, c2, k).
#
#   a region that cannot be cut into k >= 2 pieces (a single row for a
#   horizontal split, etc.) returns inf, so infeasible layouts drop out of
#   the min on their own.
#
# time = O((m + n)^2 * m * n) worst case, space = O((m + n)^2 + m * n)
class Solution(object):
    def minimumSum(self, grid):
        m, n = len(grid), len(grid[0])

        bbox_memo = {}

        def bbox_area(r1, r2, c1, c2):
            key = (r1, r2, c1, c2)
            if key in bbox_memo:
                return bbox_memo[key]
            top, bot, left, right = m, -1, n, -1
            for i in range(r1, r2):
                row = grid[i]
                for j in range(c1, c2):
                    if row[j]:
                        top = min(top, i)
                        bot = max(bot, i)
                        left = min(left, j)
                        right = max(right, j)
            res = 0 if bot < 0 else (bot - top + 1) * (right - left + 1)
            bbox_memo[key] = res
            return res

        INF = float('inf')
        dp_memo = {}

        def dfs(r1, r2, c1, c2, k):
            if k == 1:
                return bbox_area(r1, r2, c1, c2)
            key = (r1, r2, c1, c2, k)
            if key in dp_memo:
                return dp_memo[key]
            best = INF
            for i in range(1, k):
                for a in range(r1 + 1, r2):
                    best = min(best, dfs(r1, a, c1, c2, i)
                                     + dfs(a, r2, c1, c2, k - i))
                for b in range(c1 + 1, c2):
                    best = min(best, dfs(r1, r2, c1, b, i)
                                     + dfs(r1, r2, b, c2, k - i))
            dp_memo[key] = best
            return best

        return dfs(0, m, 0, n, 3)


# V0-2
# IDEA : PRECOMPUTED PREFIX BOUNDING-BOX TABLES — O(1) PER REGION LOOKUP
#
#   V0 / V0-1 both RESCAN a region to find its bounding box. but only six
#   families of regions are ever asked about, and each family can be built up
#   incrementally, merging one row (or column) at a time :
#
#       row_pre[r][c]  = bbox of row r restricted to cols [0, c)
#       row_suf[r][c]  = bbox of row r restricted to cols [c, n)
#       TL[a][c] = bbox of rows [0, a) x cols [0, c)  = TL[a-1][c] + row_pre[a-1][c]
#       TR[a][c] = bbox of rows [0, a) x cols [c, n)  = TR[a-1][c] + row_suf[a-1][c]
#       BL[a][c] = bbox of rows [a, m) x cols [0, c)  = BL[a+1][c] + row_pre[a][c]
#       BR[a][c] = bbox of rows [a, m) x cols [c, n)  = BR[a+1][c] + row_suf[a][c]
#       band[r1][r2]   = bbox of rows [r1, r2) x all cols
#       strip[c1][c2]  = bbox of cols [c1, c2) x all rows
#
#   merging two bounding boxes is min/min/max/max, so every table entry costs
#   O(1) and the whole enumeration of the six layouts becomes table lookups.
#   that drops the m*n rescan factor entirely.
#
#   empty bbox is represented as None and contributes area 0.
#
# time = O(m * n + m^2 + n^2), space = O(m * n + m^2 + n^2)
class Solution(object):
    def minimumSum(self, grid):
        m, n = len(grid), len(grid[0])

        def merge(a, b):
            if a is None:
                return b
            if b is None:
                return a
            return (min(a[0], b[0]), max(a[1], b[1]),
                    min(a[2], b[2]), max(a[3], b[3]))

        def area(b):
            if b is None:
                return 0
            return (b[1] - b[0] + 1) * (b[3] - b[2] + 1)

        # per-row prefix / suffix boxes
        row_pre = [[None] * (n + 1) for _ in range(m)]
        row_suf = [[None] * (n + 1) for _ in range(m)]
        for r in range(m):
            for c in range(1, n + 1):
                cell = (r, r, c - 1, c - 1) if grid[r][c - 1] else None
                row_pre[r][c] = merge(row_pre[r][c - 1], cell)
            for c in range(n - 1, -1, -1):
                cell = (r, r, c, c) if grid[r][c] else None
                row_suf[r][c] = merge(row_suf[r][c + 1], cell)

        # quadrant tables
        TL = [[None] * (n + 1) for _ in range(m + 1)]
        TR = [[None] * (n + 1) for _ in range(m + 1)]
        BL = [[None] * (n + 1) for _ in range(m + 1)]
        BR = [[None] * (n + 1) for _ in range(m + 1)]
        for a in range(1, m + 1):
            for c in range(n + 1):
                TL[a][c] = merge(TL[a - 1][c], row_pre[a - 1][c])
                TR[a][c] = merge(TR[a - 1][c], row_suf[a - 1][c])
        for a in range(m - 1, -1, -1):
            for c in range(n + 1):
                BL[a][c] = merge(BL[a + 1][c], row_pre[a][c])
                BR[a][c] = merge(BR[a + 1][c], row_suf[a][c])

        # full-width bands and full-height strips
        band = [[0] * (m + 1) for _ in range(m + 1)]
        for r1 in range(m):
            box = None
            for r2 in range(r1 + 1, m + 1):
                box = merge(box, row_pre[r2 - 1][n])
                band[r1][r2] = area(box)

        col_box = []
        for c in range(n):
            box = None
            for r in range(m):
                if grid[r][c]:
                    box = merge(box, (r, r, c, c))
            col_box.append(box)
        strip = [[0] * (n + 1) for _ in range(n + 1)]
        for c1 in range(n):
            box = None
            for c2 in range(c1 + 1, n + 1):
                box = merge(box, col_box[c2 - 1])
                strip[c1][c2] = area(box)

        best = float('inf')

        for a in range(1, m):
            for b in range(a + 1, m):
                best = min(best, band[0][a] + band[a][b] + band[b][m])

        for a in range(1, n):
            for b in range(a + 1, n):
                best = min(best, strip[0][a] + strip[a][b] + strip[b][n])

        for a in range(1, m):
            for c in range(1, n):
                tl, tr = area(TL[a][c]), area(TR[a][c])
                bl, br = area(BL[a][c]), area(BR[a][c])
                best = min(best, tl + tr + band[a][m])       # cut rows, split top
                best = min(best, band[0][a] + bl + br)       # cut rows, split bottom
                best = min(best, tl + bl + strip[c][n])      # cut cols, split left
                best = min(best, strip[0][c] + tr + br)      # cut cols, split right

        return best
