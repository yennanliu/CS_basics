package dev;

import LeetCodeJava.Tree.ChangeTheRootOfABinaryTree;

public class workspace13 {

    // LC 427
    // 6.19 - 6.29 PM
//    class Node {
//        public boolean val;
//        public boolean isLeaf;
//        public Node topLeft;
//        public Node topRight;
//        public Node bottomLeft;
//        public Node bottomRight;
//
//
//        public Node() {
//            this.val = false;
//            this.isLeaf = false;
//            this.topLeft = null;
//            this.topRight = null;
//            this.bottomLeft = null;
//            this.bottomRight = null;
//        }
//
//        public Node(boolean val, boolean isLeaf) {
//            this.val = val;
//            this.isLeaf = isLeaf;
//            this.topLeft = null;
//            this.topRight = null;
//            this.bottomLeft = null;
//            this.bottomRight = null;
//        }
//
//        public Node(boolean val, boolean isLeaf, Node topLeft, Node topRight, Node bottomLeft, Node bottomRight) {
//            this.val = val;
//            this.isLeaf = isLeaf;
//            this.topLeft = topLeft;
//            this.topRight = topRight;
//            this.bottomLeft = bottomLeft;
//            this.bottomRight = bottomRight;
//        }
//    }
//    /**
//     * Return the root of the Quad-Tree representing grid.
//     *
//     *  IDEA 1) DFS
//     *
//     *
//     */
//    public Node construct(int[][] grid) {
//
//        return null;
//    }


    // LC 1448
    // 3.06 - 3.16 pm
    /**
     *
     *  Given a binary tree root,
     *  a node X in the tree is named `good` if in the path from
     *  root to X there are `no nodes with a value greater` than X.
     *
     * Return the number of good nodes in the binary tree.
     *
     *
     *
     *  IDEA 1) DFS
     *
     *   -> maintain the `biggest till now` val
     *   -> and compare node val with above
     *   -> if true:  cnt += 1
     *   -> else: cnt unchanged, and update `biggest till now` val
     *
     *
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    int goodNodeCnt = 0; //1;
    public int goodNodes(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }
        if(root.left == null && root.right == null){
            return 1;
        }

        //int cnt = 1;
        countHelper(root, root.val);

        return goodNodeCnt;
    }

    private void countHelper(TreeNode root, int biggestTillNow){
        // edge
        if(root == null){
            return;
        }

        if(root.val >= biggestTillNow){
            biggestTillNow = root.val;
            goodNodeCnt += 1;
        }

        countHelper(root.left, biggestTillNow);
        countHelper(root.right, biggestTillNow);
    }

  // LC 1666
  // 3.41 - 3.51 pm
  /**
   *
   *  Given the root of a binary tree and a leaf node,
   *  reroot the tree so that the `leaf`is `the new root`.
   *
   *
   *  You can reroot the tree with the
   *  following steps for each node `cur` on the path starting from the
   * ` leaf up ` to the root  `excluding` the root:
   *
   *  `reroot` steps
   *
   *
   *  1) If cur has a left child,
   *    then that child becomes cur's right child.
   *    Note that it is guaranteed that cur will have at most one child.
   *
   *
   *  2) cur's original parent becomes cur's left child.
   *
   *
   *   IDEA 1) DFS
   *
   *
   */
  class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    };

    public Node flipBinaryTree(Node root, Node leaf) {

        return null;
    }

    // LC 1325
    // 5.17 - 5.35 pm
    /**
     *   IDEA 1) DFS (post order traverse)
     *   e.g. left -> right -> root
     *
     *    1
     *
     *
     *
     */
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        // edge
        if(root == null){
            return null;
        }
        if(root.left == null && root.right == null){
            if (root.val == target){
                return null;
            }
            return root;
        }

        // ????
        TreeNode res = removeLeafHelper(root, target);

        return res;
    }

    private TreeNode removeLeafHelper(TreeNode root, int target){
        // edge
        if(root == null){
            return null;
        }

        if(root.left == null && root.right == null){
            // if val == target
            if (root.val == target){
                return null;
            }
            // else
            return root;
        }

        root.left = removeLeafHelper(root.left, target);
        root.right = removeLeafHelper(root.right, target);

        return root;
    }



}
