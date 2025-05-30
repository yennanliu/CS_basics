package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/paint-house/
// https://leetcode.ca/all/256.html
// https://blog.csdn.net/qq_29051413/article/details/108709482
/**
 * 256. Paint House
 * There are a row of n houses, each house can be painted with one of the three colors: red, blue or green. The cost of painting each house with a certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.
 *
 * The cost of painting each house with a certain color is represented by a n x 3 cost matrix. For example, costs[0][0] is the cost of painting house 0 with color red; costs[1][2] is the cost of painting house 1 with color green, and so on... Find the minimum cost to paint all houses.
 *
 * Note:
 * All costs are positive integers.
 *
 * Example:
 *
 * Input: [[17,2,17],[16,16,5],[14,3,19]]
 * Output: 10
 * Explanation: Paint house 0 into blue, paint house 1 into green, paint house 2 into blue.
 *              Minimum cost: 2 + 5 + 3 = 10.
 * Difficulty:
 * Easy
 *
 */
public class PaintHouse {

    // V0
//    public int minCost(int[][] costs) {
//
//    }

    // V0-1
    // TODO: validate
    // IDEA: DP (fixed by gpt)
    public int minCost_0_1(int[][] costs) {
        // edge case
        if (costs == null || costs.length == 0 || costs[0].length == 0) {
            return 0;
        }

        int n = costs.length;
        int k = costs[0].length;

        // dp[i][j]: min cost to paint house 0...i with house i painted color j
        int[][] dp = new int[n][k];

        // initialize the first house
        for (int j = 0; j < k; j++) {
            dp[0][j] = costs[0][j];
        }

        // fill dp table
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int p = 0; p < k; p++) {
                    if (p != j) {
                        dp[i][j] = Math.min(dp[i][j], dp[i - 1][p] + costs[i][j]);
                    }
                }
            }
        }

        // return the min cost of painting the last house with any color
        int res = Integer.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            res = Math.min(res, dp[n - 1][j]);
        }

        return res;
    }

    // V1
    // https://leetcode.ca/2016-08-12-256-Paint-House/
    public int minCost_1(int[][] costs) {
        int r = 0, g = 0, b = 0;
        for (int[] cost : costs) {
            int _r = r, _g = g, _b = b;
            r = Math.min(_g, _b) + cost[0];
            g = Math.min(_r, _b) + cost[1];
            b = Math.min(_r, _g) + cost[2];
        }
        return Math.min(r, Math.min(g, b));
    }


    // V2

}
