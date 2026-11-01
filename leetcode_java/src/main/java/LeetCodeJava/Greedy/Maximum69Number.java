package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-69-number/description/

/**
 * 1323. Maximum 69 Number
 * Easy
 *
 * You are given a positive integer num consisting only of digits 6 and 9.
 *
 * Return the maximum number you can get by changing at most one digit (6 becomes 9, and 9
 * becomes 6).
 *
 *
 * Example 1:
 *
 * Input: num = 9669
 * Output: 9969
 * Explanation:
 * Changing the first digit results in 6669.
 * Changing the second digit results in 9969.
 * Changing the third digit results in 9699.
 * Changing the fourth digit results in 9666.
 * The maximum number is 9969.
 *
 * Example 2:
 *
 * Input: num = 9996
 * Output: 9999
 * Explanation: Changing the last digit 6 to 9 results in the maximum number.
 *
 * Example 3:
 *
 * Input: num = 9999
 * Output: 9999
 * Explanation: It is better not to apply any change.
 *
 *
 * Constraints:
 *
 * 1 <= num <= 10^4
 * num consists of only 6 and 9 digits.
 *
 */
public class Maximum69Number {

    // V0
    // IDEA: GREEDY - FLIP THE LEFTMOST 6 INTO A 9
    //   turning a 9 into a 6 only shrinks the number, so the one edit worth making is 6 -> 9,
    //   and the most significant 6 gains the largest place value. no 6 -> num is the answer.
    /**
     * time = O(D), D = number of digits
     * space = O(D)
     */
    public int maximum69Number(int num) {
        char[] digits = String.valueOf(num).toCharArray();
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] == '6') {
                digits[i] = '9';
                // only one change is allowed
                break;
            }
        }
        return Integer.parseInt(new String(digits));
    }
}
