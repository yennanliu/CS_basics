package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/find-a-corresponding-node-of-a-binary-tree-in-a-clone-of-that-tree/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1379. Find a Corresponding Node of a Binary Tree in a Clone of That Tree
 *  Easy
 *
 *  Given two binary trees original and cloned and given a reference to a node
 *  target in the original tree.
 *
 *  The cloned tree is a copy of the original tree.
 *
 *  Return a reference to the same node in the cloned tree.
 *  Note that you are not allowed to change any of the two trees or the target
 *  node and the answer must be a reference to a node in the cloned tree.
 *
 *  Example 1:
 *    Input: tree = [7,4,3,null,null,6,19], target = 3
 *    Output: 3
 *
 *  Example 2:
 *    Input: tree = [7], target = 7
 *    Output: 7
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^4].
 *    The values of the nodes of the tree are unique.
 *    target node is a node from the original tree and is not null.
 *
 *  Follow up: Could you solve the problem if repeated values on the tree are
 *  allowed?
 */
public class FindACorrespondingNodeOfABinaryTreeInACloneOfThatTree {

    // V0
    // IDEA: WALK BOTH TREES IN LOCK STEP (DFS)
    //       the two trees have the exact same shape, so whenever we stand on
    //       `target` in `original`, the node we stand on in `cloned` is the
    //       answer.
    //       NOTE !!! we compare by IDENTITY (==), not by value -> this also
    //                handles the follow up (duplicated values allowed)
    /**
     * time = O(n)
     * space = O(h), h = tree height (recursion stack)
     */
    public final TreeNode getTargetCopy(final TreeNode original, final TreeNode cloned, final TreeNode target) {
        if (original == null) {
            return null;
        }
        if (original == target) {
            return cloned;
        }
        TreeNode left = getTargetCopy(original.left, cloned.left, target);
        if (left != null) {
            return left;
        }
        return getTargetCopy(original.right, cloned.right, target);
    }
}
