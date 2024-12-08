package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/range-sum-query-2d-immutable/description/
/**
 * 304. Range Sum Query 2D - Immutable
 * Solved
 * Medium
 * Topics
 * Companies
 * Given a 2D matrix matrix, handle multiple queries of the following type:
 *
 * Calculate the sum of the elements of matrix inside the rectangle defined by its upper left corner (row1, col1) and lower right corner (row2, col2).
 * Implement the NumMatrix class:
 *
 * NumMatrix(int[][] matrix) Initializes the object with the integer matrix matrix.
 * int sumRegion(int row1, int col1, int row2, int col2) Returns the sum of the elements of matrix inside the rectangle defined by its upper left corner (row1, col1) and lower right corner (row2, col2).
 * You must design an algorithm where sumRegion works on O(1) time complexity.
 *
 *
 *
 * Example 1:
 *
 *
 * Input
 * ["NumMatrix", "sumRegion", "sumRegion", "sumRegion"]
 * [[[[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]], [2, 1, 4, 3], [1, 1, 2, 2], [1, 2, 2, 4]]
 * Output
 * [null, 8, 11, 12]
 *
 * Explanation
 * NumMatrix numMatrix = new NumMatrix([[3, 0, 1, 4, 2], [5, 6, 3, 2, 1], [1, 2, 0, 1, 5], [4, 1, 0, 1, 7], [1, 0, 3, 0, 5]]);
 * numMatrix.sumRegion(2, 1, 4, 3); // return 8 (i.e sum of the red rectangle)
 * numMatrix.sumRegion(1, 1, 2, 2); // return 11 (i.e sum of the green rectangle)
 * numMatrix.sumRegion(1, 2, 2, 4); // return 12 (i.e sum of the blue rectangle)
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * -104 <= matrix[i][j] <= 104
 * 0 <= row1 <= row2 < m
 * 0 <= col1 <= col2 < n
 * At most 104 calls will be made to sumRegion.
 *
 */
public class RangeSumQuery2DImmutable {


    /**
     * Your NumMatrix object will be instantiated and called as such:
     * NumMatrix obj = new NumMatrix(matrix);
     * int param_1 = obj.sumRegion(row1,col1,row2,col2);
     */

    // V0
//    class NumMatrix {
//
//        public NumMatrix(int[][] matrix) {
//
//        }
//
//        public int sumRegion(int row1, int col1, int row2, int col2) {
//
//        }
//    }

    // V1
    // IDEA : DP
    // https://zxi.mytechroad.com/blog/dynamic-programming/leetcode-304-range-sum-query-2d-immutable/
    // https://www.youtube.com/watch?v=MSNSqU3BnXk
    class NumMatrix_1 {
        private int[][] sums;

        public NumMatrix_1(int[][] matrix) {
            if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
                return;
            }

            int m = matrix.length;
            int n = matrix[0].length;

            // Initialize the sums array
            sums = new int[m + 1][n + 1];
            for (int i = 1; i <= m; i++) {
                for (int j = 1; j <= n; j++) {
                    sums[i][j] = matrix[i - 1][j - 1]
                            + sums[i - 1][j]
                            + sums[i][j - 1]
                            - sums[i - 1][j - 1];
                }
            }
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sums[row2 + 1][col2 + 1]
                    - sums[row2 + 1][col1]
                    - sums[row1][col2 + 1]
                    + sums[row1][col1];
        }
    }

    // V2
    // IDEA : DP
    // https://leetcode.com/problems/range-sum-query-2d-immutable/solutions/2104317/dp-visualised-interview-tips/
    class NumMatrix_2 {
        private int[][] sums;

        public NumMatrix_2(int[][] matrix) {
            int n = matrix.length;
            int m = matrix[0].length;
            sums = new int[n+1][m+1];
            for (int i=1; i<=n; i++)
                for (int j=1; j<=m; j++)
                    sums[i][j] = sums[i-1][j] + sums[i][j-1] + matrix[i-1][j-1] - sums[i-1][j-1];
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            int RED_RECTANGLE = sums[row2+1][col2+1];
            int PURPLE_RECTANGLES = sums[row1][col2+1] + sums[row2+1][col1];
            int BLUE_RECTANGLE = sums[row1][col1];
            return RED_RECTANGLE - PURPLE_RECTANGLES + BLUE_RECTANGLE;
        }
    }

    // V3
    // IDEA : DP
    // https://leetcode.com/problems/range-sum-query-2d-immutable/solutions/1204168/js-python-java-c-easy-4-rectangles-dp-solution-w-explanation/
    class NumMatrix_3 {
        long[][] dp;

        public NumMatrix_3(int[][] M) {
            int ylen = M.length + 1, xlen = M[0].length + 1;
            dp = new long[ylen][xlen];
            for (int i = 1; i < ylen; i++)
                for (int j = 1; j < xlen; j++)
                    dp[i][j] = M[i-1][j-1] + dp[i-1][j] + dp[i][j-1] - dp[i-1][j-1];
        }

        public int sumRegion(int R1, int C1, int R2, int C2) {
            return (int)(dp[R2+1][C2+1] - dp[R2+1][C1] - dp[R1][C2+1] + dp[R1][C1]);
        }
    }
}
