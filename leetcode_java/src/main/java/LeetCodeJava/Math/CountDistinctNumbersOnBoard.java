package LeetCodeJava.Math;

// https://leetcode.com/problems/count-distinct-numbers-on-board/description/

/**
 * 2549. Count Distinct Numbers on Board
 * Easy
 *
 * You are given a positive integer n, that is initially placed on a board. Every day, for 10^9 days, you perform the following procedure:
 *
 * For each number x present on the board, find all numbers 1 <= i <= n such that x % i == 1.
 * Then, place those numbers on the board.
 *
 * Return the number of distinct integers present on the board after 10^9 days have elapsed.
 *
 * Note:
 *
 * Once a number is placed on the board, it will remain on it until the end.
 * % stands for the modulo operation. For example, 14 % 3 is 2.
 *
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: 4
 * Explanation: At the end of a billion days, the distinct numbers on the board will be 2, 3, 4, and 5.
 *
 * Example 2:
 *
 * Input: n = 3
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 *
 */
public class CountDistinctNumbersOnBoard {

    // V0
    // IDEA: MATH -- x % (x - 1) == 1 for every x >= 3, so n cascades down to 2; board ends as {2..n}, except n = 1
    /**
     * time = O(1)
     * space = O(1)
     */
    public int distinctIntegers(int n) {
        // n = 1 can never add anything (1 % 1 == 0), so the board stays {1}
        if (n == 1) {
            return 1;
        }
        return n - 1;
    }
}
