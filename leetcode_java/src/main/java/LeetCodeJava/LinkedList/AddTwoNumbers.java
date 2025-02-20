package LeetCodeJava.LinkedList;

import LeetCodeJava.DataStructure.ListNode;

// https://leetcode.com/problems/add-two-numbers/description/
/**
 * 2. Add Two Numbers
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [7,0,8]
 * Explanation: 342 + 465 = 807.
 * Example 2:
 *
 * Input: l1 = [0], l2 = [0]
 * Output: [0]
 * Example 3:
 *
 * Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * Output: [8,9,9,9,0,0,0,1]
 *
 *
 * Constraints:
 *
 * The number of nodes in each linked list is in the range [1, 100].
 * 0 <= Node.val <= 9
 * It is guaranteed that the list represents a number that does not have leading zeros.
 *
 *
 */

public class AddTwoNumbers {

    // V0
    // IDEA : STACK + number op
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        if (l1 == null && l2 == null){
            return l1;
        }

        if (l1 == null && l2 != null){
            return l2;
        }

        if (l1 != null && l2 == null){
            return l1;
        }

        ListNode dummy = new ListNode();
        ListNode node = dummy;

        int extra = 0;
        int tmp = 0;

        while (l1 != null || l2 != null){
            tmp += extra;
            extra = 0;
            if (l1 != null){
                tmp += l1.val;
                l1 = l1.next;
            }
            if (l2 != null){
                tmp += l2.val;
                l2 = l2.next;
            }
            if (tmp >= 10){
                tmp -= 10;
                extra = 1;
            }

            node.next = new ListNode(tmp);
            node = node.next;
            tmp = 0;
        }

        if (extra != 0){
            node.next = new ListNode(extra);
            node = node.next;
        }

        return dummy.next;
    }

    // V0-1
    // IDEA: LINKED LIST = STR OP (fixed by gpt)
    public ListNode addTwoNumbers_0_1(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0); // Create a dummy head for the result list
        ListNode current = dummyHead; // Pointer to build the result list
        int carry = 0;

        // Traverse both lists and add corresponding digits
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry; // Start with carry value from the previous iteration

            if (l1 != null) {
                sum += l1.val; // Add the value from the first list if it's not null
                l1 = l1.next; // Move to the next node in l1
            }

            if (l2 != null) {
                sum += l2.val; // Add the value from the second list if it's not null
                l2 = l2.next; // Move to the next node in l2
            }

            // Determine the new carry and the current digit
            carry = sum / 10;
            current.next = new ListNode(sum % 10); // Create a new node with the current digit
            current = current.next; // Move to the next node in the result list
        }

        return dummyHead.next; // Return the result list, excluding the dummy head
    }


    // V1
    // https://leetcode.com/problems/add-two-numbers/editorial/
    // IDEA : Math
    // Add Two Numbers (Java improved)
    public ListNode addTwoNumbers_1(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode curr = dummyHead;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;
            int sum = carry + x + y;
            carry = sum / 10;
            curr.next = new ListNode(sum % 10);
            curr = curr.next;
            if (l1 != null)
                l1 = l1.next;
            if (l2 != null)
                l2 = l2.next;
        }
        return dummyHead.next;
    }

}
