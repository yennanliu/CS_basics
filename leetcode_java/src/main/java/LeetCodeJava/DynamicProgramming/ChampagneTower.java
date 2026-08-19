package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/champagne-tower/

/**
 *  799. Champagne Tower
 *  Medium
 *
 *  We stack glasses in a pyramid, where the first row has 1 glass, the second
 *  row has 2 glasses, and so on until the 100th row. Each glass holds one cup
 *  of champagne.
 *
 *  Then, some champagne is poured into the first glass at the top. When the
 *  topmost glass is full, any excess liquid poured will fall equally to the
 *  glass immediately to the left and right of it. When those glasses become
 *  full, any excess champagne will fall equally to the left and right of those
 *  glasses, and so on. (A glass at the bottom row has its excess champagne fall
 *  on the floor.)
 *
 *  Now after pouring some non-negative integer cups of champagne, return how
 *  full the j-th glass in the i-th row is (both i and j are 0-indexed).
 *
 *  Example 1:
 *    Input: poured = 1, query_row = 1, query_glass = 1
 *    Output: 0.00000
 *
 *  Example 2:
 *    Input: poured = 2, query_row = 1, query_glass = 1
 *    Output: 0.50000
 *
 *  Constraints:
 *    - 0 <= poured <= 10^9
 *    - 0 <= query_glass <= query_row < 100
 */
public class ChampagneTower {

    // V0
    // IDEA: simulate row by row - overflow of a glass splits evenly to the 2 glasses below
    /**
     * time = O(R^2)
     * space = O(R)
     */
    public double champagneTower(int poured, int query_row, int query_glass) {
        double[] row = new double[query_row + 2];
        row[0] = poured;

        for (int r = 0; r < query_row; r++) {
            double[] next = new double[query_row + 2];
            for (int c = 0; c <= r; c++) {
                double overflow = (row[c] - 1.0) / 2.0;
                if (overflow > 0) {
                    next[c] += overflow;
                    next[c + 1] += overflow;
                }
            }
            row = next;
        }
        return Math.min(1.0, row[query_glass]);
    }
}
