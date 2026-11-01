package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-if-all-characters-have-equal-number-of-occurrences/description/

/**
 * 1941. Check if All Characters Have Equal Number of Occurrences
 * Easy
 *
 * Given a string s, return true if s is a good string, or false otherwise.
 *
 * A string s is good if all the characters that appear in s have the same number of occurrences
 * (i.e., the same frequency).
 *
 *
 * Example 1:
 *
 * Input: s = "abacbc"
 * Output: true
 * Explanation: The characters that appear in s are 'a', 'b', and 'c'. All characters occur 2 times in s.
 *
 * Example 2:
 *
 * Input: s = "aaabb"
 * Output: false
 * Explanation: The characters that appear in s are 'a' and 'b'.
 * 'a' occurs 3 times while 'b' occurs 2 times, which is not the same number of times.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists of lowercase English letters.
 *
 */
public class CheckIfAllCharactersHaveEqualNumberOfOccurrences {

    // V0
    // IDEA: COUNT EVERY LETTER, THEN CHECK ALL NON-ZERO COUNTS ARE THE SAME
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean areOccurrencesEqual(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        // the frequency every present letter must match; 0 means "not set yet"
        int expected = 0;
        for (int freq : count) {
            // letters that do not appear are ignored
            if (freq == 0) {
                continue;
            }
            if (expected == 0) {
                expected = freq;
            } else if (freq != expected) {
                return false;
            }
        }
        return true;
    }
}
