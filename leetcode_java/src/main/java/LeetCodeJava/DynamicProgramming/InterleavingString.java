package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/interleaving-string/
/**
 * 97. Interleaving String
 * Medium
 * Topics
 * Companies
 * Given strings s1, s2, and s3, find whether s3 is formed by an interleaving of s1 and s2.
 *
 * An interleaving of two strings s and t is a configuration where s and t are divided into n and m substrings respectively, such that:
 *
 * s = s1 + s2 + ... + sn
 * t = t1 + t2 + ... + tm
 * |n - m| <= 1
 * The interleaving is s1 + t1 + s2 + t2 + s3 + t3 + ... or t1 + s1 + t2 + s2 + t3 + s3 + ...
 * Note: a + b is the concatenation of strings a and b.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
 * Output: true
 * Explanation: One way to obtain s3 is:
 * Split s1 into s1 = "aa" + "bc" + "c", and s2 into s2 = "dbbc" + "a".
 * Interleaving the two splits, we get "aa" + "dbbc" + "bc" + "a" + "c" = "aadbbcbcac".
 * Since s3 can be obtained by interleaving s1 and s2, we return true.
 * Example 2:
 *
 * Input: s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
 * Output: false
 * Explanation: Notice how it is impossible to interleave s2 with any other string to obtain s3.
 * Example 3:
 *
 * Input: s1 = "", s2 = "", s3 = ""
 * Output: true
 *
 *
 * Constraints:
 *
 * 0 <= s1.length, s2.length <= 100
 * 0 <= s3.length <= 200
 * s1, s2, and s3 consist of lowercase English letters.
 *
 *
 * Follow up: Could you solve it using only O(s2.length) additional memory space?
 *
 *
 */
public class InterleavingString {

    // V0
//    public boolean isInterleave(String s1, String s2, String s3) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/interleaving-string
    // IDEA: RECURSION
    public boolean isInterleave_1_1(String s1, String s2, String s3) {

        return dfs(0, 0, 0, s1, s2, s3);
    }

    private boolean dfs(int i, int j, int k, String s1, String s2, String s3) {
        if (k == s3.length()) {
            return (i == s1.length()) && (j == s2.length());
        }

        if (i < s1.length() && s1.charAt(i) == s3.charAt(k)) {
            if (dfs(i + 1, j, k + 1, s1, s2, s3)) {
                return true;
            }
        }

        if (j < s2.length() && s2.charAt(j) == s3.charAt(k)) {
            if (dfs(i, j + 1, k + 1, s1, s2, s3)) {
                return true;
            }
        }

        return false;
    }

    // V1-2
    // https://neetcode.io/problems/interleaving-string
    // IDEA: Dynamic Programming (Top-Down)
    private Boolean[][] dp;

    public boolean isInterleave_1_2(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;
        dp = new Boolean[m + 1][n + 1];
        return dfs_1_2(0, 0, 0, s1, s2, s3);
    }

    private boolean dfs_1_2(int i, int j, int k, String s1, String s2, String s3) {
        if (k == s3.length()) {
            return (i == s1.length()) && (j == s2.length());
        }
        if (dp[i][j] != null) {
            return dp[i][j];
        }

        boolean res = false;
        if (i < s1.length() && s1.charAt(i) == s3.charAt(k)) {
            res = dfs_1_2(i + 1, j, k + 1, s1, s2, s3);
        }
        if (!res && j < s2.length() && s2.charAt(j) == s3.charAt(k)) {
            res = dfs_1_2(i, j + 1, k + 1, s1, s2, s3);
        }

        dp[i][j] = res;
        return res;
    }


    // V1-3
    // https://neetcode.io/problems/interleaving-string
    // IDEA: Dynamic Programming (Bottom-Up)
    public boolean isInterleave_1_3(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) {
            return false;
        }

        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[m][n] = true;

        for (int i = m; i >= 0; i--) {
            for (int j = n; j >= 0; j--) {
                if (i < m && s1.charAt(i) == s3.charAt(i + j) && dp[i + 1][j]) {
                    dp[i][j] = true;
                }
                if (j < n && s2.charAt(j) == s3.charAt(i + j) && dp[i][j + 1]) {
                    dp[i][j] = true;
                }
            }
        }
        return dp[0][0];
    }



    // V1-4
    // https://neetcode.io/problems/interleaving-string
    // IDEA: Dynamic Programming (Space Optimized)
    public boolean isInterleave_1_4(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;
        if (n < m) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            int tempLength = m;
            m = n;
            n = tempLength;
        }

        boolean[] dp = new boolean[n + 1];
        dp[n] = true;
        for (int i = m; i >= 0; i--) {
            boolean[] nextDp = new boolean[n + 1];
            nextDp[n] = true;
            for (int j = n; j >= 0; j--) {
                if (i < m && s1.charAt(i) == s3.charAt(i + j) && dp[j]) {
                    nextDp[j] = true;
                }
                if (j < n && s2.charAt(j) == s3.charAt(i + j) && nextDp[j + 1]) {
                    nextDp[j] = true;
                }
            }
            dp = nextDp;
        }
        return dp[0];
    }


    // V1-5
    // https://neetcode.io/problems/interleaving-string
    // IDEA:  Dynamic Programming (Optimal)
    public boolean isInterleave_1_5(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        if (m + n != s3.length()) return false;
        if (n < m) {
            String temp = s1;
            s1 = s2;
            s2 = temp;
            int tempLen = m;
            m = n;
            n = tempLen;
        }

        boolean[] dp = new boolean[n + 1];
        dp[n] = true;
        for (int i = m; i >= 0; i--) {
            boolean nextDp = true;
            for (int j = n - 1; j >= 0; j--) {
                boolean res = false;
                if (i < m && s1.charAt(i) == s3.charAt(i + j) && dp[j]) {
                    res = true;
                }
                if (j < n && s2.charAt(j) == s3.charAt(i + j) && nextDp) {
                    res = true;
                }
                dp[j] = res;
                nextDp = dp[j];
            }
        }
        return dp[0];
    }


    // V2
}
