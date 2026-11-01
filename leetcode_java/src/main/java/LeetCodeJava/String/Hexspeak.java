package LeetCodeJava.String;

// https://leetcode.com/problems/hexspeak/description/
// https://leetcode.ca/2019-05-24-1271-Hexspeak/

/**
 * 1271. Hexspeak
 * Easy
 *
 * A decimal number can be converted to its Hexspeak representation by first
 * converting it to an uppercase hexadecimal string, then replacing all
 * occurrences of the digit '0' with the letter 'O', and the digit '1' with the
 * letter 'I'. Such a representation is valid if and only if it consists only of
 * the letters in the set {'A', 'B', 'C', 'D', 'E', 'F', 'I', 'O'}.
 *
 * Given a string num representing a decimal integer n, return the Hexspeak
 * representation of n if it is valid, otherwise return "ERROR".
 *
 *
 * Example 1:
 *
 * Input: num = "257"
 * Output: "IOI"
 * Explanation: 257 is 101 in hexadecimal.
 *
 * Example 2:
 *
 * Input: num = "3"
 * Output: "ERROR"
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 12
 * num does not contain leading zeros.
 * num represents an integer in the range [1, 10^12].
 *
 */
public class Hexspeak {

    // V0
    // IDEA: BASE CONVERSION + CHAR MAP
    //       convert to uppercase hex, map '0' -> 'O' and '1' -> 'I';
    //       any other digit (2..9) has no letter form -> "ERROR".
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public String toHexspeak(String num) {
        // num can be up to 10^12, which does not fit in an int
        String hex = Long.toHexString(Long.parseLong(num)).toUpperCase();

        StringBuilder sb = new StringBuilder();
        for (char c : hex.toCharArray()) {
            if (c == '0') {
                sb.append('O');
            } else if (c == '1') {
                sb.append('I');
            } else if (Character.isDigit(c)) {
                // 2 ~ 9 have no letter counterpart
                return "ERROR";
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
