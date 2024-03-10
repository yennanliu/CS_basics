package LeetCodeJava.Tree;

// https://leetcode.com/problems/invert-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class InvertBinaryTree {

    // V0
    // IDEA : DFS
    public TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        /** NOTE !!!!
         *
         *   instead of calling invertTree and assign value to sub tree directly,
         *   we need to CACHE invertTree result, and assign later
         *   -> since assign directly will cause tree changed, and affect the other invertTree call
         *
         *   e.g. below is WRONG,
         *      root.left = invertTree(root.right);
         *      root.right = invertTree(root.left);
         *
         *   need to cache result
         *
         *       TreeNode left = invertTree(root.left);
         *       TreeNode right = invertTree(root.right);
         *
         *   then assign to sub tree
         *
         *       root.left = right;
         *       root.right = left;
         */
        TreeNode left = invertTree(root.left);
        TreeNode right = invertTree(root.right);
        root.left = right;
        root.right = left;
        /** NOTE !!!! below is WRONG */
//        root.left = invertTree(root.right);
//        root.right = invertTree(root.left);
        return root;
    }

    // V0
    // IDEA : BFS
    public TreeNode invertTree_(TreeNode root) {

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
        TreeNode right = invertTree_2(root.right);
        TreeNode left = invertTree_2(root.left);
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
