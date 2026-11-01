package LeetCodeJava.String;

// https://leetcode.com/problems/longest-palindrome-after-substring-concatenation-i/description/

/**
 * 3503. Longest Palindrome After Substring Concatenation I
 * Medium
 *
 * You are given two strings, s and t.
 *
 * You can create a new string by selecting a substring from s (possibly empty) and
 * a substring from t (possibly empty), then concatenating them in order.
 *
 * Return the length of the longest palindrome that can be formed this way.
 *
 * Example 1:
 *
 * Input: s = "a", t = "a"
 *
 * Output: 2
 *
 * Explanation:
 *
 * Concatenating "a" from s and "a" from t results in "aa", which is a palindrome
 * of length 2.
 *
 * Example 2:
 *
 * Input: s = "abc", t = "def"
 *
 * Output: 1
 *
 * Explanation:
 *
 * Since all characters are different, the longest palindrome is any single
 * character, so the answer is 1.
 *
 * Example 3:
 *
 * Input: s = "b", t = "aaaa"
 *
 * Output: 4
 *
 * Explanation:
 *
 * Selecting "aaaa" from t is the longest palindrome, so the answer is 4.
 *
 * Example 4:
 *
 * Input: s = "abcde", t = "ecdba"
 *
 * Output: 5
 *
 * Explanation:
 *
 * Concatenating "abc" from s and "ba" from t results in "abcba", which is a
 * palindrome of length 5.
 *
 * Constraints:
 *
 * 1 <= s.length, t.length <= 30
 *
 * s and t consist of lowercase English letters.
 *
 */
public class LongestPalindromeAfterSubstringConcatenationI {

    // V0
    // IDEA: BRUTE FORCE OVER EVERY (SUBSTRING OF s, SUBSTRING OF t) PAIR, EMPTY ALLOWED
    //   |s|, |t| <= 30 -> ~496 substrings each side, ~250k concatenations to test.
    //   The empty substring on either side covers palindromes living inside s or t alone.
    /**
     * time = O(N^2 * M^2 * (N + M))
     * space = O(N + M)
     */
    public int longestPalindrome(String s, String t) {
        int best = 0;
        for (int i = 0; i <= s.length(); i++) {
            for (int j = i; j <= s.length(); j++) {
                String fromS = s.substring(i, j);
                for (int k = 0; k <= t.length(); k++) {
                    for (int l = k; l <= t.length(); l++) {
                        int candidateLength = (j - i) + (l - k);
                        // cannot beat the current best -> skip the palindrome test
                        if (candidateLength <= best) {
                            continue;
                        }
                        String candidate = fromS + t.substring(k, l);
                        if (isPalindrome(candidate)) {
                            best = candidateLength;
                        }
                    }
                }
            }
        }
        return best;
    }

    /** Two-pointer palindrome check. */
    private boolean isPalindrome(String str) {
        int left = 0;
        int right = str.length() - 1;
        while (left < right) {
            if (str.charAt(left) != str.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
