package LeetCodeJava.BFS;

// https://leetcode.com/problems/the-maze/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  490. The Maze
 *  Medium
 *
 *  There is a ball in a maze with empty spaces (represented as 0) and walls
 *  (represented as 1). The ball can go through the empty spaces by rolling up,
 *  down, left or right, but it won't stop rolling until hitting a wall. When the
 *  ball stops, it could choose the next direction.
 *
 *  Given the m x n maze, the ball's start position and the destination, where
 *  start = [startrow, startcol] and destination = [destinationrow, destinationcol],
 *  return true if the ball can stop at the destination, otherwise return false.
 *
 *  You may assume that the borders of the maze are all walls.
 *
 *
 *  Example 1:
 *
 *  Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]],
 *         start = [0,4], destination = [4,4]
 *  Output: true
 *  Explanation: One possible way is : left -> down -> left -> down -> right -> down -> right.
 *
 *  Example 2:
 *
 *  Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]],
 *         start = [0,4], destination = [3,2]
 *  Output: false
 *
 *
 *  Constraints:
 *
 *  m == maze.length, n == maze[i].length
 *  1 <= m, n <= 100
 *  maze[i][j] is 0 or 1.
 *  start.length == destination.length == 2
 *  The ball and the destination exist on an empty space, and they will not be at
 *  the same position initially.
 */
public class TheMaze {

    // V0
    // IDEA: BFS over "stop points" — from each stop, roll in all 4 directions until a wall
    /**
     * time = O(m * n * max(m, n))
     * space = O(m * n)
     */
    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return false;
        }
        int m = maze.length;
        int n = maze[0].length;
        int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        boolean[][] visited = new boolean[m][n];
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[] { start[0], start[1] });
        visited[start[0]][start[1]] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == destination[0] && cur[1] == destination[1]) {
                return true;
            }
            for (int[] d : dirs) {
                int x = cur[0];
                int y = cur[1];
                // roll until hitting a wall / the border
                while (x + d[0] >= 0 && x + d[0] < m && y + d[1] >= 0 && y + d[1] < n
                        && maze[x + d[0]][y + d[1]] == 0) {
                    x += d[0];
                    y += d[1];
                }
                if (!visited[x][y]) {
                    visited[x][y] = true;
                    q.add(new int[] { x, y });
                }
            }
        }
        return false;
    }

    // V1
    // IDEA: DFS (same "roll to a stop" transition, recursive)
    /**
     * time = O(m * n * max(m, n))
     * space = O(m * n)
     */
    public boolean hasPath_1(int[][] maze, int[] start, int[] destination) {
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return false;
        }
        boolean[][] visited = new boolean[maze.length][maze[0].length];
        return dfs(maze, start[0], start[1], destination, visited);
    }

    private boolean dfs(int[][] maze, int x, int y, int[] destination, boolean[][] visited) {
        if (visited[x][y]) {
            return false;
        }
        if (x == destination[0] && y == destination[1]) {
            return true;
        }
        visited[x][y] = true;

        int m = maze.length;
        int n = maze[0].length;
        int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] d : dirs) {
            int nx = x;
            int ny = y;
            while (nx + d[0] >= 0 && nx + d[0] < m && ny + d[1] >= 0 && ny + d[1] < n
                    && maze[nx + d[0]][ny + d[1]] == 0) {
                nx += d[0];
                ny += d[1];
            }
            if (dfs(maze, nx, ny, destination, visited)) {
                return true;
            }
        }
        return false;
    }
}
