package LeetCodeJava.Stack;

// https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 1249. Minimum Remove to Make Valid Parentheses
 * Medium
 *
 * Given a string s of '(' , ')' and lowercase English characters.
 *
 * Your task is to remove the minimum number of parentheses ( '(' or ')', in any positions )
 * so that the resulting parentheses string is valid and return any valid string.
 *
 * Formally, a parentheses string is valid if and only if:
 *
 * It is the empty string, contains only lowercase characters, or
 * It can be written as AB (A concatenated with B), where A and B are valid strings, or
 * It can be written as (A), where A is a valid string.
 *
 *
 * Example 1:
 *
 * Input: s = "lee(t(c)o)de)"
 * Output: "lee(t(c)o)de"
 * Explanation: "lee(t(co)de)" , "lee(t(c)ode)" would also be accepted.
 *
 * Example 2:
 *
 * Input: s = "a)b(c)d"
 * Output: "ab(c)d"
 *
 * Example 3:
 *
 * Input: s = "))(("
 * Output: ""
 * Explanation: An empty string is also valid.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either '(' , ')', or lowercase English letter.
 *
 */
public class MinimumRemoveToMakeValidParentheses {

    // V0
    // IDEA: STACK OF '(' INDEXES (mark the unmatched positions, then rebuild)
    // a ')' with no open '(' on the stack can never be matched -> remove it.
    // every '(' still on the stack at the end has no ')' after it -> remove it too.
    // only parentheses that could not be matched are removed, so the removal is minimal.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String minRemoveToMakeValid(String s) {
        // edge case: nothing to fix
        if (s == null || s.isEmpty()) {
            return "";
        }
        int n = s.length();

        // indexes of '(' not matched yet
        Deque<Integer> openIndexes = new ArrayDeque<>();
        boolean[] remove = new boolean[n];

        for (int i = 0; i < n; i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                openIndexes.push(i);
            } else if (ch == ')') {
                if (!openIndexes.isEmpty()) {
                    // match this ')' with the latest unmatched '('
                    openIndexes.pop();
                } else {
                    // no '(' available to match this ')'
                    remove[i] = true;
                }
            }
            // letters never affect the balance
        }

        // any '(' left on the stack is unmatched
        while (!openIndexes.isEmpty()) {
            remove[openIndexes.pop()] = true;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (!remove[i]) {
                result.append(s.charAt(i));
            }
        }
        return result.toString();
    }

    // V1
    // IDEA: GREEDY, TWO PASSES WITH A COUNTER (no stack)
    // pass 1 (left -> right): drop every ')' that has no unmatched '(' before it
    // pass 2 (right -> left): drop every '(' that has no unmatched ')' after it
    // whatever survives both passes is balanced.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String minRemoveToMakeValid_1(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }

        // pass 1: remove unmatched ')'
        StringBuilder firstPass = new StringBuilder();
        int openCount = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == ')') {
                if (openCount == 0) {
                    continue;
                }
                openCount -= 1;
            } else if (ch == '(') {
                openCount += 1;
            }
            firstPass.append(ch);
        }

        // pass 2: remove unmatched '(' by scanning backward
        StringBuilder reversedResult = new StringBuilder();
        int closeCount = 0;
        for (int i = firstPass.length() - 1; i >= 0; i--) {
            char ch = firstPass.charAt(i);
            if (ch == '(') {
                if (closeCount == 0) {
                    continue;
                }
                closeCount -= 1;
            } else if (ch == ')') {
                closeCount += 1;
            }
            reversedResult.append(ch);
        }
        return reversedResult.reverse().toString();
    }
}
