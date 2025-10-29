package LeetCodeJava.DFS;

// https://leetcode.com/problems/maximum-number-of-k-divisible-components/description/

import java.util.*;

/**
 *  2872. Maximum Number of K-Divisible Components
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There is an undirected tree with n nodes labeled from 0 to n - 1. You are given the integer n and a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.
 *
 * You are also given a 0-indexed integer array values of length n, where values[i] is the value associated with the ith node, and an integer k.
 *
 * A valid split of the tree is obtained by removing any set of edges, possibly empty, from the tree such that the resulting components all have values that are divisible by k, where the value of a connected component is the sum of the values of its nodes.
 *
 * Return the maximum number of components in any valid split.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 5, edges = [[0,2],[1,2],[1,3],[2,4]], values = [1,8,1,4,4], k = 6
 * Output: 2
 * Explanation: We remove the edge connecting node 1 with 2. The resulting split is valid because:
 * - The value of the component containing nodes 1 and 3 is values[1] + values[3] = 12.
 * - The value of the component containing nodes 0, 2, and 4 is values[0] + values[2] + values[4] = 6.
 * It can be shown that no other valid split has more than 2 connected components.
 * Example 2:
 *
 *
 * Input: n = 7, edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[2,6]], values = [3,0,6,1,5,2,1], k = 3
 * Output: 3
 * Explanation: We remove the edge connecting node 0 with 2, and the edge connecting node 0 with 1. The resulting split is valid because:
 * - The value of the component containing node 0 is values[0] = 3.
 * - The value of the component containing nodes 2, 5, and 6 is values[2] + values[5] + values[6] = 9.
 * - The value of the component containing nodes 1, 3, and 4 is values[1] + values[3] + values[4] = 6.
 * It can be shown that no other valid split has more than 3 connected components.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 3 * 104
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= ai, bi < n
 * values.length == n
 * 0 <= values[i] <= 109
 * 1 <= k <= 109
 * Sum of values is divisible by k.
 * The input is generated such that edges represents a valid tree.
 *
 */
public class MaximumNumberOfKDivisibleComponents {

    // V0
//    public int maxKDivisibleComponents(int n, int[][] edges, int[] values, int k) {
//
//    }

    // V0-1
    // IDEA: DFS (fixed by gpt)
    private int res = 0;
    private int k;
    private List<Integer>[] graph;
    private int[] values;

    public int maxKDivisibleComponents_0_1(int n, int[][] edges, int[] values, int k) {
        this.k = k;
        this.values = values;
        this.graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            int a = e[0], b = e[1];
            graph[a].add(b);
            graph[b].add(a);
        }

        dfs_0_1(0, -1);
        return res;
    }

    private long dfs_0_1(int node, int parent) {
        long sum = values[node]; // start from node value
        for (int nei : graph[node]) {
            if (nei == parent)
                continue;
            sum += dfs_0_1(nei, node);
            sum %= k; // keep sum mod k small
        }

        if (sum % k == 0) {
            res++; // we can form a divisible component
            return 0; // reset sum (as this component is split)
        }
        return sum;
    }

    // V0-2
    // IDEA: DFS (fixed by gemini)
    // Adjacency list to represent the tree
    private List<List<Integer>> adj;

    // Counter for the number of valid components "cut" from the tree
    private int components;

    // The divisor k
    private int k_0_2;

    // Array of node values
    private int[] values_0_2;

    /**
     * Main function to find the maximum number of k-divisible components.
     */
    public int maxKDivisibleComponents_O_2(int n, int[][] edges, int[] values, int k) {
        // Initialize class variables
        this.adj = new ArrayList<>();
        this.components = 0;
        this.k_0_2 = k;
        this.values_0_2 = values;

        // Build the adjacency list for the tree
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        // Start the DFS from an arbitrary root (e.g., node 0).
        // -1 indicates that node 0 has no parent.
        dfs_0_2(0, -1);

        // The total number of components is the number of "cuts" we made
        // plus the one remaining component that contains the root.
        return this.components + 1;
    }

    /**
     * Performs a post-order DFS to calculate component sums and decide where to cut.
     *
     * @param u      The current node being visited.
     * @param parent The parent of the current node (to avoid traversing backward).
     * @return The sum of the component that node 'u' belongs to.
     */
    private long dfs_0_2(int u, int parent) {
        // Start the sum of this component with the current node's value.
        // Use 'long' to prevent integer overflow.
        long currentComponentSum = values[u];

        // Iterate over all neighbors of the current node
        for (int v : adj.get(u)) {
            // Skip the neighbor if it's the parent node
            if (v == parent) {
                continue;
            }

            // Recursively call DFS on the child node
            long childComponentSum = dfs_0_2(v, u);

            // This is the greedy decision point:
            if (childComponentSum % k == 0) {
                // Case 1: The child's component sum is divisible by k.
                // We "cut" this edge, and this child component counts as one.
                this.components++;
                // We DO NOT add its sum to the parent's component.
            } else {
                // Case 2: The child's component sum is NOT divisible by k.
                // We *must* merge this child component with the parent's component.
                currentComponentSum += childComponentSum;
            }
        }

        // Return the sum of the component 'u' belongs to.
        // This will be passed up to its parent.
        return currentComponentSum;
    }

    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/maximum-number-of-k-divisible-components/editorial/
    public int maxKDivisibleComponents_1_1(
            int n,
            int[][] edges,
            int[] values,
            int k) {
        // Step 1: Create adjacency list from edges
        List<Integer>[] adjList = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjList[node1].add(node2);
            adjList[node2].add(node1);
        }

        // Step 2: Initialize component count
        int[] componentCount = new int[1]; // Use array to pass by reference

        // Step 3: Start DFS traversal from node 0
        dfs(0, -1, adjList, values, k, componentCount);

        // Step 4: Return the total number of components
        return componentCount[0];
    }

    private int dfs(
            int currentNode,
            int parentNode,
            List<Integer>[] adjList,
            int[] nodeValues,
            int k,
            int[] componentCount) {
        // Step 1: Initialize sum for the current subtree
        int sum = 0;

        // Step 2: Traverse all neighbors
        for (int neighborNode : adjList[currentNode]) {
            if (neighborNode != parentNode) {
                // Recursive call to process the subtree rooted at the neighbor
                sum += dfs(
                        neighborNode,
                        currentNode,
                        adjList,
                        nodeValues,
                        k,
                        componentCount);
                sum %= k; // Ensure the sum stays within bounds
            }
        }

        // Step 3: Add the value of the current node to the sum
        sum += nodeValues[currentNode];
        sum %= k;

        // Step 4: Check if the sum is divisible by k
        if (sum == 0) {
            componentCount[0]++;
        }

        // Step 5: Return the computed sum for the current subtree
        return sum;
    }

    // V1-2
    // IDEA: DFS
    // https://leetcode.com/problems/maximum-number-of-k-divisible-components/editorial/
    public int maxKDivisibleComponents_1_2(
            int n,
            int[][] edges,
            int[] values,
            int k) {
        // Base case: if there are less than 2 nodes, return 1
        if (n < 2)
            return 1;

        int componentCount = 0;
        Map<Integer, Set<Integer>> graph = new HashMap<>();

        // Step 1: Build the graph
        for (int[] edge : edges) {
            int node1 = edge[0], node2 = edge[1];
            graph.computeIfAbsent(node1, key -> new HashSet<>()).add(node2);
            graph.computeIfAbsent(node2, key -> new HashSet<>()).add(node1);
        }

        // Convert values to long to prevent overflow
        long[] longValues = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            longValues[i] = values[i];
        }

        // Step 2: Initialize the BFS queue with leaf nodes
        // (nodes with only one connection)
        Queue<Integer> queue = new LinkedList<>();
        for (Map.Entry<Integer, Set<Integer>> entry : graph.entrySet()) {
            if (entry.getValue().size() == 1) {
                queue.add(entry.getKey());
            }
        }

        // Step 3: Process nodes in BFS order
        while (!queue.isEmpty()) {
            int currentNode = queue.poll();

            // Find the neighbor node
            int neighborNode = -1;
            if (!graph.get(currentNode).isEmpty()) {
                neighborNode = graph.get(currentNode).iterator().next();
            }

            if (neighborNode >= 0) {
                // Remove the edge between current and neighbor
                graph.get(neighborNode).remove(currentNode);
                graph.get(currentNode).remove(neighborNode);
            }

            // Check divisibility of the current node's value
            if (longValues[currentNode] % k == 0) {
                componentCount++;
            } else if (neighborNode >= 0) {
                // Add current node's value to the neighbor
                longValues[neighborNode] += longValues[currentNode];
            }

            // If the neighbor becomes a leaf node, add it to the queue
            if (neighborNode >= 0 &&
                    graph.get(neighborNode).size() == 1) {
                queue.add(neighborNode);
            }
        }

        return componentCount;
    }

    // V1-3
    // IDEA: Topological Sort / Onion Sort
    // https://leetcode.com/problems/maximum-number-of-k-divisible-components/editorial/
    public int maxKDivisibleComponents_1_3(
            int n,
            int[][] edges,
            int[] values,
            int k) {
        if (n < 2)
            return 1; // Handle the base case where the graph has fewer than 2 nodes.

        int componentCount = 0;
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[n];

        // Build the graph and calculate in-degrees
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>()); // Initialize the adjacency list for each node.
        }
        for (int[] edge : edges) {
            int node1 = edge[0], node2 = edge[1];
            graph.get(node1).add(node2);
            graph.get(node2).add(node1);
            inDegree[node1]++;
            inDegree[node2]++;
        }

        // Convert values to long to prevent overflow
        long[] longValues = new long[n];
        for (int i = 0; i < n; i++) {
            longValues[i] = values[i];
        }

        // Initialize the queue with nodes having in-degree of 1 (leaf nodes)
        Queue<Integer> queue = new LinkedList<>();
        for (int node = 0; node < n; node++) {
            if (inDegree[node] == 1) {
                queue.offer(node);
            }
        }

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            inDegree[currentNode]--;

            long addValue = 0;

            // Check if the current node's value is divisible by k
            if (longValues[currentNode] % k == 0) {
                componentCount++;
            } else {
                addValue = longValues[currentNode];
            }

            // Propagate the value to the neighbor nodes
            for (int neighborNode : graph.get(currentNode)) {
                if (inDegree[neighborNode] == 0) {
                    continue;
                }

                inDegree[neighborNode]--;
                longValues[neighborNode] += addValue;

                // If the neighbor node's in-degree becomes 1, add it to the queue
                if (inDegree[neighborNode] == 1) {
                    queue.offer(neighborNode);
                }
            }
        }

        return componentCount;
    }


    // V3
    // https://leetcode.com/problems/maximum-number-of-k-divisible-components/solutions/6169223/topological-sort-approach-c-java-python-s98ti/
    public int maxKDivisibleComponents_3(int n, int[][] edges, int[] vals, int k) {
        if (n < 2)
            return 1;

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++)
            graph.add(new ArrayList<>());
        int[] degree = new int[n];

        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
            degree[edge[0]]++;
            degree[edge[1]]++;
        }

        long[] nodeVals = new long[n];
        for (int i = 0; i < n; i++)
            nodeVals[i] = vals[i];
        Queue<Integer> leafQ = new LinkedList<>();
        for (int i = 0; i < n; i++)
            if (degree[i] == 1)
                leafQ.add(i);

        int compCnt = 0;
        while (!leafQ.isEmpty()) {
            int curr = leafQ.poll();
            degree[curr]--;
            long carry = 0;

            if (nodeVals[curr] % k == 0)
                compCnt++;
            else
                carry = nodeVals[curr];

            for (int nbr : graph.get(curr)) {
                if (degree[nbr] == 0)
                    continue;
                degree[nbr]--;
                nodeVals[nbr] += carry;
                if (degree[nbr] == 1)
                    leafQ.add(nbr);
            }
        }

        return compCnt;
    }


    // V4
    private Map<Integer, List<Integer>> adj_4;
    private Set<Integer> visited;
    private int comp;

    public int maxKDivisibleComponents_4(int n, int[][] edges, int[] values, int k) {
        adj_4 = new HashMap<>();
        visited = new HashSet<>();
        comp = 0;

        int src = 0;

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            adj_4.computeIfAbsent(u, k1 -> new ArrayList<>()).add(v);
            adj_4.computeIfAbsent(v, k1 -> new ArrayList<>()).add(u);
        }

        dfs(src, values, k);
        return comp;
    }

    private int dfs(int root, int[] values, int k) {
        if (visited.contains(root)) {
            return 0;
        }

        visited.add(root);
        int ans = values[root];

        for (int neigh : adj_4.getOrDefault(root, Collections.emptyList())) {
            ans += dfs(neigh, values, k);
        }

        if (ans % k == 0) {
            comp++;
            return 0;
        }

        return ans % k;
    }


}
