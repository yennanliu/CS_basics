package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/

import java.util.HashMap;
import java.util.Map;

import LeetCodeJava.DataStructure.ListNode;

/**
 *  1171. Remove Zero Sum Consecutive Nodes from Linked List
 *  Medium
 *
 *  Given the head of a linked list, we repeatedly delete consecutive sequences of
 *  nodes that sum to 0 until there are no such sequences.
 *  After doing so, return the head of the final linked list. You may return any
 *  such answer.
 *
 *  Example 1:
 *    Input: head = [1,2,-3,3,1]
 *    Output: [3,1]
 *    Note: The answer [1,2,1] would also be accepted.
 *
 *  Example 2:
 *    Input: head = [1,2,3,-3,4]
 *    Output: [1,2,4]
 *
 *  Example 3:
 *    Input: head = [1,2,3,-3,-2]
 *    Output: [1]
 *
 *  Constraints:
 *    The given linked list will contain between 1 and 1000 nodes.
 *    Each node in the linked list has -1000 <= node.val <= 1000.
 */
public class RemoveZeroSumConsecutiveNodesFromLinkedList {

    // V0
    // IDEA: PREFIX SUM + HASH TABLE (2 passes)
    //       if 2 nodes share the same running prefix sum, everything strictly
    //       between them sums to 0 -> cut it out.
    //       pass 1: record the LAST node owning each prefix sum
    //       pass 2: walk again and jump straight to last[s].next
    /**
     * time = O(N)
     * space = O(N)
     */
    public ListNode removeZeroSumSublists(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        Map<Integer, ListNode> last = new HashMap<>();
        int s = 0;
        for (ListNode node = dummy; node != null; node = node.next) {
            s += node.val;
            last.put(s, node); // keeps the LAST node with this prefix sum
        }

        s = 0;
        for (ListNode node = dummy; node != null; node = node.next) {
            s += node.val;
            node.next = last.get(s).next; // skip the zero-sum block (if any)
        }
        return dummy.next;
    }
}
