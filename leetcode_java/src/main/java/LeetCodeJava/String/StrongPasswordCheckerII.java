package LeetCodeJava.String;

// https://leetcode.com/problems/strong-password-checker-ii/description/

/**
 * 2299. Strong Password Checker II
 * Easy
 *
 * A password is said to be strong if it satisfies all the following criteria:
 *
 * It has at least 8 characters.
 * It contains at least one lowercase letter.
 * It contains at least one uppercase letter.
 * It contains at least one digit.
 * It contains at least one special character. The special characters are the characters in the following string: "!@#$%^&*()-+".
 * It does not contain 2 of the same character in adjacent positions (i.e., "aab" violates this condition, but "aba" does not).
 *
 * Given a string password, return true if it is a strong password. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: password = "IloveLe3tcode!"
 * Output: true
 * Explanation: The password meets all the requirements. Therefore, we return true.
 *
 * Example 2:
 *
 * Input: password = "Me+You--IsMyDream"
 * Output: false
 * Explanation: The password does not contain a digit and also contains 2 of the same character in adjacent positions. Therefore, we return false.
 *
 * Example 3:
 *
 * Input: password = "1aB!"
 * Output: false
 * Explanation: The password does not meet the length requirement. Therefore, we return false.
 *
 *
 * Constraints:
 *
 * 1 <= password.length <= 100
 * password consists of letters, digits, and special characters: "!@#$%^&*()-+".
 *
 */
public class StrongPasswordCheckerII {

    // V0
    // IDEA: CHECK THE SIX RULES DIRECTLY IN ONE SCAN
    //       four existence flags (lower, upper, digit, special), one adjacency
    //       test, plus the length check up front
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean strongPasswordCheckerII(String password) {
        final String special = "!@#$%^&*()-+";

        if (password.length() < 8) {
            return false;
        }

        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            // rule 6: no two equal characters next to each other
            if (i > 0 && c == password.charAt(i - 1)) {
                return false;
            }
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (special.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        return hasLower && hasUpper && hasDigit && hasSpecial;
    }
}
