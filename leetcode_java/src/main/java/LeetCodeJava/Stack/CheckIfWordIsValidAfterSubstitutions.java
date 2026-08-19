package LeetCodeJava.Stack;

// https://leetcode.com/problems/check-if-word-is-valid-after-substitutions/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1003. Check If Word Is Valid After Substitutions
 *  Medium
 *
 *  Given a string s, determine if it is valid.
 *
 *  A string s is valid if, starting with an empty string t = "", you can
 *  transform t into s after performing the following operation any number of
 *  times: insert string "abc" into any position in t. More formally, t becomes
 *  tleft + "abc" + tright, where t == tleft + tright. Note that tleft and
 *  tright may be empty.
 *
 *  Return true if s is a valid string, otherwise, return false.
 *
 *  Example 1:
 *    Input: s = "aabcbc"
 *    Output: true
 *    Explanation: "" -> "abc" -> "aabcbc"
 *
 *  Example 2:
 *    Input: s = "abccba"
 *    Output: false
 *
 *  Constraints:
 *    1 <= s.length <= 2 * 10^4
 *    s consists of letters 'a', 'b', and 'c'
 */
public class CheckIfWordIsValidAfterSubstitutions {

    // V0
    // IDEA: STACK (reverse the process: keep REMOVING "abc")
    //       push chars on a stack; whenever we meet a 'c', the 2 chars right
    //       below must be 'a','b' -> pop them and drop the 'c'.
    //       valid <=> the stack is empty at the end.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char ch : s.toCharArray()) {
            if (ch == 'c') {
                if (stack.size() < 2) {
                    return false;
                }
                char b = stack.pop();
                char a = stack.pop();
                if (b != 'b' || a != 'a') {
                    return false;
                }
            } else {
                stack.push(ch);
            }
        }
        return stack.isEmpty();
    }
}
