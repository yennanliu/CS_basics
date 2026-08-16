package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/description/
/**
 * 600. Non-negative Integers without Consecutive Ones
 * Hard
 *
 * Given a positive integer n, return the number of the integers in the range [0, n]
 * whose binary representations do NOT contain consecutive ones.
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: 5
 * Explanation:
 * Here are the non-negative integers <= 5 with their corresponding binary
 * representations:
 * 0 : 0
 * 1 : 1
 * 2 : 10
 * 3 : 11
 * 4 : 100
 * 5 : 101
 * Among them, only integer 3 disobeys the rule (two consecutive ones) and the other 5
 * satisfy the rule.
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: 2
 *
 * Example 3:
 *
 * Input: n = 2
 * Output: 3
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class NonNegativeIntegersWithoutConsecutiveOnes {

    // V0
    // IDEA: FIBONACCI + BIT SCAN (greedy digit counting)
    /**
     *   f[i] = # of binary strings of length i with NO two adjacent 1s
     *        -> f[0] = 1 (empty), f[1] = 2 ("0", "1"), f[i] = f[i-1] + f[i-2]
     *      (i.e. the Fibonacci numbers)
     *
     *   Scan the bits of n from HIGH to LOW.
     *   Whenever bit i of n is 1, every number that puts a 0 at bit i (and keeps
     *   the higher bits IDENTICAL to n) is strictly SMALLER than n, and the lower
     *   i bits are then COMPLETELY FREE -> that adds f[i] valid numbers.
     *
     *   Then we `commit` to putting a 1 at bit i and move on.
     *
     *   NOTE !!! if the previously committed bit was ALSO 1, the prefix itself is
     *            already invalid, so no number >= that prefix can be counted
     *            -> STOP EARLY (and n itself is not counted).
     *
     *   If we never break, n itself is valid, so add 1 at the end.
     *
     *   time  = O(32) = O(1)
     *   space = O(32) = O(1)
     */
    public int findIntegers(int n) {
        // f[i] = count of valid binary strings of length i
        int[] f = new int[32];
        f[0] = 1;
        f[1] = 2;
        for (int i = 2; i < 32; i++) {
            f[i] = f[i - 1] + f[i - 2];
        }

        int res = 0;
        int prevBit = 0;

        for (int i = 30; i >= 0; i--) {
            if (((n >> i) & 1) == 1) {
                // put 0 here -> the lower i bits are FREE
                res += f[i];
                if (prevBit == 1) {
                    // prefix "11..." already invalid -> n itself is NOT counted
                    return res;
                }
                prevBit = 1;
            } else {
                prevBit = 0;
            }
        }

        // n itself is valid
        return res + 1;
    }

}
