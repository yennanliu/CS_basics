package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximum-number-of-moves-to-kill-all-pawns/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * 3283. Maximum Number of Moves to Kill All Pawns
 * Hard
 *
 * There is a 50 x 50 chessboard with one knight and some pawns on it. You are given two integers kx and ky where (kx, ky) denotes the position of the knight, and a 2D array positions where positions[i] = [x_i, y_i] denotes the position of the pawns on the chessboard.
 *
 * Alice and Bob play a turn-based game, where Alice goes first. In each player's turn:
 *
 * The player selects a pawn that still exists on the board and captures it with the knight in the fewest possible moves. Note that the player can select any pawn, it might not be one that can be captured in the least number of moves.
 * In the above scenario, after capturing the pawn, there might be some other pawns on the board.
 *
 * Alice is trying to maximize the sum of the number of moves made by both players until there are no more pawns on the board, whereas Bob tries to minimize them.
 *
 * Return the maximum total number of moves made during the game that Alice can achieve, assuming both players play optimally.
 *
 * Note that in one move, a chess knight has eight possible positions it can move to.
 *
 *
 * Example 1:
 *
 * Input: kx = 1, ky = 1, positions = [[0,0]]
 * Output: 4
 * Explanation:
 * The knight takes 4 moves to reach the pawn at (0, 0).
 *
 * Example 2:
 *
 * Input: kx = 0, ky = 2, positions = [[1,1],[2,2],[3,3]]
 * Output: 8
 * Explanation:
 * Alice picks the pawn at (2, 2) and captures it in two moves.
 * Bob picks the pawn at (3, 3) and captures it in two moves.
 * Alice picks the pawn at (1, 1) and captures it in four moves.
 *
 * Example 3:
 *
 * Input: kx = 0, ky = 0, positions = [[1,2],[2,4]]
 * Output: 3
 * Explanation:
 * Alice picks the pawn at (2, 4) and captures it in two moves.
 * Bob picks the pawn at (1, 2) and captures it in one move.
 *
 *
 * Constraints:
 *
 * 0 <= kx, ky <= 49
 * 1 <= positions.length <= 15
 * positions[i].length == 2
 * 0 <= positions[i][0], positions[i][1] <= 49
 * All positions[i] are unique.
 * The input is generated such that positions[i] != [kx, ky].
 *
 */
public class MaximumNumberOfMovesToKillAllPawns {

    private static final int BOARD_SIZE = 50;
    private static final int[][] KNIGHT_MOVES = {
            { 1, 2 }, { 2, 1 }, { -1, 2 }, { -2, 1 },
            { 1, -2 }, { 2, -1 }, { -1, -2 }, { -2, -1 } };

    // V0
    // IDEA: BFS KNIGHT DISTANCES FROM EVERY SOURCE + MINIMAX BITMASK DP (MEMO)
    //   the cost of a capture is the knight's shortest path, and only n + 1
    //   squares can be a source (the knight and each pawn), so n + 1 BFS runs
    //   over the 50x50 board give every distance.
    //   Game state = (square the knight stands on, set of pawns taken).
    //   Alice (even number taken) maximises, Bob (odd) minimises the total.
    /**
     * time = O((N + 1) * 50^2 + 2^N * N^2)
     * space = O(2^N * N)
     */
    public int maxMoves(int kx, int ky, int[][] positions) {
        int n = positions.length;

        // source 0 is the knight, source b + 1 is pawn b
        int[][] sources = new int[n + 1][];
        sources[0] = new int[] { kx, ky };
        for (int b = 0; b < n; b++) {
            sources[b + 1] = positions[b];
        }

        // cost[a][b] = knight moves from source a to pawn b
        int[][] cost = new int[n + 1][n];
        for (int a = 0; a <= n; a++) {
            int[][] distance = knightDistances(sources[a][0], sources[a][1]);
            for (int b = 0; b < n; b++) {
                cost[a][b] = distance[positions[b][0]][positions[b][1]];
            }
        }

        // memo[at][taken] = best total from this state, -1 = not computed
        int[][] memo = new int[n + 1][1 << n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return play(0, 0, n, cost, memo);
    }

    /** Minimax value with the knight at source `at` and pawns in bitmask `taken` gone. */
    private int play(int at, int taken, int n, int[][] cost, int[][] memo) {
        int allTaken = (1 << n) - 1;
        if (taken == allTaken) {
            return 0;
        }
        if (memo[at][taken] != -1) {
            return memo[at][taken];
        }
        // Alice moves when an even number of pawns has been taken
        boolean aliceTurn = Integer.bitCount(taken) % 2 == 0;
        int best = aliceTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int b = 0; b < n; b++) {
            // skip pawn b if its bit is already set in `taken`
            if ((taken & (1 << b)) != 0) {
                continue;
            }
            int total = cost[at][b] + play(b + 1, taken | (1 << b), n, cost, memo);
            if (aliceTurn) {
                best = Math.max(best, total);
            } else {
                best = Math.min(best, total);
            }
        }
        memo[at][taken] = best;
        return best;
    }

    /** BFS over the board: fewest knight moves from (startX, startY) to every square. */
    private int[][] knightDistances(int startX, int startY) {
        int[][] distance = new int[BOARD_SIZE][BOARD_SIZE];
        for (int[] row : distance) {
            Arrays.fill(row, -1);
        }
        distance[startX][startY] = 0;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] { startX, startY });
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] move : KNIGHT_MOVES) {
                int nx = cur[0] + move[0];
                int ny = cur[1] + move[1];
                if (nx < 0 || nx >= BOARD_SIZE || ny < 0 || ny >= BOARD_SIZE) {
                    continue;
                }
                if (distance[nx][ny] == -1) {
                    distance[nx][ny] = distance[cur[0]][cur[1]] + 1;
                    queue.offer(new int[] { nx, ny });
                }
            }
        }
        return distance;
    }
}
