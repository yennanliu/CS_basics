package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-linked-list/

public class ReverseLinkedList {

    class Solution {

        // V0
        public ListNode reverseList(ListNode head) {

            if (head == null) {
                return null;
            }

            ListNode _prev = null;

            while (head != null) {
                ListNode _next = head.next;
                head.next = _prev;
                _prev = head;
                head = _next;
            }

            // NOTE!!! we return _prev here, since it's now "new head"
            return _prev;

        }

    }
}
