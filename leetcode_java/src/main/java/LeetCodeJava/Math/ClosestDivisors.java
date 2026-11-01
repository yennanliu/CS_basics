package LeetCodeJava.Math;

// https://leetcode.com/problems/closest-divisors/description/

/**
 * 1362. Closest Divisors
 * Medium
 *
 * Given an integer num, find the closest two integers in absolute difference whose product equals num + 1 or num + 2.
 *
 * Return the two integers in any order.
 *
 *
 * Example 1:
 *
 * Input: num = 8
 * Output: [3,3]
 * Explanation: For num + 1 = 9, the closest divisors are 3 & 3, for num + 2 = 10, the closest divisors are 2 & 5, hence 3 & 3 is chosen.
 *
 * Example 2:
 *
 * Input: num = 123
 * Output: [5,25]
 *
 * Example 3:
 *
 * Input: num = 999
 * Output: [40,25]
 *
 *
 * Constraints:
 *
 * 1 <= num <= 10^9
 *
 */
public class ClosestDivisors {

    // V0
    // IDEA: ENUMERATE DIVISORS DOWN FROM sqrt(x)
    //   for a fixed product x, the closest pair (i, x / i) uses the LARGEST divisor i <= sqrt(x),
    //   so walk i down from sqrt(x) and stop at the first divisor (i = 1 always divides).
    //   do it for x = num + 1 and x = num + 2, keep the closer pair.
    /**
     * time = O(sqrt(N))
     * space = O(1)
     */
    public int[] closestDivisors(int num) {
        int[] first = closestPair(num + 1);
        int[] second = closestPair(num + 2);
        if (Math.abs(first[0] - first[1]) <= Math.abs(second[0] - second[1])) {
            return first;
        }
        return second;
    }

    /** The divisor pair of x whose two factors are closest to each other. */
    private int[] closestPair(int x) {
        // integer sqrt: start from the double estimate and correct it in both directions
        int root = (int) Math.sqrt(x);
        while ((long) root * root > x) {
            root--;
        }
        while ((long) (root + 1) * (root + 1) <= x) {
            root++;
        }

        for (int i = root; i >= 1; i--) {
            if (x % i == 0) {
                return new int[] {i, x / i};
            }
        }
        return new int[] {1, x};
    }
}
