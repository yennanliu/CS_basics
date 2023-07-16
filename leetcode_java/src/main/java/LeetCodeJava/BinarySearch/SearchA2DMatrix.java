package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-a-2d-matrix/

public class SearchA2DMatrix {

    // V0
    // IDEA : BINARY SEARCH + FLATTEN MATRIX
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


    // V1
    // IDEA : BINARY SEARCH + FLATTEN MATRIX
    // https://leetcode.com/problems/search-a-2d-matrix/editorial/
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
