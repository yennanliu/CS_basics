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
        /**
         * time = O(N)
         * space = O(1)
         */
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

        // V-0-0-1
        // IDEA: iteration
        /**
         * time = O(N)
         * space = O(1)
         */
        public ListNode reverseList_0_0_1(ListNode head) {
            // edge
            if(head == null || head.next == null) {
                return head;
            }

            ListNode _prev = null;
            /**
             *  NOTE !!!
             *
             *   we CAN'T just assign `_prev` init val to `res`
             *   and return `res` as result
             *   e.g. this is WRONG: return res;
             *
             *  Reason:
             *   - At this point, res is just a reference to null.
             * 	 - As you update _prev during the loop,
             * 	   res DOES NOT magically follow _prev.
             * 	   It stays stuck at the value it was assigned
             * 	   when you created it → null.
             *
             * 	 So by the end:
             * 	  - _prev points to the new head of the reversed list ✅
             * 	  - res is still null ❌
             *
             *
             * 	-> Java references don’t “track” each other after assignment.
             *    res = _prev copies the reference value `at that moment`
             *    If _prev later changes, res won’t update.
             *
             */
            ListNode res = _prev;
            while(head != null){
                ListNode _next = head.next;
                head.next = _prev;
                //_prev.next = head;
                _prev = head;
                head = _next;
            }

            //return res;
            return _prev;
        }


        // V0-1
        // IDEA : iteration
        /**
         * time = O(N)
         * space = O(1)
         */
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
        // IDEA: RECURSION  (Tail-Recursive Style) (fixed by gpt)
        /**
         * time = O(N)
         * space = O(N)
         */
        public ListNode reverseList_0_2(ListNode head) {
            return reverseHelper(head, null);
        }

        /**
         *  NOTE !!!
         *
         *   for reverseHelper method
         *
         *   the 1st param is `current`
         *       2nd param is `prev`
         */
        /**
         * time = O(1)
         * space = O(1)
         */
        private ListNode reverseHelper(ListNode current, ListNode prev) {
            // Base case: end of the list
            if (current == null) {
                return prev;
            }

            ListNode next = current.next;
            current.next = prev;

            /**
             *  NOTE !!!
             *
             *   below we pass `next` as `current`
             *           and `current` as `prev`
             *        to the reverseHelper recursion call
             *
             *  -> so, in the next recursive call
             *        current is next
             *        and
             *        prev is current
             *
             *   -> which is same as the logic we have in iteration version code
             *      e.g.
             *                 prev = head;
             *                 head = next;
             *
             *
             */
            return reverseHelper(next, current);
        }

        // V0-3
        // IDEA: RECURSION (Backtracking Style Instead) (gpt)
        /**
         * time = O(N)
         * space = O(N)
         */
        public ListNode reverseList_0_3(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }

            ListNode newHead = reverseList_0_3(head.next);
            head.next.next = head;
            head.next = null;

            return newHead;
        }

        // V1
        // IDEA : iteration
        // https://leetcode.com/problems/reverse-linked-list/editorial/
        /**
         * time = O(N)
         * space = O(1)
         */
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
        /**
         * time = O(N)
         * space = O(N)
         */
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
