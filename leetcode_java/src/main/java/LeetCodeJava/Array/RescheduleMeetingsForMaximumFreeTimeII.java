package LeetCodeJava.Array;

// https://leetcode.com/problems/reschedule-meetings-for-maximum-free-time-ii/description/

/**
 * 3440. Reschedule Meetings for Maximum Free Time II
 * Medium
 *
 * You are given an integer eventTime denoting the duration of an event. You are
 * also given two integer arrays startTime and endTime, each of length n.
 *
 * These represent the start and end times of n non-overlapping meetings that occur
 * during the event between time t = 0 and time t = eventTime, where the i^th
 * meeting occurs during the time [startTime[i], endTime[i]].
 *
 * You can reschedule at most one meeting by moving its start time while
 * maintaining the same duration, such that the meetings remain non-overlapping, to
 * maximize the longest continuous period of free time during the event.
 *
 * Return the maximum amount of free time possible after rearranging the meetings.
 *
 * Note that the meetings can not be rescheduled to a time outside the event and
 * they should remain non-overlapping.
 *
 * Note: In this version, it is valid for the relative ordering of the meetings to
 * change after rescheduling one meeting.
 *
 * Example 1:
 *
 * Input: eventTime = 5, startTime = [1,3], endTime = [2,5]
 *
 * Output: 2
 *
 * Explanation:
 *
 * Reschedule the meeting at [1, 2] to [2, 3], leaving no meetings during the time
 * [0, 2].
 *
 * Example 2:
 *
 * Input: eventTime = 10, startTime = [0,7,9], endTime = [1,8,10]
 *
 * Output: 7
 *
 * Explanation:
 *
 * Reschedule the meeting at [0, 1] to [8, 9], leaving no meetings during the time
 * [0, 7].
 *
 * Example 3:
 *
 * Input: eventTime = 10, startTime = [0,3,7,9], endTime = [1,4,8,10]
 *
 * Output: 6
 *
 * Explanation:
 *
 * Reschedule the meeting at [3, 4] to [8, 9], leaving no meetings during the time
 * [1, 7].
 *
 * Example 4:
 *
 * Input: eventTime = 5, startTime = [0,1,2,3,4], endTime = [1,2,3,4,5]
 *
 * Output: 0
 *
 * Explanation:
 *
 * There is no time during the event not occupied by meetings.
 *
 * Constraints:
 *
 * 1 <= eventTime <= 10^9
 * n == startTime.length == endTime.length
 * 2 <= n <= 10^5
 * 0 <= startTime[i] < endTime[i] <= eventTime
 * endTime[i] <= startTime[i + 1] where i lies in the range [0, n - 2].
 *
 */
public class RescheduleMeetingsForMaximumFreeTimeII {

    // V0
    // IDEA: GAPS + PREFIX / SUFFIX MAX OF GAPS
    //   moving meeting i always merges gaps i and i + 1; if some OTHER gap is wide enough
    //   to host the meeting, the meeting's own duration is freed too.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maxFreeTime(int eventTime, int[] startTime, int[] endTime) {
        int n = startTime.length;

        // gaps[i] = free time before meeting i; gaps[n] = free time after the last meeting
        int[] gaps = new int[n + 1];
        gaps[0] = startTime[0];
        for (int i = 1; i < n; i++) {
            gaps[i] = startTime[i] - endTime[i - 1];
        }
        gaps[n] = eventTime - endTime[n - 1];

        // maxBefore[i] = widest gap among gaps[0 .. i-1]
        int[] maxBefore = new int[n + 1];
        int running = 0;
        for (int i = 0; i <= n; i++) {
            maxBefore[i] = running;
            running = Math.max(running, gaps[i]);
        }

        // maxAfter[i] = widest gap among gaps[i+1 .. n]
        int[] maxAfter = new int[n + 1];
        running = 0;
        for (int i = n; i >= 0; i--) {
            maxAfter[i] = running;
            running = Math.max(running, gaps[i]);
        }

        int best = 0;
        for (int i = 0; i < n; i++) {
            int duration = endTime[i] - startTime[i];
            // slide meeting i against a neighbour: its two side gaps merge
            int freed = gaps[i] + gaps[i + 1];

            // a gap outside {i, i + 1} that fits the meeting lets us lift it out entirely
            int widestOutside = Math.max(maxBefore[i], maxAfter[i + 1]);
            if (widestOutside >= duration) {
                freed += duration;
            }
            best = Math.max(best, freed);
        }
        return best;
    }
}
