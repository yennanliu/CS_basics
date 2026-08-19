package LeetCodeJava.Math;

// https://leetcode.com/problems/water-and-jug-problem/

/**
 *  365. Water and Jug Problem
 *  Medium
 *
 *  You are given two jugs with capacities x and y liters. There is an infinite amount of
 *  water supply available. Determine whether it is possible to measure exactly target
 *  liters using these two jugs.
 *
 *  Operations allowed:
 *   - Fill any of the jugs completely with water.
 *   - Empty any of the jugs.
 *   - Pour water from one jug into another till the other jug is completely full,
 *     or the first jug itself is empty.
 *
 *  Example 1:
 *
 *  Input: x = 3, y = 5, target = 4
 *  Output: true
 *
 *  Example 2:
 *
 *  Input: x = 2, y = 6, target = 5
 *  Output: false
 *
 *  Constraints:
 *
 *  1 <= x, y, target <= 10^3
 */
public class WaterAndJugProblem {

    // V0
    // IDEA: Bezout's identity - target is reachable iff target <= x + y and gcd(x, y) | target
    /**
     * time = O(log(min(x, y)))
     * space = O(1)
     */
    public boolean canMeasureWater(int x, int y, int target) {
        if (target == 0) {
            return true;
        }
        if (x + y < target) {
            return false;
        }
        return target % gcd(x, y) == 0;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
