package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/split-a-circular-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2674. Split a Circular Linked List
 *  Medium
 *
 *  Given a circular linked list of positive integers, split it into 2 circular
 *  linked lists so that the first one contains the first half of the nodes
 *  (exactly ceil(list.length / 2) nodes) in the same order they appeared in list,
 *  and the second one contains the rest of the nodes in the same order.
 *
 *  Return an array answer of length 2 in which the first element is a circular
 *  linked list representing the first half and the second element is a circular
 *  linked list representing the second half.
 *
 *  Example 1:
 *    Input: nums = [1,5,7]
 *    Output: [[1,5],[7]]
 *    Explanation: The initial list has 3 nodes so the first half is the first
 *                 ceil(3 / 2) = 2 elements, the remaining 1 node is the second half.
 *
 *  Example 2:
 *    Input: nums = [2,6,1,5]
 *    Output: [[2,6],[1,5]]
 *
 *  Constraints:
 *    The number of nodes in list is in the range [2, 10^5]
 *    0 <= Node.val <= 10^9
 *    LastNode.next = FirstNode
 */
public class SplitACircularLinkedList {

    // V0
    // IDEA: FAST / SLOW POINTERS ON A CIRCULAR LIST
    //       walk `slow` 1 step and `fast` 2 steps per round, stopping as soon as
    //       `fast` is the tail (fast.next == head) or one node before it
    //       (fast.next.next == head):
    //         - odd length  -> fast IS the tail, slow sits on node #ceil(n/2)
    //         - even length -> fast is one before the tail, push it forward once,
    //                          slow again sits on node #ceil(n/2) == n/2
    //       because the list is circular there is no null sentinel: every stop
    //       condition compares against `head`.
    //       after cutting we must close BOTH rings:
    //         tail.next -> second head, slow.next -> head
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode[] splitCircularLinkedList(ListNode list) {
        ListNode head = list;
        ListNode slow = head;
        ListNode fast = head;

        while (fast.next != head && fast.next.next != head) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // even length: fast is one before the tail -> move onto the tail
        if (fast.next != head) {
            fast = fast.next;
        }

        ListNode second = slow.next;
        slow.next = head;   // close the 1st ring
        fast.next = second; // close the 2nd ring

        return new ListNode[]{head, second};
    }
}
