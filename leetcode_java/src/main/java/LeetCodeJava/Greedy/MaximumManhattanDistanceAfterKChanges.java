package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-manhattan-distance-after-k-changes/description/

/**
 * 3443. Maximum Manhattan Distance After K Changes
 * Medium
 *
 * You are given a string s consisting of the characters 'N', 'S', 'E', and 'W',
 * where s[i] indicates movements in an infinite grid:
 *
 * 'N' : Move north by 1 unit.
 * 'S' : Move south by 1 unit.
 * 'E' : Move east by 1 unit.
 * 'W' : Move west by 1 unit.
 *
 * Initially, you are at the origin (0, 0). You can change at most k characters to
 * any of the four directions.
 *
 * Find the maximum Manhattan distance from the origin that can be achieved at any
 * time while performing the movements in order.
 * The Manhattan Distance between two cells (x_i, y_i) and (x_j, y_j) is |x_i -
 * x_j| + |y_i - y_j|.
 *
 * Example 1:
 *
 * Input: s = "NWSE", k = 1
 *
 * Output: 3
 *
 * Explanation:
 *
 * Change s[2] from 'S' to 'N'. The string s becomes "NWNE".
 *
 *   Movement | Position (x, y) | Manhattan Distance | Maximum
 *   s[0] == 'N' | (0, 1) | 0 + 1 = 1 | 1
 *   s[1] == 'W' | (-1, 1) | 1 + 1 = 2 | 2
 *   s[2] == 'N' | (-1, 2) | 1 + 2 = 3 | 3
 *   s[3] == 'E' | (0, 2) | 0 + 2 = 2 | 3
 *
 * The maximum Manhattan distance from the origin that can be achieved is 3. Hence,
 * 3 is the output.
 *
 * Example 2:
 *
 * Input: s = "NSWWEW", k = 3
 *
 * Output: 6
 *
 * Explanation:
 *
 * Change s[1] from 'S' to 'N', and s[4] from 'E' to 'W'. The string s becomes
 * "NNWWWW".
 *
 * The maximum Manhattan distance from the origin that can be achieved is 6. Hence,
 * 6 is the output.
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 0 <= k <= s.length
 * s consists of only 'N', 'S', 'E', and 'W'.
 *
 */
public class MaximumManhattanDistanceAfterKChanges {

    // V0
    // IDEA: GREEDY PER PREFIX — EACH CHANGE IS WORTH +2, CAPPED BY THE STEPS TAKEN
    //       after i moves the honest distance is d = |x| + |y|. flipping a wasted step
    //       (one that moves against the dominant direction) gains 2, and the distance
    //       can never exceed i. so the best at prefix i is min(i, d + 2k).
    //       answer = max over every prefix (the same k budget applies to each moment).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maxDistance(String s, int k) {
        int x = 0;
        int y = 0;
        int best = 0;

        for (int i = 0; i < s.length(); i++) {
            char move = s.charAt(i);
            if (move == 'N') {
                y++;
            } else if (move == 'S') {
                y--;
            } else if (move == 'E') {
                x++;
            } else {
                x--;
            }
            int stepsTaken = i + 1;
            int reachable = Math.min(stepsTaken, Math.abs(x) + Math.abs(y) + 2 * k);
            best = Math.max(best, reachable);
        }
        return best;
    }
}
