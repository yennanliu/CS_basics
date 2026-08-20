package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/remove-duplicates-from-an-unsorted-linked-list/
// https://leetcode.ca/all/1836.html

import java.util.HashMap;
import java.util.Map;

/**
 * 1836. Remove Duplicates From an Unsorted Linked List
 * Given the head of a linked list, find all the values that appear more than once in the list and delete the nodes that have any of those values.
 *
 * Return the linked list after the deletions.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,2]
 * Output: [1,3]
 * Explanation: 2 appears twice in the linked list, so all 2's should be deleted. After deleting all 2's, we are left with [1,3].
 * Example 2:
 *
 *
 * Input: head = [2,1,1,2]
 * Output: []
 * Explanation: 2 and 1 both appear twice. All the elements should be deleted.
 * Example 3:
 *
 *
 * Input: head = [3,2,2,1,3,2,4]
 * Output: [1,4]
 * Explanation: 3 appears twice and 2 appears three times. After deleting all 3's and 2's, we are left with [1,4].
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [1, 105]
 * 1 <= Node.val <= 105
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 *
 */
public class RemoveDuplicatesFromAnUnsortedLinkedList {

    // NOTE !!! below is our custom listNode for this problem
    public class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }

    // V0
    // IDEA: HASHMAP (COUNT VAL) + DUMMY NODE
    /**
     *  Step 1) 1st pass: count how many times each val shows up
     *  Step 2) 2nd pass: with a `dummy` node in front, unlink EVERY node
     *          whose val count > 1 (NOT only the 2nd, 3rd .. occurrence)
     *
     *  time = O(N)
     *  space = O(N)
     */
    public ListNode deleteDuplicatesUnsorted(ListNode head) {

        // edge
        if (head == null || head.next == null) {
            return head;
        }

        // step 1) count val
        Map<Integer, Integer> cnt = new HashMap<>();
        ListNode cur = head;
        while (cur != null) {
            cnt.put(cur.val, cnt.getOrDefault(cur.val, 0) + 1);
            cur = cur.next;
        }

        /**
         *  NOTE !!!
         *
         *  we use a `dummy` node, so the `head` itself can be deleted as well
         */
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        // step 2) remove all nodes with cnt > 1
        ListNode prev = dummy;
        while (prev.next != null) {
            if (cnt.get(prev.next.val) > 1) {
                // NOTE !!! we ONLY move `prev.next` pointer, prev stays still
                prev.next = prev.next.next;
            } else {
                prev = prev.next;
            }
        }

        return dummy.next;
    }

    // V0-1
    // IDEA: COUNT MAP + `BUILD A NEW LIST` (instead of unlinking in place)
    /**
     * time = O(N)
     * space = O(N)
     */
    public ListNode deleteDuplicatesUnsorted_0_1(ListNode head) {
        // Step 1: Count frequencies of each value
        Map<Integer, Integer> freq = new HashMap<>();
        ListNode curr = head;
        while (curr != null) {
            freq.put(curr.val, freq.getOrDefault(curr.val, 0) + 1);
            curr = curr.next;
        }

        // Step 2: Build a new list with only values that appear once
        ListNode dummy = new ListNode(0); // dummy head for result
        ListNode tail = dummy;

        curr = head;
        while (curr != null) {
            if (freq.get(curr.val) == 1) {
                tail.next = new ListNode(curr.val);
                tail = tail.next;
            }
            curr = curr.next;
        }

        return dummy.next;
    }

    // V1

    // V2-1

    // https://leetcode.ca/2021-06-27-1836-Remove-Duplicates-From-an-Unsorted-Linked-List/
    /**
     * time = O(N)
     * space = O(N)
     */
    public ListNode deleteDuplicatesUnsorted_2_1(ListNode head) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        ListNode temp = head;
        while (temp != null) {
            int val = temp.val;
            map.put(val, map.getOrDefault(val, 0) + 1);
            temp = temp.next;
        }
        ListNode dummyHead = new ListNode(0);
        dummyHead.next = head;
        temp = dummyHead;
        while (temp.next != null) {
            ListNode next = temp.next;
            int nextVal = next.val;
            if (map.get(nextVal) > 1)
                temp.next = next.next;
            else
                temp = temp.next;
        }
        return dummyHead.next;
    }

    // V2-2

    // https://leetcode.ca/2021-06-27-1836-Remove-Duplicates-From-an-Unsorted-Linked-List/
    /**
     * time = O(N)
     * space = O(N)
     */
    public ListNode deleteDuplicatesUnsorted_2_2(ListNode head) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (ListNode cur = head; cur != null; cur = cur.next) {
            cnt.put(cur.val, cnt.getOrDefault(cur.val, 0) + 1);
        }
        ListNode dummy = new ListNode(0, head);
        for (ListNode pre = dummy, cur = head; cur != null; cur = cur.next) {
            if (cnt.get(cur.val) > 1) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
        }
        return dummy.next;
    }


}
