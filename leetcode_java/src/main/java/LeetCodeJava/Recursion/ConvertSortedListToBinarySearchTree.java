package LeetCodeJava.Recursion;

// https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/description/

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;

/**
 *  109. Convert Sorted List to Binary Search Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a singly linked list where elements are sorted in ascending order, convert it to a height-balanced binary search tree.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [-10,-3,0,5,9]
 * Output: [0,-3,9,-10,null,5]
 * Explanation: One possible answer is [0,-3,9,-10,null,5], which represents the shown height balanced BST.
 * Example 2:
 *
 * Input: head = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in head is in the range [0, 2 * 104].
 * -105 <= Node.val <= 105
 *
 *
 *
 */
public class ConvertSortedListToBinarySearchTree {

    // V0
//    public TreeNode sortedListToBST(ListNode head) {
//
//    }


    // V1

    // V2
    // https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/solutions/6515817/beats-100-best-interview-approach-notes-6v9y6/
    public TreeNode sortedListToBST_2(ListNode head) {
        if (head == null)
            return null;
        if (head.next == null)
            return new TreeNode(head.val);
        ListNode slow = head, fast = head, slow_Prev = null;
        while (fast != null && fast.next != null) {
            slow_Prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode root = new TreeNode(slow.val);//Making A Root Node
        slow_Prev.next = null;//Braeking The Link For L1
        root.left = sortedListToBST_2(head);
        root.right = sortedListToBST_2(slow.next);
        return root;
    }


    // V3
    // https://leetcode.com/problems/convert-sorted-list-to-binary-search-tree/solutions/3282028/java-easy-using-slow-fast-pointer-recurs-uszl/
    public TreeNode sortedListToBST_3(ListNode head) {
        if (head == null)
            return null;
        if (head.next == null)
            return new TreeNode(head.val);
        ListNode slow = head;
        ListNode fast = head.next.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode res = new TreeNode(slow.next.val);
        ListNode righthalf = slow.next.next;
        slow.next = null;
        res.left = sortedListToBST_3(head);
        res.right = sortedListToBST_3(righthalf);
        return res;
    }




}
