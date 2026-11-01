package LeetCodeJava.Array;

// https://leetcode.com/problems/the-k-weakest-rows-in-a-matrix/description/

import java.util.*;

/**
 * 1337. The K Weakest Rows in a Matrix
 * Easy
 *
 * You are given an m x n binary matrix mat of 1's (representing soldiers)
 * and 0's (representing civilians). The soldiers are positioned in front of
 * the civilians. That is, all the 1's will appear to the left of all the 0's
 * in each row.
 *
 * A row i is weaker than a row j if one of the following is true:
 *
 * - The number of soldiers in row i is less than the number of soldiers in row j.
 * - Both rows have the same number of soldiers and i < j.
 *
 * Return the indices of the k weakest rows in the matrix ordered from
 * weakest to strongest.
 *
 * Example 1:
 *
 * Input: mat =
 * [[1,1,0,0,0],
 *  [1,1,1,1,0],
 *  [1,0,0,0,0],
 *  [1,1,0,0,0],
 *  [1,1,1,1,1]],
 * k = 3
 * Output: [2,0,3]
 * Explanation:
 * The number of soldiers in each row is:
 * - Row 0: 2
 * - Row 1: 4
 * - Row 2: 1
 * - Row 3: 2
 * - Row 4: 5
 * The rows ordered from weakest to strongest are [2,0,3,1,4].
 *
 * Example 2:
 *
 * Input: mat =
 * [[1,0,0,0],
 *  [1,1,1,1],
 *  [1,0,0,0],
 *  [1,0,0,0]],
 * k = 2
 * Output: [0,2]
 * Explanation:
 * The number of soldiers in each row is:
 * - Row 0: 1
 * - Row 1: 4
 * - Row 2: 1
 * - Row 3: 1
 * The rows ordered from weakest to strongest are [0,2,3,1].
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 2 <= n, m <= 100
 * 1 <= k <= m
 * matrix[i][j] is either 0 or 1.
 *
 */
public class TheKWeakestRowsInAMatrix {

    // V0
    // IDEA: BINARY SEARCH THE SOLDIER COUNT PER ROW + SORT BY (count, row index)
    //   each row is 1...1 0...0, so the soldier count is the index of the first 0
    /**
     * time = O(M log N + M log M)
     * space = O(M)
     */
    public int[] kWeakestRows(int[][] mat, int k) {
        int m = mat.length;

        // strength[i] = { soldiers in row i, i }
        List<int[]> strength = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            strength.add(new int[] { countSoldiers(mat[i]), i });
        }

        // weaker = fewer soldiers; on a tie, the smaller row index
        strength.sort((a, b) -> compareStrength(a, b));

        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = strength.get(i)[1];
        }
        return result;
    }

    /** Index of the first 0 in a row shaped 1...1 0...0, i.e. the number of soldiers. */
    private int countSoldiers(int[] row) {
        int left = 0;
        int right = row.length;
        // invariant: the first 0 lies in [left, right]
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (row[mid] == 0) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Fewer soldiers first; on a tie, smaller row index first. */
    private int compareStrength(int[] a, int[] b) {
        if (a[0] != b[0]) {
            return Integer.compare(a[0], b[0]);
        }
        return Integer.compare(a[1], b[1]);
    }
}
