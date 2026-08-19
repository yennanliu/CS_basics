package LeetCodeJava.Math;

// https://leetcode.com/problems/mirror-reflection/

/**
 *  858. Mirror Reflection
 *  Medium
 *
 *  There is a special square room with mirrors on each of the four walls.
 *  Except for the southwest corner, there are receptors on each of the
 *  remaining corners, numbered 0, 1, and 2.
 *
 *  The square room has walls of length p and a laser ray from the southwest
 *  corner first meets the east wall at a distance q from the 0th receptor.
 *
 *  Given the two integers p and q, return the number of the receptor that the
 *  ray meets first.
 *
 *  Example 1:
 *   Input: p = 2, q = 1
 *   Output: 2
 *   Explanation: the ray meets receptor 2 the first time it gets reflected back
 *                to the left wall.
 *
 *  Example 2:
 *   Input: p = 3, q = 1
 *   Output: 1
 *
 *  Constraints:
 *   - 1 <= q <= p <= 1000
 *   - It is guaranteed that the given value of q is not equal to 0 and p is not
 *     divisible by q.
 */
public class MirrorReflection {

    // V0
    // IDEA: unfold the reflections -> find smallest k with k*q = m*p.
    //       Reduce p, q by 2 while both even; then
    //       (odd, odd) -> 1, (even, odd) -> 0, (odd, even) -> 2
    /**
     * time = O(log p)
     * space = O(1)
     */
    public int mirrorReflection(int p, int q) {
        while (p % 2 == 0 && q % 2 == 0) {
            p /= 2;
            q /= 2;
        }
        if (p % 2 == 0) {
            // p even, q odd
            return 2;
        }
        if (q % 2 == 0) {
            // p odd, q even
            return 0;
        }
        return 1;
    }

    // V1
    // IDEA: same reduction expressed with lowbit (p & -p) vs (q & -q).
    /**
     * time = O(1)
     * space = O(1)
     */
    public int mirrorReflection_1(int p, int q) {
        int lp = p & (-p);
        int lq = q & (-q);
        if (lp > lq) {
            return 2;
        }
        if (lp < lq) {
            return 0;
        }
        return 1;
    }
}
