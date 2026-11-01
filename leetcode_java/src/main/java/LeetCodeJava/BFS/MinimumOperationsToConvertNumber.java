package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-operations-to-convert-number/description/

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 2059. Minimum Operations to Convert Number
 * Medium
 *
 * You are given a 0-indexed integer array nums containing distinct numbers, an integer start, and an integer goal. There is an integer x that is initially set to start, and you want to perform operations on x such that it is converted to goal. You can perform the following operation repeatedly on the number x:
 *
 * If 0 <= x <= 1000, then for any index i in the array (0 <= i < nums.length), you can set x to any of the following:
 *
 * x + nums[i]
 * x - nums[i]
 * x ^ nums[i] (bitwise-XOR)
 *
 * Note that you can use each nums[i] any number of times in any order. Operations that set x to be out of the range 0 <= x <= 1000 are valid, but no more operations can be done afterward.
 *
 * Return the minimum number of operations needed to convert x = start into goal, and -1 if it is not possible.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,12], start = 2, goal = 12
 * Output: 2
 * Explanation: We can go from 2 -> 14 -> 12 with the following 2 operations.
 * - 2 + 12 = 14
 * - 14 - 2 = 12
 *
 * Example 2:
 *
 * Input: nums = [3,5,7], start = 0, goal = -4
 * Output: 2
 * Explanation: We can go from 0 -> 3 -> -4 with the following 2 operations.
 * - 0 + 3 = 3
 * - 3 - 7 = -4
 * Note that the last operation sets x out of the range 0 <= x <= 1000, which is valid.
 *
 * Example 3:
 *
 * Input: nums = [2,8,16], start = 0, goal = 1
 * Output: -1
 * Explanation: There is no way to convert 0 into 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * -10^9 <= nums[i], goal <= 10^9
 * 0 <= start <= 1000
 * start != goal
 * All the integers in nums are distinct.
 *
 */
public class MinimumOperationsToConvertNumber {

    // V0
    // IDEA: BFS OVER THE 1001 OPERABLE STATES (each operation = one edge)
    //   only a value in [0, 1000] can be operated on again, so those 1001 values
    //   are the whole state space and plain BFS gives the fewest operations.
    //   A value OUTSIDE [0, 1000] is still a legal FINAL value, so `next == goal`
    //   is tested before the range check (Example 2 ends at -4).
    /**
     * time = O(1000 * N)
     * space = O(1000)
     */
    public int minimumOperations(int[] nums, int start, int goal) {
        final int LIMIT = 1000;
        boolean[] visited = new boolean[LIMIT + 1];
        visited[start] = true;

        // queue holds values still inside [0, 1000]; steps = BFS layer
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(start);
        int steps = 0;

        while (!queue.isEmpty()) {
            steps++;
            int layerSize = queue.size();
            for (int i = 0; i < layerSize; i++) {
                int x = queue.poll();
                for (int num : nums) {
                    // long: x + num can pass the int range when num is near 10^9
                    long[] candidates = { (long) x + num, (long) x - num, (long) (x ^ num) };
                    for (long next : candidates) {
                        // reaching goal ends the search, even outside [0, 1000]
                        if (next == goal) {
                            return steps;
                        }
                        // only in-range values may be operated on again
                        if (next >= 0 && next <= LIMIT && !visited[(int) next]) {
                            visited[(int) next] = true;
                            queue.offer((int) next);
                        }
                    }
                }
            }
        }
        return -1;
    }
}
