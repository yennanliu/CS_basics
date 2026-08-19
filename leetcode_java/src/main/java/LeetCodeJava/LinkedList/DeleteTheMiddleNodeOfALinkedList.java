package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/delete-the-middle-node-of-a-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2095. Delete the Middle Node of a Linked List
 *  Medium
 *
 *  You are given the head of a linked list. Delete the middle node, and return the
 *  head of the modified linked list.
 *
 *  The middle node of a linked list of size n is the floor(n / 2)th node from the
 *  start using 0-based indexing, where floor(x) denotes the largest integer less
 *  than or equal to x.
 *
 *  For n = 1, 2, 3, 4, and 5, the middle nodes are 0, 1, 1, 2, and 2, respectively.
 *
 *  Example 1:
 *    Input: head = [1,3,4,7,1,2,6]
 *    Output: [1,3,4,1,2,6]
 *    Explanation: Since n = 7, node 3 with value 7 is the middle node.
 *
 *  Example 2:
 *    Input: head = [1,2,3,4]
 *    Output: [1,2,4]
 *    Explanation: For n = 4, node 2 with value 3 is the middle node.
 *
 *  Example 3:
 *    Input: head = [2,1]
 *    Output: [2]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [1, 10^5].
 *    1 <= Node.val <= 10^5
 */
public class DeleteTheMiddleNodeOfALinkedList {

    // V0
    // IDEA: SLOW / FAST POINTERS, WITH `prev` LAGGING ONE NODE BEHIND slow
    //       the classic tortoise-and-hare lands `slow` on the middle node, but to
    //       UNLINK it we need the node BEFORE it -> keep `prev` trailing slow.
    //       starting both at head and advancing fast 2 steps per iteration puts
    //       slow exactly on index floor(n / 2), which is the problem's middle.
    //       NOTE !!! a single-node list has nothing left after the deletion -> null.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteMiddle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }

        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        prev.next = slow.next;
        return head;
    }
}
