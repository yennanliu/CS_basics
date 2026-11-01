package LeetCodeJava.Greedy;

// https://leetcode.com/problems/lexicographically-smallest-string-after-substring-operation/description/

/**
 * 2734. Lexicographically Smallest String After Substring Operation
 * Medium
 *
 * Given a string s consisting of lowercase English letters. Perform the following operation:
 *
 * Select any non-empty substring then replace every letter of the substring with the preceding letter of the English alphabet. For example, 'b' is converted to 'a', and 'a' is converted to 'z'.
 *
 * Return the lexicographically smallest string after performing the operation.
 *
 *
 * Example 1:
 *
 * Input: s = "cbabc"
 * Output: "baabc"
 * Explanation:
 * Perform the operation on the substring starting at index 0, and ending at index 1 inclusive.
 *
 * Example 2:
 *
 * Input: s = "aa"
 * Output: "az"
 * Explanation:
 * Perform the operation on the last letter.
 *
 * Example 3:
 *
 * Input: s = "acbbc"
 * Output: "abaab"
 * Explanation:
 * Perform the operation on the substring starting at index 1, and ending at index 4 inclusive.
 *
 * Example 4:
 *
 * Input: s = "leetcode"
 * Output: "kddsbncd"
 * Explanation:
 * Perform the operation on the entire string.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 3 * 10^5
 * s consists of lowercase English letters
 *
 */
public class LexicographicallySmallestStringAfterSubstringOperation {

    // V0
    // IDEA: GREEDY — DECREMENT THE FIRST RUN OF NON-'a' LETTERS
    //       decrementing helps every letter except 'a' (which wraps to 'z'). the earliest change
    //       decides the order, so skip leading 'a's, then decrement until the next 'a'.
    //       all 'a': one op is mandatory, so turn the LAST char into 'z'.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String smallestString(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;

        // skip the leading 'a's — touching one would turn it into 'z'
        int start = 0;
        while (start < n && chars[start] == 'a') {
            start++;
        }

        if (start == n) {
            // every letter is 'a': sacrifice the last position, where it costs the least
            chars[n - 1] = 'z';
            return new String(chars);
        }

        // decrement the whole run of non-'a' letters, stopping before the next 'a'
        int end = start;
        while (end < n && chars[end] != 'a') {
            chars[end] = (char) (chars[end] - 1);
            end++;
        }
        return new String(chars);
    }
}
