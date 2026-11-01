package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/remove-all-ones-with-row-and-column-flips-ii/description/

import java.util.*;

/**
 * 2174. Remove All Ones With Row and Column Flips II
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given a 0-indexed m x n binary matrix grid.
 *
 * In one operation, you can choose any i and j that meet the following conditions:
 *
 * 0 <= i < m
 * 0 <= j < n
 * grid[i][j] == 1
 *
 * and change the values of all cells in row i and column j to zero.
 *
 * Return the minimum number of operations needed to remove all 1's from grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,1],[1,1,1],[0,1,0]]
 * Output: 2
 * Explanation:
 * In the first operation, change all cell values of row 1 and column 1 to zero.
 * In the second operation, change all cell values of row 0 and column 0 to zero.
 *
 * Example 2:
 *
 * Input: grid = [[0,1,0],[1,0,1],[0,1,0]]
 * Output: 2
 * Explanation:
 * In the first operation, change all cell values of row 1 and column 1 to zero.
 * In the second operation, change all cell values of row 2 and column 2 to zero.
 * Note that we cannot perform an operation using row 1 and column 1 because grid[1][1] != 1.
 *
 * Example 3:
 *
 * Input: grid = [[0,0],[0,0]]
 * Output: 0
 * Explanation:
 * There are no 1's to remove so return zero.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 15
 * 1 <= m * n <= 15
 * grid[i][j] is either 0 or 1.
 *
 */
public class RemoveAllOnesWithRowAndColumnFlipsII {

    // V0
    // IDEA: WHOLE GRID AS A BITMASK (m * n <= 15) + MEMOISED SEARCH OVER STATES
    /**
     * time = O(2^(M*N) * M * N)
     * space = O(2^(M*N))
     */
    public int removeOnes(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int cells = rows * cols;

        // encode the grid: cell (i, j) is bit (i * cols + j)
        int start = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    start |= 1 << (i * cols + j);
                }
            }
        }

        // keepMask[c] = AND-mask that wipes cell c's whole row and column
        int allBits = (1 << cells) - 1;
        int[] keepMask = new int[cells];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int wipe = 0;
                for (int y = 0; y < cols; y++) {
                    wipe |= 1 << (i * cols + y);
                }
                for (int x = 0; x < rows; x++) {
                    wipe |= 1 << (x * cols + j);
                }
                keepMask[i * cols + j] = allBits & ~wipe;
            }
        }

        // memo[state] = min operations to clear `state`; -1 = not computed yet
        int[] memo = new int[1 << cells];
        Arrays.fill(memo, -1);
        memo[0] = 0;
        return solve(start, cells, keepMask, memo);
    }

    /** Min operations to clear the grid described by state. */
    private int solve(int state, int cells, int[] keepMask, int[] memo) {
        if (memo[state] != -1) {
            return memo[state];
        }
        int best = Integer.MAX_VALUE;
        // an operation may only be applied at a cell that currently holds a 1
        for (int c = 0; c < cells; c++) {
            boolean isOne = ((state >> c) & 1) == 1;
            if (isOne) {
                best = Math.min(best, 1 + solve(state & keepMask[c], cells, keepMask, memo));
            }
        }
        memo[state] = best;
        return best;
    }
}
