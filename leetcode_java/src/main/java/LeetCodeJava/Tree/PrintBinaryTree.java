package LeetCodeJava.Tree;

// https://leetcode.com/problems/print-binary-tree/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  655. Print Binary Tree
 *  Medium
 *
 *  Given the root of a binary tree, construct a 0-indexed m x n string matrix
 *  res that represents a formatted layout of the tree:
 *
 *   - the number of rows m == height + 1 (height = height of the tree)
 *   - the number of columns n == 2^(height+1) - 1
 *   - the root sits at res[0][(n-1)/2]
 *   - a node placed at res[r][c] puts its left child at
 *     res[r+1][c - 2^(height-r-1)] and its right child at
 *     res[r+1][c + 2^(height-r-1)]
 *   - every empty cell holds the empty string ""
 *
 *  Example 1:
 *
 *  Input: root = [1,2]
 *  Output: [["","1",""],["2","",""]]
 *
 *  Example 2:
 *
 *  Input: root = [1,2,3,null,4]
 *  Output: [["","","","1","","",""],
 *           ["","2","","","","3",""],
 *           ["","","4","","","",""]]
 *
 *  Constraints:
 *
 *  The number of nodes in the tree is in the range [1, 2^10].
 *  -99 <= Node.val <= 99
 *  The depth of the tree will be in the range [1, 10].
 */
public class PrintBinaryTree {

    private int height;
    private String[][] matrix;

    // V0
    // IDEA: get the tree height first (so the matrix size is known),
    //       then DFS and place each node with offset 2^(height-row-1)
    /**
     * time = O(h * 2^h)   // h = tree height, dominated by the matrix size
     * space = O(h * 2^h)
     */
    public List<List<String>> printTree(TreeNode root) {
        List<List<String>> res = new ArrayList<>();
        if (root == null) {
            return res;
        }

        // height as LeetCode defines it (a single node tree has height 0)
        this.height = getHeight(root) - 1;

        int rows = this.height + 1;
        int cols = (1 << (this.height + 1)) - 1;

        this.matrix = new String[rows][cols];
        for (String[] row : this.matrix) {
            Arrays.fill(row, "");
        }

        helper(root, 0, (cols - 1) / 2);

        for (int r = 0; r < rows; r++) {
            List<String> cur = new ArrayList<>(Arrays.asList(this.matrix[r]));
            res.add(cur);
        }
        return res;
    }

    private int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private void helper(TreeNode node, int row, int col) {
        if (node == null) {
            return;
        }
        this.matrix[row][col] = String.valueOf(node.val);
        if (row == this.height) {
            return;
        }
        int offset = 1 << (this.height - row - 1);
        helper(node.left, row + 1, col - offset);
        helper(node.right, row + 1, col + offset);
    }
}
