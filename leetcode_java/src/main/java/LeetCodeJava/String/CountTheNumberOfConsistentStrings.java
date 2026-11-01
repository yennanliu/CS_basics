package LeetCodeJava.String;

// https://leetcode.com/problems/count-the-number-of-consistent-strings/description/

/**
 * 1684. Count the Number of Consistent Strings
 * Easy
 *
 * You are given a string allowed consisting of distinct characters and an array of strings words.
 * A string is consistent if all characters in the string appear in the string allowed.
 *
 * Return the number of consistent strings in the array words.
 *
 *
 * Example 1:
 *
 * Input: allowed = "ab", words = ["ad","bd","aaab","baa","badab"]
 * Output: 2
 * Explanation: Strings "aaab" and "baa" are consistent since they only contain characters 'a' and 'b'.
 *
 * Example 2:
 *
 * Input: allowed = "abc", words = ["a","b","c","ab","ac","bc","abc"]
 * Output: 7
 * Explanation: All strings are consistent.
 *
 * Example 3:
 *
 * Input: allowed = "cad", words = ["cc","acd","b","ba","bac","bad","ac","d"]
 * Output: 4
 * Explanation: Strings "cc", "acd", "ac", and "d" are consistent.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 10^4
 * 1 <= allowed.length <= 26
 * 1 <= words[i].length <= 10
 * The characters in allowed are distinct.
 * words[i] and allowed contain only lowercase English letters.
 *
 */
public class CountTheNumberOfConsistentStrings {

    // V0
    // IDEA: SET CONTAINMENT (A WORD IS CONSISTENT IFF EVERY CHAR IS ALLOWED)
    /**
     * time = O(A + L)   (A = allowed length, L = total length of words)
     * space = O(1)      (26 slots)
     */
    public int countConsistentStrings(String allowed, String[] words) {
        boolean[] isAllowed = new boolean[26];
        for (char c : allowed.toCharArray()) {
            isAllowed[c - 'a'] = true;
        }

        int count = 0;
        for (String word : words) {
            boolean consistent = true;
            for (char c : word.toCharArray()) {
                if (!isAllowed[c - 'a']) {
                    consistent = false;
                    break;
                }
            }
            if (consistent) {
                count++;
            }
        }
        return count;
    }
}
