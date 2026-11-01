package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-time-to-complete-trips/description/

/**
 * 2187. Minimum Time to Complete Trips
 * Medium
 *
 * You are given an array time where time[i] denotes the time taken by the ith bus to complete one trip.
 *
 * Each bus can make multiple trips successively; that is, the next trip can start immediately after
 * completing the current trip. Also, each bus operates independently; that is, the trips of one bus do
 * not influence the trips of any other bus.
 *
 * You are also given an integer totalTrips, which denotes the number of trips all buses should make in
 * total. Return the minimum time required for all buses to complete at least totalTrips trips.
 *
 *
 * Example 1:
 *
 * Input: time = [1,2,3], totalTrips = 5
 * Output: 3
 * Explanation:
 * - At time t = 1, the number of trips completed by each bus are [1,0,0].
 *   The total number of trips completed is 1 + 0 + 0 = 1.
 * - At time t = 2, the number of trips completed by each bus are [2,1,0].
 *   The total number of trips completed is 2 + 1 + 0 = 3.
 * - At time t = 3, the number of trips completed by each bus are [3,1,1].
 *   The total number of trips completed is 3 + 1 + 1 = 5.
 * So the minimum time needed for all buses to complete at least 5 trips is 3.
 *
 * Example 2:
 *
 * Input: time = [2], totalTrips = 1
 * Output: 2
 * Explanation:
 * There is only one bus, and it will complete its first trip at t = 2.
 * So the minimum time needed to complete 1 trip is 2.
 *
 *
 * Constraints:
 *
 * 1 <= time.length <= 10^5
 * 1 <= time[i], totalTrips <= 10^7
 *
 */
public class MinimumTimeToCompleteTrips {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER (A TIME, NOT AN INDEX)
    /**
     *  In t units bus i finishes t / time[i] trips, so the fleet completes
     *  sum(t / time[i]) trips: a non-decreasing function of t, which is what makes binary
     *  search valid.
     *
     *  Search t over [1, min(time) * totalTrips]: the fastest bus alone needs that long,
     *  so it is certainly enough. That bound reaches 10^14, hence long.
     */
    /**
     * time = O(N log(min(time) * totalTrips))
     * space = O(1)
     */
    public long minimumTime(int[] time, int totalTrips) {
        int fastest = Integer.MAX_VALUE;
        for (int t : time) {
            fastest = Math.min(fastest, t);
        }

        long left = 1;
        long right = (long) fastest * totalTrips;
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (tripsDone(time, mid, totalTrips) >= totalTrips) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** Trips the fleet finishes within `elapsed`, stopping once `target` is reached. */
    private long tripsDone(int[] time, long elapsed, long target) {
        long trips = 0;
        for (int t : time) {
            trips += elapsed / t;
            // stop early: also keeps the sum far from overflowing
            if (trips >= target) {
                break;
            }
        }
        return trips;
    }
}
