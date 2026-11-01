package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-paths-with-max-score/description/

import java.util.Arrays;
import java.util.List;

/**
 * 1301. Number of Paths with Max Score
 * Hard
 *
 * You are given a square board of characters. You can move on the board starting at the
 * bottom right square marked with the character 'S'.
 *
 * You need to reach the top left square marked with the character 'E'. The rest of the
 * squares are labeled either with a numeric character 1, 2, ..., 9 or with an obstacle 'X'.
 * In one move you can go up, left or up-left (diagonally) only if there is no obstacle there.
 *
 * Return a list of two integers: the first integer is the maximum sum of numeric characters
 * you can collect, and the second is the number of such paths that you can take to get that
 * maximum sum, taken modulo 10^9 + 7.
 *
 * In case there is no path, return [0, 0].
 *
 *
 * Example 1:
 *
 * Input: board = ["E23","2X2","12S"]
 * Output: [7,1]
 *
 * Example 2:
 *
 * Input: board = ["E12","1X1","21S"]
 * Output: [4,2]
 *
 * Example 3:
 *
 * Input: board = ["E11","XXX","11S"]
 * Output: [0,0]
 *
 *
 * Constraints:
 *
 * 2 <= board.length == board[i].length <= 100
 *
 */
public class NumberOfPathsWithMaxScore {

    private static final int MOD = 1_000_000_007;
    private static final int UNREACHABLE = -1;

    // V0
    // IDEA: 2D DP (MAX SCORE + PATH COUNT), FILLED BOTTOM-RIGHT -> TOP-LEFT
    /**
     * Walking from 'S', cell (r, c) is entered from (r+1, c), (r, c+1) or (r+1, c+1).
     *
     * score[r][c] = best score collected from 'S' to (r, c)   (UNREACHABLE if none)
     * count[r][c] = how many paths achieve score[r][c]
     *
     * merging a neighbour:
     *   strictly better score -> take its score AND replace the count
     *   equal score           -> keep the score, ADD the counts
     * then score[r][c] = best + digit(r, c); 'E' and 'S' add 0, 'X' is skipped.
     *
     * base: score[S] = 0, count[S] = 1; the modulo is taken while counts accumulate
     */
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int[] pathsWithMaxScore(List<String> board) {
        int n = board.size();
        int[][] score = new int[n][n];
        int[][] count = new int[n][n];
        for (int[] row : score) {
            Arrays.fill(row, UNREACHABLE);
        }
        score[n - 1][n - 1] = 0;
        count[n - 1][n - 1] = 1;

        // the three cells a move into (r, c) can come from
        int[][] fromOffsets = {{1, 0}, {0, 1}, {1, 1}};

        for (int r = n - 1; r >= 0; r--) {
            for (int c = n - 1; c >= 0; c--) {
                char cell = board.get(r).charAt(c);
                if ((r == n - 1 && c == n - 1) || cell == 'X') {
                    continue;
                }

                int bestScore = UNREACHABLE;
                int ways = 0;
                for (int[] offset : fromOffsets) {
                    int fromR = r + offset[0];
                    int fromC = c + offset[1];
                    if (fromR >= n || fromC >= n || score[fromR][fromC] == UNREACHABLE) {
                        continue;
                    }
                    if (score[fromR][fromC] > bestScore) {
                        bestScore = score[fromR][fromC];
                        ways = count[fromR][fromC];
                    } else if (score[fromR][fromC] == bestScore) {
                        ways = (ways + count[fromR][fromC]) % MOD;
                    }
                }
                if (bestScore == UNREACHABLE) {
                    continue;
                }

                // only digit cells add to the score ('E' adds nothing)
                if (Character.isDigit(cell)) {
                    bestScore += cell - '0';
                }
                score[r][c] = bestScore;
                count[r][c] = ways;
            }
        }

        if (score[0][0] == UNREACHABLE) {
            return new int[] {0, 0};
        }
        return new int[] {score[0][0], count[0][0]};
    }
}
