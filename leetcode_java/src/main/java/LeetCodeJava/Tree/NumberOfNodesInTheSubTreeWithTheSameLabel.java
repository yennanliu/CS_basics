package LeetCodeJava.Tree;

// https://leetcode.com/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/

/**
 *  1519. Number of Nodes in the Sub-Tree With the Same Label
 *  Medium
 *
 *  You are given a tree (i.e. a connected, undirected graph that has no
 *  cycles) consisting of n nodes numbered from 0 to n - 1 and exactly n - 1
 *  edges. The root of the tree is node 0, and each node has a label which is a
 *  lower-case character given in the string labels (the node numbered i has
 *  the label labels[i]).
 *
 *  The edges array is given on the form edges[i] = [a_i, b_i], meaning there
 *  is an edge between nodes a_i and b_i.
 *
 *  Return an array of size n where ans[i] is the number of nodes in the
 *  subtree of the ith node which have the same label as node i.
 *
 *  Example 1:
 *    Input: n = 7, edges = [[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]],
 *           labels = "abaedcd"
 *    Output: [2,1,1,1,1,1,1]
 *    Explanation: node 0 has label 'a' and its subtree also holds node 2
 *                 with label 'a', so the answer is 2.
 *
 *  Example 2:
 *    Input: n = 4, edges = [[0,1],[1,2],[0,3]], labels = "bbbb"
 *    Output: [4,2,1,1]
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    edges.length == n - 1
 *    0 <= a_i, b_i < n, a_i != b_i
 *    labels.length == n, lowercase English letters only
 */
public class NumberOfNodesInTheSubTreeWithTheSameLabel {

    // V0
    // IDEA: POST-ORDER OVER AN ITERATIVE DFS ORDER, MERGING 26-SLOT COUNTERS UP
    //       for every node keep a 26-length array of the label counts of its
    //       subtree. walking the DFS pre-order list in REVERSE is a valid
    //       post-order, so all children of a node are finished before it:
    //           cnt[u] = own label + sum of cnt[child]
    //           res[u] = cnt[u][labels[u]]
    //       NOTE: n can be 10^5 and the tree may be one long chain, so the
    //             traversal is iterative (recursion could blow the stack).
    /**
     * time = O(26 * N)
     * space = O(26 * N)
     */
    public int[] countSubTrees(int n, int[][] edges, String labels) {
        // adjacency via head/next arrays (no boxing)
        int[] head = new int[n];
        int[] nxt = new int[2 * (n - 1 > 0 ? n - 1 : 1)];
        int[] to = new int[2 * (n - 1 > 0 ? n - 1 : 1)];
        for (int i = 0; i < n; i++) {
            head[i] = -1;
        }
        int ec = 0;
        for (int[] e : edges) {
            to[ec] = e[1]; nxt[ec] = head[e[0]]; head[e[0]] = ec++;
            to[ec] = e[0]; nxt[ec] = head[e[1]]; head[e[1]] = ec++;
        }

        // iterative DFS -> pre-order list + parent of each node
        int[] parent = new int[n];
        int[] order = new int[n];
        boolean[] seen = new boolean[n];
        int[] stack = new int[n];
        int sp = 0, oc = 0;
        parent[0] = -1;
        seen[0] = true;
        stack[sp++] = 0;
        while (sp > 0) {
            int u = stack[--sp];
            order[oc++] = u;
            for (int e = head[u]; e != -1; e = nxt[e]) {
                int v = to[e];
                if (!seen[v]) {
                    seen[v] = true;
                    parent[v] = u;
                    stack[sp++] = v;
                }
            }
        }

        int[] res = new int[n];
        int[][] cnt = new int[n][];
        for (int i = oc - 1; i >= 0; i--) {
            int u = order[i];
            int[] cur = cnt[u];
            if (cur == null) {
                cur = new int[26];
            }
            int k = labels.charAt(u) - 'a';
            cur[k]++;
            res[u] = cur[k];

            int p = parent[u];
            if (p >= 0) {
                if (cnt[p] == null) {
                    cnt[p] = new int[26];
                }
                int[] pc = cnt[p];
                for (int j = 0; j < 26; j++) {
                    pc[j] += cur[j];
                }
            }
            cnt[u] = null;   // parent already absorbed it -> free it
        }
        return res;
    }
}
