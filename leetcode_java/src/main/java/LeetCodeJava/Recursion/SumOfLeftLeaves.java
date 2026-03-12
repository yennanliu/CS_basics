package LeetCodeJava.Recursion;

// https://leetcode.com/problems/sum-of-left-leaves/
/**
 * 404. Sum of Left Leaves
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree, return the sum of all left leaves.
 *
 * A leaf is a node with no children. A left leaf is a leaf that is the left child of another node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,9,20,null,null,15,7]
 * Output: 24
 * Explanation: There are two left leaves in the binary tree, with values 9 and 15 respectively.
 * Example 2:
 *
 * Input: root = [1]
 * Output: 0
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 1000].
 * -1000 <= Node.val <= 1000
 *
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayDeque;
import java.util.Deque;

public class SumOfLeftLeaves {

    // V0
    // IDEA + PRE ORDER TRAVERSE !!
    int leftLeavesSum = 0;

    public int sumOfLeftLeaves(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }

        getLeftNodesHelper(root, "");

        return leftLeavesSum;
    }

    // ???
    private void getLeftNodesHelper(TreeNode root, String preOp) {
        if (root == null) {
            return; //0;
        }

        // pre-order !!!
        if (root.left == null && root.right == null && preOp.equals("left")) {
            leftLeavesSum += root.val;
        }

        getLeftNodesHelper(root.left, "left");
        getLeftNodesHelper(root.right, "right");
    }


    // V0-1
    // IDEA + PRE ORDER TRAVERSE !! (GEMINI)
    public int sumOfLeftLeaves_0_1(TreeNode root) {
        if (root == null)
            return 0;
        return helper(root, false); // Initial call: root is not a left child
    }

    private int helper(TreeNode node, boolean isLeft) {
        if (node == null)
            return 0;

        // Check if current node is a leaf
        if (node.left == null && node.right == null) {
            // Only return the value if it's a leaf AND it's a left child
            return isLeft ? node.val : 0;
        }

        /** NOTE !!!
         *
         *  we still call dfs func with `left, right` sub tree,
         *  and sum over them (recursive func call),
         *  since we the logic above to check if it's `left leaf`,
         *  to decide whether append val to result.
         *
         *   e.g.
         *   ```
         *     if (node.left == null && node.right == null) {
         *             // Only return the value if it's a leaf AND it's a left child
         *             return isLeft ? node.val : 0;
         *         }
         *   ```
         *
         */
        // Standard DFS: sum up results from left and right subtrees
        // When we go left, we set the flag to true
        // When we go right, we set the flag to false
        return helper(node.left, true) + helper(node.right, false);
    }


    // V0-2
    // IDEA + PRE ORDER TRAVERSE !! (GPT)
    public int sumOfLeftLeaves_0_2(TreeNode root) {
        return helper_0_2(root, false);
    }

    private int helper_0_2(TreeNode node, boolean isLeft) {
        if (node == null) {
            return 0;
        }

        // if it's a leaf
        if (node.left == null && node.right == null) {
            return isLeft ? node.val : 0;
        }

        return helper_0_2(node.left, true) + helper_0_2(node.right, false);
    }



    // VO-7
    // IDEA: DFS (post order traverse)
    int res = 0;
    /**
     * time = O(N)
     * space = O(H)
     */
    public int sumOfLeftLeaves_0_7(TreeNode root) {

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

    // V0-8
    // IDEA : BFS (Pre-order)
    private boolean isLeaf(TreeNode node) {
        return node != null && node.left == null && node.right == null;
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public int sumOfLeftLeaves_0_8(TreeNode root) {

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

    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
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

    /**
     * time = O(N)
     * space = O(H)
     */
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
    /**
     * time = O(N)
     * space = O(H)
     */
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
