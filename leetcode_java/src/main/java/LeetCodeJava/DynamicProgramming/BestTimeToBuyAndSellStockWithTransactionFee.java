package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/description/
/**
 * 714. Best Time to Buy and Sell Stock with Transaction Fee
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an array prices where prices[i] is the price of a given stock on the ith day, and an integer fee representing a transaction fee.
 *
 * Find the maximum profit you can achieve. You may complete as many transactions as you like, but you need to pay the transaction fee for each transaction.
 *
 * Note:
 *
 * You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
 * The transaction fee is only charged once for each stock purchase and sale.
 *
 *
 * Example 1:
 *
 * Input: prices = [1,3,2,8,4,9], fee = 2
 * Output: 8
 * Explanation: The maximum profit can be achieved by:
 * - Buying at prices[0] = 1
 * - Selling at prices[3] = 8
 * - Buying at prices[4] = 4
 * - Selling at prices[5] = 9
 * The total profit is ((8 - 1) - 2) + ((9 - 4) - 2) = 8.
 * Example 2:
 *
 * Input: prices = [1,3,7,5,10,3], fee = 3
 * Output: 6
 *
 *
 * Constraints:
 *
 * 1 <= prices.length <= 5 * 104
 * 1 <= prices[i] < 5 * 104
 * 0 <= fee < 5 * 104
 *
 *
 */
public class BestTimeToBuyAndSellStockWithTransactionFee {

  // V0
  //    public int maxProfit(int[] prices, int fee) {
  //
  //    }

  // V0-1
  // IDEA: DP (gpt)
  /**
   * Solution Explanation:
   *
   *
   *  -  Use two variables to represent the state:
   * 	  1. hold: The maximum profit achievable
   * 	           while holding a stock at day i.
   *
   * 	  2. cash: The maximum profit achievable
   * 	           while not holding a stock at day i.
   *
   *  - Transition equations:
   * 	- If holding a stock:
   *       hold = max(hold, cash - price[i])
   *
   *       NOTE: 2 cases we hold th stock: 1) already hold from previous day 2) buy a new stock today
   *       (`hold`: You already held the stock from a previous day -> If you decided not to make any changes today, then the profit remains the same as the previous hold.)
   *       (`cash - price[i]`: You buy the stock today -> To buy the stock today, you need to spend money, reducing your profit. The cost to buy the stock is prices[i]. However, the amount of money you can spend is the maximum profit you had when you were not holding a stock previously (cash).)
   *
   * (You either keep holding or buy a new stock.)
   * 	- If not holding a stock:
   *       cash = max(cash, hold + price[i] - fee)
   *
   *
   * (You either keep not holding or sell the stock and pay the fee.)
   * 	- Initialize:
   * 	   - hold = -prices[0] (If you buy the stock on the first day).
   * 	   -  cash = 0 (You haven’t made any transactions yet).
   *
   */
  /**
   *  Example Walkthrough:
   *
   * Input:
   * 	•	Prices: [1, 3, 2, 8, 4, 9]
   * 	•	Fee: 2
   *
   * Steps:
   * 	1.	Day 0:
   * 	•	hold = -1 (Buy the stock at price 1).
   * 	•	cash = 0.
   * 	2.	Day 1:
   * 	•	cash = max(0, -1 + 3 - 2) = 0 (No selling since profit is 0).
   * 	•	hold = max(-1, 0 - 3) = -1 (No buying since it’s already better to hold).
   * 	3.	Day 2:
   * 	•	cash = max(0, -1 + 2 - 2) = 0.
   * 	•	hold = max(-1, 0 - 2) = -1.
   * 	4.	Day 3:
   * 	•	cash = max(0, -1 + 8 - 2) = 5 (Sell at price 8).
   * 	•	hold = max(-1, 5 - 8) = -1.
   * 	5.	Day 4:
   * 	•	cash = max(5, -1 + 4 - 2) = 5.
   * 	•	hold = max(-1, 5 - 4) = 1.
   * 	6.	Day 5:
   * 	•	cash = max(5, 1 + 9 - 2) = 8 (Sell at price 9).
   * 	•	hold = max(1, 5 - 9) = 1.
   *
   * Output:
   * 	•	cash = 8 (Max profit).
   *
   */
  public int maxProfit_0_1(int[] prices, int fee) {
        // Edge case
        if (prices == null || prices.length == 0) {
            return 0;
        }

        // Initialize states
        int hold = -prices[0]; // Maximum profit when holding a stock
        int cash = 0; // Maximum profit when not holding a stock

        // Iterate through prices
        for (int i = 1; i < prices.length; i++) {
            /**
             *  NOTE !!! there are 2 dp equations (e.g. cash, hold)
             */
            // Update cash and hold states
            cash = Math.max(cash, hold + prices[i] - fee); // Sell the stock
            hold = Math.max(hold, cash - prices[i]); // Buy the stock
        }

        // The maximum profit at the end is when not holding any stock
        return cash;
    }

    // V1
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solutions/3667440/beats-100-c-java-python-beginner-friendl-rpgh/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProfit_1(int[] prices, int fee) {
        int buy = Integer.MIN_VALUE;
        int sell = 0;

        for (int price : prices) {
            buy = Math.max(buy, sell - price);
            sell = Math.max(sell, buy + price - fee);
        }

        return sell;
    }

    // V2
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solutions/1112088/js-python-java-c-very-simple-state-machi-f4zp/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProfit_2(int[] P, int F) {
        int len = P.length, buying = 0, selling = -P[0];
        for (int i = 1; i < len; i++) {
            buying = Math.max(buying, selling + P[i] - F);
            selling = Math.max(selling, buying - P[i]);
        }
        return buying;
    }

    // V3
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solutions/1600084/dp-state-machine-top-down-to-bottom-up-w-vdzi/

    // V4
    // IDEA: DP
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solutions/3667495/beats-100-video-java-c-python-by-jeevank-uyrl/
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProfit_4(int[] prices, int fee) {
        int free = 0;
        int hold = -prices[0];
        for (int i : prices) {
            int tmp = hold;
            hold = Math.max(hold, free - i);
            free = Math.max(free, tmp + i - fee);
        }
        return free;
    }

}
