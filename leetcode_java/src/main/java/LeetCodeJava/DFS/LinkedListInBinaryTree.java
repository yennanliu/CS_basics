package LeetCodeJava.DFS;

// https://leetcode.com/problems/linked-list-in-binary-tree/

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1367. Linked List in Binary Tree
 *  Medium
 *
 *  Given a binary tree root and a linked list with head as the first node.
 *
 *  Return True if all the elements in the linked list starting from the head
 *  correspond to some downward path connected in the binary tree otherwise
 *  return False.
 *
 *  In this context downward path means a path that starts at some node and goes
 *  downwards.
 *
 *  Example 1:
 *    Input: head = [4,2,8],
 *           root = [1,4,4,null,2,2,null,1,null,6,8,null,null,null,null,1,3]
 *    Output: true
 *    Explanation: Nodes in blue form a subpath in the binary Tree.
 *
 *  Example 3:
 *    Input: head = [1,4,2,6,8],
 *           root = [1,4,4,null,2,2,null,1,null,6,8,null,null,null,null,1,3]
 *    Output: false
 *    Explanation: There is no path in the binary tree that contains all the
 *                 elements of the linked list from head.
 *
 *  Constraints:
 *    The number of nodes in the tree will be in the range [1, 2500].
 *    The number of nodes in the list will be in the range [1, 100].
 *    1 <= Node.val <= 100 for each node in the linked list and binary tree.
 */
public class LinkedListInBinaryTree {

    // V0
    // IDEA: DOUBLE RECURSION (try to "start the match" at every tree node)
    //       match(head, root) answers "does the list match a downward path
    //       STARTING exactly at root ?". isSubPath tries match at the current
    //       node, else recurses left / right to try a later starting point.
    /**
     * time = O(N * M)   // N = #tree nodes, M = #list nodes
     * space = O(N)      // recursion depth (skewed tree)
     */
    public boolean isSubPath(ListNode head, TreeNode root) {
        if (head == null) {
            return true;
        }
        if (root == null) {
            return false;
        }
        return match(head, root)
                || isSubPath(head, root.left)
                || isSubPath(head, root.right);
    }

    private boolean match(ListNode head, TreeNode node) {
        if (head == null) {
            // whole list consumed
            return true;
        }
        if (node == null) {
            // tree ran out first
            return false;
        }
        if (head.val != node.val) {
            return false;
        }
        return match(head.next, node.left) || match(head.next, node.right);
    }
}
