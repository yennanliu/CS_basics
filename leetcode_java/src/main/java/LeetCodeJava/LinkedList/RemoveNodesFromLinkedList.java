package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-nodes-from-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2487. Remove Nodes From Linked List
 *  Medium
 *
 *  You are given the head of a linked list.
 *  Remove every node which has a node with a greater value anywhere to the right
 *  side of it.
 *  Return the head of the modified linked list.
 *
 *  Example 1:
 *    Input: head = [5,2,13,3,8]
 *    Output: [13,8]
 *    Explanation: The nodes that should be removed are 5, 2 and 3.
 *                 Node 13 is to the right of node 5.
 *                 Node 13 is to the right of node 2.
 *                 Node 8 is to the right of node 3.
 *
 *  Example 2:
 *    Input: head = [1,1,1,1]
 *    Output: [1,1,1,1]
 *    Explanation: Every node has value 1, so no nodes are removed.
 *
 *  Constraints:
 *    The number of the nodes in the given list is in the range [1, 10^5].
 *    1 <= Node.val <= 10^5
 */
public class RemoveNodesFromLinkedList {

    // V0
    // IDEA: REVERSE, KEEP A RUNNING MAXIMUM, REVERSE BACK
    //       "is there anything bigger to my RIGHT?" is awkward while walking
    //       forward. reversing turns it into "anything bigger to my LEFT?", which
    //       one running maximum settles in a single pass: keep a node iff its
    //       value is >= the max seen so far. the survivors come out reversed, so
    //       reverse once more at the end.
    //       equivalently a monotonic-stack solution, but in O(1) extra space by
    //       reusing the list's own pointers.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode removeNodes(ListNode head) {
        ListNode rev = reverse(head);

        int maxSoFar = Integer.MIN_VALUE;
        ListNode dummy = new ListNode();
        ListNode tail = dummy;
        ListNode cur = rev;
        while (cur != null) {
            ListNode nxt = cur.next;
            if (cur.val >= maxSoFar) {
                maxSoFar = cur.val;
                tail.next = cur;
                tail = cur;
                tail.next = null;
            }
            cur = nxt;
        }
        return reverse(dummy.next);
    }

    private ListNode reverse(ListNode node) {
        ListNode prev = null;
        while (node != null) {
            ListNode nxt = node.next;
            node.next = prev;
            prev = node;
            node = nxt;
        }
        return prev;
    }
}
