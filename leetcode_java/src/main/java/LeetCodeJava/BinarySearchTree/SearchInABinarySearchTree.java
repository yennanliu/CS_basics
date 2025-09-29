package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/search-in-a-binary-search-tree/
/**
 * 700. Search in a Binary Search Tree
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary search tree (BST) and an integer val.
 *
 * Find the node in the BST that the node's value equals val and return the subtree rooted with that node. If such a node does not exist, return null.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [4,2,7,1,3], val = 2
 * Output: [2,1,3]
 * Example 2:
 *
 *
 * Input: root = [4,2,7,1,3], val = 5
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 5000].
 * 1 <= Node.val <= 107
 * root is a binary search tree.
 * 1 <= val <= 107
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

public class SearchInABinarySearchTree {

    // V0
    // IDEA: DFS + BST PROPERTY
    public TreeNode searchBST(TreeNode root, int val) {
        // edge
        if (root == null) {
            return null;
        }

        if (root.val == val) {
            return root;
        }

        if (root.val < val) {
            return searchBST(root.right, val);
        }

        return searchBST(root.left, val);
    }

    // V0-1
    // IDEA: ITERATIVE + BST PROPERTY
    public TreeNode searchBST_0_1(TreeNode root, int val) {
        // edge
        if (root == null) {
            return null;
        }

        while (root != null) {
            if (root.val == val) {
                return root;
            } else if (root.val < val) {
                root = root.right;
            } else {
                root = root.left;
            }
        }

        return null;
    }

    // V0-2
    // IDEA : RECURSION
    public TreeNode searchBST_0_2(TreeNode root, int val) {

        if (root.left == null && root.right == null){
            return new TreeNode();
        }

        // DFS
        return _help(root, val);
    }

    private TreeNode _help(TreeNode root, int val){

        if (root == null){
            return root;
        }

        if (root.val == val){
            return root;
        }

        if(root.val < val){
            return _help(root.right, val);
        }else{
            return _help(root.left, val);
        }
    }

    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/search-in-a-binary-search-tree/editorial/
    public TreeNode searchBST_1(TreeNode root, int val) {
        if (root == null || val == root.val) return root;

        return val < root.val ? searchBST_1(root.left, val) : searchBST_1(root.right, val);
    }


    // V2
    // IDEA : ITERATION
    // https://leetcode.com/problems/search-in-a-binary-search-tree/editorial/
    public TreeNode searchBST_2(TreeNode root, int val) {
        while (root != null && val != root.val)
            root = val < root.val ? root.left : root.right;
        return root;
    }
    

}
