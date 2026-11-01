package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-distance-after-road-addition-queries-i/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 3243. Shortest Distance After Road Addition Queries I
 * Medium
 *
 * You are given an integer n and a 2D integer array queries.
 *
 * There are n cities numbered from 0 to n - 1. Initially, there is a unidirectional road from city i to city i + 1 for all 0 <= i < n - 1.
 *
 * queries[i] = [u_i, v_i] represents the addition of a new unidirectional road from city u_i to city v_i. After each query, you need to find the length of the shortest path from city 0 to city n - 1.
 *
 * Return an array answer where for each i in the range [0, queries.length - 1], answer[i] is the length of the shortest path from city 0 to city n - 1 after processing the first i + 1 queries.
 *
 *
 * Example 1:
 *
 * Input: n = 5, queries = [[2,4],[0,2],[0,4]]
 * Output: [3,2,1]
 * Explanation:
 * After the addition of the road from 2 to 4, the length of the shortest path from 0 to 4 is 3.
 * After the addition of the road from 0 to 2, the length of the shortest path from 0 to 4 is 2.
 * After the addition of the road from 0 to 4, the length of the shortest path from 0 to 4 is 1.
 *
 * Example 2:
 *
 * Input: n = 4, queries = [[0,3],[0,2]]
 * Output: [1,1]
 * Explanation:
 * After the addition of the road from 0 to 3, the length of the shortest path from 0 to 3 is 1.
 * After the addition of the road from 0 to 2, the length of the shortest path remains 1.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 500
 * 1 <= queries.length <= 500
 * queries[i].length == 2
 * 0 <= queries[i][0] < queries[i][1] < n
 * 1 < queries[i][1] - queries[i][0]
 * There are no repeated roads among the queries.
 *
 */
public class ShortestDistanceAfterRoadAdditionQueriesI {

    // V0
    // IDEA: EVERY ROAD COSTS 1 -> RE-RUN BFS FROM CITY 0 AFTER EACH ADDITION
    //   n and the query count are both <= 500, so q BFS runs over at most
    //   n + q edges is cheap. (The sequel, LC 3244, needs a smarter structure.)
    /**
     * time = O(Q * (N + Q))
     * space = O(N + Q)
     */
    public int[] shortestDistanceAfterQueries(int n, int[][] queries) {
        // one-way roads i -> i + 1
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i + 1 < n; i++) {
            graph.get(i).add(i + 1);
        }

        int[] answer = new int[queries.length];
        for (int qi = 0; qi < queries.length; qi++) {
            graph.get(queries[qi][0]).add(queries[qi][1]);
            answer[qi] = shortestPath(graph, n);
        }
        return answer;
    }

    /** BFS distance from city 0 to city n - 1. */
    private int shortestPath(List<List<Integer>> graph, int n) {
        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        distance[0] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int city = queue.poll();
            // BFS reaches each city first along a shortest path
            if (city == n - 1) {
                break;
            }
            for (int next : graph.get(city)) {
                if (distance[next] == -1) {
                    distance[next] = distance[city] + 1;
                    queue.offer(next);
                }
            }
        }
        return distance[n - 1];
    }
}
