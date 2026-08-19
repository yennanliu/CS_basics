package LeetCodeJava.Recursion;

// https://leetcode.com/problems/binary-tree-upside-down/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  156. Binary Tree Upside Down
 *  Medium
 *
 *  Given the root of a binary tree, turn the tree upside down and return the
 *  new root.
 *
 *  You can turn a binary tree upside down with the following steps:
 *
 *   1. The original left child becomes the new root.
 *   2. The original root becomes the new right child.
 *   3. The original right child becomes the new left child.
 *
 *  The mentioned steps are done level by level. It is guaranteed that every
 *  right node has a sibling (a left node with the same parent) and has no
 *  children.
 *
 *
 *  Example 1:
 *
 *  Input: root = [1,2,3,4,5]
 *
 *      1              4
 *     / \            / \
 *    2   3    ->    5   2
 *   / \                / \
 *  4   5              3   1
 *
 *  Output: [4,5,2,null,null,3,1]
 *
 *  Example 2:
 *
 *  Input: root = []
 *  Output: []
 *
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [0, 10].
 *  1 <= Node.val <= 10
 *  Every right node in the tree has a sibling and has no children.
 */
public class BinaryTreeUpsideDown {

    // V0
    // IDEA: ITERATIVE re-linking down the LEFT spine.
    //       walk left; at each node:  node.left = prev.right, node.right = prev
    /**
     * time = O(n)
     * space = O(1)
     */
    public TreeNode upsideDownBinaryTree(TreeNode root) {
        TreeNode cur = root;
        TreeNode parent = null;
        TreeNode parentRight = null;

        while (cur != null) {
            TreeNode next = cur.left;      // remember the next node on the left spine
            cur.left = parentRight;        // old parent's right child becomes new left
            parentRight = cur.right;       // save this node's right for the next round
            cur.right = parent;            // old parent becomes new right child
            parent = cur;
            cur = next;
        }
        return parent; // deepest left node = new root
    }

    // V1
    // IDEA: RECURSION (go to the leftmost node, then rewire on the way back)
    /**
     * time = O(n)
     * space = O(h)  (recursion stack, h = tree height)
     */
    public TreeNode upsideDownBinaryTree_1(TreeNode root) {
        if (root == null || root.left == null) {
            return root;
        }
        TreeNode newRoot = upsideDownBinaryTree_1(root.left);
        // root.left is the (already rewired) subtree's original head
        root.left.left = root.right;
        root.left.right = root;
        root.left = null;
        root.right = null;
        return newRoot;
    }
}
