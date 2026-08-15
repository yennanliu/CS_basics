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
