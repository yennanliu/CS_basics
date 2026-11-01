package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-palindromic-subsequences/description/

/**
 * 2484. Count Palindromic Subsequences
 * Hard
 *
 * Given a string of digits s, return the number of palindromic subsequences of s having length 5. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Note:
 *
 * A string is palindromic if it reads the same forward and backward.
 * A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.
 *
 *
 * Example 1:
 *
 * Input: s = "103301"
 * Output: 2
 * Explanation:
 * There are 6 possible subsequences of length 5: "10330","10331","10301","10301","13301","03301".
 * Two of them (both equal to "10301") are palindromic.
 *
 * Example 2:
 *
 * Input: s = "0000000"
 * Output: 21
 * Explanation: All 21 subsequences are "00000", which is palindromic.
 *
 * Example 3:
 *
 * Input: s = "9999900000"
 * Output: 2
 * Explanation: The only two palindromic subsequences are "99999" and "00000".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^4
 * s consists of digits.
 *
 */
public class CountPalindromicSubsequences {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: FIX THE MIDDLE CHAR, COUNT "ab" PAIRS LEFT x "ba" PAIRS RIGHT (10 x 10 TABLES, INCREMENTAL)
    /**
     * time = O(100 * N)
     * space = O(100)
     */
    public int countPalindromes(String s) {
        int n = s.length();
        int[] digits = new int[n];
        for (int i = 0; i < n; i++) {
            digits[i] = s.charAt(i) - '0';
        }

        // suffix side: suffixPairs[x][y] = number of pairs "x then y" in the suffix region.
        // start with the whole string and peel characters off its left end.
        long[] suffixCount = new long[10];
        long[][] suffixPairs = new long[10][10];
        for (int d : digits) {
            for (int x = 0; x < 10; x++) {
                suffixPairs[x][d] += suffixCount[x];
            }
            suffixCount[d]++;
        }

        // prefix side: prefixPairs[x][y] = number of pairs "x then y" in s[0 .. m-1]
        long[] prefixCount = new long[10];
        long[][] prefixPairs = new long[10][10];

        long result = 0;
        for (int m = 0; m < n; m++) {
            int center = digits[m];

            // s[m] leaves the suffix region (it is the centre now): remove the
            // pairs where it was the first element
            suffixCount[center]--;
            for (int y = 0; y < 10; y++) {
                suffixPairs[center][y] -= suffixCount[y];
            }

            // palindrome a b [center] b a: left needs "ab", right needs "ba"
            for (int a = 0; a < 10; a++) {
                for (int b = 0; b < 10; b++) {
                    long left = prefixPairs[a][b] % MOD;
                    long right = suffixPairs[b][a] % MOD;
                    result = (result + left * right) % MOD;
                }
            }

            // s[m] joins the prefix region for the next centre
            for (int x = 0; x < 10; x++) {
                prefixPairs[x][center] += prefixCount[x];
            }
            prefixCount[center]++;
        }
        return (int) result;
    }
}
