package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-jumps-to-reach-home/description/

import java.util.*;

/**
 * 1654. Minimum Jumps to Reach Home
 * Medium
 *
 * A certain bug's home is on the x-axis at position x. Help them get there from position 0.
 *
 * The bug jumps according to the following rules:
 *
 * - It can jump exactly a positions forward (to the right).
 * - It can jump exactly b positions backward (to the left).
 * - It cannot jump backward twice in a row.
 * - It cannot jump to any forbidden positions.
 *
 * The bug may jump forward beyond its home, but it cannot jump to positions numbered with
 * negative integers.
 *
 * Given an array of integers forbidden, where forbidden[i] means that the bug cannot jump to
 * the position forbidden[i], and integers a, b, and x, return the minimum number of jumps
 * needed for the bug to reach its home. If there is no possible sequence of jumps that lands
 * the bug on position x, return -1.
 *
 *
 * Example 1:
 *
 * Input: forbidden = [14,4,18,1,15], a = 3, b = 15, x = 9
 * Output: 3
 * Explanation: 3 jumps forward (0 -> 3 -> 6 -> 9) will get the bug home.
 *
 * Example 2:
 *
 * Input: forbidden = [8,3,16,6,12,20], a = 15, b = 13, x = 11
 * Output: -1
 *
 * Example 3:
 *
 * Input: forbidden = [1,6,2,14,5,17,4], a = 16, b = 9, x = 7
 * Output: 2
 * Explanation: One jump forward (0 -> 16) then one jump backward (16 -> 7) will get the bug home.
 *
 *
 * Constraints:
 *
 * 1 <= forbidden.length <= 1000
 * 1 <= a, b, forbidden[i] <= 2000
 * 0 <= x <= 2000
 * All the elements in forbidden are distinct.
 * Position x is not forbidden.
 *
 */
public class MinimumJumpsToReachHome {

    private static final int FORWARD = 0;
    private static final int BACKWARD = 1;

    // V0
    // IDEA: BFS OVER STATE = (position, was the last jump backward?)
    //  position alone is not a state: a backward jump is allowed only if the last
    //  jump was forward. The search is bounded by max(x, max(forbidden)) + a + b --
    //  wandering further right can never pay off with a single backward step.
    /**
     * time = O(L), L = max(x, max(forbidden)) + a + b
     * space = O(L)
     */
    public int minimumJumps(int[] forbidden, int a, int b, int x) {
        int limit = x;
        for (int position : forbidden) {
            limit = Math.max(limit, position);
        }
        limit += a + b;

        boolean[] isForbidden = new boolean[limit + 1];
        for (int position : forbidden) {
            isForbidden[position] = true;
        }

        // visited[position][how we arrived there]
        boolean[][] visited = new boolean[limit + 1][2];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {0, FORWARD});
        visited[0][FORWARD] = true;

        int jumps = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int k = 0; k < levelSize; k++) {
                int[] state = queue.poll();
                int position = state[0];
                int lastJump = state[1];
                if (position == x) {
                    return jumps;
                }

                // forward is always allowed
                tryMove(position + a, FORWARD, limit, isForbidden, visited, queue);
                // backward only if the previous jump was not backward
                if (lastJump == FORWARD) {
                    tryMove(position - b, BACKWARD, limit, isForbidden, visited, queue);
                }
            }
            jumps++;
        }
        return -1;
    }

    /** Enqueues (position, jumpType) if it is in bounds, allowed, and unseen. */
    private void tryMove(int position, int jumpType, int limit, boolean[] isForbidden,
                         boolean[][] visited, Deque<int[]> queue) {
        if (position < 0 || position > limit) {
            return;
        }
        if (isForbidden[position] || visited[position][jumpType]) {
            return;
        }
        visited[position][jumpType] = true;
        queue.offer(new int[] {position, jumpType});
    }
}
