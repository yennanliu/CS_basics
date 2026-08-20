package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-rows-covered-by-columns/

/**
 *  2397. Maximum Rows Covered by Columns
 *  Medium
 *
 *  You are given an m x n binary matrix matrix and an integer numSelect.
 *
 *  Your goal is to select exactly numSelect distinct columns from matrix such that
 *  you cover as many rows as possible. A row is considered covered if all the 1's
 *  in that row are also part of a column that you have selected. If a row does not
 *  have any 1s, it is also considered covered.
 *
 *  Return the maximum number of rows that can be covered by a set of numSelect
 *  columns.
 *
 *  Example 1:
 *    Input: matrix = [[0,0,0],[1,0,1],[0,1,1],[0,0,1]], numSelect = 2
 *    Output: 3
 *    Explanation: choosing columns {0, 2} covers rows 0, 1 and 3.
 *
 *  Example 2:
 *    Input: matrix = [[1],[0]], numSelect = 1
 *    Output: 2
 *
 *  Constraints:
 *    m == matrix.length
 *    n == matrix[i].length
 *    1 <= m, n <= 12
 *    matrix[i][j] is either 0 or 1
 *    1 <= numSelect <= n
 */
public class MaximumRowsCoveredByColumns {

    // V0
    // IDEA: BITMASK BRUTE FORCE (n <= 12 -> only 4096 column subsets)
    //
    //  encode each row as a bitmask of the columns holding a 1. a row is covered by
    //  a chosen column set `mask` iff (row & mask) == row, i.e. every 1-bit of the
    //  row is also a 1-bit of mask. an all-zero row has mask 0 and is therefore
    //  covered by every selection, exactly as the statement requires.
    //
    //  NOTE: we must pick EXACTLY numSelect columns -> filter subsets by popcount.
    //        (picking fewer can never cover more rows anyway, but the filter keeps
    //        us honest with the statement.)
    /**
     * time = O(2^n * m)
     * space = O(m)
     */
    public int maximumRows(int[][] matrix, int numSelect) {
        int m = matrix.length;
        int n = matrix[0].length;

        int[] rows = new int[m];
        for (int i = 0; i < m; i++) {
            int mask = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    mask |= 1 << j;
                }
            }
            rows[i] = mask;
        }

        int res = 0;
        for (int mask = 0; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) != numSelect) {
                continue;
            }
            int cnt = 0;
            for (int r : rows) {
                if ((r & mask) == r) {
                    cnt++;
                }
            }
            res = Math.max(res, cnt);
        }
        return res;
    }
}
