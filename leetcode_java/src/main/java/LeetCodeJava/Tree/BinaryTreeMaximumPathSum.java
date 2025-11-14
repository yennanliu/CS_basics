package LeetCodeJava.Tree;

// https://leetcode.com/problems/binary-tree-maximum-path-sum/description/
/**
 *  124. Binary Tree Maximum Path Sum
 * Solved
 * Hard
 * Topics
 * Companies
 * A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge connecting them. A node can only appear in the sequence at most once. Note that the path does not need to pass through the root.
 *
 * The path sum of a path is the sum of the node's values in the path.
 *
 * Given the root of a binary tree, return the maximum path sum of any non-empty path.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3]
 * Output: 6
 * Explanation: The optimal path is 2 -> 1 -> 3 with a path sum of 2 + 1 + 3 = 6.
 * Example 2:
 *
 *
 * Input: root = [-10,9,20,null,null,15,7]
 * Output: 42
 * Explanation: The optimal path is 15 -> 20 -> 7 with a path sum of 15 + 20 + 7 = 42.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 3 * 104].
 * -1000 <= Node.val <= 1000
 *
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;


public class BinaryTreeMaximumPathSum {

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     *     int val;
     *     TreeNode left;
     *     TreeNode right;
     *     TreeNode() {}
     *     TreeNode(int val) { this.val = val; }
     *     TreeNode(int val, TreeNode left, TreeNode right) {
     *         this.val = val;
     *         this.left = left;
     *         this.right = right;
     *     }
     * }
     */

    // V0
    // IDEA: DFS + `path` definition (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *   (from problem description)
     *
     *   -> A path in a binary tree is a sequence of
     *     nodes where each pair of adjacent nodes in
     *     the sequence has `AN edge connecting them.`
     *
     *
     *   -> so the `path` is a `single path` that connect
     *      from a node to the other.
     *
     *   -> e.g.:
     *
     *       (`single` path)
     *      node_a ---node_1,-- node_2, ...-- node_b
     *
     */
    /**
     *  NOTE !!!
     *
     *   1. maxPathSum = Math.max(maxPathSum, root.val + left + right);
     *
     *   2. (DFS func return) root.val + Math.max(left, right);
     *
     */
    int maxPathSum = Integer.MIN_VALUE; // global tracker

    public int maxPathSum(TreeNode root) {
        dfs_0(root);
        return maxPathSum;
    }

    private int dfs_0(TreeNode root) {
        if (root == null)
            return 0;

    // compute max sum of left/right branches (ignore negatives)
    /**
     *  NOTE !!!
     *
     *   we need to use `int left = Math.max(0, dfs_0(root.left));`
     *   (instead of int left = dfs_0(root.left))
     *
     *  Reason:
     *     We want to find the maximum path sum in the tree.
     *     A “path” can start and `end at any node`,
     *     but it must be continuous.
     *
     *    ⚠️ If a subtree contributes a negative sum,
     *     including it will reduce the total path sum.
     *
     *    That’s why we take Math.max(0, dfs(...)).
     *    It means: “If the best path from this child is negative,
     *    just ignore it (treat as 0).”
     *
     *
     *   Example:
     *
     *         10
     *        /  \
     *      -5    20
     *
     *
     *      👉 Without Math.max(0, …):
     * 	        •	dfs(root.left) = -5
     * 	        •	dfs(root.right) = 20
     * 	        •	Path through root = 10 + (-5) + 20 = 25
     *
     *
     *    -> But actually, the better path
     *      is just 10 + 20 = 30 (ignoring the left side).
     *
     *   👉 With Math.max(0, …):
     * 	        •	left = Math.max(0, -5) = 0
     * 	        •	right = Math.max(0, 20) = 20
     * 	        •	Path through root = 10 + 0 + 20 = 30 ✅
     *
     */
        int left = Math.max(0, dfs_0(root.left));
        int right = Math.max(0, dfs_0(root.right));

        // update global maximum with "path through root"
        maxPathSum = Math.max(maxPathSum, root.val + left + right);

    // return the best single-branch path sum upward
    /**
     *  NOTE !!!
     *
     *   return `root.val + Math.max(left, right)
     *
     *   1) This comes from the difference between
     *     “path sum” vs “branch sum”
     *     in the Maximum Path Sum in a Binary Tree problem.
     *
     *   2) But you cannot pass both branches upward,
     *   because your parent can only connect to
     *   this node through one edge (either left or right).
     *
     *
     *  -> e.g.
     *
     *   we can ONLY propagate a `single path` (sub left or right tree) to
     *   `upper` node, NOT both path
     *
     *   Example:
     *
     *         10
     *       /  \
     *      2    10
     *     / \     \
     *    20  1     -25
     *               /  \
     *              3    4
     *
     *
     *   -> for `node 10`
     *      we can ONLY poss path (-25, 3) or (-25, 4)
     *      NOT pass 2 paths on the time
     *
     */
    return root.val + Math.max(left, right);
    }

    // V0-0-1
    // IDEA: DFS + HASHMAP (fixed by gpt)
    int maxPath = Integer.MIN_VALUE;
    public int maxPathSum_0_0_1(TreeNode root) {
        if (root == null)
            return 0;

        getPathHelper(root);
        return maxPath;
    }

    // Helper: returns max path sum starting from current node going down one branch
    private int getPathHelper(TreeNode root) {
        if (root == null)
            return 0;

        // max sum from left or right subtree, ignore negative paths
        int left = Math.max(0, getPathHelper(root.left));
        int right = Math.max(0, getPathHelper(root.right));

        // update global max: could be path through current node including both sides
        maxPath = Math.max(maxPath, left + right + root.val);

        // return max single-side path for parent to use
        return Math.max(left, right) + root.val;
    }

    // V0-0-2
    // IDEA: DFS + HASHMAP (fixed by gpt)
    // Store the "path through this node" values
    Map<TreeNode, Integer> pathMap = new HashMap<>();
    int globalMax = Integer.MIN_VALUE;

    public int maxPathSum_0_0_2(TreeNode root) {
        dfs_0_0_2(root);

        // Combine all values from pathMap
        for (int v : pathMap.values()) {
            globalMax = Math.max(globalMax, v);
        }
        return globalMax;
    }

    // Returns max downward path from this node
    private int dfs_0_0_2(TreeNode node) {
        if (node == null) return 0;

        int left = Math.max(dfs_0_0_2(node.left), 0);
        int right = Math.max(dfs_0_0_2(node.right), 0);

        int throughNode = node.val + left + right;

        // store into your pathMap (fixed)
        pathMap.put(node, throughNode);

        // update global max as well
        globalMax = Math.max(globalMax, throughNode);

        // return max downward branch
        return node.val + Math.max(left, right);
    }

    // V0-1
    // IDEA: DFS (GPT)
    private int maxSum = Integer.MIN_VALUE;

    public int maxPathSum_0_1(TreeNode root) {
        if (root == null) {
            return Integer.MIN_VALUE; // Handle null case
        }

        dfs(root);
        return maxSum;
    }

    /** NOTE !!!
     *
     *  the response type of dfs is `integer`
     *  e.g. the `max path sum` per input node
     */
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }

        // Compute max path sum of left and right children, discard negative values
        /**
         *  NOTE !!!
         *
         *   we cache `leftMax` on current node
         *   we cache `rightMax` on current node
         *
         *   so we can update global `max path sum` below
         */
        /** NOTE !!!
         *
         *
         *   -> need to get max from left_path, 0
         *
         * int _left = Math.max(getMaxPathHelper(root.left), 0);
         *
         * This line ensures that we ignore any negative path sums
         * when calculating the maximum path from the current node.
         *
         * ⸻
         *
         * 🔍 Why use Math.max(..., 0)?
         *
         * In a binary tree, a path can pass through the left and right subtrees. However:
         * 	•	If the left (or right) subtree contributes a negative sum, including it would decrease the total path sum.
         * 	•	Since we want the maximum path sum, it’s better to exclude any negative paths (i.e., treat them as 0).
         *
         * ⸻
         *
         * ✅ What it means:
         *
         * Math.max(getMaxPathHelper(root.left), 0);
         *
         * 	•	If the left subtree contributes positively → keep it.
         * 	•	If the left subtree contributes negatively → treat it as 0 (ignore it).
         *
         * ⸻
         *
         * 🧠 Example:
         *
         *       -10
         *       /  \
         *     -20  10
         *
         * Without Math.max(..., 0):
         * 	•	_left = getMaxPathHelper(root.left) = -20
         * 	•	Resulting path sum: -10 + (-20) + 10 = -20
         *
         * With Math.max(..., 0):
         * 	•	_left = 0 (ignore -20)
         * 	•	Resulting path sum: -10 + 0 + 10 = 0 ← better!
         *
         */
        int leftMax = Math.max(dfs(node.left), 0);
        int rightMax = Math.max(dfs(node.right), 0);

        // Update global max sum with current node as the highest ancestor
        /**
         *  NOTE !!!
         *
         *  we update global `max path sum`,
         *  but the `maxSum` is NOT return as method reponse,
         *  we simply update the global variable `maxSum`
         *
         *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax))
         */
        maxSum = Math.max(maxSum, node.val + leftMax + rightMax);

        // Return max sum path including this node (but only one subtree path)
        /**
         *  NOTE !!!
         *
         *
         *  -> the method return val is local max path (node.val + Math.max(leftMax, rightMax)),
         *     instead of `maxSum`
         *
         */
        return node.val + Math.max(leftMax, rightMax);
    }

    // V0-2
    // IDEA: DFS + HASHMAP (gpt) (NOT efficient)
    Map<TreeNode, Integer> pathMap = new HashMap<>();
    int maxSum_2 = Integer.MIN_VALUE;

    public int maxPathSum_0_2(TreeNode root) {
        if (root == null)
            return 0;

        getMaxPathSumHelper(root);

        for (int val : pathMap.values()) {
            maxSum_2 = Math.max(maxSum_2, val);
        }

        return maxSum_2;
    }

    private int getMaxPathSumHelper(TreeNode node) {
        if (node == null)
            return 0;

        int left = Math.max(0, getMaxPathSumHelper(node.left));
        int right = Math.max(0, getMaxPathSumHelper(node.right));

        int maxAtNode = node.val + left + right;
        int maxOneSide = node.val + Math.max(left, right);

        // Store the max *one-sided* path for this node
        pathMap.put(node, maxOneSide);

        // Update global maxSum
        maxSum_2 = Math.max(maxSum_2, maxAtNode);

        return maxOneSide;
    }

    // V0-3
    // IDEA: DFS + `Post order traverse`
    /** NOTE !!! we init res as Integer.MIN_VALUE */
    int max_path_sum = Integer.MIN_VALUE; // Fix this initialization

    public int maxPathSum_0_3(TreeNode root) {
        // edge
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return root.val;
        }

        //dfs
        getMaxPathHelper(root);

        return max_path_sum;
    }

    public int getMaxPathHelper(TreeNode root) {

        if (root == null) {
            return 0; // ??
        }

        /** NOTE !!!
         *
         *
         *   -> need to get max from left_path, 0
         *
         * int _left = Math.max(getMaxPathHelper(root.left), 0);
         *
         * This line ensures that we ignore any negative path sums
         * when calculating the maximum path from the current node.
         *
         * ⸻
         *
         * 🔍 Why use Math.max(..., 0)?
         *
         * In a binary tree, a path can pass through the left and right subtrees. However:
         * 	•	If the left (or right) subtree contributes a negative sum, including it would decrease the total path sum.
         * 	•	Since we want the maximum path sum, it’s better to exclude any negative paths (i.e., treat them as 0).
         *
         * ⸻
         *
         * ✅ What it means:
         *
         * Math.max(getMaxPathHelper(root.left), 0);
         *
         * 	•	If the left subtree contributes positively → keep it.
         * 	•	If the left subtree contributes negatively → treat it as 0 (ignore it).
         *
         * ⸻
         *
         * 🧠 Example:
         *
         *       -10
         *       /  \
         *     -20  10
         *
         * Without Math.max(..., 0):
         * 	•	_left = getMaxPathHelper(root.left) = -20
         * 	•	Resulting path sum: -10 + (-20) + 10 = -20
         *
         * With Math.max(..., 0):
         * 	•	_left = 0 (ignore -20)
         * 	•	Resulting path sum: -10 + 0 + 10 = 0 ← better!
         *
         * ⸻
         *
         * Summary:
         *
         * Expression	Meaning
         * getMaxPathHelper(root.left)	Get path sum from left child
         * Math.max(..., 0)	Eliminate negative path contributions
         * root.val + _left + _right	Best path through current node
         * root.val + Math.max(_left, _right)	Best path from current node to parent
         *
         */
        int _left = Math.max(getMaxPathHelper(root.left), 0);
        int _right = Math.max(getMaxPathHelper(root.right), 0);

        max_path_sum = Math.max(max_path_sum,
                root.val + _left + _right);

        /** NOTE !!! */
        return root.val + Math.max(_left, _right);
    }

    // V1-1
    // https://neetcode.io/problems/binary-tree-maximum-path-sum
    // IDEA: DFS
    int res = Integer.MIN_VALUE;

    public int maxPathSum_1_1(TreeNode root) {
        dfs_1(root);
        return res;
    }

    private int getMax(TreeNode root) {
        if (root == null) return 0;
        int left = getMax(root.left);
        int right = getMax(root.right);
        int path = root.val + Math.max(left, right);
        return Math.max(0, path);
    }

    private void dfs_1(TreeNode root) {
        if (root == null) return;
        int left = getMax(root.left);
        int right = getMax(root.right);
        res = Math.max(res, root.val + left + right);
        dfs_1(root.left);
        dfs_1(root.right);
    }

    // V1-2
    // https://neetcode.io/problems/binary-tree-maximum-path-sum
    // IDEA: DFS (OPTIMAL)

    public int maxPathSum_1_2(TreeNode root) {
        int[] res = new int[]{root.val};
        dfs_1_2(root, res);
        return res[0];
    }

    private int dfs_1_2(TreeNode root, int[] res) {
        if (root == null) {
            return 0;
        }

        int leftMax = Math.max(dfs_1_2(root.left, res), 0);
        int rightMax = Math.max(dfs_1_2(root.right, res), 0);

        res[0] = Math.max(res[0], root.val + leftMax + rightMax);
        return root.val + Math.max(leftMax, rightMax);
    }

    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/binary-tree-maximum-path-sum/solutions/4586190/beat-100-1/
    int max = Integer.MIN_VALUE;

    public int maxPath_2(TreeNode root) {

        if(root == null) return 0;

        int value = root.val;

        int left_sum = Math.max(maxPath_2(root.left),0);
        int right_sum = Math.max(maxPath_2(root.right),0);

        max = Math.max(max, left_sum + right_sum + value);

        return Math.max(left_sum, right_sum) + value;
    }

    public int maxPathSum_2(TreeNode root) {

        maxPath_2(root);
        return max;
    }

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/binary-tree-maximum-path-sum/solutions/4538150/simple-java-solution-dfs/
    class INT {
        int val;
    }
    public int maxPathSum_3(TreeNode root) {
        INT maxPathLen = new INT();
        maxPathLen.val = Integer.MIN_VALUE;
        dfs_2(root, maxPathLen);
        return maxPathLen.val;
    }

    int dfs_2(TreeNode root, INT maxLen) {
        if(root == null)
            return 0;
        int left = Math.max(dfs_2(root.left, maxLen), 0);
        int right = Math.max(dfs_2(root.right, maxLen), 0);
        maxLen.val = Math.max(maxLen.val, root.val + left + right);
        return root.val + Math.max(left, right);
    }



}
