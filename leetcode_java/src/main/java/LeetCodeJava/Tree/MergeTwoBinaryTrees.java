package LeetCodeJava.Tree;

// https://leetcode.com/problems/merge-two-binary-trees/
/**
 * 617. Merge Two Binary Trees
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * You are given two binary trees root1 and root2.
 *
 * Imagine that when you put one of them to cover the other, some nodes of the two trees are overlapped while the others are not. You need to merge the two trees into a new binary tree. The merge rule is that if two nodes overlap, then sum node values up as the new value of the merged node. Otherwise, the NOT null node will be used as the node of the new tree.
 *
 * Return the merged tree.
 *
 * Note: The merging process must start from the root nodes of both trees.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root1 = [1,3,2,5], root2 = [2,1,3,null,4,null,7]
 * Output: [3,4,5,5,4,null,7]
 * Example 2:
 *
 * Input: root1 = [1], root2 = [1,2]
 * Output: [2,2]
 *
 *
 * Constraints:
 *
 * The number of nodes in both trees is in the range [0, 2000].
 * -104 <= Node.val <= 104
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.Stack;

public class MergeTwoBinaryTrees {

    // V0
    // IDEA : RECURSIVE
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {

        if (t1 == null && t2 == null){
            return null;
        }

        if (t1 != null && t2 != null){
            t1.val += t2.val;
        }

        if (t1 == null && t2 != null){
            // NOTE!!! return t2 directly here
            return t2;
        }

        if (t1 != null && t2 == null){
            // NOTE!!! return t1 directly here
            return t1;
        }

        t1.left = mergeTrees(t1.left, t2.left);
        t1.right = mergeTrees(t1.right, t2.right);

        return t1;
    }

    // V0-0-1
    // IDEA: DFS
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode mergeTrees_0_0_1(TreeNode root1, TreeNode root2) {
        // edge
        if (root1 == null && root2 == null) {
            return root1;
        }
        if (root1 == null || root2 == null) {
            if (root2 == null) {
                return root1;
            }
            return root2;
        }
        /** below handling is more elegant */
//        if (root1 == null) return root2;
//        if (root2 == null) return root1;

        root1.val = root1.val + root2.val;

        // ????
        TreeNode _left = mergeTrees_0_0_1(root1.left, root2.left);
        TreeNode _right = mergeTrees_0_0_1(root1.right, root2.right);

        root1.left = _left;
        root1.right = _right;

        return root1;
    }

    // V0-1
    // IDEA: DFS (GPT)
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode mergeTrees_0_1(TreeNode root1, TreeNode root2) {
        // base cases
        if (root1 == null) return root2;
        if (root2 == null) return root1;

        // merge values
        root1.val += root2.val;

        // recursively merge children
        root1.left = mergeTrees_0_1(root1.left, root2.left);
        root1.right = mergeTrees_0_1(root1.right, root2.right);

        return root1;
    }

    // V1
    // IDEA : RECURSIVE
    // https://leetcode.com/problems/merge-two-binary-trees/editorial/
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode mergeTrees_1(TreeNode t1, TreeNode t2) {
        if (t1 == null)
            return t2;
        if (t2 == null)
            return t1;
        t1.val += t2.val;
        t1.left = mergeTrees(t1.left, t2.left);
        t1.right = mergeTrees(t1.right, t2.right);
        return t1;
    }

    // V2
    // IDEA : RECURSIVE
    // https://leetcode.com/problems/merge-two-binary-trees/editorial/
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode mergeTrees_2(TreeNode t1, TreeNode t2) {
        if (t1 == null)
            return t2;
        Stack<TreeNode[]> stack = new Stack<>();
        stack.push(new TreeNode[]{t1, t2});
        while (!stack.isEmpty()) {
            TreeNode[] t = stack.pop();
            if (t[0] == null || t[1] == null) {
                continue;
            }
            t[0].val += t[1].val;
            if (t[0].left == null) {
                t[0].left = t[1].left;
            } else {
                stack.push(new TreeNode[]{t[0].left, t[1].left});
            }
            if (t[0].right == null) {
                t[0].right = t[1].right;
            } else {
                stack.push(new TreeNode[]{t[0].right, t[1].right});
            }
        }
        return t1;
    }

    // V3
    // IDEA : RECURSIVE
    // https://leetcode.ca/2017-08-08-617-Merge-Two-Binary-Trees/
    /**
     * time = O(N)
     * space = O(H)
     */
    public TreeNode mergeTrees_3(TreeNode root1, TreeNode root2) {
        if (root1 == null) {
            return root2;
        }
        if (root2 == null) {
            return root1;
        }
        TreeNode node = new TreeNode(root1.val + root2.val);
        node.left = mergeTrees_3(root1.left, root2.left);
        node.right = mergeTrees_3(root1.right, root2.right);
        return node;
    }


}
