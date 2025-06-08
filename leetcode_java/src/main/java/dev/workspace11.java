package dev;

public class workspace11 {

  // LC 122
  // 3.34 - 3.44 pm
  /**
   * On each day, you may decide to buy and/or sell the stock.
   *  You can only hold at most one share of the stock at any time.
   *  However, you can buy it then immediately sell it on the same day.
   *
   *
   *  -> Find and return the `maximum profit` you can achieve.
   *
   *
   *   IDEA 1) GREEDY
   *
   *      -> define 3 var
   *        -> local_max, local_min, global_max ??
   *
   *
   *   IDEA 2) DP
   *
   *   IDEA 3) k**** algo ???
   *
   *
   *
   *
   *   exp 1)
   *
   *   Input: prices = [7,1,5,3,6,4]
   *   Output: 7
   *
   *
   *  [7,1,5,3,6,4]
   *     b s         -> p = 4
   *         b s     -> p = 4 + (3) = 7
   *
   *  -> 7
   *
   *  [7,1,5,3,6,4]
   *     x
   *   l_min = 1
   *   l_max= 7
   *   pr=
   *
   *
   */
  public int maxProfit(int[] prices) {
      // edge
      if(prices == null || prices.length == 0){
          return 0;
      }
      if(prices.length == 1){
          return 0;
      }
      if(prices.length == 2){
          if(prices[1] > prices[0]){
              return prices[1] - prices[0];
          }
          return 0;
      }

      // greedy
      int profit = 0;
//      int local_min = prices[0];
//      int local_max = prices[1];

      int prev_price = prices[0];

      for(int i = 1; i < prices.length; i++){

          int val = prices[i];
          if(val < prev_price){
              prev_price = val;
          }else{
              profit += (val - prev_price);
              prev_price = val; // ???
             // prev_price = -1;
          }

      }

      return profit;
    }

}
