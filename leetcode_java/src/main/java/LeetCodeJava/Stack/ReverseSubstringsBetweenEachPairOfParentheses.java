package LeetCodeJava.Stack;

// https://leetcode.com/problems/reverse-substrings-between-each-pair-of-parentheses/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1190. Reverse Substrings Between Each Pair of Parentheses
 *  Medium
 *
 *  You are given a string s that consists of lower case English letters and
 *  brackets.
 *
 *  Reverse the strings in each pair of matching parentheses, starting from the
 *  innermost one.
 *
 *  Your result should not contain any brackets.
 *
 *  Example 1:
 *    Input: s = "(abcd)"
 *    Output: "dcba"
 *
 *  Example 2:
 *    Input: s = "(u(love)i)"
 *    Output: "iloveu"
 *    Explanation: the substring "love" is reversed first, then the whole string.
 *
 *  Example 3:
 *    Input: s = "(ed(et(oc))el)"
 *    Output: "leetcode"
 *
 *  Constraints:
 *    1 <= s.length <= 2000
 *    s only contains lower case English characters and parentheses.
 *    It is guaranteed that all parentheses are balanced.
 */
public class ReverseSubstringsBetweenEachPairOfParentheses {

    // V0
    // IDEA: STACK of BUFFERS (simulation)
    //       on '(' -> push a new buffer
    //       on ')' -> pop the innermost buffer, REVERSE it, and merge it back
    //                 into its parent buffer
    //       the bottom buffer holds the answer.
    /**
     * time = O(N^2)   // each ')' reverses its buffer
     * space = O(N)
     */
    public String reverseParentheses(String s) {
        Deque<StringBuilder> stack = new ArrayDeque<>();
        stack.push(new StringBuilder());   // the "outermost" buffer

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                stack.push(new StringBuilder());
            } else if (ch == ')') {
                StringBuilder top = stack.pop();
                stack.peek().append(top.reverse());
            } else {
                stack.peek().append(ch);
            }
        }
        return stack.pop().toString();
    }
}
