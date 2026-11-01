package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-product-of-the-length-of-two-palindromic-substrings/description/
/**
 * 1960. Maximum Product of the Length of Two Palindromic Substrings
 * Hard
 *
 * You are given a 0-indexed string s and are tasked with finding two non-intersecting
 * palindromic substrings of odd length such that the product of their lengths is maximized.
 *
 * More formally, you want to choose four integers i, j, k, l such that
 * 0 <= i <= j < k <= l < s.length and both the substrings s[i...j] and s[k...l] are
 * palindromes and have odd lengths. s[i...j] denotes a substring from index i to index j
 * inclusive.
 *
 * Return the maximum possible product of the lengths of the two non-intersecting
 * palindromic substrings.
 *
 * A palindrome is a string that is the same forward and backward. A substring is a
 * contiguous sequence of characters in a string.
 *
 *
 * Example 1:
 *
 * Input: s = "ababbb"
 * Output: 9
 * Explanation: Substrings "aba" and "bbb" are palindromes with odd length. product = 3 * 3 = 9.
 *
 * Example 2:
 *
 * Input: s = "zaaaxbbby"
 * Output: 9
 * Explanation: Substrings "aaa" and "bbb" are palindromes with odd length. product = 3 * 3 = 9.
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 10^5
 * s consists of lowercase English letters.
 *
 */
public class MaximumProductOfTheLengthOfTwoPalindromicSubstrings {

    // V0
    // IDEA: MANACHER (ODD CENTERS) + PREFIX / SUFFIX BEST PALINDROME
    /**
     *  1) Manacher: radius[i] = radius of the longest odd palindrome centered at i,
     *     so it has length 2 * radius[i] + 1 and spans [i - radius[i], i + radius[i]].
     *
     *  2) bestEndingAt[j]   = longest odd palindrome ending exactly at j
     *     bestStartingAt[j] = longest odd palindrome starting exactly at j
     *     Seed them from every center, then SHRINK: a palindrome of length L ending at j
     *     trims to one of length L - 2 ending at j - 1 (and symmetrically for starts).
     *     Then a running max turns "ending at j" into "fully inside s[0..j]".
     *
     *     NOTE !!! the shrink pass must run BEFORE the running-max pass, otherwise a
     *              long palindrome never propagates to the shorter windows.
     *
     *  3) Answer = max over cuts i of prefixBest[i - 1] * suffixBest[i].
     *
     *  time = O(N)
     *  space = O(N)
     */
    public long maxProduct(String s) {
        int n = s.length();
        int[] radius = manacherOdd(s);

        long[] prefixBest = new long[n];
        long[] suffixBest = new long[n];
        for (int i = 0; i < n; i++) {
            long length = 2L * radius[i] + 1;
            int end = i + radius[i];
            int start = i - radius[i];
            prefixBest[end] = Math.max(prefixBest[end], length);
            suffixBest[start] = Math.max(suffixBest[start], length);
        }

        // shrink: a palindrome ending at j+1 of length L gives one ending at j of length L-2
        for (int j = n - 2; j >= 0; j--) {
            prefixBest[j] = Math.max(prefixBest[j], prefixBest[j + 1] - 2);
        }
        // shrink: a palindrome starting at j-1 of length L gives one starting at j of length L-2
        for (int j = 1; j < n; j++) {
            suffixBest[j] = Math.max(suffixBest[j], suffixBest[j - 1] - 2);
        }

        // running max: "ending at j" -> "anywhere inside s[0..j]"
        for (int j = 1; j < n; j++) {
            prefixBest[j] = Math.max(prefixBest[j], prefixBest[j - 1]);
        }
        // running max: "starting at j" -> "anywhere inside s[j..n-1]"
        for (int j = n - 2; j >= 0; j--) {
            suffixBest[j] = Math.max(suffixBest[j], suffixBest[j + 1]);
        }

        // the left palindrome lives in s[0..cut-1], the right one in s[cut..n-1]
        long best = 0;
        for (int cut = 1; cut < n; cut++) {
            best = Math.max(best, prefixBest[cut - 1] * suffixBest[cut]);
        }
        return best;
    }

    /** Manacher on odd centers: radius[i] of the longest odd palindrome centered at i. */
    private int[] manacherOdd(String s) {
        int n = s.length();
        int[] radius = new int[n];
        // [center - radius[center], rightEdge] is the right-most palindrome found so far
        int center = 0;
        int rightEdge = 0;
        for (int i = 0; i < n; i++) {
            if (i < rightEdge) {
                // the mirror of i around center is already known; it is valid up to rightEdge
                int mirror = 2 * center - i;
                radius[i] = Math.min(rightEdge - i, radius[mirror]);
            }
            // try to extend beyond what the mirror guarantees
            while (i - radius[i] - 1 >= 0
                    && i + radius[i] + 1 < n
                    && s.charAt(i - radius[i] - 1) == s.charAt(i + radius[i] + 1)) {
                radius[i]++;
            }
            if (i + radius[i] > rightEdge) {
                center = i;
                rightEdge = i + radius[i];
            }
        }
        return radius;
    }
}
