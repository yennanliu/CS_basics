package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-addition-to-make-integer-beautiful/description/

import java.util.*;

/**
 * 2457. Minimum Addition to Make Integer Beautiful
 * Medium
 *
 * You are given two positive integers n and target.
 *
 * An integer is considered beautiful if the sum of its digits is less than or equal to target.
 *
 * Return the minimum non-negative integer x such that n + x is beautiful. The input will be generated such that it is always possible to make n beautiful.
 *
 *
 * Example 1:
 *
 * Input: n = 16, target = 6
 * Output: 4
 * Explanation: Initially n is 16 and its digit sum is 1 + 6 = 7. After adding 4, n becomes 20 and digit sum becomes 2 + 0 = 2. It can be shown that we can not make n beautiful with adding non-negative integer less than 4.
 *
 * Example 2:
 *
 * Input: n = 467, target = 6
 * Output: 33
 * Explanation: Initially n is 467 and its digit sum is 4 + 6 + 7 = 17. After adding 33, n becomes 500 and digit sum becomes 5 + 0 + 0 = 5. It can be shown that we can not make n beautiful with adding non-negative integer less than 33.
 *
 * Example 3:
 *
 * Input: n = 1, target = 1
 * Output: 0
 * Explanation: Initially n is 1 and its digit sum is 1, which is already smaller than or equal to target.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^12
 * 1 <= target <= 20
 *
 */
public class MinimumAdditionToMakeIntegerBeautiful {

    // V0
    // IDEA: GREEDY ROUND-UP (CLEAR THE TRAILING DIGITS ONE PLACE AT A TIME)
    //       rounding n up to the next multiple of 10^k zeroes its last k digits for one carry.
    //       Try k = 1, 2, 3, ... and stop at the first value whose digit sum fits.
    /**
     * time = O(D^2), D = number of digits (D <= 13)
     * space = O(1)
     */
    public long makeIntegerBeautiful(long n, int target) {
        long value = n;
        long power = 10;
        while (digitSum(value) > target) {
            // round value up to the next multiple of power
            value = (value / power + 1) * power;
            power *= 10;
        }
        return value - n;
    }

    /** Sum of the decimal digits of x. */
    private int digitSum(long x) {
        int sum = 0;
        while (x > 0) {
            sum += (int) (x % 10);
            x /= 10;
        }
        return sum;
    }
}
