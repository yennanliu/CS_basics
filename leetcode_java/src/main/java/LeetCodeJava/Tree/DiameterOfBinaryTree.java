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
    // https://youtu.be/K81C31ytOZE?si=J87ADf5azss_tAjN
//    public int diameterOfBinaryTree(TreeNode root) {
//
//    }

    // V0-1
    // IDEA: DFS (gpt) (bottom up)
    /**
     *  NOTE !!!
     *
     *    the `diameter` is the `SUM OF DEPTHS` of sub left and sub right tree
     *
     *    ( This path may or may not pass through the root.)
     *
     *
     *  IDEA:
     *
     *  1) Path Characteristics:
     *
     *     The longest path in a tree typically
     *     goes through some node, and that node is either an ancestor
     *     or a descendant of the two nodes. Thus
     *     -> `the diameter will involve the sum of the "depths"
     *     of the left and right subtrees for a particular node`.
     *
     *  2) DFS Approach:
     *
     *    We can use a Depth-First Search (DFS) approach where:
     *
     *     - For each node, we calculate the height of the left and right subtrees.
     *
     *     - The potential `diameter` for each node is
     *       "the sum of the heights of its left and right subtrees".
     *
     *     - We keep track of the maximum diameter encountered during the
     *       DFS traversal.
     *
     *
     *  Approach:
     *
     *    - For each node:
     *
     *        - Calculate the depth of the left and right subtrees.
     *
     *        - The diameter through that node is the sum of these two depths.
     *
     *        - The result is the maximum of the current diameter and the
     *          previously calculated diameters.
     */
    /**
     *  Example:
     *
     *    For the input tree:
     *
     *         1
     *        / \
     *       2   3
     *      / \
     *     4   5
     *
     *   NOTE !!!
     *    A binary tree's maximum `depth` is the number of NODES (NOTE this !!!)
     *    along the longest path from the root node down to the farthest leaf node.
     *
     *
     *   - Step 1) Starting at node 1:
     *
     *      - Left subtree height: maxDepth(2) → returns 2 (from node 4 to node 2).
     *      - Right subtree height: maxDepth(3) → returns 1 (node 3).
     *      - The potential diameter at node 1 is 2 + 1 = 3. We update the maxDiameter to 3.
     *
     *   - Step 2) At node 2:
     *
     *    - Left subtree height: maxDepth(4) → returns 0 (no children).
     *    - Right subtree height: maxDepth(5) → returns 0 (no children).
     *    - The potential diameter at node 2 is 0 + 0 = 0, but the maxDiameter remains 3.
     *
     *   - Step 3) At node 3:
     *
     *    - Both left and right subtrees are null, so the height is 0.
     *    - The potential diameter at node 3 is 0 + 0 = 0, so the maxDiameter remains 3.
     *
     *
     *  -> The final output is 3, which is the diameter of the tree.
     *
     *
     */
    // maxDiameter Variable: This is a class-level variable that keeps track of the maximum diameter encountered so far during the DFS traversal.
    private int maxDiameter = 0;

    public int diameterOfBinaryTree_0_1(TreeNode root) {
        // Start DFS from the root to calculate the maximum diameter
        dfs_0_1(root);
        return maxDiameter;
    }

    // DFS helper function to calculate height and update the diameter
    /**
     *  - For each node, the function `recursively`
     *    computes the heights of the left and right subtrees.
     *
     * - The diameter at the current node is the sum of the
     *   heights of the left and right subtrees (leftHeight + rightHeight).
     *
     * - The function keeps track of the maximum diameter
     *    found during the DFS traversal.
     *
     * - The function returns the height of the current node,
     *    which is the maximum of the left and right subtree heights plus 1.
     *
     */
    private int dfs_0_1(TreeNode node) {
        // Base case: if the node is null, return a height of 0
        if (node == null) {
            return 0;
        }

        // Recursively find the height of the left and right subtrees
        int leftHeight = dfs_0_1(node.left);
        int rightHeight = dfs_0_1(node.right);

        // Update the maximum diameter if the path through this node is larger
        /** NOTE !!!
         *
         * return val from helper func is different from the final result
         *
         *
         *  -> diameter =  (left_depth + right_depth)
         *     ( the `diameter` is the `SUM Of DEPTHS` of sub left and sub right tree )
         *
         *  -> so we get the `updated max diameter` via below
         *
         */
        maxDiameter = Math.max(maxDiameter, leftHeight + rightHeight);

        // Return the height of the current node, which is the max of the left and right subtree heights plus 1
        /**
         *  NOTE !!!
         *
         *   we return the max (leftHeight, rightHeight) as the `upper level node` 's diameter base
         *   and `+1` is because we move `1 upper layer`, so the possible diameter increase by 1
         */
        /**
         *
         * NOTE !!!
         *
         *   -> return `Math.max(leftHeight, rightHeight) + 1`
         */
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // V0-2
    // IDEA: get Max depth + `diameter + sub left depth + sub right depth + DFS  (bottom up)
    // NOTE !!! we define below global var
    int maxDiameter_ = 0;
    public int diameterOfBinaryTree_0_2(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 0;
        }

        getMaxDiameter(root);
        return maxDiameter_;
    }

    // NOTE !!! diameter = `left sub tree depth` + `right sub tree depth`
    public int getMaxDiameter(TreeNode root) {
        if (root == null) {
            return 0;
        }
        //int
        int leftDepth = getDepth(root.left);
        int rightDepth = getDepth(root.right);

        /** NOTE !!!  return val from helper func is different from the final result */
        maxDiameter_ = Math.max(maxDiameter_, leftDepth + rightDepth);

        return Math.max(getMaxDiameter(root.left), getMaxDiameter(root.right)); // ???
    }

    public int getDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getDepth(root.left), getDepth(root.right)) + 1;
    }

    // V0-3
    // IDEA: DFS (fixed by gpt)
    private int maxDiameter_0_3 = 0;

    public int diameterOfBinaryTree_0_3(TreeNode root) {
        dfs(root);
        return maxDiameter_0_3;
    }

    private int dfs_0_3(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int left = dfs_0_3(node.left);
        int right = dfs_0_3(node.right);

        // update max diameter at this node
        maxDiameter_0_3 = Math.max(maxDiameter_0_3, left + right);

        // return height of this subtree
        return 1 + Math.max(left, right);
    }

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
