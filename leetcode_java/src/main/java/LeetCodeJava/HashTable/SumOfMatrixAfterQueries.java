package LeetCodeJava.HashTable;

// https://leetcode.com/problems/sum-of-matrix-after-queries/description/

import java.util.*;

/**
 * 2718. Sum of Matrix After Queries
 * Medium
 *
 * You are given an integer n and a 0-indexed 2D array queries where queries[i] = [type_i, index_i, val_i].
 *
 * Initially, there is a 0-indexed n x n matrix filled with 0's. For each query, you must apply one of the following changes:
 *
 * if type_i == 0, set the values in the row with index_i to val_i, overwriting any previous values.
 * if type_i == 1, set the values in the column with index_i to val_i, overwriting any previous values.
 *
 * Return the sum of integers in the matrix after all queries are applied.
 *
 *
 * Example 1:
 *
 * Input: n = 3, queries = [[0,0,1],[1,2,2],[0,2,3],[1,0,4]]
 * Output: 23
 * Explanation: The queries are applied one by one to the matrix. The sum of the matrix after all queries are applied is 23.
 *
 * Example 2:
 *
 * Input: n = 3, queries = [[0,0,4],[0,1,2],[1,0,1],[0,2,3],[1,2,1]]
 * Output: 17
 * Explanation: The queries are applied one by one to the matrix. The sum of the matrix after all queries are applied is 17.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 * 1 <= queries.length <= 5 * 10^4
 * queries[i].length == 3
 * 0 <= type_i <= 1
 * 0 <= index_i < n
 * 0 <= val_i <= 10^5
 *
 */
public class SumOfMatrixAfterQueries {

    // V0
    // IDEA: REVERSE SCAN + HASH SETS (the last write to a row / column decides its cells)
    /**
     * time = O(Q)
     * space = O(N)
     */
    public long matrixSumQueries(int n, int[][] queries) {
        // rows / columns already written by a LATER query
        Set<Integer> rowsDone = new HashSet<>();
        Set<Integer> colsDone = new HashSet<>();
        long sum = 0;

        for (int i = queries.length - 1; i >= 0; i--) {
            int type = queries[i][0];
            int index = queries[i][1];
            int value = queries[i][2];
            if (type == 0) {
                // first time seen backwards = its final write; cells in columns
                // written later are already claimed
                if (rowsDone.add(index)) {
                    sum += (long) value * (n - colsDone.size());
                }
            } else {
                if (colsDone.add(index)) {
                    sum += (long) value * (n - rowsDone.size());
                }
            }
        }
        return sum;
    }
}
