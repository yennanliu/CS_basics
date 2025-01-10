package LeetCodeJava.Recursion;

// https://leetcode.com/problems/populating-next-right-pointers-in-each-node/description/
/**
 * 116. Populating Next Right Pointers in Each Node
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given a perfect binary tree where all leaves are on the same level, and every parent has two children. The binary tree has the following definition:
 *
 * struct Node {
 *   int val;
 *   Node *left;
 *   Node *right;
 *   Node *next;
 * }
 * Populate each next pointer to point to its next right node. If there is no next right node, the next pointer should be set to NULL.
 *
 * Initially, all next pointers are set to NULL.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,5,6,7]
 * Output: [1,#,2,3,#,4,5,6,7,#]
 * Explanation: Given the above perfect binary tree (Figure A), your function should populate each next pointer to point to its next right node, just like in Figure B. The serialized output is in level order as connected by the next pointers, with '#' signifying the end of each level.
 * Example 2:
 *
 * Input: root = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 212 - 1].
 * -1000 <= Node.val <= 1000
 *
 */
public class PopulatingNextRightPointersInEachNode {

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;

        public Node() {}

        public Node(int _val) {
            val = _val;
        }

        public Node(int _val, Node _left, Node _right, Node _next) {
            val = _val;
            left = _left;
            right = _right;
            next = _next;
        }
    }

    // V0
//    public Node connect(Node root) {
//
//    }

    // V1
    // IDEA: RECURSION (gpt)
    /**
     *
     *  In this problem, we are tasked with populating the next pointer of each node in a perfect binary tree to point to its next right node. If there is no next right node, the next pointer should be set to null.
     *
     * 1) Key Observations:
     *
     *   - Perfect Binary Tree: A perfect binary tree means that every parent has exactly two children and all leaf nodes are at the same level.
     *   - Constant Extra Space: We are asked to use constant extra space. This means we cannot use additional data structures like queues, lists, or hashmaps.
     *   - Level-order traversal: We can achieve the desired result by using level-by-level traversal to connect each node to its adjacent node in the same level.
     *
     *
     *  2) Solution Approach:
     *
     *  We can solve this using a level-by-level approach, where we iterate over each level and connect the next pointers:
     *
     *  step 1) Start at the root:
     *      The root has no left or right siblings, so it has no next pointer to populate.
     *
     *  step 2) Iterate through each level:
     *      Starting from the root, for each level, connect the left child’s
     *      next to the right child, and if possible, connect the right child’s
     *      next to the next node’s left child.
     *
     *  step 3)
     *     Link nodes on the same level: Since it’s a perfect binary tree,
     *     every level is fully filled. So, the left child of a node always
     *     has a sibling (right child), and we can set their next pointers accordingly.
     *
     *  step 4)
     *    Use only constant space: We don’t use any extra data structures.
     *    Instead, we leverage the tree’s structure, using next pointers to move across
     *    the tree level by level.
     *
     *
     *  3) Explanation:
     *
     *   Initial check: If the root is null, we return null immediately as there’s no tree to process.
     *
     *   Outer while loop: We start at the root and move down level by level. currentLevel represents the leftmost node of the current level.
     *
     *   Inner while loop: We process each node at the current level. For each node:
     *
     *   We connect the left child to the right child by setting temp.left.next = temp.right.
     *  If there is a next node (temp.next), we connect the right child of the current node to the left child of the next node by setting temp.right.next = temp.next.left.
     *   Move to next level: After processing all nodes at the current level, we move to the leftmost node of the next level by setting currentLevel = currentLevel.left.
     *
     *   Return the root: The tree is now updated with the correct next pointers, and we return the root node.
     *
     *  5) Performance
     *
     * - Time Complexity: O(n) where n is the number of nodes in the tree. We traverse each node exactly once.
     * - Space Complexity: O(1). Since we are not using any extra space apart from the input tree and a few constant variables, the space complexity is constant.
     *
     */
    public Node connect_1(Node root) {
        if (root == null) {
            return null;
        }

        // Start with the root node
        Node currentLevel = root;

        while (currentLevel != null) {
            // Traverse through the current level
            Node temp = currentLevel;
            while (temp != null) {
                if (temp.left != null) {
                    // Connect the left child to the right child
                    temp.left.next = temp.right;
                }

                if (temp.right != null && temp.next != null) {
                    // Connect the right child to the next node's left child
                    temp.right.next = temp.next.left;
                }

                // Move to the next node in the current level
                temp = temp.next;
            }

            // Move to the next level (leftmost node of the next level)
            currentLevel = currentLevel.left;
        }

        return root;
    }


    // V2
}
