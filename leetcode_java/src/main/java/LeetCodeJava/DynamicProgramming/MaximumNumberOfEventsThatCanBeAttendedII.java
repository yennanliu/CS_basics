package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended-ii/description/

import java.util.*;

/**
 * 1751. Maximum Number of Events That Can Be Attended II
 * Hard
 *
 * You are given an array of events where events[i] = [startDayi, endDayi, valuei]. The
 * i^th event starts at startDayi and ends at endDayi, and if you attend this event, you
 * will receive a value of valuei. You are also given an integer k which represents the
 * maximum number of events you can attend.
 *
 * You can only attend one event at a time. If you choose to attend an event, you must
 * attend the entire event. Note that the end day is inclusive: that is, you cannot attend
 * two events where one of them starts and the other ends on the same day.
 *
 * Return the maximum sum of values that you can receive by attending events.
 *
 * Example 1:
 *
 * Input: events = [[1,2,4],[3,4,3],[2,3,1]], k = 2
 * Output: 7
 * Explanation: Choose the green events, 0 and 1 (0-indexed) for a total value of 4 + 3 = 7.
 *
 * Example 2:
 *
 * Input: events = [[1,2,4],[3,4,3],[2,3,10]], k = 2
 * Output: 10
 * Explanation: Choose event 2 for a total value of 10.
 * Notice that you cannot attend any other event as they overlap, and that you do not
 * have to attend k events.
 *
 * Example 3:
 *
 * Input: events = [[1,1,1],[2,2,2],[3,3,3],[4,4,4]], k = 3
 * Output: 9
 * Explanation: Although the events do not overlap, you can only attend 3 events. Pick
 * the highest valued three.
 *
 * Constraints:
 *
 * 1 <= k <= events.length
 * 1 <= k * events.length <= 10^6
 * 1 <= startDayi <= endDayi <= 10^9
 * 1 <= valuei <= 10^6
 *
 */
public class MaximumNumberOfEventsThatCanBeAttendedII {

    // V0
    // IDEA: SORT BY END DAY + DP + BINARY SEARCH (weighted interval scheduling with k picks)
    /**
     *  DP def:
     *     - dp[i][j] = max value using only the first i events (sorted by end day),
     *                  attending at most j of them
     *
     *  DP eq:
     *     - dp[i][j] = max( dp[i - 1][j],                 // skip event i - 1
     *                       dp[p][j - 1] + value )        // attend it
     *       where p = number of events (among the first i - 1) that END strictly
     *       before this event starts -> found by binary search on the end days.
     *
     *  time = O(N log N + N * K)
     *  space = O(N * K)
     */
    public int maxValue(int[][] events, int k) {
        int n = events.length;

        // sort by end day so "all compatible earlier events" is a prefix
        Arrays.sort(events, (a, b) -> Integer.compare(a[1], b[1]));

        int[][] dp = new int[n + 1][k + 1];
        for (int i = 1; i <= n; i++) {
            int start = events[i - 1][0];
            int value = events[i - 1][2];

            // count of earlier events whose end day < start
            int compatible = countEndingBefore(events, i - 1, start);

            for (int j = 1; j <= k; j++) {
                int skip = dp[i - 1][j];
                int take = dp[compatible][j - 1] + value;
                dp[i][j] = Math.max(skip, take);
            }
        }
        return dp[n][k];
    }

    /** Number of events in events[0..limit) whose end day is strictly less than day. */
    private int countEndingBefore(int[][] events, int limit, int day) {
        // first index in [0, limit) with end >= day
        int left = 0;
        int right = limit;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (events[mid][1] < day) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
