package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-to-cut-a-stick/description/

import java.util.Arrays;

/**
 * 1547. Minimum Cost to Cut a Stick
 * Hard
 *
 * Given a wooden stick of length n units. The stick is labelled from 0 to n.
 *
 * Given an integer array cuts where cuts[i] denotes a position you should perform a cut at.
 *
 * You should perform the cuts in order, you can change the order of the cuts as you wish.
 *
 * The cost of one cut is the length of the stick to be cut, the total cost is the sum of costs of all cuts. When you cut a stick, it will be split into two smaller sticks (i.e. the sum of their lengths is the length of the stick before the cut). Please refer to the first example for a better explanation.
 *
 * Return the minimum total cost of the cuts.
 *
 *
 * Example 1:
 *
 * Input: n = 7, cuts = [1,3,4,5]
 * Output: 16
 * Explanation: Using cuts order = [1, 3, 4, 5] as in the input leads to the following scenario:
 * The first cut is done to a rod of length 7 so the cost is 7. The second cut is done to a rod of length 6 (i.e. the second part of the first cut), the third is done to a rod of length 4 and the last cut is to a rod of length 3. The total cost is 7 + 6 + 4 + 3 = 20.
 * Rearranging the cuts to be [3, 5, 1, 4] for example will lead to a scenario with total cost = 16 (as shown in the example photo 7 + 4 + 3 + 2 = 16).
 *
 * Example 2:
 *
 * Input: n = 9, cuts = [5,6,1,4,2]
 * Output: 22
 * Explanation: If you try the given cuts ordering the cost will be 25.
 * There are much ordering with total cost <= 25, for example, the order [4, 6, 5, 2, 1] has total cost = 22 which is the minimum possible.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^6
 * 1 <= cuts.length <= min(n - 1, 100)
 * 1 <= cuts[i] <= n - 1
 * All the integers in cuts array are distinct.
 *
 */
public class MinimumCostToCutAStick {

    // V0
    // IDEA: INTERVAL DP OVER CUT POSITIONS (like Burst Balloons)
    //       add the two ends 0 and n, sort -> positions p[0..m-1].
    //       dp[i][j] = min cost to make every cut strictly between p[i] and p[j].
    //       the first cut inside (p[i], p[j]) costs the full length p[j] - p[i], then splits it:
    //         dp[i][j] = (p[j] - p[i]) + min over i < mid < j of dp[i][mid] + dp[mid][j]
    //       base: dp[i][i+1] = 0 (no cut in between).
    /**
     * time = O(M^3)   (M = cuts.length + 2)
     * space = O(M^2)
     */
    public int minCost(int n, int[] cuts) {
        int m = cuts.length + 2;
        int[] positions = new int[m];
        positions[0] = 0;
        positions[m - 1] = n;
        for (int i = 0; i < cuts.length; i++) {
            positions[i + 1] = cuts[i];
        }
        Arrays.sort(positions);

        int[][] dp = new int[m][m];
        // span = distance in indices between the two ends of the segment
        for (int span = 2; span < m; span++) {
            for (int i = 0; i + span < m; i++) {
                int j = i + span;
                int best = Integer.MAX_VALUE;
                // try every cut inside the segment as the FIRST one made
                for (int mid = i + 1; mid < j; mid++) {
                    best = Math.min(best, dp[i][mid] + dp[mid][j]);
                }
                dp[i][j] = best + positions[j] - positions[i];
            }
        }
        return dp[0][m - 1];
    }
}
