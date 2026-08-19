package LeetCodeJava.BFS;

// https://leetcode.com/problems/the-maze-ii/

import java.util.PriorityQueue;

/**
 *  505. The Maze II
 *  Medium
 *
 *  There is a ball in a maze with empty spaces (represented as 0) and walls
 *  (represented as 1). The ball can go through the empty spaces by rolling up,
 *  down, left or right, but it won't stop rolling until hitting a wall. When the
 *  ball stops, it could choose the next direction.
 *
 *  Given the m x n maze, the ball's start position and the destination, where
 *  start = [startrow, startcol] and destination = [destinationrow, destinationcol],
 *  return the shortest distance for the ball to stop at the destination. If the
 *  ball cannot stop at the destination, return -1.
 *
 *  The distance is the number of empty spaces traveled by the ball from the start
 *  position (excluded) to the destination (included).
 *
 *  You may assume that the borders of the maze are all walls.
 *
 *
 *  Example 1:
 *
 *  Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]],
 *         start = [0,4], destination = [4,4]
 *  Output: 12
 *  Explanation: left -> down -> left -> down -> right -> down -> right,
 *  total distance = 1 + 1 + 3 + 1 + 2 + 2 + 2 = 12.
 *
 *  Example 2:
 *
 *  Input: maze = [[0,0,1,0,0],[0,0,0,0,0],[0,0,0,1,0],[1,1,0,1,1],[0,0,0,0,0]],
 *         start = [0,4], destination = [3,2]
 *  Output: -1
 *
 *
 *  Constraints:
 *
 *  m == maze.length, n == maze[i].length
 *  1 <= m, n <= 100
 *  maze[i][j] is 0 or 1.
 *  start.length == destination.length == 2
 */
public class TheMazeII {

    // V0
    // IDEA: DIJKSTRA — edge weights (roll lengths) differ, so use a min-heap on distance
    /**
     * time = O(m * n * max(m, n) * log(m * n))
     * space = O(m * n)
     */
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return -1;
        }
        int m = maze.length;
        int n = maze[0].length;
        int[][] dirs = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }
        dist[start[0]][start[1]] = 0;

        // {dist, x, y}
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[0], b[0]));
        pq.add(new int[] { 0, start[0], start[1] });

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0];
            int x = cur[1];
            int y = cur[2];

            if (d > dist[x][y]) {
                continue; // stale entry
            }
            if (x == destination[0] && y == destination[1]) {
                return d;
            }

            for (int[] dir : dirs) {
                int nx = x;
                int ny = y;
                int steps = 0;
                while (nx + dir[0] >= 0 && nx + dir[0] < m && ny + dir[1] >= 0 && ny + dir[1] < n
                        && maze[nx + dir[0]][ny + dir[1]] == 0) {
                    nx += dir[0];
                    ny += dir[1];
                    steps++;
                }
                if (d + steps < dist[nx][ny]) {
                    dist[nx][ny] = d + steps;
                    pq.add(new int[] { d + steps, nx, ny });
                }
            }
        }
        return -1;
    }
}
