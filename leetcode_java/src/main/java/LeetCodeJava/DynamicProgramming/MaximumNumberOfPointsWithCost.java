package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-number-of-points-with-cost/description/

/**
 * 1937. Maximum Number of Points with Cost
 * Medium
 *
 * You are given an m x n integer matrix points (0-indexed). Starting with 0 points, you want to
 * maximize the number of points you can get from the matrix.
 *
 * To gain points, you must pick one cell in each row. Picking the cell at coordinates (r, c) will
 * add points[r][c] to your score.
 *
 * However, you will lose points if you pick a cell too far from the cell that you picked in the
 * previous row. For every two adjacent rows r and r + 1 (where 0 <= r < m - 1), picking cells at
 * coordinates (r, c1) and (r + 1, c2) will subtract abs(c1 - c2) from your score.
 *
 * Return the maximum number of points you can achieve.
 *
 * abs(x) is defined as:
 *
 * x for x >= 0.
 * -x for x < 0.
 *
 *
 * Example 1:
 *
 * Input: points = [[1,2,3],[1,5,1],[3,1,1]]
 * Output: 9
 * Explanation: pick (0, 2), (1, 1), (2, 0): 3 + 5 + 3 = 11, minus abs(2 - 1) + abs(1 - 0) = 2 -> 9.
 *
 * Example 2:
 *
 * Input: points = [[1,5],[2,3],[4,2]]
 * Output: 11
 * Explanation: pick (0, 1), (1, 1), (2, 0): 5 + 3 + 4 = 12, minus abs(1 - 1) + abs(1 - 0) = 1 -> 11.
 *
 *
 * Constraints:
 *
 * m == points.length
 * n == points[r].length
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 10^5
 * 0 <= points[r][c] <= 10^5
 *
 */
public class MaximumNumberOfPointsWithCost {

    // V0
    // IDEA: ROW DP + LEFT/RIGHT RUNNING MAX (removes the abs from the transition)
    //
    //   dp[r][j] = points[r][j] + max over k of (dp[r-1][k] - |k - j|).
    //   split the abs:
    //     k <= j : dp[r-1][k] + k - j  -> running max of (dp[k] + k) from the LEFT
    //     k >= j : dp[r-1][k] - k + j  -> running max of (dp[k] - k) from the RIGHT
    //   two linear sweeps per row instead of an O(n^2) scan.
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public long maxPoints(int[][] points) {
        int cols = points[0].length;

        // prev[j] = best score with the previous row's pick in column j
        long[] prev = new long[cols];
        for (int j = 0; j < cols; j++) {
            prev[j] = points[0][j];
        }

        for (int r = 1; r < points.length; r++) {
            long[] cur = new long[cols];

            // left sweep: bestLeft = max of prev[k] + k for k <= j
            long bestLeft = Long.MIN_VALUE;
            for (int j = 0; j < cols; j++) {
                bestLeft = Math.max(bestLeft, prev[j] + j);
                cur[j] = points[r][j] + bestLeft - j;
            }

            // right sweep: bestRight = max of prev[k] - k for k >= j
            long bestRight = Long.MIN_VALUE;
            for (int j = cols - 1; j >= 0; j--) {
                bestRight = Math.max(bestRight, prev[j] - j);
                cur[j] = Math.max(cur[j], points[r][j] + bestRight + j);
            }

            prev = cur;
        }

        long answer = Long.MIN_VALUE;
        for (long score : prev) {
            answer = Math.max(answer, score);
        }
        return answer;
    }
}
