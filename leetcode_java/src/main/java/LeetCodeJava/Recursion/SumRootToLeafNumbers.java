package LeetCodeJava.Recursion;

// https://leetcode.com/problems/sum-root-to-leaf-numbers/description/

import LeetCodeJava.DataStructure.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 *  129. Sum Root to Leaf Numbers
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You are given the root of a binary tree containing digits from 0 to 9 only.
 *
 * Each root-to-leaf path in the tree represents a number.
 *
 * For example, the root-to-leaf path 1 -> 2 -> 3 represents the number 123.
 * Return the total sum of all root-to-leaf numbers. Test cases are generated so that the answer will fit in a 32-bit integer.
 *
 * A leaf node is a node with no children.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: root = [1,2,3]
 * Output: 25
 * Explanation:
 * The root-to-leaf path 1->2 represents the number 12.
 * The root-to-leaf path 1->3 represents the number 13.
 * Therefore, sum = 12 + 13 = 25.
 * Example 2:
 *
 *
 * Input: root = [4,9,0,5,1]
 * Output: 1026
 * Explanation:
 * The root-to-leaf path 4->9->5 represents the number 495.
 * The root-to-leaf path 4->9->1 represents the number 491.
 * The root-to-leaf path 4->0 represents the number 40.
 * Therefore, sum = 495 + 491 + 40 = 1026.
 *
 *
 * Constraints:
 *
 * The number of nodes in the tree is in the range [1, 1000].
 * 0 <= Node.val <= 9
 * The depth of the tree will not exceed 10.
 *
 *
 */
public class SumRootToLeafNumbers {

    // V0
//    public int sumNumbers(TreeNode root) {
//
//    }

    // V1
    // IDEA: DFS
    // https://leetcode.ca/2016-04-07-129-Sum-Root-to-Leaf-Numbers/
    public int sumNumbers_1(TreeNode root) {
        return dfs(root, 0);
    }

    private int dfs(TreeNode root, int s) {
        if (root == null) {
            return 0;
        }
        s = s * 10 + root.val;
        if (root.left == null && root.right == null) {
            return s;
        }
        return dfs(root.left, s) + dfs(root.right, s);
    }


    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/sum-root-to-leaf-numbers/solutions/7238268/easy-solution-dfs-beats-10000-by-adhi_m_-319h/
    int sum = 0;
    public int sumNumbers_2(TreeNode root) {
        helper(root, 0);
        return sum;
    }

    void helper(TreeNode root, int path){
        if(root == null) return;
        path = path * 10 + root.val;
        if(root.left == null && root.right == null){
            sum += path;
        }
        helper(root.left, path);
        helper(root.right, path);
    }


    // V3-1
    // IDEA: DFS
    // https://leetcode.com/problems/sum-root-to-leaf-numbers/solutions/7238149/one-of-the-good-solution-to-solve-this-p-sh13/
    public int sumNumbers_3_1(TreeNode root) {
        int arr[] = new int[1];
        List<Integer> list = new ArrayList<>();
        fn(root, arr, list);
        return arr[0];
    }

    public void fn(TreeNode root, int arr[], List<Integer> list) {
        if (root == null) return;

        list.add(root.val);

        if (root.left == null && root.right == null) {
            int s = 0;
            for (int i : list) {
                s = s * 10 + i;
            }
            arr[0] += s;
        } else {
            fn(root.left, arr, list);
            fn(root.right, arr, list);
        }

        list.remove(list.size() - 1);
    }

    // V3-2
    // IDEA: OPTIMIZED DFS
    // https://leetcode.com/problems/sum-root-to-leaf-numbers/solutions/7238149/one-of-the-good-solution-to-solve-this-p-sh13/
    public int sumNumbers_3_2(TreeNode root) {
        return dfs_3_2(root, 0);
    }

    private int dfs_3_2(TreeNode root, int num) {
        if (root == null) return 0;

        num = num * 10 + root.val;

        if (root.left == null && root.right == null) {
            return num;
        }

        return dfs_3_2(root.left, num) + dfs_3_2(root.right, num);
    }


}
