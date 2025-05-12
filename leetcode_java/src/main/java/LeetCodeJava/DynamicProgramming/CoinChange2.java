package LeetCodeJava.DynamicProgramming;

// https://leetcode.ca/all/518.html
// https://leetcode.com/problems/coin-change-2/

import java.util.Arrays;

/**
 * 518. Coin Change II
 * Medium
 * Topics
 * Companies
 * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
 *
 * Return the number of combinations that make up that amount. If that amount of money cannot be made up by any combination of the coins, return 0.
 *
 * You may assume that you have an infinite number of each kind of coin.
 *
 * The answer is guaranteed to fit into a signed 32-bit integer.
 *
 *
 *
 * Example 1:
 *
 * Input: amount = 5, coins = [1,2,5]
 * Output: 4
 * Explanation: there are four ways to make up the amount:
 * 5=5
 * 5=2+2+1
 * 5=2+1+1+1
 * 5=1+1+1+1+1
 * Example 2:
 *
 * Input: amount = 3, coins = [2]
 * Output: 0
 * Explanation: the amount of 3 cannot be made up just with coins of 2.
 * Example 3:
 *
 * Input: amount = 10, coins = [10]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= coins.length <= 300
 * 1 <= coins[i] <= 5000
 * All the values of coins are unique.
 * 0 <= amount <= 5000
 *
 *
 *
 */
public class CoinChange2 {

    // V0
//    public int change(int amount, int[] coins) {
//
//    }

    // V0-1
    // IDEA: BACKTRACK (gpt) (TLE)
    public int change_0_1(int amount, int[] coins) {
        return backtrack(coins, 0, amount);
    }

    private int backtrack(int[] coins, int index, int remaining) {
        if (remaining == 0)
            return 1; // valid combination
        if (remaining < 0 || index == coins.length)
            return 0; // invalid path

        // Choose coin at `index` or skip to next coin
        int withCoin = backtrack(coins, index, remaining - coins[index]);
        int withoutCoin = backtrack(coins, index + 1, remaining);

        return withCoin + withoutCoin;
    }

    // V0-2
    // IDEA: Backtracking + Memoization (Top-Down DP)  (gpt)
    public int change_0_2(int amount, int[] coins) {
        Integer[][] memo = new Integer[coins.length][amount + 1];
        return dfs(coins, 0, amount, memo);
    }

    private int dfs(int[] coins, int i, int rem, Integer[][] memo) {
        if (rem == 0)
            return 1;
        if (rem < 0 || i == coins.length)
            return 0;
        if (memo[i][rem] != null)
            return memo[i][rem];

        int use = dfs(coins, i, rem - coins[i], memo); // take coin[i]
        int skip = dfs(coins, i + 1, rem, memo); // skip coin[i]

        memo[i][rem] = use + skip;
        return memo[i][rem];
    }

    // V1-1
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Recursion
    public int change_1_1(int amount, int[] coins) {
        Arrays.sort(coins);

        return dfs(coins, 0, amount);
    }

    private int dfs(int[] coins, int i, int a) {
        if (a == 0) {
            return 1;
        }
        if (i >= coins.length) {
            return 0;
        }

        int res = 0;
        if (a >= coins[i]) {
            res = dfs(coins, i + 1, a);
            res += dfs(coins, i, a - coins[i]);
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Top-Down)
    public int change_1_2(int amount, int[] coins) {
        Arrays.sort(coins);
        int[][] memo = new int[coins.length + 1][amount + 1];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }

        return dfs(0, amount, coins, memo);
    }

    private int dfs(int i, int a, int[] coins, int[][] memo) {
        if (a == 0) return 1;
        if (i >= coins.length) return 0;
        if (memo[i][a] != -1) return memo[i][a];

        int res = 0;
        if (a >= coins[i]) {
            res = dfs(i + 1, a, coins, memo);
            res += dfs(i, a - coins[i], coins, memo);
        }
        memo[i][a] = res;
        return res;
    }


    // V1-3
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Bottom-Up)
    public int change_1_3(int amount, int[] coins) {
        int n = coins.length;
        Arrays.sort(coins);
        int[][] dp = new int[n + 1][amount + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = 1;
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int a = 0; a <= amount; a++) {
                if (a >= coins[i]) {
                    dp[i][a] = dp[i + 1][a];
                    dp[i][a] += dp[i][a - coins[i]];
                }
            }
        }

        return dp[0][amount];
    }


    // V1-4
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Space Optimized)
    public int change_1_4(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int i = coins.length - 1; i >= 0; i--) {
            int[] nextDP = new int[amount + 1];
            nextDP[0] = 1;

            for (int a = 1; a <= amount; a++) {
                nextDP[a] = dp[a];
                if (a - coins[i] >= 0) {
                    nextDP[a] += nextDP[a - coins[i]];
                }
            }
            dp = nextDP;
        }
        return dp[amount];
    }


    // V1-5
    // https://neetcode.io/problems/coin-change-ii
    // IDEA: Dynamic Programming (Optimal)
    public int change_1_5(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for (int i = coins.length - 1; i >= 0; i--)
            for (int a = 1; a <= amount; a++)
                dp[a] = dp[a] + (coins[i] <= a ? dp[a - coins[i]] : 0);
        return dp[amount];
    }

    // V2

}
