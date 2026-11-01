package LeetCodeJava.Array;

// https://leetcode.com/problems/earliest-time-to-finish-one-task/description/

/**
 * 3683. Earliest Time to Finish One Task
 * Easy
 *
 * You are given a 2D integer array tasks where tasks[i] = [si, ti].
 *
 * Each [si, ti] in tasks represents a task with start time si that takes ti units
 * of time to finish.
 *
 * Return the earliest time at which at least one task is finished.
 *
 *
 * Example 1:
 *
 * Input: tasks = [[1,6],[2,3]]
 * Output: 5
 *
 * Example 2:
 *
 * Input: tasks = [[100,100],[100,100],[100,100]]
 * Output: 200
 *
 *
 * Constraints:
 *
 * 1 <= tasks.length <= 100
 * tasks[i] = [si, ti]
 * 1 <= si, ti <= 100
 *
 */
public class EarliestTimeToFinishOneTask {

    // V0
    // IDEA: EACH TASK FINISHES INDEPENDENTLY AT s + t -- TAKE THE MINIMUM
    /**
     * time = O(N)
     * space = O(1)
     */
    public int earliestTime(int[][] tasks) {
        int earliest = Integer.MAX_VALUE;
        for (int[] task : tasks) {
            int finish = task[0] + task[1];
            earliest = Math.min(earliest, finish);
        }
        return earliest;
    }
}
