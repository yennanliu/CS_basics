package LeetCodeJava.Tree;

// https://leetcode.com/problems/sum-of-distances-in-tree/description/

import java.util.*;

/**
 *  834. Sum of Distances in Tree
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * There is an undirected connected tree with n nodes labeled from 0 to n - 1 and n - 1 edges.
 *
 * You are given the integer n and the array edges where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree.
 *
 * Return an array answer of length n where answer[i] is the sum of the distances between the ith node in the tree and all other nodes.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 6, edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
 * Output: [8,12,6,10,10,10]
 * Explanation: The tree is shown above.
 * We can see that dist(0,1) + dist(0,2) + dist(0,3) + dist(0,4) + dist(0,5)
 * equals 1 + 1 + 2 + 2 + 2 = 8.
 * Hence, answer[0] = 8, and so on.
 * Example 2:
 *
 *
 * Input: n = 1, edges = []
 * Output: [0]
 * Example 3:
 *
 *
 * Input: n = 2, edges = [[1,0]]
 * Output: [1,1]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 3 * 104
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= ai, bi < n
 * ai != bi
 * The given input represents a valid tree.
 *
 *
 */
public class SumOfDistancesInTree {

    // V0
//    public int[] sumOfDistancesInTree(int n, int[][] edges) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/sum-of-distances-in-tree/solutions/885637/java-solution-code-photo-explaination-by-gygk/
    public int[] sumOfDistancesInTree_2(int n, int[][] edges) {
        // build graph and declare results
        final ArrayList<Integer>[] graph = new ArrayList[n];
        final int[] count = new int[n];
        Arrays.fill(count, 1);
        final int[] answer = new int[n];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        postOrder(0, -1, graph, count, answer);
        // after postOrder, only answer[root] is correct, so do preOrder to update answer
        preOrder(0, -1, graph, count, answer, n);

        return answer;
    }

    // set count et subTreeSum, here use answer[]
    private void postOrder(int node, int parent, ArrayList<Integer>[] graph, int[] count, int[] answer) {
        for (int child : graph[node]) {
            if (child != parent) {
                postOrder(child, node, graph, count, answer);
                count[node] += count[child];
                answer[node] += answer[child] + count[child];
            }
        }
    }

    private void preOrder(int node, int parent, ArrayList<Integer>[] graph, int[] count, int[] answer, int n) {
        for (int child : graph[node]) {
            if (child != parent) {
                answer[child] = answer[node] + (n - count[child]) - count[child];
                preOrder(child, node, graph, count, answer, n);
            }
        }
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/sum-of-distances-in-tree/solutions/5083475/full-detailed-explanation-with-images-by-r4sf/
    public int[] sumOfDistancesInTree_3(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList());
        }

        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        int[] ans = new int[n];
        int[] count = new int[n];

        ans[0] = dfs1(adj, 0, 0, count, new boolean[n]);
        dfs2(adj, 0, ans, count, new boolean[n]);

        return ans;
    }

    private int dfs1(List<List<Integer>> adj, int node, int level, int[] count, boolean[] vis) {
        vis[node] = true;
        int ans = level;
        for (int child : adj.get(node)) {
            if (!vis[child]) {
                ans += dfs1(adj, child, level + 1, count, vis);
                count[node] += count[child];
            }
        }
        count[node]++;

        return ans;
    }

    private void dfs2(List<List<Integer>> adj, int node, int[] ans, int[] count, boolean[] vis) {
        vis[node] = true;
        for (int child : adj.get(node)) {
            if (!vis[child]) {
                ans[child] = ans[node] - count[child] + (count.length - count[child]);
                dfs2(adj, child, ans, count, vis);
            }
        }
    }


    // V4
    // IDEA: DFS + HASHMAP
    // https://leetcode.com/problems/sum-of-distances-in-tree/solutions/5082385/faster-lesserdetailed-explainationdfsste-2w17/
    private Map<Integer, List<Integer>> graph;
    private int[] count;
    private int[] res;

    private void dfs(int node, int parent) {
        for (int child : graph.get(node)) {
            if (child != parent) {
                dfs(child, node);
                count[node] += count[child];
                res[node] += res[child] + count[child];
            }
        }
    }

    private void dfs2(int node, int parent) {
        for (int child : graph.get(node)) {
            if (child != parent) {
                res[child] = res[node] - count[child] + (count.length - count[child]);
                dfs2(child, node);
            }
        }
    }

    public int[] sumOfDistancesInTree_4(int n, int[][] edges) {
        graph = new HashMap<>();
        count = new int[n];
        res = new int[n];
        Arrays.fill(count, 1);

        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        dfs(0, -1);
        dfs2(0, -1);

        return res;
    }





}
