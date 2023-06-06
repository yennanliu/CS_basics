package LeetCodeJava.LinkedList;

import LeetCodeJava.DataStructure.ListNode;

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

// https://leetcode.com/problems/merge-two-sorted-lists/

//import java.util.LinkedList;

public class MergeTwoSortedLists {

    // V0
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        if (list1 == null && list2 == null){
            return null;
        }

        if (list1 == null){
            return list2;
        }

        if (list2 == null){
            return list1;
        }

        // NOTE here!!! we have head ref node, as returned solution
        ListNode node = new ListNode(-1);
        ListNode head = node;

        while ( list1 != null && list2 != null){
            if (list1.val <= list2.val){
                node.next = list1;
                // NOTE here!!! we move pointer to next node
                list1 = list1.next;
            }else{
                node.next = list2;
                // NOTE here!!! we move pointer to next node
                list2 = list2.next;
            }
            node = node.next;
        }

        if (list1 != null){
            node.next = list1;
        }else{
            node.next = list2;
        }
        return head.next;
    }

    // V1
    // IDEA : Recursion
    // https://leetcode.com/problems/merge-two-sorted-lists/editorial/
    public ListNode mergeTwoLists_2(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        else if (l2 == null) {
            return l1;
        }
        else if (l1.val < l2.val) {
            l1.next = mergeTwoLists(l1.next, l2);
            return l1;
        }
        else {
            l2.next = mergeTwoLists(l1, l2.next);
            return l2;
        }

    }

    // V2
    // IDEA : Iteration
    // https://leetcode.com/problems/merge-two-sorted-lists/editorial/
    public ListNode mergeTwoLists_3(ListNode l1, ListNode l2) {
        // maintain an unchanging reference to node ahead of the return node.
        ListNode prehead = new ListNode(-1);

        ListNode prev = prehead;
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }

        // At least one of l1 and l2 can still have nodes at this point, so connect
        // the non-null list to the end of the merged list.
        prev.next = l1 == null ? l2 : l1;

        return prehead.next;
    }

}
