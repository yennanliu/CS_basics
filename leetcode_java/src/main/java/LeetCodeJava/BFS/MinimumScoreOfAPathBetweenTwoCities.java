package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-score-of-a-path-between-two-cities/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 2492. Minimum Score of a Path Between Two Cities
 * Medium
 *
 * You are given a positive integer n representing n cities numbered from 1 to n. You are also given a 2D array roads where roads[i] = [ai, bi, distance_i] indicates that there is a bidirectional road between cities ai and bi with a distance equal to distance_i. The cities graph is not necessarily connected.
 *
 * The score of a path between two cities is defined as the minimum distance of a road in this path.
 *
 * Return the minimum possible score of a path between cities 1 and n.
 *
 * Note:
 *
 * A path is a sequence of roads between two cities.
 * It is allowed for a path to contain the same road multiple times, and you can visit cities 1 and n multiple times along the path.
 * The test cases are generated such that there is at least one path between 1 and n.
 *
 *
 * Example 1:
 *
 * Input: n = 4, roads = [[1,2,9],[2,3,6],[2,4,5],[1,4,7]]
 * Output: 5
 * Explanation: The path from city 1 to 4 with the minimum score is: 1 -> 2 -> 4. The score of this path is min(9,5) = 5.
 * It can be shown that no other path has less score.
 *
 * Example 2:
 *
 * Input: n = 4, roads = [[1,2,2],[1,3,4],[3,4,7]]
 * Output: 2
 * Explanation: The path from city 1 to 4 with the minimum score is: 1 -> 2 -> 1 -> 3 -> 4. The score of this path is min(2,2,4,7) = 2.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= roads.length <= 10^5
 * roads[i].length == 3
 * 1 <= ai, bi <= n
 * ai != bi
 * 1 <= distance_i <= 10^4
 * There are no repeated edges.
 * There is at least one path between 1 and n.
 *
 */
public class MinimumScoreOfAPathBetweenTwoCities {

    // V0
    // IDEA: BFS OVER CITY 1's COMPONENT, TAKE THE SMALLEST EDGE SEEN
    //   roads and cities may be revisited, so any edge in the component holding
    //   city 1 (and therefore n, which is guaranteed connected) can be detoured
    //   onto. The score is just the minimum edge weight in that component.
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int minScore(int n, int[][] roads) {
        // graph.get(u) holds {neighbour, distance}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(new int[] { road[1], road[2] });
            graph.get(road[1]).add(new int[] { road[0], road[2] });
        }

        boolean[] visited = new boolean[n + 1];
        visited[1] = true;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(1);
        int minEdge = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            int city = queue.poll();
            for (int[] edge : graph.get(city)) {
                // every edge touching the component counts, visited or not
                minEdge = Math.min(minEdge, edge[1]);
                if (!visited[edge[0]]) {
                    visited[edge[0]] = true;
                    queue.offer(edge[0]);
                }
            }
        }
        return minEdge;
    }
}
