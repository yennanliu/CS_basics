package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-pivot-integer/description/

/**
 * 2485. Find the Pivot Integer
 * Easy
 *
 * Given a positive integer n, find the pivot integer x such that:
 *
 * The sum of all elements between 1 and x inclusively equals the sum of all elements between x and n inclusively.
 *
 * Return the pivot integer x. If no such integer exists, return -1. It is guaranteed that there will be at most one pivot index for the given input.
 *
 *
 * Example 1:
 *
 * Input: n = 8
 * Output: 6
 * Explanation: 6 is the pivot integer since: 1 + 2 + 3 + 4 + 5 + 6 = 6 + 7 + 8 = 21.
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: 1
 * Explanation: 1 is the pivot integer since: 1 = 1.
 *
 * Example 3:
 *
 * Input: n = 4
 * Output: -1
 * Explanation: It can be proven that no such integer exist.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 *
 */
public class FindThePivotInteger {

    // V0
    // IDEA: ALGEBRA - 1 + ... + x == x + ... + n SIMPLIFIES TO x^2 = n(n+1)/2
    //   the pivot exists exactly when the triangular number is a perfect square.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int pivotInteger(int n) {
        int total = n * (n + 1) / 2;
        int x = (int) Math.sqrt(total);
        // correct any floating-point rounding before the exact check
        while (x * x > total) {
            x--;
        }
        while ((x + 1) * (x + 1) <= total) {
            x++;
        }
        if (x * x == total) {
            return x;
        }
        return -1;
    }

    // V1
    // IDEA: LINEAR SCAN WITH A RUNNING PREFIX SUM
    //   left sum = 1 + ... + x, right sum = total - (left sum) + x; stop when they meet.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int pivotInteger_1(int n) {
        int total = n * (n + 1) / 2;
        int leftSum = 0;
        for (int x = 1; x <= n; x++) {
            leftSum += x;
            // x is counted on both sides
            int rightSum = total - leftSum + x;
            if (leftSum == rightSum) {
                return x;
            }
        }
        return -1;
    }
}
