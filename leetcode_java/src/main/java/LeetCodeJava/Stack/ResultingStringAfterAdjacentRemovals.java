package LeetCodeJava.Stack;

// https://leetcode.com/problems/resulting-string-after-adjacent-removals/

/**
 *  3561. Resulting String After Adjacent Removals
 *  Medium
 *
 *  You are given a string s consisting of lowercase English letters.
 *
 *  You must repeatedly perform the following operation while the string s has at
 *  least two consecutive characters:
 *    - Remove the leftmost pair of adjacent characters in the string that are
 *      consecutive in the alphabet, in either order (e.g. 'a' and 'b', or 'b'
 *      and 'a').
 *    - Shift the remaining characters to the left to fill the gap.
 *
 *  Return the resulting string after no more operations can be performed.
 *
 *  Note: Consider the alphabet as circular, thus 'a' and 'z' are consecutive.
 *
 *  Example 1:
 *    Input: s = "abc"
 *    Output: "c"
 *    Explanation: remove "ab", leaving "c".
 *
 *  Example 2:
 *    Input: s = "adcb"
 *    Output: ""
 *    Explanation: remove "dc" -> "ab", then remove "ab" -> "".
 *
 *  Example 3:
 *    Input: s = "zadb"
 *    Output: "db"
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s consists only of lowercase English letters.
 */
public class ResultingStringAfterAdjacentRemovals {

    // V0
    // IDEA: STACK - REPLAY THE REQUIRED LEFTMOST REMOVALS IN ONE PASS
    //       the reduction is NOT order independent, so the "leftmost first" rule
    //       has to be honoured rather than argued away ("abc" -> "c" if "ab" goes
    //       first, but -> "a" if "bc" does).
    //       a left-to-right stack pass reproduces exactly that order: the stack
    //       always holds the fully reduced form of the prefix read so far, so
    //       nothing inside it can still cancel and its top is the only character
    //       a new one could meet. when the next character is alphabet-adjacent
    //       (mod 26) to that top, that pair IS the leftmost removable pair, so
    //       cancelling it is forced; popping it exposes an older character that
    //       may in turn cancel with a later one.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String resultingString(String s) {
        StringBuilder stack = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int top = stack.length() - 1;
            if (top >= 0) {
                int d = Math.abs(stack.charAt(top) - ch);
                if (d == 1 || d == 25) {
                    stack.deleteCharAt(top);
                    continue;
                }
            }
            stack.append(ch);
        }
        return stack.toString();
    }
}
