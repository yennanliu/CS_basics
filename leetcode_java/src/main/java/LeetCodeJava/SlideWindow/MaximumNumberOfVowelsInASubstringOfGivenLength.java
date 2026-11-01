package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/description/

/**
 * 1456. Maximum Number of Vowels in a Substring of Given Length
 * Medium
 *
 * Given a string s and an integer k, return the maximum number of vowel letters in
 * any substring of s with length k.
 *
 * Vowel letters in English are 'a', 'e', 'i', 'o', and 'u'.
 *
 *
 * Example 1:
 *
 * Input: s = "abciiidef", k = 3
 * Output: 3
 * Explanation: The substring "iii" contains 3 vowel letters.
 *
 * Example 2:
 *
 * Input: s = "aeiou", k = 2
 * Output: 2
 * Explanation: Any substring of length 2 contains 2 vowels.
 *
 * Example 3:
 *
 * Input: s = "leetcode", k = 3
 * Output: 2
 * Explanation: "lee", "eet" and "ode" contain 2 vowels.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters.
 * 1 <= k <= s.length
 *
 */
public class MaximumNumberOfVowelsInASubstringOfGivenLength {

    // V0
    // IDEA: FIXED SIZE SLIDING WINDOW
    //       the window length never changes, so each step adds s[i], drops
    //       s[i-k], and tracks the running vowel count.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxVowels(String s, int k) {
        // vowels in the first window
        int count = 0;
        for (int i = 0; i < k; i++) {
            if (isVowel(s.charAt(i))) {
                count++;
            }
        }

        int best = count;
        for (int i = k; i < s.length(); i++) {
            // s[i] enters, s[i - k] leaves
            if (isVowel(s.charAt(i))) {
                count++;
            }
            if (isVowel(s.charAt(i - k))) {
                count--;
            }
            best = Math.max(best, count);

            // a k-length window cannot hold more than k vowels
            if (best == k) {
                return k;
            }
        }
        return best;
    }

    /** True for a, e, i, o, u. */
    private boolean isVowel(char c) {
        return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
    }
}
