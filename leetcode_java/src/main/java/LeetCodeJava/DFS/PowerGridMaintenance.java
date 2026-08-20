package LeetCodeJava.DFS;

// https://leetcode.com/problems/power-grid-maintenance/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *  3607. Power Grid Maintenance
 *  Medium
 *
 *  You are given an integer c representing c power stations, each with a unique
 *  identifier id from 1 to c (1-based indexing).
 *
 *  These stations are interconnected via n bidirectional cables, represented by
 *  a 2D array connections, where connections[i] = [ui, vi] indicates a
 *  connection between station ui and station vi. Stations that are directly or
 *  indirectly connected form a power grid. Initially, all stations are online.
 *
 *  You are also given a 2D array queries, where each query is one of:
 *    [1, x]: A maintenance check is requested for station x. If station x is
 *            online, it resolves the check by itself. If station x is offline,
 *            the check is resolved by the operational station with the smallest
 *            id in the same power grid as x. If no operational station exists in
 *            that grid, return -1.
 *    [2, x]: Station x goes offline.
 *
 *  Return an array of integers representing the results of each query of type
 *  [1, x] in the order they appear.
 *
 *  Note: The power grid preserves its structure; an offline node remains part of
 *  its grid and taking it offline does not alter connectivity.
 *
 *  Example 1:
 *    Input: c = 5, connections = [[1,2],[2,3],[3,4],[4,5]],
 *           queries = [[1,3],[2,1],[1,1],[2,2],[1,2]]
 *    Output: [3,2,3]
 *
 *  Example 2:
 *    Input: c = 3, connections = [], queries = [[1,1],[2,1],[1,1]]
 *    Output: [1,-1]
 *
 *  Constraints:
 *    1 <= c <= 10^5
 *    0 <= n == connections.length <= min(10^5, c * (c - 1) / 2)
 *    connections[i].length == 2
 *    1 <= ui, vi <= c
 *    ui != vi
 *    1 <= queries.length <= 2 * 10^5
 *    queries[i].length == 2
 *    queries[i][0] is either 1 or 2.
 *    1 <= queries[i][1] <= c
 */
public class PowerGridMaintenance {

    private int[] parent;

    // V0
    // IDEA: UNION-FIND + PER-GRID MIN-HEAP WITH LAZY DELETION
    //       the note in the statement is the whole trick: going offline never
    //       cuts a cable, so the partition into grids is FIXED for the entire
    //       run -> one union-find pass up front settles every grid.
    //       give each grid a min-heap of its station ids. a type-1 query on an
    //       offline station pops stale (already offline) tops until the heap's
    //       top is an online station, which is then the answer; an empty heap
    //       means the whole grid is dark -> -1.
    //       every id is popped at most once overall, so the lazy deletion is
    //       amortised O(log c) per query.
    /**
     * time = O((C + N) * alpha(C) + (C + Q) log C)
     * space = O(C)
     */
    public int[] processQueries(int c, int[][] connections, int[][] queries) {
        parent = new int[c + 1];
        for (int i = 0; i <= c; i++) {
            parent[i] = i;
        }
        for (int[] e : connections) {
            int ra = find(e[0]), rb = find(e[1]);
            if (ra != rb) {
                parent[ra] = rb;
            }
        }

        // root -> min heap of the station ids in that grid
        Map<Integer, PriorityQueue<Integer>> heaps = new HashMap<>();
        for (int i = 1; i <= c; i++) {
            int r = find(i);
            PriorityQueue<Integer> pq = heaps.get(r);
            if (pq == null) {
                pq = new PriorityQueue<>();
                heaps.put(r, pq);
            }
            pq.add(i);
        }

        boolean[] online = new boolean[c + 1];
        for (int i = 1; i <= c; i++) {
            online[i] = true;
        }

        List<Integer> out = new ArrayList<>();
        for (int[] q : queries) {
            int type = q[0], x = q[1];
            if (type == 2) {
                online[x] = false;
                continue;
            }
            if (online[x]) {
                out.add(x);
                continue;
            }
            PriorityQueue<Integer> pq = heaps.get(find(x));
            while (!pq.isEmpty() && !online[pq.peek()]) {
                pq.poll();
            }
            out.add(pq.isEmpty() ? -1 : pq.peek());
        }

        int[] res = new int[out.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = out.get(i);
        }
        return res;
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }
}
