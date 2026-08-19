package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-path-to-get-food/

import java.util.*;

/**
 *  1730. Shortest Path to Get Food
 *  Medium
 *
 *  You are starving and you want to eat food as quickly as possible. You want
 *  to find the shortest path to arrive at any food cell.
 *
 *  You are given an m x n character matrix, grid, of these different types of cells:
 *   '*' is your location. There is exactly one '*' cell.
 *   '#' is a food cell. There may be multiple food cells.
 *   'O' is free space, and you can travel through these cells.
 *   'X' is an obstacle, and you cannot travel through these cells.
 *
 *  You can travel to any adjacent cell north, east, south, or west of your
 *  current location if there is not an obstacle.
 *
 *  Return the length of the shortest path for you to reach any food cell.
 *  If there is no path for you to reach food, return -1.
 *
 *  Example 1:
 *   Input: grid = [["X","X","X","X","X","X"],["X","*","O","O","O","X"],
 *                  ["X","O","O","#","O","X"],["X","X","X","X","X","X"]]
 *   Output: 3
 *
 *  Example 2:
 *   Input: grid = [["X","X","X","X","X"],["X","*","X","O","X"],
 *                  ["X","O","X","#","X"],["X","X","X","X","X"]]
 *   Output: -1
 *
 *  Constraints:
 *   m == grid.length, n == grid[i].length
 *   1 <= m, n <= 200
 *   grid[row][col] is '*', 'X', 'O', or '#'.
 *   The grid contains exactly one '*'.
 */
public class ShortestPathToGetFood {

    // V0
    // IDEA: plain BFS from the '*' cell, level by level, first '#' reached wins
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    private final int[][] DIRS = new int[][]{ {1, 0}, {-1, 0}, {0, 1}, {0, -1} };

    public int getFood(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }
        int m = grid.length;
        int n = grid[0].length;

        int startX = -1;
        int startY = -1;
        for (int i = 0; i < m && startX == -1; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '*') {
                    startX = i;
                    startY = j;
                    break;
                }
            }
        }
        if (startX == -1) {
            return -1;
        }

        boolean[][] visited = new boolean[m][n];
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int step = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            step += 1;
            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                for (int[] d : DIRS) {
                    int nx = cur[0] + d[0];
                    int ny = cur[1] + d[1];
                    if (nx < 0 || nx >= m || ny < 0 || ny >= n) {
                        continue;
                    }
                    if (visited[nx][ny] || grid[nx][ny] == 'X') {
                        continue;
                    }
                    if (grid[nx][ny] == '#') {
                        return step;
                    }
                    visited[nx][ny] = true;
                    q.add(new int[]{nx, ny});
                }
            }
        }
        return -1;
    }
}
