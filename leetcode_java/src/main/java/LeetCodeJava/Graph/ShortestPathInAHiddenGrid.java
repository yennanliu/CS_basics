package LeetCodeJava.Graph;

// https://leetcode.com/problems/shortest-path-in-a-hidden-grid/description/

import java.util.*;

/**
 * 1778. Shortest Path in a Hidden Grid
 * Medium
 *
 * This is an interactive problem.
 *
 * There is a robot in a hidden grid, and you are trying to get it from its starting cell to the target cell
 * in this grid. The grid is of size m x n, and each cell in the grid is either empty or blocked. It is
 * guaranteed that the starting cell and the target cell are different, and neither of them is blocked.
 *
 * You want to find the minimum distance to the target cell. However, you do not know the grid's dimensions,
 * the starting cell, nor the target cell. You are only allowed to ask queries to the GridMaster object.
 *
 * The GridMaster class has the following functions:
 *
 * boolean canMove(char direction) Returns true if the robot can move in that direction. Otherwise, it
 * returns false.
 * void move(char direction) Moves the robot in that direction. If this move would move the robot to a
 * blocked cell or off the grid, the move will be ignored, and the robot will remain in the same position.
 * boolean isTarget() Returns true if the robot is currently on the target cell. Otherwise, it returns false.
 *
 * Note that direction in the above functions should be a character from {'U','D','L','R'}, representing
 * the directions up, down, left, and right, respectively.
 *
 * Return the minimum distance between the robot's initial starting cell and the target cell. If there is
 * no valid path between the cells, return -1.
 *
 * Custom testing:
 *
 * The test input is read as a 2D matrix grid of size m x n where:
 *
 * grid[i][j] == -1 indicates that the robot is in cell (i, j) (the starting cell).
 * grid[i][j] == 0 indicates that the cell (i, j) is blocked.
 * grid[i][j] == 1 indicates that the cell (i, j) is empty.
 * grid[i][j] == 2 indicates that the cell (i, j) is the target cell.
 *
 * There is exactly one -1 and 2 in grid. Remember that you will not have this information in your code.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,2],[-1,0]]
 * Output: 2
 *
 * Example 2:
 *
 * Input: grid = [[0,0,-1],[1,1,1],[2,0,0]]
 * Output: 4
 *
 * Example 3:
 *
 * Input: grid = [[-1,0],[0,2]]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 500
 * m == grid.length
 * n == grid[i].length
 * grid[i][j] is either -1, 0, 1, or 2.
 * There is exactly one -1 in grid.
 * There is exactly one 2 in grid.
 *
 */
public class ShortestPathInAHiddenGrid {

    /**
     * This is the GridMaster's API interface (provided by LeetCode).
     * Declared here so this file compiles standalone - do NOT implement it.
     */
    public interface GridMaster {
        boolean canMove(char direction);

        void move(char direction);

        boolean isTarget();
    }

    private static final char[] MOVES = {'U', 'D', 'L', 'R'};
    private static final char[] UNDO = {'D', 'U', 'R', 'L'};
    private static final int[] DELTA_ROW = {-1, 1, 0, 0};
    private static final int[] DELTA_COL = {0, 0, -1, 1};

    // V0
    // IDEA: DFS TO MAP THE HIDDEN GRID, THEN BFS ON THE MAP
    // the robot can only be probed locally, so first walk the whole reachable region with a
    // DFS in the robot's own coordinates (start = (0, 0)), recording every free cell and the
    // target. Each step into a new cell is undone with the opposite move when that cell is
    // finished, so the robot always stands on the cell the DFS is exploring.
    // Then a plain BFS over the mapped cells gives the shortest distance.
    // NOTE: the grid can be 500 x 500, so the DFS uses an explicit stack, not recursion.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int findShortestPath(GridMaster master) {
        // cells are keyed as "row,col" relative to the start
        Set<String> free = new HashSet<>();
        free.add(key(0, 0));
        int[] target = null;
        if (master.isTarget()) {
            target = new int[] {0, 0};
        }

        // frame = {row, col, next direction to try, index of the move that entered it (-1 = start)}
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {0, 0, 0, -1});
        while (!stack.isEmpty()) {
            int[] frame = stack.peek();
            // all four directions tried: leave this cell, walking the robot back
            if (frame[2] == 4) {
                stack.pop();
                if (frame[3] != -1) {
                    master.move(UNDO[frame[3]]);
                }
                continue;
            }
            int dir = frame[2];
            frame[2]++;

            int nextRow = frame[0] + DELTA_ROW[dir];
            int nextCol = frame[1] + DELTA_COL[dir];
            if (free.contains(key(nextRow, nextCol)) || !master.canMove(MOVES[dir])) {
                continue;
            }
            free.add(key(nextRow, nextCol));
            master.move(MOVES[dir]);
            if (master.isTarget()) {
                target = new int[] {nextRow, nextCol};
            }
            stack.push(new int[] {nextRow, nextCol, 0, dir});
        }

        if (target == null) {
            return -1;
        }

        // BFS from the start over the mapped free cells
        Set<String> seen = new HashSet<>();
        seen.add(key(0, 0));
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {0, 0, 0});
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            if (cur[0] == target[0] && cur[1] == target[1]) {
                return cur[2];
            }
            for (int dir = 0; dir < 4; dir++) {
                int nextRow = cur[0] + DELTA_ROW[dir];
                int nextCol = cur[1] + DELTA_COL[dir];
                String nextKey = key(nextRow, nextCol);
                if (free.contains(nextKey) && !seen.contains(nextKey)) {
                    seen.add(nextKey);
                    queue.offer(new int[] {nextRow, nextCol, cur[2] + 1});
                }
            }
        }
        return -1;
    }

    /** Hash key of a relative cell. */
    private String key(int row, int col) {
        return row + "," + col;
    }
}
