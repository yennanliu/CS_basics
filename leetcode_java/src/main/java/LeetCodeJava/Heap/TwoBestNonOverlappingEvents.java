package LeetCodeJava.Heap;

// https://leetcode.com/problems/two-best-non-overlapping-events/

import java.util.Arrays;

/**
 *  2054. Two Best Non-Overlapping Events
 *  Medium
 *
 *  You are given a 0-indexed 2D integer array of events where
 *  events[i] = [startTime_i, endTime_i, value_i]. The ith event starts at
 *  startTime_i and ends at endTime_i, and if you attend this event you receive a
 *  value of value_i. You can choose at most two non-overlapping events to attend
 *  such that the sum of their values is maximized.
 *
 *  Return this maximum sum.
 *
 *  Note that the start time and end time are inclusive: if you attend an event with
 *  end time t, the next event must start at or after t + 1.
 *
 *  Example 1:
 *    Input: events = [[1,3,2],[4,5,2],[2,4,3]]
 *    Output: 4
 *
 *  Example 2:
 *    Input: events = [[1,3,2],[4,5,2],[1,5,5]]
 *    Output: 5
 *
 *  Example 3:
 *    Input: events = [[1,5,3],[1,5,1],[6,6,5]]
 *    Output: 8
 *
 *  Constraints:
 *    2 <= events.length <= 10^5
 *    events[i].length == 3
 *    1 <= startTime_i <= endTime_i <= 10^9
 *    1 <= value_i <= 10^6
 */
public class TwoBestNonOverlappingEvents {

    // V0
    // IDEA: SORT BY START + SUFFIX MAX + BINARY SEARCH
    //       sort events by start time and build suf[i] = max value among i..n-1.
    //       for an event (s, e, v) taken as the FIRST one, any legal partner must
    //       start at >= e + 1; sorted by start, those form the suffix beginning at
    //       the first index whose start > e (upper bound / bisect_right).
    //       candidate = v + suf[idx]; taking a single event is covered because suf
    //       contributes 0 past the end.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int maxTwoEvents(int[][] events) {
        int n = events.length;
        Arrays.sort(events, (a, b) -> Integer.compare(a[0], b[0]));

        int[] starts = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = events[i][0];
        }

        int[] suf = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            suf[i] = Math.max(suf[i + 1], events[i][2]);
        }

        int res = 0;
        for (int[] ev : events) {
            int idx = upperBound(starts, ev[1]); // first index with start > end
            res = Math.max(res, ev[2] + suf[idx]);
        }
        return res;
    }

    // first index i with arr[i] > target (arr sorted ascending)
    private int upperBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}
