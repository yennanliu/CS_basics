package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-palindromic-subsequence/description/

import java.util.Arrays;

/**
 * 516. Longest Palindromic Subsequence
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a string s, find the longest palindromic subsequence's length in s.
 *
 * A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "bbbab"
 * Output: 4
 * Explanation: One possible longest palindromic subsequence is "bbbb".
 * Example 2:
 *
 * Input: s = "cbbd"
 * Output: 2
 * Explanation: One possible longest palindromic subsequence is "bb".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists only of lowercase English letters.
 *
 *
 */
public class LongestPalindromicSubsequence {

    // V0
    // IDEA: 2D DP (gemini)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        // dp[i][j] = longest palindromic subsequence in s[i...j]
        int[][] dp = new int[n][n];

        /** NOTE !!!
         *
         *  base case: dp[i][i] = 1;
         */
        // Base case: every single character is a palindrome of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        /** NOTE !!!
         *
         *   i, j does backwards, forwards separately
         *
         *   (i: left boundary)
         *   (j: right boundary)
         *
         *   - Loop i backwards (start of the substring)
         *   - Loop j forwards (end of the substring)
         */
        // Loop i backwards (start of the substring)
        for (int i = n - 1; i >= 0; i--) {
            // Loop j forwards (end of the substring)
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    // If characters match, add 2 to the result of the inner substring
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    // If they don't match, take the max by skipping either i or j
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        /** NOTE !!!
         *
         *  final answer is `dp[0][n - 1]`
         *  (from idx=0 to idx=`n-1`)
         */
        // The answer is the LPS of the entire string from index 0 to n-1
        return dp[0][n - 1];
    }

    // V0-1
    // IDEA: 2D DP (gemini)
    /**  NOTE !!! core idea
     *
     * - DP def:
     *
     *   dp[i][j]:
     *     - the length of the `longest` palindromic subsequence
     *     in the substring s[i...j].
     *
     * - DP eq:
     *      If `s[i] == s[j]`:
     *      The two characters match.
     *      They add 2 to the length of the longest palindromic
     *      subsequence found between them.
     *         - `dp[i][j] = dp[i+1][j-1] + 2`

     *      If `s[i] != s[j]`:
     *      The characters don't match.
     *      We have two choices: either ignore `s[i]` or ignore `s[j]`.
     *      We take the maximum of these two possibilities.
     *          - `dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1])`
     */
    public int longestPalindromeSubseq_0_1(String s) {
        int n = s.length();

        /** NOTE !!!
         *
         *  DP def:
         *
         *   - the length of the `longest` palindromic subsequence
         *     in the substring s[i...j].
         */
        // dp[i][j] stores the LPS length for s[i...j]
        int[][] dp = new int[n][n];

        // Iterate from the end to the beginning for the starting index
        /** NOTE !!! //  Loop i backwards (start of the substring)  */
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1; // Base case: single character

            /** NOTE !!!
             *
             *  DP eq:
             *
             *   1. if s[i] == s[j]
             *       // NOTE !!!
             *       // if left, right pointer matched,
             *       //   -> inner sequence + 2
             *      - dp[i][j] = dp[i+1][j-1] + 2
             *
             *  2. else:
             *      // left, right pointer NOT matched,
             *      //  -> either skipping i or skipping j
             *      - dp[i][j] = max(dp[i+1][j], dp[i][j-1])
             */
            /** NOTE !!! Loop j forwards (end of the substring) */
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    // Match found: inner sequence + 2
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    // No match: best of either skipping i or skipping j
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        // The answer is the LPS for the entire string
        return dp[0][n - 1];
    }

    // V0-2
    // IDEA: 1D DP (gemini)
    /**  IDEA:
     *
     * Since dp[i] ONLY depends on the `previous row dp[i+1],`
     *  -> we can compress the 2D array into a 1D array.
     *  -> We use a variable pre to keep track of
     *  -> the dp[i+1][j-1] value (the "diagonal" value in the 2D table).
     *
     *
     *  - DP def:
     *
     *  `dp[j]` represents the **length of the Longest Palindromic Subsequence (LPS)
     *      * for the substring starting at the current  and ending at **.
     *
     *
     *  - DP eq:
     *
     *       * #### **Case 1: `s[i] == s[j]` (Match)**
     *      *
     *      * If the characters at the current start  and current end  match, we take the LPS of the inner substring () and add 2.
     *      *
     *      * * **2D Logic:**
     *      * * **1D Logic:** `dp[j] = pre + 2`
     *      * * *Note:* `pre` was saved from the previous iteration of  to represent .
     *      *
     *      *
     *      *
     *      * #### **Case 2: `s[i] != s[j]` (No Match)**
     *      *
     *      * If they don't match, we take the maximum of skipping either the character at  or the character at .
     *      *
     *      * * **2D Logic:**
     *      * * **1D Logic:** `dp[j] = Math.max(dp[j], dp[j - 1])`
     *      * * `dp[j]` (before update) still holds the value for .
     *      * * `dp[j-1]` (already updated in this loop) holds the value for .
     *      *
     *
     *
     *
     *
     *  ---------
     *
     *
     *  In 1D Dynamic Programming, the goal is to "compress"
     *  a 2D table into a single array to save memory.
     *  In the context of **LC 516 (Longest Palindromic Subsequence)**,
     *  we do this because to calculate the current row , we only ever need data from the row we just finished ().
     *
     * ---
     *
     * ### 1. DP Definition
     *
     * In the 1D version, `dp[j]` represents the **length of the Longest Palindromic Subsequence (LPS)
     * for the substring starting at the current  and ending at **.
     *
     * As we iterate  backwards from  down to :
     *
     * * **The Array:** `dp` stores the results from the *previous*  (specifically, ).
     * * **The Variable `pre`:** This is a single integer that stores the "diagonal" value from the 2D table (). This is necessary because once we update `dp[j]`, the old value (which represented ) is lost.
     *
     * ---
     *
     * ### 2. DP Transition
     *
     * The 1D transition mimics the 2D logic but uses the updated values in the array strategically.
     *
     * #### **Case 1: `s[i] == s[j]` (Match)**
     *
     * If the characters at the current start  and current end  match, we take the LPS of the inner substring () and add 2.
     *
     * * **2D Logic:**
     * * **1D Logic:** `dp[j] = pre + 2`
     * * *Note:* `pre` was saved from the previous iteration of  to represent .
     *
     *
     *
     * #### **Case 2: `s[i] != s[j]` (No Match)**
     *
     * If they don't match, we take the maximum of skipping either the character at  or the character at .
     *
     * * **2D Logic:**
     * * **1D Logic:** `dp[j] = Math.max(dp[j], dp[j - 1])`
     * * `dp[j]` (before update) still holds the value for .
     * * `dp[j-1]` (already updated in this loop) holds the value for .
     *
     *
     *
     * ---
     *
     * ### 📊 Logic Mapping Table
     *
     * | 2D Element | 1D Equivalent | Explanation |
     * | --- | --- | --- |
     * |  | `dp[j]` (new) | The result for the current range we are solving. |
     * |  | `dp[j]` (old) | The value currently sitting in the array before we overwrite it. |
     * |  | `dp[j-1]` | The value we just calculated in the current loop for the previous . |
     * |  | `pre` | The value of `dp[j]` from the *previous*  iteration before it was updated. |
     *
     * ---
     *
     * ### ⚡ Why use 1D DP?
     *
     * In **LC 516**, if the string length :
     *
     * * **2D DP:**  integers ( MB).
     * * **1D DP:**  integers ( KB).
     * As  grows, the space savings become massive, preventing `OutOfMemoryError` on large test cases.
     *
     *
     */
    public int longestPalindromeSubseq_0_2(String s) {
        int n = s.length();
        int[] dp = new int[n];

        for (int i = n - 1; i >= 0; i--) {
            dp[i] = 1;
            int pre = 0; // Represents dp[i+1][j-1]
            for (int j = i + 1; j < n; j++) {
                int temp = dp[j];
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = pre + 2;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                pre = temp;
            }
        }
        return dp[n - 1];
    }


    // V0-3
    // IDEA: 2D DP (GPT)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindromeSubseq_0_3(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();
        int[][] dp = new int[n][n];

        // Base case: single character
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        // Fill the DP table
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = 2 + dp[i + 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[0][n - 1];
    }


    // V1
    // IDEA: DP
    // https://leetcode.ca/2017-04-29-516-Longest-Palindromic-Subsequence/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindromeSubseq_1(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; ++i) {
            dp[i][i] = 1;
        }
        for (int j = 1; j < n; ++j) {
            for (int i = j - 1; i >= 0; --i) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/longest-palindromic-subsequence/solutions/3414715/easy-solutions-in-java-python-and-c-look-fl62/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindromeSubseq_2(String s) {
        // Get the length of the input string
        int n = s.length();
        // Initialize a 2D array to store the length of the longest palindromic subsequence
        int[][] dp = new int[n][n];

        // Iterate over the substrings in reverse order to fill in the dp table bottom-up
        for (int i = n - 1; i >= 0; i--) {
            // Base case: the longest palindromic subsequence of a single character is 1
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                // If the two characters match, the longest palindromic subsequence can be extended by two
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = 2 + dp[i + 1][j - 1];
                } else {
                    // Otherwise, we take the maximum of the two possible substrings
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        // The length of the longest palindromic subsequence is in the top-right corner of the dp table
        return dp[0][n - 1];
    }


    // V3
    // IDEA: ARRAY OP
    // https://leetcode.com/problems/longest-palindromic-subsequence/solutions/3414839/java-easy-image-explaination-dp-beginner-r69e/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindromeSubseq_3(String s) {
        /* If the two ends of a string are the same, then they must be included in the longest palindrome subsequence. Otherwise, both ends cannot be included in the longest palindrome subsequence. */

        int size = s.length();
        if (size == 0 || s == null)
            return 0;
        int[][] dp = new int[size][size];
        for (int[] row : dp)
            Arrays.fill(row, -1);
        return helper(s, 0, s.length() - 1, dp);
    }



    private int helper(String s, int left, int right, int[][] cache) {
        if (left == right)
            return 1;

        if (left > right)
            return 0;

        if (cache[left][right] != -1)
            return cache[left][right];

        if (s.charAt(left) == s.charAt(right)) {
            cache[left][right] = 2 + helper(s, left + 1, right - 1, cache);
            return cache[left][right];
        }

        int leftIncrement = helper(s, left + 1, right, cache);
        int rightIncrement = helper(s, left, right - 1, cache);
        cache[left][right] = Math.max(leftIncrement, rightIncrement);
        return cache[left][right];

    }

    // V4-1
    // IDEA:
    // https://leetcode.com/problems/longest-palindromic-subsequence/


    // V4-2
    // IDEA:
    // https://leetcode.com/problems/longest-palindromic-subsequence/

    // V4-3
    // IDEA:
    // https://leetcode.com/problems/longest-palindromic-subsequence/

    // V4-4
    // IDEA: Bottom Up Tabulation with space optimization
    // https://leetcode.com/problems/longest-palindromic-subsequence/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int longestPalindromeSubseq_4_4(String s) {
        int[] dp = new int[s.length()];
        for (int i = s.length() - 1; i >= 0; i--) {
            dp[i] = 1;
            int pre = 0;
            for (int j = i + 1; j < s.length(); j++) {
                int tmp = dp[j];
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = pre + 2;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                pre = tmp;
            }
        }

        return dp[s.length() - 1];
    }



}
