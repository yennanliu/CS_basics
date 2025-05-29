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
    // IDEA: string op (fixed by gpt)
    public String addStrings(String num1, String num2) {
        if (num1 == null || num2 == null) {
            if (num1 == null) {
                return num2;
            }
            return num1;
        }
        if (num1.equals("0") && num2.equals("0")) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();

        int plus = 0;
        int idx_1 = num1.length() - 1;
        int idx_2 = num2.length() - 1;

        /** NOTE !!!
         *
         *  1. while loop
         *  2. idx_1 >= 0 or idx_2 >= 0
         */
        while (idx_1 >= 0 || idx_2 >= 0) {

            int v1 = 0;
            int v2 = 0;

            int new_val = 0;

            /** NOTE !!!
             *
             *  if idx_1 >= 0, then get val from it
             */
            if (idx_1 >= 0) {
                v1 = Integer.parseInt(String.valueOf(num1.charAt(idx_1)));
                idx_1 -= 1;
            }

            /** NOTE !!!
             *
             *  if idx_1 >= 0, then get val from it
             */
            if (idx_2 >= 0) {
                v2 = Integer.parseInt(String.valueOf(num2.charAt(idx_2)));
                idx_2 -= 1;
            }

            new_val = (new_val + v1 + v2 + plus);

            /** NOTE !!!
             *
             *  if new_vla > 9,
             *  we should `subtract 10` (instead of 9)
             */
            if (new_val > 9) {
                plus = 1;
                new_val -= 10;
            } else {
                plus = 0;
            }

            sb.append(new_val);
        }

        /** NOTE !!!
         *
         *  need to add the `remaining plus` to res
         *  if there is it
         */
        if (plus > 0) {
            sb.append(plus);
        }

        // reverse
        return sb.reverse().toString();
    }

    // V0-1
    // IDEA: string op (fixed by gpt)
    public String addStrings_0_1(String num1, String num2) {
        StringBuilder sb = new StringBuilder();

        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0;

        while (i >= 0 || j >= 0 || carry != 0) {
            int digit1 = i >= 0 ? num1.charAt(i) - '0' : 0;
            int digit2 = j >= 0 ? num2.charAt(j) - '0' : 0;

            int sum = digit1 + digit2 + carry;
            carry = sum / 10;
            sb.append(sum % 10);

            i--;
            j--;
        }

        return sb.reverse().toString();
    }

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
