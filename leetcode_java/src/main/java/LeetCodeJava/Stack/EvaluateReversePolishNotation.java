package LeetCodeJava.Stack;

// https://leetcode.com/problems/evaluate-reverse-polish-notation/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class EvaluateReversePolishNotation {

    // V0 : IDEA : STACK
    public int evalRPN(String[] tokens) {

        if (tokens.length == 0){
            return 0;
        }
        int ans = 0;
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

    // V1
    // IDEA : Reducing the List In-place
    // https://leetcode.com/problems/evaluate-reverse-polish-notation/editorial/
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
