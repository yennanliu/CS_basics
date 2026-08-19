package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/delete-n-nodes-after-m-nodes-of-a-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  1474. Delete N Nodes After M Nodes of a Linked List
 *  Easy
 *
 *  You are given the head of a linked list and two integers m and n.
 *
 *  Traverse the linked list and remove some nodes in the following way:
 *   - Start with the head as the current node.
 *   - Keep the first m nodes starting with the current node.
 *   - Remove the next n nodes.
 *   - Keep repeating steps 2 and 3 until you reach the end of the list.
 *
 *  Return the head of the modified list after removing the mentioned nodes.
 *
 *  Example 1:
 *    Input: head = [1,2,3,4,5,6,7,8,9,10,11,12,13], m = 2, n = 3
 *    Output: [1,2,6,7,11,12]
 *    Explanation: Keep the first (m = 2) nodes starting from the head (1 -> 2).
 *                 Delete the next (n = 3) nodes (3 -> 4 -> 5). Continue with the
 *                 same procedure until reaching the tail of the linked list.
 *
 *  Example 2:
 *    Input: head = [1,2,3,4,5,6,7,8,9,10,11], m = 1, n = 3
 *    Output: [1,5,9]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [1, 10^4].
 *    1 <= Node.val <= 10^6
 *    1 <= m, n <= 1000
 *
 *  Follow up: Could you solve this problem by modifying the list in-place?
 */
public class DeleteNNodesAfterMNodesOfALinkedList {

    // V0
    // IDEA: SIMULATION (in-place pointer surgery)
    //       per block:
    //         1) walk (m - 1) steps to land `pre` on the LAST KEPT node
    //         2) walk n more steps to land `cur` on the LAST DELETED node
    //            (if the list runs out earlier, cur just stops at the tail)
    //         3) re-link pre.next = cur.next, then jump `pre` to the start of
    //            the next "keep" block
    //       NOTE !!! no dummy head needed : m >= 1 so the head is always kept.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode deleteNodes(ListNode head, int m, int n) {
        ListNode pre = head;
        while (pre != null) {

            // keep m nodes -> move (m - 1) steps, pre = last kept node
            for (int i = 0; i < m - 1; i++) {
                if (pre == null) {
                    break;
                }
                pre = pre.next;
            }
            if (pre == null) {
                break;
            }

            // delete the next n nodes -> cur = last node that gets deleted
            ListNode cur = pre;
            for (int i = 0; i < n; i++) {
                if (cur.next == null) {
                    break;
                }
                cur = cur.next;
            }

            // NOTE !!! re-link, then jump to the head of the next "keep" block
            pre.next = cur.next;
            pre = pre.next;
        }

        return head;
    }
}
