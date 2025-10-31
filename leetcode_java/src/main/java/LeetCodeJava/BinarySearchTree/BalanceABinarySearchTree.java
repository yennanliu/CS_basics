package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/balance-a-binary-search-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 1382. Balance a Binary Search Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given the root of a binary search tree, return a balanced binary search tree with the same node values. If there is more than one answer, return any of them.
 *
 * A binary search tree is balanced if the depth of the two subtrees of every node never differs by more than 1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,null,2,null,3,null,4,null,null]
 * Output: [2,1,3,null,null,null,4]
 * Explanation: This is not the only correct answer, [3,1,4,null,2] is also correct.
 * Example 2:
 *
 *
 * Input: root = [2,1,3]
 * Output: [2,1,3]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 1 <= Node.val <= 105
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 * Accepted
 * 296,434/349.9K
 * Acceptance Rate
 * 84.7%
 * Topics
 */
public class BalanceABinarySearchTree {

    // V0
//    public TreeNode balanceBST(TreeNode root) {
//
//    }


    // V1-1
    // IDEA: Inorder Traversal + Recursive Construction
    // https://leetcode.com/problems/balance-a-binary-search-tree/solutions/
    public TreeNode balanceBST_1_1(TreeNode root) {
        // Create a list to store the inorder traversal of the BST
        List<Integer> inorder = new ArrayList<>();
        inorderTraversal(root, inorder);

        // Construct and return the balanced BST
        return createBalancedBST(inorder, 0, inorder.size() - 1);
    }

    private void inorderTraversal(TreeNode root, List<Integer> inorder) {
        // Perform an inorder traversal to store the elements in sorted order
        if (root == null)
            return;
        inorderTraversal(root.left, inorder);
        inorder.add(root.val);
        inorderTraversal(root.right, inorder);
    }

    private TreeNode createBalancedBST(
            List<Integer> inorder,
            int start,
            int end) {
        // Base case: if the start index is greater than the end index, return null
        if (start > end)
            return null;

        // Find the middle element of the current range
        int mid = start + (end - start) / 2;

        // Recursively construct the left and right subtrees
        TreeNode leftSubtree = createBalancedBST(inorder, start, mid - 1);
        TreeNode rightSubtree = createBalancedBST(inorder, mid + 1, end);

        // Create a new node with the middle element and attach the subtrees
        TreeNode node = new TreeNode(
                inorder.get(mid),
                leftSubtree,
                rightSubtree);
        return node;
    }

    // V1-2
    // IDEA: : Day-Stout-Warren Algorithm / In-Place Balancing
    // https://leetcode.com/problems/balance-a-binary-search-tree/solutions/
    public TreeNode balanceBST_1_2(TreeNode root) {
        if (root == null)
            return null;

        // Step 1: Create the backbone (vine)
        // Temporary dummy node
        TreeNode vineHead = new TreeNode(0);
        vineHead.right = root;
        TreeNode current = vineHead;
        while (current.right != null) {
            if (current.right.left != null) {
                rightRotate(current, current.right);
            } else {
                current = current.right;
            }
        }

        // Step 2: Count the nodes
        int nodeCount = 0;
        current = vineHead.right;
        while (current != null) {
            ++nodeCount;
            current = current.right;
        }

        // Step 3: Create a balanced BST
        int m = (int) Math.pow(
                2,
                Math.floor(Math.log(nodeCount + 1) / Math.log(2))) -
                1;
        makeRotations(vineHead, nodeCount - m);
        while (m > 1) {
            m /= 2;
            makeRotations(vineHead, m);
        }

        TreeNode balancedRoot = vineHead.right;
        return balancedRoot;
    }

    // Function to perform a right rotation
    private void rightRotate(TreeNode parent, TreeNode node) {
        TreeNode tmp = node.left;
        node.left = tmp.right;
        tmp.right = node;
        parent.right = tmp;
    }

    // Function to perform a left rotation
    private void leftRotate(TreeNode parent, TreeNode node) {
        TreeNode tmp = node.right;
        node.right = tmp.left;
        tmp.left = node;
        parent.right = tmp;
    }

    // Function to perform a series of left rotations to balance the vine
    private void makeRotations(TreeNode vineHead, int count) {
        TreeNode current = vineHead;
        for (int i = 0; i < count; ++i) {
            TreeNode tmp = current.right;
            leftRotate(current, tmp);
            current = current.right;
        }
    }

    // V2
    // https://leetcode.ca/2019-09-12-1382-Balance-a-Binary-Search-Tree/
    private List<Integer> nums = new ArrayList<>();

    public TreeNode balanceBST_2(TreeNode root) {
        dfs(root);
        return build(0, nums.size() - 1);
    }

    private void dfs(TreeNode root) {
        if (root == null) {
            return;
        }
        dfs(root.left);
        nums.add(root.val);
        dfs(root.right);
    }

    private TreeNode build(int i, int j) {
        if (i > j) {
            return null;
        }
        int mid = (i + j) >> 1;
        TreeNode left = build(i, mid - 1);
        TreeNode right = build(mid + 1, j);
        return new TreeNode(nums.get(mid), left, right);
    }



}
