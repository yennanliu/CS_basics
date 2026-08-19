package LeetCodeJava.Recursion;

// https://leetcode.com/problems/second-minimum-node-in-a-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  671. Second Minimum Node In a Binary Tree
 *  Easy
 *
 *  Given a non-empty special binary tree consisting of nodes with the
 *  non-negative value, where each node in this tree has exactly two or zero
 *  sub-node. If the node has two sub-nodes, then this node's value is the
 *  smaller value among its two sub-nodes. More formally, the property
 *  root.val = min(root.left.val, root.right.val) always holds.
 *
 *  Given such a binary tree, output the second minimum value in the set made
 *  of all the nodes' value in the whole tree. If no such second minimum value
 *  exists, output -1 instead.
 *
 *
 *  Example 1:
 *
 *  Input: root = [2,2,5,null,null,5,7]
 *  Output: 5
 *  Explanation: the smallest value is 2, the second smallest value is 5.
 *
 *  Example 2:
 *
 *  Input: root = [2,2,2]
 *  Output: -1
 *  Explanation: the smallest value is 2, but there isn't any second smallest.
 *
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 25].
 *  1 <= Node.val <= 2^31 - 1
 *  root.val == min(root.left.val, root.right.val) for each internal node.
 */
public class SecondMinimumNodeInABinaryTree {

    // V0
    // IDEA: DFS + PRUNE.
    //       root.val is the global minimum. Look for the smallest value that
    //       is strictly greater than root.val; once a subtree's root already
    //       exceeds root.val we can stop, since it is that subtree's minimum.
    private long secondMin = Long.MAX_VALUE;
    private int rootVal = 0;

    /**
     * time = O(n)
     * space = O(h)   (h = tree height, recursion stack)
     */
    public int findSecondMinimumValue(TreeNode root) {
        if (root == null) {
            return -1;
        }
        this.rootVal = root.val;
        this.secondMin = Long.MAX_VALUE;
        dfs(root);
        return this.secondMin == Long.MAX_VALUE ? -1 : (int) this.secondMin;
    }

    private void dfs(TreeNode node) {
        if (node == null) {
            return;
        }
        if (node.val > this.rootVal) {
            // this subtree's min is node.val -> no need to go deeper
            this.secondMin = Math.min(this.secondMin, node.val);
            return;
        }
        dfs(node.left);
        dfs(node.right);
    }
}
