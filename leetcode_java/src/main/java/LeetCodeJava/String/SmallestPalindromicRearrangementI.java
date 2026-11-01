package LeetCodeJava.String;

// https://leetcode.com/problems/smallest-palindromic-rearrangement-i/description/

/**
 * 3517. Smallest Palindromic Rearrangement I
 * Medium
 *
 * You are given a palindromic string s.
 *
 * Return the lexicographically smallest palindromic permutation of s.
 *
 * Example 1:
 *
 * Input: s = "z"
 *
 * Output: "z"
 *
 * Explanation:
 *
 * A string of only one character is already the lexicographically smallest
 * palindrome.
 *
 * Example 2:
 *
 * Input: s = "babab"
 *
 * Output: "abbba"
 *
 * Explanation:
 *
 * Rearranging "babab" → "abbba" gives the smallest lexicographic palindrome.
 *
 * Example 3:
 *
 * Input: s = "daccad"
 *
 * Output: "acddca"
 *
 * Explanation:
 *
 * Rearranging "daccad" → "acddca" gives the smallest lexicographic palindrome.
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 *
 * s consists of lowercase English letters.
 *
 * s is guaranteed to be palindromic.
 *
 */
public class SmallestPalindromicRearrangementI {

    // V0
    // IDEA: A PALINDROME IS FIXED BY ITS LEFT HALF -> SORTED HALF + ODD-COUNT MIDDLE + MIRROR
    /**
     * time = O(N)
     * space = O(N)
     */
    public String smallestPalindrome(String s) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        // the left half in sorted order is the smallest possible left half
        StringBuilder leftHalf = new StringBuilder();
        String middle = "";
        for (int c = 0; c < 26; c++) {
            char letter = (char) ('a' + c);
            for (int k = 0; k < count[c] / 2; k++) {
                leftHalf.append(letter);
            }
            // s is a palindrome, so at most one letter has an odd count
            if (count[c] % 2 == 1) {
                middle = String.valueOf(letter);
            }
        }
        String left = leftHalf.toString();
        String right = new StringBuilder(left).reverse().toString();
        return left + middle + right;
    }
}
