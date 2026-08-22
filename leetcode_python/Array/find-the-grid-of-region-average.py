"""

3030. Find the Grid of Region Average
Medium

You are given m x n grid image which represents a grayscale image, where image[i][j] represents a pixel with intensity in the range [0..255]. You are also given a non-negative integer threshold.

Two pixels are adjacent if they share an edge.

A region is a 3 x 3 subgrid where the absolute difference in intensity between any two adjacent pixels is less than or equal to threshold.

All pixels in a region belong to that region, note that a pixel can belong to multiple regions.

You need to calculate a m x n grid result, where result[i][j] is the average intensity of the regions to which image[i][j] belongs, rounded down to the nearest integer. If image[i][j] belongs to multiple regions, result[i][j] is the average of the rounded-down average intensities of these regions, rounded down to the nearest integer. If image[i][j] does not belong to any region, result[i][j] is equal to image[i][j].

Return the grid result.


Example 1:

Input: image = [[5,6,7,10],[8,9,10,10],[11,12,13,10]], threshold = 3
Output: [[9,9,9,9],[9,9,9,9],[9,9,9,9]]
Explanation: There are two regions as illustrated above. The average intensity of the first region is 9, while the average intensity of the second region is 9.67 which is rounded down to 9. The average intensity of both of the regions is (9 + 9) / 2 = 9. As all the pixels belong to either region 1, region 2, or both of them, the intensity of every pixel in the result is 9.
Please note that the rounded-down values are used when calculating the average of multiple regions.

Example 2:

Input: image = [[10,20,30],[15,25,35],[20,30,40],[25,35,45]], threshold = 12
Output: [[25,25,25],[27,27,27],[27,27,27],[30,30,30]]
Explanation: There are two regions as illustrated above. The average intensity of the first region is 25, while the average intensity of the second region is 30. The average intensity of both of the regions is (25 + 30) / 2 = 27.5 which is rounded down to 27.
All the pixels in row 0 of the image belong to region 1, hence all the pixels in row 0 in the result are 25.
Similarly, all the pixels in row 3 in the result are 30.
The pixels in rows 1 and 2 of the image belong to region 1 and region 2, hence their assigned value is 27 in the result.

Example 3:

Input: image = [[5,6,7],[8,9,10],[11,12,13]], threshold = 1
Output: [[5,6,7],[8,9,10],[11,12,13]]
Explanation: There is only one 3x3 subgrid, while it has two adjacent pixels with an absolute difference greater than threshold, so it is not a region.
Hence, the result is the same as the image.


Constraints:

3 <= n, m <= 500
0 <= image[i][j] <= 255
0 <= threshold <= 255

"""

# V0
# IDEA : TEST EVERY 3x3 WINDOW, THEN AVERAGE THE PER-REGION AVERAGES
#
#   a region is any 3x3 window whose neighbouring pixels all differ by at
#   most `threshold`, so check the 6 horizontal and 6 vertical adjacencies
#   inside each window — a constant 12 comparisons per position.
#
#   a qualifying window contributes its own rounded-down average (sum // 9)
#   to all NINE of its pixels. so accumulate per pixel a running total and a
#   region count; the final value is total // count, or the original pixel
#   when the count is 0.
#
#   NOTE : the spec averages the ALREADY ROUNDED region averages, so the
#          floor has to be applied twice — first per region, then per pixel.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def resultGrid(self, image, threshold):
        m, n = len(image), len(image[0])
        total = [[0] * n for _ in range(m)]
        count = [[0] * n for _ in range(m)]

        for i in range(m - 2):
            for j in range(n - 2):
                ok = True
                for a in range(i, i + 3):
                    for b in range(j, j + 3):
                        if b + 1 < j + 3 and abs(image[a][b] - image[a][b + 1]) > threshold:
                            ok = False
                        if a + 1 < i + 3 and abs(image[a][b] - image[a + 1][b]) > threshold:
                            ok = False
                    if not ok:
                        break
                if not ok:
                    continue
                avg = sum(image[a][b] for a in range(i, i + 3)
                          for b in range(j, j + 3)) // 9
                for a in range(i, i + 3):
                    for b in range(j, j + 3):
                        total[a][b] += avg
                        count[a][b] += 1

        return [[total[i][j] // count[i][j] if count[i][j] else image[i][j]
                 for j in range(n)] for i in range(m)]


# V0-1
# IDEA : PREFIX SUMS OF VIOLATIONS + 2D PREFIX SUM, SO EACH WINDOW IS O(1)
#
#   the brute force re-reads the same 12 adjacencies and the same 9 values for
#   overlapping windows. precompute instead :
#     hbad[i][j] = 1 when the horizontal pair (i,j)-(i,j+1) breaks threshold
#     vbad[i][j] = 1 when the vertical   pair (i,j)-(i+1,j) breaks threshold
#   a 3x3 window at (i,j) is a region iff the rectangle rows i..i+2 /
#   cols j..j+1 of hbad AND rows i..i+1 / cols j..j+2 of vbad both sum to 0.
#   a third prefix sum over the pixels themselves gives the window total.
#
#   every window then costs a constant number of table lookups instead of a
#   nested rescan — same asymptotics, but the constant drops from ~21 reads to
#   3 rectangle queries.
#
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def resultGrid(self, image, threshold):
        m, n = len(image), len(image[0])

        def build(grid, rows, cols):
            p = [[0] * (cols + 1) for _ in range(rows + 1)]
            for i in range(rows):
                for j in range(cols):
                    p[i + 1][j + 1] = (p[i][j + 1] + p[i + 1][j]
                                       - p[i][j] + grid[i][j])
            return p

        def query(p, r1, c1, r2, c2):
            return (p[r2 + 1][c2 + 1] - p[r1][c2 + 1]
                    - p[r2 + 1][c1] + p[r1][c1])

        hbad = [[1 if abs(image[i][j] - image[i][j + 1]) > threshold else 0
                 for j in range(n - 1)] for i in range(m)]
        vbad = [[1 if abs(image[i][j] - image[i + 1][j]) > threshold else 0
                 for j in range(n)] for i in range(m - 1)]

        ph = build(hbad, m, n - 1)
        pv = build(vbad, m - 1, n)
        pi = build(image, m, n)

        total = [[0] * n for _ in range(m)]
        count = [[0] * n for _ in range(m)]

        for i in range(m - 2):
            for j in range(n - 2):
                if query(ph, i, j, i + 2, j + 1):
                    continue
                if query(pv, i, j, i + 1, j + 2):
                    continue
                avg = query(pi, i, j, i + 2, j + 2) // 9
                for a in range(i, i + 3):
                    for b in range(j, j + 3):
                        total[a][b] += avg
                        count[a][b] += 1

        return [[total[i][j] // count[i][j] if count[i][j] else image[i][j]
                 for j in range(n)] for i in range(m)]


# V0-2
# IDEA : RUN LENGTHS FOR VALIDITY + SEPARABLE (ROW-TRIPLE) SUMS
#
#   validity : hrun[i][j] = how many cells of row i, ending at column j, form
#   an unbroken chain of within-threshold horizontal neighbours (reset to 1 on
#   a break). row i covers columns j..j+2 iff hrun[i][j + 2] >= 3. vrun does
#   the same downwards for columns. a window is a region iff its 3 rows and
#   3 columns all have a run of at least 3 — 6 comparisons, no rectangle math.
#
#   sums : a 3x3 total is separable, so first collapse each row to
#   rows3[i][j] = image[i][j] + image[i][j+1] + image[i][j+2] with a rolling
#   window, then the window total is just rows3[i][j] + rows3[i+1][j] +
#   rows3[i+2][j] — three additions, and only 1D tables are needed.
#
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def resultGrid(self, image, threshold):
        m, n = len(image), len(image[0])

        hrun = [[1] * n for _ in range(m)]
        for i in range(m):
            for j in range(1, n):
                if abs(image[i][j] - image[i][j - 1]) <= threshold:
                    hrun[i][j] = hrun[i][j - 1] + 1

        vrun = [[1] * n for _ in range(m)]
        for i in range(1, m):
            for j in range(n):
                if abs(image[i][j] - image[i - 1][j]) <= threshold:
                    vrun[i][j] = vrun[i - 1][j] + 1

        rows3 = [[0] * (n - 2) for _ in range(m)]
        for i in range(m):
            w = sum(image[i][:3])
            rows3[i][0] = w
            for j in range(1, n - 2):
                w += image[i][j + 2] - image[i][j - 1]
                rows3[i][j] = w

        total = [[0] * n for _ in range(m)]
        count = [[0] * n for _ in range(m)]

        for i in range(m - 2):
            for j in range(n - 2):
                if min(hrun[i][j + 2], hrun[i + 1][j + 2],
                       hrun[i + 2][j + 2]) < 3:
                    continue
                if min(vrun[i + 2][j], vrun[i + 2][j + 1],
                       vrun[i + 2][j + 2]) < 3:
                    continue
                avg = (rows3[i][j] + rows3[i + 1][j] + rows3[i + 2][j]) // 9
                for a in range(i, i + 3):
                    for b in range(j, j + 3):
                        total[a][b] += avg
                        count[a][b] += 1

        return [[total[i][j] // count[i][j] if count[i][j] else image[i][j]
                 for j in range(n)] for i in range(m)]
