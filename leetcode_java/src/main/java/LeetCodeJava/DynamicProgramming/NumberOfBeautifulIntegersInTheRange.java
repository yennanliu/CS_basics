package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-beautiful-integers-in-the-range/description/

import java.util.*;

/**
 * 2827. Number of Beautiful Integers in the Range
 * Hard
 *
 * You are given positive integers low, high, and k.
 *
 * A number is beautiful if it meets both of the following conditions:
 *
 * The count of even digits in the number is equal to the count of odd digits.
 * The number is divisible by k.
 *
 * Return the number of beautiful integers in the range [low, high].
 *
 *
 * Example 1:
 *
 * Input: low = 10, high = 20, k = 3
 * Output: 2
 * Explanation: There are 2 beautiful integers in the given range: [12,18].
 * - 12 is beautiful because it contains 1 odd digit and 1 even digit, and is divisible by k = 3.
 * - 18 is beautiful because it contains 1 odd digit and 1 even digit, and is divisible by k = 3.
 * Additionally we can see that:
 * - 16 is not beautiful because it is not divisible by k = 3.
 * - 15 is not beautiful because it does not contain equal counts even and odd digits.
 * It can be shown that there are only 2 beautiful integers in the given range.
 *
 * Example 2:
 *
 * Input: low = 1, high = 10, k = 1
 * Output: 1
 * Explanation: There is 1 beautiful integer in the given range: [10].
 * - 10 is beautiful because it contains 1 odd digit and 1 even digit, and is divisible by k = 1.
 * It can be shown that there is only 1 beautiful integer in the given range.
 *
 * Example 3:
 *
 * Input: low = 5, high = 5, k = 2
 * Output: 0
 * Explanation: There are 0 beautiful integers in the given range.
 * - 5 is not beautiful because it is not divisible by k = 2 and it does not contain equal even and odd digits.
 *
 *
 * Constraints:
 *
 * 0 < low <= high <= 10^9
 * 0 < k <= 20
 *
 */
public class NumberOfBeautifulIntegersInTheRange {

    // V0
    // IDEA: DIGIT DP + PREFIX SUBTRACTION
    //   answer = count(high) - count(low - 1), where count(x) = beautiful integers in [1, x].
    //   Build x digit by digit with state (pos, mod, diff, leading, tight):
    //     mod  = value so far modulo k, diff = #odd - #even digits so far,
    //     leading = still inside the leading-zero run (zeros there are NOT even digits),
    //     tight = prefix equals x's prefix, so the next digit is capped.
    /**
     * time = O(D * k * D * 10) per bound, D = number of digits (<= 10)
     * space = O(D * k * D)
     */
    public int numberOfBeautifulIntegers(int low, int high, int k) {
        return countUpTo(high, k) - countUpTo(low - 1, k);
    }

    /** Number of beautiful integers in [1, x]. */
    private int countUpTo(int x, int k) {
        if (x <= 0) {
            return 0;
        }
        char[] digits = Integer.toString(x).toCharArray();
        int n = digits.length;
        // diff ranges over [-n, n]; shift by n so it can index an array
        // memo[pos][mod][diff + n][leading][tight], -1 = not computed yet
        int[][][][][] memo = new int[n][k][2 * n + 1][2][2];
        for (int[][][][] a : memo) {
            for (int[][][] b : a) {
                for (int[][] c : b) {
                    for (int[] d : c) {
                        Arrays.fill(d, -1);
                    }
                }
            }
        }
        return dfs(digits, 0, 0, 0, true, true, k, memo);
    }

    private int dfs(char[] digits, int pos, int mod, int diff, boolean leading, boolean tight,
                    int k, int[][][][][] memo) {
        int n = digits.length;
        if (pos == n) {
            // all leading zeros means the integer 0, which is outside [1, x]
            if (leading) {
                return 0;
            }
            return (mod == 0 && diff == 0) ? 1 : 0;
        }
        int leadIdx = leading ? 1 : 0;
        int tightIdx = tight ? 1 : 0;
        if (memo[pos][mod][diff + n][leadIdx][tightIdx] != -1) {
            return memo[pos][mod][diff + n][leadIdx][tightIdx];
        }

        // the largest digit allowed at this position
        int upper = tight ? digits[pos] - '0' : 9;
        int count = 0;
        for (int d = 0; d <= upper; d++) {
            boolean nextTight = tight && (d == upper);
            if (leading && d == 0) {
                // still a leading zero: it does not count as an even digit
                count += dfs(digits, pos + 1, 0, 0, true, nextTight, k, memo);
            } else {
                int nextDiff = (d % 2 == 1) ? diff + 1 : diff - 1;
                int nextMod = (mod * 10 + d) % k;
                count += dfs(digits, pos + 1, nextMod, nextDiff, false, nextTight, k, memo);
            }
        }

        memo[pos][mod][diff + n][leadIdx][tightIdx] = count;
        return count;
    }
}
