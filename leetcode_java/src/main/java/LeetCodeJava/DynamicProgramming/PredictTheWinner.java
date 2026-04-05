package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/predict-the-winner/description/
/**
 *  486. Predict the Winner
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given an integer array nums. Two players are playing a game with this array: player 1 and player 2.
 *
 * Player 1 and player 2 take turns, with player 1 starting first. Both players start the game with a score of 0. At each turn, the player takes one of the numbers from either end of the array (i.e., nums[0] or nums[nums.length - 1]) which reduces the size of the array by 1. The player adds the chosen number to their score. The game ends when there are no more elements in the array.
 *
 * Return true if Player 1 can win the game. If the scores of both players are equal, then player 1 is still the winner, and you should also return true. You may assume that both players are playing optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,2]
 * Output: false
 * Explanation: Initially, player 1 can choose between 1 and 2.
 * If he chooses 2 (or 1), then player 2 can choose from 1 (or 2) and 5. If player 2 chooses 5, then player 1 will be left with 1 (or 2).
 * So, final score of player 1 is 1 + 2 = 3, and player 2 is 5.
 * Hence, player 1 will never be the winner and you need to return false.
 * Example 2:
 *
 * Input: nums = [1,5,233,7]
 * Output: true
 * Explanation: Player 1 first chooses 1. Then player 2 has to choose between 5 and 7. No matter which number player 2 choose, player 1 can choose 233.
 * Finally, player 1 has more score (234) than player 2 (12), so you need to return True representing player1 can win.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 20
 * 0 <= nums[i] <= 107
 *
 */
public class PredictTheWinner {

    // V0
//    public boolean predictTheWinner(int[] nums) {
//
//    }

    // V0-1
    // IDEA: 2D DP (fixed by gemini)
    public boolean predictTheWinner_0_1(int[] nums) {
        int n = nums.length;
        if (n <= 1)
            return true;

        // dp[i][j] is the max relative score a player can get from nums[i...j]
        int[][] dp = new int[n][n];

        // Base case: only one number left, the player takes it
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // Fill the table for lengths 2 up to n
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;

                // Option 1: Take nums[i], then the other player gets dp[i+1][j]
                // Option 2: Take nums[j], then the other player gets dp[i][j-1]
                // We subtract the other player's max relative score from our pick
                dp[i][j] = Math.max(nums[i] - dp[i + 1][j],
                        nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1] >= 0;
    }


    // V0-2
    // IDEA: 2D DP (fixed by GPT)
    public boolean predictTheWinner_0_2(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n];

        // base case
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // fill dp
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = Math.max(
                        nums[i] - dp[i + 1][j],
                        nums[j] - dp[i][j - 1]);
            }
        }

        return dp[0][n - 1] >= 0;
    }


    // V1

    // V2

    // V3




}
