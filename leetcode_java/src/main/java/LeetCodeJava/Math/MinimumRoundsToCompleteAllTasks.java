package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-rounds-to-complete-all-tasks/description/

import java.util.*;

/**
 * 2244. Minimum Rounds to Complete All Tasks
 * Medium
 *
 * You are given a 0-indexed integer array tasks, where tasks[i] represents the difficulty level of a task. In each round, you can complete either 2 or 3 tasks of the same difficulty level.
 *
 * Return the minimum rounds required to complete all the tasks, or -1 if it is not possible to complete all the tasks.
 *
 *
 * Example 1:
 *
 * Input: tasks = [2,2,3,3,2,4,4,4,4,4]
 * Output: 4
 * Explanation: To complete all the tasks, a possible plan is:
 * - In the first round, you complete 3 tasks of difficulty level 2.
 * - In the second round, you complete 2 tasks of difficulty level 3.
 * - In the third round, you complete 3 tasks of difficulty level 4.
 * - In the fourth round, you complete 2 tasks of difficulty level 4.
 * It can be shown that all the tasks cannot be completed in fewer than 4 rounds, so the answer is 4.
 *
 * Example 2:
 *
 * Input: tasks = [2,3,3]
 * Output: -1
 * Explanation: There is only 1 task of difficulty level 2, but in each round, you can only complete either 2 or 3 tasks of the same difficulty level. Hence, you cannot complete all the tasks, and the answer is -1.
 *
 *
 * Constraints:
 *
 * 1 <= tasks.length <= 10^5
 * 1 <= tasks[i] <= 10^9
 *
 */
public class MinimumRoundsToCompleteAllTasks {

    // V0
    // IDEA: HASH MAP COUNT + GREEDY - PER DIFFICULTY, USE AS MANY 3s AS POSSIBLE
    //   a count of 1 is impossible; any other count c needs ceil(c / 3) rounds
    //   (c % 3 == 1 swaps one triple for two pairs, c % 3 == 2 adds one pair).
    /**
     * time = O(n)
     * space = O(n)
     */
    public int minimumRounds(int[] tasks) {
        // how many tasks share each difficulty level
        Map<Integer, Integer> countByDifficulty = new HashMap<>();
        for (int task : tasks) {
            countByDifficulty.put(task, countByDifficulty.getOrDefault(task, 0) + 1);
        }

        int rounds = 0;
        for (int count : countByDifficulty.values()) {
            // a single task can never form a round of 2 or 3
            if (count == 1) {
                return -1;
            }
            // ceil(count / 3)
            rounds += (count + 2) / 3;
        }
        return rounds;
    }
}
