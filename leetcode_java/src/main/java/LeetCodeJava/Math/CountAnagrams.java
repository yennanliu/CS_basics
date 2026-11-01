package LeetCodeJava.Math;

// https://leetcode.com/problems/count-anagrams/description/

/**
 * 2514. Count Anagrams
 * Hard
 *
 * You are given a string s containing one or more words. Every consecutive pair of words is separated by a single space ' '.
 *
 * A string t is an anagram of string s if the ith word of t is a permutation of the ith word of s.
 *
 * For example, "acb dfe" is an anagram of "abc def", but "def cab" and "adc bef" are not.
 *
 * Return the number of distinct anagrams of s. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "too hot"
 * Output: 18
 * Explanation: Some of the anagrams of the given string are "too hot", "oot hot", "oto toh", "too toh", and "too oht".
 *
 * Example 2:
 *
 * Input: s = "aa"
 * Output: 1
 * Explanation: There is only one anagram possible for the given string.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of lowercase English letters and spaces ' '.
 * There is single space between consecutive words.
 *
 */
public class CountAnagrams {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COMBINATORICS (MULTINOMIAL PER WORD) + MODULAR INVERSE VIA FERMAT
    //   each word permutes independently: distinct(w) = len(w)! / (c1! * c2! * ... * c26!).
    //   the denominator is built incrementally - bump cnt[c] and multiply by the new cnt[c] -
    //   so no factorial table is needed; 1 / d == d^(MOD - 2) mod MOD.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int countAnagrams(String s) {
        long numerator = 1;   // product of len(w)!
        long denominator = 1; // product of every letter-count factorial

        for (String word : s.split(" ")) {
            int[] letterCount = new int[26];
            for (int i = 0; i < word.length(); i++) {
                int c = word.charAt(i) - 'a';
                letterCount[c]++;
                // extends c's count factorial by one more factor
                denominator = denominator * letterCount[c] % MOD;
                // extends len(w)! by one more factor
                numerator = numerator * (i + 1) % MOD;
            }
        }
        return (int) (numerator * modPow(denominator, MOD - 2) % MOD);
    }

    /** Fast exponentiation: base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            // the lowest bit of the exponent is set -> this power of base is part of the product
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
