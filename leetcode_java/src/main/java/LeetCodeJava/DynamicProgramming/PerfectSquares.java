package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/perfect-squares/description/

import java.util.Arrays;

/**
 * 279. Perfect Squares
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer n, return the least number of perfect square numbers that sum to n.
 *
 * A perfect square is an integer that is the square of an integer; in other words, it is the product of some integer with itself. For example, 1, 4, 9, and 16 are perfect squares while 3 and 11 are not.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 12
 * Output: 3
 * Explanation: 12 = 4 + 4 + 4.
 * Example 2:
 *
 * Input: n = 13
 * Output: 2
 * Explanation: 13 = 4 + 9.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 104
 *
 */
public class PerfectSquares {

    // V0
//    public int numSquares(int n) {
//
//    }

    // V1
    // IDEA: DP
    // https://leetcode.com/problems/perfect-squares/solutions/4694883/beats-99-users-cjavapythonjavascript-exp-37yg/
    public int numSquares_1(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; ++i) {
            int min_val = Integer.MAX_VALUE;
            for (int j = 1; j * j <= i; ++j) {
                min_val = Math.min(min_val, dp[i - j * j] + 1);
            }
            dp[i] = min_val;
        }
        return dp[n];
    }

    // V2
    // https://leetcode.com/problems/perfect-squares/solutions/1520447/dp-easy-to-understand-js-java-python-c-b-sk59/
    // IDEA: DP
    public int numSquares_2(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        int count = 1;
        while (count * count <= n) {
            int sq = count * count;
            for (int i = sq; i <= n; i++) {
                dp[i] = Math.min(dp[i - sq] + 1, dp[i]);
            }
            count++;
        }
        return dp[n];
    }

    // V3-1
    // https://leetcode.com/problems/perfect-squares/solutions/2837992/java-recursion-memoization-dp-3-square-t-72qb/
    // IDEA: Top Down DP (Recursion + Memoization)
    public int numSquares_3_1(int n) {
        int[] memo = new int[n + 1];
        return helper(n, memo);
    }

    public int helper(int n, int[] memo) {
        if (n < 4)
            return n;

        if (memo[n] != 0)
            return memo[n];

        int ans = n;

        for (int i = 1; i * i <= n; i++) {
            int square = i * i;
            ans = Math.min(ans, 1 + helper(n - square, memo));
        }

        return memo[n] = ans;
    }

    // V3-2
    // https://leetcode.com/problems/perfect-squares/solutions/2837992/java-recursion-memoization-dp-3-square-t-72qb/
    // IDEA: Top Down DP (Recursion + Memoization)
    // Time complexity: O(N * sqrt(N))
    // Space complexity: O(N)
    public int numSquares_3_2(int n) {
        int[] dp = new int[n + 1];
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            dp[i] = i;

            for (int j = 1; j * j <= i; j++) {
                int square = j * j;
                dp[i] = Math.min(dp[i], 1 + dp[i - square]);
            }
        }

        return dp[n];
    }

}
