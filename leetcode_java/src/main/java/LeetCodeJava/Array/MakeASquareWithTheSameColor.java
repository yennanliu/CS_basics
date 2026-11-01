package LeetCodeJava.Array;

// https://leetcode.com/problems/make-a-square-with-the-same-color/description/

/**
 * 3127. Make a Square with the Same Color
 * Easy
 *
 * You are given a 2D matrix grid of size 3 x 3 consisting only of characters 'B' and 'W'. Character 'W' represents the white color, and character 'B' represents the black color.
 *
 * Your task is to change the color of at most one cell so that the matrix has a 2 x 2 square where all cells are of the same color.
 *
 * Return true if it is possible to create a 2 x 2 square of the same color, otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [["B","W","B"],["B","W","W"],["B","W","B"]]
 * Output: true
 * Explanation:
 * It can be done by changing the color of the grid[0][2].
 *
 * Example 2:
 *
 * Input: grid = [["B","W","B"],["W","B","W"],["B","W","B"]]
 * Output: false
 * Explanation:
 * It cannot be done by changing at most one cell.
 *
 * Example 3:
 *
 * Input: grid = [["B","W","B"],["B","W","W"],["B","W","W"]]
 * Output: true
 * Explanation:
 * The grid already contains a 2 x 2 square of the same color.
 *
 *
 * Constraints:
 *
 * grid.length == 3
 * grid[i].length == 3
 * grid[i][j] is either 'W' or 'B'.
 *
 */
public class MakeASquareWithTheSameColor {

    // V0
    // IDEA: A 2x2 BLOCK IS FIXABLE UNLESS IT IS AN EVEN 2-2 SPLIT
    // with one change allowed, four cells can be made uniform whenever at least
    // three already agree, i.e. the block's count of 'B' is 0, 1, 3 or 4.
    // a 3x3 grid has only four 2x2 blocks, so check them all.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean canMakeSquare(char[][] grid) {
        // (top, left) is the upper-left cell of each 2x2 block
        for (int top = 0; top < 2; top++) {
            for (int left = 0; left < 2; left++) {
                int blacks = 0;
                for (int r = top; r < top + 2; r++) {
                    for (int c = left; c < left + 2; c++) {
                        if (grid[r][c] == 'B') {
                            blacks++;
                        }
                    }
                }
                if (blacks != 2) {
                    return true;
                }
            }
        }
        return false;
    }
}
