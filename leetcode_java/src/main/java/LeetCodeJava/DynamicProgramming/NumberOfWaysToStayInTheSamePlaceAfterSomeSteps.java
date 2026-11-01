package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/description/

/**
 * 1269. Number of Ways to Stay in the Same Place After Some Steps
 * Hard
 *
 * You have a pointer at index 0 in an array of size arrLen. At each step,
 * you can move 1 position to the left, 1 position to the right in the array,
 * or stay in the same place (The pointer should not be placed outside the array
 * at any time).
 *
 * Given two integers steps and arrLen, return the number of ways such that your
 * pointer is still at index 0 after exactly steps steps.
 * Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: steps = 3, arrLen = 2
 * Output: 4
 * Explanation: There are 4 differents ways to stay at index 0 after 3 steps.
 * Right, Left, Stay
 * Stay, Right, Left
 * Right, Stay, Left
 * Stay, Stay, Stay
 *
 * Example 2:
 *
 * Input: steps = 2, arrLen = 4
 * Output: 2
 * Explanation: There are 2 differents ways to stay at index 0 after 2 steps
 * Right, Left
 * Stay, Stay
 *
 * Example 3:
 *
 * Input: steps = 4, arrLen = 2
 * Output: 8
 *
 *
 * Constraints:
 *
 * 1 <= steps <= 500
 * 1 <= arrLen <= 10^6
 *
 */
public class NumberOfWaysToStayInTheSamePlaceAfterSomeSteps {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 1D DP (ROLLING ARRAY) + PRUNE THE WIDTH TO steps / 2 + 1
    /**
     * dp[j] = number of ways to be at index j after the steps taken so far
     *
     * next[j] = dp[j]       // stay
     *         + dp[j - 1]   // arrive from the left
     *         + dp[j + 1]   // arrive from the right
     *
     * KEY PRUNE: the pointer must come back to 0, so it never goes further right
     * than steps / 2 -> width = min(arrLen, steps / 2 + 1). Without it arrLen = 10^6
     * would be far too wide.
     *
     * base: dp[0] = 1; answer = dp[0] after `steps` rounds
     */
    /**
     * time = O(steps * min(arrLen, steps))
     * space = O(min(arrLen, steps))
     */
    public int numWays(int steps, int arrLen) {
        int width = Math.min(arrLen, steps / 2 + 1);
        long[] dp = new long[width];
        dp[0] = 1;

        for (int step = 0; step < steps; step++) {
            long[] next = new long[width];
            for (int j = 0; j < width; j++) {
                long ways = dp[j];
                if (j > 0) {
                    ways += dp[j - 1];
                }
                if (j + 1 < width) {
                    ways += dp[j + 1];
                }
                next[j] = ways % MOD;
            }
            dp = next;
        }
        return (int) dp[0];
    }
}
