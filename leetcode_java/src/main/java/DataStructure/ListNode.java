package DataStructure;

/**
 *  LIST NODE -- one link of a singly linked list
 *
 *  The building block behind every linked-list problem on LeetCode:
 *
 *      head
 *       |
 *       v
 *      [1] -> [2] -> [3] -> null
 *
 *  Fields are PUBLIC on purpose. Getters would only get in the way here:
 *  linked-list algorithms are written as direct pointer surgery
 *  (`prev.next = curr.next`), and that is how LeetCode's own signatures
 *  present them.
 *
 *  THREE CONSTRUCTORS, matching LeetCode's template exactly:
 *      ListNode()                  an empty node, val defaults to 0
 *      ListNode(val)               a tail node -- next is null
 *      ListNode(val, next)         a node linked to an existing chain,
 *                                  which is what makes building a list
 *                                  back-to-front a one-liner
 *
 *  A note on the DUMMY HEAD idiom, which these constructors exist to
 *  support: allocating `ListNode dummy = new ListNode(0, head)` removes
 *  the special case for "the node being removed IS the head", because
 *  every real node then has a predecessor.
 *
 *  Time  : field access O(1)
 *  Space : O(1) per node
 *
 *  See data_structure/python/linkedList.py and
 *      data_structure/js/linkedlist.js for full list implementations.
 */
public class ListNode {

    /** The value carried by this node. */
    public int val;

    /** The next node, or null at the end of the list. */
    public ListNode next;

    /** Empty node: val is 0 and next is null. */
    public ListNode() {
    }

    /** Tail node: carries a value, points at nothing. */
    public ListNode(int val) {
        this.val = val;
    }

    /** Node linked to an existing chain -- build a list back-to-front. */
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    /** "1 -> 2 -> 3", for debugging. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ListNode node = this; node != null; node = node.next) {
            sb.append(node.val);
            if (node.next != null) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        ListNode empty = new ListNode();
        assertThat(empty.val == 0 && empty.next == null, "the no-arg constructor gives 0 -> null");

        ListNode tail = new ListNode(3);
        assertThat(tail.val == 3 && tail.next == null, "a tail node points at nothing");

        // building back-to-front, which the two-arg constructor makes trivial
        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3)));
        assertThat(head.toString().equals("1 -> 2 -> 3"), "a three-node list");
        assertThat(head.next.next.val == 3, "walking the chain");

        // the dummy-head idiom: removing the head needs no special case
        ListNode dummy = new ListNode(0, head);
        dummy.next = dummy.next.next;              // drop the old head
        assertThat(dummy.next.toString().equals("2 -> 3"), "head removed via the dummy");

        System.out.println(new ListNode(1, new ListNode(2, new ListNode(3))));
        System.out.println("Success.");
    }

    private static void assertThat(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
