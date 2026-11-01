package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-sideway-jumps/description/

/**
 * 1824. Minimum Sideway Jumps
 * Medium
 *
 * There is a 3 lane road of length n that consists of n + 1 points labeled from
 * 0 to n. A frog starts at point 0 in the second lane and wants to jump to point
 * n. However, there could be obstacles along the way.
 *
 * You are given an array obstacles of length n + 1 where each obstacles[i]
 * (ranging from 0 to 3) describes an obstacle on the lane obstacles[i] at point
 * i. If obstacles[i] == 0, there are no obstacles at point i. There will be at
 * most one obstacle in the 3 lanes at each point.
 *
 * For example, if obstacles[2] == 1, then there is an obstacle on lane 1 at point 2.
 *
 * The frog can only travel from point i to point i + 1 on the same lane if there
 * is not an obstacle on the lane at point i + 1. To avoid obstacles, the frog can
 * also perform a side jump to jump to another lane (even if they are not
 * adjacent) at the same point if there is no obstacle on the new lane.
 *
 * For example, the frog can jump from lane 3 at point 3 to lane 1 at point 3.
 *
 * Return the minimum number of side jumps the frog needs to reach any lane at
 * point n starting from lane 2 at point 0.
 *
 * Note: There will be no obstacles on points 0 and n.
 *
 *
 * Example 1:
 *
 * Input: obstacles = [0,1,2,3,0]
 * Output: 2
 * Explanation: There are 2 side jumps. Note that the frog can jump over
 * obstacles only when making side jumps (as shown at point 2).
 *
 * Example 2:
 *
 * Input: obstacles = [0,1,1,3,3,0]
 * Output: 0
 * Explanation: There are no obstacles on lane 2. No side jumps are required.
 *
 * Example 3:
 *
 * Input: obstacles = [0,2,1,0,3,0]
 * Output: 2
 * Explanation: There are 2 side jumps.
 *
 *
 * Constraints:
 *
 * obstacles.length == n + 1
 * 1 <= n <= 5 * 10^5
 * 0 <= obstacles[i] <= 3
 * obstacles[0] == obstacles[n] == 0
 *
 */
public class MinimumSidewayJumps {

    // larger than any reachable answer (at most n side jumps), and safe to add 1 to
    private static final int UNREACHABLE = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: ROLLING DP OVER 3 LANES (jumps[lane] = min side jumps to stand on that lane here)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minSideJumps(int[] obstacles) {
        // at point 0 the frog is on lane 2 for free; lanes 1 and 3 cost one side jump
        int[] jumps = {1, 0, 1};

        for (int point = 1; point < obstacles.length; point++) {
            int blockedLane = obstacles[point];

            // 1) walking forward into the obstacle is impossible
            if (blockedLane != 0) {
                jumps[blockedLane - 1] = UNREACHABLE;
            }

            // 2) side jumps at this point: from the cheapest lane, any free lane costs +1
            //    (must come after step 1 so the blocked lane is not used as a stepping stone)
            int cheapest = Math.min(jumps[0], Math.min(jumps[1], jumps[2]));
            for (int lane = 0; lane < 3; lane++) {
                if (lane + 1 != blockedLane) {
                    jumps[lane] = Math.min(jumps[lane], cheapest + 1);
                }
            }
        }

        return Math.min(jumps[0], Math.min(jumps[1], jumps[2]));
    }
}
