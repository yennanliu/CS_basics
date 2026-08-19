package LeetCodeJava.DFS;

// https://leetcode.com/problems/weighted-sum-of-a-tree/

/**
 *  4015. Weighted Sum of a Tree
 *  Medium
 *
 *  You are given an integer array parent of length n representing a rooted tree with
 *  nodes labeled from 0 to n - 1. The tree is rooted at node 0, so parent[0] = -1.
 *  For each node i where 1 <= i <= n - 1, parent[i] denotes the parent of node i.
 *
 *  You are also given an integer array nums of length n, where nums[i] is the value of
 *  node i.
 *
 *  The weight of a node i at depth d is nums[i] * (h - d + 1), where h is the height of
 *  the tree. The depth of a node is the number of nodes on the path from the root to
 *  that node inclusive (root has depth 1); the height is the maximum depth.
 *
 *  Return the sum of the weights of all nodes in the tree.
 *
 *  Example 1:
 *    Input: parent = [-1,0,0,0,2,2], nums = [5,2,3,1,4,6]
 *    Output: 37
 *
 *  Example 2:
 *    Input: parent = [-1,0,1,2], nums = [1,2,3,4]
 *    Output: 20
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    n == parent.length == nums.length
 *    parent[0] == -1
 *    0 <= parent[i] <= n - 1 for all i in [1, n - 1]
 *    1 <= nums[i] <= 10^6
 */
public class WeightedSumOfATree {

    // V0
    // IDEA: memoized depth walk up the parent chain (no children lists needed):
    //       depth(i) = depth(parent[i]) + 1, height = max depth, then sum the weights.
    //       Each node's depth is resolved once, so the total work stays linear.
    /**
     * time = O(n)
     * space = O(n)
     */
    public long weightedSum(int[] parent, int[] nums) {

        int n = parent.length;
        int[] depth = new int[n]; // 0 = not computed yet
        depth[0] = 1;

        int height = 1;
        int[] chain = new int[n]; // reusable buffer of unresolved ancestors

        for (int i = 0; i < n; i++) {
            if (depth[i] != 0) {
                height = Math.max(height, depth[i]);
                continue;
            }

            // walk up until a node with a known depth
            int len = 0;
            int cur = i;
            while (depth[cur] == 0) {
                chain[len++] = cur;
                cur = parent[cur];
            }

            int d = depth[cur];
            // fill the chain back down
            for (int k = len - 1; k >= 0; k--) {
                d++;
                depth[chain[k]] = d;
            }
            height = Math.max(height, d);
        }

        long res = 0L;
        for (int i = 0; i < n; i++) {
            res += (long) nums[i] * (height - depth[i] + 1);
        }

        return res;
    }
}
