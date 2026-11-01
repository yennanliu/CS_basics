package LeetCodeJava.Heap;

// https://leetcode.com/problems/process-tasks-using-servers/description/

import java.util.PriorityQueue;

/**
 * 1882. Process Tasks Using Servers
 * Medium
 *
 * You are given two 0-indexed integer arrays servers and tasks of lengths n and m
 * respectively. servers[i] is the weight of the i-th server, and tasks[j] is the time
 * needed to process the j-th task in seconds.
 *
 * Tasks are assigned to the servers using a task queue. Initially, all servers are
 * free, and the queue is empty.
 *
 * At second j, the j-th task is inserted into the queue (starting with the 0-th task
 * being inserted at second 0). As long as there are free servers and the queue is not
 * empty, the task in the front of the queue will be assigned to a free server with
 * the smallest weight, and in case of a tie, it is assigned to a free server with the
 * smallest index.
 *
 * If there are no free servers and the queue is not empty, we wait until a server
 * becomes free and immediately assign the next task. If multiple servers become free
 * at the same time, then multiple tasks from the queue will be assigned in order of
 * insertion following the weight and index priorities above.
 *
 * A server that is assigned task j at second t will be free again at second
 * t + tasks[j].
 *
 * Build an array ans of length m, where ans[j] is the index of the server the j-th
 * task will be assigned to.
 *
 * Return the array ans.
 *
 *
 * Example 1:
 *
 * Input: servers = [3,3,2], tasks = [1,2,3,2,1,2]
 * Output: [2,2,0,2,1,2]
 * Explanation: Events in chronological order go as follows:
 * - At second 0, task 0 is added and processed using server 2 until second 1.
 * - At second 1, server 2 gets free. Task 1 is added and processed using server 2
 *   until second 3.
 * - At second 2, task 2 is added and processed using server 0 until second 5.
 * - At second 3, server 2 gets free. Task 3 is added and processed using server 2
 *   until second 5.
 * - At second 4, task 4 is added and processed using server 1 until second 5.
 * - At second 5, all servers get free. Task 5 is added and processed using server 2
 *   until second 7.
 *
 * Example 2:
 *
 * Input: servers = [5,1,4,3,2], tasks = [2,1,2,4,5,2,1]
 * Output: [1,4,1,4,1,3,2]
 *
 *
 * Constraints:
 *
 * servers.length == n
 * tasks.length == m
 * 1 <= n, m <= 2 * 10^5
 * 1 <= servers[i], tasks[j] <= 2 * 10^5
 *
 */
public class ProcessTasksUsingServers {

    // V0
    // IDEA: TWO HEAPS (free servers by (weight, index), busy servers by (freeAt, weight, index))
    // task j arrives at second j: first move every busy server with freeAt <= j back to `free`, then
    //   - free is non-empty -> take the smallest (weight, index)                       [the rule]
    //   - free is empty     -> wait for the busy server that comes back soonest, and start the task
    //                          at THAT moment (its freeAt), not at second j
    // NOTE: freeAt can reach about 2*10^5 * 2*10^5 = 4*10^10, so it is a long.
    /**
     * time = O((N + M) log N)
     * space = O(N)
     */
    public int[] assignTasks(int[] servers, int[] tasks) {
        int m = tasks.length;
        int[] result = new int[m];
        // edge case: no tasks to assign
        if (m == 0) {
            return result;
        }

        // free servers: {weight, index}, smallest weight first, then smallest index
        PriorityQueue<int[]> free = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });
        // busy servers: {freeAt, weight, index}, earliest first, then the same rule as `free`
        PriorityQueue<long[]> busy = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) {
                return Long.compare(a[0], b[0]);
            }
            if (a[1] != b[1]) {
                return Long.compare(a[1], b[1]);
            }
            return Long.compare(a[2], b[2]);
        });

        for (int i = 0; i < servers.length; i++) {
            free.add(new int[] { servers[i], i });
        }

        for (int j = 0; j < m; j++) {
            // every server that finished at or before second j is available again
            while (!busy.isEmpty() && busy.peek()[0] <= j) {
                long[] done = busy.poll();
                free.add(new int[] { (int) done[1], (int) done[2] });
            }

            if (!free.isEmpty()) {
                // a server is free right now: the task starts at second j
                int[] server = free.poll();
                result[j] = server[1];
                busy.add(new long[] { (long) j + tasks[j], server[0], server[1] });
            } else {
                // nothing free: wait for the first server to come back, start the task then
                long[] server = busy.poll();
                result[j] = (int) server[2];
                busy.add(new long[] { server[0] + tasks[j], server[1], server[2] });
            }
        }
        return result;
    }
}
