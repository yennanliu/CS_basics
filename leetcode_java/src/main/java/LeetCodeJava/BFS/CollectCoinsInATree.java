package LeetCodeJava.BFS;

// https://leetcode.com/problems/collect-coins-in-a-tree/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * 2603. Collect Coins in a Tree
 * Hard
 *
 * There exists an undirected and unrooted tree with n nodes indexed from 0 to n - 1. You are given an integer n and a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree. You are also given an array coins of size n where coins[i] can be either 0 or 1, where 1 indicates the presence of a coin in the vertex i.
 *
 * Initially, you choose to start at any vertex in the tree. Then, you can perform the following operations any number of times:
 *
 * - Collect all the coins that are at a distance of at most 2 from the current vertex, or
 * - Move to any adjacent vertex in the tree.
 *
 * Find the minimum number of edges you need to go through to collect all the coins and go back to the initial vertex.
 *
 * Note that if you pass an edge several times, you need to count it into the answer several times.
 *
 *
 * Example 1:
 *
 * Input: coins = [1,0,0,0,0,1], edges = [[0,1],[1,2],[2,3],[3,4],[4,5]]
 * Output: 2
 * Explanation: Start at vertex 2, collect the coin at vertex 0, move to vertex 3, collect the coin at vertex 5 then move back to vertex 2.
 *
 * Example 2:
 *
 * Input: coins = [0,0,0,1,1,0,0,1], edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[5,6],[5,7]]
 * Output: 2
 * Explanation: Start at vertex 0, collect the coins at vertices 4 and 3, move to vertex 2, collect the coin at vertex 7, then move back to vertex 0.
 *
 *
 * Constraints:
 *
 * n == coins.length
 * 1 <= n <= 3 * 10^4
 * 0 <= coins[i] <= 1
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= ai, bi < n
 * ai != bi
 * edges represents a valid tree.
 *
 */
public class CollectCoinsInATree {

    // V0
    // IDEA: TOPOLOGICAL LEAF PRUNING, TWO ROUNDS
    //   any route is a closed walk over a connected subtree S of length
    //   2 * (edges of S). So shrink the tree to the smallest subtree that sees
    //   every coin within distance 2:
    //   1) cascade-remove leaves with no coin (Kahn-style queue on degree-1 nodes);
    //   2) now every leaf has a coin, collectable from 2 edges away, so strip the
    //      outer leaf layer exactly twice (layer by layer, not cascading).
    //   Answer = 2 * (edges whose both endpoints survive).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int collectTheCoins(int[] coins, int[][] edges) {
        int n = coins.length;
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // 1) cascade-remove coin-free leaves
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (graph.get(i).size() == 1 && coins[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int leaf = queue.poll();
            for (int neighbour : graph.get(leaf)) {
                graph.get(neighbour).remove(leaf);
                // the neighbour may have just become a coin-free leaf itself
                if (coins[neighbour] == 0 && graph.get(neighbour).size() == 1) {
                    queue.offer(neighbour);
                }
            }
            graph.get(leaf).clear();
        }

        // 2) peel exactly two whole leaf layers (snapshot first, then delete)
        for (int round = 0; round < 2; round++) {
            List<Integer> layer = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if (graph.get(i).size() == 1) {
                    layer.add(i);
                }
            }
            for (int leaf : layer) {
                for (int neighbour : graph.get(leaf)) {
                    graph.get(neighbour).remove(leaf);
                }
                graph.get(leaf).clear();
            }
        }

        // every surviving edge is walked twice (out and back)
        int moves = 0;
        for (int[] edge : edges) {
            if (!graph.get(edge[0]).isEmpty() && !graph.get(edge[1]).isEmpty()) {
                moves += 2;
            }
        }
        return moves;
    }
}
