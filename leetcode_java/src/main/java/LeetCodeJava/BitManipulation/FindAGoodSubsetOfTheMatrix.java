package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/find-a-good-subset-of-the-matrix/description/

import java.util.*;

/**
 * 2732. Find a Good Subset of the Matrix
 * Hard
 *
 * You are given a 0-indexed m x n binary matrix grid.
 *
 * Let us call a non-empty subset of rows good if the sum of each column of the subset is at most half of the length of the subset.
 *
 * More formally, if the length of the chosen subset of rows is k, then the sum of each column should be at most floor(k / 2).
 *
 * Return an integer array that contains row indices of a good subset sorted in ascending order.
 *
 * If there are multiple good subsets, you can return any of them. If there are no good subsets, return an empty array.
 *
 * A subset of rows of the matrix grid is any matrix that can be obtained by deleting some (possibly none or all) rows from grid.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,1,0],[0,0,0,1],[1,1,1,1]]
 * Output: [0,1]
 * Explanation: We can choose the 0th and 1st rows to create a good subset of rows.
 * The length of the chosen subset is 2.
 * - The sum of the 0th column is 0 + 0 = 0, which is at most half of the length of the subset.
 * - The sum of the 1st column is 1 + 0 = 1, which is at most half of the length of the subset.
 * - The sum of the 2nd column is 1 + 0 = 1, which is at most half of the length of the subset.
 * - The sum of the 3rd column is 0 + 1 = 1, which is at most half of the length of the subset.
 *
 * Example 2:
 *
 * Input: grid = [[0]]
 * Output: [0]
 * Explanation: We can choose the 0th row to create a good subset of rows.
 * The length of the chosen subset is 1.
 * - The sum of the 0th column is 0, which is at most half of the length of the subset.
 *
 * Example 3:
 *
 * Input: grid = [[1,1,1],[1,1,1]]
 * Output: []
 * Explanation: It is impossible to choose any subset of rows to create a good subset.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m <= 10^4
 * 1 <= n <= 5
 * grid[i][j] is either 0 or 1.
 *
 */
public class FindAGoodSubsetOfTheMatrix {

    // V0
    // IDEA: BITMASK ROWS + ONLY k = 1 OR k = 2 CAN EVER WIN
    //       encode each row (n <= 5 columns) as a bitmask.
    //       k = 1 -> an all-zero row. k = 2 -> two rows with no shared 1 (maskA & maskB == 0).
    //       any odd k has the same cap as k - 1 so a smaller subset already works, and for
    //       even k >= 4 a pigeonhole count over <= 5 columns shows a valid pair must exist.
    //       one representative row per mask is enough -> at most 32 candidates.
    /**
     * time = O(M * N + 4^N)
     * space = O(2^N)
     */
    public List<Integer> goodSubsetofBinaryMatrix(int[][] grid) {
        // firstRowOfMask[mask] = first row index with that mask, -1 if none
        int columns = grid[0].length;
        int maskCount = 1 << columns;
        int[] firstRowOfMask = new int[maskCount];
        Arrays.fill(firstRowOfMask, -1);

        for (int i = 0; i < grid.length; i++) {
            int mask = 0;
            for (int j = 0; j < columns; j++) {
                if (grid[i][j] == 1) {
                    // set bit j: column j has a 1 in this row
                    mask |= 1 << j;
                }
            }
            // k = 1: an all-zero row is good on its own
            if (mask == 0) {
                return Collections.singletonList(i);
            }
            if (firstRowOfMask[mask] == -1) {
                firstRowOfMask[mask] = i;
            }
        }

        // k = 2: two distinct masks that share no column
        for (int maskA = 1; maskA < maskCount; maskA++) {
            if (firstRowOfMask[maskA] == -1) {
                continue;
            }
            for (int maskB = maskA + 1; maskB < maskCount; maskB++) {
                if (firstRowOfMask[maskB] == -1) {
                    continue;
                }
                // no column where both rows hold a 1
                if ((maskA & maskB) == 0) {
                    int rowA = firstRowOfMask[maskA];
                    int rowB = firstRowOfMask[maskB];
                    List<Integer> result = new ArrayList<>();
                    // the indices must come back in ascending order
                    result.add(Math.min(rowA, rowB));
                    result.add(Math.max(rowA, rowB));
                    return result;
                }
            }
        }
        return new ArrayList<>();
    }
}
