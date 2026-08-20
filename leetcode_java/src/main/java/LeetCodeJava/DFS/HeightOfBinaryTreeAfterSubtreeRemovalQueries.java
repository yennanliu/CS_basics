package LeetCodeJava.DFS;

// https://leetcode.com/problems/height-of-binary-tree-after-subtree-removal-queries/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2458. Height of Binary Tree After Subtree Removal Queries
 *  Hard
 *
 *  You are given the root of a binary tree with n nodes. Each node is assigned a unique
 *  value from 1 to n. You are also given an array queries of size m.
 *
 *  You have to perform m independent queries on the tree where in the ith query you
 *  remove the subtree rooted at the node with the value queries[i] from the tree. It is
 *  guaranteed that queries[i] will not be equal to the value of the root.
 *
 *  Return an array answer of size m where answer[i] is the height of the tree after
 *  performing the ith query.
 *
 *  Note: the queries are independent (the tree returns to its initial state after each
 *  query) and the height of a tree is the number of edges in the longest simple path
 *  from the root to some node in the tree.
 *
 *  Example 1:
 *    Input: root = [1,3,4,2,null,6,5,null,null,null,null,null,7], queries = [4]
 *    Output: [2]
 *    Explanation: after removing the subtree at 4, the height is 2 (path 1 -> 3 -> 2).
 *
 *  Example 2:
 *    Input: root = [5,8,9,2,1,3,7,4,6], queries = [3,2,4,8]
 *    Output: [3,2,3,2]
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    1 <= Node.val <= n, all values are unique
 *    m == queries.length
 *    1 <= m <= min(n, 10^4)
 *    1 <= queries[i] <= n
 *    queries[i] != root.val
 */
public class HeightOfBinaryTreeAfterSubtreeRemovalQueries {

    // V0
    // IDEA: PRECOMPUTE, FOR EVERY NODE, THE TREE HEIGHT WITHOUT ITS SUBTREE
    //       the queries are independent and there can be 10^4 of them, so recomputing
    //       the height per query is too slow -> answer ALL nodes in two passes.
    //       pass 1 : height[node] = edges down to its deepest descendant.
    //       pass 2 : walk down carrying `rest` = the deepest depth reachable WITHOUT
    //                entering the current node's subtree. Descending into a child,
    //                `rest` picks up this node itself (at `depth`) and the sibling's
    //                branch:  restChild = max(rest, depth, depth + 1 + height[sibling])
    //                and the answer for a node is exactly the `rest` it was handed.
    //       both passes are ITERATIVE - n reaches 10^5 and the tree may be a path.
    /**
     * time = O(n + m)
     * space = O(n)
     */
    public int[] treeQueries(TreeNode root, int[] queries) {

        // ---- pass 1 : subtree heights, via a reversed pre-order ----
        List<TreeNode> order = new ArrayList<>();
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            order.add(node);
            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }

        Map<TreeNode, Integer> height = new HashMap<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            TreeNode node = order.get(i);
            int h = 0;
            if (node.left != null) {
                h = Math.max(h, height.get(node.left) + 1);
            }
            if (node.right != null) {
                h = Math.max(h, height.get(node.right) + 1);
            }
            height.put(node, h);
        }

        // ---- pass 2 : best depth reachable while avoiding each subtree ----
        int n = order.size();
        int[] answer = new int[n + 1]; // node values are 1..n

        Deque<Frame> st = new ArrayDeque<>();
        st.push(new Frame(root, 0, 0));
        while (!st.isEmpty()) {
            Frame f = st.pop();
            TreeNode node = f.node;
            answer[node.val] = f.rest;

            // deleting a child's subtree still leaves this node (at `depth`)
            // and whatever the sibling branch reaches
            int base = Math.max(f.rest, f.depth);
            if (node.left != null) {
                int r = base;
                if (node.right != null) {
                    r = Math.max(r, f.depth + 1 + height.get(node.right));
                }
                st.push(new Frame(node.left, f.depth + 1, r));
            }
            if (node.right != null) {
                int r = base;
                if (node.left != null) {
                    r = Math.max(r, f.depth + 1 + height.get(node.left));
                }
                st.push(new Frame(node.right, f.depth + 1, r));
            }
        }

        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            res[i] = answer[queries[i]];
        }
        return res;
    }

    // traversal frame for the iterative pass 2
    private static class Frame {
        TreeNode node;
        int depth;
        int rest;

        Frame(TreeNode node, int depth, int rest) {
            this.node = node;
            this.depth = depth;
            this.rest = rest;
        }
    }
}
