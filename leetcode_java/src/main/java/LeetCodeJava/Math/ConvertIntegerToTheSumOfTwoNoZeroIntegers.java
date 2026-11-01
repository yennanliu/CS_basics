package LeetCodeJava.Math;

// https://leetcode.com/problems/convert-integer-to-the-sum-of-two-no-zero-integers/description/

/**
 * 1317. Convert Integer to the Sum of Two No-Zero Integers
 * Easy
 *
 * No-Zero integer is a positive integer that does not contain any 0 in its decimal
 * representation.
 *
 * Given an integer n, return a list of two integers [a, b] where:
 *
 * - a and b are No-Zero integers.
 * - a + b = n
 *
 * The test cases are generated so that there is at least one valid solution. If there are many
 * valid solutions, you can return any of them.
 *
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: [1,1]
 * Explanation: Let a = 1 and b = 1.
 * Both a and b are no-zero integers, and a + b = 2 = n.
 *
 * Example 2:
 *
 * Input: n = 11
 * Output: [2,9]
 * Explanation: Let a = 2 and b = 9.
 * Both a and b are no-zero integers, and a + b = 11 = n.
 * Note that there are other valid answers as [8, 3] that can be accepted.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^4
 *
 */
public class ConvertIntegerToTheSumOfTwoNoZeroIntegers {

    // V0
    // IDEA: BRUTE FORCE - TRY EVERY a, LET b = n - a, RETURN THE FIRST PAIR WITH NO ZERO DIGIT
    /**
     * time = O(N log N)
     * space = O(1)
     */
    public int[] getNoZeroIntegers(int n) {
        for (int a = 1; a < n; a++) {
            int b = n - a;
            if (hasNoZeroDigit(a) && hasNoZeroDigit(b)) {
                return new int[] {a, b};
            }
        }
        // unreachable: the problem guarantees a valid answer
        return new int[0];
    }

    /** True when no decimal digit of x (x > 0) is 0. */
    private boolean hasNoZeroDigit(int x) {
        while (x > 0) {
            if (x % 10 == 0) {
                return false;
            }
            x /= 10;
        }
        return true;
    }
}
