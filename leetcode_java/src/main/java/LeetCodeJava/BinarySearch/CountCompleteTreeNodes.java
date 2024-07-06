package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/count-complete-tree-nodes/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
public class CountCompleteTreeNodes {

    // V0
    // IDEA : BFS
    public int countNodes(TreeNode root) {

        if (root == null){
            return 0;
        }
        List<TreeNode> collected = new ArrayList<>();
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while (!q.isEmpty()){
            TreeNode cur = q.poll();
            collected.add(cur);
            if (cur.left != null) {
                q.add(cur.left);
            }
            if (cur.right != null) {
                q.add(cur.right);
            }
        }

        //return this.count;
        System.out.println("collected = " + collected.toString());
        return collected.size();
    }

    // V0'
    // IDEA : DFS
    public int countNodes_0_1(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // Recursively count the nodes in the left subtree
        int leftCount = countNodes(root.left);

        // Recursively count the nodes in the right subtree
        int rightCount = countNodes(root.right);

        // Return the total count (current node + left subtree + right subtree)
        return 1 + leftCount + rightCount;
    }

    // V1

    // V2
}
