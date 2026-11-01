package LeetCodeJava.Math;

// https://leetcode.com/problems/determine-color-of-a-chessboard-square/description/

/**
 * 1812. Determine Color of a Chessboard Square
 * Easy
 *
 * You are given coordinates, a string that represents the coordinates of a square of the chessboard. Below is a chessboard for your reference.
 *
 * Return true if the square is white, and false if the square is black.
 *
 * The coordinate will always represent a valid chessboard square. The coordinate will always have the letter first, and the number second.
 *
 *
 * Example 1:
 *
 * Input: coordinates = "a1"
 * Output: false
 * Explanation: From the chessboard above, the square with coordinates "a1" is black, so return false.
 *
 * Example 2:
 *
 * Input: coordinates = "h3"
 * Output: true
 * Explanation: From the chessboard above, the square with coordinates "h3" is white, so return true.
 *
 * Example 3:
 *
 * Input: coordinates = "c7"
 * Output: false
 *
 *
 * Constraints:
 *
 * coordinates.length == 2
 * 'a' <= coordinates[0] <= 'h'
 * '1' <= coordinates[1] <= '8'
 *
 */
public class DetermineColorOfAChessboardSquare {

    // V0
    // IDEA: PARITY (a chessboard colours by (file + rank) % 2)
    //   "a1" is black and one step along a file or rank flips the colour,
    //   so with 0-based indices an even sum is black and an odd sum is white.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean squareIsWhite(String coordinates) {
        int file = coordinates.charAt(0) - 'a';
        int rank = coordinates.charAt(1) - '1';
        return (file + rank) % 2 == 1;
    }
}
