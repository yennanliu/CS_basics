package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/median-of-a-row-wise-sorted-matrix/description/

/**
 * 2387. Median of a Row Wise Sorted Matrix
 * Medium
 *
 * Given an m x n matrix grid containing an odd number of integers where each row is sorted in
 * non-decreasing order, return the median of the matrix.
 *
 * You must solve the problem in less than O(m * n) time complexity.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,2],[2,3,3],[1,3,4]]
 * Output: 2
 * Explanation: The sorted array is [1,1,1,2,2,3,3,3,4]. The median is 2.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,3,3,4]]
 * Output: 3
 * Explanation: The sorted array is [1,1,3,3,4]. The median is 3.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 500
 * m and n are both odd.
 * 1 <= grid[i][j] <= 10^6
 * grid[i][j] is sorted in non-decreasing order.
 *
 */
public class MedianOfARowWiseSortedMatrix {

    // V0
    // IDEA: BINARY SEARCH ON THE VALUE, COUNTING WITH ONE UPPER BOUND PER ROW
    /**
     *  The median of an odd-sized collection is the smallest x with
     *  count(elements <= x) >= (m * n + 1) / 2, and that count is monotone in x -> binary
     *  search x over the value range. Each row is sorted, so a row's count is one upper bound.
     */
    /**
     * time = O(M log N log(max value))
     * space = O(1)
     */
    public int matrixMedian(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int needed = (rows * cols + 1) / 2;

        // the median lies between the smallest row start and the largest row end
        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;
        for (int[] row : grid) {
            low = Math.min(low, row[0]);
            high = Math.max(high, row[cols - 1]);
        }

        while (low < high) {
            int mid = low + (high - low) / 2;
            int countAtMost = 0;
            for (int[] row : grid) {
                countAtMost += upperBound(row, mid);
            }
            if (countAtMost >= needed) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    /** Number of values in the sorted row that are <= target. */
    private int upperBound(int[] row, int target) {
        int left = 0;
        int right = row.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (row[mid] > target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
}
