package LeetCodeJava.Stack;

// https://leetcode.com/problems/car-fleet-ii/description/

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * 1776. Car Fleet II
 * Hard
 *
 * There are n cars traveling at different speeds in the same direction along a one-lane road. You are given an array cars of length n, where cars[i] = [positioni, speedi] represents:
 *
 * positioni is the distance between the i^th car and the beginning of the road in meters. It is guaranteed that positioni < positioni+1.
 * speedi is the initial speed of the i^th car in meters per second.
 *
 * For simplicity, cars can be considered as points moving along the number line. Two cars collide when they occupy the same position. Once a car collides with another car, they unite and form a single car fleet. The cars in the formed fleet will have the same position and the same speed, which is the initial speed of the slowest car in the fleet.
 *
 * Return an array answer, where answer[i] is the time, in seconds, at which the i^th car collides with the next car, or -1 if the car does not collide with the next car. Answers within 10^-5 of the actual answers are accepted.
 *
 * Example 1:
 *
 * Input: cars = [[1,2],[2,1],[4,3],[7,2]]
 * Output: [1.00000,-1.00000,3.00000,-1.00000]
 * Explanation: After exactly one second, the first car will collide with the second car, and form a car fleet with speed 1 m/s. After exactly 3 seconds, the third car will collide with the fourth car, and form a car fleet with speed 2 m/s.
 *
 * Example 2:
 *
 * Input: cars = [[3,4],[5,4],[6,3],[9,1]]
 * Output: [2.00000,1.00000,1.50000,-1.00000]
 *
 * Constraints:
 *
 * 1 <= cars.length <= 10^5
 * 1 <= positioni, speedi <= 10^6
 * positioni < positioni+1
 *
 */
public class CarFleetII {

    private static final double NO_COLLISION = -1.0;

    // V0
    // IDEA: MONOTONIC STACK SCANNED RIGHT TO LEFT
    //   car i can only hit cars ahead of it. The stack holds the cars ahead that i might still catch.
    //   for the top candidate j:
    //     - speed[i] <= speed[j]: i never reaches j -> pop j
    //     - else t = (pos[j] - pos[i]) / (speed[i] - speed[j]); if j already merged before t
    //       (collision[j] < t), j has slowed by then -> pop j and try the next candidate
    //     - otherwise t is the answer for i
    //   collision[j] == -1 means j never collides, so its speed never changes and t is valid.
    /**
     * time = O(N) amortised
     * space = O(N)
     */
    public double[] getCollisionTimes(int[][] cars) {
        int n = cars.length;
        double[] collision = new double[n];
        Arrays.fill(collision, NO_COLLISION);
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = n - 1; i >= 0; i--) {
            int position = cars[i][0];
            int speed = cars[i][1];

            while (!stack.isEmpty()) {
                int j = stack.peek();
                int aheadPosition = cars[j][0];
                int aheadSpeed = cars[j][1];

                if (speed > aheadSpeed) {
                    double time = (double) (aheadPosition - position) / (speed - aheadSpeed);
                    // j still has its own speed at `time` -> this is i's collision
                    if (collision[j] == NO_COLLISION || time <= collision[j]) {
                        collision[i] = time;
                        break;
                    }
                }
                // j is unreachable, or gone (merged into a slower car) before i gets there
                stack.pop();
            }
            stack.push(i);
        }
        return collision;
    }
}
