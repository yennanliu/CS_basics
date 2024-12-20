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
  // TODO : implement
  //    public int[] corpFlightBookings(int[][] bookings, int n) {
  //
  //    }

  // V1
  // IDEA : difference ARRAY (gpt)
  public int[] corpFlightBookings_1(int[][] bookings, int n) {
      int[] res = new int[n + 1]; // Use n+1 to handle range updates easily with a difference array.

      // Create a difference array
      for (int[] booking : bookings) {
          int start = booking[0]; // Starting index (1-based)
          int end = booking[1]; // Ending index (1-based)
          int seats = booking[2]; // Seats to add

          res[start] += seats; // Add seats at the start
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
