package LeetCodeJava.BFS;

// https://leetcode.com/problems/nearest-exit-from-entrance-in-maze/description/

import java.util.*;

/**
 * 1926. Nearest Exit from Entrance in Maze
 * Medium
 *
 * You are given an m x n matrix maze (0-indexed) with empty cells (represented as '.') and walls
 * (represented as '+'). You are also given the entrance of the maze, where
 * entrance = [entrancerow, entrancecol] denotes the row and column of the cell you are initially
 * standing at.
 *
 * In one step, you can move one cell up, down, left, or right. You cannot step into a cell with a
 * wall, and you cannot step outside the maze. Your goal is to find the nearest exit from the
 * entrance. An exit is defined as an empty cell that is at the border of the maze. The entrance
 * does not count as an exit.
 *
 * Return the number of steps in the shortest path from the entrance to the nearest exit, or -1 if
 * no such path exists.
 *
 *
 * Example 1:
 *
 * Input: maze = [["+","+",".","+"],[".",".",".","+"],["+","+","+","."]], entrance = [1,2]
 * Output: 1
 * Explanation: There are 3 exits in this maze at [1,0], [0,2], and [2,3].
 * Initially, you are at the entrance cell [1,2].
 * - You can reach [1,0] by moving 2 steps left.
 * - You can reach [0,2] by moving 1 step up.
 * It is impossible to reach [2,3] from the entrance.
 * Thus, the nearest exit is [0,2], which is 1 step away.
 *
 * Example 2:
 *
 * Input: maze = [["+","+","+"],[".",".","."],["+","+","+"]], entrance = [1,0]
 * Output: 2
 * Explanation: There is 1 exit in this maze at [1,2].
 * [1,0] does not count as an exit since it is the entrance cell.
 * Initially, you are at the entrance cell [1,0].
 * - You can reach [1,2] by moving 2 steps right.
 * Thus, the nearest exit is [1,2], which is 2 steps away.
 *
 * Example 3:
 *
 * Input: maze = [[".","+"]], entrance = [0,0]
 * Output: -1
 * Explanation: There are no exits in this maze.
 *
 *
 * Constraints:
 *
 * maze.length == m
 * maze[i].length == n
 * 1 <= m, n <= 100
 * maze[i][j] is either '.' or '+'.
 * entrance.length == 2
 * 0 <= entrancerow < m
 * 0 <= entrancecol < n
 * entrance will always be an empty cell.
 *
 */
public class NearestExitFromEntranceInMaze {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: BFS LEVEL BY LEVEL (UNWEIGHTED GRID -> THE FIRST BORDER CELL REACHED WINS)
    //  NOTE: the entrance is not an exit, so the border test runs only on cells
    //        we MOVE INTO, never on the start cell.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int nearestExit(char[][] maze, int[] entrance) {
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(entrance);
        visited[entrance[0]][entrance[1]] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            steps++;
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                int[] cell = queue.poll();
                for (int[] dir : DIRECTIONS) {
                    int nr = cell[0] + dir[0];
                    int nc = cell[1] + dir[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                        continue;
                    }
                    if (maze[nr][nc] == '+' || visited[nr][nc]) {
                        continue;
                    }
                    // an empty border cell (other than the entrance) is an exit
                    if (nr == 0 || nr == rows - 1 || nc == 0 || nc == cols - 1) {
                        return steps;
                    }
                    visited[nr][nc] = true;
                    queue.offer(new int[] {nr, nc});
                }
            }
        }
        return -1;
    }
}
