package LeetCodeJava.Math;

// https://leetcode.com/problems/determine-if-a-cell-is-reachable-at-a-given-time/description/

/**
 * 2849. Determine if a Cell Is Reachable at a Given Time
 * Medium
 *
 * You are given four integers sx, sy, fx, fy, and a non-negative integer t.
 *
 * In an infinite 2D grid, you start at the cell (sx, sy). Each second, you must move to any of its adjacent cells.
 *
 * Return true if you can reach cell (fx, fy) after exactly t seconds, or false otherwise.
 *
 * A cell's adjacent cells are the 8 cells around it that share at least one corner with it. You can visit the same cell several times.
 *
 *
 * Example 1:
 *
 * Input: sx = 2, sy = 4, fx = 7, fy = 7, t = 6
 * Output: true
 * Explanation: Starting at cell (2, 4), we can reach cell (7, 7) in exactly 6 seconds by going through the cells depicted in the picture above.
 *
 * Example 2:
 *
 * Input: sx = 3, sy = 1, fx = 7, fy = 3, t = 3
 * Output: false
 * Explanation: Starting at cell (3, 1), it takes at least 4 seconds to reach cell (7, 3) by going through the cells depicted in the picture above. Hence, we cannot reach cell (7, 3) at the third second.
 *
 *
 * Constraints:
 *
 * 1 <= sx, sy, fx, fy <= 10^9
 * 0 <= t <= 10^9
 *
 */
public class DetermineIfACellIsReachableAtAGivenTime {

    // V0
    // IDEA: MATH (Chebyshev distance + one degenerate case)
    //       with 8-directional moves the fewest steps is max(|dx|, |dy|), and any extra time
    //       can be burnt with a detour on the infinite grid. the one exception: start == end
    //       with t == 1, since we must move every second and cannot come back in one step.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isReachableAtTime(int sx, int sy, int fx, int fy, int t) {
        if (sx == fx && sy == fy) {
            // t == 0: stay put; t >= 2: step away and back
            return t != 1;
        }
        int dx = Math.abs(sx - fx);
        int dy = Math.abs(sy - fy);
        return Math.max(dx, dy) <= t;
    }
}
