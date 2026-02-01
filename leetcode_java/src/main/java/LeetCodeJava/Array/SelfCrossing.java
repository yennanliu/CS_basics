package LeetCodeJava.Array;

// https://leetcode.com/problems/self-crossing/description/

import java.util.HashSet;
import java.util.Set;

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

    // V0-0-1
    // TODO: fix below
//    public boolean isSelfCrossing(int[] distance) {
//        // edge
//        if(distance == null || distance.length == 0){
//            return false; // ??
//        }
//        if(distance.length < 4){
//            return false;
//        }
//
//        // set ( x-y )
//        Set<String> set = new HashSet<>();
//        int x = 0;
//        int y = 0;
//        for(int i = 0; i < distance.length; i++){
//
//            int val = distance[i];
//
//            int[] res = new int[]{x, y};
//
//            // north
//            if(i % 4 == 0){
//                for(int j = 0; j < val; j++){
//                    res[1] += val;
//                    String str_val = res[0] + "-" + res[1];
//                    if(set.contains(str_val)){
//                        return true;
//                    }
//                    set.add(str_val);
//                }
//
//                y = res[1];
//
//            }
//            // west
//            else if(i % 4 == 1){
//                for(int j = 0; j < val; j++){
//                    res[0] -= val;
//                    String str_val = res[0] + "-" + res[1];
//                    if(set.contains(str_val)){
//                        return true;
//                    }
//                    set.add(str_val);
//                }
//
//                x = res[0];
//            }
//
//            // north
//            else if(i % 4 == 2){
//                for(int j = 0; j < val; j++){
//                    res[1] -= val;
//                    String str_val = res[0] + "-" + res[1];
//                    if(set.contains(str_val)){
//                        return true;
//                    }
//                    set.add(str_val);
//                }
//
//                y = res[1];
//            }
//            // east
//            else{
//                for(int j = 0; j < val; j++){
//                    res[0] += val;
//                    String str_val = res[0] + "-" + res[1];
//                    if(set.contains(str_val)){
//                        return true;
//                    }
//                    set.add(str_val);
//                }
//
//                x = res[0];
//            }
//
//        }
//
//        return false;
//    }

    // V0-1
    // IDEA: ARRAY OP (fixed by gpt)
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean isSelfCrossing_0_1(int[] distance) {
        if (distance == null || distance.length < 4)
            return false;

        for (int i = 3; i < distance.length; i++) {
            // Case 1: current line crosses the line 3 steps ahead of it
            // Case 1: Basic “spiral inward” crossing.
            if (distance[i] >= distance[i - 2] && distance[i - 1] <= distance[i - 3]) {
                return true;
            }

            // Case 2: current line overlaps the line 4 steps ahead of it
            // Case 2: When the 4th line overlays the first.
            if (i >= 4) {
                if (distance[i - 1] == distance[i - 3] &&
                        distance[i] + distance[i - 4] >= distance[i - 2]) {
                    return true;
                }
            }

            // Case 3: current line crosses the line 5 steps ahead of it
            // Case 3: Complex inward spiral with a twist back.
            if (i >= 5) {
                if (distance[i - 2] >= distance[i - 4] &&
                        distance[i] >= distance[i - 2] - distance[i - 4] &&
                        distance[i - 1] >= distance[i - 3] - distance[i - 5] &&
                        distance[i - 1] <= distance[i - 3]) {
                    return true;
                }
            }
        }

        return false;
    }

    // V0-2
    // IDEA: geometric crossing rules, math (gpt)
    /**
     *  NOTE !!!
     *
     *   At step i, the path crosses if:
     *    1) 	Case 1: current line crosses the line 3 steps back
     *
     *            i >= 3 && distance[i] >= distance[i-2] && distance[i-1] <= distance[i-3]
     *
     *    2)	Case 2: current line overlaps the line 4 steps back
     *
     *          i >= 4 && distance[i-1] == distance[i-3] && distance[i] + distance[i-4] >= distance[i-2]
     *
     *    3) Case 3: current line crosses the line 6 steps back
     *
     *       i >= 5 && distance[i-2] >= distance[i-4] &&
     *            distance[i] + distance[i-4] >= distance[i-2] &&
     *            distance[i-1] <= distance[i-3] &&
     *            distance[i-1] + distance[i-5] >= distance[i-3]
     *
     */
    public boolean isSelfCrossing_0_2(int[] distance) {
        if (distance == null || distance.length < 4) {
            return false;
        }

        for (int i = 3; i < distance.length; i++) {
            // Case 1: current line crosses the line 3 steps ahead
            if (distance[i] >= distance[i - 2] && distance[i - 1] <= distance[i - 3]) {
                return true;
            }
            // Case 2: current line overlaps the line 4 steps ahead
            if (i >= 4 && distance[i - 1] == distance[i - 3] &&
                    distance[i] + distance[i - 4] >= distance[i - 2]) {
                return true;
            }
            // Case 3: current line crosses the line 6 steps ahead
            if (i >= 5 && distance[i - 2] >= distance[i - 4] &&
                    distance[i] + distance[i - 4] >= distance[i - 2] &&
                    distance[i - 1] <= distance[i - 3] &&
                    distance[i - 1] + distance[i - 5] >= distance[i - 3]) {
                return true;
            }
        }

        return false;
    }

    // V1

    // V2
    // https://leetcode.com/problems/self-crossing/solutions/1018879/java-arm-leg-and-swirl-by-orc-dev-5a9e/
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
    /**
     * time = O(N)
     * space = O(1)
     */
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
