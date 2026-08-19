package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-minimum-time-to-reach-last-room-i/

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  3341. Find Minimum Time to Reach Last Room I
 *  Medium
 *
 *  There is a dungeon with n x m rooms arranged as a grid.
 *
 *  You are given a 2D array moveTime of size n x m, where moveTime[i][j] represents
 *  the minimum time in seconds when you can start moving to that room. You start
 *  from the room (0, 0) at time t = 0 and can move to an adjacent room. Moving
 *  between adjacent rooms takes exactly one second.
 *
 *  Return the minimum time to reach the room (n - 1, m - 1).
 *  Two rooms are adjacent if they share a common wall, horizontally or vertically.
 *
 *  Example 1:
 *    Input: moveTime = [[0,4],[4,4]]
 *    Output: 6
 *    Explanation: wait until t == 4, step to (1,0) at t == 5, then to (1,1) at t == 6.
 *
 *  Example 2:
 *    Input: moveTime = [[0,0,0],[0,0,0]]
 *    Output: 3
 *
 *  Constraints:
 *    2 <= n == moveTime.length <= 50
 *    2 <= m == moveTime[i].length <= 50
 *    0 <= moveTime[i][j] <= 10^9
 */
public class FindMinimumTimeToReachLastRoomI {

    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: DIJKSTRA WHERE THE EDGE COST DEPENDS ON THE ARRIVAL TIME
    //
    //   the earliest one can ENTER a room is max(now, moveTime[room]) - waiting is
    //   free - and the move itself costs one more second:
    //
    //       arrival = max(current time, moveTime[x][y]) + 1
    //
    //   that value never decreases along a path, so the usual Dijkstra argument
    //   holds: the first time a room is popped from the priority queue its time is
    //   final.
    /**
     * time = O(n * m * log(n * m))
     * space = O(n * m)
     */
    public int minTimeToReach(int[][] moveTime) {
        int n = moveTime.length;
        int m = moveTime[0].length;

        long[][] best = new long[n][m];
        for (int i = 0; i < n; i++) {
            java.util.Arrays.fill(best[i], Long.MAX_VALUE);
        }
        best[0][0] = 0L;

        // {time, row, col}
        PriorityQueue<long[]> pq = new PriorityQueue<>(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[0], b[0]);
            }
        });
        pq.add(new long[]{0L, 0L, 0L});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long t = cur[0];
            int i = (int) cur[1];
            int j = (int) cur[2];
            if (t > best[i][j]) {
                continue;
            }
            if (i == n - 1 && j == m - 1) {
                return (int) t;
            }
            for (int[] d : DIRS) {
                int x = i + d[0];
                int y = j + d[1];
                if (x < 0 || x >= n || y < 0 || y >= m) {
                    continue;
                }
                long nt = Math.max(t, moveTime[x][y]) + 1;
                if (nt < best[x][y]) {
                    best[x][y] = nt;
                    pq.add(new long[]{nt, x, y});
                }
            }
        }
        return (int) best[n - 1][m - 1];
    }
}
