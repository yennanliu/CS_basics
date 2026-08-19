package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  1290. Convert Binary Number in a Linked List to Integer
 *  Easy
 *
 *  Given head which is a reference node to a singly-linked list.
 *  The value of each node in the linked list is either 0 or 1.
 *  The linked list holds the binary representation of a number.
 *
 *  Return the decimal value of the number in the linked list.
 *
 *  The most significant bit is at the head of the linked list.
 *
 *  Example 1:
 *    Input: head = [1,0,1]
 *    Output: 5
 *    Explanation: (101) in base 2 = (5) in base 10
 *
 *  Example 2:
 *    Input: head = [0]
 *    Output: 0
 *
 *  Constraints:
 *    The Linked List is not empty.
 *    Number of nodes will not exceed 30.
 *    Each node's value is either 0 or 1.
 */
public class ConvertBinaryNumberInALinkedListToInteger {

    // V0
    // IDEA: LINKED LIST traversal + BIT SHIFT (HORNER'S RULE)
    //       head holds the MOST significant bit, so we can accumulate left to
    //       right in a single pass:  res = res * 2 + node.val
    //       (equivalently res = (res << 1) | node.val)
    //       -> no need to collect the bits first / know the length up front.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int getDecimalValue(ListNode head) {
        int res = 0;
        while (head != null) {
            res = (res << 1) | head.val;
            head = head.next;
        }
        return res;
    }
}
