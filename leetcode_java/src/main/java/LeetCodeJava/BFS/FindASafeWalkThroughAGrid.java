package LeetCodeJava.BFS;

// https://leetcode.com/problems/find-a-safe-walk-through-a-grid/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * 3286. Find a Safe Walk Through a Grid
 * Medium
 *
 * You are given an m x n binary matrix grid and an integer health.
 *
 * You start on the upper-left corner (0, 0) and would like to get to the lower-right corner (m - 1, n - 1).
 *
 * You can move up, down, left, or right from one cell to another adjacent cell as long as your health remains positive.
 *
 * Cells (i, j) with grid[i][j] = 1 are considered unsafe and reduce your health by 1.
 *
 * Return true if you can reach the final cell with a health value of 1 or more, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: grid = [[0,1,0,0,0],[0,1,0,1,0],[0,0,0,1,0]], health = 1
 * Output: true
 * Explanation:
 * The final cell can be reached safely by walking along the gray cells below.
 *
 * Example 2:
 *
 * Input: grid = [[0,1,1,0,0,0],[1,0,1,0,0,0],[0,1,1,1,0,1],[0,0,1,0,1,0]], health = 3
 * Output: false
 * Explanation:
 * A minimum of 4 health points is needed to reach the final cell safely.
 *
 * Example 3:
 *
 * Input: grid = [[1,1,1],[1,0,1],[1,1,1]], health = 5
 * Output: true
 * Explanation:
 * The final cell can be reached safely by walking along the gray cells below.
 * Any path that does not go through the cell (1, 1) is unsafe since your health will drop to 0 when reaching the final cell.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 50
 * 2 <= m * n
 * 1 <= health <= m + n
 * grid[i][j] is either 0 or 1
 *
 */
public class FindASafeWalkThroughAGrid {

    // V0
    // IDEA: 0-1 BFS ON DAMAGE TAKEN, THEN COMPARE WITH health
    //   "arrive with health >= 1" == "least damage over any path < health".
    //   Entering a 1 costs 1, a 0 costs 0. With only two weights a deque keeps
    //   the frontier sorted: 0-cost moves go to the front, 1-cost to the back.
    //   The start cell's own value counts as damage too.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public boolean findSafeWalk(List<List<Integer>> grid, int health) {
        int rows = grid.size();
        int cols = grid.get(0).size();
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        // damage[r][c] = least damage taken to stand on (r, c)
        int[][] damage = new int[rows][cols];
        for (int[] row : damage) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        damage[0][0] = grid.get(0).get(0);

        Deque<int[]> deque = new ArrayDeque<>();
        deque.offerFirst(new int[] { 0, 0 });
        while (!deque.isEmpty()) {
            int[] cur = deque.pollFirst();
            int r = cur[0];
            int c = cur[1];
            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                int cellCost = grid.get(nr).get(nc);
                int newDamage = damage[r][c] + cellCost;
                if (newDamage < damage[nr][nc]) {
                    damage[nr][nc] = newDamage;
                    // a free move keeps the same distance -> front of the deque
                    if (cellCost == 0) {
                        deque.offerFirst(new int[] { nr, nc });
                    } else {
                        deque.offerLast(new int[] { nr, nc });
                    }
                }
            }
        }
        return damage[rows - 1][cols - 1] < health;
    }
}
