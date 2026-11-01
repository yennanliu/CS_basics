package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-least-frequent-digit/description/

/**
 * 3663. Find The Least Frequent Digit
 * Easy
 *
 * Given an integer n, find the digit that occurs least frequently in its
 * decimal representation. If multiple digits have the same frequency, choose
 * the smallest digit.
 *
 * Return the chosen digit as an integer.
 *
 * The frequency of a digit x is the number of times it appears in the decimal
 * representation of n.
 *
 * Example 1:
 *
 * Input: n = 1553322
 * Output: 1
 * Explanation:
 * The least frequent digit in n is 1, which appears only once. All other
 * digits appear twice.
 *
 * Example 2:
 *
 * Input: n = 723344511
 * Output: 2
 * Explanation:
 * The least frequent digits in n are 7, 2, and 5; each appears only once.
 *
 * Constraints:
 *
 * 1 <= n <= 2^31 - 1
 *
 */
public class FindTheLeastFrequentDigit {

    // V0
    // IDEA: COUNT DIGITS, THEN PICK THE SMALLEST COUNT AMONG DIGITS THAT ACTUALLY OCCUR
    //       a digit with count 0 must never win. scanning digits 0..9 in ascending order and
    //       replacing only on a STRICTLY smaller count settles the "smallest digit" tie-break.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int getLeastFrequentDigit(int n) {
        int[] count = new int[10];
        int remaining = n;
        while (remaining > 0) {
            count[remaining % 10]++;
            remaining /= 10;
        }

        int bestDigit = -1;
        for (int digit = 0; digit <= 9; digit++) {
            if (count[digit] == 0) {
                continue; // absent digits are not candidates
            }
            if (bestDigit == -1 || count[digit] < count[bestDigit]) {
                bestDigit = digit;
            }
        }
        return bestDigit;
    }
}
