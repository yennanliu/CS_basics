package LeetCodeJava.Stack;

// https://leetcode.com/problems/verify-preorder-sequence-in-binary-search-tree/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  255. Verify Preorder Sequence in Binary Search Tree
 *  Medium
 *
 *  Given an array of unique integers preorder, return true if it is the correct preorder
 *  traversal sequence of a binary search tree.
 *
 *  Example 1:
 *  Input: preorder = [5,2,1,3,6]
 *  Output: true
 *
 *  Example 2:
 *  Input: preorder = [5,2,6,1,3]
 *  Output: false
 *
 *  Constraints:
 *  1 <= preorder.length <= 10^4
 *  1 <= preorder[i] <= 10^4
 *  All the elements of preorder are unique.
 *
 *  Follow up: Could you do it using only O(1) space?
 */
public class VerifyPreorderSequenceInBinarySearchTree {

    // V0
    // IDEA: MONOTONIC STACK — popping the stack means we moved into a right subtree,
    //       so the last popped value becomes a hard lower bound for everything after it
    /**
     * time = O(n)
     * space = O(n)
     */
    public boolean verifyPreorder(int[] preorder) {
        Deque<Integer> stack = new ArrayDeque<>();
        long lowerBound = Long.MIN_VALUE;
        for (int p : preorder) {
            // p sits in some right subtree but is smaller than that subtree's root -> invalid
            if (p < lowerBound) {
                return false;
            }
            while (!stack.isEmpty() && p > stack.peek()) {
                lowerBound = stack.pop();
            }
            stack.push(p);
        }
        return true;
    }

    // V1
    // IDEA: same monotonic stack, but stored IN-PLACE inside preorder -> O(1) extra space
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean verifyPreorder_1(int[] preorder) {
        long lowerBound = Long.MIN_VALUE;
        int top = -1; // index of the stack top inside preorder
        for (int p : preorder) {
            if (p < lowerBound) {
                return false;
            }
            while (top >= 0 && p > preorder[top]) {
                lowerBound = preorder[top];
                top--;
            }
            top++;
            preorder[top] = p;
        }
        return true;
    }
}
