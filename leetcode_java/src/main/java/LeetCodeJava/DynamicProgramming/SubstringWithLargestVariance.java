package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/substring-with-largest-variance/description/

/**
 * 2272. Substring With Largest Variance
 * Hard
 *
 * The variance of a string is defined as the largest difference between the number of occurrences of any 2 characters present in the string. Note the two characters may or may not be the same.
 *
 * Given a string s consisting of lowercase English letters only, return the largest variance possible among all substrings of s.
 *
 * A substring is a contiguous sequence of characters within a string.
 *
 *
 * Example 1:
 *
 * Input: s = "aababbb"
 * Output: 3
 * Explanation:
 * All possible variances along with their respective substrings are listed below:
 * - Variance 0 for substrings "a", "aa", "ab", "abab", "aababb", "ba", "b", "bb", and "bbb".
 * - Variance 1 for substrings "aab", "aba", "abb", "aabab", "ababb", "aababbb", and "bab".
 * - Variance 2 for substrings "aaba", "ababbb", "abbb", and "babb".
 * - Variance 3 for substring "babbb".
 * Since the largest possible variance is 3, we return it.
 *
 * Example 2:
 *
 * Input: s = "abcde"
 * Output: 0
 * Explanation:
 * No letter occurs more than once in s, so the variance of every substring is 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s consists of lowercase English letters.
 *
 */
public class SubstringWithLargestVariance {

    // V0
    // IDEA: KADANE PER ORDERED LETTER PAIR (a = +1, b = -1, other letters ignored),
    //       where the subarray must contain at least one b
    //       withoutB = best running sum of a suffix containing no b (always >= 0)
    //       withB    = best running sum of a suffix containing >= 1 b (very negative until a b is seen)
    //         on a: withoutB += 1, withB += 1
    //         on b: withB = max(withB, withoutB) - 1, withoutB = 0
    /**
     * time = O(26 * 26 * N)
     * space = O(1)
     */
    public int largestVariance(String s) {
        // which letters actually occur; pairs of absent letters cannot score
        boolean[] present = new boolean[26];
        for (int i = 0; i < s.length(); i++) {
            present[s.charAt(i) - 'a'] = true;
        }

        int bestVariance = 0;
        for (int a = 0; a < 26; a++) {
            for (int b = 0; b < 26; b++) {
                if (a == b || !present[a] || !present[b]) {
                    continue;
                }
                char high = (char) ('a' + a);
                char low = (char) ('a' + b);
                int withoutB = 0;
                // "minus infinity", kept far from Integer.MIN_VALUE so adding 1 cannot overflow
                int withB = -s.length() - 1;
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c == high) {
                        withoutB++;
                        withB++;
                    } else if (c == low) {
                        // spend this b: extend the best b-containing suffix, or start one from the b-free suffix
                        withB = Math.max(withB, withoutB) - 1;
                        // a b-free suffix must restart after a b
                        withoutB = 0;
                    }
                    bestVariance = Math.max(bestVariance, withB);
                }
            }
        }
        return bestVariance;
    }
}
