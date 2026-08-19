package LeetCodeJava.Math;

// https://leetcode.com/problems/escape-the-ghosts/

/**
 *  789. Escape The Ghosts
 *  Medium
 *
 *  You are playing a simplified PAC-MAN game on an infinite 2-D grid. You
 *  start at the point [0, 0], and you are given a destination point
 *  target = [xtarget, ytarget] that you are trying to get to. There are
 *  several ghosts on the map with their starting positions given as a 2-D
 *  array ghosts, where ghosts[i] = [xi, yi] represents the starting position
 *  of the ith ghost. All inputs are integral coordinates.
 *
 *  Each turn, you and all the ghosts may independently choose to either move
 *  1 unit in any of the four cardinal directions: north, east, south, west,
 *  or stay still. All actions happen simultaneously.
 *
 *  You escape if and only if you can reach the target before any ghost reaches
 *  you. If you reach any square (including the target) at the same time as a
 *  ghost, it does not count as an escape.
 *
 *  Return true if it is possible to escape regardless of how the ghosts move,
 *  otherwise return false.
 *
 *  Example 1:
 *    Input: ghosts = [[1,0],[0,3]], target = [0,1]
 *    Output: true
 *
 *  Example 2:
 *    Input: ghosts = [[1,0]], target = [2,0]
 *    Output: false
 *
 *  Constraints:
 *   - 1 <= ghosts.length <= 100
 *   - ghosts[i].length == 2
 *   - -10^4 <= xi, yi <= 10^4
 *   - target.length == 2
 *   - -10^4 <= xtarget, ytarget <= 10^4
 */
public class EscapeTheGhosts {

    // V0
    // IDEA: MANHATTAN DISTANCE.
    //       A ghost can always intercept you if it can reach the target no
    //       later than you can. So you escape iff YOUR distance to the target
    //       is strictly smaller than EVERY ghost's distance to the target.
    /**
     * time = O(n), n = ghosts.length
     * space = O(1)
     */
    public boolean escapeGhosts(int[][] ghosts, int[] target) {

        int myDist = Math.abs(target[0]) + Math.abs(target[1]);

        for (int[] g : ghosts) {
            int ghostDist = Math.abs(g[0] - target[0]) + Math.abs(g[1] - target[1]);
            if (ghostDist <= myDist) {
                return false;
            }
        }

        return true;
    }
}
