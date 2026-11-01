package LeetCodeJava.String;

// https://leetcode.com/problems/increasing-decreasing-string/description/

/**
 * 1370. Increasing Decreasing String
 * Easy
 *
 * You are given a string s. Reorder the string using the following algorithm:
 *
 * Remove the smallest character from s and append it to the result.
 * Remove the smallest character from s that is greater than the last appended character,
 * and append it to the result.
 * Repeat step 2 until no more characters can be removed.
 * Remove the largest character from s and append it to the result.
 * Remove the largest character from s that is smaller than the last appended character,
 * and append it to the result.
 * Repeat step 5 until no more characters can be removed.
 * Repeat steps 1 through 6 until all characters from s have been removed.
 *
 * If the smallest or largest character appears more than once, you may choose any occurrence
 * to append to the result.
 *
 * Return the resulting string after reordering s using this algorithm.
 *
 *
 * Example 1:
 *
 * Input: s = "aaaabbbbcccc"
 * Output: "abccbaabccba"
 * Explanation: After steps 1, 2 and 3 of the first iteration, result = "abc"
 * After steps 4, 5 and 6 of the first iteration, result = "abccba"
 * First iteration is done. Now s = "aabbcc" and we go back to step 1
 * After steps 1, 2 and 3 of the second iteration, result = "abccbaabc"
 * After steps 4, 5 and 6 of the second iteration, result = "abccbaabccba"
 *
 * Example 2:
 *
 * Input: s = "rat"
 * Output: "art"
 * Explanation: The word "rat" becomes "art" after re-ordering it with the mentioned algorithm.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 500
 * s consists of only lowercase English letters.
 *
 */
public class IncreasingDecreasingString {

    // V0
    // IDEA: COUNTING - ONE ROUND = SCAN 'a'..'z' THEN 'z'..'a'
    //       steps 1-3 take one copy of each available letter in increasing order, steps 4-6
    //       the same in decreasing order. repeat rounds until every char is used.
    //       the "strictly greater / smaller" rule holds automatically: a one-way scan never
    //       visits the same letter twice.
    /**
     * time = O(26 * N)
     * space = O(N)
     */
    public String sortString(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        StringBuilder sb = new StringBuilder();
        while (sb.length() < s.length()) {
            // increasing half of the round
            for (int c = 0; c < 26; c++) {
                if (count[c] > 0) {
                    sb.append((char) ('a' + c));
                    count[c]--;
                }
            }
            // decreasing half of the round
            for (int c = 25; c >= 0; c--) {
                if (count[c] > 0) {
                    sb.append((char) ('a' + c));
                    count[c]--;
                }
            }
        }
        return sb.toString();
    }
}
