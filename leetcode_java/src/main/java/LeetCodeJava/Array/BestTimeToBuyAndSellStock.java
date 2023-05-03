package LeetCodeJava.Array;

// https://leetcode.com/problems/best-time-to-buy-and-sell-stock/

public class BestTimeToBuyAndSellStock {

    // V0
    public int maxProfit(int[] prices) {

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

    // V1
    // https://leetcode.com/problems/best-time-to-buy-and-sell-stock/solutions/1735493/java-c-best-ever-explanation-could-possible/
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
