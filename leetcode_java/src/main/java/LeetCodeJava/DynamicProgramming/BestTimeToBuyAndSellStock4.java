package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-iv/description/

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * 188. Best Time to Buy and Sell Stock IV
 * Hard
 *
 * You are given an integer array prices where prices[i] is the price of a given stock on
 * the ith day, and an integer k.
 *
 * Find the maximum profit you can achieve. You may complete at most k transactions:
 * i.e. you may buy at most k times and sell at most k times.
 *
 * Note: You may not engage in multiple transactions simultaneously
 * (i.e., you must sell the stock before you buy again).
 *
 *
 * Example 1:
 *
 * Input: k = 2, prices = [2,4,1]
 * Output: 2
 * Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
 *
 * Example 2:
 *
 * Input: k = 2, prices = [3,2,6,5,0,3]
 * Output: 7
 * Explanation: Buy on day 2 (price = 2) and sell on day 3 (price = 6), profit = 6-2 = 4.
 * Then buy on day 5 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 *
 *
 * Constraints:
 *
 * 1 <= k <= 100
 * 1 <= prices.length <= 1000
 * 0 <= prices[i] <= 1000
 *
 */
public class BestTimeToBuyAndSellStock4 {

    // V0
    // IDEA: DP over (transaction count, holding state), rolled into 1D arrays
    /**
     *  DP def (after processing day i):
     *    - buy[j]  = max profit having STARTED the j-th transaction and STILL HOLDING
     *    - sell[j] = max profit having COMPLETED j transactions and holding NOTHING
     *
     *  DP eq (for each price p, for j in 1..k):
     *    - buy[j]  = max(buy[j],  sell[j-1] - p)   # OPEN the j-th transaction
     *    - sell[j] = max(sell[j], buy[j]   + p)    # CLOSE the j-th transaction
     *
     *  NOTE !!! updating buy[j] BEFORE sell[j] within the SAME day is fine here -
     *           it would only model buy-and-sell on the same day, which yields
     *           0 profit and can never beat the existing value.
     *
     *  time  = O(n * k)
     *  space = O(k)
     */
    public int maxProfit(int k, int[] prices) {
        int n = prices.length;
        if (n < 2 || k <= 0) {
            return 0;
        }

        /** NOTE !!!
         *
         *  a transaction needs >= 2 days, so more than n/2 transactions is USELESS
         *  -> this also keeps the loop cheap when k is huge
         */
        k = Math.min(k, n / 2);
        if (k == 0) {
            return 0;
        }

        final int NEG = Integer.MIN_VALUE / 2; // /2 avoids overflow on `+ p`

        int[] buy = new int[k + 1];   // buy[0] unused
        int[] sell = new int[k + 1];  // sell[0] = 0 -> zero transactions, zero profit
        Arrays.fill(buy, NEG);

        for (int p : prices) {
            for (int j = 1; j <= k; j++) {
                buy[j] = Math.max(buy[j], sell[j - 1] - p);
                sell[j] = Math.max(sell[j], buy[j] + p);
            }
        }

        return sell[k];
    }


    // V1
    // IDEA: FULL 2D TABLE over (day, transactions) with an inner max
    /**
     *  dp[t][i] = best profit using at most t transactions through day i:
     *      dp[t][i] = max(dp[t][i-1], prices[i] + max over j<i of (dp[t-1][j] - prices[j]))
     *
     *  Keeping the inner max as a running variable makes it O(n * k); writing the
     *  table out in full is how the recurrence is usually derived before it is
     *  rolled into the buy/sell pair of V0.
     *
     *  time  = O(n * k)
     *  space = O(n * k)
     */
    public int maxProfit_1(int k, int[] prices) {
        int n = prices.length;
        if (n < 2 || k <= 0) {
            return 0;
        }
        k = Math.min(k, n / 2);
        if (k == 0) {
            return 0;
        }

        int[][] dp = new int[k + 1][n];
        for (int t = 1; t <= k; t++) {
            int best = -prices[0];   // max over j of dp[t-1][j] - prices[j]
            for (int i = 1; i < n; i++) {
                dp[t][i] = Math.max(dp[t][i - 1], prices[i] + best);
                best = Math.max(best, dp[t - 1][i] - prices[i]);
            }
        }
        return dp[k][n - 1];
    }

    // V2
    // IDEA: UNLIMITED-TRANSACTION SHORTCUT when k is large
    /**
     *  Once k >= n / 2 the constraint is vacuous, and the answer is the sum of
     *  every upward price move -- LC 122 in one line.
     *
     *  V0 handles that by CLAMPING k; splitting it out explicitly turns the common
     *  large-k case into O(n) with no DP array at all.
     *
     *  time  = O(n) for large k, O(n * k) otherwise
     *  space = O(k)
     */
    public int maxProfit_2(int k, int[] prices) {
        int n = prices.length;
        if (n < 2 || k <= 0) {
            return 0;
        }

        if (k >= n / 2) {
            int profit = 0;
            for (int i = 1; i < n; i++) {
                if (prices[i] > prices[i - 1]) {
                    profit += prices[i] - prices[i - 1];
                }
            }
            return profit;
        }

        int[] buy = new int[k + 1];
        int[] sell = new int[k + 1];
        Arrays.fill(buy, Integer.MIN_VALUE / 2);
        for (int p : prices) {
            for (int j = 1; j <= k; j++) {
                buy[j] = Math.max(buy[j], sell[j - 1] - p);
                sell[j] = Math.max(sell[j], buy[j] + p);
            }
        }
        return sell[k];
    }

    // V3
    // IDEA: MERGE THE PRICE RUNS INTO (valley, peak) PAIRS FIRST
    /**
     *  Collapse the price series into monotone transactions -- each maximal rise is
     *  one candidate trade -- BEFORE running the DP.
     *
     *  When the series is noisy this shrinks n dramatically (only the turning points
     *  survive), and the DP then runs over transactions rather than days.
     *
     *  time  = O(n + t * k), t = number of price runs
     *  space = O(t + k)
     */
    public int maxProfit_3(int k, int[] prices) {
        int n = prices.length;
        if (n < 2 || k <= 0) {
            return 0;
        }

        // compress into (valley, peak) transactions
        List<int[]> trades = new ArrayList<>();
        int i = 0;
        while (i < n - 1) {
            while (i < n - 1 && prices[i] >= prices[i + 1]) {
                i += 1;
            }
            int valley = prices[i];
            while (i < n - 1 && prices[i] <= prices[i + 1]) {
                i += 1;
            }
            int peak = prices[i];
            if (peak > valley) {
                trades.add(new int[] { valley, peak });
            }
        }

        if (k >= trades.size()) {
            int profit = 0;
            for (int[] t : trades) {
                profit += t[1] - t[0];
            }
            return profit;
        }

        int[] buy = new int[k + 1];
        int[] sell = new int[k + 1];
        Arrays.fill(buy, Integer.MIN_VALUE / 2);
        for (int[] t : trades) {
            for (int p : new int[] { t[0], t[1] }) {
                for (int j = 1; j <= k; j++) {
                    buy[j] = Math.max(buy[j], sell[j - 1] - p);
                    sell[j] = Math.max(sell[j], buy[j] + p);
                }
            }
        }
        return sell[k];
    }

}
