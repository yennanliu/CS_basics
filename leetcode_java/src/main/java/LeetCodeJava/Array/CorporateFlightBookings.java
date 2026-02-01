package LeetCodeJava.Array;

// https://leetcode.com/problems/corporate-flight-bookings/description/
// https://leetcode.cn/problems/corporate-flight-bookings/description/

import java.util.Arrays;

/**
 * 1109. Corporate Flight Bookings
 * Solved
 * Medium
 * Topics
 * Companies
 * There are n flights that are labeled from 1 to n.
 *
 * You are given an array of flight bookings bookings, where bookings[i] = [firsti, lasti, seatsi] represents a booking for flights firsti through lasti (inclusive) with seatsi seats reserved for each flight in the range.
 *
 * Return an array answer of length n, where answer[i] is the total number of seats reserved for flight i.
 *
 *
 *
 * Example 1:
 *
 * Input: bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
 * Output: [10,55,45,25,25]
 * Explanation:
 * Flight labels:        1   2   3   4   5
 * Booking 1 reserved:  10  10
 * Booking 2 reserved:      20  20
 * Booking 3 reserved:      25  25  25  25
 * Total seats:         10  55  45  25  25
 * Hence, answer = [10,55,45,25,25]
 * Example 2:
 *
 * Input: bookings = [[1,2,10],[2,2,15]], n = 2
 * Output: [10,25]
 * Explanation:
 * Flight labels:        1   2
 * Booking 1 reserved:  10  10
 * Booking 2 reserved:      15
 * Total seats:         10  25
 * Hence, answer = [10,25]
 *
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 104
 * 1 <= bookings.length <= 2 * 104
 * bookings[i].length == 3
 * 1 <= firsti <= lasti <= n
 * 1 <= seatsi <= 104
 *
 *
 * Example :
 *
 *
 * 示例 1：
 *
 * 输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
 * 输出：[10,55,45,25,25]
 * 解释：
 * 航班编号        1   2   3   4   5
 * 预订记录 1 ：   10  10
 * 预订记录 2 ：       20  20
 * 预订记录 3 ：       25  25  25  25
 * 总座位数：      10  55  45  25  25
 * 因此，answer = [10,55,45,25,25]
 * 示例 2：
 *
 * 输入：bookings = [[1,2,10],[2,2,15]], n = 2
 * 输出：[10,25]
 * 解释：
 * 航班编号        1   2
 * 预订记录 1 ：   10  10
 * 预订记录 2 ：       15
 * 总座位数：      10  25
 * 因此，answer = [10,25]
 *
 */
public class CorporateFlightBookings {

  // V0
  // IDEA: PREFIX SUM
  // https://labuladong.online/algo/data-structure/diff-array/#%E7%AE%97%E6%B3%95%E5%AE%9E%E8%B7%B5
    /**
     * time = O(bookings * n)
     * space = O(n)
     *
     *  -> [i, j, k] :  k seats, from i -> j
     *
     *    exp 1)
     *
     *    输入：bookings = [[1,2,10],[2,3,20],[2,5,25]], n = 5
     *    输出：[10,55,45,25,25]
     *
     *    -> presum =
     *          [10,10,0,0,0]
     *          [10,30,20,0,0]
     *          [10,55,45,25,25]  <--- ans
     *
     *   exp 2)
     *
     *   输入：bookings = [[1,2,10],[2,2,15]], n = 2
     *   输出：[10,25]
     *
      *    -> presum =
     *           [10,10]
     *           [10,25] <--- ans
     *
     *    -> sum interval =
     *        [ 10, 25 ]
     *
     */
  public int[] corpFlightBookings(int[][] bookings, int n) {
      // edge
      if(bookings == null || bookings.length == 0){
          return null;
      }
      if(n == 0){
          return new int[]{0}; // ??
      }
      // IDEA: prefix sum
      int[] preSum = new int[n];

      for(int[] b: bookings){
          int start = b[0];
          int end = b[1];
          int amount = b[2];
          for(int i = start-1; i < end; i++){
              preSum[i] += amount;
          }
      }

      return preSum;
  }

  // V1
  // IDEA : DIFFERENCE ARRAY (gpt)
  // https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/difference_array.md
  /**
   * time = O(bookings + n)
   * space = O(n)
   *
   *  IDEA : DIFFERENCE ARRAY  + presum
   *
   *  -> Step 1) for loop over bookings
   *    we add `seats` to res[start] to record the "starting point" of seats
   *    and subtract `seats`  (res[end + 1] -= seats;) to "remove" the "seats addition" so
   *    it NOT affects the following operation
   *
   *  -> Step 2) add up presum  ( for (int i = 1; i <= n; i++) {  res[i] += res[i - 1]; } )
   *     we add "added seats" within ranges, (DON'T need to worry about `end` index, since we already disable its effect in step 1)
   *     then we get the presum of seats per different start, end idx from different array
   *
   *  -> Step 3) copy the needed range of array as final result
   *
   */
  public int[] corpFlightBookings_1(int[][] bookings, int n) {
      int[] res = new int[n + 1]; // Use n+1 to handle range updates easily with a difference array.

      // Create a difference array
      for (int[] booking : bookings) {
          int start = booking[0]; // Starting index (1-based)
          int end = booking[1]; // Ending index (1-based)
          int seats = booking[2]; // Seats to add

          res[start] += seats; // Add seats at the start
      /**
       * NOTE !!!
       *
       *  why `if (end + 1 <= n) {res[end + 1] -= seats;}`; ?
       *
       *  ->
       *
       *  - 1) The diffArray[end] -= seats;
       *    statement marks the end of the booking range.
       *    It ensures that any flight after end will
       *    not be affected by this booking.
       *
       *
       *  - 2) The condition if (end < n) prevents going out of bounds.
       *    This is necessary because the end + 1 index might not exist
       *    if end is the last flight.
       *
       */
      if (end + 1 <= n) { // Subtract seats after the end
              res[end + 1] -= seats;
          }
      }

      // Compute the prefix sum to get the final result
      for (int i = 1; i <= n; i++) {
          res[i] += res[i - 1];
      }

      // Exclude the extra index used for the difference array
      return Arrays.copyOfRange(res, 1, n + 1);
  }

  // V2
  // https://leetcode.com/problems/corporate-flight-bookings/submissions/1483568206/
  /**

   * time = O(bookings + n)

   * space = O(n)

   */
  public int[] corpFlightBookings_2(int[][] bookings, int n) {
      int[] ans = new int[n];
      for (int[] booking : bookings) {
          int i = booking[0] - 1;
          int j = booking[1];
          int seats = booking[2];
          ans[i] += seats;
          if (j != n)
              ans[j] -= seats;
      }

      int count = 0;
      for (int i = 0; i < ans.length; i++) {
          ans[i] += count;
          count = ans[i];
      }
      return ans;
  }

  // V3-1
  // https://leetcode.com/problems/corporate-flight-bookings/solutions/1338804/java-2-approaches-brute-force-optimal-ap-e12t/
  // IDEA : BRUTE FORCE
  /**

   * time = O(bookings * n)

   * space = O(n)

   */
  public int[] corpFlightBookings_3_1(int[][] bookings, int n) {

      int[] res = new int[n];
      for (int[] curr : bookings) {
          int start = curr[0] - 1;
          int end = curr[1];
          int val = curr[2];

          for (int i = start; i < end; i++) {
              res[i] += val;
          }
      }
      return res;
  }

  // V3-2
  // https://leetcode.com/problems/corporate-flight-bookings/solutions/1338804/java-2-approaches-brute-force-optimal-ap-e12t/
  // Optimized  BRUTE FORCE
  /**

   * time = O(bookings + n)

   * space = O(n)

   */
  public int[] corpFlightBookings_3_2(int[][] bookings, int n) {

      int[] res = new int[n];
      for (int[] curr : bookings) {
          int start = curr[0] - 1;
          int end = curr[1];
          int val = curr[2];
          res[start] += val;
          if (end < n) {
              res[end] -= val;
          }
      }
      for (int i = 1; i < n; i++) {
          res[i] += res[i - 1];
      }
      return res;
  }



}
