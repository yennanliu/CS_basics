package LeetCodeJava.String;

// https://leetcode.com/problems/to-lower-case/description/
/**
 * 709. To Lower Case
 * Easy
 *
 * Given a string s, return the string after replacing every uppercase letter
 * with the same lowercase letter.
 *
 *
 * Example 1:
 *
 * Input: s = "Hello"
 * Output: "hello"
 *
 * Example 2:
 *
 * Input: s = "here"
 * Output: "here"
 *
 * Example 3:
 *
 * Input: s = "LOVELY"
 * Output: "lovely"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists of printable ASCII characters.
 *
 */
public class ToLowerCase {

    // V0
    // IDEA: ASCII BIT TRICK
    /**
     *   For ASCII letters, lowercase = uppercase | 32 ('A' = 65, 'a' = 97).
     *   Every other printable character is left UNTOUCHED.
     *
     *   NOTE !!! the `A <= c <= Z` guard matters -- OR-ing 32 into a non-letter
     *            (e.g. '@' -> '`') would corrupt it.
     *
     *   time  = O(n)
     *   space = O(n)
     */
    public String toLowerCase(String s) {
        char[] res = s.toCharArray();
        for (int i = 0; i < res.length; i++) {
            if (res[i] >= 'A' && res[i] <= 'Z') {
                res[i] = (char) (res[i] | 32);
            }
        }
        return new String(res);
    }

    // V0-1
    // IDEA: BUILT IN
    /**
     *  time  = O(n)
     *  space = O(n)
     */
    public String toLowerCase_0_1(String s) {
        return s.toLowerCase();
    }

}
