package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/toss-strange-coins/description/
// https://leetcode.ca/2019-04-13-1230-Toss-Strange-Coins/

/**
 * 1230. Toss Strange Coins
 * Medium
 *
 * You have some coins. The i-th coin has a probability prob[i] of facing heads when tossed.
 *
 * Return the probability that the number of coins facing heads equals target if you toss
 * every coin exactly once.
 *
 *
 * Example 1:
 *
 * Input: prob = [0.4], target = 1
 * Output: 0.40000
 *
 * Example 2:
 *
 * Input: prob = [0.5,0.5,0.5,0.5,0.5], target = 0
 * Output: 0.03125
 *
 *
 * Constraints:
 *
 * 1 <= prob.length <= 1000
 * 0 <= prob[i] <= 1
 * 0 <= target <= prob.length
 * Answers will be accepted as correct if they are within 10^-5 of the correct answer.
 *
 */
public class TossStrangeCoins {

    // V0
    // IDEA: 1D DP (ROLLED FROM THE 2D "FIRST i COINS, j HEADS" TABLE)
    /**
     * dp[j] = probability of exactly j heads among the coins tossed so far
     *
     * adding a coin with head probability p:
     *   dp[j] = dp[j] * (1 - p) + dp[j - 1] * p     (j > 0)
     *   dp[0] = dp[0] * (1 - p)
     *
     * j runs BACKWARDS, so dp[j - 1] still holds the previous coin's value.
     * base: dp[0] = 1 (no coins -> zero heads with probability 1)
     */
    /**
     * time = O(N * target)
     * space = O(target)
     */
    public double probabilityOfHeads(double[] prob, int target) {
        double[] dp = new double[target + 1];
        dp[0] = 1.0;

        for (double p : prob) {
            for (int heads = target; heads >= 1; heads--) {
                // this coin is tails (stay at `heads`) or heads (came from heads - 1)
                dp[heads] = dp[heads] * (1 - p) + dp[heads - 1] * p;
            }
            dp[0] = dp[0] * (1 - p);
        }
        return dp[target];
    }
}
