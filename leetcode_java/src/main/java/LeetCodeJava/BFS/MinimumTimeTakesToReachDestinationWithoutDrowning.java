package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-time-takes-to-reach-destination-without-drowning/description/
// https://leetcode.ca/2023-08-14-2814-Minimum-Time-Takes-to-Reach-Destination-Without-Drowning/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 2814. Minimum Time Takes to Reach Destination Without Drowning
 * Hard
 *
 * You are given an n * m 0-indexed grid of string land. Right now, you are standing at the cell that contains "S", and you want to get to the cell containing "D". There are three other types of cells in this land:
 *
 * ".": These cells are empty.
 * "X": These cells are stone.
 * "*": These cells are flooded.
 *
 * At each second, you can move to a cell that shares a side with your current cell (if it exists). Also, at each second, every empty cell that shares a side with a flooded cell becomes flooded as well.
 *
 * There are two problems ahead of your journey:
 *
 * You can't step on stone cells.
 * You can't step on flooded cells since you will drown (also, you can't step on a cell that will be flooded at the same time as you step on it).
 *
 * Return the minimum time it takes you to reach the destination in seconds, or -1 if it is impossible.
 *
 * Note that the destination will never be flooded.
 *
 *
 * Example 1:
 *
 * Input: land = [["D",".","*"],[".",".","."],[".","S","."]]
 * Output: 3
 * Explanation: The picture below shows the simulation of the land second by second. The blue cells are flooded, and the gray cells are stone.
 * Picture (0) shows the initial state and picture (3) shows the final state when we reach destination. As you see, it takes us 3 second to reach destination and the answer would be 3.
 * It can be shown that 3 is the minimum time needed to reach from S to D.
 *
 * Example 2:
 *
 * Input: land = [["D","X","*"],[".",".","."],[".",".","S"]]
 * Output: -1
 * Explanation: The picture below shows the simulation of the land second by second. The blue cells are flooded, and the gray cells are stone.
 * Picture (0) shows the initial state. As you see, no matter which paths we choose, we will drown at the 3rd second. Also the minimum path takes us 4 seconds to reach from S to D.
 * So the answer would be -1.
 *
 * Example 3:
 *
 * Input: land = [["D",".",".",".","*","."],[".","X",".","X",".","."],[".",".",".",".","S","."]]
 * Output: 6
 * Explanation: It can be shown that we can reach destination in 6 seconds. Also it can be shown that 6 is the minimum seconds one need to reach from S to D.
 *
 *
 * Constraints:
 *
 * 2 <= n, m <= 100
 * land consists only of "S", "D", ".", "*" and "X".
 * Exactly one of the cells is equal to "S".
 * Exactly one of the cells is equal to "D".
 *
 */
public class MinimumTimeTakesToReachDestinationWithoutDrowning {

    private static final int[][] DIRECTIONS = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    // V0
    // IDEA: TWO BFS -- FLOOD SPREAD FIRST, THEN THE WALKER
    //   BFS 1: multi-source BFS from every "*" gives floodTime[r][c], the second
    //          a cell floods (INF if never). Stone "X" and the destination "D"
    //          never flood.
    //   BFS 2: shortest-path BFS from "S". Entering (x, y) at second t + 1 is
    //          legal only if it is not stone and floodTime[x][y] > t + 1 (STRICT:
    //          stepping onto a cell as it floods drowns you). "D" is exempt.
    /**
     * time = O(N * M)
     * space = O(N * M)
     */
    public int minimumSeconds(List<List<String>> land) {
        int rows = land.size();
        int cols = land.get(0).size();
        int[][] floodTime = new int[rows][cols];
        Queue<int[]> queue = new ArrayDeque<>();
        int[] start = null;
        int[] destination = null;

        // BFS 1: every flooded cell is a source at second 0
        for (int r = 0; r < rows; r++) {
            Arrays.fill(floodTime[r], Integer.MAX_VALUE);
            for (int c = 0; c < cols; c++) {
                String cell = land.get(r).get(c);
                if (cell.equals("*")) {
                    floodTime[r][c] = 0;
                    queue.offer(new int[] { r, c });
                } else if (cell.equals("S")) {
                    start = new int[] { r, c };
                } else if (cell.equals("D")) {
                    destination = new int[] { r, c };
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] dir : DIRECTIONS) {
                int nr = cur[0] + dir[0];
                int nc = cur[1] + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (floodTime[nr][nc] != Integer.MAX_VALUE) {
                    continue;
                }
                String cell = land.get(nr).get(nc);
                // water cannot enter stone, and the destination never floods
                if (cell.equals("X") || cell.equals("D")) {
                    continue;
                }
                floodTime[nr][nc] = floodTime[cur[0]][cur[1]] + 1;
                queue.offer(new int[] { nr, nc });
            }
        }

        // BFS 2: the walker; queue entries are {row, col, second}
        boolean[][] visited = new boolean[rows][cols];
        visited[start[0]][start[1]] = true;
        queue.offer(new int[] { start[0], start[1], 0 });
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            if (cur[0] == destination[0] && cur[1] == destination[1]) {
                return cur[2];
            }
            int arrival = cur[2] + 1;
            for (int[] dir : DIRECTIONS) {
                int nr = cur[0] + dir[0];
                int nc = cur[1] + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || visited[nr][nc]) {
                    continue;
                }
                String cell = land.get(nr).get(nc);
                if (cell.equals("X")) {
                    continue;
                }
                // must arrive strictly before the water does
                if (!cell.equals("D") && floodTime[nr][nc] <= arrival) {
                    continue;
                }
                visited[nr][nc] = true;
                queue.offer(new int[] { nr, nc, arrival });
            }
        }
        return -1;
    }
}
