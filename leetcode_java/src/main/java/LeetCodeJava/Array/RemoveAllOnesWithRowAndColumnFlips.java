package LeetCodeJava.Array;

// https://leetcode.com/problems/remove-all-ones-with-row-and-column-flips/description/
// https://leetcode.ca/2021-09-27-2128-Remove-All-Ones-With-Row-and-Column-Flips/

/**
 * 2128. Remove All Ones With Row and Column Flips
 * Medium
 *
 *
 * You are given an m x n binary matrix grid.
 *
 * In one operation, you can choose any row or column and flip each value in that row or column (i.e., changing all 0's to 1's, and all 1's to 0's).
 *
 * Return true if it is possible to remove all 1's from grid using any number of operations or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,0],[1,0,1],[0,1,0]]
 * Output: true
 * Explanation: One possible way to remove all 1's from grid is to:
 * - Flip the middle row
 * - Flip the middle column
 *
 * Example 2:
 *
 * Input: grid = [[1,1,0],[0,0,0],[0,0,0]]
 * Output: false
 * Explanation: It is impossible to remove all 1's from grid.
 *
 * Example 3:
 *
 * Input: grid = [[0]]
 * Output: true
 * Explanation: There are no 1's in grid and it is already all 0's.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 300
 * grid[i][j] is either 0 or 1.
 *
 */
public class RemoveAllOnesWithRowAndColumnFlips {

    // V0
    // IDEA: EVERY ROW MUST EQUAL ROW 0 OR ITS EXACT COMPLEMENT
    /**
     *  The column flips are forced: they must turn row 0 into all zeros (flip
     *  column j iff grid[0][j] == 1). After those flips every other row has to be
     *  all 0s (done) or all 1s (one row flip clears it), which is exactly
     *  "row == row 0" or "row == complement of row 0".
     *
     * time = O(M * N)
     * space = O(1)
     */
    public boolean removeOnes(int[][] grid) {
        int[] first = grid[0];
        for (int[] row : grid) {
            boolean sameAsFirst = true;
            boolean complementOfFirst = true;
            for (int c = 0; c < first.length; c++) {
                if (row[c] != first[c]) {
                    sameAsFirst = false;
                } else {
                    complementOfFirst = false;
                }
            }
            if (!sameAsFirst && !complementOfFirst) {
                return false;
            }
        }
        return true;
    }
}
