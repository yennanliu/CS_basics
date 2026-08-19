package LeetCodeJava.Sort;

// https://leetcode.com/problems/insertion-sort-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  147. Insertion Sort List
 *  Medium
 *
 *  Given the head of a singly linked list, sort the list using insertion sort,
 *  and return the sorted list's head.
 *
 *  The steps of the insertion sort algorithm:
 *   1. Insertion sort iterates, consuming one input element each repetition
 *      and growing a sorted output list.
 *   2. At each iteration, insertion sort removes one element from the input
 *      data, finds the location it belongs within the sorted list and inserts
 *      it there.
 *   3. It repeats until no input elements remain.
 *
 *
 *  Example 1:
 *
 *  Input: head = [4,2,1,3]
 *  Output: [1,2,3,4]
 *
 *  Example 2:
 *
 *  Input: head = [-1,5,3,4,0]
 *  Output: [-1,0,3,4,5]
 *
 *
 *  Constraints:
 *
 *  The number of nodes in the list is in the range [1, 5000].
 *  -5000 <= Node.val <= 5000
 */
public class InsertionSortList {

    // V0
    // IDEA: dummy head + scan the already sorted prefix for the insert spot
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public ListNode insertionSortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode dummy = new ListNode(0);

        while (head != null) {
            ListNode next = head.next;   // keep the rest of the unsorted input

            // find last node of the sorted part whose val < head.val
            ListNode cur = dummy;
            while (cur.next != null && cur.next.val < head.val) {
                cur = cur.next;
            }

            // insert head between cur and cur.next
            head.next = cur.next;
            cur.next = head;

            head = next;
        }
        return dummy.next;
    }
}
