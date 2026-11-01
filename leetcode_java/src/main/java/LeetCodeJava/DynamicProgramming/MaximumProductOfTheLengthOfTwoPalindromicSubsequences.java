package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-product-of-the-length-of-two-palindromic-subsequences/description/
/**
 * 2002. Maximum Product of the Length of Two Palindromic Subsequences
 * Medium
 *
 * Given a string s, find two disjoint palindromic subsequences of s such that the product
 * of their lengths is maximized. The two subsequences are disjoint if they do not both
 * pick a character at the same index.
 *
 * Return the maximum possible product of the lengths of the two palindromic subsequences.
 *
 * A subsequence is a string that can be derived from another string by deleting some or
 * no characters without changing the order of the remaining characters. A string is
 * palindromic if it reads the same forward and backward.
 *
 *
 * Example 1:
 *
 * Input: s = "leetcodecom"
 * Output: 9
 * Explanation: An optimal solution is to choose "ete" for the 1st subsequence and "cdc"
 * for the 2nd subsequence. The product of their lengths is: 3 * 3 = 9.
 *
 * Example 2:
 *
 * Input: s = "bb"
 * Output: 1
 * Explanation: An optimal solution is to choose "b" (the first character) for the 1st
 * subsequence and "b" (the second character) for the 2nd subsequence.
 * The product of their lengths is: 1 * 1 = 1.
 *
 * Example 3:
 *
 * Input: s = "accbcaxxcxx"
 * Output: 25
 * Explanation: An optimal solution is to choose "accca" for the 1st subsequence and
 * "xxcxx" for the 2nd subsequence. The product of their lengths is: 5 * 5 = 25.
 *
 *
 * Constraints:
 *
 * 2 <= s.length <= 12
 * s consists of lowercase English letters only.
 *
 */
public class MaximumProductOfTheLengthOfTwoPalindromicSubsequences {

    // V0
    // IDEA: BITMASK ENUMERATION + SUBMASK ENUMERATION (n <= 12, so 2^n masks is tiny)
    /**
     *  step 1: for every mask of indices, build the picked subsequence and record
     *          isPalindrome[mask] and length[mask] (= number of set bits).
     *  step 2: for every palindromic mask a, walk every SUBMASK b of its complement
     *          (two subsequences are disjoint exactly when their masks share no bit)
     *          and keep the best length[a] * length[b] over palindromic b.
     *
     *  Walking submasks of every complement costs sum 2^(n - popcount) = 3^n total.
     *
     *  time = O(3^N + 2^N * N)
     *  space = O(2^N)
     */
    public int maxProduct(String s) {
        int n = s.length();
        int totalMasks = 1 << n;
        int fullMask = totalMasks - 1;

        boolean[] isPalindrome = new boolean[totalMasks];
        int[] length = new int[totalMasks];

        // step 1: classify every mask
        for (int mask = 1; mask < totalMasks; mask++) {
            StringBuilder picked = new StringBuilder();
            for (int i = 0; i < n; i++) {
                // bit i set -> index i belongs to this subsequence
                if (((mask >> i) & 1) == 1) {
                    picked.append(s.charAt(i));
                }
            }
            length[mask] = picked.length();
            isPalindrome[mask] = isPalindrome(picked);
        }

        // step 2: pair each palindromic mask with palindromic submasks of its complement
        int best = 0;
        for (int first = 1; first < totalMasks; first++) {
            if (!isPalindrome[first]) {
                continue;
            }
            int complement = fullMask ^ first;
            int second = complement;
            while (second > 0) {
                if (isPalindrome[second]) {
                    best = Math.max(best, length[first] * length[second]);
                }
                // (second - 1) & complement = the next smaller submask of complement
                second = (second - 1) & complement;
            }
        }
        return best;
    }

    /** Returns true when the characters read the same forwards and backwards. */
    private boolean isPalindrome(StringBuilder text) {
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            if (text.charAt(left) != text.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
