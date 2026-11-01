package LeetCodeJava.String;

// https://leetcode.com/problems/sum-of-scores-of-built-strings/description/

/**
 * 2223. Sum of Scores of Built Strings
 * Hard
 *
 * You are building a string s of length n one character at a time, prepending each new character to the front of the string. The strings are labeled from 1 to n, where the string with length i is labeled si.
 *
 * For example, for s = "abaca", s1 == "a", s2 == "ca", s3 == "aca", etc.
 *
 * The score of si is the length of the longest common prefix between si and sn (Note that s == sn).
 *
 * Given the final string s, return the sum of the score of every si.
 *
 *
 * Example 1:
 *
 * Input: s = "babab"
 * Output: 9
 * Explanation:
 * For s1 == "b", the longest common prefix is "b" which has a score of 1.
 * For s2 == "ab", there is no common prefix so the score is 0.
 * For s3 == "bab", the longest common prefix is "bab" which has a score of 3.
 * For s4 == "abab", there is no common prefix so the score is 0.
 * For s5 == "babab", the longest common prefix is "babab" which has a score of 5.
 * The sum of the scores is 1 + 0 + 3 + 0 + 5 = 9, so we return 9.
 *
 * Example 2:
 *
 * Input: s = "azbazbzaz"
 * Output: 14
 * Explanation:
 * For s2 == "az", the longest common prefix is "az" which has a score of 2.
 * For s6 == "azbzaz", the longest common prefix is "azb" which has a score of 3.
 * For s9 == "azbazbzaz", the longest common prefix is "azbazbzaz" which has a score of 9.
 * For all other si, the score is 0.
 * The sum of the scores is 2 + 3 + 9 = 14, so we return 14.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters.
 *
 */
public class SumOfScoresOfBuiltStrings {

    // V0
    // IDEA: Z-ALGORITHM — s_i is the suffix of s of length i, so score(s_i) = z[n - i]
    //       and the answer is the sum of the whole Z-array (with z[0] = n)
    /**
     * time = O(N)
     * space = O(N)
     */
    public long sumScores(String s) {
        int n = s.length();
        int[] z = new int[n];
        z[0] = n;
        // [left, right) = the rightmost segment known to match a prefix of s
        int left = 0;
        int right = 0;
        for (int i = 1; i < n; i++) {
            // inside the z-box: reuse the value at the mirrored position, capped at the box edge
            if (i < right) {
                z[i] = Math.min(right - i, z[i - left]);
            }
            // extend by direct comparison; each success pushes right forward, so total work is linear
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] > right) {
                left = i;
                right = i + z[i];
            }
        }
        // the sum can exceed int for n = 10^5
        long total = 0;
        for (int value : z) {
            total += value;
        }
        return total;
    }
}
