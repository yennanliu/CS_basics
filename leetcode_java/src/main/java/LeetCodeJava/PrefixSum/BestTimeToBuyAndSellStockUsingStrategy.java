package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-using-strategy/description/

/**
 * 3652. Best Time to Buy and Sell Stock using Strategy
 * Medium
 *
 * You are given two integer arrays prices and strategy, where:
 *
 * prices[i] is the price of a given stock on the i^th day.
 * strategy[i] represents a trading action on the i^th day, where:
 *   -1 indicates buying one unit of the stock.
 *    0 indicates holding the stock.
 *    1 indicates selling one unit of the stock.
 *
 * You are also given an even integer k, and may perform at most one modification
 * to strategy. A modification consists of:
 *
 * Selecting exactly k consecutive elements in strategy.
 * Set the first k / 2 elements to 0 (hold).
 * Set the last k / 2 elements to 1 (sell).
 *
 * The profit is defined as the sum of strategy[i] * prices[i] across all days.
 *
 * Return the maximum possible profit you can achieve.
 *
 * Note: There are no constraints on budget or stock ownership, so all buy and
 * sell operations are feasible regardless of past actions.
 *
 *
 * Example 1:
 *
 * Input: prices = [4,2,8], strategy = [-1,0,1], k = 2
 * Output: 10
 *
 * Example 2:
 *
 * Input: prices = [5,4,3], strategy = [1,1,0], k = 2
 * Output: 9
 *
 *
 * Constraints:
 *
 * 2 <= prices.length == strategy.length <= 10^5
 * 1 <= prices[i] <= 10^5
 * -1 <= strategy[i] <= 1
 * 2 <= k <= prices.length
 * k is even
 *
 */
public class BestTimeToBuyAndSellStockUsingStrategy {

    // V0
    // IDEA: PREFIX SUMS OF strategy * prices AND OF prices
    //   modifying window [i, i+k) removes those days' contribution and adds the
    //   prices of the second half:
    //     profit(i) = base - sum(strategy*prices over [i, i+k)) + sum(prices over [i+k/2, i+k))
    //   both are range sums, so each window is O(1). "at most one" keeps base itself.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maxProfit(int[] prices, int[] strategy, int k) {
        int n = prices.length;
        long[] profitPrefix = new long[n + 1]; // prefix of strategy[i] * prices[i]
        long[] pricePrefix = new long[n + 1];  // prefix of prices[i]
        for (int i = 0; i < n; i++) {
            profitPrefix[i + 1] = profitPrefix[i] + (long) strategy[i] * prices[i];
            pricePrefix[i + 1] = pricePrefix[i] + prices[i];
        }

        long base = profitPrefix[n];
        long best = base;
        int half = k / 2;
        for (int i = 0; i + k <= n; i++) {
            long removed = profitPrefix[i + k] - profitPrefix[i];
            long addedSells = pricePrefix[i + k] - pricePrefix[i + half];
            best = Math.max(best, base - removed + addedSells);
        }
        return best;
    }

    // V1
    // IDEA: SLIDING WINDOW -- CARRY THE TWO RANGE SUMS INSTEAD OF PREFIX ARRAYS
    //   same formula as V0; windowProfit = sum(strategy*prices) over [i, i+k) and
    //   secondHalfPrices = sum(prices) over [i+k/2, i+k) are updated by one entering
    //   and one leaving index per slide, so the extra arrays disappear.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maxProfit_1(int[] prices, int[] strategy, int k) {
        int n = prices.length;
        int half = k / 2;

        long base = 0;
        for (int i = 0; i < n; i++) {
            base += (long) strategy[i] * prices[i];
        }

        // first window [0, k)
        long windowProfit = 0;
        long secondHalfPrices = 0;
        for (int i = 0; i < k; i++) {
            windowProfit += (long) strategy[i] * prices[i];
            if (i >= half) {
                secondHalfPrices += prices[i];
            }
        }
        long best = Math.max(base, base - windowProfit + secondHalfPrices);

        for (int start = 1; start + k <= n; start++) {
            int entering = start + k - 1;
            int leaving = start - 1;
            windowProfit += (long) strategy[entering] * prices[entering];
            windowProfit -= (long) strategy[leaving] * prices[leaving];
            // the second half gains the entering day and loses the day that crossed into the first half
            secondHalfPrices += prices[entering] - prices[start + half - 1];
            best = Math.max(best, base - windowProfit + secondHalfPrices);
        }
        return best;
    }
}
