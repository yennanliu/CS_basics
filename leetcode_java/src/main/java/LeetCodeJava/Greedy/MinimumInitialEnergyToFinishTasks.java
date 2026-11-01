package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-initial-energy-to-finish-tasks/description/

import java.util.Arrays;

/**
 * 1665. Minimum Initial Energy to Finish Tasks
 * Hard
 *
 * You are given an array tasks where tasks[i] = [actuali, minimumi]:
 *
 * - actuali is the actual amount of energy you spend to finish the ith task.
 * - minimumi is the minimum amount of energy you require to begin the ith task.
 *
 * For example, if the task is [10, 12] and your current energy is 11, you cannot
 * start this task. However, if your current energy is 13, you can complete this
 * task, and your energy will be 3 after finishing it.
 *
 * You can finish the tasks in any order you like.
 *
 * Return the minimum initial amount of energy you will need to finish all the
 * tasks.
 *
 *
 * Example 1:
 *
 * Input: tasks = [[1,2],[2,4],[4,8]]
 * Output: 8
 *
 * Example 2:
 *
 * Input: tasks = [[1,3],[2,4],[10,11],[10,12],[8,9]]
 * Output: 32
 *
 * Example 3:
 *
 * Input: tasks = [[1,7],[2,8],[3,9],[4,10],[5,11],[6,12]]
 * Output: 27
 *
 *
 * Constraints:
 *
 * 1 <= tasks.length <= 10^5
 * 1 <= actuali <= minimumi <= 10^4
 *
 */
public class MinimumInitialEnergyToFinishTasks {

    // V0
    // IDEA: GREEDY + EXCHANGE-ARGUMENT SORT (BIGGEST SLACK FIRST)
    //       slack = minimum - actual. Comparing two adjacent tasks a, b shows the
    //       cheaper order does the LARGER slack first. Sort by slack descending,
    //       simulate, and top up the starting energy whenever we fall short.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int minimumEffort(int[][] tasks) {
        int[][] sorted = tasks.clone();
        // larger (minimum - actual) first
        Arrays.sort(sorted, (x, y) -> Integer.compare(y[1] - y[0], x[1] - x[0]));

        int initial = 0;
        int current = 0;
        for (int[] task : sorted) {
            int actual = task[0];
            int minimum = task[1];
            // not enough to start: we must have begun with (minimum - current) more
            if (current < minimum) {
                initial += minimum - current;
                current = minimum;
            }
            current -= actual;
        }
        return initial;
    }
}
