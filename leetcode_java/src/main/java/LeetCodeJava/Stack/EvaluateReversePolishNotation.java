package LeetCodeJava.Stack;

// https://leetcode.com/problems/evaluate-reverse-polish-notation/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class EvaluateReversePolishNotation {

    // V0 : IDEA : STACK
    // TODO: fix below
//    public int evalRPN(String[] tokens) {
//
//        if (tokens.length == 0 || tokens.equals(null)){
//            return 0;
//        }
//
//        Stack<String> stack = new Stack<>();
//        List<String> _signs =  Arrays.asList("+", "-", "*", "/");
//        for (int i = 0; i < tokens.length; i++){
//            String cur = tokens[i];
//            System.out.println("cur = " + cur);
//            stack.forEach(System.out::println);
//            // case 1 : digit
//            if (!_signs.contains(cur)){
//                stack.push(cur);
//            } // case 2 : "+", "-", "*", "/"
//            else{
//                if(stack.size() >= 2){
//                    int _first = Integer.parseInt(stack.pop());
//                    int _second = Integer.parseInt(stack.pop());
//                    System.out.println("_first = " + _first + " _second = " + _second);
//
//                    if (cur == "+"){
//                        int _res = _second + _first;
//                        stack.push(String.valueOf(_res));
//                    }else if (cur == "-"){
//                        int _res = _second - _first;
//                        stack.push(String.valueOf(_res));
//                    }else if (cur == "*"){
//                        int _res = _second * _first;
//                        stack.push(String.valueOf(_res));
//                    }else if (cur == "/"){
//                        int _res = _second / _first;
//                        stack.push(String.valueOf(_res));
//                    }
//                }
//            }
//        }
//
//        System.out.println("stack = " + stack.toArray());
//        int ans = Integer.parseInt(stack.peek());
//        while (!stack.isEmpty()){
//            System.out.println("--> " + stack.pop());
//        }
//        return ans;
//    }

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
