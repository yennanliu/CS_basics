package LeetCodeJava.Array;

// https://leetcode.com/problems/queens-that-can-attack-the-king/description/

import java.util.*;

/**
 * 1222. Queens That Can Attack the King
 * Medium
 *
 * On a 0-indexed 8 x 8 chessboard, there can be multiple black queens and one white king.
 *
 * You are given a 2D integer array queens where queens[i] = [xQueen_i, yQueen_i] represents
 * the position of the ith black queen on the chessboard. You are also given an integer
 * array king of length 2 where king = [xKing, yKing] represents the position of the white
 * king.
 *
 * Return the coordinates of the black queens that can directly attack the king.
 * You may return the answer in any order.
 *
 * Example 1:
 *
 * Input: queens = [[0,1],[1,0],[4,0],[0,4],[3,3],[2,4]], king = [0,0]
 * Output: [[0,1],[1,0],[3,3]]
 *
 * Example 2:
 *
 * Input: queens = [[0,0],[1,1],[2,2],[3,4],[3,5],[4,4],[4,5]], king = [3,3]
 * Output: [[2,2],[3,4],[4,4]]
 *
 * Constraints:
 *
 * 1 <= queens.length < 64
 * queens[i].length == king.length == 2
 * 0 <= xQueen_i, yQueen_i, xKing, yKing < 8
 * All the given positions are unique.
 *
 */
public class QueensThatCanAttackTheKing {

    private static final int BOARD_SIZE = 8;

    // V0
    // IDEA: RAY CASTING FROM THE KING
    //   walk outward from the king in each of the 8 directions; the FIRST queen
    //   met on a ray is the only one attacking along it (it blocks the rest)
    /**
     * time = O(Q + 8 * 8)
     * space = O(8 * 8)
     */
    public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
        boolean[][] hasQueen = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int[] queen : queens) {
            hasQueen[queen[0]][queen[1]] = true;
        }

        List<List<Integer>> result = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                // (0, 0) is not a direction
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int x = king[0] + dx;
                int y = king[1] + dy;
                while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
                    if (hasQueen[x][y]) {
                        result.add(Arrays.asList(x, y));
                        // anything further along this ray is blocked
                        break;
                    }
                    x += dx;
                    y += dy;
                }
            }
        }
        return result;
    }
}
