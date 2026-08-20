package LeetCodeJava.Array;

// https://leetcode.com/problems/maximize-distance-to-closest-person/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 849. Maximize Distance to Closest Person
 * Medium
 * Topics
 * Companies
 * You are given an array representing a row of seats where seats[i] = 1 represents a person sitting in the ith seat, and seats[i] = 0 represents that the ith seat is empty (0-indexed).
 *
 * There is at least one empty seat, and at least one person sitting.
 *
 * Alex wants to sit in the seat such that the distance between him and the closest person to him is maximized.
 *
 * Return that maximum distance to the closest person.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: seats = [1,0,0,0,1,0,1]
 * Output: 2
 * Explanation:
 * If Alex sits in the second open seat (i.e. seats[2]), then the closest person has distance 2.
 * If Alex sits in any other open seat, the closest person has distance 1.
 * Thus, the maximum distance to the closest person is 2.
 * Example 2:
 *
 * Input: seats = [1,0,0,0]
 * Output: 3
 * Explanation:
 * If Alex sits in the last seat (i.e. seats[3]), the closest person is 3 seats away.
 * This is the maximum distance possible, so the answer is 3.
 * Example 3:
 *
 * Input: seats = [0,1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 2 <= seats.length <= 2 * 104
 * seats[i] is 0 or 1.
 * At least one seat is empty.
 * At least one seat is occupied.
 *
 */
public class MaximizeDistanceToClosestPerson {

  // V0
  // IDEA: 1 PASS SCAN over the `gaps` of empty seats
  /**
   *  NOTE !!!
   *
   *   there are 3 kinds of `gap` of empty seats, and each has its OWN distance:
   *
   *    1) leading gap   (e.g. `0001`)   -> dist = gap length          (sit at idx = 0)
   *    2) enclosed gap  (e.g. `10001`)  -> dist = (gap length + 1) / 2 (sit at the `middle`)
   *    3) trailing gap  (e.g. `10000`)  -> dist = gap length          (sit at the last idx)
   *
   *   -> so we just scan once, and collect the max over all of the above
   */
  /**
   * time = O(N)
   * space = O(1)
   */
  public int maxDistToClosest(int[] seats) {
      // edge
      if (seats == null || seats.length == 0) {
          return 0;
      }

      int res = 0;
      /** NOTE !!! `prev` = idx of the last seen occupied seat, -1 means `not seen yet` */
      int prev = -1;

      for (int i = 0; i < seats.length; i++) {
          if (seats[i] != 1) {
              continue;
          }
          if (prev == -1) {
              // case 1) leading gap : all `0` till the first `1`
              res = Math.max(res, i);
          } else {
              // case 2) enclosed gap : need to sit at the `middle` of the 2 persons
              res = Math.max(res, (i - prev) / 2);
          }
          prev = i;
      }

      // case 3) trailing gap : all `0` after the last `1`
      /** NOTE !!! `prev != -1` is guaranteed by the problem (at least 1 occupied seat) */
      if (prev != -1) {
          res = Math.max(res, seats.length - 1 - prev);
      }

      return res;
  }

  // V0-1
  // IDEA (fixed by gpt)
  /**
   *  IDEA :
   *
   *  Explanation of the Code:
   * 	1.	Initial Setup:
   * 	    •	lastOccupied keeps track of the index of the last seat occupied by a person.
   * 	    •	maxDistance is initialized to 0 to store the maximum distance found.
   *
   * 	2.	Iterate Through the Array:
   * 	    •	When a seat is occupied (seats[i] == 1):
   * 	    •	If it's the first occupied seat, calculate the distance from the start of the array to this seat (i).
   * 	    •	Otherwise, calculate the middle distance between the current and the last occupied seat using (i - lastOccupied) / 2.
   *
   * 	3.	Check the Last Segment:
   * 	    •	If the last seat is empty, calculate the distance from the last occupied seat to the end of the array (seats.length - 1 - lastOccupied).
   *
   * 	4.	Return the Maximum Distance:
   * 	    •	The value of maxDistance at the end of the loop is the answer.
   *
   *
   * Example :
   *  input : seats = [1, 0, 0, 0, 1, 0, 1]
   *
   *   Execution Steps:
   * 	1.	First occupied seat at index 0 → Distance to start = 0.
   * 	2.	Second occupied seat at index 4 → Middle distance = (4 - 0) / 2 = 2.
   * 	3.	Third occupied seat at index 6 → Middle distance = (6 - 4) / 2 = 1.
   * 	4.	No empty seats after the last occupied seat.
   * 	5.	maxDistance = 2.
   *
   *  output:  2
   *
   */
  /**
   *  Cases
   *
   *  Case 1)  0001  ( all "0" till meat first "1")
   *  Case 2)  1001001 (all "0" are enclosed by "1")
   *  Case 3)  1001000 (there are "0" that NOT enclosed by "1" on the right hand side)
   *
   */
  /**
   * time = O(N)
   * space = O(1)
   */
  public int maxDistToClosest_0_1(int[] seats) {
        int maxDistance = 0;
        int lastOccupied = -1;

        // Traverse the array to calculate maximum distances
        for (int i = 0; i < seats.length; i++) {
            /** NOTE !!! handle the seat val == 1 cases */
            if (seats[i] == 1) {
                if (lastOccupied == -1) {
                    // Handle the case where the `first` occupied seat is found
                    /**
                     * NOTE !!!
                     *
                     *  for handling below case:
                     *
                     *    e.g. :  0001
                     *
                     *  (so, elements are all "0" till first visit "1")
                     *  in this case, we still can get put a person to seat, and get distance
                     *
                     */
                    maxDistance = i; // Distance from the start to the first occupied seat
                } else {
                    // Calculate the distance to the closest person for the middle segment
                    /** NOTE !!! need to divided by 2, since the person need to seat at `middle` seat */
                    maxDistance = Math.max(maxDistance, (i - lastOccupied) / 2);
                }
                lastOccupied = i;
            }
        }

        // Handle the case where the last segment is empty
       /**
        *  NOTE !!!
        *
        *   the condition is actually quite straightforward,
        *   just need to check if the last element in array is "0"
        *   if is "0", means the array is NOT enclosed by "1"
        *   then we need to handle such case
        *   (example as below)
        *
        *   e.g.  100010000
        *
        */
        if (seats[seats.length - 1] == 0) {
            maxDistance = Math.max(maxDistance, seats.length - 1 - lastOccupied);
        }

        return maxDistance;
    }

    // V1-1
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/maximize-distance-to-closest-person/editorial/
    /**
     *  IDEA :
     *
     *  In a group of K adjacent empty seats between two people, the answer is (K+1) / 2.
     *
     * Algorithm
     *
     * For each group of K empty seats between two people,
     * we can take into account the candidate answer (K+1) / 2.
     *
     * For groups of empty seats between the edge of the row and one other person,
     * the answer is K, and we should take into account those answers too.
     *
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxDistToClosest_1_1(int[] seats) {
        int N = seats.length;
        int prev = -1, future = 0;
        int ans = 0;

        for (int i = 0; i < N; ++i) {
            if (seats[i] == 1) {
                prev = i;
            } else {
                while (future < N && seats[future] == 0 || future < i)
                    future++;

                int left = prev == -1 ? N : i - prev;
                int right = future == N ? N : future - i;
                ans = Math.max(ans, Math.min(left, right));
            }
        }

        return ans;
    }

    // V1-2
    // IDEA : Next Array
    // https://leetcode.com/problems/maximize-distance-to-closest-person/editorial/
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxDistToClosest_1_2(int[] seats) {
        int N = seats.length;
        int[] left = new int[N], right = new int[N];
        Arrays.fill(left, N);
        Arrays.fill(right, N);

        for (int i = 0; i < N; ++i) {
            if (seats[i] == 1)
                left[i] = 0;
            else if (i > 0)
                left[i] = left[i - 1] + 1;
        }

        for (int i = N - 1; i >= 0; --i) {
            if (seats[i] == 1)
                right[i] = 0;
            else if (i < N - 1)
                right[i] = right[i + 1] + 1;
        }

        int ans = 0;
        for (int i = 0; i < N; ++i)
            if (seats[i] == 0)
                ans = Math.max(ans, Math.min(left[i], right[i]));
        return ans;
    }

    // V2
}
