package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iii/

/**
 *  123. Best Time to Buy and Sell Stock III
 *  Hard
 *
 *  You are given an array prices where prices[i] is the price of a given stock on the ith day.
 *
 *  Find the maximum profit you can achieve. You may complete at most two transactions.
 *
 *  Note: You may not engage in multiple transactions simultaneously (i.e. you must sell the
 *  stock before you buy again).
 *
 *  Example 1:
 *  Input: prices = [3,3,5,0,0,3,1,4]
 *  Output: 6      // buy at 0 sell at 3 (profit 3), buy at 1 sell at 4 (profit 3)
 *
 *  Example 2:
 *  Input: prices = [1,2,3,4,5]
 *  Output: 4      // buy at 1 sell at 5
 *
 *  Example 3:
 *  Input: prices = [7,6,4,3,1]
 *  Output: 0      // no transaction is done
 *
 *  Constraints:
 *
 *   1 <= prices.length <= 10^5
 *   0 <= prices[i] <= 10^5
 */
public class BestTimeToBuyAndSellStockIII {

    // V0
    // IDEA: STATE MACHINE DP (4 states, rolling)
    //       buy1  = best balance after the 1st buy
    //       sell1 = best profit after the 1st sell
    //       buy2  = best balance after the 2nd buy
    //       sell2 = best profit after the 2nd sell   <- the answer
    /**
     * time = O(n)
     * space = O(1)
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int buy1 = Integer.MIN_VALUE;
        int sell1 = 0;
        int buy2 = Integer.MIN_VALUE;
        int sell2 = 0;
        for (int p : prices) {
            buy1 = Math.max(buy1, -p);
            sell1 = Math.max(sell1, buy1 + p);
            buy2 = Math.max(buy2, sell1 - p);
            sell2 = Math.max(sell2, buy2 + p);
        }
        return sell2;
    }

    // V1
    // IDEA: SPLIT POINT - best profit of one transaction in prefix [0..i]
    //       plus best profit of one transaction in suffix [i..n-1]
    /**
     * time = O(n)
     * space = O(n)
     */
    public int maxProfit_1(int[] prices) {
        if (prices == null || prices.length < 2) {
            return 0;
        }
        int n = prices.length;
        int[] leftBest = new int[n];   // max profit using 1 transaction within [0..i]
        int[] rightBest = new int[n];  // max profit using 1 transaction within [i..n-1]

        int minPrice = prices[0];
        for (int i = 1; i < n; i++) {
            minPrice = Math.min(minPrice, prices[i]);
            leftBest[i] = Math.max(leftBest[i - 1], prices[i] - minPrice);
        }

        int maxPrice = prices[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            maxPrice = Math.max(maxPrice, prices[i]);
            rightBest[i] = Math.max(rightBest[i + 1], maxPrice - prices[i]);
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            res = Math.max(res, leftBest[i] + rightBest[i]);
        }
        return res;
    }
}
