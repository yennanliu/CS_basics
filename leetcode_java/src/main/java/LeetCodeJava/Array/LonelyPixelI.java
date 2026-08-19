package LeetCodeJava.Array;

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
}
