package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-operations-to-make-the-integer-zero/description/

/**
 * 2749. Minimum Operations to Make the Integer Zero
 * Medium
 *
 * You are given two integers num1 and num2.
 *
 * In one operation, you can choose integer i in the range [0, 60] and subtract 2^i + num2 from num1.
 *
 * Return the integer denoting the minimum number of operations needed to make num1 equal to 0.
 *
 * If it is impossible to make num1 equal to 0, return -1.
 *
 *
 * Example 1:
 *
 * Input: num1 = 3, num2 = -2
 * Output: 3
 *
 * Example 2:
 *
 * Input: num1 = 5, num2 = 7
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= num1 <= 10^9
 * -10^9 <= num2 <= 10^9
 *
 */
public class MinimumOperationsToMakeTheIntegerZero {

    private static final int MAX_OPERATIONS = 60;

    // V0
    // IDEA: ENUMERATE THE OPERATION COUNT k -- x = num1 - k * num2 must be a sum of EXACTLY k powers of two,
    //       which holds iff popcount(x) <= k <= x
    /**
     * time = O(1)  (k <= 60)
     * space = O(1)
     */
    public int makeTheIntegerZero(int num1, int num2) {
        for (int k = 1; k <= MAX_OPERATIONS; k++) {
            // x reaches ~6 * 10^10 -> long
            long x = (long) num1 - (long) k * num2;
            if (x < 0) {
                // num2 > 0 here, so x only keeps shrinking
                return -1;
            }
            // at least popcount(x) terms are needed; each term is >= 1 so at most x terms
            if (Long.bitCount(x) <= k && k <= x) {
                return k;
            }
        }
        return -1;
    }
}
