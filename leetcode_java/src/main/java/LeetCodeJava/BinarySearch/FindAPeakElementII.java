package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-a-peak-element-ii/description/

/**
 * 1901. Find a Peak Element II
 * Medium
 *
 * A peak element in a 2D grid is an element that is strictly greater than all of its adjacent neighbors
 * to the left, right, top, and bottom.
 *
 * Given a 0-indexed m x n matrix mat where no two adjacent cells are equal, find any peak element
 * mat[i][j] and return the length 2 array [i,j].
 *
 * You may assume that the entire matrix is surrounded by an outer perimeter with the value -1 in each
 * cell.
 *
 * You must write an algorithm that runs in O(m log(n)) or O(n log(m)) time.
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,4],[3,2]]
 * Output: [0,1]
 * Explanation: Both 3 and 4 are peak elements so [1,0] and [0,1] are both acceptable answers.
 *
 * Example 2:
 *
 * Input: mat = [[10,20,15],[21,30,14],[7,16,32]]
 * Output: [1,1]
 * Explanation: Both 30 and 32 are peak elements so [1,1] and [2,2] are both acceptable answers.
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 500
 * 1 <= mat[i][j] <= 10^5
 * No two adjacent cells are equal.
 *
 */
public class FindAPeakElementII {

    // V0
    // IDEA: BINARY SEARCH ON ROWS (THE ROW MAXIMUM IS A PEAK OR POINTS UPHILL)
    /**
     *  Take the middle row and the column of its maximum. That cell already beats its left and
     *  right neighbours, so it can only lose vertically:
     *    - mat[mid][col] > mat[mid + 1][col] -> a peak exists in rows [left .. mid]
     *    - otherwise values rise going down  -> a peak exists in rows [mid + 1 .. right]
     *  The border is -1, so climbing must stop somewhere inside the kept half.
     *  Each step halves the rows and costs O(n) to find the row maximum.
     */
    /**
     * time = O(N log M)
     * space = O(1)
     */
    public int[] findPeakGrid(int[][] mat) {
        int left = 0;
        int right = mat.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            int col = indexOfRowMax(mat[mid]);
            if (mat[mid][col] > mat[mid + 1][col]) {
                // the row below is lower: a peak is at mid or above
                right = mid;
            } else {
                // the row below is higher: climb down
                left = mid + 1;
            }
        }
        return new int[]{left, indexOfRowMax(mat[left])};
    }

    /** Index of the largest value in the row. */
    private int indexOfRowMax(int[] row) {
        int best = 0;
        for (int j = 1; j < row.length; j++) {
            if (row[j] > row[best]) {
                best = j;
            }
        }
        return best;
    }
}
