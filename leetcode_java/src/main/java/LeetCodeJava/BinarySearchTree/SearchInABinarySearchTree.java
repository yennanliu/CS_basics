package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/search-in-a-binary-search-tree/

import LeetCodeJava.DataStructure.TreeNode;

public class SearchInABinarySearchTree {

    // V0
    // IDEA : RECURSION
    public TreeNode searchBST(TreeNode root, int val) {

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
