package LeetCodeJava.Greedy;

// https://leetcode.com/problems/construct-the-longest-new-string/description/

/**
 * 2745. Construct the Longest New String
 * Medium
 *
 * You are given three integers x, y, and z.
 *
 * You have x strings equal to "AA", y strings equal to "BB", and z strings equal to "AB". You want to choose some (possibly all or none) of these strings and concatenate them in some order to form a new string. This new string must not contain "AAA" or "BBB" as a substring.
 *
 * Return the maximum possible length of the new string.
 *
 * A substring is a contiguous non-empty sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: x = 2, y = 5, z = 1
 * Output: 12
 * Explanation: We can concatenate the strings "BB", "AA", "BB", "AA", "BB", and "AB" in that order. Then, our new string is "BBAABBAABBAB".
 * That string has length 12, and we can show that it is impossible to construct a string of longer length.
 *
 * Example 2:
 *
 * Input: x = 3, y = 2, z = 2
 * Output: 14
 * Explanation: We can concatenate the strings "AB", "AB", "AA", "BB", "AA", "BB", and "AA" in that order. Then, our new string is "ABABAABBAABBAA".
 * That string has length 14, and we can show that it is impossible to construct a string of longer length.
 *
 *
 * Constraints:
 *
 * 1 <= x, y, z <= 50
 *
 */
public class ConstructTheLongestNewString {

    // V0
    // IDEA: GREEDY / CASE ANALYSIS ON THE "AA" vs "BB" ALTERNATION
    //       "AA" and "BB" can never touch themselves, so they must strictly alternate:
    //       AA BB AA BB ... -> if x == y all of them fit, otherwise min(x, y) of each plus one
    //       extra of the larger pile. every "AB" is always usable: a run of ABAB... can be
    //       put in front of a chain starting with AA (or after one ending with BB).
    //       every block is 2 characters long.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int longestString(int x, int y, int z) {
        int blocks;
        if (x == y) {
            blocks = x + y + z;
        } else {
            // the larger pile can only exceed the smaller by one in an alternation
            blocks = 2 * Math.min(x, y) + 1 + z;
        }
        return blocks * 2;
    }
}
