package LeetCodeJava.Greedy;

// https://leetcode.com/problems/construct-k-palindrome-strings/description/

/**
 * 1400. Construct K Palindrome Strings
 * Medium
 *
 * Given a string s and an integer k, return true if you can use all the characters in s to
 * construct non-empty k palindrome strings or false otherwise.
 *
 *
 * Example 1:
 *
 * Input: s = "annabelle", k = 2
 * Output: true
 * Explanation: You can construct two palindromes using all characters in s.
 * Some possible constructions "anna" + "elble", "anbna" + "elle", "anellena" + "b"
 *
 * Example 2:
 *
 * Input: s = "leetcode", k = 3
 * Output: false
 * Explanation: It is impossible to construct 3 palindromes using all the characters of s.
 *
 * Example 3:
 *
 * Input: s = "true", k = 4
 * Output: true
 * Explanation: The only possible solution is to put each character in a separate string.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters.
 * 1 <= k <= 10^5
 *
 */
public class ConstructKPalindromeStrings {

    // V0
    // IDEA: GREEDY / PARITY COUNT (each odd-count letter needs its own palindrome centre)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean canConstruct(String s, int k) {
        // not enough letters to make k non-empty strings
        if (s.length() < k) {
            return false;
        }

        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        // a palindrome holds at most one letter with an odd count (its centre)
        int oddLetters = 0;
        for (int c : count) {
            if (c % 2 == 1) {
                oddLetters++;
            }
        }

        // pairs can be split across palindromes freely, so this is also sufficient
        return oddLetters <= k;
    }
}
