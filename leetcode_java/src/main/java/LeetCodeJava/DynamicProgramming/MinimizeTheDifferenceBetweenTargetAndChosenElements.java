package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimize-the-difference-between-target-and-chosen-elements/description/
/**
 * 1981. Minimize the Difference Between Target and Chosen Elements
 * Medium
 *
 * You are given an m x n integer matrix mat and an integer target.
 *
 * Choose one integer from each row in the matrix such that the absolute difference between
 * target and the sum of the chosen elements is minimized.
 *
 * Return the minimum absolute difference.
 *
 * The absolute difference between two numbers a and b is the absolute value of a - b.
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,2,3],[4,5,6],[7,8,9]], target = 13
 * Output: 0
 * Explanation: Choose 1, 5 and 7; the sum is 13, which equals the target.
 *
 * Example 2:
 *
 * Input: mat = [[1],[2],[3]], target = 100
 * Output: 94
 * Explanation: The only choice sums to 6, and the absolute difference is 94.
 *
 * Example 3:
 *
 * Input: mat = [[1,2,9,8,7]], target = 6
 * Output: 1
 * Explanation: The best choice is to choose 7 from the first row.
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 70
 * 1 <= mat[i][j] <= 70
 * 1 <= target <= 800
 *
 */
public class MinimizeTheDifferenceBetweenTargetAndChosenElements {

    // V0
    // IDEA: GROUPED KNAPSACK OVER REACHABLE SUMS (ONE GROUP PER ROW)
    /**
     *  Only WHICH sums are reachable matters, not how many ways.
     *
     *  DP def:
     *     - reachable[s] = true if some choice from the rows processed so far sums to s
     *
     *  DP eq (next row):
     *     - nextReachable[s + x] = true  for every reachable s and every value x in the row
     *
     *  The total is at most 70 * 70 = 4900, so the array stays small.
     *  NOTE !!! the best sum can overshoot the target as easily as undershoot it,
     *           so every reachable sum is scanned at the end.
     *
     *  time = O(M * N * S), S = max total sum (4900)
     *  space = O(S)
     */
    public int minimizeTheDifference(int[][] mat, int target) {
        // largest sum any choice can reach
        int maxSum = 0;
        for (int[] row : mat) {
            int rowMax = 0;
            for (int value : row) {
                rowMax = Math.max(rowMax, value);
            }
            maxSum += rowMax;
        }

        boolean[] reachable = new boolean[maxSum + 1];
        reachable[0] = true;  // before any row, only the empty sum exists

        for (int[] row : mat) {
            boolean[] nextReachable = new boolean[maxSum + 1];
            for (int sum = 0; sum <= maxSum; sum++) {
                if (!reachable[sum]) {
                    continue;
                }
                // exactly one value must be taken from this row
                for (int value : row) {
                    if (sum + value <= maxSum) {
                        nextReachable[sum + value] = true;
                    }
                }
            }
            reachable = nextReachable;
        }

        int best = Integer.MAX_VALUE;
        for (int sum = 0; sum <= maxSum; sum++) {
            if (reachable[sum]) {
                best = Math.min(best, Math.abs(sum - target));
            }
        }
        return best;
    }
}
