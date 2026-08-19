package LeetCodeJava.Math;

// https://leetcode.com/problems/linked-list-random-node/

import java.util.Random;

import LeetCodeJava.DataStructure.ListNode;

/**
 *  382. Linked List Random Node
 *  Medium
 *
 *  Given a singly linked list, return a random node's value from the linked list.
 *  Each node must have the same probability of being chosen.
 *
 *  Implement the Solution class:
 *   - Solution(ListNode head) Initializes the object with the head of the singly-linked list.
 *   - int getRandom() Chooses a node randomly from the list and returns its value.
 *     All the nodes of the list should be equally likely to be chosen.
 *
 *  Example 1:
 *
 *  Input
 *  ["Solution", "getRandom", "getRandom", "getRandom"]
 *  [[[1, 2, 3]], [], [], []]
 *  Output
 *  [null, 1, 3, 2]
 *
 *  Constraints:
 *
 *  The number of nodes in the linked list will be in the range [1, 10^4].
 *  -10^4 <= Node.val <= 10^4
 *  At most 10^4 calls will be made to getRandom.
 */
public class LinkedListRandomNode {

    private final ListNode head;
    private final Random rand;

    // V0
    // IDEA: reservoir sampling (size 1) - the i-th node (1-indexed) replaces the
    //       current pick with probability 1/i, so every node ends up equally likely
    /**
     * time = O(1) for the constructor, O(n) per getRandom
     * space = O(1)
     */
    public LinkedListRandomNode(ListNode head) {
        this.head = head;
        this.rand = new Random();
    }

    /**
     * time = O(n)
     * space = O(1)
     */
    public int getRandom() {
        int result = 0;
        int count = 0;
        ListNode cur = this.head;
        while (cur != null) {
            count++;
            // pick current node with probability 1/count
            if (this.rand.nextInt(count) == 0) {
                result = cur.val;
            }
            cur = cur.next;
        }
        return result;
    }
}
