package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-number-of-refueling-stops/description/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * 871. Minimum Number of Refueling Stops Hard Topics Companies A car travels from a starting
 * position to a destination which is target miles east of the starting position.
 *
 * <p>There are gas stations along the way. The gas stations are represented as an array stations
 * where stations[i] = [positioni, fueli] indicates that the ith gas station is positioni miles east
 * of the starting position and has fueli liters of gas.
 *
 * <p>The car starts with an infinite tank of gas, which initially has startFuel liters of fuel in
 * it. It uses one liter of gas per one mile that it drives. When the car reaches a gas station, it
 * may stop and refuel, transferring all the gas from the station into the car.
 *
 * <p>Return the minimum number of refueling stops the car must make in order to reach its
 * destination. If it cannot reach the destination, return -1.
 *
 * <p>Note that if the car reaches a gas station with 0 fuel left, the car can still refuel there.
 * If the car reaches the destination with 0 fuel left, it is still considered to have arrived.
 *
 * <p>Example 1:
 *
 * <p>Input: target = 1, startFuel = 1, stations = [] Output: 0 Explanation: We can reach the target
 * without refueling. Example 2:
 *
 * <p>Input: target = 100, startFuel = 1, stations = [[10,100]] Output: -1 Explanation: We can not
 * reach the target (or even the first gas station). Example 3:
 *
 * <p>Input: target = 100, startFuel = 10, stations = [[10,60],[20,30],[30,30],[60,40]] Output: 2
 * Explanation: We start with 10 liters of fuel. We drive to position 10, expending 10 liters of
 * fuel. We refuel from 0 liters to 60 liters of gas. Then, we drive from position 10 to position 60
 * (expending 50 liters of fuel), and refuel from 10 liters to 50 liters of gas. We then drive to
 * and reach the target. We made 2 refueling stops along the way, so we return 2.
 *
 * <p>Constraints:
 *
 * <p>1 <= target, startFuel <= 109 0 <= stations.length <= 500 1 <= positioni < positioni+1 <
 * target 1 <= fueli < 109
 */
public class MinimumNumberOfRefuelingStops {

  // V0
  // TODO : implement
  //    public int minRefuelStops(int target, int startFuel, int[][] stations) {
  //
  //    }

  // V1
  // IDEA : DP
  // https://leetcode.com/problems/minimum-number-of-refueling-stops/editorial/
  /**
   * time = O(N)
   * space = O(N)
   */
  public int minRefuelStops_1(int target, int startFuel, int[][] stations) {
    int N = stations.length;
    long[] dp = new long[N + 1];
    dp[0] = startFuel;
    for (int i = 0; i < N; ++i)
      for (int t = i; t >= 0; --t)
        if (dp[t] >= stations[i][0]) dp[t + 1] = Math.max(dp[t + 1], dp[t] + (long) stations[i][1]);

    for (int i = 0; i <= N; ++i) if (dp[i] >= target) return i;
    return -1;
  }

  // V2
  // IDEA : HEAP
  // https://leetcode.com/problems/minimum-number-of-refueling-stops/editorial/
  /**
   * time = O(N)
   * space = O(N)
   */
  public int minRefuelStops(int target, int tank, int[][] stations) {
    // pq is a maxheap of gas station capacities
    PriorityQueue<Integer> pq = new PriorityQueue(Collections.reverseOrder());
    int ans = 0, prev = 0;
    for (int[] station : stations) {
      int location = station[0];
      int capacity = station[1];
      tank -= location - prev;
      while (!pq.isEmpty() && tank < 0) { // must refuel in past
        tank += pq.poll();
        ans++;
      }

      if (tank < 0) return -1;
      pq.offer(capacity);
      prev = location;
    }

    // Repeat body for station = (target, inf)
    {
      tank -= target - prev;
      while (!pq.isEmpty() && tank < 0) {
        tank += pq.poll();
        ans++;
      }
      if (tank < 0) return -1;
    }

    return ans;
  }

}
