package LeetCodeJava.Math;

// https://leetcode.com/problems/remove-9/description/
/**
 * 660. Remove 9
 * Hard
 * Lock: Prime
 *
 * Start from integer 1, remove any integer that contains 9 such as 9, 19, 29...
 *
 * Now, you will have a new integer sequence [1, 2, 3, 4, 5, 6, 7, 8, 10, 11, ...].
 *
 * Given an integer n, return the nth (1-indexed) integer in the new sequence.
 *
 * Example 1:
 *
 * Input: n = 9
 * Output: 10
 *
 * Example 2:
 *
 * Input: n = 10
 * Output: 11
 *
 * Constraints:
 *
 * 1 <= n <= 8 * 10^8
 *
 */
public class Remove9 {

    // V0
    // IDEA: BASE 9 -- the sequence IS the base-9 numbers read as base-10
    /**
     *   Removing every integer containing the digit 9 leaves EXACTLY the numbers
     *   whose decimal digits all come from {0..8} -- i.e. valid BASE-9 numerals.
     *   Listed in increasing order they line up ONE-TO-ONE with 1, 2, 3, ... in base 9.
     *
     *   So: write n in BASE 9, then REINTERPRET those same digits as a DECIMAL number.
     *
     *     n = 9  -> base9 "10" -> 10
     *     n = 10 -> base9 "11" -> 11
     *
     *   NOTE !!! n reaches 8 * 10^8, whose base-9 form has 10 digits;
     *            read as decimal that OVERFLOWS int -> build it as `long`
     *            (the problem guarantees the answer fits in the return type).
     *
     *   time  = O(log(n))
     *   space = O(log(n))
     */
    public int newInteger(int n) {
        StringBuilder digits = new StringBuilder();
        while (n > 0) {
            digits.append(n % 9);
            n /= 9;
        }
        // digits were collected LEAST-significant first
        return (int) Long.parseLong(digits.reverse().toString());
    }

}
