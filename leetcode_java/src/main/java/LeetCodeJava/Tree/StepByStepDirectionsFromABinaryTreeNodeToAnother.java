package LeetCodeJava.Tree;

// https://leetcode.com/problems/step-by-step-directions-from-a-binary-tree-node-to-another/

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2096. Step-By-Step Directions From a Binary Tree Node to Another
 *  Medium
 *
 *  You are given the root of a binary tree with n nodes. Each node is uniquely
 *  assigned a value from 1 to n. You are also given an integer startValue
 *  representing the value of the start node s, and a different integer
 *  destValue representing the value of the destination node t.
 *
 *  Find the shortest path starting from node s and ending at node t. Generate
 *  step-by-step directions of such path as a string consisting of only the
 *  uppercase letters 'L', 'R', and 'U'. Each letter indicates a direction:
 *    'L' means to go from a node to its left child node.
 *    'R' means to go from a node to its right child node.
 *    'U' means to go from a node to its parent node.
 *
 *  Return the step-by-step directions of the shortest path from node s to t.
 *
 *  Example 1:
 *    Input: root = [5,1,2,3,null,6,4], startValue = 3, destValue = 6
 *    Output: "UURL"
 *    Explanation: The shortest path is: 3 -> 1 -> 5 -> 2 -> 6.
 *
 *  Example 2:
 *    Input: root = [2,1], startValue = 2, destValue = 1
 *    Output: "L"
 *
 *  Constraints:
 *    The number of nodes in the tree is n.
 *    2 <= n <= 10^5
 *    1 <= Node.val <= n
 *    All the values in the tree are unique.
 *    1 <= startValue, destValue <= n
 *    startValue != destValue
 */
public class StepByStepDirectionsFromABinaryTreeNodeToAnother {

    // V0
    // IDEA: ROOT-TO-NODE PATHS + STRIP THE COMMON PREFIX (= the LCA path)
    //       build the 'L'/'R' path from the root down to s and down to t.
    //       their common prefix is exactly the path down to their LCA.
    //       after dropping that prefix:
    //         - every remaining step of the s-path becomes a 'U' (climb to LCA)
    //         - the remaining t-path is appended unchanged (walk back down)
    //       the DFS builds the path into a StringBuilder and pops on the way
    //       out, so it stays O(n) instead of concatenating strings.
    /**
     * time = O(N)
     * space = O(N)
     */
    public String getDirections(TreeNode root, int startValue, int destValue) {
        StringBuilder pStart = new StringBuilder();
        StringBuilder pDest = new StringBuilder();
        find(root, startValue, pStart);
        find(root, destValue, pDest);

        // drop the shared prefix (the path down to the LCA)
        int i = 0;
        while (i < pStart.length() && i < pDest.length()
                && pStart.charAt(i) == pDest.charAt(i)) {
            i++;
        }

        StringBuilder sb = new StringBuilder();
        for (int k = i; k < pStart.length(); k++) {
            sb.append('U');
        }
        sb.append(pDest, i, pDest.length());
        return sb.toString();
    }

    private boolean find(TreeNode node, int target, StringBuilder path) {
        if (node == null) {
            return false;
        }
        if (node.val == target) {
            return true;
        }
        path.append('L');
        if (find(node.left, target, path)) {
            return true;
        }
        path.setCharAt(path.length() - 1, 'R');
        if (find(node.right, target, path)) {
            return true;
        }
        path.deleteCharAt(path.length() - 1);
        return false;
    }
}
