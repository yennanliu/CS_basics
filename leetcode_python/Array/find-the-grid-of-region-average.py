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
