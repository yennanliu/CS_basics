package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/cherry-pickup-ii/description/

import java.util.Arrays;

/**
 * 1463. Cherry Pickup II
 * Hard
 *
 * You are given a rows x cols matrix grid representing a field of cherries where grid[i][j] represents the number of cherries that you can collect from the (i, j) cell.
 *
 * You have two robots that can collect cherries for you:
 *
 * Robot #1 is located at the top-left corner (0, 0), and
 * Robot #2 is located at the top-right corner (0, cols - 1).
 *
 * Return the maximum number of cherries collection using both robots by following the rules below:
 *
 * From a cell (i, j), robots can move to cell (i + 1, j - 1), (i + 1, j), or (i + 1, j + 1).
 * When any robot passes through a cell, It picks up all cherries, and the cell becomes an empty cell.
 * When both robots stay in the same cell, only one takes the cherries.
 * Both robots cannot move outside of the grid at any moment.
 * Both robots should reach the bottom row in grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[3,1,1],[2,5,1],[1,5,5],[2,1,1]]
 * Output: 24
 * Explanation: Path of robot #1 and #2 are described in color green and blue respectively.
 * Cherries taken by Robot #1, (3 + 2 + 5 + 2) = 12.
 * Cherries taken by Robot #2, (1 + 5 + 5 + 1) = 12.
 * Total of cherries: 12 + 12 = 24.
 *
 * Example 2:
 *
 * Input: grid = [[1,0,0,0,0,0,1],[2,0,0,0,0,3,0],[2,0,9,0,0,0,0],[0,3,0,5,4,0,0],[1,0,2,3,0,0,6]]
 * Output: 28
 * Explanation: Path of robot #1 and #2 are described in color green and blue respectively.
 * Cherries taken by Robot #1, (1 + 9 + 5 + 2) = 17.
 * Cherries taken by Robot #2, (1 + 3 + 4 + 3) = 11.
 * Total of cherries: 17 + 11 = 28.
 *
 *
 * Constraints:
 *
 * rows == grid.length
 * cols == grid[i].length
 * 2 <= rows, cols <= 70
 * 0 <= grid[i][j] <= 100
 *
 */
public class CherryPickupII {

    private static final int NEG = Integer.MIN_VALUE / 2;

    // V0
    // IDEA: DP ON BOTH ROBOTS AT ONCE (they always share the same row)
    //       both robots move down exactly one row per step, so the state is (row, col1, col2).
    //       dp[c1][c2] = most cherries after the current row with the robots at c1 and c2.
    //       each robot came from one of 3 columns -> 9 predecessors.
    //       when c1 == c2 the cell is counted ONCE.
    /**
     * time = O(R * C^2 * 9)
     * space = O(C^2)
     */
    public int cherryPickup(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // row 0: robot 1 at column 0, robot 2 at column cols-1; every other pair unreachable
        int[][] dp = new int[cols][cols];
        for (int[] line : dp) {
            Arrays.fill(line, NEG);
        }
        dp[0][cols - 1] = (cols == 1) ? grid[0][0] : grid[0][0] + grid[0][cols - 1];

        for (int r = 1; r < rows; r++) {
            int[][] next = new int[cols][cols];
            for (int[] line : next) {
                Arrays.fill(line, NEG);
            }
            for (int c1 = 0; c1 < cols; c1++) {
                for (int c2 = 0; c2 < cols; c2++) {
                    // cherries collected on this row; a shared cell is picked once
                    int gain = grid[r][c1];
                    if (c1 != c2) {
                        gain += grid[r][c2];
                    }

                    // best reachable predecessor among the 3 x 3 previous column pairs
                    int best = NEG;
                    for (int prev1 = c1 - 1; prev1 <= c1 + 1; prev1++) {
                        for (int prev2 = c2 - 1; prev2 <= c2 + 1; prev2++) {
                            if (prev1 < 0 || prev1 >= cols || prev2 < 0 || prev2 >= cols) {
                                continue;
                            }
                            best = Math.max(best, dp[prev1][prev2]);
                        }
                    }
                    if (best != NEG) {
                        next[c1][c2] = best + gain;
                    }
                }
            }
            dp = next;
        }

        int answer = 0;
        for (int c1 = 0; c1 < cols; c1++) {
            for (int c2 = 0; c2 < cols; c2++) {
                answer = Math.max(answer, dp[c1][c2]);
            }
        }
        return answer;
    }
}
