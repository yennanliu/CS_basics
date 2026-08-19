package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/split-linked-list-in-parts/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  725. Split Linked List in Parts
 *  Medium
 *
 *  Given the head of a singly linked list and an integer k, split the linked list
 *  into k consecutive linked list parts.
 *
 *  The length of each part should be as equal as possible: no two parts should have
 *  a size differing by more than one. This may lead to some parts being null.
 *
 *  The parts should be in the order of occurrence in the input list, and parts
 *  occurring earlier should always have a size greater than or equal to parts
 *  occurring later.
 *
 *  Return an array of the k parts.
 *
 *  Example 1:
 *    Input: head = [1,2,3], k = 5
 *    Output: [[1],[2],[3],[],[]]
 *
 *  Example 2:
 *    Input: head = [1,2,3,4,5,6,7,8,9,10], k = 3
 *    Output: [[1,2,3,4],[5,6,7],[8,9,10]]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [0, 1000].
 *    0 <= Node.val <= 1000
 *    1 <= k <= 50
 */
public class SplitLinkedListInParts {

    // V0
    // IDEA: count length, first (len % k) parts get one extra node, then cut in place
    /**
     * time = O(n + k)
     * space = O(k)   (output array)
     */
    public ListNode[] splitListToParts(ListNode head, int k) {

        int len = 0;
        ListNode cur = head;
        while (cur != null) {
            len++;
            cur = cur.next;
        }

        int base = len / k;
        int extra = len % k;

        ListNode[] res = new ListNode[k];
        cur = head;
        for (int i = 0; i < k && cur != null; i++) {
            res[i] = cur;
            int size = base + (i < extra ? 1 : 0);
            // move to the last node of this part
            for (int j = 1; j < size; j++) {
                cur = cur.next;
            }
            ListNode next = cur.next;
            cur.next = null;   // cut
            cur = next;
        }

        return res;
    }
}
