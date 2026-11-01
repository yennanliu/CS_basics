package LeetCodeJava.String;

// https://leetcode.com/problems/remove-all-occurrences-of-a-substring/description/

/**
 * 1910. Remove All Occurrences of a Substring
 * Medium
 *
 * Given two strings s and part, perform the following operation on s until all occurrences of the substring part are removed:
 *
 * Find the leftmost occurrence of the substring part and remove it from s.
 *
 * Return s after removing all occurrences of part.
 *
 * A substring is a contiguous sequence of characters in a string.
 *
 *
 * Example 1:
 *
 * Input: s = "daabcbaabcbc", part = "abc"
 * Output: "dab"
 * Explanation: The following operations are done:
 * - s = "daabcbaabcbc", remove "abc" starting at index 2, so s = "dabaabcbc".
 * - s = "dabaabcbc", remove "abc" starting at index 4, so s = "dababc".
 * - s = "dababc", remove "abc" starting at index 3, so s = "dab".
 * Now s has no occurrences of "abc".
 *
 * Example 2:
 *
 * Input: s = "axxxxyyyyb", part = "xy"
 * Output: "ab"
 * Explanation: The following operations are done:
 * - s = "axxxxyyyyb", remove "xy" starting at index 4 so s = "axxxyyyb".
 * - s = "axxxyyyb", remove "xy" starting at index 3 so s = "axxyyb".
 * - s = "axxyyb", remove "xy" starting at index 2 so s = "axyb".
 * - s = "axyb", remove "xy" starting at index 1 so s = "ab".
 * Now s has no occurrences of "xy".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * 1 <= part.length <= 1000
 * s and part consists of lowercase English letters.
 *
 */
public class RemoveAllOccurrencesOfASubstring {

    // V0
    // IDEA: STACK (A StringBuilder) -- POP THE TAIL WHENEVER IT JUST BECAME part
    //       push chars of s; after each push, if the last m chars equal part,
    //       delete them. A match can only be completed by the char just pushed,
    //       and a deletion may expose an earlier match that the tail already
    //       holds, so this reproduces "always remove the leftmost occurrence".
    /**
     * time = O(N * M)
     * space = O(N)
     */
    public String removeOccurrences(String s, String part) {
        int m = part.length();
        StringBuilder stack = new StringBuilder();
        for (char c : s.toCharArray()) {
            stack.append(c);
            int length = stack.length();
            if (length >= m && endsWithPart(stack, part)) {
                stack.setLength(length - m);
            }
        }
        return stack.toString();
    }

    /** True if the last part.length() chars of sb equal part. */
    private boolean endsWithPart(StringBuilder sb, String part) {
        int offset = sb.length() - part.length();
        for (int i = 0; i < part.length(); i++) {
            if (sb.charAt(offset + i) != part.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
