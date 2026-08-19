package LeetCodeJava.Stack;

// https://leetcode.com/problems/ternary-expression-parser/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  439. Ternary Expression Parser
 *  Medium
 *
 *  Given a string expression representing arbitrarily nested ternary expressions,
 *  evaluate it and return the result of it.
 *
 *  You can always assume that the given expression is valid and only contains digits,
 *  '?', ':', 'T', and 'F' where 'T' is true and 'F' is false. All the numbers in the
 *  expression are one-digit numbers (i.e. in the range [0, 9]).
 *
 *  The conditional expressions group right-to-left (as usual in most languages), and
 *  the result of the expression will always evaluate to either a digit, 'T' or 'F'.
 *
 *  Example 1:
 *  Input: expression = "T?2:3"
 *  Output: "2"
 *
 *  Example 2:
 *  Input: expression = "F?1:T?4:5"
 *  Output: "4"
 *
 *  Example 3:
 *  Input: expression = "T?T?F:5:3"
 *  Output: "F"
 *
 *  Constraints:
 *  5 <= expression.length <= 10^4
 */
public class TernaryExpressionParser {

    // V0
    // IDEA: RIGHT-TO-LEFT STACK — scanning backwards, whenever the stack top is '?' the
    //       char we just read is the condition, so we can resolve that ternary immediately
    /**
     * time = O(n)
     * space = O(n)
     */
    public String parseTernary(String expression) {

        if (expression == null || expression.isEmpty()) {
            return "";
        }

        Deque<Character> stack = new ArrayDeque<>();
        for (int i = expression.length() - 1; i >= 0; i--) {
            char c = expression.charAt(i);
            if (!stack.isEmpty() && stack.peek() == '?') {
                stack.pop();                 // drop '?'
                char first = stack.pop();    // value taken when the condition is true
                stack.pop();                 // drop ':'
                char second = stack.pop();   // value taken when the condition is false
                stack.push(c == 'T' ? first : second);
            } else {
                stack.push(c);
            }
        }
        return String.valueOf(stack.peek());
    }
}
