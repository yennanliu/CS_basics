package LeetCodeJava.Math;

// https://leetcode.com/problems/count-total-number-of-colored-cells/description/

/**
 * 2579. Count Total Number of Colored Cells
 * Medium
 *
 * There exists an infinitely large two-dimensional grid of uncolored unit cells.
 * You are given a positive integer n, indicating that you must do the following routine for n minutes:
 *
 * At the first minute, color any arbitrary unit cell blue.
 * Every minute thereafter, color blue every uncolored cell that touches a blue cell.
 *
 * Return the number of colored cells at the end of n minutes.
 *
 *
 * Example 1:
 *
 * Input: n = 1
 * Output: 1
 *
 * Example 2:
 *
 * Input: n = 2
 * Output: 5
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 *
 */
public class CountTotalNumberOfColoredCells {

    // V0
    // IDEA: CLOSED FORM -- diamond of radius n - 1: columns 1, 3, ..., 2n-1, ..., 3, 1 sum to 2 * n * (n - 1) + 1
    /**
     * time = O(1)
     * space = O(1)
     */
    public long coloredCells(int n) {
        // ~2 * 10^10 for n = 10^5 -> long
        long size = n;
        return 2 * size * (size - 1) + 1;
    }
}
