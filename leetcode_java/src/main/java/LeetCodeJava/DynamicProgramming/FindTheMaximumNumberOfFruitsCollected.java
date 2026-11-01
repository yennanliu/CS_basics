package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-maximum-number-of-fruits-collected/description/

import java.util.Arrays;

/**
 * 3363. Find the Maximum Number of Fruits Collected
 * Hard
 *
 * There is a game dungeon comprised of n x n rooms arranged in a grid.
 *
 * You are given a 2D array fruits of size n x n, where fruits[i][j] represents the number of
 * fruits in the room (i, j). Three children will play in the game dungeon, with initial positions
 * at corner rooms (0, 0), (0, n - 1), and (n - 1, 0).
 *
 * The children will make exactly n - 1 moves according to the following rules to reach the room
 * (n - 1, n - 1):
 *
 * The child starting from (0, 0) must move from their current room (i, j) to one of the rooms
 * (i + 1, j + 1), (i + 1, j), and (i, j + 1) if the target room exists.
 * The child starting from (0, n - 1) must move from their current room (i, j) to one of the rooms
 * (i + 1, j - 1), (i + 1, j), and (i + 1, j + 1) if the target room exists.
 * The child starting from (n - 1, 0) must move from their current room (i, j) to one of the rooms
 * (i - 1, j + 1), (i, j + 1), and (i + 1, j + 1) if the target room exists.
 *
 * When a child enters a room, they will collect all the fruits there. If two or more children
 * enter the same room, only one child will collect the fruits, and the room will be emptied after
 * they leave.
 *
 * Return the maximum number of fruits the children can collect from the dungeon.
 *
 *
 * Example 1:
 *
 * Input: fruits = [[1,2,3,4],[5,6,8,7],[9,10,11,12],[13,14,15,16]]
 * Output: 100
 * Explanation:
 * The 1st child (from the top left corner) moves on this path (0,0) -> (1,1) -> (2,2) -> (3,3).
 * The 2nd child (from the top right corner) moves on this path (0,3) -> (1,2) -> (2,3) -> (3,3).
 * The 3rd child (from the bottom left corner) moves on this path (3,0) -> (3,1) -> (3,2) -> (3,3).
 * In total they collect 1 + 6 + 11 + 16 + 4 + 8 + 12 + 13 + 14 + 15 = 100 fruits.
 *
 * Example 2:
 *
 * Input: fruits = [[1,1],[1,1]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 2 <= n == fruits.length == fruits[i].length <= 1000
 * 0 <= fruits[i][j] <= 1000
 *
 */
public class FindTheMaximumNumberOfFruitsCollected {

    private static final int UNREACHABLE = Integer.MIN_VALUE;

    // V0
    // IDEA: DIAGONAL CHILD IS FORCED + TWO MIRRORED GRID DPs (ONE ABOVE, ONE BELOW THE DIAGONAL)
    /**
     *  The child from (0, 0) needs n - 1 moves to reach (n-1, n-1), so every move must be
     *  (i+1, j+1): its path is the main diagonal, a fixed sum.
     *
     *  The child from (0, n-1) stays strictly above the diagonal and the one from (n-1, 0)
     *  strictly below, so the two are independent. The lower child is the upper child on the
     *  transposed grid, so one DP routine serves both.
     *
     *  DP def:
     *     - dp[i][j] = max fruits collected reaching (i, j) from (0, n-1)
     *  DP eq:
     *     - dp[i][j] = grid[i][j] + max(dp[i-1][j-1], dp[i-1][j], dp[i-1][j+1]),  j > i
     *  Answer: diagonal sum + upper(fruits) + upper(transpose(fruits))
     *
     *  time = O(N^2)
     *  space = O(N^2)
     */
    public int maxCollectedFruits(int[][] fruits) {
        int n = fruits.length;

        // the first child walks the diagonal
        int diagonalSum = 0;
        for (int i = 0; i < n; i++) {
            diagonalSum += fruits[i][i];
        }

        // the third child is the mirror image of the second across the diagonal
        int[][] transposed = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                transposed[i][j] = fruits[j][i];
            }
        }

        return diagonalSum + bestUpperPath(fruits) + bestUpperPath(transposed);
    }

    /** Best walk from (0, n-1) down to (n-2, n-1) staying strictly right of the diagonal. */
    private int bestUpperPath(int[][] grid) {
        int n = grid.length;
        int[][] dp = new int[n][n];
        for (int[] row : dp) {
            Arrays.fill(row, UNREACHABLE);
        }
        dp[0][n - 1] = grid[0][n - 1];

        // the last hop lands on (n-1, n-1), already collected by the diagonal child
        for (int i = 1; i < n - 1; i++) {
            // stay right of the diagonal, and within reach of the start column
            int lowColumn = Math.max(i + 1, n - 1 - i);
            for (int j = lowColumn; j < n; j++) {
                int best = UNREACHABLE;
                for (int prevColumn = j - 1; prevColumn <= j + 1; prevColumn++) {
                    if (prevColumn >= 0 && prevColumn < n && dp[i - 1][prevColumn] > best) {
                        best = dp[i - 1][prevColumn];
                    }
                }
                if (best != UNREACHABLE) {
                    dp[i][j] = best + grid[i][j];
                }
            }
        }
        return dp[n - 2][n - 1];
    }
}
