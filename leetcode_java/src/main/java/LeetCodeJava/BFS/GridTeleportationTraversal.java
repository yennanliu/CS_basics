package LeetCodeJava.BFS;

// https://leetcode.com/problems/grid-teleportation-traversal/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * 3552. Grid Teleportation Traversal
 * Medium
 *
 * You are given a 2D character grid matrix of size m x n, represented as an array
 * of strings, where matrix[i][j] represents the cell at the intersection of the
 * i^th row and j^th column. Each cell is one of the following:
 *
 * '.' representing an empty cell.
 *
 * '#' representing an obstacle.
 *
 * An uppercase letter ('A'-'Z') representing a teleportation portal.
 *
 * You start at the top-left cell (0, 0), and your goal is to reach the
 * bottom-right cell (m - 1, n - 1). You can move from the current cell to any
 * adjacent cell (up, down, left, right) as long as the destination cell is within
 * the grid bounds and is not an obstacle.
 *
 * If you step on a cell containing a portal letter and you haven't used that
 * portal letter before, you may instantly teleport to any other cell in the grid
 * with the same letter. This teleportation does not count as a move, but each
 * portal letter can be used at most once during your journey.
 *
 * Return the minimum number of moves required to reach the bottom-right cell. If
 * it is not possible to reach the destination, return -1.
 *
 * Example 1:
 *
 * Input: matrix = ["A..",".A.","..."]
 *
 * Output: 2
 *
 * Explanation:
 *
 * Before the first move, teleport from (0, 0) to (1, 1).
 *
 * In the first move, move from (1, 1) to (1, 2).
 *
 * In the second move, move from (1, 2) to (2, 2).
 *
 * Example 2:
 *
 * Input: matrix = [".#...",".#.#.",".#.#.","...#."]
 *
 * Output: 13
 *
 * Explanation:
 *
 * Constraints:
 *
 * 1 <= m == matrix.length <= 10^3
 *
 * 1 <= n == matrix[i].length <= 10^3
 *
 * matrix[i][j] is either '#', '.', or an uppercase English letter.
 *
 * matrix[0][0] is not an obstacle.
 *
 */
public class GridTeleportationTraversal {

    // V0
    // IDEA: 0-1 BFS -- A TELEPORT IS A ZERO-COST EDGE
    //   walking costs 1 and teleporting costs 0, so a deque (0-cost moves to the
    //   front, 1-cost moves to the back) pops cells in distance order.
    //   "each portal letter at most once": the first time a cell with letter c
    //   is popped, release every other c cell at the same distance, then mark
    //   the letter used. Popping in distance order means that first use is
    //   already the cheapest one.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int minMoves(String[] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length();
        if (matrix[rows - 1].charAt(cols - 1) == '#') {
            return -1;
        }

        // portals.get(letter) = every cell holding that letter
        List<List<int[]>> portals = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            portals.add(new ArrayList<>());
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = matrix[r].charAt(c);
                if (ch >= 'A' && ch <= 'Z') {
                    portals.get(ch - 'A').add(new int[] { r, c });
                }
            }
        }
        boolean[] letterUsed = new boolean[26];

        int[][] distance = new int[rows][cols];
        for (int[] row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        distance[0][0] = 0;
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offerFirst(new int[] { 0, 0 });
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!deque.isEmpty()) {
            int[] cur = deque.pollFirst();
            int r = cur[0];
            int c = cur[1];
            int dist = distance[r][c];
            if (r == rows - 1 && c == cols - 1) {
                return dist;
            }

            // teleport: every cell with the same letter, at zero cost, once per letter
            char ch = matrix[r].charAt(c);
            if (ch >= 'A' && ch <= 'Z' && !letterUsed[ch - 'A']) {
                letterUsed[ch - 'A'] = true;
                for (int[] target : portals.get(ch - 'A')) {
                    if (dist < distance[target[0]][target[1]]) {
                        distance[target[0]][target[1]] = dist;
                        deque.offerFirst(target);
                    }
                }
            }

            // walk: one step to a non-obstacle neighbour, cost 1
            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                    continue;
                }
                if (matrix[nr].charAt(nc) == '#') {
                    continue;
                }
                if (dist + 1 < distance[nr][nc]) {
                    distance[nr][nc] = dist + 1;
                    deque.offerLast(new int[] { nr, nc });
                }
            }
        }
        return -1;
    }
}
