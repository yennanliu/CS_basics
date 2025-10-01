package dev;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

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

    // LC 235
    // 16.39 - 49 pm
    /**
     * -> Given a `binary search tree (BST),`
     *    find the `lowest common ancestor (LCA)`
     *    node of two given nodes in the BST.
     *
     * NOTE !!!
     *  - BST
     *  - find `LCA`
     *  - All Node.val are unique.
     *  - P != Q
     *  - p and q will exist in the BST.
     *
     *
     *
     *
     *  IDEA 1) DFS
     *
     */
    public TreeNode lowestCommonAncestor_100(TreeNode root, TreeNode p, TreeNode q) {
        // edge
        if(root == null){
            return root;
        }
        // ????
//        if(root.val == p.val || root.val == q.val){
//            return root;
//        }

        // BST property:
        // left < root < right

        // case 1) p, q is on the DIFFERENT size of current node
//        if( (p.val < root.val && root.val < q.val) ||
//                (q.val < root.val && root.val < p.val) ){
//            return root;
//        }
        if( isBigger(root.val, p.val) * isBigger(root.val, q.val) < 1 ){
            return root;
        }

        // case 2) p, q are the LEFT side of current node
        if(root.val > p.val && root.val > q.val){
            return lowestCommonAncestor_100(root.left, p, q);
        }
        // case 3) p, q are the RIGHT side of current node
        else{
            return lowestCommonAncestor_100(root.right, p, q);
        }

       // return root; // ????
    }

    private int isBigger(int a, int b){
        if(a > b){
            return 1;
        }
        return -1;
    }

    // LC 236
    // 17.18 - 28 pm
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null){
            return null;
        }

        // Early exit: if root matches p or q
        if (root.val == p.val || root.val == q.val){
            return root;
        }

//        if (isBigger(root.val, p.val) * isBigger(root.val, q.val) < 0) {
//            return root;
//        }

        ///  ???
        TreeNode _right = lowestCommonAncestor(root.left, p, q);
        TreeNode _left = lowestCommonAncestor(root.right, p, q);

        if(_right != null && _left != null){
            return root; // ???
        }
        if(_left != null){
            return _left;
        }
        return _right;

        //return root; // ???
    }

//    private int isBigger(int a, int b) {
//        return a > b ? 1 : -1;
//    }

    // LC 173
    /**
     * Your BSTIterator object will be instantiated and called as such:
     * BSTIterator obj = new BSTIterator(root);
     * int param_1 = obj.next();
     * boolean param_2 = obj.hasNext();
     */
    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */
    // 9.48 - 58 am
    /**
     * Implement the `BSTIterator` class that represents an iterator
     * over the `in-order` traversal of a binary search tree (BST):
     *
     * -> `in-order`  traverse of BST
     *
     *  - hasNext() Returns `true `if there is a number in the traversal
     *    to the `right` of the pointer
     *
     *  - Moves the pointer to the `right`, then `returns the number at the pointer.`
     *
     *   - when init pointer to a non-existent smallest number,
     *     the first call to next() will return the smallest element in the BST.
     *
     *
     *   IDEA 1) DFS + build BST + BST inorder traverse
     *
     *   // NOTE !!! BST inorder traverse if actually the
     *      `small -> big` ordering array
     *
     *   // NOTE !!! for BST,
     *      left < root < right
     *
     */
    class BSTIterator {


        // attr
        List<Integer> list = new ArrayList<>();
        int idx = 0;

        public BSTIterator(TreeNode root) {
            getAllNodes(root);
            // sort (small -> big)
            System.out.println(">>> (before sort) list = " + list);
            //Collections.sort(list); // default: small -> big ???
            System.out.println(">>> (after sort) list = " + list);
        }

        public int next() {
            if(list.isEmpty() || idx > list.size() - 1){
                return 0; // ???
            }
            int preIdx = idx;
            idx += 1;
            return list.get(preIdx);
        }

        public boolean hasNext() {
            // ???
            System.out.println(">>> (hasNext) idx = " + idx + ", ");
            return idx < list.size() - 1;
        }

        private void getAllNodes(TreeNode root){
            if(root == null){
                return;
            }

            getAllNodes(root.left);
            list.add(root.val);
            getAllNodes(root.right);
            //return root.val + "," + getAllNodes(root.left) + "," + getAllNodes(root.right);
        }
    }

    // LC 98
    // 10.27 - 37 am
    /**
     *   IDEA 1) DFS + BST PROPERTY
     *
     *      *   // NOTE !!! for BST,
     *      *      left < root < right
     */
    public boolean isValidBST(TreeNode root) {
        // edge
        if(root == null){
            return true;
        }
        if(root.left == null && root.right == null){
            return true;
        }

        long smallest_val = Long.MIN_VALUE;
        long biggest_val = Long.MAX_VALUE;

        // ???
        return isValidBSTHelper(root, smallest_val, biggest_val);
    }

    private boolean isValidBSTHelper(TreeNode root, long smallestVal, long biggestVal){

        if(root == null){
            return true; // ???
        }

        // check `current` node
//        if(root.right != null && root.val >= root.right.val){
//            return false;
//        }
//        if(root.left != null && root.val <= root.left.val){
//            return false;
//        }

        if(root.val <= smallestVal){
            return false;
        }
        if(root.val >= biggestVal){
            return false;
        }


        // recursively check sub left, right tree
        return isValidBSTHelper(root.left, smallestVal, root.val) &&
                isValidBSTHelper(root.right, root.val, biggestVal);
    }


    // LC 501
    // 9.45 - 55 am
    /**
     * Given the `root` of a binary search tree (BST) with duplicates,
     * return all the mode(s) (i.e., the most frequently occurred element) in it.
     *
     */
    //List<Integer> cache = new ArrayList<>();
    // {val: cnt}
    Map<Integer, Integer> map = new HashMap<>();
    //Integer topCnt = 0;
    public int[] findMode(TreeNode root) {
        // edge
        if(root == null){
            return new int[]{}; // ??
        }

        getNodes(root);
        List<Integer> list = new ArrayList<>();

        int topCnt = 0;
        for(Integer x: map.values()){
            topCnt = Math.max(topCnt, x);
        }

        System.out.println(">>> map = " + map);
        System.out.println(">>> topCnt = " + topCnt);
        for(Integer x: map.keySet()){
            if(map.get(x).equals(topCnt)){
                list.add(x);
            }
        }
        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }

        return res;
    }

    private void getNodes(TreeNode root){
        if(root == null){
            return;
        }
        map.put(root.val, map.getOrDefault(root.val, 0) + 1);
        //topCnt = Math.max(topCnt, map.getOrDefault(root.val, 0) + 1);
        getNodes(root.left);
        getNodes(root.right);
    }




}
