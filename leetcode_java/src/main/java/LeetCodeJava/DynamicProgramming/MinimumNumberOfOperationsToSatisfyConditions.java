package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  3122. Minimum Number of Operations to Satisfy Conditions
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given a 2D matrix grid of size m x n. In one operation, you can change the value of any cell to any non-negative number. You need to perform some operations such that each cell grid[i][j] is:
 *
 * Equal to the cell below it, i.e. grid[i][j] == grid[i + 1][j] (if it exists).
 * Different from the cell to its right, i.e. grid[i][j] != grid[i][j + 1] (if it exists).
 * Return the minimum number of operations needed.
 *
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,0,2],[1,0,2]]
 *
 * Output: 0
 *
 * Explanation:
 *
 *
 *
 * All the cells in the matrix already satisfy the properties.
 *
 * Example 2:
 *
 * Input: grid = [[1,1,1],[0,0,0]]
 *
 * Output: 3
 *
 * Explanation:
 *
 *
 *
 * The matrix becomes [[1,0,1],[1,0,1]] which satisfies the properties, by doing these 3 operations:
 *
 * Change grid[1][0] to 1.
 * Change grid[0][1] to 0.
 * Change grid[1][2] to 1.
 * Example 3:
 *
 * Input: grid = [[1],[2],[3]]
 *
 * Output: 2
 *
 * Explanation:
 *
 *
 *
 * There is a single column. We can change the value to 1 in each cell using 2 operations.
 *
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 1000
 * 0 <= grid[i][j] <= 9
 *
 */
public class MinimumNumberOfOperationsToSatisfyConditions {

    // V0
//    public int minimumOperations(int[][] grid) {
//
//    }

    // V1
    // IDEA: DP
    // https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/solutions/5052842/java-python-dp-optimized-solution-with-e-9bzi/
    public int minimumOperations_1(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][] count = new int[m][10];
        for (int column = 0; column < m; column++) {
            for (int value = 0; value < 10; value++) {
                for (int j = 0; j < n; j++) {
                    count[column][value] += grid[j][column] != value ? 1 : 0;
                }
            }
        }
        int[][] dp = new int[m + 1][10];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        Arrays.fill(dp[m], 0);
        for (int column = m - 1; column >= 0; column--) {
            for (int value = 0; value < 10; value++) {
                for (int i = 0; i < 10; i++) {
                    if (i != value) {
                        dp[column][value] = Math.min(dp[column][value], count[column][value] + dp[column + 1][i]);
                    }
                }
            }
        }
        return Arrays.stream(dp[0]).min().getAsInt();
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/solutions/7372556/column-based-dp-java-solution-by-mainoo1-y3to/
    int[][] dp;
    public int minimumOperations_2(int[][] grid) {

        int n = grid[0].length;
        dp = new int[grid[0].length][10];
        int[][] colOperations = new int[n][10];
        int[][] freq = new int[n][10];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < grid.length; j++) {
                freq[i][grid[j][i]]++;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= 9; j++) {
                // no of extra operations we would neeed to do to make that column all equal to j
                colOperations[i][j] = grid.length - freq[i][j];
            }
        }
        dp = new int[n][10];
        for (int[] r : dp)
            Arrays.fill(r, -1);
        return f(0, -1, colOperations, grid);
    }

    public int f(int col, int prevVal, int[][] colOperations, int[][] grid) {
        if (col >= grid[0].length)
            return 0;
        if (prevVal != -1 && dp[col][prevVal] != -1)
            return dp[col][prevVal];
        int op = Integer.MAX_VALUE;
        if (prevVal != -1) {

            for (int i = 0; i <= 9; i++) {
                if (i == prevVal)
                    continue;
                int curr = colOperations[col][i] + f(col + 1, i, colOperations, grid);
                op = Math.min(op, curr);
            }
        } else {
            for (int i = 0; i <= 9; i++) {
                int curr = colOperations[col][i] + f(col + 1, i, colOperations, grid);
                op = Math.min(op, curr);
            }
        }
        if (prevVal == -1)
            return op;
        else
            return dp[col][prevVal] = op;
    }


    // V3
    // IDEA: PQ
    // https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/solutions/5087884/java-priority-queue-solution-by-adarshsp-plqi/
    private PriorityQueue<List<Integer>> dfs(int[][] grid, int j) {
        if (j == grid[0].length - 1) {
            PriorityQueue<List<Integer>> queue = new PriorityQueue<>((a, b) -> Integer.compare(a.get(0), b.get(0)));
            for (int value = 0; value <= 9; value++) {
                int count = 0;
                for (int i = 0; i < grid.length; i++) {
                    if (grid[i][j] != value)
                        count++;
                }
                queue.add(new ArrayList<>(Arrays.asList(count, value)));
            }
            return queue;
        }
        PriorityQueue<List<Integer>> next = dfs(grid, j + 1);
        PriorityQueue<List<Integer>> queue = new PriorityQueue<>((a, b) -> Integer.compare(a.get(0), b.get(0)));
        for (int value = 0; value <= 9; value++) {
            int count = 0;
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][j] != value)
                    count++;
            }
            List<Integer> arr = next.poll();
            if (arr.get(1) == value) {
                count += next.peek().get(0);
            } else {
                count += arr.get(0);
            }
            next.add(arr);
            queue.add(new ArrayList<>(Arrays.asList(count, value)));
        }
        return queue;
    }

    public int minimumOperations_3(int[][] grid) {
        PriorityQueue<List<Integer>> queue = dfs(grid, 0);
        return queue.peek().get(0);
    }

    // V4
    // IDEA: DP
    // https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/solutions/5052743/dp-tabulation-time-omn-space-on-java-by-4ldud/
    private int[][] dp_4;

    public int minimumOperations_4(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp_4 = new int[n][11];

        for (int[] row : dp_4) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        for (int j = 0; j < 10; j++) {
            int sum = 0;
            for (int i = 0; i < m; i++) {
                sum += grid[i][0] != j ? 1 : 0;
            }
            dp_4[0][j] = sum;
        }

        for (int col = 1; col < n; col++) {
            for (int j = 0; j < 10; j++) {
                int sum = 0;
                for (int i = 0; i < m; i++) {
                    sum += grid[i][col] != j ? 1 : 0;
                }
                for (int k = 0; k < 10; k++) {
                    if (j != k) {
                        dp_4[col][j] = Math.min(dp_4[col][j], dp_4[col - 1][k] + sum);
                    }
                }
            }
        }

        int minOps = Integer.MAX_VALUE;
        for (int j = 0; j < 10; j++) {
            minOps = Math.min(minOps, dp_4[n - 1][j]);
        }

        return minOps;
    }



}
