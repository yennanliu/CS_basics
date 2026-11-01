package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-walls-destroyed-by-robots/description/

import java.util.*;

/**
 * 3661. Maximum Walls Destroyed by Robots
 * Hard
 *
 * There is an endless straight line populated with some robots and walls. You
 * are given integer arrays robots, distance, and walls:
 *
 * robots[i] is the position of the ith robot.
 * distance[i] is the maximum distance the ith robot's bullet can travel.
 * walls[j] is the position of the jth wall.
 *
 * Every robot has one bullet that can either fire to the left or the right at
 * most distance[i] meters.
 *
 * A bullet destroys every wall in its path that lies within its range. Robots
 * are fixed obstacles: if a bullet hits another robot before reaching a wall,
 * it immediately stops at that robot and cannot continue.
 *
 * Return the maximum number of unique walls that can be destroyed by the
 * robots.
 *
 * Notes:
 *
 * A wall and a robot may share the same position; the wall can be destroyed
 * by the robot at that position.
 * Robots are not destroyed by bullets.
 *
 *
 * Example 1:
 *
 * Input: robots = [4], distance = [3], walls = [1,10]
 * Output: 1
 * Explanation:
 * robots[0] = 4 fires left with distance[0] = 3, covering [1, 4] and destroys
 * walls[0] = 1.
 * Thus, the answer is 1.
 *
 * Example 2:
 *
 * Input: robots = [10,2], distance = [5,1], walls = [5,2,7]
 * Output: 3
 * Explanation:
 * robots[0] = 10 fires left with distance[0] = 5, covering [5, 10] and
 * destroys walls[0] = 5 and walls[2] = 7.
 * robots[1] = 2 fires left with distance[1] = 1, covering [1, 2] and destroys
 * walls[1] = 2.
 * Thus, the answer is 3.
 *
 * Example 3:
 *
 * Input: robots = [1,2], distance = [100,1], walls = [10]
 * Output: 0
 * Explanation:
 * In this example, only robots[0] can reach the wall, but its shot to the
 * right is blocked by robots[1]; thus the answer is 0.
 *
 *
 * Constraints:
 *
 * 1 <= robots.length == distance.length <= 10^5
 * 1 <= walls.length <= 10^5
 * 1 <= robots[i], walls[j] <= 10^9
 * 1 <= distance[i] <= 10^5
 * All values in robots are unique
 * All values in walls are unique
 *
 */
public class MaximumWallsDestroyedByRobots {

    // V0
    // IDEA: SORT ROBOTS BY POSITION, DP OVER "WHICH WAY DID THIS ROBOT FIRE"
    //   a bullet stops at the next robot, so after sorting, a robot's range lies
    //   strictly between its neighbours:
    //     right -> [p_i, min(p_i + d_i, p_{i+1} - 1)]
    //     left  -> [max(p_i - d_i, p_{i-1} + 1), p_i]
    //   only "robot i-1 fires right, robot i fires left" can hit the same wall, so
    //   one bit of state is enough; the overlap is removed by trimming i's left range.
    //   fireLeft  = best total with robot i firing left
    //   fireRight = best total with robot i firing right
    //   wall counts in a range = two binary searches over the sorted walls.
    /**
     * time = O(N log N + M log M)
     * space = O(N + M)
     */
    public int maxWalls(int[] robots, int[] distance, int[] walls) {
        int n = robots.length;
        // robots as {position, distance}, sorted by position
        int[][] sorted = new int[n][];
        for (int i = 0; i < n; i++) {
            sorted[i] = new int[] {robots[i], distance[i]};
        }
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));
        int[] sortedWalls = walls.clone();
        Arrays.sort(sortedWalls);

        long fireLeft = 0;
        long fireRight = 0;
        for (int i = 0; i < n; i++) {
            long position = sorted[i][0];
            long reach = sorted[i][1];

            // firing right: stops before the next robot
            long rightEnd = position + reach;
            if (i + 1 < n) {
                rightEnd = Math.min(rightEnd, sorted[i + 1][0] - 1L);
            }
            long newRight = Math.max(fireLeft, fireRight) + countWalls(sortedWalls, position, rightEnd);

            // firing left: stops after the previous robot
            long leftStart = position - reach;
            long newLeft;
            if (i == 0) {
                newLeft = fireLeft + countWalls(sortedWalls, leftStart, position);
            } else {
                long prevPosition = sorted[i - 1][0];
                leftStart = Math.max(leftStart, prevPosition + 1);
                // where the previous robot's right shot ended (never past this robot)
                long prevRightEnd = Math.min(prevPosition + sorted[i - 1][1], position - 1);
                // if the previous robot fired right, skip the walls it already destroyed
                long trimmedStart = Math.max(leftStart, prevRightEnd + 1);
                long afterLeft = fireLeft + countWalls(sortedWalls, leftStart, position);
                long afterRight = fireRight + countWalls(sortedWalls, trimmedStart, position);
                newLeft = Math.max(afterLeft, afterRight);
            }
            fireLeft = newLeft;
            fireRight = newRight;
        }
        return (int) Math.max(fireLeft, fireRight);
    }

    /** Number of walls with low <= wall <= high. */
    private int countWalls(int[] sortedWalls, long low, long high) {
        if (low > high) {
            return 0;
        }
        return firstGreater(sortedWalls, high) - firstAtLeast(sortedWalls, low);
    }

    /** First index whose wall is >= target. */
    private int firstAtLeast(int[] sortedWalls, long target) {
        int left = 0;
        int right = sortedWalls.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sortedWalls[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }

    /** First index whose wall is > target. */
    private int firstGreater(int[] sortedWalls, long target) {
        int left = 0;
        int right = sortedWalls.length;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (sortedWalls[mid] <= target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return left;
    }
}
