package LeetCodeJava.Math;

// https://leetcode.com/problems/find-the-child-who-has-the-ball-after-k-seconds/description/

/**
 * 3178. Find the Child Who Has the Ball After K Seconds
 * Easy
 *
 * You are given two positive integers n and k. There are n children numbered from 0 to n - 1 standing
 * in a queue in order from left to right.
 *
 * Initially, child 0 holds a ball and the direction of passing the ball is towards the right direction.
 * After each second, the child holding the ball passes it to the child next to them. Once the ball
 * reaches either end of the line, i.e. child 0 or child n - 1, the direction of passing is reversed.
 *
 * Return the number of the child who receives the ball after k seconds.
 *
 *
 * Example 1:
 *
 * Input: n = 3, k = 5
 * Output: 1
 *
 * Example 2:
 *
 * Input: n = 5, k = 6
 * Output: 2
 *
 * Example 3:
 *
 * Input: n = 4, k = 2
 * Output: 2
 *
 *
 * Constraints:
 *
 * 2 <= n <= 50
 * 1 <= k <= 50
 *
 */
public class FindTheChildWhoHasTheBallAfterKSeconds {

    // V0
    // IDEA: PERIODIC MOTION — PERIOD 2 * (n - 1); REFLECT THE SECOND HALF OF THE PERIOD
    /**
     * time = O(1)
     * space = O(1)
     */
    public int numberOfChild(int n, int k) {
        // a full round trip 0 -> n-1 -> 0 takes 2 * (n - 1) seconds
        int period = 2 * (n - 1);
        int offset = k % period;
        // first half: still moving right
        if (offset < n) {
            return offset;
        }
        // second half: bouncing back from n - 1
        return period - offset;
    }

    // V1
    // IDEA: SIMULATION — MOVE ONE STEP PER SECOND, FLIP DIRECTION AT EITHER END (k <= 50)
    /**
     * time = O(K)
     * space = O(1)
     */
    public int numberOfChild_1(int n, int k) {
        int position = 0;
        int direction = 1;
        for (int second = 0; second < k; second++) {
            position += direction;
            // reaching an end reverses the passing direction
            if (position == 0 || position == n - 1) {
                direction = -direction;
            }
        }
        return position;
    }
}
