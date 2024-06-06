package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reorder-list/

import LeetCodeJava.DataStructure.ListNode;

public class ReorderList {

    // V0
    // IDEA : split linked list, reorder + merge (fixed by GPT)
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
    /**
     *
     *
     *  example 1:
     *
     *  Input: head = [1,2,3,4]
     *  Output: [1,4,2,3]
     *
     *  make 2 node
     *   - 1st half node : 1->2
     *   - 2nd half  node : 3->4
     *
     * then reverse even node: 4 -> 3
     * then merge 2 node:
     *   start from odd, then even
     *   ...
     *
     *   1 -> 4 -> 2 -> 3
     *
     *
     *  example 2 :
     *
     *  Input: head = [1,2,3,4,5]
     *  Output: [1,5,2,4,3]
     *
     *
     *  make 2 node
     *
     *  - 1st half node : 1->2 -> 3 (?)
     *  - 2nd half  node : 4 -> 5
     *
     *  reverse even node : 5 -> 4
     *
     *  then merge 2 node:
     *      start from odd, then even
     *      ...
     *
     *   1 -> 5 -> 2 -> 4 -> 3
     *
     */
    public void reorderList(ListNode head) {
        if (head == null || head.next == null) {
            return;
        }

        // Find the middle of the list (via fast - slow pointer)
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Reverse the second half of the list
        /**
         *  NOTE !!!!
         *
         *   4 operations
         *
         *    step 1) cache next
         *    step 2) point cur to prev
         *    step 3) move prev to cur
         *    step 4) move cur to next
         *
         */
        // NOTE !!!  slow is actually the "right half linked list"
        ListNode prev = null;
        ListNode curr = slow;
        while (curr != null) {
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }

        // Merge the two halves
        /** NOTE !!! setup below val for clear code */
        ListNode firstHalf = head;
        ListNode secondHalf = prev;

        while (secondHalf.next != null) {
            /**
             *  3 steps
             *
             *  step 1) get next
             *  step 2) point cur to next
             *  step 3) move cur to next
             *
             */
            ListNode temp1 = firstHalf.next;
            ListNode temp2 = secondHalf.next;

            firstHalf.next = secondHalf;
            secondHalf.next = temp1;

            firstHalf = temp1;
            secondHalf = temp2;
        }
    }

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
