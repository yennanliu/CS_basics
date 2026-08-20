package LeetCodeJava.Design;

// https://leetcode.com/problems/binary-search-tree-iterator-ii/

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1586. Binary Search Tree Iterator II
 *  Medium
 *
 *  Implement the BSTIterator class that represents an iterator over the in-order
 *  traversal of a binary search tree (BST):
 *
 *   - BSTIterator(TreeNode root) Initializes an object of the BSTIterator class.
 *     The root of the BST is given as part of the constructor. The pointer should
 *     be initialized to a non-existent number smaller than any element in the BST.
 *   - boolean hasNext() Returns true if there exists a number in the traversal to
 *     the right of the pointer, otherwise returns false.
 *   - int next() Moves the pointer to the right, then returns the number at the pointer.
 *   - boolean hasPrev() Returns true if there exists a number in the traversal to
 *     the left of the pointer, otherwise returns false.
 *   - int prev() Moves the pointer to the left, then returns the number at the pointer.
 *
 *  Notice that by initializing the pointer to a non-existent smallest number, the
 *  first call to next() will return the smallest element in the BST.
 *
 *  You may assume that next() and prev() calls will always be valid.
 *
 *  Example 1:
 *    Input
 *      ["BSTIterator","next","next","prev","next","hasNext","next","next","next",
 *       "hasNext","hasPrev","prev","prev"]
 *      [[[7,3,15,null,null,9,20]],[],[],[],[],[],[],[],[],[],[],[],[]]
 *    Output
 *      [null,3,7,3,7,true,9,15,20,false,true,15,9]
 *    Explanation
 *      the in-order traversal is [3, 7, 9, 15, 20]; the pointer starts before 3.
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^5].
 *    0 <= Node.val <= 10^6
 *    At most 10^5 calls will be made to hasNext, next, hasPrev, and prev.
 *
 *  Follow up: Could you solve the problem without precalculating the values of the tree?
 */
public class BinarySearchTreeIteratorII {

    // V0
    // IDEA: FLATTEN THE BST (in-order -> sorted array) + A CURSOR
    //       an in-order walk of a BST yields the values in ascending order, so the
    //       iterator is nothing but an index into that array. the pointer starts at
    //       -1 ("before the first element"):
    //         next()    -> ++i then vals[i]      prev()    -> --i then vals[i]
    //         hasNext() -> i + 1 < size          hasPrev() -> i > 0
    /**
     * time = O(N) to build, O(1) per call
     * space = O(N)
     */
    private final List<Integer> vals = new ArrayList<>();
    private int i = -1;

    public BinarySearchTreeIteratorII(TreeNode root) {
        Deque<TreeNode> stack = new ArrayDeque<>();
        TreeNode node = root;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
            node = stack.pop();
            vals.add(node.val);
            node = node.right;
        }
    }

    public boolean hasNext() {
        return this.i + 1 < vals.size();
    }

    public int next() {
        this.i++;
        return vals.get(this.i);
    }

    public boolean hasPrev() {
        return this.i > 0;
    }

    public int prev() {
        this.i--;
        return vals.get(this.i);
    }
}
