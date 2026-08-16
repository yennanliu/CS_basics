package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/smallest-rectangle-enclosing-black-pixels/description/
/**
 * 302. Smallest Rectangle Enclosing Black Pixels
 * Hard
 * Lock: Prime
 *
 * You are given an m x n binary matrix image where 0 represents a white pixel and
 * 1 represents a black pixel.
 *
 * The black pixels are connected (i.e., there is only one black region).
 * Pixels are connected horizontally and vertically.
 *
 * Given two integers x and y that represents the location of one of the black pixels,
 * return the area of the smallest (axis-aligned) rectangle that encloses all black pixels.
 *
 * You must write an algorithm with less than O(mn) runtime complexity.
 *
 *
 * Example 1:
 *
 * Input: image = [["0","0","1","0"],["0","1","1","0"],["0","1","0","0"]], x = 0, y = 2
 * Output: 6
 *
 * Example 2:
 *
 * Input: image = [["1"]], x = 0, y = 0
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == image.length
 * n == image[i].length
 * 1 <= m, n <= 100
 * image[i][j] is either '0' or '1'.
 * 0 <= x < m
 * 0 <= y < n
 * image[x][y] == '1'.
 * The black pixels in the image only form one component.
 *
 */
public class SmallestRectangleEnclosingBlackPixels {

    // V0
    // IDEA: BINARY SEARCH on each of the 4 boundaries
    /**
     *  KEY OBSERVATION: because the black pixels form ONE CONNECTED component, the set of
     *  rows containing a black pixel is a CONTIGUOUS interval that contains row x.
     *
     *  -> `does row i contain black?` is MONOTONIC on each side of x, so it is binary
     *     searchable. Same argument for columns around y.
     *
     *  Each predicate check costs a full row/column scan, giving less than O(mn) overall.
     *
     *  time  = O(m * log n + n * log m)
     *  space = O(1)
     */
    public int minArea(char[][] image, int x, int y) {
        // edge
        if (image == null || image.length == 0 || image[0].length == 0) {
            return 0;
        }

        int m = image.length;
        int n = image[0].length;

        // top = SMALLEST row index in [0, x] that has a black pixel
        int lo = 0;
        int hi = x;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (rowHasBlack(image, mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        int top = lo;

        /** NOTE !!!
         *
         *  bottom = LARGEST row index in [x, m-1] that has a black pixel.
         *  here we must use the UPPER mid `(lo + hi + 1) / 2`,
         *  otherwise `lo = mid` never advances -> INFINITE LOOP
         */
        lo = x;
        hi = m - 1;
        while (lo < hi) {
            int mid = (lo + hi + 1) / 2;
            if (rowHasBlack(image, mid)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        int bottom = lo;

        // left = SMALLEST col index in [0, y] that has a black pixel
        lo = 0;
        hi = y;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (colHasBlack(image, mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        int left = lo;

        // right = LARGEST col index in [y, n-1] that has a black pixel
        lo = y;
        hi = n - 1;
        while (lo < hi) {
            int mid = (lo + hi + 1) / 2;
            if (colHasBlack(image, mid)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        int right = lo;

        return (bottom - top + 1) * (right - left + 1);
    }

    private boolean rowHasBlack(char[][] image, int i) {
        for (char c : image[i]) {
            if (c == '1') {
                return true;
            }
        }
        return false;
    }

    private boolean colHasBlack(char[][] image, int j) {
        for (int i = 0; i < image.length; i++) {
            if (image[i][j] == '1') {
                return true;
            }
        }
        return false;
    }

}
