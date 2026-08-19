package LeetCodeJava.BFS;

// https://leetcode.com/problems/path-sum-iv/

import java.util.*;

/**
 *  666. Path Sum IV
 *  Medium
 *
 *  If the depth of a tree is smaller than 5, then this tree can be represented
 *  by an array of three-digit integers. For each integer in this array:
 *
 *   - The hundreds digit represents the depth d of this node, 1 <= d <= 4.
 *   - The tens digit represents the position p of this node in the level it
 *     belongs to, 1 <= p <= 8. The position is the same as that in a full
 *     binary tree.
 *   - The units digit represents the value v of this node, 0 <= v <= 9.
 *
 *  Given an array of ascending three-digit integers nums representing a binary
 *  tree with a depth smaller than 5, return the sum of all paths from the root
 *  towards the leaves.
 *
 *  It is guaranteed that the given array represents a valid connected binary tree.
 *
 *  Example 1:
 *   Input: nums = [113,215,221]
 *   Output: 12
 *   Explanation: the tree is  3 / \ 5 1 , paths are 3+5=8 and 3+1=4, total 12.
 *
 *  Example 2:
 *   Input: nums = [113,221]
 *   Output: 4
 *
 *  Constraints:
 *   1 <= nums.length <= 15
 *   110 <= nums[i] <= 489
 *   nums represents a valid binary tree with depth less than 5.
 */
public class PathSumIV {

    // V0
    // IDEA: hash map (depth*10 + pos -> val) + DFS accumulating the running path sum
    /**
     * time = O(n)
     * space = O(n)
     */
    public int pathSum(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        // key = depth * 10 + position, value = node value
        Map<Integer, Integer> tree = new HashMap<>();
        for (int num : nums) {
            tree.put(num / 10, num % 10);
        }
        // root is always at depth 1, position 1
        return dfs(tree, 11, 0);
    }

    private int dfs(Map<Integer, Integer> tree, int key, int preSum) {
        if (!tree.containsKey(key)) {
            return 0;
        }
        int depth = key / 10;
        int pos = key % 10;
        int cur = preSum + tree.get(key);

        int leftKey = (depth + 1) * 10 + pos * 2 - 1;
        int rightKey = (depth + 1) * 10 + pos * 2;

        // leaf -> the whole root-to-leaf path sum counts once
        if (!tree.containsKey(leftKey) && !tree.containsKey(rightKey)) {
            return cur;
        }
        return dfs(tree, leftKey, cur) + dfs(tree, rightKey, cur);
    }

    // V1
    // IDEA: iterative - keep a running sum per node and a "leaf" set, since nums is ascending
    //       every node is processed after its parent
    /**
     * time = O(n)
     * space = O(n)
     */
    public int pathSum_1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        // key = depth * 10 + position, value = accumulated sum from root
        Map<Integer, Integer> sumMap = new HashMap<>();
        Set<Integer> leaves = new HashSet<>();

        for (int num : nums) {
            int key = num / 10;
            int val = num % 10;
            int depth = key / 10;
            int pos = key % 10;
            int parent = (depth - 1) * 10 + (pos + 1) / 2;

            int parentSum = sumMap.containsKey(parent) ? sumMap.get(parent) : 0;
            sumMap.put(key, parentSum + val);
            leaves.add(key);
            leaves.remove(parent);
        }

        int res = 0;
        for (Integer leaf : leaves) {
            res += sumMap.get(leaf);
        }
        return res;
    }
}
