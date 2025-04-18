package LeetCodeJava.Recursion;

// https://leetcode.com/problems/house-robber-iii/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 337. House Robber III
 * Medium
 * Topics
 * Companies
 * The thief has found himself a new place for his thievery again. There is only one entrance to this area, called root.
 *
 * Besides the root, each house has one and only one parent house. After a tour, the smart thief realized that all houses in this place form a binary tree. It will automatically contact the police if two directly-linked houses were broken into on the same night.
 *
 * Given the root of the binary tree, return the maximum amount of money the thief can rob without alerting the police.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [3,2,3,null,3,null,1]
 * Output: 7
 * Explanation: Maximum amount of money the thief can rob = 3 + 3 + 1 = 7.
 * Example 2:
 *
 *
 * Input: root = [3,4,5,1,3,null,1]
 * Output: 9
 * Explanation: Maximum amount of money the thief can rob = 4 + 5 = 9.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 104].
 * 0 <= Node.val <= 104
 *
 */
public class HouseRobber3 {

  // V0
  //    public int rob(TreeNode root) {
  //
  //    }

  // V0-1
  // IDEA : DFS + DP (gpt)
  /**
   *  At every node, we decide:
   * 	•	If we rob this node, we cannot rob its children.
   * 	•	If we don’t rob this node, we can rob its children.
   *
   * We’ll return two values for each subtree:
   * 	•	res[0]: max money if we don’t rob this node
   * 	•	res[1]: max money if we do rob this node
   */
  /**
   *   Example:
   *
   *    For root = [3,2,3,null,3,null,1]:
   *
   *          3
   *        / \
   *       2   3
   *        \    \
   *         3    1
   *
   *   	•	Rob 3 (root) → Can’t rob 2 or 3 → Total = 3 + 3 + 1 = 7
   * 	•	Don’t rob 3 (root) → Rob 2 and/or 3
   *
   *  -> The DP traversal picks the best at each level.
   *
   */
  public int rob_0_1(TreeNode root) {
        int[] result = dfs_0_1(root);
        return Math.max(result[0], result[1]);
    }

    private int[] dfs_0_1(TreeNode node) {
        if (node == null) {
            return new int[] { 0, 0 }; // {notRobbed, robbed}
        }

        int[] left = dfs_0_1(node.left);
        int[] right = dfs_0_1(node.right);

        int notRobbed = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        int robbed = node.val + left[0] + right[0];

        return new int[] { notRobbed, robbed };
    }

    // V1
    // https://youtu.be/nHR8ytpzz7c?si=7y46QM-wwMWAmn8b
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0337-house-robber-iii.java
    public int rob_1(TreeNode root) {
        // NOTE !!! we call helper func below
        int[] ans = dfs(root);
        return Math.max(ans[0], ans[1]);
    }

    // helper func
    public int[] dfs(TreeNode root){
        if(root == null) return new int[2];

        int []left = dfs(root.left);
        int []right = dfs(root.right);

        int []res = new int[2];

        res[0] = left[1] + right[1] + root.val; //with Root
        res[1] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]); //without Root

        return res;
    }

    // V2
    // https://leetcode.com/problems/house-robber-iii/solutions/79363/easy-understanding-solution-with-dfs-by-1tqxw/
    public int rob_2(TreeNode root) {
        int[] num = dfs_2(root);
        return Math.max(num[0], num[1]);
    }

    private int[] dfs_2(TreeNode x) {
        if (x == null)
            return new int[2];
        int[] left = dfs_2(x.left);
        int[] right = dfs_2(x.right);
        int[] res = new int[2];
        res[0] = left[1] + right[1] + x.val;
        res[1] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        return res;
    }

    // V3-1
    // IDEA: RECURSIVE (TLE)
    // https://leetcode.com/problems/house-robber-iii/solutions/1611881/java-3-approaches-recursion-dp-greedy-de-tvu9/
    public int rob_3_1(TreeNode root) {
        if (root == null)
            return 0;

        int ans = 0;

        // max value from left grandchildren
        if (root.left != null) {
            ans += rob_3_1(root.left.left) + rob_3_1(root.left.right);
        }

        // max value from right grandchildren
        if (root.right != null) {
            ans += rob_3_1(root.right.left) + rob_3_1(root.right.right);
        }

        return Math.max(ans + root.val, rob_3_1(root.left) + rob_3_1(root.right)); //(Case1,Case2)
    }


    // V3-2
    // IDEA: Rucrsion Using HashMap
    // https://leetcode.com/problems/house-robber-iii/solutions/1611881/java-3-approaches-recursion-dp-greedy-de-tvu9/
    public int rob_3_2(TreeNode root) {
        return rob_3_2(root, new HashMap<>());
    }

    public int rob_3_2(TreeNode root, Map<TreeNode, Integer> map) {

        if (root == null)
            return 0;

        if (map.containsKey(root))
            return map.get(root);

        int ans = 0;

        if (root.left != null) {
            ans += rob_3_2(root.left.left, map) + rob_3_2(root.left.right, map);
        }

        if (root.right != null) {
            ans += rob_3_2(root.right.left, map) + rob_3_2(root.right.right, map);
        }

        ans = Math.max(ans + root.val, rob_3_2(root.left, map) + rob_3_2(root.right, map));
        map.put(root, ans);

        return ans;
    }

    // V3-3
    // IDEA: Greedy Approach
    // https://leetcode.com/problems/house-robber-iii/solutions/1611881/java-3-approaches-recursion-dp-greedy-de-tvu9/
    public int rob_3_3(TreeNode root) {
        int ans[] = robHouse(root);
        return Math.max(ans[0], ans[1]);
    }

    public int[] robHouse(TreeNode root) {
        if (root == null) {
            return new int[2];
        }

        int left[] = robHouse(root.left);
        int right[] = robHouse(root.right);

        int ans[] = new int[2];

        ans[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        ans[1] = root.val + left[0] + right[0];

        return ans;
    }

    // V4
    // https://leetcode.com/problems/house-robber-iii/solutions/1510119/java-from-recursive-tle-to-dp-memoizatio-4wlx/

}
