package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-window-subsequence/description/
// https://leetcode.ca/all/727.html

/**
 *
 * 727. Minimum Window Subsequence
 * Given strings S and T, find the minimum (contiguous) substring W of S, so that T is a subsequence of W.
 *
 * If there is no such window in S that covers all characters in T, return the empty string "". If there are multiple such minimum-length windows, return the one with the left-most starting index.
 *
 * Example 1:
 *
 * Input:
 * S = "abcdebdde", T = "bde"
 * Output: "bcde"
 * Explanation:
 * "bcde" is the answer because it occurs before "bdde" which has the same length.
 * "deb" is not a smaller window because the elements of T in the window must occur in order.
 *
 *
 * Note:
 *
 * All the strings in the input will only contain lowercase letters.
 * The length of S will be in the range [1, 20000].
 * The length of T will be in the range [1, 100].
 *
 *
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon Bloomberg eBay Google Houzz Microsoft
 *
 */

public class MinimumWindowSubsequence {

    // V0
    // TODO : implement
//    public String minWindow(String s1, String s2) {
//    }


    // V0-1
    // IDEA: DP (GPT)
    /**  NOTE !!!
     *
     *  - DP def:
     *      dp[i][j] = the starting index (1-based)
     *      of the `shortest window` in S[0…i-1]
     *      that contains T[0…j-1] as a subsequence.
     *
     *   - DP eq:
     *      For i ≥ 1 and j ≥ 1:
     *
     *     - If S[i−1] == T[j−1]:
     *         - If j == 1: start a new subsequence → dp[i][j] = i
     *         - Else: extend existing subsequence → dp[i][j] = dp[i−1][j−1]
     *
     *     - Else:
     *         - Skip this S char → dp[i][j] = dp[i−1][j]
     *
     */
    public String minWindow_0_1(String S, String T) {
        int m = S.length(), n = T.length();
        int[][] dp = new int[m + 1][n + 1];

        // Build the DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (S.charAt(i - 1) == T.charAt(j - 1)) {
                    if (j == 1) {
                        dp[i][j] = i; // start new subsequence
                    } else {
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        // Find the minimum window
        /**
         * Great — this is the **most important part** of LC 727.
         *
         * Let’s break it down carefully.
         *
         * ---
         *
         * # 🧠 What `dp[i][n]` Means
         *
         * Recall the DP definition:
         *
         * ```
         * dp[i][j] = starting index (1-based) of a window
         *            in S[0..i-1] that contains T[0..j-1] as subsequence
         * ```
         *
         * So:
         *
         * ```
         * dp[i][n]
         * ```
         *
         * means:
         *
         * > The starting index of a window that ends at position `i-1` in S
         * > and contains the ENTIRE string T as a subsequence.
         *
         * If it's `0`, that means:
         * 👉 No valid subsequence ending at `i-1`.
         *
         * ---
         *
         * # 🔎 Now Let’s Analyze the Code
         *
         * ```java
         * int start = 0, minLen = m + 1;
         * ```
         *
         * * `start` → best window starting index
         * * `minLen` → smallest window length found so far
         * * Initialize `minLen` to something large
         *
         * ---
         *
         * ## 🔁 Loop Through Every Ending Position
         *
         * ```java
         * for (int i = 1; i <= m; i++) {
         * ```
         *
         * We check:
         *
         * > "Does a valid subsequence T end at position i-1?"
         *
         * ---
         *
         * ## ✅ Check If T Is Fully Matched Here
         *
         * ```java
         * if (S.charAt(i - 1) == T.charAt(n - 1) && dp[i][n] != 0)
         * ```
         *
         * Two conditions:
         *
         * ### 1️⃣ `S.charAt(i - 1) == T.charAt(n - 1)`
         *
         * We only care about positions where the last character of `T` matches.
         *
         * Why?
         *
         * Because for T to be fully matched as a subsequence,
         * the last character of T must align somewhere.
         *
         * ---
         *
         * ### 2️⃣ `dp[i][n] != 0`
         *
         * This means:
         *
         * > A valid subsequence of full T exists ending at i-1.
         *
         * If it's 0 → no valid window.
         *
         * ---
         *
         * # 📌 Compute Window
         *
         * ```java
         * int windowStart = dp[i][n] - 1;
         * ```
         *
         * Why `-1`?
         *
         * Because:
         *
         * * `dp` stored start as **1-based index**
         * * Java string is **0-based**
         *
         * ---
         *
         * ```java
         * int length = i - windowStart;
         * ```
         *
         * Why this works:
         *
         * * Window ends at index `i-1`
         * * Window starts at `windowStart`
         * * Length = `(i-1 - windowStart + 1)`
         * * Simplifies to:
         *
         *   ```
         *   i - windowStart
         *   ```
         *
         * ---
         *
         * # 🏆 Keep The Minimum Window
         *
         * ```java
         * if (length < minLen) {
         *     minLen = length;
         *     start = windowStart;
         * }
         * ```
         *
         * If this window is shorter → update answer.
         *
         * ---
         *
         * # 🔥 Big Picture
         *
         * The DP table already told us:
         *
         * > "If a subsequence T finishes at position i-1, where did it start?"
         *
         * Now this loop:
         *
         * ✔ Scans all possible ending positions
         * ✔ Computes window length
         * ✔ Picks the shortest one
         *
         * ---
         *
         * # 🧠 Example
         *
         * S = `"abcdebdde"`
         * T = `"bde"`
         *
         * Suppose:
         *
         * ```
         * dp[5][3] = 2
         * ```
         *
         * Meaning:
         *
         * * A full match of "bde" ends at `S[4]`
         * * Window started at index `2` (1-based)
         * * So actual start = `1` (0-based)
         *
         * Window:
         *
         * ```
         * S[1..4] = "bcde"
         * ```
         *
         * Length:
         *
         * ```
         * 5 - 1 = 4
         * ```
         *
         * ---
         *
         * # 🎯 Why We Must Scan All i
         *
         * Because multiple valid windows may exist:
         *
         * ```
         * "abcdebdde"
         * ```
         *
         * Possible windows:
         *
         * * "bcde"
         * * "bdde"
         *
         * We must choose the shortest AND leftmost.
         *
         * ---
         *
         * # ⚡ Summary
         *
         * This block:
         *
         * 1. Checks every possible ending position.
         * 2. Uses DP to find where that window started.
         * 3. Computes window length.
         * 4. Keeps the minimum.
         *
         */
        int start = 0, minLen = m + 1;
        for (int i = 1; i <= m; i++) {
            // If T is fully matched ending at i
            if (S.charAt(i - 1) == T.charAt(n - 1) && dp[i][n] != 0) {
                int windowStart = dp[i][n] - 1; // convert to 0-based index
                int length = i - windowStart;
                if (length < minLen) {
                    minLen = length;
                    start = windowStart;
                }
            }
        }

        return minLen <= m ? S.substring(start, start + minLen) : "";
    }



    // V0-2
    // IDEA: DP (gemini)
    public String minWindow_0_2(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        // dp[i][j] stores the STARTING index of the substring in s1[0...i-1]
        // that contains s2[0...j-1] as a subsequence.
        // We use 1-based indexing for the DP table for convenience.
        int[][] dp = new int[m + 1][n + 1];

        // Base Case: If s2 is empty, any position in s1 can be a "start"
        // We initialize dp[i][0] with the current position (1-indexed)
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i + 1;
        }

        // Fill DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    // If characters match, this subsequence starts wherever
                    // the previous match (j-1) started.
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // If they don't match, we carry over the start index
                    // from the previous character in s1.
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        int start = -1;
        int minLen = Integer.MAX_VALUE;

        // Look at the last column (where all of s2 is matched)
        /**
         * This final loop is the "reaping" phase.
         * After the DP table is filled, it contains the starting
         * positions for all possible valid windows.
         *
         * This loop scans them to find the **best** one.
         *
         * Here is the breakdown of what each line is doing:
         *
         * ### 1. The Strategy: Checking Every "End"
         *
         * ```java
         * for (int i = 1; i <= m; i++) {
         *     if (dp[i][n] > 0) { ... }
         * }
         *
         * ```
         *
         * * **`i`** represents the **end position** in  (where we just finished matching the entire string ).
         * * **`dp[i][n]`** stores the **starting position** for a sequence that ends at `i`.
         * * **`> 0`** simply checks if a valid subsequence was actually found. If it's `0`, it means  couldn't be formed as a subsequence ending at this .
         *
         * ---
         *
         * ### 2. Calculating the Dimensions
         *
         * ```java
         * int currentStart = dp[i][n] - 1; // back to 0-indexed
         * int currentLen = i - currentStart;
         *
         * ```
         *
         * * **`currentStart`**: Because our DP table used -based indexing (to handle the empty base case), we subtract  to get the actual index in the original string .
         * * **`currentLen`**: Since `i` is the end and `currentStart` is the beginning,  gives us the total length of that substring.
         * > *Example: If it starts at index 2 and ends at index 5, the substring is . The length is .*
         *
         *
         *
         * ---
         *
         * ### 3. Finding the "Minimum"
         *
         * ```java
         * if (currentLen < minLen) {
         *     minLen = currentLen;
         *     start = currentStart;
         * }
         *
         * ```
         *
         * * This is a standard "find the minimum" check.
         * * **Crucial Note**: We use `< minLen` rather than `<=`.
         * * **The Leftmost Rule**: The problem says if there are multiple windows of the same length, return the one that starts **earliest**. By only updating when we find a *strictly shorter* length, we naturally keep the first one we encountered (the leftmost one).
         *
         * ---
         *
         * ### 📊 A Quick Visualization
         *
         * Suppose  and .
         *
         * The loop will find several "ends" (`i`) where  is completed:
         *
         * 1. At **index 5** (`...abcde`): Starts at index 1 ("bcde"), **Length 4**.
         * 2. At **index 9** (`...debdde`): Starts at index 5 ("bdde"), **Length 4**.
         *
         * The loop sees the first length  and sets `minLen = 4`. When it sees the second length , the `if (4 < 4)` is **false**, so it keeps the first one.
         *
         */
        for (int i = 1; i <= m; i++) {
            if (dp[i][n] > 0) {
                int currentStart = dp[i][n] - 1; // back to 0-indexed
                int currentLen = i - currentStart;

                if (currentLen < minLen) {
                    minLen = currentLen;
                    start = currentStart;
                }
            }
        }

        return (start == -1) ? "" : s1.substring(start, start + minLen);
    }



    // V1
    // https://leetcode.ca/2017-11-26-727-Minimum-Window-Subsequence/
    // IDEA : DP
    /**
     * time = O(N)
     * space = O(N)
     */
    public String minWindow_1(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] f = new int[m + 1][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    f[i][j] = j == 1 ? i : f[i - 1][j - 1];
                } else {
                    f[i][j] = f[i - 1][j];
                }
            }
        }
        int p = 0, k = m + 1;
        for (int i = 1; i <= m; ++i) {
            if (s1.charAt(i - 1) == s2.charAt(n - 1) && f[i][n] > 0) {
                int j = f[i][n] - 1;
                if (i - j < k) {
                    k = i - j;
                    p = j;
                }
            }
        }
        return k > m ? "" : s1.substring(p, p + k);
    }


    // V2
    // https://github.com/doocs/leetcode/blob/main/solution/0700-0799/0727.Minimum%20Window%20Subsequence/README.md
    /**
     * time = O(N)
     * space = O(N)
     */
    public String minWindow_2(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] f = new int[m + 1][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    f[i][j] = j == 1 ? i : f[i - 1][j - 1];
                } else {
                    f[i][j] = f[i - 1][j];
                }
            }
        }
        int p = 0, k = m + 1;
        for (int i = 1; i <= m; ++i) {
            if (s1.charAt(i - 1) == s2.charAt(n - 1) && f[i][n] > 0) {
                int j = f[i][n] - 1;
                if (i - j < k) {
                    k = i - j;
                    p = j;
                }
            }
        }
        return k > m ? "" : s1.substring(p, p + k);
    }

    // V3
    // https://www.cnblogs.com/grandyang/p/8684817.html
    // IDEA : DP (modified by GPT)
    // TODO : validate below
    /**
     * time = O(N)
     * space = O(N)
     */
    public String minWindow_3_1(String S, String T) {
        int m = S.length();
        int n = T.length();
        int start = -1;
        int minLen = Integer.MAX_VALUE;
        int[][] dp = new int[m + 1][n + 1];

        // Initialize dp array with -1
        for (int[] row : dp) {
            java.util.Arrays.fill(row, -1);
        }

        // Base case: empty T can be matched starting at any index in S
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }

        // Fill the dp array
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= Math.min(i, n); ++j) {
                dp[i][j] = (S.charAt(i - 1) == T.charAt(j - 1)) ? dp[i - 1][j - 1] : dp[i - 1][j];
            }
            if (dp[i][n] != -1) {
                int len = i - dp[i][n];
                if (minLen > len) {
                    minLen = len;
                    start = dp[i][n];
                }
            }
        }

        return (start != -1) ? S.substring(start, start + minLen) : "";
    }

    // V3-1
    // https://www.cnblogs.com/grandyang/p/8684817.html
    // IDEA : 2 POINTERS (modified by GPT)
    // TODO : validate below
    /**
     * time = O(N)
     * space = O(N)
     */
    public String minWindow_3_2(String S, String T) {
        int m = S.length();
        int n = T.length();
        int start = -1;
        int minLen = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;

        while (i < m) {
            if (S.charAt(i) == T.charAt(j)) {
                if (++j == n) {
                    int end = i + 1;
                    while (--j >= 0) {
                        while (i >= 0 && S.charAt(i) != T.charAt(j)) {
                            i--;
                        }
                        i--;
                    }
                    i++;
                    j++;
                    if (end - i < minLen) {
                        minLen = end - i;
                        start = i;
                    }
                }
            }
            i++;
        }

        return (start != -1) ? S.substring(start, start + minLen) : "";
    }



}
