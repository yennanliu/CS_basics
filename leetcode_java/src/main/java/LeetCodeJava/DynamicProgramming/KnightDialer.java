package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/knight-dialer/

/**
 *  935. Knight Dialer
 *  Medium
 *
 *  The chess knight has a unique movement, it may move two squares vertically
 *  and one square horizontally, or two squares horizontally and one square
 *  vertically (with both forming the shape of an L).
 *
 *  We have a chess knight and a phone pad as shown below, the knight can only
 *  stand on a numeric cell (i.e. blue cell).
 *
 *      1 2 3
 *      4 5 6
 *      7 8 9
 *      * 0 #
 *
 *  Given an integer n, return how many distinct phone numbers of length n we
 *  can dial. You are allowed to place the knight on any numeric cell initially
 *  and then you should perform n - 1 jumps to dial a number of length n. All
 *  jumps should be valid knight jumps.
 *
 *  As the answer may be very large, return the answer modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: n = 1
 *    Output: 10
 *    Explanation: We need to dial a number of length 1, so placing the knight
 *                 over any numeric cell of the 10 cells is sufficient.
 *
 *  Example 2:
 *    Input: n = 2
 *    Output: 20
 *
 *  Constraints:
 *    - 1 <= n <= 5000
 */
public class KnightDialer {

    private static final int MOD = 1_000_000_007;

    // where a knight standing on digit d can jump to
    private static final int[][] NEXT = {
            {4, 6},     // 0
            {6, 8},     // 1
            {7, 9},     // 2
            {4, 8},     // 3
            {0, 3, 9},  // 4
            {},         // 5 (unreachable / no moves)
            {0, 1, 7},  // 6
            {2, 6},     // 7
            {1, 3},     // 8
            {2, 4}      // 9
    };

    // V0
    // IDEA: DP over jump count - dp[d] = # of length-t numbers ending on digit d
    /**
     * time = O(N)
     * space = O(1)   (10 digits)
     */
    public int knightDialer(int n) {
        long[] dp = new long[10];
        for (int d = 0; d < 10; d++) {
            dp[d] = 1;
        }

        for (int step = 1; step < n; step++) {
            long[] next = new long[10];
            for (int d = 0; d < 10; d++) {
                if (dp[d] == 0) {
                    continue;
                }
                for (int nd : NEXT[d]) {
                    next[nd] = (next[nd] + dp[d]) % MOD;
                }
            }
            dp = next;
        }

        long res = 0;
        for (int d = 0; d < 10; d++) {
            res = (res + dp[d]) % MOD;
        }
        return (int) res;
    }
}
