package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/double-a-number-represented-as-a-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2816. Double a Number Represented as a Linked List
 *  Medium
 *
 *  You are given the head of a non-empty linked list representing a non-negative
 *  integer without leading zeroes.
 *
 *  Return the head of the linked list after doubling it.
 *
 *  Example 1:
 *    Input: head = [1,8,9]
 *    Output: [3,7,8]
 *    Explanation: the list represents 189, and 189 * 2 = 378.
 *
 *  Example 2:
 *    Input: head = [9,9,9]
 *    Output: [1,9,9,8]
 *    Explanation: the list represents 999, and 999 * 2 = 1998.
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [1, 10^4]
 *    0 <= Node.val <= 9
 *    The input is generated such that the list represents a number that does not
 *    have leading zeros, except the number 0 itself.
 */
public class DoubleANumberRepresentedAsALinkedList {

    // V0
    // IDEA: SINGLE FORWARD PASS + LOOK-AHEAD CARRY (NO REVERSING NEEDED)
    //       doubling is special : the carry out of any digit is at most 1, and it is
    //       decided by THAT DIGIT ALONE  (digit * 2 >= 10  <=>  digit >= 5).
    //       so while sitting on `cur` we can just PEEK at cur.next and know whether
    //       it will hand us a carry:
    //
    //         cur.val = (cur.val * 2) % 10 + (cur.next != null && cur.next.val >= 5 ? 1 : 0)
    //
    //       NOTE !!! the only overflow into a NEW leading node happens when the FIRST
    //                digit is >= 5 -> prepend a 0 node and let the same rule fill it.
    //       this rewrites nodes in place -> O(1) extra space, no reverse/reverse-back
    //       and no big-integer conversion.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode doubleIt(ListNode head) {
        if (head.val >= 5) {
            head = new ListNode(0, head);
        }

        ListNode cur = head;
        while (cur != null) {
            cur.val = (cur.val * 2) % 10;
            if (cur.next != null && cur.next.val >= 5) {
                cur.val += 1;
            }
            cur = cur.next;
        }

        return head;
    }
}
