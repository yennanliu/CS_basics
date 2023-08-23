package LeetCodeJava.Recursion;

// https://leetcode.com/problems/sum-of-left-leaves/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class SumOfLeftLeaves {

    // VO
    // IDEA: DFS (post order traverse)
    int res = 0;
    public int sumOfLeftLeaves(TreeNode root) {

        if (root == null){
            return this.res;
        }

        if (root.left == null && root.right == null){
            return this.res;
        }

        // helper
        //this._help(root, false, 0);
        this._help(root, false);
        return this.res;
    }

    private void _help(TreeNode root, boolean isLeft){

        // post traverse
        if (root == null){
            return;
        }

        _help(root.left, true);
        _help(root.right, false);

        if (root.left == null && root.right == null){
            if(isLeft){
                //System.out.println(">>> lastval = " + lastVal);
                /** NOTE !!! we add cur root val here, instead of last val */
                this.res += root.val;
            }
        }
    }

    // V0'
    // IDEA : BFS (Pre-order)
    private boolean isLeaf(TreeNode node) {
        return node != null && node.left == null && node.right == null;
    }

    public int sumOfLeftLeaves_0_1(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int total = 0;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode subRoot = stack.pop();
            // Check if the left node is a leaf node.
            if (isLeaf(subRoot.left)) {
                total += subRoot.left.val;
            }
            // If the right node exists, put it on the stack.
            if (subRoot.right != null) {
                stack.push(subRoot.right);
            }
            // If the left node exists, put it on the stack.
            if (subRoot.left != null) {
                stack.push(subRoot.left);
            }
        }

        return total;
    }


    // V1
    // IDEA :  Iterative Tree Traversal (Pre-order)
    // https://leetcode.com/problems/sum-of-left-leaves/editorial/
    private boolean isLeaf_1(TreeNode node) {
        return node != null && node.left == null && node.right == null;
    }

    public int sumOfLeftLeaves_1(TreeNode root) {

        if (root == null) {
            return 0;
        }

        int total = 0;
        Deque<TreeNode> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode subRoot = stack.pop();
            // Check if the left node is a leaf node.
            if (isLeaf_1(subRoot.left)) {
                total += subRoot.left.val;
            }
            // If the right node exists, put it on the stack.
            if (subRoot.right != null) {
                stack.push(subRoot.right);
            }
            // If the left node exists, put it on the stack.
            if (subRoot.left != null) {
                stack.push(subRoot.left);
            }
        }

        return total;
    }

    // V2
    // IDEA : Recursive Tree Traversal (Pre-order)
    // https://leetcode.com/problems/sum-of-left-leaves/editorial/
    public int sumOfLeftLeaves_2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return processSubtree(root, false);
    }

    private int processSubtree(TreeNode subtree, boolean isLeft) {

        // Base case: This is a leaf node.
        if (subtree.left == null && subtree.right == null) {
            return isLeft ? subtree.val : 0;
        }

        // Recursive case: We need to add and return the results of the
        // left and right subtrees.
        int total = 0;
        if (subtree.left != null) {
            total += processSubtree(subtree.left, true);
        }
        if (subtree.right != null) {
            total += processSubtree(subtree.right, false);
        }
        return total;
    }

    // V2-1
    // IDEA : Recursive Tree Traversal (Pre-order) simplified
    // https://leetcode.com/problems/sum-of-left-leaves/editorial/

    public int sumOfLeftLeaves_2_1(TreeNode root) {
        return processSubtree_2_1(root, false);
    }

    private int processSubtree_2_1(TreeNode subtree, boolean isLeft) {

        // Base case: This is an empty subtree.
        if (subtree == null) {
            return 0;
        }

        // Base case: This is a leaf node.
        if (subtree.left == null && subtree.right == null) {
            return isLeft ? subtree.val : 0;
        }

        // Recursive case: We need to add and return the results of the
        // left and right subtrees.
        return processSubtree_2_1(subtree.left, true) + processSubtree_2_1(subtree.right, false);
    }

    // V3
    // IDEA : Morris Tree Traversal (Pre-order)
    // https://leetcode.com/problems/sum-of-left-leaves/editorial/
    public int sumOfLeftLeaves_3(TreeNode root) {
        int totalSum = 0;
        TreeNode currentNode = root;
        while (currentNode != null) {
            // If there is no left child, we can simply explore the right subtree
            // without needing to worry about keeping track of currentNode's other
            // child.
            if (currentNode.left == null) {
                currentNode = currentNode.right;
            } else {
                TreeNode previous = currentNode.left;
                // Check if this left node is a leaf node.
                if (previous.left == null && previous.right == null) {
                    totalSum += previous.val;
                }
                // Find the inorder predecessor for currentNode.
                while (previous.right != null && !previous.right.equals(currentNode)) {
                    previous = previous.right;
                }
                // We've not yet visited the inorder predecessor. This means that we
                // still need to explore currentNode's left subtree. Before doing this,
                // we will put a link back so that we can get back to the right subtree
                // when we need to.
                if (previous.right == null) {
                    previous.right = currentNode;
                    currentNode = currentNode.left;
                }
                // We have already visited the inorder predecessor. This means that we
                // need to remove the link we added, and then move onto the right
                // subtree and explore it.
                else {
                    previous.right = null;
                    currentNode = currentNode.right;
                }
            }
        }
        return totalSum;
    }

}
