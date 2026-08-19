package LeetCodeJava.Stack;

// https://leetcode.com/problems/is-array-a-preorder-of-some-binary-tree/

import java.util.List;

/**
 *  2764. Is Array a Preorder of Some Binary Tree
 *  Medium
 *
 *  Given a 0-indexed integer 2D array nodes, your task is to determine if the
 *  given array represents the preorder traversal of some binary tree.
 *
 *  For each index i, nodes[i] = [id, parentId], where id is the id of the node
 *  at index i and parentId is the id of its parent in the tree (if the node has
 *  no parent, then parentId == -1).
 *
 *  Return true if the given array represents the preorder traversal of some
 *  tree, and false otherwise.
 *
 *  Example 1:
 *    Input: nodes = [[0,-1],[1,0],[2,0],[3,2],[4,2]]
 *    Output: true
 *    Explanation: visit 0, then the subtree [1], then the subtree [2,3,4].
 *
 *  Example 2:
 *    Input: nodes = [[0,-1],[1,0],[2,0],[3,1],[4,1]]
 *    Output: false
 *    Explanation: 2 comes between 1 and 3, so 1's subtree is not contiguous.
 *
 *  Constraints:
 *    1 <= nodes.length <= 10^5
 *    nodes[i].length == 2
 *    0 <= nodes[i][0] <= 10^5
 *    -1 <= nodes[i][1] <= 10^5
 *    The input is generated such that nodes make a binary tree.
 */
public class IsArrayAPreorderOfSomeBinaryTree {

    // V0
    // IDEA: MONOTONIC "ANCESTOR PATH" STACK
    //       Left/right is not fixed for us - a node's two children may be
    //       handed to the traversal in either order. So the question is only
    //       whether the array is SOME depth-first order, which has a clean
    //       characterisation: when a node is emitted, its parent must still be
    //       on the active root-to-current path.
    //       Keep that path in a stack. For each (id, par) in array order:
    //         - pop until the stack top is `par` (every popped node's subtree
    //           is finished, exactly what a DFS does on the way back up),
    //         - if the stack drains without exposing `par`, some node was
    //           emitted outside its parent's window -> not a preorder,
    //         - otherwise push `id`.
    //       The root (par == -1) is unique, so the only node ever allowed to
    //       face an empty stack is nodes[0].
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean isPreorder(List<List<Integer>> nodes) {
        int n = nodes.size();
        int[] path = new int[n]; // current root -> node path
        int top = -1;

        for (int i = 0; i < n; i++) {
            int nid = nodes.get(i).get(0);
            int par = nodes.get(i).get(1);
            while (top >= 0 && path[top] != par) {
                top--;
            }
            if (top < 0 && par != -1) {
                return false;
            }
            path[++top] = nid;
        }
        return true;
    }

    // same logic, int[][] input flavour
    public boolean isPreorder(int[][] nodes) {
        int n = nodes.length;
        int[] path = new int[n];
        int top = -1;

        for (int i = 0; i < n; i++) {
            int nid = nodes[i][0];
            int par = nodes[i][1];
            while (top >= 0 && path[top] != par) {
                top--;
            }
            if (top < 0 && par != -1) {
                return false;
            }
            path[++top] = nid;
        }
        return true;
    }
}
