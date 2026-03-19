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


    // V0-1
    // IDEA: DFS (gemini)
    /**  NOTE !!
     *
     * This is a classic `rerooting DP` template.
     * Same pattern shows up in many tree problems.
     *
     */
    /**
     * In **LC 834 (Sum of Distances in Tree)**,
     * a simple BFS/DFS from every node would take $O(N^2)$,
     * which will **Time Limit Exceeded (TLE)** for $N = 3 \times 10^4$.
     *
     * To solve this in **$O(N)$**,
     * we use a technique called **Re-rooting DP**. We perform two DFS passes:
     *
     *  - 1.  **Post-order (Bottom-Up)**:
     *           Calculate the subtree size (`count[]`)
     *           and the sum of distances for
     *           one fixed root (node 0).
     *
     *  - 2.  **Pre-order (Top-Down)**:
     *          Shift the root from a parent to its
     *          child using a mathematical relationship
     *          to update the total distance
     *          sum for all other nodes.
     *
     * ---
     *
     * ### 🔍 The Mathematical "Magic"
     * The core of the $O(N)$ solution is the formula used in `dfs2`. When you move the root from parent **u** to child **v**:
     * 1.  **v's Subtree**: Every node in `v`'s subtree is now **one step closer** to the root. There are `count[v]` such nodes.
     * 2.  **The Rest of the Tree**: Every node *not* in `v`'s subtree is now **one step farther** from the root. There are `n - count[v]` such nodes.
     *
     * $$ans[v] = ans[u] - count[v] + (n - count[v])$$
     *
     * ### 📊 Complexity Analysis
     *
     * | Metric | Complexity | Explanation |
     * | :--- | :--- | :--- |
     * | **Time** | **$O(N)$** | Two DFS passes. Each node and edge is visited a constant number of times. |
     * | **Space** | **$O(N)$** | Storing the adjacency list, `ans` array, `count` array, and the recursion stack. |
     *
     *
     */
    int[] ans;
    int[] count;
    List<Set<Integer>> adj;
    int n;

    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        this.n = n;
        ans = new int[n];
        count = new int[n];
        adj = new ArrayList<>();

        for (int i = 0; i < n; i++)
            adj.add(new HashSet<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // Pass 1: Post-order DFS to get count and base ans[0]
        dfs1_0_1(0, -1);

        // Pass 2: Pre-order DFS to re-root and find all ans[i]
        dfs2_0_1(0, -1);

        return ans;
    }

    private void dfs1_0_1(int u, int p) {
        count[u] = 1;
        for (int v : adj.get(u)) {
            if (v == p)
                continue;
            dfs1_0_1(v, u);
            count[u] += count[v];
            // Distance from u to all nodes in v's subtree
            // is (dist from v to its subtree) + (number of nodes in v's subtree)
            ans[u] += ans[v] + count[v];
        }
    }

    private void dfs2_0_1(int u, int p) {
        for (int v : adj.get(u)) {
            if (v == p)
                continue;
            // Mathematical Re-rooting Formula:
            // When moving root from u to v:
            // - count[v] nodes get 1 unit closer to the new root.
            // - (n - count[v]) nodes get 1 unit farther away.
            ans[v] = ans[u] - count[v] + (n - count[v]);
            dfs2_0_1(v, u);
        }
    }



    // V0-2
    // IDEA: DFS (GPT)
    /**
     * Great — let’s walk through a concrete example for
     * LeetCode 834 Sum of Distances in Tree so the rerooting idea *really clicks*.
     *
     * ---
     *
     * ## 🌳 Example
     *
     * ```
     * n = 6
     * edges = [[0,1],[0,2],[2,3],[2,4],[2,5]]
     * ```
     *
     * Tree structure:
     *
     * ```
     *         0
     *        / \
     *       1   2
     *          /|\
     *         3 4 5
     * ```
     *
     * ---
     *
     * # 1️⃣ Post-order DFS (`dfs1`)
     *
     * We compute:
     *
     * * `count[u]`: subtree size
     * * `res[u]`: sum of distances to subtree nodes
     *
     * ---
     *
     * ### Start from leaves
     *
     * #### Node 1
     *
     * * `count[1] = 1`
     * * `res[1] = 0`
     *
     * #### Node 3, 4, 5
     *
     * Same:
     *
     * * `count = 1`, `res = 0`
     *
     * ---
     *
     * ### Node 2
     *
     * Children: 3, 4, 5
     *
     * ```
     * count[2] = 1 + 1 + 1 + 1 = 4
     * res[2] = (res[3]+1) + (res[4]+1) + (res[5]+1)
     *        = 1 + 1 + 1 = 3
     * ```
     *
     * ---
     *
     * ### Node 0
     *
     * Children: 1, 2
     *
     * ```
     * count[0] = 1 + count[1] + count[2]
     *          = 1 + 1 + 4 = 6
     *
     * res[0] = (res[1] + count[1]) + (res[2] + count[2])
     *        = (0+1) + (3+4)
     *        = 1 + 7 = 8
     * ```
     *
     * ---
     *
     * ✅ After `dfs1`:
     *
     * | Node | count | res |
     * | ---- | ----- | --- |
     * | 0    | 6     | 8   |
     * | 1    | 1     | 0   |
     * | 2    | 4     | 3   |
     * | 3    | 1     | 0   |
     * | 4    | 1     | 0   |
     * | 5    | 1     | 0   |
     *
     * 👉 `res[0] = 8` means:
     *
     * ```
     * dist(0→1)=1
     * dist(0→2)=1
     * dist(0→3)=2
     * dist(0→4)=2
     * dist(0→5)=2
     * TOTAL = 8 ✅
     * ```
     *
     * ---
     *
     * # 2️⃣ Pre-order DFS (`dfs2`) — Rerooting
     *
     * Now we “move root” and update results.
     *
     * ---
     *
     * ## 🔁 Move root: 0 → 1
     *
     * Formula:
     *
     * ```
     * res[1] = res[0] - count[1] + (N - count[1])
     *        = 8 - 1 + (6 - 1)
     *        = 8 - 1 + 5 = 12
     * ```
     *
     * 👉 Check manually:
     *
     * ```
     * dist(1→0)=1
     * dist(1→2)=2
     * dist(1→3)=3
     * dist(1→4)=3
     * dist(1→5)=3
     * TOTAL = 12 ✅
     * ```
     *
     * ---
     *
     * ## 🔁 Move root: 0 → 2
     *
     * ```
     * res[2] = 8 - 4 + (6 - 4)
     *        = 8 - 4 + 2 = 6
     * ```
     *
     * ---
     *
     * ## 🔁 Move root: 2 → 3
     *
     * ```
     * res[3] = res[2] - count[3] + (N - count[3])
     *        = 6 - 1 + 5 = 10
     * ```
     *
     * Same for 4, 5:
     *
     * ```
     * res[4] = 10
     * res[5] = 10
     * ```
     *
     * ---
     *
     * # ✅ Final Answer
     *
     * ```
     * [8, 12, 6, 10, 10, 10]
     * ```
     *
     * ---
     *
     * # 🧠 The KEY Insight (This is the “aha” moment)
     *
     * When moving root from `u → v`:
     *
     * ### What changes?
     *
     * * Nodes in `v`’s subtree → **1 step closer**
     *   → subtract `count[v]`
     *
     * * All other nodes → **1 step farther**
     *   → add `(N - count[v])`
     *
     * ---
     *
     * ### 🔥 Final Formula
     *
     * ```
     * res[v] = res[u] - count[v] + (N - count[v])
     * ```
     *
     * ---
     *
     * # 💡 Mental Model
     *
     * Think:
     *
     * > “When I move root into a subtree,
     * that subtree gets closer, everything else gets farther.”
     *
     * ---
     */
//    int[] res;
//    int[] count;
//    List<Set<Integer>> graph;
//    int N;
//
//    public int[] sumOfDistancesInTree(int n, int[][] edges) {
//        N = n;
//        res = new int[n];
//        count = new int[n];
//        graph = new ArrayList<>();
//
//        for (int i = 0; i < n; i++) {
//            graph.add(new HashSet<>());
//        }
//
//        for (int[] e : edges) {
//            graph.get(e[0]).add(e[1]);
//            graph.get(e[1]).add(e[0]);
//        }
//
//        dfs1(0, -1); // post-order
//        dfs2(0, -1); // pre-order (reroot)
//
//        return res;
//    }
//
//    // Post-order: compute count[] and res[0]
//    private void dfs1(int node, int parent) {
//        count[node] = 1;
//
//        for (int nei : graph.get(node)) {
//            if (nei == parent)
//                continue;
//
//            dfs1(nei, node);
//            count[node] += count[nei];
//            res[node] += res[nei] + count[nei];
//        }
//    }
//
//    // Pre-order: reroot to compute all res[]
//    private void dfs2(int node, int parent) {
//        for (int nei : graph.get(node)) {
//            if (nei == parent)
//                continue;
//
//            // move root from node -> nei
//            res[nei] = res[node] - count[nei] + (N - count[nei]);
//
//            dfs2(nei, node);
//        }
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
    private int[] count_4;
    private int[] res;

    private void dfs(int node, int parent) {
        for (int child : graph.get(node)) {
            if (child != parent) {
                dfs(child, node);
                count_4[node] += count_4[child];
                res[node] += res[child] + count_4[child];
            }
        }
    }

    private void dfs2(int node, int parent) {
        for (int child : graph.get(node)) {
            if (child != parent) {
                res[child] = res[node] - count_4[child] + (count_4.length - count_4[child]);
                dfs2(child, node);
            }
        }
    }

    public int[] sumOfDistancesInTree_4(int n, int[][] edges) {
        graph = new HashMap<>();
        count_4 = new int[n];
        res = new int[n];
        Arrays.fill(count_4, 1);

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
