package LeetCodeJava.Tree;

// https://leetcode.com/problems/invert-binary-tree/
/**
 * 226. Invert Binary Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, invert the tree, and return its root.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [4,2,7,1,3,6,9]
 * Output: [4,7,2,9,6,3,1]
 * Example 2:
 *
 *
 * Input: root = [2,1,3]
 * Output: [2,3,1]
 * Example 3:
 *
 * Input: root = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 100].
 * -100 <= Node.val <= 100
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class InvertBinaryTree {

    // V0
    // IDEA: DFS
    public TreeNode invertTree(TreeNode root) {

        // edge
        if (root == null || (root.left == null && root.right == null)) {
            return root;
        }
        // dfs
        TreeNode reversedRoot = reverseTree(root);
        //System.out.println(">>> reversedRoot = " + reversedRoot);

        return reversedRoot;
    }

    private TreeNode reverseTree(TreeNode root) {
        if (root == null) {
            return root;
        }

        // cache `reversed right tree`, so this object remains unchanged, and can be
        // used in following code
        TreeNode tmpRight = reverseTree(root.right);
        root.right = reverseTree(root.left);
        root.left = tmpRight;

        return root;
    }

    // VO-1
    // IDEA : DFS + cache
    public TreeNode invertTree_0_1(TreeNode root) {

        if (root == null){
            return root;
        }

        // NOTE !!! we cache left sub tree first
        // then can assign such node to right sub tree
        TreeNode tmp = root.left;
        root.left = invertTree_0_1(root.right);
        root.right = invertTree_0_1(tmp);

        return root;
    }


    // V0-2
    // IDEA : DFS
    public TreeNode invertTree_0_2(TreeNode root) {
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
        TreeNode left = invertTree_0_2(root.left);
        TreeNode right = invertTree_0_2(root.right);
        root.left = right;
        root.right = left;
        /** NOTE !!!! below is WRONG */
//        root.left = invertTree(root.right);
//        root.right = invertTree(root.left);
        return root;
    }

    // V0-3
    // IDEA : BFS
    public TreeNode invertTree_0_3(TreeNode root) {

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
    public TreeNode invertTree_1(TreeNode root) {
        if (root == null) {
            return null;
        }
        TreeNode right = invertTree_1(root.right);
        TreeNode left = invertTree_1(root.left);
        root.left = right;
        root.right = left;
        return root;
    }

    // V1-1
    // IDEA : Iterative
    // https://leetcode.com/problems/invert-binary-tree/editorial/
    public TreeNode invertTree_1_1(TreeNode root) {
        if (root == null) return null;
        /**
         * NOTE !!!
         *
         * Queue is the common tricks that we transform recursive code to iterative
         * -> so via FIFO properties, we're sure that the `earlier reached` code is
         *    executed first, then the `following code`,... and so on
         */
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
