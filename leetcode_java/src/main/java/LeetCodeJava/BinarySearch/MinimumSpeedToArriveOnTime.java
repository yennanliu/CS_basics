package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-speed-to-arrive-on-time/description/

/**
 * 1870. Minimum Speed to Arrive on Time
 * Medium
 *
 * You are given a floating-point number hour, representing the amount of time you have to reach the
 * office. To commute to the office, you must take n trains in sequential order. You are also given an
 * integer array dist of length n, where dist[i] describes the distance (in kilometers) of the ith
 * train ride.
 *
 * Each train can only depart at an integer hour, so you may need to wait in between each train ride.
 *
 * For example, if the 1st train ride takes 1.5 hours, you must wait for an additional 0.5 hours before
 * you can depart on the 2nd train ride at the 2 hour mark.
 *
 * Return the minimum positive integer speed (in kilometers per hour) that all the trains must travel at
 * for you to reach the office on time, or -1 if it is impossible to be on time.
 *
 * Tests are generated such that the answer will not exceed 10^7 and hour will have at most two digits
 * after the decimal point.
 *
 *
 * Example 1:
 *
 * Input: dist = [1,3,2], hour = 6
 * Output: 1
 * Explanation: At speed 1:
 * - The first train ride takes 1/1 = 1 hour.
 * - Since we are already at an integer hour, we depart immediately at the 1 hour mark. The second train
 *   takes 3/1 = 3 hours.
 * - Since we are already at an integer hour, we depart immediately at the 4 hour mark. The third train
 *   takes 2/1 = 2 hours.
 * - You will arrive at exactly the 6 hour mark.
 *
 * Example 2:
 *
 * Input: dist = [1,3,2], hour = 2.7
 * Output: 3
 * Explanation: At speed 3:
 * - The first train ride takes 1/3 = 0.33333 hours.
 * - Since we are not at an integer hour, we wait until the 1 hour mark to depart. The second train ride
 *   takes 3/3 = 1 hour.
 * - Since we are already at an integer hour, we depart immediately at the 2 hour mark. The third train
 *   takes 2/3 = 0.66667 hours.
 * - You will arrive at the 2.66667 hour mark.
 *
 * Example 3:
 *
 * Input: dist = [1,3,2], hour = 1.9
 * Output: -1
 * Explanation: It is impossible because the earliest the third train can depart is at the 2 hour mark.
 *
 *
 * Constraints:
 *
 * n == dist.length
 * 1 <= n <= 10^5
 * 1 <= dist[i] <= 10^5
 * 1 <= hour <= 10^9
 * There will be at most two digits after the decimal point in hour.
 *
 */
public class MinimumSpeedToArriveOnTime {

    private static final int MAX_SPEED = 10_000_000;

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER (TOTAL TIME IS MONOTONE IN SPEED)
    /**
     *  total(v) = sum over the first n - 1 rides of ceil(dist[i] / v)  +  dist[n-1] / v
     *  never grows as v grows, so "total(v) <= hour" is monotone -> binary search the
     *  smallest v in [1, 10^7].
     *
     *  The first n - 1 rides each take at least one whole hour, so hour <= n - 1 is
     *  impossible -> -1.
     *
     *  hour has at most 2 decimals: scale it to hundredths and compare integers to avoid
     *  float rounding:  100 * (wholeHours * v + dist[n-1]) <= hundredths * v.
     */
    /**
     * time = O(N log(10^7))
     * space = O(1)
     */
    public int minSpeedOnTime(int[] dist, double hour) {
        int n = dist.length;
        long hundredths = Math.round(hour * 100);

        // the first n - 1 rides need at least n - 1 hours whatever the speed
        if (hundredths <= (long) (n - 1) * 100) {
            return -1;
        }

        int left = 1;
        int right = MAX_SPEED;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arrivesOnTime(dist, mid, hundredths)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        if (arrivesOnTime(dist, left, hundredths)) {
            return left;
        }
        return -1;
    }

    /** True when travelling at this speed arrives within the time limit (given in hundredths). */
    private boolean arrivesOnTime(int[] dist, long speed, long hundredths) {
        int n = dist.length;
        // every ride but the last is rounded up to a whole hour (wait for the next train)
        long wholeHours = 0;
        for (int i = 0; i < n - 1; i++) {
            wholeHours += (dist[i] + speed - 1) / speed;
        }
        // wholeHours + dist[n-1] / speed <= hundredths / 100, multiplied through by 100 * speed
        return 100 * (wholeHours * speed + dist[n - 1]) <= hundredths * speed;
    }
}
