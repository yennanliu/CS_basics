package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/merge-in-between-linked-lists/

import LeetCodeJava.DataStructure.ListNode;

/**
 *  1669. Merge In Between Linked Lists
 *  Medium
 *
 *  You are given two linked lists: list1 and list2 of sizes n and m respectively.
 *  Remove list1's nodes from the ath node to the bth node, and put list2 in their place.
 *  Build the result list and return its head.
 *
 *  Example 1:
 *    Input: list1 = [10,1,13,6,9,5], a = 3, b = 4, list2 = [1000000,1000001,1000002]
 *    Output: [10,1,13,1000000,1000001,1000002,5]
 *    Explanation: We remove the nodes 3 and 4 and put the entire list2 in their place.
 *
 *  Example 2:
 *    Input: list1 = [0,1,2,3,4,5,6], a = 2, b = 5,
 *           list2 = [1000000,1000001,1000002,1000003,1000004]
 *    Output: [0,1,1000000,1000001,1000002,1000003,1000004,6]
 *
 *  Constraints:
 *    3 <= list1.length <= 10^4
 *    1 <= a <= b < list1.length - 1
 *    1 <= list2.length <= 10^4
 */
public class MergeInBetweenLinkedLists {

    // V0
    // IDEA: POINTER SURGERY (find the 2 splice points, then relink)
    //       p = node at index a-1 (last node KEPT before the cut)
    //       q = node at index b   (last node REMOVED)
    //       then p.next = list2 head, and tail(list2).next = q.next
    //       constraints guarantee a >= 1 and b < n-1, so no dummy head is needed
    //       and the returned head is still list1.
    /**
     * time = O(N + M)
     * space = O(1)
     */
    public ListNode mergeInBetween(ListNode list1, int a, int b, ListNode list2) {
        ListNode p = list1;
        for (int i = 0; i < a - 1; i++) {
            p = p.next;
        }

        ListNode q = p;
        for (int i = 0; i < b - a + 1; i++) {
            q = q.next; // q is now the node at index b
        }

        ListNode tail = list2;
        while (tail.next != null) {
            tail = tail.next;
        }

        p.next = list2;
        tail.next = q.next;
        return list1;
    }
}
