package LeetCodeJava.String;

// https://leetcode.com/problems/delete-columns-to-make-sorted/

/**
 *  944. Delete Columns to Make Sorted
 *  Easy
 *
 *  You are given an array of n strings strs, all of the same length.
 *
 *  The strings can be arranged such that there is one on each line, making a
 *  grid.
 *
 *  You want to delete the columns that are not sorted lexicographically.
 *
 *  Return the number of columns that you will delete.
 *
 *  Example 1:
 *  Input: strs = ["cba","daf","ghi"]
 *  Output: 1
 *  Explanation: the grid's columns are "cdg", "bah", "afi"; column 1 ("bah")
 *  is not sorted, so we delete 1 column.
 *
 *  Example 2:
 *  Input: strs = ["a","b"]
 *  Output: 0
 *
 *  Example 3:
 *  Input: strs = ["zyx","wvu","tsr"]
 *  Output: 3
 *
 *  Constraints:
 *   - n == strs.length
 *   - 1 <= n <= 100
 *   - 1 <= strs[i].length <= 1000
 */
public class DeleteColumnsToMakeSorted {

    // V0
    // IDEA: scan each column top-down, count it as deleted on the first
    //       out-of-order adjacent pair.
    /**
     * time = O(n * l)
     * space = O(1)
     */
    public int minDeletionSize(String[] strs) {
        int rows = strs.length;
        int cols = strs[0].length();
        int res = 0;
        for (int c = 0; c < cols; c++) {
            for (int r = 1; r < rows; r++) {
                if (strs[r - 1].charAt(c) > strs[r].charAt(c)) {
                    res++;
                    break;
                }
            }
        }
        return res;
    }
}
