package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/find-the-longest-substring-containing-vowels-in-even-counts/description/

import java.util.*;

/**
 * 1371. Find the Longest Substring Containing Vowels in Even Counts
 * Medium
 *
 * Given the string s, return the size of the longest substring containing each vowel an even
 * number of times. That is, 'a', 'e', 'i', 'o', and 'u' must appear an even number of times.
 *
 *
 * Example 1:
 *
 * Input: s = "eleetminicoworoep"
 * Output: 13
 * Explanation: The longest substring is "leetminicowor" which contains two each of the vowels:
 * e, i and o and zero of the vowels: a and u.
 *
 * Example 2:
 *
 * Input: s = "leetcodeisgreat"
 * Output: 5
 * Explanation: The longest substring is "leetc" which contains two e's.
 *
 * Example 3:
 *
 * Input: s = "bcbcbc"
 * Output: 6
 * Explanation: In this case, the given string "bcbcbc" is the longest because all vowels:
 * a, e, i, o and u appear zero times.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 5 x 10^5
 * s contains only lowercase English letters.
 *
 */
public class FindTheLongestSubstringContainingVowelsInEvenCounts {

    // V0
    // IDEA: PREFIX XOR MASK + FIRST-SEEN INDEX OF EACH MASK
    //       only the PARITY of each vowel count matters -> a 5-bit mask (one bit per vowel)
    //       toggled with XOR while scanning.
    //       s[j+1..i] has every vowel an even number of times <=> mask(i) == mask(j),
    //       so for each mask only its FIRST index matters: it gives the longest window.
    //       NOTE: mask 0 is "seen" at index -1, so a valid prefix measures i + 1.
    /**
     * time = O(N)
     * space = O(1)   (32 masks)
     */
    public int findTheLongestSubstring(String s) {
        final String VOWELS = "aeiou";

        // firstIndex[mask] = first index where the prefix had this parity mask
        int[] firstIndex = new int[32];
        Arrays.fill(firstIndex, Integer.MIN_VALUE);   // MIN_VALUE = not seen yet
        firstIndex[0] = -1;

        int mask = 0;
        int longest = 0;
        for (int i = 0; i < s.length(); i++) {
            int vowelBit = VOWELS.indexOf(s.charAt(i));
            if (vowelBit >= 0) {
                // flip this vowel's parity
                mask ^= 1 << vowelBit;
            }
            if (firstIndex[mask] == Integer.MIN_VALUE) {
                firstIndex[mask] = i;
            } else {
                longest = Math.max(longest, i - firstIndex[mask]);
            }
        }
        return longest;
    }
}
