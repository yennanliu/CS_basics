package LeetCodeJava.HashTable;

// https://leetcode.com/problems/grid-illumination/description/

import java.util.*;

/**
 * 1001. Grid Illumination
 * Hard
 *
 * There is a 2D grid of size n x n where each cell of this grid has a lamp that is initially turned off.
 *
 * You are given a 2D array of lamp positions lamps, where lamps[i] = [rowi, coli] indicates that the
 * lamp at grid[rowi][coli] is turned on. Even if the same lamp is listed more than once, it is turned on.
 *
 * When a lamp is turned on, it illuminates its cell and all other cells in the same row, column, or diagonal.
 *
 * You are also given another 2D array queries, where queries[j] = [rowj, colj]. For the jth query,
 * determine whether grid[rowj][colj] is illuminated or not. After answering the jth query, turn off
 * the lamp at grid[rowj][colj] and its 8 adjacent lamps if they exist. A lamp is adjacent if its
 * cell shares either a side or corner with grid[rowj][colj].
 *
 * Return an array of integers ans, where ans[j] should be 1 if the cell in the jth query was
 * illuminated, or 0 if the lamp was not.
 *
 *
 * Example 1:
 *
 * Input: n = 5, lamps = [[0,0],[4,4]], queries = [[1,1],[1,0]]
 * Output: [1,0]
 *
 * Example 2:
 *
 * Input: n = 5, lamps = [[0,0],[4,4]], queries = [[1,1],[1,1]]
 * Output: [1,1]
 *
 * Example 3:
 *
 * Input: n = 5, lamps = [[0,0],[0,4]], queries = [[0,4],[0,1],[1,4]]
 * Output: [1,1,0]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 * 0 <= lamps.length <= 20000
 * 0 <= queries.length <= 20000
 * lamps[i].length == 2
 * 0 <= rowi, coli < n
 * queries[j].length == 2
 * 0 <= rowj, colj < n
 *
 */
public class GridIllumination {

    // V0
    // IDEA: HASH TABLE (count lamps per row / col / diagonal / anti-diagonal)
    /**
     * time = O(M + Q)
     * space = O(M)
     */
    public int[] gridIllumination(int n, int[][] lamps, int[][] queries) {
        // a lamp cell is encoded as one long key: row * n + col (n can be 10^9, so use long)
        Set<Long> lampsOn = new HashSet<>();

        // how many lit lamps sit on each line; a cell is lit if any of its 4 lines is > 0
        Map<Integer, Integer> rowCount = new HashMap<>();
        Map<Integer, Integer> colCount = new HashMap<>();
        Map<Integer, Integer> diagCount = new HashMap<>();     // key = row - col ("\" diagonal)
        Map<Integer, Integer> antiDiagCount = new HashMap<>(); // key = row + col ("/" diagonal)

        for (int[] lamp : lamps) {
            int row = lamp[0];
            int col = lamp[1];
            // lamps may repeat: count each cell once, or the counters never drop back to 0
            if (!lampsOn.add(toKey(row, col, n))) {
                continue;
            }
            addTo(rowCount, row, 1);
            addTo(colCount, col, 1);
            addTo(diagCount, row - col, 1);
            addTo(antiDiagCount, row + col, 1);
        }

        int[] result = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int row = queries[q][0];
            int col = queries[q][1];

            boolean lit = rowCount.getOrDefault(row, 0) > 0
                    || colCount.getOrDefault(col, 0) > 0
                    || diagCount.getOrDefault(row - col, 0) > 0
                    || antiDiagCount.getOrDefault(row + col, 0) > 0;
            result[q] = lit ? 1 : 0;

            // switch off the lamp at the query cell and its 8 neighbours
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    if (r < 0 || r >= n || c < 0 || c >= n) {
                        continue;
                    }
                    if (lampsOn.remove(toKey(r, c, n))) {
                        addTo(rowCount, r, -1);
                        addTo(colCount, c, -1);
                        addTo(diagCount, r - c, -1);
                        addTo(antiDiagCount, r + c, -1);
                    }
                }
            }
        }
        return result;
    }

    /** Encodes a cell as a single long so it can live in a HashSet. */
    private long toKey(int row, int col, int n) {
        return (long) row * n + col;
    }

    /** Adds delta to the counter stored under key. */
    private void addTo(Map<Integer, Integer> counter, int key, int delta) {
        counter.put(key, counter.getOrDefault(key, 0) + delta);
    }
}
