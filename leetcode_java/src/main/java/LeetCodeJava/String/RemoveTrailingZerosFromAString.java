package LeetCodeJava.String;

// https://leetcode.com/problems/remove-trailing-zeros-from-a-string/description/

/**
 * 2710. Remove Trailing Zeros From a String
 * Easy
 *
 * Given a positive integer num represented as a string, return the integer num without trailing zeros as a string.
 *
 *
 * Example 1:
 *
 * Input: num = "51230100"
 * Output: "512301"
 * Explanation: Integer "51230100" has 2 trailing zeros, we remove them and return integer "512301".
 *
 * Example 2:
 *
 * Input: num = "123"
 * Output: "123"
 * Explanation: Integer "123" has no trailing zeros, we return integer "123".
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 1000
 * num consists of only digits.
 * num doesn't have any leading zeros.
 *
 */
public class RemoveTrailingZerosFromAString {

    // V0
    // IDEA: SCAN FROM THE RIGHT, CUT AFTER THE LAST NON-ZERO DIGIT
    //       num is positive with no leading zeros, so the scan always stops
    //       on a non-'0' digit and the result is never empty
    /**
     * time = O(N)
     * space = O(1) beyond the output
     */
    public String removeTrailingZeros(String num) {
        int end = num.length() - 1;
        while (end >= 0 && num.charAt(end) == '0') {
            end--;
        }
        return num.substring(0, end + 1);
    }
}
