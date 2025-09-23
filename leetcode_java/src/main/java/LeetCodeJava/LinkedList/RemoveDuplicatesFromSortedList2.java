package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/description/
/**
 * 82. Remove Duplicates from Sorted List II
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list. Return the linked list sorted as well.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,3,4,4,5]
 * Output: [1,2,5]
 * Example 2:
 *
 *
 * Input: head = [1,1,1,2,3]
 * Output: [2,3]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 300].
 * -100 <= Node.val <= 100
 * The list is guaranteed to be sorted in ascending order.
 */

import LeetCodeJava.DataStructure.ListNode;

public class RemoveDuplicatesFromSortedList2 {

    // V0
//    public ListNode deleteDuplicates(ListNode head) {
//
//    }

    // V1
    // https://leetcode.ca/2016-02-20-82-Remove-Duplicates-from-Sorted-List-II/
    public ListNode deleteDuplicates_1(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur != null) {
            while (cur.next != null && cur.next.val == cur.val) {
                cur = cur.next;
            }
            if (pre.next == cur) {
                pre = cur;
            } else {
                pre.next = cur.next;
            }
            cur = cur.next;
        }
        return dummy.next;
    }

    //  V2
    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/solutions/6801800/beats-100-easiest-explanation-for-beginn-6noz/
    public ListNode deleteDuplicates_2(ListNode head) {
        if (head == null || head.next == null)
            return head;

        ListNode dummy = new ListNode(-1); // Dummy node to handle head removals
        dummy.next = head;
        ListNode prev = dummy;
        ListNode cur = head;

        while (cur != null && cur.next != null) {
            if (cur.val == cur.next.val) {
                // Skip all nodes with the same value
                while (cur.next != null && cur.val == cur.next.val) {
                    cur = cur.next;
                }
                prev.next = cur.next; // Remove duplicates
            } else {
                prev = prev.next; // Move to next distinct node
            }
            cur = cur.next;
        }

        return dummy.next;
    }

    // V3
    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/solutions/7002012/simple-solution-by-harshita_114-2gqn/
    public ListNode deleteDuplicates_3(ListNode head) {
        if (head == null || head.next == null)
            return head;
        ListNode prev = new ListNode(-1);
        ListNode dummy = prev;
        dummy.next = head;
        ListNode curr = head;
        while (curr != null && curr.next != null) {
            if (curr.val == curr.next.val) {
                while (curr.next != null && curr.val == curr.next.val) {
                    curr = curr.next;
                }
                dummy.next = curr.next;
            } else {
                dummy = dummy.next;
            }
            curr = curr.next;
        }
        return prev.next;
    }

    // V4
    // https://leetcode.com/problems/remove-duplicates-from-sorted-list-ii/solutions/7192741/remove-duplicates-from-sorted-list-ii-on-4dmz/
    public ListNode deleteDuplicates_4(ListNode head) {
        if (head == null)
            return head;
        ListNode temp = new ListNode(0, head);
        ListNode prev = temp;
        ListNode curr = head;

        while (curr != null && curr.next != null) {
            boolean isDuplicate = false;

            while (curr.next != null && curr.val == curr.next.val) {
                curr = curr.next;
                isDuplicate = true;
            }

            if (isDuplicate)
                prev.next = curr.next;
            else
                prev = prev.next;

            curr = curr.next;
        }
        return temp.next;
    }



}
