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
  // TODO : implement
//  public ListNode plusOne(ListNode head) {
//  }


  // V1
  // IDEA : LINKED LIST OP (gpt)
  /**
   *  Step 1) reverse linked list
   *  Step 2) plus 1, bring `carry` to next digit if curSum > 9, ... repeat for all nodes
   *  Step 3) reverse linked list again
   */
  public ListNode plusOne_1(ListNode head) {
    if (head == null) return new ListNode(1); // Handle edge case

    // Reverse the linked list
    head = reverseList(head);

    // Add one to the reversed list
    ListNode current = head;
    int carry = 1; // Start with adding one

    while (current != null && carry > 0) {
      int sum = current.val + carry;
      current.val = sum % 10; // Update the current node value
      carry = sum / 10; // Calculate carry for the next node
      if (current.next == null && carry > 0) {
        current.next = new ListNode(carry); // Add a new node for carry
        carry = 0; // No more carry after this
      }
      current = current.next;
    }

    // Reverse the list back to original order
    return reverseList(head);
  }

  // Utility to reverse a linked list
  private ListNode reverseList(ListNode head) {
    ListNode prev = null;
    ListNode current = head;

    while (current != null) {
      ListNode next = current.next; // Save the next node
      current.next = prev; // Reverse the link
      prev = current; // Move prev forward
      current = next; // Move current forward
    }

    return prev;
  }

  // V2
  // https://leetcode.ca/2016-12-03-369-Plus-One-Linked-List/
  public ListNode plusOne_2(ListNode head) {
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

  // V3
}
