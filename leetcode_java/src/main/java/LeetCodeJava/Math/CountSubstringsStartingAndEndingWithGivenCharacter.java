package LeetCodeJava.Math;

// https://leetcode.com/problems/count-substrings-starting-and-ending-with-given-character/description/

/**
 * 3084. Count Substrings Starting and Ending with Given Character
 * Medium
 *
 * You are given a string s and a character c. Return the total number of substrings of s that start
 * and end with c.
 *
 *
 * Example 1:
 *
 * Input: s = "abada", c = "a"
 * Output: 6
 * Explanation: Substrings starting and ending with "a" are: "abada", "abada", "abada", "abada", "abada", "abada".
 *
 * Example 2:
 *
 * Input: s = "zzz", c = "z"
 * Output: 6
 * Explanation: There are a total of 6 substrings in s and all start and end with "z".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s and c consist only of lowercase English letters.
 *
 */
public class CountSubstringsStartingAndEndingWithGivenCharacter {

    // V0
    // IDEA: COMBINATORICS — CHOOSE A START AND AN END OCCURRENCE (start <= end): m * (m + 1) / 2
    //   m choose 2 pairs of distinct occurrences, plus m single-character substrings.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long countSubstrings(String s, char c) {
        long occurrences = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                occurrences++;
            }
        }
        // long: m can be 10^5, so m * (m + 1) overflows int
        return occurrences * (occurrences + 1) / 2;
    }
}
