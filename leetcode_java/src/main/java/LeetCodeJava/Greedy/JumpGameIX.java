package LeetCodeJava.Greedy;

// https://leetcode.com/problems/jump-game-ix/description/

/**
 * 3660. Jump Game IX
 * Medium
 *
 * You are given an integer array nums.
 *
 * From any index i, you can jump to another index j under the following rules:
 *
 * Jump to index j where j > i is allowed only if nums[j] < nums[i].
 *
 * Jump to index j where j < i is allowed only if nums[j] > nums[i].
 *
 * For each index i, find the maximum value in nums that can be reached by
 * following any sequence of valid jumps starting at i.
 *
 * Return an array ans where ans[i] is the maximum value reachable starting
 * from index i.
 *
 * Example 1:
 *
 * Input: nums = [2,1,3]
 * Output: [2,2,3]
 * Explanation:
 * For i = 0: No jump increases the value.
 * For i = 1: Jump to j = 0 as nums[j] = 2 is greater than nums[i].
 * For i = 2: Since nums[2] = 3 is the maximum value in nums, no jump increases
 * the value.
 * Thus, ans = [2, 2, 3].
 *
 * Example 2:
 *
 * Input: nums = [2,3,1]
 * Output: [3,3,3]
 * Explanation:
 * For i = 0: Jump forward to j = 2 as nums[j] = 1 is less than nums[i] = 2,
 * then from i = 2 jump to j = 1 as nums[j] = 3 is greater than nums[2].
 * For i = 1: Since nums[1] = 3 is the maximum value in nums, no jump increases
 * the value.
 * For i = 2: Jump to j = 1 as nums[j] = 3 is greater than nums[2] = 1.
 * Thus, ans = [3, 3, 3].
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 *
 */
public class JumpGameIX {

    // V0
    // IDEA: COMPONENTS OF THE INVERSION GRAPH = "MAX CHUNKS TO MAKE SORTED" BLOCKS
    //       for i < j both jump rules reduce to nums[i] > nums[j], so the graph is the
    //       undirected inversion graph. a cut after i separates components exactly when
    //       max(nums[0..i]) <= min(nums[i+1..]); inside a block everything connects.
    //       every index answers its block's maximum.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] maxValue(int[] nums) {
        int n = nums.length;

        // suffixMin[i] = min(nums[i..n-1]); suffixMin[n] = +infinity
        int[] suffixMin = new int[n + 1];
        suffixMin[n] = Integer.MAX_VALUE;
        for (int i = n - 1; i >= 0; i--) {
            suffixMin[i] = Math.min(suffixMin[i + 1], nums[i]);
        }

        int[] answer = new int[n];
        int blockStart = 0;
        int blockMax = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            blockMax = Math.max(blockMax, nums[i]);
            // nothing to the right is smaller than this block's max -> the block closes
            if (blockMax <= suffixMin[i + 1]) {
                for (int j = blockStart; j <= i; j++) {
                    answer[j] = blockMax;
                }
                blockStart = i + 1;
                blockMax = Integer.MIN_VALUE;
            }
        }
        return answer;
    }
}
