package LeetCodeJava.Tree;

// https://leetcode.com/problems/boundary-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  545. Boundary of Binary Tree
 *  Medium
 *
 *  Given a binary tree, return the values of its boundary in anti-clockwise
 *  direction starting from root. Boundary includes left boundary, leaves and
 *  right boundary in order without duplicate nodes.
 *
 *  Left boundary is the path from root to the left-most node; right boundary
 *  is the path from root to the right-most node. If the root has no left
 *  (resp. right) subtree, the root itself is the left (resp. right) boundary.
 *
 *  Example 1:
 *
 *  Input: root = [1,null,2,3,4]
 *  Output: [1,3,4,2]
 *
 *  Example 2:
 *
 *  Input: root = [1,2,3,4,5,6,null,null,null,7,8,9,10]
 *  Output: [1,2,4,7,8,9,10,6,3]
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 10^4].
 *  -1000 <= Node.val <= 1000
 */
public class BoundaryOfBinaryTree {

    // V0
    // IDEA: 3 passes -> left boundary (top down, leaves excluded),
    //       all leaves (left to right), right boundary (bottom up, leaves excluded)
    /**
     * time = O(n)
     * space = O(n)
     */
    public List<Integer> boundaryOfBinaryTree(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        if (!isLeaf(root)) {
            res.add(root.val);
        }

        // left boundary (skip leaves, they are collected by addLeaves)
        TreeNode cur = root.left;
        while (cur != null) {
            if (!isLeaf(cur)) {
                res.add(cur.val);
            }
            cur = (cur.left != null) ? cur.left : cur.right;
        }

        // leaves, left to right
        addLeaves(res, root);

        // right boundary, collected top down then reversed
        List<Integer> rightBoundary = new ArrayList<>();
        cur = root.right;
        while (cur != null) {
            if (!isLeaf(cur)) {
                rightBoundary.add(cur.val);
            }
            cur = (cur.right != null) ? cur.right : cur.left;
        }
        Collections.reverse(rightBoundary);
        res.addAll(rightBoundary);

        return res;
    }

    private boolean isLeaf(TreeNode node) {
        return node.left == null && node.right == null;
    }

    private void addLeaves(List<Integer> res, TreeNode node) {
        if (node == null) {
            return;
        }
        if (isLeaf(node)) {
            // a single-node tree: root already handled as "not added" above
            res.add(node.val);
            return;
        }
        addLeaves(res, node.left);
        addLeaves(res, node.right);
    }
}
