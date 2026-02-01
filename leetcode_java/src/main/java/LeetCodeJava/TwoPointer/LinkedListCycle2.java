package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/linked-list-cycle-ii/description/

import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.List;

/**
 * 42. Linked List Cycle II
 * Solved
 * Medium
 * Topics
 * Companies
 * Given the head of a linked list, return the node where the cycle begins. If there is no cycle, return null.
 *
 * There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer. Internally, pos is used to denote the index of the node that tail's next pointer is connected to (0-indexed). It is -1 if there is no cycle. Note that pos is not passed as a parameter.
 *
 * Do not modify the linked list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [3,2,0,-4], pos = 1
 * Output: tail connects to node index 1
 * Explanation: There is a cycle in the linked list, where tail connects to the second node.
 * Example 2:
 *
 *
 * Input: head = [1,2], pos = 0
 * Output: tail connects to node index 0
 * Explanation: There is a cycle in the linked list, where tail connects to the first node.
 * Example 3:
 *
 *
 * Input: head = [1], pos = -1
 * Output: no cycle
 * Explanation: There is no cycle in the linked list.
 *
 *
 * Constraints:
 *
 * The number of the nodes in the list is in the range [0, 104].
 * -105 <= Node.val <= 105
 * pos is -1 or a valid index in the linked-list.
 *
 *
 * Follow up: Can you solve it using O(1) (i.e. constant) memory?
 *
 *
 */
public class LinkedListCycle2 {

    /**
     * Definition for singly-linked list.
     * class ListNode {
     *     int val;
     *     ListNode next;
     *     ListNode(int x) {
     *         val = x;
     *         next = null;
     *     }
     * }
     */

    // V0
    // IDEA: LIST NODE + HASHSET
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode detectCycle(ListNode head) {
        // edge
        if (head == null || head.next == null) {
            return null;
        }
        HashSet<ListNode> set = new HashSet<>();
        while (head != null) {
            if (set.contains(head)) {
                return head;
            }
            set.add(head);
            head = head.next;
        }

        return null;
    }

    // V0-1
    // IDEA: LIS TNODE + HASHSET
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode detectCycle_0_1(ListNode head) {
        // edge
        if (head == null || head.next == null) {
            return null;
        }
        // check if there is a `cycle`
        ListNode head2 = head;
        HashSet<ListNode> set = new HashSet<>();
        boolean foundCycle = false;
        /**
         *  NOTE !!!
         *
         *   when a circular node is found, assign it `circuleNode`
         *   and return as answer
         */
        ListNode circuleNode = null;

        while (head2 != null) {
            if (set.contains(head2)) {
                // there is a cycle
                circuleNode = head2;
                foundCycle = true;
                break;
            }
            set.add(head2);
            head2 = head2.next;
        }
        if (!foundCycle) {
            return null;
        }
        return circuleNode;
    }

    // V0-2
    // IDEA: SLOW - FAST POINTER
    /**
     *  KEY:
     *
     *  step 1) setup slow, fast pointer
     *  step 2) move till fast == null or fast.next == null
     *  step 3) check if NO cycle
     *  step 4) move slow, fast on the same time, return the 1st node
     *          when slow == fast (which is the `cycle start node`)
     *
     */
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode detectCycle_0_2(ListNode head) {
        if (head == null || head.next == null)
            return null;

        ListNode slow = head, fast = head;

        // Step 1: detect if cycle exists
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) { // cycle detected
                break;
            }
        }

        // No cycle
        if (fast == null || fast.next == null)
            return null;

        // Step 2: find cycle entry
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow; // start of cycle
    }

    // V1
    // https://youtu.be/eW5pVXX2dow?si=_AGwcJCcelesPkcM

    // V2
    // https://leetcode.com/problems/linked-list-cycle-ii/solutions/1701128/cjavapython-slow-and-fast-image-explanat-uxjx/
    // IDEA:  fast, slow pointers
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode detectCycle_2(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast)
                break;
        }
        if (fast == null || fast.next == null)
            return null;
        while (head != slow) {
            head = head.next;
            slow = slow.next;
        }
        return head;
    }

    // V3
    // https://leetcode.com/problems/linked-list-cycle-ii/solutions/3274329/clean-codes-full-explanation-floyds-cycl-mugo/
    /**
     * time = O(N)
     * space = O(1)
     */
    public ListNode detectCycle_3(ListNode head) {
        // Initialize two pointers, slow and fast, to the head of the linked list.
        ListNode slow = head;
        ListNode fast = head;

        // Move the slow pointer one step and the fast pointer two steps at a time through the linked list,
        // until they either meet or the fast pointer reaches the end of the list.
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                // If the pointers meet, there is a cycle in the linked list.
                // Reset the slow pointer to the head of the linked list, and move both pointers one step at a time
                // until they meet again. The node where they meet is the starting point of the cycle.
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }

        // If the fast pointer reaches the end of the list without meeting the slow pointer,
        // there is no cycle in the linked list. Return null.
        return null;
    }

}
