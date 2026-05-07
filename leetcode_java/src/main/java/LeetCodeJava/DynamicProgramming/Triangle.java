package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/triangle/description/

import java.util.List;

/**
 * 120. Triangle
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a triangle array, return the minimum path sum from top to bottom.
 *
 * For each step, you may move to an adjacent number of the row below. More formally, if you are on index i on the current row, you may move to either index i or index i + 1 on the next row.
 *
 *
 *
 * Example 1:
 *
 * Input: triangle = [[2],[3,4],[6,5,7],[4,1,8,3]]
 * Output: 11
 * Explanation: The triangle looks like:
 *    2
 *   3 4
 *  6 5 7
 * 4 1 8 3
 * The minimum path sum from top to bottom is 2 + 3 + 5 + 1 = 11 (underlined above).
 * Example 2:
 *
 * Input: triangle = [[-10]]
 * Output: -10
 *
 *
 * Constraints:
 *
 * 1 <= triangle.length <= 200
 * triangle[0].length == 1
 * triangle[i].length == triangle[i - 1].length + 1
 * -104 <= triangle[i][j] <= 104
 *
 *
 * Follow up: Could you do this using only O(n) extra space, where n is the total number of rows in the triangle?
 *
 *
 *
 */
public class Triangle {

    // V0
//    public int minimumTotal(List<List<Integer>> triangle) {
//
//    }


    // V1-1
    // IDEA: 2D DP (gpt)
    // DP
    public int minimumTotal_1_1(List<List<Integer>> triangle) {

        // edge
        if (triangle == null || triangle.size() == 0) {
            return 0;
        }

        int l = triangle.size();

        // dp[i][j] = minimum path sum to reach triangle[i][j]
        int[][] dp = new int[l][l];

        // init
        dp[0][0] = triangle.get(0).get(0);

        // build dp
        for (int y = 1; y < l; y++) {

            List<Integer> row = triangle.get(y);

            for (int x = 0; x < row.size(); x++) {

                int val = row.get(x);

                // left edge
                if (x == 0) {
                    dp[y][x] = dp[y - 1][x] + val;
                }
                // right edge
                else if (x == row.size() - 1) {
                    dp[y][x] = dp[y - 1][x - 1] + val;
                }
                // middle
                else {
                    dp[y][x] = Math.min(dp[y - 1][x - 1],
                            dp[y - 1][x]) + val;
                }
            }
        }

        // answer = min value in last row
        int minPathSum = Integer.MAX_VALUE;

        for (int x = 0; x < l; x++) {
            minPathSum = Math.min(minPathSum, dp[l - 1][x]);
        }

        return minPathSum;
    }


    // V1-2
    // IDEA: 1D DP (gemini)
    public int minimumTotal_1_2(List<List<Integer>> triangle) {
        int n = triangle.size();
        // Use a 1D DP array initialized with the bottom row of the triangle
        int[] dp = new int[n];
        List<Integer> bottomRow = triangle.get(n - 1);

        for (int i = 0; i < n; i++) {
            dp[i] = bottomRow.get(i);
        }

        // Start from the second-to-last row and move upwards
        for (int y = n - 2; y >= 0; y--) {
            List<Integer> currentRow = triangle.get(y);
            for (int x = 0; x <= y; x++) {
                // The minimum path to current cell is its own value
                // plus the smaller of the two adjacent values below it
                dp[x] = currentRow.get(x) + Math.min(dp[x], dp[x + 1]);
            }
        }

        // The result is stored in the first element
        return dp[0];
    }



    // V2
    // IDEA: DP
    // https://leetcode.com/problems/triangle/solutions/7221386/solution-by-la_castille-s82u/
    public int minimumTotal_2(List<List<Integer>> tri) {
        for (int i = tri.size() - 2; i >= 0; i--)
            for (int j = 0; j < tri.get(i).size(); j++)
                tri.get(i).set(j, tri.get(i).get(j) + Math.min(
                        tri.get(i + 1).get(j),
                        tri.get(i + 1).get(j + 1)));
        return tri.get(0).get(0);
    }


    // V3
    // IDEA: DP
    // https://leetcode.com/problems/triangle/solutions/6097942/video-explanation-by-niits-d8vr/
    public int minimumTotal_3(List<List<Integer>> triangle) {
        int row = triangle.size();
        int[] memo = new int[row];

        for (int i = 0; i < row; i++) {
            memo[i] = triangle.get(row - 1).get(i);
        }

        for (int r = row - 2; r >= 0; r--) {
            for (int c = 0; c <= r; c++) {
                memo[c] = Math.min(memo[c], memo[c + 1]) + triangle.get(r).get(c);
            }
        }

        return memo[0];
    }






}
