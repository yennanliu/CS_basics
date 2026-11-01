package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-amount-of-money-robot-can-earn/description/

/**
 * 3418. Maximum Amount of Money Robot Can Earn
 * Medium
 *
 * You are given an m x n grid. A robot starts at the top-left corner of the grid (0, 0)
 * and wants to reach the bottom-right corner (m - 1, n - 1). The robot can move either
 * right or down at any point in time.
 *
 * The grid contains a value coins[i][j] in each cell:
 *
 * If coins[i][j] >= 0, the robot gains that many coins.
 * If coins[i][j] < 0, the robot encounters a robber, and the robber steals the absolute
 * value of coins[i][j] coins.
 *
 * The robot has a special ability to neutralize robbers in at most 2 cells on its path,
 * preventing them from stealing coins in those cells.
 *
 * Note: The robot's total coins can be negative.
 *
 * Return the maximum profit the robot can gain on the route.
 *
 *
 * Example 1:
 *
 * Input: coins = [[0,1,-1],[1,-2,3],[2,-3,4]]
 * Output: 8
 * Explanation: Path (0,0) -> (0,1) -> (1,1) [neutralized] -> (1,2) -> (2,2),
 * collecting 0 + 1 + 0 + 3 + 4 = 8.
 *
 * Example 2:
 *
 * Input: coins = [[10,10,10],[10,10,10]]
 * Output: 40
 *
 *
 * Constraints:
 *
 * m == coins.length
 * n == coins[i].length
 * 1 <= m, n <= 500
 * -1000 <= coins[i][j] <= 1000
 *
 */
public class MaximumAmountOfMoneyRobotCanEarn {

    private static final int MAX_SHIELDS = 2;
    private static final int UNREACHABLE = Integer.MIN_VALUE;

    // V0
    // IDEA: GRID DP WITH "NEUTRALISATIONS USED" AS A THIRD AXIS (ROLLING ROW)
    /**
     *  DP def:
     *     - dp[i][j][k] = max coins on arriving at (i, j) having used k shields, k in {0,1,2}
     *
     *  DP eq:
     *     - pay the cell:    dp[i][j][k] = max(dp[i-1][j][k],   dp[i][j-1][k])   + coins[i][j]
     *     - shield the cell: dp[i][j][k] = max(dp[i-1][j][k-1], dp[i][j-1][k-1])
     *     (shielding a positive cell is allowed but never wins, so robbers need no special case)
     *
     *  NOTE !!! this is a MAX and totals can be negative, so "unreachable" must be
     *           -infinity, not 0 -- otherwise a phantom free path leaks in.
     *
     *  Only the previous row is read, so one rolling row of triples suffices.
     *
     *  time  = O(M * N)
     *  space = O(N)
     */
    public int maximumAmount(int[][] coins) {
        int rows = coins.length;
        int cols = coins[0].length;

        int[][] prevRow = newRow(cols);
        for (int i = 0; i < rows; i++) {
            int[][] curRow = newRow(cols);
            for (int j = 0; j < cols; j++) {
                int value = coins[i][j];

                // start cell: either pay it, or shield it (collect 0)
                if (i == 0 && j == 0) {
                    curRow[0][0] = value;
                    curRow[0][1] = 0;
                    curRow[0][2] = 0;
                    continue;
                }

                for (int k = 0; k <= MAX_SHIELDS; k++) {
                    // pay this cell, shields used unchanged
                    int bestBefore = bestPredecessor(prevRow, curRow, i, j, k);
                    if (bestBefore != UNREACHABLE) {
                        curRow[j][k] = bestBefore + value;
                    }
                    // shield this cell: arrive with k-1 used, collect 0
                    if (k > 0) {
                        int bestShield = bestPredecessor(prevRow, curRow, i, j, k - 1);
                        if (bestShield > curRow[j][k]) {
                            curRow[j][k] = bestShield;
                        }
                    }
                }
            }
            prevRow = curRow;
        }

        int answer = UNREACHABLE;
        for (int k = 0; k <= MAX_SHIELDS; k++) {
            answer = Math.max(answer, prevRow[cols - 1][k]);
        }
        return answer;
    }

    /** Best value among the cell above (previous row) and the cell to the left, with k shields used. */
    private int bestPredecessor(int[][] prevRow, int[][] curRow, int i, int j, int k) {
        int best = UNREACHABLE;
        if (i > 0) {
            best = Math.max(best, prevRow[j][k]);
        }
        if (j > 0) {
            best = Math.max(best, curRow[j - 1][k]);
        }
        return best;
    }

    /** A row of cols cells, each holding 3 shield-count states, all unreachable. */
    private int[][] newRow(int cols) {
        int[][] row = new int[cols][MAX_SHIELDS + 1];
        for (int[] cell : row) {
            java.util.Arrays.fill(cell, UNREACHABLE);
        }
        return row;
    }
}
