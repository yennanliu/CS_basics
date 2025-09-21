package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/middle-of-the-linked-list/
/**
 * 876. Middle of the Linked List
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the head of a singly linked list, return the middle node of the linked list.
 *
 * If there are two middle nodes, return the second middle node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5]
 * Output: [3,4,5]
 * Explanation: The middle node of the list is node 3.
 * Example 2:
 *
 *
 * Input: head = [1,2,3,4,5,6]
 * Output: [4,5,6]
 * Explanation: Since the list has two middle nodes with values 3 and 4, we return the second one.
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [1, 100].
 * 1 <= Node.val <= 100
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 *
 *
 */
import LeetCodeJava.DataStructure.ListNode;

public class MiddleOfTheLinkedList {

    // V0
    // IDEA: SLOW - FAST POINTER
    public ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // V0-1
    // IDEA: SLOW - FAST POINTER
    public ListNode middleNode_0_1(ListNode head) {
        // edge
        if (head == null || head.next == null) {
            return head;
        }

        // fast, slow pointers
        ListNode fast = head;
        ListNode slow = head;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        return slow;
    }

    // V0-2
    // IDEA : 2 POINTERS
    public ListNode middleNode_0_2(ListNode head) {

        if (head == null) {
            return null;
        }

        ListNode _head = head;

        // fast, slow pointers
        while (head != null && _head != null) {

            if (_head.next == null){
                return head;
            }

            _head = _head.next.next;
            head = head.next;
        }

        return head;
    }

    // V1
    // IDEA : OUTPUT TO ARRAY
    // https://leetcode.com/problems/middle-of-the-linked-list/editorial/
    public ListNode middleNode_2(ListNode head) {
        ListNode[] A = new ListNode[100];
        int t = 0;
        while (head != null) {
            A[t++] = head;
            head = head.next;
        }
        return A[t / 2];
    }

    // V2
    // IDEA : 2 POINTERS
    // https://leetcode.com/problems/middle-of-the-linked-list/editorial/
    public ListNode middleNode_3(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

}
