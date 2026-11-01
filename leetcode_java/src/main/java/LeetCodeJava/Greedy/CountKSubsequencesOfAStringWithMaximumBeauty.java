package LeetCodeJava.Greedy;

// https://leetcode.com/problems/count-k-subsequences-of-a-string-with-maximum-beauty/description/

import java.util.*;

/**
 * 2842. Count K-Subsequences of a String With Maximum Beauty
 * Hard
 *
 * You are given a string s and an integer k.
 *
 * A k-subsequence is a subsequence of s, having length k, and all its characters are unique, i.e., every character occurs once.
 *
 * Let f(c) denote the number of times the character c occurs in s.
 *
 * The beauty of a k-subsequence is the sum of f(c) for every character c in the k-subsequence.
 *
 * For example, consider s = "abbbdd" and k = 2:
 *
 * f('a') = 1, f('b') = 3, f('d') = 2
 * Some k-subsequences of s are:
 * "abbbdd" -> "ab" having a beauty of f('a') + f('b') = 4
 * "abbbdd" -> "ad" having a beauty of f('a') + f('d') = 3
 * "abbbdd" -> "bd" having a beauty of f('b') + f('d') = 5
 *
 * Return an integer denoting the number of k-subsequences whose beauty is the maximum among all k-subsequences. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * A subsequence of a string is a new string formed from the original string by deleting some (possibly none) of the characters without disturbing the relative positions of the remaining characters.
 *
 * Notes
 *
 * - f(c) is the number of times a character c occurs in s, not a k-subsequence.
 * - Two k-subsequences are considered different if one is formed by an index that is not present in the other. So, two k-subsequences may form the same string.
 *
 *
 * Example 1:
 *
 * Input: s = "bcca", k = 2
 * Output: 4
 * Explanation: From s we have f('a') = 1, f('b') = 1, and f('c') = 2.
 * The k-subsequences of s are:
 * bcca having a beauty of f('b') + f('c') = 3
 * bcca having a beauty of f('b') + f('c') = 3
 * bcca having a beauty of f('b') + f('a') = 2
 * bcca having a beauty of f('c') + f('a') = 3
 * bcca having a beauty of f('c') + f('a') = 3
 * There are 4 k-subsequences that have the maximum beauty, 3.
 * Hence, the answer is 4.
 *
 * Example 2:
 *
 * Input: s = "abbcd", k = 4
 * Output: 2
 * Explanation: From s we have f('a') = 1, f('b') = 2, f('c') = 1, and f('d') = 1.
 * The k-subsequences of s are:
 * abbcd having a beauty of f('a') + f('b') + f('c') + f('d') = 5
 * abbcd having a beauty of f('a') + f('b') + f('c') + f('d') = 5
 * There are 2 k-subsequences that have the maximum beauty, 5.
 * Hence, the answer is 2.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 2 * 10^5
 * 1 <= k <= s.length
 * s consists only of lowercase English letters.
 *
 */
public class CountKSubsequencesOfAStringWithMaximumBeauty {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: GREEDY (TAKE THE K BIGGEST FREQUENCIES) + COMBINATORICS
    /**
     *  A k-subsequence uses k distinct letters, and its beauty is the sum of
     *  their frequencies, so the max is the k largest frequencies. Each chosen
     *  letter can be realised by any one of its f(c) positions:
     *    - letters with frequency strictly above the k-th largest (val) are
     *      forced into every optimal pick -> multiply their frequencies;
     *    - x letters tie at val and r slots remain -> C(x, r) * val^r.
     *
     *  time = O(N + 26 log 26)
     *  space = O(26)
     */
    public int countKSubsequencesWithMaxBeauty(String s, int k) {
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
        }

        // the non-zero frequencies, sorted descending
        List<Integer> frequencies = new ArrayList<>();
        for (int c : count) {
            if (c > 0) {
                frequencies.add(c);
            }
        }
        if (frequencies.size() < k) {
            return 0; // not enough distinct letters
        }
        frequencies.sort(Collections.reverseOrder());

        int val = frequencies.get(k - 1);

        // letters strictly more frequent than val are always chosen
        long answer = 1;
        int forced = 0;
        while (forced < k && frequencies.get(forced) > val) {
            answer = answer * frequencies.get(forced) % MOD;
            forced++;
        }

        // x letters tie at val; choose r of them
        int tied = 0;
        for (int f : frequencies) {
            if (f == val) {
                tied++;
            }
        }
        int remaining = k - forced;

        // C(tied, remaining) exactly; tied <= 26 so it fits in a long
        long combinations = 1;
        for (int i = 0; i < remaining; i++) {
            combinations = combinations * (tied - i) / (i + 1);
        }

        answer = answer * (combinations % MOD) % MOD;
        answer = answer * modPow(val, remaining) % MOD;
        return (int) answer;
    }

    /** base^exp mod MOD by repeated squaring. */
    private long modPow(long base, int exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
}
