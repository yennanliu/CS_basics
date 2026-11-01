package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-moves-to-move-a-box-to-their-target-location/description/

import java.util.*;

/**
 * 1263. Minimum Moves to Move a Box to Their Target Location
 * Hard
 *
 * A storekeeper is a game in which the player pushes boxes around in a warehouse
 * trying to get them to target locations.
 *
 * The game is represented by an m x n grid of characters grid where each element
 * is a wall, floor, or box.
 *
 * Your task is to move the box 'B' to the target position 'T' under the following rules:
 *
 * The character 'S' represents the player. The player can move up, down, left, right
 * in grid if it is a floor (empty cell).
 * The character '.' represents the floor which means a free cell to walk.
 * The character '#' represents the wall which means an obstacle (impossible to walk there).
 * There is only one box 'B' and one target cell 'T' in the grid.
 * The box can be moved to an adjacent free cell by standing next to the box and then
 * moving in the direction of the box. This is a push.
 * The player cannot walk through the box.
 *
 * Return the minimum number of pushes to move the box to the target.
 * If there is no way to reach the target, return -1.
 *
 *
 * Example 1:
 *
 * Input: grid = [["#","#","#","#","#","#"],
 *                ["#","T","#","#","#","#"],
 *                ["#",".",".","B",".","#"],
 *                ["#",".","#","#",".","#"],
 *                ["#",".",".",".","S","#"],
 *                ["#","#","#","#","#","#"]]
 * Output: 3
 * Explanation: We return only the number of times the box is pushed.
 *
 * Example 2:
 *
 * Input: grid = [["#","#","#","#","#","#"],
 *                ["#","T","#","#","#","#"],
 *                ["#",".",".","B",".","#"],
 *                ["#","#","#","#",".","#"],
 *                ["#",".",".",".","S","#"],
 *                ["#","#","#","#","#","#"]]
 * Output: -1
 *
 * Example 3:
 *
 * Input: grid = [["#","#","#","#","#","#"],
 *                ["#","T",".",".","#","#"],
 *                ["#",".","#","B",".","#"],
 *                ["#",".",".",".",".","#"],
 *                ["#",".",".",".","S","#"],
 *                ["#","#","#","#","#","#"]]
 * Output: 5
 * Explanation: push the box down, left, left, up and up.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 20
 * grid contains only characters '.', '#', 'S', 'T', or 'B'.
 * There is only one character 'S', 'B', and 'T' in the grid.
 *
 */
public class MinimumMovesToMoveABoxToTheirTargetLocation {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: 0-1 BFS ON STATE = (box cell, player cell)
    //  - a player step that does not touch the box costs 0 (pushed to the FRONT of the deque)
    //  - a player step into the box is a push, costs 1   (pushed to the BACK of the deque)
    //  so states leave the deque in non-decreasing push count, like Dijkstra without a heap.
    /**
     * time = O((M * N)^2)
     * space = O((M * N)^2)
     */
    public int minPushBox(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int cellCount = rows * cols;

        int boxCell = -1;
        int playerCell = -1;
        int targetCell = -1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'B') {
                    boxCell = r * cols + c;
                } else if (grid[r][c] == 'S') {
                    playerCell = r * cols + c;
                } else if (grid[r][c] == 'T') {
                    targetCell = r * cols + c;
                }
            }
        }

        // state id = boxCell * cellCount + playerCell; dist = fewest pushes to reach it
        int[] pushes = new int[cellCount * cellCount];
        Arrays.fill(pushes, Integer.MAX_VALUE);
        int startState = boxCell * cellCount + playerCell;
        pushes[startState] = 0;

        Deque<Integer> deque = new ArrayDeque<>();
        deque.offer(startState);

        while (!deque.isEmpty()) {
            int state = deque.pollFirst();
            int box = state / cellCount;
            int player = state % cellCount;
            int cost = pushes[state];
            if (box == targetCell) {
                return cost;
            }

            int boxRow = box / cols;
            int boxCol = box % cols;
            int playerRow = player / cols;
            int playerCol = player % cols;

            for (int[] dir : DIRECTIONS) {
                int nextPlayerRow = playerRow + dir[0];
                int nextPlayerCol = playerCol + dir[1];
                if (!isWalkable(grid, nextPlayerRow, nextPlayerCol)) {
                    continue;
                }

                int nextState;
                int weight;
                if (nextPlayerRow == boxRow && nextPlayerCol == boxCol) {
                    // a push: the box slides one cell further in the same direction
                    int nextBoxRow = boxRow + dir[0];
                    int nextBoxCol = boxCol + dir[1];
                    if (!isWalkable(grid, nextBoxRow, nextBoxCol)) {
                        continue;
                    }
                    nextState = (nextBoxRow * cols + nextBoxCol) * cellCount + (nextPlayerRow * cols + nextPlayerCol);
                    weight = 1;
                } else {
                    // a free walk: the box stays put
                    nextState = box * cellCount + (nextPlayerRow * cols + nextPlayerCol);
                    weight = 0;
                }

                if (cost + weight < pushes[nextState]) {
                    pushes[nextState] = cost + weight;
                    if (weight == 0) {
                        deque.offerFirst(nextState);
                    } else {
                        deque.offerLast(nextState);
                    }
                }
            }
        }
        return -1;
    }

    /** True if (r, c) is inside the grid and not a wall. */
    private boolean isWalkable(char[][] grid, int r, int c) {
        return r >= 0 && r < grid.length && c >= 0 && c < grid[0].length && grid[r][c] != '#';
    }
}
