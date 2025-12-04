package LeetCodeJava.DFS;

// https://leetcode.com/problems/minesweeper/description/
// https://leetcode.cn/problems/minesweeper/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 529. Minesweeper
 * Solved
 * Medium
 * Topics
 * Companies
 * Let's play the minesweeper game (Wikipedia, online game)!
 *
 * You are given an m x n char matrix board representing the game board where:
 *
 * 'M' represents an unrevealed mine,
 * 'E' represents an unrevealed empty square,
 * 'B' represents a revealed blank square that has no adjacent mines (i.e., above, below, left, right, and all 4 diagonals),
 * digit ('1' to '8') represents how many mines are adjacent to this revealed square, and
 * 'X' represents a revealed mine.
 * You are also given an integer array click where click = [clickr, clickc] represents the next click position among all the unrevealed squares ('M' or 'E').
 *
 * Return the board after revealing this position according to the following rules:
 *
 * If a mine 'M' is revealed, then the game is over. You should change it to 'X'.
 * If an empty square 'E' with no adjacent mines is revealed, then change it to a revealed blank 'B' and all of its adjacent unrevealed squares should be revealed recursively.
 * If an empty square 'E' with at least one adjacent mine is revealed, then change it to a digit ('1' to '8') representing the number of adjacent mines.
 * Return the board when no more squares will be revealed.
 *
 *
 * Example 1:
 *
 *
 * Input: board = [["E","E","E","E","E"],["E","E","M","E","E"],["E","E","E","E","E"],["E","E","E","E","E"]], click = [3,0]
 * Output: [["B","1","E","1","B"],["B","1","M","1","B"],["B","1","1","1","B"],["B","B","B","B","B"]]
 * Example 2:
 *
 *
 * Input: board = [["B","1","E","1","B"],["B","1","M","1","B"],["B","1","1","1","B"],["B","B","B","B","B"]], click = [1,2]
 * Output: [["B","1","E","1","B"],["B","1","X","1","B"],["B","1","1","1","B"],["B","B","B","B","B"]]
 *
 *
 * Constraints:
 *
 * m == board.length
 * n == board[i].length
 * 1 <= m, n <= 50
 * board[i][j] is either 'M', 'E', 'B', or a digit from '1' to '8'.
 * click.length == 2
 * 0 <= clickr < m
 * 0 <= clickc < n
 * board[clickr][clickc] is either 'M' or 'E'.
 *
 *
 */
public class Minesweeper {

    // V0
//    public char[][] updateBoard(char[][] board, int[] click) {
//
//    }

    // V1
    // IDEA: DFS + ARRAY OP (GPT)
    public char[][] updateBoard_1(char[][] board, int[] click) {
        int rows = board.length;
        int cols = board[0].length;

        int x = click[0], y = click[1];

        // Edge case: 1x1 grid
        if (rows == 1 && cols == 1) {
            if (board[0][0] == 'M') {
                board[0][0] = 'X';
            } else {
                board[0][0] = 'B'; // Fix: properly set 'B' if it's 'E'
            }
            return board;
        }

        // If a mine is clicked, mark as 'X'
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
            return board;
        }

        // Otherwise, reveal cells recursively
        reveal_1(board, x, y);
        return board;
    }

    private void reveal_1(char[][] board, int x, int y) {
        int rows = board.length;
        int cols = board[0].length;

    // Boundary check and already revealed check
    /** NOTE !!!
     *
     *  - 1) 'E' represents an unrevealed empty square,
     *
     *  - 2) board[x][y] != 'E'
     *      -> ensures that we only process unrevealed empty cells ('E')
     *         and avoid unnecessary recursion.
     *
     * 	 - 3) board[x][y] != 'E'
     * 	 •	Avoids re-processing non-‘E’ cells
     * 	 •	The board can have:
     * 	    •	'M' → Mine (already handled separately)
     * 	    •	'X' → Clicked mine (game over case)
     *  	•	'B' → Blank (already processed)
     * 	    •	'1' to '8' → Number (already processed)
     * 	•	If a cell is not 'E', it means:
     * 	    •	It has already been processed
     * 	    •	It does not need further expansion
     * 	•	This prevents infinite loops and redundant checks.
     *
     *
     * 	- 4) example:
     *
     * 	   input:
     * 	        E E E
     *          E M E
     *          E E E
     *
     *   Click at (0,0)
     * 	    1.	We call reveal(board, 0, 0), which:
     * 	        •	Counts 1 mine nearby → Updates board[0][0] = '1'
     * 	        •	Does NOT recurse further, avoiding unnecessary work.
     *
     *      What If We Didn’t Check board[x][y] != 'E'?
     * 	        •	It might try to expand into already processed cells, leading to redundant computations or infinite recursion.
     *
     */
    if (x < 0 || x >= rows || y < 0 || y >= cols || board[x][y] != 'E') {
            return;
        }

        // Directions for 8 neighbors
        int[][] directions = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        // Count adjacent mines
        int mineCount = 0;
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && board[newX][newY] == 'M') {
                mineCount++;
            }
        }

        // If there are adjacent mines, show count
        if (mineCount > 0) {
            board[x][y] = (char) ('0' + mineCount);
        } else {
            // Otherwise, reveal this cell and recurse on neighbors
            board[x][y] = 'B';
            for (int[] dir : directions) {
                reveal_1(board, x + dir[0], y + dir[1]);
            }
        }
    }

    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/minesweeper/solutions/495383/java-simple-recursive-solution-with-comm-5mtf/
    public char[][] updateBoard_2(char[][] board, int[] click) {
        // once a mine is revealed, we can terminate immediately
        if (board[click[0]][click[1]] == 'M') {
            board[click[0]][click[1]] = 'X';
            return board;
        }

        reveal(board, click[0], click[1]);
        return board;
    }

    private void reveal(char[][] board, int i, int j) {
        // edge cases
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] != 'E')
            return;

        board[i][j] = '0';
        int[][] neighbors = { { i - 1, j - 1 }, { i - 1, j }, { i - 1, j + 1 },
                { i, j - 1 }, { i, j + 1 },
                { i + 1, j - 1 }, { i + 1, j }, { i + 1, j + 1 } };
        // check all neighbors for number of mines
        for (int[] neighbor : neighbors) {
            if (neighbor[0] < 0 || neighbor[1] < 0 || neighbor[0] >= board.length || neighbor[1] >= board[0].length)
                continue;
            if (board[neighbor[0]][neighbor[1]] == 'M')
                board[i][j]++;
        }

        if (board[i][j] != '0')
            return;

        // all neighbors are empty, recursively reveal them
        board[i][j] = 'B';
        for (int[] neighbor : neighbors)
            reveal(board, neighbor[0], neighbor[1]);
    }

    // V3-1
    // IDEA: BFS
    // https://leetcode.com/problems/minesweeper/solutions/1524772/java-tc-omn-sc-omn-bfs-dfs-solutions-by-n589j/
    private static final int[][] DIRS = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 },
            { -1, 1 } };

    public char[][] updateBoard_3_1(char[][] board, int[] click) {
        if (board == null || click == null) {
            throw new IllegalArgumentException("Inputs are null");
        }
        if (board[click[0]][click[1]] != 'M' && board[click[0]][click[1]] != 'E') {
            return board;
        }
        if (board[click[0]][click[1]] == 'M') {
            board[click[0]][click[1]] = 'X';
            return board;
        }

        int mines = getMinesCount(board, click[0], click[1]);
        if (mines != 0) {
            board[click[0]][click[1]] = (char) (mines + '0');
            return board;
        }
        board[click[0]][click[1]] = 'B';

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(click);
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] d : DIRS) {
                int x = cur[0] + d[0];
                int y = cur[1] + d[1];
                if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] != 'E') {
                    continue;
                }
                mines = getMinesCount(board, x, y);
                if (mines != 0) {
                    board[x][y] = (char) (mines + '0');
                    continue;
                }
                board[x][y] = 'B';
                queue.offer(new int[] { x, y });
            }
        }

        return board;
    }

    private int getMinesCount(char[][] board, int x, int y) {
        int mines = 0;
        for (int[] d : DIRS) {
            int r = x + d[0];
            int c = y + d[1];
            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == 'M') {
                mines++;
            }
        }
        return mines;
    }

    // 3-2
    // IDEA: DFS
    // https://leetcode.com/problems/minesweeper/solutions/1524772/java-tc-omn-sc-omn-bfs-dfs-solutions-by-n589j/
//    private static final int[][] DIRS = { { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, -1 }, { -1, 0 },
//            { -1, 1 } };

    public char[][] updateBoard(char[][] board, int[] click) {
        if (board == null || click == null) {
            throw new IllegalArgumentException("Inputs are null");
        }
        if (board[click[0]][click[1]] != 'M' && board[click[0]][click[1]] != 'E') {
            return board;
        }
        if (board[click[0]][click[1]] == 'M') {
            board[click[0]][click[1]] = 'X';
            return board;
        }

        revealBoard(board, click[0], click[1]);

        return board;
    }

    private void revealBoard(char[][] board, int x, int y) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] != 'E') {
            return;
        }
        int mines = getMinesCount_2(board, x, y);
        if (mines != 0) {
            board[x][y] = (char) (mines + '0');
            return;
        }
        board[x][y] = 'B';
        for (int[] d : DIRS) {
            revealBoard(board, x + d[0], y + d[1]);
        }
    }

    private int getMinesCount_2(char[][] board, int x, int y) {
        int mines = 0;
        for (int[] d : DIRS) {
            int r = x + d[0];
            int c = y + d[1];
            if (r >= 0 && r < board.length && c >= 0 && c < board[0].length && board[r][c] == 'M') {
                mines++;
            }
        }
        return mines;
    }



}
