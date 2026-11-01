package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/equal-sum-grid-partition-i/description/

/**
 * 3546. Equal Sum Grid Partition I
 * Medium
 *
 * You are given an m x n matrix grid of positive integers. Your task is to
 * determine if it is possible to make either one horizontal or one vertical cut on
 * the grid such that:
 *
 * Each of the two resulting sections formed by the cut is non-empty.
 * The sum of the elements in both sections is equal.
 *
 * Return true if such a partition exists; otherwise return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,4],[2,3]]
 * Output: true
 * Explanation: A horizontal cut between row 0 and row 1 results in two non-empty
 * sections, each with a sum of 5.
 *
 * Example 2:
 *
 * Input: grid = [[1,3],[2,4]]
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= m == grid.length <= 10^5
 * 1 <= n == grid[i].length <= 10^5
 * 2 <= m * n <= 10^5
 * 1 <= grid[i][j] <= 10^5
 *
 */
public class EqualSumGridPartitionI {

    // V0
    // IDEA: A CUT IS JUST A PREFIX -- COMPARE EVERY PREFIX SUM AGAINST HALF THE TOTAL
    //   a horizontal cut after row i leaves rows 0..i on top, so the two sides are
    //   equal exactly when the prefix of row sums equals total / 2; same for columns.
    //   an odd total can never be split evenly.
    /**
     * time = O(M * N)
     * space = O(M + N)
     */
    public boolean canPartitionGrid(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        long[] rowSums = new long[m];
        long[] colSums = new long[n];
        long total = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                rowSums[r] += grid[r][c];
                colSums[c] += grid[r][c];
                total += grid[r][c];
            }
        }

        // an odd total cannot be split into two equal integer halves
        if (total % 2 != 0) {
            return false;
        }
        long half = total / 2;

        // horizontal cuts: stop before the last row so the bottom part is non-empty
        long running = 0;
        for (int r = 0; r < m - 1; r++) {
            running += rowSums[r];
            if (running == half) {
                return true;
            }
        }

        // vertical cuts: stop before the last column
        running = 0;
        for (int c = 0; c < n - 1; c++) {
            running += colSums[c];
            if (running == half) {
                return true;
            }
        }
        return false;
    }
}
