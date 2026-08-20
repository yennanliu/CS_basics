package LeetCodeJava.Stack;

// https://leetcode.com/problems/score-of-parentheses/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 856. Score of Parentheses
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a balanced parentheses string s, return the score of the string.
 *
 * The score of a balanced parentheses string is based on the following rule:
 *
 * "()" has score 1.
 * AB has score A + B, where A and B are balanced parentheses strings.
 * (A) has score 2 * A, where A is a balanced parentheses string.
 *
 *
 * Example 1:
 *
 * Input: s = "()"
 * Output: 1
 * Example 2:
 *
 * Input: s = "(())"
 * Output: 2
 * Example 3:
 *
 * Input: s = "()()"
 * Output: 2
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 50
 * s consists of only '(' and ')'.
 * s is a balanced parentheses string.
 *
 */
public class ScoreIOfParentheses {

    // V0
    // IDEA: STACK OF PARTIAL SCORES (one slot per nesting level)
    /**
     *  The stack holds the score accumulated SO FAR at each open nesting level,
     *  with the outermost (top level) score at the bottom.
     *
     *   - on '(' : we enter a new level -> push a fresh 0
     *   - on ')' : the level just closed. Its accumulated score is `inner`:
     *        * inner == 0 -> the level contained nothing, so it was a bare
     *                        "()" -> it is worth 1
     *        * inner  > 0 -> it was "(A)" -> it is worth 2 * inner
     *      and that value is added onto the parent level (which is the "AB
     *      scores A + B" rule, since siblings keep accumulating into the same
     *      parent slot).
     *
     *  So `Math.max(2 * inner, 1)` folds the "()" = 1 and "(A)" = 2*A rules
     *  into one line. Input is guaranteed balanced, so the bottom slot holds
     *  the answer at the end.
     *
     *  Trace on "(()(()))":
     *      (        -> [0, 0]
     *      (        -> [0, 0, 0]
     *      )        -> [0, 1]              inner 0 -> 1
     *      (        -> [0, 1, 0]
     *      (        -> [0, 1, 0, 0]
     *      )        -> [0, 1, 1]           inner 0 -> 1
     *      )        -> [0, 3]              inner 1 -> 2, added to the 1
     *      )        -> [6]                 inner 3 -> 6
     *
     *  time = O(N)
     *  space = O(N)  (stack depth = max nesting depth)
     */
    public int scoreOfParentheses(String s) {
        // edge
        if (s == null || s.isEmpty()) {
            return 0;
        }

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0); // score of the outermost (virtual) level

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(0);
            } else {
                int inner = stack.pop();
                // "()" -> 1, "(A)" -> 2 * A
                int val = Math.max(2 * inner, 1);
                // "AB" -> A + B : merge into the parent level
                stack.push(stack.pop() + val);
            }
        }

        return stack.pop();
    }

    // V0-1
    // IDEA: COUNT THE DEPTH OF EACH "()" CORE (O(1) space)
    /**
     *  Every point of score in the answer comes from some bare "()" pair, and a
     *  "()" sitting at depth d (i.e. wrapped by d other pairs) has each of those
     *  d wrappers doubling it -> it contributes 2^d.
     *
     *  So: walk the string tracking `depth`; whenever we see a ')' whose
     *  previous char is '(' (that is a bare "()" core), add 1 << depth, where
     *  depth is the number of pairs still open AROUND it (hence the `depth--`
     *  before the add).
     *
     *  time = O(N)
     *  space = O(1)
     */
    public int scoreOfParentheses_0_1(String s) {
        // edge
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int res = 0;
        int depth = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                depth++;
            } else {
                depth--;
                /** NOTE !!! only a bare "()" core scores directly */
                if (s.charAt(i - 1) == '(') {
                    res += 1 << depth;
                }
            }
        }

        return res;
    }

    // V1

    // V2
}
