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

    // V0-0-1
    // IDEA: 2D (n x 3 array) DP (gemini)
    /**  NOTE !!!
     *
     *  1. we use 2D DP
     *
     *  2. `int[][] dp = new int[n][3]`
     *    -> A `n x 3` 2D array
     *    -> n rows, and 3 cols  (NOTE !!!!)
     *    e.g.:
     *      n is the number of Rows (usually representing Time/Days).
     *      3 is the number of Columns (representing your specific States).
     *
     *  3. we use different col save the different op
     *     - 0: `bought` OP
     *     - 1: `sold` op
     *     - 2: `do nothing` op
     *
     *  4.
     *    DP def:
     *
     *         // dp[i][0]: Max profit on day i if we HOLD a stock
     *         // dp[i][1]: Max profit on day i if we just SOLD a stock
     *         // dp[i][2]: Max profit on day i if we are RESTING (doing nothing)
     *
     *    DP eq:
     *       (as below)
     *
     *
     *  5. Example:
     *
     * Day (i),Col 0: HOLD,Col 1: SOLD,Col 2: REST
     * 0,dp[0][0],dp[0][1],dp[0][2]
     * 1,dp[1][0],dp[1][1],dp[1][2]
     * 2,dp[2][0],dp[2][1],dp[2][2]
     * 3,dp[3][0],dp[3][1],dp[3][2]
     * 4,dp[4][0],dp[4][1],dp[4][2]
     *
     *
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProfit_0_0_1(int[] prices) {
        if (prices == null || prices.length <= 1)
            return 0;

        int n = prices.length;

        /**  NOTE !!!!!!!!
         *
         *  - DP def:
         *
         *    // dp[i][0]: Max profit on day i if we HOLD a stock
         *    // dp[i][1]: Max profit on day i if we just SOLD a stock
         *    // dp[i][2]: Max profit on day i if we are RESTING (doing nothing)
         *
         *    (3 op: `hold`, sold, do nothing)
         *
         *
         *  NOTE !!!
         *     the dp array is `n x 3` dimension
         *     (still a 2D array, but with
         *       n row and 3 columns)
         *
         */
        int[][] dp = new int[n][3];

        // Base Case: Day 0
        /**  NOTE: the `day 0` base case
         *
         *  -> we init day 0 per each case
         *   - 0: `bought` OP
         *   - 1: `sold` op
         *   - 2: `do nothing` op
         */
        dp[0][0] = -prices[0]; // Bought on day 0
        dp[0][1] = 0; // Can't sell on day 0
        dp[0][2] = 0; // Doing nothing

        for (int i = 1; i < n; i++) {

            /** NOTE: the `hold` op
             *
             *    -> can be either
             *       - hold the stock
             *       or
             *       - buy new stock today
             */
            // 1. To HOLD today:
            /**  NOTE !!!
             *
             *  for `hold` case, we can either do below in prev day:
             *
             *   1. `hold` as well in prev day (D-1)
             *   2. `do nothing` (rest) in prev day (D-1) and  bought today
             *
             *
             *   NOTE !!!
             *
             *    we get max from
             *     - dp[i - 1][0]
             *     - dp[i - 1][2] - prices[i]
             */
            // Either you held it yesterday OR you were resting yesterday and bought today
            dp[i][0] = Math.max(dp[i - 1][0], dp[i - 1][2] - prices[i]);

            // 2. To SOLD today:
            // You must have held a stock yesterday and you sell it at today's price
            dp[i][1] = dp[i - 1][0] + prices[i];

            // 3. To REST today:
            // Either you rested yesterday OR you sold yesterday (cooldown)
            dp[i][2] = Math.max(dp[i - 1][2], dp[i - 1][1]);
        }

        // The max profit will be the maximum of having sold or resting on the last day
        return Math.max(dp[n - 1][1], dp[n - 1][2]);
    }

    // V0-0-2
    // IDEA: DP (SPACE OPTIMIZATION) (gemini)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProfit_0_0_2(int[] prices) {
        int hold = -prices[0];
        int sold = 0;
        int rest = 0;

        for (int i = 1; i < prices.length; i++) {
            int prevHold = hold;
            int prevSold = sold;
            int prevRest = rest;

            hold = Math.max(prevHold, prevRest - prices[i]);
            sold = prevHold + prices[i];
            rest = Math.max(prevRest, prevSold);
        }

        return Math.max(sold, rest);
    }


    // V0-0-3
    // IDEA: 2D DP (GPT)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProfit_0_0_3(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;

        int n = prices.length;

        // dp0 = holding
        int dp0 = -prices[0];
        // dp1 = not holding (free)
        int dp1 = 0;
        // dp2 = in cooldown
        int dp2 = 0;

        for (int i = 1; i < n; i++) {
            int newDp0 = Math.max(dp0, dp1 - prices[i]);
            int newDp1 = Math.max(dp1, dp2);
            int newDp2 = dp0 + prices[i];

            dp0 = newDp0;
            dp1 = newDp1;
            dp2 = newDp2;
        }

        // The max profit on last day is either not holding stock (free or in cooldown)
        return Math.max(dp1, dp2);
    }



    // V0-1
    // IDEA: DP (gpt)
    /**
     * Absolutely — let’s break down the **DP solution**
     * for **Leetcode 309 - Best Time to Buy and Sell Stock with Cooldown**.
     *
     * ---
     *
     * ## 🧩 Problem Recap:
     *
     * You're given an array `prices[]` representing stock prices on each day.
     *
     * * You can buy and sell **as many times as you want**.
     * * **BUT**: after you **sell**, you must **cool down for 1 day** before buying again.
     * * You **cannot hold multiple stocks** at once.
     *
     * ---
     *
     * ## ✅ Key Idea: Use 3 DP States
     *
     * We simulate the behavior across days by tracking 3 possible "states" you're in:
     *
     * | State  | Meaning                                                                   |
     * | ------ | ------------------------------------------------------------------------- |
     * | `hold` | You currently **own** a stock (i.e., you've bought it, haven't sold yet). |
     * | `sold` | You **just sold** a stock today.                                          |
     * | `rest` | You're in cooldown or doing nothing — **not holding any stock**.          |
     *
     * We use these to compute the **maximum profit** at each day.
     *
     * ---
     *
     * ## 📌 State Transitions (Core Logic):
     *
     * ### 1. **hold (on day i)**:
     *
     * You can arrive at `hold` by either:
     *
     * * Continuing to **hold** the stock from yesterday → `hold = hold`
     * * Or you **buy today**, which means yesterday you must’ve been in `rest` → `hold = rest - price[i]`
     *
     * ```java
     * hold = Math.max(prevHold, prevRest - prices[i]);
     * ```
     *
     * ---
     *
     * ### 2. **sold (on day i)**:
     *
     * You can only arrive here by **selling today**, so:
     *
     * * You must’ve **held a stock** yesterday → `sold = hold + prices[i]`
     *
     * ```java
     * sold = prevHold + prices[i];
     * ```
     *
     * ---
     *
     * ### 3. **rest (on day i)**:
     *
     * You can be in `rest` by:
     *
     * * Resting again today → `rest = rest`
     * * Or you just sold yesterday → `rest = sold`
     *
     * ```java
     * rest = Math.max(prevRest, prevSold);
     * ```
     *
     * ---
     *
     * ## 🧠 Initialization (Day 0):
     *
     * ```java
     * hold = -prices[0]; // if you buy on day 0
     * sold = 0;          // can't sell on day 0
     * rest = 0;          // haven't done anything yet
     * ```
     *
     * ---
     *
     * ## 🔁 Final Step:
     *
     * At the end, you return the **max profit where you're not holding** a stock:
     *
     * ```java
     * return Math.max(sold, rest);
     * ```
     *
     * You can’t return `hold` because that means you still have a stock you haven't sold.
     *
     * ---
     *
     * ## 🔢 Time and Space Complexity:
     *
     * * **Time**: O(n) — one pass through prices
     * * **Space**: O(1) — we only store variables for `hold`, `sold`, and `rest`
     *
     * ---
     *
     * ## ✅ Example Walkthrough (prices = \[1,2,3,0,2]):
     *
     * | Day | Price | Hold | Sold | Rest |
     * | --- | ----- | ---- | ---- | ---- |
     * | 0   | 1     | -1   | 0    | 0    |
     * | 1   | 2     | -1   | 1    | 0    |
     * | 2   | 3     | -1   | 2    | 1    |
     * | 3   | 0     | 1    | 2    | 2    |
     * | 4   | 2     | 1    | 3    | 2    |
     *
     * ✔️ Final result = `max(sold, rest)` = `max(3, 2)` = **3**
     *
     * ---
     *
     *
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxProfit_0_1(int[] prices) {
        if (prices == null || prices.length == 0)
            return 0;

        int n = prices.length;
        int hold = -prices[0]; // Buying on day 0
        int sold = 0;
        int rest = 0;

        for (int i = 1; i < n; i++) {
            int prevSold = sold;

            // If we sell today, we had to hold before
            sold = hold + prices[i];

            // If we hold today, we either continue holding, or buy today (after cooldown)
            hold = Math.max(hold, rest - prices[i]);

            // If we rest today, we take the max of resting or having sold yesterday
            rest = Math.max(rest, prevSold);
        }

        // Final profit is max of sold or rest (not holding)
        return Math.max(sold, rest);
    }

    // V0-2
    // IDEA: RECURSIVE (gpt) (TLE)
    /**
     * time = O(2^N)
     * space = O(N)
     */
    public int maxProfit_0_2(int[] prices) {
        return dfs(prices, 0, false);
    }

    /**
     * time = O(2^N)
     * space = O(N)
     */
    private int dfs(int[] prices, int day, boolean holding) {
        if (day >= prices.length)
            return 0;

        int doNothing = dfs(prices, day + 1, holding);
        int doSomething = 0;

        if (holding) {
            // Option: sell today and cooldown tomorrow
            doSomething = prices[day] + dfs(prices, day + 2, false);
        } else {
            // Option: buy today
            doSomething = -prices[day] + dfs(prices, day + 1, true);
        }

        return Math.max(doNothing, doSomething);
    }

    // V1-1
    // https://neetcode.io/problems/buy-and-sell-crypto-with-cooldown
    // IDEA:  RECURSION
    /**
     * time = O(2^N)
     * space = O(N)
     */
    public int maxProfit_1_1(int[] prices) {

        return dfs(0, true, prices);
    }

    /**
     * time = O(2^N)
     * space = O(N)
     */
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

    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxProfit_1_2(int[] prices) {
        return dfs_1_2(0, true, prices);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(N)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
