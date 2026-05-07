package LeetCodeJava.String;

// https://leetcode.ca/all/161.html
// https://leetcode.com/problems/one-edit-distance/description/

import java.util.Objects;

/**
 * 161. One Edit Distance
 * Given two strings s and t, determine if they are both one edit distance apart.
 *
 * Note:
 *
 * There are 3 possiblities to satisify one edit distance apart:
 *
 * Insert a character into s to get t
 * Delete a character from s to get t
 * Replace a character of s to get t
 * Example 1:
 *
 * Input: s = "ab", t = "acb"
 * Output: true
 * Explanation: We can insert 'c' into s to get t.
 * Example 2:
 *
 * Input: s = "cab", t = "ad"
 * Output: false
 * Explanation: We cannot get t from s by only one step.
 * Example 3:
 *
 * Input: s = "1203", t = "1213"
 * Output: true
 * Explanation: We can replace '0' with '1' to get t.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Microsoft Snapchat Twitter Uber
 * Problem Solution
 * 161-One-Edit-Distance
 *
 *
 */
public class OneEditDistance {

    // V0
//    public boolean isOneEditDistance(String s, String t) {
//    }



    // V0-1
    // IDEA: 2 POINTERS (fixed by gpt)
    // TODO: validate
    public boolean isOneEditDistance_0_1(String s, String t) {

        // equal strings -> 0 edits
        if (s.equals(t)) {
            return false;
        }

        int len_s = s.length();
        int len_t = t.length();

        // length difference > 1 impossible
        if (Math.abs(len_s - len_t) > 1) {
            return false;
        }

        /** NOTE !!!
         *
         *  we assume `s` is the shorter one,
         *  if NOT, we swap them
         *  e.g. s -> t, t -> s
         */
        // ensure s is shorter (or equal)
        if (len_s > len_t) {
            String tmp = s;
            s = t;
            t = tmp;
        }

        /** NOTE !!!
         *
         *  actually, for `swap handling`, we could do below as well,
         *  which is more elegant and clean
         */
//        if (len_s > len_t) {
//            return isOneEditDistance_0_1(t, s);
//        }


        len_s = s.length();
        len_t = t.length();

        /** NOTE !!!
         *
         *  we setup this boolean flag (isLenEquals),
         *  since, we need to `special handling` if
         *  s, t have same len
         */
        boolean isLenEquals = (len_s == len_t);

        int i = 0;
        int j = 0;

        int op = 0;

        while (i < len_s && j < len_t) {

            if (s.charAt(i) == t.charAt(j)) {
                i++;
                j++;
            } else {

                op++;

                if (op > 1) {
                    return false;
                }

                /** NOTE !!!
                 *
                 *  if s, t have same len,
                 *  we do `replace` op.
                 *  -> move both idx (i, j)
                 */
                // replace
                if (isLenEquals) {
                    i++;
                    j++;
                }

                /** NOTE !!!
                 *
                 *  if s, t have DIFFERENT len,
                 *  we should insert char to the `SHORTER` one
                 */
                // insert into shorter string
                else {
                    j++;
                }
            }
        }

        /** NOTE !!!
         *
         *  still need to handle the `remaining elements`
         *  e.g. leftover chars
         */
        // leftover character
        if (j < len_t || i < len_s) {
            op++;
        }


        /** NOTE !!!
         *
         *  ONLY return true,
         *  if the op is exactly 1
         */
        return op == 1;
    }


    // V0-2
    // IDEA: 2 POINTERS + SUB STRING OP (fixed by gemini)
    // TODO: validate
    public boolean isOneEditDistance_0_2(String s, String t) {
        int ns = s.length();
        int nt = t.length();

        // Ensure s is always the shorter string
        if (ns > nt) {
            return isOneEditDistance_0_2(t, s);
        }

        // If length difference is more than 1, they can't be one edit apart
        if (nt - ns > 1) {
            return false;
        }

        for (int i = 0; i < ns; i++) {
            if (s.charAt(i) != t.charAt(i)) {
                // If lengths are the same, they must be equal after this character (Replace)
                if (ns == nt) {
                    return s.substring(i + 1).equals(t.substring(i + 1));
                } else {
                    // If lengths are different, s must match t starting from the next char of t (Insert)
                    // s.substring(i) because we haven't consumed s.charAt(i) yet
                    return s.substring(i).equals(t.substring(i + 1));
                }
            }
        }

        // If we get here, all characters of s matched the prefix of t.
        // They are one edit apart only if t has exactly one more character at the end.
        return (ns + 1 == nt);
    }


    // V0-3
    // IDEA: DP (GPT)
    // TODO: validate
    boolean isOneEditDistance_0_3(String s, String t) {

        // equal strings -> 0 edits
        if (s.equals(t)) {
            return false;
        }

        int len_s = s.length();
        int len_t = t.length();

        // impossible
        if (Math.abs(len_s - len_t) > 1) {
            return false;
        }

        // optional optimization
        if (len_s > len_t) {
            return isOneEditDistance_0_3(t, s);
        }

        /**
         * dp[i][j]
         * =
         * min edits to convert:
         *
         * s[0...i-1] -> t[0...j-1]
         */
        int[][] dp = new int[len_s + 1][len_t + 1];

        // init
        for (int i = 0; i <= len_s; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len_t; j++) {
            dp[0][j] = j;
        }

        // build dp
        for (int i = 1; i <= len_s; i++) {

            for (int j = 1; j <= len_t; j++) {

                // same char
                if (s.charAt(i - 1) == t.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    /**  DP eq:
                     *
                     * | Operation | Previous Subproblem |
                     * | --------- | ------------------- |
                     * | Insert    | dp[i][j-1]          |
                     * | Delete    | dp[i-1][j]          |
                     * | Replace   | dp[i-1][j-1]        |
                     *
                     */

                    // insert
                    /** NOTE !!  INSERT
                     *
                     * ## Visualization
                     *
                     * Example:
                     *
                     * ```text id="i8pv7d"
                     * s = "ab"
                     * t = "acb"
                     * ```
                     *
                     * Suppose:
                     *
                     * ```
                     * i = 2
                     * j = 3
                     * ```
                     *
                     * We're computing:
                     *
                     * ```
                     * "ab" -> "acb"
                     * ```
                     *
                     * Insert interpretation:
                     *
                     * First solve:
                     *
                     * ```
                     * "ab" -> "ac"
                     * ```
                     *
                     * which is:
                     *
                     * ```
                     * dp[2][2]
                     * ```
                     *
                     * Then insert:
                     *
                     * ```
                     * 'b'
                     * ```
                     *
                     * So:
                     *
                     * ```
                     * dp[2][3] = dp[2][2] + 1
                     *
                     */
                    int insert = dp[i][j - 1] + 1;

                    // delete
                    /**  NOTE !!! DELETE
                     *
                     *  ## Visualization
                     *
                     *   ```
                     *    "abc" -> "ac"
                     *    ```
                     *
                     * Delete:  'b'
                     *
                     * First solve:
                     *
                     *      ```
                     *      "ab" -> "ac"
                     *       ```
                     *
                     * then delete final char.
                     *
                     *
                     */
                    int delete = dp[i - 1][j] + 1;

                    // replace
                    /**  NOTE !!!   REPLACE
                     *
                     *  ## Visualization
                     *
                     *   ```
                     *   "abc" -> "adc"
                     *   ```
                     *
                     *  At mismatch:
                     *
                     *    'b' vs 'd'
                     *
                     * First solve:
                     *
                     *     "a" -> "a"
                     *
                     * then replace:
                     *
                     *     b -> d
                     *
                     *
                     */
                    int replace = dp[i - 1][j - 1] + 1;

                    dp[i][j] = Math.min(insert,
                            Math.min(delete, replace));
                }
            }
        }

        return dp[len_s][len_t] == 1;
    }


    // V0-4
    // IDEA: DP (GEMINI)
    // TODO: validate
    public boolean isOneEditDistance_0_4(String s, String t) {
        int ns = s.length();
        int nt = t.length();

        // Optimization: If length difference > 1, impossible to be 1 edit apart
        if (Math.abs(ns - nt) > 1) {
            return false;
        }

        // dp[i][j] = min edits between s[0...i-1] and t[0...j-1]
        int[][] dp = new int[ns + 1][nt + 1];

        // Initialization: distance from empty string
        for (int i = 0; i <= ns; i++) dp[i][0] = i;
        for (int j = 0; j <= nt; j++) dp[0][j] = j;

        for (int i = 1; i <= ns; i++) {
            for (int j = 1; j <= nt; j++) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    // Characters match, no operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Characters differ, choose min of:
                    // 1. Replace: dp[i-1][j-1]
                    // 2. Delete from S: dp[i-1][j]
                    // 3. Insert into S (Delete from T): dp[i][j-1]
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        // We return true ONLY if the final distance is exactly 1
        return dp[ns][nt] == 1;
    }


    // V1-1
    // IDEA:
    // https://leetcode.ca/2016-05-09-161-One-Edit-Distance/
    // best solution ha!
    public boolean isOneEditDistance_1_1(String s, String t) {
        for (int i = 0; i < Math.min(s.length(), t.length()); ++i) {
            if (s.charAt(i) != t.charAt(i)) {
                if (s.length() == t.length()) {
                    return Objects.equals(s.substring(i + 1), t.substring(i + 1));
                }
                if (s.length() < t.length()) {
                    return Objects.equals(s.substring(i), t.substring(i + 1));
                } else {
                    return Objects.equals(s.substring(i + 1), t.substring(i));
                }
            }
        }

        // case: abc, abcdefg
        return Math.abs((int)s.length() - (int)t.length()) == 1;

    }


    // V1-2
    // IDEA: DP
    // https://leetcode.ca/2016-05-09-161-One-Edit-Distance/
    public boolean isOneEditDistance_1_2(String s, String t) {
        if (s.length() < t.length()) return isOneEditDistance_1_2(t, s); // so 't' is always the shorter one in below logic
        int m = s.length(), n = t.length(), diff = m - n;
        if (diff >= 2) return false;
        else if (diff == 1) {
            for (int i = 0; i < n; ++i) {
                if (s.charAt(i) != t.charAt(i)) {
                    return s.substring(i + 1).equals(t.substring(i));
                }
            }
            return true; // meaning, diff is happening at the final char
        } else {
            int cnt = 0;
            for (int i = 0; i < m; ++i) {
                if (s.charAt(i) != t.charAt(i)) ++cnt;
            }
            return cnt == 1;
        }
    }



    // V1-3
    // IDEA:
    // https://leetcode.ca/2016-05-09-161-One-Edit-Distance/
    public boolean isOneEditDistance_1_3(String s, String t) {
        int m = s.length(), n = t.length();
        if (m < n) {
            return isOneEditDistance_1_3(t, s);
        }
        if (m - n > 1) {
            return false;
        }
        for (int i = 0; i < n; ++i) {
            if (s.charAt(i) != t.charAt(i)) {
                if (m == n) {
                    return s.substring(i + 1).equals(t.substring(i + 1));
                }
                return s.substring(i + 1).equals(t.substring(i));
            }
        }
        return m == n + 1;
    }




    // V2





}
