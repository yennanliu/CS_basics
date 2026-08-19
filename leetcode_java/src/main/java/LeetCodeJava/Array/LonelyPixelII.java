package LeetCodeJava.Array;

// https://leetcode.com/problems/lonely-pixel-ii/

import java.util.HashMap;
import java.util.Map;

/**
 *  533. Lonely Pixel II
 *  Medium
 *
 *  Given an m x n picture consisting of black 'B' and white 'W' pixels and an
 *  integer target, return the number of black lonely pixels.
 *
 *  A black lonely pixel is a character 'B' at location (r, c) such that:
 *    - Row r and column c both contain exactly target black pixels.
 *    - For all rows that have a black pixel at column c, they should be exactly
 *      the same as row r.
 *
 *  Example 1:
 *  Input: picture = [["W","B","W","B","B","W"],
 *                    ["W","B","W","B","B","W"],
 *                    ["W","B","W","B","B","W"],
 *                    ["W","W","B","W","B","W"]], target = 3
 *  Output: 6
 *
 *  Example 2:
 *  Input: picture = [["W","W","B"],["W","W","B"],["W","W","B"]], target = 1
 *  Output: 0
 *
 *  Constraints:
 *  m == picture.length
 *  n == picture[i].length
 *  1 <= m, n <= 200
 *  picture[i][j] is 'W' or 'B'.
 *  1 <= target <= min(m, n)
 */
public class LonelyPixelII {

    // V0
    // IDEA: count 'B' per row / col, plus a count of identical row strings.
    //       A row qualifies iff it has exactly `target` B's AND appears exactly
    //       `target` times (that guarantees every row with a B in column c is identical).
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    public int findBlackPixel(char[][] picture, int target) {
        if (picture == null || picture.length == 0 || picture[0].length == 0) {
            return 0;
        }
        int m = picture.length;
        int n = picture[0].length;

        int[] rows = new int[m];
        int[] cols = new int[n];
        Map<String, Integer> rowCount = new HashMap<>();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B') {
                    rows[i]++;
                    cols[j]++;
                }
            }
            String key = new String(picture[i]);
            Integer c = rowCount.get(key);
            rowCount.put(key, c == null ? 1 : c + 1);
        }

        int res = 0;
        for (int i = 0; i < m; i++) {
            if (rows[i] != target) {
                continue;
            }
            String key = new String(picture[i]);
            Integer cnt = rowCount.get(key);
            if (cnt == null || cnt != target) {
                continue;
            }
            for (int j = 0; j < n; j++) {
                if (picture[i][j] == 'B' && cols[j] == target) {
                    res++;
                }
            }
        }
        return res;
    }
}
