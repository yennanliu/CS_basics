package LeetCodeJava.BinarySearchTree;

// https://leetcode.ca/all/285.html
// https://leetcode.com/problems/inorder-successor-in-bst/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  285. Inorder Successor in BST
 * Given a binary search tree and a node in it, find the in-order successor of that node in the BST.
 *
 * The successor of a node p is the node with the smallest key greater than p.val.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [2,1,3], p = 1
 * Output: 2
 * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of TreeNode type.
 * Example 2:
 *
 *
 * Input: root = [5,3,6,2,4,null,null,1], p = 6
 * Output: null
 * Explanation: There is no in-order successor of the current node, so the answer is null.
 *
 *
 * Note:
 *
 * If the given node has no in-order successor in the tree, return null.
 * It's guaranteed that the values of the tree are unique.
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Microsoft Palantir Technologies Pocket Gems Quip (Salesforce) Zillow
 * Problem Solution
 * 285-Inorder-Successor-in-BST
 *
 */
public class InorderSuccessorInBST {

    // V0
//    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
//
//    }

    // V1
    // https://leetcode.ca/2016-09-10-285-Inorder-Successor-in-BST/
    public TreeNode inorderSuccessor_1(TreeNode root, TreeNode p) {
        TreeNode ans = null;
        while (root != null) {
            if (root.val > p.val) {
                ans = root;
                root = root.left;
            } else {
                root = root.right;
            }
        }
        return ans;
    }


    // V2



    // V3




}
