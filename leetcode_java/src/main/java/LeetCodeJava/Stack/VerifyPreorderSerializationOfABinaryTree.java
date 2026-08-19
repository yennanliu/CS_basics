package LeetCodeJava.Stack;

// https://leetcode.com/problems/verify-preorder-serialization-of-a-binary-tree/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  331. Verify Preorder Serialization of a Binary Tree
 *  Medium
 *
 *  One way to serialize a binary tree is to use preorder traversal. When we encounter a
 *  non-null node, we record the node's value. If it is a null node, we record '#'.
 *
 *  For example, the tree above could be serialized to "9,3,4,#,#,1,#,#,2,#,6,#,#".
 *
 *  Given a string of comma-separated values preorder, return true if it is a correct
 *  preorder traversal serialization of a binary tree.
 *
 *  It is guaranteed that each comma-separated value is either an integer or '#'.
 *  You may assume that the input format is always valid (e.g. never two consecutive commas).
 *
 *  Note: You are not allowed to reconstruct the tree.
 *
 *  Example 1:
 *  Input: preorder = "9,3,4,#,#,1,#,#,2,#,6,#,#"
 *  Output: true
 *
 *  Example 2:
 *  Input: preorder = "1,#"
 *  Output: false
 *
 *  Example 3:
 *  Input: preorder = "9,#,#,1"
 *  Output: false
 *
 *  Constraints:
 *  1 <= preorder.length <= 10^4
 *  preorder consists of integers in the range [0, 100] and '#' separated by commas.
 */
public class VerifyPreorderSerializationOfABinaryTree {

    // V0
    // IDEA: SLOT COUNTING — the tree starts with 1 open slot; every node consumes one slot,
    //       a non-null node then opens 2 new ones. Valid iff slots never go negative and end at 0
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean isValidSerialization(String preorder) {
        if (preorder == null || preorder.isEmpty()) {
            return false;
        }
        int slots = 1;
        String[] nodes = preorder.split(",");
        for (String node : nodes) {
            slots--;              // this node takes one slot
            if (slots < 0) {
                return false;     // no slot left, but a node showed up
            }
            if (!"#".equals(node)) {
                slots += 2;       // a real node opens 2 child slots
            }
        }
        return slots == 0;
    }

    // V1
    // IDEA: STACK — collapse "x,#,#" into "#" repeatedly; a valid tree collapses to a single "#"
    /**
     * time = O(n)
     * space = O(n)
     */
    public boolean isValidSerialization_1(String preorder) {
        if (preorder == null || preorder.isEmpty()) {
            return false;
        }
        Deque<String> stack = new ArrayDeque<>();
        for (String node : preorder.split(",")) {
            stack.push(node);
            while (stack.size() >= 3) {
                String first = stack.pop();
                String second = stack.pop();
                String third = stack.peek();
                if ("#".equals(first) && "#".equals(second) && !"#".equals(third)) {
                    stack.pop();      // drop the parent
                    stack.push("#");  // it becomes a leaf-collapsed "#"
                } else {
                    // restore and stop
                    stack.push(second);
                    stack.push(first);
                    break;
                }
            }
        }
        return stack.size() == 1 && "#".equals(stack.peek());
    }
}
