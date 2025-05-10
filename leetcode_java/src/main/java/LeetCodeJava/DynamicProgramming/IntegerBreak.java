package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/integer-break/description/
/**
 * 343. Integer Break
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an integer n, break it into the sum of k positive integers, where k >= 2, and maximize the product of those integers.
 *
 * Return the maximum product you can get.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 1
 * Explanation: 2 = 1 + 1, 1 × 1 = 1.
 * Example 2:
 *
 * Input: n = 10
 * Output: 36
 * Explanation: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 58
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 393.9K
 * Submissions
 *
 *
 *
 */
public class IntegerBreak {

    // V0
//    public int integerBreak(int n) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=in6QbUPMJ3I
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0343-integer-break.java
    public int integerBreak_1(int n) {
        if (n < 4) return n - 1;

        int res = 1;

        while (n > 4) {
            n -= 3;
            res *= 3;
        }

        res *= n;
        return res;
    }

    // V2-1
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: 1D DP (BOTTOM UP)
    public int integerBreak_2_1(int n) {
        if (n <= 1) {
            return 0;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j < i; j++) {
                dp[i] = Math.max(dp[i], Math.max(j * (i - j), j * dp[i - j]));
            }
        }
        return dp[n];
    }

    // V2-2
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: Dynamic Programming (Top-Down with Memoization):
    public int integerBreak_2_2(int n) {
        if (n <= 1) {
            return 0;
        }
        int[] memo = new int[n + 1];
        return maxProduct(n, memo);
    }

    private int maxProduct(int n, int[] memo) {
        if (n == 2) {
            return 1;
        }
        if (memo[n] != 0) {
            return memo[n];
        }
        int max = 0;
        for (int i = 1; i < n; i++) {
            max = Math.max(max, Math.max(i * (n - i), i * maxProduct(n - i, memo)));
        }
        memo[n] = max;
        return max;
    }


    // V2-3
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: GREEDY
    public int integerBreak_2_3(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int result = 1;
        while (n > 4) {
            result *= 3;
            n -= 3;
        }
        result *= n;
        return result;
    }

    // V2-4
    // https://leetcode.com/problems/integer-break/solutions/4135963/faster-lesser4-methodssimple-mathdynamic-lc7i/
    // IDEA: MATH
    public int integerBreak_2_4(int n) {
        if (n <= 1) {
            return 0;
        }
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }
        int quotient = n / 3;
        int remainder = n % 3;
        if (remainder == 0) {
            return (int) Math.pow(3, quotient);
        } else if (remainder == 1) {
            return (int) (Math.pow(3, quotient - 1) * 4);
        } else {
            return (int) (Math.pow(3, quotient) * 2);
        }
    }


    // V3
    // https://leetcode.com/problems/integer-break/solutions/80785/ologn-time-solution-with-explanation-by-6deq2/
    public int integerBreak_3(int n) {
        if (n == 2)
            return 1;
        else if (n == 3)
            return 2;
        else if (n % 3 == 0)
            return (int) Math.pow(3, n / 3);
        else if (n % 3 == 1)
            return 2 * 2 * (int) Math.pow(3, (n - 4) / 3);
        else
            return 2 * (int) Math.pow(3, n / 3);
    }

    // V4
    // https://leetcode.com/problems/integer-break/solutions/6210269/video-give-me-10-minutes-how-we-think-ab-publ/
    public int integerBreak_4(int n) {
        if (n == 2) {
            return 1;
        }
        if (n == 3) {
            return 2;
        }

        // Try to divide n into as many threes as possible
        int threes = n / 3;
        int remainder = n % 3;

        if (remainder == 1) {
            threes -= 1; // remove 3 * 1
            remainder = 4; // create 2 * 2
        } else if (remainder == 0) {
            remainder = 1; // when remainder is 0, set 1 which doesn't affect your answer.
        }

        return (int) (Math.pow(3, threes) * remainder);
    }


}
