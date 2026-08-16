package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/smallest-rectangle-enclosing-black-pixels/description/

import java.util.ArrayDeque;
import java.util.Deque;
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


    // V1
    // IDEA: FULL SCAN (track min/max row and column)
    /**
     *  Walk every cell and keep the bounding box of the black pixels.
     *
     *  O(mn), which the problem explicitly asks us to beat -- but it needs neither
     *  the connectivity assumption nor any monotonicity argument, so it is the
     *  oracle the binary-search versions are checked against.
     *
     *  time  = O(m * n)
     *  space = O(1)
     */
    public int minArea_1(char[][] image, int x, int y) {
        int top = Integer.MAX_VALUE;
        int bottom = Integer.MIN_VALUE;
        int left = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j] == '1') {
                    top = Math.min(top, i);
                    bottom = Math.max(bottom, i);
                    left = Math.min(left, j);
                    right = Math.max(right, j);
                }
            }
        }

        if (top == Integer.MAX_VALUE) {
            return 0;
        }
        return (bottom - top + 1) * (right - left + 1);
    }

    // V2
    // IDEA: FLOOD FILL FROM THE GIVEN PIXEL (cost scales with the REGION)
    /**
     *  The black pixels form ONE connected component and we are handed a pixel
     *  inside it, so a DFS from (x, y) visits exactly that component and can track
     *  the bounding box as it goes.
     *
     *  O(size of the region) rather than O(mn) -- the best of the three when the
     *  blob is small, and it uses the connectivity guarantee directly rather than
     *  through a monotonicity argument.
     *
     *  time  = O(size of the black region)
     *  space = O(size of the black region)
     */
    public int minArea_2(char[][] image, int x, int y) {
        int m = image.length;
        int n = image[0].length;
        boolean[][] seen = new boolean[m][n];
        int[] box = { x, x, y, y }; // top, bottom, left, right

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] { x, y });
        seen[x][y] = true;

        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
        while (!stack.isEmpty()) {
            int[] cur = stack.pop();
            box[0] = Math.min(box[0], cur[0]);
            box[1] = Math.max(box[1], cur[0]);
            box[2] = Math.min(box[2], cur[1]);
            box[3] = Math.max(box[3], cur[1]);

            for (int[] d : dirs) {
                int nr = cur[0] + d[0];
                int nc = cur[1] + d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n
                        && image[nr][nc] == '1' && !seen[nr][nc]) {
                    seen[nr][nc] = true;
                    stack.push(new int[] { nr, nc });
                }
            }
        }

        return (box[1] - box[0] + 1) * (box[3] - box[2] + 1);
    }

    // V3
    // IDEA: BINARY SEARCH, BUT SCAN COLUMNS ONLY INSIDE THE ROW BAND
    /**
     *  V0 probes a column over the FULL height m every time. Once the top and
     *  bottom rows are known, a column can only hold black pixels inside that band,
     *  so the column predicate only has to scan (bottom - top + 1) cells.
     *
     *  -> the column phase costs O((bottom - top) * log n) instead of O(m log n),
     *     a real win for a wide, short blob.
     *
     *  time  = O(m log n + (bottom - top) * log m)
     *  space = O(1)
     */
    public int minArea_3(char[][] image, int x, int y) {
        int m = image.length;
        int n = image[0].length;

        int top = firstRow(image, 0, x, true);
        int bottom = firstRow(image, x, m - 1, false);

        int left = firstColBand(image, 0, y, top, bottom, true);
        int right = firstColBand(image, y, n - 1, top, bottom, false);

        return (bottom - top + 1) * (right - left + 1);
    }

    /** smallest (wantLow) or largest row in [lo, hi] holding a black pixel */
    private int firstRow(char[][] image, int lo, int hi, boolean wantLow) {
        while (lo < hi) {
            int mid = wantLow ? (lo + hi) / 2 : (lo + hi + 1) / 2;
            boolean has = false;
            for (char c : image[mid]) {
                if (c == '1') {
                    has = true;
                    break;
                }
            }
            if (wantLow) {
                if (has) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            } else {
                if (has) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
        }
        return lo;
    }

    /** same for columns, scanning only rows [top, bottom] */
    private int firstColBand(char[][] image, int lo, int hi, int top, int bottom,
                             boolean wantLow) {
        while (lo < hi) {
            int mid = wantLow ? (lo + hi) / 2 : (lo + hi + 1) / 2;
            boolean has = false;
            for (int i = top; i <= bottom; i++) {
                if (image[i][mid] == '1') {
                    has = true;
                    break;
                }
            }
            if (wantLow) {
                if (has) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            } else {
                if (has) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
        }
        return lo;
    }

}
