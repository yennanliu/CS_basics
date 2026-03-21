package LeetCodeJava.DFS;

// https://leetcode.com/problems/smallest-string-starting-from-leaf/description/

import DataStructure.Pair;
import LeetCodeJava.DataStructure.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  988. Smallest String Starting From Leaf
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary tree where each node has a value in the range [0, 25] representing the letters 'a' to 'z'.
 *
 * Return the lexicographically smallest string that starts at a leaf of this tree and ends at the root.
 *
 * As a reminder, any shorter prefix of a string is lexicographically smaller.
 *
 * For example, "ab" is lexicographically smaller than "aba".
 * A leaf of a node is a node that has no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [0,1,2,3,4,3,4]
 * Output: "dba"
 * Example 2:
 *
 *
 * Input: root = [25,1,3,1,3,0,2]
 * Output: "adz"
 * Example 3:
 *
 *
 * Input: root = [2,2,1,null,1,0,null,0]
 * Output: "abc"
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 8500].
 * 0 <= Node.val <= 25
 *
 *
 *
 */

public class SmallestStringStartingFromLeaf {

    // V0
//    public String smallestFromLeaf(TreeNode root) {
//
//    }

    // V0-1
    // IDEA: DFS + BACKTRACK (gemini)
    private String smallest = "";

    public String smallestFromLeaf_0_1(TreeNode root) {
        dfs(root, new StringBuilder());
        return smallest;
    }

    private void dfs(TreeNode node, StringBuilder sb) {
        if (node == null)
            return;


        /** NOTE !!!
         *
         *  PRE-ORDER DFS
         */

        // 1. Add current character (0 -> 'a', 1 -> 'b', etc.)
        sb.append((char) ('a' + node.val));

        /** NOTE !!!
         *
         *  ONLY treat as result when reach `leaf`
         */
        // 2. Leaf check: If we reach a leaf, we have a candidate path
        if (node.left == null && node.right == null) {

            /** NOTE !!!
             *
             *  we reverse cur StringBuilder, to fit the requirement
             */
            // Reverse to get string from leaf to root
            String currentStr = new StringBuilder(sb).reverse().toString();

            /** NOTE !!!
             *
             *  how we get the `lexicographically smaller` one
             *
             *  e.g.
             *
             *  currentStr.compareTo(smallest) < 0
             */
            // Update global smallest if it's empty or lexicographically smaller
            if (smallest.equals("") || currentStr.compareTo(smallest) < 0) {
                smallest = currentStr;
            }
        }

        // 3. Standard DFS
        dfs(node.left, sb);
        dfs(node.right, sb);

        /** NOTE !!!
         *
         *  for StringBuilder (NOT a primitive type),
         *  we have to do BACKTRACK (undo)
         */
        // 4. BACKTRACK: Remove the last character before returning to parent
        sb.deleteCharAt(sb.length() - 1);
    }

    // V0-2
    // IDEA: DFS + BACKTRACK (GPT)
    String result = null;

    public String smallestFromLeaf_0_2(TreeNode root) {
        dfs_0_2(root, new StringBuilder());
        return result;
    }

    private void dfs_0_2(TreeNode node, StringBuilder sb) {
        if (node == null)
            return;

        // prepend current char (leaf → root direction)
        sb.insert(0, (char) ('a' + node.val));

        // if leaf
        if (node.left == null && node.right == null) {
            String path = sb.toString();
            if (result == null || path.compareTo(result) < 0) {
                result = path;
            }
        }

        dfs_0_2(node.left, sb);
        dfs_0_2(node.right, sb);

        // backtrack
        sb.deleteCharAt(0);
    }



    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/smallest-string-starting-from-leaf/editorial/
    String smallestString = "";

    public String smallestFromLeaf_1_1(TreeNode root) {
        dfs(root, "");
        return smallestString;
    }

    // Helper function to find the lexicographically smallest string
    void dfs(TreeNode root, String currentString) {

        // If the current node is NULL, return
        if (root == null) {
            return;
        }

        // Construct the current string by appending
        // the character corresponding to the node's value
        currentString = (char) (root.val + 'a') + currentString;

        // If the current node is a leaf node
        if (root.left == null && root.right == null) {

            // If the current string is smaller than the result
            // or if the result is empty
            if (smallestString.isEmpty() || smallestString.compareTo(currentString) > 0) {
                smallestString = currentString;
            }
        }

        // Recursively traverse the left subtree
        if (root.left != null) {
            dfs(root.left, currentString);
        }

        // Recursively traverse the right subtree
        if (root.right != null) {
            dfs(root.right, currentString);
        }

    }



    // V1-2
    // IDEA: BFS
    // https://leetcode.com/problems/smallest-string-starting-from-leaf/editorial/
    public String smallestFromLeaf_1_2(TreeNode root) {
        String smallestString = "";
        Queue<Pair<TreeNode, String>> nodeQueue = new LinkedList<>();

        // Add root node to queue along with its value converted to a character
        nodeQueue.add(new Pair<>(root, String.valueOf((char) (root.val + 'a'))));

        // Perform BFS traversal until queue is empty
        while (!nodeQueue.isEmpty()) {

            // Pop the leftmost node and its corresponding string from queue
            Pair<TreeNode, String> pair = nodeQueue.poll();
            TreeNode node = pair.getKey();
            String currentString = pair.getValue();

            // If current node is a leaf node
            if (node.left == null && node.right == null) {

                // Update smallest_string if it's empty or current string is smaller
                if (smallestString.isEmpty()) {
                    smallestString = currentString;
                } else {
                    smallestString = currentString.compareTo(smallestString) < 0 ? currentString : smallestString;
                }
            }

            // If current node has a left child, append it to queue
            if (node.left != null) {
                nodeQueue.add(new Pair<>(node.left, (char) (node.left.val + 'a') + currentString));
            }

            // If current node has a right child, append it to queue
            if (node.right != null) {
                nodeQueue.add(new Pair<>(node.right, (char) (node.right.val + 'a') + currentString));
            }
        }

        return smallestString;
    }



    // V2




}
