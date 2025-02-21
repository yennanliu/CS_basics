package LeetCodeJava.Tree;

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

// https://leetcode.com/problems/diameter-of-binary-tree/
/**
 * 543. Diameter of Binary Tree
 * Solved
 * Easy
 * Topics
 * Companies
 * Given the root of a binary tree, return the length of the diameter of the tree.
 *
 * The diameter of a binary tree is the length of the longest path between any two nodes in a tree. This path may or may not pass through the root.
 *
 * The length of a path between two nodes is represented by the number of edges between them.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3,4,5]
 * Output: 3
 * Explanation: 3 is the length of the path [4,2,1,3] or [5,2,1,3].
 * Example 2:
 *
 * Input: root = [1,2]
 * Output: 1
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * -100 <= Node.val <= 100
 *
 */

public class DiameterOfBinaryTree {

    // V0
    // IDEA : DFS
    // TODO: implement
//    public int diameterOfBinaryTree(TreeNode root) {
//
//    }

    // V1-1
    // https://youtu.be/bkxqA8Rfv04?feature=shared
    // https://neetcode.io/problems/binary-tree-diameter
    // IDEA: BRUTE FORCE
    public int diameterOfBinaryTree_1_1(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int leftHeight = maxHeight(root.left);
        int rightHeight = maxHeight(root.right);
        int diameter = leftHeight + rightHeight;
        int sub = Math.max(diameterOfBinaryTree_1_1(root.left),
                diameterOfBinaryTree_1_1(root.right));
        return Math.max(diameter, sub);
    }

    public int maxHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(maxHeight(root.left), maxHeight(root.right));
    }


    // V1-2
    // https://youtu.be/bkxqA8Rfv04?feature=shared
    // https://neetcode.io/problems/binary-tree-diameter
    // IDEA: DFS
    public int diameterOfBinaryTree_1_2(TreeNode root) {
        int[] res = new int[1];
        dfs(root, res);
        return res[0];
    }

    private int dfs(TreeNode root, int[] res) {
        if (root == null) {
            return 0;
        }
        int left = dfs(root.left, res);
        int right = dfs(root.right, res);
        res[0] = Math.max(res[0], left + right);
        return 1 + Math.max(left, right);
    }


    // V1-3
    // https://youtu.be/bkxqA8Rfv04?feature=shared
    // https://neetcode.io/problems/binary-tree-diameter
    // IDEA: Iterative DFS
    public int diameterOfBinaryTree_1_3(TreeNode root) {
        Map<TreeNode, int[]> mp = new HashMap<>();
        mp.put(null, new int[]{0, 0});
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            TreeNode node = stack.peek();

            if (node.left != null && !mp.containsKey(node.left)) {
                stack.push(node.left);
            } else if (node.right != null && !mp.containsKey(node.right)) {
                stack.push(node.right);
            } else {
                node = stack.pop();

                int[] leftData = mp.get(node.left);
                int[] rightData = mp.get(node.right);

                int leftHeight = leftData[0], leftDiameter = leftData[1];
                int rightHeight = rightData[0], rightDiameter = rightData[1];

                int height = 1 + Math.max(leftHeight, rightHeight);
                int diameter = Math.max(leftHeight + rightHeight,
                        Math.max(leftDiameter, rightDiameter));

                mp.put(node, new int[]{height, diameter});
            }
        }
        return mp.get(root)[1];
    }

    
    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/editorial/
    private int diameter;
    public int diameterOfBinaryTree_2(TreeNode root) {
        diameter = 0;
        longestPath(root);
        return diameter;
    }
    private int longestPath(TreeNode node){
        if(node == null) return 0;
        // recursively find the longest path in
        // both left child and right child
        int leftPath = longestPath(node.left);
        int rightPath = longestPath(node.right);

        // update the diameter if left_path plus right_path is larger
        diameter = Math.max(diameter, leftPath + rightPath);

        // return the longest one between left_path and right_path;
        // remember to add 1 for the path connecting the node and its parent
        return Math.max(leftPath, rightPath) + 1;
    }

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/solutions/3396281/solution/
    int result = -1;
    public int diameterOfBinaryTree_3(TreeNode root) {
        dfs(root);
        return result;
    }
    private int dfs(TreeNode current) {
        if (current == null) {
            return -1;
        }
        int left = 1 + dfs(current.left);
        int right = 1 + dfs(current.right);
        result = Math.max(result, (left + right));
        return Math.max(left, right);
    }

    // V4
    // IDEA: DFS
    // https://leetcode.com/problems/diameter-of-binary-tree/solutions/3280141/beats-100-onlycode-in-java/
    public int diameterOfBinaryTree_4(TreeNode root) {
        int dia[] = new int[1];
        ht(root,dia);
        return dia[0];
    }
    public int ht(TreeNode root , int dia[]){
        if(root == null) return 0;
        int lh = ht(root.left,dia);
        int rh = ht(root.right,dia);
        dia[0] = Math.max(dia[0],lh+rh);
        return 1 + Math.max(lh,rh);
    }

    // V5
    // IDEA; DFS (GPT)
    public int diameterOfBinaryTree_5(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int[] diameter = new int[1]; // Use an array to pass by reference
        calculateDiameter(root, diameter);
        return diameter[0];
    }

    private int calculateDiameter(TreeNode node, int[] diameter) {
        if (node == null) {
            return 0;
        }

        int leftHeight = calculateDiameter(node.left, diameter);
        int rightHeight = calculateDiameter(node.right, diameter);

        // Update the diameter if the current path is longer
        diameter[0] = Math.max(diameter[0], leftHeight + rightHeight);

        // Return the height of the current node (1 + max of left and right)
        return 1 + Math.max(leftHeight, rightHeight);
    }

}
