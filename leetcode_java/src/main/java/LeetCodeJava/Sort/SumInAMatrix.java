package LeetCodeJava.Sort;

// https://leetcode.com/problems/sum-in-a-matrix/

import java.util.Arrays;

/**
 *  2679. Sum in a Matrix
 *  Medium
 *
 *  You are given a 0-indexed 2D integer array nums. Initially, your score is 0.
 *  Perform the following operations until the matrix becomes empty:
 *    1. From each row in the matrix, select the largest number and remove it. In
 *       the case of a tie, it does not matter which number is chosen.
 *    2. Identify the highest number amongst all those removed in step 1. Add
 *       that number to your score.
 *
 *  Return the final score.
 *
 *  Example 1:
 *    Input: nums = [[7,2,1],[6,4,2],[6,5,3],[3,2,1]]
 *    Output: 15
 *    Explanation: rounds remove {7,6,6,3} -> +7, {2,4,5,2} -> +5,
 *                 {1,2,3,1} -> +3. Total 15.
 *
 *  Example 2:
 *    Input: nums = [[1]]
 *    Output: 1
 *
 *  Constraints:
 *    1 <= nums.length <= 300
 *    1 <= nums[i].length <= 500
 *    0 <= nums[i][j] <= 10^3
 */
public class SumInAMatrix {

    // V0
    // IDEA: SORT EACH ROW DESCENDING, THEN SUM THE PER-COLUMN MAXIMUM
    //       round r always removes the r-th largest element of every row, so if
    //       each row is sorted descending, round r simply consumes column r.
    //       NOTE: rows are independent — sorting a row does not change WHICH
    //             value it gives up in a round, it only makes it easy to read.
    //       NOTE: answer = sum over columns of max(column), so the removals
    //             never have to be simulated.
    //       Arrays.sort only sorts int[] ascending, so sort ascending and read
    //       the columns from the END (index n-1-c) to get the descending order.
    /**
     * time = O(m * n log n)
     * space = O(1) extra   // rows sorted in place
     */
    public int matrixSum(int[][] nums) {
        for (int[] row : nums) {
            Arrays.sort(row);
        }

        int n = nums[0].length;
        int ans = 0;
        for (int c = 0; c < n; c++) {
            int colMax = 0;
            for (int[] row : nums) {
                colMax = Math.max(colMax, row[n - 1 - c]);
            }
            ans += colMax;
        }
        return ans;
    }
}
