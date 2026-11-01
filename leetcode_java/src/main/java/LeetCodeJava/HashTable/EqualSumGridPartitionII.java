package LeetCodeJava.HashTable;

// https://leetcode.com/problems/equal-sum-grid-partition-ii/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 3548. Equal Sum Grid Partition II
 * Hard
 *
 * You are given an m x n matrix grid of positive integers. Your task is to
 * determine if it is possible to make either one horizontal or one vertical cut on
 * the grid such that:
 *
 * Each of the two resulting sections formed by the cut is non-empty.
 * The sum of elements in both sections is equal, or can be made equal by
 * discounting at most one single cell in total (from either section).
 * If a cell is discounted, the rest of the section must remain connected.
 *
 * Return true if such a partition exists; otherwise, return false.
 *
 * Note: A section is connected if every cell in it can be reached from any other
 * cell by moving up, down, left, or right through other cells in the section.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,4],[2,3]]
 * Output: true
 *
 * Example 2:
 *
 * Input: grid = [[1,2],[3,4]]
 * Output: true
 * Explanation: A vertical cut gives sums 4 and 6; discounting 2 from the right
 * section (6 - 2 = 4) makes them equal and it stays connected.
 *
 * Example 3:
 *
 * Input: grid = [[1,2,4],[2,3,5]]
 * Output: false
 * Explanation: Discounting 3 from the bottom section would split it into [2] and [5].
 *
 * Example 4:
 *
 * Input: grid = [[4,1,8],[3,2,6]]
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
public class EqualSumGridPartitionII {

    // V0
    // IDEA: SWEEP EVERY HORIZONTAL CUT WITH TWO VALUE->COUNT MAPS (+ SAME ON THE TRANSPOSE)
    //   after a cut, diff = heavy - light; discounting one cell of value diff from the
    //   heavier piece balances it, so each cut is one membership test.
    //   connectivity: a piece with >= 2 rows AND >= 2 columns stays connected whichever
    //   cell is removed; a one-row / one-column strip only survives losing an END cell.
    //   the top map grows and the bottom map shrinks as the cut moves down.
    //   vertical cuts are the same sweep over the transposed grid.
    //   (brute force -- re-summing both pieces per cut -- is O((m+n)*m*n) and times out.)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public boolean canPartitionGrid(int[][] grid) {
        if (scanHorizontalCuts(grid)) {
            return true;
        }
        return scanHorizontalCuts(transpose(grid));
    }

    /** Tries every horizontal cut of g, allowing one connectivity-safe discount. */
    private boolean scanHorizontalCuts(int[][] g) {
        int rows = g.length;
        int cols = g[0].length;

        long total = 0;
        Map<Integer, Integer> topCount = new HashMap<>();     // values in rows 0..i
        Map<Integer, Integer> bottomCount = new HashMap<>();  // values in rows i+1..rows-1
        for (int[] row : g) {
            for (int value : row) {
                total += value;
                bottomCount.put(value, bottomCount.getOrDefault(value, 0) + 1);
            }
        }

        long topSum = 0;
        for (int i = 0; i < rows - 1; i++) {
            // move row i from the bottom piece into the top piece
            for (int value : g[i]) {
                topSum += value;
                topCount.put(value, topCount.getOrDefault(value, 0) + 1);
                int left = bottomCount.get(value) - 1;
                if (left == 0) {
                    bottomCount.remove(value);
                } else {
                    bottomCount.put(value, left);
                }
            }

            long bottomSum = total - topSum;
            long diff = topSum - bottomSum;
            if (diff == 0) {
                return true;
            }

            if (diff > 0) {
                // discount a cell from the top piece (rows 0..i)
                int topRows = i + 1;
                if (topRows >= 2 && cols >= 2) {
                    if (diff <= Integer.MAX_VALUE && topCount.containsKey((int) diff)) {
                        return true;
                    }
                } else if (diff == g[0][0] || diff == g[i][cols - 1]) {
                    // a strip: only one of its two end cells can go
                    return true;
                }
            } else {
                // discount a cell from the bottom piece (rows i+1..rows-1)
                long need = -diff;
                int bottomRows = rows - 1 - i;
                if (bottomRows >= 2 && cols >= 2) {
                    if (need <= Integer.MAX_VALUE && bottomCount.containsKey((int) need)) {
                        return true;
                    }
                } else if (need == g[i + 1][0] || need == g[rows - 1][cols - 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Returns the transpose, so vertical cuts can reuse the horizontal sweep. */
    private int[][] transpose(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] result = new int[n][m];
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                result[c][r] = grid[r][c];
            }
        }
        return result;
    }
}
