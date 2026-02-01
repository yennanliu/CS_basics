package LeetCodeJava.Stack;

// https://leetcode.com/problems/basic-calculator-iii/
// https://leetcode.ca/all/772.html

import java.util.Stack;

/**
 * 772. Basic Calculator III
 * Implement a basic calculator to evaluate a simple expression string.
 *
 * The expression string may contain open ( and closing parentheses ), the plus + or minus sign -, non-negative integers and empty spaces .
 *
 * The expression string contains only non-negative integers, +, -, *, / operators , open ( and closing parentheses ) and empty spaces . The integer division should truncate toward zero.
 *
 * You may assume that the given expression is always valid. All intermediate results will be in the range of [-2147483648, 2147483647].
 *
 * Some examples:
 *
 * "1 + 1" = 2
 * " 6-4 / 2 " = 4
 * "2*(5+5*2)/3+(6/2+8)" = 21
 * "(2+6* 3+5- (3*14/7+2)*5)+3"=-12
 *
 *
 * Note: Do not use the eval built-in library function.
 *
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon Apple DoorDash Facebook Google Houzz Hulu Jingchi Microsoft Pinterest Pocket Gems Salesforce Snapchat Uber
 *
 */
public class BasicCalculatorIII {

    // V0
//    public int calculate(String s) {
//
//    }

    // V1-1
    // https://leetcode.ca/2018-01-10-772-Basic-Calculator-III/
    // http://buttercola.blogspot.com/2019/03/leetcode-772-basic-calculator-iii.html
    /**
     * time = O(1)
     * space = O(1)
     */
    public int calculate_1_1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        // remove leading and trailing spaces and white spaces.
        //
        s = s.trim().replaceAll("[ ]+", "");

        if (s == null || s.length() == 0) {
            return 0;
        }

        Stack<Character> opStack = new Stack<>();
        Stack<Integer> numStack = new Stack<>();

        int i = 0;
        while (i < s.length()) {
            if (Character.isDigit(s.charAt(i))) {
                int num = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + Character.getNumericValue(s.charAt(i));
                    i++;
                }
                numStack.push(num);
            } else {
                char op = s.charAt(i);
                if (opStack.isEmpty()) {
                    opStack.push(op);
                    i++;
                } else if (op == '+' || op == '-') {
                    char top = opStack.peek();
                    if (top == '(') {
                        opStack.push(op);
                        i++;
                    } else {
                        calculate(numStack, opStack);
                    }
                } else if (op == '*' || op == '/') {
                    char top = opStack.peek();
                    if (top == '(') {
                        opStack.push(op);
                        i++;
                    } else if (top == '*' || top == '/') {
                        calculate(numStack, opStack);
                    } else if (top == '+' || top == '-') {
                        opStack.push(op);
                        i++;
                    }
                } else if (op == '(') {
                    opStack.push(op);
                    i++;
                } else if (op == ')') {
                    while (opStack.peek() != '(') {
                        calculate(numStack, opStack);
                    }
                    opStack.pop();
                    i++;
                }
            }
        }

        while (!opStack.isEmpty()) {
            calculate(numStack, opStack);
        }

        return numStack.peek();
    }

    private void calculate(Stack<Integer> numStack, Stack<Character> opStack) {
        int num2 = numStack.pop();
        int num1 = numStack.pop();

        char op = opStack.pop();

        int ans = 0;

        switch(op) {
            case '+':
                ans = num1 + num2;
                break;
            case '-':
                ans = num1 - num2;
                break;
            case '*':
                ans = num1 * num2;
                break;
            case '/':
                ans = num1 / num2;
                break;
        }

        numStack.push(ans);
    }

    // V2

}
