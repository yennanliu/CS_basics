package LeetCodeJava.Recursion;

// https://leetcode.com/problems/balanced-binary-tree/

// A height-balanced binary tree is a binary tree in which the depth of the two subtrees of every node never differs by more than one.

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Arrays;
import java.util.List;

public class BalancedBinaryTree {

    // V0
//    public boolean isBalanced(TreeNode root) {
//
//        List<Integer> cache = Arrays.asList();
//
//        return true;
//    }
//
//    private int getDepth(TreeNode node){
//
//        if (node == null){
//            return 0;
//        }
//
//        if (node.left != null){
//            return getDepth(node.left) + 1;
//        }
//
//        if (node.right != null){
//            return getDepth(node.right) + 1;
//        }
//
//        return 0;
//    }

    // V1
    // IDEA :  TOP DOWN RECURSION
    // https://leetcode.com/problems/balanced-binary-tree/editorial/
    // Recursively obtain the height of a tree. An empty tree has -1 height
    private int height(TreeNode root) {
        // An empty tree has height -1
        if (root == null) {
            return -1;
        }
        return 1 + Math.max(height(root.left), height(root.right));
    }

    public boolean isBalanced(TreeNode root) {
        // An empty tree satisfies the definition of a balanced tree
        if (root == null) {
            return true;
        }

        // Check if subtrees have height within 1. If they do, check if the
        // subtrees are balanced
        return Math.abs(height(root.left) - height(root.right)) < 2
                && isBalanced(root.left)
                && isBalanced(root.right);
    }

    // V2
    // IDEA :  BOTTOM UP RECURSION
    // https://leetcode.com/problems/balanced-binary-tree/editorial/

    // define custom data structure
    final class TreeInfo {
        public final int height;
        public final boolean balanced;

        public TreeInfo(int height, boolean balanced) {
            this.height = height;
            this.balanced = balanced;
        }
    }

    // the tree's height in a reference variable.
    private TreeInfo isBalancedTreeHelper(TreeNode root) {
        // An empty tree is balanced and has height = -1
        if (root == null) {
            return new TreeInfo(-1, true);
        }

        // Check subtrees to see if they are balanced.
        TreeInfo left = isBalancedTreeHelper(root.left);
        if (!left.balanced) {
            return new TreeInfo(-1, false);
        }
        TreeInfo right = isBalancedTreeHelper(root.right);
        if (!right.balanced) {
            return new TreeInfo(-1, false);
        }

        // Use the height obtained from the recursive calls to
        // determine if the current node is also balanced.
        if (Math.abs(left.height - right.height) < 2) {
            return new TreeInfo(Math.max(left.height, right.height) + 1, true);
        }
        return new TreeInfo(-1, false);
    }

    public boolean isBalanced_2(TreeNode root) {
        return isBalancedTreeHelper(root).balanced;
    }

}
