package LeetCodeJava.String;

// https://leetcode.com/problems/permutation-difference-between-two-strings/description/

/**
 * 3146. Permutation Difference between Two Strings
 * Easy
 *
 * You are given two strings s and t such that every character occurs at most once in s and t is a permutation of s.
 *
 * The permutation difference between s and t is defined as the sum of the absolute difference between the index of the occurrence of each character in s and the index of the occurrence of the same character in t.
 *
 * Return the permutation difference between s and t.
 *
 *
 * Example 1:
 *
 * Input: s = "abc", t = "bac"
 * Output: 2
 * Explanation:
 * For s = "abc" and t = "bac", the permutation difference of s and t is equal to the sum of:
 * The absolute difference between the index of the occurrence of "a" in s and the index of the occurrence of "a" in t.
 * The absolute difference between the index of the occurrence of "b" in s and the index of the occurrence of "b" in t.
 * The absolute difference between the index of the occurrence of "c" in s and the index of the occurrence of "c" in t.
 * That is, |0 - 1| + |1 - 0| + |2 - 2| = 2.
 *
 * Example 2:
 *
 * Input: s = "abcde", t = "edbac"
 * Output: 12
 * Explanation: The permutation difference between s and t is equal to |0 - 3| + |1 - 2| + |2 - 4| + |3 - 1| + |4 - 0| = 12.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 26
 * Each character occurs at most once in s.
 * t is a permutation of s.
 * s consists only of lowercase English letters.
 *
 */
public class PermutationDifferenceBetweenTwoStrings {

    // V0
    // IDEA: INDEX EACH CHARACTER'S POSITION IN s, THEN SUM |pos_s - pos_t| WHILE WALKING t
    /**
     * time = O(N)
     * space = O(1)
     */
    public int findPermutationDifference(String s, String t) {
        // characters are unique, so one position per letter is well defined
        int[] positionInS = new int[26];
        for (int i = 0; i < s.length(); i++) {
            positionInS[s.charAt(i) - 'a'] = i;
        }
        int difference = 0;
        for (int j = 0; j < t.length(); j++) {
            difference += Math.abs(positionInS[t.charAt(j) - 'a'] - j);
        }
        return difference;
    }
}
