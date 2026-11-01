package LeetCodeJava.String;

// https://leetcode.com/problems/delete-characters-to-make-fancy-string/description/

/**
 * 1957. Delete Characters to Make Fancy String
 * Easy
 *
 * A fancy string is a string where no three consecutive characters are equal.
 *
 * Given a string s, delete the minimum possible number of characters from s to make it fancy.
 *
 * Return the final string after the deletion. It can be shown that the answer will always be unique.
 *
 *
 * Example 1:
 *
 * Input: s = "leeetcode"
 * Output: "leetcode"
 * Explanation:
 * Remove an 'e' from the first group of 'e's to create "leetcode".
 * No three consecutive characters are equal, so return "leetcode".
 *
 * Example 2:
 *
 * Input: s = "aaabaaaa"
 * Output: "aabaa"
 * Explanation:
 * Remove an 'a' from the first group of 'a's to create "aabaaaa".
 * Remove two 'a's from the second group of 'a's to create "aabaa".
 * No three consecutive characters are equal, so return "aabaa".
 *
 * Example 3:
 *
 * Input: s = "aab"
 * Output: "aab"
 * Explanation: No three consecutive characters are equal, so return "aab".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists only of lowercase English letters.
 *
 */
public class DeleteCharactersToMakeFancyString {

    // V0
    // IDEA: GREEDY (KEEP AT MOST 2 OF EVERY RUN)
    //       append each char unless the last two KEPT chars already equal it;
    //       comparing against the answer (not s) is what makes the greedy right.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String makeFancyString(String s) {
        StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray()) {
            int length = result.length();
            boolean makesThreeInARow = length >= 2
                    && result.charAt(length - 1) == c
                    && result.charAt(length - 2) == c;
            if (!makesThreeInARow) {
                result.append(c);
            }
        }
        return result.toString();
    }
}
