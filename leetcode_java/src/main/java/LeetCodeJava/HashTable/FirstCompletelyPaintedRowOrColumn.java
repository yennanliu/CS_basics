package LeetCodeJava.HashTable;

// https://leetcode.com/problems/first-completely-painted-row-or-column/description/

/**
 * 2661. First Completely Painted Row or Column
 * Medium
 *
 * You are given a 0-indexed integer array arr, and an m x n integer matrix mat. arr and mat both contain all the integers in the range [1, m * n].
 *
 * Go through each index i in arr starting from index 0 and paint the cell in mat containing the integer arr[i].
 *
 * Return the smallest index i at which either a row or a column will be completely painted in mat.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,3,4,2], mat = [[1,4],[2,3]]
 * Output: 2
 * Explanation: The moves are shown in order, and both the first row and second column of the matrix become fully painted at arr[2].
 *
 * Example 2:
 *
 * Input: arr = [2,8,7,4,1,3,5,6,9], mat = [[3,2,5],[1,4,6],[8,7,9]]
 * Output: 3
 * Explanation: The second column becomes fully painted at arr[3].
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n = mat[i].length
 * arr.length == m * n
 * 1 <= m, n <= 10^5
 * 1 <= m * n <= 10^5
 * 1 <= arr[i], mat[r][c] <= m * n
 * All the integers of arr are unique.
 * All the integers of mat are unique.
 *
 */
public class FirstCompletelyPaintedRowOrColumn {

    // V0
    // IDEA: HASH TABLE (value -> cell) + PAINT COUNTERS PER ROW AND PER COLUMN
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int firstCompleteIndex(int[] arr, int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        // values are a permutation of 1..m*n, so a flat array works as the map
        int[] rowOf = new int[rows * cols + 1];
        int[] colOf = new int[rows * cols + 1];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rowOf[mat[r][c]] = r;
                colOf[mat[r][c]] = c;
            }
        }

        int[] paintedInRow = new int[rows];
        int[] paintedInCol = new int[cols];
        for (int i = 0; i < arr.length; i++) {
            int r = rowOf[arr[i]];
            int c = colOf[arr[i]];
            paintedInRow[r]++;
            paintedInCol[c]++;
            // a row is full at `cols` cells, a column at `rows` cells (the thresholds cross)
            if (paintedInRow[r] == cols || paintedInCol[c] == rows) {
                return i;
            }
        }
        return -1;
    }
}
