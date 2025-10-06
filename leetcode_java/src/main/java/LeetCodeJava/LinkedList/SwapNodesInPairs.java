package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/swap-nodes-in-pairs/
/**
 *
 * 24. Swap Nodes in Pairs
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a linked list, swap every two adjacent nodes and return its head. You must solve the problem without modifying the values in the list's nodes (i.e., only nodes themselves may be changed.)
 *
 *
 *
 * Example 1:
 *
 * Input: head = [1,2,3,4]
 *
 * Output: [2,1,4,3]
 *
 * Explanation:
 *
 *
 *
 * Example 2:
 *
 * Input: head = []
 *
 * Output: []
 *
 * Example 3:
 *
 * Input: head = [1]
 *
 * Output: [1]
 *
 * Example 4:
 *
 * Input: head = [1,2,3]
 *
 * Output: [2,1,3]
 *
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [0, 100].
 * 0 <= Node.val <= 100
 *
 */
import LeetCodeJava.DataStructure.ListNode;

public class SwapNodesInPairs {


    // V0
    // IDEA : linked list op + swap
    // https://youtu.be/GI1Ghz7Lej0?t=915
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/swap-nodes-in-pairs.py
    public ListNode swapPairs(ListNode head) {

        if (head == null || head.next == null){
            return head;
        }

        // NOTE here!!!
        ListNode dummy  = new ListNode(0);
        dummy.next = head;
        head = dummy;

        // reverse
        while (head.next != null && head.next.next != null){

            ListNode firstNode = head.next;
            ListNode SecondNode = head.next.next;
            ListNode _next = SecondNode.next;

            firstNode.next = _next;
            SecondNode.next = firstNode;
            head.next = SecondNode;

            // for next iteration
            head = firstNode;
        }

        return dummy.next;
    }


    // V1
    // IDEA : Recursive
    // https://leetcode.com/problems/swap-nodes-in-pairs/editorial/
    public ListNode swapPairs_2(ListNode head) {

        // If the list has no node or has only one node left.
        if ((head == null) || (head.next == null)) {
            return head;
        }

        // Nodes to be swapped
        ListNode firstNode = head;
        ListNode secondNode = head.next;

        // Swapping
        firstNode.next  = swapPairs_2(secondNode.next);
        secondNode.next = firstNode;

        // Now the head is the second node
        return secondNode;
    }


    // V2
    // IDEA : Iterative
    // https://leetcode.com/problems/swap-nodes-in-pairs/editorial/
    public ListNode swapPairs_3(ListNode head) {

        // Dummy node acts as the prevNode for the head node
        // of the list and hence stores pointer to the head node.
        ListNode dummy = new ListNode(-1);
        dummy.next = head;

        ListNode prevNode = dummy;

        while ((head != null) && (head.next != null)) {

            // Nodes to be swapped
            ListNode firstNode = head;
            ListNode secondNode = head.next;

            // Swapping
            prevNode.next = secondNode;
            firstNode.next = secondNode.next;
            secondNode.next = firstNode;

            // Reinitializing the head and prevNode for next swap
            prevNode = firstNode;
            head = firstNode.next; // jump
        }

        // Return the new head node.
        return dummy.next;
    }

    

}
