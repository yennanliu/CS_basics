package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/reschedule-meetings-for-maximum-free-time-i/description/

/**
 * 3439. Reschedule Meetings for Maximum Free Time I
 * Medium
 *
 * You are given an integer eventTime denoting the duration of an event, where the event occurs
 * from time t = 0 to time t = eventTime.
 *
 * You are also given two integer arrays startTime and endTime, each of length n. These represent
 * the start and end time of n non-overlapping meetings, where the i^th meeting occurs during the
 * time [startTime[i], endTime[i]].
 *
 * You can reschedule at most k meetings by moving their start time while maintaining the same
 * duration, to maximize the longest continuous period of free time during the event.
 *
 * The relative order of all the meetings should stay the same and they should remain
 * non-overlapping.
 *
 * Return the maximum amount of free time possible after rearranging the meetings.
 *
 * Note that the meetings can not be rescheduled to a time outside the event.
 *
 *
 * Example 1:
 *
 * Input: eventTime = 5, k = 1, startTime = [1,3], endTime = [2,5]
 * Output: 2
 *
 * Example 2:
 *
 * Input: eventTime = 10, k = 1, startTime = [0,2,9], endTime = [1,4,10]
 * Output: 6
 *
 * Example 3:
 *
 * Input: eventTime = 5, k = 2, startTime = [0,1,2,3,4], endTime = [1,2,3,4,5]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= eventTime <= 10^9
 * n == startTime.length == endTime.length
 * 2 <= n <= 10^5
 * 1 <= k <= n
 * 0 <= startTime[i] < endTime[i] <= eventTime
 * endTime[i] <= startTime[i + 1] where i lies in the range [0, n - 2].
 *
 */
public class RescheduleMeetingsForMaximumFreeTimeI {

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW OVER THE n + 1 GAPS
    //  moving k consecutive meetings to one side welds together the k + 1 gaps around them, and
    //  no k moves can merge more, so the answer is the largest sum of k + 1 consecutive gaps.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxFreeTime(int eventTime, int k, int[] startTime, int[] endTime) {
        int n = startTime.length;

        // gaps[0] before the first meeting, gaps[i] between meetings i-1 and i, gaps[n] after the last
        int[] gaps = new int[n + 1];
        gaps[0] = startTime[0];
        for (int i = 1; i < n; i++) {
            gaps[i] = startTime[i] - endTime[i - 1];
        }
        gaps[n] = eventTime - endTime[n - 1];

        int width = k + 1;
        // total free time <= eventTime <= 10^9, so int is safe
        int windowSum = 0;
        for (int i = 0; i < width; i++) {
            windowSum += gaps[i];
        }
        int best = windowSum;
        for (int i = width; i < gaps.length; i++) {
            windowSum += gaps[i] - gaps[i - width];
            best = Math.max(best, windowSum);
        }
        return best;
    }
}
