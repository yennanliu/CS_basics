package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-v/description/

import java.util.*;

/**
 * 3573. Best Time to Buy and Sell Stock V
 * Medium
 *
 * You are given an integer array prices where prices[i] is the price of a stock in dollars on the ith day, and an integer k.
 *
 * You are allowed to make at most k transactions, where each transaction can be either of the following:
 *
 * Normal transaction: buy on day i, then sell on a later day j where i < j. You profit prices[j] - prices[i].
 * Short selling transaction: sell on day i, then buy back on a later day j where i < j. You profit prices[i] - prices[j].
 *
 * Note that you must complete each transaction before starting another. Additionally, you can't buy or sell on the same day you are completing another transaction.
 *
 * Return the maximum total profit you can earn by making at most k transactions.
 *
 *
 * Example 1:
 *
 * Input: prices = [1,7,9,8,2], k = 2
 * Output: 14
 * Explanation:
 * We can make $14 of profit through 2 transactions:
 * A normal transaction: buy the stock on day 0 for $1 then sell it on day 2 for $9.
 * A short selling transaction: sell the stock on day 3 for $8 then buy back on day 4 for $2.
 *
 * Example 2:
 *
 * Input: prices = [12,16,19,19,8,1,19,13,9], k = 3
 * Output: 36
 * Explanation:
 * We can make $36 of profit through 3 transactions:
 * A normal transaction: buy the stock on day 0 for $12 then sell it on day 2 for $19.
 * A short selling transaction: sell the stock on day 3 for $19 then buy back on day 4 for $8.
 * A normal transaction: buy the stock on day 5 for $1 then sell it on day 6 for $19.
 *
 *
 * Constraints:
 *
 * 2 <= prices.length <= 10^3
 * 1 <= prices[i] <= 10^9
 * 1 <= k <= prices.length / 2
 *
 */
public class BestTimeToBuyAndSellStockV {

    // V0
    // IDEA: 3-STATE DP (IDLE / LONG OPEN / SHORT OPEN) PER TRANSACTION COUNT
    //   idle[j]  : best profit with at most j closed transactions and no open position
    //   hold[j]  : best profit with a LONG position open that will be transaction j
    //   short[j] : best profit with a SHORT position open that will be transaction j
    //   for each price p, from YESTERDAY's values (so no open + close on the same day):
    //     hold[j]  = max(hold[j],  idle[j-1] - p)            // buy
    //     short[j] = max(short[j], idle[j-1] + p)            // short sell
    //     idle[j]  = max(idle[j], hold[j] + p, short[j] - p, idle[j-1])
    /**
     * time = O(N * K)
     * space = O(K)
     */
    public long maximumProfit(int[] prices, int k) {
        // "impossible" without risking overflow when a price is added
        final long NEG = Long.MIN_VALUE / 4;
        long[] idle = new long[k + 1];
        long[] holdLong = new long[k + 1];
        long[] holdShort = new long[k + 1];
        Arrays.fill(holdLong, NEG);
        Arrays.fill(holdShort, NEG);

        for (int price : prices) {
            long[] newIdle = idle.clone();
            long[] newLong = holdLong.clone();
            long[] newShort = holdShort.clone();
            for (int j = 1; j <= k; j++) {
                // open a position using yesterday's idle state
                newLong[j] = Math.max(newLong[j], idle[j - 1] - price);
                newShort[j] = Math.max(newShort[j], idle[j - 1] + price);
                // close a position opened on an earlier day
                newIdle[j] = Math.max(newIdle[j], holdLong[j] + price);
                newIdle[j] = Math.max(newIdle[j], holdShort[j] - price);
                // "at most j" transactions
                newIdle[j] = Math.max(newIdle[j], newIdle[j - 1]);
            }
            idle = newIdle;
            holdLong = newLong;
            holdShort = newShort;
        }
        return idle[k];
    }
}
