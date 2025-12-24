package LeetCodeJava.Greedy;

// https://leetcode.com/problems/find-center-of-star-graph/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 1791. Find Center of Star Graph
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There is an undirected star graph consisting of n nodes labeled from 1 to n. A star graph is a graph where there is one center node and exactly n - 1 edges that connect the center node with every other node.
 *
 * You are given a 2D integer array edges where each edges[i] = [ui, vi] indicates that there is an edge between the nodes ui and vi. Return the center of the given star graph.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: edges = [[1,2],[2,3],[4,2]]
 * Output: 2
 * Explanation: As shown in the figure above, node 2 is connected to every other node, so 2 is the center.
 * Example 2:
 *
 * Input: edges = [[1,2],[5,1],[1,3],[1,4]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * 3 <= n <= 105
 * edges.length == n - 1
 * edges[i].length == 2
 * 1 <= ui, vi <= n
 * ui != vi
 * The given edges represent a valid star graph.
 *
 *
 */
public class FindCenterOfStarGraph {

    // V0
    // IDEA: HASHMAP + HASHSET
    public int findCenter(int[][] edges) {
        // edge

        Set<Integer> set = new HashSet<>();

        // { val : cnt}
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] e : edges) {
            int ui = e[0];
            int vi = e[1];
            if (!map.containsKey(ui)) {
                map.put(ui, new HashSet<>());
            }
            if (!map.containsKey(vi)) {
                map.put(vi, new HashSet<>());
            }

            Set<Integer> set1 = map.get(ui);
            Set<Integer> set2 = map.get(vi);

            set1.add(vi);
            set2.add(ui);

            map.put(ui, set1);
            map.put(vi, set2);

            set.add(ui);
            set.add(vi);
        }

        //System.out.println(">>> map = " + map);
        int n = set.size();
        for (int k : map.keySet()) {
            if (map.get(k).size() == n - 1) {
                return k;
            }
        }

        return -1;
    }

    // V1-1
    // https://leetcode.com/problems/find-center-of-star-graph/editorial/
    // IDEA: DEGREE COUNT
    public int findCenter_1_1(int[][] edges) {
        Map<Integer, Integer> degree = new HashMap<>();

        for (int[] edge : edges) {
            degree.put(edge[0], degree.getOrDefault(edge[0], 0) + 1);
            degree.put(edge[1], degree.getOrDefault(edge[1], 0) + 1);
        }

        for (int node : degree.keySet()) {
            if (degree.get(node) == edges.length) {
                return node;
            }
        }

        return -1;
    }


    // V1-2
    // https://leetcode.com/problems/find-center-of-star-graph/editorial/
    // IDEA: GREEDY
    public int findCenter_1_2(int[][] edges) {
        int[] firstEdge = edges[0];
        int[] secondEdge = edges[1];

        return (firstEdge[0] == secondEdge[0] || firstEdge[0] == secondEdge[1])
                ? firstEdge[0]
                : firstEdge[1];
    }


    // V2
    // https://leetcode.com/problems/find-center-of-star-graph/solutions/1108319/o1-1-liner-by-votrubac-4ekx/
    public int findCenter_2(int[][] e) {
        return e[0][0] == e[1][0] || e[0][0] == e[1][1] ? e[0][0] : e[0][1];
    }

    // V3

}
