"""

1878. Get Biggest Three Rhombus Sums in a Grid
Easy

You are given an m x n integer matrix grid.

A rhombus sum is the sum of the elements that form the border of a regular rhombus shape in grid. The rhombus must have the shape of a square rotated 45 degrees with each of the corners centered in a grid cell.

Note that the rhombus can have an area of 0, which is the degenerate single-cell rhombus.

Return the biggest three distinct rhombus sums in the grid in descending order. If there are less than three distinct values, return all of them.


Example 1:

Input: grid = [[3,4,5,1,3],[3,3,4,2,3],[20,30,200,40,10],[1,5,5,4,1],[4,3,2,2,5]]
Output: [228,216,211]
Explanation: The rhombus shapes for the three biggest distinct rhombus sums are:
- Blue: 20 + 3 + 200 + 5 = 228
- Red: 200 + 2 + 10 + 4 = 216
- Green: 5 + 200 + 4 + 2 = 211

Example 2:

Input: grid = [[1,2,3],[4,5,6],[7,8,9]]
Output: [20,9,8]
Explanation:
- Blue: 4 + 2 + 6 + 8 = 20
- Red: 9 (area 0 rhombus in the bottom right corner)
- Green: 8 (area 0 rhombus in the bottom middle)

Example 3:

Input: grid = [[7,7,7]]
Output: [7]
Explanation: All three possible rhombus sums are the same, so return [7].


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
1 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : ENUMERATE (TOP CORNER, HALF-DIAGONAL) AND WALK THE BORDER
#
#   a rhombus is fully determined by its TOP cell (i, j) and its half
#   diagonal k :
#     top    = (i,       j)
#     right  = (i + k,   j + k)
#     bottom = (i + 2k,  j)
#     left   = (i + k,   j - k)
#   it fits iff  i + 2k < m  and  0 <= j - k  and  j + k < n.
#
#   walking the 4 edges (each contributing k cells, corners counted once)
#   costs O(k), and k <= min(m, n) / 2, so the whole enumeration is
#   O(m * n * K^2) which is tiny for a 50 x 50 grid.
#
#   NOTE : k = 0 is legal -> the single cell grid[i][j].
#   NOTE : the answer wants DISTINCT sums -> collect into a set first.
#
# time = O(m * n * K^2), K = min(m, n) / 2
# space = O(m * n * K) for the set of sums
class Solution(object):
    def getBiggestThree(self, grid):
        m, n = len(grid), len(grid[0])
        sums = set()

        for i in range(m):
            for j in range(n):
                sums.add(grid[i][j])                 # k = 0
                k = 1
                while i + 2 * k < m and j - k >= 0 and j + k < n:
                    total = 0
                    for d in range(k):
                        total += grid[i + d][j + d]              # top   -> right
                        total += grid[i + k + d][j + k - d]      # right -> bottom
                        total += grid[i + 2 * k - d][j - d]      # bottom-> left
                        total += grid[i + k - d][j - k + d]      # left  -> top
                    sums.add(total)
                    k += 1

        return sorted(sums, reverse=True)[:3]


# V0-1
# IDEA : DIAGONAL PREFIX SUMS -> EACH RHOMBUS BORDER IN O(1)
#
#   the four edges of a rhombus are DIAGONAL segments, so precompute a prefix
#   sum along each of the two diagonal directions (1-indexed, padded) :
#
#       dr[i][j] = grid[i][j] + dr[i - 1][j - 1]   ("\" ray ending at (i, j))
#       dl[i][j] = grid[i][j] + dl[i - 1][j + 1]   ("/" ray ending at (i, j))
#
#   then any diagonal segment is a single subtraction :
#
#       "\" from (r1, c1) to (r2, c2) = dr[r2][c2] - dr[r1 - 1][c1 - 1]
#       "/" from (r1, c1) to (r2, c2) = dl[r2][c2] - dl[r1 - 1][c1 + 1]
#
#   a border = 4 such segments, and summing them counts each of the 4 CORNERS
#   twice, so subtract the four corner cells once.
#
#   NOTE : this removes the inner O(k) walk, so the enumeration drops from
#          O(m * n * K^2) to O(m * n * K).
#   NOTE : dl needs a column n + 1 sentinel, hence the width n + 2 padding.
#
# time = O(m * n * K), K = min(m, n) / 2
# space = O(m * n)
class Solution(object):
    def getBiggestThree(self, grid):
        m, n = len(grid), len(grid[0])

        dr = [[0] * (n + 2) for _ in range(m + 1)]
        dl = [[0] * (n + 2) for _ in range(m + 1)]
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                dr[i][j] = grid[i - 1][j - 1] + dr[i - 1][j - 1]
                dl[i][j] = grid[i - 1][j - 1] + dl[i - 1][j + 1]

        def down_right(r1, c1, r2, c2):
            return dr[r2][c2] - dr[r1 - 1][c1 - 1]

        def down_left(r1, c1, r2, c2):
            return dl[r2][c2] - dl[r1 - 1][c1 + 1]

        sums = set()
        for i in range(m):
            for j in range(n):
                sums.add(grid[i][j])                      # k = 0
                k = 1
                while i + 2 * k < m and j - k >= 0 and j + k < n:
                    r, c = i + 1, j + 1                   # 1-indexed top corner
                    total = (down_right(r, c, r + k, c + k)
                             + down_left(r + k, c + k, r + 2 * k, c)
                             + down_left(r, c, r + k, c - k)
                             + down_right(r + k, c - k, r + 2 * k, c))
                    total -= (grid[i][j] + grid[i + k][j + k]
                              + grid[i + 2 * k][j] + grid[i + k][j - k])
                    sums.add(total)
                    k += 1

        return sorted(sums, reverse=True)[:3]


# V0-2
# IDEA : BRUTE FORCE BY (CENTER, RADIUS) + MANHATTAN MEMBERSHIP TEST
#
#   describe the rhombus by its CENTER (cy, cx) and radius k instead of its top
#   corner: the border is exactly the cells with
#
#       |r - cy| + |c - cx| == k
#
#   so scan the (2k + 1) x (2k + 1) bounding box around the center and add up
#   whatever satisfies that test -- no edge-walking geometry to get right, at
#   the cost of touching O(k^2) cells per rhombus instead of O(k).
#
#   the top-3 are kept in a 3-slot list rather than a set of every sum, so the
#   extra space is O(1) -- useful when the grid is far larger than 50 x 50.
#
#   NOTE : k = 0 makes the box a single cell, so the degenerate rhombus is
#          handled by the same loop with no special case.
#
# time = O(m * n * K^3), K = min(m, n) / 2
# space = O(1)
class Solution(object):
    def getBiggestThree(self, grid):
        m, n = len(grid), len(grid[0])
        top = []

        def offer(v):
            if v in top:
                return
            top.append(v)
            top.sort(reverse=True)
            del top[3:]

        for cy in range(m):
            for cx in range(n):
                k = 0
                while cy - k >= 0 and cy + k < m and cx - k >= 0 and cx + k < n:
                    total = 0
                    for r in range(cy - k, cy + k + 1):
                        rest = k - abs(r - cy)
                        for c in range(cx - k, cx + k + 1):
                            if abs(c - cx) == rest:
                                total += grid[r][c]
                    offer(total)
                    k += 1

        return top
