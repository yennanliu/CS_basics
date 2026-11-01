package LeetCodeJava.HashTable;

// https://leetcode.com/problems/check-whether-two-strings-are-almost-equivalent/description/

/**
 * 2068. Check Whether Two Strings are Almost Equivalent
 * Easy
 *
 * Two strings word1 and word2 are considered almost equivalent if the differences between the
 * frequencies of each letter from 'a' to 'z' between word1 and word2 is at most 3.
 *
 * Given two strings word1 and word2, each of length n, return true if word1 and word2 are almost
 * equivalent, or false otherwise.
 *
 * The frequency of a letter x is the number of times it occurs in the string.
 *
 *
 * Example 1:
 *
 * Input: word1 = "aaaa", word2 = "bccb"
 * Output: false
 * Explanation: There are 4 'a's in "aaaa" but 0 'a's in "bccb".
 * The difference is 4, which is more than the allowed 3.
 *
 * Example 2:
 *
 * Input: word1 = "abcdeef", word2 = "abaaacc"
 * Output: true
 *
 * Example 3:
 *
 * Input: word1 = "cccddabba", word2 = "babababab"
 * Output: true
 *
 *
 * Constraints:
 *
 * n == word1.length == word2.length
 * 1 <= n <= 100
 * word1 and word2 consist only of lowercase English letters.
 *
 */
public class CheckWhetherTwoStringsAreAlmostEquivalent {

    private static final int MAX_DIFFERENCE = 3;

    // V0
    // IDEA: ONE 26-SLOT COUNTER (+1 for word1, -1 for word2), THEN CHECK EVERY LETTER
    //       the rule covers all 26 letters, including those present in only one word
    /**
     * time = O(N + 26)
     * space = O(1)
     */
    public boolean checkAlmostEquivalent(String word1, String word2) {
        // difference[i] = count of letter i in word1 minus its count in word2
        int[] difference = new int[26];
        for (char c : word1.toCharArray()) {
            difference[c - 'a']++;
        }
        for (char c : word2.toCharArray()) {
            difference[c - 'a']--;
        }

        for (int diff : difference) {
            if (Math.abs(diff) > MAX_DIFFERENCE) {
                return false;
            }
        }
        return true;
    }
}
