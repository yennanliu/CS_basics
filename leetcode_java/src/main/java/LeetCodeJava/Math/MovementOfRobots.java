package LeetCodeJava.Math;

// https://leetcode.com/problems/movement-of-robots/description/

import java.util.Arrays;

/**
 * 2731. Movement of Robots
 * Medium
 *
 * Some robots are standing on an infinite number line with their initial coordinates given by a 0-indexed integer array nums and will start moving once given the command to move. The robots will move a unit distance each second.
 *
 * You are given a string s denoting the direction in which robots will move on command. 'L' means the robot will move towards the left side or negative side of the number line, whereas 'R' means the robot will move towards the right side or positive side of the number line.
 *
 * If two robots collide, they will start moving in opposite directions.
 *
 * Return the sum of distances between all the pairs of robots d seconds after the command. Since the sum can be very large, return it modulo 10^9 + 7.
 *
 * Note:
 *
 * For two robots at the index i and j, pair (i,j) and pair (j,i) are considered the same pair.
 * When robots collide, they instantly change their directions without wasting any time.
 * Collision happens when two robots share the same place in a moment.
 *
 *
 * Example 1:
 *
 * Input: nums = [-2,0,2], s = "RLL", d = 3
 * Output: 8
 *
 * Example 2:
 *
 * Input: nums = [1,0], s = "RL", d = 2
 * Output: 5
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * -2 * 10^9 <= nums[i] <= 2 * 10^9
 * 0 <= d <= 10^9
 * nums.length == s.length
 * s consists of 'L' and 'R' only
 * nums[i] will be unique.
 *
 */
public class MovementOfRobots {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: COLLISIONS ARE A RELABELLING -- bouncing == passing through for the multiset of positions,
    //       so move every robot freely, SORT, then sum pairwise distances with a PREFIX SUM
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int sumDistance(int[] nums, String s, int d) {
        int n = nums.length;
        // final positions reach 3 * 10^9 -> long
        long[] positions = new long[n];
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'R') {
                positions[i] = (long) nums[i] + d;
            } else {
                positions[i] = (long) nums[i] - d;
            }
        }
        Arrays.sort(positions);

        // in sorted order, positions[i] is at least every earlier one, so
        // sum_{j < i} (positions[i] - positions[j]) = i * positions[i] - prefixSum
        long answer = 0;
        long prefixSum = 0;
        for (int i = 0; i < n; i++) {
            long contribution = i * positions[i] - prefixSum;
            // contribution >= 0 and fits in a long (< 10^5 * 6 * 10^9); reduce it before adding
            answer = (answer + contribution % MOD) % MOD;
            prefixSum += positions[i];
        }
        return (int) answer;
    }
}
