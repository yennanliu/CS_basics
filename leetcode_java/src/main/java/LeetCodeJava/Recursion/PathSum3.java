package LeetCodeJava.Recursion;

// https://leetcode.com/problems/path-sum-iii/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 437. Path Sum III
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree and an integer targetSum, return the number of paths where the sum of the values along the path equals targetSum.
 *
 * The path does not need to start or end at the root or a leaf, but it must go downwards (i.e., traveling only from parent nodes to child nodes).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
 * Output: 3
 * Explanation: The paths that sum to 8 are shown.
 * Example 2:
 *
 * Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * Output: 3
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 1000].
 * -109 <= Node.val <= 109
 * -1000 <= targetSum <= 1000
 *
 */
public class PathSum3 {

    // V0
//    public int pathSum(TreeNode root, int targetSum) {
//
//    }

    // V0-1
    // IDEA: DFS + HASHMAP (fixed by gpt)
    /**
     * 	 IDEA:
     *
     * 	•	cur stores the current path from root to node.
     * 	•	pathCntMap counts all sub-path sums ending at each node.
     * 	•	Using long prevents overflow for very large values.
     * 	•	Backtracking ensures paths are independent for different branches.
     * 	•	targetSum is checked at the end in pathCntMap to get the final count.
     */
    // map: { path_sum : count }
    /**
     *
     * 	•	What it is: A Map that stores all path sums
     * 	                encountered as keys and the number of
     * 	                times each sum occurs as the value.
     *
     * 	•	Why Long: Because path sums can exceed Integer.MAX_VALUE
     * 	             (2,147,483,647). Using long prevents integer overflow.
     */
    Map<Long, Integer> pathCntMap = new HashMap<>();
    public int pathSum_0_1(TreeNode root, int targetSum) {
        if (root == null)
            return 0;

        // 	•	Start a DFS traversal from the root.
        //	•	Pass an empty ArrayList<Long> called cur to track
        //    	the current path from root to current node.
        getPathHelper(root, new ArrayList<>());
        return pathCntMap.getOrDefault((long) targetSum, 0);
    }

    // 	•	Helper function for DFS traversal.
    //	•	Base case: if the current node is null, stop recursion.
    private void getPathHelper(TreeNode node, List<Long> cur) {
        if (node == null)
            return;

        //	•	Add the current node’s value to the current path.
        //	•	Cast to long to prevent overflow when summing large number
        // add current node
        cur.add((long) node.val);

        /** NOTE !!!
         *
         *  we use below track to `get ALL SUB PATHS `ENDING` AT THIS NODE.
         *  e.g. if cache = [1,2,3]
         *   -> then below code calculates path sum over
         *     [1]
         *     [1,2]
         *     [1,2,3]
         *
         *     and update the path sum cnt to map as well
         */
        /**
         *
         * 	This is the core logic:
         * 	    1.
         * 	        - We calculate the sum of `ALL SUB PATHS ENDING AT THIS NODE`, `
         * 	          not just from the root.
         * 	        - For example, if the path is [1, 2, 3]
         * 	          and current node is 3, we calculate 3, 2+3=5, 1+2+3=6.
         *  	2.
         *          - sum += cur.get(i) keeps a running sum from the current node backwards.
         * 	    3.
         * 	        - pathCntMap.put(sum, pathCntMap.getOrDefault(sum, 0) + 1)
         * 	          stores the sum and increments its count.
         */
        // update all sub-path sums ending at this node
        long sum = 0;
        for (int i = cur.size() - 1; i >= 0; i--) {
            sum += cur.get(i);
            pathCntMap.put(sum, pathCntMap.getOrDefault(sum, 0) + 1);
        }

        // DFS
        //	•	Continue DFS recursively for left and right child nodes.
        //	•	cur already contains the path from root to current node,
        //    	so children will extend it.
        getPathHelper(node.left, cur);
        getPathHelper(node.right, cur);

        /**
         *  NOTE !!!  BACKTRACK (undo last op)
         *
         *  •	After visiting both children, backtrack: remove the current node from cur.
         * 	•	This ensures cur only contains the path for the current DFS branch.
         */
        // backtrack
        cur.remove(cur.size() - 1);
    }


    // V0-2
    // IDEA: DFS + HASHMAP (fixed by gemini)
    // Global counter for the number of valid paths found.
    private int count = 0;
    // Map to store the frequency of prefix sums encountered from the root down to the current node.
    // Key: Prefix Sum | Value: Number of times this sum has been seen.
    private Map<Long, Integer> prefixSumCounts = new HashMap<>();
    public int pathSum_0_2(TreeNode root, int targetSum) {
        if (root == null) {
            return 0;
        }

        // Initialize the map with 0 sum seen once (the path before the root).
        prefixSumCounts.put(0L, 1);

        // Start the recursive DFS traversal.
        dfs(root, 0L, targetSum);

        return count;
    }

    /**
     * Performs DFS to find paths.
     * @param node The current node.
     * @param currentSum The cumulative sum from the tree root to the current node (inclusive).
     * @param targetSum The target sum we are looking for.
     */
    private void dfs(TreeNode node, long currentSum, int targetSum) {
        if (node == null) {
            return;
        }

        // 1. Calculate the new prefix sum.
        currentSum += node.val;

        // 2. Check for paths ending at the current node.
        // If (currentSum - targetSum) exists in the map, it means there is a segment
        // starting from an ancestor that, when added to the segment from ancestor+1 to 'node', equals targetSum.
        long requiredAncestorSum = currentSum - targetSum;
        if (prefixSumCounts.containsKey(requiredAncestorSum)) {
            count += prefixSumCounts.get(requiredAncestorSum);
        }

        // 3. Update the map for the current prefix sum.
        // We do this BEFORE the recursive calls to ensure the children can use this sum.
        prefixSumCounts.put(currentSum, prefixSumCounts.getOrDefault(currentSum, 0) + 1);

        // 4. Recurse on children.
        dfs(node.left, currentSum, targetSum);
        dfs(node.right, currentSum, targetSum);

        // 5. Backtrack: Remove the current prefix sum from the map.
        // This ensures that prefix sums from one branch don't interfere with an unrelated branch.
        prefixSumCounts.put(currentSum, prefixSumCounts.get(currentSum) - 1);
        if (prefixSumCounts.get(currentSum) == 0) {
            prefixSumCounts.remove(currentSum);
        }
    }

    // V1
    // https://leetcode.ca/2017-02-09-437-Path-Sum-III/
    // IDEA: DFS
    private Map<Long, Integer> cnt = new HashMap<>();
    private int targetSum;

    public int pathSum_1(TreeNode root, int targetSum) {
        cnt.put(0L, 1);
        this.targetSum = targetSum;
        return dfs(root, 0);
    }

    private int dfs(TreeNode node, long s) {
        if (node == null) {
            return 0;
        }
        s += node.val;
        int ans = cnt.getOrDefault(s - targetSum, 0);
        cnt.merge(s, 1, Integer::sum);
        ans += dfs(node.left, s);
        ans += dfs(node.right, s);
        cnt.merge(s, -1, Integer::sum);
        return ans;
    }

    // V2
    // IDEA: HASHMAP + DFS
    // https://leetcode.com/problems/path-sum-iii/solutions/7182200/prefix-sum-hashmap-on-c-by-mkhlz-psr9/
    public int pathSum_2(TreeNode root, int targetSum) {
        Map<Long, Integer> prefix = new HashMap<>();
        prefix.put(0L, 1); // base
        return dfs(root, 0L, targetSum, prefix);
    }

    private int dfs(TreeNode node, long currSum, int target,
                    Map<Long, Integer> prefix) {
        if (node == null)
            return 0;
        currSum += node.val;
        int count = prefix.getOrDefault(currSum - target, 0);
        prefix.put(currSum, prefix.getOrDefault(currSum, 0) + 1);
        count += dfs(node.left, currSum, target, prefix);
        count += dfs(node.right, currSum, target, prefix);
        prefix.put(currSum, prefix.get(currSum) - 1); // backtrack
        return count;
    }

    // V3



}
