package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-dice-rolls-with-target-sum/description/

/**
 * 1155. Number of Dice Rolls With Target Sum
 * Medium
 *
 * You have n dice, and each dice has k faces numbered from 1 to k.
 *
 * Given three integers n, k, and target, return the number of possible ways
 * (out of the k^n total ways) to roll the dice, so the sum of the face-up numbers
 * equals target. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 1, k = 6, target = 3
 * Output: 1
 * Explanation: You throw one die with 6 faces.
 * There is only one way to get a sum of 3.
 *
 * Example 2:
 *
 * Input: n = 2, k = 6, target = 7
 * Output: 6
 * Explanation: You throw two dice, each with 6 faces.
 * There are 6 ways to get a sum of 7: 1+6, 2+5, 3+4, 4+3, 5+2, 6+1.
 *
 * Example 3:
 *
 * Input: n = 30, k = 30, target = 500
 * Output: 222616187
 * Explanation: The answer must be returned modulo 10^9 + 7.
 *
 *
 * Constraints:
 *
 * 1 <= n, k <= 30
 * 1 <= target <= 1000
 *
 */
public class NumberOfDiceRollsWithTargetSum {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 2D DP (DICE x SUM) WITH A ROLLING 1D ROW
    /**
     * dp[s] = number of ways to reach sum s with the dice rolled so far
     *
     * after one more die: nextDp[s] = sum of dp[s - face] for face in 1..k, s - face >= 0
     *
     * base: dp[0] = 1 (zero dice, sum 0); answer = dp[target] after n rounds
     */
    /**
     * time = O(N * K * target)
     * space = O(target)
     */
    public int numRollsToTarget(int n, int k, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1;

        for (int die = 0; die < n; die++) {
            int[] nextDp = new int[target + 1];
            for (int sum = 1; sum <= target; sum++) {
                for (int face = 1; face <= k && face <= sum; face++) {
                    nextDp[sum] = (nextDp[sum] + dp[sum - face]) % MOD;
                }
            }
            dp = nextDp;
        }
        return dp[target];
    }
}
