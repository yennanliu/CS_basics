package LeetCodeJava.Stack;

// https://leetcode.com/problems/next-greater-node-in-linked-list/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import LeetCodeJava.DataStructure.ListNode;

/**
 *  1019. Next Greater Node In Linked List
 *  Medium
 *
 *  You are given the head of a linked list with n nodes.
 *
 *  For each node in the list, find the value of the next greater node. That is,
 *  for each node, find the value of the first node that is next to it and has a
 *  strictly larger value than it.
 *
 *  Return an integer array answer where answer[i] is the value of the next
 *  greater node of the ith node (1-indexed). If the ith node does not have a
 *  next greater node, set answer[i] = 0.
 *
 *  Example 1:
 *    Input: head = [2,1,5]
 *    Output: [5,5,0]
 *
 *  Example 2:
 *    Input: head = [2,7,4,3,5]
 *    Output: [7,0,5,5,0]
 *
 *  Constraints:
 *    1 <= n <= 10^4
 *    1 <= Node.val <= 10^9
 */
public class NextGreaterNodeInLinkedList {

    // V0
    // IDEA: MONOTONIC (DECREASING) STACK
    //       1) dump the list into an array (so we can index it)
    //       2) walk left to right keeping a stack of INDICES whose answer is
    //          still unknown, with strictly decreasing values. when the current
    //          value is bigger than the value at the stack top, that top's
    //          "next greater" is the current value -> pop and fill it in.
    //       whatever is left on the stack at the end has no next greater -> 0.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] nextLargerNodes(ListNode head) {
        List<Integer> vals = new ArrayList<>();
        ListNode node = head;
        while (node != null) {
            vals.add(node.val);
            node = node.next;
        }

        int n = vals.size();
        int[] res = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // indices, values decreasing
        for (int i = 0; i < n; i++) {
            int v = vals.get(i);
            while (!stack.isEmpty() && vals.get(stack.peek()) < v) {
                res[stack.pop()] = v;
            }
            stack.push(i);
        }
        return res;
    }
}
