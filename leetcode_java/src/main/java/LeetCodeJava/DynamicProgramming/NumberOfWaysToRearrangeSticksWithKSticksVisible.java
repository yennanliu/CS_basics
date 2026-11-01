package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/description/

/**
 * 1866. Number of Ways to Rearrange Sticks With K Sticks Visible
 * Hard
 *
 * There are n uniquely-sized sticks whose lengths are integers from 1 to n. You want to arrange the
 * sticks such that exactly k sticks are visible from the left. A stick is visible from the left if
 * there are no longer sticks to the left of it.
 *
 * For example, if the sticks are arranged [1,3,2,5,4], then the sticks with lengths 1, 3, and 5
 * are visible from the left.
 *
 * Given n and k, return the number of such arrangements. Since the answer may be large, return it
 * modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 2
 * Output: 3
 * Explanation: [1,3,2], [2,3,1], and [2,1,3] are the only arrangements such that exactly 2 sticks are visible.
 *
 * Example 2:
 *
 * Input: n = 5, k = 5
 * Output: 1
 * Explanation: [1,2,3,4,5] is the only arrangement such that all 5 sticks are visible.
 *
 * Example 3:
 *
 * Input: n = 20, k = 11
 * Output: 647427950
 * Explanation: There are 647427950 (mod 10^9 + 7) ways to rearrange the sticks such that exactly 11 sticks are visible.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 * 1 <= k <= n
 *
 */
public class NumberOfWaysToRearrangeSticksWithKSticksVisible {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP (STIRLING NUMBERS OF THE FIRST KIND), DECIDE THE LAST POSITION
    //
    //   dp[i][j] = ways to arrange sticks 1..i with exactly j visible.
    //   look at the LAST position:
    //     - the tallest stick i is last  -> it is visible:            dp[i-1][j-1]
    //     - one of the other i-1 sticks is last -> hidden behind i:   dp[i-1][j] * (i-1)
    //   dp[i][j] = dp[i-1][j-1] + (i-1) * dp[i-1][j],  dp[0][0] = 1.
    //   rolled into one row, j sweeping DOWN so dp[j-1] is still the old row.
    /**
     * time = O(N * K)
     * space = O(K)
     */
    public int rearrangeSticks(int n, int k) {
        long[] dp = new long[k + 1];
        dp[0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = Math.min(i, k); j >= 1; j--) {
                dp[j] = (dp[j] * (i - 1) + dp[j - 1]) % MOD;
            }
            // with at least one stick, zero visible is impossible
            dp[0] = 0;
        }
        return (int) dp[k];
    }
}
