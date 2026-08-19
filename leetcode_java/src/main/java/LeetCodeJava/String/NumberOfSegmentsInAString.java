package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-segments-in-a-string/

/**
 *  434. Number of Segments in a String
 *  Easy
 *
 *  Given a string s, return the number of segments in the string.
 *
 *  A segment is defined to be a contiguous sequence of non-space characters.
 *
 *  Example 1:
 *    Input: s = "Hello, my name is John"   Output: 5
 *  Example 2:
 *    Input: s = "Hello"                    Output: 1
 *
 *  Constraints:
 *    0 <= s.length <= 300
 *    s consists of lowercase and uppercase English letters, digits,
 *    or one of the characters "!@#$%^&*()_+-=',.:".
 *    The only space character in s is ' '.
 */
public class NumberOfSegmentsInAString {

    // V0
    // IDEA: count positions that start a segment (non-space whose left neighbour is a space / start)
    /**
     * time = O(n)
     * space = O(1)
     */
    public int countSegments(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ' && (i == 0 || s.charAt(i - 1) == ' ')) {
                count++;
            }
        }
        return count;
    }
}
