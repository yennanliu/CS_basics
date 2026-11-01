package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-moves-to-clean-the-classroom/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * 3568. Minimum Moves to Clean the Classroom
 * Medium
 *
 * You are given an m x n grid classroom where a student volunteer is tasked with cleaning up litter scattered around the room. Each cell in the grid is one of the following:
 *
 * 'S': Starting position of the student.
 * 'L': Litter that must be collected (once collected, the cell becomes an empty cell).
 * 'R': Reset area that restores the student's energy to full capacity, regardless of their current energy level (can be used multiple times).
 * 'X': Obstacle the student cannot pass through.
 * '.': Empty space.
 *
 * You are also given an integer energy, representing the maximum energy the student can have. The student starts with this energy from the starting position.
 *
 * Each move to an adjacent cell (up, down, left, or right) costs 1 unit of energy. If the energy reaches zero, the student can only continue if they are on a reset area 'R', which sets the energy back to the maximum value energy.
 *
 * Return the minimum number of moves required to collect all litter items, or -1 if it's impossible.
 *
 *
 * Example 1:
 *
 * Input: classroom = ["S.", "XL"], energy = 2
 * Output: 2
 * Explanation:
 * The student starts at (0, 0) with 2 units of energy.
 * Move right to (0, 1), costing 1 unit of energy, leaving 1 unit of energy.
 * Move down to (1, 1) to collect the litter 'L'.
 * The student collects all the litter using 2 moves. Thus, the output is 2.
 *
 * Example 2:
 *
 * Input: classroom = ["LS", "RL"], energy = 4
 * Output: 3
 * Explanation:
 * The student starts at (0, 1) with 4 units of energy.
 * Move down to (1, 1) to collect the litter 'L'.
 * Move left to (1, 0) which is a reset area 'R', restoring the energy back to full.
 * Move up to (0, 0) to collect the remaining litter 'L'.
 * The student collects all the litter using 3 moves. Thus, the output is 3.
 *
 * Example 3:
 *
 * Input: classroom = ["L.S", "RXL"], energy = 3
 * Output: -1
 * Explanation:
 * No valid path collects all 'L'.
 *
 *
 * Constraints:
 *
 * 1 <= m == classroom.length <= 20
 * 1 <= n == classroom[i].length <= 20
 * classroom[i][j] is one of 'S', 'L', 'R', 'X', or '.'
 * 1 <= energy <= 50
 * There is exactly one 'S' in the grid.
 * There are at most 10 'L' cells in the grid.
 *
 */
public class MinimumMovesToCleanTheClassroom {

    // V0
    // IDEA: BFS OVER (ROW, COL, COLLECTED-LITTER MASK), KEEPING THE BEST ENERGY PER STATE
    //   at most 10 litter cells -> the "already picked up" set is a 10-bit mask.
    //   Energy is not part of the key: for the same (cell, mask) more energy is
    //   never worse, so a state is re-expanded only when reached with strictly
    //   more energy. Every move costs 1, so BFS layers give the fewest moves.
    /**
     * time = O(M * N * 2^L * E)   (L = litter count, E = energy)
     * space = O(M * N * 2^L)
     */
    public int minMoves(String[] classroom, int energy) {
        int rows = classroom.length;
        int cols = classroom[0].length();

        // litterId[r][c] = bit index of the litter at (r, c), or -1
        int[][] litterId = new int[rows][cols];
        int litterCount = 0;
        int startRow = 0;
        int startCol = 0;
        for (int r = 0; r < rows; r++) {
            Arrays.fill(litterId[r], -1);
            for (int c = 0; c < cols; c++) {
                char ch = classroom[r].charAt(c);
                if (ch == 'S') {
                    startRow = r;
                    startCol = c;
                } else if (ch == 'L') {
                    litterId[r][c] = litterCount;
                    litterCount++;
                }
            }
        }
        int allCollected = (1 << litterCount) - 1;
        if (allCollected == 0) {
            return 0;
        }

        // bestEnergy[r][c][mask] = most energy left on reaching this state, -1 = never
        int[][][] bestEnergy = new int[rows][cols][allCollected + 1];
        for (int[][] plane : bestEnergy) {
            for (int[] row : plane) {
                Arrays.fill(row, -1);
            }
        }
        bestEnergy[startRow][startCol][0] = energy;

        // queue entries: {row, col, mask, energy left}
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] { startRow, startCol, 0, energy });
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
        int moves = 0;

        while (!queue.isEmpty()) {
            moves++;
            int layerSize = queue.size();
            for (int i = 0; i < layerSize; i++) {
                int[] cur = queue.poll();
                int energyLeft = cur[3];
                // no energy, no move
                if (energyLeft == 0) {
                    continue;
                }
                for (int[] dir : directions) {
                    int nr = cur[0] + dir[0];
                    int nc = cur[1] + dir[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
                        continue;
                    }
                    char ch = classroom[nr].charAt(nc);
                    if (ch == 'X') {
                        continue;
                    }
                    int nextEnergy = energyLeft - 1;
                    int nextMask = cur[2];
                    if (ch == 'L') {
                        // set this litter's bit: it is now collected
                        nextMask = nextMask | (1 << litterId[nr][nc]);
                    } else if (ch == 'R') {
                        // a reset area refills the energy
                        nextEnergy = energy;
                    }
                    if (nextMask == allCollected) {
                        return moves;
                    }
                    if (bestEnergy[nr][nc][nextMask] < nextEnergy) {
                        bestEnergy[nr][nc][nextMask] = nextEnergy;
                        queue.offer(new int[] { nr, nc, nextMask, nextEnergy });
                    }
                }
            }
        }
        return -1;
    }
}
