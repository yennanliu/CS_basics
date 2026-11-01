package LeetCodeJava.String;

// https://leetcode.com/problems/existence-of-a-substring-in-a-string-and-its-reverse/description/

/**
 * 3083. Existence of a Substring in a String and Its Reverse
 * Easy
 *
 * Given a string s, find any substring of length 2 which is also present in the reverse of s.
 *
 * Return true if such a substring exists, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: s = "leetcode"
 * Output: true
 * Explanation: Substring "ee" is of length 2 which is also present in reverse(s) == "edocteel".
 *
 * Example 2:
 *
 * Input: s = "abcba"
 * Output: true
 * Explanation: All of the substrings of length 2 "ab", "bc", "cb", "ba" are also present in reverse(s) == "abcba".
 *
 * Example 3:
 *
 * Input: s = "abcd"
 * Output: false
 * Explanation: There is no substring of length 2 in s, which is also present in the reverse of s.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists only of lowercase English letters.
 *
 */
public class ExistenceOfASubstringInAStringAndItsReverse {

    // V0
    // IDEA: A LENGTH-2 SUBSTRING "xy" IS IN reverse(s) IFF "yx" IS IN s
    //       record every adjacent pair in a 26 x 26 table, then look for a
    //       pair whose flipped version was also recorded
    /**
     * time = O(N)
     * space = O(1)   the 26 x 26 table is fixed size
     */
    public boolean isSubstringPresent(String s) {
        // seen[x][y] == true  <=>  "xy" occurs in s
        boolean[][] seen = new boolean[26][26];
        for (int i = 0; i + 1 < s.length(); i++) {
            seen[s.charAt(i) - 'a'][s.charAt(i + 1) - 'a'] = true;
        }
        for (int i = 0; i + 1 < s.length(); i++) {
            int first = s.charAt(i) - 'a';
            int second = s.charAt(i + 1) - 'a';
            if (seen[second][first]) {
                return true;
            }
        }
        return false;
    }
}
