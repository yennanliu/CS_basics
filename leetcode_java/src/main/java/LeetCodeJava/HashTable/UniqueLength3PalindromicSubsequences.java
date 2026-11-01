package LeetCodeJava.HashTable;

// https://leetcode.com/problems/unique-length-3-palindromic-subsequences/description/

import java.util.*;

/**
 * 1930. Unique Length-3 Palindromic Subsequences
 * Medium
 *
 * Given a string s, return the number of unique palindromes of length three that are a subsequence of s.
 *
 * Note that even if there are multiple ways to obtain the same subsequence, it is still only counted once.
 *
 * A palindrome is a string that reads the same forwards and backwards.
 *
 * A subsequence of a string is a new string generated from the original string with some characters
 * (can be none) deleted without changing the relative order of the remaining characters.
 *
 * For example, "ace" is a subsequence of "abcde".
 *
 *
 * Example 1:
 *
 * Input: s = "aabca"
 * Output: 3
 * Explanation: The 3 palindromic subsequences of length 3 are "aba", "aaa", "aca".
 *
 * Example 2:
 *
 * Input: s = "adc"
 * Output: 0
 *
 * Example 3:
 *
 * Input: s = "bbcbaba"
 * Output: 4
 * Explanation: The 4 palindromic subsequences of length 3 are "bbb", "bcb", "bab", "aba".
 *
 *
 * Constraints:
 *
 * 3 <= s.length <= 10^5
 * s consists of only lowercase English letters.
 *
 */
public class UniqueLength3PalindromicSubsequences {

    // V0
    // IDEA: FIX THE OUTER LETTER (26 passes) + COUNT DISTINCT MIDDLE LETTERS
    //       a palindrome "c ? c" can use the widest pair of c's (first and last occurrence);
    //       any letter strictly between them is a valid middle
    /**
     * time = O(26 * N)
     * space = O(1)
     */
    public int countPalindromicSubsequence(String s) {
        int palindromes = 0;

        for (char outer = 'a'; outer <= 'z'; outer++) {
            int first = s.indexOf(outer);
            int last = s.lastIndexOf(outer);
            // missing, single, or adjacent occurrences leave no room for a middle letter
            if (last - first <= 1) {
                continue;
            }

            Set<Character> middles = new HashSet<>();
            for (int i = first + 1; i < last; i++) {
                middles.add(s.charAt(i));
            }
            palindromes += middles.size();
        }
        return palindromes;
    }
}
