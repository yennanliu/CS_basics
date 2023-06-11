package LeetCodeJava.Tree;

// https://leetcode.com/problems/invert-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class InvertBinaryTree {

    // V0
    // IDEA : BFS
    public TreeNode invertTree(TreeNode root) {

        if (root == null) {
            return null;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.empty()) {
            for (int i = 0; i < stack.size(); i ++){
                TreeNode cur = stack.pop();
                // NOTE : trick here
                TreeNode tmp1 = cur.left;
                TreeNode tmp2 = cur.right;
                cur.left = tmp2;
                cur.right = tmp1;
                if (cur.left != null) {
                    stack.push(cur.left);
                }
                if (cur.right != null) {
                    stack.push(cur.right);
                }
            }
        }

        return root;
    }

    // V1
    // IDEA: Recursive
    // https://leetcode.com/problems/invert-binary-tree/editorial/
    public TreeNode invertTree_2(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode right = invertTree(root.right);
        TreeNode left = invertTree(root.left);
        root.left = right;
        root.right = left;
        return root;
    }

    // V1'
    // IDEA : Iterative
    // https://leetcode.com/problems/invert-binary-tree/editorial/
    public TreeNode invertTree_3(TreeNode root) {
        if (root == null) return null;
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode current = queue.poll();
            TreeNode temp = current.left;
            current.left = current.right;
            current.right = temp;
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        return root;
    }

}
