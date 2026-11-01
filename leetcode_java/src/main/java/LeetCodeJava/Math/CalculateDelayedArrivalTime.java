package LeetCodeJava.Math;

// https://leetcode.com/problems/calculate-delayed-arrival-time/description/

/**
 * 2651. Calculate Delayed Arrival Time
 * Easy
 *
 * You are given a positive integer arrivalTime denoting the arrival time of a train in hours, and another positive integer delayedTime denoting the amount of delay in hours.
 *
 * Return the time when the train will arrive at the station.
 *
 * Note that the time in this problem is in 24-hours format.
 *
 *
 * Example 1:
 *
 * Input: arrivalTime = 15, delayedTime = 5
 * Output: 20
 *
 * Example 2:
 *
 * Input: arrivalTime = 13, delayedTime = 11
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= arrivaltime < 24
 * 1 <= delayedTime <= 24
 *
 */
public class CalculateDelayedArrivalTime {

    private static final int HOURS_PER_DAY = 24;

    // V0
    // IDEA: MODULAR CLOCK ARITHMETIC -- 24 must map to 0, which % 24 does
    /**
     * time = O(1)
     * space = O(1)
     */
    public int findDelayedArrivalTime(int arrivalTime, int delayedTime) {
        return (arrivalTime + delayedTime) % HOURS_PER_DAY;
    }
}
