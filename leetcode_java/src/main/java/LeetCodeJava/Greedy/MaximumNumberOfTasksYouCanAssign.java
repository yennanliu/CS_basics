package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-number-of-tasks-you-can-assign/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * 2071. Maximum Number of Tasks You Can Assign
 * Hard
 *
 * You have n tasks and m workers. Each task has a strength requirement stored in a 0-indexed integer array
 * tasks, with the ith task requiring tasks[i] strength to complete. The strength of each worker is stored in
 * a 0-indexed integer array workers, with the jth worker having workers[j] strength. Each worker can only be
 * assigned to a single task and must have a strength greater than or equal to the task's strength
 * requirement (i.e., workers[j] >= tasks[i]).
 *
 * Additionally, you have pills magical pills that will increase a worker's strength by strength. You can
 * decide which workers receive the magical pills, however, you may only give each worker at most one magical
 * pill.
 *
 * Given the 0-indexed integer arrays tasks and workers and the integers pills and strength, return the
 * maximum number of tasks that can be completed.
 *
 *
 * Example 1:
 *
 * Input: tasks = [3,2,1], workers = [0,3,3], pills = 1, strength = 1
 * Output: 3
 *
 * Example 2:
 *
 * Input: tasks = [5,4], workers = [0,0,0], pills = 1, strength = 5
 * Output: 1
 *
 * Example 3:
 *
 * Input: tasks = [10,15,30], workers = [0,10,10,10,10], pills = 3, strength = 10
 * Output: 2
 *
 *
 * Constraints:
 *
 * n == tasks.length
 * m == workers.length
 * 1 <= n, m <= 5 * 10^4
 * 0 <= pills <= m
 * 0 <= tasks[i], workers[j], strength <= 10^9
 *
 */
public class MaximumNumberOfTasksYouCanAssign {

    // V0
    // IDEA: BINARY SEARCH ON THE ANSWER k + GREEDY FEASIBILITY WITH A DEQUE
    /**
     * time = O(N log N + M log M + (N + M) log(min(N, M)))
     * space = O(N + M)
     */
    public int maxTaskAssign(int[] tasks, int[] workers, int pills, int strength) {
        int[] sortedTasks = tasks.clone();
        int[] sortedWorkers = workers.clone();
        Arrays.sort(sortedTasks);
        Arrays.sort(sortedWorkers);

        // if k tasks can be done, so can k - 1: the predicate is monotone in k
        int left = 0;
        int right = Math.min(sortedTasks.length, sortedWorkers.length);
        while (left < right) {
            // upper mid, so `left = mid` always makes progress
            int mid = left + (right - left + 1) / 2;
            if (canFinish(sortedTasks, sortedWorkers, pills, strength, mid)) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }

    /** Can the k easiest tasks be done by the k strongest workers with the given pills? */
    private boolean canFinish(int[] tasks, int[] workers, int pills, int strength, int k) {
        int m = workers.length;
        // workers who could do the current task WITH a pill; strongest at the front, weakest at the back
        Deque<Integer> candidates = new ArrayDeque<>();
        int workerIdx = m - 1;
        int pillsLeft = pills;

        // hardest of the k easiest tasks first
        for (int i = k - 1; i >= 0; i--) {
            int task = tasks[i];

            // admit every (still unused) strong worker who reaches this task with a pill;
            // long arithmetic: worker + strength can pass Integer.MAX_VALUE
            while (workerIdx >= m - k && (long) workers[workerIdx] + strength >= task) {
                candidates.addLast(workers[workerIdx]);
                workerIdx--;
            }

            if (candidates.isEmpty()) {
                return false;
            }

            if (candidates.peekFirst() >= task) {
                // the strongest available worker does it unaided - no pill spent
                candidates.pollFirst();
            } else {
                // nobody can do it unaided: spend a pill on the WEAKEST candidate,
                // saving the stronger ones for the harder tasks still to come
                if (pillsLeft == 0) {
                    return false;
                }
                pillsLeft--;
                candidates.pollLast();
            }
        }
        return true;
    }
}
