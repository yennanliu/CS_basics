package LeetCodeJava.Recursion;

// https://leetcode.com/problems/trim-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  669. Trim a Binary Search Tree
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary search tree and the lowest and highest boundaries as low and high, trim the tree so that all its elements lies in [low, high]. Trimming the tree should not change the relative structure of the elements that will remain in the tree (i.e., any node's descendant should remain a descendant). It can be proven that there is a unique answer.
 *
 * Return the root of the trimmed binary search tree. Note that the root may change depending on the given bounds.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,0,2], low = 1, high = 2
 * Output: [1,null,2]
 * Example 2:
 *
 *
 * Input: root = [3,0,4,null,2,null,null,1], low = 1, high = 3
 * Output: [3,2,null,1]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 0 <= Node.val <= 104
 * The value of each node in the tree is unique.
 * root is guaranteed to be a valid binary search tree.
 * 0 <= low <= high <= 104
 *
 *
 *
 */
public class TrimABinarySearchTree {

    // V0
//    public TreeNode trimBST(TreeNode root, int low, int high) {
//
//    }

    // V1
    // https://leetcode.com/problems/trim-a-binary-search-tree/solutions/1947897/easy-to-understand-with-explanation-0ms-eb6wl/
    public TreeNode trimBST_1(TreeNode root, int L, int R) {
        if (root == null) {
            return null;
        }
        //if the range is correct then checking both root left and right
        if (root.val >= L && root.val <= R) {
            root.left = trimBST_1(root.left, L, R);//Trim the left subtree
            root.right = trimBST_1(root.right, L, R);//Trim the right subtree
        } else if (root.val < L) {
            //if the root val is less than low then getting values from left will be even lower(binary tree rule) so we need to set the root to root.right;
            root = trimBST_1(root.right, L, R);
        } else if (root.val > R) {
            //if the root val is greater than high then getting values from right will be even higher(binary tree rule) so we need to the root to root.left.
            root = trimBST_1(root.left, L, R);
        }
        return root;
    }


    // V2
    // https://leetcode.com/problems/trim-a-binary-search-tree/solutions/1046706/trim-binary-search-tree-detailed-explana-ryxh/
    public TreeNode trimBST_2(TreeNode root, int low, int high) {
        if (root == null)
            return null;
        if (root.val > high)
            return trimBST_2(root.left, low, high);
        if (root.val < low)
            return trimBST_2(root.right, low, high);
        root.left = trimBST_2(root.left, low, high);
        root.right = trimBST_2(root.right, low, high);
        ;
        return root;
    }





}
