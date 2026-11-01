package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-stepping-numbers-in-range/description/

import java.util.*;

/**
 * 2801. Count Stepping Numbers in Range
 * Hard
 *
 * Given two positive integers low and high represented as strings, find the count of stepping
 * numbers in the inclusive range [low, high].
 *
 * A stepping number is an integer such that all of its adjacent digits have an absolute
 * difference of exactly 1.
 *
 * Return an integer denoting the count of stepping numbers in the inclusive range [low, high].
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Note: A stepping number should not have a leading zero.
 *
 *
 * Example 1:
 *
 * Input: low = "1", high = "11"
 * Output: 10
 * Explanation: The stepping numbers in the range [1,11] are 1, 2, 3, 4, 5, 6, 7, 8, 9 and 10.
 *
 * Example 2:
 *
 * Input: low = "90", high = "101"
 * Output: 2
 * Explanation: The stepping numbers in the range [90,101] are 98 and 101.
 *
 *
 * Constraints:
 *
 * 1 <= int(low) <= int(high) < 10^100
 * 1 <= low.length, high.length <= 100
 * low and high consist of only digits.
 * low and high don't have any leading zeros.
 *
 */
public class CountSteppingNumbersInRange {

    private static final int MOD = 1_000_000_007;

    /** Marks "no digit placed yet" for the previous-digit slot. */
    private static final int NO_DIGIT = 10;

    // V0
    // IDEA: DIGIT DP - countUpTo(high) - countUpTo(low) + (low IS STEPPING); STATE = (position, previous digit, leading, tight)
    /**
     * time = O(L * 10 * 10)
     * space = O(L * 10)
     */
    public int countSteppingNumbers(String low, String high) {
        long upToHigh = countUpTo(high);
        long upToLow = countUpTo(low);

        // countUpTo(low) includes low itself, which is inside [low, high]
        long lowItself = isStepping(low) ? 1 : 0;

        return (int) (((upToHigh - upToLow + lowItself) % MOD + MOD) % MOD);
    }

    /** Number of stepping numbers in [1, num]. */
    private long countUpTo(String num) {
        // memo[pos][previousDigit], used only once a digit is placed and we are not tight
        long[][] memo = new long[num.length()][NO_DIGIT + 1];
        for (long[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(num, 0, NO_DIGIT, true, true, memo);
    }

    /**
     * Ways to fill digits pos..end. `leading` = nothing placed yet (only leading zeros);
     * `tight` = every earlier digit equals num's, so this digit is capped at num[pos].
     */
    private long dfs(String num, int pos, int previousDigit, boolean leading, boolean tight, long[][] memo) {
        if (pos == num.length()) {
            // all leading zeros means no number was formed
            return leading ? 0 : 1;
        }
        boolean cacheable = !leading && !tight;
        if (cacheable && memo[pos][previousDigit] != -1) {
            return memo[pos][previousDigit];
        }

        int upperDigit = tight ? num.charAt(pos) - '0' : 9;
        long total = 0;
        for (int digit = 0; digit <= upperDigit; digit++) {
            boolean nextTight = tight && digit == upperDigit;
            if (leading) {
                if (digit == 0) {
                    // still a leading zero: the number has not started
                    total += dfs(num, pos + 1, NO_DIGIT, true, nextTight, memo);
                } else {
                    // the first real digit has no stepping constraint
                    total += dfs(num, pos + 1, digit, false, nextTight, memo);
                }
            } else if (Math.abs(digit - previousDigit) == 1) {
                total += dfs(num, pos + 1, digit, false, nextTight, memo);
            }
        }
        total %= MOD;

        if (cacheable) {
            memo[pos][previousDigit] = total;
        }
        return total;
    }

    /** True if every pair of adjacent digits differs by exactly 1. */
    private boolean isStepping(String num) {
        for (int i = 1; i < num.length(); i++) {
            if (Math.abs(num.charAt(i) - num.charAt(i - 1)) != 1) {
                return false;
            }
        }
        return true;
    }
}
