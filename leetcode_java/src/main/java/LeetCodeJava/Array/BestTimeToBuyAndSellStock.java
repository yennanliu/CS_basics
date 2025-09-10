package LeetCodeJava.Array;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock/
/**
 * 121. Best Time to Buy and Sell Stock
 * Solved
 * Easy
 * Topics
 * Companies
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 *
 * You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.
 *
 * Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
 *
 *
 *
 * Example 1:
 *
 * Input: prices = [7,1,5,3,6,4]
 * Output: 5
 * Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
 * Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
 * Example 2:
 *
 * Input: prices = [7,6,4,3,1]
 * Output: 0
 * Explanation: In this case, no transactions are done and the max profit = 0.
 *
 *
 * Constraints:
 *
 * 1 <= prices.length <= 105
 * 0 <= prices[i] <= 104
 */
public class BestTimeToBuyAndSellStock {

    // V0
    // IDEA: maintain `min till now` var + problem observation
    public int maxProfit(int[] prices) {
        // edge
        if (prices.length <= 1) {
            return 0;
        }
        int ans = 0;
        int minTillNow = Integer.MAX_VALUE;

        /**  NOTE !!!
         *
         *  since we can ONLY `buy first then sell`, so
         *    ->
         *      1. we CAN'T sell before buy
         *      2. a min buy time potential means `biggest revenue`
         *
         *    -> so, we maintain the `min price we've seen till now`
         *       and check the current revenue by (cur_val - small_till_now)
         *       and maintain the `biggest revenue` within looping values.
         *       by this way, we can get the `biggest revenue` in a single loop
         */
        for (int i = 0; i < prices.length; i++) {
            int x = prices[i];
            minTillNow = Math.min(x, minTillNow);
            if (x > minTillNow) {
                ans = Math.max(ans, x - minTillNow);
            }
        }

        return ans;
    }

    // V0_0_1
    // IDEA: 2 POINTERS
    // time: O(N), space: O(1)
    /**
     *  IDEA: 2 POINTERS
     *  maintain local min, max, and global max
     *  (global max as result)
     *
     *
     *  EXP 1)
     *
     *  Input: prices = [7,1,5,3,6,4]
     *  -> Output: 5
     *
     *
     *  prices = [7,1,5,3,6,4]
     *            x
     *           l_min = 7
     *
     *
     *  prices = [7,1,5,3,6,4]
     *              x
     *              l_min = 1
     *
     *
     *   prices = [7,1,5,3,6,4]
     *                 x
     *                 l_min = 1
     *                 l_max = 5
     *                 profit = 4
     *                 g_profit = 4
     *
     *   prices = [7,1,5,3,6,4]
     *                   x
     *                   l_min = 1
     *                   l_max = 3
     *                   profit = 2
     *                   g_profit = 4
     *
     *
     *  prices = [7,1,5,3,6,4]
     *                    x
     *                    l_min = 1
     *                    l_max = 6
     *                    profit = 5
     *                    g_profit = 5
     *
     *   prices = [7,1,5,3,6,4]
     *                       x
     *                       l_min = 1
     *                       l_max = 4
     *                       profit = 3
     *                       g_profit = 5
     *
     *  -> res = 5
     */
    public int maxProfit_0_0_1(int[] prices) {

        // edge
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int globalProfit = 0;
        int localMin = -1;
        int localMax = -1;
        for (int x : prices) {
            if (localMin == -1) {
                localMin = x;
            } else if (x < localMin) {
                localMin = x;
            } else {
                localMax = x;
                int profit = localMax - localMin;
                globalProfit = Math.max(globalProfit, profit);
            }
        }

        return globalProfit;
    }

    // V0-1
    // time: O(N), space: O(1)
    public int maxProfit_0_1(int[] prices) {

        if (prices.length == 1){
            return 0;
        }

        int profit = 0;
        int local_min = Integer.MAX_VALUE;
        int local_max = -1;

        for(int x : prices){
            //System.out.println("x = " + x + ", local_min = " + local_min + ", local_max = " + local_max + ", profit = " + profit);
            if (local_min == Integer.MAX_VALUE){
                local_min = x;
            }else if (local_min > x){
                local_min = x;
            }else if(x > local_min){
                local_max = x;
                profit = Math.max(profit, local_max - local_min);
                // already "sold", can't reuse local_max, so make it as initial value again
                local_max = -1;
            }
        }

        return profit;
    }

    // V0-2
    // time: O(N), space: O(1)
    public int maxProfit_0_2(int[] prices) {

        if (prices.length == 0){
            return 0;
        }

        int res = 0;
        int min = -1;
        int max = -1;

        for (int i : prices){
            int cur = i;
            //System.out.println("cur = " + cur);
            if (min == -1){
                min = cur;
                continue;
            }
            if (min > cur){
                min = cur;
                continue;
            }
            if (max == -1){
                max = cur;
            }
            if (cur > max){
                max = cur;
            }
            int tmp = max - min;
            //System.out.println("max = " + max + " min = " + min + " tmp = " + tmp);
            /** NOTE : need to reset max val after get "revenue", so we don't reuse previous max val */
            max = -1;
            res = Math.max(tmp, res);
        }

        return res;
    }

    // V0-3
    // time: O(N), space: O(1)
    public int maxProfit_0_3(int[] prices) {

        int minVal = (int) Math.pow(10, 4);
        int maxVal = 0;
        int maxProfit = 0;
        //HashSet<Integer> seen = new HashSet<Integer>();
        for (int i = 0; i < prices.length; i++){
            int cur = prices[i];
            //seen.add(cur);
            if (cur < minVal){
                minVal = cur;
            }else{
                maxVal = cur;
                maxProfit = Math.max( maxVal - minVal, maxProfit);
            }
        }
        return maxProfit > 0 ? maxProfit : 0;
    }

    // V0-4
    // IDEA: BRUTE FORCE (TLE)
    //IDEA 1) BRUTE FORCE
    public int maxProfit_0_4(int[] prices) {
        // edge
        if(prices.length <= 1){
            return 0;
        }
        int ans = 0;
        for(int i = 0; i < prices.length; i++){
            for(int j = i + 1; j < prices.length; j++){
                ans = Math.max(ans, prices[j] - prices[i]);
            }
        }

        return ans;
    }

    // V1
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock/solutions/1735493/java-c-best-ever-explanation-could-possible/
    // time: O(N), space: O(1)
    public int maxProfit_2(int[] prices) {
        int lsf = Integer.MAX_VALUE;
        int op = 0;
        int pist = 0;

        for(int i = 0; i < prices.length; i++){
            if(prices[i] < lsf){
                lsf = prices[i];
            }
            pist = prices[i] - lsf;
            if(op < pist){
                op = pist;
            }
        }
        return op;
    }

}
