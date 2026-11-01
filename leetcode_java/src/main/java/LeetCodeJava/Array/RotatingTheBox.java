package LeetCodeJava.Array;

// https://leetcode.com/problems/rotating-the-box/description/
/**
 * 1861. Rotating the Box
 * Medium
 *
 * You are given an m x n matrix of characters boxGrid representing a side-view of a box. Each cell
 * of the box is one of the following:
 *
 * A stone '#'
 * A stationary obstacle '*'
 * Empty '.'
 *
 * The box is rotated 90 degrees clockwise, causing some of the stones to fall due to gravity. Each
 * stone falls down until it lands on an obstacle, another stone, or the bottom of the box. Gravity
 * does not affect the obstacles' positions, and the inertia from the box's rotation does not affect
 * the stones' horizontal positions.
 *
 * It is guaranteed that each stone in boxGrid rests on an obstacle, another stone, or the bottom of
 * the box.
 *
 * Return an n x m matrix representing the box after the rotation described above.
 *
 *
 * Example 1:
 *
 * Input: boxGrid = [["#",".","#"]]
 * Output: [["."],["#"],["#"]]
 *
 * Example 2:
 *
 * Input: boxGrid = [["#",".","*","."],["#","#","*","."]]
 * Output: [["#","."],["#","#"],["*","*"],[".","."]]
 *
 * Example 3:
 *
 * Input: boxGrid = [["#","#","*",".","*","."],["#","#","#","*",".","."],["#","#","#",".","#","."]]
 * Output: [[".","#","#"],[".","#","#"],["#","#","*"],["#","*","."],["#",".","*"],["#",".","."]]
 *
 *
 * Constraints:
 *
 * m == boxGrid.length
 * n == boxGrid[i].length
 * 1 <= m, n <= 500
 * boxGrid[i][j] is either '#', '*', or '.'.
 *
 */
public class RotatingTheBox {

    private static final char STONE = '#';
    private static final char OBSTACLE = '*';
    private static final char EMPTY = '.';

    // V0
    // IDEA: SIMULATE GRAVITY IN THE ORIGINAL FRAME, THEN ROTATE
    // after a clockwise rotation "down" is "right" inside each original row, so each row is settled
    // with a right-to-left write pointer (the next free slot a stone can fall into), then cell
    // (i, j) is copied to (j, m - 1 - i).
    /**
     * time = O(M * N)
     * space = O(M * N) for the output
     */
    public char[][] rotateTheBox(char[][] boxGrid) {
        int rows = boxGrid.length;
        int cols = boxGrid[0].length;

        // settle a copy so the input is left untouched
        char[][] settled = new char[rows][];
        for (int i = 0; i < rows; i++) {
            settled[i] = boxGrid[i].clone();
        }

        for (int i = 0; i < rows; i++) {
            char[] row = settled[i];
            // lowest free slot (after rotation) a stone can fall into
            int write = cols - 1;
            for (int j = cols - 1; j >= 0; j--) {
                if (row[j] == OBSTACLE) {
                    // nothing passes an obstacle: stones to its left pile up just before it
                    write = j - 1;
                } else if (row[j] == STONE) {
                    row[j] = EMPTY;
                    row[write] = STONE;
                    write--;
                }
            }
        }

        // rotate 90 degrees clockwise: (i, j) -> (j, rows - 1 - i)
        char[][] rotated = new char[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotated[j][rows - 1 - i] = settled[i][j];
            }
        }
        return rotated;
    }
}
