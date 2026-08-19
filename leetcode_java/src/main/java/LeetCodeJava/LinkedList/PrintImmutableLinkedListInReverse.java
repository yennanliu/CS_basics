package LeetCodeJava.LinkedList;

// https://leetcode.com/problems/print-immutable-linked-list-in-reverse/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  1265. Print Immutable Linked List in Reverse
 *  Medium
 *
 *  You are given an immutable linked list, print out all values of each node in
 *  reverse with the help of the following interface:
 *
 *  ImmutableListNode: An interface of immutable linked list, you are given the head
 *  of the list.
 *
 *  You need to use the following functions to access the linked list
 *  (you can't access the ImmutableListNode directly):
 *    ImmutableListNode.printValue(): Print value of the current node.
 *    ImmutableListNode.getNext(): Return the next node.
 *
 *  The input is only given to initialize the linked list internally. You must solve
 *  this problem without modifying the linked list. In other words, you must operate
 *  the linked list using only the mentioned APIs.
 *
 *  Example 1:
 *    Input: head = [1,2,3,4]
 *    Output: [4,3,2,1]
 *
 *  Example 2:
 *    Input: head = [0,-4,-1,3,-5]
 *    Output: [-5,3,-1,-4,0]
 *
 *  Constraints:
 *    The length of the linked list is between [1, 1000].
 *    The value of each node in the linked list is between [-1000, 1000].
 */
public class PrintImmutableLinkedListInReverse {

    /** the API given by LeetCode (declared here so this file compiles standalone) */
    public interface ImmutableListNode {
        void printValue();
        ImmutableListNode getNext();
    }

    // V0
    // IDEA: RECURSION
    //       go all the way to the tail first, print on the way back.
    //       (length <= 1000, so the recursion depth is safe)
    /**
     * time = O(N)
     * space = O(N)   // recursion stack
     */
    public void printLinkedListInReverse(ImmutableListNode head) {
        if (head == null) {
            return;
        }
        printLinkedListInReverse(head.getNext());
        head.printValue();
    }

    // V1
    // IDEA: EXPLICIT STACK (iterative, no recursion depth risk)
    /**
     * time = O(N)
     * space = O(N)
     */
    public void printLinkedListInReverse_1(ImmutableListNode head) {
        Deque<ImmutableListNode> stack = new ArrayDeque<>();
        ImmutableListNode cur = head;
        while (cur != null) {
            stack.push(cur);
            cur = cur.getNext();
        }
        while (!stack.isEmpty()) {
            stack.pop().printValue();
        }
    }
}
