package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/sort-linked-list-already-sorted-using-absolute-values/description/

import LeetCodeJava.DataStructure.ListNode;

/**
 * 2046. Sort Linked List Already Sorted Using Absolute Values
 * Medium
 *
 * Given the head of a singly linked list that is sorted in non-decreasing order using the absolute values of its nodes, return the list sorted in non-decreasing order using the actual values of its nodes.
 *
 *
 * Example 1:
 *
 * Input: head = [0,2,-5,5,10,-10]
 * Output: [-10,-5,0,2,5,10]
 * Explanation:
 * The list sorted in non-descending order using the absolute values of the nodes is [0,2,-5,5,10,-10].
 * The list sorted in non-descending order using the actual values is [-10,-5,0,2,5,10].
 *
 * Example 2:
 *
 * Input: head = [0,1,2]
 * Output: [0,1,2]
 * Explanation:
 * The linked list is already sorted in non-decreasing order.
 *
 * Example 3:
 *
 * Input: head = [1]
 * Output: [1]
 * Explanation:
 * The linked list is already sorted in non-decreasing order.
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is the range [1, 10^5].
 * -5000 <= Node.val <= 5000
 * head is sorted in non-decreasing order using the absolute value of its nodes.
 *
 */
public class SortLinkedListAlreadySortedUsingAbsoluteValues {

    // V0
    // IDEA: MOVE EVERY NEGATIVE NODE TO THE FRONT (HEAD INSERTION)
    //  sorted by absolute value means the negatives, read left to right, have DECREASING
    //  actual values (-5 before -10). so the answer is (negatives reversed) + (non-negatives).
    //  one pass: whenever the next node is negative, unlink it and push it to the front;
    //  taking them in encounter order and always pushing to the front reverses them.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode sortLinkedList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode prev = head;
        ListNode cur = head.next;
        while (cur != null) {
            if (cur.val < 0) {
                // unlink cur, then insert it before the current head
                prev.next = cur.next;
                cur.next = head;
                head = cur;
                // prev stays put: its new next is the node after the one we moved
                cur = prev.next;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        return head;
    }
}
