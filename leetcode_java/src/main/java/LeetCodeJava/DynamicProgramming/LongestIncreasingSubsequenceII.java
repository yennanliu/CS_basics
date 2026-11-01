package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-increasing-subsequence-ii/description/

/**
 * 2407. Longest Increasing Subsequence II
 * Hard
 *
 * You are given an integer array nums and an integer k.
 *
 * Find the longest subsequence of nums that meets the following requirements:
 *
 * The subsequence is strictly increasing and
 * The difference between adjacent elements in the subsequence is at most k.
 *
 * Return the length of the longest subsequence that meets the requirements.
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,2,1,4,3,4,5,8,15], k = 3
 * Output: 5
 * Explanation:
 * The longest subsequence that meets the requirements is [1,3,4,5,8].
 * The subsequence has a length of 5, so we return 5.
 * Note that the subsequence [1,3,4,5,8,15] does not meet the requirements because 15 - 8 = 7 is larger than 3.
 *
 * Example 2:
 *
 * Input: nums = [7,4,5,1,8,12,4,7], k = 5
 * Output: 4
 * Explanation:
 * The longest subsequence that meets the requirements is [4,5,8,12].
 * The subsequence has a length of 4, so we return 4.
 *
 * Example 3:
 *
 * Input: nums = [1,5], k = 1
 * Output: 1
 * Explanation:
 * The longest subsequence that meets the requirements is [1].
 * The subsequence has a length of 1, so we return 1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i], k <= 10^5
 *
 */
public class LongestIncreasingSubsequenceII {

    // V0
    // IDEA: DP INDEXED BY VALUE, WITH A SEGMENT TREE FOR RANGE MAXIMUM
    //       best[v] = length of the longest valid subsequence ENDING with value v.
    //       scanning nums left to right keeps subsequence order, and
    //         best[v] = 1 + max(best[u] for u in [v - k, v - 1])
    //       since the predecessor must be strictly smaller but within k.
    //       that inner max is a range-max over the value axis -> segment tree,
    //       O(log V) per step instead of the O(n^2) classic LIS DP.
    /**
     * time = O(N log V)
     * space = O(V)
     */
    public int lengthOfLIS(int[] nums, int k) {
        int maxValue = 0;
        for (int value : nums) {
            maxValue = Math.max(maxValue, value);
        }

        // tree[node] = max of best[] over that node's value range; values span [0, maxValue]
        int[] tree = new int[4 * (maxValue + 1)];

        int answer = 0;
        for (int value : nums) {
            int low = Math.max(0, value - k);
            int high = value - 1;
            int bestBefore = 0;
            if (low <= high) {
                bestBefore = query(tree, 1, 0, maxValue, low, high);
            }
            int current = bestBefore + 1;
            answer = Math.max(answer, current);
            update(tree, 1, 0, maxValue, value, current);
        }
        return answer;
    }

    /** Raises best[position] to at least value, refreshing the maxima on the way up. */
    private void update(int[] tree, int node, int nodeLow, int nodeHigh, int position, int value) {
        if (nodeLow == nodeHigh) {
            tree[node] = Math.max(tree[node], value);
            return;
        }
        int mid = nodeLow + (nodeHigh - nodeLow) / 2;
        if (position <= mid) {
            update(tree, 2 * node, nodeLow, mid, position, value);
        } else {
            update(tree, 2 * node + 1, mid + 1, nodeHigh, position, value);
        }
        tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
    }

    /** Maximum of best[] over the inclusive value range [low, high]. */
    private int query(int[] tree, int node, int nodeLow, int nodeHigh, int low, int high) {
        // no overlap with the asked range
        if (high < nodeLow || nodeHigh < low) {
            return 0;
        }
        // node range fully inside the asked range
        if (low <= nodeLow && nodeHigh <= high) {
            return tree[node];
        }
        int mid = nodeLow + (nodeHigh - nodeLow) / 2;
        int leftMax = query(tree, 2 * node, nodeLow, mid, low, high);
        int rightMax = query(tree, 2 * node + 1, mid + 1, nodeHigh, low, high);
        return Math.max(leftMax, rightMax);
    }
}
