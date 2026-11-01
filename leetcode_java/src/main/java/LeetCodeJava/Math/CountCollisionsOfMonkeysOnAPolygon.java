package LeetCodeJava.Math;

// https://leetcode.com/problems/count-collisions-of-monkeys-on-a-polygon/description/

/**
 * 2550. Count Collisions of Monkeys on a Polygon
 * Medium
 *
 * There is a regular convex polygon with n vertices. The vertices are labeled from 0 to n - 1 in a clockwise direction, and each vertex has exactly one monkey.
 *
 * Simultaneously, each monkey moves to a neighboring vertex. A collision happens if at least two monkeys reside on the same vertex after the movement or intersect on an edge.
 *
 * Return the number of ways the monkeys can move so that at least one collision happens. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 3
 * Output: 6
 *
 * Example 2:
 *
 * Input: n = 4
 * Output: 14
 *
 *
 * Constraints:
 *
 * 3 <= n <= 10^9
 *
 */
public class CountCollisionsOfMonkeysOnAPolygon {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: COMPLEMENT COUNTING -- 2^n patterns, only "all clockwise" / "all counter-clockwise" are collision-free -> 2^n - 2
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int monkeyMove(int n) {
        long total = modPow(2, n);
        // + MOD keeps the result non-negative when 2^n mod p is 0 or 1
        return (int) ((total - 2 + MOD) % MOD);
    }

    /** Computes base^exp mod MOD by repeated squaring. */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
}
