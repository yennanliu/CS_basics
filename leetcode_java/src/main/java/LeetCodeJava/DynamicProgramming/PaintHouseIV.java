package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/paint-house-iv/description/

/**
 * 3429. Paint House IV
 * Medium
 *
 * You are given an even integer n representing the number of houses arranged in a straight
 * line, and a 2D array cost of size n x 3, where cost[i][j] represents the cost of painting
 * house i with color j + 1.
 *
 * The houses will look beautiful if they satisfy the following conditions:
 *
 * No two adjacent houses are painted the same color.
 * Houses equidistant from the ends of the row are not painted the same color. For example,
 * if n = 6, houses at positions (0, 5), (1, 4), and (2, 3) are considered equidistant.
 *
 * Return the minimum cost to paint the houses such that they look beautiful.
 *
 *
 * Example 1:
 *
 * Input: n = 4, cost = [[3,5,7],[6,2,9],[4,8,1],[7,3,5]]
 * Output: 9
 * Explanation: The optimal painting sequence is [1, 2, 3, 2] with corresponding costs
 * [3, 2, 1, 3].
 *
 * Example 2:
 *
 * Input: n = 6, cost = [[2,4,6],[5,3,8],[7,1,9],[4,6,2],[3,5,7],[8,2,4]]
 * Output: 18
 * Explanation: The optimal painting sequence is [1, 3, 2, 3, 1, 2] with corresponding
 * costs [2, 8, 1, 2, 3, 2].
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * n is even.
 * cost.length == n
 * cost[i].length == 3
 * 0 <= cost[i][j] <= 10^5
 *
 */
public class PaintHouseIV {

    private static final int COLORS = 3;

    // V0
    // IDEA: FOLD THE ROW IN HALF -- DP OVER PAIRS (t, n-1-t) WITH STATE = (LEFT COLOR, RIGHT COLOR)
    /**
     *  DP def:
     *     - dp[a][b] = min cost of painting pairs 0..t, with house t painted a and
     *                  house n-1-t painted b (a != b by the mirror rule)
     *
     *  DP eq:
     *     - dp'[c][d] = cost[t][c] + cost[n-1-t][d] + min dp[a][b]
     *                   over a != c, b != d     (adjacency on BOTH sides)
     *
     *  Init: dp[a][b] = cost[0][a] + cost[n-1][b]
     *
     *  The halves meet at houses n/2-1 and n/2 -- the final pair, which are adjacent;
     *  the mirror rule c != d there IS the adjacency rule, so the seam needs nothing extra.
     *
     *  Answer: min over dp after n/2 pairs
     *
     *  time  = O(N)   (constant 3^4 work per pair)
     *  space = O(1)
     */
    public long minCost(int n, int[][] cost) {
        // long: up to 10^5 houses * 10^5 cost overflows int
        long[][] dp = new long[COLORS][COLORS];
        for (int a = 0; a < COLORS; a++) {
            for (int b = 0; b < COLORS; b++) {
                dp[a][b] = (a == b) ? Long.MAX_VALUE : cost[0][a] + cost[n - 1][b];
            }
        }

        for (int t = 1; t < n / 2; t++) {
            int leftHouse = t;
            int rightHouse = n - 1 - t;
            long[][] next = new long[COLORS][COLORS];
            for (int c = 0; c < COLORS; c++) {
                for (int d = 0; d < COLORS; d++) {
                    next[c][d] = Long.MAX_VALUE;
                    // mirror rule: a pair never shares a color
                    if (c == d) {
                        continue;
                    }
                    // cheapest previous pair compatible with (c, d) on both sides
                    long bestPrev = Long.MAX_VALUE;
                    for (int a = 0; a < COLORS; a++) {
                        for (int b = 0; b < COLORS; b++) {
                            if (a != c && b != d && dp[a][b] < bestPrev) {
                                bestPrev = dp[a][b];
                            }
                        }
                    }
                    if (bestPrev != Long.MAX_VALUE) {
                        next[c][d] = bestPrev + cost[leftHouse][c] + cost[rightHouse][d];
                    }
                }
            }
            dp = next;
        }

        long answer = Long.MAX_VALUE;
        for (int a = 0; a < COLORS; a++) {
            for (int b = 0; b < COLORS; b++) {
                answer = Math.min(answer, dp[a][b]);
            }
        }
        return answer;
    }
}
