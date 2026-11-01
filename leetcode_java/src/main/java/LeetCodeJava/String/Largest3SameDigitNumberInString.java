package LeetCodeJava.String;

// https://leetcode.com/problems/largest-3-same-digit-number-in-string/description/

/**
 * 2264. Largest 3-Same-Digit Number in String
 * Easy
 *
 * You are given a string num representing a large integer. An integer is good if it meets the following conditions:
 *
 * It is a substring of num with length 3.
 * It consists of only one unique digit.
 *
 * Return the maximum good integer as a string or an empty string "" if no such integer exists.
 *
 * Note:
 *
 * A substring is a contiguous sequence of characters within a string.
 * There may be leading zeroes in num or a good integer.
 *
 *
 * Example 1:
 *
 * Input: num = "6777133339"
 * Output: "777"
 * Explanation: There are two distinct good integers: "777" and "333".
 * "777" is the largest, so we return "777".
 *
 * Example 2:
 *
 * Input: num = "2300019"
 * Output: "000"
 * Explanation: "000" is the only good integer.
 *
 * Example 3:
 *
 * Input: num = "42352338"
 * Output: ""
 * Explanation: No substring of length 3 consists of only one unique digit. Therefore, there are no good integers.
 *
 *
 * Constraints:
 *
 * 3 <= num.length <= 1000
 * num only consists of digits.
 *
 */
public class Largest3SameDigitNumberInString {

    // V0
    // IDEA: ONE PASS — TRACK THE LARGEST DIGIT THAT ENDS A RUN OF THREE
    /**
     * time = O(N)
     * space = O(1)
     */
    public String largestGoodInteger(String num) {
        // -1 = no good integer found yet ("000" is a valid answer, so 0 cannot mean "none")
        int bestDigit = -1;
        for (int i = 2; i < num.length(); i++) {
            char c = num.charAt(i);
            if (c == num.charAt(i - 1) && c == num.charAt(i - 2)) {
                bestDigit = Math.max(bestDigit, c - '0');
            }
        }
        if (bestDigit == -1) {
            return "";
        }
        char digit = (char) ('0' + bestDigit);
        return "" + digit + digit + digit;
    }
}
