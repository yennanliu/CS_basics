package LeetCodeJava.String;

// https://leetcode.com/problems/thousand-separator/description/

/**
 * 1556. Thousand Separator
 * Easy
 *
 * Given an integer n, add a dot (".") as the thousands separator and return it in string format.
 *
 * Example 1:
 *
 * Input: n = 987
 * Output: "987"
 *
 * Example 2:
 *
 * Input: n = 1234
 * Output: "1.234"
 *
 * Constraints:
 *
 * 0 <= n <= 2^31 - 1
 *
 */
public class ThousandSeparator {

    // V0
    // IDEA: STRING (CUT THE DECIMAL FORM INTO 3-DIGIT GROUPS FROM THE RIGHT)
    /**
     * time = O(D)   (D = number of digits)
     * space = O(D)
     */
    public String thousandSeparator(int n) {
        String digits = String.valueOf(n);
        StringBuilder result = new StringBuilder();

        // walk from the left; a dot goes before every position whose
        // distance to the end is a positive multiple of 3
        for (int i = 0; i < digits.length(); i++) {
            int remaining = digits.length() - i;
            if (i > 0 && remaining % 3 == 0) {
                result.append('.');
            }
            result.append(digits.charAt(i));
        }
        return result.toString();
    }
}
