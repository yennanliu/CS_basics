package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/recover-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 99. Recover Binary Search Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary search tree (BST), where the values of exactly two nodes of the tree were swapped by mistake. Recover the tree without changing its structure.
 *
 *Example 1:
 *
 *
 * Input: root = [1,3,null,null,2]
 * Output: [3,1,null,null,2]
 * Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
 * Example 2:
 *
 *
 * Input: root = [3,1,4,null,null,2]
 * Output: [2,1,4,null,null,3]
 * Explanation: 2 cannot be in the right subtree of 3 because 2 < 3. Swapping 2 and 3 makes the BST valid.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 1000].
 * -231 <= Node.val <= 231 - 1
 *
 *
 * Follow up: A solution using O(n) space is pretty straight-forward. Could you devise a constant O(1) space solution?
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 *
 */
public class RecoverBinarySearchTree {

    // V0
//    public void recoverTree(TreeNode root) {
//
//    }

    // V1
    // https://leetcode.com/problems/recover-binary-search-tree/solutions/7021623/beats-100-beginner-friendly-explanation-gjz5a/
    TreeNode first;
    TreeNode second;
    TreeNode prev;
    public void recoverTree_1(TreeNode root) {
        helper(root);
        // Swap the values of the two misplaced nodes
        int temp = first.val;
        first.val = second.val;
        second.val = temp;
    }

    void helper(TreeNode node) {
        if (node == null)
            return;

        // Traverse left subtree
        helper(node.left);

        // Detect swapped nodes
        if (prev != null && prev.val > node.val) {
            if (first == null) {
                first = prev;
            }
            second = node;
        }

        prev = node;

        // Traverse right subtree
        helper(node.right);
    }


    // V2
    TreeNode prev_2 = null;
    TreeNode first_2 = null;
    TreeNode second_2 = null;

    void inorder(TreeNode root) {
        if (root == null)
            return;
        inorder(root.left);
        if (prev_2 != null && root.val < prev_2.val) {
            if (first_2 == null)
                first_2 = prev_2;
            second_2 = root;
        }
        prev_2 = root;
        inorder(root.right);
    }

    public void recoverTree_2(TreeNode root) {
        if (root == null)
            return;
        inorder(root);
        int temp = first_2.val;
        first_2.val = second_2.val;
        second_2.val = temp;
    }


}
