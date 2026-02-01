package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/interleaving-string/
// https://leetcode.cn/problems/interleaving-string/
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

    // V0-1
    // IDEA: 2D DP (gemini)
    /**
     *
     * To solve **LeetCode 97: Interleaving String**,
     * we use 2D Dynamic Programming.
     * The goal is to determine if a string
     * is formed by interleaving  and
     * while maintaining the relative order of characters from each.
     *
     * ### 💡 The DP Logic
     *
     * 1. **State Definition:** `dp[i][j]` is a boolean value representing whether  can be formed by interleaving  and .
     * 2. **Base Case:** `dp[0][0] = true` (two empty strings interleave to form an empty string).
     * 3. **Transition:** * If we take a character from : `dp[i][j]` is true if `dp[i-1][j]` was true AND .
     * * If we take a character from : `dp[i][j]` is true if `dp[i][j-1]` was true AND .
     *
     * ---
     *
     * ### 📊 Complexity Analysis
     *
     * * **Time Complexity:**  where  and  are lengths of  and .
     * * **Space Complexity:**  for the 2D table. (Note: This can be optimized to  space since we only ever need the previous row).
     *
     *
     */
    public boolean isInterleave_0_1(String s1, String s2, String s3) {
        int m = s1.length(), n = s2.length();
        // If lengths don't match, it's impossible
        if (m + n != s3.length())
            return false;

        /**  NOTE !!!
         *
         *  DP def:
         *
         *  dp[i][j]:
         *    boolean value representing whether `s_3[0.... i+j-1]`
         *    can be formed by `interleaving`
         *       s1[0... i-1]
         *       and
         *       s2[0... j-1]
         */
        boolean[][] dp = new boolean[m + 1][n + 1];
        /**  NOTE !!!
         *
         *   base case:
         *
         *    dp[0][0] = true (two empty strings interleave to form an empty string).
         */
        dp[0][0] = true;

        // 1. Initialize first column (using only s1)
        /**  NOTE !!!
         *
         *   why `dp[i - 1][0] ` ?
         *
         * 1. dp[i - 1][0] (The Previous Link)
         *    The string up until the previous character
         *    must have already been a valid interleave.
         *
         *    -> If we couldn't even form the first 3 characters,
         *       we certainly can't form the first 4.
         *       This preserves the relative order.
         *
         */
        for (int i = 1; i <= m; i++) {
            dp[i][0] = dp[i - 1][0] && s1.charAt(i - 1) == s3.charAt(i - 1);
        }

        // 2. Initialize first row (using only s2)
        for (int j = 1; j <= n; j++) {
            dp[0][j] = dp[0][j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        /**  NOTE !!!
         *
         *   DP eq:
         *
         *      Transition:
         *          - If we take a character from s1:
         *             dp[i][j] is true if dp[i-1][j] was true
         *             AND
         *             s_1[i-1] == s_3[i+j-1].
         *
         *          - If we take a character from s_2:
         *              - dp[i][j] is true if dp[i][j-1] was true
         *                 AND
         *                 s_2[j-1] == s_3[i+j-1].
         *
         *
         *   ---------------------
         *
         *   Explanation:
         *
         *    - Think of the DP table as a grid where you are trying
         *      to find a path from the
         *      top-left (0,0) to
         *      the bottom-right (m,n).
         *
         *   - Moving Down (from dp[i-1][j]): You are choosing to use a character from s1.
         *       - You can ONLY do this if the character at s1[i-1]
         *         matches the required character in s3.
         *
         *   - Moving Right (from dp[i][j-1]): You are choosing to use a character from s2.
         *       - You can ONLY do this if the character at s2[j-1]
         *         matches the required character in s3.
         *
         */
        // 3. Fill the rest of the table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Check if current s3 char matches s1's current char
                // AND the previous state was valid
                boolean fromS1 = dp[i - 1][j] && s1.charAt(i - 1) == s3.charAt(i + j - 1);

                // Check if current s3 char matches s2's current char
                // AND the previous state was valid
                boolean fromS2 = dp[i][j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1);

                dp[i][j] = fromS1 || fromS2;
            }
        }

        return dp[m][n];
    }

    // V0-2
    // IDEA: DP (GPT)
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isInterleave_0_2(String s1, String s2, String s3) {

        int n1 = s1.length(), n2 = s2.length(), n3 = s3.length();

        // If total lengths don't match, can't interleave
        if (n1 + n2 != n3) {
            return false;
        }

        // dp[i][j] = using s1[0..i-1] and s2[0..j-1] to match s3[0..i+j-1]
        boolean[][] dp = new boolean[n1 + 1][n2 + 1];

        dp[0][0] = true; // empty string interleaves to empty

        // Only s1 prefix builds s3 prefix
        for (int i = 1; i <= n1; i++) {
            dp[i][0] = dp[i - 1][0] && s1.charAt(i - 1) == s3.charAt(i - 1);
        }

        // Only s2 prefix builds s3 prefix
        for (int j = 1; j <= n2; j++) {
            dp[0][j] = dp[0][j - 1] && s2.charAt(j - 1) == s3.charAt(j - 1);
        }

        // Fill DP
        for (int i = 1; i <= n1; i++) {
            for (int j = 1; j <= n2; j++) {
                dp[i][j] = (dp[i - 1][j] && s1.charAt(i - 1) == s3.charAt(i + j - 1))
                        || (dp[i][j - 1] && s2.charAt(j - 1) == s3.charAt(i + j - 1));
            }
        }

        return dp[n1][n2];
    }


    // V1-1
    // https://neetcode.io/problems/interleaving-string
    // IDEA: RECURSION
    /**
     * time = O(N)
     * space = O(N)
     */
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

    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
