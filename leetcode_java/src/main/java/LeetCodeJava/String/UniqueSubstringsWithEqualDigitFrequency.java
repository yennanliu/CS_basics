package LeetCodeJava.String;

// https://leetcode.com/problems/unique-substrings-with-equal-digit-frequency/description/

import java.util.*;

/**
 * 2168. Unique Substrings With Equal Digit Frequency
 * Medium
 *
 * You are given a digit string s.
 *
 * Return the number of unique substrings of s where every digit appears the same number of times.
 *
 *
 * Example 1:
 *
 * Input: s = "1212"
 * Output: 5
 * Explanation: The substrings that meet the requirements are "1", "2", "12", "21", "1212".
 * Note that although the substring "12" appears twice, it is counted only once.
 *
 * Example 2:
 *
 * Input: s = "12321"
 * Output: 9
 * Explanation: The substrings that meet the requirements are "1", "2", "3", "12", "23", "32", "21", "123", "321".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists of digits.
 *
 */
public class UniqueSubstringsWithEqualDigitFrequency {

    private static final long MOD1 = 1_000_000_007L;
    private static final long MOD2 = 1_000_000_009L;
    private static final long BASE = 131;

    // V0
    // IDEA: EXPAND EVERY START, TRACK DIGIT COUNTS INCREMENTALLY, DEDUPE BY ROLLING HASH
    //       a window is balanced iff distinct * maxCount == window length.
    //       each balanced substring is identified in O(1) by a double polynomial hash
    //       (two moduli, packed into one long) built from prefix hashes.
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int equalDigitFrequency(String s) {
        int n = s.length();
        // prefix hashes: hash[i] = hash of s[0 .. i-1]; pow[i] = BASE^i
        long[] hash1 = new long[n + 1];
        long[] hash2 = new long[n + 1];
        long[] pow1 = new long[n + 1];
        long[] pow2 = new long[n + 1];
        pow1[0] = 1;
        pow2[0] = 1;
        for (int i = 0; i < n; i++) {
            hash1[i + 1] = (hash1[i] * BASE + s.charAt(i)) % MOD1;
            hash2[i + 1] = (hash2[i] * BASE + s.charAt(i)) % MOD2;
            pow1[i + 1] = pow1[i] * BASE % MOD1;
            pow2[i + 1] = pow2[i] * BASE % MOD2;
        }

        Set<Long> seen = new HashSet<>();
        for (int start = 0; start < n; start++) {
            int[] count = new int[10];
            int distinct = 0;
            int maxCount = 0;
            for (int end = start; end < n; end++) {
                int digit = s.charAt(end) - '0';
                if (count[digit] == 0) {
                    distinct++;
                }
                count[digit]++;
                maxCount = Math.max(maxCount, count[digit]);
                // every present digit occurs maxCount times exactly when the totals line up
                if (distinct * maxCount == end - start + 1) {
                    seen.add(substringKey(hash1, hash2, pow1, pow2, start, end));
                }
            }
        }
        return seen.size();
    }

    /** Hash of s[start..end] under both moduli, packed into one long (each part is < 2^30). */
    private long substringKey(long[] hash1, long[] hash2, long[] pow1, long[] pow2, int start, int end) {
        int length = end - start + 1;
        long h1 = ((hash1[end + 1] - hash1[start] * pow1[length]) % MOD1 + MOD1) % MOD1;
        long h2 = ((hash2[end + 1] - hash2[start] * pow2[length]) % MOD2 + MOD2) % MOD2;
        return (h1 << 32) | h2;
    }

    // V1
    // IDEA: SAME EXPANSION, DEDUPE WITH A SET OF THE SUBSTRINGS THEMSELVES
    //       simplest to explain; costs O(length) per balanced substring instead of O(1)
    /**
     * time = O(N^3) worst case
     * space = O(N^3) worst case
     */
    public int equalDigitFrequency_1(String s) {
        int n = s.length();
        Set<String> seen = new HashSet<>();
        for (int start = 0; start < n; start++) {
            int[] count = new int[10];
            int distinct = 0;
            int maxCount = 0;
            for (int end = start; end < n; end++) {
                int digit = s.charAt(end) - '0';
                if (count[digit] == 0) {
                    distinct++;
                }
                count[digit]++;
                maxCount = Math.max(maxCount, count[digit]);
                if (distinct * maxCount == end - start + 1) {
                    seen.add(s.substring(start, end + 1));
                }
            }
        }
        return seen.size();
    }
}
