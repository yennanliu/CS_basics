package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-linked-list-ii/description/

import LeetCodeJava.DataStructure.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 92. Reverse Linked List II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given the head of a singly linked list and two integers left and right where left <= right, reverse the nodes of the list from position left to position right, and return the reversed list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5], left = 2, right = 4
 * Output: [1,4,3,2,5]
 * Example 2:
 *
 * Input: head = [5], left = 1, right = 1
 * Output: [5]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is n.
 * 1 <= n <= 500
 * -500 <= Node.val <= 500
 * 1 <= left <= right <= n
 *
 *
 * Follow up: Could you do it in one pass?
 *
 */

public class ReverseLinkedList2 {

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
  // IDEA: reverse ListNode within start, end idx
  // TODO: fix below
//  public ListNode reverseBetween(ListNode head, int left, int right) {
//      // edge
//      if(head == null || head.next == null){
//          return head;
//      }
//      // ??
//      if(right == left){
//          return head;
//      }
//      if(right < left){
//          throw new RuntimeException("invalid left, right val");
//      }
//
//      /**
//       *  ListNode : reverse nodes withon start, end idx
//       */
//      ListNode dummy = null;
//      dummy.next = head;
//      ListNode _prev = null;
//
//      boolean shouldReverse = false;
//      int idx = 0;
//
//      while(head != null){
//          if(idx < left || idx > right){
//              shouldReverse = false;
//          }
//          else{
//              shouldReverse = true;
//          }
//
//          // get `prev` node before reverse
//          if(idx == left - 1){
//              _prev = head;
//          }
//          // start `reverse`
//          if(shouldReverse){
//              ListNode _next = head.next;
//              head.next = _prev;
//              _prev = head;
//              head = _next;
//              //continue; // /?
//          }
//
//          _prev.next = head; // ???
//          // end reverse
//          if(!shouldReverse){
//              head = head.next;
//          }
//          //head = _next;
//          idx += 1;
//      }
//
//      return dummy.next; // ???
//  }

  // V0-1
  // IDEA: LINKED LIST OP (iteration 1)
  // https://neetcode.io/solutions/reverse-linked-list-ii
  // https://youtu.be/RF_M9tX4Eag?si=K6U8hW9bIoO8mDMW
  // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_92_1.png
  // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_92_2.png
  // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_92_3.png
  // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_92_4.png
  // https://github.com/yennanliu/CS_basics/blob/master/doc/pic/lc_92_5.png
  public ListNode reverseBetween_0_1(ListNode head, int left, int right) {

      /**
       * NOTE !!!
       *
       *  we init dummy, and point it to head
       */
      ListNode dummy = new ListNode(0);
      dummy.next = head;
      ListNode leftPrev = dummy;
      ListNode cur = head;

      /**
       * NOTE !!!
       *
       *  we move to `left - 1 ` idx (instead of `left`)
       *  -> for simpler operation (less edge cases)
       *
       *
       *  NOTE `i < left - 1` idx
       */
      for (int i = 0; i < left - 1; i++) {
          leftPrev = cur;
          cur = cur.next;
      }

      /**
       * NOTE !!!
       *
       *  1) we init prev as null
       *  2) we move i to `right - left + 1` idx,  (instead of `right - left`)
       */
      ListNode prev = null;
      for (int i = 0; i < right - left + 1; i++) {
          ListNode tmpNext = cur.next;
          cur.next = prev;
          prev = cur;
          cur = tmpNext;
      }

    /**
     * NOTE !!!
     *
     *  1) we point  leftPrev.next to `prev`,
     *    which is the head of `reverse linked list`
     *
     *  2) we point  leftPrev.next.next to `cur`,
     *     since we still need the `remaining` linked list
     *     which is NOT reversed
     *     (e.g. linked list with idx > r)
     *
     */
    /**
     * `leftPrev.next` is the `original left pointer` (now become the `end of reversed listNode`)
     * `leftPrev.next.next` is the `head of remaining listNode`
     *
     * -> since now `sub listNode is reversed`,
     *    we need to repoint `the end of reversed listNode` to the `remaining listNode`
     *    -> so that's why `leftPrev.next.next = cur;` is needed
     *
     */
    leftPrev.next.next = cur; //  Connects the end of the reversed sublist to the rest of the list.
    /**
     *  `leftPrev.next` is the `next` node of node before `reversed listNode`
     *   -> so we need to point the `prev reversed listNode` to the `head of reversed listNode`
     *     -> so that's why ` leftPrev.next = prev;` is needed
     */
    leftPrev.next = prev; //  Connects the node before the reversed section to the new head of the reversed sublist.

    return dummy.next;
  }

  // V0-2
  // IDEA: LINKED LIST OP (iteration 2)
  // https://neetcode.io/solutions/reverse-linked-list-ii
  // https://youtu.be/RF_M9tX4Eag?si=vTfAtfbmGwzsmtpi
  public ListNode reverseBetween_0_2(ListNode head, int left, int right) {
      ListNode dummy = new ListNode(0);
      dummy.next = head;
      ListNode prev = dummy;

      for (int i = 0; i < left - 1; i++) {
          prev = prev.next;
      }

      ListNode sublistHead = prev.next;
      ListNode sublistTail = sublistHead;
      for (int i = 0; i < right - left; i++) {
          sublistTail = sublistTail.next;
      }

      ListNode nextNode = sublistTail.next;
      sublistTail.next = null;
      prev.next = reverseList(sublistHead);
      sublistHead.next = nextNode;

      return dummy.next;
  }

    private ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

        while (curr != null) {
            ListNode temp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = temp;
        }
        return prev;
    }

  // V0-3
  // IDEA: ARRAY (fixed by gpt)
  public ListNode reverseBetween_0_3(ListNode head, int left, int right) {
      if (head == null || head.next == null || left == right) {
          return head;
      }

      List<Integer> values = new ArrayList<>();
      ListNode current = head;

      // Store all node values in an ArrayList
      while (current != null) {
          values.add(current.val);
          current = current.next;
      }

      /**
       *  NOTE !!!  below trick
       *
       *  -> we can simply reverse the sub array
       *     via `SWAP` the element at left, right pointers
       */
      // Reverse the segment from index (left-1) to (right-1)
      int startIdx = left - 1;
      int endIdx = right - 1;
      while (startIdx < endIdx) {
          int temp = values.get(startIdx);
          values.set(startIdx, values.get(endIdx));
          values.set(endIdx, temp);
          startIdx++;
          endIdx--;
      }

      // Reconstruct the linked list with modified values
      ListNode dummy = new ListNode(0);
      ListNode newHead = dummy;
      for (int val : values) {
          newHead.next = new ListNode(val);
          newHead = newHead.next;
      }

      return dummy.next;
  }

  // V0-4
  // IDEA: reverse in start, end idx (fixed by gpt)
  public ListNode reverseBetween_0_4(ListNode head, int left, int right) {
      if (head == null || head.next == null || left == right) {
          return head;
      }
      if (right < left) {
          throw new RuntimeException("Invalid left, right values");
      }

      ListNode dummy = new ListNode(0);
      dummy.next = head;
      ListNode prev = dummy;

      // Move `prev` to the node before `left`
      for (int i = 0; i < left - 1; i++) {
          prev = prev.next;
      }

    // Start reversing
    /**
     *   NOTE !!!
     *
     *   - curr: Tracks the current node in the reversal process
     *
     *
     *   - next: Temporarily stores the `next node` `BEFORE` modifying curr.next
     *           . -> for `next pointer` within while loop (reverse op)
     *
     *   - prevRev: Tracks the `previous node` in the `REVERSED` section
     *             . -> so via `prevRev`, we know which `prev` node to point to
     *              within while loop (reverse op)
     */
    ListNode _curr = prev.next;
    ListNode _next = null;
    ListNode _prevRev = null;

      for (int i = left; i <= right; i++) {
          _next = _curr.next;
          _curr.next = _prevRev;
          _prevRev = _curr;
          _curr = _next;
      }

      // Reconnect reversed part with original list
      prev.next.next = _curr;
      prev.next = _prevRev;

      return dummy.next;
  }

  // V1
  // IDEA: LINKED LIST (GPT)
  /**
   *  IDEA:
   *
   *  	1.	Use a dummy node to handle cases where left = 1 (reversal starts at the head).
   * 	2.	Traverse to the node before left (prev).
   * 	3.	Reverse the sublist using the standard in-place reversal technique:
   * 	    •	Maintain a start pointer at left.
   * 	    •	Use then to iterate and reverse the segment between left and right.
   * 	4.	Reconnect the reversed sublist properly.
   *
   *   Time & Space Complexity
   * 	•	Time Complexity: O(N) (traverse once and reverse a portion)
   * 	•	Space Complexity: O(1) (in-place reversal, no extra space)
   */
  public ListNode reverseBetween_1(ListNode head, int left, int right) {
        // Edge case: if the list is empty or has only one node
        if (head == null || head.next == null || left == right) {
            return head;
        }

        // Dummy node to handle edge cases where reversal starts at the head
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;

        // Move `prev` to just before the `left` position
        for (int i = 1; i < left; i++) {
            prev = prev.next;
        }

        // `start` will be the first node of the sublist to reverse
        ListNode start = prev.next;
        ListNode then = start.next;

        // Reverse the sublist
        for (int i = 0; i < right - left; i++) {
            start.next = then.next;
            then.next = prev.next;
            prev.next = then;
            then = start.next;
        }

        return dummy.next; // Return the modified list head
    }

    // V2
    // IDEA: LINKED LIST
    // https://leetcode.com/problems/reverse-linked-list-ii/solutions/30666/simple-java-solution-with-clear-explanat-yd1u/
    public ListNode reverseBetween_2(ListNode head, int m, int n) {
        if (head == null)
            return null;
        ListNode dummy = new ListNode(0); // create a dummy node to mark the head of this list
        dummy.next = head;
        ListNode pre = dummy; // make a pointer pre as a marker for the node before reversing
        for (int i = 0; i < m - 1; i++)
            pre = pre.next;

        ListNode start = pre.next; // a pointer to the beginning of a sub-list that will be reversed
        ListNode then = start.next; // a pointer to a node that will be reversed

        // 1 - 2 -3 - 4 - 5 ; m=2; n =4 ---> pre = 1, start = 2, then = 3
        // dummy-> 1 -> 2 -> 3 -> 4 -> 5

        for (int i = 0; i < n - m; i++) {
            start.next = then.next;
            then.next = pre.next;
            pre.next = then;
            then = start.next;
        }

        // first reversing : dummy->1 - 3 - 2 - 4 - 5; pre = 1, start = 2, then = 4
        // second reversing: dummy->1 - 4 - 3 - 2 - 5; pre = 1, start = 2, then = 5
        // (finish)

        return dummy.next;
    }

    // V3
    // https://leetcode.com/problems/reverse-linked-list-ii/solutions/2311084/javac-tried-to-explain-every-step-by-hi-5w5kl/
    public ListNode reverseBetween_3(ListNode head, int left, int right) {
        ListNode dummy = new ListNode(0); // created dummy node
        dummy.next = head;
        ListNode prev = dummy; // intialising prev pointer on dummy node

        for (int i = 0; i < left - 1; i++)
            prev = prev.next; // adjusting the prev pointer on it's actual index

        ListNode curr = prev.next; // curr pointer will be just after prev
        // reversing
        for (int i = 0; i < right - left; i++) {
            ListNode forw = curr.next; // forw pointer will be after curr
            curr.next = forw.next;
            forw.next = prev.next;
            prev.next = forw;
        }
        return dummy.next;
    }

    // V3
    // https://leetcode.com/problems/reverse-linked-list-ii/solutions/4011862/9240-two-pointers-stack-recursion-by-van-sz5v/

}
