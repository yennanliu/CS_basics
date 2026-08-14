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
