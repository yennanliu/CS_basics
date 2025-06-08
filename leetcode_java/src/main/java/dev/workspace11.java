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

  // LC 978
  // 4.13 - 4.23 pm
  /**
   *
   *  -> Given an integer array arr,
   *    return the length of a `maximum` size
   *     turbulent subarray of arr.
   *
   *
   *    turbulent array:
   *      given [arr[i], arr[i + 1], ..., arr[j]]
   *
   *      ->  for  i <= k < j,
   *          arr[k] > arr[k+1] when k is odd
   *          and
   *          arr[k] < arr[k + 1] when k is even
   *
   *      or
   *
   *      ->  for  i <= k < j,
   *          arr[k] > arr[k + 1] when k is even, and
   *          and
   *          arr[k] < arr[k + 1] when k is odd.
   *
   *
   *   IDEA 1) GREEDY ???
   *
   */
  public int maxTurbulenceSize_(int[] arr) {
      // edge
      if(arr == null || arr.length == 0){
          return 0;
      }
      if(arr.length == 1){
          return 1;
      }

      int max_len = 1;
      for(int i = 0; i < arr.length; i++){
          // ??
          for(int j = i+1; j < arr.length - 1; j++){
              if(arr[j] > arr[j-1] && arr[j] < arr[j+1]){
                  max_len = Math.max(max_len, j - i + 1);
              }else{
                  // NOT possible to go further
                  // per given (i, and the current j)
                  // so we break the 2nd loop
                  // and start a `new i` loop
                  break;
              }
          }
      }

      return max_len;
    }


    ////////


//    public int maxTurbulenceSize(int[] arr) {
//        int n = arr.length;
//        if (n == 1)
//            return 1;
//
//        /**
//         * maxLen:
//         *    stores the maximum turbulent subarray length found so far.
//         *    Starts at 1 (any element alone is trivially turbulent).
//         *
//         * start: the start index of the current turbulent window.
//         *
//         */
//        int maxLen = 1;
//        int start = 0;
//
//        /**
//         * Start from the second element.
//         *
//         * We’re comparing arr[i - 1] and arr[i], so i starts at 1.
//         *
//         */
//        for (int i = 1; i < n; i++) {
//
//            /**
//             * Compares adjacent elements and stores the result in cmp:
//             *
//             *   - Returns -1 if arr[i - 1] < arr[i]
//             *
//             *   - Returns 0 if arr[i - 1] == arr[i]
//             *
//             *   - Returns 1 if arr[i - 1] > arr[i]
//             *
//             *
//             *  -> This is how we capture the direction of change.
//             *
//             */
//            int cmp = Integer.compare(arr[i - 1], arr[i]);
//
//            /**
//             * If two adjacent elements are equal, the turbulence breaks.
//             *
//             * We reset the start of our current window to i.
//             *
//             */
//            if (cmp == 0) {
//                // Equal values: turbulence broken, reset window
//                // NOTE !!! we `reset` window below
//                start = i;
//            }
//            /**
//             *  NOTE !!!
//             *
//             *  We check two things:
//             *
//             * 1) i == n - 1:
//             *         if we’re at the `last` element,
//             *         we can’t compare with the next one,
//             *         so we treat this as the END of the window.
//             *
//             *
//             * 2) cmp * Integer.compare(arr[i], arr[i + 1]) != -1:
//             *
//             *   - Checks whether the comparison direction `flips`.
//             *
//             *   - If cmp = 1 (descending), the next should be ascending -1 → product = -1
//             *
//             *   - If cmp = -1, the next should be descending 1 → product = -1
//             *
//             *  -  Anything else (1*1, -1*-1, or 0) → turbulence is broken
//             *
//             */
//            else if (i == n - 1 || cmp * Integer.compare(arr[i], arr[i + 1]) != -1) {
//                // Turbulence breaks at next step — record max
//                /**
//                 *
//                 *  NOTE !!!  if `Turbulence` breaks
//                 *
//                 *
//                 *  - Update the maxLen if the current turbulent
//                 *    sub-array is longer.
//                 *
//                 * -  Reset start to begin a new window from i.
//                 *
//                 */
//                maxLen = Math.max(maxLen, i - start + 1);
//                start = i;
//            }
//        }
//
//        // Return the maximum turbulent window size found.
//        return maxLen;
//    }


}
