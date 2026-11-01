package LeetCodeJava.Heap;

// https://leetcode.com/problems/furthest-building-you-can-reach/description/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * 1642. Furthest Building You Can Reach
 * Medium
 *
 * You are given an integer array heights representing the heights of buildings, some
 * bricks, and some ladders.
 *
 * You start your journey from building 0 and move to the next building by possibly
 * using bricks or ladders.
 *
 * While moving from building i to building i+1 (0-indexed),
 *
 * If the current building's height is greater than or equal to the next building's
 * height, you do not need a ladder or bricks.
 * If the current building's height is less than the next building's height, you can
 * either use one ladder or (h[i+1] - h[i]) bricks.
 *
 * Return the furthest building index (0-indexed) you can reach if you use the given
 * ladders and bricks optimally.
 *
 *
 * Example 1:
 *
 * Input: heights = [4,2,7,6,9,14,12], bricks = 5, ladders = 1
 * Output: 4
 * Explanation: Starting at building 0, you can follow these steps:
 * - Go to building 1 without using ladders nor bricks since 4 >= 2.
 * - Go to building 2 using 5 bricks. You must use either bricks or ladders because
 *   2 < 7.
 * - Go to building 3 without using ladders nor bricks since 7 >= 6.
 * - Go to building 4 using your only ladder. You must use either bricks or ladders
 *   because 6 < 9.
 * It is impossible to go beyond building 4 because you do not have any more bricks or
 * ladders.
 *
 * Example 2:
 *
 * Input: heights = [4,12,2,7,3,18,20,3,19], bricks = 10, ladders = 2
 * Output: 7
 *
 * Example 3:
 *
 * Input: heights = [14,3,19,3], bricks = 17, ladders = 0
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= heights.length <= 10^5
 * 1 <= heights[i] <= 10^6
 * 0 <= bricks <= 10^9
 * 0 <= ladders <= heights.length
 *
 */
public class FurthestBuildingYouCanReach {

    // V0
    // IDEA: MIN-HEAP OF THE CLIMBS THAT GOT A LADDER (ladders go to the biggest climbs seen so far)
    // hand a ladder to EVERY climb as it appears; once more ladders are handed out than we own,
    // take the ladder back from the smallest climb in the heap and pay bricks for that one instead.
    // the heap always holds the `ladders` largest climbs of the prefix walked = the optimal assignment.
    /**
     * time = O(N log L)   (L = ladders; the heap never holds more than L + 1 climbs)
     * space = O(L)
     */
    public int furthestBuilding(int[] heights, int bricks, int ladders) {
        // edge case: nothing to walk
        if (heights == null || heights.length == 0) {
            return 0;
        }

        // climbs currently covered by a ladder, smallest on top
        PriorityQueue<Integer> ladderClimbs = new PriorityQueue<>();
        int bricksLeft = bricks;

        for (int i = 0; i < heights.length - 1; i++) {
            int climb = heights[i + 1] - heights[i];
            // going down or staying flat is free
            if (climb <= 0) {
                continue;
            }

            ladderClimbs.add(climb);
            if (ladderClimbs.size() > ladders) {
                // the smallest ladder climb is the cheapest one to pay for in bricks
                bricksLeft -= ladderClimbs.poll();
                if (bricksLeft < 0) {
                    // could not cross from i to i + 1, so i is the furthest building reached
                    return i;
                }
            }
        }
        return heights.length - 1;
    }

    // V1
    // IDEA: MAX-HEAP OF THE CLIMBS PAID IN BRICKS (the mirror image of V0)
    // pay bricks for every climb; when bricks run out, swap the LARGEST brick-paid climb for a ladder
    // (refunding its bricks). stop when bricks are short and no ladder is left.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int furthestBuilding_1(int[] heights, int bricks, int ladders) {
        if (heights == null || heights.length == 0) {
            return 0;
        }

        // climbs currently paid with bricks, largest on top
        PriorityQueue<Integer> brickClimbs = new PriorityQueue<>(Collections.reverseOrder());
        int bricksLeft = bricks;
        int laddersLeft = ladders;

        for (int i = 0; i < heights.length - 1; i++) {
            int climb = heights[i + 1] - heights[i];
            if (climb <= 0) {
                continue;
            }

            bricksLeft -= climb;
            brickClimbs.add(climb);

            if (bricksLeft < 0) {
                // out of bricks: a ladder on the largest brick climb refunds the most bricks
                if (laddersLeft == 0) {
                    return i;
                }
                bricksLeft += brickClimbs.poll();
                laddersLeft -= 1;
            }
        }
        return heights.length - 1;
    }
}
