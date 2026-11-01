package LeetCodeJava.Sort;

// https://leetcode.com/problems/matrix-cells-in-distance-order/description/

import java.util.*;

/**
 * 1030. Matrix Cells in Distance Order
 * Easy
 *
 * You are given four integers row, cols, rCenter, and cCenter. There is a rows x cols
 * matrix and you are on the cell with the coordinates (rCenter, cCenter).
 *
 * Return the coordinates of all cells in the matrix, sorted by their distance from
 * (rCenter, cCenter) from the smallest distance to the largest distance.
 * You may return the answer in any order that satisfies this condition.
 *
 * The distance between two cells (r1, c1) and (r2, c2) is |r1 - r2| + |c1 - c2|.
 *
 * Example 1:
 *
 * Input: rows = 1, cols = 2, rCenter = 0, cCenter = 0
 * Output: [[0,0],[0,1]]
 * Explanation: The distances from (0, 0) to other cells are: [0,1]
 *
 * Example 2:
 *
 * Input: rows = 2, cols = 2, rCenter = 0, cCenter = 1
 * Output: [[0,1],[0,0],[1,1],[1,0]]
 * Explanation: The distances from (0, 1) to other cells are: [0,1,1,2]
 * The answer [[0,1],[1,1],[0,0],[1,0]] would also be accepted as correct.
 *
 * Example 3:
 *
 * Input: rows = 2, cols = 3, rCenter = 1, cCenter = 2
 * Output: [[1,2],[0,2],[1,1],[0,1],[1,0],[0,0]]
 * Explanation: The distances from (1, 2) to other cells are: [0,1,1,2,2,3]
 * There are other answers that would also be accepted as correct,
 * such as [[1,2],[1,1],[0,2],[1,0],[0,1],[0,0]].
 *
 * Constraints:
 *
 * 1 <= rows, cols <= 100
 * 0 <= rCenter < rows
 * 0 <= cCenter < cols
 *
 */
public class MatrixCellsInDistanceOrder {

    // V0
    // IDEA: COLLECT EVERY CELL + SORT BY MANHATTAN DISTANCE TO THE CENTER
    /**
     * time = O(R * C * log(R * C))
     * space = O(R * C)
     */
    public int[][] allCellsDistOrder(int rows, int cols, int rCenter, int cCenter) {
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells.add(new int[] { r, c });
            }
        }

        // order cells by their distance to (rCenter, cCenter), nearest first
        cells.sort((a, b) -> Integer.compare(
                distance(a, rCenter, cCenter),
                distance(b, rCenter, cCenter)));

        int[][] result = new int[cells.size()][];
        for (int i = 0; i < cells.size(); i++) {
            result[i] = cells.get(i);
        }
        return result;
    }

    /** Manhattan distance from cell to (rCenter, cCenter). */
    private int distance(int[] cell, int rCenter, int cCenter) {
        return Math.abs(cell[0] - rCenter) + Math.abs(cell[1] - cCenter);
    }

    // V1
    // IDEA: BUCKET SORT BY DISTANCE (distances are bounded by rows + cols - 2, so no comparison sort is needed)
    /**
     * time = O(R * C)
     * space = O(R * C)
     */
    public int[][] allCellsDistOrder_1(int rows, int cols, int rCenter, int cCenter) {
        // the farthest cell is at most (rows - 1) + (cols - 1) away
        int maxDistance = rows + cols - 2;
        List<List<int[]>> buckets = new ArrayList<>();
        for (int d = 0; d <= maxDistance; d++) {
            buckets.add(new ArrayList<>());
        }

        // drop every cell into the bucket of its distance
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int d = Math.abs(r - rCenter) + Math.abs(c - cCenter);
                buckets.get(d).add(new int[] { r, c });
            }
        }

        // reading the buckets in order gives cells sorted by distance
        int[][] result = new int[rows * cols][];
        int index = 0;
        for (List<int[]> bucket : buckets) {
            for (int[] cell : bucket) {
                result[index] = cell;
                index++;
            }
        }
        return result;
    }
}
