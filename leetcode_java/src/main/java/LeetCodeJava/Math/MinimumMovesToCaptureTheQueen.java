package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-moves-to-capture-the-queen/description/

/**
 * 3001. Minimum Moves to Capture The Queen
 * Medium
 *
 * There is a 1-indexed 8 x 8 chessboard containing 3 pieces.
 *
 * You are given 6 integers a, b, c, d, e, and f where:
 *
 * (a, b) denotes the position of the white rook.
 * (c, d) denotes the position of the white bishop.
 * (e, f) denotes the position of the black queen.
 *
 * Given that you can only move the white pieces, return the minimum number of moves required to capture the black queen.
 *
 * Note that:
 *
 * Rooks can move any number of squares either vertically or horizontally, but cannot jump over other pieces.
 * Bishops can move any number of squares diagonally, but cannot jump over other pieces.
 * A rook or a bishop can capture the queen if it is located in a square that they can move to.
 * The queen does not move.
 *
 *
 * Example 1:
 *
 * Input: a = 1, b = 1, c = 8, d = 8, e = 2, f = 3
 * Output: 2
 * Explanation: We can capture the black queen in two moves by moving the white rook to (1, 3) then to (2, 3).
 * It is impossible to capture the black queen in less than two moves since it is not being attacked by any of the pieces at the beginning.
 *
 * Example 2:
 *
 * Input: a = 5, b = 3, c = 3, d = 4, e = 5, f = 2
 * Output: 1
 * Explanation: We can capture the black queen in a single move by doing one of the following:
 * - Move the white rook to (5, 2).
 * - Move the white bishop to (5, 2).
 *
 *
 * Constraints:
 *
 * 1 <= a, b, c, d, e, f <= 8
 *
 */
public class MinimumMovesToCaptureTheQueen {

    // V0
    // IDEA: THE ANSWER IS ONLY EVER 1 OR 2 - SO JUST TEST FOR 1
    // two moves always suffice: the rook moves onto the queen's row (or column)
    // and captures next move. so the only question is whether a piece already
    // attacks the queen without the other white piece standing in the way.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minMovesToCaptureTheQueen(int a, int b, int c, int d, int e, int f) {
        // rook (a, b) and queen (e, f) share a row; blocked only if the bishop
        // is on that row strictly between them
        if (a == e) {
            boolean bishopBlocks = c == a && isStrictlyBetween(b, d, f);
            if (!bishopBlocks) {
                return 1;
            }
        }

        // rook and queen share a column; blocked only if the bishop is on that
        // column strictly between them
        if (b == f) {
            boolean bishopBlocks = d == b && isStrictlyBetween(a, c, e);
            if (!bishopBlocks) {
                return 1;
            }
        }

        // bishop (c, d) and queen share a diagonal; blocked only if the rook is
        // on that same diagonal strictly between them
        if (Math.abs(c - e) == Math.abs(d - f)) {
            boolean rookOnBishopDiagonal = Math.abs(a - c) == Math.abs(b - d);
            boolean rookBlocks = rookOnBishopDiagonal
                    && isStrictlyBetween(c, a, e)
                    && isStrictlyBetween(d, b, f);
            if (!rookBlocks) {
                return 1;
            }
        }

        return 2;
    }

    /** True if y lies in the open interval between x and z. */
    private boolean isStrictlyBetween(int x, int y, int z) {
        return Math.min(x, z) < y && y < Math.max(x, z);
    }
}
