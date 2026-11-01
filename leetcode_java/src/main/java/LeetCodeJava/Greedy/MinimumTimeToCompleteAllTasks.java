package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-time-to-complete-all-tasks/description/

import java.util.*;

/**
 * 2589. Minimum Time to Complete All Tasks
 * Hard
 *
 * There is a computer that can run an unlimited number of tasks at the same time. You are given a 2D integer array tasks where tasks[i] = [starti, endi, durationi] indicates that the ith task should run for a total of durationi seconds (not necessarily continuous) within the inclusive time range [starti, endi].
 *
 * You may turn on the computer only when it needs to run a task. You can also turn it off if it is idle.
 *
 * Return the minimum time during which the computer should be turned on to complete all tasks.
 *
 *
 * Example 1:
 *
 * Input: tasks = [[2,3,1],[4,5,1],[1,5,2]]
 * Output: 2
 * Explanation:
 * - The first task can be run in the inclusive time range [2, 2].
 * - The second task can be run in the inclusive time range [5, 5].
 * - The third task can be run in the two inclusive time ranges [2, 2] and [5, 5].
 * The computer will be on for a total of 2 seconds.
 *
 * Example 2:
 *
 * Input: tasks = [[1,3,2],[2,5,3],[5,6,2]]
 * Output: 4
 * Explanation:
 * - The first task can be run in the inclusive time range [2, 3].
 * - The second task can be run in the inclusive time ranges [2, 3] and [5, 5].
 * - The third task can be run in the two inclusive time range [5, 6].
 * The computer will be on for a total of 4 seconds.
 *
 *
 * Constraints:
 *
 * 1 <= tasks.length <= 2000
 * tasks[i].length == 3
 * 1 <= starti, endi <= 2000
 * 1 <= durationi <= endi - starti + 1
 *
 */
public class MinimumTimeToCompleteAllTasks {

    private static final int MAX_TIME = 2000;

    // V0
    // IDEA: GREEDY (SORT BY END TIME, THEN TURN ON THE LATEST SECONDS)
    //       process tasks by increasing end. seconds already on inside [start, end] count for free;
    //       the rest are switched on as LATE as possible, because a later second lies inside more
    //       of the future windows (they all end at or after this one).
    /**
     * time = O(N log N + N * T)   (T = 2000, the time axis)
     * space = O(T)
     */
    public int findMinimumTime(int[][] tasks) {
        // sort a copy by end time
        int[][] sorted = new int[tasks.length][];
        for (int i = 0; i < tasks.length; i++) {
            sorted[i] = tasks[i];
        }
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[1], b[1]));

        boolean[] isOn = new boolean[MAX_TIME + 1];
        int totalOn = 0;

        for (int[] task : sorted) {
            int start = task[0];
            int end = task[1];
            int duration = task[2];

            // seconds of this window that earlier tasks already turned on
            int alreadyOn = 0;
            for (int t = start; t <= end; t++) {
                if (isOn[t]) {
                    alreadyOn++;
                }
            }
            int need = duration - alreadyOn;

            // switch on the missing seconds from the right edge leftwards
            for (int t = end; t >= start && need > 0; t--) {
                if (!isOn[t]) {
                    isOn[t] = true;
                    need--;
                    totalOn++;
                }
            }
        }
        return totalOn;
    }
}
