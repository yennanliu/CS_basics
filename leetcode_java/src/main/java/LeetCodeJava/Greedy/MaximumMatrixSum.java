package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-matrix-sum/description/

/**
 * 1975. Maximum Matrix Sum
 * Medium
 *
 * You are given an n x n integer matrix. You can do the following operation any number of times:
 *
 * Choose any two adjacent elements of matrix and multiply each of them by -1.
 *
 * Two elements are considered adjacent if and only if they share a border.
 *
 * Your goal is to maximize the summation of the matrix's elements. Return the maximum sum of the matrix's
 * elements using the operation mentioned above.
 *
 *
 * Example 1:
 *
 * Input: matrix = [[1,-1],[-1,1]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: matrix = [[1,2,3],[-1,-2,-3],[1,2,3]]
 * Output: 16
 *
 *
 * Constraints:
 *
 * n == matrix.length == matrix[i].length
 * 2 <= n <= 250
 * -10^5 <= matrix[i][j] <= 10^5
 *
 */
public class MaximumMatrixSum {

    // V0
    // IDEA: GREEDY / PARITY INVARIANT - ONE OPERATION FLIPS TWO SIGNS, SO THE PARITY OF NEGATIVES IS FIXED
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public long maxMatrixSum(int[][] matrix) {
        long absSum = 0;
        int negativeCount = 0;
        int smallestAbs = Integer.MAX_VALUE;

        for (int[] row : matrix) {
            for (int value : row) {
                if (value < 0) {
                    negativeCount++;
                }
                int abs = Math.abs(value);
                absSum += abs;
                smallestAbs = Math.min(smallestAbs, abs);
            }
        }

        // even number of negatives: pairs of minus signs can be walked together and cancelled
        if (negativeCount % 2 == 0) {
            return absSum;
        }

        // odd: exactly one minus sign survives; park it on the smallest |x| (a zero makes it free)
        return absSum - 2L * smallestAbs;
    }
}
