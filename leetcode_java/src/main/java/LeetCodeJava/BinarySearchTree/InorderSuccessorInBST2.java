package LeetCodeJava.BinarySearchTree;

// https://leetcode.com/problems/inorder-successor-in-bst-ii/description/
// https://leetcode.ca/all/510.html

/**
 * 510. Inorder Successor in BST II
 * Given a binary search tree and a node in it, find the in-order successor of that node in the BST.
 *
 * The successor of a node p is the node with the smallest key greater than p.val.
 *
 * You will have direct access to the node but not to the root of the tree. Each node will have a reference to its parent node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input:
 * root = {"$id":"1","left":{"$id":"2","left":null,"parent":{"$ref":"1"},"right":null,"val":1},"parent":null,"right":{"$id":"3","left":null,"parent":{"$ref":"1"},"right":null,"val":3},"val":2}
 * p = 1
 * Output: 2
 * Explanation: 1's in-order successor node is 2. Note that both p and the return value is of Node type.
 * Example 2:
 *
 *
 * Input:
 * root = {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":{"$id":"4","left":null,"parent":{"$ref":"3"},"right":null,"val":1},"parent":{"$ref":"2"},"right":null,"val":2},"parent":{"$ref":"1"},"right":{"$id":"5","left":null,"parent":{"$ref":"2"},"right":null,"val":4},"val":3},"parent":null,"right":{"$id":"6","left":null,"parent":{"$ref":"1"},"right":null,"val":6},"val":5}
 * p = 6
 * Output: null
 * Explanation: There is no in-order successor of the current node, so the answer is null.
 * Example 3:
 *
 *
 * Input:
 * root = {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":{"$id":"4","left":null,"parent":{"$ref":"3"},"right":null,"val":2},"parent":{"$ref":"2"},"right":{"$id":"5","left":null,"parent":{"$ref":"3"},"right":null,"val":4},"val":3},"parent":{"$ref":"1"},"right":{"$id":"6","left":null,"parent":{"$ref":"2"},"right":{"$id":"7","left":{"$id":"8","left":null,"parent":{"$ref":"7"},"right":null,"val":9},"parent":{"$ref":"6"},"right":null,"val":13},"val":7},"val":6},"parent":null,"right":{"$id":"9","left":{"$id":"10","left":null,"parent":{"$ref":"9"},"right":null,"val":17},"parent":{"$ref":"1"},"right":{"$id":"11","left":null,"parent":{"$ref":"9"},"right":null,"val":20},"val":18},"val":15}
 * p = 15
 * Output: 17
 * Example 4:
 *
 *
 * Input:
 * root = {"$id":"1","left":{"$id":"2","left":{"$id":"3","left":{"$id":"4","left":null,"parent":{"$ref":"3"},"right":null,"val":2},"parent":{"$ref":"2"},"right":{"$id":"5","left":null,"parent":{"$ref":"3"},"right":null,"val":4},"val":3},"parent":{"$ref":"1"},"right":{"$id":"6","left":null,"parent":{"$ref":"2"},"right":{"$id":"7","left":{"$id":"8","left":null,"parent":{"$ref":"7"},"right":null,"val":9},"parent":{"$ref":"6"},"right":null,"val":13},"val":7},"val":6},"parent":null,"right":{"$id":"9","left":{"$id":"10","left":null,"parent":{"$ref":"9"},"right":null,"val":17},"parent":{"$ref":"1"},"right":{"$id":"11","left":null,"parent":{"$ref":"9"},"right":null,"val":20},"val":18},"val":15}
 * p = 13
 * Output: 15
 *
 *
 * Note:
 *
 * If the given node has no in-order successor in the tree, return null.
 * It's guaranteed that the values of the tree are unique.
 * Remember that we are using the Node type instead of TreeNode type so their string representation are different.
 *
 *
 * Follow up:
 *
 * Could you solve it without looking up any of the node's values?
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 *
 */
public class InorderSuccessorInBST2 {


    // Definition for a Node.
    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    };


    // V0
    // Commented out - no implementation
//    public Node inorderSuccessor(Node node) {
//
//    }

    // V0-1
    // IDEA: BST in-order traversal, node op (gpt)
    /**
     *  NOTE !!!
     *
     *   BST property:   left_child.val < root.val < right_child.val
     *
     *   ```
     *   Binary Search Tree (BST) is a binary tree data structure where each
     *   node follows the ordering property:
     *   left child < parent < right child.
     *   This property enables efficient searching,
     *   insertion, and deletion operations.
     *   ```
     */
    /**
     *  NOTE !!!
     *  key:
     *  - Each node has a parent pointer.
     * 	- We do not have access to the root.
     * 	- The goal is to find the in-order successor,
     * 	   i.e., the node with the smallest value greater than p.val.
     *  -> We can solve this without comparing values by using `BST in-order traversal` logic.
     * time = O(H)
     * space = O(1)
     */
    // TODO: validate
    public Node inorderSuccessor_0_1(Node node) {
        if (node == null) return null;

        // Case 1: Node has right child
        // The successor is the **leftmost node in right subtree**
        if (node.right != null) {
            Node curr = node.right;
            while (curr.left != null) {
                curr = curr.left;
            }
            return curr;
        }

        // Case 2: Node has no right child
        // Move up until we find a node that is a **left child of its parent**
        Node curr = node;
        while (curr.parent != null && curr.parent.right == curr) {
            curr = curr.parent;
        }
        return curr.parent; // could be null if no successor
    }

    // V0-2
    // IDEA: BST in-order traversal, node op (gpt)
    // Could you solve it without looking up any of the node's values?
    /**
     * time = O(H)
     * space = O(1)
     */
    public Node inorderSuccessor_0_2(Node node) {
        if (node == null) return null;

        // Case 1: Node has right subtree
        if (node.right != null) {
            Node curr = node.right;
            while (curr.left != null) {
                curr = curr.left;
            }
            return curr;
        }

        // Case 2: Node has no right subtree
        Node curr = node;
        while (curr.parent != null && curr.parent.right == curr) {
            curr = curr.parent;
        }
        return curr.parent;  // Could be null if no successor exists
    }

    // V0-3
    // IDEA: node op (gemini)
    // TODO: validate
    /**
     * time = O(H)
     * space = O(1)
     */
    public Node inorderSuccessor_0_3(Node p) {
        if (p == null) {
            return null;
        }

        // Case 1: The node has a right child.
        // The successor is the LEFT-MOST node in the right subtree.
        if (p.right != null) {
            Node current = p.right;
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }

        // Case 2: The node does NOT have a right child.
        // The successor is the first ancestor for which the current node is in its LEFT subtree.
        Node current = p;
        Node parent = p.parent;

        while (parent != null && current == parent.right) {
            // Keep moving up the tree as long as 'current' is a RIGHT child of its parent.
            // This means 'current' and its ancestors are smaller than the successor.
            current = parent;
            parent = parent.parent;
        }

        // If the loop terminated because parent became null, 'p' is the rightmost node
        // in the tree (the largest value), and has no successor.
        // Otherwise, 'parent' is the successor (the first ancestor that we came up from the left).
        return parent;
    }

    // V1
    // https://leetcode.ca/2017-04-23-510-Inorder-Successor-in-BST-II/
    /**
     * time = O(H)
     * space = O(1)
     */
    public Node inorderSuccessor_1(Node node) {
        if (node.right != null) {
            node = node.right;
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }
        while (node.parent != null && node == node.parent.right) {
            node = node.parent;
        }
        return node.parent;
    }

    // V2
    // https://www.cnblogs.com/grandyang/p/10424982.html

    // V3
    // https://blog.csdn.net/qq_46105170/article/details/127610920


}
