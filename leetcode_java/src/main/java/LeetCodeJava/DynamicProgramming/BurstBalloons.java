package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/burst-balloons/
/**
 * 312. Burst Balloons
 * Hard
 * Topics
 * Companies
 * You are given n balloons, indexed from 0 to n - 1. Each balloon is painted with a number on it represented by an array nums. You are asked to burst all the balloons.
 *
 * If you burst the ith balloon, you will get nums[i - 1] * nums[i] * nums[i + 1] coins. If i - 1 or i + 1 goes out of bounds of the array, then treat it as if there is a balloon with a 1 painted on it.
 *
 * Return the maximum coins you can collect by bursting the balloons wisely.
 *
 * Example 1:
 *
 * Input: nums = [3,1,5,8]
 * Output: 167
 * Explanation:
 * nums = [3,1,5,8] --> [3,5,8] --> [3,8] --> [8] --> []
 * coins =  3*1*5    +   3*5*8   +  1*3*8  + 1*8*1 = 167
 * Example 2:
 *
 * Input: nums = [1,5]
 * Output: 10
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 300
 * 0 <= nums[i] <= 100
 *
 *
 */
public class BurstBalloons {

    // V0
//    public int maxCoins(int[] nums) {
//
//    }

    // V0-1
    // IDEA: DP (gpt)
    public int maxCoins_0_1(int[] nums) {
        int n = nums.length;

        // Add 1 before and after to handle boundary cases easily
        int[] balloons = new int[n + 2];
        balloons[0] = balloons[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            balloons[i + 1] = nums[i];
        }

        // Memoization table
        int[][] dp = new int[n + 2][n + 2];

        // Compute max coins from (left, right)
        return burst(balloons, dp, 1, n);
    }

    private int burst(int[] balloons, int[][] dp, int left, int right) {
        if (left > right)
            return 0;
        if (dp[left][right] > 0)
            return dp[left][right];

        int maxCoins = 0;

        // Try bursting every balloon in the interval [left, right]
        for (int i = left; i <= right; i++) {
            int coins = balloons[left - 1] * balloons[i] * balloons[right + 1]; // current burst
            int leftCoins = burst(balloons, dp, left, i - 1); // recursively burst left side
            int rightCoins = burst(balloons, dp, i + 1, right); // recursively burst right side

            maxCoins = Math.max(maxCoins, coins + leftCoins + rightCoins);
        }

        dp[left][right] = maxCoins;
        return maxCoins;
    }

    // V1-1
    // https://neetcode.io/problems/burst-balloons
    // IDEA:  Brute Force (Recursion)
    public int maxCoins_1_1(int[] nums) {
        int[] newNums = new int[nums.length + 2];
        newNums[0] = newNums[nums.length + 1] = 1;
        for (int i = 0; i < nums.length; i++) {
            newNums[i + 1] = nums[i];
        }

        return dfs(newNums);
    }

    public int dfs(int[] nums) {
        if (nums.length == 2) {
            return 0;
        }

        int maxCoins = 0;
        for (int i = 1; i < nums.length - 1; i++) {
            int coins = nums[i - 1] * nums[i] * nums[i + 1];
            int[] newNums = new int[nums.length - 1];
            for (int j = 0, k = 0; j < nums.length; j++) {
                if (j != i) {
                    newNums[k++] = nums[j];
                }
            }
            coins += dfs(newNums);
            maxCoins = Math.max(maxCoins, coins);
        }
        return maxCoins;
    }


    // V1-2
    // https://neetcode.io/problems/burst-balloons
    // IDEA: Dynamic Programming (Top-Down)
    public int maxCoins_1_2(int[] nums) {
        int n = nums.length;
        int[] newNums = new int[n + 2];
        newNums[0] = newNums[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            newNums[i + 1] = nums[i];
        }

        int[][] dp = new int[n + 2][n + 2];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dp[i][j] = -1;
            }
        }

        return dfs(newNums, 1, newNums.length - 2, dp);
    }

    public int dfs(int[] nums, int l, int r, int[][] dp) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r] != -1) {
            return dp[l][r];
        }

        dp[l][r] = 0;
        for (int i = l; i <= r; i++) {
            int coins = nums[l - 1] * nums[i] * nums[r + 1];
            coins += dfs(nums, l, i - 1, dp) + dfs(nums, i + 1, r, dp);
            dp[l][r] = Math.max(dp[l][r], coins);
        }
        return dp[l][r];
    }


    // V1-3
    // https://neetcode.io/problems/burst-balloons
    // IDEA: Dynamic Programming (Bottom-Up)
    public int maxCoins_1_3(int[] nums) {
        int n = nums.length;
        int[] newNums = new int[n + 2];
        newNums[0] = newNums[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            newNums[i + 1] = nums[i];
        }

        int[][] dp = new int[n + 2][n + 2];
        for (int l = n; l >= 1; l--) {
            for (int r = l; r <= n; r++) {
                for (int i = l; i <= r; i++) {
                    int coins = newNums[l - 1] * newNums[i] * newNums[r + 1];
                    coins += dp[l][i - 1] + dp[i + 1][r];
                    dp[l][r] = Math.max(dp[l][r], coins);
                }
            }
        }

        return dp[1][n];
    }


    // V2

}
