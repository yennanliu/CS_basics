package LeetCodeJava.Recursion;

// https://leetcode.com/problems/validate-binary-search-tree/
/**
 *  98. Validate Binary Search Tree
 * Solved
 * Medium
 * Topics
 * Companies
 * Given the root of a binary tree, determine if it is a valid binary search tree (BST).
 *
 * A valid BST is defined as follows:
 *
 * The left
 * subtree
 *  of a node contains only nodes with keys less than the node's key.
 * The right subtree of a node contains only nodes with keys greater than the node's key.
 * Both the left and right subtrees must also be binary search trees.
 *
 *
 * Example 1:
 *
 *
 * Input: root = [2,1,3]
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [5,1,4,null,null,3,6]
 * Output: false
 * Explanation: The root node's value is 5 but its right child's value is 4.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -231 <= Node.val <= 231 - 1
 *
 */
import LeetCodeJava.DataStructure.TreeNode;
import dev.workspace11;

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
    // IDEA : DFS + BST property + setup smallest, biggest val
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }

        /** NOTE !!!
         *
         *  since for valid BST, all its sub-tree needs to be valid BST as well,
         *  so we need to keep comparing sub-tree with "its root and its sub-tree"
         *  -> so we need to set up a "tree-wide" smallest, and biggest value,
         *  -> for "left sub tree val < root.val < right sub tree val" check
         *
         *  if any sub-tree failed to meet BST requirement
         *  (e.g. left sub tree val < root.val < right sub tree val),
         *  then this tree is NOT a BST
         */
        /**
         *  NOTE !!!
         *
         *
         *
         *  Use `long` to handle edge cases for Integer.MIN_VALUE and Integer.MAX_VALUE
         *  -> use `long` to handle overflow issue (NOT use int type)
         */
        long smallest_val = Long.MIN_VALUE;
        long biggest_val = Long.MAX_VALUE;

        return check_(root, smallest_val, biggest_val);
    }

    public boolean check_(TreeNode root, long smallest_val, long biggest_val) {

        if (root == null) {
            return true;
        }

//        if (root.val <= smallest_val || root.val >= biggest_val) {
//            return false;
//        }

        if (root.val <= smallest_val){
            return false;
        }

        if (root.val >= biggest_val){
            return false;
        }

        return check_(root.left, smallest_val, root.val) &&
                check_(root.right, root.val, biggest_val);
    }

    // V0-1
    // IDEA: DFS + BST property
    public boolean isValidBST_0_1(TreeNode root) {
        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

        // dfs call
        /**
         *  NOTE !!!
         *
         *   use `long` type for setting min, max val
         *
         */
        return validBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean validBSTHelper(TreeNode root, long smallestVal, long biggestVal){
        // edge
        if(root == null){
            return true;
        }

        // validate
        if(root.val >= biggestVal || root.val <= smallestVal){
            return false;
        }

        return validBSTHelper(root.left, smallestVal, root.val) &&
                validBSTHelper(root.right, root.val, biggestVal);
    }

    // V0-2
    // IDEA: RECURSION + BST property (GPT)
    public boolean isValidBST_0_2(TreeNode root) {
        return isValidateBST(root, null, null);
    }

    private boolean isValidateBST(TreeNode root, Integer min, Integer max) {
        // Base case: an empty tree is a valid BST
        if (root == null) {
            return true;
        }

        // Check current node against the range constraints
        if ((min != null && root.val <= min) || (max != null && root.val >= max)) {
            return false;
        }

        // Recursively check left and right subtrees
        return isValidateBST(root.left, min, root.val) && isValidateBST(root.right, root.val, max);
    }

    // V0-3
    // IDEA: DFS + maintain `max till now`, `min till now` val (gpt)
    public boolean isValidBST_0_3(TreeNode root) {
        return checkIsValidBst(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public boolean checkIsValidBst(TreeNode node, long min, long max) {
        if (node == null) {
            return true;
        }

        // node value must be in (min, max)
        if (node.val <= min || node.val >= max) {
            return false;
        }

        // check left and right subtree with updated bounds
        return checkIsValidBst(node.left, min, node.val) &&
                checkIsValidBst(node.right, node.val, max);
    }

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
