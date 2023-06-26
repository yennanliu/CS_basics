package LeetCodeJava.Recursion;

// https://leetcode.com/problems/same-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;

public class SameTree {

    // V0
    // IDEA : RECURSION
    public boolean isSameTree(TreeNode p, TreeNode q) {

        if (p == null && q == null){
            return true;
        }

        if ((p == null && q != null) || (p != null && q == null)){
            return false;
        }

        if (p.val != q.val){
            return false;
        }

        return check(p.left, q.left) && check(p.right, q.right);
    }

    private Boolean check(TreeNode t1, TreeNode t2){

        if (t1 == null && t2 == null){
            return true;
        }

        if ((t1 == null && t2 != null) || (t1 != null && t2 == null)){
            return false;
        }

        if (t1.val != t2.val){
            return false;
        }

        return check(t1.left, t2.left) && check(t1.right, t2.right);
    }

    // V1
    // IDEA : RECURSION
    // https://leetcode.com/problems/same-tree/editorial/
    public boolean isSameTre_2(TreeNode p, TreeNode q) {
        // p and q are both null
        if (p == null && q == null) return true;
        // one of p and q is null
        if (q == null || p == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.right, q.right) &&
                isSameTree(p.left, q.left);
    }

    // V2
    // IDEA : ITERATIVE
    // https://leetcode.com/problems/same-tree/editorial/
    public boolean check2(TreeNode p, TreeNode q) {
        // p and q are null
        if (p == null && q == null) return true;
        // one of p and q is null
        if (q == null || p == null) return false;
        if (p.val != q.val) return false;
        return true;
    }

    public boolean isSameTree_3(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (!check(p, q)) return false;

        // init deques
        ArrayDeque<TreeNode> deqP = new ArrayDeque<TreeNode>();
        ArrayDeque<TreeNode> deqQ = new ArrayDeque<TreeNode>();
        deqP.addLast(p);
        deqQ.addLast(q);

        while (!deqP.isEmpty()) {
            p = deqP.removeFirst();
            q = deqQ.removeFirst();

            if (!check2(p, q)) return false;
            if (p != null) {
                // in Java nulls are not allowed in Deque
                if (!check2(p.left, q.left)) return false;
                if (p.left != null) {
                    deqP.addLast(p.left);
                    deqQ.addLast(q.left);
                }
                if (!check2(p.right, q.right)) return false;
                if (p.right != null) {
                    deqP.addLast(p.right);
                    deqQ.addLast(q.right);
                }
            }
        }
        return true;
    }

}
