package LeetCodeJava.Heap;

// https://leetcode.com/problems/find-minimum-time-to-reach-last-room-ii/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  3342. Find Minimum Time to Reach Last Room II
 *  Medium
 *
 *  There is a dungeon with n x m rooms arranged as a grid.
 *
 *  You are given a 2D array moveTime of size n x m, where moveTime[i][j] represents
 *  the minimum time in seconds when you can start moving to that room. You start
 *  from the room (0, 0) at time t = 0 and can move to an adjacent room. Moving
 *  between adjacent rooms takes one second for one move and two seconds for the
 *  next, alternating between the two.
 *
 *  Return the minimum time to reach the room (n - 1, m - 1).
 *
 *  Example 1:
 *    Input: moveTime = [[0,4],[4,4]]
 *    Output: 7
 *    Explanation: at t == 4 move to (1,0) in one second, then to (1,1) in two.
 *
 *  Example 2:
 *    Input: moveTime = [[0,0,0,0],[0,0,0,0]]
 *    Output: 6
 *
 *  Constraints:
 *    2 <= n == moveTime.length <= 750
 *    2 <= m == moveTime[i].length <= 750
 *    0 <= moveTime[i][j] <= 10^9
 */
public class FindMinimumTimeToReachLastRoomII {

    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: SAME DIJKSTRA AS LC 3341, BUT THE STEP COST FOLLOWS THE BOARD PARITY
    //
    //   each move flips the parity of i + j, so after k moves the walker always
    //   stands on a cell with (i + j) % 2 == k % 2. The costs alternate
    //   1, 2, 1, 2, ... starting at 1, which means the move LEAVING a cell costs
    //
    //       1 when (i + j) is even, 2 when it is odd
    //
    //   - no extra state dimension is needed, the parity is baked into the cell.
    //   The rest is LC 3341: a room cannot be entered before its moveTime, so
    //   arrival = max(now, moveTime[x][y]) + cost.
    /**
     * time = O(n * m * log(n * m))
     * space = O(n * m)
     */
    public int minTimeToReach(int[][] moveTime) {
        int n = moveTime.length;
        int m = moveTime[0].length;

        long[][] best = new long[n][m];
        for (int i = 0; i < n; i++) {
            Arrays.fill(best[i], Long.MAX_VALUE);
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
            int cost = ((i + j) % 2 == 0) ? 1 : 2;
            for (int[] d : DIRS) {
                int x = i + d[0];
                int y = j + d[1];
                if (x < 0 || x >= n || y < 0 || y >= m) {
                    continue;
                }
                long nt = Math.max(t, moveTime[x][y]) + cost;
                if (nt < best[x][y]) {
                    best[x][y] = nt;
                    pq.add(new long[]{nt, x, y});
                }
            }
        }
        return (int) best[n - 1][m - 1];
    }
}
