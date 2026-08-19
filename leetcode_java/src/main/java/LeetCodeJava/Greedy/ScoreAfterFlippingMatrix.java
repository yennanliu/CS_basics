package LeetCodeJava.Greedy;

// https://leetcode.com/problems/score-after-flipping-matrix/

/**
 *  861. Score After Flipping Matrix
 *  Medium
 *
 *  You are given an m x n binary matrix grid.
 *
 *  A move consists of choosing any row or column and toggling each value in that
 *  row or column (i.e., changing all 0's to 1's, and all 1's to 0's).
 *
 *  Every row of the matrix is interpreted as a binary number, and the score of the
 *  matrix is the sum of these numbers.
 *
 *  Return the highest possible score after making any number of moves (including zero).
 *
 *  Example 1:
 *    Input: grid = [[0,0,1,1],[1,0,1,0],[1,1,0,0]]
 *    Output: 39   (0b1111 + 0b1001 + 0b1111 = 15 + 9 + 15)
 *
 *  Example 2:
 *    Input: grid = [[0]]
 *    Output: 1
 *
 *  Constraints:
 *    m == grid.length
 *    n == grid[i].length
 *    1 <= m, n <= 20
 *    grid[i][j] is either 0 or 1.
 */
public class ScoreAfterFlippingMatrix {

    // V0
    // IDEA: greedy, bit by bit.
    //       (1) row flips: make the most significant bit of every row a 1 (worth 2^(n-1) each).
    //       (2) column flips: for every other column keep whichever of {ones, zeros} is larger.
    //       A cell "is 1 after the row flip" iff it equals that row's first column value.
    /**
     * time = O(m * n)
     * space = O(1)
     */
    public int matrixScore(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // after row flips every row starts with 1
        int score = m * (1 << (n - 1));

        for (int c = 1; c < n; c++) {
            int ones = 0;
            for (int r = 0; r < m; r++) {
                // grid[r][c] becomes 1 exactly when it matches grid[r][0]
                if (grid[r][c] == grid[r][0]) {
                    ones++;
                }
            }
            // optionally flip this column
            score += Math.max(ones, m - ones) * (1 << (n - 1 - c));
        }
        return score;
    }
}
