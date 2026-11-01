package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/number-of-substrings-containing-all-three-characters/description/

/**
 * 1358. Number of Substrings Containing All Three Characters
 * Medium
 *
 * Given a string s consisting only of characters a, b and c.
 *
 * Return the number of substrings containing at least one occurrence of all these characters a, b and c.
 *
 *
 * Example 1:
 *
 * Input: s = "abcabc"
 * Output: 10
 * Explanation: The substrings containing at least one occurrence of the characters a, b and c are "abc", "abca", "abcab", "abcabc", "bca", "bcab", "bcabc", "cab", "cabc" and "abc" (again).
 *
 * Example 2:
 *
 * Input: s = "aaacb"
 * Output: 3
 * Explanation: The substrings containing at least one occurrence of the characters a, b and c are "aaacb", "aacb" and "acb".
 *
 * Example 3:
 *
 * Input: s = "abc"
 * Output: 1
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 5 x 10^4
 * s only consists of a, b or c characters.
 *
 */
public class NumberOfSubstringsContainingAllThreeCharacters {

    // V0
    // IDEA: LAST-SEEN INDEX (one pass)
    //       fix the right end at i. s[j..i] is valid iff it still holds an 'a',
    //       a 'b' and a 'c'. with lastSeen[c] = last index <= i of char c, every
    //       start j in [0, min(lastSeen)] works -> min(lastSeen) + 1 substrings.
    //       a char never seen has lastSeen = -1, so it contributes 0.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numberOfSubstrings(String s) {
        int[] lastSeen = {-1, -1, -1};
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            lastSeen[s.charAt(i) - 'a'] = i;
            int earliestOfLast = Math.min(lastSeen[0], Math.min(lastSeen[1], lastSeen[2]));
            total += earliestOfLast + 1;
        }
        return total;
    }

    // V1
    // IDEA: SLIDING WINDOW (two pointers)
    //       shrink from the left while [left, right] still holds all 3 chars.
    //       after shrinking, every start 0 .. left-1 gives a valid substring
    //       ending at right -> add left to the answer.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numberOfSubstrings_1(String s) {
        int[] count = new int[3];
        int total = 0;
        int left = 0;
        for (int right = 0; right < s.length(); right++) {
            count[s.charAt(right) - 'a']++;
            while (count[0] > 0 && count[1] > 0 && count[2] > 0) {
                count[s.charAt(left) - 'a']--;
                left++;
            }
            // starts 0 .. left-1 all contain a, b and c up to right
            total += left;
        }
        return total;
    }
}
