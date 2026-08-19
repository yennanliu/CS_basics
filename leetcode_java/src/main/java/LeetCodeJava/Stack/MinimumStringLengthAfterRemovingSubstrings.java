package LeetCodeJava.Stack;

// https://leetcode.com/problems/minimum-string-length-after-removing-substrings/

/**
 *  2696. Minimum String Length After Removing Substrings
 *  Easy
 *
 *  You are given a string s consisting only of uppercase English letters.
 *
 *  You can apply some operations to this string where, in one operation, you can
 *  remove any occurrence of one of the substrings "AB" or "CD" from s.
 *
 *  Return the minimum possible length of the resulting string that you can obtain.
 *
 *  Note that the string concatenates after removing the substring and could
 *  produce new "AB" or "CD" substrings.
 *
 *  Example 1:
 *    Input: s = "ABFCACDB"
 *    Output: 2
 *    Explanation: "ABFCACDB" -> "FCACDB" -> "FCAB" -> "FC", so length 2.
 *
 *  Example 2:
 *    Input: s = "ACBBD"
 *    Output: 5
 *    Explanation: We cannot do any operations, so the length remains the same.
 *
 *  Constraints:
 *    1 <= s.length <= 100
 *    s consists only of uppercase English letters.
 */
public class MinimumStringLengthAfterRemovingSubstrings {

    // V0
    // IDEA: STACK (adjacent-pair cancellation, same shape as "valid parentheses")
    //       scan left to right; before pushing c, if the stack top together with
    //       c forms "AB" or "CD", pop instead of pushing. that cancels the pair
    //       and automatically exposes the newly adjacent chars, which is exactly
    //       the "string concatenates after removing" rule.
    //       NOTE: removal order does not matter (the rewriting is confluent),
    //             so this single greedy pass reaches the minimum length.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minLength(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        // char stack via StringBuilder
        StringBuilder stack = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int top = stack.length() - 1;
            if (top >= 0 && ((c == 'B' && stack.charAt(top) == 'A')
                    || (c == 'D' && stack.charAt(top) == 'C'))) {
                stack.deleteCharAt(top);
            } else {
                stack.append(c);
            }
        }
        return stack.length();
    }
}
