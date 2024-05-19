package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-linked-list/

import LeetCodeJava.DataStructure.ListNode;

public class ReverseLinkedList {

    class Solution {

        // V0
        // IDEA : iteration
        public ListNode reverseList(ListNode head) {

            if (head == null) {
                return null;
            }

            ListNode _prev = null;

            while (head != null) {
                /**
                 *  NOTE !!!!
                 *
                 *   4 operations
                 *
                 *    step 1) cache next
                 *    step 2) point cur to prev
                 *    step 3) move prev to cur
                 *    step 4) move cur to next
                 */
                ListNode _next = head.next;
                head.next = _prev;
                _prev = head;
                head = _next;
            }

            // NOTE!!! we return _prev here, since it's now "new head"
            return _prev;

        }

        // V0'
        // IDEA : iteration
        public ListNode reverseList_0(ListNode head) {

            if (head == null){
                return head;
            }

            /**
             *    1 -> 2 -> 3
             *
             *   pre 1 -> 2 -> 3
             *    ↑  ↑
             *
             *   pre <- 1 -> 2 -> 3
             *          ↑    ↑
             *
             *   pre <- 1 <- 2 -> 3
             *               ↑    ↑
             */

            /**
             *  Can't set prev as  new ListNode(),
             *  since it has default value = 0,
             *  which is not null value,
             *  should set as null instead
             */
//            ListNode prev = new ListNode();
//            System.out.println("prev.val = " + prev.val);

            ListNode prev = null;

            while (head != null){
                /**
                 *  NOTE !!!!
                 *
                 *   4 operations
                 *
                 *    step 1) cache next
                 *    step 2) point cur to prev
                 *    step 3) move prev to cur
                 *    step 4) move cur to next
                 */
                ListNode next = head.next;
                head.next = prev;
                prev = head;
                head = next;
            }

            return prev;
        }

        // V1
        // IDEA : iteration
        // https://leetcode.com/problems/reverse-linked-list/editorial/
        public ListNode reverseList_2(ListNode head) {
            ListNode prev = null;
            ListNode curr = head;
            while (curr != null) {
                ListNode nextTemp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nextTemp;
            }
            return prev;
        }

        // V2
        // IDEA : Recursive
        // https://leetcode.com/problems/reverse-linked-list/editorial/
        public ListNode reverseList_3(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode p = reverseList_3(head.next);
            head.next.next = head;
            head.next = null;
            return p;
        }

    }
}
