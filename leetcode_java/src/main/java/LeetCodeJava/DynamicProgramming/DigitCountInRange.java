package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/digit-count-in-range/description/

import java.util.*;

/**
 * 1067. Digit Count in Range
 * Hard
 *
 * Given a single-digit integer d and two integers low and high, return the number of times that
 * d occurs as a digit in all integers in the inclusive range [low, high].
 *
 *
 * Example 1:
 *
 * Input: d = 1, low = 1, high = 13
 * Output: 6
 * Explanation: The digit d = 1 occurs 6 times in 1, 10, 11, 12, 13.
 * Note that the digit d = 1 occurs twice in the number 11.
 *
 * Example 2:
 *
 * Input: d = 3, low = 100, high = 250
 * Output: 35
 * Explanation: The digit d = 3 occurs 35 times in 103,113,123,130,131,...,238,239,243.
 *
 *
 * Constraints:
 *
 * 0 <= d <= 9
 * 1 <= low <= high <= 2 * 10^8
 *
 */
public class DigitCountInRange {

    // V0
    // IDEA: DIGIT DP + PREFIX TRICK: count(high) - count(low - 1)
    //   count(n) = how many times d appears when writing out 1..n.
    //   dfs(pos, cnt, leading, limit):
    //     pos     : current digit position, left to right
    //     cnt     : how many d's placed so far
    //     leading : still inside leading zeros (those zeros are not real digits)
    //     limit   : prefix equals n's prefix, so this digit is capped at n's digit
    //   only the (pos, cnt) states with leading == false and limit == false repeat, so those are memoised.
    /**
     * time = O(L * L * 10), L = number of digits
     * space = O(L * L)
     */
    public int digitsCount(int d, int low, int high) {
        return countUpTo(high, d) - countUpTo(low - 1, d);
    }

    private String number;
    private int target;
    private Integer[][] memo;

    /** Occurrences of digit d in all of 1..n. */
    private int countUpTo(int n, int d) {
        if (n <= 0) {
            return 0;
        }
        number = String.valueOf(n);
        target = d;
        memo = new Integer[number.length()][number.length() + 1];
        return dfs(0, 0, true, true);
    }

    /** Total d's over every completion of the current prefix. */
    private int dfs(int pos, int count, boolean leading, boolean limit) {
        // a full number is formed: it contributes the d's it contains
        if (pos == number.length()) {
            return count;
        }
        boolean free = !leading && !limit;
        if (free && memo[pos][count] != null) {
            return memo[pos][count];
        }

        int upper = limit ? number.charAt(pos) - '0' : 9;
        int total = 0;
        for (int digit = 0; digit <= upper; digit++) {
            boolean nextLimit = limit && digit == upper;
            if (digit == 0 && leading) {
                // a leading zero is not written, so it does not count even when d == 0
                total += dfs(pos + 1, count, true, nextLimit);
            } else {
                int nextCount = (digit == target) ? count + 1 : count;
                total += dfs(pos + 1, nextCount, false, nextLimit);
            }
        }

        if (free) {
            memo[pos][count] = total;
        }
        return total;
    }
}
