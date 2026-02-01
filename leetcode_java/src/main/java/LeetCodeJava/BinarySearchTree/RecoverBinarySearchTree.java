package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/recover-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

/**
 * 99. Recover Binary Search Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary search tree (BST), where the values of exactly two nodes of the tree were swapped by mistake. Recover the tree without changing its structure.
 *
 *Example 1:
 *
 *
 * Input: root = [1,3,null,null,2]
 * Output: [3,1,null,null,2]
 * Explanation: 3 cannot be a left child of 1 because 3 > 1. Swapping 1 and 3 makes the BST valid.
 * Example 2:
 *
 *
 * Input: root = [3,1,4,null,null,2]
 * Output: [2,1,4,null,null,3]
 * Explanation: 2 cannot be in the right subtree of 3 because 2 < 3. Swapping 2 and 3 makes the BST valid.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [2, 1000].
 * -231 <= Node.val <= 231 - 1
 *
 *
 * Follow up: A solution using O(n) space is pretty straight-forward. Could you devise a constant O(1) space solution?
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 *
 */
public class RecoverBinarySearchTree {

    // V0
//    /**
     * time = O(N)
     * space = O(H)
     */
    public void recoverTree(TreeNode root) {
//
//    }

    // V0-1
    // IDEA: BST + find node and swap to fix (fixed by gpt)
    /**
     *  Steps:
     *
     * → Do an inorder traversal,
     * → Detect the two nodes that violate ascending order,
     * → Swap their values once.
     */
    /**
     *  NOTE !!!  we define below vars:
     *
     *  - first: The first node where order is violated
     *  - second: The second node where order is violated
     *  - prev: The last visited node in inorder traversal
     *
     */
    private TreeNode first = null;
    private TreeNode second = null;
    private TreeNode prev = new TreeNode(Integer.MIN_VALUE);

    /**

     * time = O(N)

     * space = O(H)
     */


    public void recoverTree_0_1(TreeNode root) {
        if (root == null)
            return;

        // 1️⃣ Inorder traversal to find the two swapped nodes
        /** NOTE !!!
         *
         *  we run the inorder_0_1, and update first, second,
         *  which are the to-swap nodes
         */
        inorder_0_1(root);

        // 2️⃣ Swap the two nodes’ values
        /** NOTE !!!
         *
         *  ONLY if first, and second nodes are NOT null,
         *  then we have to SWAP. otherwise, it's a valid BST already.
         *  No need to swap.
         */
        if (first != null && second != null) {
            int temp = first.val;
            first.val = second.val;
            second.val = temp;
        }
    }

    /** NOTE !!!! Inorder traversal
     *
     */
    /**
     * Performs a standard in-order traversal (Left, Root, Right)
     * to find the two nodes that violate the sorted order.
     */
    private void inorder_0_1(TreeNode root) {
        if (root == null)
            return;

        // 1. Go Left
        inorder_0_1(root.left);

        // Violation: current node’s value < previous node’s value
        // If prevNode is null, this is the first (smallest) node.
        if (prev != null && root.val < prev.val) {

    /**

     * time = O(N)

     * space = O(H)
     */


    public void recoverTree(TreeNode root) {
        if (root == null) {
            return;
        }

        // Initialize instance variables for this run.
        prevNode = null;
        firstViolation = null;
        secondViolation = null;

        // 1. Traverse the tree in-order to find the two nodes that are swapped.
        inOrderTraversal(root);

        // 2. After the traversal, swap the values of the two found nodes.
        // We must check if firstViolation is not null, in case the tree was already valid.
        if (firstViolation != null && secondViolation != null) {
            int temp = firstViolation.val;
            firstViolation.val = secondViolation.val;
            secondViolation.val = temp;
        }
    }

    /**
     * Performs a standard in-order traversal (Left, Root, Right)
     * to find the two nodes that violate the sorted order.
     */
    private void inOrderTraversal(TreeNode currentNode) {
        if (currentNode == null) {
            return;
        }

        // 1. Go Left
        inOrderTraversal(currentNode.left);

        // 2. Visit (Process) the current node
        // This is where we check for the BST violation.

        // If prevNode is null, this is the first (smallest) node.
        if (prevNode != null && prevNode.val > currentNode.val) {
            // A violation is found (prev > current)!

            // If this is the *first* violation we've seen,
            // the 'prevNode' is the first node of the swapped pair.
            if (firstViolation == null) {
                firstViolation = prevNode;
            }

            // The 'currentNode' is the second node of the swapped pair.
            // If there's a second, non-adjacent violation, this will
            // correctly update 'secondViolation' to the new 'currentNode'.
            secondViolation = currentNode;
        }

        // Update prevNode to the current node for the next comparison.
        prevNode = currentNode;

        // 3. Go Right
        inOrderTraversal(currentNode.right);
    }

    // The original isValidBST and swapBST methods are removed as they are
    // either redundant or fundamentally flawed.

    // V1
    // https://leetcode.com/problems/recover-binary-search-tree/solutions/7021623/beats-100-beginner-friendly-explanation-gjz5a/
    TreeNode first_1;
    TreeNode second_1;
    TreeNode prev_1;
    /**
     * time = O(N)
     * space = O(H)
     */

    public void recoverTree_1(TreeNode root) {
        helper(root);
        // Swap the values of the two misplaced nodes
        int temp = first_1.val;
        first_1.val = second_1.val;
        second_1.val = temp;
    }

    void helper(TreeNode node) {
        if (node == null)
            return;

        // Traverse left subtree
        helper(node.left);

        // Detect swapped nodes
        if (prev_1 != null && prev_1.val > node.val) {
            if (first_1 == null) {
                first_1 = prev_1;
            }
            second_1 = node;
        }

        prev_1 = node;

        // Traverse right subtree
        helper(node.right);
    }


    // V2
    TreeNode prev_2 = null;
    TreeNode first_2 = null;
    TreeNode second_2 = null;

    void inorder(TreeNode root) {
        if (root == null)
            return;
        inorder(root.left);
        if (prev_2 != null && root.val < prev_2.val) {
            if (first_2 == null)
                first_2 = prev_2;
            second_2 = root;
        }
        prev_2 = root;
        inorder(root.right);
    }

    /**

     * time = O(N)

     * space = O(H)
     */


    public void recoverTree_2(TreeNode root) {
        if (root == null)
            return;
        inorder(root);
        int temp = first_2.val;
        first_2.val = second_2.val;
        second_2.val = temp;
    }


}
