package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/merge-nodes-in-between-zeros/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2181. Merge Nodes in Between Zeros
 *  Medium
 *
 *  You are given the head of a linked list, which contains a series of integers
 *  separated by 0's. The beginning and end of the linked list will have Node.val == 0.
 *
 *  For every two consecutive 0's, merge all the nodes lying in between them into a
 *  single node whose value is the sum of all the merged nodes. The modified list
 *  should not contain any 0's.
 *
 *  Return the head of the modified linked list.
 *
 *  Example 1:
 *    Input: head = [0,3,1,0,4,5,2,0]
 *    Output: [4,11]
 *    Explanation: 3 + 1 = 4, and 4 + 5 + 2 = 11.
 *
 *  Example 2:
 *    Input: head = [0,1,0,3,0,2,2,0]
 *    Output: [1,3,4]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [3, 2 * 10^5].
 *    0 <= Node.val <= 1000
 *    There are no two consecutive nodes with Node.val == 0.
 *    The beginning and end of the linked list have Node.val == 0.
 */
public class MergeNodesInBetweenZeros {

    // V0
    // IDEA: ONE PASS ON THE LINKED LIST (accumulate until the next 0)
    //       the list starts and ends with a 0, so skip the leading 0 and keep a
    //       running sum; every time we hit a 0 the current block is finished ->
    //       append one node holding that sum and reset.
    //       written iteratively (up to 2*10^5 nodes, recursion would overflow).
    /**
     * time = O(N)
     * space = O(1)   // ignoring the output nodes
     */
    public ListNode mergeNodes(ListNode head) {
        ListNode dummy = new ListNode();
        ListNode tail = dummy;

        ListNode cur = head.next; // skip the leading 0
        int total = 0;
        while (cur != null) {
            if (cur.val == 0) {
                tail.next = new ListNode(total);
                tail = tail.next;
                total = 0;
            } else {
                total += cur.val;
            }
            cur = cur.next;
        }
        return dummy.next;
    }
}
