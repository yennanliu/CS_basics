package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-number-of-subsequences-with-equal-gcd/description/
/**
 * 3336. Find the Number of Subsequences With Equal GCD
 * Hard
 *
 * You are given an integer array nums.
 *
 * Your task is to find the number of pairs of non-empty subsequences (seq1, seq2) of nums that
 * satisfy the following conditions:
 *
 * The subsequences seq1 and seq2 are disjoint, meaning no index of nums is common between them.
 * The GCD of the elements of seq1 is equal to the GCD of the elements of seq2.
 *
 * Return the total number of such pairs.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 10
 *
 * Example 2:
 *
 * Input: nums = [10,20,30]
 * Output: 2
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,1]
 * Output: 50
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 200
 *
 */
public class FindTheNumberOfSubsequencesWithEqualGCD {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP OVER THE PAIR OF RUNNING GCDs (0 = SIDE STILL EMPTY)
    /**
     *  Each element goes to seq1, to seq2, or to neither; disjointness is automatic
     *  because every index is offered exactly once.
     *
     *  DP def:
     *     - dp[g1][g2] = number of ways the first side has gcd g1 and the second has gcd g2,
     *                    where 0 means "that side is still empty" (gcd(0, x) = x)
     *  DP eq, for each x:
     *     - newDp[g1][g2]           += dp[g1][g2]   (skip x)
     *     - newDp[gcd(g1, x)][g2]   += dp[g1][g2]   (x -> seq1)
     *     - newDp[g1][gcd(g2, x)]   += dp[g1][g2]   (x -> seq2)
     *  Init: dp[0][0] = 1
     *  Answer: sum of dp[g][g] for g > 0
     *
     *  time = O(N * M^2 * log M), M = max(nums)
     *  space = O(M^2)
     */
    public int subsequencePairCount(int[] nums) {
        int maxValue = 0;
        for (int x : nums) {
            maxValue = Math.max(maxValue, x);
        }

        long[][] dp = new long[maxValue + 1][maxValue + 1];
        dp[0][0] = 1;

        for (int x : nums) {
            // start from a copy: the "skip x" option keeps every state
            long[][] next = new long[maxValue + 1][maxValue + 1];
            for (int g1 = 0; g1 <= maxValue; g1++) {
                for (int g2 = 0; g2 <= maxValue; g2++) {
                    next[g1][g2] = dp[g1][g2];
                }
            }
            for (int g1 = 0; g1 <= maxValue; g1++) {
                for (int g2 = 0; g2 <= maxValue; g2++) {
                    long ways = dp[g1][g2];
                    if (ways == 0) {
                        continue;
                    }
                    // x joins seq1
                    int newG1 = gcd(g1, x);
                    next[newG1][g2] = (next[newG1][g2] + ways) % MOD;
                    // x joins seq2
                    int newG2 = gcd(g2, x);
                    next[g1][newG2] = (next[g1][newG2] + ways) % MOD;
                }
            }
            dp = next;
        }

        // both sides non-empty and equal
        long answer = 0;
        for (int g = 1; g <= maxValue; g++) {
            answer = (answer + dp[g][g]) % MOD;
        }
        return (int) answer;
    }

    /** Euclid's gcd; gcd(0, x) = x so an empty side just adopts x. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
