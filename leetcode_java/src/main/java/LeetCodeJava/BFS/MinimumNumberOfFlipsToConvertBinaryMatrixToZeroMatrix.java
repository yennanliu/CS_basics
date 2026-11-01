package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-number-of-flips-to-convert-binary-matrix-to-zero-matrix/description/

import java.util.*;

/**
 * 1284. Minimum Number of Flips to Convert Binary Matrix to Zero Matrix
 * Hard
 *
 * Given a m x n binary matrix mat. In one step, you can choose one cell and flip it
 * and all the four neighbors of it if they exist
 * (Flip is changing 1 to 0 and 0 to 1).
 * A pair of cells are called neighbors if they share one edge.
 *
 * Return the minimum number of steps required to convert mat to a zero matrix
 * or -1 if you cannot.
 *
 * A binary matrix is a matrix with all cells equal to 0 or 1 only.
 *
 * A zero matrix is a matrix with all cells equal to 0.
 *
 *
 * Example 1:
 *
 * Input: mat = [[0,0],[0,1]]
 * Output: 3
 * Explanation: One possible solution is to flip (1, 0) then (0, 1) and finally (1, 1).
 *
 * Example 2:
 *
 * Input: mat = [[0]]
 * Output: 0
 * Explanation: Given matrix is a zero matrix. We do not need to change it.
 *
 * Example 3:
 *
 * Input: mat = [[1,0,0],[1,0,0]]
 * Output: -1
 * Explanation: Given matrix cannot be a zero matrix.
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 3
 * mat[i][j] is either 0 or 1.
 *
 */
public class MinimumNumberOfFlipsToConvertBinaryMatrixToZeroMatrix {

    private static final int[][] FLIP_OFFSETS = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // V0
    // IDEA: BFS OVER BITMASK STATES
    //  m, n <= 3, so the matrix fits in at most 9 bits (bit r * n + c = cell (r, c)).
    //  Each of the m * n clicks is an edge (XOR with that cell's flip mask);
    //  the first time BFS reaches state 0 is the minimum number of clicks.
    /**
     * time = O(2^(M*N) * M * N)
     * space = O(2^(M*N))
     */
    public int minFlips(int[][] mat) {
        int start = encode(mat);
        if (start == 0) {
            return 0;
        }
        int[] flipMasks = buildFlipMasks(mat.length, mat[0].length);

        boolean[] visited = new boolean[1 << (mat.length * mat[0].length)];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        visited[start] = true;

        int steps = 0;
        while (!queue.isEmpty()) {
            steps++;
            int levelSize = queue.size();
            for (int k = 0; k < levelSize; k++) {
                int current = queue.poll();
                for (int mask : flipMasks) {
                    // XOR toggles exactly the clicked cell and its neighbours
                    int next = current ^ mask;
                    if (next == 0) {
                        return steps;
                    }
                    if (!visited[next]) {
                        visited[next] = true;
                        queue.offer(next);
                    }
                }
            }
        }
        return -1;
    }

    // V1
    // IDEA: BRUTE FORCE OVER SUBSETS OF CLICKS
    //  clicking a cell twice is a no-op and click order does not matter,
    //  so an answer is just a SUBSET of cells; try all 2^(m*n) and keep the smallest.
    /**
     * time = O(2^(M*N) * M * N)
     * space = O(M * N)
     */
    public int minFlips_1(int[][] mat) {
        int start = encode(mat);
        int[] flipMasks = buildFlipMasks(mat.length, mat[0].length);
        int cellCount = flipMasks.length;

        int best = -1;
        for (int subset = 0; subset < (1 << cellCount); subset++) {
            int current = start;
            int clicks = 0;
            for (int cell = 0; cell < cellCount; cell++) {
                // is bit `cell` set in subset -> click that cell
                if ((subset & (1 << cell)) != 0) {
                    current ^= flipMasks[cell];
                    clicks++;
                }
            }
            if (current == 0 && (best == -1 || clicks < best)) {
                best = clicks;
            }
        }
        return best;
    }

    /** Packs the matrix into an int: bit r * n + c is mat[r][c]. */
    private int encode(int[][] mat) {
        int cols = mat[0].length;
        int state = 0;
        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 1) {
                    state |= 1 << (r * cols + c);
                }
            }
        }
        return state;
    }

    /** For every cell, the bitmask of the cells a click on it toggles (itself + 4 neighbours). */
    private int[] buildFlipMasks(int rows, int cols) {
        int[] masks = new int[rows * cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int mask = 0;
                for (int[] offset : FLIP_OFFSETS) {
                    int nr = r + offset[0];
                    int nc = c + offset[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        mask |= 1 << (nr * cols + nc);
                    }
                }
                masks[r * cols + c] = mask;
            }
        }
        return masks;
    }
}
