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

    // V1
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solutions/3667440/beats-100-c-java-python-beginner-friendl-rpgh/
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
