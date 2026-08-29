package LeetCodeJava.Array;

import java.util.Arrays;

// https://leetcode.com/problems/lonely-pixel-i/

/**
 *  531. Lonely Pixel I
 *  Medium
 *
 *  Given an m x n picture consisting of black 'B' and white 'W' pixels,
 *  return the number of black lonely pixels.
 *
 *  A black lonely pixel is a character 'B' that located at a specific position
 *  where the same row and same column don't have any other black pixels.
 *
 *  Example 1:
 *  Input: picture = [["W","W","B"],["W","B","W"],["B","W","W"]]
 *  Output: 3
 *  Explanation: All the three 'B's are black lonely pixels.
 *
 *  Example 2:
 *  Input: picture = [["B","B","B"],["B","B","W"],["B","B","B"]]
 *  Output: 0
 *
 *  Constraints:
 *  m == picture.length
 *  n == picture[i].length
 *  1 <= m, n <= 500
 *  picture[i][j] is 'W' or 'B'.
 */
public class LonelyPixelI {

    // V0
    // IDEA: count 'B' per row and per column, then a 'B' is lonely iff both counts == 1
    /**
     * time = O(m * n)
     * space = O(m + n)
     */
    public int findLonelyPixel(char[][] picture) {
        if (picture == null || picture.length == 0 || picture[0].length == 0) {
            return 0;
        }
        int m = picture.length;
        int n = picture[0].length;
        int[] rows = new int[m];
        int[] cols = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }
        int res = 0;
        for (int i = 0; i < m; i++) {
            if (rows[i] != 1) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B' && cols[j] == 1) {
                    res++;
                }
            }
        }
        return res;
    }


    // V1
    // IDEA: brute force O(m*n*(m+n)) - for every 'B' rescan its own row and column.
    //       Kept as a readable correctness reference for the counting solution above.
    /**
     * time = O(m * n * (m + n))
     * space = O(1)
     */
    public int findLonelyPixel_1(char[][] picture) {
        if (picture == null || picture.length == 0 || picture[0].length == 0) {
            return 0;
        }
        int m = picture.length;
        int n = picture[0].length;
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] != 'B') {
                    continue;
                }
                boolean lonely = true;
                for (int c = 0; c < n && lonely; c++) {
                    if (c != j && picture[i][c] == 'B') {
                        lonely = false;
                    }
                }
                for (int r = 0; r < m && lonely; r++) {
                    if (r != i && picture[r][j] == 'B') {
                        lonely = false;
                    }
                }
                if (lonely) {
                    res++;
                }
            }
        }
        return res;
    }

    // V2
    // IDEA: store the POSITION of the unique 'B' per row / per column instead of a
    //       count (-1 = none, -2 = more than one). A pixel is lonely iff the row
    //       record and the column record point at each other, so the final pass is
    //       O(m) and never touches the grid again.
    /**
     * time = O(m * n)
     * space = O(m + n)
     */
    public int findLonelyPixel_2(char[][] picture) {
        if (picture == null || picture.length == 0 || picture[0].length == 0) {
            return 0;
        }
        int m = picture.length;
        int n = picture[0].length;

        int[] rowMark = new int[m];
        int[] colMark = new int[n];
        Arrays.fill(rowMark, -1);
        Arrays.fill(colMark, -1);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] != 'B') {
                    continue;
                }
                rowMark[i] = (rowMark[i] == -1) ? j : -2;
                colMark[j] = (colMark[j] == -1) ? i : -2;
            }
        }

        int res = 0;
        for (int i = 0; i < m; i++) {
            int j = rowMark[i];
            if (j >= 0 && colMark[j] == i) {
                res++;
            }
        }
        return res;
    }
}
