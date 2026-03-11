package LeetCodeJava.DFS;

// https://leetcode.com/problems/path-sum/
/**
 * 112. Path Sum
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path such that adding up all the values along the path equals targetSum.
 *
 * A leaf is a node with no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
 * Output: true
 * Explanation: The root-to-leaf path with the target sum is shown.
 * Example 2:
 *
 *
 * Input: root = [1,2,3], targetSum = 5
 * Output: false
 * Explanation: There are two root-to-leaf paths in the tree:
 * (1 --> 2): The sum is 3.
 * (1 --> 3): The sum is 4.
 * There is no root-to-leaf path with sum = 5.
 * Example 3:
 *
 * Input: root = [], targetSum = 0
 * Output: false
 * Explanation: Since the tree is empty, there are no root-to-leaf paths.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 5000].
 * -1000 <= Node.val <= 1000
 * -1000 <= targetSum <= 1000
 *
 */

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PathSum {



    // V0
    // IDEA: HASHMAP + DFS (pre order)
    /**  NOTE !!!
     *
     *  the reason we don't need to do backtrack on `numSum` is that:
     *    ->  numSum is `primitive` data type in java,
     *        so it is NOT `accumulated
     *        its value` to next recursion.
     *
     * --------
     *
     *
     *  Explanation (gemini):
     *
     *  Exactly! You are thinking along the right lines. Let me explain carefully.
     *
     * ---
     *
     * ### **Why we don’t need to backtrack `curSum` (or `numSum`) in Java**
     *
     * 1. In Java, **primitive types** (`int`, `long`, `double`, etc.)
     *    are **passed by value**, not by reference.
     *
     * 2. This means **each recursive call gets its own copy of
     *    `curSum`**. Any modifications inside the recursion
     *    **do not affect the parent call**.
     *
     * In your code:
     *
     * ```java
     * private void getPathHelper(TreeNode root, Integer curSum) {
     *     int newSum = curSum + root.val;
     *     getPathHelper(root.left, newSum);
     *     getPathHelper(root.right, newSum);
     * }
     * ```
     *
     * * `curSum` is **copied** for each recursion.
     * * `newSum` is a new variable in that stack frame.
     * * When the function returns, the parent call’s `curSum` is unchanged.
     *
     * ✅ Therefore, **no backtracking is needed**.
     * Each path maintains its own sum independently.
     *
     * ---
     *
     * ### **Contrast with using a global variable**
     *
     * If you used a global variable like:
     *
     * ```java
     * int pathSum = 0;
     * ```
     *
     * Then every recursion modifies the **same shared variable**.
     * this case, you **must backtrack** (`pathSum -= root.val`)
     * when returning from recursion, otherwise the sum accumulates
     * incorrectly across different paths.
     *
     * ---
     *
     * ### **Your HashMap + DFS approach**
     *
     * Your `hasPathSum_0_1` works correctly because:
     *
     * 1. `curSum` is local to each recursion.
     * 2. You calculate `newSum = curSum + root.val` in each recursive call.
     * 3. Leaf nodes store `newSum` into the `pathSumMap`.
     * 4. No backtracking is required.
     *
     * 💡 Extra tip: You could simplify it even more by *
     * *not using a HashMap**, and just return `true/false`
     * directly when a leaf equals `targetSum`. Using a map is overkill
     * for LC 112, but it’s fine if you want to record all path sums.
     *
     */
    Map<Integer, Integer> pathSumMap = new HashMap<>();
    public boolean hasPathSum(TreeNode root, int targetSum) {
        // edge
        if (root == null) {
            return false;
        }
        if (root.left == null && root.right == null) {
            return root.val == targetSum;
        }

        //System.out.println(">>> (before op) pathSumMap = " + pathSumMap);
        // post order traverse: left -> right -> root
        getPathHelper(root, 0);
        //System.out.println(">>> (after op) pathSumMap = " + pathSumMap);

        return pathSumMap.containsValue(targetSum);
    }

    private void getPathHelper(TreeNode root, Integer curSum) {
        if (root == null) {
            return;
        }

        int newSum = curSum + root.val;
        /**
         *  NOTE !!!
         *
         *  ONLY add to map if `has NO children`
         *
         */
        if (root.left == null && root.right == null) {
            pathSumMap.put(newSum, newSum);
        }

        /**
         *  NOTE !!!
         *
         *  recursively calculate sub left, right node depth
         */
        getPathHelper(root.left, newSum);
        getPathHelper(root.right, newSum);
    }


    // V0-1
    // IDEA: pre-order DFS + targetSum deduction
    /**
     * time = O(N)
     * space = O(H)
     */
    /** NOTE !!! pre-order DFS */
    public boolean hasPathSum_0_1(TreeNode root, int targetSum) {

        if (root == null){
            return false;
        }

        if (root.left == null && root.right == null){
            return root.val == targetSum;
        }

        return checkSum(root, targetSum);
    }

    private Boolean checkSum(TreeNode root, int targetSum){

        if (root == null){
            return false;
        }

        /** NOTE !!! we deduct root.val */
        targetSum -= root.val;

        /** NOTE !!! pre-order DFS */
        if (root.left == null && root.right == null){
            return targetSum == 0;
        }

        /** NOTE !!! below trick */
        return checkSum(root.left ,targetSum) || checkSum(root.right ,targetSum);
    }


    // V0-2
    // IDEA: PRE-ORDER DFS (GEMINI)
    private int curSum = 0;

    public boolean hasPathSum_0_2(TreeNode root, int targetSum) {
        // 1. Base Case: An empty tree has no path
        if (root == null) {
            return false;
        }

        // 2. Pre-Order: Process the current node
        curSum += root.val;

        // 3. Leaf Check
        if (root.left == null && root.right == null) {
            if (curSum == targetSum) {
                /** NOTE !!! */
                // IMPORTANT: We found it, but we still need to backtrack
                // before returning true to keep curSum clean for other branches
                curSum -= root.val;
                return true;
            }
        }

        // 4. Recursive Step: Capture the result from children
        // If the LEFT side finds it, we stop and return true
        if (hasPathSum_0_2(root.left, targetSum)) {
            /** NOTE !!! */
            curSum -= root.val; // Backtrack before returning!
            return true;
        }

        // If the RIGHT side finds it, we stop and return true
        if (hasPathSum_0_2(root.right, targetSum)) {
            /** NOTE !!! */
            curSum -= root.val; // Backtrack before returning!
            return true;
        }

        /** NOTE !!! */
        // 5. Backtrack: If neither side found it, undo the addition for this node
        curSum -= root.val;
        return false;
    }


    // V1
    // https://leetcode.com/problems/path-sum/editorial/
    // IDEA : RECURSION
    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean hasPathSum_2(TreeNode root, int sum) {
        if (root == null)
            return false;

        sum -= root.val;
        if ((root.left == null) && (root.right == null))
            return (sum == 0);
        return hasPathSum_2(root.left, sum) || hasPathSum_2(root.right, sum);
    }

    // V2
    // https://leetcode.com/problems/path-sum/editorial/
    // IDEA : Iterations
    /**
     * time = O(N)
     * space = O(H)
     */
    public boolean hasPathSum_3(TreeNode root, int sum) {
        if (root == null)
            return false;

        LinkedList<TreeNode> node_stack = new LinkedList();
        LinkedList<Integer> sum_stack = new LinkedList();
        node_stack.add(root);
        sum_stack.add(sum - root.val);

        TreeNode node;
        int curr_sum;
        while ( !node_stack.isEmpty() ) {
            node = node_stack.pollLast();
            curr_sum = sum_stack.pollLast();
            if ((node.right == null) && (node.left == null) && (curr_sum == 0))
                return true;

            if (node.right != null) {
                node_stack.add(node.right);
                sum_stack.add(curr_sum - node.right.val);
            }
            if (node.left != null) {
                node_stack.add(node.left);
                sum_stack.add(curr_sum - node.left.val);
            }
        }
        return false;
    }




}
