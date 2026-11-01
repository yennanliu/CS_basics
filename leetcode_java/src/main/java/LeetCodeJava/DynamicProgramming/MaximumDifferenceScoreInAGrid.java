package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-difference-score-in-a-grid/description/

import java.util.*;

/**
 * 3148. Maximum Difference Score in a Grid
 * Medium
 *
 * You are given an m x n matrix grid consisting of positive integers. You can move from a cell in the matrix to any other cell that is either to the bottom or to the right (not necessarily adjacent). The score of a move from a cell with the value c1 to a cell with the value c2 is c2 - c1.
 *
 * You can start at any cell, and you have to make at least one move.
 *
 * Return the maximum total score you can achieve.
 *
 *
 * Example 1:
 *
 * Input: grid = [[9,5,7,3],[8,9,6,1],[6,7,14,3],[2,5,3,1]]
 * Output: 9
 * Explanation: We start at the cell (0, 1), and we perform the following moves:
 * - Move from the cell (0, 1) to (2, 1) with a score of 7 - 5 = 2.
 * - Move from the cell (2, 1) to (2, 2) with a score of 14 - 7 = 7.
 * The total score is 2 + 7 = 9.
 *
 * Example 2:
 *
 * Input: grid = [[4,3,2],[3,2,1]]
 * Output: -1
 * Explanation: We start at the cell (0, 0), and we perform one move: (0, 0) to (0, 1). The score is 3 - 4 = -1.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 2 <= m, n <= 1000
 * 4 <= m * n <= 10^5
 * 1 <= grid[i][j] <= 10^5
 *
 */
public class MaximumDifferenceScoreInAGrid {

    // V0
    // IDEA: PATH SCORES TELESCOPE -> ONLY ENDPOINTS MATTER, 2D PREFIX MINIMUM DP
    //   (c2 - c1) + (c3 - c2) + ... = last - first, so pick a start and a later (down/right)
    //   end that maximise end - start.
    //   minUpLeft[i][j] = min value in the rectangle (0,0)..(i,j)
    //                   = min(grid[i][j], minUpLeft[i-1][j], minUpLeft[i][j-1])
    //   The best move ending at (i, j) uses the min of the two neighbours' rectangles
    //   (strictly up or left), because at least one move is required.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int maxScore(List<List<Integer>> grid) {
        int rows = grid.size();
        int cols = grid.get(0).size();
        final int INF = Integer.MAX_VALUE;
        int[][] minUpLeft = new int[rows][cols];
        int best = Integer.MIN_VALUE;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int value = grid.get(r).get(c);
                // smallest start cell strictly above or strictly left of (r, c)
                int reachableMin = INF;
                if (r > 0) {
                    reachableMin = Math.min(reachableMin, minUpLeft[r - 1][c]);
                }
                if (c > 0) {
                    reachableMin = Math.min(reachableMin, minUpLeft[r][c - 1]);
                }
                if (reachableMin != INF) {
                    best = Math.max(best, value - reachableMin);
                }
                minUpLeft[r][c] = Math.min(value, reachableMin);
            }
        }
        return best;
    }
}
