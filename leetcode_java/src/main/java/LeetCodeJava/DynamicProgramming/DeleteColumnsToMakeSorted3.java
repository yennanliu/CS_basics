package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/delete-columns-to-make-sorted-iii/description/

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * 960. Delete Columns to Make Sorted III
 * Hard
 *
 * You are given an array of n strings strs, all of the same length.
 *
 * We may choose any deletion indices, and we delete all the characters in those indices
 * for each string.
 *
 * For example, if we have strs = ["abcdef","uvwxyz"] and deletion indices {0, 2, 3},
 * then the final array after deletions is ["bef", "vyz"].
 *
 * Suppose we chose a set of deletion indices answer such that after deletions, the final
 * array has every string (row) in lexicographic order. (i.e., (strs[0][0] <= strs[0][1]
 * <= ... <= strs[0][strs[0].length - 1]), and (strs[1][0] <= strs[1][1] <= ... <=
 * strs[1][strs[1].length - 1]), and so on).
 *
 * Return the minimum possible value of answer.length.
 *
 * Example 1:
 *
 * Input: strs = ["babca","bbazb"]
 * Output: 3
 * Explanation: After deleting columns 0, 1, and 4, the final array is
 * strs = ["bc", "az"].
 * Both these rows are individually in lexicographic order
 * (ie. strs[0][0] <= strs[0][1] and strs[1][0] <= strs[1][1]).
 * Note that strs[0] > strs[1] - the array strs is not necessarily in lexicographic order.
 *
 * Example 2:
 *
 * Input: strs = ["edcba"]
 * Output: 4
 * Explanation: If we delete less than 4 columns, the only row will not be
 * lexicographically sorted.
 *
 * Example 3:
 *
 * Input: strs = ["ghi","def","abc"]
 * Output: 0
 * Explanation: All rows are already lexicographically sorted.
 *
 * Constraints:
 *
 * n == strs.length
 * 1 <= n <= 100
 * 1 <= strs[i].length <= 100
 * strs[i] consists of lowercase English letters.
 *
 */
public class DeleteColumnsToMakeSorted3 {

    // V0
    // IDEA: LONGEST INCREASING SUBSEQUENCE over COLUMNS
    /**
     *  - Deleting the FEWEST columns == KEEPING the MOST columns.
     *  - A set of kept columns is valid iff, reading them left to right,
     *    EVERY row is non-decreasing.
     *  - So this is just LIS where `column i can follow column j` means
     *    strs[r][j] <= strs[r][i] for EVERY row r.
     *
     *  DP def:
     *     - dp[i] = length of the longest valid column subsequence ending at column i
     *  DP eq:
     *     - dp[i] = max(dp[i], dp[j] + 1) for all j < i with all rows non-decreasing
     *
     *  answer = m - max(dp), where m = number of columns
     *
     *  time  = O(m^2 * n), m = strs[0].length, n = strs.length
     *  space = O(m)
     */
    public int minDeletionSize(String[] strs) {
        int m = strs[0].length();

        int[] dp = new int[m];
        Arrays.fill(dp, 1);

        for (int i = 1; i < m; i++) {
            for (int j = 0; j < i; j++) {
                /** NOTE !!!
                 *
                 *  column j may precede column i only if NO row breaks order
                 *  -> a single violating row disqualifies the pair
                 */
                boolean ok = true;
                for (String s : strs) {
                    if (s.charAt(j) > s.charAt(i)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }

        int best = 0;
        for (int v : dp) {
            best = Math.max(best, v);
        }
        return m - best;
    }


    // V1
    // IDEA: PRECOMPUTE THE `column j may precede column i` MATRIX
    /**
     *  V0 re-scans all n rows inside the inner LIS loop. Building an m x m
     *  compatibility matrix ONCE hoists that scan out, so the LIS itself becomes a
     *  plain O(m^2) table walk.
     *
     *  Same total complexity but the row scan happens m^2/2 times instead of inside
     *  every relaxation -- and the matrix is inspectable.
     *
     *  time  = O(m^2 * n)
     *  space = O(m^2)
     */
    public int minDeletionSize_1(String[] strs) {
        int m = strs[0].length();
        boolean[][] canFollow = new boolean[m][m];

        for (int j = 0; j < m; j++) {
            for (int i = j + 1; i < m; i++) {
                boolean ok = true;
                for (String s : strs) {
                    if (s.charAt(j) > s.charAt(i)) {
                        ok = false;
                        break;
                    }
                }
                canFollow[j][i] = ok;
            }
        }

        int[] dp = new int[m];
        Arrays.fill(dp, 1);
        int best = 1;
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < i; j++) {
                if (canFollow[j][i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            best = Math.max(best, dp[i]);
        }
        return m - best;
    }

    // V2
    // IDEA: TOP-DOWN MEMOISED LIS
    /**
     *  keep(i) = the longest valid column subsequence STARTING at column i.
     *
     *  The recursion makes the `which columns may follow i` question explicit, and
     *  columns that can never start a useful chain are never expanded.
     *
     *  time  = O(m^2 * n)
     *  space = O(m)
     */
    private Integer[] memoCol;

    public int minDeletionSize_2(String[] strs) {
        int m = strs[0].length();
        memoCol = new Integer[m];
        int best = 0;
        for (int i = 0; i < m; i++) {
            best = Math.max(best, keepFrom(strs, i, m));
        }
        return m - best;
    }

    private int keepFrom(String[] strs, int i, int m) {
        if (memoCol[i] != null) {
            return memoCol[i];
        }
        int best = 1;
        for (int nxt = i + 1; nxt < m; nxt++) {
            boolean ok = true;
            for (String s : strs) {
                if (s.charAt(i) > s.charAt(nxt)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                best = Math.max(best, 1 + keepFrom(strs, nxt, m));
            }
        }
        memoCol[i] = best;
        return best;
    }

    // V3
    // IDEA: BRUTE FORCE over the KEPT column subsets
    /**
     *  Enumerate every subset of columns and check whether all rows stay sorted.
     *
     *  O(2^m * m * n), so it only runs for m <= ~20, but it makes no LIS claim --
     *  it validates that `keep the most columns` really is an LIS.
     *
     *  time  = O(2^m * m * n)
     *  space = O(m)
     */
    public int minDeletionSize_3(String[] strs) {
        int m = strs[0].length();
        int best = 0;

        for (int mask = 0; mask < (1 << m); mask++) {
            List<Integer> cols = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                if (((mask >> i) & 1) == 1) {
                    cols.add(i);
                }
            }
            boolean ok = true;
            for (String s : strs) {
                for (int t = 1; t < cols.size() && ok; t++) {
                    if (s.charAt(cols.get(t - 1)) > s.charAt(cols.get(t))) {
                        ok = false;
                    }
                }
                if (!ok) {
                    break;
                }
            }
            if (ok) {
                best = Math.max(best, cols.size());
            }
        }
        return m - best;
    }

}
