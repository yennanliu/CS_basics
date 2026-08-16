package LeetCodeJava.Math;

// https://leetcode.com/problems/transform-to-chessboard/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 782. Transform to Chessboard
 * Hard
 *
 * You are given an n x n binary grid board. In each move, you can swap any two rows
 * with each other, or any two columns with each other.
 *
 * Return the minimum number of moves to transform the board into a chessboard board.
 * If the task is impossible, return -1.
 *
 * A chessboard board is a board where no 0's and no 1's are 4-directionally adjacent.
 *
 *
 * Example 1:
 *
 * Input: board = [[0,1,1,0],[0,1,1,0],[1,0,0,1],[1,0,0,1]]
 * Output: 2
 * Explanation: One potential sequence of moves is shown.
 * The first move swaps the first and second column.
 * The second move swaps the second and third row.
 *
 * Example 2:
 *
 * Input: board = [[0,1],[1,0]]
 * Output: 0
 * Explanation: Also note that the board with 0 in the top left corner,
 * is also a valid chessboard.
 *
 * Example 3:
 *
 * Input: board = [[1,0],[1,0]]
 * Output: -1
 * Explanation: No matter what sequence of moves you make,
 * you cannot end with a valid chessboard.
 *
 *
 * Constraints:
 *
 * n == board.length
 * n == board[i].length
 * 2 <= n <= 30
 * board[i][j] is either 0 or 1.
 *
 */
public class TransformToChessboard {

    // V0
    // IDEA: MATH / PATTERN OBSERVATION
    /**
     *  KEY OBSERVATIONS:
     *
     *   1) A board is fixable ONLY IF every row is either IDENTICAL to row 0
     *      or the exact COMPLEMENT of row 0 (same for columns).
     *      That is equivalent to:
     *          board[0][0] ^ board[i][0] ^ board[0][j] ^ board[i][j] == 0
     *      for every (i, j) -> the whole board is DETERMINED by its
     *      first row and first column.
     *
     *   2) Row / column swaps NEVER change the multiset of rows, so the
     *      number of 1s in row 0 (and col 0) must be n/2 or (n+1)/2.
     *
     *   3) Once valid, the answer is decided purely by how many entries of
     *      the first column (resp. first row) already sit on the `right`
     *      parity. Each swap fixes 2 misplaced entries, so we divide by 2.
     *      - n EVEN : the board may start with 0 or 1, take the CHEAPER one.
     *      - n ODD  : only ONE starting bit is possible, the odd count is invalid.
     *
     *  time  = O(n^2)
     *  space = O(1)
     */
    public int movesToChessboard(int[][] board) {
        int n = board.length;

        // 1) every 2x2 sub-rectangle formed with (0,0) must XOR to 0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((board[0][0] ^ board[i][0] ^ board[0][j] ^ board[i][j]) != 0) {
                    return -1;
                }
            }
        }

        // 2) the first row / first column must be (almost) BALANCED
        int rowOnes = 0;
        int colOnes = 0;
        for (int i = 0; i < n; i++) {
            rowOnes += board[0][i];
            colOnes += board[i][0];
        }
        if (rowOnes < n / 2 || rowOnes > (n + 1) / 2) {
            return -1;
        }
        if (colOnes < n / 2 || colOnes > (n + 1) / 2) {
            return -1;
        }

        /** NOTE !!!
         *
         *  3) count the entries already matching the `starts with 0` pattern,
         *     i.e. board[i][0] == i % 2  ->  0,1,0,1,...
         */
        int rowSwap = 0;
        int colSwap = 0;
        for (int i = 0; i < n; i++) {
            if (board[i][0] == i % 2) {
                rowSwap += 1;
            }
            if (board[0][i] == i % 2) {
                colSwap += 1;
            }
        }

        if (n % 2 == 1) {
            /** NOTE !!!
             *
             *  odd n: only ONE of the two patterns is reachable,
             *  and the reachable one always leaves an EVEN number of mismatches
             */
            if (rowSwap % 2 == 1) {
                rowSwap = n - rowSwap;
            }
            if (colSwap % 2 == 1) {
                colSwap = n - colSwap;
            }
        } else {
            // even n: BOTH patterns are reachable, pick the cheaper one
            rowSwap = Math.min(rowSwap, n - rowSwap);
            colSwap = Math.min(colSwap, n - colSwap);
        }

        // each swap puts 2 lines in place
        return (rowSwap + colSwap) / 2;
    }


    // V1
    // IDEA: COMPARE EVERY ROW AGAINST ROW 0 (identical or complement)
    /**
     *  Rather than the XOR identity over all (i, j), check the structural claim
     *  directly: every row must be either EQUAL to row 0 or its exact COMPLEMENT.
     *
     *  Same O(n^2), but the failure mode is legible -- you can print the offending
     *  row instead of an abstract parity violation.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public int movesToChessboard_1(int[][] board) {
        int n = board.length;

        for (int i = 1; i < n; i++) {
            boolean same = true;
            boolean opposite = true;
            for (int j = 0; j < n; j++) {
                if (board[i][j] != board[0][j]) {
                    same = false;
                }
                if (board[i][j] == board[0][j]) {
                    opposite = false;
                }
            }
            if (!same && !opposite) {
                return -1;
            }
        }

        int rowOnes = 0;
        int colOnes = 0;
        for (int i = 0; i < n; i++) {
            rowOnes += board[0][i];
            colOnes += board[i][0];
        }
        if (rowOnes < n / 2 || rowOnes > (n + 1) / 2) {
            return -1;
        }
        if (colOnes < n / 2 || colOnes > (n + 1) / 2) {
            return -1;
        }

        return swapsFor(board, true, n) + swapsFor(board, false, n);
    }

    /** minimum swaps to make the first column (or row) alternate */
    private int swapsFor(int[][] board, boolean column, int n) {
        int matches = 0;
        for (int i = 0; i < n; i++) {
            int v = column ? board[i][0] : board[0][i];
            if (v == i % 2) {
                matches += 1;
            }
        }
        if (n % 2 == 1) {
            if (matches % 2 == 1) {
                matches = n - matches;
            }
        } else {
            matches = Math.min(matches, n - matches);
        }
        return matches / 2;
    }

    // V2
    // IDEA: ROWS AS BITMASKS -- there may be at most TWO distinct rows
    /**
     *  Pack each row into an int. A fixable board has at most two distinct row
     *  values, they must be bitwise complements over n bits, and their counts must
     *  differ by at most one.
     *
     *  Reduces the whole validity check to a HashMap of at most two keys, and makes
     *  `two distinct rows` -- the real structural fact -- explicit.
     *
     *  time  = O(n^2)
     *  space = O(n)
     */
    public int movesToChessboard_2(int[][] board) {
        int n = board.length;
        int full = (1 << n) - 1;

        Map<Integer, Integer> rowCount = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int j = 0; j < n; j++) {
                mask = (mask << 1) | board[i][j];
            }
            rowCount.merge(mask, 1, Integer::sum);
        }

        if (rowCount.size() != 2) {
            return -1;
        }
        List<Integer> keys = new ArrayList<>(rowCount.keySet());
        int a = keys.get(0);
        int b = keys.get(1);
        if ((a ^ b) != full) {
            return -1;      // not complements
        }
        if (Math.abs(rowCount.get(a) - rowCount.get(b)) > 1) {
            return -1;
        }
        if (Math.abs(Integer.bitCount(a) - n / 2) > (n % 2)) {
            return -1;
        }

        return swapsFor(board, true, n) + swapsFor(board, false, n);
    }

    // V3
    // IDEA: BRUTE FORCE BFS over board states (tiny n only)
    /**
     *  Treat each board as a state and BFS over row swaps and column swaps.
     *
     *  Factorial state space, so it only terminates for n <= 4 -- but it makes no
     *  structural claim at all, which is exactly what makes it the oracle for the
     *  two clever versions.
     *
     *  time  = O((n!)^2 * n^2)
     *  space = O((n!)^2)
     */
    public int movesToChessboard_3(int[][] board) {
        int n = board.length;
        String start = encode(board, n);
        String goalA = target(n, 0);
        String goalB = target(n, 1);

        if (start.equals(goalA) || start.equals(goalB)) {
            return 0;
        }

        Deque<String> q = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();
        q.offer(start);
        seen.add(start);
        int steps = 0;

        while (!q.isEmpty()) {
            steps += 1;
            int levelSize = q.size();
            for (int t = 0; t < levelSize; t++) {
                String cur = q.poll();
                int[][] g = decode(cur, n);
                for (int i = 0; i < n; i++) {
                    for (int j = i + 1; j < n; j++) {
                        for (int mode = 0; mode < 2; mode++) {
                            int[][] nx = swap(g, i, j, mode == 0, n);
                            String key = encode(nx, n);
                            if (key.equals(goalA) || key.equals(goalB)) {
                                return steps;
                            }
                            if (seen.add(key)) {
                                q.offer(key);
                            }
                        }
                    }
                }
            }
            if (steps > n * 2) {
                break;   // the answer is never larger than this
            }
        }
        return -1;
    }

    private String encode(int[][] g, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(g[i][j]);
            }
        }
        return sb.toString();
    }

    private int[][] decode(String s, int n) {
        int[][] g = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                g[i][j] = s.charAt(i * n + j) - '0';
            }
        }
        return g;
    }

    private String target(int n, int firstBit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append((i + j + firstBit) % 2);
            }
        }
        return sb.toString();
    }

    private int[][] swap(int[][] g, int i, int j, boolean rows, int n) {
        int[][] r = new int[n][n];
        for (int a = 0; a < n; a++) {
            r[a] = g[a].clone();
        }
        if (rows) {
            int[] t = r[i];
            r[i] = r[j];
            r[j] = t;
        } else {
            for (int a = 0; a < n; a++) {
                int t = r[a][i];
                r[a][i] = r[a][j];
                r[a][j] = t;
            }
        }
        return r;
    }

}
