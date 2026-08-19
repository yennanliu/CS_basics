package LeetCodeJava.Stack;

// https://leetcode.com/problems/asteroid-collision/

import java.util.ArrayList;
import java.util.List;

/**
 *  735. Asteroid Collision
 *  Medium
 *
 *  We are given an array asteroids of integers representing asteroids in a row.
 *  For each asteroid, the absolute value represents its size, and the sign represents
 *  its direction (positive = right, negative = left). Each asteroid moves at the same speed.
 *
 *  Find out the state of the asteroids after all collisions. If two asteroids meet, the
 *  smaller one explodes. If both are the same size, both explode. Two asteroids moving
 *  in the same direction never meet.
 *
 *  Example 1:
 *  Input: asteroids = [5,10,-5]
 *  Output: [5,10]
 *
 *  Example 2:
 *  Input: asteroids = [8,-8]
 *  Output: []
 *
 *  Example 3:
 *  Input: asteroids = [10,2,-5]
 *  Output: [10]
 *
 *  Constraints:
 *  2 <= asteroids.length <= 10^4
 *  -1000 <= asteroids[i] <= 1000
 *  asteroids[i] != 0
 */
public class AsteroidCollision {

    // V0
    // IDEA: STACK — a collision only happens when the incoming asteroid goes left (< 0)
    //       and the stack top goes right (> 0); resolve repeatedly until it survives or dies
    /**
     * time = O(n)
     * space = O(n)
     */
    public int[] asteroidCollision(int[] asteroids) {

        int[] stack = new int[asteroids.length];
        int size = 0;

        for (int cur : asteroids) {
            boolean alive = true;
            while (alive && cur < 0 && size > 0 && stack[size - 1] > 0) {
                if (stack[size - 1] < -cur) {
                    size--;               // the right-moving one explodes, cur keeps going
                } else if (stack[size - 1] == -cur) {
                    size--;               // both explode
                    alive = false;
                } else {
                    alive = false;        // cur explodes
                }
            }
            if (alive) {
                stack[size] = cur;
                size++;
            }
        }

        int[] res = new int[size];
        System.arraycopy(stack, 0, res, 0, size);
        return res;
    }
}
