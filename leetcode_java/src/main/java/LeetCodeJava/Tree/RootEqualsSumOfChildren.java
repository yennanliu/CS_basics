package LeetCodeJava.Tree;

// https://leetcode.com/problems/root-equals-sum-of-children/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2236. Root Equals Sum of Children
 *  Easy
 *
 *  You are given the root of a binary tree that consists of exactly 3 nodes:
 *  the root, its left child, and its right child.
 *
 *  Return true if the value of the root is equal to the sum of the values of
 *  its two children, or false otherwise.
 *
 *  Example 1:
 *    Input: root = [10,4,6]
 *    Output: true
 *    Explanation: The values of the root, its left child, and its right child
 *                 are 10, 4, and 6. 10 == 4 + 6, so we return true.
 *
 *  Example 2:
 *    Input: root = [5,3,1]
 *    Output: false
 *    Explanation: 5 != 3 + 1.
 *
 *  Constraints:
 *    The tree consists only of the root, its left child, and its right child.
 *    -100 <= Node.val <= 100
 */
public class RootEqualsSumOfChildren {

    // V0
    // IDEA: DIRECT NODE ACCESS
    //       the tree is guaranteed to have exactly 3 nodes, so both children
    //       always exist -> no traversal needed, just one comparison.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean checkTree(TreeNode root) {
        return root.val == root.left.val + root.right.val;
    }
}
