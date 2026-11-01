package LeetCodeJava.Graph;

// https://leetcode.com/problems/strange-printer-ii/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * 1591. Strange Printer II
 * Hard
 *
 * There is a strange printer with the following two special requirements:
 *
 * - On each turn, the printer will print a solid rectangular pattern of a single
 *   color on the grid. This will cover up the existing colors in the rectangle.
 * - Once the printer has used a color for the above operation, the same color
 *   cannot be used again.
 *
 * You are given a m x n matrix targetGrid, where targetGrid[row][col] is the color
 * in the position (row, col) of the grid.
 *
 * Return true if it is possible to print the matrix targetGrid, otherwise, return
 * false.
 *
 *
 * Example 1:
 *
 * Input: targetGrid = [[1,1,1,1],[1,2,2,1],[1,2,2,1],[1,1,1,1]]
 * Output: true
 *
 * Example 2:
 *
 * Input: targetGrid = [[1,1,1,1],[1,1,3,3],[1,1,3,4],[5,5,1,4]]
 * Output: true
 *
 * Example 3:
 *
 * Input: targetGrid = [[1,2,1],[2,1,2],[1,2,1]]
 * Output: false
 * Explanation: It is impossible to form targetGrid because it is not allowed to
 * print the same color in different turns.
 *
 *
 * Constraints:
 *
 * m == targetGrid.length
 * n == targetGrid[i].length
 * 1 <= m, n <= 60
 * 1 <= targetGrid[row][col] <= 60
 *
 */
public class StrangePrinterII {

    private static final int MAX_COLOR = 60;

    // V0
    // IDEA: BOUNDING BOX + TOPOLOGICAL SORT (KAHN)
    //       each colour is printed as exactly its bounding box. Any other colour d
    //       inside colour c's box must have been printed AFTER c -> edge c -> d.
    //       The grid is printable iff this dependency graph has no cycle.
    /**
     * time = O(C * M * N)   # C = number of colours (<= 60)
     * space = O(C^2)
     */
    public boolean isPrintable(int[][] targetGrid) {
        int rows = targetGrid.length;
        int cols = targetGrid[0].length;

        // bounding box of every colour
        int[] top = new int[MAX_COLOR + 1];
        int[] bottom = new int[MAX_COLOR + 1];
        int[] left = new int[MAX_COLOR + 1];
        int[] right = new int[MAX_COLOR + 1];
        boolean[] present = new boolean[MAX_COLOR + 1];
        for (int color = 0; color <= MAX_COLOR; color++) {
            top[color] = rows;
            left[color] = cols;
            bottom[color] = -1;
            right[color] = -1;
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int color = targetGrid[r][c];
                present[color] = true;
                top[color] = Math.min(top[color], r);
                bottom[color] = Math.max(bottom[color], r);
                left[color] = Math.min(left[color], c);
                right[color] = Math.max(right[color], c);
            }
        }

        // node = colour, edge color -> other means "color is printed before other"
        List<Set<Integer>> graph = new ArrayList<>();
        for (int color = 0; color <= MAX_COLOR; color++) {
            graph.add(new HashSet<>());
        }
        int[] inDegree = new int[MAX_COLOR + 1];
        for (int color = 1; color <= MAX_COLOR; color++) {
            if (!present[color]) {
                continue;
            }
            for (int r = top[color]; r <= bottom[color]; r++) {
                for (int c = left[color]; c <= right[color]; c++) {
                    int other = targetGrid[r][c];
                    // `other` covers part of `color`'s rectangle, so it came later
                    if (other != color && graph.get(color).add(other)) {
                        inDegree[other]++;
                    }
                }
            }
        }

        // Kahn: repeatedly print a colour nothing else still has to precede
        Queue<Integer> queue = new ArrayDeque<>();
        int colorCount = 0;
        for (int color = 1; color <= MAX_COLOR; color++) {
            if (present[color]) {
                colorCount++;
                if (inDegree[color] == 0) {
                    queue.offer(color);
                }
            }
        }

        int printed = 0;
        while (!queue.isEmpty()) {
            int color = queue.poll();
            printed++;
            for (int next : graph.get(color)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        // a cycle leaves some colour never reaching in-degree 0
        return printed == colorCount;
    }
}
