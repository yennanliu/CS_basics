package LeetCodeJava.HashTable;

// https://leetcode.com/problems/find-smallest-common-element-in-all-rows/description/

import java.util.*;

/**
 * 1198. Find Smallest Common Element in All Rows
 * Medium
 *
 * Given an m x n matrix mat where every row is sorted in strictly increasing order,
 * return the smallest common element in all rows.
 *
 * If there is no common element, return -1.
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,2,3,4,5],[2,4,5,8,10],[3,5,7,9,11],[1,3,5,7,9]]
 * Output: 5
 *
 * Example 2:
 *
 * Input: mat = [[1,2,3],[2,3,4],[2,3,5]]
 * Output: 2
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 500
 * 1 <= mat[i][j] <= 10^4
 * mat[i] is sorted in strictly increasing order.
 *
 */
public class FindSmallestCommonElementInAllRows {

    // V0
    // IDEA: HASH TABLE (counting)
    //       rows are strictly increasing, so a value appears at most once per row:
    //       count[x] == m  <=>  x is in every row. A count can only reach m in the last row,
    //       which is scanned in ascending order, so the first value to hit m is the smallest.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int smallestCommonElement(int[][] mat) {
        int rows = mat.length;
        Map<Integer, Integer> count = new HashMap<>();

        for (int[] row : mat) {
            for (int value : row) {
                int seen = count.getOrDefault(value, 0) + 1;
                count.put(value, seen);
                if (seen == rows) {
                    return value;
                }
            }
        }
        return -1;
    }

    // V1
    // IDEA: ROW POINTERS (k-way merge, no hash map)
    //       move every row's pointer up to the current max; when all rows agree, that is the answer
    /**
     * time = O(M * N)
     * space = O(M)
     */
    public int smallestCommonElement_1(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[] pointer = new int[rows];

        while (true) {
            // the candidate is the largest value currently under any pointer
            int candidate = 0;
            for (int i = 0; i < rows; i++) {
                candidate = Math.max(candidate, mat[i][pointer[i]]);
            }

            boolean allSame = true;
            for (int i = 0; i < rows; i++) {
                // values smaller than the candidate can never be common: skip past them
                while (pointer[i] < cols && mat[i][pointer[i]] < candidate) {
                    pointer[i]++;
                }
                // a row ran out -> nothing is common to all rows
                if (pointer[i] == cols) {
                    return -1;
                }
                if (mat[i][pointer[i]] != candidate) {
                    allSame = false;
                }
            }
            if (allSame) {
                return candidate;
            }
        }
    }
}
