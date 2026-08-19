package LeetCodeJava.Stack;

// https://leetcode.com/problems/remove-outermost-parentheses/

/**
 *  1021. Remove Outermost Parentheses
 *  Easy
 *
 *  A valid parentheses string is either empty "", "(" + A + ")", or A + B, where
 *  A and B are valid parentheses strings, and + represents string concatenation.
 *
 *  A valid parentheses string s is primitive if it is nonempty, and there does
 *  not exist a way to split it into s = A + B, with A and B nonempty valid
 *  parentheses strings.
 *
 *  Given a valid parentheses string s, consider its primitive decomposition
 *  s = P1 + P2 + ... + Pk. Return s after removing the outermost parentheses of
 *  every primitive string in the primitive decomposition of s.
 *
 *  Example 1:
 *    Input: s = "(()())(())"
 *    Output: "()()()"
 *
 *  Example 2:
 *    Input: s = "(()())(())(()(()))"
 *    Output: "()()()()(())"
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s[i] is either '(' or ')'.
 *    s is a valid parentheses string.
 */
public class RemoveOutermostParentheses {

    // V0
    // IDEA: DEPTH COUNTER (a stack of size 1)
    //       the outermost '(' of a primitive is the one that takes depth 0 -> 1,
    //       and its matching ')' is the one that takes depth 1 -> 0, so SKIP
    //       exactly those two:
    //         '(' : keep it only if depth > 0, then depth += 1
    //         ')' : depth -= 1, then keep it only if depth > 0
    /**
     * time = O(N)
     * space = O(N)   // for the output
     */
    public String removeOuterParentheses(String s) {
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '(') {
                if (depth > 0) {
                    sb.append(ch);
                }
                depth++;
            } else {
                depth--;
                if (depth > 0) {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }
}
