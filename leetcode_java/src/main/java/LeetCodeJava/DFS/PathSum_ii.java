package LeetCodeJava.DFS;

// https://leetcode.com/problems/path-sum-ii/
/**
 *
 * Test Result
 * 113. Path Sum II
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given the root of a binary tree and an integer targetSum, return all root-to-leaf paths where the sum of the node values in the path equals targetSum. Each path should be returned as a list of the node values, not node references.
 *
 * A root-to-leaf path is a path starting from the root and ending at any leaf node. A leaf is a node with no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [5,4,8,11,null,13,4,7,2,null,null,5,1], targetSum = 22
 * Output: [[5,4,11,2],[5,8,4,5]]
 * Explanation: There are two paths whose sum equals targetSum:
 * 5 + 4 + 11 + 2 = 22
 * 5 + 8 + 4 + 5 = 22
 * Example 2:
 *
 *
 * Input: root = [1,2,3], targetSum = 5
 * Output: []
 * Example 3:
 *
 * Input: root = [1,2], targetSum = 0
 * Output: []
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [0, 5000].
 * -1000 <= Node.val <= 1000
 * -1000 <= targetSum <= 1000
 */
import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathSum_ii {

    /** NOTE !!!
     *
     *  why we need `pre-order` traverse (TOP -> DOWN)
     *  for this LC (but NOT `post-order`
     *
     *
     *  also note that this is NOT a `prefix sum` LC problem.
     *
     *
     *  | Traversal  | Use case                                  |
     * | ---------- | ----------------------------------------- |
     * | Pre-order  | build path top-down                       |
     * | Post-order | compute subtree results (height, sum, DP) |
     * | In-order   | BST problems                              |
     *
     *
     *  -> (gpt)
     *
     *  Yes — your intuition is mostly correct.
     *  For LeetCode 113 Path Sum II
     *  we typically use **pre-order (top-down DFS)**.
     *  Let's clarify **why**.
     *
     * ---
     *
     * # 1️⃣ Why Pre-Order (Top → Down)?
     *
     * In this problem we must build the **path from root → leaf**.
     *
     * At each step we need to:
     *
     * 1. **Add the current node to the path**
     * 2. **Update the running sum**
     * 3. **Check if we reached a leaf**
     * 4. Continue exploring children
     * 5. **Backtrack**
     *
     * That naturally matches **pre-order traversal**:
     *
     * ```
     * process node
     * → go left
     * → go right
     * ```
     *
     * Example tree:
     *
     * ```
     *       5
     *      / \
     *     4   8
     *    /
     *  11
     * ```
     *
     * DFS flow:
     *
     * ```
     * 5
     * 5 -> 4
     * 5 -> 4 -> 11
     * ```
     *
     * We build the **path progressively downward**.
     *
     * ---
     *
     * # 2️⃣ Why Post-Order Doesn't Fit Well
     *
     * Post-order order:
     *
     * ```
     * left
     * right
     * node
     * ```
     *
     * That means the **node is processed after its children**.
     *
     * But the path we need is:
     *
     * ```
     * root → ... → leaf
     * ```
     *
     * If using post-order:
     *
     * * You visit **children first**
     * * But you **haven't added the parent to the path yet**
     *
     * Example problem:
     *
     * ```
     * root = 5
     * child = 4
     * leaf = 11
     * ```
     *
     * Post-order visits:
     *
     * ```
     * 11
     * 4
     * 5
     * ```
     *
     * But the required path must be:
     *
     * ```
     * 5 → 4 → 11
     * ```
     *
     * So you'd need to **reconstruct the path backwards**, which becomes messy.
     *
     * ---
     *
     * # 3️⃣ Why We Check Leaf After Adding Node
     *
     * Typical template:
     *
     * ```java
     * path.add(node.val);
     * remain -= node.val;
     *
     * if (leaf && remain == 0)
     *     save path
     * ```
     *
     * Because we want to verify:
     *
     * ```
     * sum(root → leaf) == target
     * ```
     *
     * We only know that **after including the leaf value**.
     *
     * ---
     *
     * # 4️⃣ Standard DFS + Backtracking Pattern
     *
     * This pattern appears in many tree problems:
     *
     * ```
     * process node
     * add to path
     *
     * if leaf:
     *     check condition
     *
     * dfs(left)
     * dfs(right)
     *
     * remove from path  // backtrack
     * ```
     *
     * ---
     *
     * # 5️⃣ Visual Execution
     *
     * Example:
     *
     * ```
     * target = 22
     *
     *       5
     *      / \
     *     4   8
     *    /
     *  11
     * ```
     *
     * Execution:
     *
     * ```
     * path=[5]
     * path=[5,4]
     * path=[5,4,11]
     * leaf && sum=22 → save
     * ```
     *
     * Then:
     *
     * ```
     * backtrack → path=[5,4]
     * backtrack → path=[5]
     * ```
     *
     * ---
     *
     * ✅ **Key rule to remember**
     *
     * | Traversal  | Use case                                  |
     * | ---------- | ----------------------------------------- |
     * | Pre-order  | build path top-down                       |
     * | Post-order | compute subtree results (height, sum, DP) |
     * | In-order   | BST problems                              |
     *
     * ---
     *
     * ⭐ **Interview trick:**
     * If a problem asks for **"root → leaf path"**,
     * it is **almost always pre-order DFS + backtracking**.
     *
     */

    // V0
    // IDEA : DFS (pre-order traverse) + backtracking
    // NOTE !!! we have res attr, so can use this.res collect result
    private List<List<Integer>> res = new ArrayList<>();

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {

        if (root == null){
            return this.res;
        }

        List<Integer> cur = new ArrayList<>();
        getPath(root, cur, targetSum);
        return this.res;
    }

     private void getPath(TreeNode root, List<Integer> cur, int targetSum){

        // return directly if root is null (not possible to go further, so just quit directly)
        if (root == null){
            return;
        }

        // NOTE !!! we add val to cache here instead of while calling method recursively ( e.g. getPath(root.left, cur, targetSum - root.val))
        //          -> so we just need to backtrack (cancel last operation) once (e.g. cur.remove(cur.size() - 1);)
        //          -> please check V0' for example with backtrack in recursively step
        cur.add(root.val);

        if (root.left == null && root.right == null && targetSum == root.val){
            this.res.add(new ArrayList<>(cur));
        }else{
            // NOTE !!! we update targetSum here (e.g. targetSum - root.val)
            getPath(root.left, cur, targetSum - root.val);
            getPath(root.right, cur, targetSum - root.val);
        }

         // NOTE !!! we do backtrack here (cancel previous adding to cur)
         cur.remove(cur.size() - 1);
    }


    // V0-0-1
    // IDEA: DFS (pre-order traverse) + backtrack (gpt)
    // IDEA 1) DFS + backtracking
    List<List<Integer>> nodeList = new ArrayList<>();

    public List<List<Integer>> pathSum_0_0_1(TreeNode root, int targetSum) {

        if (root == null) {
            return nodeList;
        }

        pathSumHelper(root, targetSum, 0, new ArrayList<>());

        return nodeList;
    }

    private void pathSumHelper(TreeNode root, int targetSum, int curSum, List<Integer> cache) {

        if (root == null) {
            return;
        }

        /** NOTE !!!
         *
         *  DFS (pre-order traverse) !!!!
         *
         *  (NOT post-order)
         */
        // add current node
        curSum += root.val;
        cache.add(root.val);

        // check leaf
        if (root.left == null && root.right == null && curSum == targetSum) {
            nodeList.add(new ArrayList<>(cache));
        }

        // DFS
        pathSumHelper(root.left, targetSum, curSum, cache);
        pathSumHelper(root.right, targetSum, curSum, cache);

        // backtrack
        cache.remove(cache.size() - 1);
    }



    // V0-1
    // IDEA: DFS + MAP record path + backtrack (fixed by gpt)
    /**
     *  NOTE !!!
     *
     *  we design map as {sum: list},
     *  so can handle the `duplicated path` cases.
     *  e.g. different nodes (path) but with the same total path sum
     *
     *  -> (in such case) we have to return all paths as the result
     *
     */
    // Reverse map: sum -> list of paths
    Map<Integer, List<List<Integer>>> sumToPathsMap = new HashMap<>();

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<List<Integer>> pathSum_0_1(TreeNode root, int targetSum) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null)
            return res;

        // DFS
        getPathHelper(root, 0, new ArrayList<>());

        //System.out.println(">>> sumToPathsMap = " + sumToPathsMap);

        // retrieve paths for targetSum
        if (sumToPathsMap.containsKey(targetSum)) {
            res.addAll(sumToPathsMap.get(targetSum));
        }

        return res;
    }

    private void getPathHelper(TreeNode root, int curSum, List<Integer> nodes) {
        if (root == null)
            return;

        nodes.add(root.val);
        int newSum = curSum + root.val;

        /**
         *  NOTE !!!
         *
         *  ONLY add to map if leaf
         */
        if (root.left == null && root.right == null) {
            // initialize list if needed
            // V1
//            sumToPathsMap.computeIfAbsent(newSum, k -> new ArrayList<>())
//                    .add(new ArrayList<>(nodes)); // copy list

            // V2
            List<List<Integer>> _list = new ArrayList<>();
            if (sumToPathsMap.containsKey(newSum)) {
                _list = sumToPathsMap.get(newSum);
            }
            // Add the current path (make a copy)
            _list.add(new ArrayList<>(nodes));
            // Put it back into the map (important if it was newly created)
            sumToPathsMap.put(newSum, _list);
        }

        getPathHelper(root.left, newSum, nodes);
        getPathHelper(root.right, newSum, nodes);

        /**
         *  NOTE !!!
         *
         *   need to `undo` the `add node` op
         *   e.g. backtrack
         *
         *   -> since we DON'T want to re-calculate the node added in the other branch
         */
        // backtrack
        nodes.remove(nodes.size() - 1);
    }


    // V0-2
    // IDEA : DFS + backtracking V2
    private List<List<Integer>> res2 = new ArrayList<>();

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<List<Integer>> pathSum_0_2(TreeNode root, int targetSum) {

        if (root == null){
            return this.res2;
        }

        List<Integer> cur = new ArrayList<>();
        getPath_2(root, cur, targetSum);
        return this.res2;
    }

    private void getPath_2(TreeNode root, List<Integer> cur, int targetSum){

        if (root == null){
            return;
        }

        //cur.add(root.val);

        if (root.left == null && root.right == null && targetSum == root.val){
            // if condition meet, we add last val to cur
            cur.add(root.val);
            this.res2.add(new ArrayList<>(cur));
            // and of course, we need to cancel previous adding
            cur.remove(cur.size() - 1);
        }else{
            // NOTE !!! we add val to cur before recursive call, then we cancel it
            cur.add(root.val);
            getPath(root.left, cur, targetSum - root.val);
            // cancel last adding
            cur.remove(cur.size() - 1);
            // NOTE !!! we add val to cur before recursive call, then we cancel it
            cur.add(root.val);
            getPath(root.right, cur, targetSum - root.val);
            // cancel last adding
            cur.remove(cur.size() - 1);
        }

        //cur.remove(cur.size() - 1);
    }


    // V1
    // IDEA : DFS (RECURSION)
    // https://leetcode.com/problems/path-sum-ii/editorial/
    private void recurseTree(TreeNode node, int remainingSum, List<Integer> pathNodes, List<List<Integer>> pathsList) {

        if (node == null) {
            return;
        }

        // Add the current node to the path's list
        pathNodes.add(node.val);

        // Check if the current node is a leaf and also, if it
        // equals our remaining sum. If it does, we add the path to
        // our list of paths
        if (remainingSum == node.val && node.left == null && node.right == null) {
            pathsList.add(new ArrayList<>(pathNodes));
        } else {

            // Else, we will recurse on the left and the right children
            this.recurseTree(node.left, remainingSum - node.val, pathNodes, pathsList);
            this.recurseTree(node.right, remainingSum - node.val, pathNodes, pathsList);
        }

        // We need to pop the node once we are done processing ALL of it's
        // subtrees.
        pathNodes.remove(pathNodes.size() - 1);
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public List<List<Integer>> pathSum_3(TreeNode root, int sum) {
        List<List<Integer>> pathsList = new ArrayList<List<Integer>>();
        List<Integer> pathNodes = new ArrayList<Integer>();
        this.recurseTree(root, sum, pathNodes, pathsList);
        return pathsList;
    }





}
