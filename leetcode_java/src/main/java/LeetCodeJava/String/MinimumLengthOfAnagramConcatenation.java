package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-length-of-anagram-concatenation/description/

import java.util.Arrays;

/**
 * 3138. Minimum Length of Anagram Concatenation
 * Medium
 *
 * You are given a string s, which is known to be a concatenation of anagrams of some string t.
 *
 * Return the minimum possible length of the string t.
 *
 * An anagram is formed by rearranging the letters of a string. For example, "aab", "aba", and, "baa" are anagrams of "aab".
 *
 *
 * Example 1:
 *
 * Input: s = "abba"
 * Output: 2
 * Explanation:
 * One possible string t could be "ba".
 *
 * Example 2:
 *
 * Input: s = "cdef"
 * Output: 4
 * Explanation:
 * One possible string t could be "cdef", notice that t can be equal to s.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consist only of lowercase English letters.
 *
 */
public class MinimumLengthOfAnagramConcatenation {

    // V0
    // IDEA: THE BLOCK COUNT MUST DIVIDE EVERY LETTER'S TOTAL (GCD PRUNING) -> VERIFY EACH CANDIDATE
    //   if s splits into d anagram blocks, each block holds total[c] / d copies of letter c,
    //   so d divides gcd(all letter totals). The block length is n / d, so the smallest
    //   length comes from the LARGEST valid d: try divisors of the gcd from large to small.
    /**
     * time = O(D(g) * N), D(g) = number of divisors of the gcd of letter counts
     * space = O(1)
     */
    public int minAnagramLength(String s) {
        int n = s.length();

        // letter totals over the whole string
        int[] total = new int[26];
        for (int i = 0; i < n; i++) {
            total[s.charAt(i) - 'a']++;
        }

        // g = gcd of all non-zero letter totals; any block count must divide it
        int g = 0;
        for (int count : total) {
            if (count > 0) {
                g = gcd(g, count);
            }
        }

        // largest block count first -> shortest block length first
        for (int blocks = g; blocks >= 1; blocks--) {
            if (g % blocks != 0) {
                continue;
            }
            int blockLen = n / blocks;
            if (allBlocksAreAnagrams(s, blockLen)) {
                return blockLen;
            }
        }
        // blocks = 1 (the whole string) always verifies, so this is unreachable
        return n;
    }

    /** True when every length-blockLen block of s has the same letter counts as the first block. */
    private boolean allBlocksAreAnagrams(String s, int blockLen) {
        int[] base = new int[26];
        for (int i = 0; i < blockLen; i++) {
            base[s.charAt(i) - 'a']++;
        }
        for (int start = blockLen; start < s.length(); start += blockLen) {
            int[] current = new int[26];
            for (int i = start; i < start + blockLen; i++) {
                current[s.charAt(i) - 'a']++;
            }
            if (!Arrays.equals(base, current)) {
                return false;
            }
        }
        return true;
    }

    /** Euclid's gcd; gcd(0, x) = x. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
