package LeetCodeJava.Math;

// https://leetcode.com/problems/vowels-of-all-substrings/description/

/**
 * 2063. Vowels of All Substrings
 * Medium
 *
 * Given a string word, return the sum of the number of vowels ('a', 'e', 'i', 'o', and 'u') in every substring of word.
 *
 * A substring is a contiguous (non-empty) sequence of characters within a string.
 *
 * Note: Due to the large constraints, the answer may not fit in a signed 32-bit integer. Please be careful during the calculations.
 *
 *
 * Example 1:
 *
 * Input: word = "aba"
 * Output: 6
 * Explanation:
 * All possible substrings are: "a", "ab", "aba", "b", "ba", and "a".
 * - "b" has 0 vowels in it
 * - "a", "ab", "ba", and "a" have 1 vowel each
 * - "aba" has 2 vowels in it
 * Hence, the total sum of vowels = 0 + 1 + 1 + 1 + 1 + 2 = 6.
 *
 * Example 2:
 *
 * Input: word = "abc"
 * Output: 3
 * Explanation:
 * All possible substrings are: "a", "ab", "abc", "b", "bc", and "c".
 * - "a", "ab", and "abc" have 1 vowel each
 * - "b", "bc", and "c" have 0 vowels each
 * Hence, the total sum of vowels = 1 + 1 + 1 + 0 + 0 + 0 = 3.
 *
 * Example 3:
 *
 * Input: word = "ltcd"
 * Output: 0
 * Explanation: There are no vowels in any substring of "ltcd".
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 10^5
 * word consists of lowercase English letters.
 *
 */
public class VowelsOfAllSubstrings {

    // V0
    // IDEA: COUNT EACH VOWEL'S CONTRIBUTION - index i lies in (i + 1) * (n - i) substrings
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countVowels(String word) {
        int n = word.length();
        long total = 0;
        for (int i = 0; i < n; i++) {
            if (isVowel(word.charAt(i))) {
                // (i + 1) choices for the left end, (n - i) for the right end; long avoids overflow
                total += (long) (i + 1) * (n - i);
            }
        }
        return total;
    }

    /** True for 'a', 'e', 'i', 'o', 'u'. */
    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }
}
