package LeetCodeJava.Graph;

// https://leetcode.com/problems/last-day-where-you-can-still-cross/description/

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 1970. Last Day Where You Can Still Cross
 * Hard
 *
 * There is a 1-based binary matrix where 0 represents land and 1 represents water. You are given
 * integers row and col representing the number of rows and columns in the matrix, respectively.
 *
 * Initially on day 0, the entire matrix is land. However, each day a new cell becomes flooded with
 * water. You are given a 1-based 2D array cells, where cells[i] = [ri, ci] represents that on the
 * ith day, the cell on the rith row and cith column (1-based coordinates) will be covered with water
 * (i.e., changed to 1).
 *
 * You want to find the last day that it is possible to walk from the top to the bottom by only
 * walking on land cells. You can start from any cell in the top row and end at any cell in the
 * bottom row. You can only travel in the four cardinal directions (left, right, up, and down).
 *
 * Return the last day where it is possible to walk from the top to the bottom by only walking on
 * land cells.
 *
 *
 * Example 1:
 *
 * Input: row = 2, col = 2, cells = [[1,1],[2,1],[1,2],[2,2]]
 * Output: 2
 *
 * Example 2:
 *
 * Input: row = 2, col = 2, cells = [[1,1],[1,2],[2,1],[2,2]]
 * Output: 1
 *
 * Example 3:
 *
 * Input: row = 3, col = 3, cells = [[1,2],[2,1],[3,3],[2,2],[1,1],[1,3],[2,3],[3,2],[3,1]]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 2 <= row, col <= 2 * 10^4
 * 4 <= row * col <= 2 * 10^4
 * cells.length == row * col
 * 1 <= ri <= row
 * 1 <= ci <= col
 * All the values of cells are unique.
 *
 */
public class LastDayWhereYouCanStillCross {

    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // V0
    // IDEA: REVERSE TIME + UNION FIND
    // forward in time land only disappears, and union-find cannot delete edges. So replay backwards:
    // start fully flooded and turn cells[i] back into land for i = n-1, n-2, ... Two virtual nodes,
    // TOP (joined to every top-row land cell) and BOTTOM (every bottom-row land cell). The first i at
    // which TOP and BOTTOM connect is the answer: the grid then equals the state after day i.
    /**
     * time = O(R * C * alpha(R * C))
     * space = O(R * C)
     */
    public int latestDayToCross(int row, int col, int[][] cells) {
        int cellCount = row * col;
        int top = cellCount;
        int bottom = cellCount + 1;
        int[] parent = new int[cellCount + 2];
        int[] size = new int[cellCount + 2];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        boolean[][] isLand = new boolean[row][col];
        for (int day = cells.length - 1; day >= 0; day--) {
            int r = cells[day][0] - 1;
            int c = cells[day][1] - 1;
            isLand[r][c] = true;
            int id = r * col + c;

            // connect to the land neighbours restored so far
            for (int[] direction : DIRECTIONS) {
                int nextR = r + direction[0];
                int nextC = c + direction[1];
                if (nextR >= 0 && nextR < row && nextC >= 0 && nextC < col && isLand[nextR][nextC]) {
                    union(parent, size, id, nextR * col + nextC);
                }
            }
            if (r == 0) {
                union(parent, size, id, top);
            }
            if (r == row - 1) {
                union(parent, size, id, bottom);
            }

            if (find(parent, top) == find(parent, bottom)) {
                return day;
            }
        }
        return 0;
    }

    /** Root of x, compressing the path iteratively (the grid can hold 2 * 10^4 cells). */
    private int find(int[] parent, int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    /** Union by size. */
    private void union(int[] parent, int[] size, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA == rootB) {
            return;
        }
        if (size[rootA] < size[rootB]) {
            int swap = rootA;
            rootA = rootB;
            rootB = swap;
        }
        parent[rootB] = rootA;
        size[rootA] += size[rootB];
    }

    // V1
    // IDEA: BINARY SEARCH THE DAY + BFS
    // crossing is monotone: possible on day d implies possible on every earlier day. Binary search
    // the largest d for which a BFS from the top row reaches the bottom row once cells[0 .. d-1] are
    // flooded.
    /**
     * time = O(R * C * log(R * C))
     * space = O(R * C)
     */
    public int latestDayToCross_1(int row, int col, int[][] cells) {
        int low = 1;
        int high = cells.length;
        int lastDay = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (canCross(row, col, cells, mid)) {
                lastDay = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return lastDay;
    }

    /** BFS from every top-row land cell after the first `day` cells have flooded. */
    private boolean canCross(int row, int col, int[][] cells, int day) {
        boolean[][] blocked = new boolean[row][col];
        for (int i = 0; i < day; i++) {
            blocked[cells[i][0] - 1][cells[i][1] - 1] = true;
        }

        Queue<int[]> queue = new ArrayDeque<>();
        for (int c = 0; c < col; c++) {
            if (!blocked[0][c]) {
                queue.offer(new int[]{0, c});
                // reuse `blocked` as the visited marker
                blocked[0][c] = true;
            }
        }
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            if (cell[0] == row - 1) {
                return true;
            }
            for (int[] direction : DIRECTIONS) {
                int nextR = cell[0] + direction[0];
                int nextC = cell[1] + direction[1];
                if (nextR >= 0 && nextR < row && nextC >= 0 && nextC < col && !blocked[nextR][nextC]) {
                    blocked[nextR][nextC] = true;
                    queue.offer(new int[]{nextR, nextC});
                }
            }
        }
        return false;
    }
}
