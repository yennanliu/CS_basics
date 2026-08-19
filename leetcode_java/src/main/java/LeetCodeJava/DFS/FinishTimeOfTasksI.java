package LeetCodeJava.DFS;

// https://leetcode.com/problems/finish-time-of-tasks-i/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  3965. Finish Time of Tasks I
 *  Medium
 *
 *  You are given an integer n representing the number of tasks in a project, numbered
 *  from 0 to n - 1. These tasks are connected as a tree rooted at task 0, represented by
 *  a 2D integer array edges of length n - 1, where edges[i] = [ui, vi] indicates that
 *  task ui is the parent of task vi.
 *
 *  You are also given an array baseTime of length n, where baseTime[i] is the time to
 *  complete task i.
 *
 *  The finish time of each task:
 *   - Leaf task: finish time is baseTime[i].
 *   - Non-leaf task: let earliest / latest be the min / max finish time among its
 *     children, ownDuration = (latest - earliest) + baseTime[i], and the finish time
 *     of task i is latest + ownDuration.
 *
 *  Return the finish time of the root task 0.
 *
 *  Example 1:
 *    Input: n = 3, edges = [[0,1],[1,2]], baseTime = [9,5,3]
 *    Output: 17
 *
 *  Example 2:
 *    Input: n = 3, edges = [[0,1],[0,2]], baseTime = [4,7,6]
 *    Output: 12
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    edges.length == n - 1, edges[i] == [ui, vi], 0 <= ui, vi <= n - 1, ui != vi
 *    baseTime.length == n, 1 <= baseTime[i] <= 10^5
 *    The finish time of every task is guaranteed to be less than 2^53.
 */
public class FinishTimeOfTasksI {

    // V0
    // IDEA: post-order DFS over the children lists, done ITERATIVELY
    //       (n can be 1e5 and the tree may be a chain -> recursion would overflow).
    /**
     * time = O(n)
     * space = O(n)
     */
    public long finishTime(int n, int[][] edges, int[] baseTime) {

        // children as CSR-like adjacency (head / next arrays), edges are directed u -> v
        int[] head = new int[n];
        for (int i = 0; i < n; i++) {
            head[i] = -1;
        }
        int m = edges.length;
        int[] nextEdge = new int[m];
        int[] to = new int[m];
        for (int i = 0; i < m; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            to[i] = v;
            nextEdge[i] = head[u];
            head[u] = i;
        }

        // pre-order push, then process in reverse -> children resolved before parents
        Deque<Integer> stack = new ArrayDeque<>();
        Deque<Integer> order = new ArrayDeque<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            order.push(cur);
            for (int e = head[cur]; e != -1; e = nextEdge[e]) {
                stack.push(to[e]);
            }
        }

        long[] finish = new long[n];
        while (!order.isEmpty()) {
            int cur = order.pop();

            if (head[cur] == -1) {
                // leaf
                finish[cur] = baseTime[cur];
                continue;
            }

            long earliest = Long.MAX_VALUE;
            long latest = Long.MIN_VALUE;
            for (int e = head[cur]; e != -1; e = nextEdge[e]) {
                long val = finish[to[e]];
                earliest = Math.min(earliest, val);
                latest = Math.max(latest, val);
            }

            long ownDuration = (latest - earliest) + baseTime[cur];
            finish[cur] = latest + ownDuration;
        }

        return finish[0];
    }
}
