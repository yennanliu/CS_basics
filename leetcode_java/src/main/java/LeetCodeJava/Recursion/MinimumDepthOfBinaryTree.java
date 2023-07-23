package LeetCodeJava.Recursion;

// https://leetcode.com/problems/minimum-depth-of-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 *  The minimum depth is the number of nodes
 *  along the shortest path from the root node down to the nearest leaf node.
 *
 *  -> NOTE !!! depth is node number, not distance
 *  > The minimum depth is the number of nodes ...
 *
 */

public class MinimumDepthOfBinaryTree {

    // V0
    // NOTE !!! below is wrong
//    public int minDepth(TreeNode root) {
//
//        if (root == null){
//            return 0;
//        }
//
//        int leftD = minDepth(root.left) + 1;
//        int rightD = minDepth(root.right) + 1;
//
//        return Math.min(leftD, rightD);
//
//    }

    // V0'
    // IDEA : DFS
    public int minDepth(TreeNode root) {

        if (root == null){
            return  0;
        }

        //maxDepth = Math.max(getDepth(root.left), getDepth(root.right));
        return getDepth(root);
    }

    private int getDepth(TreeNode root){

        if (root == null){
            return 0;
        }

//        if (root.left != null){
//            return 1 + getDepth(root.left);
//        }
//
//        if (root.right != null){
//            return 1 + getDepth(root.right);
//        }

        /**
         *  NOTE !!! below condition
         *  -> we need to go till meat a node, then calculate min depths (number of node)
         *  -> Note: A leaf is a node with no children.
         *  -> plz check below example for idea
         *  example : [2,null,3,null,4,null,5,null,6]
         *
         *
         */
        if (root.left == null) {
            return 1 + getDepth(root.right);
        } else if (root.right == null) {
            return 1 + getDepth(root.left);
        }

        return 1 + Math.min(getDepth(root.left), getDepth(root.right));
    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/minimum-depth-of-binary-tree/editorial/
    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // If only one of child is non-null, then go into that recursion.
        if (root.left == null) {
            return 1 + dfs(root.right);
        } else if (root.right == null) {
            return 1 + dfs(root.left);
        }

        // Both children are non-null, hence call for both childs.
        return 1 + Math.min(dfs(root.left), dfs(root.right));
    }

    public int minDepth_2(TreeNode root) {
        return dfs(root);
    }

    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/minimum-depth-of-binary-tree/editorial/
    public int minDepth_3(TreeNode root) {
        if (root == null) {
            return 0;
        }

        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        int depth = 1;

        while (q.isEmpty() == false) {
            int qSize = q.size();

            while (qSize > 0) {
                qSize--;

                TreeNode node = q.remove();
                // Since we added nodes without checking null, we need to skip them here.
                if (node == null) {
                    continue;
                }

                // The first leaf would be at minimum depth, hence return it.
                if (node.left == null && node.right == null) {
                    return depth;
                }

                q.add(node.left);
                q.add(node.right);
            }
            depth++;
        }
        return -1;
    }

}
