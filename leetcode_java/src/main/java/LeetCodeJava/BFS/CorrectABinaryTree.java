package LeetCodeJava.BFS;

// https://leetcode.com/problems/correct-a-binary-tree/description/

import java.util.*;
import LeetCodeJava.DataStructure.TreeNode;

/**
 * 1660. Correct a Binary Tree
 * Medium
 *
 * You have a binary tree with a small defect. There is exactly one invalid node where its right
 * child incorrectly points to another node at the same depth but to the invalid node's right.
 *
 * Given the root of the binary tree with this defect, root, return the root of the binary tree
 * after removing this invalid node and every node underneath it (minus the node it incorrectly
 * points to).
 *
 * Custom testing:
 *
 * The test input is read as 3 lines:
 *
 * TreeNode root
 * int fromNode (not available to correctBinaryTree)
 * int toNode (not available to correctBinaryTree)
 *
 * After the binary tree rooted at root is parsed, the TreeNode with value of fromNode will have
 * its right child pointer pointing to the TreeNode with a value of toNode. Then, root is passed
 * to correctBinaryTree.
 *
 *
 * Example 1:
 *
 * Input: root = [1,2,3], fromNode = 2, toNode = 3
 * Output: [1,null,3]
 * Explanation: The node with value 2 is invalid, so remove it.
 *
 * Example 2:
 *
 * Input: root = [8,3,1,7,null,9,4,2,null,null,null,5,6], fromNode = 7, toNode = 4
 * Output: [8,3,1,null,null,9,4,null,null,5,6]
 * Explanation: The node with value 7 is invalid, so remove it and the node underneath it, node 2.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [3, 10^4].
 * -10^9 <= Node.val <= 10^9
 * All Node.val are unique.
 * fromNode != toNode
 * fromNode and toNode will exist in the tree and will be on the same depth.
 * toNode is to the right of fromNode.
 * fromNode.right is null in the initial tree from the test data.
 *
 */
public class CorrectABinaryTree {

    // V0
    // IDEA: DFS VISITING RIGHT BEFORE LEFT (THE BAD EDGE POINTS TO AN ALREADY-SEEN NODE)
    //  the invalid node's right child sits on the same depth further RIGHT, so a
    //  right-first traversal has always seen that target before reaching the invalid node.
    //  When node.right is already seen, node is the invalid one -> detach it (return null).
    //  NOTE: the set holds node objects -- identity is what the defective pointer shares.
    /**
     * time = O(N)
     * space = O(N)
     */
    public TreeNode correctBinaryTree(TreeNode root) {
        Set<TreeNode> seen = new HashSet<>();
        return removeInvalid(root, seen);
    }

    /** Returns node with the invalid node (if under it) cut out; visits right subtree first. */
    private TreeNode removeInvalid(TreeNode node, Set<TreeNode> seen) {
        if (node == null) {
            return null;
        }
        if (node.right != null && seen.contains(node.right)) {
            return null;
        }
        seen.add(node);
        node.right = removeInvalid(node.right, seen);
        node.left = removeInvalid(node.left, seen);
        return node;
    }

    // V1
    // IDEA: BFS LEVEL ORDER, RIGHT TO LEFT, WITH PARENT POINTERS
    //  scanning each level right to left, the target of the bad pointer is enqueued
    //  (seen) before the invalid node is popped. When a popped node's right child is
    //  already in the seen set, unhook that node from its parent.
    /**
     * time = O(N)
     * space = O(N)
     */
    public TreeNode correctBinaryTree_1(TreeNode root) {
        Map<TreeNode, TreeNode> parentOf = new HashMap<>();
        Set<TreeNode> seen = new HashSet<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        seen.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();

            // node points right to a node already seen on its level -> it is the invalid one
            if (node.right != null && seen.contains(node.right)) {
                TreeNode parent = parentOf.get(node);
                if (parent.left == node) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
                return root;
            }

            // right child first, so each level is scanned right to left
            if (node.right != null) {
                seen.add(node.right);
                parentOf.put(node.right, node);
                queue.offer(node.right);
            }
            if (node.left != null) {
                seen.add(node.left);
                parentOf.put(node.left, node);
                queue.offer(node.left);
            }
        }
        return root;
    }
}
