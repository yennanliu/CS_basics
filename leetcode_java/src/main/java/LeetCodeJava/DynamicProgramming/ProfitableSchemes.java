package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/profitable-schemes/description/
/**
 * 879. Profitable Schemes
 * Hard
 *
 * There is a group of n members, and a list of various crimes they could commit. The ith
 * crime generates a profit[i] and requires group[i] members to participate in it. If a
 * member participates in one crime, that member can't participate in another crime.
 *
 * Let's call a profitable scheme any subset of these crimes that generates at least
 * minProfit profit, and the total number of members participating in that subset of
 * crimes is at most n.
 *
 * Return the number of schemes that can be chosen. Since the answer may be very large,
 * return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: n = 5, minProfit = 3, group = [2,2], profit = [2,3]
 * Output: 2
 * Explanation: To make a profit of at least 3, the group could either commit crimes 0
 * and 1, or just crime 1.
 * In total, there are 2 schemes.
 *
 * Example 2:
 *
 * Input: n = 10, minProfit = 5, group = [2,3,5], profit = [6,7,8]
 * Output: 7
 * Explanation: To make a profit of at least 5, the group could commit any crimes, as
 * long as they commit one.
 * There are 7 possible schemes: (0), (1), (2), (0,1), (0,2), (1,2), and (0,1,2).
 *
 * Constraints:
 *
 * 1 <= n <= 100
 * 0 <= minProfit <= 100
 * 1 <= group.length <= 100
 * 1 <= group[i] <= 100
 * profit.length == group.length
 * 0 <= profit[i] <= 100
 *
 */
public class ProfitableSchemes {

    // V0
    // IDEA: 2D 0/1 KNAPSACK
    /**
     *  DP def:
     *     - dp[j][k] = number of subsets of the crimes seen so far that use
     *                  AT MOST j members and reach profit >= k
     *
     *     NOTE !!! profit is CAPPED at minProfit, since anything beyond
     *              minProfit behaves EXACTLY the same -> that is what keeps
     *              the state space finite.
     *
     *  Init:
     *     - dp[j][0] = 1 for every j  (the EMPTY subset already has profit >= 0)
     *
     *  DP eq (for a crime with g members, p profit):
     *     - dp[j][k] += dp[j - g][max(0, k - p)]
     *
     *     NOTE !!! looping j DOWNWARDS is what makes this 0/1 knapsack
     *              (each crime used AT MOST ONCE); ascending would allow reuse.
     *
     *  time  = O(m * n * minProfit), m = group.length
     *  space = O(n * minProfit)
     */
    public int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        final int MOD = 1_000_000_007;

        int[][] dp = new int[n + 1][minProfit + 1];
        // the empty scheme already satisfies `profit >= 0` for any member budget
        for (int j = 0; j <= n; j++) {
            dp[j][0] = 1;
        }

        for (int idx = 0; idx < group.length; idx++) {
            int g = group[idx];
            int p = profit[idx];

            for (int j = n; j >= g; j--) {
                for (int k = minProfit; k >= 0; k--) {
                    // max(0, k - p) CAPS the profit at minProfit
                    dp[j][k] = (dp[j][k] + dp[j - g][Math.max(0, k - p)]) % MOD;
                }
            }
        }

        return dp[n][minProfit];
    }

}
