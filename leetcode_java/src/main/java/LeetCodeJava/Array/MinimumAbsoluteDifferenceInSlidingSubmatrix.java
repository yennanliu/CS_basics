package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-absolute-difference-in-sliding-submatrix/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * 3567. Minimum Absolute Difference in Sliding Submatrix
 * Medium
 *
 * You are given an m x n integer matrix grid and an integer k.
 *
 * For every contiguous k x k submatrix of grid, compute the minimum absolute
 * difference between any two distinct values within that submatrix.
 *
 * Return a 2D array ans of size (m - k + 1) x (n - k + 1), where ans[i][j] is the
 * minimum absolute difference in the submatrix whose top-left corner is (i, j) in grid.
 *
 * Note: If all elements in the submatrix have the same value, the answer will be 0.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,8],[3,-2]], k = 2
 * Output: [[2]]
 *
 * Example 2:
 *
 * Input: grid = [[3,-1]], k = 1
 * Output: [[0,0]]
 *
 * Example 3:
 *
 * Input: grid = [[1,-2,3],[2,3,5]], k = 2
 * Output: [[1,2]]
 *
 *
 * Constraints:
 *
 * 1 <= m == grid.length <= 30
 * 1 <= n == grid[i].length <= 30
 * -10^5 <= grid[i][j] <= 10^5
 * 1 <= k <= min(m, n)
 *
 */
public class MinimumAbsoluteDifferenceInSlidingSubmatrix {

    // V0
    // IDEA: BRUTE FORCE PER WINDOW + SORT THE DISTINCT VALUES
    //   after sorting, the closest pair of distinct values is always a pair of
    //   neighbours, so one scan over the sorted distinct values gives the minimum
    //   gap. the grid is at most 30 x 30, so re-collecting each window is cheap.
    /**
     * time = O(M * N * K^2 * log(K^2))
     * space = O(K^2)
     */
    public int[][] minAbsDiff(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] answer = new int[m - k + 1][n - k + 1];

        for (int i = 0; i + k <= m; i++) {
            for (int j = 0; j + k <= n; j++) {
                // collect the k x k window, then sort it
                int[] values = new int[k * k];
                int size = 0;
                for (int r = i; r < i + k; r++) {
                    for (int c = j; c < j + k; c++) {
                        values[size++] = grid[r][c];
                    }
                }
                Arrays.sort(values);

                // smallest gap between neighbouring DISTINCT values; 0 if all equal
                int best = Integer.MAX_VALUE;
                for (int t = 1; t < size; t++) {
                    if (values[t] != values[t - 1]) {
                        best = Math.min(best, values[t] - values[t - 1]);
                    }
                }
                answer[i][j] = (best == Integer.MAX_VALUE) ? 0 : best;
            }
        }
        return answer;
    }

    // V1
    // IDEA: SLIDE THE WINDOW ACROSS EACH ROW BAND WITH A COUNTED TreeSet
    //   for a fixed band of k rows, moving the window one column right removes the
    //   k cells of the leaving column and adds the k cells of the entering one.
    //   a value -> count map keeps duplicates honest; the TreeSet holds the distinct
    //   values in order, so the minimum neighbour gap is one walk over it.
    /**
     * time = O(M * N * K * log(K^2) + M * N * K^2)
     * space = O(K^2)
     */
    public int[][] minAbsDiff_1(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] answer = new int[m - k + 1][n - k + 1];

        for (int i = 0; i + k <= m; i++) {
            Map<Integer, Integer> count = new HashMap<>();
            TreeSet<Integer> distinct = new TreeSet<>();

            // first window of this band: columns 0 .. k-1
            for (int r = i; r < i + k; r++) {
                for (int c = 0; c < k; c++) {
                    addValue(grid[r][c], count, distinct);
                }
            }

            for (int j = 0; j + k <= n; j++) {
                if (j > 0) {
                    // slide right: drop column j-1, add column j+k-1
                    for (int r = i; r < i + k; r++) {
                        removeValue(grid[r][j - 1], count, distinct);
                        addValue(grid[r][j + k - 1], count, distinct);
                    }
                }

                // walk the distinct values in ascending order
                int best = 0;
                Integer previous = null;
                for (int value : distinct) {
                    if (previous != null) {
                        int gap = value - previous;
                        if (best == 0 || gap < best) {
                            best = gap;
                        }
                    }
                    previous = value;
                }
                answer[i][j] = best;
            }
        }
        return answer;
    }

    private void addValue(int value, Map<Integer, Integer> count, TreeSet<Integer> distinct) {
        count.put(value, count.getOrDefault(value, 0) + 1);
        distinct.add(value);
    }

    private void removeValue(int value, Map<Integer, Integer> count, TreeSet<Integer> distinct) {
        int left = count.get(value) - 1;
        if (left == 0) {
            count.remove(value);
            distinct.remove(value);
        } else {
            count.put(value, left);
        }
    }
}
