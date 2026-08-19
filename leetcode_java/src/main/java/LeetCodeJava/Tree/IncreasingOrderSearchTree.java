package LeetCodeJava.Tree;

// https://leetcode.com/problems/increasing-order-search-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  897. Increasing Order Search Tree
 *  Easy
 *
 *  Given the root of a binary search tree, rearrange the tree in in-order so
 *  that the leftmost node in the tree is now the root of the tree, and every
 *  node has no left child and only one right child.
 *
 *  Example 1:
 *
 *  Input: root = [5,3,6,2,4,null,8,1,null,null,null,7,9]
 *  Output: [1,null,2,null,3,null,4,null,5,null,6,null,7,null,8,null,9]
 *
 *  Example 2:
 *
 *  Input: root = [5,1,7]
 *  Output: [1,null,5,null,7]
 *
 *  Constraints:
 *
 *  The number of nodes in the given tree will be in the range [1, 100].
 *  0 <= Node.val <= 1000
 */
public class IncreasingOrderSearchTree {

    private TreeNode prev;

    // V0
    // IDEA: in-order DFS with a "prev" pointer -> relink each visited node as
    //       the right child of the previously visited one, clearing left links.
    //       A dummy head keeps the answer reachable.
    /**
     * time = O(n)
     * space = O(h)
     */
    public TreeNode increasingBST(TreeNode root) {
        TreeNode dummy = new TreeNode(-1);
        this.prev = dummy;
        inOrder(root);
        return dummy.right;
    }

    private void inOrder(TreeNode node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);

        node.left = null;
        this.prev.right = node;
        this.prev = node;

        inOrder(node.right);
    }
}
