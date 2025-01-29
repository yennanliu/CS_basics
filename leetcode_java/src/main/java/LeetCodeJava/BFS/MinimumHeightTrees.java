package LeetCodeJava.BFS;

import java.util.*;

// https://leetcode.com/problems/minimum-height-trees/description/
// https://leetcode.cn/problems/minimum-height-trees/description/

/**
 * 310. Minimum Height Trees
 * Medium
 * Topics
 * Companies
 * Hint
 * A tree is an undirected graph in which any two vertices are connected by exactly one path. In other words, any connected graph without simple cycles is a tree.
 *
 * Given a tree of n nodes labelled from 0 to n - 1, and an array of n - 1 edges where edges[i] = [ai, bi] indicates that there is an undirected edge between the two nodes ai and bi in the tree, you can choose any node of the tree as the root. When you select a node x as the root, the result tree has height h. Among all possible rooted trees, those with minimum height (i.e. min(h))  are called minimum height trees (MHTs).
 *
 * Return a list of all MHTs' root labels. You can return the answer in any order.
 *
 * The height of a rooted tree is the number of edges on the longest downward path between the root and a leaf.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 4, edges = [[1,0],[1,2],[1,3]]
 * Output: [1]
 * Explanation: As shown, the height of the tree is 1 when the root is the node with label 1 which is the only MHT.
 * Example 2:
 *
 *
 * Input: n = 6, edges = [[3,0],[3,1],[3,2],[3,4],[5,4]]
 * Output: [3,4]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 104
 * edges.length == n - 1
 * 0 <= ai, bi < n
 * ai != bi
 * All the pairs (ai, bi) are distinct.
 * The given input is guaranteed to be a tree and there will be no repeated edges.
 */
public class MinimumHeightTrees {

    // V0
//    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
//
//    }

    // V1
    // IDEA:  topological sorting with a BFS (Breadth-First Search) (GPT)
    public List<Integer> findMinHeightTrees_1(int n, int[][] edges) {
        if (n == 1)
            return Collections.singletonList(0); // If there's only one node, it's the MHT root.

        // Step 1: Build the graph as an adjacency list.
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // Step 2: Find all leaves (nodes with only one connection).
        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (graph.get(i).size() == 1) {
                leaves.add(i);
            }
        }

        // Step 3: Remove leaves level by level until 1 or 2 nodes remain.
        int remainingNodes = n;
        while (remainingNodes > 2) {
            remainingNodes -= leaves.size();
            List<Integer> newLeaves = new ArrayList<>();

            for (int leaf : leaves) {
                // The only neighbor of a leaf
                int neighbor = graph.get(leaf).iterator().next();
                graph.get(neighbor).remove(leaf); // Remove the leaf from the graph

                // If this neighbor becomes a leaf, add it to newLeaves
                if (graph.get(neighbor).size() == 1) {
                    newLeaves.add(neighbor);
                }
            }

            leaves = newLeaves; // Move to the next layer of leaves
        }

        // The remaining nodes are the roots of the Minimum Height Trees
        return leaves;
    }

    // V2
    // https://leetcode.com/problems/minimum-height-trees/solutions/76055/share-some-thoughts-by-dietpepsi-mjsc/
    public List<Integer> findMinHeightTrees_2(int n, int[][] edges) {
        if (n == 1)
            return Collections.singletonList(0);

        List<Set<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; ++i)
            adj.add(new HashSet<>());
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; ++i)
            if (adj.get(i).size() == 1)
                leaves.add(i);

        while (n > 2) {
            n -= leaves.size();
            List<Integer> newLeaves = new ArrayList<>();
            for (int i : leaves) {
                int j = adj.get(i).iterator().next();
                adj.get(j).remove(i);
                if (adj.get(j).size() == 1)
                    newLeaves.add(j);
            }
            leaves = newLeaves;
        }
        return leaves;
    }

    // V3
    // https://github.com/lydxlx1/LeetCode/blob/master/src/_310_1.java

    // V4
    // IDEA: BFS
    // https://leetcode.com/problems/minimum-height-trees/solutions/5060930/full-explanation-bfs-remove-leaf-nodes-b-4x00/
    public List<Integer> findMinHeightTrees_4(int n, int[][] edges) {
        if (n == 1)
            return Collections.singletonList(0);

        int ind[] = new int[n];
        Map<Integer, List<Integer>> map = new HashMap();
        for (int[] edge : edges) {
            ind[edge[0]]++;
            ind[edge[1]]++;
            map.putIfAbsent(edge[0], new ArrayList());
            map.putIfAbsent(edge[1], new ArrayList());
            map.get(edge[0]).add(edge[1]);
            map.get(edge[1]).add(edge[0]);
        }

        Queue<Integer> queue = new LinkedList();
        for (int i = 0; i < ind.length; i++) {
            if (ind[i] == 1) {
                queue.add(i);
            }
        }

        int processed = 0;
        while (n - processed > 2) {
            int size = queue.size();
            processed += size;
            for (int i = 0; i < size; i++) {
                int poll = queue.poll();
                for (int adj : map.get(poll)) {
                    if (--ind[adj] == 1) {
                        queue.add(adj);
                    }
                }
            }
        }

        List<Integer> list = new ArrayList();
        list.addAll(queue);
        return list;
    }

    // V5
    // https://leetcode.com/problems/minimum-height-trees/solutions/5062733/fasterlesserdetailed-explainationbfsstep-085i/
    public List<Integer> findMinHeightTrees_5(int n, int[][] edges) {
        if (n == 1) {
            return Collections.singletonList(0); // Only one node, return the root
        }

        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] degrees = new int[n];

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
            degrees[u]++;
            degrees[v]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degrees[i] == 1) {
                queue.offer(i);
            }
        }

        int remainingNodes = n;
        while (remainingNodes > 2) {
            int size = queue.size();
            remainingNodes -= size;

            for (int i = 0; i < size; i++) {
                int leaf = queue.poll();
                if (graph.containsKey(leaf)) {
                    for (int neighbor : graph.get(leaf)) {
                        degrees[neighbor]--;
                        if (degrees[neighbor] == 1) {
                            queue.offer(neighbor);
                        }
                    }
                    graph.remove(leaf); // Optional: Remove leaf node from the graph
                }
            }
        }

        return new ArrayList<>(queue);
    }

}
