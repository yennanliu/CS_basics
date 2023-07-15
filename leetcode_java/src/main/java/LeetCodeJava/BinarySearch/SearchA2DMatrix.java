package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/search-a-2d-matrix/

import java.util.Arrays;

public class SearchA2DMatrix {

    // V0
    // IDEA : BINARY SEARCH
//    public boolean searchMatrix(int[][] matrix, int target) {
//
//        if (matrix.length == 0 && matrix[0].length == 0){
//            return false;
//        }
//
//        if (matrix.length == 1 && matrix[0].length == 1){
//            return matrix[0][0] == target;
//        }
//
//        if (matrix.length == 1){
//            //return Arrays.asList(matrix[0]).contains(target);
//            for (int x : matrix[0]){
//                if (x == target){
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        int length = matrix.length;
//        int width = matrix[0].length;;
//
//        int l = 0;
//        int r = matrix[0].length - 1;
//        for (int i = 0; i < length - 1; i++){
//
//            int mid = (l + r) / 2;
//            int cur = matrix[i][mid];
//            // case 1 : match target
//            if (cur == target){
//                return true;
//                // case 2 : target > biggest num in current row, move to next row
//            }else if (target > matrix[i][width - 1]){
//                // to next row
//                continue;
//            }else{
//                if (binarySearch(matrix[i], target)){
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
//
//    private Boolean binarySearch(int[] input, int target){
//
//        int l = 0;
//        int r = input.length -1;
//
//        while (r >= l){
//            int mid = (l + r) / 2;
//            int cur = input[mid];
//            if (cur == target){
//                return true;
//            }
//            else if (cur > target){
//                r = mid - 1;
//            }else{
//                l = mid + 1;
//            }
//        }
//        return false;
//    }


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
