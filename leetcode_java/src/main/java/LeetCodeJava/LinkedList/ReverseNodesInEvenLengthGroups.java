package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/reverse-nodes-in-even-length-groups/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  2074. Reverse Nodes in Even Length Groups
 *  Medium
 *
 *  You are given the head of a linked list.
 *  The nodes in the linked list are sequentially assigned to non-empty groups whose
 *  lengths form the sequence of the natural numbers (1, 2, 3, 4, ...):
 *    the 1st node is assigned to the first group,
 *    the 2nd and the 3rd nodes are assigned to the second group,
 *    the 4th, 5th, and 6th nodes are assigned to the third group, and so on.
 *  Note that the length of the last group may be less than or equal to
 *  1 + the length of the second to last group.
 *
 *  Reverse the nodes in each group with an even length, and return the head of the
 *  modified linked list.
 *
 *  Example 1:
 *    Input: head = [5,2,6,3,9,1,7,3,8,4]
 *    Output: [5,6,2,3,9,1,4,8,3,7]
 *    Explanation: group lengths are 1 (odd, kept), 2 (even, reversed),
 *                 3 (odd, kept), 4 (even, reversed).
 *
 *  Example 2:
 *    Input: head = [1,1,0,6]
 *    Output: [1,0,1,6]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [1, 10^5].
 *    0 <= Node.val <= 10^5
 */
public class ReverseNodesInEvenLengthGroups {

    // V0
    // IDEA: WALK GROUP BY GROUP, REVERSE IN PLACE WHEN THE ACTUAL LENGTH IS EVEN
    //       `prev` always points at the node just before the current group.
    //       for group size k, first COUNT how many nodes are actually there (the
    //       tail group may be short) - that real count, not k, decides odd/even.
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode reverseEvenLengthGroups(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy;

        int k = 1;
        while (prev.next != null) {
            // how many nodes does this group REALLY have?
            int cnt = 0;
            ListNode probe = prev.next;
            while (probe != null && cnt < k) {
                cnt++;
                probe = probe.next;
            }

            if (cnt % 2 == 0) {
                // reverse the `cnt` nodes right after prev
                ListNode groupHead = prev.next; // becomes the group's new tail
                ListNode cur = groupHead;
                ListNode rev = null;
                for (int i = 0; i < cnt; i++) {
                    ListNode nxt = cur.next;
                    cur.next = rev;
                    rev = cur;
                    cur = nxt;
                }
                prev.next = rev;
                groupHead.next = cur;
                prev = groupHead;
            } else {
                for (int i = 0; i < cnt; i++) {
                    prev = prev.next;
                }
            }
            k++;
        }
        return dummy.next;
    }
}
