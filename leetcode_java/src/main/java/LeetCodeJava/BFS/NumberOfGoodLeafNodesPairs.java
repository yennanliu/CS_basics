package LeetCodeJava.BFS;

// https://leetcode.com/problems/number-of-good-leaf-nodes-pairs/

import java.util.*;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1530. Number of Good Leaf Nodes Pairs
 *  Medium
 *
 *  You are given the root of a binary tree and an integer distance. A pair of
 *  two different leaf nodes of a binary tree is said to be good if the length
 *  of the shortest path between them is less than or equal to distance.
 *
 *  Return the number of good leaf node pairs in the tree.
 *
 *  Example 1:
 *   Input: root = [1,2,3,null,4], distance = 3
 *   Output: 1
 *   Explanation: The leaf nodes are 3 and 4, shortest path between them = 3.
 *
 *  Example 2:
 *   Input: root = [1,2,3,4,5,6,7], distance = 3
 *   Output: 2
 *
 *  Constraints:
 *   The number of nodes in the tree is in the range [1, 2^10].
 *   1 <= Node.val <= 100
 *   1 <= distance <= 10
 */
public class NumberOfGoodLeafNodesPairs {

    // V0
    // IDEA: post order DFS, each node returns the distances (from itself) of the
    //       leaves in its subtree; pairs are counted when merging left & right lists
    /**
     * time = O(n * d^2), d = distance (each list has at most d entries)
     * space = O(n)
     */
    private int res = 0;

    public int countPairs(TreeNode root, int distance) {
        this.res = 0;
        collect(root, distance);
        return this.res;
    }

    // returns list of leaf depths (relative to `node`) that are <= distance
    private List<Integer> collect(TreeNode node, int distance) {
        List<Integer> depths = new ArrayList<>();
        if (node == null) {
            return depths;
        }
        if (node.left == null && node.right == null) {
            depths.add(1);
            return depths;
        }

        List<Integer> left = collect(node.left, distance);
        List<Integer> right = collect(node.right, distance);

        // count cross pairs going through the current node
        for (Integer l : left) {
            for (Integer r : right) {
                if (l + r <= distance) {
                    this.res += 1;
                }
            }
        }

        for (Integer l : left) {
            if (l + 1 <= distance) {
                depths.add(l + 1);
            }
        }
        for (Integer r : right) {
            if (r + 1 <= distance) {
                depths.add(r + 1);
            }
        }
        return depths;
    }

    // V1
    // IDEA: build an undirected graph (node <-> parent), then BFS from every leaf
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int countPairs_1(TreeNode root, int distance) {
        Map<TreeNode, List<TreeNode>> graph = new HashMap<>();
        List<TreeNode> leaves = new ArrayList<>();
        buildGraph(root, null, graph, leaves);

        int cnt = 0;
        for (TreeNode start : leaves) {
            Queue<TreeNode> q = new LinkedList<>();
            Set<TreeNode> visited = new HashSet<>();
            q.add(start);
            visited.add(start);
            int dist = 0;

            while (!q.isEmpty() && dist < distance) {
                int size = q.size();
                dist += 1;
                for (int i = 0; i < size; i++) {
                    TreeNode cur = q.poll();
                    List<TreeNode> nextList = graph.get(cur);
                    if (nextList == null) {
                        continue;
                    }
                    for (TreeNode nxt : nextList) {
                        if (visited.contains(nxt)) {
                            continue;
                        }
                        visited.add(nxt);
                        if (nxt.left == null && nxt.right == null) {
                            cnt += 1;
                        }
                        q.add(nxt);
                    }
                }
            }
        }
        // every good pair is counted twice (once from each leaf)
        return cnt / 2;
    }

    private void buildGraph(TreeNode node, TreeNode parent,
                            Map<TreeNode, List<TreeNode>> graph, List<TreeNode> leaves) {
        if (node == null) {
            return;
        }
        if (!graph.containsKey(node)) {
            graph.put(node, new ArrayList<TreeNode>());
        }
        if (parent != null) {
            graph.get(node).add(parent);
            graph.get(parent).add(node);
        }
        if (node.left == null && node.right == null) {
            leaves.add(node);
        }
        buildGraph(node.left, node, graph, leaves);
        buildGraph(node.right, node, graph, leaves);
    }
}
