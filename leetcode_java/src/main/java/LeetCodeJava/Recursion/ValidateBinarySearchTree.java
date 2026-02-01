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
    /**
     * time = O(N)
     * space = O(H)
     */
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

        /** NOTE !!!!
         *
         *   below is WRONG
         *
         *   -> Instead of below logic,
         *     we SHOULD keep tracking `smallest`, `biggest` val over all nodes
         *     (as logic above)
         *     and keep validating each node with `smallest`, `biggest`.
         *     that's the ONLY way to validate BST
         *
         *  ----
         *
         *   Reason:
         *
         *   Perfect — this is one of the most common pitfalls
         *   with the BST validation problem.
         *   Let’s visualize why just checking the
         *   immediate children (root.left and root.right) is insufficient.
         *
         * ⸻
         *
         * ❌ Example 1: Fails on deeper left child in the right subtree
         *
         *        5
         *       / \
         *      3   8
         *         /
         *        4   <-- Problem here
         *
         * 	•	Immediate checks:
         * 	•	8 > 5 ✅
         * 	•	3 < 5 ✅
         * 	•	4 < 8 ✅
         * 	•	Code thinks it’s fine.
         * 	•	But in a valid BST, all nodes in the right subtree must be > 5.
         * Here, 4 < 5 violates the BST rule, but we never checked against the ancestor’s value.
         *
         * ⸻
         *
         * ❌ Example 2: Fails on deeper right child in the left subtree
         *
         *        10
         *       /  \
         *      5    15
         *       \
         *        12   <-- Problem here
         *
         * 	•	Immediate checks:
         * 	•	5 < 10 ✅
         * 	•	15 > 10 ✅
         * 	•	12 > 5 ✅
         * 	•	Code thinks it’s fine.
         * 	•	But in a valid BST, all nodes in the left subtree must be < 10.
         * Here, 12 > 10 violates the BST rule, but we only compared 12 > 5.
         *
         * ⸻
         *
         * ❌ Example 3: Edge case with equal values
         *
         *        2
         *       / \
         *      2   3
         *
         * 	•	Immediate checks:
         * 	•	Left child: 2 <= 2 ❌
         * 	•	Your code catches this one.
         * But if you had a duplicate deeper in the subtree, it would escape detection.
         *
         * ⸻
         *
         * ✅ Why range checking works
         *
         * Instead of only comparing a node to its parent,
         * we enforce the global valid range carried down from ancestors:
         * 	•	Left subtree of root → must be (minVal, root.val)
         * 	•	Right subtree of root → must be (root.val, maxVal)
         *
         * -> That’s why the minVal / maxVal approach
         *  (or inorder traversal) is the only correct way.
         *
         *
         */
        // check `current` node
        //        if(root.right != null && root.val >= root.right.val){
        //            return false;
        //        }
        //        if(root.left != null && root.val <= root.left.val){
        //            return false;
        //        }


        return check_(root, smallest_val, biggest_val);
    }

    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
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

    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean isValidBST_0_3(TreeNode root) {
        return checkIsValidBst(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
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

    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean isValidBST_2(TreeNode root) {
        return validate(root, null, null);
    }

    // V2
    // IDEA : Iterative Traversal with Valid Range
    // https://leetcode.com/problems/validate-binary-search-tree/editorial/
    private Deque<TreeNode> stack = new LinkedList();
    private Deque<Integer> upperLimits = new LinkedList();
    private Deque<Integer> lowerLimits = new LinkedList();

    /**
     * time = O(N)
     * space = O(H)
     */
    public void update(TreeNode root, Integer low, Integer high) {
        stack.add(root);
        lowerLimits.add(low);
        upperLimits.add(high);
    }

    /**
     * time = O(N)
     * space = O(H)
     */
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

    /**
     * time = O(N)
     * space = O(H)
     */
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
