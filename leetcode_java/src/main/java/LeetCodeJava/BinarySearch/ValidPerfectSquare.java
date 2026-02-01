package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/valid-perfect-square/
/**
 * 367. Valid Perfect Square
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given a positive integer num, return true if num is a perfect square or false otherwise.
 *
 * A perfect square is an integer that is the square of an integer. In other words, it is the product of some integer with itself.
 *
 * You must not use any built-in library function, such as sqrt.
 *
 *
 *
 * Example 1:
 *
 * Input: num = 16
 * Output: true
 * Explanation: We return true because 4 * 4 = 16 and 4 is an integer.
 * Example 2:
 *
 * Input: num = 14
 * Output: false
 * Explanation: We return false because 3.742 * 3.742 = 14 and 3.742 is not an integer.
 *
 *
 * Constraints:
 *
 * 1 <= num <= 231 - 1
 */
public class ValidPerfectSquare {

    // V0
    // IDEA : BINARY SEARCH
    /**
     * time = O(log N)
     * space = O(1)
     */
    public boolean isPerfectSquare(int num) {
        //edge
        if (num < 0) {
            return false;
        }
        if (num <= 1) {
            return true;
        }

        int l = 0;
        int r = (int) Math.sqrt(num) + 1; // ??
//        System.out.println(">>> l = " + l +
//                ", r = " + r);

        // find
        while (r >= l) {
            int mid = (l + r) / 2;
            int square = mid * mid;
            if (square == num) {
                return true;
            }
            if (square < num) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }

        return false;
    }

    // V0-1
    // IDEA : BINARY SEARCH
    public boolean isPerfectSquare_0_1(int num) {

        if (num < 2) {
            return true;
        }

        long left = 2;
        long right = num / 2; // NOTE !!!, "long right = num;" is OK as well
        long x;
        long guessSquared;

        while (left <= right) {
            x = (left + right) / 2;
            guessSquared = x * x;
            if (guessSquared == num) {
                return true;
            }
            if (guessSquared > num) {
                right = x - 1;
            } else {
                left = x + 1;
            }
        }
        return false;
    }

    // V1
    // IDEA : BINARY SEARCH
    // https://leetcode.com/problems/valid-perfect-square/editorial/
    /**
     * time = O(log N)
     * space = O(1)
     */
    public boolean isPerfectSquare_1(int num) {
        if (num < 2) {
            return true;
        }

        long left = 2, right = num / 2, x, guessSquared;
        while (left <= right) {
            x = left + (right - left) / 2;
            guessSquared = x * x;
            if (guessSquared == num) {
                return true;
            }
            if (guessSquared > num) {
                right = x - 1;
            } else {
                left = x + 1;
            }
        }
        return false;
    }



}
