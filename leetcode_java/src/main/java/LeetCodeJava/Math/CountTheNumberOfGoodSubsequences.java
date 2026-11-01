package LeetCodeJava.Math;

// https://leetcode.com/problems/count-the-number-of-good-subsequences/description/

/**
 * 2539. Count the Number of Good Subsequences
 * Medium
 *
 * A subsequence of a string is good if it is not empty and the frequency of each one of its characters is the same.
 *
 * Given a string s, return the number of good subsequences of s. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 * A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.
 *
 *
 * Example 1:
 *
 * Input: s = "aabb"
 * Output: 11
 * Explanation: The total number of subsequences is 2^4. There are five subsequences which are not good: "aab", "abb", "abb", "aab", and the empty subsequence. Hence, the number of good subsequences is 2^4 - 5 = 11.
 *
 * Example 2:
 *
 * Input: s = "leet"
 * Output: 12
 *
 * Example 3:
 *
 * Input: s = "abcd"
 * Output: 15
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s consists of only lowercase English letters.
 *
 */
public class CountTheNumberOfGoodSubsequences {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: COMBINATORICS -- FIX THE COMMON FREQUENCY f, MULTIPLY (C(cnt[c], f) + 1) OVER LETTERS, SUBTRACT THE EMPTY PICK
    /**
     * time = O(26 * N)
     * space = O(N)
     */
    public int countGoodSubsequences(String s) {
        int n = s.length();

        // factorials and inverse factorials for nCr mod p
        long[] fact = new long[n + 1];
        long[] invFact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }
        // Fermat: a^(p-2) is the inverse of a, since MOD is prime
        invFact[n] = modPow(fact[n], MOD - 2);
        for (int i = n; i > 0; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }

        int[] count = new int[26];
        int maxCount = 0;
        for (char ch : s.toCharArray()) {
            count[ch - 'a']++;
            maxCount = Math.max(maxCount, count[ch - 'a']);
        }

        long answer = 0;
        for (int f = 1; f <= maxCount; f++) {
            // each letter either skips (1 way) or contributes exactly f occurrences
            long ways = 1;
            for (int c = 0; c < 26; c++) {
                if (count[c] >= f) {
                    long choose = fact[count[c]] * invFact[f] % MOD * invFact[count[c] - f] % MOD;
                    ways = ways * (choose + 1) % MOD;
                }
            }
            // drop the case where every letter skipped (the empty subsequence)
            answer = (answer + ways - 1 + MOD) % MOD;
        }
        return (int) answer;
    }

    /** Computes base^exp mod MOD by repeated squaring. */
    private long modPow(long base, long exp) {
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
