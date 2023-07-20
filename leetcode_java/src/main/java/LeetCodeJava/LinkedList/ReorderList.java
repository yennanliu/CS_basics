package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reorder-list/

import LeetCodeJava.DataStructure.ListNode;

public class ReorderList {

    // V0
//    public void reorderList(ListNode head) {
//
//        if (head.equals(null) || head.next.equals(null)){
//            return;
//        }
//
//        int n = 0;
//        ListNode _tmp = head;
//        while (!_tmp.next.equals(null)){
//            _tmp = _tmp.next;
//            n += 1;
//        }
//
//        ListNode dummy = new ListNode();
//        dummy.next = head;
//
//        for (int j = 0; j < n; j++){
//            int _start = j;
//            int _end = n - j;
//            ListNode _tmp2 = head;
//            while(!_tmp2.next.equals(null)){
//
//            }
//
//        }
//
//        return;
//
//    }

    // V1
    // https://leetcode.com/problems/reorder-list/editorial/
    /**
     *  IDEA :
     *
     *     This problem is a combination of these three easy problems:
     *
     *     LC 876 Middle of the Linked List.
     *
     *     LC 206 Reverse Linked List.
     *
     *     LC 21 Merge Two Sorted Lists.
     */

    public void reorderList_2(ListNode head) {
        if (head == null) return;

        // find the middle of linked list [Problem 876]
        // in 1->2->3->4->5->6 find 4
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // reverse the second part of the list [Problem 206]
        // convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4
        // reverse the second half in-place
        ListNode prev = null, curr = slow, tmp;
        while (curr != null) {
            tmp = curr.next;

            curr.next = prev;
            prev = curr;
            curr = tmp;
        }

        // merge two sorted linked lists [Problem 21]
        // merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4
        ListNode first = head, second = prev;
        while (second.next != null) {
            tmp = first.next;
            first.next = second;
            first = tmp;

            tmp = second.next;
            second.next = first;
            second = tmp;
        }
    }
    
}
