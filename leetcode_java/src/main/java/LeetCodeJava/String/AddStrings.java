package LeetCodeJava.String;

// https://leetcode.com/problems/add-strings/description/
/**
 * 415. Add Strings
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given two non-negative integers, num1 and num2 represented as string, return the sum of num1 and num2 as a string.
 *
 * You must solve the problem without using any built-in library for handling large integers (such as BigInteger). You must also not convert the inputs to integers directly.
 *
 *
 *
 * Example 1:
 *
 * Input: num1 = "11", num2 = "123"
 * Output: "134"
 * Example 2:
 *
 * Input: num1 = "456", num2 = "77"
 * Output: "533"
 * Example 3:
 *
 * Input: num1 = "0", num2 = "0"
 * Output: "0"
 *
 *
 * Constraints:
 *
 * 1 <= num1.length, num2.length <= 104
 * num1 and num2 consist of only digits.
 * num1 and num2 don't have any leading zeros except for the zero itself.
 *
 *
 */
public class AddStrings {

    // V0
//    public String addStrings(String num1, String num2) {
//
//    }

    // V1
    // https://leetcode.com/problems/add-strings/solutions/6789491/simple-java-code-by-vikrant_heer-64g6/
    public String addStrings_1(String num1, String num2) {
        StringBuilder result = new StringBuilder();
        int pointer1 = num1.length() - 1;
        int pointer2 = num2.length() - 1;
        int carry = 0;

        while (pointer1 >= 0 || pointer2 >= 0 || carry > 0) {
            int digit1 = (pointer1 >= 0) ? num1.charAt(pointer1--) - '0' : 0;
            int digit2 = (pointer2 >= 0) ? num2.charAt(pointer2--) - '0' : 0;

            int sum = digit1 + digit2 + carry;
            result.append(sum % 10);
            carry = sum / 10;
        }

        return result.reverse().toString();
    }

    // V2

}
