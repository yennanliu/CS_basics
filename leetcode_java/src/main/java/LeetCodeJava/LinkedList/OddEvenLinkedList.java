package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/odd-even-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  328. Odd Even Linked List
 *  Medium
 *
 *  Given the head of a singly linked list, group all the nodes with odd indices
 *  together followed by the nodes with even indices, and return the reordered list.
 *
 *  The first node is considered odd, and the second node is even, and so on.
 *  Note that the relative order inside both the even and odd groups should remain
 *  as it was in the input.
 *
 *  You must solve the problem in O(1) extra space complexity and O(n) time complexity.
 *
 *  Example 1:
 *  Input: head = [1,2,3,4,5]
 *  Output: [1,3,5,2,4]
 *
 *  Example 2:
 *  Input: head = [2,1,3,5,6,4,7]
 *  Output: [2,3,6,7,1,5,4]
 *
 *  Constraints:
 *  The number of nodes in the linked list is in the range [0, 10^4].
 *  -10^6 <= Node.val <= 10^6
 */
public class OddEvenLinkedList {

    // V0
    // IDEA: TWO POINTERS - weave the list in place into an "odd" chain and an
    //       "even" chain, then attach the even head after the odd tail.
    /**
     * time = O(n)
     * space = O(1)
     */
    public ListNode oddEvenList(ListNode head) {

        // edge
        if (head == null || head.next == null) {
            return head;
        }

        ListNode odd = head;          // 1st, 3rd, 5th ...
        ListNode even = head.next;    // 2nd, 4th, 6th ...
        ListNode evenHead = even;     // remember where the even chain starts

        while (even != null && even.next != null) {
            odd.next = even.next;
            odd = odd.next;
            even.next = odd.next;
            even = even.next;
        }

        odd.next = evenHead;

        return head;
    }
}
