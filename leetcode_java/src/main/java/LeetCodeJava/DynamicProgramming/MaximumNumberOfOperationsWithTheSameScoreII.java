package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-number-of-operations-with-the-same-score-ii/description/

import java.util.*;

/**
 * 3040. Maximum Number of Operations With the Same Score II
 * Medium
 *
 * Given an array of integers called nums, you can perform any of the following operation while nums contains at least 2 elements:
 *
 * Choose the first two elements of nums and delete them.
 * Choose the last two elements of nums and delete them.
 * Choose the first and the last elements of nums and delete them.
 *
 * The score of the operation is the sum of the deleted elements.
 *
 * Your task is to find the maximum number of operations that can be performed, such that all operations have the same score.
 *
 * Return the maximum number of operations possible that satisfy the condition mentioned above.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,1,2,3,4]
 * Output: 3
 * Explanation: We perform the following operations:
 * - Delete the first two elements, with score 3 + 2 = 5, nums = [1,2,3,4].
 * - Delete the first and the last elements, with score 1 + 4 = 5, nums = [2,3].
 * - Delete the first and the last elements, with score 2 + 3 = 5, nums = [].
 * We are unable to perform any more operations as nums is empty.
 *
 * Example 2:
 *
 * Input: nums = [3,2,6,1,4]
 * Output: 2
 * Explanation: We perform the following operations:
 * - Delete the first two elements, with score 3 + 2 = 5, nums = [6,1,4].
 * - Delete the last two elements, with score 1 + 4 = 5, nums = [6].
 * It can be proven that we can perform at most 2 operations.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 2000
 * 1 <= nums[i] <= 1000
 *
 */
public class MaximumNumberOfOperationsWithTheSameScoreII {

    // V0
    // IDEA: ONLY 3 POSSIBLE SCORES -> FIX ONE, THEN MEMOIZED INTERVAL DP ON (left, right)
    //   The first operation decides the score: nums[0]+nums[1], nums[n-2]+nums[n-1] or
    //   nums[0]+nums[n-1]. With the score fixed the array is always a window [left, right]:
    //   go(l, r) = max(1 + go(l+2, r)   if nums[l] + nums[l+1] == target,
    //                  1 + go(l, r-2)   if nums[r-1] + nums[r] == target,
    //                  1 + go(l+1, r-1) if nums[l] + nums[r]   == target,
    //                  0)
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int maxOperations(int[] nums) {
        int n = nums.length;
        int[] targets = {
            nums[0] + nums[1],
            nums[n - 2] + nums[n - 1],
            nums[0] + nums[n - 1]
        };
        int best = 0;
        for (int target : targets) {
            int[][] memo = new int[n][n];
            for (int[] row : memo) {
                Arrays.fill(row, -1);
            }
            best = Math.max(best, go(nums, 0, n - 1, target, memo));
        }
        return best;
    }

    /** Max operations with score `target` on the window nums[left..right]. */
    private int go(int[] nums, int left, int right, int target, int[][] memo) {
        // fewer than two elements left
        if (left >= right) {
            return 0;
        }
        if (memo[left][right] != -1) {
            return memo[left][right];
        }
        int best = 0;
        if (nums[left] + nums[left + 1] == target) {
            best = Math.max(best, 1 + go(nums, left + 2, right, target, memo));
        }
        if (nums[right - 1] + nums[right] == target) {
            best = Math.max(best, 1 + go(nums, left, right - 2, target, memo));
        }
        if (nums[left] + nums[right] == target) {
            best = Math.max(best, 1 + go(nums, left + 1, right - 1, target, memo));
        }
        memo[left][right] = best;
        return best;
    }
}
