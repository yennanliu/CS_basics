package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-of-integers/description/

import java.util.*;

/**
 * 2719. Count of Integers
 * Hard
 *
 * You are given two numeric strings num1 and num2 and two integers max_sum and min_sum. We denote
 * an integer x to be good if:
 *
 * num1 <= x <= num2
 * min_sum <= digit_sum(x) <= max_sum.
 *
 * Return the number of good integers. Since the answer may be large, return it modulo 10^9 + 7.
 *
 * Note that digit_sum(x) denotes the sum of the digits of x.
 *
 *
 * Example 1:
 *
 * Input: num1 = "1", num2 = "12", min_sum = 1, max_sum = 8
 * Output: 11
 * Explanation: There are 11 integers whose sum of digits lies between 1 and 8 are 1,2,3,4,5,6,7,8,10,11, and 12. Thus, we return 11.
 *
 * Example 2:
 *
 * Input: num1 = "1", num2 = "5", min_sum = 1, max_sum = 5
 * Output: 5
 * Explanation: The 5 integers whose sum of digits lies between 1 and 5 are 1,2,3,4, and 5. Thus, we return 5.
 *
 *
 * Constraints:
 *
 * 1 <= num1 <= num2 <= 10^22
 * 1 <= min_sum <= max_sum <= 400
 *
 */
public class CountOfIntegers {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DIGIT DP - countUpTo(num2) - countUpTo(num1) + (num1 IS GOOD); STATE = (position, digit sum, tight)
    /**
     * time = O(10 * L * maxSum)
     * space = O(L * maxSum)
     */
    public int count(String num1, String num2, int min_sum, int max_sum) {
        long upToHigh = countUpTo(num2, min_sum, max_sum);
        long upToLow = countUpTo(num1, min_sum, max_sum);

        // countUpTo(num1) includes num1 itself, which belongs in the range [num1, num2]
        int num1DigitSum = 0;
        for (int i = 0; i < num1.length(); i++) {
            num1DigitSum += num1.charAt(i) - '0';
        }
        long num1Good = (min_sum <= num1DigitSum && num1DigitSum <= max_sum) ? 1 : 0;

        return (int) (((upToHigh - upToLow + num1Good) % MOD + MOD) % MOD);
    }

    /** Number of integers x in [0, num] with minSum <= digitSum(x) <= maxSum. */
    private long countUpTo(String num, int minSum, int maxSum) {
        int length = num.length();
        // memo[pos][sum] for the non-tight state only (tight is a single path)
        long[][] memo = new long[length][maxSum + 1];
        for (long[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(num, 0, 0, true, minSum, maxSum, memo);
    }

    /**
     * Ways to fill digits pos..end, given the digit sum so far and whether every earlier digit
     * matched num exactly (tight -> this digit is capped at num[pos]).
     */
    private long dfs(String num, int pos, int sum, boolean tight, int minSum, int maxSum, long[][] memo) {
        // digit sums only grow, so an over-budget prefix is dead
        if (sum > maxSum) {
            return 0;
        }
        if (pos == num.length()) {
            return sum >= minSum ? 1 : 0;
        }
        if (!tight && memo[pos][sum] != -1) {
            return memo[pos][sum];
        }

        int upperDigit = tight ? num.charAt(pos) - '0' : 9;
        long total = 0;
        for (int digit = 0; digit <= upperDigit; digit++) {
            boolean nextTight = tight && digit == upperDigit;
            total += dfs(num, pos + 1, sum + digit, nextTight, minSum, maxSum, memo);
        }
        total %= MOD;

        if (!tight) {
            memo[pos][sum] = total;
        }
        return total;
    }
}
