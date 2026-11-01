package LeetCodeJava.Stack;

// https://leetcode.com/problems/count-robot-groups/description/

/**
 * 4045. Count Robot Groups
 * Medium
 *
 * You are given an integer array position, sorted in increasing order, where
 * position[i] is the initial position of the ith robot at time t = 0.
 *
 * You are also given an integer array speed, where speed[i] is the constant
 * speed of the ith robot in units per second, and an integer distance.
 *
 * All robots move in the positive direction. Whenever the distance between two
 * robots or groups becomes at most distance, they merge into a single group.
 * After a merge, the resulting group takes the current position and speed of
 * the rightmost robot in that group. Once merged, robots never separate.
 *
 * Return the number of groups remaining after all possible merges have occurred.
 *
 *
 * Example 1:
 *
 * Input: position = [1,5,6,20], speed = [4,3,2,3], distance = 1
 * Output: 2
 * Explanation:
 * Robots at 5 and 6 are already within distance 1 at t = 0, so they merge and
 * the group takes the rightmost robot's state (position 6, speed 2).
 * The robot at 1 (speed 4) closes on that group at 2 units/sec and merges at
 * t = 2. The robot at 20 (speed 3) is faster than the group ahead of it, so the
 * gap only grows and it stays alone.
 *
 * Example 2:
 *
 * Input: position = [0,10], speed = [1,100], distance = 1
 * Output: 2
 * Explanation:
 * The rear robot is slower than the front one, so the gap never shrinks.
 *
 *
 * Constraints:
 *
 * 1 <= position.length == speed.length <= 10^5
 * 0 <= position[i] <= 10^9
 * position is sorted in strictly increasing order
 * 1 <= speed[i] <= 10^6
 * 0 <= distance <= 10^9
 *
 */
public class CountRobotGroups {

    // V0
    // IDEA: RIGHT -> LEFT SCAN (like LC 853 Car Fleet), COLLAPSED TO ONE VARIABLE -- NO STACK NEEDED
    //
    //   a robot can never pass the one ahead, so it can only ever merge with what is
    //   DIRECTLY ahead of it. walking right -> left, `frontSpeed` is the speed of the
    //   frontmost group that has SURVIVED so far (a merge keeps the rightmost robot's
    //   state, so joining from behind never changes it).
    //
    //   robot i is absorbed iff EITHER test fires:
    //      position[i+1] - position[i] <= distance  -> touching its neighbour at t = 0
    //      speed[i] > frontSpeed                    -> closing on the front group, so
    //                                                  the gap eventually drops to distance
    //
    //   NOTE !!! the two tests use DIFFERENT references. at t = 0 the merges are
    //            simultaneous and chain through the NEIGHBOUR, not the front group:
    //              pos = [18,19,22,24], distance = 3 -> gaps 1,3,2 all touch -> 1 group,
    //              even though 18 is 6 away from the group's position 24.
    //            the speed test is safe against the front group alone: a robot faster
    //            than a neighbour that itself caught the front is faster than the front.
    //
    //   e.g. pos = [0,10,20], speed = [5,100,1], distance = 1
    //        -> 20 survives (speed 1); 10 is faster (100 > 1) so it merges;
    //           0 is faster (5 > 1) so it merges too -> 1
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countGroups(int[] position, int[] speed, int distance) {
        // edge case: no robots
        if (position == null || position.length == 0) {
            return 0;
        }

        int n = position.length;

        // the frontmost robot is always a group on its own
        int frontSpeed = speed[n - 1];
        int groups = 1;

        for (int i = n - 2; i >= 0; i--) {
            // touching test -> the NEIGHBOUR; closing test -> the FRONT group
            boolean touchingNeighbour = position[i + 1] - position[i] <= distance;
            boolean catchesFront = speed[i] > frontSpeed;

            if (touchingNeighbour || catchesFront) {
                // merges into the group ahead; that group's state is unchanged
                continue;
            }

            // robot i never catches anything: it starts a new surviving group
            groups++;
            frontSpeed = speed[i];
        }

        return groups;
    }
}
