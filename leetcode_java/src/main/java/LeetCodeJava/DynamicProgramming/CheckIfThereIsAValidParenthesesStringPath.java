package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/check-if-there-is-a-valid-parentheses-string-path/description/

/**
 * 2267. Check if There Is a Valid Parentheses String Path
 * Hard
 *
 * A parentheses string is a non-empty string consisting only of '(' and ')'. It is valid if any of the following conditions is true:
 *
 * It is ().
 * It can be written as AB (A concatenated with B), where A and B are valid parentheses strings.
 * It can be written as (A), where A is a valid parentheses string.
 *
 * You are given an m x n matrix of parentheses grid. A valid parentheses string path in the grid is a path satisfying all of the following conditions:
 *
 * The path starts from the upper left cell (0, 0).
 * The path ends at the bottom-right cell (m - 1, n - 1).
 * The path only ever moves down or right.
 * The resulting parentheses string formed by the path is valid.
 *
 * Return true if there exists a valid parentheses string path in the grid. Otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: grid = [["(","(","("],[")","(",")"],["(","(",")"],["(","(",")"]]
 * Output: true
 * Explanation: The above diagram shows two possible paths that form valid parentheses strings.
 * The first path shown results in the valid parentheses string "()(())".
 * The second path shown results in the valid parentheses string "((()))".
 * Note that there may be other valid parentheses string paths.
 *
 * Example 2:
 *
 * Input: grid = [[")",")"],["(","("]]
 * Output: false
 * Explanation: The two possible paths form the parentheses strings "))(" and ")((". Since neither of them are valid parentheses strings, we return false.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 100
 * grid[i][j] is either '(' or ')'.
 *
 */
public class CheckIfThereIsAValidParenthesesStringPath {

    // V0
    // IDEA: DP OVER (ROW, COL, OPEN-BALANCE) — reachable[i][j][b] = SOME PATH TO (i, j) HAS BALANCE b
    /**
     * time = O(M * N * (M + N))
     * space = O(M * N * (M + N))
     */
    public boolean hasValidPath(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int pathLength = rows + cols - 1;

        // a valid string has even length, starts with '(' and ends with ')'
        if (pathLength % 2 == 1 || grid[0][0] == ')' || grid[rows - 1][cols - 1] == '(') {
            return false;
        }

        // the balance never needs to exceed half the path (it must come back to 0)
        int maxBalance = pathLength / 2;
        boolean[][][] reachable = new boolean[rows][cols][maxBalance + 2];
        reachable[0][0][1] = true;  // grid[0][0] is '('

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int delta = (grid[i][j] == '(') ? 1 : -1;
                for (int balance = 0; balance <= maxBalance; balance++) {
                    // balance before entering (i, j), reached from above or from the left
                    boolean fromTop = i > 0 && reachable[i - 1][j][balance];
                    boolean fromLeft = j > 0 && reachable[i][j - 1][balance];
                    if (!fromTop && !fromLeft) {
                        continue;
                    }
                    int newBalance = balance + delta;
                    // a prefix may never close more than it opened
                    if (newBalance >= 0 && newBalance <= maxBalance) {
                        reachable[i][j][newBalance] = true;
                    }
                }
            }
        }
        return reachable[rows - 1][cols - 1][0];
    }
}
