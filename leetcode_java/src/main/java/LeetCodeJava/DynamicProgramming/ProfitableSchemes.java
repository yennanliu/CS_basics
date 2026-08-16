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


    // V1
    // IDEA: TOP-DOWN MEMOISED RECURSION on (crime, members used, capped profit)
    /**
     *  dfs(i, j, k) = the number of schemes from crime i onward given j members
     *  already committed and capped profit k.
     *
     *  Reads as `skip it or take it`, which is what the knapsack loop encodes; only
     *  the reachable states are visited.
     *
     *  time  = O(m * n * minProfit)
     *  space = O(m * n * minProfit)
     */
    private Integer[][][] memoPs;

    public int profitableSchemes_1(int n, int minProfit, int[] group, int[] profit) {
        memoPs = new Integer[group.length][n + 1][minProfit + 1];
        return dfsPs(0, 0, 0, n, minProfit, group, profit);
    }

    private int dfsPs(int i, int used, int gained, int n, int minProfit,
                      int[] group, int[] profit) {
        final int MOD = 1_000_000_007;
        if (i == group.length) {
            return gained == minProfit ? 1 : 0;
        }
        if (memoPs[i][used][gained] != null) {
            return memoPs[i][used][gained];
        }

        long res = dfsPs(i + 1, used, gained, n, minProfit, group, profit);
        if (used + group[i] <= n) {
            res += dfsPs(i + 1, used + group[i],
                    Math.min(minProfit, gained + profit[i]), n, minProfit, group, profit);
        }

        int out = (int) (res % MOD);
        memoPs[i][used][gained] = out;
        return out;
    }

    // V2
    // IDEA: 3D TABLE indexed by crime as well (no in-place reuse)
    /**
     *  dp[i][j][k] with the crime index kept explicitly, so nothing depends on the
     *  loop direction.
     *
     *  V0's correctness rests on iterating members DOWNWARD; carrying the crime
     *  index removes that obligation entirely, which is the version to reach for
     *  when the 0/1 vs unbounded distinction is in doubt.
     *
     *  time  = O(m * n * minProfit)
     *  space = O(m * n * minProfit)
     */
    public int profitableSchemes_2(int n, int minProfit, int[] group, int[] profit) {
        final int MOD = 1_000_000_007;
        int m = group.length;
        int[][][] dp = new int[m + 1][n + 1][minProfit + 1];

        for (int j = 0; j <= n; j++) {
            dp[m][j][minProfit] = minProfit == 0 ? 1 : 0;
        }
        // base: with no crimes left, only an already-satisfied profit counts
        for (int j = 0; j <= n; j++) {
            dp[m][j][minProfit] = 1;
        }

        for (int i = m - 1; i >= 0; i--) {
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= minProfit; k++) {
                    long res = dp[i + 1][j][k];                       // skip
                    if (j + group[i] <= n) {
                        int nk = Math.min(minProfit, k + profit[i]);
                        res += dp[i + 1][j + group[i]][nk];           // take
                    }
                    dp[i][j][k] = (int) (res % MOD);
                }
            }
        }
        return dp[0][0][0];
    }

    // V3
    // IDEA: BRUTE FORCE over subsets of crimes
    /**
     *  Enumerate all 2^m subsets and count those within the member budget that
     *  reach minProfit.
     *
     *  Only runs for m <= ~20, but it counts exactly the `schemes` the statement
     *  defines -- the oracle for the knapsack.
     *
     *  time  = O(2^m * m)
     *  space = O(1)
     */
    public int profitableSchemes_3(int n, int minProfit, int[] group, int[] profit) {
        final int MOD = 1_000_000_007;
        int m = group.length;
        long res = 0;

        for (int mask = 0; mask < (1 << m); mask++) {
            int members = 0;
            int gain = 0;
            for (int i = 0; i < m; i++) {
                if (((mask >> i) & 1) == 1) {
                    members += group[i];
                    gain += profit[i];
                }
            }
            if (members <= n && gain >= minProfit) {
                res += 1;
            }
        }
        return (int) (res % MOD);
    }

}
