package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/jump-game-v/description/

import java.util.Arrays;

/**
 * 1340. Jump Game V
 * Hard
 *
 * Given an array of integers arr and an integer d. In one step you can jump from index i
 * to index:
 *
 * - i + x where: i + x < arr.length and 0 < x <= d.
 * - i - x where: i - x >= 0 and 0 < x <= d.
 *
 * In addition, you can only jump from index i to index j if arr[i] > arr[j]
 * and arr[i] > arr[k] for all indices k between i and j
 * (More formally min(i, j) < k < max(i, j)).
 *
 * You can choose any index of the array and start jumping.
 * Return the maximum number of indices you can visit.
 *
 * Notice that you can not jump outside of the array at any time.
 *
 *
 * Example 1:
 *
 * Input: arr = [6,4,14,6,8,13,9,7,10,6,12], d = 2
 * Output: 4
 * Explanation: You can start at index 10. You can jump 10 --> 8 --> 6 --> 7 as shown.
 * Note that if you start at index 6 you can only jump to index 7.
 * You cannot jump to index 5 because 13 > 9. You cannot jump to index 4 because
 * index 5 is between index 4 and 6 and 13 > 9.
 * Similarly You cannot jump from index 3 to index 2 or index 1.
 *
 * Example 2:
 *
 * Input: arr = [3,3,3,3,3], d = 3
 * Output: 1
 * Explanation: You can start at any index. You always cannot jump to any index.
 *
 * Example 3:
 *
 * Input: arr = [7,6,5,4,3,2,1], d = 1
 * Output: 7
 * Explanation: Start at index 0. You can visit all the indicies.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 1000
 * 1 <= arr[i] <= 10^5
 * 1 <= d <= arr.length
 *
 */
public class JumpGameV {

    // V0
    // IDEA: SORT INDICES BY VALUE + DP
    /**
     * dp[i] = max number of indices visitable when STARTING at index i
     *
     * a jump always lands on a STRICTLY SMALLER value, so processing indices in
     * ascending value order means every reachable dp[j] is already final.
     * The "no taller bar in between" rule: scan outward from i and stop at the first
     * arr[j] >= arr[i] (or once |i - j| > d).
     *
     * dp[i] = max(dp[i], 1 + dp[j]) for each reachable j; base dp[i] = 1
     */
    /**
     * time = O(N log N + N * D)
     * space = O(N)
     */
    public int maxJumps(int[] arr, int d) {
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);

        // indices ordered from the smallest value to the largest
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(arr[a], arr[b]));

        for (int i : order) {
            // scan left until out of range or blocked by a bar that is not lower
            for (int j = i - 1; j >= 0 && i - j <= d && arr[j] < arr[i]; j--) {
                dp[i] = Math.max(dp[i], 1 + dp[j]);
            }
            // scan right, same rule
            for (int j = i + 1; j < n && j - i <= d && arr[j] < arr[i]; j++) {
                dp[i] = Math.max(dp[i], 1 + dp[j]);
            }
        }

        int best = 0;
        for (int visits : dp) {
            best = Math.max(best, visits);
        }
        return best;
    }

    // V1
    // IDEA: MEMOIZED DFS (TOP-DOWN, SAME RECURRENCE, NO SORT NEEDED)
    /**
     * time = O(N * D)
     * space = O(N)
     */
    public int maxJumps_1(int[] arr, int d) {
        int n = arr.length;
        // 0 = not computed yet (every real answer is >= 1)
        int[] memo = new int[n];
        int best = 0;
        for (int i = 0; i < n; i++) {
            best = Math.max(best, dfs(arr, d, i, memo));
        }
        return best;
    }

    /** Max indices visitable starting from index i. */
    private int dfs(int[] arr, int d, int i, int[] memo) {
        if (memo[i] != 0) {
            return memo[i];
        }
        int best = 1;
        for (int j = i - 1; j >= 0 && i - j <= d && arr[j] < arr[i]; j--) {
            best = Math.max(best, 1 + dfs(arr, d, j, memo));
        }
        for (int j = i + 1; j < arr.length && j - i <= d && arr[j] < arr[i]; j++) {
            best = Math.max(best, 1 + dfs(arr, d, j, memo));
        }
        memo[i] = best;
        return best;
    }
}
