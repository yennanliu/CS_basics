package LeetCodeJava.Tree;

// https://leetcode.com/problems/create-binary-tree-from-descriptions/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import LeetCodeJava.DataStructure.TreeNode;

/**
 *  2196. Create Binary Tree From Descriptions
 *  Medium
 *
 *  You are given a 2D integer array descriptions where
 *  descriptions[i] = [parent_i, child_i, isLeft_i] indicates that parent_i is the
 *  parent of child_i in a binary tree of unique values. Furthermore,
 *
 *   - If isLeft_i == 1, then child_i is the left child of parent_i.
 *   - If isLeft_i == 0, then child_i is the right child of parent_i.
 *
 *  Construct the binary tree described by descriptions and return its root.
 *  The test cases will be generated such that the binary tree is valid.
 *
 *  Example 1:
 *    Input: descriptions = [[20,15,1],[20,17,0],[50,20,1],[50,80,0],[80,19,1]]
 *    Output: [50,20,80,15,17,19]
 *    Explanation: the root is 50 since it has no parent.
 *
 *  Example 2:
 *    Input: descriptions = [[1,2,1],[2,3,0],[3,4,1]]
 *    Output: [1,2,null,null,3,4]
 *
 *  Constraints:
 *    1 <= descriptions.length <= 10^4
 *    descriptions[i].length == 3
 *    1 <= parent_i, child_i <= 10^5
 *    0 <= isLeft_i <= 1
 *    The binary tree described by descriptions is valid.
 */
public class CreateBinaryTreeFromDescriptions {

    // V0
    // IDEA: NODE POOL KEYED BY VALUE + A SET OF EVERY VALUE THAT HAS A PARENT
    //       values are unique, so a map value -> TreeNode lets each description
    //       wire up its two nodes regardless of the row order (creating either
    //       endpoint on demand).
    //       the root is the ONE value that never appears as a CHILD, so track
    //       the set of children while building and take the single leftover.
    /**
     * time = O(N)
     * space = O(N)
     */
    public TreeNode createBinaryTree(int[][] descriptions) {
        Map<Integer, TreeNode> nodes = new HashMap<>();
        Set<Integer> children = new HashSet<>();

        for (int[] d : descriptions) {
            int parent = d[0];
            int child = d[1];
            int isLeft = d[2];
            TreeNode p = getNode(nodes, parent);
            TreeNode c = getNode(nodes, child);
            if (isLeft == 1) {
                p.left = c;
            } else {
                p.right = c;
            }
            children.add(child);
        }

        for (Map.Entry<Integer, TreeNode> e : nodes.entrySet()) {
            if (!children.contains(e.getKey())) {
                return e.getValue();
            }
        }
        return null;
    }

    private TreeNode getNode(Map<Integer, TreeNode> nodes, int val) {
        if (!nodes.containsKey(val)) {
            nodes.put(val, new TreeNode(val));
        }
        return nodes.get(val);
    }
}
