package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-linked-list/
/**
 *  206. Reverse Linked List
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the head of a singly linked list, reverse the list, and return the reversed list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5]
 * Output: [5,4,3,2,1]
 * Example 2:
 *
 *
 * Input: head = [1,2]
 * Output: [2,1]
 * Example 3:
 *
 * Input: head = []
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is the range [0, 5000].
 * -5000 <= Node.val <= 5000
 *
 *
 * Follow up: A linked list can be reversed either iteratively or recursively. Could you implement both?
 *
 */
import LeetCodeJava.DataStructure.ListNode;

public class ReverseLinkedList {

        // V0
        // IDEA : iteration
        public ListNode reverseList(ListNode head) {

            if (head == null) {
                return null;
            }

            /**
             * NOTE !!!
             *
             *  init _prev as null (but NOT `new ListNode()`
             *  -> (or the result is wrong) (`new ListNode()` default val is 1, instead of null)
             *
             *
             *  e.g. below is WRONG !!!
             *   ListNode _prev = new ListNode();
             */
            ListNode _prev = null; // NOTE !!! this

            while (head != null) {
                /**
                 *  NOTE !!!!
                 *
                 *   4 operations
                 *
                 *    Step 0) set _prev as null
                 *    step 1) cache next
                 *    step 2) point cur to prev
                 *    step 3) move prev to cur  (NOTE !!! the step)
                 *    step 4) move cur to next
                 */
                ListNode _next = head.next;
                head.next = _prev;
                /** NOTE !!!
                 *
                 *  -> have to assign _prev val to head first,
                 *     then assign head val to _next,
                 *     since if we assign head val to _next first,
                 *     then head is changed (become "_next" node),
                 *     then we will assign _prev to _next node,
                 *     which is WRONG
                 */
                _prev = head;
                head = _next;
            }

            /**
             *  NOTE!!!
             *
             *  return `_prev` here, since it's now "new head"
             */
            return _prev;

        }

        // V0-1
        // IDEA : iteration
        public ListNode reverseList_0_1(ListNode head) {

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

        // V0-2
        // IDEA: RECURSION (fixed by gpt)
        public ListNode reverseList_0_2(ListNode head) {
            return reverseHelper(head, null);
        }

        private ListNode reverseHelper(ListNode current, ListNode prev) {
            // Base case: end of the list
            if (current == null) {
                return prev;
            }

            ListNode next = current.next;
            current.next = prev;
            return reverseHelper(next, current);
        }
        
        // V1
        // IDEA : iteration
        // https://leetcode.com/problems/reverse-linked-list/editorial/
        public ListNode reverseList_1(ListNode head) {
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
        public ListNode reverseList_2(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode p = reverseList_2(head.next);
            head.next.next = head;
            head.next = null;
            return p;
        }

}
