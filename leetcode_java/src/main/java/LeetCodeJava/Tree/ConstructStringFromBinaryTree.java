package LeetCodeJava.Tree;

// https://leetcode.com/problems/construct-string-from-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  606. Construct String from Binary Tree
 *  Easy
 *
 *  Given the root of a binary tree, construct a string consisting of
 *  parenthesis and integers from a binary tree with the preorder traversal
 *  way, and return it.
 *
 *  Omit all the empty parenthesis pairs that do not affect the one-to-one
 *  mapping relationship between the string and the original binary tree.
 *
 *  Example 1:
 *
 *  Input: root = [1,2,3,4]
 *  Output: "1(2(4))(3)"
 *
 *  Example 2:
 *
 *  Input: root = [1,2,3,null,4]
 *  Output: "1(2()(4))(3)"
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 10^4].
 *  -1000 <= Node.val <= 1000
 */
public class ConstructStringFromBinaryTree {

    // V0
    // IDEA: pre-order DFS with 3 cases -> leaf, no right child (drop the
    //       trailing "()"), right child present (keep "()" for a null left)
    /**
     * time = O(n)
     * space = O(n)
     */
    public String tree2str(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        dfs(root, sb);
        return sb.toString();
    }

    private void dfs(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.val);

        // case 1: leaf -> nothing more
        if (node.left == null && node.right == null) {
            return;
        }

        // left part is always printed when any child exists
        sb.append('(');
        dfs(node.left, sb);
        sb.append(')');

        // case 2: no right child -> omit the empty right pair
        if (node.right != null) {
            sb.append('(');
            dfs(node.right, sb);
            sb.append(')');
        }
    }
}
