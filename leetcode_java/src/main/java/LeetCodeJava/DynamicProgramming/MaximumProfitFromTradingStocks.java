package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-profit-from-trading-stocks/description/

/**
 * 2291. Maximum Profit From Trading Stocks
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given two 0-indexed integer arrays of the same length present and future where present[i] is the current price of the ith stock and future[i] is the price of the ith stock a year in the future. You may buy each stock at most once. You are also given an integer budget representing the amount of money you currently have.
 *
 * Return the maximum amount of profit you can make.
 *
 *
 * Example 1:
 *
 * Input: present = [5,4,6,2,3], future = [8,5,4,3,5], budget = 10
 * Output: 6
 * Explanation: One possible way to maximize your profit is to:
 * Buy the 0th, 3rd, and 4th stocks for a total of 5 + 2 + 3 = 10.
 * Next year, sell all three stocks for a total of 8 + 3 + 5 = 16.
 * The profit you made is 16 - 10 = 6.
 * It can be shown that the maximum profit you can make is 6.
 *
 * Example 2:
 *
 * Input: present = [2,2,5], future = [3,4,10], budget = 6
 * Output: 5
 * Explanation: The only possible way to maximize your profit is to:
 * Buy the 2nd stock, and make a profit of 10 - 5 = 5.
 * It can be shown that the maximum profit you can make is 5.
 *
 * Example 3:
 *
 * Input: present = [3,3,12], future = [0,3,15], budget = 10
 * Output: 0
 * Explanation: One possible way to maximize your profit is to:
 * Buy the 1st stock, and make a profit of 3 - 3 = 0.
 * It can be shown that the maximum profit you can make is 0.
 *
 *
 * Constraints:
 *
 * n == present.length == future.length
 * 1 <= n <= 1000
 * 0 <= present[i], future[i] <= 100
 * 0 <= budget <= 1000
 *
 */
public class MaximumProfitFromTradingStocks {

    // V0
    // IDEA: 0/1 KNAPSACK — WEIGHT = present[i], VALUE = future[i] - present[i]
    /**
     * time = O(N * B), B = budget
     * space = O(B)
     */
    public int maximumProfit(int[] present, int[] future, int budget) {
        // dp[c] = max profit spending at most c
        int[] dp = new int[budget + 1];

        for (int i = 0; i < present.length; i++) {
            int cost = present[i];
            int gain = future[i] - present[i];
            // a stock that does not gain (or costs more than the budget) is never worth buying
            if (gain <= 0 || cost > budget) {
                continue;
            }
            // iterate capacity downward so each stock is bought at most once
            for (int c = budget; c >= cost; c--) {
                dp[c] = Math.max(dp[c], dp[c - cost] + gain);
            }
        }
        return dp[budget];
    }
}
