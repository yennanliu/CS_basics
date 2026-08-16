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


    // V1
    // IDEA: CHARACTER ARITHMETIC (+ 32 instead of | 32)
    /**
     *  'a' - 'A' is exactly 32, so adding the gap works as well as OR-ing the bit.
     *  Spelling it as a DIFFERENCE rather than a bit trick makes the intent obvious
     *  and survives a hypothetical non-ASCII encoding where the bit trick would not.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public String toLowerCase_1(String s) {
        char[] res = s.toCharArray();
        for (int i = 0; i < res.length; i++) {
            if (res[i] >= 'A' && res[i] <= 'Z') {
                res[i] = (char) (res[i] + ('a' - 'A'));
            }
        }
        return new String(res);
    }

    // V2
    // IDEA: PRECOMPUTED 128-ENTRY LOOKUP TABLE
    /**
     *  Build the ASCII mapping once in a static table, then the conversion is a
     *  single array index per character -- no branch at all.
     *
     *  The shape you would use if this ran in a hot loop: branch-free and trivially
     *  vectorisable.
     *
     *  time  = O(n)
     *  space = O(1) (a fixed 128-entry table)
     */
    private static final char[] LOWER_TABLE = buildLowerTable();

    private static char[] buildLowerTable() {
        char[] t = new char[128];
        for (int c = 0; c < 128; c++) {
            t[c] = (c >= 'A' && c <= 'Z') ? (char) (c + 32) : (char) c;
        }
        return t;
    }

    public String toLowerCase_2(String s) {
        char[] res = s.toCharArray();
        for (int i = 0; i < res.length; i++) {
            if (res[i] < 128) {
                res[i] = LOWER_TABLE[res[i]];
            }
        }
        return new String(res);
    }

    // V3
    // IDEA: STREAM / FUNCTIONAL MAPPING
    /**
     *  Map the code points through a lambda and collect.
     *
     *  The most declarative rendering -- no index, no mutable buffer -- at the cost
     *  of boxing every character. Kept to show the idiomatic-Java end of the range.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public String toLowerCase_3(String s) {
        StringBuilder sb = new StringBuilder();
        s.chars()
         .map(c -> (c >= 'A' && c <= 'Z') ? c + 32 : c)
         .forEach(c -> sb.append((char) c));
        return sb.toString();
    }

}
