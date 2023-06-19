package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/middle-of-the-linked-list/

import LeetCodeJava.DataStructure.ListNode;

public class MiddleOfTheLinkedList {

    // V0
    // IDEA : 2 POINTERS
    public ListNode middleNode(ListNode head) {

        if (head == null) {
            return null;
        }

        ListNode _head = head;

        // fast, slow pointers
        while (head != null && _head != null) {

            if (_head.next == null){
                return head;
            }

            _head = _head.next.next;
            head = head.next;
        }

        return head;
    }

    // V1
    // IDEA : OUTPUT TO ARRAY
    // https://leetcode.com/problems/middle-of-the-linked-list/editorial/
    public ListNode middleNode_2(ListNode head) {
        ListNode[] A = new ListNode[100];
        int t = 0;
        while (head != null) {
            A[t++] = head;
            head = head.next;
        }
        return A[t / 2];
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/middle-of-the-linked-list/editorial/
    public ListNode middleNode_3(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

}
