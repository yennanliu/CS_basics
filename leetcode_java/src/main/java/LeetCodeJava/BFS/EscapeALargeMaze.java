package LeetCodeJava.BFS;

// https://leetcode.com/problems/escape-a-large-maze/description/

import java.util.*;

/**
 * 1036. Escape a Large Maze
 * Hard
 *
 * There is a 1 million by 1 million grid on an XY-plane, and the coordinates of each
 * grid square are (x, y).
 *
 * We start at the source = [sx, sy] square and want to reach the target = [tx, ty]
 * square. There is also an array of blocked squares, where each blocked[i] = [xi, yi]
 * represents a blocked square with coordinates (xi, yi).
 *
 * Each move, we can walk one square north, east, south, or west if the square is not
 * in the array of blocked squares. We are also not allowed to walk outside of the grid.
 *
 * Return true if and only if it is possible to reach the target square from the source
 * square through a sequence of valid moves.
 *
 *
 * Example 1:
 *
 * Input: blocked = [[0,1],[1,0]], source = [0,0], target = [0,2]
 * Output: false
 * Explanation: The target square is inaccessible starting from the source square
 * because we cannot move.
 * We cannot move north or east because those squares are blocked.
 * We cannot move south or west because we cannot go outside of the grid.
 *
 * Example 2:
 *
 * Input: blocked = [], source = [0,0], target = [999999,999999]
 * Output: true
 * Explanation: Because there are no blocked cells, it is possible to reach the
 * target square.
 *
 *
 * Constraints:
 *
 * 0 <= blocked.length <= 200
 * blocked[i].length == 2
 * 0 <= xi, yi < 10^6
 * source.length == target.length == 2
 * 0 <= sx, sy, tx, ty < 10^6
 * source != target
 * It is guaranteed that source and target are not blocked.
 *
 */
public class EscapeALargeMaze {

    private static final long GRID_SIZE = 1_000_000L;
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // V0
    // IDEA: BOUNDED BFS FROM BOTH ENDS
    //  The grid is 10^6 x 10^6, so a full BFS is impossible. But m blocked cells can
    //  enclose at most m * (m - 1) / 2 cells (a diagonal wall cutting off a corner).
    //  So a BFS that visits more than that many cells has escaped into open space.
    //  source reaches target iff each side either escapes or meets the other.
    /**
     * time = O(M^2)
     * space = O(M^2)
     */
    public boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {
        Set<Long> blockedSet = new HashSet<>();
        for (int[] cell : blocked) {
            blockedSet.add(encode(cell[0], cell[1]));
        }

        // largest area the blocked cells can seal off
        int blockedCount = blocked.length;
        long limit = (long) blockedCount * (blockedCount - 1) / 2;

        return canEscape(blockedSet, source, target, limit)
                && canEscape(blockedSet, target, source, limit);
    }

    /** True if a BFS from start reaches end directly, or visits more than limit cells. */
    private boolean canEscape(Set<Long> blockedSet, int[] start, int[] end, long limit) {
        long endKey = encode(end[0], end[1]);
        Set<Long> visited = new HashSet<>();
        Deque<int[]> queue = new ArrayDeque<>();
        visited.add(encode(start[0], start[1]));
        queue.offer(start);

        while (!queue.isEmpty() && visited.size() <= limit) {
            int[] cell = queue.poll();
            for (int[] dir : DIRECTIONS) {
                int nx = cell[0] + dir[0];
                int ny = cell[1] + dir[1];
                if (nx < 0 || nx >= GRID_SIZE || ny < 0 || ny >= GRID_SIZE) {
                    continue;
                }
                long key = encode(nx, ny);
                if (blockedSet.contains(key) || visited.contains(key)) {
                    continue;
                }
                if (key == endKey) {
                    return true;
                }
                visited.add(key);
                queue.offer(new int[] {nx, ny});
            }
        }
        // queue ran dry before the limit -> start is enclosed
        return visited.size() > limit;
    }

    /** Packs (x, y) into one long so it can live in a HashSet. */
    private long encode(int x, int y) {
        return (long) x * GRID_SIZE + y;
    }
}
