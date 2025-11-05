package LeetCodeJava.Tree;

// https://leetcode.com/problems/cousins-in-binary-tree-ii/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.*;

/**
 * 2641. Cousins in Binary Tree II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given the root of a binary tree, replace the value of each node in the tree with the sum of all its cousins' values.
 *
 * Two nodes of a binary tree are cousins if they have the same depth with different parents.
 *
 * Return the root of the modified tree.
 *
 * Note that the depth of a node is the number of edges in the path from the root node to it.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,4,9,1,10,null,7]
 * Output: [0,0,0,7,7,null,11]
 * Explanation: The diagram above shows the initial binary tree and the binary tree after changing the value of each node.
 * - Node with value 5 does not have any cousins so its sum is 0.
 * - Node with value 4 does not have any cousins so its sum is 0.
 * - Node with value 9 does not have any cousins so its sum is 0.
 * - Node with value 1 has a cousin with value 7 so its sum is 7.
 * - Node with value 10 has a cousin with value 7 so its sum is 7.
 * - Node with value 7 has cousins with values 1 and 10 so its sum is 11.
 * Example 2:
 *
 *
 * Input: root = [3,1,2]
 * Output: [0,0,0]
 * Explanation: The diagram above shows the initial binary tree and the binary tree after changing the value of each node.
 * - Node with value 3 does not have any cousins so its sum is 0.
 * - Node with value 1 does not have any cousins so its sum is 0.
 * - Node with value 2 does not have any cousins so its sum is 0.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 105].
 * 1 <= Node.val <= 104
 *
 */
public class CousinsInBinaryTree2 {

    // V0
//    public TreeNode replaceValueInTree(TreeNode root) {
//
//    }

    // V1
    // IDEA: BFS (gpt)
    public TreeNode replaceValueInTree_1(TreeNode root) {
        if (root == null)
            return null;

        // root has no cousins
        root.val = 0;

        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);

        while (!q.isEmpty()) {
            int size = q.size();

            // Collect children of this level and compute total sum of their original values
            List<TreeNode> children = new ArrayList<>();
            Map<TreeNode, Integer> parentChildrenSum = new HashMap<>();
            int nextLevelSum = 0;

            // iterate current level
            for (int i = 0; i < size; i++) {
                TreeNode parent = q.poll();
                int siblingSum = 0;

                if (parent.left != null) {
                    siblingSum += parent.left.val;
                    children.add(parent.left);
                    nextLevelSum += parent.left.val;
                }
                if (parent.right != null) {
                    siblingSum += parent.right.val;
                    children.add(parent.right);
                    nextLevelSum += parent.right.val;
                }

                parentChildrenSum.put(parent, siblingSum);
            }

            // assign new values for children (based on nextLevelSum and siblingSum for their parent)
            // note: we must use parentChildrenSum (keyed by parent) to exclude sibling contributions
            for (Map.Entry<TreeNode, Integer> entry : parentChildrenSum.entrySet()) {
                TreeNode parent = entry.getKey();
                int siblingSum = entry.getValue();
                if (parent.left != null) {
                    parent.left.val = nextLevelSum - siblingSum;
                }
                if (parent.right != null) {
                    parent.right.val = nextLevelSum - siblingSum;
                }
            }

            // push children into queue for next iteration
            for (TreeNode child : children) {
                q.offer(child);
            }
        }

        return root;
    }

    // V2
    // IDEA: BFS (gemini)
    // Map to store the total sum of node values for each depth.
    private Map<Integer, Integer> depthSum = new HashMap<>();

    public TreeNode replaceValueInTree_2(TreeNode root) {
        if (root == null) {
            return null;
        }

        // --- Pass 1: Calculate the total sum for each depth using BFS ---
        calculateDepthSums(root);

        // --- Pass 2: Traverse and Replace Values using BFS ---

        // The root node (depth 0) is a special case: its value is always 0.
        root.val = 0;

        // Use a queue to store the PARENT nodes of the level being processed.
        // We will process the children of these nodes.
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int depth = 1;

        // BFS loop continues as long as there are nodes to process
        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            // Get the total sum for the current depth being modified (depth 1, 2, 3, etc.)
            // We only need to check the depth if it exists in the map (i.e., the level is not empty).
            if (!depthSum.containsKey(depth)) {
                break;
            }
            int totalLevelSum = depthSum.get(depth);

            // Iterate through all parent nodes from the previous level
            for (int i = 0; i < levelSize; i++) {
                TreeNode parent = queue.poll();

                // 1. Calculate the sum of the siblings (children of the current parent)
                int childrenSum = 0;
                if (parent.left != null) {
                    childrenSum += parent.left.val;
                }
                if (parent.right != null) {
                    childrenSum += parent.right.val;
                }

                // 2. Apply the replacement logic to the left child
                if (parent.left != null) {
                    // New Value = (Total Sum at Depth) - (Sibling Sum)
                    // The sibling sum here is the combined value of the left and right child.
                    parent.left.val = totalLevelSum - childrenSum;

                    // Add the modified node to the queue for the next iteration (next depth's parents)
                    queue.add(parent.left);
                }

                // 3. Apply the replacement logic to the right child
                if (parent.right != null) {
                    parent.right.val = totalLevelSum - childrenSum;

                    // Add the modified node to the queue for the next iteration
                    queue.add(parent.right);
                }
            }
            depth++; // Move to the next depth
        }

        return root;
    }

    /**
     * Pass 1 Helper: Calculates the total sum of all nodes at each depth using BFS.
     */
    private void calculateDepthSums(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            int currentLevelSum = 0;

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevelSum += node.val;

                if (node.left != null)
                    queue.add(node.left);
                if (node.right != null)
                    queue.add(node.right);
            }
            // Store the total sum for the current depth
            depthSum.put(depth, currentLevelSum);
            depth++;
        }
    }

    // V3
    // IDEA: DFS (gpt)
    Map<Integer, Integer> levelSum = new HashMap<>();

    public TreeNode replaceValueInTree_3(TreeNode root) {
        if (root == null)
            return null;

        // 1️⃣ collect sums per depth
        collectLevelSum(root, 0);

        // 2️⃣ replace values based on cousin logic
        root.val = 0;
        dfsReplace(root, 0);

        return root;
    }

    private void collectLevelSum(TreeNode node, int depth) {
        if (node == null)
            return;
        levelSum.put(depth, levelSum.getOrDefault(depth, 0) + node.val);
        collectLevelSum(node.left, depth + 1);
        collectLevelSum(node.right, depth + 1);
    }

    private void dfsReplace(TreeNode node, int depth) {
        if (node == null)
            return;

        int nextDepth = depth + 1;
        int siblingSum = 0;
        if (node.left != null)
            siblingSum += node.left.val;
        if (node.right != null)
            siblingSum += node.right.val;

        if (node.left != null) {
            node.left.val = levelSum.getOrDefault(nextDepth, 0) - siblingSum;
        }
        if (node.right != null) {
            node.right.val = levelSum.getOrDefault(nextDepth, 0) - siblingSum;
        }

        dfsReplace(node.left, nextDepth);
        dfsReplace(node.right, nextDepth);
    }

    // V4

    // V5


}
