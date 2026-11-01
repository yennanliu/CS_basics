package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-number-of-flips-to-make-binary-grid-palindromic-ii/description/

/**
 * 3240. Minimum Number of Flips to Make Binary Grid Palindromic II
 * Medium
 *
 * You are given an m x n binary matrix grid.
 *
 * A row or column is considered palindromic if its values read the same forward and backward.
 *
 * You can flip any number of cells in grid from 0 to 1, or from 1 to 0.
 *
 * Return the minimum number of cells that need to be flipped to make all rows and columns palindromic, and the total number of 1's in grid divisible by 4.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,0],[0,1,0],[0,0,1]]
 * Output: 3
 * Explanation:
 *
 * Example 2:
 *
 * Input: grid = [[0,1],[0,1],[0,0]]
 * Output: 2
 * Explanation:
 *
 * Example 3:
 *
 * Input: grid = [[1],[1]]
 * Output: 2
 * Explanation:
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m * n <= 2 * 10^5
 *
 */
public class MinimumNumberOfFlipsToMakeBinaryGridPalindromicII {

    // V0
    // IDEA: GROUPS OF FOUR MIRRORED CELLS + MIDDLE ROW / COLUMN PAIRS + CENTRE CELL (parity of 1s mod 4)
    /**
     * time = O(M * N)
     * space = O(1)
     */
    public int minFlips(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int cost = 0;

        // 1) four cells tied together by row AND column symmetry must all agree.
        //    they end with 0 or 4 ones, so they never affect the "divisible by 4" rule.
        for (int r = 0; r < rows / 2; r++) {
            for (int c = 0; c < cols / 2; c++) {
                int ones = grid[r][c] + grid[r][cols - 1 - c]
                        + grid[rows - 1 - r][c] + grid[rows - 1 - r][cols - 1 - c];
                cost += Math.min(ones, 4 - ones);
            }
        }

        // 2) middle row (odd rows) / middle column (odd cols): cells pair up two at a time
        int matchedOnePairs = 0;  // pairs already "1-1" -> contribute 2 ones each
        int mismatchedPairs = 0;  // pairs "0-1" -> cost 1, final value is our choice

        if (rows % 2 == 1) {
            int r = rows / 2;
            for (int c = 0; c < cols / 2; c++) {
                int a = grid[r][c];
                int b = grid[r][cols - 1 - c];
                if (a != b) {
                    mismatchedPairs++;
                } else if (a == 1) {
                    matchedOnePairs++;
                }
            }
        }

        if (cols % 2 == 1) {
            int c = cols / 2;
            for (int r = 0; r < rows / 2; r++) {
                int a = grid[r][c];
                int b = grid[rows - 1 - r][c];
                if (a != b) {
                    mismatchedPairs++;
                } else if (a == 1) {
                    matchedOnePairs++;
                }
            }
        }

        cost += mismatchedPairs;

        // an odd number of 1-1 pairs leaves the total at 2 (mod 4).
        // if some mismatched pair exists, settle it on 1-1 instead (no extra cost);
        // otherwise one 1-1 pair has to be zeroed, costing 2 flips.
        if (matchedOnePairs % 2 == 1 && mismatchedPairs == 0) {
            cost += 2;
        }

        // 3) the very centre cell has no partner, so it must end up 0
        if (rows % 2 == 1 && cols % 2 == 1) {
            cost += grid[rows / 2][cols / 2];
        }

        return cost;
    }
}
