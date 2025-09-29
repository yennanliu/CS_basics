package dev;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    // 16.28 - 38 pm
    /**
     *
     * Given the root of a Binary Search Tree (BST),
     * return the `minimum absolute difference` between
     * the values of `any two different nodes `in the tree.
     *
     *
     * IDEA 1) get all nodes as list, sort, maintain the `minimum absolute difference`
     *         between any 2 nodes
     *
     *
     * IDEA 2) BST property
     */
    List<Integer> nodes = new ArrayList<>();
    public int getMinimumDifference(TreeNode root) {
        // edge
        if(root == null){
            return -1; // ????
        }
        if(root.left == null && root.right == null){
            return -1; // ???
        }

        getNodesDFS(root);

        // sort (small -> big)
        Collections.sort(nodes);
        System.out.println(">>> nodes = " + nodes);

        int minDiff = Integer.MAX_VALUE;

        for(int i = 0; i < nodes.size() - 1; i++){
            int diff = Math.abs(nodes.get(i+1) - nodes.get(i));
            minDiff = Math.min(minDiff, diff);
        }

        return minDiff;
    }

    private void getNodesDFS(TreeNode root){
        if(root == null){
            return;
        }
        nodes.add(root.val);
        getNodesDFS(root.left);
        getNodesDFS(root.right);
    }



}
