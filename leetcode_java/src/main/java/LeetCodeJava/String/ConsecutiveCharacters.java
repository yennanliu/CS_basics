package LeetCodeJava.String;

// https://leetcode.com/problems/consecutive-characters/description/

/**
 * 1446. Consecutive Characters
 * Easy
 *
 * The power of the string is the maximum length of a non-empty substring that contains only one unique character.
 *
 * Given a string s, return the power of s.
 *
 *
 * Example 1:
 *
 * Input: s = "leetcode"
 * Output: 2
 * Explanation: The substring "ee" is of length 2 with the character 'e' only.
 *
 * Example 2:
 *
 * Input: s = "abbcccddddeeeeedcba"
 * Output: 5
 * Explanation: The substring "eeeee" is of length 5 with the character 'e' only.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of only lowercase English letters.
 *
 */
public class ConsecutiveCharacters {

    // V0
    // IDEA: ONE PASS RUN LENGTH (TRACK THE CURRENT RUN, KEEP THE BEST)
    //       s is non-empty, so the answer is at least 1.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxPower(String s) {
        int best = 1;
        int run = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                // same char as before: the run grows
                run++;
                best = Math.max(best, run);
            } else {
                run = 1;
            }
        }
        return best;
    }
}
