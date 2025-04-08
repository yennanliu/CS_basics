package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-nodes-in-k-group/description/
// https://neetcode.io/problems/reverse-nodes-in-k-group
/**
 * 25. Reverse Nodes in k-Group
 * Solved
 * Hard
 * Topics
 * Companies
 * Given the head of a linked list, reverse the nodes of the list k at a time, and return the modified list.
 *
 * k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of k then left-out nodes, in the end, should remain as it is.
 *
 * You may not alter the values in the list's nodes, only nodes themselves may be changed.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [2,1,4,3,5]
 * Example 2:
 *
 *
 * Input: head = [1,2,3,4,5], k = 3
 * Output: [3,2,1,4,5]
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is n.
 * 1 <= k <= n <= 5000
 * 0 <= Node.val <= 1000
 *
 *
 * Follow-up: Can you solve the problem in O(1) extra memory space?
 *
 */

import LeetCodeJava.DataStructure.ListNode;

public class ReverseNodesInKGroup {

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
//    public ListNode reverseKGroup(ListNode head, int k) {
//
//    }

    // V0-1
    // IDEA : reverse in iteration (fixed by gpt)
    public ListNode reverseKGroup_0_1(ListNode head, int k) {
        // edge case
        if (head == null || k <= 1) {
            return head;
        }

        // get the length of the list
        int len = 0;
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode current = head;

        while (current != null) {
            len += 1;
            current = current.next;
        }

        // reverse groups of k nodes
        ListNode prevGroupEnd = dummy;
        current = head;
        while (len >= k) {
            ListNode groupStart = current;
            ListNode groupEnd = current;
            // Move groupEnd to the k-th node
            for (int i = 1; i < k; i++) {
                groupEnd = groupEnd.next;
            }
            ListNode nextGroupStart = groupEnd.next;

            // Reverse the k nodes
            groupEnd.next = null;
            prevGroupEnd.next = reverseLinkedList(groupStart, k);
            groupStart.next = nextGroupStart;

            // Move to the next group
            prevGroupEnd = groupStart;
            current = nextGroupStart;
            len -= k;
        }

        return dummy.next;
    }

    public ListNode reverseLinkedList(ListNode head, int k) {
        ListNode prev = null;
        ListNode current = head;
        while (k > 0) {
            ListNode nextNode = current.next;
            current.next = prev;
            prev = current;
            current = nextNode;
            k--;
        }
        return prev; // Return the new head of the reversed segment
    }

    // V1-1
    // https://www.youtube.com/watch?v=1UOPsfP85V4
    // https://neetcode.io/problems/reverse-nodes-in-k-group
    // IDEA: RECURSION
    public ListNode reverseKGroup_1_1(ListNode head, int k) {
        ListNode cur = head;
        int group = 0;
        while (cur != null && group < k) {
            cur = cur.next;
            group++;
        }

        if (group == k) {
            cur = reverseKGroup_1_1(cur, k);
            while (group-- > 0) {
                ListNode tmp = head.next;
                head.next = cur;
                cur = head;
                head = tmp;
            }
            head = cur;
        }
        return head;
    }


    // V1-2
    // https://www.youtube.com/watch?v=1UOPsfP85V4
    // https://neetcode.io/problems/reverse-nodes-in-k-group
    // IDEA: Iteration
    public ListNode reverseKGroup_1_2(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode groupPrev = dummy;

        while (true) {
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) {
                break;
            }
            ListNode groupNext = kth.next;

            ListNode prev = kth.next;
            ListNode curr = groupPrev.next;
            while (curr != groupNext) {
                ListNode tmp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = tmp;
            }

            ListNode tmp = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = tmp;
        }
        return dummy.next;
    }

    private ListNode getKth(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }
        return curr;
    }


    // V2
    // IDEA : LINKED LIST OP + RECURSIVE
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0025-reverse-nodes-in-k-group.java
    public ListNode reverseKGroup_2(ListNode head, int k) {
        ListNode cur = head;
        int count = 0;
        while (cur != null && count != k) {
            cur = cur.next;
            count++;
        }
        if (count == k) {
            cur = reverseKGroup_2(cur, k);
            while (count-- > 0) { // TODO : check code meaning (if count-- is bigger than 0)
                ListNode temp = head.next;
                head.next = cur;
                cur = head;
                head = temp;
            }
            head = cur;
        }
        return head;
    }

    // V3
    // IDEA : LINKED LIST OP + ITERATION
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0025-reverse-nodes-in-k-group.java
    public ListNode reverseKGroup_3(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode curr = head;
        ListNode prev = dummy;
        ListNode temp = null;

        int count = k;

        while (curr != null) {
            if (count > 1) {
                temp = prev.next;
                prev.next = curr.next;
                curr.next = curr.next.next;
                prev.next.next = temp;

                count--;
            } else {
                prev = curr;
                curr = curr.next;

                ListNode end = curr;

                for (int i = 0; i < k; ++i) {
                    if (end == null) {
                        return dummy.next;
                    }
                    end = end.next;
                }
                count = k;
            }
        }

        return dummy.next;
    }

}
