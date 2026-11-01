package LeetCodeJava.Greedy;

// https://leetcode.com/problems/latest-time-by-replacing-hidden-digits/description/

/**
 * 1736. Latest Time by Replacing Hidden Digits
 * Easy
 *
 * You are given a string time in the form of hh:mm, where some of the digits in the
 * string are hidden (represented by ?).
 *
 * The valid times are those inclusively between 00:00 and 23:59.
 *
 * Return the latest valid time you can get from time by replacing the hidden digits.
 *
 *
 * Example 1:
 *
 * Input: time = "2?:?0"
 * Output: "23:50"
 *
 * Example 2:
 *
 * Input: time = "0?:3?"
 * Output: "09:39"
 *
 * Example 3:
 *
 * Input: time = "1?:22"
 * Output: "19:22"
 *
 *
 * Constraints:
 *
 * time is in the format hh:mm.
 * It is guaranteed that you can produce a valid time from the given string.
 *
 */
public class LatestTimeByReplacingHiddenDigits {

    // V0
    // IDEA: GREEDY, DIGIT BY DIGIT (EARLIER DIGITS DOMINATE)
    //       hour tens: '2' unless the hour ones digit is a known 4..9 (then '1').
    //       hour ones: '3' if the tens is '2', else '9'. minute tens '5', ones '9'.
    //       decide the hour tens BEFORE the hour ones: the ones cap depends on it.
    /**
     * time = O(1)
     * space = O(1)
     */
    public String maximumTime(String time) {
        char[] digits = time.toCharArray();

        if (digits[0] == '?') {
            boolean onesTooBigForTwenty = digits[1] >= '4' && digits[1] <= '9';
            if (onesTooBigForTwenty) {
                digits[0] = '1';
            } else {
                digits[0] = '2';
            }
        }
        if (digits[1] == '?') {
            if (digits[0] == '2') {
                digits[1] = '3';
            } else {
                digits[1] = '9';
            }
        }
        if (digits[3] == '?') {
            digits[3] = '5';
        }
        if (digits[4] == '?') {
            digits[4] = '9';
        }
        return new String(digits);
    }
}
