package LeetCodeJava.HashTable;

// https://leetcode.com/problems/path-crossing/description/

import java.util.*;

/**
 * 1496. Path Crossing
 * Easy
 *
 * Given a string path, where path[i] = 'N', 'S', 'E' or 'W', each representing moving one unit north, south, east, or west, respectively. You start at the origin (0, 0) on a 2D plane and walk on the path specified by path.
 *
 * Return true if the path crosses itself at any point, that is, if at any time you are on a location you have previously visited. Return false otherwise.
 *
 *
 * Example 1:
 *
 * Input: path = "NES"
 * Output: false
 * Explanation: Notice that the path doesn't cross any point more than once.
 *
 * Example 2:
 *
 * Input: path = "NESWW"
 * Output: true
 * Explanation: Notice that the path visits the origin twice.
 *
 *
 * Constraints:
 *
 * 1 <= path.length <= 10^4
 * path[i] is either 'N', 'S', 'E', or 'W'.
 *
 */
public class PathCrossing {

    // V0
    // IDEA: HASH SET OF VISITED COORDINATES
    //       walk the path step by step; the moment a coordinate repeats, the path crosses.
    //       NOTE: the origin is visited before the first move, so returning to it counts.
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPathCrossing(String path) {
        Set<String> visited = new HashSet<>();
        int x = 0;
        int y = 0;
        visited.add(x + "," + y);

        for (char move : path.toCharArray()) {
            if (move == 'N') {
                y++;
            } else if (move == 'S') {
                y--;
            } else if (move == 'E') {
                x++;
            } else {
                x--;
            }
            // add() returns false when the point was already there
            if (!visited.add(x + "," + y)) {
                return true;
            }
        }
        return false;
    }
}
