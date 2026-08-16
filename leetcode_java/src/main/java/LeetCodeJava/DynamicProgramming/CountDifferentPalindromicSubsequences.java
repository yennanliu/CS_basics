package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-different-palindromic-subsequences/description/
/**
 * 730. Count Different Palindromic Subsequences
 * Hard
 *
 * Given a string s, return the number of different non-empty palindromic subsequences
 * in s. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * A subsequence of a string is obtained by deleting zero or more characters from the
 * string.
 *
 * A sequence is palindromic if it is equal to the sequence reversed.
 *
 * Two sequences a1, a2, ... and b1, b2, ... are different if there is some i for which
 * ai != bi.
 *
 * Example 1:
 *
 * Input: s = "bccb"
 * Output: 6
 * Explanation: The 6 different non-empty palindromic subsequences are 'b', 'c', 'bb',
 * 'cc', 'bcb', 'bccb'.
 * Note that 'bcb' is counted only once, even though it occurs twice.
 *
 * Example 2:
 *
 * Input: s = "abcdabcdabcdabcdabcdabcdabcdabcddcbadcbadcbadcbadcbadcbadcbadcba"
 * Output: 104860361
 * Explanation: There are 3104860382 different non-empty palindromic subsequences, which
 * is 104860361 modulo 10^9 + 7.
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either 'a', 'b', 'c', or 'd'.
 *
 */
public class CountDifferentPalindromicSubsequences {

    // V0
    // IDEA: INTERVAL DP
    /**
     *   DP def:
     *     - dp[i][j] = number of DISTINCT non-empty palindromic subsequences in s[i..j]
     *
     *   DP eq:
     *     - s[i] != s[j]:
     *         dp[i][j] = dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]   (INCLUSION-EXCLUSION)
     *
     *     - s[i] == s[j]: let lo / hi be the FIRST / LAST index strictly inside (i, j)
     *       holding that SAME character.
     *         no such char (lo > hi) : dp[i][j] = 2*dp[i+1][j-1] + 2
     *             -> every inner palindrome can be WRAPPED by s[i]..s[j],
     *                plus "c" and "cc"
     *         exactly one (lo == hi) : dp[i][j] = 2*dp[i+1][j-1] + 1
     *             -> "c" is ALREADY counted by the inner part, only "cc" is new
     *         two or more            : dp[i][j] = 2*dp[i+1][j-1] - dp[lo+1][hi-1]
     *             -> SUBTRACT the palindromes double counted between the two copies
     *
     *   NOTE !!! the subtractions can drive an intermediate value NEGATIVE under the
     *            modulo -> normalise with `((x % MOD) + MOD) % MOD`.
     *
     *   time  = O(n^2 * 4)  (the lo/hi scan is bounded by the 4-letter alphabet)
     *   space = O(n^2)
     */
    public int countPalindromicSubsequences(String s) {
        final long MOD = 1_000_000_007L;
        int n = s.length();
        if (n == 0) {
            return 0;
        }

        long[][] dp = new long[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1; // a single char is one palindrome
        }

        // grow by INTERVAL LENGTH so dp[i+1][j-1] etc. are already known
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i + length - 1 < n; i++) {
                int j = i + length - 1;

                if (s.charAt(i) != s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j] + dp[i][j - 1] - dp[i + 1][j - 1];
                } else {
                    // find the innermost pair of the SAME char inside (i, j)
                    int lo = i + 1;
                    int hi = j - 1;
                    while (lo <= hi && s.charAt(lo) != s.charAt(i)) {
                        lo += 1;
                    }
                    while (lo <= hi && s.charAt(hi) != s.charAt(i)) {
                        hi -= 1;
                    }

                    if (lo > hi) {
                        dp[i][j] = 2 * dp[i + 1][j - 1] + 2;
                    } else if (lo == hi) {
                        dp[i][j] = 2 * dp[i + 1][j - 1] + 1;
                    } else {
                        dp[i][j] = 2 * dp[i + 1][j - 1] - dp[lo + 1][hi - 1];
                    }
                }

                dp[i][j] = ((dp[i][j] % MOD) + MOD) % MOD;
            }
        }

        return (int) dp[0][n - 1];
    }

}
