package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/linked-list-cycle/
/**
 *  141. Linked List Cycle
 * Solved
 * Easy
 * Topics
 * Companies
 * Given head, the head of a linked list, determine if the linked list has a cycle in it.
 *
 * There is a cycle in a linked list if there is some node in the list that can be reached again by continuously following the next pointer. Internally, pos is used to denote the index of the node that tail's next pointer is connected to. Note that pos is not passed as a parameter.
 *
 * Return true if there is a cycle in the linked list. Otherwise, return false.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [3,2,0,-4], pos = 1
 * Output: true
 * Explanation: There is a cycle in the linked list, where the tail connects to the 1st node (0-indexed).
 * Example 2:
 *
 *
 * Input: head = [1,2], pos = 0
 * Output: true
 * Explanation: There is a cycle in the linked list, where the tail connects to the 0th node.
 * Example 3:
 *
 *
 * Input: head = [1], pos = -1
 * Output: false
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
 */
import LeetCodeJava.DataStructure.ListNode;

import java.util.HashSet;
import java.util.Set;

public class LinkedListCycle {

    // V0
    // IDEA: HASHSET
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasCycle(ListNode head) {
        // edge
        if(head == null || head.next == null){
            return false;
        }

        /**
         * hashset needs to save ListNode, INSTEAD of its value
         * since we need to check if the node already visited
         * via checking its `object instance` in hashset
         *
         * e.g.
         *
         *  -> use HashSet<ListNode> set
         *  (but NOT HashSet<Integer>
         *
         */
        // NOTE !!! HashSet<ListNode> set
        HashSet<ListNode> set = new HashSet<>();
        while(head != null){
            if(set.contains(head)){
                return true;
            }
            set.add(head);
            head = head.next;
        }

        return false;
    }

    // V0-1
    // IDEA : HASHSET
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasCycle_0_1(ListNode head) {

        if (head == null){
            return false;
        }

        HashSet<ListNode> set = new HashSet<>();

        while (head != null){
            ListNode cur = head;
            if (set.contains(cur)){
                return true;
            }
            set.add(cur);
            ListNode _next = head.next;
            head = _next;
        }

        return false;
    }

    // V0-2
    // IDEA : Hash table
    // https://leetcode.com/problems/linked-list-cycle/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasCycle_0_2(ListNode head) {
        Set<ListNode> nodesSeen = new HashSet<>();
        while (head != null) {
            if (nodesSeen.contains(head)) {
                return true;
            }
            nodesSeen.add(head);
            head = head.next;
        }
        return false;
    }

    // V0-3
    // IDEA : 2 POINTERS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Two_Pointers/linked-list-cycle.py#L49
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasCycle_0_3(ListNode head) {

        if (head == null || head.next == null || head.next.next == null){
            return false;
        }

        /**
         *  NOTE !!!
         *
         *   we move fast, slow pointer first,
         *   then compare if they are the same
         *   (if same, means cyclic linkedlist)
         *
         *
         *   (explanation from gpt)
         *
         *   Impact on Detection:
         *
         *      - Before the Check: This method ensures that fast and slow pointers are always moved before checking for equality. This approach works well for detecting cycles because it ensures that both pointers have progressed further before comparison.
         *
         *      - After the Check: This method checks for equality at the very start of the loop iteration, meaning it checks if they are at the same position before they are moved. This approach is generally less common because it might prematurely check pointers that have not yet advanced enough to potentially meet.
         *
         */
        ListNode fast = head;
        ListNode slow = head;
        while (fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow){
                return true;
            }
            /**
             *  NOTE !!!
             *
             *   below is WRONG,
             *   we should move pointers first,
             *   then compare them
             *
             */
            // fast = fast.next.next;
            // slow = slow.next;
        }

        return false;
    }

    // V1
    // IDEA :  Floyd's Cycle Finding Algorithm (fast, slow pointer)
    // https://leetcode.com/problems/linked-list-cycle/editorial/
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean hasCycle_1(ListNode head) {
        if (head == null) {
            return false;
        }

        ListNode slow = head;
        ListNode fast = head.next;
        while (slow != fast) {
            if (fast == null || fast.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return true;
    }

}
