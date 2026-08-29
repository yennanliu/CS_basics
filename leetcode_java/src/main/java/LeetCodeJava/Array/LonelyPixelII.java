package LeetCodeJava.Array;

// https://leetcode.com/problems/lonely-pixel-ii/

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

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


    // V1
    // IDEA: COLUMN driven - for each column holding exactly `target` B's, take the
    //       rows that own them; if those rows are all identical and each has exactly
    //       `target` B's, that column contributes `target` lonely pixels.
    //       No hashing of rows needed.
    /**
     * time = O(m * n * target)
     * space = O(m)
     */
    public int findBlackPixel_1(char[][] picture, int target) {
        if (picture == null || picture.length == 0 || picture[0].length == 0) {
            return 0;
        }
        int m = picture.length;
        int n = picture[0].length;
        int res = 0;

        for (int c = 0; c < n; c++) {
            List<Integer> owners = new ArrayList<>();
            for (int r = 0; r < m; r++) {
                if (picture[r][c] == 'B') {
                    owners.add(r);
                }
            }
            if (owners.size() != target) {
                continue;
            }
            int first = owners.get(0);
            int cnt = 0;
            for (int j = 0; j < n; j++) {
                if (picture[first][j] == 'B') {
                    cnt++;
                }
            }
            if (cnt != target) {
                continue;
            }
            boolean allSame = true;
            for (int k = 1; k < owners.size() && allSame; k++) {
                int r = owners.get(k);
                for (int j = 0; j < n; j++) {
                    if (picture[r][j] != picture[first][j]) {
                        allSame = false;
                        break;
                    }
                }
            }
            if (allSame) {
                res += target;
            }
        }
        return res;
    }

    // V2
    // IDEA: brute force - transcribe the definition literally for every 'B' pixel
    //       (row count, column count, and "every row with a B in this column equals
    //       my row"). Kept as a readable correctness reference.
    /**
     * time = O(m^2 * n^2)
     * space = O(1)
     */
    public int findBlackPixel_2(char[][] picture, int target) {
        if (picture == null || picture.length == 0 || picture[0].length == 0) {
            return 0;
        }
        int m = picture.length;
        int n = picture[0].length;
        int res = 0;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (picture[r][c] != 'B') {
                    continue;
                }
                int rowCnt = 0;
                for (int j = 0; j < n; j++) {
                    if (picture[r][j] == 'B') {
                        rowCnt++;
                    }
                }
                if (rowCnt != target) {
                    continue;
                }
                int colCnt = 0;
                for (int i = 0; i < m; i++) {
                    if (picture[i][c] == 'B') {
                        colCnt++;
                    }
                }
                if (colCnt != target) {
                    continue;
                }
                boolean ok = true;
                for (int i = 0; i < m && ok; i++) {
                    if (i == r || picture[i][c] != 'B') {
                        continue;
                    }
                    for (int j = 0; j < n; j++) {
                        if (picture[i][j] != picture[r][j]) {
                            ok = false;
                            break;
                        }
                    }
                }
                if (ok) {
                    res++;
                }
            }
        }
        return res;
    }
}
