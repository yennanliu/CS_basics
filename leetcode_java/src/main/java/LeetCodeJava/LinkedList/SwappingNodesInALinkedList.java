package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/swapping-nodes-in-a-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  1721. Swapping Nodes in a Linked List
 *  Medium
 *
 *  You are given the head of a linked list, and an integer k.
 *  Return the head of the linked list after swapping the values of the kth node
 *  from the beginning and the kth node from the end (the list is 1-indexed).
 *
 *  Example 1:
 *    Input: head = [1,2,3,4,5], k = 2
 *    Output: [1,4,3,2,5]
 *
 *  Example 2:
 *    Input: head = [7,9,6,6,7,8,3,0,9,5], k = 5
 *    Output: [7,9,6,6,8,7,3,0,9,5]
 *
 *  Constraints:
 *    The number of nodes in the list is n.
 *    1 <= k <= n <= 10^5
 *    0 <= Node.val <= 100
 */
public class SwappingNodesInALinkedList {

    // V0
    // IDEA: TWO POINTERS, FIXED GAP (one pass, no length precomputation)
    //       advance `fast` k-1 steps -> it sits on the kth node from the front,
    //       remember it as p. then walk `fast` and `slow` together until fast hits
    //       the LAST node: fast moved n-k more steps, so slow (started at head)
    //       is at 0-based index n-k = the kth node from the END, remember it as q.
    //       only the VALUES have to swap, so no pointer surgery is needed.
    //       p and q may be the same node (odd length, k = (n+1)/2) -> harmless.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode swapNodes(ListNode head, int k) {
        ListNode fast = head;
        for (int i = 0; i < k - 1; i++) {
            fast = fast.next;
        }
        ListNode p = fast;

        ListNode slow = head;
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        ListNode q = slow;

        int tmp = p.val;
        p.val = q.val;
        q.val = tmp;

        return head;
    }
}
