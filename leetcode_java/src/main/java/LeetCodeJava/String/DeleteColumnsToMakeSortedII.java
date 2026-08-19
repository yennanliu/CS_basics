package LeetCodeJava.String;

// https://leetcode.com/problems/delete-columns-to-make-sorted-ii/

/**
 *  955. Delete Columns to Make Sorted II
 *  Medium
 *
 *  You are given an array of n strings strs, all of the same length.
 *
 *  We may choose any deletion indices, and we delete all the characters in
 *  those indices for each string.
 *
 *  For example, if we have strs = ["abcdef","uvwxyz"] and deletion indices
 *  {0, 2, 3}, then the final array after deletions is ["bef", "vyz"].
 *
 *  Suppose we chose a set of deletion indices answer such that after
 *  deletions, the final array has its elements in lexicographic order
 *  (i.e., strs[0] <= strs[1] <= strs[2] <= ... <= strs[n - 1]). Return the
 *  minimum possible value of answer.length.
 *
 *  Example 1:
 *  Input: strs = ["ca","bb","ac"]
 *  Output: 1
 *  Explanation: after deleting column 0, we get ["a","b","c"] which is sorted.
 *
 *  Example 2:
 *  Input: strs = ["xc","yb","za"]
 *  Output: 0
 *
 *  Example 3:
 *  Input: strs = ["zyx","wvu","tsr"]
 *  Output: 3
 *
 *  Constraints:
 *   - n == strs.length
 *   - 1 <= n <= 100
 *   - 1 <= strs[i].length <= 100
 */
public class DeleteColumnsToMakeSortedII {

    // V0
    // IDEA: GREEDY - keep a column unless it breaks the order for some adjacent
    //       pair that is not already strictly settled by a previously kept
    //       column; keeping a column can only settle more pairs.
    /**
     * time = O(n * l)   // n = rows, l = cols
     * space = O(n)
     */
    public int minDeletionSize(String[] strs) {
        int rows = strs.length;
        int cols = strs[0].length();

        // settled[i] == true  =>  row i is already strictly < row i+1
        boolean[] settled = new boolean[Math.max(rows - 1, 0)];
        int res = 0;

        for (int c = 0; c < cols; c++) {
            boolean bad = false;
            for (int r = 0; r + 1 < rows; r++) {
                if (!settled[r] && strs[r].charAt(c) > strs[r + 1].charAt(c)) {
                    bad = true;
                    break;
                }
            }
            if (bad) {
                res++;
                continue;
            }
            // keep this column -> some pairs become strictly ordered
            for (int r = 0; r + 1 < rows; r++) {
                if (!settled[r] && strs[r].charAt(c) < strs[r + 1].charAt(c)) {
                    settled[r] = true;
                }
            }
        }
        return res;
    }
}
