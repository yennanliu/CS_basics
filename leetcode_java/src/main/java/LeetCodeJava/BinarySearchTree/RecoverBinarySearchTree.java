package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/recover-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

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

    /**
     *
     *  NOTE !!!
     *
     *   we can't use logic like split, check if BST is valid BST
     *   -> logic is complex, struggling in handle cases,
     *      error-prone...
     *
     */

    // V0
    // IDEA: IN-ORDER DFS + BST property + `node val` swap (fixed by gemini)
    List<TreeNode> inOrderNodes = new ArrayList<>();

    public void recoverTree(TreeNode root) {
        if (root == null)
            return;

        // 1. Get the list of nodes in In-order
        getInorder(root);

        // 2. Identify the two swapped nodes
        TreeNode first = null;
        TreeNode second = null;

        for (int i = 0; i < inOrderNodes.size() - 1; i++) {
            // Check for a "drop" in the sorted sequence
            if (inOrderNodes.get(i).val > inOrderNodes.get(i + 1).val) {
                // If it's the first drop, 'first' is the larger node (i)
                if (first == null) {
                    first = inOrderNodes.get(i);
                }
                // 'second' is always the smaller node (i + 1)
                // If there's a second drop later, this will overwrite 'second' correctly
                second = inOrderNodes.get(i + 1);
            }
        }


        /** NOTE !!
         *
         *   since we already the `first` and `second` node.
         *   in order to `swap` them, all we need to do is:
         *   -> swap their `value`.
         *
         *   that's it. the swap is not really `swap all node object`
         *   , just change values
         */
        // 3. Swap the values of the two identified nodes
        if (first != null && second != null) {
            int temp = first.val;
            first.val = second.val;
            second.val = temp;
        }
    }

    // get in-order list,
    // for BST, it's a increasing list
    private void getInorder(TreeNode root) {
        if (root == null)
            return;
        getInorder(root.left);
        inOrderNodes.add(root); // Store the node, not just the value!
        getInorder(root.right);
    }


    // V0-1
    // IDEA: IN-ORDER DFS + BST (fixed by gpt)
    /**  NOTE !!! why in-order ??
     *
     *  -> An In-order traversal of a valid BST must be a
     *     strictly increasing sequence.
     *     If two nodes are swapped, you will see "drops" in that sequence.
     *

     */
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
        if (prev != null && root.val < prev.val) {
            if (first == null) {
                first = prev;
            }
            second = root;
        }

        // Update prev
        prev = root;

        // 3. Go Right
        inorder_0_1(root.right);
    }

    // V0-2
    // IDEA: IN-ORDER DFS (fixed by gemini)
    private TreeNode first_0_2 = null;
    private TreeNode second_0_2 = null;
    private TreeNode prev_0_2 = new TreeNode(Integer.MIN_VALUE);

    public void recoverTree_0_2(TreeNode root) {
        // 1. Traverse and find the two swapped nodes
        inorder(root);

        // 2. Swap the values (not the nodes themselves)
        if (first_0_2 != null && second_0_2 != null) {
            int temp = first_0_2.val;
            first_0_2.val = second_0_2.val;
            second_0_2.val = temp;
        }
    }

    private void inorder_0_2(TreeNode root) {
        if (root == null)
            return;

        inorder_0_2(root.left);

        // --- Logic Start ---
        // Check if the sequence is increasing
        if (prev_0_2.val > root.val) {
            // If this is the first time we find a drop, 'first' is the 'prev' node
            if (first_0_2 == null) {
                first_0_2 = prev_0_2;
            }
            // 'second' is always the current node in a drop
            // (handles both adjacent and distant swaps)
            second_0_2 = root;
        }
        prev_0_2 = root;
        // --- Logic End ---

        inorder_0_2(root.right);
    }




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
