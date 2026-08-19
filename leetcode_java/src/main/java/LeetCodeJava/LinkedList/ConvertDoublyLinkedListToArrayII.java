package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/convert-doubly-linked-list-to-array-ii/

/**
 *  3294. Convert Doubly Linked List to Array II
 *  Medium
 *  (premium)
 *
 *  You are given an arbitrary node from a doubly linked list, which contains nodes
 *  that have a next pointer and a previous pointer.
 *
 *  Return an integer array which contains the elements of the linked list in order.
 *
 *  Example 1:
 *    Input: head = [1,2,3,4,5], node = 5
 *    Output: [1,2,3,4,5]
 *
 *  Example 2:
 *    Input: head = [4,5,6,7,8], node = 8
 *    Output: [4,5,6,7,8]
 *
 *  Constraints:
 *    The number of nodes in the given list is in the range [1, 500].
 *    1 <= Node.val <= 1000
 *    All nodes have unique Node.val.
 */
public class ConvertDoublyLinkedListToArrayII {

    // Definition for a doubly linked list Node.
    public static class Node {
        public int val;
        public Node prev;
        public Node next;

        public Node() {}

        public Node(int val) {
            this.val = val;
        }

        public Node(int val, Node prev, Node next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }

    // V0
    // IDEA: WALK BACK TO THE HEAD FIRST - THAT IS WHAT prev IS FOR
    //       unlike LC 3263 the given node can sit ANYWHERE in the list, so the
    //       traversal has 2 phases:
    //         1) follow prev until it runs out -> that node is the real head
    //         2) follow next from there, collecting the values
    //       each node is touched at most twice -> still linear.
    /**
     * time = O(N)
     * space = O(N)   // the output array
     */
    public int[] toArray(Node node) {
        // phase 1 : rewind to the head
        Node head = node;
        while (head.prev != null) {
            head = head.prev;
        }

        // phase 2 : count, then collect
        int n = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            n++;
        }

        int[] res = new int[n];
        int i = 0;
        for (Node cur = head; cur != null; cur = cur.next) {
            res[i++] = cur.val;
        }
        return res;
    }
}
