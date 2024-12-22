package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/plus-one-linked-list/description/
// https://leetcode.ca/all/369.html

import LeetCodeJava.DataStructure.ListNode;

/**
 * 369. Plus One Linked List Given a non-negative integer represented as non-empty a singly linked
 * list of digits, plus one to the integer.
 *
 * <p>You may assume the integer do not contain any leading zero, except the number 0 itself.
 *
 * <p>The digits are stored such that the most significant digit is at the head of the list.
 *
 * <p>Example :
 *
 * <p>Input: [1,2,3] Output: [1,2,4] Difficulty: Medium Lock: Prime Company: Amazon Apple Google
 */
public class PlusOneLinkedList {

  /**
   * Definition for singly-linked list.
   * public class ListNode {
   *     int val;
   *     ListNode next;
   *     ListNode() {}
   *     ListNode(int val) { this.val = val; }
   *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
   * }
   */

  // V0
//  public ListNode plusOne(ListNode head) {
//  }

  // V1
  // https://leetcode.ca/2016-12-03-369-Plus-One-Linked-List/
  public ListNode plusOne_1(ListNode head) {
    ListNode dummy = new ListNode(0, head);
    ListNode target = dummy;
    while (head != null) {
      if (head.val != 9) {
        target = head;
      }
      head = head.next;
    }
    ++target.val;
    target = target.next;
    while (target != null) {
      target.val = 0;
      target = target.next;
    }
    return dummy.val == 1 ? dummy : dummy.next;
  }

  // V2
}
