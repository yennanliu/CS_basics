package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/linked-list-frequency/

import java.util.HashMap;
import java.util.Map;

import LeetCodeJava.DataStructure.ListNode;

/**
 *  3063. Linked List Frequency
 *  Medium
 *  (premium / locked)
 *
 *  Given the head of a linked list containing k distinct elements, return the head
 *  to a linked list of length k containing the frequency of each distinct element
 *  in the given linked list in any order.
 *
 *  Example 1:
 *    Input: head = [1,1,2,1,2,3]
 *    Output: [3,2,1]
 *    Explanation: There are 3 distinct elements in the list. The frequency of 1 is 3,
 *                 the frequency of 2 is 2 and the frequency of 3 is 1.
 *                 Hence, we return 3 -> 2 -> 1.
 *                 Note that 1 -> 2 -> 3, 2 -> 1 -> 3, 3 -> 2 -> 1, and 3 -> 1 -> 2
 *                 are also valid answers.
 *
 *  Example 2:
 *    Input: head = [1,1,2,2,2]
 *    Output: [2,3]
 *
 *  Constraints:
 *    The number of nodes in the list is in the range [1, 10^5].
 *    1 <= Node.val <= 10^5
 */
public class LinkedListFrequency {

    // V0
    // IDEA: COUNT INTO A HASH MAP, THEN REBUILD A LIST FROM THE COUNTS
    //       the output order is free, so nothing has to be preserved from the
    //       input: one pass to tally the values, then one pass over the tallies
    //       building fresh nodes. a dummy head keeps the append loop free of
    //       "is this the first node" special-casing.
    /**
     * time = O(N)
     * space = O(K)   // K = number of distinct values
     */
    public ListNode frequenciesOfElements(ListNode head) {
        Map<Integer, Integer> cnt = new HashMap<>();
        ListNode node = head;
        while (node != null) {
            Integer c = cnt.get(node.val);
            cnt.put(node.val, c == null ? 1 : c + 1);
            node = node.next;
        }

        ListNode dummy = new ListNode();
        ListNode tail = dummy;
        for (Integer freq : cnt.values()) {
            tail.next = new ListNode(freq);
            tail = tail.next;
        }
        return dummy.next;
    }
}
