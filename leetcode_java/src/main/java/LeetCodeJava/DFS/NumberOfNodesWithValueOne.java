package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-nodes-with-value-one/

/**
 *  2445. Number of Nodes With Value One
 *  Medium
 *
 *  There is an undirected connected tree with n nodes labeled from 1 to n and
 *  n - 1 edges. You are given the integer n. The parent node of a node with a
 *  label v is the node with the label floor(v / 2). The root of the tree is the
 *  node with the label 1.
 *
 *  You are also given an integer array queries. Initially, every node has a
 *  value 0 on it. For each query queries[i], you should flip all values in the
 *  subtree of the node with the label queries[i].
 *
 *  Return the total number of nodes with the value 1 after processing all the
 *  queries.
 *
 *  Example 1:
 *    Input: n = 5, queries = [1,2,5]
 *    Output: 3
 *    Explanation: nodes 1, 3 and 5 end up with value 1.
 *
 *  Example 2:
 *    Input: n = 3, queries = [2,3,3]
 *    Output: 1
 *    Explanation: only node 2 ends up with value 1.
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    1 <= queries.length <= 10^5
 *    1 <= queries[i] <= n
 */
public class NumberOfNodesWithValueOne {

    // V0
    // IDEA: LAZY FLIP TAG + TOP-DOWN PROPAGATION (heap indexing, parent = i / 2)
    //       a query on node q flips every node in q's subtree, so put a "tag" of
    //       parity 1 on q; two flips on the same node cancel, so only the parity
    //       matters.
    //       the final value of node i is the XOR of all tags on the path
    //       root..i, i.e.  val[i] = tag[i] XOR val[i / 2].
    //       because the labels ARE heap indices, i / 2 < i, so a single
    //       ascending sweep i = 1..n computes every val[i] - no DFS at all.
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public int numberOfNodes(int n, int[] queries) {
        int[] tag = new int[n + 1];
        for (int q : queries) {
            tag[q] ^= 1;
        }

        int[] val = new int[n + 1];
        int res = 0;
        for (int i = 1; i <= n; i++) {
            val[i] = tag[i] ^ (i > 1 ? val[i >> 1] : 0);
            res += val[i];
        }
        return res;
    }
}
