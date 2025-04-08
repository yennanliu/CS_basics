package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-preorder-traversal/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 144. Binary Tree Preorder Traversal
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, return the preorder traversal of its nodes' values.
 *
 *
 *
 * Example 1:
 *
 * Input: root = [1,null,2,3]
 *
 * Output: [1,2,3]
 *
 * Explanation:
 *
 *
 *
 * Example 2:
 *
 * Input: root = [1,2,3,4,5,null,8,null,null,6,7,9]
 *
 * Output: [1,2,4,5,6,7,3,8,9]
 *
 * Explanation:
 *
 *
 *
 * Example 3:
 *
 * Input: root = []
 *
 * Output: []
 *
 * Example 4:
 *
 * Input: root = [1]
 *
 * Output: [1]
 *
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 100].
 * -100 <= Node.val <= 100
 *
 *
 */

public class BinaryTreePreorderTraversal {

    // V0
    // IDEA: DFS
    List<Integer> res = new ArrayList<>();

    public List<Integer> preorderTraversal(TreeNode root) {
        // edge
        if (root == null) {
            return res;
        }
        if (root.left == null && root.right == null) {
            res.add(root.val);
            return res;
        }

        preOrderDfs(root);
        return res;
    }

    public void preOrderDfs(TreeNode root) {
        if (root == null) {
            return;
        }
        // preorder : root -> left  -> right
        res.add(root.val);
        preOrderDfs(root.left);
        preOrderDfs(root.right);
    }

    // V0-1
    // IDEA: BFS

    // V1

    // V2

}
