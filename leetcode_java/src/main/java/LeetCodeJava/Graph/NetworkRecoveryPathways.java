package LeetCodeJava.Graph;

// https://leetcode.com/problems/network-recovery-pathways/description/

import java.util.*;

/**
 * 3620. Network Recovery Pathways
 * Hard
 *
 * You are given a directed acyclic graph of n nodes numbered from 0 to n - 1.
 * This is represented by a 2D array edges of length m, where
 * edges[i] = [ui, vi, costi] indicates a one-way communication from node ui to
 * node vi with a recovery cost of costi.
 *
 * Some nodes may be offline. You are given a boolean array online where
 * online[i] = true means node i is online. Nodes 0 and n - 1 are always online.
 *
 * A path from 0 to n - 1 is valid if:
 *
 * All intermediate nodes on the path are online.
 * The total recovery cost of all edges on the path does not exceed k.
 *
 * For each valid path, define its score as the minimum edge-cost along that path.
 *
 * Return the maximum path score (i.e., the largest minimum-edge cost) among all
 * valid paths. If no valid path exists, return -1.
 *
 *
 * Example 1:
 *
 * Input: edges = [[0,1,5],[1,3,10],[0,2,3],[2,3,4]],
 *        online = [true,true,true,true], k = 10
 * Output: 3
 * Explanation:
 * The graph has two possible routes from node 0 to node 3:
 * 1. Path 0 -> 1 -> 3
 *    Total cost = 5 + 10 = 15, which exceeds k (15 > 10), so this path is
 *    invalid.
 * 2. Path 0 -> 2 -> 3
 *    Total cost = 3 + 4 = 7 <= k, so this path is valid.
 *    The minimum edge-cost along this path is min(3, 4) = 3.
 * There are no other valid paths. Hence, the maximum among all valid path-scores
 * is 3.
 *
 * Example 2:
 *
 * Input: edges = [[0,1,7],[1,4,5],[0,2,6],[2,3,6],[3,4,2],[2,4,6]],
 *        online = [true,true,true,false,true], k = 12
 * Output: 6
 * Explanation:
 * Node 3 is offline, so any path passing through 3 is invalid.
 * Consider the remaining routes from 0 to 4:
 * 1. Path 0 -> 1 -> 4
 *    Total cost = 7 + 5 = 12 <= k, so this path is valid.
 *    The minimum edge-cost along this path is min(7, 5) = 5.
 * 2. Path 0 -> 2 -> 3 -> 4
 *    Node 3 is offline, so this path is invalid regardless of cost.
 * 3. Path 0 -> 2 -> 4
 *    Total cost = 6 + 6 = 12 <= k, so this path is valid.
 *    The minimum edge-cost along this path is min(6, 6) = 6.
 * Among the two valid paths, their scores are 5 and 6. Therefore, the answer
 * is 6.
 *
 *
 * Constraints:
 *
 * n == online.length
 * 2 <= n <= 5 * 10^4
 * 0 <= m == edges.length <= min(10^5, n * (n - 1) / 2)
 * edges[i] = [ui, vi, costi]
 * 0 <= ui, vi < n
 * ui != vi
 * 0 <= costi <= 10^9
 * 0 <= k <= 5 * 10^13
 * online[i] is either true or false, and both online[0] and online[n - 1] are
 * true.
 * The given graph is a directed acyclic graph.
 *
 */
public class NetworkRecoveryPathways {

    // V0
    // IDEA: BINARY SEARCH THE SCORE + CHEAPEST PATH ON THE DAG (topological relaxation)
    //  - demanding score >= mid makes every edge cheaper than mid unusable; feasible(mid) =
    //    "cheapest 0 -> n-1 path over the remaining edges costs <= k", which is monotone
    //  - binary search over the distinct edge costs
    //  - the graph is a DAG, so the cheapest path is one relaxation sweep in topological order
    //    (built once); edges touching an offline node are dropped up front
    /**
     * time = O((n + m) * log m)
     * space = O(n + m)
     */
    public int findMaxPathScore(int[][] edges, boolean[] online, long k) {
        int n = online.length;

        // graph over online nodes only: u -> list of {v, cost}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        int[] inDegree = new int[n];
        TreeSet<Integer> costSet = new TreeSet<>();
        for (int[] edge : edges) {
            if (!online[edge[0]] || !online[edge[1]]) {
                continue;
            }
            graph.get(edge[0]).add(new int[] {edge[1], edge[2]});
            inDegree[edge[1]]++;
            costSet.add(edge[2]);
        }
        if (costSet.isEmpty()) {
            return -1;
        }

        // topological order of the surviving DAG (Kahn), independent of the threshold
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                stack.push(i);
            }
        }
        while (!stack.isEmpty()) {
            int node = stack.pop();
            order.add(node);
            for (int[] next : graph.get(node)) {
                inDegree[next[0]]--;
                if (inDegree[next[0]] == 0) {
                    stack.push(next[0]);
                }
            }
        }

        int[] costs = new int[costSet.size()];
        int index = 0;
        for (int cost : costSet) {
            costs[index++] = cost;
        }

        // even with every edge allowed, no path fits the budget
        if (!feasible(n, graph, order, k, costs[0])) {
            return -1;
        }

        // largest costs[i] that is still feasible
        int low = 0;
        int high = costs.length - 1;
        while (low < high) {
            int mid = low + (high - low + 1) / 2;
            if (feasible(n, graph, order, k, costs[mid])) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return costs[low];
    }

    /** True if the cheapest 0 -> n-1 path using only edges with cost >= minEdge costs <= k. */
    private boolean feasible(int n, List<List<int[]>> graph, List<Integer> order, long k, int minEdge) {
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;
        for (int node : order) {
            // unreachable, or already over budget
            if (dist[node] > k) {
                continue;
            }
            for (int[] next : graph.get(node)) {
                if (next[1] >= minEdge && dist[node] + next[1] < dist[next[0]]) {
                    dist[next[0]] = dist[node] + next[1];
                }
            }
        }
        return dist[n - 1] <= k;
    }
}
