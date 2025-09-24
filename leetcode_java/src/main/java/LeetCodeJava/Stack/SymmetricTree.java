package LeetCodeJava.Stack;

// https://leetcode.com/problems/symmetric-tree/

/**
 * 101. Symmetric Tree
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,2,3,4,4,3]
 * Output: true
 * Example 2:
 *
 *
 * Input: root = [1,2,2,null,3,null,3]
 * Output: false
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 1000].
 * -100 <= Node.val <= 100
 *
 *
 * Follow up: Could you solve it both recursively and iteratively?
 *
 *
 */
import java.util.LinkedList;
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
public class SymmetricTree {

    // V0
    // IDEA: DFS (fixed by gpt)
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        // NOTE !!! helper func
        /** NOTE !!!
         *
         *  the help func is for comparing
         *    - left sub tree VS right sub tree
         *
         *   -> since from `root` node,
         *     it ONLY has a single `sub left tree` and a single `sub right tree`
         *     -> so we ONLY need to take them as param and put into our helper func
         */
        return symmetricHelper(root.left, root.right);
    }

    /**
     *     //  t1: left sub tree
     *     //  t2: right sub tree
     */
    private boolean symmetricHelper(TreeNode t1, TreeNode t2) {
        // Both null → symmetric
        if (t1 == null && t2 == null) {
            return true;
        }
        // One null but not both → not symmetric
        if (t1 == null || t2 == null) {
            return false;
        }
        // Values must match
        /** NOTE !!!
         *
         *  the help func is for comparing
         *    - left sub tree VS right sub tree
         *
         *    -> so if the sub tree val NOT equal with each other
         *    -> return false
         */
        if (t1.val != t2.val) {
            return false;
        }

        /** NOTE !!!
         *
         *
         *  recursion check
         */
        // Check mirrored children
        return symmetricHelper(t1.left, t2.right) &&
                symmetricHelper(t1.right, t2.left);
    }

    // V0-0-1
    // https://www.bilibili.com/video/BV1ue4y1Y7Mf/?spm_id_from=333.999.0.0&vd_source=28459ac8543f6a81e3a8c993dc73b54e
    // IDEA : post traversal // TODO : check if post traversal is necessary
    public boolean isSymmetric_0_0_1(TreeNode root) {

        return _compare(root, root);
    }

    public boolean _compare(TreeNode t1, TreeNode t2) {

        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;

        boolean b1 = _compare(t1.left, t2.right);
        boolean b2 = _compare(t1.right, t2.left);
        boolean b3 = t1.val == t2.val;

        return b1 && b2 && b3;
    }

    // V1 : IDEA : Recursive
    // https://leetcode.com/problems/symmetric-tree/editorial/
    public boolean isSymmetric_2(TreeNode root) {

        return isMirror_2(root, root);
    }

    public boolean isMirror_2(TreeNode t1, TreeNode t2) {

        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;
        return (t1.val == t2.val)
                && isMirror_2(t1.right, t2.left)
                && isMirror_2(t1.left, t2.right);
    }

    // V1' : IDEA : Iterative, queue
    // https://leetcode.com/problems/symmetric-tree/editorial/
    public boolean isSymmetric_1(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        q.add(root);
        while (!q.isEmpty()) {
            TreeNode t1 = q.poll();
            TreeNode t2 = q.poll();
            if (t1 == null && t2 == null) continue;
            if (t1 == null || t2 == null) return false;
            if (t1.val != t2.val) return false;
            q.add(t1.left);
            q.add(t2.right);
            q.add(t1.right);
            q.add(t2.left);
        }
        return true;
    }

    public class TreeNode {

        // attr
        Integer val;
        TreeNode left;
        TreeNode right;

        // constructor
        TreeNode() {
        }

        TreeNode(Integer val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
