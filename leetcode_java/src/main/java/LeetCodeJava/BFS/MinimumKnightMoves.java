package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-knight-moves/description/
// https://leetcode.ca/all/1197.html

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 1197. Minimum Knight Moves
 * In an infinite chess board with coordinates from -infinity to +infinity, you have a knight at square [0, 0].
 * <p>
 * A knight has 8 possible moves it can make, as illustrated below. Each move is two squares in a cardinal direction, then one square in an orthogonal direction.
 * <p>
 * <p>
 * <p>
 * Return the minimum number of steps needed to move the knight to the square [x, y].  It is guaranteed the answer exists.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: x = 2, y = 1
 * Output: 1
 * Explanation: [0, 0] → [2, 1]
 * Example 2:
 * <p>
 * Input: x = 5, y = 5
 * Output: 4
 * Explanation: [0, 0] → [2, 1] → [4, 2] → [3, 4] → [5, 5]
 * <p>
 * <p>
 * Constraints:
 * <p>
 * |x| + |y| <= 300
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Oracle
 * Problem Solution
 * 1197-Minimum-Knight-Moves
 */
public class MinimumKnightMoves {

    // V0
    // IDEA : BFS + `SYMMETRY FOLDING` + a BOUNDED search area
    /**
     *  the board is INFINITE, so a plain BFS over all 8 moves never terminates
     *  (the queue grows forever). 2 things make it finite:
     *
     *  1) SYMMETRY : the knight graph is symmetric on both axes,
     *     so dist((0,0) -> (x,y)) == dist((0,0) -> (|x|,|y|))
     *     -> we only ever search the 1st quadrant
     *
     *  2) BOUND : an optimal path never needs to wander further than 2 cells
     *     past the target, nor more than 2 cells `behind` the origin
     *     (the only reason to go negative at all is the (1,1) / (0,1) style
     *      corner cases, e.g. (0,0) -> (2,-1) -> (1,1))
     *     -> so we clamp the search to  x, y in [-2, target + 2]
     *
     *  then it is a normal level-by-level BFS : the level at which we pop the
     *  target IS the min number of moves
     *
     * time = O(|x| * |y|)
     * space = O(|x| * |y|)
     */
    public int minKnightMoves(int x, int y) {

        /** NOTE !!! fold to the 1st quadrant (symmetry) */
        x = Math.abs(x);
        y = Math.abs(y);

        // edge
        if (x == 0 && y == 0) {
            return 0;
        }

        int[][] moves = new int[][]{
                {1, 2}, {2, 1}, {2, -1}, {1, -2},
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
        };

        /** NOTE !!!
         *
         *  OFFSET = 2 does 2 jobs:
         *   - it is how far `behind` the origin we allow the knight to step
         *   - it shifts a coordinate into a NON negative array idx
         *     (idx = coord + OFFSET)
         */
        final int OFFSET = 2;
        boolean[][] visited = new boolean[x + 2 * OFFSET + 1][y + 2 * OFFSET + 1];

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{0, 0});
        visited[OFFSET][OFFSET] = true;

        int step = 0;
        while (!q.isEmpty()) {
            // NOTE !!! we consume the WHOLE level, then step += 1
            for (int sz = q.size(); sz > 0; sz--) {
                int[] cur = q.poll();
                if (cur[0] == x && cur[1] == y) {
                    return step;
                }
                for (int[] mv : moves) {
                    int nx = cur[0] + mv[0];
                    int ny = cur[1] + mv[1];
                    // outside of the bounded search area -> skip
                    if (nx < -OFFSET || ny < -OFFSET || nx > x + OFFSET || ny > y + OFFSET) {
                        continue;
                    }
                    if (visited[nx + OFFSET][ny + OFFSET]) {
                        continue;
                    }
                    visited[nx + OFFSET][ny + OFFSET] = true;
                    q.offer(new int[]{nx, ny});
                }
            }
            step++;
        }

        return -1; // NOT reachable (the problem guarantees an answer exists)
    }

    // V1
    // IDEA : BFS
    // https://leetcode.ca/2019-03-11-1197-Minimum-Knight-Moves/
    /**
     * time = O(x * y)
     * space = O(x * y)
     */
    public int minKnightMoves_1(int x, int y) {
        x += 310;
        y += 310;
        int ans = 0;
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{310, 310});
        // NOTE !!! use vis (boolean 2D array) to AVOID visit same coordination again
        boolean[][] vis = new boolean[700][700];
        vis[310][310] = true;
        int[][] dirs = {{-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}};
        while (!q.isEmpty()) {
            for (int k = q.size(); k > 0; --k) {
                int[] p = q.poll();
                if (p[0] == x && p[1] == y) {
                    return ans;
                }
                for (int[] dir : dirs) {
                    int c = p[0] + dir[0];
                    int d = p[1] + dir[1];
                    if (!vis[c][d]) {
                        vis[c][d] = true;
                        q.offer(new int[]{c, d});
                    }
                }
            }
            ++ans;
        }
        return -1;
    }

    // V2
    // IDEA : BFD
    // https://www.cnblogs.com/cnoodle/p/12820573.html
    /**
     * time = O(x * y)
     * space = O(x * y)
     */
    public int minKnightMoves_2(int x, int y) {
        int[][] dirs = new int[][]{{-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {2, -1}, {2, 1}};
        x = Math.abs(x);
        y = Math.abs(y);
        HashSet<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited.add("0,0");

        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                int[] cur = queue.poll();
                if (cur[0] == x && cur[1] == y) {
                    return step;
                }

                for (int[] dir : dirs) {
                    int i = cur[0] + dir[0];
                    int j = cur[1] + dir[1];
                    // (0, 0) -> (2, -1) -> (1, 1)
                    // +2的意思是多给两个格子的空间以便于骑士跳出去再跳回来的操作
                    if (!visited.contains(i + "," + j) && i >= -1 && j >= -1 && i <= x + 2 && j <= y + 2) {
                        queue.offer(new int[]{i, j});
                        visited.add(i + "," + j);
                    }
                }
            }
            step++;
        }
        return -1;
    }

}
