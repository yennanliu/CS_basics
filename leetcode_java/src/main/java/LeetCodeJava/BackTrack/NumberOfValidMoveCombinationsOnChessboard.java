package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/number-of-valid-move-combinations-on-chessboard/

/**
 *  2056. Number of Valid Move Combinations On Chessboard
 *  Hard
 *
 *  There is an 8 x 8 chessboard containing n pieces (rooks, queens, or bishops).
 *  You are given a string array pieces of length n, where pieces[i] describes the
 *  type (rook, queen, or bishop) of the ith piece. In addition, you are given a 2D
 *  integer array positions also of length n, where positions[i] = [ri, ci]
 *  indicates that the ith piece is currently at the 1-based coordinate (ri, ci) on
 *  the chessboard.
 *
 *  When making a move for a piece, you choose a destination square that the piece
 *  will travel toward and stop on.
 *
 *  A rook can only travel horizontally or vertically.
 *  A queen can only travel horizontally, vertically, or diagonally.
 *  A bishop can only travel diagonally.
 *
 *  You must make a move for every piece on the board simultaneously. A move
 *  combination consists of all the moves performed on all the given pieces. Every
 *  second, each piece will instantaneously travel one square towards their
 *  destination if they are not already at it. All pieces start traveling at the 0th
 *  second. A move combination is invalid if, at a given time, two or more pieces
 *  occupy the same square.
 *
 *  Return the number of valid move combinations.
 *
 *  Notes:
 *    No two pieces will start in the same square.
 *    You may choose the square a piece is already on as its destination.
 *    If two pieces are directly adjacent to each other, it is valid for them to
 *    move past each other and swap positions in one second.
 *
 *  Example 1:
 *    Input: pieces = ["rook"], positions = [[1,1]]
 *    Output: 15
 *
 *  Example 2:
 *    Input: pieces = ["queen"], positions = [[1,1]]
 *    Output: 22
 *
 *  Example 3:
 *    Input: pieces = ["bishop"], positions = [[4,3]]
 *    Output: 12
 *
 *  Constraints:
 *    n == pieces.length
 *    n == positions.length
 *    1 <= n <= 4
 *    pieces only contains the strings "rook", "queen", and "bishop".
 *    There will be at most one queen on the chessboard.
 *    1 <= ri, ci <= 8
 *    Each positions[i] is distinct.
 */
public class NumberOfValidMoveCombinationsOnChessboard {

    private static final int[][] ROOK = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
    private static final int[][] BISHOP = { { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
    private static final int[][] QUEEN = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 },
            { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

    private static final int M = 9; // 1-based board, index 1..8

    private String[] pieces;
    private int[][] positions;
    private int n;
    private int[][][] dist; // dist[i][x][y] = second at which piece i sits on (x, y), else -1
    private int[][] end;    // end[i] = {x, y, t} : where piece i finally parks
    private int res;

    // V0
    // IDEA: BACKTRACKING PIECE BY PIECE, RECORDING EACH PIECE'S TIMELINE
    //       n <= 4 and every piece walks at most 7 squares in one of <= 8 directions,
    //       so brute forcing (direction, stop-square) per piece is tiny.
    //
    //       when placing piece i we only need to check it against pieces 0..i-1:
    //         - checkPass(x, y, t) : nobody earlier is on (x, y) at exactly second t,
    //           and nobody earlier has already PARKED on (x, y) at a second <= t.
    //         - checkStop(x, y, t) : to park here, every earlier piece must have left
    //           (x, y) strictly before t.
    //
    //       passing THROUGH each other (swapping) is legal, which is why the collision
    //       test compares equal timestamps rather than segments.
    /**
     * time = O((8 * 8)^n) with n <= 4
     * space = O(n * 81)
     */
    public int countCombinations(String[] pieces, int[][] positions) {
        this.pieces = pieces;
        this.positions = positions;
        this.n = pieces.length;
        this.dist = new int[n][M][M];
        this.end = new int[n][3];
        this.res = 0;
        for (int i = 0; i < n; i++) {
            reset(i);
        }
        dfs(0);
        return res;
    }

    private int[][] dirsOf(String p) {
        char c = p.charAt(0);
        if (c == 'r') {
            return ROOK;
        }
        if (c == 'b') {
            return BISHOP;
        }
        return QUEEN;
    }

    private void reset(int i) {
        for (int a = 0; a < M; a++) {
            for (int b = 0; b < M; b++) {
                dist[i][a][b] = -1;
            }
        }
    }

    private boolean checkStop(int i, int x, int y, int t) {
        for (int j = 0; j < i; j++) {
            if (dist[j][x][y] >= t) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPass(int i, int x, int y, int t) {
        for (int j = 0; j < i; j++) {
            if (dist[j][x][y] == t) {
                return false;
            }
            if (end[j][0] == x && end[j][1] == y && end[j][2] <= t) {
                return false;
            }
        }
        return true;
    }

    private void dfs(int i) {
        if (i == n) {
            res++;
            return;
        }
        int x = positions[i][0];
        int y = positions[i][1];

        // option 1 : stay put
        reset(i);
        dist[i][x][y] = 0;
        end[i][0] = x;
        end[i][1] = y;
        end[i][2] = 0;
        if (checkStop(i, x, y, 0)) {
            dfs(i + 1);
        }

        // option 2 : walk along one direction and stop somewhere
        for (int[] d : dirsOf(pieces[i])) {
            reset(i);
            dist[i][x][y] = 0;
            int nx = x + d[0];
            int ny = y + d[1];
            int nt = 1;
            while (nx >= 1 && nx < M && ny >= 1 && ny < M && checkPass(i, nx, ny, nt)) {
                dist[i][nx][ny] = nt;
                end[i][0] = nx;
                end[i][1] = ny;
                end[i][2] = nt;
                if (checkStop(i, nx, ny, nt)) {
                    dfs(i + 1);
                }
                nx += d[0];
                ny += d[1];
                nt++;
            }
        }
    }
}
