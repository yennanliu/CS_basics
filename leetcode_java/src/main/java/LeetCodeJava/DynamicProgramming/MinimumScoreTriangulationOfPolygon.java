package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-score-triangulation-of-polygon/description/

import java.util.Arrays;

/**
 * 1039. Minimum Score Triangulation of Polygon
 * Medium
 *
 * You have a convex n-sided polygon where each vertex has an integer value.
 * You are given an integer array values where values[i] is the value of the ith vertex
 * in clockwise order.
 *
 * Polygon triangulation is a process where you divide a polygon into a set of triangles
 * and the vertices of each triangle must also be vertices of the original polygon.
 * Note that no other shapes other than triangles are allowed in the division.
 * This process will result in n - 2 triangles.
 *
 * You will triangulate the polygon. For each triangle, the weight of that triangle is
 * the product of the values at its vertices. The total score of the triangulation is
 * the sum of these weights over all n - 2 triangles.
 *
 * Return the minimum possible score that you can achieve with some triangulation of
 * the polygon.
 *
 *
 * Example 1:
 *
 * Input: values = [1,2,3]
 * Output: 6
 * Explanation: The polygon is already triangulated, and the score of the only
 * triangle is 6.
 *
 * Example 2:
 *
 * Input: values = [3,7,4,5]
 * Output: 144
 * Explanation: There are two triangulations, with possible scores:
 * 3*7*5 + 4*5*7 = 245, or 3*4*5 + 3*4*7 = 144.
 * The minimum score is 144.
 *
 * Example 3:
 *
 * Input: values = [1,3,1,4,1,5]
 * Output: 13
 * Explanation: The minimum score triangulation is
 * 1*1*3 + 1*1*4 + 1*1*5 + 1*1*1 = 13.
 *
 *
 * Constraints:
 *
 * n == values.length
 * 3 <= n <= 50
 * 1 <= values[i] <= 100
 *
 */
public class MinimumScoreTriangulationOfPolygon {

    // V0
    // IDEA: INTERVAL DP (BOTTOM-UP)
    /**
     * dp[i][j] = min score to triangulate the sub-polygon with vertices i, i+1, ..., j
     *            (the edge i-j is one of its sides)
     *
     * dp[i][j] = min over k in (i, j) of
     *              dp[i][k] + dp[k][j] + values[i] * values[k] * values[j]
     *   -> k is the third vertex of the triangle sitting on edge (i, j); that triangle
     *      splits the rest into two independent sub-polygons
     *
     * base: dp[i][j] = 0 when j - i < 2 (a line, not a polygon)
     */
    /**
     * time = O(N^3)
     * space = O(N^2)
     */
    public int minScoreTriangulation(int[] values) {
        int n = values.length;
        int[][] dp = new int[n][n];

        // length = number of vertices in the sub-polygon; shorter ones are ready first
        for (int length = 3; length <= n; length++) {
            for (int i = 0; i + length - 1 < n; i++) {
                int j = i + length - 1;
                int best = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; k++) {
                    int score = dp[i][k] + dp[k][j] + values[i] * values[k] * values[j];
                    best = Math.min(best, score);
                }
                dp[i][j] = best;
            }
        }
        return dp[0][n - 1];
    }

    // V1
    // IDEA: TOP-DOWN MEMOIZATION (SAME RECURRENCE)
    /**
     * time = O(N^3)
     * space = O(N^2)
     */
    public int minScoreTriangulation_1(int[] values) {
        int n = values.length;
        int[][] memo = new int[n][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(values, 0, n - 1, memo);
    }

    /** Min score to triangulate the sub-polygon i..j. */
    private int dfs(int[] values, int i, int j, int[][] memo) {
        // fewer than 3 vertices: no triangle
        if (j - i < 2) {
            return 0;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        int best = Integer.MAX_VALUE;
        for (int k = i + 1; k < j; k++) {
            int score = dfs(values, i, k, memo) + dfs(values, k, j, memo)
                    + values[i] * values[k] * values[j];
            best = Math.min(best, score);
        }
        memo[i][j] = best;
        return best;
    }
}
