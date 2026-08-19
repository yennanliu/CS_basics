package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/convert-doubly-linked-list-to-array-i/

/**
 *  3263. Convert Doubly Linked List to Array I
 *  Easy
 *  (premium)
 *
 *  You are given the head of a doubly linked list, which contains nodes that have
 *  a next pointer and a previous pointer.
 *
 *  Return an integer array which contains the elements of the linked list in order.
 *
 *  Example 1:
 *    Input: head = [1,2,3,4,3,2,1]
 *    Output: [1,2,3,4,3,2,1]
 *
 *  Example 2:
 *    Input: head = [2,2,2,2,2]
 *    Output: [2,2,2,2,2]
 *
 *  Constraints:
 *    The number of nodes in the given list is in the range [1, 50].
 *    1 <= Node.val <= 50
 */
public class ConvertDoublyLinkedListToArrayI {

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
    // IDEA: WALK FORWARD FROM THE HEAD - THE prev POINTERS ARE NOT NEEDED
    //       the traversal already starts at the head, so the doubly linked
    //       structure adds nothing here. (the sequel LC 3294 hands over an
    //       ARBITRARY node, and that is where prev earns its keep.)
    //       one pass to count the nodes so we can size the int[] exactly,
    //       a second pass to fill it.
    /**
     * time = O(N)
     * space = O(N)   // the output array
     */
    public int[] toArray(Node head) {
        int n = 0;
        for (Node node = head; node != null; node = node.next) {
            n++;
        }

        int[] res = new int[n];
        int i = 0;
        for (Node node = head; node != null; node = node.next) {
            res[i++] = node.val;
        }
        return res;
    }
}
