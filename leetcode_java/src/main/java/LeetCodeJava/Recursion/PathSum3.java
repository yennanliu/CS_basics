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
    // map: { path_sum : count }
    Map<Long, Integer> pathCntMap = new HashMap<>();

    public int pathSum_0_1(TreeNode root, int targetSum) {
        if (root == null)
            return 0;

        getPathHelper(root, new ArrayList<>());
        return pathCntMap.getOrDefault((long) targetSum, 0);
    }

    private void getPathHelper(TreeNode node, List<Long> cur) {
        if (node == null)
            return;

        // add current node
        cur.add((long) node.val);

        // update all sub-path sums ending at this node
        long sum = 0;
        for (int i = cur.size() - 1; i >= 0; i--) {
            sum += cur.get(i);
            pathCntMap.put(sum, pathCntMap.getOrDefault(sum, 0) + 1);
        }

        // DFS
        getPathHelper(node.left, cur);
        getPathHelper(node.right, cur);

        // backtrack
        cur.remove(cur.size() - 1);
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
