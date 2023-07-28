package LeetCodeJava.Recursion;

// https://leetcode.com/problems/validate-binary-search-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.Deque;
import java.util.LinkedList;

/**
 *  A valid BST is defined as follows:
 *
 *  1) The left subtree of a node contains only nodes with keys less than the node's key.
 *  2) The right subtree of a node contains only nodes with keys greater than the node's key.
 *  3) Both the left and right subtrees must also be binary search trees.
 */

public class ValidateBinarySearchTree {

    // V0
    // IDEA : DFS
    // TODO : fix below
//    public boolean isValidBST(TreeNode root) {
//
//        if (root == null){
//            return true;
//        }
//
//        if (root.left == null && root.right == null){
//            return true;
//        }
//
//        if (!check(root, Integer.MAX_VALUE-1, Integer.MIN_VALUE)){
//            return false;
//        }
//
//        return true;
//    }
//
//    private Boolean check(TreeNode root, Integer curMax, Integer curMin){
//
//        if (root == null){
//            return true;
//        }
//
//        if (root.val < curMin || root.val > curMax){
//            return false;
//        }
//
//        return check(root.left, Math.max(root.val, Integer.MIN_VALUE), curMin)
//                && check(root.right, curMax, Math.min(root.val, Integer.MAX_VALUE));
//    }

    // V1
    // IDEA : Recursive Traversal with Valid Range
    // https://leetcode.com/problems/validate-binary-search-tree/editorial/
    public boolean validate(TreeNode root, Integer low, Integer high) {
        // Empty trees are valid BSTs.
        if (root == null) {
            return true;
        }
        // The current node's value must be between low and high.
        if ((low != null && root.val <= low) || (high != null && root.val >= high)) {
            return false;
        }
        // The left and right subtree must also be valid.
        return validate(root.right, root.val, high) && validate(root.left, low, root.val);
    }

    public boolean isValidBST_2(TreeNode root) {
        return validate(root, null, null);
    }

    // V2
    // IDEA : Iterative Traversal with Valid Range
    // https://leetcode.com/problems/validate-binary-search-tree/editorial/
    private Deque<TreeNode> stack = new LinkedList();
    private Deque<Integer> upperLimits = new LinkedList();
    private Deque<Integer> lowerLimits = new LinkedList();

    public void update(TreeNode root, Integer low, Integer high) {
        stack.add(root);
        lowerLimits.add(low);
        upperLimits.add(high);
    }

    public boolean isValidBST_3(TreeNode root) {
        Integer low = null, high = null, val;
        update(root, low, high);

        while (!stack.isEmpty()) {
            root = stack.poll();
            low = lowerLimits.poll();
            high = upperLimits.poll();

            if (root == null) continue;
            val = root.val;
            if (low != null && val <= low) {
                return false;
            }
            if (high != null && val >= high) {
                return false;
            }
            update(root.right, val, high);
            update(root.left, low, val);
        }
        return true;
    }


    // V3
    // IDEA : Recursive Inorder Traversal
    // https://leetcode.com/problems/validate-binary-search-tree/editorial/
    // We use Integer instead of int as it supports a null value.
    private Integer prev;

    public boolean isValidBST_4(TreeNode root) {
        prev = null;
        return inorder(root);
    }

    private boolean inorder(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (!inorder(root.left)) {
            return false;
        }
        if (prev != null && root.val <= prev) {
            return false;
        }
        prev = root.val;
        return inorder(root.right);
    }

}
