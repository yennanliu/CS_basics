package LeetCodeJava.Greedy;

// https://leetcode.com/problems/lucky-numbers-in-a-matrix/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  1380. Lucky Numbers in a Matrix
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given an m x n matrix of distinct numbers, return all lucky numbers in the matrix in any order.
 *
 * A lucky number is an element of the matrix such that it is the minimum element in its row and maximum in its column.
 *
 *
 *
 * Example 1:
 *
 * Input: matrix = [[3,7,8],[9,11,13],[15,16,17]]
 * Output: [15]
 * Explanation: 15 is the only lucky number since it is the minimum in its row and the maximum in its column.
 * Example 2:
 *
 * Input: matrix = [[1,10,4,2],[9,3,8,7],[15,16,17,12]]
 * Output: [12]
 * Explanation: 12 is the only lucky number since it is the minimum in its row and the maximum in its column.
 * Example 3:
 *
 * Input: matrix = [[7,8],[1,2]]
 * Output: [7]
 * Explanation: 7 is the only lucky number since it is the minimum in its row and the maximum in its column.
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= n, m <= 50
 * 1 <= matrix[i][j] <= 105.
 * All elements in the matrix are distinct.
 */
public class LuckyNumbersInAMatrix {

    // V0
//    public List<Integer> luckyNumbers(int[][] matrix) {
//
//    }

    // V0-1
    // IDEA: ARRAY OP (fixed by gemini)
    public List<Integer> luckyNumbers_0_1(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // 1. Find the maximum value for each column
        int[] colMax = new int[cols];
        for (int j = 0; j < cols; j++) {
            int maxVal = Integer.MIN_VALUE;
            for (int i = 0; i < rows; i++) {
                maxVal = Math.max(maxVal, matrix[i][j]);
            }
            colMax[j] = maxVal;
        }

        // 2. Find the minimum value for each row
        int[] rowMin = new int[rows];
        for (int i = 0; i < rows; i++) {
            int minVal = Integer.MAX_VALUE;
            for (int j = 0; j < cols; j++) {
                minVal = Math.min(minVal, matrix[i][j]);
            }
            rowMin[i] = minVal;
        }

        // 3. A number is "lucky" if it is both a row minimum and a column maximum
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == rowMin[i] && matrix[i][j] == colMax[j]) {
                    res.add(matrix[i][j]);
                }
            }
        }

        return res;
    }


    // V0-2
    // IDEA: ARRAY OP (gpt)
    public List<Integer> luckyNumbers_0_2(int[][] matrix) {
        int l = matrix.length;
        int w = matrix[0].length;

        List<Integer> res = new ArrayList<>();

        // max of each column
        int[] colMax = new int[w];
        Arrays.fill(colMax, Integer.MIN_VALUE);

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < l; y++) {
                colMax[x] = Math.max(colMax[x], matrix[y][x]);
            }
        }

        // for each row, find min and its column
        for (int y = 0; y < l; y++) {
            int rowMin = Integer.MAX_VALUE;
            int colIdx = -1;

            for (int x = 0; x < w; x++) {
                if (matrix[y][x] < rowMin) {
                    rowMin = matrix[y][x];
                    colIdx = x;
                }
            }

            // check lucky number condition
            if (rowMin == colMax[colIdx]) {
                res.add(rowMin);
            }
        }

        return res;
    }


    // V1-1
    // IDEA: SIMULATION
    // https://leetcode.com/problems/lucky-numbers-in-a-matrix/editorial/
    public List<Integer> luckyNumbers_1_1(int[][] matrix) {
        int N = matrix.length, M = matrix[0].length;

        List<Integer> rowMin = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int rMin = Integer.MAX_VALUE;
            for (int j = 0; j < M; j++) {
                rMin = Math.min(rMin, matrix[i][j]);
            }
            rowMin.add(rMin);
        }

        List<Integer> colMax = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            int cMax = Integer.MIN_VALUE;
            for (int j = 0; j < N; j++) {
                cMax = Math.max(cMax, matrix[j][i]);
            }
            colMax.add(cMax);
        }

        List<Integer> luckyNumbers = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (matrix[i][j] == rowMin.get(i) && matrix[i][j] == colMax.get(j)) {
                    luckyNumbers.add(matrix[i][j]);
                }
            }
        }

        return luckyNumbers;
    }

    // V1-2
    // IDEA: GREEDY
    // https://leetcode.com/problems/lucky-numbers-in-a-matrix/editorial/
    public List<Integer> luckyNumbers_1_2(int[][] matrix) {
        int N = matrix.length, M = matrix[0].length;

        int rMinMax = Integer.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            int rMin = Integer.MAX_VALUE;
            for (int j = 0; j < M; j++) {
                rMin = Math.min(rMin, matrix[i][j]);
            }
            rMinMax = Math.max(rMinMax, rMin);
        }

        int cMaxMin = Integer.MAX_VALUE;
        for (int i = 0; i < M; i++) {
            int cMax = Integer.MIN_VALUE;
            for (int j = 0; j < N; j++) {
                cMax = Math.max(cMax, matrix[j][i]);
            }
            cMaxMin = Math.min(cMaxMin, cMax);
        }

        if (rMinMax == cMaxMin) {
            return new ArrayList<>(Arrays.asList(rMinMax));
        }

        return new ArrayList<>();
    }

    // V2-1
    // IDEA: Storing Maximums And Minimums
    // https://leetcode.com/problems/lucky-numbers-in-a-matrix/solutions/5499307/explanations-no-one-will-give-you2-detai-a9hi/
    public List<Integer> luckyNumbers_2_1(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[] row_minimums = new int[rows];
        Arrays.fill(row_minimums, Integer.MAX_VALUE);
        int[] col_maximums = new int[cols];

        for (int row_ind = 0; row_ind < rows; ++row_ind) {
            for (int col_ind = 0; col_ind < cols; ++col_ind) {
                int el = matrix[row_ind][col_ind];
                row_minimums[row_ind] = Math.min(row_minimums[row_ind], el);
                col_maximums[col_ind] = Math.max(col_maximums[col_ind], el);
            }
        }

        for (int row_ind = 0; row_ind < rows; ++row_ind) {
            for (int col_ind = 0; col_ind < cols; ++col_ind) {
                int el = matrix[row_ind][col_ind];
                if (el == row_minimums[row_ind] && el == col_maximums[col_ind]) {
                    return Collections.singletonList(el);
                }
            }
        }

        return Collections.emptyList();
    }


    // V2-2
    // IDEA: Find The Only Lucky Number With Math
    // https://leetcode.com/problems/lucky-numbers-in-a-matrix/solutions/5499307/explanations-no-one-will-give-you2-detai-a9hi/
    public List<Integer> luckyNumbers_2_2(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int row_maximum_of_minimums = 0;
        for (int[] row : matrix) {
            int row_minimum = Arrays.stream(row).min().getAsInt();
            row_maximum_of_minimums = Math.max(row_maximum_of_minimums, row_minimum);
        }

        int col_minimum_of_maximums = Integer.MAX_VALUE;
        for (int col_ind = 0; col_ind < cols; ++col_ind) {
            int col_maximum = 0;
            for (int row_ind = 0; row_ind < rows; ++row_ind) {
                col_maximum = Math.max(col_maximum, matrix[row_ind][col_ind]);
            }
            col_minimum_of_maximums = Math.min(col_minimum_of_maximums, col_maximum);
        }

        return row_maximum_of_minimums == col_minimum_of_maximums ? Collections.singletonList(col_minimum_of_maximums)
                : Collections.emptyList();
    }



}
