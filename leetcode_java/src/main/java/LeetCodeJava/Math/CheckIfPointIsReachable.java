package LeetCodeJava.Math;

// https://leetcode.com/problems/check-if-point-is-reachable/description/

/**
 * 2543. Check if Point Is Reachable
 * Hard
 *
 * There exists an infinitely large grid. You are currently at point (1, 1), and you need to reach the point (targetX, targetY) using a finite number of steps.
 *
 * In one step, you can move from point (x, y) to any one of the following points:
 *
 * (x, y - x)
 * (x - y, y)
 * (2 * x, y)
 * (x, 2 * y)
 *
 * Given two integers targetX and targetY representing the X-coordinate and Y-coordinate of your final position, return true if you can reach the point from (1, 1) using some number of steps, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: targetX = 6, targetY = 9
 * Output: false
 *
 * Example 2:
 *
 * Input: targetX = 4, targetY = 7
 * Output: true
 * Explanation: You can follow the path (1,1) -> (1,2) -> (1,4) -> (1,8) -> (1,7) -> (2,7) -> (4,7).
 *
 *
 * Constraints:
 *
 * 1 <= targetX, targetY <= 10^9
 *
 */
public class CheckIfPointIsReachable {

    // V0
    // IDEA: NUMBER THEORY -- subtraction keeps gcd, doubling can only multiply it by 2, so reachable iff gcd is a power of 2
    /**
     * time = O(log(min(X, Y)))
     * space = O(1)
     */
    public boolean isReachable(int targetX, int targetY) {
        int g = gcd(targetX, targetY);
        // strip every factor of 2; a power of two ends at exactly 1
        while (g % 2 == 0) {
            g /= 2;
        }
        return g == 1;
    }

    /** Euclid's algorithm. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
