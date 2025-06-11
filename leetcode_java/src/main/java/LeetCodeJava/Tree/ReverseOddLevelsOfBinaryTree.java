package LeetCodeJava.Tree;

// https://leetcode.com/problems/reverse-odd-levels-of-binary-tree/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 2415. Reverse Odd Levels of Binary Tree
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given the root of a perfect binary tree, reverse the node values at each odd level of the tree.
 *
 * For example, suppose the node values at level 3 are [2,1,3,4,7,11,29,18], then it should become [18,29,11,7,4,3,1,2].
 * Return the root of the reversed tree.
 *
 * A binary tree is perfect if all parent nodes have two children and all leaves are on the same level.
 *
 * The level of a node is the number of edges along the path between it and the root node.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [2,3,5,8,13,21,34]
 * Output: [2,5,3,8,13,21,34]
 * Explanation:
 * The tree has only one odd level.
 * The nodes at level 1 are 3, 5 respectively, which are reversed and become 5, 3.
 * Example 2:
 *
 *
 * Input: root = [7,13,11]
 * Output: [7,11,13]
 * Explanation:
 * The nodes at level 1 are 13, 11, which are reversed and become 11, 13.
 * Example 3:
 *
 * Input: root = [0,1,2,0,0,0,0,1,1,1,1,2,2,2,2]
 * Output: [0,2,1,0,0,0,0,2,2,2,2,1,1,1,1]
 * Explanation:
 * The odd levels have non-zero values.
 * The nodes at level 1 were 1, 2, and are 2, 1 after the reversal.
 * The nodes at level 3 were 1, 1, 1, 1, 2, 2, 2, 2, and are 2, 2, 2, 2, 1, 1, 1, 1 after the reversal.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 214].
 * 0 <= Node.val <= 105
 * root is a perfect binary tree.
 *
 *
 */
public class ReverseOddLevelsOfBinaryTree {

    // V0
//    public TreeNode reverseOddLevels(TreeNode root) {
//
//    }

    // V0-1
    // IDEA: DFS + `left, right, layer as helper func parameter` (fixed by gpt)
    public TreeNode reverseOddLevels_0_1(TreeNode root) {
        if (root == null)
            return null;

        reverseHelper(root.left, root.right, 1);
        return root;
    }

    /**
     *  NOTE !!!
     *
     *   we NEED to setup 3 parameter in the helper func
     *
     *   1. left node
     *   2. right node
     *   3. layer
     *
     *
     *  NOTE !!!
     *
     *   the helper func return NOTHING !!! (e.g. void)
     */
    private void reverseHelper(TreeNode left, TreeNode right, int level) {
        if (left == null || right == null)
            return;

        // Swap values if we're at an odd level
        if (level % 2 == 1) {
            int temp = left.val;
            left.val = right.val;
            right.val = temp;
        }

        /**  NOTE !!! below
         *
         *
         */
        // Recurse into symmetric children
        reverseHelper(left.left, right.right, level + 1);
        reverseHelper(left.right, right.left, level + 1);
    }

    // V1

    // V2-1
    // https://leetcode.com/problems/reverse-odd-levels-of-binary-tree/editorial/
    // IDEA: DFS
    public TreeNode reverseOddLevels_2_1(TreeNode root) {
        traverseDFS(root.left, root.right, 0);
        return root;
    }

    /**
     *  NOTE !!!
     *
     *   we NEED to setup 3 parameter in the helper func
     *
     *   1. left node
     *   2. right node
     *   3. layer
     *
     *
     *  NOTE !!!
     *
     *   the helper func return NOTHING !!! (e.g. void)
     */
    private void traverseDFS(
            TreeNode leftChild,
            TreeNode rightChild,
            int level) {
        if (leftChild == null || rightChild == null) {
            return;
        }
        //If the current level is odd, swap the values of the children.
        if (level % 2 == 0) {
            int temp = leftChild.val;
            leftChild.val = rightChild.val;
            rightChild.val = temp;
        }

        traverseDFS(leftChild.left, rightChild.right, level + 1);
        traverseDFS(leftChild.right, rightChild.left, level + 1);
    }


    // V2-2
    // https://leetcode.com/problems/reverse-odd-levels-of-binary-tree/editorial/
    // IDEA: BFS
    public TreeNode reverseOddLevels_2_2(TreeNode root) {
        if (root == null) {
            return null; // Return null if the tree is empty.
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root); // Start BFS with the root node.
        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            List<TreeNode> currentLevelNodes = new ArrayList<>();

            // Process all nodes at the current level.
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                currentLevelNodes.add(node);

                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
            }

            // Reverse node values if the current level is odd.
            if (level % 2 == 1) {
                int left = 0, right = currentLevelNodes.size() - 1;
                while (left < right) {
                    int temp = currentLevelNodes.get(left).val;
                    currentLevelNodes.get(left).val = currentLevelNodes.get(
                            right).val;
                    currentLevelNodes.get(right).val = temp;
                    left++;
                    right--;
                }
            }

            level++;
        }

        return root; // Return the modified tree root.
    }


}
