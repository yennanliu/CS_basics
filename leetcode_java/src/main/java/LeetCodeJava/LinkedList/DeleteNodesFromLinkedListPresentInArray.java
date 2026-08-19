package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/delete-nodes-from-linked-list-present-in-array/

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 *  3217. Delete Nodes From Linked List Present in Array
 *  Medium
 *
 *  You are given an array of integers nums and the head of a linked list. Return the
 *  head of the modified linked list after removing all nodes from the linked list
 *  that have a value that exists in nums.
 *
 *  Example 1:
 *    Input: nums = [1,2,3], head = [1,2,3,4,5]
 *    Output: [4,5]
 *    Explanation: Remove the nodes with values 1, 2, and 3.
 *
 *  Example 2:
 *    Input: nums = [1], head = [1,2,1,2,1,2]
 *    Output: [2,2,2]
 *    Explanation: Remove the nodes with value 1.
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^5
 *    All elements in nums are unique.
 *    The number of nodes in the given list is in the range [1, 3 * 10^5].
 *    1 <= Node.val <= 10^5
 *    The input is generated such that there is at least one node in the linked list
 *    that has a value not present in nums.
 */
public class DeleteNodesFromLinkedListPresentInArray {

    // V0
    // IDEA: HASH THE VALUES, THEN ONE UNLINKING PASS BEHIND A DUMMY HEAD
    //       turning nums into a set makes each membership test O(1) - re-scanning
    //       the array per node would be 3*10^5 * 10^5 comparisons.
    //       a dummy node in front kills the "what if the HEAD itself is deleted"
    //       special case : `tail` simply links past every doomed node.
    //       NOTE !!! must set tail.next = null at the end, otherwise a trailing
    //                run of deleted nodes stays attached.
    /**
     * time = O(N + M)   // N = list length, M = nums length
     * space = O(M)
     */
    public ListNode modifiedList(int[] nums, ListNode head) {
        Set<Integer> drop = new HashSet<>();
        for (int x : nums) {
            drop.add(x);
        }

        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        ListNode node = head;
        while (node != null) {
            if (!drop.contains(node.val)) {
                tail.next = node;
                tail = node;
            }
            node = node.next;
        }
        tail.next = null;   // NOTE !!!

        return dummy.next;
    }
}
