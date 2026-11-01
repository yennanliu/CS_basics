package LeetCodeJava.Graph;

// https://leetcode.com/problems/flower-planting-with-no-adjacent/description/

import java.util.*;

/**
 * 1042. Flower Planting With No Adjacent
 * Medium
 *
 * You have n gardens, labeled from 1 to n, and an array paths where
 * paths[i] = [xi, yi] describes a bidirectional path between garden xi to garden yi.
 * In each garden, you want to plant one of 4 types of flowers.
 *
 * All gardens have at most 3 paths coming into or leaving it.
 *
 * Your task is to choose a flower type for each garden such that, for any two gardens
 * connected by a path, they have different types of flowers.
 *
 * Return any such a choice as an array answer, where answer[i] is the type of flower
 * planted in the (i+1)th garden. The flower types are denoted 1, 2, 3, or 4.
 * It is guaranteed an answer exists.
 *
 *
 * Example 1:
 *
 * Input: n = 3, paths = [[1,2],[2,3],[3,1]]
 * Output: [1,2,3]
 * Explanation:
 * Gardens 1 and 2 have different types.
 * Gardens 2 and 3 have different types.
 * Gardens 3 and 1 have different types.
 * Hence, [1,2,3] is a valid answer. Other valid answers include [1,2,4], [1,4,2], and [3,2,1].
 *
 * Example 2:
 *
 * Input: n = 4, paths = [[1,2],[3,4]]
 * Output: [1,2,1,2]
 *
 * Example 3:
 *
 * Input: n = 4, paths = [[1,2],[2,3],[3,4],[4,1],[1,3],[2,4]]
 * Output: [1,2,3,4]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 * 0 <= paths.length <= 2 * 10^4
 * paths[i].length == 2
 * 1 <= xi, yi <= n
 * xi != yi
 * Every garden has at most 3 paths coming into or leaving it.
 *
 */
public class FlowerPlantingWithNoAdjacent {

    // V0
    // IDEA: GREEDY GRAPH COLORING
    // every garden has degree <= 3 and there are 4 colors, so when garden i is
    // colored its neighbours use at most 3 colors -> one of {1,2,3,4} is always free.
    // No backtracking is needed.
    /**
     * time = O(N + E)
     * space = O(N + E)
     */
    public int[] gardenNoAdj(int n, int[][] paths) {
        // node = garden (1-indexed), edge = a bidirectional path
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] path : paths) {
            graph.get(path[0]).add(path[1]);
            graph.get(path[1]).add(path[0]);
        }

        // color[i] = flower type of garden i, 0 = not planted yet
        int[] color = new int[n + 1];
        for (int garden = 1; garden <= n; garden++) {
            // mark the colors the neighbours already use
            boolean[] used = new boolean[5];
            for (int neighbour : graph.get(garden)) {
                used[color[neighbour]] = true;
            }
            // take the first free color
            for (int flower = 1; flower <= 4; flower++) {
                if (!used[flower]) {
                    color[garden] = flower;
                    break;
                }
            }
        }

        // shift to 0-indexed answer
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = color[i + 1];
        }
        return answer;
    }
}
