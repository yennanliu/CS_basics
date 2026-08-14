"""

302. Smallest Rectangle Enclosing Black Pixels
Hard
(premium)

You are given an m x n binary matrix image where 0 represents a white pixel and
1 represents a black pixel.

The black pixels are connected (i.e., there is only one black region).
Pixels are connected horizontally and vertically.

Given two integers x and y that represents the location of one of the black pixels,
return the area of the smallest (axis-aligned) rectangle that encloses all black pixels.

You must write an algorithm with less than O(mn) runtime complexity.


Example 1:

Input: image = [["0","0","1","0"],["0","1","1","0"],["0","1","0","0"]], x = 0, y = 2
Output: 6

Example 2:

Input: image = [["1"]], x = 0, y = 0
Output: 1


Constraints:

m == image.length
n == image[i].length
1 <= m, n <= 100
image[i][j] is either '0' or '1'.
0 <= x < m
0 <= y < n
image[x][y] == '1'.
The black pixels in the image only form one component.

"""

# V0
# IDEA : BINARY SEARCH on each of the 4 boundaries
#
#  KEY OBSERVATION: because the black pixels form ONE connected component, the set of
#  rows containing a black pixel is a contiguous interval that contains row x.
#  -> "does row i contain black?" is monotonic on each side of x, so it is binary
#  searchable. Same argument for columns around y.
#
#  Each predicate check costs a full row/column scan, giving less than O(mn) overall.
#
# time  = O(m * log n + n * log m)
# space = O(1)
class Solution(object):
    def minArea(self, image, x, y):
        if not image or not image[0]:
            return 0

        m, n = len(image), len(image[0])

        def row_has_black(i):
            return any(c == "1" for c in image[i])

        def col_has_black(j):
            return any(image[i][j] == "1" for i in range(m))

        # top = smallest row index in [0, x] that has a black pixel
        lo, hi = 0, x
        while lo < hi:
            mid = (lo + hi) // 2
            if row_has_black(mid):
                hi = mid
            else:
                lo = mid + 1
        top = lo

        # bottom = largest row index in [x, m-1] that has a black pixel
        # NOTE: use the UPPER mid `(lo + hi + 1) // 2` to avoid an infinite loop
        lo, hi = x, m - 1
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if row_has_black(mid):
                lo = mid
            else:
                hi = mid - 1
        bottom = lo

        # left = smallest col index in [0, y] that has a black pixel
        lo, hi = 0, y
        while lo < hi:
            mid = (lo + hi) // 2
            if col_has_black(mid):
                hi = mid
            else:
                lo = mid + 1
        left = lo

        # right = largest col index in [y, n-1] that has a black pixel
        lo, hi = y, n - 1
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if col_has_black(mid):
                lo = mid
            else:
                hi = mid - 1
        right = lo

        return (bottom - top + 1) * (right - left + 1)
