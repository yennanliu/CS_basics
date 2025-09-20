package LeetCodeJava.Recursion;

// https://leetcode.com/problems/flatten-binary-tree-to-linked-list/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 114. Flatten Binary Tree to Linked List
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given the root of a binary tree, flatten the tree into a "linked list":
 *
 * The "linked list" should use the same TreeNode class where the right child pointer points to the next node in the list and the left child pointer is always null.
 * The "linked list" should be in the same order as a pre-order traversal of the binary tree.
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,5,3,4,null,6]
 * Output: [1,null,2,null,3,null,4,null,5,null,6]
 * Example 2:
 *
 * Input: root = []
 * Output: []
 * Example 3:
 *
 * Input: root = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 2000].
 * -100 <= Node.val <= 100
 *
 *
 * Follow up: Can you flatten the tree in-place (with O(1) extra space)?
 *
 */
public class FlattenBinaryTreeToLinkedList {

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


    // V0
//    public void flatten(TreeNode root) {
//
//    }

    // V0-0-1
    // IDEA: DFS + LIST (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *   helper val (list) to collect visited tree node
     */
    List<TreeNode> list = new ArrayList<>();
    public void flatten_0_0_1(TreeNode root) {
        if (root == null) {
            return;
        }

        // Collect nodes in preorder
        preOrderTraverse(root);

        // Re-link nodes in list
        for (int i = 0; i < list.size() - 1; i++) {
            TreeNode curr = list.get(i);
            TreeNode next = list.get(i + 1);
            curr.left = null; // left must be null
            curr.right = next; // flatten into "linked list"
        }
    }

    private void preOrderTraverse(TreeNode root) {
        if (root == null) {
            return;
        }
        list.add(root); // add every node (not just leaves!)
        preOrderTraverse(root.left);
        preOrderTraverse(root.right);
    }

    // V1
    // https://leetcode.ca/2016-03-23-114-Flatten-Binary-Tree-to-Linked-List/
    public void flatten_1(TreeNode root) {
        while (root != null) {
            if (root.left != null) {
                TreeNode pre = root.left;
                while (pre.right != null) {
                    pre = pre.right;
                }
                pre.right = root.right;
                root.right = root.left;
                root.left = null;
            }
            root = root.right;
        }
    }


    // V2-1
    // IDEA: PREORDER TRAVERSE
    // https://leetcode.com/problems/flatten-binary-tree-to-linked-list/solutions/7096855/2-approaches-in-place-depth-first-search-wmwk/
    public void flatten_2(TreeNode root) {
        if (root == null)
            return;
        List<TreeNode> nodes = new ArrayList<>();
        preorder(root, nodes);

        for (int i = 0; i < nodes.size() - 1; i++) {
            nodes.get(i).left = null;
            nodes.get(i).right = nodes.get(i + 1);
        }
    }

    private void preorder(TreeNode node, List<TreeNode> nodes) {
        if (node == null)
            return;
        nodes.add(node);
        preorder(node.left, nodes);
        preorder(node.right, nodes);
    }

    // V2-2
    // IDEA: In-Place Optimized
    // https://leetcode.com/problems/flatten-binary-tree-to-linked-list/solutions/7096855/2-approaches-in-place-depth-first-search-wmwk/
    public void flatten_2_2(TreeNode root) {
        TreeNode current = root;
        while (current != null) {
            if (current.left != null) {
                TreeNode temp = current.left;
                while (temp.right != null) {
                    temp = temp.right;
                }
                temp.right = current.right;
                current.right = current.left;
                current.left = null;
            }
            current = current.right;
        }
    }

    // V3
    // https://leetcode.com/problems/flatten-binary-tree-to-linked-list/solutions/7198280/runtime-0ms-beats-10000-binary-tree-flat-lwm4/
    public void flatten_3(TreeNode root) {
        if (root == null)
            return;
        TreeNode Rhead = root.right;
        TreeNode Lhead = root.left;
        root.left = null;
        root.right = null;

        Rhead = flattenN(root, Rhead);
        if (Rhead != null) {
            Rhead.right = root.right;
            Rhead.left = null;
            root.right = Rhead;
        }

        Lhead = flattenN(root, Lhead);
        if (Lhead != null) {
            Lhead.right = root.right;
            Lhead.left = null;
            root.right = Lhead;
        }
    }

    private TreeNode flattenN(TreeNode root, TreeNode node) {
        if (node == null) {
            return null;
        }

        TreeNode right = flattenN(root, node.right);

        if (right != null) {
            right.right = root.right;
            right.left = null;
            root.right = right;
        }

        TreeNode left = flattenN(root, node.left);

        if (left != null) {
            left.right = root.right;
            left.left = null;
            root.right = left;
        }

        return node;

    }

    // V4
    // https://leetcode.com/problems/flatten-binary-tree-to-linked-list/solutions/6907687/beats-100-beginner-friendly-explanation-d3g32/
    public void flatten(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        insert(root, q);
        q.poll(); // skip the original root since we'll relink from it
        while (!q.isEmpty()) {
            root.right = q.poll(); // connect to next node
            root.left = null; // left should be null in linked list
            root = root.right;
        }
    }

    // Preorder traversal to collect nodes
    void insert(TreeNode node, Queue<TreeNode> queue) {
        if (node == null)
            return;
        queue.add(node);
        insert(node.left, queue);
        insert(node.right, queue);
    }


}
