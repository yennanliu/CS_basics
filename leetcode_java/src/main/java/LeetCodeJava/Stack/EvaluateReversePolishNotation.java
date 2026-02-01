package LeetCodeJava.Stack;

// https://leetcode.com/problems/evaluate-reverse-polish-notation/
/**
 * 150. Evaluate Reverse Polish Notation
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an array of strings tokens that represents an arithmetic expression in a Reverse Polish Notation.
 *
 * Evaluate the expression. Return an integer that represents the value of the expression.
 *
 * Note that:
 *
 * The valid operators are '+', '-', '*', and '/'.
 * Each operand may be an integer or another expression.
 * The division between two integers always truncates toward zero.
 * There will not be any division by zero.
 * The input represents a valid arithmetic expression in a reverse polish notation.
 * The answer and all the intermediate calculations can be represented in a 32-bit integer.
 *
 *
 * Example 1:
 *
 * Input: tokens = ["2","1","+","3","*"]
 * Output: 9
 * Explanation: ((2 + 1) * 3) = 9
 * Example 2:
 *
 * Input: tokens = ["4","13","5","/","+"]
 * Output: 6
 * Explanation: (4 + (13 / 5)) = 6
 * Example 3:
 *
 * Input: tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
 * Output: 22
 * Explanation: ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
 * = ((10 * (6 / (12 * -11))) + 17) + 5
 * = ((10 * (6 / -132)) + 17) + 5
 * = ((10 * 0) + 17) + 5
 * = (0 + 17) + 5
 * = 17 + 5
 * = 22
 *
 *
 * Constraints:
 *
 * 1 <= tokens.length <= 104
 * tokens[i] is either an operator: "+", "-", "*", or "/", or an integer in the range [-200, 200].
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class EvaluateReversePolishNotation {

    // V0 : IDEA : STACK
    /**
     * time = O(1)
     * space = O(1)
     */
    public int evalRPN(String[] tokens) {

        if (tokens.length == 0){
            return 0;
        }
        int ans = 0;
        /**
         * NOTE !!!
         *
         *  should use `Stack`, instead of `Queue`
         */
        Stack<Integer> st1 = new Stack<>();
        Stack<String> st2 = new Stack<>();

        String operators = "+-*/";

        for (String x : tokens){
            //System.out.println("x = " + x + ", st1 = " + st1 + ", st2 = " + st2);
            // case 1) input is operator (in "+-*/"), so pop and do calculation
            if (operators.contains(x)){
                st2.add(x);
                String op = st2.pop();
                Integer i1 = st1.pop();
                Integer i2 = st1.pop();
                Integer res = this.calculate(i1, i2, op);
                //System.out.println("--> res = " + res);
                st1.add(res);
            }
            // case 2) input is "number", so just add it to the stack, for following operation
            else{
                st1.add(Integer.parseInt(x));
            }
        }

        // do final calculation at once
        while (!st1.empty()){
            ans += st1.pop();
        }

        return ans;
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public Integer calculate(Integer i1, Integer i2, String op){
        if (op.equals("+")){
            return i1 + i2;
        }
        if(op.equals("-")){
            return  i2 - i1;
        }
        if(op.equals( "*")){
            return i1 * i2;
        }else{
            if (i1 == 0){
                return 0;
            }
        }
        return  i2 / i1;
    }

    // V0-1
    // IDEA: STACK (fixed by gpt)
    /**
     * time = O(1)
     * space = O(1)
     */
    public int evalRPN_0_1(String[] tokens) {
        if (tokens == null || tokens.length == 0) {
            return 0;
        }

        Stack<Integer> stack = new Stack<>();

        for (String t : tokens) {
            if (isNumber(t)) {
                stack.push(Integer.parseInt(t));
            } else {
                int b = stack.pop(); // Last inserted number
                int a = stack.pop(); // Second last inserted number
                stack.push(calculate(a, b, t)); // Compute and push result
            }
        }

        return stack.pop();
    }

    private boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int calculate(int a, int b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    // V1
    // IDEA : Reducing the List In-place
    // https://leetcode.com/problems/evaluate-reverse-polish-notation/editorial/
    /**
     * time = O(1)
     * space = O(1)
     */
    public int evalRPN_2(String[] tokens) {

        int currentPosition = 0;
        int length = tokens.length; // We will need to keep track of this ourselves.

        while (length > 1) {

            // Move the position pointer to the next operator token.
            while (!"+-*/".contains(tokens[currentPosition])) {
                currentPosition++;
            }

            // Extract the numbers.
            int number1 = Integer.parseInt(tokens[currentPosition - 2]);
            int number2 = Integer.parseInt(tokens[currentPosition  - 1]);

            // Calculate the result to overwrite the operator with.
            int newValue = 0;
            switch (tokens[currentPosition]) {
                case "+":
                    newValue = number1 + number2;
                    break;
                case "-":
                    newValue = number1 - number2;
                    break;
                case "*":
                    newValue = number1 * number2;
                    break;
                case "/":
                    newValue = number1 / number2;
                    break;
            }
            tokens[currentPosition] = Integer.toString(newValue);

            // Delete numbers and point pointers correctly.
            delete2AtIndex(tokens, currentPosition - 2, length);
            currentPosition--;
            length -= 2;
        }

        return Integer.parseInt(tokens[0]);
    }

    private void delete2AtIndex(String[] tokens, int d, int length) {
        for (int i = d; i < length - 2; i++) {
            tokens[i] = tokens[i + 2];
        }
    }

    // V2
    // IDEA : Evaluate with Stack
    // https://leetcode.com/problems/evaluate-reverse-polish-notation/editorial/
    /**
     * time = O(1)
     * space = O(1)
     */
    public int evalRPN_3(String[] tokens) {

        Stack<Integer> stack = new Stack<>();

        for (String token : tokens) {

            if (!"+-*/".contains(token)) {
                stack.push(Integer.valueOf(token));
                continue;
            }

            int number2 = stack.pop();
            int number1 = stack.pop();

            int result = 0;

            switch (token) {
                case "+":
                    result = number1 + number2;
                    break;
                case "-":
                    result = number1 - number2;
                    break;
                case "*":
                    result = number1 * number2;
                    break;
                case "/":
                    result = number1 / number2;
                    break;
            }

            stack.push(result);

        }

        return stack.pop();
    }

}
