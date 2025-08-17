package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/count-complete-tree-nodes/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 222. Count Complete Tree Nodes
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a complete binary tree, return the number of the nodes in the tree.
 *
 * According to Wikipedia, every level, except possibly the last, is completely filled in a complete binary tree, and all nodes in the last level are as far left as possible. It can have between 1 and 2h nodes inclusive at the last level h.
 *
 * Design an algorithm that runs in less than O(n) time complexity.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,5,6]
 * Output: 6
 * Example 2:
 *
 * Input: root = []
 * Output: 0
 * Example 3:
 *
 * Input: root = [1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 5 * 104].
 * 0 <= Node.val <= 5 * 104
 * The tree is guaranteed to be complete.
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 1,019,796/1.4M
 * Acceptance Rate
 * 70.6%
 *
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

    // V0-1
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

    // V0-2
    // IDEA: DFS
    int nodeCnt = 0;
    public int countNodes_0_2(TreeNode root) {
        // edge
        if(root == null){
            return 0;
        }

        nodeCnt += 1;
        countNodes_0_2(root.left);
        countNodes_0_2(root.right);

        return nodeCnt;
    }

    // V1

    // V2
}
