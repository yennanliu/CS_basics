package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-total-distance-traveled/description/

import java.util.*;

/**
 * 2463. Minimum Total Distance Traveled
 * Hard
 *
 * There are some robots and factories on the X-axis. You are given an integer array robot where
 * robot[i] is the position of the ith robot. You are also given a 2D integer array factory where
 * factory[j] = [position_j, limit_j] indicates that position_j is the position of the jth factory
 * and that it can repair at most limit_j robots.
 *
 * The positions of each robot are unique. The positions of each factory are also unique. Note
 * that a robot can be in the same position as a factory initially.
 *
 * All the robots are initially broken; they keep moving in one direction. The direction could be
 * the negative or the positive direction of the X-axis. When a robot reaches a factory that did
 * not reach its limit, the factory repairs the robot, and it stops moving.
 *
 * At any moment, you can set the initial direction of moving for some robot. Your target is to
 * minimize the total distance traveled by all the robots.
 *
 * Return the minimum total distance traveled by all the robots. The test cases are generated such
 * that all the robots can be repaired.
 *
 * Note that
 *
 * All robots move at the same speed.
 * If two robots move in the same direction, they will never collide.
 * If two robots move in opposite directions and they meet at some point, they do not collide. They are considered to move past each other.
 * If a robot passes by a factory that reached its limits, it crosses it as if it does not exist.
 * If a robot reached a factory that did not reach its limit, it will be repaired inside it, and it will not leave it.
 * Initially, all the robots are broken; they keep moving in one direction.
 *
 *
 * Example 1:
 *
 * Input: robot = [0,4,6], factory = [[2,2],[6,2]]
 * Output: 4
 * Explanation:
 * - The first robot at position 0 moves in the positive direction. It will be repaired at the first factory.
 * - The second robot at position 4 moves in the negative direction. It will be repaired at the first factory.
 * - The third robot at position 6 will be repaired at the second factory. It does not need to move.
 * The total distance is |2 - 0| + |2 - 4| + |6 - 6| = 4.
 *
 * Example 2:
 *
 * Input: robot = [1,-1], factory = [[-2,1],[2,1]]
 * Output: 2
 * Explanation:
 * - The first robot at position 1 moves in the positive direction. It will be repaired at the second factory.
 * - The second robot at position -1 moves in the negative direction. It will be repaired at the first factory.
 * The total distance is |2 - 1| + |(-2) - (-1)| = 2.
 *
 *
 * Constraints:
 *
 * 1 <= robot.length, factory.length <= 100
 * factory[j].length == 2
 * -10^9 <= robot[i], position_j <= 10^9
 * 0 <= limit_j <= robot.length
 * The test cases are generated such that all the robots can be repaired.
 *
 */
public class MinimumTotalDistanceTraveled {

    private static final long INF = Long.MAX_VALUE / 2;

    // V0
    // IDEA: SORT BOTH, DP - AN OPTIMAL ASSIGNMENT NEVER CROSSES, SO EACH FACTORY TAKES A CONTIGUOUS BLOCK OF ROBOTS
    /**
     * time = O(N * M * L)
     * space = O(N * M)
     */
    public long minimumTotalDistance(List<Integer> robot, int[][] factory) {
        // sort robots and factories by position
        List<Integer> robots = new ArrayList<>(robot);
        Collections.sort(robots);
        int[][] factories = factory.clone();
        Arrays.sort(factories, (a, b) -> Integer.compare(a[0], b[0]));

        int n = robots.size();
        int m = factories.length;

        // dp[i][j] = least distance to repair the first i robots using the first j factories
        long[][] dp = new long[n + 1][m + 1];
        for (long[] row : dp) {
            Arrays.fill(row, INF);
        }
        // base case: no robots left to repair costs nothing
        for (int j = 0; j <= m; j++) {
            dp[0][j] = 0;
        }

        for (int j = 1; j <= m; j++) {
            long position = factories[j - 1][0];
            int limit = factories[j - 1][1];
            for (int i = 1; i <= n; i++) {
                // factory j repairs nobody
                dp[i][j] = dp[i][j - 1];

                // factory j repairs the last t robots; their cost is accumulated as t grows
                long blockCost = 0;
                for (int t = 1; t <= Math.min(limit, i); t++) {
                    blockCost += Math.abs(robots.get(i - t) - position);
                    if (dp[i - t][j - 1] < INF) {
                        dp[i][j] = Math.min(dp[i][j], dp[i - t][j - 1] + blockCost);
                    }
                }
            }
        }
        return dp[n][m];
    }
}
