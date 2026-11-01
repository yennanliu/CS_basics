package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/palindrome-removal/description/
// https://leetcode.ca/2019-04-29-1246-Palindrome-Removal/

/**
 * 1246. Palindrome Removal
 * Hard
 *
 * You are given an integer array arr.
 *
 * In one move, you can select a palindromic subarray arr[i], arr[i + 1], ..., arr[j] where
 * i <= j, and remove that subarray from the given array. Note that after removing a
 * subarray, the elements on the left and on the right of that subarray move to fill the
 * gap left by the removal.
 *
 * Return the minimum number of moves needed to remove all numbers from the array.
 *
 *
 * Example 1:
 *
 * Input: arr = [1,2]
 * Output: 2
 *
 * Example 2:
 *
 * Input: arr = [1,3,4,1,5]
 * Output: 3
 * Explanation: Remove [4] then remove [1,3,1] then remove [5].
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 100
 * 1 <= arr[i] <= 20
 *
 */
public class PalindromeRemoval {

    // V0
    // IDEA: INTERVAL DP
    /**
     * dp[i][j] = min moves to remove everything in arr[i..j]
     *
     * base: dp[i][i] = 1; dp[i][i+1] = 1 if arr[i] == arr[i+1] else 2
     * else:
     *   dp[i][j] = dp[i+1][j-1]                      if arr[i] == arr[j]
     *     (the matching ends ride along with the palindrome that removes the inner part LAST)
     *   dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j])   for k in [i, j)   (split in two)
     */
    /**
     * time = O(N^3)
     * space = O(N^2)
     */
    public int minimumMoves(int[] arr) {
        int n = arr.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        // i goes down and j goes up, so every shorter interval inside [i, j] is ready
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (j == i + 1) {
                    dp[i][j] = (arr[i] == arr[j]) ? 1 : 2;
                    continue;
                }
                int best = Integer.MAX_VALUE;
                if (arr[i] == arr[j]) {
                    best = dp[i + 1][j - 1];
                }
                for (int k = i; k < j; k++) {
                    best = Math.min(best, dp[i][k] + dp[k + 1][j]);
                }
                dp[i][j] = best;
            }
        }
        return dp[0][n - 1];
    }
}
