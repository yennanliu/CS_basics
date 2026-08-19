package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  148. Sort List
 *  Medium
 *
 *  Given the head of a linked list, return the list after sorting it in
 *  ascending order.
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
 *  Example 3:
 *
 *  Input: head = []
 *  Output: []
 *
 *
 *  Constraints:
 *
 *  The number of nodes in the list is in the range [0, 5 * 10^4].
 *  -10^5 <= Node.val <= 10^5
 *
 *  Follow up: can you sort the linked list in O(n log n) time and O(1) memory
 *  (i.e. constant space)?
 */
public class SortList {

    // V0
    // IDEA: TOP DOWN MERGE SORT (split by slow/fast pointer, merge two halves)
    /**
     * time = O(n log n)
     * space = O(log n)   (recursion stack)
     */
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        // split into 2 halves; `prev` ends at the tail of the 1st half
        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prev.next = null;

        ListNode l1 = sortList(head);
        ListNode l2 = sortList(slow);
        return merge(l1, l2);
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        cur.next = (l1 != null) ? l1 : l2;
        return dummy.next;
    }

    // V1
    // IDEA: BOTTOM UP MERGE SORT -> O(1) extra space (the follow up)
    /**
     * time = O(n log n)
     * space = O(1)
     */
    public ListNode sortList_1(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        int n = 0;
        for (ListNode p = head; p != null; p = p.next) {
            n++;
        }

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        for (int step = 1; step < n; step <<= 1) {
            ListNode prev = dummy;
            ListNode cur = dummy.next;
            while (cur != null) {
                ListNode left = cur;
                ListNode right = split(left, step);
                cur = split(right, step);       // rest of the list
                prev = mergeInto(prev, left, right);
            }
        }
        return dummy.next;
    }

    // cut `step` nodes off the front of head, return the head of the remainder
    private ListNode split(ListNode head, int step) {
        if (head == null) {
            return null;
        }
        for (int i = 1; i < step && head.next != null; i++) {
            head = head.next;
        }
        ListNode rest = head.next;
        head.next = null;
        return rest;
    }

    // merge l1 & l2 after `prev`, return the new tail
    private ListNode mergeInto(ListNode prev, ListNode l1, ListNode l2) {
        ListNode cur = prev;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        cur.next = (l1 != null) ? l1 : l2;
        while (cur.next != null) {
            cur = cur.next;
        }
        return cur;
    }
}
