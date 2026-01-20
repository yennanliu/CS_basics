package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/edit-distance/description/
/**
 * 72. Edit Distance
 * Solved
 * Medium
 * Topics
 * Companies
 * Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.
 *
 * You have the following three operations permitted on a word:
 *
 * Insert a character
 * Delete a character
 * Replace a character
 *
 *
 * Example 1:
 *
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 * Explanation:
 * horse -> rorse (replace 'h' with 'r')
 * rorse -> rose (remove 'r')
 * rose -> ros (remove 'e')
 * Example 2:
 *
 * Input: word1 = "intention", word2 = "execution"
 * Output: 5
 * Explanation:
 * intention -> inention (remove 't')
 * inention -> enention (replace 'i' with 'e')
 * enention -> exention (replace 'n' with 'x')
 * exention -> exection (replace 'n' with 'c')
 * exection -> execution (insert 'u')
 *
 *
 * Constraints:
 *
 * 0 <= word1.length, word2.length <= 500
 * word1 and word2 consist of lowercase English letters.
 *
 *
 */
public class EditDistance {

    // V0
//    public int minDistance(String word1, String word2) {
//    }


    // V0-1
    // IDEA: 2D DP (gemini)
    /**  NOTE !!! Core idea:
     *
     * Following the **"Two-String Grid"** pattern we just discussed, **LeetCode 72: Edit Distance** is the most comprehensive example of this logic. In this problem, we want the minimum number of operations (insert, delete, or replace) to transform `word1` into `word2`.
     *
     * ### 💡 The DP Logic
     *
     * 1. **State Definition**: `dp[i][j]` is the minimum operations to convert `word1.substring(0, i)` to `word2.substring(0, j)`.
     * 2. **Base Cases (The Edges)**:
     * * If `word1` is empty (`i=0`), we must **insert** all characters of `word2`. So, `dp[0][j] = j`.
     * * If `word2` is empty (`j=0`), we must **delete** all characters of `word1`. So, `dp[i][0] = i`.
     *
     *
     * 3. **Transition**:
     * * **If characters match (`word1[i-1] == word2[j-1]`)**:
     * No operation is needed. We take the diagonal value.
     * `dp[i][j] = dp[i-1][j-1]`
     * * **If characters don't match**:
     * We must pick the best (minimum) of the three possible operations and add 1:
     * 1. **Replace**: `dp[i-1][j-1] + 1` (Diagonal)
     * 2. **Delete**: `dp[i-1][j] + 1` (Top)
     * 3. **Insert**: `dp[i][j-1] + 1` (Left)
     *
     * ---
     *
     * ### 🔍 Why this fits the pattern
     *
     * * **Grid Size**: `(m+1) x (n+1)` to handle empty string prefixes.
     * * **Initialization**: The first row and column are sequential numbers representing "all deletes" or "all inserts."
     * * **Neighbors**: Every cell `dp[i][j]` looks at exactly three neighbors (Top, Left, Diagonal).
     *
     * ### 📊 Comparing Formula Logic
     *
     * | Problem | Move if Characters Match | Move if Characters Mismatch |
     * | --- | --- | --- |
     * | **LCS** | `Diagonal + 1` | `Max(Top, Left)` |
     * | **Edit Distance** | `Diagonal` (No cost) | `1 + Min(Top, Left, Diagonal)` |
     *
     */
    public int minDistance_0_1(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        /**  NOTE !!!
         *
         *  DP def:
         *
         *   dp[i][j]
         *      - the `minimum` operations to convert
         *        `word1.substring(0, i) `
         *        to
         *        `word2.substring(0, j).`
         *
         */
        // 2D grid: Rows = word1, Cols = word2
        int[][] dp = new int[m + 1][n + 1];

        /**  NOTE !!! DP base
         *
         *  - If word1 is empty (i=0),
         *    we must insert all characters of word2. So, dp[0][j] = j.
         *
         *   - If word2 is empty (j=0),
         *    we must delete all characters of word1. So, dp[i][0] = i.
         *
         */
        // Base case: word2 is empty (Delete all from word1)
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Base case: word1 is empty (Insert all into word1)
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }


        /**  NOTE !!!
         *
         *  DP eq:
         *
         *   - If characters match (word1[i-1] == word2[j-1]):
         *         - No operation is needed. We take the diagonal value.
         *           dp[i][j] = dp[i-1][j-1]
         *
         *   - If characters don't match: We must pick the best (minimum) o
         *     f the three possible operations and add 1:
         *
         *       - Replace: dp[i-1][j-1] + 1 (Diagonal)
         *
         *       - Delete: dp[i-1][j] + 1 (Top)
         *
         *       -  Insert: dp[i][j-1] + 1 (Left)
         *
         */
        // Fill the grid
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // Match: Just take the previous best
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Mismatch: 1 + min of (Replace, Delete, Insert)
                    int replace = dp[i - 1][j - 1];
                    int delete = dp[i - 1][j];
                    int insert = dp[i][j - 1];

                    dp[i][j] = 1 + Math.min(replace, Math.min(delete, insert));
                }
            }
        }

        return dp[m][n];
    }

    // V0-2
    // IDEA: 2D DP + SPACE OPTIMIZED (gemini)
    public int minDistance_0_2(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[] dp = new int[n + 1];
        for (int j = 0; j <= n; j++)
            dp[j] = j;

        for (int i = 1; i <= m; i++) {
            int prev = dp[0]; // This represents dp[i-1][j-1]
            dp[0] = i; // This represents dp[i][0]
            for (int j = 1; j <= n; j++) {
                int temp = dp[j]; // Store original dp[i-1][j] for next j's diagonal
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[j] = prev;
                } else {
                    dp[j] = 1 + Math.min(prev, Math.min(dp[j - 1], dp[j]));
                }
                prev = temp;
            }
        }
        return dp[n];
    }

    // V0-3
    // IDEA: 2D DP (gpt)
    public int minDistance_0_3(String word1, String word2) {
        int n1 = word1.length();
        int n2 = word2.length();

        // dp[i][j] = edit distance converting first i chars of word1
        // to first j chars of word2
        int[][] dp = new int[n1 + 1][n2 + 1];

        // base cases
        for (int i = 0; i <= n1; i++) {
            dp[i][0] = i; // delete all chars in word1
        }
        for (int j = 0; j <= n2; j++) {
            dp[0][j] = j; // insert all chars of word2
        }

        for (int i = 1; i <= n1; i++) {
            for (int j = 1; j <= n2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int delete = dp[i - 1][j];
                    int insert = dp[i][j - 1];
                    int replace = dp[i - 1][j - 1];
                    dp[i][j] = 1 + Math.min(delete, Math.min(insert, replace));
                }
            }
        }

        return dp[n1][n2];
    }




    // V0-5
    // IDEA: 2D DP (gpt)
    /**
     *
     * This is best solved using Dynamic Programming (DP).
     *
     * We define a DP table dp[i][j] as the minimum number of
     * operations to convert word1[0..i-1] to word2[0..j-1].
     *
     * Recurrence Relation:
     * If word1[i-1] == word2[j-1]:
     * → No operation needed. dp[i][j] = dp[i-1][j-1]
     *
     * Else, choose the minimum of:
     *
     * Insert: dp[i][j-1] + 1
     *
     * Delete: dp[i-1][j] + 1
     *
     * Replace: dp[i-1][j-1] + 1
     *
     * Base Cases:
     * dp[0][j] = j → Convert empty string to prefix of word2
     *
     * dp[i][0] = i → Convert prefix of word1 to empty string
     *
     */
    public int minDistance_0_5(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();

        // DP array: (m+1) x (n+1)
        int[][] dp = new int[m + 1][n + 1];

        // Base case initialization
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Deleting all characters
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Inserting all characters
        }

        // Bottom-up DP
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    // No operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Insert, Delete, Replace
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1], // Replace
                            Math.min(dp[i - 1][j], // Delete
                                    dp[i][j - 1]) // Insert
                    );
                }
            }
        }

        return dp[m][n];
    }


    // V1-1
    // https://neetcode.io/problems/edit-distance
    // IDEA: RECURSION
    public int minDistance_1_1(String word1, String word2) {
        int m = word1.length(), n = word2.length();

        return dfs(0, 0, word1, word2, m, n);
    }

    private int dfs(int i, int j, String word1, String word2, int m, int n) {
        if (i == m) return n - j;
        if (j == n) return m - i;

        if (word1.charAt(i) == word2.charAt(j)) {
            return dfs(i + 1, j + 1, word1, word2, m, n);
        }

        int res = Math.min(dfs(i + 1, j, word1, word2, m, n),
                dfs(i, j + 1, word1, word2, m, n));
        res = Math.min(res, dfs(i + 1, j + 1, word1, word2, m, n));
        return res + 1;
    }

    // V1-2
    // https://neetcode.io/problems/edit-distance
    // IDEA:  Dynamic Programming (Top-Down)
    private int[][] dp;
    public int minDistance_1_2(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return dfs_1_2(0, 0, word1, word2, m, n);
    }

    private int dfs_1_2(int i, int j, String word1, String word2, int m, int n) {
        if (i == m) return n - j;
        if (j == n) return m - i;
        if (dp[i][j] != -1) return dp[i][j];

        if (word1.charAt(i) == word2.charAt(j)) {
            dp[i][j] = dfs_1_2(i + 1, j + 1, word1, word2, m, n);
        } else {
            int res = Math.min(dfs(i + 1, j, word1, word2, m, n),
                    dfs_1_2(i, j + 1, word1, word2, m, n));
            res = Math.min(res, dfs_1_2(i + 1, j + 1, word1, word2, m, n));
            dp[i][j] = res + 1;
        }
        return dp[i][j];
    }

    // V1-3
    // https://neetcode.io/problems/edit-distance
    // IDEA: Dynamic Programming (Bottom-Up)
    public int minDistance_1_3(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int j = 0; j <= word2.length(); j++) {
            dp[word1.length()][j] = word2.length() - j;
        }
        for (int i = 0; i <= word1.length(); i++) {
            dp[i][word2.length()] = word1.length() - i;
        }

        for (int i = word1.length() - 1; i >= 0; i--) {
            for (int j = word2.length() - 1; j >= 0; j--) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[i][j] = dp[i + 1][j + 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i + 1][j],
                            Math.min(dp[i][j + 1], dp[i + 1][j + 1]));
                }
            }
        }
        return dp[0][0];
    }

    // V1-4
    // https://neetcode.io/problems/edit-distance
    // IDEA: Dynamic Programming (Space Optimized)
    public int minDistance_1_4(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        if (m < n) {
            int temp = m;
            m = n;
            n = temp;
            String t = word1;
            word1 = word2;
            word2 = t;
        }

        int[] dp = new int[n + 1];
        int[] nextDp = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            dp[j] = n - j;
        }

        for (int i = m - 1; i >= 0; i--) {
            nextDp[n] = m - i;
            for (int j = n - 1; j >= 0; j--) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    nextDp[j] = dp[j + 1];
                } else {
                    nextDp[j] = 1 + Math.min(dp[j],
                            Math.min(nextDp[j + 1], dp[j + 1]));
                }
            }
            System.arraycopy(nextDp, 0, dp, 0, n + 1);
        }

        return dp[0];
    }

    // V1-5
    // https://neetcode.io/problems/edit-distance
    // IDEA: Dynamic Programming (Optimal)
    public int minDistance_1_5(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        if (m < n) {
            String temp = word1; word1 = word2; word2 = temp;
            m = word1.length(); n = word2.length();
        }

        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) dp[i] = n - i;

        for (int i = m - 1; i >= 0; i--) {
            int nextDp = dp[n];
            dp[n] = m - i;
            for (int j = n - 1; j >= 0; j--) {
                int temp = dp[j];
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[j] = nextDp;
                } else {
                    dp[j] = 1 + Math.min(dp[j],
                            Math.min(dp[j + 1], nextDp));
                }
                nextDp = temp;
            }
        }
        return dp[0];
    }


    // V2
    // https://leetcode.ca/2016-02-10-72-Edit-Distance/
    // IDEA: DP
    public int minDistance_2(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] f = new int[m + 1][n + 1];
        for (int j = 1; j <= n; ++j) {
            f[0][j] = j;
        }
        for (int i = 1; i <= m; ++i) {
            f[i][0] = i;
            for (int j = 1; j <= n; ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    f[i][j] = f[i - 1][j - 1];
                } else {
                    f[i][j] = Math.min(f[i - 1][j], Math.min(f[i][j - 1], f[i - 1][j - 1])) + 1;
                }
            }
        }
        return f[m][n];
    }



}
