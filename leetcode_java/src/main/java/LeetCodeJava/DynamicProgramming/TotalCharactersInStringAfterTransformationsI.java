package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/total-characters-in-string-after-transformations-i/description/
/**
 * 3335. Total Characters in String After Transformations I
 * Medium
 *
 * You are given a string s and an integer t, representing the number of transformations to
 * perform. In one transformation, every character in s is replaced according to the following rules:
 *
 * If the character is 'z', replace it with the string "ab".
 * Otherwise, replace it with the next character in the alphabet. For example, 'a' is replaced
 * with 'b', 'b' is replaced with 'c', and so on.
 *
 * Return the length of the resulting string after exactly t transformations.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "abcyy", t = 2
 * Output: 7
 * Explanation:
 * String after the first transformation: "bcdzz"
 * String after the second transformation: "cdeabab"
 * Final Length of the string: The string is "cdeabab", which has 7 characters.
 *
 * Example 2:
 *
 * Input: s = "azbk", t = 1
 * Output: 5
 * Explanation:
 * String after the first transformation: "bcabl"
 * Final Length of the string: The string is "bcabl", which has 5 characters.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 1 <= t <= 10^5
 * s consists only of lowercase English letters.
 *
 */
public class TotalCharactersInStringAfterTransformationsI {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON THE 26-LETTER HISTOGRAM (EACH CHAR EVOLVES INDEPENDENTLY)
    /**
     *  DP def:
     *     - count[i] = how many copies of letter i exist after the current round
     *  DP eq (one round):
     *     - newCount[0] = count[25]                 ('z' -> "ab" gives an 'a')
     *     - newCount[1] = count[0] + count[25]      ('a' -> 'b', and 'z' gives a 'b')
     *     - newCount[i] = count[i - 1]   for i >= 2
     *  Answer: sum(count) after t rounds.
     *
     *  time = O(N + 26 * T)
     *  space = O(1)
     */
    public int lengthAfterTransformations(String s, int t) {
        long[] count = new long[26];
        for (char ch : s.toCharArray()) {
            count[ch - 'a']++;
        }

        for (int round = 0; round < t; round++) {
            long[] next = new long[26];
            next[0] = count[25];
            next[1] = (count[0] + count[25]) % MOD;
            for (int i = 2; i < 26; i++) {
                next[i] = count[i - 1];
            }
            count = next;
        }

        long total = 0;
        for (long c : count) {
            total = (total + c) % MOD;
        }
        return (int) total;
    }
}
