package LeetCodeJava.Greedy;

// https://leetcode.com/problems/remove-k-digits/description/

import java.util.Stack;

/**
 * 402. Remove K Digits
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given string num representing a non-negative integer num, and an integer k, return the smallest possible integer after removing k digits from num.
 *
 *
 *
 * Example 1:
 *
 * Input: num = "1432219", k = 3
 * Output: "1219"
 * Explanation: Remove the three digits 4, 3, and 2 to form the new number 1219 which is the smallest.
 * Example 2:
 *
 * Input: num = "10200", k = 1
 * Output: "200"
 * Explanation: Remove the leading 1 and the number is 200. Note that the output must not contain leading zeroes.
 * Example 3:
 *
 * Input: num = "10", k = 2
 * Output: "0"
 * Explanation: Remove all the digits from the number and it is left with nothing which is 0.
 *
 *
 * Constraints:
 *
 * 1 <= k <= num.length <= 105
 * num consists of only digits.
 * num does not have any leading zeros except for the zero itself.
 *
 */
public class RemoveKDigits {

    // V0
//    public String removeKdigits(String num, int k) {
//
//    }

    // V1
    // IDEA: STACK
    // https://leetcode.com/problems/remove-k-digits/solutions/88708/straightforward-java-solution-using-stac-2ny3/
    public String removeKdigits_1(String num, int k) {
        int len = num.length();
        //corner case
        if (k == len)
            return "0";

        Stack<Character> stack = new Stack<>();
        int i = 0;
        while (i < num.length()) {
            //whenever meet a digit which is less than the previous digit, discard the previous one
            while (k > 0 && !stack.isEmpty() && stack.peek() > num.charAt(i)) {
                stack.pop();
                k--;
            }
            stack.push(num.charAt(i));
            i++;
        }

        // corner case like "1111"
        while (k > 0) {
            stack.pop();
            k--;
        }

        //construct the number from the stack
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty())
            sb.append(stack.pop());
        sb.reverse();

        //remove all the 0 at the head
        while (sb.length() > 1 && sb.charAt(0) == '0')
            sb.deleteCharAt(0);
        return sb.toString();
    }



    // V2
    // IDEA: STACK
    // https://leetcode.com/problems/remove-k-digits/solutions/5005706/faster-lesserdetailed-explainationstackg-ve6y/
    public String removeKdigits_2(String num, int k) {
        Stack<Character> stack = new Stack<>();

        for (char digit : num.toCharArray()) {
            while (!stack.isEmpty() && k > 0 && stack.peek() > digit) {
                stack.pop();
                k--;
            }
            stack.push(digit);
        }

        // Remove remaining k digits from the end of the stack
        while (k > 0 && !stack.isEmpty()) {
            stack.pop();
            k--;
        }

        // Construct the resulting string from the stack
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
        sb.reverse(); // Reverse to get the correct order

        // Remove leading zeros
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }

        // Handle edge case where result might be empty
        return sb.length() > 0 ? sb.toString() : "0";
    }




}
