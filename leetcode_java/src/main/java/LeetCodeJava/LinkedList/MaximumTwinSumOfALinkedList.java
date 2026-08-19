package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/maximum-twin-sum-of-a-linked-list/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2130. Maximum Twin Sum of a Linked List
 *  Medium
 *
 *  In a linked list of size n, where n is even, the ith node (0-indexed) of the
 *  linked list is known as the twin of the (n-1-i)th node, if 0 <= i <= (n / 2) - 1.
 *
 *  For example, if n = 4, then node 0 is the twin of node 3, and node 1 is the twin
 *  of node 2. These are the only nodes with twins for n = 4.
 *
 *  The twin sum is defined as the sum of a node and its twin.
 *  Given the head of a linked list with even length, return the maximum twin sum
 *  of the linked list.
 *
 *  Example 1:
 *    Input: head = [5,4,2,1]
 *    Output: 6
 *    Explanation: Nodes 0 and 1 are the twins of nodes 3 and 2, respectively.
 *                 All have twin sum = 6.
 *
 *  Example 2:
 *    Input: head = [4,2,2,3]
 *    Output: 7
 *
 *  Constraints:
 *    The number of nodes in the list is an even integer in the range [2, 10^5].
 *    1 <= Node.val <= 10^5
 */
public class MaximumTwinSumOfALinkedList {

    // V0
    // IDEA: SLOW/FAST TO THE MIDDLE, REVERSE THE SECOND HALF, WALK IN LOCKSTEP
    //       twins are mirror positions, so after reversing the back half the two
    //       halves line up index by index and one parallel walk yields every twin
    //       sum. the length is guaranteed EVEN, so slow lands exactly at the start
    //       of the second half.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int pairSum(ListNode head) {
        // find the start of the 2nd half
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // reverse the 2nd half
        ListNode prev = null;
        while (slow != null) {
            ListNode nxt = slow.next;
            slow.next = prev;
            prev = slow;
            slow = nxt;
        }

        // walk both halves together
        int res = 0;
        ListNode p = head;
        ListNode q = prev;
        while (q != null) {
            res = Math.max(res, p.val + q.val);
            p = p.next;
            q = q.next;
        }
        return res;
    }
}
