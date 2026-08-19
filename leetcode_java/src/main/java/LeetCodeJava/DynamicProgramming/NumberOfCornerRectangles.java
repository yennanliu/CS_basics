package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-corner-rectangles/

import java.util.HashMap;
import java.util.Map;

/**
 *  750. Number Of Corner Rectangles
 *  Medium
 *
 *  Given an m x n integer matrix grid where each entry is only 0 or 1,
 *  return the number of corner rectangles.
 *
 *  A corner rectangle is four distinct 1's on the grid that forms an
 *  axis-aligned rectangle. Note that only the corners need to have the
 *  value 1. Also, all four 1's used must be distinct.
 *
 *  Example 1:
 *    Input: grid = [[1,0,0,1,0],[0,0,1,0,1],[0,0,0,1,0],[1,0,1,0,1]]
 *    Output: 1
 *    Explanation: There is only one corner rectangle, with corners
 *                 grid[1][2], grid[1][4], grid[3][2], grid[3][4].
 *
 *  Example 2:
 *    Input: grid = [[1,1,1],[1,1,1],[1,1,1]]
 *    Output: 9
 *
 *  Constraints:
 *    - m == grid.length
 *    - n == grid[i].length
 *    - 1 <= m, n <= 200
 *    - grid[i][j] is either 0 or 1.
 *    - The number of 1's in the grid is in the range [1, 6000].
 */
public class NumberOfCornerRectangles {

    // V0
    // IDEA: for every pair of rows, count columns that are 1 in BOTH -> C(cnt, 2) rectangles
    /**
     * time = O(M^2 * N)
     * space = O(1)
     */
    public int countCornerRectangles(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        int res = 0;

        for (int r1 = 0; r1 < m; r1++) {
            for (int r2 = r1 + 1; r2 < m; r2++) {
                int cnt = 0;
                for (int c = 0; c < n; c++) {
                    if (grid[r1][c] == 1 && grid[r2][c] == 1) {
                        cnt++;
                    }
                }
                res += cnt * (cnt - 1) / 2;
            }
        }
        return res;
    }

    // V1
    // IDEA: row-by-row counting of column PAIRS seen so far (better when 1's are sparse)
    /**
     * time = O(M * K^2) where K = max number of 1's in a row
     * space = O(N^2)
     */
    public int countCornerRectangles_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int n = grid[0].length;
        Map<Integer, Integer> pairCount = new HashMap<>();
        int res = 0;

        for (int[] row : grid) {
            for (int c1 = 0; c1 < n; c1++) {
                if (row[c1] != 1) {
                    continue;
                }
                for (int c2 = c1 + 1; c2 < n; c2++) {
                    if (row[c2] != 1) {
                        continue;
                    }
                    int key = c1 * n + c2;
                    int seen = pairCount.getOrDefault(key, 0);
                    res += seen;
                    pairCount.put(key, seen + 1);
                }
            }
        }
        return res;
    }

    // V2
    // IDEA: BITSET - pack every row into a long[] bitmap, then AND each row pair and
    //       popcount the overlap, so the inner column scan runs 64 columns at a time
    /**
     * time = O(M^2 * N / 64)
     * space = O(M * N / 64)
     */
    public int countCornerRectangles_2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int m = grid.length;
        int n = grid[0].length;
        int words = (n + 63) / 64;

        long[][] bits = new long[m][words];
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 1) {
                    bits[r][c >> 6] |= 1L << (c & 63);
                }
            }
        }

        int res = 0;
        for (int r1 = 0; r1 < m; r1++) {
            for (int r2 = r1 + 1; r2 < m; r2++) {
                int cnt = 0;
                for (int w = 0; w < words; w++) {
                    cnt += Long.bitCount(bits[r1][w] & bits[r2][w]);
                }
                res += cnt * (cnt - 1) / 2;
            }
        }
        return res;
    }
}
