package LeetCodeJava.Math;

// https://leetcode.com/problems/clumsy-factorial/description/

/**
 * 1006. Clumsy Factorial
 * Medium
 *
 * The factorial of a positive integer n is the product of all positive integers less than or equal to n.
 *
 * For example, factorial(10) = 10 * 9 * 8 * 7 * 6 * 5 * 4 * 3 * 2 * 1.
 *
 * We make a clumsy factorial using the integers in decreasing order by swapping out the multiply
 * operations for a fixed rotation of operations with multiply '*', divide '/', add '+', and
 * subtract '-' in this order.
 *
 * For example, clumsy(10) = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1.
 *
 * However, these operations are still applied using the usual order of operations of arithmetic.
 * We do all multiplication and division steps before any addition or subtraction steps, and
 * multiplication and division steps are processed left to right.
 *
 * Additionally, the division that we use is floor division such that 10 * 9 / 8 = 90 / 8 = 11.
 *
 * Given an integer n, return the clumsy factorial of n.
 *
 *
 * Example 1:
 *
 * Input: n = 4
 * Output: 7
 * Explanation: 7 = 4 * 3 / 2 + 1
 *
 * Example 2:
 *
 * Input: n = 10
 * Output: 12
 * Explanation: 12 = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 *
 */
public class ClumsyFactorial {

    // V0
    // IDEA: SIMULATION, GROUP THE TERMS 4 AT A TIME
    //   clumsy(n) = (n*(n-1)/(n-2) + (n-3)) - (n-4)*(n-5)/(n-6) + (n-7) - ...
    //   the first block "a*b/c + d" is added, every later block contributes "- a*b/c + d",
    //   and a tail of fewer than 4 numbers is subtracted as a whole (1, 2*1 = 2, 3*2/1 = 6).
    //   every a*b/c has a > b > c > 0, so Java's integer division is the required floor.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int clumsy(int n) {
        // small n: the whole expression is shorter than one full block
        if (n <= 2) {
            return n;
        }
        if (n == 3) {
            return 6;
        }
        if (n == 4) {
            return 7;
        }

        // the first block is the only one whose product is added
        int result = n * (n - 1) / (n - 2) + (n - 3);
        int current = n - 4;

        // every later full block: "- a * b / c + d"
        while (current >= 4) {
            int product = current * (current - 1) / (current - 2);
            result = result - product + (current - 3);
            current -= 4;
        }

        // leftover of 1..3 numbers is one subtracted term
        if (current == 1) {
            result -= 1;
        } else if (current == 2) {
            result -= 2;
        } else if (current == 3) {
            result -= 6;
        }
        return result;
    }

    // V1
    // IDEA: O(1) FORMULA - once n >= 5 the answer depends only on n % 4
    //   (the "+ d - e * f / g" pairs cancel out to a constant per residue).
    //   good to know, but derive V0 first in an interview.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int clumsy_1(int n) {
        if (n <= 2) {
            return n;
        }
        if (n == 3) {
            return 6;
        }
        if (n == 4) {
            return 7;
        }
        int remainder = n % 4;
        if (remainder == 0) {
            return n + 1;
        }
        if (remainder == 1 || remainder == 2) {
            return n + 2;
        }
        return n - 1;
    }
}
