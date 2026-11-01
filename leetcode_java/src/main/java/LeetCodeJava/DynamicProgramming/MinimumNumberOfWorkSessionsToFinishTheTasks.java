package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-number-of-work-sessions-to-finish-the-tasks/description/

import java.util.Arrays;

/**
 * 1986. Minimum Number of Work Sessions to Finish the Tasks
 * Medium
 *
 * There are n tasks assigned to you. The task times are represented as an integer array
 * tasks of length n, where the ith task takes tasks[i] hours to finish. A work session is
 * when you work for at most sessionTime consecutive hours and then take a break.
 *
 * You should finish the given tasks in a way that satisfies the following conditions:
 *
 * If you start a task in a work session, you must complete it in the same work session.
 * You can start a new task immediately after finishing the previous one.
 * You may complete the tasks in any order.
 *
 * Given tasks and sessionTime, return the minimum number of work sessions needed to finish
 * all the tasks following the conditions above.
 *
 * The tests are generated such that sessionTime is greater than or equal to the maximum
 * element in tasks[i].
 *
 *
 * Example 1:
 *
 * Input: tasks = [1,2,3], sessionTime = 3
 * Output: 2
 *
 * Example 2:
 *
 * Input: tasks = [3,1,3,1,1], sessionTime = 8
 * Output: 2
 *
 * Example 3:
 *
 * Input: tasks = [1,2,3,4,5], sessionTime = 15
 * Output: 1
 *
 *
 * Constraints:
 *
 * n == tasks.length
 * 1 <= n <= 14
 * 1 <= tasks[i] <= 10
 * max(tasks[i]) <= sessionTime <= 15
 *
 */
public class MinimumNumberOfWorkSessionsToFinishTheTasks {

    // V0
    // IDEA: BITMASK DP + SUBMASK ENUMERATION
    /**
     *  n <= 14, so a mask over the tasks has at most 2^14 = 16384 states.
     *
     *  DP def:
     *     - cost[mask]     = total hours of the tasks in mask
     *     - sessions[mask] = min number of sessions to finish exactly the tasks in mask
     *
     *  DP eq:
     *     - sessions[mask] = min over submasks sub of mask with cost[sub] <= sessionTime
     *                        of sessions[mask without sub] + 1
     *       (sub is the set of tasks done in the LAST session)
     *
     *  Init: sessions[0] = 0.  Answer: sessions[all tasks].
     *
     *  time = O(3^N)
     *  space = O(2^N)
     */
    public int minSessions(int[] tasks, int sessionTime) {
        int n = tasks.length;
        int fullMask = (1 << n) - 1;

        // cost[mask] = sum of task hours whose bit is set in mask
        int[] cost = new int[fullMask + 1];
        for (int mask = 1; mask <= fullMask; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    // reuse the mask with task i removed, then add task i back
                    cost[mask] = cost[mask ^ (1 << i)] + tasks[i];
                    break;
                }
            }
        }

        int[] sessions = new int[fullMask + 1];
        Arrays.fill(sessions, Integer.MAX_VALUE);
        sessions[0] = 0;

        for (int mask = 1; mask <= fullMask; mask++) {
            // walk every non-empty submask of mask:
            // (sub - 1) & mask is the next smaller submask
            for (int sub = mask; sub > 0; sub = (sub - 1) & mask) {
                if (cost[sub] > sessionTime) {
                    continue;  // these tasks do not fit in one session
                }
                int rest = mask ^ sub;
                if (sessions[rest] != Integer.MAX_VALUE) {
                    sessions[mask] = Math.min(sessions[mask], sessions[rest] + 1);
                }
            }
        }
        return sessions[fullMask];
    }
}
