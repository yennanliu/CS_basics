package dev;

import LeetCodeJava.DataStructure.TreeNode;

public class WorkSpace17 {

    // LC 510
    // 16.51 - 17:07 pm
    // Definition for a Node.
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    };

    /**
     *
     *  Given a `binary search tree` BST and a node in it,
     *   find the `in-order` `successor` of that node in the BST.
     *
     *   - The successor of a node p is the node with
     *     the smallest key greater than p.val.
     *
     *
     *  IDEA 1)
     *
     */
    // V0
    public Node inorderSuccessor(Node node) {

        return null;
    }

    // LC 700
    // 15.00 - 10 pm
    /**
     *  IDEA 1) DFS
     *
     */
    // IDEA 1) iterative
    public TreeNode searchBST(TreeNode root, int val) {
        // edge
        if(root == null){
            return null;
        }

        while(root != null){
            if(root.val == val){
                return root;
            }else if(root.val < val){
                root = root.right;
            }else{
                root = root.left;
            }
        }

        // ????
        return null;
    }

    //---------------------------

    // IDEA 2) dfs
    public TreeNode searchBST_100(TreeNode root, int val) {
        // edge
        if(root == null){
            return null;
        }
//        if(root.left == null && root.right == null){
//            if(root.val == val){
//                return root;
//            }
//            return null;
//        }

        if(root.val == val){
            return root;
        }

        if(root.val < val){
            return searchBST_100(root.right, val);
        }

        // ???
        return searchBST_100(root.left, val);
    }

    // LC 530
    public int getMinimumDifference(TreeNode root) {

        return 0;
    }




}
