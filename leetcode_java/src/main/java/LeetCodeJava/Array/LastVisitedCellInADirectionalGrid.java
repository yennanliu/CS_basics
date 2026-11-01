package LeetCodeJava.Array;

// https://leetcode.com/problems/last-visited-cell-in-a-directional-grid/description/

/**
 * 4084. Last Visited Cell in a Directional Grid
 * Medium
 *
 * You are given an m x n matrix grid where each cell contains one of the
 * following characters:
 *
 * 'L': move one cell left.
 * 'R': move one cell right.
 * 'U': move one cell up.
 * 'D': move one cell down.
 *
 * You start at cell (0, 0), which is considered visited. At each step, move
 * to the cell indicated by the character in your current cell.
 *
 * The process ends if the indicated move would either leave the grid or
 * enter a cell that has already been visited. In either case, the move is
 * not made.
 *
 * Return an integer array [r, c] representing the row and column indices of
 * the last visited cell. The indices are 0-based.
 *
 *
 * Example 1:
 *
 * Input: grid = [["R","D"],["U","L"]]
 * Output: [1,0]
 * Explanation:
 * Start at cell (0, 0), which contains 'R', so move right to (0, 1).
 * At cell (0, 1), the character is 'D', so move down to (1, 1).
 * At cell (1, 1), the character is 'L', so move left to (1, 0).
 * At cell (1, 0), the character is 'U', which indicates moving to (0, 0).
 * However, (0, 0) has already been visited, so this move is not made.
 * Therefore, the process ends at the last visited cell (1, 0), so the
 * answer is [1, 0].
 *
 * Example 2:
 *
 * Input: grid = [["R","R","R"]]
 * Output: [0,2]
 * Explanation:
 * Start at cell (0, 0), which contains 'R', so move right to (0, 1).
 * At cell (0, 1), the character is 'R', so move right to (0, 2).
 * At cell (0, 2), the character is 'R', which indicates moving to (0, 3).
 * However, (0, 3) is outside the grid, so this move is not made.
 * Therefore, the process ends at the last visited cell (0, 2), so the
 * answer is [0, 2].
 *
 *
 * Constraints:
 *
 * 1 <= m == grid.length <= 10^5
 * 1 <= n == grid[i].length <= 10^5
 * m * n <= 10^5
 * grid[i][j] is one of 'L', 'R', 'U', or 'D'.
 *
 */
public class LastVisitedCellInADirectionalGrid {

    // V0
    // IDEA: SIMULATION + VISITED GRID
    //       follow the arrows from (0, 0); the CURRENT cell is always the last visited one,
    //       so the answer is wherever we stand when the next move is refused (out of the
    //       grid, or into a visited cell). every step visits a NEW cell -> at most m * n steps.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[] lastVisitedCell(char[][] grid) {
        // edge case: empty grid has no cell to stand on
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return new int[0];
        }

        int rows = grid.length;
        int cols = grid[0].length;

        // m * n <= 10^5, so a boolean grid is cheaper than a set of coordinates
        boolean[][] visited = new boolean[rows][cols];
        int r = 0;
        int c = 0;
        visited[r][c] = true;

        while (true) {
            // NOTE: rows grow DOWNWARD, so 'U' is row - 1 and 'D' is row + 1
            int nextR = r;
            int nextC = c;
            char move = grid[r][c];
            if (move == 'U') {
                nextR = r - 1;
            } else if (move == 'D') {
                nextR = r + 1;
            } else if (move == 'L') {
                nextC = c - 1;
            } else {
                nextC = c + 1;
            }

            // the move would leave the grid -> not made, stop here
            if (nextR < 0 || nextR >= rows || nextC < 0 || nextC >= cols) {
                return new int[] { r, c };
            }

            // the move would revisit a cell -> not made, stop here
            if (visited[nextR][nextC]) {
                return new int[] { r, c };
            }

            r = nextR;
            c = nextC;
            visited[r][c] = true;
        }
    }
}
