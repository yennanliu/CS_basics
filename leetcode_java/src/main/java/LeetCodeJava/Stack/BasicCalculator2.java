package LeetCodeJava.Stack;

// https://leetcode.com/problems/basic-calculator-ii/description/

import java.util.Stack;

/**
 * 227. Basic Calculator II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a string s which represents an expression, evaluate this expression and return its value.
 *
 * The integer division should truncate toward zero.
 *
 * You may assume that the given expression is always valid. All intermediate results will be in the range of [-231, 231 - 1].
 *
 * Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().
 *
 *
 *
 * Example 1:
 *
 * Input: s = "3+2*2"
 * Output: 7
 * Example 2:
 *
 * Input: s = " 3/2 "
 * Output: 1
 * Example 3:
 *
 * Input: s = " 3+5 / 2 "
 * Output: 5
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 3 * 105
 * s consists of integers and operators ('+', '-', '*', '/') separated by some number of spaces.
 * s represents a valid expression.
 * All the integers in the expression are non-negative integers in the range [0, 231 - 1].
 * The answer is guaranteed to fit in a 32-bit integer.
 *
 *
 *
 *
 */
public class BasicCalculator2 {

    // V0
    // IDEA: STACK + STRING OP (fixed by gpt)
    public int calculate(String s) {
        if (s == null || s.length() == 0)
            return 0;

        Stack<Integer> stack = new Stack<>();
        int num = 0;
        char sign = '+'; // previous operator

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);

            if (Character.isDigit(ch)) {
                num = num * 10 + (ch - '0');
            }

            // if operator OR last char
            if ((!Character.isDigit(ch) && ch != ' ') || i == s.length() - 1) {

                if (sign == '+') {
                    stack.push(num);
                } else if (sign == '-') {
                    stack.push(-num);
                } else if (sign == '*') {
                    stack.push(stack.pop() * num);
                } else if (sign == '/') {
                    stack.push(stack.pop() / num);
                }

                sign = ch;
                num = 0;
            }
        }

        int res = 0;
        for (int val : stack) {
            res += val;
        }

        return res;
    }

    // V0-1
    // IDEA: STACK + STRING OP (fixed by GEMINI)
    public int calculate_0_1(String s) {
        if (s == null || s.isEmpty())
            return 0;

        Stack<Integer> stack = new Stack<>();
        int currentNumber = 0;
        char operation = '+'; // Track the LAST operator seen
        int n = s.length();

        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);

            // 1. Build the multi-digit number
            if (Character.isDigit(ch)) {
                currentNumber = (currentNumber * 10) + (ch - '0');
            }

            // 2. If it's an operator OR the last character of the string
            if (!Character.isDigit(ch) && ch != ' ' || i == n - 1) {
                if (operation == '+') {
                    stack.push(currentNumber);
                } else if (operation == '-') {
                    stack.push(-currentNumber);
                } else if (operation == '*') {
                    // Precedence: Solve immediate and push result
                    stack.push(stack.pop() * currentNumber);
                } else if (operation == '/') {
                    stack.push(stack.pop() / currentNumber);
                }

                // Update to the new operator and reset current number
                operation = ch;
                currentNumber = 0;
            }
        }

        // 3. The stack now contains only values to be summed
        int result = 0;
        for (int val : stack) {
            result += val;
        }

        return result;
    }


    // V1-1
    // IDEA: STACK
    // https://leetcode.com/problems/basic-calculator-ii/editorial/
    public int calculate_1_1(String s) {

        if (s == null || s.isEmpty())
            return 0;
        int len = s.length();
        Stack<Integer> stack = new Stack<Integer>();
        int currentNumber = 0;
        char operation = '+';
        for (int i = 0; i < len; i++) {
            char currentChar = s.charAt(i);
            if (Character.isDigit(currentChar)) {
                currentNumber = (currentNumber * 10) + (currentChar - '0');
            }
            if (!Character.isDigit(currentChar) && !Character.isWhitespace(currentChar) || i == len - 1) {
                if (operation == '-') {
                    stack.push(-currentNumber);
                } else if (operation == '+') {
                    stack.push(currentNumber);
                } else if (operation == '*') {
                    stack.push(stack.pop() * currentNumber);
                } else if (operation == '/') {
                    stack.push(stack.pop() / currentNumber);
                }
                operation = currentChar;
                currentNumber = 0;
            }
        }
        int result = 0;
        while (!stack.isEmpty()) {
            result += stack.pop();
        }
        return result;
    }


    // V1-2
    // IDEA: Optimised Approach without the stack
    // https://leetcode.com/problems/basic-calculator-ii/editorial/
    public int calculate_1_2(String s) {
        if (s == null || s.isEmpty())
            return 0;
        int length = s.length();
        int currentNumber = 0, lastNumber = 0, result = 0;
        char operation = '+';
        for (int i = 0; i < length; i++) {
            char currentChar = s.charAt(i);
            if (Character.isDigit(currentChar)) {
                currentNumber = (currentNumber * 10) + (currentChar - '0');
            }
            if (!Character.isDigit(currentChar) && !Character.isWhitespace(currentChar) || i == length - 1) {
                if (operation == '+' || operation == '-') {
                    result += lastNumber;
                    lastNumber = (operation == '+') ? currentNumber : -currentNumber;
                } else if (operation == '*') {
                    lastNumber = lastNumber * currentNumber;
                } else if (operation == '/') {
                    lastNumber = lastNumber / currentNumber;
                }
                operation = currentChar;
                currentNumber = 0;
            }
        }
        result += lastNumber;
        return result;
    }


    // V2




}
