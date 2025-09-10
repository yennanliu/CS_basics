package LeetCodeJava.Greedy;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/description/
/**
 * 122. Best Time to Buy and Sell Stock II
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given an integer array prices where prices[i] is the price of a given stock on the ith day.
 *
 * On each day, you may decide to buy and/or sell the stock. You can only hold at most one share of the stock at any time. However, you can buy it then immediately sell it on the same day.
 *
 * Find and return the maximum profit you can achieve.
 *
 *
 *
 * Example 1:
 *
 * Input: prices = [7,1,5,3,6,4]
 * Output: 7
 * Explanation: Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
 * Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.
 * Total profit is 4 + 3 = 7.
 * Example 2:
 *
 * Input: prices = [1,2,3,4,5]
 * Output: 4
 * Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
 * Total profit is 4.
 * Example 3:
 *
 * Input: prices = [7,6,4,3,1]
 * Output: 0
 * Explanation: There is no way to make a positive profit, so we never buy the stock to achieve the maximum profit of 0.
 *
 *
 * Constraints:
 *
 * 1 <= prices.length <= 3 * 104
 * 0 <= prices[i] <= 104
 *
 *
 */
public class BestTimeToBuyAndSellStock2 {

    // V0
    // IDEA: GREEDY
    public int maxProfit(int[] prices) {

        int prev_price = prices[0];
        int profit = 0;

        /** NOTE !!! we start from idx = 1 */
        for(int i = 1; i < prices.length; i++){

            /** NOTE !!!
             *
             *  case 1) cur price < prev price
             */
            if(prices[i] < prev_price){
                prev_price = prices[i];
            }
            /** NOTE !!!
             *
             *  case 2) cur price >= prev price
             */
            else{
                profit += prices[i] - prev_price;
                /**
                 *  NOTE !!! below
                 *
                 *  -> we assign prev value as prices[i]
                 *
                 */
                prev_price = prices[i];
            }
        }
        return profit;
    }

    // V0-0-1
    // IDEA: GREEDY
    public int maxProfit_0_0_1(int[] prices) {
        // edge
        if(prices.length <= 1){
            return 0;
        }
        int ans = 0;

        /** NOTE !!!
         *
         *   we maintain 2 var: prev, cur
         *   so ONLY do `buy-sell` when `cur > prev`, then add result to final answer
         *   otherwise, we update prev as cur and DO NOTHING ELSE
         *
         *
         */
        int prev = prices[0];

        /** NOTE !!! idx start from 1 */
        for(int i = 1; i < prices.length; i++){
            int cur = prices[i];
            /** NOTE !!! 2 cases */
            // case 1) cur > prev
            // do `buy-sell` when `cur > prev`, then add result to final answer
            if(cur > prev){
                ans += (cur - prev);
                prev = cur; // ???
            }
            // case 2) cur < prev
            // we update prev as cur and DO NOTHING ELSE
            else{
                prev = cur;
            }
        }

        return ans;
    }


    // V0-1
    // IDEA: GREEDY (fixed by gpt)
    public int maxProfit_0_1(int[] prices) {
        // edge case
        if (prices == null || prices.length < 2) {
            return 0;
        }

        int profit = 0;

        // greedy: accumulate profit from every ascending pair
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += (prices[i] - prices[i - 1]);
            }
        }

        return profit;
    }

    // V1-1
    // IDEA: GREEDY
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0122-best-time-to-buy-and-sell-stock-II.java
    public int maxProfit_1_1(int[] prices) {

        int oldStockPrice = prices[0];
        int profit = 0;

        /** NOTE !!! we start from idx = 1 */
        for(int i = 1; i<prices.length; i++){

            /** NOTE !!!
             *
             *  case 1) cur price < prev price
             */
            if(prices[i] < oldStockPrice){
                oldStockPrice = prices[i];
            }
            /** NOTE !!!
             *
             *  case 2) cur price >= prev price
             */
            else{
                profit += prices[i] - oldStockPrice;
                /**
                 *  NOTE !!! below
                 *
                 *  -> we assign prev value as prices[i]
                 *
                 */
                oldStockPrice = prices[i];
            }
        }
        return profit;
    }

    // V2
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/solutions/5816678/video-sell-a-stock-immediately-by-niits-nquk/
    public int maxProfit_2(int[] prices) {
        int profit = 0;

        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }

        return profit;
    }

    // V3
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/solutions/4836121/simple-beginner-friendly-dry-run-greedy-j74or/
    public int maxProfit_3(int[] prices) {
        int max = 0;
        int start = prices[0];
        int len = prices.length;
        for (int i = 1; i < len; i++) {
            if (start < prices[i])
                max += prices[i] - start;
            start = prices[i];
        }
        return max;
    }

    // V4
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock-ii/solutions/6552146/java-simple-solution-beats-100-by-dhanos-sohx/
    public int maxProfit_4(int[] prices) {
        int maxProfit = 0;
        for(int i = 0; i < prices.length-1; i++){
            if(prices[i+1]-prices[i]> 0){
                maxProfit += prices[i+1]-prices[i];
            }
        }
        return maxProfit;
    }

    // V6
    // https://leetcode.ca/2016-03-31-122-Best-Time-to-Buy-and-Sell-Stock-II/
    public int maxProfit_6(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; ++i) {
            ans += Math.max(0, prices[i] - prices[i - 1]);
        }
        return ans;
    }

}
