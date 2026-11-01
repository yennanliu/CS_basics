package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-separate-numbers/description/
/**
 * 1977. Number of Ways to Separate Numbers
 * Hard
 *
 * You wrote down many positive integers in a string called num. However, you realized that
 * you forgot to add commas to seperate the different numbers. You remember that the list of
 * integers was non-decreasing and that no integer had leading zeros.
 *
 * Return the number of possible lists of integers that you could have written down to get
 * the string num. Since the answer may be large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: num = "327"
 * Output: 2
 * Explanation: You could have written down the numbers:
 * 3, 27
 * 327
 *
 * Example 2:
 *
 * Input: num = "094"
 * Output: 0
 * Explanation: No numbers can have leading zeros and all numbers must be positive.
 *
 * Example 3:
 *
 * Input: num = "0"
 * Output: 0
 * Explanation: No numbers can have leading zeros and all numbers must be positive.
 *
 *
 * Constraints:
 *
 * 1 <= num.length <= 3500
 * num consists of digits '0' through '9'.
 *
 */
public class NumberOfWaysToSeparateNumbers {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP + PREFIX SUM OVER LAST-NUMBER LENGTH + LCP TABLE
    /**
     *  DP def:
     *     - dp[i][j] = ways to split num[0..i-1] where the LAST number has length <= j
     *                  (it is already a prefix sum along j)
     *
     *  DP eq: let exact = ways where the last number has length EXACTLY j (starts at i-j)
     *     - num[i-j] == '0'                          -> exact = 0 (leading zero)
     *     - previous number has length k <= j:
     *         k < j  : always fine (a shorter number is strictly smaller)
     *         k == j : only if num[i-2j .. i-j-1] <= num[i-j .. i-1]
     *       exact = dp[i-j][j]                  if the same-length compare passes
     *             = dp[i-j][min(j-1, i-j)]      otherwise
     *     - dp[i][j] = dp[i][j-1] + exact
     *
     *  lcp[a][b] = longest common prefix of num[a:] and num[b:], built backwards,
     *  so two equal-length blocks compare in O(1).
     *
     *  Init: dp[0][0] = 1 (empty prefix).  Answer: dp[n][n].
     *
     *  time = O(N^2)
     *  space = O(N^2)
     */
    public int numberOfCombinations(String num) {
        int n = num.length();
        if (num.charAt(0) == '0') {
            return 0;
        }

        // lcp[a][b]: length of the common prefix of num[a:] and num[b:]
        int[][] lcp = new int[n + 1][n + 1];
        for (int a = n - 1; a >= 0; a--) {
            for (int b = n - 1; b >= 0; b--) {
                if (num.charAt(a) == num.charAt(b)) {
                    lcp[a][b] = lcp[a + 1][b + 1] + 1;
                }
            }
        }

        int[][] dp = new int[n + 1][n + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= i; j++) {
                int start = i - j;  // the last number is num[start .. i-1]
                long exact = 0;
                if (num.charAt(start) != '0') {
                    int prevStart = start - j;
                    if (prevStart >= 0 && isGreaterOrEqual(num, lcp, start, prevStart, j)) {
                        // previous number may also have length j
                        exact = dp[start][j];
                    } else {
                        // previous number must be strictly shorter
                        exact = dp[start][Math.min(j - 1, start)];
                    }
                }
                dp[i][j] = (int) ((dp[i][j - 1] + exact) % MOD);
            }
        }
        return dp[n][n];
    }

    /** Is num[first .. first+len-1] >= num[second .. second+len-1] ? */
    private boolean isGreaterOrEqual(String num, int[][] lcp, int first, int second, int len) {
        int common = lcp[first][second];
        if (common >= len) {
            return true;  // the two blocks are equal
        }
        // the first differing digit decides
        return num.charAt(first + common) >= num.charAt(second + common);
    }
}
