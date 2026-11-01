package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/find-longest-awesome-substring/description/

import java.util.*;

/**
 * 1542. Find Longest Awesome Substring
 * Hard
 *
 * You are given a string s. An awesome substring is a non-empty substring of s such that we can make any number of swaps in order to make it a palindrome.
 *
 * Return the length of the maximum length awesome substring of s.
 *
 *
 * Example 1:
 *
 * Input: s = "3242415"
 * Output: 5
 * Explanation: "24241" is the longest awesome substring, we can form the palindrome "24142" with some swaps.
 *
 * Example 2:
 *
 * Input: s = "12345678"
 * Output: 1
 *
 * Example 3:
 *
 * Input: s = "213123"
 * Output: 6
 * Explanation: "213123" is the longest awesome substring, we can form the palindrome "231132" with some swaps.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists only of digits.
 *
 */
public class FindLongestAwesomeSubstring {

    // V0
    // IDEA: PREFIX PARITY BITMASK + FIRST INDEX OF EACH MASK
    //       a substring can be rearranged into a palindrome iff AT MOST ONE digit occurs an
    //       odd number of times. prefix mask bit d = parity of digit d so far, and
    //       s[j+1..i] has parity mask(i) ^ mask(j). it is awesome iff that xor has 0 or 1
    //       bits set: mask(j) == mask(i), or mask(j) == mask(i) with one bit d flipped.
    //       keep the FIRST index of every mask and test those 11 candidates at each i.
    //       NOTE: mask 0 is seen at index -1 (the empty prefix).
    /**
     * time = O(10 * N)
     * space = O(2^10)
     */
    public int longestAwesome(String s) {
        final int MASKS = 1 << 10;

        // firstIndex[mask] = first prefix index with this parity mask
        int[] firstIndex = new int[MASKS];
        Arrays.fill(firstIndex, Integer.MIN_VALUE);   // MIN_VALUE = not seen yet
        firstIndex[0] = -1;

        int mask = 0;
        int longest = 0;
        for (int i = 0; i < s.length(); i++) {
            mask ^= 1 << (s.charAt(i) - '0');

            // every digit even inside the substring
            if (firstIndex[mask] == Integer.MIN_VALUE) {
                firstIndex[mask] = i;
            } else {
                longest = Math.max(longest, i - firstIndex[mask]);
            }

            // exactly digit d odd inside the substring
            for (int d = 0; d < 10; d++) {
                int other = mask ^ (1 << d);
                if (firstIndex[other] != Integer.MIN_VALUE) {
                    longest = Math.max(longest, i - firstIndex[other]);
                }
            }
        }
        return longest;
    }
}
