package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/count-submatrices-with-top-left-element-and-sum-less-than-k/description/

/**
 * 3070. Count Submatrices with Top-Left Element and Sum Less Than k
 * Medium
 *
 * You are given a 0-indexed integer matrix grid and an integer k.
 *
 * Return the number of submatrices that contain the top-left element of the grid, and have a sum less than or equal to k.
 *
 *
 * Example 1:
 *
 * Input: grid = [[7,6,3],[6,6,1]], k = 18
 * Output: 4
 * Explanation: There are only 4 submatrices, shown in the image above, that contain the top-left element of grid, and have a sum less than or equal to 18.
 *
 * Example 2:
 *
 * Input: grid = [[7,2,9],[1,5,0],[2,6,6]], k = 20
 * Output: 6
 * Explanation: There are only 6 submatrices, shown in the image above, that contain the top-left element of grid, and have a sum less than or equal to 20.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= n, m <= 1000
 * 0 <= grid[i][j] <= 1000
 * 1 <= k <= 10^9
 *
 */
public class CountSubmatricesWithTopLeftElementAndSumLessThanK {

    // V0
    // IDEA: A SUBMATRIX CONTAINING (0,0) IS A PREFIX RECTANGLE -> 2D PREFIX SUM
    // such a submatrix is fixed by its bottom-right corner (i, j), and its sum is
    // the 2D prefix sum there:
    //   prefix[i+1][j+1] = grid[i][j] + prefix[i][j+1] + prefix[i+1][j] - prefix[i][j]
    // so count the corners whose prefix sum is <= k.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int countSubmatrices(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;
        // long: a 1000 x 1000 grid of 1000s sums to 10^9, close to the int limit
        long[][] prefix = new long[rows + 1][cols + 1];
        int count = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                prefix[i + 1][j + 1] = grid[i][j] + prefix[i][j + 1] + prefix[i + 1][j] - prefix[i][j];
                if (prefix[i + 1][j + 1] <= k) {
                    count++;
                }
            }
        }
        return count;
    }
}
