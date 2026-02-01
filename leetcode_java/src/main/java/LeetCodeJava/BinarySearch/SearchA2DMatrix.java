package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-a-2d-matrix/
/**
 * 74. Search a 2D Matrix
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an m x n integer matrix matrix with the following two properties:
 *
 * Each row is sorted in non-decreasing order.
 * The first integer of each row is greater than the last integer of the previous row.
 * Given an integer target, return true if target is in matrix or false otherwise.
 *
 * You must write a solution in O(log(m * n)) time complexity.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 3
 * Output: true
 * Example 2:
 *
 *
 * Input: matrix = [[1,3,5,7],[10,11,16,20],[23,30,34,60]], target = 13
 * Output: false
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 100
 * -104 <= matrix[i][j], target <= 104
 *
 */
public class SearchA2DMatrix {

    // V0
    // IDEA : BINARY SEARCH + FLATTEN MATRIX
    /**
     * time = O(log(M * N))
     * space = O(1)
     */
    public boolean searchMatrix(int[][] matrix, int target) {

        int length = matrix[0].length;
        int width = matrix.length;

        // flatten matrix + binary search
        /** NOTE !!! we FLATTEN matrix as 1D array, and do binary search */
        int l = 0;
        int r = length * width - 1;

        while (r >= l){
            int mid = (l + r) / 2;
            /** NOTE !!!
             *   -> whether x or y axis
             *   -> we do with length
             *   -> e.g. y = mid / length
             *   ->      x = mid % length
             */
            int cur = matrix[mid / length][mid % length];
            if (cur == target){
                return true;
            }else if (cur < target){
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }

        return false;
    }

    // V0-1
    // IDEA: BINARY SEARCH + 2D ARRAY
    /**
     * time = O(M + N)
     * space = O(1)
     */
    public boolean searchMatrix_0_1(int[][] matrix, int target) {

        int l = matrix.length;
        int w = matrix[0].length;

        // edge
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        if (matrix.length == 1 || matrix[0].length == 1) {

            if (matrix.length == 1) {
                for (int x : matrix[0]) {
                    if (x == target) {
                        return true;
                    }
                }
                return false;
            }

            if (matrix[0].length == 1) {
                for (int i = 0; i < l; i++) {
                    if (matrix[i][0] == target) {
                        return true;
                    }
                }
                return false;
            }
        }

        // binary search
        for (int i = 0; i < l; i++) {
            int[] curArray = matrix[i];
            int left = 0;
            int right = curArray.length - 1;
            // do binary search
            if (target >= curArray[left] && target <= curArray[right]) {
                while (right >= left) {
                    int mid = (left + right) / 2;
                    if (curArray[mid] == target) {
                        return true;
                    } else if (curArray[mid] > target) {
                        right = mid - 1;
                    } else {
                        left = mid + 1;
                    }
                }
            }
        }

        return false;
    }

    // V1
    // IDEA : BINARY SEARCH + FLATTEN MATRIX
    // https://leetcode.com/problems/search-a-2d-matrix/editorial/
    /**
     * time = O(log(M * N))
     * space = O(1)
     */
    public boolean searchMatrix_2(int[][] matrix, int target) {
        int m = matrix.length;
        if (m == 0)
            return false;
        int n = matrix[0].length;

        // binary search
        /** NOTE !!! FLATTEN MATRIX */
        int left = 0, right = m * n - 1;
        int pivotIdx, pivotElement;
        while (left <= right) {
            pivotIdx = (left + right) / 2;
            /** NOTE !!! TRICK HERE :
             *
             *   pivotIdx / n : y index
             *   pivotIdx % n : x index
             */
            pivotElement = matrix[pivotIdx / n][pivotIdx % n];
            if (target == pivotElement)
                return true;
            else {
                if (target < pivotElement)
                    right = pivotIdx - 1;
                else
                    left = pivotIdx + 1;
            }
        }
        return false;
    }

}
