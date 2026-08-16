package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/strange-printer/description/
/**
 * 664. Strange Printer
 * Hard
 *
 * There is a strange printer with the following two special properties:
 *
 * The printer can only print a sequence of the same character each time.
 * At each turn, the printer can print new characters starting from and ending at any
 * place and will cover the original existing characters.
 *
 * Given a string s, return the minimum number of turns the printer needed to print it.
 *
 * Example 1:
 *
 * Input: s = "aaabbb"
 * Output: 2
 * Explanation: Print "aaa" first and then print "bbb".
 *
 * Example 2:
 *
 * Input: s = "aba"
 * Output: 2
 * Explanation: Print "aaa" first and then print "b" from the second place of the string,
 * which will cover the existing character 'a'.
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s consists of lowercase English letters.
 *
 */
public class StrangePrinter {

    // V0
    // IDEA: INTERVAL DP
    /**
     *  PRE-STEP: COLLAPSE runs of equal characters ("aaabbb" -> "ab").
     *  A run costs the SAME as a single character, and collapsing shrinks n a lot.
     *
     *  DP def:
     *    - dp[i][j] = minimum turns to print s[i..j]
     *
     *  DP eq (think about the LAST character s[j]):
     *    - worst case: print s[j] on its OWN turn      -> dp[i][j] = dp[i][j-1] + 1
     *    - BETTER: if some k in [i, j-1] has s[k] == s[j], the SAME turn that printed
     *      s[k] can be STRETCHED to cover position j, so s[j] is FREE:
     *        dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j-1])
     *      (dp[k+1][j-1] is 0 when the middle segment is empty)
     *
     *  Iterate i DOWNWARDS so dp[i][k] / dp[k+1][j-1] are already computed.
     *
     *  time  = O(n^3)
     *  space = O(n^2)
     */
    public int strangePrinter(String s) {
        // COLLAPSE consecutive duplicates
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (sb.length() == 0 || sb.charAt(sb.length() - 1) != s.charAt(i)) {
                sb.append(s.charAt(i));
            }
        }

        char[] compact = sb.toString().toCharArray();
        int n = compact.length;
        if (n == 0) {
            return 0;
        }

        int[][] dp = new int[n][n];

        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                // baseline: s[j] needs its OWN turn
                dp[i][j] = dp[i][j - 1] + 1;
                for (int k = i; k < j; k++) {
                    if (compact[k] == compact[j]) {
                        int mid = (k + 1 <= j - 1) ? dp[k + 1][j - 1] : 0;
                        dp[i][j] = Math.min(dp[i][j], dp[i][k] + mid);
                    }
                }
            }
        }

        return dp[0][n - 1];
    }

}
