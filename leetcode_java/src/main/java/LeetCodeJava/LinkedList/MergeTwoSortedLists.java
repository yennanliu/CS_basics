package LeetCodeJava.LinkedList;

import LeetCodeJava.DataStructure.ListNode;

// https://leetcode.com/problems/merge-two-sorted-lists/
/**
 *  21. Merge Two Sorted Lists
 * Solved
 * Easy
 * Topics
 * Companies
 * You are given the heads of two sorted linked lists list1 and list2.
 *
 * Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
 *
 * Return the head of the merged linked list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: list1 = [1,2,4], list2 = [1,3,4]
 * Output: [1,1,2,3,4,4]
 * Example 2:
 *
 * Input: list1 = [], list2 = []
 * Output: []
 * Example 3:
 *
 * Input: list1 = [], list2 = [0]
 * Output: [0]
 *
 *
 * Constraints:
 *
 * The number of nodes in both lists is in the range [0, 50].
 * -100 <= Node.val <= 100
 * Both list1 and list2 are sorted in non-decreasing order.
 *
 *
 */
// import java.util.LinkedList;

public class MergeTwoSortedLists {

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

    // V0
    // IDEA: LINKED LIST OP
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {

        // edge case
        if (list1 == null || list2 == null) {
            if (list1 == null) {
                return list2;
            }
            return list1;
        }

        /**
         * NOTE !!!
         *
         *  as usual, in linked list problems,
         *  we define PSEUDO node, since it makes edge cases handling more easily
         *
         *  -> define pseudo node : ListNode node = new ListNode();
         *
         *  NOTE !!!
         *
         *  we also define a REFERENCE node  (ListNode head = node;) that refer pseudo node,
         *  so we can return it as final result
         *
         */
        ListNode node = new ListNode();
        ListNode head = node;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                /**
                 * NOTE !!! need to point node to next list1 node
                 */
                node.next = list1;
                // node = list1; // ???
                list1 = list1.next;
            } else {
                /**
                 * NOTE !!! need to point node to next list2 node
                 */
                node.next = list2;
                // node = list2; // ???
                list2 = list2.next;
            }

            /**
             * NOTE !!! need to move node as well
             */
            node = node.next;
        }

        if (list1 != null) {
            node.next = list1;
        }
        else {
            node.next = list2;
        }

        return head.next; // NOTE !!!
    }

    // V0-0-1
    // IDEA: LINKED LIST OP
    public ListNode mergeTwoLists_0_0_1(ListNode list1, ListNode list2) {
        // edge
        if (list1 == null && list2 == null) {
            return null;
        }
        if (list1 == null || list2 == null) {
            return list1 == null ? list2 : list1;
        }

        ListNode node = new ListNode();
        ListNode res = node;

        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                node.next = new ListNode(list1.val);
                list1 = list1.next;
            } else {
                node.next = new ListNode(list2.val);
                list2 = list2.next;
            }
            node = node.next;
        }

        // check if list1 still has node or list2 still has node
        if (list1 != null) {
            node.next = list1;
        } else {
            node.next = list2;
        }

        return res.next;
    }

    // V0-1
    public ListNode mergeTwoLists_0_1(ListNode list1, ListNode list2) {

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

    // V0-2
    // IDEA : linked list op + iteration
    public ListNode mergeTwoLists_0_2(ListNode list1, ListNode list2) {

        if (list1 == null && list2 == null){
            return null;
        }

        if (list1 == null || list2 == null){
            if (list1 == null){
                return list2;
            }
            return list1;
        }

        /** NOTE here !!!
         *
         * Can't set res as null, but need to set as ListNode(),
         * since null has NO next attr
         */
        ListNode res = new ListNode();
        /** NOTE !!! set root here as final return result
         *
         *  (since res's "cur node" will be changed when iteration run)
         */
        ListNode root = res;

        while (list1 != null && list2 != null){
            if (list1.val < list2.val){
                res.next = list1;
                list1 = list1.next;
            }else{
                res.next = list2;
                list2 = list2.next;
            }
            res = res.next;
        }

        if (list1 != null){
            res.next = list1;
        }else{
            res.next = list2;
        }

        return root.next;
    }

    // V1
    // IDEA : Recursion
    // https://leetcode.com/problems/merge-two-sorted-lists/editorial/
    public ListNode mergeTwoLists_1(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        else if (l2 == null) {
            return l1;
        }
        else if (l1.val < l2.val) {
            l1.next = mergeTwoLists_1(l1.next, l2);
            return l1;
        }
        else {
            l2.next = mergeTwoLists_1(l1, l2.next);
            return l2;
        }

    }

    // V2
    // IDEA : Iteration
    // https://leetcode.com/problems/merge-two-sorted-lists/editorial/
    public ListNode mergeTwoLists_2(ListNode l1, ListNode l2) {
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
