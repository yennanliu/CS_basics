package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 309. Best Time to Buy and Sell Stock with Cooldown
 * Medium
 * Topics
 * Companies
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 *
 * Find the maximum profit you can achieve. You may complete as many transactions as you like (i.e., buy one and sell one share of the stock multiple times) with the following restrictions:
 *
 * After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
 *
 *
 *
 * Example 1:
 *
 * Input: prices = [1,2,3,0,2]
 * Output: 3
 * Explanation: transactions = [buy, sell, cooldown, buy, sell]
 * Example 2:
 *
 * Input: prices = [1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= prices.length <= 5000
 * 0 <= prices[i] <= 1000
 *
 */
public class BestTimeToBuyAndSellStockWithCooldown {

    // V0
//    public int maxProfit(int[] prices) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/buy-and-sell-crypto-with-cooldown
    // IDEA:  RECURSION
    public int maxProfit_1_1(int[] prices) {

        return dfs(0, true, prices);
    }

    private int dfs(int i, boolean buying, int[] prices) {
        if (i >= prices.length) {
            return 0;
        }

        int cooldown = dfs(i + 1, buying, prices);
        if (buying) {
            int buy = dfs(i + 1, false, prices) - prices[i];
            return Math.max(buy, cooldown);
        } else {
            int sell = dfs(i + 2, true, prices) + prices[i];
            return Math.max(sell, cooldown);
        }
    }


    // V1-2
    // https://neetcode.io/problems/buy-and-sell-crypto-with-cooldown
    // IDEA: Dynamic Programming (Top-Down)
    private Map<String, Integer> dp = new HashMap<>();

    public int maxProfit_1_2(int[] prices) {
        return dfs_1_2(0, true, prices);
    }

    private int dfs_1_2(int i, boolean buying, int[] prices) {
        if (i >= prices.length) {
            return 0;
        }

        String key = i + "-" + buying;
        if (dp.containsKey(key)) {
            return dp.get(key);
        }

        int cooldown = dfs_1_2(i + 1, buying, prices);
        if (buying) {
            int buy = dfs_1_2(i + 1, false, prices) - prices[i];
            dp.put(key, Math.max(buy, cooldown));
        } else {
            int sell = dfs_1_2(i + 2, true, prices) + prices[i];
            dp.put(key, Math.max(sell, cooldown));
        }

        return dp.get(key);
    }


    // V1-3
    // https://neetcode.io/problems/buy-and-sell-crypto-with-cooldown
    // IDEA: Dynamic Programming (Bottom-Up)
    public int maxProfit_1_3(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n + 1][2];

        for (int i = n - 1; i >= 0; i--) {
            for (int buying = 1; buying >= 0; buying--) {
                if (buying == 1) {
                    int buy = dp[i + 1][0] - prices[i];
                    int cooldown = dp[i + 1][1];
                    dp[i][1] = Math.max(buy, cooldown);
                } else {
                    int sell = (i + 2 < n) ? dp[i + 2][1] + prices[i] : prices[i];
                    int cooldown = dp[i + 1][0];
                    dp[i][0] = Math.max(sell, cooldown);
                }
            }
        }

        return dp[0][1];
    }

    // V1-4
    // https://neetcode.io/problems/buy-and-sell-crypto-with-cooldown
    // IDEA: Dynamic Programming (Space Optimized)
    public int maxProfit_1_4(int[] prices) {
        int n = prices.length;
        int dp1_buy = 0, dp1_sell = 0;
        int dp2_buy = 0;

        for (int i = n - 1; i >= 0; i--) {
            int dp_buy = Math.max(dp1_sell - prices[i], dp1_buy);
            int dp_sell = Math.max(dp2_buy + prices[i], dp1_sell);
            dp2_buy = dp1_buy;
            dp1_buy = dp_buy;
            dp1_sell = dp_sell;
        }

        return dp1_buy;
    }


    // V2

}
