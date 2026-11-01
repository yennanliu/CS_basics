package LeetCodeJava.Array;

// https://leetcode.com/problems/snake-in-matrix/description/

import java.util.List;

/**
 * 3248. Snake in Matrix
 * Easy
 *
 * There is a snake in an n x n matrix grid and can move in four possible directions. Each cell in the grid is identified by the position: grid[i][j] = (i * n) + j.
 *
 * The snake starts at cell 0 and follows a sequence of commands.
 *
 * You are given an integer n representing the size of the grid and an array of strings commands where each command[i] is either "UP", "RIGHT", "DOWN", and "LEFT". It's guaranteed that the snake will remain within the grid boundaries throughout its movement.
 *
 * Return the position of the final cell where the snake ends up after executing commands.
 *
 *
 * Example 1:
 *
 * Input: n = 2, commands = ["RIGHT","DOWN"]
 * Output: 3
 * Explanation:
 *
 * Example 2:
 *
 * Input: n = 3, commands = ["DOWN","RIGHT","UP"]
 * Output: 1
 * Explanation:
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10
 * 1 <= commands.length <= 100
 * commands consists only of "UP", "RIGHT", "DOWN", and "LEFT".
 * The input is generated such that the snake will not move outside of the boundaries.
 *
 */
public class SnakeInMatrix {

    // V0
    // IDEA: SIMULATION (track row, col; encode as row * n + col at the end)
    /**
     * time = O(C)
     * space = O(1)
     */
    public int finalPositionOfSnake(int n, List<String> commands) {
        int row = 0;
        int col = 0;

        // the input guarantees the snake stays on the board, so no bounds checks
        for (String command : commands) {
            if (command.equals("UP")) {
                row--;
            } else if (command.equals("DOWN")) {
                row++;
            } else if (command.equals("LEFT")) {
                col--;
            } else {
                col++;
            }
        }
        return row * n + col;
    }
}
