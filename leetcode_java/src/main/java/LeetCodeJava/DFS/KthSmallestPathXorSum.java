package LeetCodeJava.DFS;

// https://leetcode.com/problems/kth-smallest-path-xor-sum/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 3590. Kth Smallest Path XOR Sum
 * Hard
 *
 * You are given an undirected tree rooted at node 0 with n nodes numbered from 0
 * to n - 1. Each node i has an integer value vals[i], and its parent is given by
 * par[i].
 *
 * The path XOR sum from the root to a node u is defined as the bitwise XOR of all
 * vals[i] for nodes i on the path from the root node to node u, inclusive.
 *
 * You are given a 2D integer array queries, where queries[j] = [u_j, k_j]. For
 * each query, find the k_j-th smallest distinct path XOR sum among all nodes in
 * the subtree rooted at u_j. If there are fewer than k_j distinct path XOR sums
 * in that subtree, the answer is -1.
 *
 * Return an integer array where the j-th element is the answer to the j-th query.
 *
 * In a rooted tree, the subtree of a node v includes v and all nodes whose path
 * to the root passes through v, that is, v and its descendants.
 *
 *
 * Example 1:
 *
 * Input: par = [-1,0,0], vals = [1,1,1], queries = [[0,1],[0,2],[0,3]]
 * Output: [0,1,-1]
 * Explanation:
 *
 * Path XORs:
 * Node 0: 1
 * Node 1: 1 XOR 1 = 0
 * Node 2: 1 XOR 1 = 0
 *
 * Subtree of 0: Subtree rooted at node 0 includes nodes [0, 1, 2] with Path XORs
 * = [1, 0, 0]. The distinct XORs are [0, 1].
 *
 * Queries:
 * queries[0] = [0, 1]: The 1st smallest distinct path XOR in the subtree of node
 * 0 is 0.
 * queries[1] = [0, 2]: The 2nd smallest distinct path XOR in the subtree of node
 * 0 is 1.
 * queries[2] = [0, 3]: Since there are only two distinct path XORs in this
 * subtree, the answer is -1.
 *
 * Output: [0, 1, -1]
 *
 * Example 2:
 *
 * Input: par = [-1,0,1], vals = [5,2,7], queries = [[0,1],[1,2],[1,3],[2,1]]
 * Output: [0,7,-1,0]
 * Explanation:
 *
 * Path XORs:
 * Node 0: 5
 * Node 1: 5 XOR 2 = 7
 * Node 2: 5 XOR 2 XOR 7 = 0
 *
 * Subtrees and Distinct Path XORs:
 * Subtree of 0: Subtree rooted at node 0 includes nodes [0, 1, 2] with Path XORs
 * = [5, 7, 0]. The distinct XORs are [0, 5, 7].
 * Subtree of 1: Subtree rooted at node 1 includes nodes [1, 2] with Path XORs =
 * [7, 0]. The distinct XORs are [0, 7].
 * Subtree of 2: Subtree rooted at node 2 includes only node [2] with Path XOR =
 * [0]. The distinct XORs are [0].
 *
 * Queries:
 * queries[0] = [0, 1]: The 1st smallest distinct path XOR in the subtree of node
 * 0 is 0.
 * queries[1] = [1, 2]: The 2nd smallest distinct path XOR in the subtree of node
 * 1 is 7.
 * queries[2] = [1, 3]: Since there are only two distinct path XORs, the answer is
 * -1.
 * queries[3] = [2, 1]: The 1st smallest distinct path XOR in the subtree of node
 * 2 is 0.
 *
 * Output: [0, 7, -1, 0]
 *
 *
 * Constraints:
 *
 * 1 <= n == vals.length <= 5 * 10^4
 * 0 <= vals[i] <= 10^5
 * par.length == n
 * par[0] == -1
 * 0 <= par[i] < n for i in [1, n - 1]
 * 1 <= queries.length <= 5 * 10^4
 * queries[j] == [u_j, k_j]
 * 0 <= u_j < n
 * 1 <= k_j <= n
 * The input is generated such that the parent array par represents a valid tree.
 *
 */
public class KthSmallestPathXorSum {

    // value bits: vals[i] <= 10^5 < 2^17, and xor never sets a higher bit
    private static final int BITS = 17;
    private static final int NULL_NODE = 0;

    // flat trie storage; index 0 is the shared null node with count 0
    private int[] zeroChild;
    private int[] oneChild;
    private int[] distinctCount;
    private int nextFree;

    // V0
    // IDEA: MERGEABLE BINARY TRIE OVER THE XOR VALUES, MERGED BOTTOM-UP (post-order DFS)
    //       a query needs an order statistic over the DISTINCT path xors of one subtree,
    //       and a subtree's set is the union of its children's sets plus its own value.
    //       so keep, per node, a binary trie on the 17 bits of the value (vals[i] <= 10^5
    //       < 2^17, and xor keeps every value below 2^17), where each trie node stores
    //       how many distinct values live beneath it.
    //
    //       union = the segment-tree-merge trick: walk only the trie nodes both sides
    //       share and splice the rest in whole. every recursive step discards one node,
    //       so ALL merges together cost O(nodes ever created) = O(n * 17), however
    //       lopsided the tree is (better than small-to-large re-insertion).
    //
    //       distinctness is free: when both sides reach the same leaf they hold the same
    //       value, so one copy is dropped and the leaf count stays 1; internal counts are
    //       recomputed from their two children.
    //
    //       k-th smallest = descend from the root: the 0-branch holds the smaller values,
    //       so if k fits in its count go left, otherwise subtract that count, set the bit
    //       and go right. queries are grouped by node and answered right after that
    //       node's merge finishes, before its trie is merged into its parent's.
    //
    //       NOTE: the tree walk is iterative (a 5 * 10^4-long chain would overflow the
    //             call stack); merge() recurses only 17 levels deep.
    /**
     * time = O((N + Q) * log(maxVal))
     * space = O(N * log(maxVal))
     */
    public int[] kthSmallest(int[] par, int[] vals, int[][] queries) {
        int n = par.length;

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        for (int i = 1; i < n; i++) {
            children.get(par[i]).add(i);
        }

        // pre-order sweep: every node's xor from the root, and a visiting order
        int[] pathXor = new int[n];
        pathXor[0] = vals[0];
        int[] preOrder = new int[n];
        int visited = 0;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            preOrder[visited] = node;
            visited++;
            for (int child : children.get(node)) {
                pathXor[child] = pathXor[node] ^ vals[child];
                stack.push(child);
            }
        }

        // each inserted value creates BITS + 1 trie nodes
        int capacity = n * (BITS + 1) + 1;
        zeroChild = new int[capacity];
        oneChild = new int[capacity];
        distinctCount = new int[capacity];
        nextFree = 1;

        // group the queries by the node they ask about: {k, query index}
        List<List<int[]>> askedAt = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            askedAt.add(new ArrayList<>());
        }
        for (int i = 0; i < queries.length; i++) {
            askedAt.get(queries[i][0]).add(new int[]{queries[i][1], i});
        }

        int[] answer = new int[queries.length];
        int[] trieRootOf = new int[n];

        // reverse pre-order is a valid post-order: every child is done before its parent
        for (int i = n - 1; i >= 0; i--) {
            int node = preOrder[i];
            int root = insert(pathXor[node]);
            for (int child : children.get(node)) {
                root = merge(root, trieRootOf[child], BITS);
                // the child's trie now belongs to this node's trie
                trieRootOf[child] = NULL_NODE;
            }
            trieRootOf[node] = root;

            for (int[] query : askedAt.get(node)) {
                answer[query[1]] = kthValue(root, query[0]);
            }
        }
        return answer;
    }

    /** Builds a fresh single-value trie (one node per bit level plus the leaf); returns its root. */
    private int insert(int value) {
        int root = nextFree;
        int current = nextFree;
        for (int bit = BITS - 1; bit >= 0; bit--) {
            distinctCount[current] = 1;
            int next = current + 1;
            // (value >> bit) & 1 = the bit of value at this level
            if (((value >> bit) & 1) == 1) {
                oneChild[current] = next;
            } else {
                zeroChild[current] = next;
            }
            current = next;
        }
        distinctCount[current] = 1;
        nextFree = current + 1;
        return root;
    }

    /** Merges trie b into trie a (bitsLeft levels remain below); returns the merged root. */
    private int merge(int a, int b, int bitsLeft) {
        if (a == NULL_NODE) {
            return b;
        }
        if (b == NULL_NODE) {
            return a;
        }
        if (bitsLeft == 0) {
            // same leaf = same value: keep one copy, so the count stays "distinct"
            return a;
        }
        zeroChild[a] = merge(zeroChild[a], zeroChild[b], bitsLeft - 1);
        oneChild[a] = merge(oneChild[a], oneChild[b], bitsLeft - 1);
        distinctCount[a] = distinctCount[zeroChild[a]] + distinctCount[oneChild[a]];
        return a;
    }

    /** The k-th smallest distinct value in the trie, or -1 if it holds fewer than k. */
    private int kthValue(int root, int k) {
        if (distinctCount[root] < k) {
            return -1;
        }
        int node = root;
        int result = 0;
        for (int bit = BITS - 1; bit >= 0; bit--) {
            int smallerHalf = zeroChild[node];
            int smallerCount = distinctCount[smallerHalf];
            if (k <= smallerCount) {
                // the answer has a 0 at this bit
                node = smallerHalf;
            } else {
                // skip every value with a 0 here; the answer has a 1 at this bit
                k -= smallerCount;
                result |= (1 << bit);
                node = oneChild[node];
            }
        }
        return result;
    }
}
