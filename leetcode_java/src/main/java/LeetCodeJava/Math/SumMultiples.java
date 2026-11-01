package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-multiples/description/

/**
 * 2652. Sum Multiples
 * Easy
 *
 * Given a positive integer n, find the sum of all integers in the range [1, n] inclusive that are divisible by 3, 5, or 7.
 *
 * Return an integer denoting the sum of all numbers in the given range satisfying the constraint.
 *
 *
 * Example 1:
 *
 * Input: n = 7
 * Output: 21
 *
 * Example 2:
 *
 * Input: n = 10
 * Output: 40
 *
 * Example 3:
 *
 * Input: n = 9
 * Output: 30
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^3
 *
 */
public class SumMultiples {

    // V0
    // IDEA: DIRECT SCAN -- n <= 1000; the "or" counts a number like 15 only once
    /**
     * time = O(N)
     * space = O(1)
     */
    public int sumOfMultiples(int n) {
        int total = 0;
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                total += i;
            }
        }
        return total;
    }

    // V1
    // IDEA: INCLUSION-EXCLUSION -- f(3) + f(5) + f(7) - f(15) - f(21) - f(35) + f(105),
    //       where f(d) = sum of multiples of d up to n = d * m * (m + 1) / 2 with m = n / d
    /**
     * time = O(1)
     * space = O(1)
     */
    public int sumOfMultiples_1(int n) {
        return sumOfMultiplesOf(3, n) + sumOfMultiplesOf(5, n) + sumOfMultiplesOf(7, n)
                - sumOfMultiplesOf(15, n) - sumOfMultiplesOf(21, n) - sumOfMultiplesOf(35, n)
                + sumOfMultiplesOf(105, n);
    }

    /** Sum of d, 2d, ..., md where md is the largest multiple of d not above n. */
    private int sumOfMultiplesOf(int d, int n) {
        int m = n / d;
        return d * m * (m + 1) / 2;
    }
}
