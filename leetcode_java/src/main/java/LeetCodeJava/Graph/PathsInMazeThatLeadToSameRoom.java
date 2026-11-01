package LeetCodeJava.Graph;

// https://leetcode.com/problems/paths-in-maze-that-lead-to-same-room/description/

import java.util.*;

/**
 * 2077. Paths in Maze That Lead to Same Room
 * Medium
 *
 * A maze consists of n rooms numbered from 1 to n, and some rooms are connected by corridors. You are given
 * a 2D integer array corridors where corridors[i] = [room1_i, room2_i] indicates that there is a corridor
 * connecting room1_i and room2_i, allowing a person in the maze to go from room1_i to room2_i and vice versa.
 *
 * The designer of the maze wants to know how confusing the maze is. The confusion score of the maze is the
 * number of different cycles of length 3.
 *
 * For example, 1 -> 2 -> 3 -> 1 is a cycle of length 3, but 1 -> 2 -> 3 -> 4 and 1 -> 2 -> 3 -> 2 -> 1 are not.
 *
 * Two cycles are considered to be different if one or more of the rooms visited in the first cycle is not in
 * the second cycle.
 *
 * Return the confusion score of the maze.
 *
 *
 * Example 1:
 *
 * Input: n = 5, corridors = [[1,2],[5,2],[4,1],[2,4],[3,1],[3,4]]
 * Output: 2
 * Explanation:
 * One cycle of length 3 is 4 -> 1 -> 3 -> 4.
 * Another cycle of length 3 is 1 -> 2 -> 4 -> 1.
 * Note that 4 -> 2 -> 1 -> 4 is the same cycle as 1 -> 2 -> 4 -> 1, so it is not counted again.
 *
 * Example 2:
 *
 * Input: n = 4, corridors = [[1,2],[3,4]]
 * Output: 0
 * Explanation: There are no cycles of length 3.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 1 <= corridors.length <= 5 * 10^4
 * corridors[i].length == 2
 * 1 <= room1_i, room2_i <= n
 * room1_i != room2_i
 * There are no duplicate corridors.
 *
 */
public class PathsInMazeThatLeadToSameRoom {

    // V0
    // IDEA: COUNT TRIANGLES BY INTERSECTING THE NEIGHBOUR SETS OF EACH EDGE
    // a length-3 cycle is a triangle. For every edge (u, v), each common neighbour w closes
    // one triangle {u, v, w}. Each triangle is found once per edge, i.e. 3 times -> divide by 3.
    // Iterating the smaller neighbour set keeps each intersection at O(min degree).
    /**
     * time = O(E * N) worst case
     * space = O(N + E)
     */
    public int numberOfPaths(int n, int[][] corridors) {
        List<Set<Integer>> neighbours = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            neighbours.add(new HashSet<>());
        }
        for (int[] corridor : corridors) {
            neighbours.get(corridor[0]).add(corridor[1]);
            neighbours.get(corridor[1]).add(corridor[0]);
        }

        long triangleCorners = 0;
        for (int[] corridor : corridors) {
            Set<Integer> setU = neighbours.get(corridor[0]);
            Set<Integer> setV = neighbours.get(corridor[1]);
            // walk the smaller set, probe the larger one
            Set<Integer> smaller = setU.size() <= setV.size() ? setU : setV;
            Set<Integer> larger = smaller == setU ? setV : setU;
            for (int room : smaller) {
                if (larger.contains(room)) {
                    triangleCorners++;
                }
            }
        }
        return (int) (triangleCorners / 3);
    }
}
