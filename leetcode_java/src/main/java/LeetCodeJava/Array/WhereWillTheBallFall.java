package LeetCodeJava.Array;

// https://leetcode.com/problems/where-will-the-ball-fall/description/
/**
 * 1706. Where Will the Ball Fall
 * Medium
 *
 * You have a 2-D grid of size m x n representing a box, and you have n balls. The box is open on
 * the top and bottom sides.
 *
 * Each cell in the box has a diagonal board spanning two corners of the cell that can redirect a
 * ball to the right or to the left.
 *
 * A board that redirects the ball to the right spans the top-left corner to the bottom-right corner
 * and is represented in the grid as 1.
 * A board that redirects the ball to the left spans the top-right corner to the bottom-left corner
 * and is represented in the grid as -1.
 *
 * We drop one ball at the top of each column of the box. Each ball can get stuck in the box or fall
 * out of the bottom. A ball gets stuck if it hits a "V" shaped pattern between two boards or if a
 * board redirects the ball into either wall of the box.
 *
 * Return an array answer of size n where answer[i] is the column that the ball falls out of at the
 * bottom after dropping the ball from the ith column at the top, or -1 if the ball gets stuck in the
 * box.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,1,-1,-1],[1,1,1,-1,-1],[-1,-1,-1,1,1],[1,1,1,1,-1],[-1,-1,-1,-1,-1]]
 * Output: [1,-1,-1,-1,-1]
 *
 * Example 2:
 *
 * Input: grid = [[-1]]
 * Output: [-1]
 * Explanation: The ball gets stuck against the left wall.
 *
 * Example 3:
 *
 * Input: grid = [[1,1,1,1,1,1],[-1,-1,-1,-1,-1,-1],[1,1,1,1,1,1],[-1,-1,-1,-1,-1,-1]]
 * Output: [0,1,2,3,4,-1]
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 100
 * grid[i][j] is 1 or -1.
 *
 */
public class WhereWillTheBallFall {

    // V0
    // IDEA: SIMULATION (drop each ball row by row)
    // a ball at (row, col) slides to col + grid[row][col]. It passes the row only if that column is
    // inside the box AND its board slopes the same way; otherwise it hit a wall or a "V".
    /**
     * time = O(M * N)
     * space = O(1) excluding the output
     */
    public int[] findBall(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] answer = new int[cols];

        for (int start = 0; start < cols; start++) {
            int col = start;
            for (int row = 0; row < rows; row++) {
                int direction = grid[row][col];
                int nextCol = col + direction;
                // stuck against a wall, or a "V" formed with the neighbouring board
                if (nextCol < 0 || nextCol >= cols || grid[row][nextCol] != direction) {
                    col = -1;
                    break;
                }
                col = nextCol;
            }
            answer[start] = col;
        }
        return answer;
    }
}
