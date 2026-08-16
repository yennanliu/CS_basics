package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-different-palindromic-subsequences/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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


    // V1
    // IDEA: 3D DP SPLIT BY THE OUTER CHARACTER
    /**
     *  dp[c][i][j] = distinct palindromic subsequences of s[i..j] that START AND
     *  END with character c. The four families are disjoint, so the answer is
     *  simply their sum -- no inclusion-exclusion at all.
     *
     *      s[i] != c            -> dp[c][i][j] = dp[c][i+1][j]
     *      s[j] != c            -> dp[c][i][j] = dp[c][i][j-1]
     *      s[i] == s[j] == c    -> dp[c][i][j] = 2 + sum over d of dp[d][i+1][j-1]
     *                              ("c", "cc", and every inner palindrome wrapped)
     *
     *  NOTE !!! this avoids V0's subtractions entirely, so no intermediate value
     *           can go negative under the modulo -- the usual source of bugs here.
     *
     *  time  = O(4 * n^2)
     *  space = O(4 * n^2)
     */
    public int countPalindromicSubsequences_1(String s) {
        final long MOD = 1_000_000_007L;
        int n = s.length();
        if (n == 0) {
            return 0;
        }

        long[][][] dp = new long[4][n][n];
        for (int i = 0; i < n; i++) {
            dp[s.charAt(i) - 'a'][i][i] = 1;
        }

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                for (int c = 0; c < 4; c++) {
                    char ch = (char) ('a' + c);
                    if (s.charAt(i) != ch) {
                        dp[c][i][j] = dp[c][i + 1][j];
                    } else if (s.charAt(j) != ch) {
                        dp[c][i][j] = dp[c][i][j - 1];
                    } else {
                        long inner = 0;
                        if (i + 1 <= j - 1) {
                            for (int d = 0; d < 4; d++) {
                                inner += dp[d][i + 1][j - 1];
                            }
                        }
                        dp[c][i][j] = (2 + inner) % MOD;
                    }
                    dp[c][i][j] %= MOD;
                }
            }
        }

        long total = 0;
        for (int c = 0; c < 4; c++) {
            total += dp[c][0][n - 1];
        }
        return (int) (total % MOD);
    }

    // V2
    // IDEA: MEMOISED RECURSION with the same interval recurrence
    /**
     *  Top-down version of V0: the three `s[i] == s[j]` sub-cases read as branches
     *  rather than as table writes, and only the intervals actually reachable get
     *  computed.
     *
     *  time  = O(n^2 * 4)
     *  space = O(n^2)
     */
    private Long[][] memoPal;
    private String sPal;

    public int countPalindromicSubsequences_2(String s) {
        sPal = s;
        memoPal = new Long[s.length()][s.length()];
        return (int) countPal(0, s.length() - 1);
    }

    private long countPal(int i, int j) {
        final long MOD = 1_000_000_007L;
        if (i > j) {
            return 0;
        }
        if (i == j) {
            return 1;
        }
        if (memoPal[i][j] != null) {
            return memoPal[i][j];
        }

        long res;
        if (sPal.charAt(i) != sPal.charAt(j)) {
            res = countPal(i + 1, j) + countPal(i, j - 1) - countPal(i + 1, j - 1);
        } else {
            int lo = i + 1;
            int hi = j - 1;
            while (lo <= hi && sPal.charAt(lo) != sPal.charAt(i)) {
                lo += 1;
            }
            while (lo <= hi && sPal.charAt(hi) != sPal.charAt(i)) {
                hi -= 1;
            }
            if (lo > hi) {
                res = 2 * countPal(i + 1, j - 1) + 2;
            } else if (lo == hi) {
                res = 2 * countPal(i + 1, j - 1) + 1;
            } else {
                res = 2 * countPal(i + 1, j - 1) - countPal(lo + 1, hi - 1);
            }
        }

        res = ((res % MOD) + MOD) % MOD;
        memoPal[i][j] = res;
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE -- enumerate the subsequences into a set (tiny n)
    /**
     *  All 2^n subsequences, keep the palindromic ones, de-duplicate.
     *
     *  Only runs for n <= ~20, but it counts exactly the objects the statement
     *  describes -- the oracle for the two interval recurrences.
     *
     *  time  = O(2^n * n)
     *  space = O(2^n * n)
     */
    public int countPalindromicSubsequences_3(String s) {
        int n = s.length();
        Set<String> seen = new HashSet<>();
        for (int mask = 1; mask < (1 << n); mask++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    sb.append(s.charAt(i));
                }
            }
            String t = sb.toString();
            if (t.equals(sb.reverse().toString())) {
                seen.add(t);
            }
        }
        return (int) (seen.size() % 1_000_000_007L);
    }

}
