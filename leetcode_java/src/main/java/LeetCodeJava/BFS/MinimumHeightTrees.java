package LeetCodeJava.BFS;

import java.util.*;

// https://leetcode.com/problems/minimum-height-trees/description/
// https://leetcode.cn/problems/minimum-height-trees/description/
// https://youtu.be/wQGQnyv_9hI?si=Fw35T5CyLVy5Z5t_
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

    // V0-1
    // IDEA: BRUTE FORCE (loop over nodes, then get dist via dfs/bfs) (GPT) (TLE)
    // https://neetcode.io/solutions/minimum-height-trees
    public List<Integer> findMinHeightTrees_0_1(int n, int[][] edges) {
        if (n == 1)
            return Collections.singletonList(0); // Edge case: single node

        // Step 1: Build the graph as an adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        int minHeight = Integer.MAX_VALUE;
        List<Integer> result = new ArrayList<>();

        // Step 2: Iterate through all nodes and calculate the height using BFS
        for (int root = 0; root < n; root++) {
            int height = bfsHeight(graph, root, n);
            if (height < minHeight) {
                minHeight = height;
                result.clear();
                result.add(root);
            } else if (height == minHeight) {
                result.add(root);
            }
        }

        return result;
    }

    // BFS to calculate tree height given a root node
    private int bfsHeight(List<List<Integer>> graph, int root, int n) {
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];
        queue.offer(root);
        visited[root] = true;
        int height = -1; // Start from -1 because the first iteration represents the root level.

        while (!queue.isEmpty()) {
            int size = queue.size();
            height++; // Each level increases height

            for (int i = 0; i < size; i++) {
                int node = queue.poll();
                for (int neighbor : graph.get(node)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(neighbor);
                    }
                }
            }
        }

        return height;
    }

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

    // V2-1
    // Dynamic Programming On Trees (Rerooting)
    // https://neetcode.io/solutions/minimum-height-trees

    // V2-2
    // Find Centroids of the Tree (DFS)
    // https://neetcode.io/solutions/minimum-height-trees
    private List<Integer>[] adj;
    private List<Integer> centroids;
    private int nodeB;

    public List<Integer> findMinHeightTrees_2_2(int n, int[][] edges) {
        if (n == 1)
            return Collections.singletonList(0);

        adj = new ArrayList[n];
        for (int i = 0; i < n; ++i)
            adj[i] = new ArrayList<>();

        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }

        int nodeA = dfs(0, -1)[0];
        nodeB = dfs(nodeA, -1)[0];
        centroids = new ArrayList<>();
        findCentroids(nodeA, -1);

        int L = centroids.size();
        if (dfs(nodeA, -1)[1] % 2 == 0) {
            return Collections.singletonList(centroids.get(L / 2));
        } else {
            return Arrays.asList(centroids.get(L / 2 - 1), centroids.get(L / 2));
        }
    }

    private int[] dfs(int node, int parent) {
        int farthestNode = node, maxDistance = 0;
        for (int neighbor : adj[node]) {
            if (neighbor != parent) {
                int[] res = dfs(neighbor, node);
                if (res[1] + 1 > maxDistance) {
                    maxDistance = res[1] + 1;
                    farthestNode = res[0];
                }
            }
        }
        return new int[] { farthestNode, maxDistance };
    }

    private boolean findCentroids(int node, int parent) {
        if (node == nodeB) {
            centroids.add(node);
            return true;
        }
        for (int neighbor : adj[node]) {
            if (neighbor != parent) {
                if (findCentroids(neighbor, node)) {
                    centroids.add(node);
                    return true;
                }
            }
        }
        return false;
    }

    // V2-3
    // Topological Sorting (BFS)
    // https://neetcode.io/solutions/minimum-height-trees
    public List<Integer> findMinHeightTrees_2_3(int n, int[][] edges) {
        if (n == 1)
            return Collections.singletonList(0);

        List<Integer>[] adj = new ArrayList[n];
        for (int i = 0; i < n; ++i)
            adj[i] = new ArrayList<>();

        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }

        int[] edge_cnt = new int[n];
        Queue<Integer> leaves = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            edge_cnt[i] = adj[i].size();
            if (adj[i].size() == 1)
                leaves.offer(i);
        }

        while (!leaves.isEmpty()) {
            if (n <= 2)
                return new ArrayList<>(leaves);
            int size = leaves.size();
            for (int i = 0; i < size; ++i) {
                int node = leaves.poll();
                n--;
                for (int nei : adj[node]) {
                    edge_cnt[nei]--;
                    if (edge_cnt[nei] == 1)
                        leaves.offer(nei);
                }
            }
        }

        return new ArrayList<>();
    }


    // V3
    // https://leetcode.com/problems/minimum-height-trees/solutions/76055/share-some-thoughts-by-dietpepsi-mjsc/
    public List<Integer> findMinHeightTrees_3(int n, int[][] edges) {
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

    // V4
    // https://github.com/lydxlx1/LeetCode/blob/master/src/_310_1.java

    // V5
    // IDEA: BFS
    // https://leetcode.com/problems/minimum-height-trees/solutions/5060930/full-explanation-bfs-remove-leaf-nodes-b-4x00/
    public List<Integer> findMinHeightTrees_5(int n, int[][] edges) {
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

    // V6
    // https://leetcode.com/problems/minimum-height-trees/solutions/5062733/fasterlesserdetailed-explainationbfsstep-085i/
    public List<Integer> findMinHeightTrees_6(int n, int[][] edges) {
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
