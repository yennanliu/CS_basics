package LeetCodeJava.Trie;

// https://leetcode.com/problems/maximum-xor-of-two-non-overlapping-subtrees/description/

import java.util.*;

/**
 * 2479. Maximum XOR of Two Non-Overlapping Subtrees
 * Hard
 *
 * There is an undirected tree with n nodes labeled from 0 to n - 1. You are given the integer n and a 2D integer array edges of length n - 1, where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree. The root of the tree is the node labeled 0.
 *
 * Each node has an associated value. You are given an array values of length n, where values[i] is the value of the ith node.
 *
 * Select any two non-overlapping subtrees. Your score is the bitwise XOR of the sum of the values within those subtrees.
 *
 * Return the maximum possible score you can achieve. If it is impossible to find two nonoverlapping subtrees, return 0.
 *
 * Note that:
 *
 * The subtree of a node is the tree consisting of that node and all of its descendants.
 * Two subtrees are non-overlapping if they do not share any common node.
 *
 *
 * Example 1:
 *
 * Input: n = 6, edges = [[0,1],[0,2],[1,3],[1,4],[2,5]], values = [2,8,3,6,2,5]
 * Output: 24
 * Explanation: Node 1's subtree has sum of values 16, while node 2's subtree has sum of values 8, so choosing these nodes will yield a score of 16 XOR 8 = 24. It can be proved that is the maximum possible score we can obtain.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1],[1,2]], values = [4,6,1]
 * Output: 0
 * Explanation: There is no possible way to select two non-overlapping subtrees, so we just return 0.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 10^4
 * edges.length == n - 1
 * 0 <= ai, bi < n
 * values.length == n
 * 1 <= values[i] <= 10^9
 * It is guaranteed that edges represents a valid tree.
 *
 */
public class MaximumXorOfTwoNonOverlappingSubtrees {

    // V0
    // IDEA: SUBTREE SUMS + BINARY TRIE (QUERY ON ENTRY, INSERT ON EXIT)
    //       two subtrees are disjoint iff neither root is an ancestor of the other. During a DFS,
    //       query node v when first reached and insert its subtree sum only after its whole
    //       subtree is done: the trie then holds exactly the subtrees disjoint from v's.
    //       Both passes are iterative because the tree can be a 5 * 10^4 long chain.
    /**
     * time = O(N * B), B = 46 bits
     * space = O(N * B)
     */
    public long maxXor(int n, int[][] edges, int[] values) {
        // subtree sums reach 5 * 10^4 * 10^9 < 2^46
        final int BITS = 46;

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // pass 1: BFS order from root 0, then accumulate sums children-first
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n];
        int head = 0;
        int tail = 0;
        boolean[] visited = new boolean[n];
        order[tail++] = 0;
        visited[0] = true;
        while (head < tail) {
            int node = order[head++];
            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    parent[next] = node;
                    order[tail++] = next;
                }
            }
        }
        long[] subtreeSum = new long[n];
        for (int i = 0; i < n; i++) {
            subtreeSum[i] = values[i];
        }
        for (int i = n - 1; i > 0; i--) {
            int node = order[i];
            subtreeSum[parent[node]] += subtreeSum[node];
        }

        // pass 2: DFS with an explicit stack; frame = {node, parent, exiting ? 1 : 0}
        TrieNode root = new TrieNode();
        boolean trieEmpty = true;
        long best = 0;
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {0, -1, 0});
        while (!stack.isEmpty()) {
            int[] frame = stack.pop();
            int node = frame[0];
            int from = frame[1];
            boolean exiting = frame[2] == 1;

            if (exiting) {
                // the whole subtree of node is finished: it may now pair with later nodes
                insert(root, subtreeSum[node], BITS);
                trieEmpty = false;
                continue;
            }

            if (!trieEmpty) {
                best = Math.max(best, queryMaxXor(root, subtreeSum[node], BITS));
            }
            // revisit this node after its children, to insert it
            stack.push(new int[] {node, from, 1});
            for (int next : graph.get(node)) {
                if (next != from) {
                    stack.push(new int[] {next, node, 0});
                }
            }
        }
        return best;
    }

    /** Binary trie node; children[b] follows bit b. */
    private static class TrieNode {
        TrieNode[] children = new TrieNode[2];
    }

    /** Inserts value into the trie, most significant bit first. */
    private void insert(TrieNode root, long value, int bits) {
        TrieNode current = root;
        for (int i = bits - 1; i >= 0; i--) {
            int bit = (int) ((value >> i) & 1);
            if (current.children[bit] == null) {
                current.children[bit] = new TrieNode();
            }
            current = current.children[bit];
        }
    }

    /** Largest value XOR x over everything in the (non-empty) trie. */
    private long queryMaxXor(TrieNode root, long x, int bits) {
        TrieNode current = root;
        long result = 0;
        for (int i = bits - 1; i >= 0; i--) {
            int bit = (int) ((x >> i) & 1);
            int wanted = 1 - bit;
            // prefer the opposite bit: it sets bit i of the XOR
            if (current.children[wanted] != null) {
                result |= 1L << i;
                current = current.children[wanted];
            } else {
                current = current.children[bit];
            }
        }
        return result;
    }
}
