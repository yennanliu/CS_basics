package LeetCodeJava.Tree;

// https://leetcode.com/problems/construct-binary-search-tree-from-preorder-traversal/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1008. Construct Binary Search Tree from Preorder Traversal
 *  Medium
 *
 *  Given an array of integers preorder, which represents the preorder traversal
 *  of a BST (i.e., binary search tree), construct the tree and return its root.
 *
 *  It is guaranteed that there is always possible to find a binary search tree
 *  with the given requirements for the given test cases.
 *
 *  Example 1:
 *    Input: preorder = [8,5,1,7,10,12]
 *    Output: [8,5,10,1,7,null,12]
 *
 *  Example 2:
 *    Input: preorder = [1,3]
 *    Output: [1,null,3]
 *
 *  Constraints:
 *    1 <= preorder.length <= 100
 *    1 <= preorder[i] <= 1000
 *    All the values of preorder are unique.
 */
public class ConstructBinarySearchTreeFromPreorderTraversal {

    // V0
    // IDEA: RECURSION + UPPER BOUND (single pass)
    //       preorder gives the node BEFORE its subtrees, so we can consume the
    //       array left to right and only need the largest value still allowed
    //       in the current subtree:
    //         - if the next value > bound -> it does NOT belong here, stop
    //         - else consume it as the root, then
    //             left  subtree: values < root.val -> build(root.val)
    //             right subtree: values < bound    -> build(bound)
    /**
     * time = O(N)
     * space = O(N)   // recursion depth (skewed tree)
     */
    private int idx = 0;

    public TreeNode bstFromPreorder(int[] preorder) {
        this.idx = 0;
        return build(preorder, Integer.MAX_VALUE);
    }

    private TreeNode build(int[] preorder, int bound) {
        if (this.idx == preorder.length || preorder[this.idx] > bound) {
            return null;
        }
        TreeNode node = new TreeNode(preorder[this.idx]);
        this.idx++;
        node.left = build(preorder, node.val);
        node.right = build(preorder, bound);
        return node;
    }
}
