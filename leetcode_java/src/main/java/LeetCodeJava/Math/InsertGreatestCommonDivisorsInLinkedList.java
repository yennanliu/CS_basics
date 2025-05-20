package LeetCodeJava.Math;

// https://leetcode.com/problems/insert-greatest-common-divisors-in-linked-list/description/

import LeetCodeJava.DataStructure.ListNode;

/**
 * 2807. Insert Greatest Common Divisors in Linked List
 * Medium
 * Topics
 * Companies
 * Given the head of a linked list head, in which each node contains an integer value.
 *
 * Between every pair of adjacent nodes, insert a new node with a value equal to the greatest common divisor of them.
 *
 * Return the linked list after insertion.
 *
 * The greatest common divisor of two numbers is the largest positive integer that evenly divides both numbers.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [18,6,10,3]
 * Output: [18,6,6,2,10,1,3]
 * Explanation: The 1st diagram denotes the initial linked list and the 2nd diagram denotes the linked list after inserting the new nodes (nodes in blue are the inserted nodes).
 * - We insert the greatest common divisor of 18 and 6 = 6 between the 1st and the 2nd nodes.
 * - We insert the greatest common divisor of 6 and 10 = 2 between the 2nd and the 3rd nodes.
 * - We insert the greatest common divisor of 10 and 3 = 1 between the 3rd and the 4th nodes.
 * There are no more adjacent nodes, so we return the linked list.
 * Example 2:
 *
 *
 * Input: head = [7]
 * Output: [7]
 * Explanation: The 1st diagram denotes the initial linked list and the 2nd diagram denotes the linked list after inserting the new nodes.
 * There are no pairs of adjacent nodes, so we return the initial linked list.
 *
 *
 * Constraints:
 *
 * The number of nodes in the list is in the range [1, 5000].
 * 1 <= Node.val <= 1000
 *
 */
public class InsertGreatestCommonDivisorsInLinkedList {

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
//    public ListNode insertGreatestCommonDivisors(ListNode head) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=SS_IlBrocYQ
    // https://neetcode.io/solutions/insert-greatest-common-divisors-in-linked-list
    public ListNode insertGreatestCommonDivisors_1(ListNode head) {
        if (head == null) return null;

        ListNode cur = head;

        while (cur.next != null) {
            int n1 = cur.val, n2 = cur.next.val;
            int gcdValue = gcd(n1, n2);
            ListNode newNode = new ListNode(gcdValue, cur.next);
            cur.next = newNode;
            cur = newNode.next;
        }

        return head;
    }

    private int gcd(int a, int b) {
        while (b > 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // V2
    // https://leetcode.com/problems/insert-greatest-common-divisors-in-linked-list/description/
    public ListNode insertGreatestCommonDivisors_2(ListNode head) {
        // If the list contains only one node, return the head as no insertion is needed
        if (head.next == null)
            return head;

        // Initialize pointers to traverse the list
        ListNode node1 = head;
        ListNode node2 = head.next;

        // Traverse the linked list
        while (node2 != null) {
            int gcdValue = calculateGCD(node1.val, node2.val);
            ListNode gcdNode = new ListNode(gcdValue);

            // Insert the GCD node between node1 and node2
            node1.next = gcdNode;
            gcdNode.next = node2;

            // Move to the next pair of nodes
            node1 = node2;
            node2 = node2.next;
        }

        return head;
    }

    // Helper method to calculate the greatest common divisor using the Euclidean algorithm
    private int calculateGCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

}
