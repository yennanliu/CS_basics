package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-grid-of-region-average/description/

/**
 * 3030. Find the Grid of Region Average
 * Medium
 *
 * You are given m x n grid image which represents a grayscale image, where image[i][j] represents a pixel with intensity in the range [0..255]. You are also given a non-negative integer threshold.
 *
 * Two pixels are adjacent if they share an edge.
 *
 * A region is a 3 x 3 subgrid where the absolute difference in intensity between any two adjacent pixels is less than or equal to threshold.
 *
 * All pixels in a region belong to that region, note that a pixel can belong to multiple regions.
 *
 * You need to calculate a m x n grid result, where result[i][j] is the average intensity of the regions to which image[i][j] belongs, rounded down to the nearest integer. If image[i][j] belongs to multiple regions, result[i][j] is the average of the rounded-down average intensities of these regions, rounded down to the nearest integer. If image[i][j] does not belong to any region, result[i][j] is equal to image[i][j].
 *
 * Return the grid result.
 *
 *
 * Example 1:
 *
 * Input: image = [[5,6,7,10],[8,9,10,10],[11,12,13,10]], threshold = 3
 * Output: [[9,9,9,9],[9,9,9,9],[9,9,9,9]]
 * Explanation: There are two regions as illustrated above. The average intensity of the first region is 9, while the average intensity of the second region is 9.67 which is rounded down to 9. The average intensity of both of the regions is (9 + 9) / 2 = 9. As all the pixels belong to either region 1, region 2, or both of them, the intensity of every pixel in the result is 9.
 * Please note that the rounded-down values are used when calculating the average of multiple regions.
 *
 * Example 2:
 *
 * Input: image = [[10,20,30],[15,25,35],[20,30,40],[25,35,45]], threshold = 12
 * Output: [[25,25,25],[27,27,27],[27,27,27],[30,30,30]]
 * Explanation: There are two regions as illustrated above. The average intensity of the first region is 25, while the average intensity of the second region is 30. The average intensity of both of the regions is (25 + 30) / 2 = 27.5 which is rounded down to 27.
 * All the pixels in row 0 of the image belong to region 1, hence all the pixels in row 0 in the result are 25.
 * Similarly, all the pixels in row 3 in the result are 30.
 * The pixels in rows 1 and 2 of the image belong to region 1 and region 2, hence their assigned value is 27 in the result.
 *
 * Example 3:
 *
 * Input: image = [[5,6,7],[8,9,10],[11,12,13]], threshold = 1
 * Output: [[5,6,7],[8,9,10],[11,12,13]]
 * Explanation: There is only one 3x3 subgrid, while it has two adjacent pixels with an absolute difference greater than threshold, so it is not a region.
 * Hence, the result is the same as the image.
 *
 *
 * Constraints:
 *
 * 3 <= n, m <= 500
 * 0 <= image[i][j] <= 255
 * 0 <= threshold <= 255
 *
 */
public class FindTheGridOfRegionAverage {

    // V0
    // IDEA: TEST EVERY 3x3 WINDOW, THEN AVERAGE THE PER-REGION AVERAGES
    // a qualifying window adds its own rounded-down average to each of its nine
    // pixels; each pixel keeps a running total and a region count. the floor is
    // applied twice on purpose: once per region, then once per pixel.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] resultGrid(int[][] image, int threshold) {
        int rows = image.length;
        int cols = image[0].length;
        int[][] total = new int[rows][cols];
        int[][] regionCount = new int[rows][cols];

        // (top, left) is the corner of each 3x3 window
        for (int top = 0; top + 2 < rows; top++) {
            for (int left = 0; left + 2 < cols; left++) {
                if (!isRegion(image, top, left, threshold)) {
                    continue;
                }

                int windowSum = 0;
                for (int r = top; r < top + 3; r++) {
                    for (int c = left; c < left + 3; c++) {
                        windowSum += image[r][c];
                    }
                }
                int regionAverage = windowSum / 9;

                // every pixel of the window belongs to this region
                for (int r = top; r < top + 3; r++) {
                    for (int c = left; c < left + 3; c++) {
                        total[r][c] += regionAverage;
                        regionCount[r][c]++;
                    }
                }
            }
        }

        int[][] result = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (regionCount[r][c] == 0) {
                    // in no region: keep the original intensity
                    result[r][c] = image[r][c];
                } else {
                    result[r][c] = total[r][c] / regionCount[r][c];
                }
            }
        }
        return result;
    }

    /** True if every pair of edge-adjacent pixels in the 3x3 window differs by at most threshold. */
    private boolean isRegion(int[][] image, int top, int left, int threshold) {
        for (int r = top; r < top + 3; r++) {
            for (int c = left; c < left + 3; c++) {
                // right neighbour, if still inside the window
                if (c + 1 < left + 3 && Math.abs(image[r][c] - image[r][c + 1]) > threshold) {
                    return false;
                }
                // lower neighbour, if still inside the window
                if (r + 1 < top + 3 && Math.abs(image[r][c] - image[r + 1][c]) > threshold) {
                    return false;
                }
            }
        }
        return true;
    }
}
