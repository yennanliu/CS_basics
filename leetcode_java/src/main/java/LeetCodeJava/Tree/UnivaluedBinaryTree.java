package LeetCodeJava.Tree;

// https://leetcode.com/problems/univalued-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  965. Univalued Binary Tree
 *  Easy
 *
 *  A binary tree is uni-valued if every node in the tree has the same value.
 *
 *  Given the root of a binary tree, return true if the given tree is
 *  uni-valued, or false otherwise.
 *
 *  Example 1:
 *    Input: root = [1,1,1,1,1,null,1]
 *    Output: true
 *
 *  Example 2:
 *    Input: root = [2,2,2,5,2]
 *    Output: false
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 100].
 *    0 <= Node.val < 100
 */
public class UnivaluedBinaryTree {

    // V0
    // IDEA: DFS - compare every node against the root value
    /**
     * time = O(N)
     * space = O(H)   // recursion stack, H = tree height
     */
    public boolean isUnivalTree(TreeNode root) {
        if (root == null) {
            return true;
        }
        return check(root, root.val);
    }

    private boolean check(TreeNode node, int val) {
        if (node == null) {
            return true;
        }
        if (node.val != val) {
            return false;
        }
        return check(node.left, val) && check(node.right, val);
    }

    // V1
    // IDEA: DFS - compare each node only with its direct children
    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean isUnivalTree_1(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (root.left != null && root.left.val != root.val) {
            return false;
        }
        if (root.right != null && root.right.val != root.val) {
            return false;
        }
        return isUnivalTree_1(root.left) && isUnivalTree_1(root.right);
    }
}
