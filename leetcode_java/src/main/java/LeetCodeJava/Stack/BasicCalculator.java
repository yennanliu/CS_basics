package LeetCodeJava.Stack;

// https://leetcode.com/problems/basic-calculator/description/

import java.util.Stack;

/**
 *  224. Basic Calculator
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given a string s representing a valid expression, implement a basic calculator to evaluate it, and return the result of the evaluation.
 *
 * Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().
 *
 *
 *
 * Example 1:
 *
 * Input: s = "1 + 1"
 * Output: 2
 * Example 2:
 *
 * Input: s = " 2-1 + 2 "
 * Output: 3
 * Example 3:
 *
 * Input: s = "(1+(4+5+2)-3)+(6+8)"
 * Output: 23
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 3 * 105
 * s consists of digits, '+', '-', '(', ')', and ' '.
 * s represents a valid expression.
 * '+' is not used as a unary operation (i.e., "+1" and "+(2 + 3)" is invalid).
 * '-' could be used as a unary operation (i.e., "-1" and "-(2 + 3)" is valid).
 * There will be no two consecutive operators in the input.
 * Every number and running calculation will fit in a signed 32-bit integer.
 *
 *
 *
 */
public class BasicCalculator {

    // V0
//    public int calculate(String s) {
//
//    }


    // V1


    // V2
    // IDEA: STACK
    // https://leetcode.com/problems/basic-calculator/solutions/7576826/most-optimal-beats-100-all-languages-sta-jd4n/
    public int calculate_2(String s) {

        Stack<Integer> stack = new Stack<>();

        int res = 0;
        int curr = 0;
        int sign = 1;

        for (char c : s.toCharArray()) {

            if (Character.isDigit(c)) {
                curr = curr * 10 + (c - '0');
            } else if (c == '+') {
                res += curr * sign;
                sign = 1;
                curr = 0;
            } else if (c == '-') {
                res += curr * sign;
                sign = -1;
                curr = 0;
            } else if (c == '(') {
                stack.push(res);
                stack.push(sign);
                res = 0;
                sign = 1;
                curr = 0;
            } else if (c == ')') {
                res += curr * sign;
                curr = 0;
                res *= stack.pop();
                res += stack.pop();
            }
        }

        res += sign * curr;
        return res;
    }


    // V3
    // IDEA: STACK
    // https://leetcode.com/problems/basic-calculator/solutions/62361/iterative-java-solution-with-stack-by-so-7qs7/
    public int calculate_3(String s) {
        Stack<Integer> stack = new Stack<Integer>();
        int result = 0;
        int number = 0;
        int sign = 1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                number = 10 * number + (int) (c - '0');
            } else if (c == '+') {
                result += sign * number;
                number = 0;
                sign = 1;
            } else if (c == '-') {
                result += sign * number;
                number = 0;
                sign = -1;
            } else if (c == '(') {
                //we push the result first, then sign;
                stack.push(result);
                stack.push(sign);
                //reset the sign and result for the value in the parenthesis
                sign = 1;
                result = 0;
            } else if (c == ')') {
                result += sign * number;
                number = 0;
                result *= stack.pop(); //stack.pop() is the sign before the parenthesis
                result += stack.pop(); //stack.pop() now is the result calculated before the parenthesis

            }
        }
        if (number != 0)
            result += sign * number;
        return result;
    }


    // V4
    // IDEA: STACK
    // https://leetcode.com/problems/basic-calculator/solutions/8078322/hidden-overflow-behavior-in-java-int-sol-hgo0/
    public int calculate_4(String s) {

        Stack<Long> stack = new Stack<>();

        long res = 0;
        long curr = 0;
        long sign = 1;

        for (char c : s.toCharArray()) {

            if (Character.isDigit(c)) {
                curr = curr * 10 + (c - '0');
            } else if (c == '+') {
                res += curr * sign;
                sign = 1;
                curr = 0;
            } else if (c == '-') {
                res += curr * sign;
                sign = -1;
                curr = 0;
            } else if (c == '(') {
                stack.push(res);
                stack.push(sign);
                res = 0;
                sign = 1;
                curr = 0;
            } else if (c == ')') {
                res += curr * sign;
                curr = 0;
                res *= stack.pop();
                res += stack.pop();
            }
        }

        res += sign * curr;
        return (int) res;
    }





}
