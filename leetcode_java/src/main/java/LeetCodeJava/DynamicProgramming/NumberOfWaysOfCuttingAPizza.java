package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-of-cutting-a-pizza/description/

import java.util.Arrays;

/**
 * 1444. Number of Ways of Cutting a Pizza
 * Hard
 *
 * Given a rectangular pizza represented as a rows x cols matrix containing the following characters: 'A' (an apple) and '.' (empty cell) and given the integer k. You have to cut the pizza into k pieces using k-1 cuts.
 *
 * For each cut you choose the direction: vertical or horizontal, then you choose a cut position at the cell boundary and cut the pizza into two pieces. If you cut the pizza vertically, give the left part of the pizza to a person. If you cut the pizza horizontally, give the upper part of the pizza to a person. Give the last piece of pizza to the last person.
 *
 * Return the number of ways of cutting the pizza such that each piece contains at least one apple. Since the answer can be a huge number, return this modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: pizza = ["A..","AAA","..."], k = 3
 * Output: 3
 * Explanation: The figure above shows the three ways to cut the pizza. Note that pieces must contain at least one apple.
 *
 * Example 2:
 *
 * Input: pizza = ["A..","AA.","..."], k = 3
 * Output: 1
 *
 * Example 3:
 *
 * Input: pizza = ["A..","A..","..."], k = 1
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= rows, cols <= 50
 * rows == pizza.length
 * cols == pizza[i].length
 * 1 <= k <= 10
 * pizza consists of characters 'A' and '.' only.
 *
 */
public class NumberOfWaysOfCuttingAPizza {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: 2D PREFIX SUM + MEMOIZED DFS (state = remaining bottom-right block)
    //       every cut gives away the top or the left strip, so what is left is always the
    //       block (row, col) .. (rows-1, cols-1) -> the state is (row, col, cutsLeft).
    //       dfs(row, col, 0) = 1 if the block holds an apple, else 0
    //       dfs(row, col, c) = sum over horizontal cuts x > row whose top strip has an apple
    //                          of dfs(x, col, c-1), plus the same for vertical cuts y > col.
    //       apples[i][j] (prefix sum) answers "does this strip hold an apple" in O(1).
    /**
     * time = O(R * C * K * (R + C))
     * space = O(R * C * K)
     */
    public int ways(String[] pizza, int k) {
        int rows = pizza.length;
        int cols = pizza[0].length();

        // apples[i][j] = number of apples in rows [0, i) and cols [0, j)
        int[][] apples = new int[rows + 1][cols + 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int here = pizza[i].charAt(j) == 'A' ? 1 : 0;
                apples[i + 1][j + 1] = apples[i][j + 1] + apples[i + 1][j] - apples[i][j] + here;
            }
        }

        // memo[row][col][cuts] = ways to cut block (row, col) with `cuts` cuts left; -1 = unknown
        long[][][] memo = new long[rows][cols][k];
        for (long[][] plane : memo) {
            for (long[] line : plane) {
                Arrays.fill(line, -1);
            }
        }
        return (int) countWays(0, 0, k - 1, rows, cols, apples, memo);
    }

    /** Ways to make `cuts` more cuts on the block whose top-left corner is (row, col). */
    private long countWays(int row, int col, int cuts, int rows, int cols, int[][] apples, long[][][] memo) {
        // no cuts left: the remaining block is the last piece and must hold an apple
        if (cuts == 0) {
            return applesIn(apples, row, col, rows, cols) > 0 ? 1 : 0;
        }
        if (memo[row][col][cuts] != -1) {
            return memo[row][col][cuts];
        }

        long total = 0;
        // horizontal cut above row x: rows [row, x) are given away
        for (int x = row + 1; x < rows; x++) {
            if (applesIn(apples, row, col, x, cols) > 0) {
                total += countWays(x, col, cuts - 1, rows, cols, apples, memo);
            }
        }
        // vertical cut left of column y: cols [col, y) are given away
        for (int y = col + 1; y < cols; y++) {
            if (applesIn(apples, row, col, rows, y) > 0) {
                total += countWays(row, y, cuts - 1, rows, cols, apples, memo);
            }
        }

        memo[row][col][cuts] = total % MOD;
        return memo[row][col][cuts];
    }

    /** Apples in rows [r1, r2) and cols [c1, c2). */
    private int applesIn(int[][] apples, int r1, int c1, int r2, int c2) {
        return apples[r2][c2] - apples[r1][c2] - apples[r2][c1] + apples[r1][c1];
    }
}
