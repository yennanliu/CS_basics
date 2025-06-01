package LeetCodeJava.Array;

// https://leetcode.com/problems/self-crossing/description/
/**
 * 335. Self Crossing
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are given an array of integers distance.
 *
 * You start at the point (0, 0) on an X-Y plane, and you move distance[0] meters to the north, then distance[1] meters to the west, distance[2] meters to the south, distance[3] meters to the east, and so on. In other words, after each move, your direction changes counter-clockwise.
 *
 * Return true if your path crosses itself or false if it does not.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: distance = [2,1,1,2]
 * Output: true
 * Explanation: The path crosses itself at the point (0, 1).
 * Example 2:
 *
 *
 * Input: distance = [1,2,3,4]
 * Output: false
 * Explanation: The path does not cross itself at any point.
 * Example 3:
 *
 *
 * Input: distance = [1,1,1,2,1]
 * Output: true
 * Explanation: The path crosses itself at the point (0, 0).
 *
 *
 * Constraints:
 *
 * 1 <= distance.length <= 105
 * 1 <= distance[i] <= 105
 *
 *
 */
public class SelfCrossing {

    // V0
//    public boolean isSelfCrossing(int[] distance) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/self-crossing/solutions/1018879/java-arm-leg-and-swirl-by-orc-dev-5a9e/
    public boolean isSelfCrossing_2(int[] x) {
        boolean arm = false;
        boolean leg = false;

        for (int i = 2; i < x.length; ++i) {
            int a = f(x, i - 2) - f(x, i - 4);
            int b = f(x, i - 2);

            if (arm && x[i] >= b)
                return true; // cross [i - 2]
            if (leg && x[i] >= a && a > 0)
                return true; // cross [i - 4]

            if (x[i] < a)
                arm = true;
            else if (x[i] <= b)
                leg = true;
        }
        return false;
    }

    private int f(int[] x, int index) {
        return (index < 0) ? 0 : x[index];
    }

    // V3
    public boolean isSelfCrossing_3(int[] x) {
        int a1, a2, a3, a4, a5;

        // if it's increasing
        boolean up = false;

        if (x.length < 4) {
            return false;
        }

        a1 = 0;
        a2 = x[0];
        a3 = x[1];
        a4 = x[2];

        if (a2 < a4) {
            up = true;
        } else {
            up = false;
        }

        for (int i = 3; i < x.length; i++) {
            a5 = x[i];

            if (!up && a5 >= a3) {
                return true;
            } else if (up && a5 <= a3) {
                // succeeded in turning into decreasing
                if (a5 + a1 < a3 || (i + 1 < x.length && x[i + 1] + a2 < a4)) {
                    up = false;
                }
                // not end yet
                else if (i + 1 < x.length) {
                    return true;
                }
            }

            a1 = a2;
            a2 = a3;
            a3 = a4;
            a4 = a5;
        }

        return false;
    }

    // V4
    // https://leetcode.com/problems/self-crossing/solutions/79168/simple-java-solution-by-munsteur-kntf/
    public boolean isSelfCrossing_4(int[] x) {
        if (x.length <= 3) {
            return false;
        }
        int i = 2;
        // keep spiraling outward
        while (i < x.length && x[i] > x[i - 2]) {
            i++;
        }
        if (i >= x.length) {
            return false;
        }
        // transition from spiraling outward to spiraling inward
        if ((i >= 4 && x[i] >= x[i - 2] - x[i - 4]) ||
                (i == 3 && x[i] == x[i - 2])) {
            x[i - 1] -= x[i - 3];
        }
        i++;
        // keep spiraling inward
        while (i < x.length) {
            if (x[i] >= x[i - 2]) {
                return true;
            }
            i++;
        }
        return false;
    }

}
