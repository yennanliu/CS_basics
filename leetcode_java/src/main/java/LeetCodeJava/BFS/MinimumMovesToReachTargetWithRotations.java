package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-moves-to-reach-target-with-rotations/description/

import java.util.*;

/**
 * 1210. Minimum Moves to Reach Target with Rotations
 * Hard
 *
 * In an n*n grid, there is a snake that spans 2 cells and starts moving from the top left corner
 * at (0, 0) and (0, 1). The grid has empty cells represented by zeros and blocked cells represented
 * by ones. The snake wants to reach the lower right corner at (n-1, n-2) and (n-1, n-1).
 *
 * In one move the snake can:
 *
 * Move one cell to the right if there are no blocked cells there.
 * This move keeps the horizontal/vertical position of the snake as it is.
 *
 * Move down one cell if there are no blocked cells there.
 * This move keeps the horizontal/vertical position of the snake as it is.
 *
 * Rotate clockwise if it's in a horizontal position and the two cells under it are both empty.
 * In that case the snake moves from (r, c) and (r, c+1) to (r, c) and (r+1, c).
 *
 * Rotate counterclockwise if it's in a vertical position and the two cells to its right are both empty.
 * In that case the snake moves from (r, c) and (r+1, c) to (r, c) and (r, c+1).
 *
 * Return the minimum number of moves to reach the target.
 *
 * If there is no way to reach the target, return -1.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,0,0,0,0,1],
 *                [1,1,0,0,1,0],
 *                [0,0,0,0,1,1],
 *                [0,0,1,0,1,0],
 *                [0,1,1,0,0,0],
 *                [0,1,1,0,0,0]]
 * Output: 11
 * Explanation:
 * One possible solution is [right, right, rotate clockwise, right, down, down, down, down,
 * rotate counterclockwise, right, down].
 *
 * Example 2:
 *
 * Input: grid = [[0,0,1,1,1,1],
 *                [0,0,0,0,1,1],
 *                [1,1,0,0,0,1],
 *                [1,1,1,0,0,1],
 *                [1,1,1,0,0,1],
 *                [1,1,1,0,0,0]]
 * Output: 9
 *
 *
 * Constraints:
 *
 * 2 <= n <= 100
 * 0 <= grid[i][j] <= 1
 * It is guaranteed that the snake starts at empty cells.
 *
 */
public class MinimumMovesToReachTargetWithRotations {

    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;

    // V0
    // IDEA: BFS ON STATE = (row, col, orientation)
    //  (r, c) is the snake's tail; horizontal body = (r, c),(r, c+1),
    //  vertical body = (r, c),(r+1, c). Every move costs 1, so plain BFS gives the minimum.
    //    horizontal: right -> (r, c+1, H) needs (r, c+2)
    //                down  -> (r+1, c, H) needs (r+1, c), (r+1, c+1)
    //                CW    -> (r, c, V)   needs (r+1, c), (r+1, c+1)
    //    vertical  : down  -> (r+1, c, V) needs (r+2, c)
    //                right -> (r, c+1, V) needs (r, c+1), (r+1, c+1)
    //                CCW   -> (r, c, H)   needs (r, c+1), (r+1, c+1)
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int minimumMoves(int[][] grid) {
        int n = grid.length;
        boolean[][][] visited = new boolean[n][n][2];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {0, 0, HORIZONTAL});
        visited[0][0][HORIZONTAL] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int k = 0; k < levelSize; k++) {
                int[] state = queue.poll();
                int r = state[0];
                int c = state[1];
                int orientation = state[2];

                // goal: horizontal, occupying (n-1, n-2) and (n-1, n-1)
                if (r == n - 1 && c == n - 2 && orientation == HORIZONTAL) {
                    return steps;
                }

                List<int[]> nextStates = new ArrayList<>();
                if (orientation == HORIZONTAL) {
                    if (isFree(grid, r, c + 2)) {
                        nextStates.add(new int[] {r, c + 1, HORIZONTAL});
                    }
                    // the two cells below the body enable both "down" and "rotate clockwise"
                    if (isFree(grid, r + 1, c) && isFree(grid, r + 1, c + 1)) {
                        nextStates.add(new int[] {r + 1, c, HORIZONTAL});
                        nextStates.add(new int[] {r, c, VERTICAL});
                    }
                } else {
                    if (isFree(grid, r + 2, c)) {
                        nextStates.add(new int[] {r + 1, c, VERTICAL});
                    }
                    // the two cells right of the body enable both "right" and "rotate counterclockwise"
                    if (isFree(grid, r, c + 1) && isFree(grid, r + 1, c + 1)) {
                        nextStates.add(new int[] {r, c + 1, VERTICAL});
                        nextStates.add(new int[] {r, c, HORIZONTAL});
                    }
                }

                for (int[] next : nextStates) {
                    if (!visited[next[0]][next[1]][next[2]]) {
                        visited[next[0]][next[1]][next[2]] = true;
                        queue.offer(next);
                    }
                }
            }
            steps++;
        }
        return -1;
    }

    /** True if (r, c) is inside the grid and empty. */
    private boolean isFree(int[][] grid, int r, int c) {
        int n = grid.length;
        return r >= 0 && r < n && c >= 0 && c < n && grid[r][c] == 0;
    }
}
