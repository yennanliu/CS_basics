package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/knight-probability-in-chessboard/

/**
 *  688. Knight Probability in Chessboard
 *  Medium
 *
 *  On an n x n chessboard, a knight starts at the cell (row, column) and
 *  attempts to make exactly k moves. The rows and columns are 0-indexed,
 *  so the top-left cell is (0, 0) and the bottom-right cell is (n - 1, n - 1).
 *
 *  A chess knight has eight possible moves it can make. Each move is two
 *  cells in a cardinal direction, then one cell in an orthogonal direction.
 *
 *  Each time the knight is to move, it chooses one of eight possible moves
 *  uniformly at random (even if the piece would go off the chessboard) and
 *  moves there. The knight continues moving until it has made exactly k moves
 *  or has moved off the chessboard.
 *
 *  Return the probability that the knight remains on the board after it has
 *  stopped moving.
 *
 *  Example 1:
 *    Input: n = 3, k = 2, row = 0, column = 0
 *    Output: 0.06250
 *
 *  Example 2:
 *    Input: n = 1, k = 0, row = 0, column = 0
 *    Output: 1.00000
 *
 *  Constraints:
 *    - 1 <= n <= 25
 *    - 0 <= k <= 100
 *    - 0 <= row, column <= n - 1
 */
public class KnightProbabilityInChessboard {

    private static final int[][] MOVES = {
            {1, 2}, {2, 1}, {2, -1}, {1, -2},
            {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
    };

    // V0
    // IDEA: DP over move count - dp[r][c] = probability the knight is on (r,c) after t moves
    /**
     * time = O(K * N^2)
     * space = O(N^2)
     */
    public double knightProbability(int n, int k, int row, int column) {
        double[][] dp = new double[n][n];
        dp[row][column] = 1.0;

        for (int step = 0; step < k; step++) {
            double[][] next = new double[n][n];
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < n; c++) {
                    if (dp[r][c] == 0.0) {
                        continue;
                    }
                    for (int[] mv : MOVES) {
                        int nr = r + mv[0];
                        int nc = c + mv[1];
                        if (nr >= 0 && nr < n && nc >= 0 && nc < n) {
                            next[nr][nc] += dp[r][c] / 8.0;
                        }
                    }
                }
            }
            dp = next;
        }

        double res = 0.0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                res += dp[r][c];
            }
        }
        return res;
    }

    // V1
    // IDEA: TOP-DOWN MEMOIZATION on (movesLeft, r, c)
    /**
     * time = O(K * N^2)
     * space = O(K * N^2)
     */
    public double knightProbability_1(int n, int k, int row, int column) {
        double[][][] memo = new double[k + 1][n][n];
        boolean[][][] seen = new boolean[k + 1][n][n];
        return helper(n, k, row, column, memo, seen);
    }

    private double helper(int n, int k, int r, int c, double[][][] memo, boolean[][][] seen) {
        if (r < 0 || r >= n || c < 0 || c >= n) {
            return 0.0;
        }
        if (k == 0) {
            return 1.0;
        }
        if (seen[k][r][c]) {
            return memo[k][r][c];
        }
        double res = 0.0;
        for (int[] mv : MOVES) {
            res += helper(n, k - 1, r + mv[0], c + mv[1], memo, seen) / 8.0;
        }
        seen[k][r][c] = true;
        memo[k][r][c] = res;
        return res;
    }
}
