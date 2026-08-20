package LeetCodeJava.DFS;

// https://leetcode.com/problems/sum-of-nodes-with-even-valued-grandparent/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  1315. Sum of Nodes with Even-Valued Grandparent
 *  Medium
 *
 *  Given the root of a binary tree, return the sum of values of nodes with an
 *  even-valued grandparent. If there are no nodes with an even-valued
 *  grandparent, return 0.
 *
 *  A grandparent of a node is the parent of its parent if it exists.
 *
 *  Example 1:
 *    Input: root = [6,7,8,2,7,1,3,9,null,1,4,null,null,null,5]
 *    Output: 18
 *
 *  Example 2:
 *    Input: root = [1]
 *    Output: 0
 *
 *  Constraints:
 *    The number of nodes in the tree is in the range [1, 10^4].
 *    1 <= Node.val <= 100
 */
public class SumOfNodesWithEvenValuedGrandparent {

    // V0
    // IDEA: DFS CARRYING (parent value, grandparent value)
    //       a node is counted when its grandparent value is even. -1 is used as
    //       the sentinel for a missing parent / grandparent: it is odd, so the
    //       root and its children are never counted.
    /**
     * time = O(N)
     * space = O(H)   // H = tree height (recursion depth)
     */
    public int sumEvenGrandparent(TreeNode root) {
        return dfs(root, -1, -1);
    }

    private int dfs(TreeNode node, int parentVal, int grandParentVal) {
        if (node == null) {
            return 0;
        }
        int cur = (grandParentVal % 2 == 0) ? node.val : 0;
        return cur
                + dfs(node.left, node.val, parentVal)
                + dfs(node.right, node.val, parentVal);
    }
}
